package by.matvey.lshkn.service;


import by.matvey.lshkn.entity.Meter;
import by.matvey.lshkn.entity.MeterType;
import by.matvey.lshkn.entity.Role;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.repository.impl.MeterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MeterServiceTest {
    @Mock
    private MeterRepository meterRepository;
    @InjectMocks
    private MeterService meterService = MeterService.getInstance();

    @Test
    void addMeterToUser() {
        User user = User.builder()
                .username("Test")
                .password("test")
                .role(Role.USER)
                .build();
        Meter meter = Meter.builder()
                .type(new MeterType(null, "SomeType"))
                .build();
        Mockito.doReturn(meter).when(meterRepository).save(meter);

        meterService.addMeterToUser(meter, user);

        assertThat(meter.getOwner()).isEqualTo(user);
        assertThat(user.getMeters()).contains(meter);
    }
}