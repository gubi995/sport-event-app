package hu.szeged.sporteventapp.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.navigator.SpringViewProvider;

@PrototypeScope
@SpringComponent
public class MainController {

	private final SpringViewProvider viewProvider;
	private final MainScreen mainScreen;

	@Autowired
	public MainController(SpringViewProvider viewProvider, MainScreen mainScreen) {
		this.viewProvider = viewProvider;
		this.mainScreen = mainScreen;
	}

	public MainScreen getView() {
		return mainScreen;
	}
}
