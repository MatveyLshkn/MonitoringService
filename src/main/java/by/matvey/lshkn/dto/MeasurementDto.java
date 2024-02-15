package by.matvey.lshkn.dto;

import lombok.*;

/**
 * Measurement dto
 */
@Data
@EqualsAndHashCode(exclude = "meter")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeasurementDto {
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
    private String date;
    /**
     * Meter to which the measurements belong
     *
     * @see MeterDto
     */
    private MeterDto meter;
}
