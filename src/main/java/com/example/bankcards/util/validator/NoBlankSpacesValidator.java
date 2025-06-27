package com.example.bankcards.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoBlankSpacesValidator implements ConstraintValidator<NoBlankSpaces, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        return value == null || !value.trim().isEmpty();
    }
}
