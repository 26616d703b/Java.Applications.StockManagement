package com.ampersand.sm.panels;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.ampersand.lcu.gui.component.panel.image.ImagePane;

public class HomePanel extends ImagePane {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = 2377076632170787987L;

	private static Image m_background = new ImageIcon("res/images/home.png").getImage();

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public HomePanel() {

		super(m_background);
	}
}
