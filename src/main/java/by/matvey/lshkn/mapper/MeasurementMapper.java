package by.matvey.lshkn.mapper;

import by.matvey.lshkn.dto.MeasurementDto;
import by.matvey.lshkn.entity.Measurement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for Measurement and MeasurementDto
 */
@Mapper
public interface MeasurementMapper {
    /**
     * Maps Measurement to MeasurementDto
     *
     * @param measurement measurement to map
     * @return MeasurementDto in which measurement was mapped
     */
    @Mapping(target = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    MeasurementDto measurementToMeasurementDto(Measurement measurement);

    /**
     * Maps MeasurementDto to Measurement
     *
     * @param measurementDto measurementDto to map
     * @return Measurement in which MeasurementDto was mapped
     */
    @Mapping(target = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    Measurement measurementDtoToMeasurement(MeasurementDto measurementDto);
}
