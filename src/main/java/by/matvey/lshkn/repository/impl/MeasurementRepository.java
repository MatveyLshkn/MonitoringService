package by.matvey.lshkn.repository.impl;

import by.matvey.lshkn.entity.Measurement;
import by.matvey.lshkn.repository.Repository;
import by.matvey.lshkn.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class intended to work with database table measurement
 */
public class MeasurementRepository implements Repository<Measurement, Long> {
    private static final MeasurementRepository INSTANCE = new MeasurementRepository();
    private static final String SAVE_SQL = """
            INSERT INTO measurement(value, date, meter_id)
            VALUES(?,?,?)
            RETURNING id;
            """;
    public static final String FIND_BY_ID_SQL = """
            SELECT * FROM measurement WHERE id = ?;
            """;
    public static final String FIND_ALL_BY_METER_ID_SQL = """
            SELECT * FROM measurement WHERE meter_id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM measurement WHERE id = ?;
            """;
    private static final String UPDATE_SQL = """
            UPDATE measurement
            SET value = ?,
                date = ?,
                meter_id = ?
            WHERE id = ?;
            """;

    private MeasurementRepository() {
    }

    public static MeasurementRepository getInstance() {
        return INSTANCE;
    }

    /**
     * Saves measurement in database
     *
     * @param measurement measurement to save
     * @return measurement with assigned id
     */
    @Override
    public Measurement save(Measurement measurement) {
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            statement.setObject(1, measurement.getValue());
            statement.setObject(2, Timestamp.valueOf(measurement.getDate()));
            statement.setObject(3, measurement.getMeter().getId());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            while (generatedKeys.next()) {
                measurement.setId(generatedKeys.getLong("id"));
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
        return measurement;
    }

    /**
     * Finds measurement by id in database
     *
     * @param id meters id
     * @return Optional of measurement if measurement found or empty optional if not found
     */
    @Override
    public Optional<Measurement> findById(Long id) {
        Optional<Measurement> measurement = Optional.empty();
        MeterRepository meterRepository = MeterRepository.getInstance();
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                measurement = Optional.of(Measurement.builder()
                        .id(resultSet.getLong("id"))
                        .value(resultSet.getDouble("value"))
                        .date(resultSet.getTimestamp("date").toLocalDateTime())
                        .meter(meterRepository.findById(resultSet.getLong("meter_id")).orElse(null))
                        .build());
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
        return measurement;
    }

    /**
     * Collects all measurements that related to specific meter in database
     *
     * @param id id of meter
     * @return List of all measurements that are related to meter
     */
    public List<Measurement> findAllByMeterId(Long id) {
        List<Measurement> measurements = new ArrayList<>();
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_METER_ID_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                measurements.add(Measurement.builder()
                        .id(resultSet.getLong("id"))
                        .value(resultSet.getDouble("value"))
                        .date(resultSet.getTimestamp("date").toLocalDateTime())
                        .build());
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
        return measurements;
    }

    /**
     * Updates measurements data in database
     *
     * @param measurement measurement with info to update
     * @return result of update operation
     */
    @Override
    public boolean update(Measurement measurement) {
        boolean result = false;
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, measurement.getValue());
            statement.setObject(2, Timestamp.valueOf(measurement.getDate()));
            statement.setObject(3, measurement.getMeter().getId());
            statement.setObject(4, measurement.getId());
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
     * Deletes measurement from database. Deletes using measurements id
     *
     * @param measurement measurement to delete (must have id)
     * @return result of delete operation
     */
    @Override
    public boolean delete(Measurement measurement) {
        if (findById(measurement.getId()).isEmpty()) return false;
        boolean result = false;
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, measurement.getId());
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
