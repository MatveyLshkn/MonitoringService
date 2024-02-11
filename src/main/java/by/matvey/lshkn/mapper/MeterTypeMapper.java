package by.matvey.lshkn.mapper;

import by.matvey.lshkn.dto.MeterTypeDto;
import by.matvey.lshkn.entity.MeterType;
import org.mapstruct.Mapper;

@Mapper
public interface MeterTypeMapper {
    MeterTypeDto meterTypeToMeterTypeDto(MeterType meterType);

    MeterType meterTypeDtoToMeterType(MeterTypeDto meterTypeDto);
}
