package by.matvey.lshkn.service;

import by.matvey.lshkn.entity.Measurement;
import by.matvey.lshkn.entity.Meter;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.util.LoggerUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class intended to work with Measurements
 */
public class MeasurementService {
    private static final MeasurementService INSTANCE = new MeasurementService();

    private MeasurementService() {
    }

    public static MeasurementService getInstance() {
        return INSTANCE;
    }

    /**
     * Prints all relevant measurements of user
     * @param user user for whom will be printed all relevant measurements
     */
    public List<Measurement> getRelevantMeasurements(User user) {
        List<Measurement> measurements = new ArrayList<>();
        user.getMeters().forEach(meter -> {
            Optional<Measurement> optionalMeasurement = getRelevantMeasurement(meter);
            optionalMeasurement.ifPresent(measurements::add);
        });
        LoggerUtil.log(LocalDateTime.now() + " Returned relevant measurements for user: " + user+" measurements: "+measurements);
        return measurements;
    }

    /**
     * Gets relevant measurement from meter
     * @param meter from which relevant measurement will be taken
     * @return Optional of relevant measurement
     */
    public Optional<Measurement> getRelevantMeasurement(Meter meter) {
        Optional<Measurement> measurement = meter.getMeasurements().stream()
                .reduce((first, second) -> first);
        LoggerUtil.log(LocalDateTime.now() + " Got relevant measurement for meter: " + meter+" measurement: "+measurement);
        return measurement;
    }


    /**
     * Prints all measurements for user by month (year and month)
     * @param user user for whom all measurements in specific month will be printed
     * @param date month and year in which measurements will be printed
     */
    public List<Measurement> getMeasurementsByMonth(User user, LocalDate date) {
        List<Measurement> measurements = new ArrayList<>();
        user.getMeters().forEach(meter -> {
            Optional<Measurement> optional = meter.getMeasurements().stream()
                    .filter(measurement -> measurement.getDate().getYear() == date.getYear())
                    .filter(measurement -> measurement.getDate().getMonth() == date.getMonth())
                    .findFirst();
            optional.ifPresent(measurements::add);
        });
        LoggerUtil.log(LocalDateTime.now()+" Returned measurements by date: " +date+" for user: "+user+" measurements: "+measurements);
        return measurements;
    }

    /**
     * Prints all measurements (relevant and irrelevant) for specific user
     * @param user for whom all measurements will be printed
     */
    public List<Measurement> getAllMeasurements(User user) {
        List<Measurement> measurements = new ArrayList<>();
        user.getMeters().forEach(meter -> {
            measurements.addAll(meter.getMeasurements());
        });
        LoggerUtil.log(LocalDateTime.now()+" Returned all measurements for user: "+user+" measurements: "+measurements);
        return measurements;
    }

    /**
     * Adds measurement for specific meter. Validates that previous value is less or equal to new measurement value and validates that previous and new measurements are created not in the same month
     * @param meter meter in which measurement will be added
     * @param measurement measurement that will be added after successful validation
     */
    public boolean addMeasurement(Meter meter, Measurement measurement) {
        int prevSize = meter.getMeasurements().size();
        Optional<Measurement> relevantMeasurement = getRelevantMeasurement(meter);
        relevantMeasurement.ifPresentOrElse(measurement1 -> {
            if (!measurement1.getDate().getMonth().equals(measurement.getDate().getMonth())
                && !(measurement1.getDate().getYear() == measurement.getDate().getYear())
                && measurement1.getValue() <= measurement.getValue()) {
                meter.addMeasurement(measurement);
            }
        }, () -> {
            meter.addMeasurement(measurement);
        });
        if(prevSize==meter.getMeasurements().size()){
            LoggerUtil.log(LocalDateTime.now()+" Measurement: "+measurement+" wasn't added to meter: "+meter);
            return false;
        }
        LoggerUtil.log(LocalDateTime.now()+" Measurement: "+measurement+" was added to meter: "+meter);
        return true;

    }
}
