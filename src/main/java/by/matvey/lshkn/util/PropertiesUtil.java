package by.matvey.lshkn.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.util.Properties;

/**
 * Class intended to work with properties from application.properties
 */
@UtilityClass
public class PropertiesUtil {
    private static final ClassLoader CLASS_LOADER = PropertiesUtil.class.getClassLoader();
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    /**
     * Loads properties from file application.properties
     */
    @SneakyThrows
    private static void loadProperties() {
        try (InputStream inputStream = CLASS_LOADER.getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        }
    }

    /**
     * Gets property from file application.properties
     *
     * @param key a string key to a property
     * @return value of property
     */
    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
