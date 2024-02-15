package by.matvey.lshkn.in.servlet;

import by.matvey.lshkn.annotation.Loggable;
import by.matvey.lshkn.dto.UserDto;
import by.matvey.lshkn.entity.Role;
import by.matvey.lshkn.service.UserService;
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

/**
 * Servlet for users
 */
@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private UserService userService = UserService.getInstance();

    @Loggable
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession();
        UserDto userDto = (UserDto) session.getAttribute("user");
        if (!Validator.validateUserDto(userDto)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (userDto.getRole().equals(Role.ADMIN.name())) {
            userService.getAllUsersUsernames().forEach(writer::println);
        } else {
            String value = objectMapper.writeValueAsString(userDto);
            writer.write(value);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Loggable
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDto userDto = userService.save(req);
        if (!Validator.validateUserDto(userDto)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (userDto.getId() != null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            String json = objectMapper.writeValueAsString(userDto);
            writer.write(json);
            HttpSession session = req.getSession();
            session.setAttribute("user", userDto);
        } else resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
