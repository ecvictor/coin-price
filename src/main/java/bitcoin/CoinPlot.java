package bitcoin;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import static bitcoin.PriceFetch.getPrice;

public class CoinPlot extends ApplicationFrame {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
    public static final Long DAY_IN_SECOND = 24 * 60 * 60L;
    public static final int SECOND_TO_MILLISECOND = 1000;
    public static final int DAYS = 7;

    public CoinPlot(String applicationTitle, String chartTitle, Coin coin) throws IOException {
        super(applicationTitle);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Date", "Price of " + coin.getName() + " " + coin.getUnit(),
                createDataset(coin),
                PlotOrientation.VERTICAL,
                true, true, false);

        CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setShapesVisible(true);
        DecimalFormat decimalformat1 = new DecimalFormat("##");
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat1));
        renderer.setItemLabelsVisible(true);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);

        int width = 1080;    /* Width of the image */
        int height = 768;   /* Height of the image */
        SimpleDateFormat sdfPlot = new SimpleDateFormat("yyyy_MM_dd");
        String date = sdfPlot.format(coin.getTimestamp() * SECOND_TO_MILLISECOND);

        File chartFile = new File(coin.getName() + "_" + date + ".jpeg");
        ChartUtilities.saveChartAsJPEG(chartFile, lineChart, width, height);
    }

    private DefaultCategoryDataset createDataset(Coin coin) {

        double totalPrice = 0;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        long currentTime = coin.getTimestamp();
        long startTime;
        for (int i = DAYS; i > 0; i--) {
            startTime = currentTime - DAY_IN_SECOND * i;
            double price = getPrice("https://min-api.cryptocompare.com/data/pricehistorical?fsym=" + coin.getName()

                    + "&tsyms=" + coin.getUnit() + "&ts=" + startTime, coin);

            String date = sdf.format(startTime * SECOND_TO_MILLISECOND);
            System.out.println(date + " : " + price);
            dataset.addValue(price, coin.getName(), date);

            totalPrice += price;

        }
        System.out.println("Average Price = " + totalPrice / DAYS);


        return dataset;
    }
}