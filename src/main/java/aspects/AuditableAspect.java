package aspects;

import by.matvey.lshkn.dto.UserDto;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.util.AuditUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Aspect that creates code to write auditions about user
 * */
@Aspect
public class AuditableAspect {
    @Pointcut("@annotation(by.matvey.lshkn.annotation.Auditable) && execution(* *(..))")
    public void annotatedByAuditable() {}

    @Around("annotatedByAuditable()")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Optional<HttpServletRequest> maybeReq = getRequestParameter(joinPoint);
        Optional<User> maybeUser = getUserParameter(joinPoint);

        String auditText;
        if(maybeReq.isPresent()){
            HttpSession session = maybeReq.get().getSession();
            UserDto userDto = (UserDto) session.getAttribute("user");
            if(userDto!=null) {
                String username = userDto.getUsername();
                auditText = String.format("[%s] Method '%s' called by user '%s'", timestamp, methodName, username);
                System.out.println(auditText);
                AuditUtil.write(auditText);
            }
        }
        else if(maybeUser.isPresent()){
            String username = maybeUser.get().getUsername();
            auditText = String.format("[%s] Method '%s' called by user '%s'", timestamp, methodName, username);
            System.out.println(auditText);
            AuditUtil.write(auditText);
        }
        return joinPoint.proceed();
    }

    private Optional<HttpServletRequest> getRequestParameter(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                return Optional.of((HttpServletRequest) arg);
            }
        }
        return Optional.empty();
    }

    private Optional<User> getUserParameter(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof User) {
                return Optional.of((User) arg);
            }
        }
        return Optional.empty();
    }
}
