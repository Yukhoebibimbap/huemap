package com.huemap.backend.common.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LongitudeValidator implements ConstraintValidator<LongitudeValid, Double> {

  @Override
  public boolean isValid(Double value, ConstraintValidatorContext context) {
    final String LONGITUDE_REGEX =
        "^(\\+|-)?(?:180(?:(?:\\.0{1,12})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,12})?))$";

    if (value == null) {
      return false;
    }

    Pattern pattern = Pattern.compile(LONGITUDE_REGEX);
    return pattern.matcher(String.valueOf(value)).find();
  }
}
