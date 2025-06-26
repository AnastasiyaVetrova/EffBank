package com.example.bankcards.util.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NoBlankSpacesValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoBlankSpaces {

    String message() default "Поле не должно быть пустым или состоять только из пробелов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
