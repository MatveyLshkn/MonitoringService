package by.matvey.lshkn.mapper;

import by.matvey.lshkn.dto.MeterTypeDto;
import by.matvey.lshkn.entity.MeterType;
import org.mapstruct.Mapper;

/**
 * Mapper for MeterType and MeterTypeDto
 */
@Mapper
public interface MeterTypeMapper {
    /**
     * Maps MeterType to MeterTypeDto
     *
     * @param meterType meter to map
     * @return MeterTypeDto in which MeterType was mapped
     */
    MeterTypeDto meterTypeToMeterTypeDto(MeterType meterType);

    /**
     * Maps MeterTypeDto to MeterType
     *
     * @param meterTypeDto meterDto to map
     * @return MeterType in which MeterTypeDto was mapped
     */
    MeterType meterTypeDtoToMeterType(MeterTypeDto meterTypeDto);
}
