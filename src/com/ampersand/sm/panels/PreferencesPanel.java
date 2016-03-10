package com.ampersand.sm.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ampersand.lcu.gui.color.ColorPalette;
import com.ampersand.lcu.gui.component.button.HighlightButton;
import com.ampersand.lcu.gui.font.FontManager;
import com.ampersand.lcu.io.properties.PropertiesManager;
import com.ampersand.sm.observers.PreferencesObservable;
import com.ampersand.sm.observers.PreferencesObserver;

public class PreferencesPanel extends ABasicPanel
		implements ActionListener, ChangeListener, ItemListener, PreferencesObservable {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = -6055019468676274378L;

	private ArrayList<PreferencesObserver> m_observers;

	// GUI

	private final JPanel m_general;
	private final JPanel m_display;
	private final JPanel m_cell;
	private final JPanel m_cell_display;
	private final JPanel m_cell_font;

	private final JLabel m_cell_size_label;
	private final JLabel m_cell_color_label;
	private final JLabel m_cell_selection_color_label;
	private final JLabel m_font_family_label;
	private final JLabel m_font_size_label;
	private final JLabel m_font_color_label;

	private final JCheckBox m_cell_editable;
	private final JSlider m_cell_size_slider;
	private final JButton m_cell_color_button;
	private final JButton m_cell_selection_color_button;
	private final JComboBox<String> m_font_family_box;
	private final JSlider m_font_size_slider;
	private final JButton m_font_color_button;

	private final HighlightButton m_default_button;

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public PreferencesPanel() {

		m_observers = new ArrayList<PreferencesObserver>();

		// General parameters

		m_cell_editable = new JCheckBox("Permettre la modification de la base à partir des tableaux (non recommandé)");
		m_cell_editable.addActionListener(this);
		m_cell_editable.setFont(m_default_font);
		m_cell_editable.setSelected(
				Boolean.valueOf(PropertiesManager.loadProperty("stock_management.prefs", "is_cell_editable", "false")));

		m_general = new JPanel(new GridLayout());
		m_general.setBackground(ColorPalette.WHITE);
		m_general.setBorder(new TitledBorder(new LineBorder(ColorPalette.BLACK, 2), "Général", TitledBorder.LEFT,
				TitledBorder.TOP, FontManager.CENTURY_GOTHIC_18));

		m_general.add(m_cell_editable);

		add(m_general, BorderLayout.NORTH);

		// Display parameters

		// Cell

		// Color

		m_cell_color_label = new JLabel("  Couleur de la cellule :",
				new ImageIcon("res/icons/preferences/cell_color-48.png"), SwingConstants.LEFT);
		m_cell_color_label.setFont(m_default_font);

		m_cell_color_button = new JButton();
		m_cell_color_button.addActionListener(this);
		m_cell_color_button.setBackground(new Color(
				Integer.parseInt(PropertiesManager.loadProperty("stock_management.prefs", "cell_color", "-1"))));
		m_cell_color_button.setBorder(new LineBorder(ColorPalette.BLACK, 2));

		// Selection Color

		m_cell_selection_color_label = new JLabel("  Couleur de la selection :",
				new ImageIcon("res/icons/preferences/cell_selection_color-48.png"), SwingConstants.LEFT);
		m_cell_selection_color_label.setFont(m_default_font);

		m_cell_selection_color_button = new JButton();
		m_cell_selection_color_button.addActionListener(this);
		m_cell_selection_color_button.setBorder(new LineBorder(ColorPalette.BLACK, 2));
		m_cell_selection_color_button
				.setBackground(new Color(Integer.parseInt(PropertiesManager.loadProperty("stock_management.prefs",
						"cell_selection_color", String.valueOf(ColorPalette.DEEP_SKY_BLUE.getRGB())))));

		// Size

		m_cell_size_label = new JLabel("  Taille de la cellule :",
				new ImageIcon("res/icons/preferences/cell_size-48.png"), SwingConstants.LEFT);
		m_cell_size_label.setFont(m_default_font);

		m_cell_size_slider = new JSlider(30, 100,
				Integer.parseInt(PropertiesManager.loadProperty("stock_management.prefs", "cell_size", "50")));
		m_cell_size_slider.addChangeListener(this);
		m_cell_size_slider.setBackground(ColorPalette.WHITE);
		m_cell_size_slider.setFont(m_default_font);
		m_cell_size_slider.setMinorTickSpacing(1);
		m_cell_size_slider.setMajorTickSpacing(5);
		m_cell_size_slider.setPaintLabels(true);
		m_cell_size_slider.setPaintTicks(true);

		// -

		m_cell_display = new JPanel(new GridLayout(3, 2, 0, 10));
		m_cell_display.add(m_cell_color_label);
		m_cell_display.add(m_cell_color_button);
		m_cell_display.add(m_cell_selection_color_label);
		m_cell_display.add(m_cell_selection_color_button);
		m_cell_display.add(m_cell_size_label);
		m_cell_display.add(m_cell_size_slider);
		m_cell_display.setBackground(ColorPalette.WHITE);

		// Font

		// Type

		m_font_family_label = new JLabel("  Type de la police :",
				new ImageIcon("res/icons/preferences/font_family-48.png"), SwingConstants.LEFT);
		m_font_family_label.setFont(m_default_font);

		final String[] items = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		m_font_family_box = new JComboBox<String>(items);
		m_font_family_box.addItemListener(this);
		m_font_family_box.setBackground(ColorPalette.WHITE);
		m_font_family_box.setFont(m_default_font);
		m_font_family_box
				.setSelectedItem(PropertiesManager.loadProperty("stock_management.prefs", "font_family", "Calibri"));

		// Color

		m_font_color_label = new JLabel("  Couleur de la police :",
				new ImageIcon("res/icons/preferences/font_color-48.png"), SwingConstants.LEFT);
		m_font_color_label.setFont(m_default_font);

		m_font_color_button = new JButton();
		m_font_color_button.addActionListener(this);
		m_font_color_button.setBorder(new LineBorder(ColorPalette.BLACK, 2));
		m_font_color_button.setBackground(new Color(
				Integer.parseInt(PropertiesManager.loadProperty("stock_management.prefs", "font_color", "-16777216"))));

		// Size

		m_font_size_label = new JLabel("  Taille de la police :",
				new ImageIcon("res/icons/preferences/font_size-48.png"), SwingConstants.LEFT);
		m_font_size_label.setFont(m_default_font);

		m_font_size_slider = new JSlider(10, 30,
				Integer.parseInt(PropertiesManager.loadProperty("stock_management.prefs", "font_size", "15")));
		m_font_size_slider.addChangeListener(this);
		m_font_size_slider.setBackground(ColorPalette.WHITE);
		m_font_size_slider.setFont(m_default_font);
		m_font_size_slider.setMinorTickSpacing(1);
		m_font_size_slider.setMajorTickSpacing(5);
		m_font_size_slider.setPaintLabels(true);
		m_font_size_slider.setPaintTicks(true);

		// -

		m_cell_font = new JPanel(new GridLayout(3, 2, 0, 10));
		m_cell_font.setBackground(ColorPalette.WHITE);
		m_cell_font.setBorder(new TitledBorder(new LineBorder(ColorPalette.BLACK), "Police", TitledBorder.LEFT,
				TitledBorder.TOP, FontManager.CENTURY_GOTHIC_16));

		m_cell_font.add(m_font_family_label);
		m_cell_font.add(m_font_family_box);
		m_cell_font.add(m_font_color_label);
		m_cell_font.add(m_font_color_button);
		m_cell_font.add(m_font_size_label);
		m_cell_font.add(m_font_size_slider);

		// -

		m_cell = new JPanel(new GridLayout(2, 1, 0, 10));
		m_cell.setBackground(ColorPalette.WHITE);
		m_cell.setBorder(new TitledBorder(new LineBorder(ColorPalette.BLACK), "Cellule", TitledBorder.LEFT,
				TitledBorder.TOP, FontManager.CENTURY_GOTHIC_16));

		m_cell.add(m_cell_display);
		m_cell.add(m_cell_font);

		// END Cell

		m_display = new JPanel(new GridLayout());
		m_display.setBackground(ColorPalette.WHITE);
		m_display.setBorder(new TitledBorder(new LineBorder(ColorPalette.BLACK, 2), "Affichage", TitledBorder.LEFT,
				TitledBorder.TOP, FontManager.CENTURY_GOTHIC_18));

		m_display.add(m_cell);

		// END Display

		add(m_display, BorderLayout.CENTER);

		// Reset parameters

		m_default_button = new HighlightButton(new ImageIcon("res/icons/preferences/refresh-32.png"),
				ColorPalette.WHITE, ColorPalette.LIGHT_GRAY);

		m_default_button.addActionListener(this);
		m_default_button.setFont(m_default_font);
		m_default_button.setPreferredSize(new Dimension(getWidth(), 50));
		m_default_button.setToolTipText("Restaurer la configuration par défaut");

		add(m_default_button, BorderLayout.SOUTH);
	}

	// RE-IMPLEMENTED METHODS

	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource().equals(m_cell_editable)) {

			notifyObservers(m_cell_editable.isSelected(), m_cell_color_button.getBackground(),
					m_cell_selection_color_button.getBackground(), m_cell_size_slider.getValue());
		} else if (event.getSource().equals(m_cell_color_button)) {

			final Color cell_color = JColorChooser.showDialog(this, "Choix de la couleur",
					m_cell_color_button.getBackground());

			if (cell_color != null) {

				m_cell_color_button.setBackground(cell_color);
			}

			notifyObservers(m_cell_editable.isSelected(), m_cell_color_button.getBackground(),
					m_cell_selection_color_button.getBackground(), m_cell_size_slider.getValue());
		} else if (event.getSource().equals(m_cell_selection_color_button)) {

			final Color cell_selection_color = JColorChooser.showDialog(this, "Choix de la couleur",
					m_cell_selection_color_button.getBackground());

			if (cell_selection_color != null) {

				m_cell_selection_color_button.setBackground(cell_selection_color);
			}

			notifyObservers(m_cell_editable.isSelected(), m_cell_color_button.getBackground(),
					m_cell_selection_color_button.getBackground(), m_cell_size_slider.getValue());
		} else if (event.getSource().equals(m_font_color_button)) {

			final Color cell_font_color = JColorChooser.showDialog(this, "Choix de la couleur",
					m_font_color_button.getBackground());

			if (cell_font_color != null) {

				m_font_color_button.setBackground(cell_font_color);
			}

			final Font font = new Font((String) m_font_family_box.getSelectedItem(), Font.PLAIN,
					m_font_size_slider.getValue());

			notifyObservers(font, m_font_color_button.getBackground());
		} else if (event.getSource().equals(m_default_button)) {

			final boolean cell_editable = false;
			final Color cell_color = ColorPalette.WHITE;
			final Color cell_selection_color = ColorPalette.DEEP_SKY_BLUE;
			final int cell_size = 50;

			final String font_family = "Calibri";
			final Color font_color = ColorPalette.BLACK;
			final int font_size = 15;

			m_cell_editable.setSelected(cell_editable);
			m_cell_color_button.setBackground(cell_color);
			m_cell_selection_color_button.setBackground(cell_selection_color);
			m_cell_size_slider.setValue(cell_size);
			m_font_family_box.setSelectedItem(font_family);
			m_font_color_button.setBackground(font_color);
			m_font_size_slider.setValue(font_size);

			notifyObservers(cell_editable, cell_color, cell_selection_color, cell_size);
			notifyObservers(new Font(font_family, Font.PLAIN, font_size), font_color);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent event) {

		if (event.getSource().equals(m_font_family_box) && m_font_size_slider != null) {

			final Font font = new Font((String) m_font_family_box.getSelectedItem(), Font.PLAIN,
					m_font_size_slider.getValue());

			notifyObservers(font, m_font_color_button.getBackground());
		}
	}

	@Override
	public void stateChanged(ChangeEvent event) {

		if (event.getSource().equals(m_cell_size_slider)) {

			notifyObservers(m_cell_editable.isSelected(), m_cell_color_button.getBackground(),
					m_cell_selection_color_button.getBackground(), m_cell_size_slider.getValue());
		} else if (event.getSource().equals(m_font_size_slider)) {

			final Font font = new Font((String) m_font_family_box.getSelectedItem(), Font.PLAIN,
					m_font_size_slider.getValue());

			notifyObservers(font, m_font_color_button.getBackground());
		}
	}

	// OBSERVABLE

	@Override
	public void addObserver(PreferencesObserver observer) {

		m_observers.add(observer);
	}

	@Override
	public void notifyObservers(boolean is_cell_editable, Color cell_color, Color cell_selection_color, int cell_size) {

		for (final PreferencesObserver observer : m_observers) {

			observer.update(is_cell_editable, cell_color, cell_selection_color, cell_size);
		}
	}

	@Override
	public void notifyObservers(Font cell_font, Color cell_font_color) {

		for (final PreferencesObserver observer : m_observers) {

			observer.update(cell_font, cell_font_color);
		}
	}

	@Override
	public void removeObservers() {

		m_observers = new ArrayList<PreferencesObserver>();
	}

	// IMPLEMENTED METHODS

	public void savePreferences() {

		final Object[] keys = { "is_cell_editable", "cell_size", "cell_color", "cell_selection_color", "font_family",
				"font_size", "font_color" };
		final Object[] values = { String.valueOf(m_cell_editable.isSelected()),
				String.valueOf(m_cell_size_slider.getValue()),
				String.valueOf(m_cell_color_button.getBackground().getRGB()),
				String.valueOf(m_cell_selection_color_button.getBackground().getRGB()),
				m_font_family_box.getSelectedItem(), String.valueOf(m_font_size_slider.getValue()),
				String.valueOf(m_font_color_button.getBackground().getRGB()) };

		PropertiesManager.saveProperties("stock_management.prefs", keys, values);
	}
}
