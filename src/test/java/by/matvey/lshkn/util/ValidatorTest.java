package by.matvey.lshkn.util;

import by.matvey.lshkn.dto.MeasurementDto;
import by.matvey.lshkn.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValidatorTest {

    @Test
    @DisplayName("Validation of UserDto is true if userDto is valid(has role,username,password)")
    void validateTrueIfUserDtoValid() {
        UserDto userDto = UserDto.builder()
                .role("ADMIN")
                .username("Stub")
                .password("stub")
                .build();

        assertThat(Validator.validateUserDto(userDto)).isTrue();
    }

    @Test
    @DisplayName("Validation of UserDto is false if userDto is invalid(has no role or username or password)")
    void validateFalseIfUserDtoInValid() {
        UserDto userDto = UserDto.builder()
                .build();

        assertThat(Validator.validateUserDto(userDto)).isFalse();
    }

    @Test
    @DisplayName("Validation of MeasurementDto is true if measurementDto is valid(has date and value)")
    void validateTrueIfMeasurementDtoValid() {
        MeasurementDto measurementDto = MeasurementDto.builder()
                .date("date")
                .value(123.2)
                .build();

        assertThat(Validator.validateMeasurementDto(measurementDto)).isTrue();
    }

    @Test
    @DisplayName("Validation of MeasurementDto is true if measurementDto is invalid(has no date or value)")
    void validateFalseIfMeasurementDtoInValid() {
        MeasurementDto measurementDto = MeasurementDto.builder()
                .build();

        assertThat(Validator.validateMeasurementDto(measurementDto)).isFalse();
    }
}