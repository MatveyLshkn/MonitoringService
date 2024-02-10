package by.matvey.lshkn.service;

import by.matvey.lshkn.entity.MeterType;
import by.matvey.lshkn.repository.impl.MeterTypeRepository;

import java.util.List;
import java.util.Optional;

/**
 * Class intended to work with meter types
 */
public class MeterTypeService {
    private static final MeterTypeService INSTANCE = new MeterTypeService();
    private MeterTypeRepository meterTypeRepository = MeterTypeRepository.getInstance();
    private List<MeterType> meterTypes;

    public static MeterTypeService getInstance() {
        return INSTANCE;
    }

    /**
     * Gets all available meterTypes
     *
     * @return list with all available meterTypes
     */
    public List<MeterType> getAllMeterTypes() {
        if (meterTypes == null) meterTypes = meterTypeRepository.findAll();
        return meterTypes;
    }

    /**
     * Adds new meter type
     *
     * @param name name of new meter type
     */
    public void addMeterType(String name) {
        MeterType newType = new MeterType();
        newType.setName(name);
        meterTypeRepository.save(newType);
        if (meterTypes != null) meterTypes.add(newType);
    }

    /**
     * Gets MeterType by meterType name
     *
     * @param name name of meter type
     * @return Optional of MeterType with that name
     */
    public Optional<MeterType> getMeterTypeByName(String name) {
        return getAllMeterTypes().stream().filter(type -> type.getName().equals(name)).findFirst();
    }
}
