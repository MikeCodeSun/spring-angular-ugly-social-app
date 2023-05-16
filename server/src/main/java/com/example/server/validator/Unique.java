package com.example.server.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Unique  {
  String message();
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
