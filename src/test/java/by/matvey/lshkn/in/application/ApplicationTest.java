package by.matvey.lshkn.in.application;

import by.matvey.lshkn.entity.Role;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.service.UserService;
import org.junit.jupiter.api.DisplayName;
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
    @Mock
    private UserService userService;
    @InjectMocks
    private Application application = new Application();

    @Test
    @DisplayName("Test will be passed if there's no user with that username and if userService.register returns optional of user")
    void registerIfDataIsCorrect() {
        User user = User.builder()
                .username("Test")
                .password("123")
                .role(Role.USER)
                .build();
        Mockito.doReturn("Test","123","USER").when(scan).nextLine();
        Mockito.doReturn(Optional.of(user)).when(userService).register(user);

        Optional<User> maybeUser = application.register();

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getUsername()).isEqualTo("Test");
    }

    @Test
    @DisplayName("Test will be failed if there's user with that username and if userService.register returns empty optional")
    void registerIfDataIsInCorrect() {
        User user = User.builder()
                .username("User")
                .password("user")
                .role(Role.USER)
                .build();
        Mockito.doReturn("User","user","USER").when(scan).nextLine();
        Mockito.doReturn(Optional.empty()).when(userService).register(user);

        Optional<User> maybeUser = application.register();

        assertThat(maybeUser).isEmpty();
    }

    @Test
    @DisplayName("Test will be passed if there's user with that username, password is correct and if userService.authorize returns optional of user")
    void authorizeIfDataIsCorrect() {
        User user = User.builder()
                .username("User")
                .password("user")
                .build();
        Mockito.doReturn("User","user").when(scan).nextLine();
        Mockito.doReturn(Optional.of(user)).when(userService).authenticate(user);

        Optional<User> maybeUser = application.authorize();

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getUsername()).isEqualTo("User");
    }

    @Test
    @DisplayName("Test will be passed if there's no user with that username or wrong password and if userService.authorize returns empty optional")
    void authorizeIfDataIsInCorrect() {
        User user = User.builder()
                .username("Stub")
                .password("stub")
                .build();
        Mockito.doReturn("Stub","stub").when(scan).nextLine();
        Mockito.doReturn(Optional.empty()).when(userService).authenticate(user);

        Optional<User> maybeUser = application.authorize();

        assertThat(maybeUser).isEmpty();
    }
}