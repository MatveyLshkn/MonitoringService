package by.matvey.lshkn.entity;

import lombok.*;

/**
 * MeterType entity. Contains data with all types of meter
 *
 * @see Meter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeterType {
    /**
     * Unique meterType identifier
     */
    private Long id;
    /**
     * Name of meter type
     */
    private String name;
}
