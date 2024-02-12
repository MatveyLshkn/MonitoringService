package by.matvey.lshkn.mapper;

import by.matvey.lshkn.dto.MeterDto;
import by.matvey.lshkn.entity.Meter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for Meter and MeterDto
 */
@Mapper
public interface MeterMapper {
    /**
     * Maps Meter to MeterDto
     *
     * @param meter meter to map
     * @return MeterDto in which Meter was mapped
     */
    MeterDto meterToMeterDto(Meter meter);

    /**
     * Maps MeterDto to Meter
     *
     * @param meterDto meterDto to map
     * @return Meter in which MeterDto was mapped
     */
    @Mapping(target = "measurements", ignore = true)
    Meter meterDtoToMeter(MeterDto meterDto);

}
