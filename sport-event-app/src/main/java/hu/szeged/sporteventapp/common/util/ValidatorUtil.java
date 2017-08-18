package hu.szeged.sporteventapp.common.util;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;

public class ValidatorUtil {
	public static void addValidator(Button button, Validator validator, AbstractField field) {
		field.addValueChangeListener(event -> {
			ValidationResult result = validator.apply(event.getValue(),
					new ValueContext(field));

			if (result.isError()) {
				UserError error = new UserError(result.getErrorMessage());
				field.setComponentError(error);
				button.setEnabled(false);
			}
			else {
				field.setComponentError(null);
				button.setEnabled(true);
			}
		});
	}
}
