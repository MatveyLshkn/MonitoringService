package by.matvey.lshkn.repository.impl;

import by.matvey.lshkn.DatabaseCreator;
import by.matvey.lshkn.entity.Measurement;
import by.matvey.lshkn.entity.Meter;
import by.matvey.lshkn.entity.MeterType;
import by.matvey.lshkn.util.ConnectionManager;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeasurementRepositoryTest {
    private final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    private final MeasurementRepository measurementRepository = MeasurementRepository.getInstance();
    private Measurement measurement = Measurement.builder()
            .meter(Meter.builder().id(1L).type(new MeterType(1L, "heat")).build())
            .value(123.23)
            .date(LocalDateTime.now())
            .build();

    @BeforeAll
    void beforeAll() {
        postgres.start();
        ConnectionManager.setBaseUrlValue(postgres.getJdbcUrl());
        ConnectionManager.setUrlValue(postgres.getJdbcUrl() + "&currentSchema=monitoring_service");
        ConnectionManager.setUsernameValue(postgres.getUsername());
        ConnectionManager.setPasswordValue(postgres.getPassword());

        DatabaseCreator.createAll();
    }

    @AfterAll
    void afterAll() {
        postgres.stop();
    }

    @Test
    @Order(1)
    void findById() {
        Optional<Measurement> maybeMeasurement = measurementRepository.findById(1L);

        assertThat(maybeMeasurement).isPresent();
    }

    @Test
    @Order(2)
    void findAllByMeterId() {
        List<Measurement> measurements = measurementRepository.findAllByMeterId(1L);

        assertThat(measurements.size()).isEqualTo(1);
    }

    @Test
    @Order(3)
    void save() {
        measurement.setId(measurementRepository.save(measurement).getId());

        assertThat(measurement.getId()).isNotNull();
        assertThat(measurementRepository.findById(measurement.getId())).isPresent();
    }

    @Test
    @Order(4)
    void update() {
        Optional<Measurement> maybe1 = measurementRepository.findById(measurement.getId());
        assertThat(maybe1).isPresent();
        Measurement measurement1 = maybe1.get();
        LocalDate date = LocalDate.now().minusYears(5);
        measurement1.setDate(date.atStartOfDay());
        measurement1.setMeter(measurement.getMeter());
        measurement.setDate(date.atStartOfDay());

        boolean result = measurementRepository.update(measurement1);
        Optional<Measurement> maybeMeasurement2 = measurementRepository.findById(measurement.getId());

        assertThat(result).isTrue();
        assertThat(maybeMeasurement2).isPresent();
        assertThat(maybeMeasurement2.get().getDate()).isEqualTo(date.atStartOfDay());
    }

    @Test
    @Order(5)
    void delete() {
        int prevSize = measurementRepository.findAllByMeterId(1L).size();

        boolean result = measurementRepository.delete(measurement);

        assertThat(result).isTrue();
        assertThat(measurementRepository.findAllByMeterId(1L).size()).isLessThan(prevSize);
    }
}