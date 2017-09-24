package hu.szeged.sporteventapp.ui.listeners;

import java.io.Serializable;

import hu.szeged.sporteventapp.ui.events.RegistrationEvent;

public interface RegistrationEventListener extends Serializable {

	void onRegistration(RegistrationEvent event);
}