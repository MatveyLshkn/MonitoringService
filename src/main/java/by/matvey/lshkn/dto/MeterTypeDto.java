package by.matvey.lshkn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Meter type dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeterTypeDto {
    /**
     * Unique meterType identifier
     */
    private Long id;
    /**
     * Name of meter type
     */
    private String name;
}
