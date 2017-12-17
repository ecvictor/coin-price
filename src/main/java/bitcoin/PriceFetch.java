package bitcoin;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class PriceFetch {

    public static double getPrice(String url, Coin coin) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost(url);
            request.addHeader("content-type", "application/json");
            HttpResponse result = httpClient.execute(request);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");


            JsonParser parser = new JsonParser();
            JsonObject o = parser.parse(json).getAsJsonObject();
            double price = o.get(coin.getName()).getAsJsonObject().get(coin.getUnit()).getAsDouble();
            return price;
        } catch (IOException ex) {
        }
        return Double.parseDouble(null);
    }

}