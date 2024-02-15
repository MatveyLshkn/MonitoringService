package by.matvey.lshkn.util;

import by.matvey.lshkn.dto.MeasurementDto;
import by.matvey.lshkn.dto.UserDto;
import lombok.experimental.UtilityClass;

/**
 * Class intended to validate Data
 */
@UtilityClass
public class Validator {
    /**
     * Validates userDto
     *
     * @param userDto userDto to validate
     * @return result of validation
     */
    public static boolean validateUserDto(UserDto userDto) {
        return userDto != null && userDto.getRole() != null && userDto.getPassword() != null && userDto.getUsername() != null;
    }

    /**
     * Validates measurementDto
     *
     * @param measurementDto measurementDto to validate
     * @return result of validation
     */
    public static boolean validateMeasurementDto(MeasurementDto measurementDto) {
        return measurementDto != null && measurementDto.getValue() != null && measurementDto.getDate() != null;
    }
}
