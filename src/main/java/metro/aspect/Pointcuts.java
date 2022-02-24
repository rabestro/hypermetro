package metro.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(public * metro.shell.MetroCommands.*(..))")
    public void allCommands() {
    }
}