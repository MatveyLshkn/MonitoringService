package by.matvey.lshkn.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User entity
 *
 * @see Role
 * @see Meter
 */
@Data
@ToString(exclude = {"meters", "password"})
@EqualsAndHashCode(exclude = "meters")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    /**
     * Unique user identifier
     */
    private Long id;
    /**
     * Username of user
     */
    private String username;
    /**
     * user's password
     */
    private String password;
    /**
     * Role of user
     *
     * @see Role
     */
    private Role role;
    /**
     * List with all meters that belong to user
     */
    @Builder.Default
    private List<Meter> meters = new ArrayList<>();

    /**
     * Adds meter to List and connects User entity with Meter entity
     *
     * @param meter meter to add
     * @see Meter
     */
    public void addMeter(Meter meter) {
        meters.add(meter);
        meter.setOwner(this);
    }

    /**
     * Deletes measurement from List and breaks connection between User entity and Meter entity
     *
     * @param meter meter to delete
     * @see Meter
     */
    public void deleteMeter(Meter meter) {
        meters.remove(meter);
        meter.setOwner(null);
    }
}
