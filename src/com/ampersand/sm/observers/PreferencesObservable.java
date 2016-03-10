package com.ampersand.sm.observers;

import java.awt.Color;
import java.awt.Font;

public interface PreferencesObservable {

	public void addObserver(PreferencesObserver observer);

	public void notifyObservers(boolean is_cell_editable, Color cell_color, Color cell_selection_color, int cell_size);

	public void notifyObservers(Font cell_font, Color cell_font_color);

	public void removeObservers();
}
