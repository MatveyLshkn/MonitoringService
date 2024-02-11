package by.matvey.lshkn.service;

import by.matvey.lshkn.annotation.Auditable;
import by.matvey.lshkn.dto.MeasurementDto;
import by.matvey.lshkn.dto.UserDto;
import by.matvey.lshkn.entity.*;
import by.matvey.lshkn.mapper.MeasurementMapper;
import by.matvey.lshkn.repository.impl.MeasurementRepository;
import by.matvey.lshkn.repository.impl.UserRepository;
import by.matvey.lshkn.util.Validator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mapstruct.factory.Mappers;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class intended to work with Measurements
 */
public class MeasurementService {
    private MeasurementRepository measurementRepository = MeasurementRepository.getInstance();
    private UserRepository userRepository = UserRepository.getInstance();
    private static final MeasurementService INSTANCE = new MeasurementService();

    private MeasurementService() {
    }

    public static MeasurementService getInstance() {
        return INSTANCE;
    }

    public List<MeasurementDto> get(HttpServletRequest req) throws IOException {
        HttpSession session = req.getSession();
        UserDto userDto = (UserDto) session.getAttribute("user");
        if(!Validator.validateUserDto(userDto))return new ArrayList<>();

        Optional<User> maybeUser = userRepository.findByUsername(userDto.getUsername());
        if (maybeUser.isEmpty()) return new ArrayList<>();
        User user = maybeUser.get();

        BufferedReader reader = req.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        while (reader.ready()) {
            stringBuilder.append((char) reader.read());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(stringBuilder.toString());
        JsonNode typeNode = jsonNode.get("type");
        String type = typeNode == null ? "" : typeNode.asText();
        if(user.getRole().equals(Role.ADMIN))type = "username";

        MeasurementMapper measurementMapper = Mappers.getMapper(MeasurementMapper.class);
        List<Measurement> measurements = new ArrayList<>();
        switch (type) {
            case "relevant": {
                measurements = getRelevantMeasurementsForUser(user);
                break;
            }
            case "date": {
                String date = jsonNode.get("date").asText();
                LocalDate localDate = LocalDate.parse(date);
                measurements = getMeasurementsByDateAndUser(user, localDate);
                break;
            }
            case "username":{
                if(user.getRole().equals(Role.ADMIN)) {
                    JsonNode usernameNode = jsonNode.get("username");
                    if (usernameNode != null) {
                        Optional<User> user1 = userRepository.findByUsername(usernameNode.asText());
                        if (user1.isPresent()) measurements = getAllMeasurementsByUser(user1.get());
                    }
                }
                break;
            }
            default: {
                measurements = getAllMeasurementsByUser(user);
            }
        }
        return measurements.stream()
                .map(measurementMapper::measurementToMeasurementDto)
                .filter(Validator::validateMeasurementDto)
                .collect(Collectors.toList());
    }
    @Auditable
    public MeasurementDto save(HttpServletRequest req) throws IOException {
        HttpSession session = req.getSession();
        UserDto userDto = (UserDto) session.getAttribute("user");
        if(!Validator.validateUserDto(userDto)){
            return new MeasurementDto();
        }

        Optional<User> maybeUser = userRepository.findByUsername(userDto.getUsername());
        if (maybeUser.isEmpty()) return new MeasurementDto();
        User user = maybeUser.get();

        BufferedReader reader = req.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        while (reader.ready()) {
            stringBuilder.append((char) reader.read());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(stringBuilder.toString());

        Measurement measurement = new Measurement();
        measurement.setDate(LocalDateTime.now());

        JsonNode valueNode = jsonNode.get("value");
        if (valueNode != null) measurement.setValue(Double.parseDouble(valueNode.asText()));

        JsonNode meterNode = jsonNode.get("meter");
        Meter meter = null;
        if (meterNode != null) {
            String meterName = meterNode.asText();
            Optional<Meter> maybeMeter = user.getMeters().stream()
                    .filter(meter1 -> meter1.getType().getName().equals(meterName))
                    .findFirst();

            if (maybeMeter.isEmpty()) {
                MeterService meterService = MeterService.getInstance();
                MeterTypeService meterTypeService = MeterTypeService.getInstance();
                Optional<MeterType> meterType = meterTypeService.getMeterTypeByName(meterName);
                if (meterType.isPresent()) {
                    meter = Meter.builder()
                            .type(meterType.get())
                            .build();
                    meterService.addMeterToUser(meter, user);
                }
            }
        }
        if (meter != null && measurement.getValue() != null) {
            measurement = addMeasurementToMeter(meter, measurement);
        }
        MeasurementMapper measurementMapper = Mappers.getMapper(MeasurementMapper.class);
        return measurementMapper.measurementToMeasurementDto(measurement);
    }

    /**
     * Retrieves relevant measurements from every meter of user
     *
     * @param user user for whom will be printed all relevant measurements
     * @return returns all relevant measurements for user
     */
    public List<Measurement> getRelevantMeasurementsForUser(User user) {
        List<Measurement> measurements = new ArrayList<>();
        user.getMeters().forEach(meter -> {
            Optional<Measurement> optionalMeasurement = getRelevantMeasurementFromMeter(meter);
            optionalMeasurement.ifPresent(measurements::add);
        });
        return measurements;
    }

    /**
     * Retrieves relevant measurement from meter
     *
     * @param meter from which relevant measurement will be taken
     * @return Optional of relevant measurement
     */
    public Optional<Measurement> getRelevantMeasurementFromMeter(Meter meter) {
        return meter.getMeasurements().stream()
                .reduce((first, second) -> first);
    }


    /**
     * Returns all measurements for user by date (year and month)
     *
     * @param user user for whom all measurements in specific month will be printed
     * @param date month and year in which measurements will be printed
     */
    public List<Measurement> getMeasurementsByDateAndUser(User user, LocalDate date) {
        List<Measurement> measurements = new ArrayList<>();
        user.getMeters().forEach(meter -> {
            Optional<Measurement> optional = meter.getMeasurements().stream()
                    .filter(measurement -> measurement.getDate().getYear() == date.getYear())
                    .filter(measurement -> measurement.getDate().getMonth() == date.getMonth())
                    .findFirst();
            optional.ifPresent(measurements::add);
        });
        return measurements;
    }

    /**
     * Returns all measurements (relevant and irrelevant) for specific user
     *
     * @param user user for whom all measurements will be printed
     */
    public List<Measurement> getAllMeasurementsByUser(User user) {
        List<Measurement> measurements = new ArrayList<>();
        user.getMeters().forEach(meter -> measurements.addAll(meter.getMeasurements()));
        return measurements;
    }

    /**
     * Adds measurement for specific meter. Validates that previous value is less or equal to new measurement value and validates that previous and new measurements are created not in the same month
     *
     * @param meter       meter in which measurement will be added
     * @param measurement measurement that will be added after successful validation
     */
    public Measurement addMeasurementToMeter(Meter meter, Measurement measurement) {
        Optional<Measurement> maybeRelevantMeasurement = getRelevantMeasurementFromMeter(meter);
        if (maybeRelevantMeasurement.isPresent()) {
            Measurement releavantMeasurement = maybeRelevantMeasurement.get();
            if (releavantMeasurement.getValue() <= measurement.getValue() &&
                !(releavantMeasurement.getDate().getMonth().equals(measurement.getDate().getMonth())
                  && releavantMeasurement.getDate().getYear() == measurement.getDate().getYear())) {
                meter.addMeasurement(measurement);
                measurement = measurementRepository.save(measurement);
            }
        } else {
            meter.addMeasurement(measurement);
            measurement = measurementRepository.save(measurement);
        }
        return measurement;
    }
}
