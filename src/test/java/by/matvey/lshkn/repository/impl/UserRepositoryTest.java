package by.matvey.lshkn.repository.impl;

import by.matvey.lshkn.DatabaseCreator;
import by.matvey.lshkn.entity.Role;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.util.ConnectionManager;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {
    private final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    private final UserRepository userRepository = UserRepository.getInstance();
    private User user = User.builder()
            .username("Stub")
            .password("stub")
            .role(Role.USER)
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
        Optional<User> maybeUser = userRepository.findById(2L);

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getUsername()).isEqualTo("User");
    }

    @Test
    @Order(2)
    void findByUsername() {
        Optional<User> maybeUser = userRepository.findByUsername("User");

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getUsername()).isEqualTo("User");
    }

    @Test
    @Order(3)
    void save() {
        user = userRepository.save(user);

        assertThat(user.getId()).isNotNull();
        assertThat(userRepository.findById(user.getId())).isPresent();
        assertThat(userRepository.findByUsername(user.getUsername())).isPresent();
    }

    @Test
    @Order(4)
    void getAllUsernames() {
        List<String> usernames = userRepository.getAllUsernames();

        assertThat(usernames).hasSize(3);
    }

    @Test
    @Order(5)
    void update() {
        Optional<User> maybeUser1 = userRepository.findByUsername("Stub");
        assertThat(maybeUser1).isPresent();
        User user1 = maybeUser1.get();
        user1.setPassword("newPassword");

        boolean result = userRepository.update(user1);

        Optional<User> maybeUser2 = userRepository.findByUsername("Stub");

        assertThat(result).isTrue();
        assertThat(maybeUser2).isPresent();
        assertThat(maybeUser2.get().getPassword()).isEqualTo(user1.getPassword());
        assertThat(maybeUser2.get().getPassword()).isNotEqualTo(user.getPassword());
    }

    @Test
    @Order(6)
    void delete() {
        int prevSize = userRepository.getAllUsernames().size();

        boolean result = userRepository.delete(user);

        assertThat(result).isTrue();
        assertThat(userRepository.getAllUsernames().size()).isLessThan(prevSize);
    }
}