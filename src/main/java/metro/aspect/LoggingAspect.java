package metro.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;
import static java.util.stream.Collectors.joining;

@Aspect
@Component
public class LoggingAspect {
    private static final System.Logger LOG = System.getLogger(LoggingAspect.class.getName());

    @Pointcut("within(metro.shell..*) && @annotation(org.springframework.shell.standard.ShellMethod)")
    public void shellMethod() {
    }

    @Before("shellMethod()")
    public void beforeCommand(JoinPoint joinPoint) {
        LOG.log(INFO, () -> {
            var parameters = Arrays.stream(joinPoint.getArgs())
                    .map(Object::toString)
                    .collect(joining(", ", "(", ")"));
            return command(joinPoint) + " " + parameters;
        });
    }

    @AfterReturning("shellMethod()")
    public void afterSuccessful(JoinPoint joinPoint) {
        LOG.log(INFO, () -> command(joinPoint) + " successful processed.");
    }

    @AfterThrowing("shellMethod()")
    public void afterThrowing(JoinPoint joinPoint) {
        LOG.log(WARNING, () -> command(joinPoint) + " fails.");
    }

    private String command(JoinPoint joinPoint) {
        var signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getName();
    }
}