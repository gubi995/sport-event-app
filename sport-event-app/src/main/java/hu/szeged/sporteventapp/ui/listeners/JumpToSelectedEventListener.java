package hu.szeged.sporteventapp.ui.listeners;

import java.io.Serializable;

import hu.szeged.sporteventapp.ui.events.JumpToSelectedSportEvent;

public interface JumpToSelectedEventListener extends Serializable {

	void onJump(JumpToSelectedSportEvent event);
}
