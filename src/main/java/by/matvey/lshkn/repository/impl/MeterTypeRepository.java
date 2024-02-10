package by.matvey.lshkn.repository.impl;

import by.matvey.lshkn.entity.MeterType;
import by.matvey.lshkn.repository.Repository;
import by.matvey.lshkn.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class intended to work with database table meter_type
 */
public class MeterTypeRepository implements Repository<MeterType, Long> {
    private static final MeterTypeRepository INSTANCE = new MeterTypeRepository();
    private static final String SAVE_SQL = """
            INSERT INTO meter_type(name)
            VALUES(?)
            RETURNING id;
            """;
    public static final String FIND_BY_ID_SQL = """
            SELECT * FROM meter_type WHERE id = ?;
            """;
    public static final String FIND_BY_NAME_SQL = """
            SELECT * FROM meter_type WHERE name = ?;
            """;
    public static final String FIND_ALL_SQL = """
            SELECT * FROM meter_type;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM meter_type WHERE name = ?;
            """;
    private static final String UPDATE_SQL = """
            UPDATE meter_type
            SET name = ?
            WHERE id = ?;
            """;

    private MeterTypeRepository() {
    }

    public static MeterTypeRepository getInstance() {
        return INSTANCE;
    }

    /**
     * Saves meter type in database
     *
     * @param meterType meter type to save
     * @return meter type with assigned id
     */
    @Override
    public MeterType save(MeterType meterType) {
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            statement.setObject(1, meterType.getName());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            while (generatedKeys.next()) {
                meterType.setId(generatedKeys.getLong("id"));
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
        return meterType;
    }

    /**
     * Collects all meters types in database
     *
     * @return List of all meter types
     */
    public List<MeterType> findAll() {
        List<MeterType> types = new ArrayList<>();
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL)) {
            connection.setAutoCommit(false);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                types.add(new MeterType(resultSet.getLong("id"), resultSet.getString("name")));
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
        return types;
    }

    /**
     * Finds meter type by id in database
     *
     * @param id meter types id
     * @return Optional of meter type if meter type found or empty optional if not found
     */
    @Override
    public Optional<MeterType> findById(Long id) {
        Optional<MeterType> meterType = Optional.empty();
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                meterType = Optional.of(new MeterType(resultSet.getLong("id"), resultSet.getString("name")));
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
        return meterType;
    }

    /**
     * Finds meter type by username in database
     *
     * @param name meter types name
     * @return Optional of meter type if meter type found or empty optional if not found
     */
    public Optional<MeterType> findByName(String name) {
        Optional<MeterType> meterType = Optional.empty();
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                meterType = Optional.of(new MeterType(resultSet.getLong("id"), resultSet.getString("name")));
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
        return meterType;
    }

    /**
     * Updates meter types name in database
     *
     * @param meterType meter type with info to update
     * @return result of update operation
     */
    @Override
    public boolean update(MeterType meterType) {
        boolean result = false;
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, meterType.getName());
            statement.setObject(2, meterType.getId());
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
     * Deletes meter type from database. Deletes using types name
     *
     * @param meterType meter type to delete (must have name)
     * @return result of delete operation
     */
    @Override
    public boolean delete(MeterType meterType) {
        if (findByName(meterType.getName()).isEmpty()) return false;
        boolean result = false;
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, meterType.getName());
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
