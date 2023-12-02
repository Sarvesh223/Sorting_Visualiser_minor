import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ScatterChartGenerator {
    private JFrame scatterChartFrame;
    private XYSeries scatterChartSeries;

    public ScatterChartGenerator(int[] data) {
        initialize(data);
    }

    private void initialize(int[] data) {
        scatterChartFrame = new JFrame("Scatter Chart");
        scatterChartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel scatterChartPanel = new JPanel();
        scatterChartPanel.add(new JLabel("Bubble Sort Scatter Chart:"));

        scatterChartSeries = new XYSeries("Bubble Sort");

        for (int i = 0; i < data.length; i++) {
            scatterChartSeries.add(i + 1, data[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(scatterChartSeries);
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Bubble Sort Scatter Chart",
                "Index",
                "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        scatterChartFrame.add(scatterChartPanel);
        scatterChartFrame.add(chartPanel);
        scatterChartFrame.setSize(400, 300);
        scatterChartFrame.setLocationRelativeTo(null);
        scatterChartFrame.setVisible(true);
    }

    public void updateScatterData(int[] newData) {
        scatterChartSeries.clear();
        for (int i = 0; i < newData.length; i++) {
            scatterChartSeries.add(i + 1, newData[i]);
        }
    }
}
