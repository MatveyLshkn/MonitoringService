package by.matvey.lshkn.service;

import by.matvey.lshkn.annotation.Auditable;
import by.matvey.lshkn.annotation.Loggable;
import by.matvey.lshkn.dto.UserDto;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.mapper.UserMapper;
import by.matvey.lshkn.repository.impl.UserRepository;
import by.matvey.lshkn.util.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.mapstruct.factory.Mappers;

import java.io.BufferedReader;
import java.io.IOException;
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
     * Saves or authenticates user from HttpServletRequest content
     *
     * @param req HttpServletRequest
     * @return returns saved/authenticated userDto
     */
    @Loggable
    public UserDto save(HttpServletRequest req) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        String action = req.getHeader("action");
        if (action == null) action = "";
        BufferedReader reader = req.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        while (reader.ready()) {
            stringBuilder.append((char) reader.read());
        }
        UserDto userDto = objectMapper.readValue(stringBuilder.toString(), UserDto.class);
        if (!Validator.validateUserDto(userDto)) {
            return userDto;
        }
        UserMapper mapper = Mappers.getMapper(UserMapper.class);
        User user = mapper.userDtoToUser(userDto);
        if (action.equals("register")) {
            Optional<User> maybeUser = register(user);
            if (maybeUser.isPresent()) {
                return userMapper.userToUserDto(maybeUser.get());
            }
            return userDto;
        } else if (action.equals("authorize")) {
            Optional<User> maybeUser = authenticate(user);
            if (maybeUser.isPresent()) {
                return userMapper.userToUserDto(maybeUser.get());
            }
            return userDto;
        } else return new UserDto();
    }

    /**
     * Performs registration of user.
     *
     * @param user user that will be registered if his username is unique
     * @return result of registration: true of successful otherwise false (if there is user with that username in database)
     * //@see UserService#users
     */
    @Auditable
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
    @Auditable
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


