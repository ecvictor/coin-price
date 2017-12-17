package bitcoin;


import org.jfree.ui.RefineryUtilities;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class priceMain {

    public static final Long DAY_IN_SECOND = 24 * 60 * 60L;
    public static final int SECOND_TO_MILISECOND = 1000;
    public static final int DAYS = 7;

    public static void main(String... args) throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Long currentTime = timestamp.getTime() / SECOND_TO_MILISECOND;

        Coin bitcoin = new Coin();
        bitcoin.setName("ETH");
        bitcoin.setUnit("CAD");
        bitcoin.setTimestamp(currentTime);


        CoinPlot chart = new CoinPlot(
                "Price Vs Date",
                bitcoin.getName() + " Price vs Date", bitcoin);

        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}
