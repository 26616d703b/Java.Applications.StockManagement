package com.ampersand.sm.panels;

import java.awt.event.ActionEvent;

import com.ampersand.lcu.db.MySQLDatabase;

public class SuppliersPanel extends APersonTablePanel {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = 4798935711838850106L;

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public SuppliersPanel(MySQLDatabase base) {

		super(base);

		// NORTH [EMPTY]

		// EAST [EMPTY]

		// WEST [EMPTY]

		// CENTER [X]

		// SOUTH [X]

	}

	// RE-IMPLEMENTED METHODS

	@Override
	public void actionPerformed(ActionEvent event) {

		super.actionPerformed(event);
	}
}
