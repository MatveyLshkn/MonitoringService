package by.matvey.lshkn.service;

import by.matvey.lshkn.entity.MeterType;
import by.matvey.lshkn.repository.impl.MeterTypeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class MeterTypeServiceTest {
    @Mock
    private MeterTypeRepository meterTypeRepository;
    @InjectMocks
    private MeterTypeService meterTypeService = MeterTypeService.getInstance();

    @Test
    @Order(1)
    void getAllMeterTypes() {
        Mockito.doReturn(new ArrayList<>()).when(meterTypeRepository).findAll();

        List<MeterType> types = meterTypeService.getAllMeterTypes();

        assertThat(types).hasSize(0);
    }

    @Test
    @Order(2)
    void addMeterType() {
        Mockito.doReturn(new MeterType(1L, "Stub")).when(meterTypeRepository).save(new MeterType(null, "Stub"));

        meterTypeService.addMeterType("Stub");
        List<MeterType> meterTypes = meterTypeService.getAllMeterTypes();

        assertThat(meterTypes).hasSize(1);
        assertThat(meterTypes.get(0).getName()).isEqualTo("Stub");
    }

    @Test
    @Order(3)
    void getMeterTypeByName() {
        assertThat(meterTypeService.getMeterTypeByName("Stub")).isPresent();
        assertThat(meterTypeService.getMeterTypeByName("Stub").get().getName()).isEqualTo("Stub");
    }
}