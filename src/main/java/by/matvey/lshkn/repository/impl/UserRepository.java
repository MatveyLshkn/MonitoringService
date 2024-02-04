package by.matvey.lshkn.repository.impl;

import by.matvey.lshkn.entity.Meter;
import by.matvey.lshkn.entity.Role;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.repository.Repository;
import by.matvey.lshkn.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class intended to work with database table users
 */
public class UserRepository implements Repository<User, Long> {
    private static final UserRepository INSTANCE = new UserRepository();
    private static final String SAVE_SQL = """
            INSERT INTO users(username, password, role)
            VALUES(?,?,?)
            RETURNING id;
            """;
    public static final String FIND_BY_ID_SQL = """
            SELECT * FROM users WHERE id = ?;
            """;
    public static final String FIND_BY_USERNAME_SQL = """
            SELECT * FROM users WHERE username = ?;
            """;
    public static final String FIND_ALL_USERNAMES_SQL = """
            SELECT username FROM users;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM users WHERE username = ?;
            """;
    private static final String UPDATE_SQL = """
            UPDATE users
            SET username = ?,
                password = ?,
                role = ?
            WHERE id = ?;
            """;

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    /**
     * Saves User in database
     *
     * @param user user to save
     * @return user with assigned id
     */
    @Override
    public User save(User user) {
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            statement.setObject(1, user.getUsername());
            statement.setObject(2, user.getPassword());
            statement.setObject(3, user.getRole().toString());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            while (generatedKeys.next()) {
                user.setId(generatedKeys.getLong("id"));
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Error while performing rollback!");
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error while closing connection!");
                e.printStackTrace();
            }
        }
        return user;
    }

    /**
     * Finds user by id in database
     *
     * @param id users id
     * @return Optional of user if user found or empty optional if not found
     */
    @Override
    public Optional<User> findById(Long id) {
        Optional<User> maybeUser = Optional.empty();
        MeterRepository meterRepository = MeterRepository.getInstance();
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getLong("id"))
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .role(Role.valueOf(resultSet.getString("role")))
                        .build();

                List<Meter> meters = meterRepository.findAllByUserId(user.getId());
                meters.forEach(user::addMeter);
                maybeUser = Optional.of(user);
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Error while performing rollback!");
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error while closing connection!");
                e.printStackTrace();
            }
        }
        return maybeUser;
    }

    /**
     * Finds user by username in database
     *
     * @param username users username
     * @return Optional of user if user found or empty optional if not found
     */
    public Optional<User> findByUsername(String username) {
        MeterRepository meterRepository = MeterRepository.getInstance();
        Optional<User> maybeUser = Optional.empty();
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getLong("id"))
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .role(Role.valueOf(resultSet.getString("role")))
                        .build();

                List<Meter> meters = meterRepository.findAllByUserId(user.getId());
                meters.forEach(meter -> meter.setOwner(user));
                user.setMeters(meters);
                maybeUser = Optional.of(user);
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Error while performing rollback!");
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error while closing connection!");
                e.printStackTrace();
            }
        }
        return maybeUser;
    }

    /**
     * Collects all taken usernames
     *
     * @return list of taken usernames
     */
    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_USERNAMES_SQL)) {
            connection.setAutoCommit(false);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                usernames.add(resultSet.getString("username"));
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Error while performing rollback!");
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error while closing connection!");
                e.printStackTrace();
            }
        }
        return usernames;
    }

    /**
     * Updates user info in database
     *
     * @param user user with info to update
     * @return result of update operation
     */
    @Override
    public boolean update(User user) {
        boolean result = false;
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, user.getUsername());
            statement.setObject(2, user.getPassword());
            statement.setObject(3, user.getRole().toString());
            statement.setObject(4, user.getId());
            statement.executeUpdate();
            result = true;
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Error while performing rollback!");
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error while closing connection!");
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Deletes user from database. Deletes using users username
     *
     * @param user user to delete (must have username)
     * @return result of delete operation
     */
    @Override
    public boolean delete(User user) {
        if (findByUsername(user.getUsername()).isEmpty()) return false;
        boolean result = false;
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, user.getUsername());
            statement.executeUpdate();
            result = true;
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Error while performing rollback!");
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error while closing connection!");
                e.printStackTrace();
            }
        }
        return result;
    }
}
