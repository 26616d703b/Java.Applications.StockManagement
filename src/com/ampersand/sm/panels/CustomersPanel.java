package com.ampersand.sm.panels;

import java.awt.event.ActionEvent;

import com.ampersand.lcu.db.MySQLDatabase;

public class CustomersPanel extends APersonTablePanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 4211408399568982745L;

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public CustomersPanel(MySQLDatabase database) {

		super(database);

		// NORTH [EMPTY]

		// EAST [EMPTY]

		// WEST [EMPTY]

		// CENTER [X]

		// SOUTH [X]
	}

	// INITALIZATIONS

	// RE-IMPLEMENTED METHODS

	@Override
	public void actionPerformed(ActionEvent event) {

		super.actionPerformed(event);
	}

	/*
	 * Attributes
	 */
}
