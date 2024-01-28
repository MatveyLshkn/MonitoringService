package by.matvey.lshkn.service;

import by.matvey.lshkn.entity.Measurement;
import by.matvey.lshkn.entity.Meter;
import by.matvey.lshkn.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MeasurementServiceTest {
    private final MeasurementService service = MeasurementService.getInstance();

    @Test
    void getRelevantMeasurementIfNoMeasurements() {
        Meter meter = new Meter();

        Optional<Measurement> measurement = service.getRelevantMeasurement(meter);

        assertThat(measurement).isEmpty();
    }

    @Test
    void getRelevantMeasurementIfMeasurementsPresent() {
        Meter meter = new Meter();
        Measurement measurement1 = Measurement.builder()
                .value(123d)
                .date(LocalDateTime.now())
                .build();
        Measurement measurement2 = Measurement.builder()
                .value(321d)
                .date(LocalDateTime.now().minusYears(12L))
                .build();
        meter.addMeasurement(measurement1);
        meter.addMeasurement(measurement2);

        Optional<Measurement> relevantMeasurement = service.getRelevantMeasurement(meter);

        assertThat(relevantMeasurement).isNotEmpty();
        assertThat(relevantMeasurement.get()).isEqualTo(measurement1);
    }

    @Test
    void getRelevantMeasurementsIfNoMeasurements() {
        User user = new User();

        List<Measurement> relevantMeasurements = service.getRelevantMeasurements(user);

        assertThat(relevantMeasurements).isEmpty();
    }

    @Test
    void getRelevantMeasurementsIfMeasurementsPresent() {
        Meter meter = new Meter();
        Meter meter2 = new Meter();
        Measurement measurement1 = Measurement.builder()
                .value(123d)
                .date(LocalDateTime.now())
                .build();
        Measurement measurement2 = Measurement.builder()
                .value(321d)
                .date(LocalDateTime.now().minusYears(12L))
                .build();
        meter.addMeasurement(measurement1);
        meter.addMeasurement(measurement2);
        User user = new User();
        user.addMeter(meter);
        user.addMeter(meter2);

        List<Measurement> relevantMeasurements = service.getRelevantMeasurements(user);

        assertThat(relevantMeasurements).hasSize(1);
        assertThat(relevantMeasurements).contains(measurement1);
    }

    @Test
    void getMeasurementsByMonth() {
        Meter meter = new Meter();
        Meter meter2 = new Meter();
        Measurement measurement1 = Measurement.builder()
                .value(123d)
                .date(LocalDateTime.now())
                .build();
        Measurement measurement2 = Measurement.builder()
                .value(321d)
                .date(LocalDateTime.now().minusYears(12L))
                .build();
        Measurement measurement3 = Measurement.builder()
                .value(321d)
                .date(LocalDateTime.now())
                .build();
        meter.addMeasurement(measurement1);
        meter.addMeasurement(measurement2);
        meter2.addMeasurement(measurement3);
        User user = new User();
        user.addMeter(meter);
        user.addMeter(meter2);

        List<Measurement> measurements = service.getMeasurementsByMonth(user, LocalDate.now());

        assertThat(measurements).hasSize(2);
    }

    @Test
    void getAllMeasurements() {
        Meter meter = new Meter();
        Measurement measurement1 = Measurement.builder()
                .value(123d)
                .date(LocalDateTime.now())
                .build();
        Measurement measurement2 = Measurement.builder()
                .value(321d)
                .date(LocalDateTime.now().minusYears(12L))
                .build();
        meter.addMeasurement(measurement1);
        meter.addMeasurement(measurement2);
        User user = new User();
        user.addMeter(meter);

        List<Measurement> allMeasurements = service.getAllMeasurements(user);

        assertThat(allMeasurements).hasSize(2);
    }

    @Test
    void addMeasurementIfValid() {
        Meter meter = new Meter();
        Measurement measurement = Measurement.builder()
                .value(123d)
                .date(LocalDateTime.now())
                .build();

        boolean result = service.addMeasurement(meter, measurement);

        assertThat(result).isTrue();
        assertThat(measurement.getMeter()).isEqualTo(meter);
        assertThat(meter.getMeasurements()).hasSize(1);
    }

    @Test
    void addMeasurementIfInvalidDate() {
        Meter meter = new Meter();
        Measurement measurement = Measurement.builder()
                .value(123d)
                .date(LocalDateTime.now())
                .build();
        Measurement measurement2 = Measurement.builder()
                .value(124d)
                .date(LocalDateTime.now())
                .build();
        meter.addMeasurement(measurement);

        boolean result = service.addMeasurement(meter, measurement2);

        assertThat(result).isFalse();
    }

    @Test
    void addMeasurementIfInvalidValue() {
        Meter meter = new Meter();
        Measurement measurement = Measurement.builder()
                .value(123d)
                .date(LocalDateTime.now())
                .build();
        Measurement measurement2 = Measurement.builder()
                .value(12d)
                .date(LocalDateTime.now().minusYears(12L))
                .build();
        meter.addMeasurement(measurement);

        boolean result = service.addMeasurement(meter, measurement2);

        assertThat(result).isFalse();
    }
}