package com.ampersand.sm.panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import com.ampersand.lcu.db.MySQLDatabase;
import com.ampersand.lcu.gui.component.field.TextValidationField;
import com.ampersand.lcu.gui.font.FontManager;
import com.ampersand.lcu.validator.Validator;

public class StockPanel extends ABasicTablePanel {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = 4959440324496197428L;

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public StockPanel(MySQLDatabase base) {

		super(base);

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

		if (event.getSource().equals(m_add_button)) {

			final TextValidationField name_field = new TextValidationField(Validator.ALPHA_NUMERIC);
			final TextValidationField price_field = new TextValidationField(Validator.UNSIGNED_FLOAT);
			final TextValidationField quantity_field = new TextValidationField(Validator.UNSIGNED_INTEGER);

			final JTextArea description_area = new JTextArea();
			description_area.setLineWrap(true);
			description_area.setPreferredSize(new Dimension(300, 100));

			final int state = JOptionPane.showOptionDialog(this, new Object[] { "Nom :", name_field, "Prix TTC :",
					price_field, "Quantité", quantity_field, "Description :", description_area },

					"Ajout de produit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
					new ImageIcon("res/icons/actions/add_property.png"), null, null);

			if (state == JOptionPane.OK_OPTION) {

				if (name_field.inputIsValid() && price_field.inputIsValid() && quantity_field.inputIsValid()) {

					description_area.getText().replaceAll("'", "\'");

					m_database_adapter.getDatabase()
							.executeUpdate("INSERT INTO Produit (Nom, Prix_TTC, Quantite, Description) VALUES ('"
									+ name_field.getText() + "'," + Double.parseDouble(price_field.getText()) + ","
									+ Integer.parseInt(quantity_field.getText()) + ",'" + description_area.getText()
									+ "')");

					m_database_adapter.executeQuery("SELECT * FROM Produit");
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

					final TextValidationField name_field = new TextValidationField(Validator.ALPHA_NUMERIC);
					final TextValidationField price_field = new TextValidationField(Validator.UNSIGNED_FLOAT);
					final TextValidationField quantity_field = new TextValidationField(Validator.UNSIGNED_INTEGER);

					final JTextArea description_area = new JTextArea();
					description_area.setLineWrap(true);
					description_area.setPreferredSize(new Dimension(300, 100));

					final int selected_id = Integer
							.parseInt(String.valueOf(m_database_adapter.getValueAt(m_table.getSelectedRow(), 0)));
					final ResultSet result_set = m_database_adapter.getDatabase()
							.executeSelection("SELECT * FROM Produit WHERE Id = " + selected_id);

					try {

						result_set.next();

						name_field.setText(result_set.getString("Nom"));
						price_field.setText(result_set.getString("Prix_TTC"));
						quantity_field.setText(result_set.getString("Quantite"));
						description_area.setText(result_set.getString("Description"));
					} catch (final SQLException e) {

						e.printStackTrace();
					}

					final int state = JOptionPane.showOptionDialog(this, new Object[] { "Nom :", name_field,
							"Prix TTC :", price_field, "Quantité", quantity_field, "Description :", description_area },

							"Modification de produit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
							new ImageIcon("res/icons/actions/pencil.png"), null, null);

					if (state == JOptionPane.OK_OPTION) {

						if (name_field.inputIsValid() && price_field.inputIsValid() && quantity_field.inputIsValid()) {

							description_area.getText().replaceAll("'", "\'");

							m_database_adapter.getDatabase()
									.executeUpdate("UPDATE Produit SET " + "Nom = '" + name_field.getText() + "', "
											+ "Prix_TTC = " + price_field.getText() + ", " + "Quantite = "
											+ quantity_field.getText() + ", " + "Description = '"
											+ description_area.getText() + "' WHERE Id = " + selected_id);

							m_database_adapter.executeQuery("SELECT * FROM Produit");
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
							new ImageIcon("res/icons/actions/delete_property.png"));

					if (state == JOptionPane.YES_OPTION) {

						final int selected_id = Integer
								.parseInt(String.valueOf(m_database_adapter.getValueAt(m_table.getSelectedRow(), 0)));

						m_database_adapter.getDatabase().executeUpdate("DELETE FROM Produit WHERE Id = " + selected_id);
						m_database_adapter.executeQuery("SELECT * FROM Produit");
					}
				}
			}
		} else if (event.getSource().equals(m_search_button)) {

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

					final TextValidationField search_field = new TextValidationField(Validator.UNSIGNED_INTEGER);
					search_field.setFont(FontManager.CENTURY_GOTHIC_16);
					search_field.setPreferredSize(new Dimension(250, 50));

					final JRadioButton id_button = new JRadioButton("Id", true);
					id_button.addActionListener(event1 -> search_field.setValidator(Validator.UNSIGNED_INTEGER));

					final JRadioButton name_button = new JRadioButton("Nom");
					name_button.addActionListener(event1 -> search_field.setValidator(Validator.ALPHA_NUMERIC));

					final JRadioButton price_button = new JRadioButton("Prix");
					price_button.addActionListener(event1 -> search_field.setValidator(Validator.UNSIGNED_FLOAT));

					final JRadioButton quantity_button = new JRadioButton("Quantité");
					quantity_button.addActionListener(event1 -> search_field.setValidator(Validator.UNSIGNED_INTEGER));

					final JRadioButton description_button = new JRadioButton("Description");
					description_button.addActionListener(event1 -> search_field.setValidator(Validator.NO_RESTRICTION));

					final ButtonGroup button_group = new ButtonGroup();
					button_group.add(id_button);
					button_group.add(name_button);
					button_group.add(price_button);
					button_group.add(quantity_button);
					button_group.add(description_button);

					final int state = JOptionPane.showOptionDialog(this,
							new Object[] { search_field, id_button, name_button, price_button, quantity_button,
									description_button },
							"Recherche", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
							new ImageIcon("res/icons/actions/search.png"), null, null);

					if (state == JOptionPane.OK_OPTION) {

						if (id_button.isSelected()) {

							for (int i = 0; i < m_table.getRowCount(); i++) {

								if (String.valueOf(m_table.getModel().getValueAt(i, 0))
										.equals(search_field.getText())) {

									m_table.getSelectionModel().setSelectionInterval(i, i);
									break;
								}
							}
						} else if (name_button.isSelected()) {

							for (int i = 0; i < m_table.getRowCount(); i++) {

								if (String.valueOf(m_table.getModel().getValueAt(i, 1))
										.equals(search_field.getText())) {

									m_table.getSelectionModel().setSelectionInterval(i, i);
									break;
								}
							}
						} else if (price_button.isSelected()) {

							for (int i = 0; i < m_table.getRowCount(); i++) {

								if (String.valueOf(m_table.getModel().getValueAt(i, 2))
										.equals(search_field.getText())) {

									m_table.getSelectionModel().setSelectionInterval(i, i);
									break;
								}
							}
						} else if (quantity_button.isSelected()) {

							for (int i = 0; i < m_table.getRowCount(); i++) {

								if (String.valueOf(m_table.getModel().getValueAt(i, 3))
										.equals(search_field.getText())) {

									m_table.getSelectionModel().setSelectionInterval(i, i);
									break;
								}
							}
						} else if (description_button.isSelected()) {

							for (int i = 0; i < m_table.getRowCount(); i++) {

								if (String.valueOf(m_table.getModel().getValueAt(i, 4))
										.equals(search_field.getText())) {

									m_table.getSelectionModel().setSelectionInterval(i, i);
									break;
								}
							}
						}
					}
				}
			}
		}
	}
}
