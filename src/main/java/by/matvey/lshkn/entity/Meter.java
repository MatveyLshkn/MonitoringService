package by.matvey.lshkn.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Meter entity
 * <p>Fields:</p>
 * <p>Name -> name of meter</p>
 * <p>Owner -> User which owns current meter</p>
 * <p>Measurements -> List of measurements which are related to this meter</p>
 * @see User
 * @see Measurement
 */
@Data
@ToString(exclude = "measurements")
@EqualsAndHashCode(exclude = "measurements")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meter {
    private String name;
    private User owner;
    @Builder.Default
    private List<Measurement> measurements = new ArrayList<>();

    /**
     * Adds measurement to List and connects Meter entity with Measurement entity
     * @param measurement measurement to add
     * @see Measurement
     */
    public void addMeasurement(Measurement measurement) {
        measurements.add(measurement);
        measurement.setMeter(this);
    }

    /**
     * Deletes measurement from List and breaks connection between Meter entity and Measurement entity
     * @param measurement measurement to delete
     * @see Measurement
     */
    public void deleteMeasurement(Measurement measurement) {
        measurements.remove(measurement);
        measurement.setMeter(null);
    }
}
