package by.matvey.lshkn.dto;

import lombok.*;

/**
 * Meter dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeterDto {
    /**
     * Unique meter identifier
     */
    private Long id;
    /**
     * Type of meter
     *
     * @see MeterTypeDto
     */
    private MeterTypeDto type;
    /**
     * Owner of this meter
     *
     * @see UserDto
     */
    private UserDto owner;
}
