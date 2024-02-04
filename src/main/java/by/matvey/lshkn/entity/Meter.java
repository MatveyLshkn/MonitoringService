package by.matvey.lshkn.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Meter entity
 *
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
    /**
     * Unique meter identifier
     */
    private Long id;
    /**
     * Type of meter
     *
     * @see MeterType
     */
    private MeterType type;
    /**
     * Owner of this meter
     */
    private User owner;
    /**
     * List with all measurements that belong to this meter
     *
     * @see Measurement
     */
    @Builder.Default
    private List<Measurement> measurements = new ArrayList<>();

    /**
     * Adds measurement to List and connects Meter entity with Measurement entity
     *
     * @param measurement measurement to add
     * @see Measurement
     */
    public void addMeasurement(Measurement measurement) {
        measurements.add(measurement);
        measurement.setMeter(this);
    }

    /**
     * Deletes measurement from List and breaks connection between Meter entity and Measurement entity
     *
     * @param measurement measurement to delete
     * @see Measurement
     */
    public void deleteMeasurement(Measurement measurement) {
        measurements.remove(measurement);
        measurement.setMeter(null);
    }
}
