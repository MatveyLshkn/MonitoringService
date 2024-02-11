package by.matvey.lshkn.mapper;

import by.matvey.lshkn.dto.MeasurementDto;
import by.matvey.lshkn.entity.Measurement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MeasurementMapper {
    @Mapping(target = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    MeasurementDto measurementToMeasurementDto(Measurement measurement);

    @Mapping(target = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    Measurement measurementDtoToMeasurement(MeasurementDto measurementDto);
}
