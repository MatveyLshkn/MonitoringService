package by.matvey.lshkn.repository.impl;

import by.matvey.lshkn.DatabaseCreator;
import by.matvey.lshkn.entity.MeterType;
import by.matvey.lshkn.util.ConnectionManager;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeterTypeRepositoryTest {
    private final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    private final MeterTypeRepository meterTypeRepository = MeterTypeRepository.getInstance();
    private MeterType type = new MeterType();

    @BeforeAll
    void beforeAll() {
        postgres.start();
        ConnectionManager.setBaseUrlValue(postgres.getJdbcUrl());
        ConnectionManager.setUrlValue(postgres.getJdbcUrl() + "&currentSchema=monitoring_service");
        ConnectionManager.setUsernameValue(postgres.getUsername());
        ConnectionManager.setPasswordValue(postgres.getPassword());

        DatabaseCreator.createAll();

        type.setName("Stub");
    }

    @AfterAll
    void afterAll() {
        postgres.stop();
    }

    @Test
    @Order(1)
    void findById() {
        Optional<MeterType> maybeType = meterTypeRepository.findById(1L);

        assertThat(maybeType).isPresent();
        assertThat(maybeType.get().getName()).isEqualTo("heat");
    }

    @Test
    @Order(2)
    void findByName() {
        Optional<MeterType> maybeType = meterTypeRepository.findByName("heat");

        assertThat(maybeType).isPresent();
        assertThat(maybeType.get().getName()).isEqualTo("heat");
    }

    @Test
    @Order(3)
    void findAll() {
        List<MeterType> types = meterTypeRepository.findAll();

        assertThat(types).hasSize(3);
    }

    @Test
    @Order(4)
    void save() {
        type = meterTypeRepository.save(type);

        assertThat(type.getId()).isNotNull();
        assertThat(meterTypeRepository.findById(type.getId())).isPresent();
        assertThat(meterTypeRepository.findByName(type.getName())).isPresent();
    }

    @Test
    @Order(5)
    void update() {
        Optional<MeterType> maybeType1 = meterTypeRepository.findByName("Stub");
        assertThat(maybeType1).isPresent();
        MeterType type1 = maybeType1.get();
        type1.setName("newStub");

        boolean result = meterTypeRepository.update(type1);

        Optional<MeterType> maybeUser2 = meterTypeRepository.findByName("newStub");

        assertThat(result).isTrue();
        assertThat(maybeUser2).isPresent();
    }

    @Test
    @Order(6)
    void delete() {
        int prevSize = meterTypeRepository.findAll().size();
        type.setName("newStub");
        boolean result = meterTypeRepository.delete(type);

        assertThat(result).isTrue();
        assertThat(meterTypeRepository.findAll().size()).isLessThan(prevSize);
    }
}