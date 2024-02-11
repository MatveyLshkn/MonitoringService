package by.matvey.lshkn.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeterDto {
    private Long id;
    private MeterTypeDto type;
    private UserDto owner;
}
