package by.matvey.lshkn.servlet;

import by.matvey.lshkn.dto.UserDto;
import by.matvey.lshkn.entity.Role;
import by.matvey.lshkn.util.AuditUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/auditions")
public class AuditServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserDto userDto = (UserDto) session.getAttribute("user");
        if (userDto == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } else if (!userDto.getRole().equals(Role.ADMIN.name())) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");
        PrintWriter writer = resp.getWriter();
        AuditUtil.getAuditInfo().forEach(writer::println);
        writer.close();
    }
}
