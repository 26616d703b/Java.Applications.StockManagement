package com.ampersand.sm;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import com.ampersand.lcu.db.MySQLDatabase;
import com.ampersand.lcu.gui.color.ColorPalette;
import com.ampersand.lcu.gui.component.panel.tab.PlasticTabbedPaneUI;
import com.ampersand.lcu.gui.font.FontManager;
import com.ampersand.sm.observers.DatabaseObservable;
import com.ampersand.sm.observers.DatabaseObserver;
import com.ampersand.sm.panels.ContactsPanel;
import com.ampersand.sm.panels.CustomersPanel;
import com.ampersand.sm.panels.PreferencesPanel;
import com.ampersand.sm.panels.StatisticsPanel;
import com.ampersand.sm.panels.StockPanel;
import com.ampersand.sm.panels.SuppliersPanel;
import com.ampersand.sm.panels.ToolsPanel;

public class MainWindow extends JFrame implements DatabaseObservable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2481404072129106738L;

	/*
	 * Methods:
	 */

	// CONSTRUCTOR

	public MainWindow(MySQLDatabase base) {

		m_database = base;

		m_observers = new ArrayList<DatabaseObserver>();

		// Window properties

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent event) {

				notifyObservers(true);

				m_preferences_pane.savePreferences();
			}
		});

		getContentPane().setBackground(ColorPalette.WHITE);

		setSize(1080, 680);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("res/icons/package-48.png"));
		setTitle("Stock Management");
		setLocationRelativeTo(null);

		initialize();
	}

	// INITIALIZATIONS:

	public void initialize() {

		initMenu();
		initTabs();
	}

	public void initMenu() {

		m_file_menu = new JMenu("Fichier");
		m_file_menu.setMnemonic('f');

		m_file_menu.addSeparator();

		m_exit = new JMenuItem("Quitter", new ImageIcon("res/icons/menu/switch_off-32.png"));
		m_exit.addActionListener(new MenuBarListener());
		m_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
		m_exit.setMnemonic('q');

		m_file_menu.add(m_exit);

		m_about = new JMenuItem("À propos", new ImageIcon("res/icons/menu/about-32.png"));
		m_about.addActionListener(new MenuBarListener());
		m_about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_DOWN_MASK));
		m_about.setMnemonic('p');

		m_help_menu = new JMenu("?");
		m_help_menu.add(m_about);
		m_help_menu.setMnemonic('?');

		m_menu_bar = new JMenuBar();
		m_menu_bar.add(m_file_menu);
		m_menu_bar.add(m_help_menu);

		setJMenuBar(m_menu_bar);
	}

	public void initTabs() {

		// m_home_pane = new HomePane();

		m_customers_pane = new CustomersPanel(m_database);

		m_suppliers_pane = new SuppliersPanel(m_database);

		m_stock_pane = new StockPanel(m_database);

		m_statistics_pane = new StatisticsPanel(m_database);

		m_tools_pane = new ToolsPanel();

		addObserver(m_customers_pane);
		addObserver(m_suppliers_pane);
		addObserver(m_stock_pane);

		m_preferences_pane = new PreferencesPanel();
		m_preferences_pane.addObserver(m_customers_pane);
		m_preferences_pane.addObserver(m_suppliers_pane);
		m_preferences_pane.addObserver(m_stock_pane);

		m_contacts_pane = new ContactsPanel();

		m_tabs = new JTabbedPane(SwingConstants.NORTH);
		m_tabs.addTab(" Acceuil", new ImageIcon("res/icons/home/home-32.png"), new JPanel());
		m_tabs.addTab(" Clients", new ImageIcon("res/icons/home/user-32.png"), m_customers_pane);
		m_tabs.addTab(" Fournisseurs", new ImageIcon("res/icons/home/businessman-32.png"), m_suppliers_pane);
		m_tabs.addTab(" Stock", new ImageIcon("res/icons/home/box-32.png"), m_stock_pane);
		m_tabs.addTab(" Statistiques", new ImageIcon("res/icons/home/statistics-32.png"), m_statistics_pane);
		m_tabs.addTab(" Outils", new ImageIcon("res/icons/home/screwdriver-32.png"), m_tools_pane);
		m_tabs.addTab(" Préfèrences", new ImageIcon("res/icons/home/monitor-32.png"), m_preferences_pane);
		m_tabs.addTab(" Nous Contacter", new ImageIcon("res/icons/home/new_post-32.png"), m_contacts_pane);

		m_tabs.setFont(FontManager.CENTURY_GOTHIC_BOLD_18);
		m_tabs.setUI(new PlasticTabbedPaneUI(ColorPalette.GOLD));

		add(m_tabs);
	}

	// LISTENERS

	public class MenuBarListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {

			if (event.getSource().equals(m_exit)) {

				notifyObservers(true);

				System.exit(0);
			} else if (event.getSource().equals(m_about)) {

				JOptionPane.showMessageDialog(null, "&Stock_Management est un projet réalisé pour le fun!", "À propos",
						JOptionPane.INFORMATION_MESSAGE, new ImageIcon("res/icons/package-48.png"));
			}
		}
	}

	// RE-IMPLEMENTED METHODS

	@Override
	public void addObserver(DatabaseObserver observer) {

		m_observers.add(observer);
	}

	@Override
	public void notifyObservers(boolean closing_connections_to_database) {

		for (final DatabaseObserver observer : m_observers) {

			observer.update(closing_connections_to_database);
		}
	}

	@Override
	public void removeObservers() {

		m_observers = new ArrayList<DatabaseObserver>();
	}

	/*
	 * Attributes:
	 */

	private final MySQLDatabase m_database;
	private ArrayList<DatabaseObserver> m_observers;

	// GUI

	private JMenuBar m_menu_bar;

	private JMenu m_file_menu;
	private JMenuItem m_exit;

	private JMenu m_help_menu;
	private JMenuItem m_about;

	private JTabbedPane m_tabs;

	// private ImagePane m_home_pane;
	private CustomersPanel m_customers_pane;
	private SuppliersPanel m_suppliers_pane;
	private StockPanel m_stock_pane;
	private StatisticsPanel m_statistics_pane;
	private ToolsPanel m_tools_pane;
	private PreferencesPanel m_preferences_pane;
	private ContactsPanel m_contacts_pane;
}