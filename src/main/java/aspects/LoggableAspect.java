package aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Aspect that creates code to write logs for methods
 * */
@Aspect
public class LoggableAspect {
    @Pointcut("@annotation(by.matvey.lshkn.annotation.Loggable) && execution(* *(..))")
    public void annotatedByLoggable() {}

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("Calling method " + proceedingJoinPoint.getSignature());
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis() - start;
        System.out.println("Execution of method " + proceedingJoinPoint.getSignature() +
                           " finished. Execution time is " + end + " ms.");
        return result;
    }
}
