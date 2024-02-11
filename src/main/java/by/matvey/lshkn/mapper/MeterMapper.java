package by.matvey.lshkn.mapper;

import by.matvey.lshkn.dto.MeterDto;
import by.matvey.lshkn.entity.Meter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MeterMapper {
    MeterDto meterToMeterDto(Meter meter);

    @Mapping(target = "measurements", ignore = true)
    Meter meterDtoToMeter(MeterDto meterDto);

}
