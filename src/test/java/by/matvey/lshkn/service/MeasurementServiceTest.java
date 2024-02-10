package by.matvey.lshkn.service;

import by.matvey.lshkn.entity.Measurement;
import by.matvey.lshkn.entity.Meter;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.repository.impl.MeasurementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceTest {
    @Mock
    private MeasurementRepository measurementRepository;
    @InjectMocks
    private final MeasurementService service = MeasurementService.getInstance();

    @Test
    @DisplayName("Get relevant measurement from meter is empty if there are no measurements")
    void getRelevantMeasurementFromMeterIfNoMeasurements() {
        Meter meter = new Meter();

        Optional<Measurement> measurement = service.getRelevantMeasurementFromMeter(meter);

        assertThat(measurement).isEmpty();
    }

    @Test
    @DisplayName("Get relevant measurement from meter is not empty if measurements present")
    void getRelevantMeasurementFromMeterIfMeasurementsPresent() {
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

        Optional<Measurement> relevantMeasurement = service.getRelevantMeasurementFromMeter(meter);

        assertThat(relevantMeasurement).isNotEmpty();
        assertThat(relevantMeasurement.get()).isEqualTo(measurement1);
    }

    @Test
    @DisplayName("Get relevant measurements for user is empty if there are no measurements")
    void getRelevantMeasurementsForUserIfNoMeasurements() {
        User user = new User();

        List<Measurement> relevantMeasurements = service.getRelevantMeasurementsForUser(user);

        assertThat(relevantMeasurements).isEmpty();
    }

    @Test
    @DisplayName("Get relevant measurements for user is not empty if measurements present")
    void getRelevantMeasurementsForUserIfMeasurementsPresent() {
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

        List<Measurement> relevantMeasurements = service.getRelevantMeasurementsForUser(user);

        assertThat(relevantMeasurements).hasSize(1);
        assertThat(relevantMeasurements).contains(measurement1);
    }

    @Test
    @DisplayName("Get Measurements by date(year and month) and user")
    void getMeasurementsByDateAndUser() {
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

        List<Measurement> measurements = service.getMeasurementsByDateAndUser(user, LocalDate.now());

        assertThat(measurements).hasSize(2);
    }

    @Test
    @DisplayName("Get all measurements by user")
    void getAllMeasurementsByUser() {
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

        List<Measurement> allMeasurements = service.getAllMeasurementsByUser(user);

        assertThat(allMeasurements).hasSize(2);
    }

    @Test
    @DisplayName("Add measurement if it is valid to meter ")
    void addMeasurementToMeterIfValid() {
        Meter meter = new Meter();
        Measurement measurement = Measurement.builder()
                .value(123d)
                .date(LocalDateTime.now())
                .build();
        Mockito.doReturn(measurement).when(measurementRepository).save(measurement);

        boolean result = service.addMeasurementToMeter(meter, measurement);

        assertThat(result).isTrue();
        assertThat(measurement.getMeter()).isEqualTo(meter);
        assertThat(meter.getMeasurements()).hasSize(1);
    }

    @Test
    @DisplayName("Do not add measurement to meter if measurement has invalid date")
    void addMeasurementToMeterIfInvalidDate() {
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

        boolean result = service.addMeasurementToMeter(meter, measurement2);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Do not add measurement to meter if measurement has invalid value")
    void addMeasurementToMeterIfInvalidValue() {
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

        boolean result = service.addMeasurementToMeter(meter, measurement2);

        assertThat(result).isFalse();
    }
}