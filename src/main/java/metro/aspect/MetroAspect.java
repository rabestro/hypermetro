package metro.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;

@Aspect
@Component
public class MetroAspect {
    private static final System.Logger LOGGER = System.getLogger(MetroAspect.class.getName());

    @Around("Pointcuts.allCommands()")
    public Object aroundAddingAdvice(ProceedingJoinPoint joinPoint) {
        var signature = (MethodSignature) joinPoint.getSignature();
        LOGGER.log(DEBUG, "Metro command \"{0}\" started.", signature.getName());
        Arrays.stream(joinPoint.getArgs())
                .forEach(arg -> LOGGER.log(DEBUG, "---> {0}", arg));

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            LOGGER.log(WARNING, e.getMessage(), e);
        }

        LOGGER.log(DEBUG, "Metro command \"{0}\" processed", signature.getName());
        return result;
    }
}
