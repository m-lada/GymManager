package pl.maciek.app.user.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Pattern(regexp = "(^[0-9]{2}[-][0-9]{3})", message = "{pl.maciek.app.user.User.postalCode.PostalCodeValidator}")
@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface PostalCodeValidator {
    String message() default "{pl.maciek.app.user.User.postalCode.PostalCodeValidator}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}