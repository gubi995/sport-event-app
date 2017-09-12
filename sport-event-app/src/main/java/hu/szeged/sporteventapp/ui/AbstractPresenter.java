package hu.szeged.sporteventapp.ui;

import java.io.Serializable;

import hu.szeged.sporteventapp.ui.views.AbstractView;

public class AbstractPresenter<V extends AbstractView> implements Serializable {
	protected V view;

	public void setView(V view) {
		this.view = view;
	}

	public V getView() {
		return view;
	}
}
