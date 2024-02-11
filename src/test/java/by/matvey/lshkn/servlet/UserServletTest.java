package by.matvey.lshkn.servlet;

import by.matvey.lshkn.dto.UserDto;
import by.matvey.lshkn.service.UserService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserServlet userServlet = new UserServlet();

    @Test
    @DisplayName("Get all usernames if user is admin")
    void getAllUsernamesIfUserIsAdmin() throws ServletException, IOException {
        UserDto userDto = UserDto.builder()
                .role("ADMIN")
                .username("stub")
                .password("stub")
                .build();
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(userDto).when(session).getAttribute("user");
        Mockito.doReturn(List.of("User", "Admin")).when(userService).getAllUsersUsernames();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.doReturn(writer).when(response).getWriter();
        userServlet.doGet(request, response);
        writer.flush();

        assertThat(stringWriter.toString()).isNotEmpty();
    }

    @Test
    @DisplayName("Get user if user is regular user")
    void getUserIfUserIsRegularUser() throws ServletException, IOException {
        UserDto userDto = UserDto.builder()
                .role("USER")
                .username("stub")
                .password("stub")
                .build();
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(userDto).when(session).getAttribute("user");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.doReturn(writer).when(response).getWriter();

        userServlet.doGet(request, response);
        writer.flush();

        assertThat(stringWriter.toString()).isNotEmpty();
        assertThat(stringWriter.toString()).contains("stub");
    }

    @Test
    @DisplayName("Do not get anything if user is incorrect")
    void doNotGetIfUserIsIncorrect() throws ServletException, IOException {
        UserDto userDto = UserDto.builder()
                .build();
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(userDto).when(session).getAttribute("user");

        userServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }


    @Test
    @DisplayName("Save user if user is correct")
    void doSaveIfUserIsCorrect() throws IOException, ServletException {
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn(UserDto.builder()
                .id(1L)
                .username("stub")
                .password("stub")
                .role("ADMIN")
                .build()).when(userService).save(request);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.doReturn(writer).when(response).getWriter();

        userServlet.doPost(request, response);

        assertThat(stringWriter.toString()).isNotEmpty();
    }

    @Test
    @DisplayName("Do not save user if user is incorrect")
    void doNotSaveIfUserIsIncorrect() throws ServletException, IOException {
        UserDto userDto = UserDto.builder()
                .build();
        Mockito.doReturn(userDto).when(userService).save(request);

        userServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}