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
@Constraint(validatedBy = ExistingStationValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, PARAMETER})
public @interface ExistingStation {
    String message() default "must exist on metro line";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
