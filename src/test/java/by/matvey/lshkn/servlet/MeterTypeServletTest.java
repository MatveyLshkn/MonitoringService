package by.matvey.lshkn.servlet;

import by.matvey.lshkn.dto.MeterTypeDto;
import by.matvey.lshkn.dto.UserDto;
import by.matvey.lshkn.entity.MeterType;
import by.matvey.lshkn.service.MeterTypeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MeterTypeServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private MeterTypeService meterTypeService;
    @InjectMocks
    private MeterTypeServlet meterTypeServlet = new MeterTypeServlet();

    @Test
    void getAllMeterTypes() throws ServletException, IOException {
        Mockito.doReturn(new ArrayList<>(List.of(new MeterType(1L, "Stub"))))
                .when(meterTypeService).getAllMeterTypes();

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.doReturn(writer).when(response).getWriter();

        meterTypeServlet.doGet(request, response);
        writer.flush();

        assertThat(stringWriter.toString()).isNotEmpty();
    }

    @Test
    @DisplayName("Save meter type if user and meter type is correct")
    void saveIfUserAndMeterTypeIsCorrect() throws ServletException, IOException {
        UserDto userDto = UserDto.builder()
                .role("ADMIN")
                .build();
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(userDto).when(session).getAttribute("user");
        Mockito.doReturn(new MeterTypeDto(1L, "stub"))
                .when(meterTypeService).save(request);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.doReturn(writer).when(response).getWriter();

        meterTypeServlet.doPost(request, response);
        writer.flush();

        assertThat(stringWriter.toString()).isNotEmpty();
    }

    @Test
    @DisplayName("Do not save meter type if meter type is incorrect")
    void doNotSaveIfMeterTypeIsIncorrect() throws ServletException, IOException {
        UserDto userDto = UserDto.builder()
                .role("ADMIN")
                .build();
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(userDto).when(session).getAttribute("user");
        Mockito.doReturn(new MeterTypeDto(null, "stub"))
                .when(meterTypeService).save(request);

        meterTypeServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Do not save meter type if user is incorrect")
    void doNotSaveIfUserIsIncorrect() throws ServletException, IOException {
        UserDto userDto = UserDto.builder()
                .role("USER")
                .build();
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(userDto).when(session).getAttribute("user");

        meterTypeServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}