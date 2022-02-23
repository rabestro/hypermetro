package metro.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NotBlank
@Constraint(validatedBy = {})
public @interface MetroStation {
    String message() default "must exist on metro line";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
