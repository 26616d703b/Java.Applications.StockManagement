package com.ampersand.sm.panels;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;

import com.ampersand.lcu.gui.color.ColorPalette;
import com.ampersand.lcu.gui.font.FontManager;

public abstract class ABasicPanel extends JPanel {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = 6830690132203036240L;

	protected Font m_default_font;

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public ABasicPanel() {

		m_default_font = FontManager.CENTURY_GOTHIC_18;

		setBackground(ColorPalette.WHITE);
		setLayout(new BorderLayout());
	}
}
