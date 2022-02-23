package metro.validation;

import metro.repository.MetroRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public record ExistingLineValidator(MetroRepository metro) implements ConstraintValidator<ExistingLine, String> {

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return metro().getSchema().containsKey(value);
    }
}
