package com.ampersand.sm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.ampersand.lcu.db.MySQLDatabase;
import com.ampersand.lcu.gui.color.ColorPalette;
import com.ampersand.lcu.gui.component.button.HighlightButton;
import com.ampersand.lcu.gui.component.field.TextValidationField;
import com.ampersand.lcu.gui.component.panel.image.ImagePane;
import com.ampersand.lcu.gui.font.FontManager;
import com.ampersand.lcu.validator.Validator;

public class LoginDialog extends JDialog implements ActionListener {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = 1440173787305719239L;

	private final ImagePane m_image_pane;

	private JPanel m_components_pane;
	private JPanel m_actions_pane;

	private JLabel m_login_label;
	private JLabel m_username_label;
	private TextValidationField m_username_field;
	private JLabel m_password_label;
	private TextValidationField m_password_field;
	private HighlightButton m_submit_button;
	private HighlightButton m_cancel_button;

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public LoginDialog() {

		m_image_pane = new ImagePane("res/images/dialog_background.png");

		setUndecorated(true);
		setAlwaysOnTop(true);
		setContentPane(m_image_pane);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(m_image_pane.getBackgroundImage().getWidth(null), m_image_pane.getBackgroundImage().getHeight(null));
		setLocationRelativeTo(null);
		setModal(true);
		setTitle("Authentification");

		initComponents();
	}

	// INISIALIZATIONS

	public void initComponents() {

		// Username

		m_username_label = new JLabel("  Nom d'utilisateur  ");
		m_username_label.setHorizontalAlignment(SwingConstants.CENTER);
		m_username_label.setFont(FontManager.CENTURY_GOTHIC_16);
		m_username_label.setForeground(ColorPalette.WHITE);
		m_username_label.setSize(300, 20);

		m_username_field = new TextValidationField(Validator.LOGIN);
		m_username_field.setOpaque(true);

		// Password

		m_password_label = new JLabel("  Mot de passe ");
		m_password_label.setHorizontalAlignment(SwingConstants.CENTER);
		m_password_label.setFont(FontManager.CENTURY_GOTHIC_16);
		m_password_label.setForeground(ColorPalette.WHITE);

		m_password_field = new TextValidationField(Validator.WEAK_PASSWORD);
		m_password_field.setOpaque(true);

		// Actions

		m_submit_button = new HighlightButton(new ImageIcon("res/icons/login/login-32.png"));
		m_submit_button.addActionListener(this);
		m_submit_button.setToolTipText("Se connecter");

		m_cancel_button = new HighlightButton(new ImageIcon("res/icons/login/cancel-32.png"));
		m_cancel_button.addActionListener(this);
		m_cancel_button.setToolTipText("Annuler");

		m_image_pane.setLayout(new BorderLayout());

		m_components_pane = new JPanel();
		m_components_pane.setOpaque(false);
		m_components_pane.setLayout(new GridLayout(4, 1, 10, 10));
		m_components_pane.add(new JLabel());
		m_components_pane.add(m_username_field);

		m_components_pane.add(m_password_field);

		m_actions_pane = new JPanel(new GridLayout(1, 2, 10, 10));
		m_actions_pane.setOpaque(false);
		m_actions_pane.add(m_submit_button);
		m_actions_pane.add(m_cancel_button);

		m_components_pane.add(m_actions_pane);

		getContentPane().add(m_components_pane, BorderLayout.CENTER);

		m_login_label = new JLabel("Login", SwingConstants.CENTER);
		m_login_label.setFont(FontManager.CENTURY_GOTHIC_BOLD_30);
		m_login_label.setForeground(Color.BLACK);

		final JPanel left_pane = new JPanel(new GridLayout(4, 1, 10, 10));
		left_pane.add(m_login_label);
		left_pane.add(m_username_label);
		left_pane.add(m_password_label);
		left_pane.add(new JLabel());
		left_pane.setOpaque(false);

		getContentPane().add(left_pane, BorderLayout.WEST);

		final JPanel[] empty_pane = new JPanel[2];
		empty_pane[0] = new JPanel();
		empty_pane[0].setOpaque(false);
		empty_pane[1] = new JPanel();
		empty_pane[1].setOpaque(false);

		getContentPane().add(empty_pane[0], BorderLayout.EAST);
		getContentPane().add(empty_pane[1], BorderLayout.SOUTH);
	}

	// RE-IMPLEMENTED METHODS

	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource().equals(m_submit_button)) {

			final MySQLDatabase base = new MySQLDatabase("localhost:3306/stock", m_username_field.getText(),
					m_password_field.getText());

			if (base.connect()) {

				final MainWindow main_window = new MainWindow(base);
				main_window.setVisible(true);

				dispose();
			} else {

				JOptionPane.showMessageDialog(null, "Connexion impossible!", "Attention", JOptionPane.WARNING_MESSAGE);
			}
		} else if (event.getSource().equals(m_cancel_button)) {

			System.exit(0);
		}
	}
}
