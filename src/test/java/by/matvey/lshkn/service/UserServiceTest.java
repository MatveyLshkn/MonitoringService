package by.matvey.lshkn.service;

import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.repository.impl.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService = UserService.getInstance();

    @Test
    @DisplayName("Test will be passed if userRepository.findByUsername returns empty optional and userRepository.save returns Optional.of(user)")
    void registerIfDataIsCorrect() {
        User user = User.builder()
                .username("Test")
                .password("test")
                .build();
        Mockito.doReturn(Optional.empty()).when(userRepository).findByUsername(user.getUsername());
        Mockito.doReturn(user).when(userRepository).save(user);

        Optional<User> maybeUser = userService.register(user);

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("Test will be passed if userRepository.findByUsername returns Optional.of(user)")
    void registerIfDataIsIncorrect() {
        User user = User.builder()
                .username("User")
                .password("user")
                .build();
        Mockito.doReturn(Optional.of(user)).when(userRepository).findByUsername(user.getUsername());

        Optional<User> maybeUser = userService.register(user);

        assertThat(maybeUser).isEmpty();
    }

    @Test
    @DisplayName("Test will be passed if userRepository.findByUsername returns Optional.of(user) and password is correct")
    void authenticateIfDataIsCorrect() {
        User user = User.builder()
                .username("User")
                .password("user")
                .build();
        Mockito.doReturn(Optional.of(user)).when(userRepository).findByUsername(user.getUsername());

        Optional<User> maybeUser = userService.authenticate(user);

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("Test will be passed if userRepository.findByUsername returns empty optional or password is incorrect")
    void authenticateIfDataIsIncorrect() {
        User user1 = User.builder()
                .username("Stub")
                .password("stub")
                .build();
        Mockito.doReturn(Optional.empty()).when(userRepository).findByUsername(user1.getUsername());

        Optional<User> maybeUser = userService.authenticate(user1);

        assertThat(maybeUser).isEmpty();
    }

    @Test
    @DisplayName("Test will be passed if userRepository.findByUsername returns Optional.of(user1) for user1 and empty optional for user2")
    void getUserByUsername() {
        Mockito.doReturn(Optional.of(User.builder()
                .username("User")
                .build())).when(userRepository).findByUsername("User");
        Mockito.doReturn(Optional.empty()).when(userRepository).findByUsername("Stub");

        Optional<User> user1 = userService.getUserByUsername("User");
        Optional<User> user2 = userService.getUserByUsername("Stub");

        assertThat(user1).isNotEmpty();
        assertThat(user1.get().getUsername()).isEqualTo("User");
        assertThat(user2).isEmpty();
    }

    @Test
    @DisplayName("Test will be passed if userRepository.getAllUsernames() returns list with size 1 (userRepository mocked to return list with size 1)")
    void getAllUsersUsernames() {
        Mockito.doReturn(new ArrayList<>(List.of("Test"))).when(userRepository).getAllUsernames();

        List<String> usernames = userService.getAllUsersUsernames();

        assertThat(usernames).hasSize(1);
    }
}