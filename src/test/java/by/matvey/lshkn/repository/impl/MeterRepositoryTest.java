package by.matvey.lshkn.repository.impl;

import by.matvey.lshkn.DatabaseCreator;
import by.matvey.lshkn.entity.Meter;
import by.matvey.lshkn.entity.MeterType;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.util.ConnectionManager;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeterRepositoryTest {
    private final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    private final MeterRepository meterRepository = MeterRepository.getInstance();
    private Meter meter = Meter.builder()
            .type(new MeterType(1L, "heat"))
            .owner(User.builder().id(2L).build())
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
    @DisplayName("Find meter by id")
    void findById() {
        Optional<Meter> maybeMeter = meterRepository.findById(1L);

        assertThat(maybeMeter).isPresent();
    }

    @Test
    @Order(2)
    @DisplayName("Find all meters by user id")
    void findAllByUserId() {
        List<Meter> meters = meterRepository.findAllByUserId(2L);

        assertThat(meters).hasSize(2);
    }

    @Test
    @Order(3)
    void save() {
        meter = meterRepository.save(meter);

        assertThat(meter.getId()).isNotNull();
        assertThat(meterRepository.findById(meter.getId())).isPresent();
    }

    @Test
    @Order(4)
    void update() {
        Optional<Meter> maybe1 = meterRepository.findById(meter.getId());
        assertThat(maybe1).isPresent();
        Meter type1 = maybe1.get();
        type1.setType(new MeterType(2L, "hot_water"));

        maybe1.get().setOwner(User.builder().id(2L).build());
        boolean result = meterRepository.update(type1);

        Optional<Meter> maybeUser2 = meterRepository.findById(meter.getId());

        assertThat(result).isTrue();
        assertThat(maybeUser2).isPresent();
        assertThat(maybeUser2.get().getType().getName()).isEqualTo("hot_water");
    }

    @Test
    @Order(5)
    void delete() {
        int prevSize = meterRepository.findAllByUserId(2L).size();

        boolean result = meterRepository.delete(meter);

        assertThat(result).isTrue();
        assertThat(meterRepository.findAllByUserId(2L).size()).isLessThan(prevSize);
    }
}