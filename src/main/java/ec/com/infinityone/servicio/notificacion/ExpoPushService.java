package ec.com.infinityone.servicio.notificacion;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.primefaces.shaded.json.JSONObject;

/**
 * Envío asíncrono de notificaciones push hacia Expo. La llamada HTTP corre en
 * un hilo secundario para no bloquear la pantalla ni la transacción.
 */
@LocalBean
@Stateless
public class ExpoPushService {

    private static final String EXPO_URL = "https://exp.host/--/api/v2/push/send";
    private static final int TIMEOUT_MS = 5000;
    private static final String CANAL_PYS = "pys_prepedidos";

    @Asynchronous
    public void enviarPrepedidoAutorizado(String pushToken, String numeroPrepedido, String codigoTerminal) {
        if (pushToken == null || !pushToken.startsWith("ExponentPushToken")) {
            System.out.println("[ExpoPush] Token nulo o inválido, no se envía notificación.");
            return;
        }
        HttpURLConnection connection = null;
        try {
            JSONObject data = new JSONObject();
            data.put("tipo", "PREPEDIDO_AUTORIZADO");
            data.put("numeroPrepedido", numeroPrepedido);
            data.put("terminal", codigoTerminal);

            JSONObject payload = new JSONObject();
            payload.put("to", pushToken);
            // Escapes Unicode: independiente del encoding del compilador (\u2705 = check,
            // \u00e9 = e acentuada)
            payload.put("title", "\u2705 Prepedido Autorizado - PYS");
            payload.put("body", "El prepedido #" + numeroPrepedido + " ha sido autorizado con \u00e9xito.");
            payload.put("sound", "default");
            payload.put("priority", "high");
            payload.put("channelId", CANAL_PYS);
            payload.put("data", data);

            connection = (HttpURLConnection) new URL(EXPO_URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            }

            int code = connection.getResponseCode();
            InputStream is = code >= 400 ? connection.getErrorStream() : connection.getInputStream();
            StringBuilder resp = new StringBuilder();
            if (is != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        resp.append(line);
                    }
                }
            }
            System.out.println("[ExpoPush] HTTP " + code + " prepedido " + numeroPrepedido + ": " + resp);
        } catch (Exception e) {
            System.out
                    .println("[ExpoPush] Error enviando push del prepedido " + numeroPrepedido + ": " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
