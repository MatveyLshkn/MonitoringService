package by.matvey.lshkn.servlet;

import by.matvey.lshkn.dto.MeasurementDto;
import by.matvey.lshkn.dto.UserDto;
import by.matvey.lshkn.entity.Role;
import by.matvey.lshkn.service.MeasurementService;
import by.matvey.lshkn.util.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/measurements")
public class MeasurementServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MeasurementService measurementService = MeasurementService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        PrintWriter writer = resp.getWriter();
        List<MeasurementDto> measurementDtos = measurementService.get(req);
        String value = objectMapper.writeValueAsString(measurementDtos);
        writer.println(value);
        writer.close();
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserDto userDto = (UserDto) session.getAttribute("user");
        if (userDto == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } else if (!userDto.getRole().equals(Role.USER.name())) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        MeasurementDto measurementDto = measurementService.save(req);
        if (measurementDto.getId() == null || Validator.validateMeasurementDto(measurementDto)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = resp.getWriter();
            writer.write(objectMapper.writeValueAsString(measurementDto));
            writer.close();
        }
    }
}
