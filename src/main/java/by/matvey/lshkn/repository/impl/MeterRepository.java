package by.matvey.lshkn.repository.impl;

import by.matvey.lshkn.entity.Measurement;
import by.matvey.lshkn.entity.Meter;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.repository.Repository;
import by.matvey.lshkn.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class intended to work with database table meter
 */
public class MeterRepository implements Repository<Meter, Long> {
    private static final MeterRepository INSTANCE = new MeterRepository();
    private static final String SAVE_SQL = """
            INSERT INTO meter(meter_type_id, owner_id)
            VALUES(?,?)
            RETURNING id;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT * FROM meter WHERE id = ?;
            """;
    private static final String FIND_ALL_BY_USER_SQL = """
            SELECT * FROM meter WHERE owner_id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM meter WHERE id = ?;
            """;
    private static final String UPDATE_SQL = """
            UPDATE meter
            SET meter_type_id = ?,
                owner_id = ?
            WHERE id = ?;
            """;

    private MeterRepository() {
    }

    public static MeterRepository getInstance() {
        return INSTANCE;
    }

    /**
     * Saves meter in database
     *
     * @param meter meter to save
     * @return meter with assigned id
     */
    @Override
    public Meter save(Meter meter) {
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            statement.setObject(1, meter.getType().getId());
            statement.setObject(2, meter.getOwner().getId());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            while (generatedKeys.next()) {
                meter.setId(generatedKeys.getLong("id"));
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
        return meter;
    }

    /**
     * Finds meter by id in database
     *
     * @param id meters id
     * @return Optional of meter if meter found or empty optional if not found
     */
    @Override
    public Optional<Meter> findById(Long id) {
        Optional<Meter> maybeMeter = Optional.empty();
        MeterTypeRepository meterTypeRepository = MeterTypeRepository.getInstance();
        MeasurementRepository measurementRepository = MeasurementRepository.getInstance();
        UserRepository userRepository = UserRepository.getInstance();
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Meter meter = Meter.builder()
                        .id(resultSet.getLong("id"))
                        .type(meterTypeRepository.findById(resultSet.getLong("meter_type_id")).orElse(null))
                        .owner(userRepository.findById(resultSet.getLong("owner_id")).orElse(null))
                        .build();

                List<Measurement> measurements = measurementRepository.findAllByMeterId(meter.getId());
                measurements.forEach(measurement -> measurement.setMeter(meter));
                meter.setMeasurements(measurements);

                maybeMeter = Optional.of(meter);
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
        return maybeMeter;
    }

    /**
     * Collects all meters that related to specific user in database
     *
     * @param id id of user
     * @return List of all meters that are related to user
     */
    public List<Meter> findAllByUserId(Long id) {
        List<Meter> meters = new ArrayList<>();
        MeterTypeRepository meterTypeRepository = MeterTypeRepository.getInstance();
        MeasurementRepository measurementRepository = MeasurementRepository.getInstance();
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_USER_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Meter meter = Meter.builder()
                        .id(resultSet.getLong("id"))
                        .type(meterTypeRepository.findById(resultSet.getLong("meter_type_id")).orElse(null))
                        .build();
                meters.add(meter);
            }
            for (Meter meter : meters) {
                List<Measurement> measurements = measurementRepository.findAllByMeterId(meter.getId());
                measurements.forEach(measurement -> measurement.setMeter(meter));
                meter.setMeasurements(measurements);
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
        return meters;
    }

    /**
     * Updates meters data in database
     *
     * @param meter meter with info to update
     * @return result of update operation
     */
    @Override
    public boolean update(Meter meter) {
        boolean result = false;
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, meter.getType().getId());
            statement.setObject(2, meter.getOwner().getId());
            statement.setObject(3, meter.getId());
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
     * Deletes meter from database. Deletes using meters id
     *
     * @param meter meter to delete (must have id)
     * @return result of delete operation
     */
    @Override
    public boolean delete(Meter meter) {
        if (findById(meter.getId()).isEmpty()) return false;
        boolean result = false;
        Connection connection = ConnectionManager.get();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            connection.setAutoCommit(false);
            statement.setObject(1, meter.getId());
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
