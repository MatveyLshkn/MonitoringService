package by.matvey.lshkn.service;

import by.matvey.lshkn.entity.Measurement;
import by.matvey.lshkn.entity.Meter;
import by.matvey.lshkn.entity.Role;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.util.LoggerUtil;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;


/**
 * Class intended to work with Users
 */
@Setter
public class UserService {
    /**
     * Map that contains all registered users
     */
    private Map<String, User> users = new HashMap<>();
    private static final UserService INSTANCE = new UserService();

    {
        users.put("Admin", User.builder()
                .username("Admin")
                .password("admin")
                .role(Role.ADMIN)
                .build());

        User user = User.builder()
                .username("User")
                .password("user")
                .role(Role.USER)
                .build();

        Meter heat = Meter.builder()
                .name("heat")
                .build();
        heat.addMeasurement(Measurement.builder()
                .date(LocalDateTime.now().minusYears(35))
                .value(10d)
                .build());
        Meter hotWater = Meter.builder()
                .name("hot_water")
                .build();
        hotWater.addMeasurement(Measurement.builder()
                .value(17d)
                .date(LocalDateTime.now().minusMonths(6))
                .build());
        user.addMeter(heat);
        user.addMeter(hotWater);

        users.put("User",user);
    }

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
     * @see UserService#users
     */
    public boolean register(User user) {
        if (users == null) users = new HashMap<>();
        LoggerUtil.log(LocalDateTime.now() + " Attempting to register user: " + user);
        if (users.get(user.getUsername()) != null) {
            LoggerUtil.log("User with username: " + user.getUsername() + " already exists");
            return false;
        }
        users.put(user.getUsername(), user);
        LoggerUtil.log("User: " + user + " registered");
        return true;
    }

    /**
     * Authenticates user from param (validates username and password)
     *
     * @param user user to authenticate which will be replaced with the real one if authentication was successful
     * @return returns Optional of real user from users map if authentication was successful (valid username and password), else empty optional
     * @see UserService#users
     */
    public Optional<User> authenticate(User user) {
        if (users == null) users = new HashMap<>();
        LoggerUtil.log(LocalDateTime.now() + " Authenticating user: " + user);
        User actual = users.get(user.getUsername());
        if (actual != null && actual.getPassword().equals(user.getPassword())) {
            LoggerUtil.log("User: " + user + " authenticated");
            return Optional.of(actual);
        }
        LoggerUtil.log(LocalDateTime.now() + " Wrong password or user: " + user.getUsername() + "does not exist");
        return Optional.empty();
    }

    /**
     * @return set of all users stored in program
     * @see UserService#users
     */
    public Set<User> getAllUsers() {
        if (users == null) users = new HashMap<>();
        LoggerUtil.log(LocalDateTime.now() + " Action getAllUsers performed");
        return new HashSet<>(users.values());
    }

    /**
     * @param username User with this username will be returned from users map
     * @return User with username
     * @see UserService#users
     */
    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
}
