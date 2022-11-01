package com.huemap.backend.common.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LatitudeValidator implements ConstraintValidator<LatitudeValid, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        final String LATITUDE_REGEX =
            "^(\\+|-)?(?:90(?:(?:\\.0{1,13})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,13})?))$";

        if (value == null) {
            return false;
        }

        Pattern pattern = Pattern.compile(LATITUDE_REGEX);
        return pattern.matcher(String.valueOf(value)).find();
    }
}
