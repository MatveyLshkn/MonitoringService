package by.matvey.lshkn.service;

import by.matvey.lshkn.entity.Meter;
import by.matvey.lshkn.util.LoggerUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;


/**
 * Class intended to work with Meters
 */
@Setter
@Getter
public class MeterService {

    private static final MeterService INSTANCE = new MeterService();

    /**
     * Contains all names of meters that can be used in program
     */
    private Set<String> availableMeterNames = new HashSet<>();

    {
        availableMeterNames.add("heat");
        availableMeterNames.add("hot_water");
        availableMeterNames.add("cold_water");
    }

    private MeterService() {
    }

    public static MeterService getInstance() {
        return INSTANCE;
    }

    /**
     * Adds new meter name
     *
     * @param name name of new meter that can be used in program
     * @see MeterService#availableMeterNames
     */
    public void addNewMeterName(String name) {
        if(availableMeterNames==null)availableMeterNames = new HashSet<>();
        availableMeterNames.add(name);
    }

    /**
     * Creates meter entity if such name exists in availableMeterNames
     *
     * @param name name of meter
     * @return Optional of meter if such name exists in availableMeterNames, else empty Optional
     * @see MeterService#availableMeterNames
     */
    public Optional<Meter> getMeter(String name) {
        if (availableMeterNames.contains(name)) {
            LoggerUtil.log(LocalDateTime.now() + " Meter found");
            return Optional.of(Meter.builder()
                    .name(name)
                    .build());
        }
        LoggerUtil.log(LocalDateTime.now() + " No such meter: " + name);
        return Optional.empty();
    }
}
