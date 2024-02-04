package by.matvey.lshkn.util;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Class intended to perform audition of user's actions. Doesn't save auditInfo in database (works in runtime).
 */
@UtilityClass
public class AuditUtil {
    /**
     * Contains all auditions
     */
    @Getter
    @Setter
    private static List<String> auditInfo = new ArrayList<>();

    /**
     * Stores auditions
     *
     * @param info info to store
     * @see AuditUtil#auditInfo
     */
    public static void write(String info) {
        if (auditInfo == null) auditInfo = new ArrayList<>();
        auditInfo.add(info);
    }
}
