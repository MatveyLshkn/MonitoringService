package by.matvey.lshkn.service;

import by.matvey.lshkn.entity.Measurement;
import by.matvey.lshkn.entity.Meter;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.repository.impl.MeasurementRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class intended to work with Measurements
 */
public class MeasurementService {
    private MeasurementRepository measurementRepository = MeasurementRepository.getInstance();
    private static final MeasurementService INSTANCE = new MeasurementService();

    private MeasurementService() {
    }

    public static MeasurementService getInstance() {
        return INSTANCE;
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
    public boolean addMeasurementToMeter(Meter meter, Measurement measurement) {
        int prevSize = meter.getMeasurements().size();
        Optional<Measurement> relevantMeasurement = getRelevantMeasurementFromMeter(meter);
        relevantMeasurement.ifPresentOrElse(measurement1 -> {
            if (!measurement1.getDate().getMonth().equals(measurement.getDate().getMonth())
                && !(measurement1.getDate().getYear() == measurement.getDate().getYear())
                && measurement1.getValue() <= measurement.getValue()) {
                meter.addMeasurement(measurement);
                measurementRepository.save(measurement);
            }
        }, () -> {
            meter.addMeasurement(measurement);
            measurementRepository.save(measurement);
        });
        return prevSize != meter.getMeasurements().size();
    }
}
