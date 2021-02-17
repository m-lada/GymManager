package pl.maciek.app.user.validators;

import pl.maciek.app.user.User;

import static java.lang.annotation.ElementType.*;

import javax.validation.constraints.*;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import java.lang.annotation.Documented;

@Email
@Pattern(regexp = ".+@.+\\..+", message = "{pl.maciek.app.user.User.userName.ExtendedEmailValidator}")
@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ExtendedEmailValidator {
    String message() default "{pl.maciek.app.user.User.userName.ExtendedEmailValidator}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}