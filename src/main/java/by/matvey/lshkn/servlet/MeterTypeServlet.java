package by.matvey.lshkn.servlet;

import by.matvey.lshkn.dto.MeterTypeDto;
import by.matvey.lshkn.dto.UserDto;
import by.matvey.lshkn.mapper.MeterTypeMapper;
import by.matvey.lshkn.service.MeterTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/meter-types")
public class MeterTypeServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private  MeterTypeService meterTypeService = MeterTypeService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        MeterTypeMapper meterTypeMapper = Mappers.getMapper(MeterTypeMapper.class);
        List<MeterTypeDto> meterTypeDtos = meterTypeService.getAllMeterTypes().stream()
                .map(meterTypeMapper::meterTypeToMeterTypeDto)
                .collect(Collectors.toList());
        PrintWriter writer = resp.getWriter();
        String json = objectMapper.writeValueAsString(meterTypeDtos);
        writer.write(json);
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserDto userDto = (UserDto) session.getAttribute("user");
        if (userDto != null && userDto.getRole().equals("ADMIN")) {
            resp.setStatus(HttpServletResponse.SC_OK);
            MeterTypeDto meterTypeDto = meterTypeService.save(req);
            if (meterTypeDto.getId() != null) {
                resp.setContentType("application/json");
                PrintWriter writer = resp.getWriter();
                writer.write(objectMapper.writeValueAsString(meterTypeDto));
                writer.close();
            } else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
