package by.matvey.lshkn.util;


import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Class intended to manage connections
 */
@UtilityClass
public class ConnectionManager {
    private static final String PASSWORD_KEY = "db.password";
    private static final String USERNAME_KEY = "db.user";
    private static final String URL_KEY = "db.url";
    private static final String BASE_URL_KEY = "db.baseUrl";
    @Setter
    private static String baseUrlValue;
    @Setter
    private static String urlValue;
    @Setter
    private static String passwordValue;
    @Setter
    private static String usernameValue;

    static {
        loadDriver();
    }

    /**
     * Loads JDBC driver
     */
    @SneakyThrows
    private static void loadDriver() {
        Class.forName("org.postgresql.Driver");
    }

    /**
     * Creates connection
     * @return created connection
     */
    @SneakyThrows
    public static Connection get() {
        if (urlValue != null && usernameValue != null && passwordValue != null) {
            return DriverManager.getConnection(urlValue, usernameValue, passwordValue);
        }
        return DriverManager.getConnection(
                PropertiesUtil.get(URL_KEY),
                PropertiesUtil.get(USERNAME_KEY),
                PropertiesUtil.get(PASSWORD_KEY)
        );
    }

    /**
     * <p>Opens connection using base url (db.baseUrl in application/properties).</p>
     * <p>Base url should lead exactly to database and shouldn't include schema.</p>
     */
    @SneakyThrows
    public static Connection openBasicConnection() {
        if (baseUrlValue != null && usernameValue != null && passwordValue != null) {
            return DriverManager.getConnection(baseUrlValue, usernameValue, passwordValue);
        }
        return DriverManager.getConnection(
                PropertiesUtil.get(BASE_URL_KEY),
                PropertiesUtil.get(USERNAME_KEY),
                PropertiesUtil.get(PASSWORD_KEY)
        );
    }
}
