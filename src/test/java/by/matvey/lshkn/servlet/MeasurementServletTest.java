package by.matvey.lshkn.servlet;

import by.matvey.lshkn.dto.MeasurementDto;
import by.matvey.lshkn.dto.UserDto;
import by.matvey.lshkn.service.MeasurementService;
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
class MeasurementServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private MeasurementService measurementService;
    @InjectMocks
    private MeasurementServlet measurementServlet = new MeasurementServlet();

    @Test
    @DisplayName("Measurements will be printed if user is valid")
    void printMeasurementsIfUserIsValid() throws ServletException, IOException {
        UserDto userDto = UserDto.builder()
                .role("ADMIN")
                .build();
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(userDto).when(session).getAttribute("user");
        Mockito.doReturn(new ArrayList<>(List.of(new MeasurementDto(1L, 12.2, null, null))))
                .when(measurementService).get(request);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.doReturn(writer).when(response).getWriter();

        measurementServlet.doGet(request, response);
        writer.flush();

        assertThat(stringWriter.toString()).isNotEmpty();
    }

    @Test
    @DisplayName("Measurements will not be printed if user is null")
    void doNotPrintIfUserIsNull() throws IOException, ServletException {
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(null).when(session).getAttribute("user");

        measurementServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Measurement will be saved if user and measurement is valid")
    void doSaveIfValidMeasurementAndUser() throws ServletException, IOException {
        UserDto userDto = UserDto.builder()
                .role("USER")
                .build();
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(userDto).when(session).getAttribute("user");
        Mockito.doReturn(new MeasurementDto(1L, 12.2, null, null))
                .when(measurementService).save(request);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.doReturn(writer).when(response).getWriter();

        measurementServlet.doPost(request, response);
        writer.flush();

        assertThat(stringWriter.toString()).isNotEmpty();
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Measurement will not be saved if user is null")
    void doNotSaveIfUserIsNull() throws ServletException, IOException {
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(null).when(session).getAttribute("user");

        measurementServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Measurement will not be saved if measurement is invalid")
    void doFailIfInvalidMeasurement() throws ServletException, IOException {
        UserDto userDto = UserDto.builder()
                .role("USER")
                .build();
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(userDto).when(session).getAttribute("user");
        Mockito.doReturn(new MeasurementDto(null,12.2, null, null))
                .when(measurementService).save(request);

        measurementServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}