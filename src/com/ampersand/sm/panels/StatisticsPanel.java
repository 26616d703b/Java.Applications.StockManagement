package com.ampersand.sm.panels;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import com.ampersand.lcu.db.MySQLDatabase;
import com.ampersand.lcu.gui.color.ColorPalette;
import com.ampersand.lcu.gui.component.panel.tab.PlasticTabbedPaneUI;
import com.ampersand.lcu.gui.font.FontManager;
import com.ampersand.sm.panels.statistics.BestArticlesPanel;
import com.ampersand.sm.panels.statistics.BestCustomersPanel;
import com.ampersand.sm.panels.statistics.BestSuppliersPanel;

public class StatisticsPanel extends ABasicPanel {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = -8061567946486220181L;

	private final JTabbedPane m_tabs;

	private final BestCustomersPanel m_best_customers_pane;
	private final BestSuppliersPanel m_best_suppliers_pane;
	private final BestArticlesPanel m_best_articles_pane;

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public StatisticsPanel(MySQLDatabase m_database) {

		m_best_customers_pane = new BestCustomersPanel(m_database);
		m_best_suppliers_pane = new BestSuppliersPanel(m_database);
		m_best_articles_pane = new BestArticlesPanel(m_database);

		m_tabs = new JTabbedPane(SwingConstants.BOTTOM);
		m_tabs.setFont(FontManager.CENTURY_GOTHIC_BOLD_18);
		m_tabs.setUI(new PlasticTabbedPaneUI(ColorPalette.YELLOW));

		m_tabs.addTab(" Clients", new ImageIcon("res/icons/statistics/user-48.png"), m_best_customers_pane);
		m_tabs.addTab(" Fournisseurs", new ImageIcon("res/icons/statistics/businessman-48.png"), m_best_suppliers_pane);
		m_tabs.addTab(" Produits", new ImageIcon("res/icons/statistics/filled_box-48.png"), m_best_articles_pane);

		add(m_tabs, BorderLayout.CENTER);
	}

	// ACCESSORS and MUTATORS

	public BestCustomersPanel getBestCustomersPane() {

		return m_best_customers_pane;
	}

	public BestSuppliersPanel getBestSuppliersPane() {

		return m_best_suppliers_pane;
	}

	public BestArticlesPanel getBestArticlesPane() {

		return m_best_articles_pane;
	}
}
