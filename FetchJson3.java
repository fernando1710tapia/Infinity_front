import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class FetchJson3 {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.prepedido/paraFactura?fecha1=2026/06/16&fecha2=2026/06/16&codigoabastecedora=0001&codigocomercializadora=0008&tipofecha=1");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }
        in.close();
    }
}
