package by.matvey.lshkn.service;

import by.matvey.lshkn.dto.MeterTypeDto;
import by.matvey.lshkn.entity.MeterType;
import by.matvey.lshkn.mapper.MeterTypeMapper;
import by.matvey.lshkn.repository.impl.MeterTypeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.mapstruct.factory.Mappers;

import java.io.BufferedReader;
import java.io.IOException;
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
     * Saves meterType from HttpServletRequest content
     *
     * @param req HttpServletRequest
     * @return returns saved meterTypeDto
     */
    public MeterTypeDto save(HttpServletRequest req) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BufferedReader reader = req.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        while (reader.ready()) {
            stringBuilder.append((char) reader.read());
        }
        JsonNode jsonNode = objectMapper.readTree(stringBuilder.toString());
        JsonNode nameNode = jsonNode.get("name");
        if (nameNode != null) {
            MeterTypeMapper meterTypeMapper = Mappers.getMapper(MeterTypeMapper.class);
            return meterTypeMapper.meterTypeToMeterTypeDto(addMeterType(nameNode.asText()));
        }
        return new MeterTypeDto();
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
    public MeterType addMeterType(String name) {
        MeterType newType = new MeterType();
        newType.setName(name);
        MeterType type = meterTypeRepository.save(newType);
        if (meterTypes != null) meterTypes.add(type);
        return type;
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
