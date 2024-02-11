package by.matvey.lshkn.util;

import by.matvey.lshkn.dto.MeasurementDto;
import by.matvey.lshkn.dto.UserDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Validator {
    public static boolean validateUserDto(UserDto userDto) {
        return userDto != null && userDto.getRole() != null && userDto.getPassword() != null && userDto.getUsername() != null;
    }

    public static boolean validateMeasurementDto(MeasurementDto measurementDto) {
        return measurementDto!=null && measurementDto.getValue() != null && measurementDto.getDate() != null;
    }
}
