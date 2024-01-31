package by.matvey.lshkn.service;

import by.matvey.lshkn.entity.Role;
import by.matvey.lshkn.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {
    private static UserService service = UserService.getInstance();

    @BeforeAll
    static void init(){
        Map<String, User> users = new HashMap<>();
        users.put("Admin", User.builder()
                .username("Admin")
                .password("admin")
                .role(Role.ADMIN)
                .build());

        users.put("User", User.builder()
                .username("User")
                .password("user")
                .role(Role.USER)
                .build());
        service.setUsers(users);
    }

    @Test
    void registerIfDataIsCorrect() {
        int prevSize = service.getAllUsers().size();
        User user = User.builder()
                .username("Test")
                .password("test")
                .build();

        boolean registerResult = service.register(user);

        assertThat(registerResult).isTrue();
        assertThat(prevSize).isLessThan(service.getAllUsers().size());
    }

    @Test
    void registerIfDataIsIncorrect() {
        User user = User.builder()
                .username("User")
                .password("user")
                .build();

        boolean registerResult = service.register(user);

        assertThat(registerResult).isFalse();
    }

    @Test
    void authenticateIfDataIsCorrect() {
        User user = User.builder()
                .username("User")
                .password("user")
                .build();

        Optional<User> maybeUser = service.authenticate(user);

        assertThat(maybeUser).isNotEmpty();

    }

    @Test
    void authenticateIfDataIsIncorrect() {
        User user = User.builder()
                .username("User")
                .password("123")
                .build();

        Optional<User> maybeUser = service.authenticate(user);

        assertThat(maybeUser).isEmpty();
    }

    @Test
    void getUserByUsername() {
        Optional<User> user1 = service.getUserByUsername("User");
        Optional<User> user2 = service.getUserByUsername("Stub");
        assertThat(user1).isNotEmpty();
        assertThat(user1.get().getUsername()).isEqualTo("User");
        assertThat(user2).isEmpty();
    }
}