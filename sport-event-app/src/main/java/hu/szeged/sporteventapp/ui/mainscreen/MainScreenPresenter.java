package hu.szeged.sporteventapp.ui.mainscreen;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;

@UIScope
@SpringComponent
public class MainScreenPresenter {

	private final SpringViewProvider viewProvider;
	private final MainScreen mainScreen;

	@Autowired
	public MainScreenPresenter(SpringViewProvider viewProvider, MainScreen mainScreen) {
		this.viewProvider = viewProvider;
		this.mainScreen = mainScreen;
	}

	public MainScreen getView() {
		return mainScreen;
	}
}
