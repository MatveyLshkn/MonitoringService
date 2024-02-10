package by.matvey.lshkn;

import by.matvey.lshkn.util.ConnectionManager;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class that performs database migration using liquibase
 */
public class DatabaseCreator {

    /**
     * Run clearAll method and createAll after that
     */
    public static void main(String[] args) {
        clearAll();
        createAll();
    }

    /**
     * Creates monitoring_service and liquibase schemas from current database and inserts initial data
     */
    public static void createAll() {
        try (Connection connection = ConnectionManager.openBasicConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibaseSchemas =
                    new Liquibase("db/changelog/001-create-schemas.xml", new ClassLoaderResourceAccessor(), database);
            liquibaseSchemas.update();

            Liquibase liquibaseTables =
                    new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            database.setLiquibaseSchemaName("liquibase");
            database.setDefaultSchemaName("monitoring_service");
            liquibaseTables.update();

            System.out.println("Migration is completed");
        } catch (LiquibaseException | SQLException e) {
            System.out.println("Exception occurred!");
            e.printStackTrace();
        }
    }

    /**
     * Deletes monitoring_service and liquibase schemas from current database
     */
    private static void clearAll() {
        try (Connection connection = ConnectionManager.openBasicConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibaseSchemas =
                    new Liquibase("db/changelog/004-drop-all.xml", new ClassLoaderResourceAccessor(), database);
            liquibaseSchemas.update();
        } catch (LiquibaseException | SQLException e) {
            System.out.println("Exception occurred!");
            e.printStackTrace();
        }
    }
}
