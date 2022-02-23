package metro.validation;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.Assert;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.lang.reflect.InvocationTargetException;

import static java.lang.System.Logger.Level.INFO;

public class ExistingStationValidator implements ConstraintValidator<ExistingStation, Object> {
    private static final System.Logger LOGGER = System.getLogger(ExistingStationValidator.class.getName());

    @Override
    public void initialize(final ExistingStation constraintAnnotation) {
        LOGGER.log(INFO, "initialize");
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        LOGGER.log(INFO, "{0} {1}", value, context);
        try {
            var info = PropertyUtils.describe(value);
            LOGGER.log(INFO, "object: {0}", info);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return true;
    }
}
