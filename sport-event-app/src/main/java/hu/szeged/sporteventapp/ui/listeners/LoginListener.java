package hu.szeged.sporteventapp.ui.listeners;

import java.io.Serializable;

import hu.szeged.sporteventapp.ui.events.LoginEvent;

public interface LoginListener extends Serializable {
	void onLogin(LoginEvent event);
}