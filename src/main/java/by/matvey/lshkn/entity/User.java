package by.matvey.lshkn.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Meter entity
 * <p>Fields:</p>
 * <p>username -> username</p>
 * <p>password -> password</p>
 * <p>role -> role of user</p>
 * <p>meters -> List of meters which are related to this User</p>
 * @see User
 * @see Measurement
 */
@Data
@ToString(exclude = {"meters", "password"})
@EqualsAndHashCode(exclude = "meters")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String username;
    private String password;
    private Role role;
    @Builder.Default
    private List<Meter> meters = new ArrayList<>();

    /**
     * Adds meter to List and connects User entity with Meter entity
     * @param meter meter to add
     * @see Meter
     */
    public void addMeter(Meter meter) {
        meters.add(meter);
        meter.setOwner(this);
    }

    /**
     * Deletes measurement from List and breaks connection between User entity and Meter entity
     * @param meter meter to delete
     * @see Meter
     */
    public void deleteMeter(Meter meter) {
        meters.remove(meter);
        meter.setOwner(null);
    }
}
