package metro.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingLineValidator.class)
@Target({METHOD, FIELD, PARAMETER})
public @interface ExistingLine {
    String message() default "must exist on metro schema";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
