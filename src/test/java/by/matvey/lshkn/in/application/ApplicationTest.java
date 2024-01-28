package by.matvey.lshkn.in.application;

import by.matvey.lshkn.entity.Role;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.in.application.Application;
import by.matvey.lshkn.service.MeterService;
import by.matvey.lshkn.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ApplicationTest {
    @Mock
    private Scanner scan;
    @InjectMocks
    private Application application = new Application();

    @BeforeAll
    static void init(){
        UserService userService = UserService.getInstance();
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
        userService.setUsers(users);

        MeterService meterService = MeterService.getInstance();
        Set<String> availableMeterNames = new HashSet<>();
        availableMeterNames.add("heat");
        availableMeterNames.add("hot_water");
        availableMeterNames.add("cold_water");
        meterService.setAvailableMeterNames(availableMeterNames);
    }

    @Test
    void registerIfDataIsCorrect() {
        Mockito.when(scan.nextLine()).thenReturn("Test").thenReturn("123").thenReturn("USER");

        Optional<User> user = application.register();

        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo("Test");
    }

    @Test
    void registerIfDataIsInCorrect() {
        Mockito.when(scan.nextLine()).thenReturn("User").thenReturn("user").thenReturn("USER");

        Optional<User> user = application.register();

        assertThat(user).isEmpty();
    }

    @Test
    void authorizeIfDataIsCorrect() {
        Mockito.when(scan.nextLine()).thenReturn("User").thenReturn("user");

        Optional<User> user = application.authorize();

        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo("User");
    }

    @Test
    void authorizeIfDataIsInCorrect() {
        Mockito.when(scan.nextLine()).thenReturn("User").thenReturn("stub");
        Optional<User> user = application.authorize();
        assertThat(user).isEmpty();

        Mockito.when(scan.nextLine()).thenReturn("Stub").thenReturn("stub");
        Optional<User> user2 = application.authorize();
        assertThat(user2).isEmpty();
    }
}