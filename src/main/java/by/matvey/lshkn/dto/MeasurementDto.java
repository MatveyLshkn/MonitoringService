package by.matvey.lshkn.dto;

import lombok.*;

@Data
@EqualsAndHashCode(exclude = "meter")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeasurementDto {
    private Long id;
    private Double value;
    private String date;
    private MeterDto meter;
}
