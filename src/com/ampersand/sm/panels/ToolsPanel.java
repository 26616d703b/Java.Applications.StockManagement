package com.ampersand.sm.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.ampersand.calculette.controllers.BasicCalculatorController;
import com.ampersand.calculette.models.data.BasicCalculatorModel;
import com.ampersand.calculette.views.BasicCalculatorView;
import com.ampersand.lcu.gui.component.button.HighlightButton;
import com.ampersand.mailr.MaileR;
import com.ampersand.uc.UnitConverter;

public class ToolsPanel extends ABasicPanel implements ActionListener {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = -2056252102697424769L;

	private final JPanel m_tools;

	private final HighlightButton m_calculator_button;
	private final HighlightButton m_unit_converter_button;
	private final HighlightButton m_mailer_button;

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public ToolsPanel() {

		// CENTER

		m_calculator_button = new HighlightButton(new ImageIcon("res/icons/tools/calculator-128.png"));
		m_calculator_button.addActionListener(this);

		m_unit_converter_button = new HighlightButton(new ImageIcon("res/icons/tools/available_updates-128.png"));
		m_unit_converter_button.addActionListener(this);

		m_mailer_button = new HighlightButton(new ImageIcon("res/icons/tools/new_post-128.png"));
		m_mailer_button.addActionListener(this);

		m_tools = new JPanel(new GridLayout(3, 1));
		m_tools.add(m_calculator_button);
		m_tools.add(m_unit_converter_button);
		m_tools.add(m_mailer_button);

		add(m_tools, BorderLayout.CENTER);

		// SOUTH

	}

	// RE-IMPLEMENTED METHODS

	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource().equals(m_calculator_button)) {

			final BasicCalculatorModel model = new BasicCalculatorModel();
			final BasicCalculatorView view = new BasicCalculatorView();

			final BasicCalculatorController controller = new BasicCalculatorController(model, view);
			controller.control();
		} else if (event.getSource().equals(m_unit_converter_button)) {

			final UnitConverter converter = new UnitConverter();
			converter.setVisible(true);
		} else if (event.getSource().equals(m_mailer_button)) {

			final MaileR mailer = new MaileR();
			mailer.setVisible(true);
		}
	}
}
