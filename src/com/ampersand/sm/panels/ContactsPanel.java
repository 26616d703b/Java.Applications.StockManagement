package com.ampersand.sm.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.ampersand.lcu.gui.component.button.HighlightButton;

public class ContactsPanel extends ABasicPanel implements ActionListener {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = -7663132519548366703L;

	private final HighlightButton m_facebook_button;
	private final HighlightButton m_twitter_button;
	private final HighlightButton m_google_button;
	private final HighlightButton m_mail_button;

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public ContactsPanel() {

		// NORTH

		m_mail_button = new HighlightButton(new ImageIcon("res/icons/contacts/outlook-128.png"));
		m_mail_button.addActionListener(this);

		add(m_mail_button, BorderLayout.NORTH);

		// EAST

		m_twitter_button = new HighlightButton(new ImageIcon("res/icons/contacts/twitter-128.png"));
		m_twitter_button.addActionListener(this);

		add(m_twitter_button, BorderLayout.EAST);

		// WEST

		m_google_button = new HighlightButton(new ImageIcon("res/icons/contacts/google_plus-128.png"));
		m_google_button.addActionListener(this);

		add(m_google_button, BorderLayout.WEST);

		// CENTER

		// SOUTH

		m_facebook_button = new HighlightButton(new ImageIcon("res/icons/contacts/facebook-128.png"));
		m_facebook_button.addActionListener(this);

		add(m_facebook_button, BorderLayout.SOUTH);
	}

	// RE-IMPLEMENTED METHODS

	@Override
	public void actionPerformed(ActionEvent event) {

		final Runtime runtime = Runtime.getRuntime();
		String command = "cmd /c start ";

		if (event.getSource().equals(m_facebook_button)) {

			command += "https://www.facebook.com/";
		} else if (event.getSource().equals(m_twitter_button)) {

			command += "https://twitter.com/";
		} else if (event.getSource().equals(m_google_button)) {

			command += "https://plus.google.com/";
		} else if (event.getSource().equals(m_mail_button)) {

			command += "https://outlook.com/";
		}

		try {

			runtime.exec(command);
		} catch (final IOException e) {

			e.printStackTrace();
		}
	}
}
