package by.matvey.lshkn.servlet;

import by.matvey.lshkn.dto.UserDto;
import by.matvey.lshkn.util.AuditUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    private AuditServlet auditServlet = new AuditServlet();

    @BeforeAll
    static void initAuditor() {
        AuditUtil.write("Stub");
        AuditUtil.write("Test");
    }

    @Test
    @DisplayName("Auditions will be printed if Users role is ADMIN")
    void printIfUserIsAdmin() throws IOException, ServletException {
        UserDto userDto = UserDto.builder()
                .role("ADMIN")
                .build();
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(userDto).when(session).getAttribute("user");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.doReturn(writer).when(response).getWriter();

        auditServlet.doGet(request, response);
        writer.flush();

        assertThat(stringWriter.toString()).contains("Stub");
    }

    @Test
    @DisplayName("Auditions will not be printed if Users role is not ADMIN")
    void doNotPrintIfUserIsRegular() throws IOException, ServletException {
        UserDto userDto = UserDto.builder()
                .role("USER")
                .build();
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(userDto).when(session).getAttribute("user");

        auditServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Auditions will not be printed if User is null")
    void doNotPrintIfUserIsNull() throws IOException, ServletException {
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(null).when(session).getAttribute("user");

        auditServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}