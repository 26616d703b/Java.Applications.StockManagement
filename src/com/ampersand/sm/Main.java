package com.ampersand.sm;

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) { // TODO: don't forget to configure
												// the db

		SwingUtilities.invokeLater(() -> {

			final LoginDialog login_dialog = new LoginDialog();
			login_dialog.setVisible(true);
		});
	}
}
