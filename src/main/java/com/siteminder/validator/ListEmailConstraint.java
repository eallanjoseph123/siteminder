package com.siteminder.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = ListEmailConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListEmailConstraint {
	String message() default "The input list cannot contain invalid emails";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
