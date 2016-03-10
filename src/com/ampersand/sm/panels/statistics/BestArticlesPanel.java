package com.ampersand.sm.panels.statistics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.ampersand.lcu.db.MySQLDatabase;
import com.ampersand.lcu.gui.color.ColorPalette;
import com.ampersand.lcu.gui.component.chart.bar.BasicBarRenderer;
import com.ampersand.lcu.gui.font.FontManager;
import com.ampersand.sm.panels.ABasicBestPanel;

public class BestArticlesPanel extends ABasicBestPanel {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = -5022885393251648021L;

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public BestArticlesPanel(MySQLDatabase database) {

		super(database);
	}

	// RE-IMPLEMENTED METHODS

	// IMPLEMENTED METHODS

	@Override
	public void initCharts() {

		super.initCharts();

		initBarChart();
		initPieChart();
	}

	private void initBarChart() {

		final DefaultCategoryDataset category_dataset = new DefaultCategoryDataset();

		int i = 0;

		try {

			final ResultSet result_set = m_database.executeSelection("SELECT * FROM Produit");

			while (result_set.next()) {

				category_dataset.addValue(result_set.getInt("Unites_Vendues"), result_set.getString("Nom"),
						new Integer(i));

				i++;
			}
		} catch (final SQLException e) {

			e.printStackTrace();
		}

		final JFreeChart chart = ChartFactory.createBarChart("Meilleurs produits", null, "Unités vendues",
				category_dataset, PlotOrientation.VERTICAL, true, true, true);

		chart.getTitle().setFont(FontManager.CENTURY_GOTHIC_20);

		final CategoryPlot plot = chart.getCategoryPlot();
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

		m_directed_pane.add(new ChartPanel(chart));
	}

	private void initPieChart() {

		final DefaultPieDataset data_set = new DefaultPieDataset();

		try {

			final ResultSet result_set = m_database.executeSelection("SELECT * FROM Produit");

			while (result_set.next()) {

				data_set.setValue(result_set.getString("Nom"), result_set.getInt("Quantite"));
			}
		} catch (final SQLException e) {

			e.printStackTrace();
		}

		final JFreeChart chart = ChartFactory.createPieChart("Quantité de produits en stock", data_set, true, true,
				true);

		chart.getLegend().setItemFont(FontManager.CENTURY_GOTHIC_16);
		chart.getTitle().setFont(FontManager.CENTURY_GOTHIC_20);

		final PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(ColorPalette.WHITE);
		plot.setOutlineStroke(new BasicStroke(2));
		plot.setShadowPaint(null);
		plot.setOutlinePaint(null);

		final Color[] colors = ColorPalette.getDefaults();

		for (int i = 0; i < data_set.getItemCount(); i++) {

			plot.setSectionPaint(data_set.getKey(i), colors[i % colors.length]);
		}

		m_directed_pane.add(new ChartPanel(chart));
	}
}
