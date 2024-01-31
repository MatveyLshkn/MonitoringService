package by.matvey.lshkn.util;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Class intended to work with logs
 */
@UtilityClass
public class LoggerUtil {
    /**
     * Contains all log info
     */
    @Getter
    @Setter
    private static List<String> logInfo = new ArrayList<>();

    /**
     * Stores log info
     *
     * @param info info to store
     * @see LoggerUtil#logInfo
     */
    public static void log(String info) {
        if(logInfo==null)logInfo = new ArrayList<>();
        logInfo.add(info);
    }
}
