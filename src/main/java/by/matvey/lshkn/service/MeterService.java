package by.matvey.lshkn.service;

import by.matvey.lshkn.entity.Meter;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.repository.impl.MeterRepository;
import lombok.Setter;

/**
 * Class intended to work with Meters
 */
@Setter
public class MeterService {
    private MeterRepository meterRepository = MeterRepository.getInstance();

    private static final MeterService INSTANCE = new MeterService();

    private MeterService() {
    }

    public static MeterService getInstance() {
        return INSTANCE;
    }

    /**
     * Adds meter to user. Saves meter in database. Connects meter and user entities
     *
     * @param meter meter which will be added to user
     * @param user  user to which meter will be added
     */
    public void addMeterToUser(Meter meter, User user) {
        user.addMeter(meter);
        meterRepository.save(meter);
    }
}
