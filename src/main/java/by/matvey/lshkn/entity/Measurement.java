package by.matvey.lshkn.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Measurement entity
 */
@Data
@EqualsAndHashCode(exclude = "meter")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Measurement {
    /**
     * Unique measurement identifier
     */
    private Long id;
    /**
     * Value of measurement
     */
    private Double value;
    /**
     * Date the measurement was taken
     */
    private LocalDateTime date;
    /**
     * Meter to which the measurements belong
     */
    private Meter meter;
}
