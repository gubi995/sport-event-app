package hu.szeged.sporteventapp.ui.validator;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

public class PasswordValidator implements Validator<String> {
	@Override
	public ValidationResult apply(String password, ValueContext valueContext) {
		if (password.length() > 7 && containLowerCharacter(password)
				&& containUpperCharacter(password) && containNumber(password)) {
			return ValidationResult.ok();
		}
		else {
			return ValidationResult
					.error("The password length must greater than 7 character and "
							+ "contains number, upper and lower character.");
		}
	}

	private boolean containUpperCharacter(String password) {
		return password.matches(".*[A-Z]+.*");
	}

	private boolean containLowerCharacter(String password) {
		return password.matches(".*[a-z]+.*");
	}

	private boolean containNumber(String password) {
		return password.matches(".*\\d+.*");
	}
}
