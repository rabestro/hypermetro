package metro.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank
@Documented
@ReportAsSingleViolation
@Constraint(validatedBy = {})
public @interface MetroStation {
    String message() default "the name of metro station should not be empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
