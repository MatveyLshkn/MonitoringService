package by.matvey.lshkn.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Measurement entity
 * <p>Fields:</p>
 * <p>Value -> measurement value</p>
 * <p>Date -> date of measurement submission</p>
 * <p>Meter -> meter to which measurement related</p>
 */
@Data
@EqualsAndHashCode(exclude = "meter")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Measurement {
    private Double value;
    private LocalDateTime date;
    private Meter meter;
}
