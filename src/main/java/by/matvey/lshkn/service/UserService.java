package by.matvey.lshkn.service;

import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.repository.impl.UserRepository;
import lombok.Setter;

import java.util.*;

/**
 * Class intended to work with Users
 */
@Setter
public class UserService {
    private static final UserService INSTANCE = new UserService();
    private UserRepository userRepository = UserRepository.getInstance();

    private UserService() {
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    /**
     * Performs registration of user.
     *
     * @param user user that will be registered if his username is unique
     * @return result of registration: true of successful otherwise false (if there is user with that username in database)
     * //@see UserService#users
     */
    public Optional<User> register(User user) {
        Optional<User> maybeUser = userRepository.findByUsername(user.getUsername());
        if (maybeUser.isPresent()) return Optional.empty();
        return Optional.of(userRepository.save(user));
    }

    /**
     * Authenticates user from param (validates username and password)
     *
     * @param user user to authenticate which will be replaced with the real one if authentication was successful
     * @return returns Optional of user if authentication was successful (valid username and password), else empty optional
     * @see User
     */
    public Optional<User> authenticate(User user) {
        Optional<User> maybeUser = userRepository.findByUsername(user.getUsername());
        if (maybeUser.isEmpty()) return maybeUser;
        if (!maybeUser.get().getPassword().equals(user.getPassword())) return Optional.empty();
        return maybeUser;
    }

    /**
     * Gets usernames of all users
     *
     * @return list of all users stored in program
     */
    public List<String> getAllUsersUsernames() {
        return userRepository.getAllUsernames();
    }

    /**
     * Gets user by username from database
     *
     * @param username User with this username will be returned from users map
     * @return Optional User with that username
     * @see User
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
