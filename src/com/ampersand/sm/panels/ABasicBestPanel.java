package com.ampersand.sm.panels;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.ampersand.lcu.db.MySQLDatabase;
import com.ampersand.lcu.gui.component.panel.menu.DirectedMenuPane;

public class ABasicBestPanel extends ABasicPanel implements ComponentListener {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = 5912199535401521340L;

	protected MySQLDatabase m_database;

	// GUI

	protected DirectedMenuPane m_directed_pane;

	/*
	 * Methods
	 */

	public ABasicBestPanel(MySQLDatabase database) {

		m_database = database;

		// NORTH [EMPTY]

		// EAST [EMPTY]

		// WEST

		// CENTER

		initCharts();

		add(m_directed_pane, BorderLayout.CENTER);

		// SOUTH [EMPTY]

		addComponentListener(this);
	}

	// RE-IMPLEMENTED METHODS

	@Override
	public void componentHidden(ComponentEvent event) {
	}

	@Override
	public void componentMoved(ComponentEvent event) {
	}

	@Override
	public void componentResized(ComponentEvent event) {
	}

	@Override
	public void componentShown(ComponentEvent event) {

		remove(m_directed_pane);

		initCharts();

		add(m_directed_pane, BorderLayout.CENTER);

		validate();
	}

	// IMPLEMENTED METHODS

	public void initCharts() {

		m_directed_pane = new DirectedMenuPane();
	}
}
