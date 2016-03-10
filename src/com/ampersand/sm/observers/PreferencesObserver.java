package com.ampersand.sm.observers;

import java.awt.Color;
import java.awt.Font;

public interface PreferencesObserver {

	public void update(boolean is_cell_editable, Color cell_color, Color cell_selection_color, int cell_size);

	public void update(Font cell_font, Color cell_font_color);
}
