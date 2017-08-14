package hu.szeged.sporteventapp.ui.listeners;

import java.io.Serializable;

import hu.szeged.sporteventapp.ui.events.RegistrationEvent;

public interface RegistrationListener extends Serializable {
	void onRegistration(RegistrationEvent event);
}