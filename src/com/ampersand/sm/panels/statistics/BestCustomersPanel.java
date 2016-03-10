package com.ampersand.sm.panels.statistics;

import java.awt.BasicStroke;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.ampersand.lcu.db.MySQLDatabase;
import com.ampersand.lcu.gui.color.ColorPalette;
import com.ampersand.lcu.gui.component.chart.bar.BasicBarRenderer;
import com.ampersand.lcu.gui.font.FontManager;
import com.ampersand.sm.panels.ABasicBestPanel;

public class BestCustomersPanel extends ABasicBestPanel {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = 4176278480523085080L;

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public BestCustomersPanel(MySQLDatabase database) {

		super(database);
	}

	// RE-IMPLEMENTED METHODS

	// IMPLEMENTED METHODS

	@Override
	public void initCharts() {

		super.initCharts();

		initBarChart();
	}

	private void initBarChart() {

		final ResultSet result_set = m_database.executeSelection("SELECT * FROM Client");

		final DefaultCategoryDataset category_dataset = new DefaultCategoryDataset();

		int i = 0;

		try {

			while (result_set.next()) {

				category_dataset.addValue(result_set.getInt("Articles_Achetes"), result_set.getString("Nom"),
						new Integer(i));

				i++;
			}
		} catch (final SQLException e) {

			e.printStackTrace();
		}

		final JFreeChart bar_chart = ChartFactory.createBarChart("Meilleurs clients", null, "Articles achetés",
				category_dataset, PlotOrientation.VERTICAL, true, true, true);

		bar_chart.getTitle().setFont(FontManager.CENTURY_GOTHIC_20);

		final CategoryPlot plot = bar_chart.getCategoryPlot();
		plot.setBackgroundPaint(ColorPalette.WHITE);
		plot.setDomainGridlinePaint(ColorPalette.BLACK);
		plot.setRangeGridlinePaint(ColorPalette.BLACK);
		plot.setOutlineStroke(new BasicStroke(2));

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setLabelFont(FontManager.CENTURY_GOTHIC_16);
		rangeAxis.setTickLabelFont(FontManager.CENTURY_GOTHIC_16);

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(FontManager.CENTURY_GOTHIC_16);
		domainAxis.setTickLabelFont(FontManager.CENTURY_GOTHIC_16);

		plot.setRenderer(new BasicBarRenderer());

		final ChartPanel chart_pane = new ChartPanel(bar_chart);

		m_directed_pane.add(chart_pane);
	}
}
