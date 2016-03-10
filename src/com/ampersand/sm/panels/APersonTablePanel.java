package com.ampersand.sm.panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import com.ampersand.lcu.db.MySQLDatabase;
import com.ampersand.lcu.gui.component.button.HighlightButton;
import com.ampersand.lcu.gui.component.field.TextValidationField;
import com.ampersand.lcu.gui.font.FontManager;
import com.ampersand.lcu.validator.Validator;
import com.ampersand.mailr.MaileR;

public abstract class APersonTablePanel extends ABasicTablePanel {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = 2016607390494446315L;

	protected HighlightButton m_contact_button;

	/*
	 * Methods
	 */

	// CONSTRUCTORS

	public APersonTablePanel(MySQLDatabase database) {

		super(database);

		// NORTH [EMPTY]

		// EAST [EMPTY]

		// WEST [EMPTY]

		// CENTER [X]

		// SOUTH [X]
	}

	// INITALIZATIONS

	@Override
	public void initActions() {

		super.initActions();

		m_contact_button = new HighlightButton(new ImageIcon("res/icon/action/business_contact-32.png"));
		m_contact_button.addActionListener(this);
		m_contact_button.setToolTipText("Contacter");

		m_actions_pane.add(m_contact_button);
	}

	// RE-IMPLEMENTED METHODS

	@Override
	public void actionPerformed(ActionEvent event) {

		String table_name = null;

		if (this instanceof CustomersPanel) {

			table_name = "Client";
		} else if (this instanceof SuppliersPanel) {

			table_name = "Fournisseur";
		}

		if (event.getSource().equals(m_add_button)) {

			final TextValidationField name_field = new TextValidationField(Validator.LAST_NAME);
			final TextValidationField address_field = new TextValidationField(Validator.ALGERIAN_POSTAL_ADDRESS);
			final TextValidationField phone_number_field = new TextValidationField(
					Validator.INTERNATIONAL_PHONE_NUMBER);
			final TextValidationField mail_field = new TextValidationField(Validator.E_MAIL_ADDRESS);

			final int state = JOptionPane.showOptionDialog(this,
					new Object[] { "Nom :", name_field, "Adresse (Facultative) :", address_field, "N° Téléphone :",
							phone_number_field, "E-Mail :", mail_field },

					"Ajout de " + table_name, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
					new ImageIcon("res/icon/action/add_property.png"), null, null);

			if (state == JOptionPane.OK_OPTION) {

				if (name_field.inputIsValid() && address_field.inputIsValid() && phone_number_field.inputIsValid()
						&& mail_field.inputIsValid()) {

					m_database_adapter.getDatabase()
							.executeUpdate("INSERT INTO " + table_name + " (Nom, Adresse, Telephone, E_Mail) VALUES ('"
									+ name_field.getText() + "','" + address_field.getText() + "','"
									+ phone_number_field.getText() + "','" + mail_field.getText() + "')");

					m_database_adapter.executeQuery("SELECT * FROM " + table_name);
				} else {

					JOptionPane.showMessageDialog(this,
							"Vérifiez que tous les champs sont remplis et que toutes les entrées sont valides.",
							"Attention!", JOptionPane.WARNING_MESSAGE);
				}
			}
		} else if (event.getSource().equals(m_modify_button)) {

			if (m_table.getRowCount() == 0) {

				JOptionPane.showMessageDialog(this, "La table est vide...", "Information",
						JOptionPane.INFORMATION_MESSAGE);
			} else {

				if (m_table.getSelectedRowCount() == 0) {

					JOptionPane.showMessageDialog(this, "Vous devez d'abord selectionner une entrée.", "Information",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (m_table.getSelectedRowCount() > 1) {

					JOptionPane.showMessageDialog(this,
							"Pour plus de sécurité, vous n'êtes autorisés a modifier qu'une seule entrée à la fois...",
							"Attention!", JOptionPane.WARNING_MESSAGE);
				} else {

					final TextValidationField name_field = new TextValidationField(Validator.LAST_NAME);
					final TextValidationField address_field = new TextValidationField(
							Validator.ALGERIAN_POSTAL_ADDRESS);
					final TextValidationField phone_number_field = new TextValidationField(
							Validator.INTERNATIONAL_PHONE_NUMBER);
					final TextValidationField mail_field = new TextValidationField(Validator.E_MAIL_ADDRESS);

					final int selected_id = Integer
							.parseInt(String.valueOf(m_database_adapter.getValueAt(m_table.getSelectedRow(), 0)));
					ResultSet result_set = null;

					if (this instanceof CustomersPanel) {

						result_set = m_database_adapter.getDatabase()
								.executeSelection("SELECT * FROM Client WHERE Id = " + selected_id);
					} else if (this instanceof SuppliersPanel) {

						result_set = m_database_adapter.getDatabase()
								.executeSelection("SELECT * FROM Fournisseur WHERE Id = " + selected_id);
					}

					try {

						result_set.next();

						name_field.setText(result_set.getString("Nom"));
						address_field.setText(result_set.getString("Adresse"));
						phone_number_field.setText(result_set.getString("Telephone"));
						mail_field.setText(result_set.getString("E_Mail"));
					} catch (final SQLException e) {

						e.printStackTrace();
					}

					final int state = JOptionPane.showOptionDialog(this,
							new Object[] { "Nom :", name_field, "Adresse (Facultative) :", address_field,
									"N° Téléphone :", phone_number_field, "E-Mail :", mail_field },

							"Modification de " + table_name, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
							new ImageIcon("res/icon/action/pencil.png"), null, null);

					if (state == JOptionPane.OK_OPTION) {

						if (name_field.inputIsValid() && address_field.inputIsValid()
								&& phone_number_field.inputIsValid() && mail_field.inputIsValid()) {

							m_database_adapter.getDatabase()
									.executeUpdate("UPDATE " + table_name + " SET " + "Nom = '" + name_field.getText()
											+ "', " + "Adresse = '" + address_field.getText() + "', " + "Telephone = '"
											+ phone_number_field.getText() + "', " + "E_Mail = '" + mail_field.getText()
											+ "' WHERE Id = " + selected_id);

							m_database_adapter.executeQuery("SELECT * FROM " + table_name);
						} else {

							JOptionPane.showMessageDialog(this,
									"Vérifiez que tous les champs sont remplis et que toutes les entrées sont valides.",
									"Attention!", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
		} else if (event.getSource().equals(m_delete_button)) {

			if (m_table.getRowCount() == 0) {

				JOptionPane.showMessageDialog(this, "La table est vide...", "Information",
						JOptionPane.INFORMATION_MESSAGE);
			} else {

				if (m_table.getSelectedRowCount() == 0) {

					JOptionPane.showMessageDialog(this, "Vous devez d'abord selectionner une entrée.", "Information",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (m_table.getSelectedRowCount() > 1) {

					JOptionPane.showMessageDialog(this,
							"Pour plus de sécurité, vous n'êtes autorisés a supprimer qu'une seule entrée à la fois...",
							"Attention!", JOptionPane.WARNING_MESSAGE);
				} else {

					final int state = JOptionPane.showConfirmDialog(this,
							"Voulez-vous vraiment supprimer cette entrée ? \nSi vous continuer la perte de celle-ci sera définitive.",
							"Suppréssion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
							new ImageIcon("res/icon/action/delete_property.png"));

					if (state == JOptionPane.YES_OPTION) {

						final int selected_id = Integer
								.parseInt(String.valueOf(m_database_adapter.getValueAt(m_table.getSelectedRow(), 0)));

						m_database_adapter.getDatabase()
								.executeUpdate("DELETE FROM " + table_name + " WHERE Id = " + selected_id);
						m_database_adapter.executeQuery("SELECT * FROM " + table_name);
					}
				}
			}
		} else if (event.getSource().equals(m_search_button)) {

			if (m_table.getRowCount() == 0) {

				JOptionPane.showMessageDialog(this, "La table est vide...", "Information",
						JOptionPane.INFORMATION_MESSAGE);
			} else {

				if (this instanceof CustomersPanel || this instanceof SuppliersPanel) {

					final TextValidationField search_field = new TextValidationField(Validator.UNSIGNED_INTEGER);
					search_field.setFont(FontManager.CENTURY_GOTHIC_16);
					search_field.setPreferredSize(new Dimension(250, 50));

					final JRadioButton id_button = new JRadioButton("Id", true);
					id_button.addActionListener(event1 -> search_field.setValidator(Validator.UNSIGNED_INTEGER));

					final JRadioButton name_button = new JRadioButton("Nom");
					name_button.addActionListener(event1 -> search_field.setValidator(Validator.LAST_NAME));

					final JRadioButton address_button = new JRadioButton("Adresse");
					address_button
							.addActionListener(event1 -> search_field.setValidator(Validator.ALGERIAN_POSTAL_ADDRESS));

					final JRadioButton phone_number_button = new JRadioButton("N° Téléphone");
					phone_number_button.addActionListener(
							event1 -> search_field.setValidator(Validator.INTERNATIONAL_PHONE_NUMBER));

					final JRadioButton mail_button = new JRadioButton("E-Mail");
					mail_button.addActionListener(event1 -> search_field.setValidator(Validator.E_MAIL_ADDRESS));

					final ButtonGroup button_group = new ButtonGroup();
					button_group.add(id_button);
					button_group.add(name_button);
					button_group.add(address_button);
					button_group.add(phone_number_button);
					button_group.add(mail_button);

					final int state = JOptionPane.showOptionDialog(this, new Object[] { search_field, id_button,
							name_button, address_button, phone_number_button, mail_button },

							"Recherche", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
							new ImageIcon("res/icon/action/search.png"), null, null);

					if (state == JOptionPane.OK_OPTION) {

						boolean found = false;

						if (id_button.isSelected()) {

							for (int i = 0; i < m_table.getRowCount() && found == false; i++) {

								if (String.valueOf(m_table.getModel().getValueAt(i, 0))
										.equals(search_field.getText())) {

									m_table.getSelectionModel().setSelectionInterval(i, i);

									found = true;
								}
							}
						} else if (name_button.isSelected()) {

							for (int i = 0; i < m_table.getRowCount() && found == false; i++) {

								if (String.valueOf(m_table.getModel().getValueAt(i, 1))
										.equals(search_field.getText())) {

									m_table.getSelectionModel().setSelectionInterval(i, i);

									found = true;
								}
							}
						} else if (address_button.isSelected()) {

							for (int i = 0; i < m_table.getRowCount() && found == false; i++) {

								if (String.valueOf(m_table.getModel().getValueAt(i, 2))
										.equals(search_field.getText())) {

									m_table.getSelectionModel().setSelectionInterval(i, i);

									found = true;
								}
							}
						} else if (phone_number_button.isSelected()) {

							for (int i = 0; i < m_table.getRowCount() && found == false; i++) {

								if (String.valueOf(m_table.getModel().getValueAt(i, 3))
										.equals(search_field.getText())) {

									m_table.getSelectionModel().setSelectionInterval(i, i);

									found = true;
								}
							}
						} else if (mail_button.isSelected()) {

							for (int i = 0; i < m_table.getRowCount() && found == false; i++) {

								if (String.valueOf(m_table.getModel().getValueAt(i, 4))
										.equals(search_field.getText())) {

									m_table.getSelectionModel().setSelectionInterval(i, i);

									found = true;
								}
							}
						}

						if (!found) {

							JOptionPane.showMessageDialog(this, "L'entrée recherchée est introuvable!", "Information",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			}
		} else if (event.getSource().equals(m_contact_button)) {

			if (m_table.getRowCount() == 0) {

				JOptionPane.showMessageDialog(this, "La table est vide...", "Information",
						JOptionPane.INFORMATION_MESSAGE);
			} else {

				if (m_table.getSelectedRowCount() == 0) {

					JOptionPane.showMessageDialog(this, "Vous devez d'abord selectionner une entrée.", "Information",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (m_table.getSelectedRowCount() > 1) {

					JOptionPane.showMessageDialog(this, "Vous ne pouvez pas contacter plusieurs personnes à la fois...",
							"Attention!", JOptionPane.WARNING_MESSAGE);
				} else {

					String table = null;

					if (this instanceof CustomersPanel) {

						table = "Client";
					} else if (this instanceof SuppliersPanel) {

						table = "Fournisseur";
					}

					final ResultSet result_set = m_database_adapter.getDatabase()
							.executeSelection("SELECT E_Mail FROM " + table + " WHERE Id = "
									+ String.valueOf(m_database_adapter.getValueAt(m_table.getSelectedRow(), 0)));

					String recipient_address = null;

					try {

						result_set.next();
						recipient_address = result_set.getString("E_Mail");
					} catch (final SQLException e) {

						e.printStackTrace();
					}

					final MaileR mailer = new MaileR();
					mailer.goToMainPane("amp@hotmail.com", "xxxxxxxx", recipient_address); // TODO:
																							// Put
																							// address
																							// and
																							// password
																							// here
					mailer.setVisible(true);
				}
			}
		}
	}
}
