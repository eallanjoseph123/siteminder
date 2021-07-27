package com.siteminder.validator;

import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListEmailConstraintValidator implements ConstraintValidator<ListEmailConstraint, List<String>> {

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	@Override public boolean isValid(List<String> emails, ConstraintValidatorContext constraintValidatorContext) {
		if(CollectionUtils.isEmpty(emails)) return true;

		for(String email : emails){
			Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
			if(!matcher.find()){
				return false;
			}
		}
		return true;
	}
}
