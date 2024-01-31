package by.matvey.lshkn.service;


import by.matvey.lshkn.entity.Meter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class MeterServiceTest {
    private static MeterService service = MeterService.getInstance();

    @BeforeAll
    static void init() {
        Set<String> availableMeterNames = new HashSet<>();
        availableMeterNames.add("heat");
        availableMeterNames.add("hot_water");
        availableMeterNames.add("cold_water");
        service.setAvailableMeterNames(availableMeterNames);
    }

    @Test
    void getMeterIfExists() {
        Optional<Meter> maybeMeter = service.getMeter("heat");

        assertThat(maybeMeter).isNotEmpty();
        assertThat(maybeMeter.get().getName()).isEqualTo("heat");
    }

    @Test
    void getEmptyMeterIfDoesntExist() {
        Optional<Meter> maybeMeter = service.getMeter("stub");

        assertThat(maybeMeter).isEmpty();
    }

    @Test
    void addNewMeterName() {
        int prevSize = service.getAvailableMeterNames().size();

        service.addNewMeterName("Test");

        assertAll(
                () -> assertThat(prevSize).isLessThan(service.getAvailableMeterNames().size()),
                () -> assertThat(service.getAvailableMeterNames()).contains("Test")
        );
    }
}