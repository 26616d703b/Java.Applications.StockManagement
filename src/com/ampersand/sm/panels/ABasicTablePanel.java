package com.ampersand.sm.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import com.ampersand.lcu.db.MySQLDatabase;
import com.ampersand.lcu.db.MySQLDatabaseAdapter;
import com.ampersand.lcu.gui.color.ColorPalette;
import com.ampersand.lcu.gui.component.button.HighlightButton;
import com.ampersand.lcu.gui.component.scrollbar.FlatScrollBarUI;
import com.ampersand.lcu.io.properties.PropertiesManager;
import com.ampersand.sm.observers.DatabaseObserver;
import com.ampersand.sm.observers.PreferencesObserver;

public abstract class ABasicTablePanel extends ABasicPanel
		implements ActionListener, DatabaseObserver, PreferencesObserver {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = -7117563147731545614L;

	protected MySQLDatabaseAdapter m_database_adapter;

	// GUI

	protected JPanel m_actions_pane;
	protected HighlightButton m_add_button;
	protected HighlightButton m_modify_button;
	protected HighlightButton m_delete_button;
	protected HighlightButton m_search_button;

	protected JTable m_table;
	protected TableCellRenderer m_table_cell_renderer;

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public ABasicTablePanel(MySQLDatabase database) {

		setLayout(new BorderLayout());

		// NORTH [EMPTY]

		// EAST

		initActions();

		// WEST [EMPTY]

		// CENTER

		initTable(database);

		// SOUTH [EMPTY]

	}

	// ACCESSORS and MUTATORS

	public MySQLDatabaseAdapter getDatabaseAdapter() {

		return m_database_adapter;
	}

	// INISIALIZATIONS

	public void initTable(MySQLDatabase database) {

		m_database_adapter = new MySQLDatabaseAdapter(database);
		m_database_adapter.setCellEditable(true);

		if (this instanceof CustomersPanel) {

			m_database_adapter.executeQuery("SELECT * FROM Client");
		} else if (this instanceof SuppliersPanel) {

			m_database_adapter.executeQuery("SELECT * FROM Fournisseur");
		} else if (this instanceof StockPanel) {

			m_database_adapter.executeQuery("SELECT * FROM Produit");
		}

		m_table = new JTable(m_database_adapter);
		m_table.setAutoCreateRowSorter(true);
		m_table.setBackground(new Color(Integer.parseInt(PropertiesManager.loadProperty("stock_management.prefs",
				"cell_color", String.valueOf(ColorPalette.WHITE.getRGB())))));

		((MySQLDatabaseAdapter) m_table.getModel()).setCellEditable(
				Boolean.valueOf(PropertiesManager.loadProperty("stock_management.prefs", "is_cell_editable", "false")));

		m_table_cell_renderer = m_table.getDefaultRenderer(String.class);

		m_table.setDefaultRenderer(Integer.class, (table, value, isSelected, hasFocus, row, column) -> {

			final Component component = m_table_cell_renderer.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);
			((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);

			return component;
		});

		m_table.setFont(
				new Font(PropertiesManager.loadProperty("stock_management.prefs", "font_type", "Calibri"), Font.PLAIN,
						Integer.parseInt(PropertiesManager.loadProperty("stock_management.prefs", "font_size", "15"))));
		m_table.setForeground(new Color(Integer.parseInt(PropertiesManager.loadProperty("stock_management.prefs",
				"font_color", String.valueOf(ColorPalette.BLACK.getRGB())))));
		m_table.setRowHeight(
				Integer.parseInt(PropertiesManager.loadProperty("stock_management.prefs", "cell_size", "50")));
		m_table.setSelectionBackground(
				new Color(Integer.parseInt(PropertiesManager.loadProperty("stock_management.prefs",
						"cell_selection_color", String.valueOf(ColorPalette.DEEP_SKY_BLUE.getRGB())))));

		final JScrollPane scroll_pane = new JScrollPane(m_table);
		scroll_pane.getVerticalScrollBar().setUI(new FlatScrollBarUI());

		add(scroll_pane, BorderLayout.CENTER);
	}

	public void initActions() {

		m_add_button = new HighlightButton(new ImageIcon("res/icon/action/add_property-32.png"));
		m_add_button.addActionListener(this);
		m_add_button.setToolTipText("Ajouter");

		m_modify_button = new HighlightButton(new ImageIcon("res/icon/action/edit_property-32.png"));
		m_modify_button.addActionListener(this);
		m_modify_button.setToolTipText("Modifier");

		m_delete_button = new HighlightButton(new ImageIcon("res/icon/action/delete_property-32.png"));
		m_delete_button.addActionListener(this);
		m_delete_button.setToolTipText("Supprimer");

		m_search_button = new HighlightButton(new ImageIcon("res/icon/action/search-32.png"));
		m_search_button.addActionListener(this);
		m_search_button.setToolTipText("Rechercher");

		m_actions_pane = new JPanel(new GridLayout(1, 5));
		m_actions_pane.add(m_add_button);
		m_actions_pane.add(m_modify_button);
		m_actions_pane.add(m_delete_button);
		m_actions_pane.add(m_search_button);

		add(m_actions_pane, BorderLayout.SOUTH);
	}

	// RE-IMPLEMENTED METHODS

	// LISTENERS

	@Override
	public abstract void actionPerformed(ActionEvent event);

	// OBSERVERS

	@Override
	public void update(boolean closing_connections_to_database) {

		if (closing_connections_to_database) {

			if (m_database_adapter.getDatabase().isConnected()) {

				m_database_adapter.getDatabase().disconnect();
			}
		}
	}

	@Override
	public void update(boolean is_cell_editable, Color cell_color, Color cell_selection_color, int cell_size) {

		((MySQLDatabaseAdapter) m_table.getModel()).setCellEditable(is_cell_editable);

		m_table.setBackground(cell_color);
		m_table.setSelectionBackground(cell_selection_color);
		m_table.setRowHeight(cell_size);
	}

	@Override
	public void update(Font cell_font, Color cell_font_color) {

		m_table.setFont(cell_font);
		m_table.setForeground(cell_font_color);
	}
}
