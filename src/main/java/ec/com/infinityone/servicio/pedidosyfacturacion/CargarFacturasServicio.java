/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.servicio.pedidosyfacturacion;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author David Ayala
 */
@LocalBean
@Stateless
public class CargarFacturasServicio extends ReusableBean {

    public void cargarFacturasBanco(List<JSONObject> arregloJSON) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.factura/cargarfacturasbancos";
        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/cargarfacturasbancos";
        final int SUCCESS_CODE = 200;

        try {
            // Primero crea un URI y luego lo convierte a URL
            URI uri = new URI(direcc);
            URL url = uri.toURL();
            String respuesta;

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                respuesta = arregloJSON.toString();
                writer.write(respuesta);
                writer.close();
            }

            if (connection.getResponseCode() == SUCCESS_CODE) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JSONObject jsonResponse = new JSONObject(response.toString());
                String developerMessage = jsonResponse.optString("developerMessage", "No message provided");
                this.dialogo(FacesMessage.SEVERITY_INFO, developerMessage);
                System.out.println("Se ha registrado con éxito");
            } else {
                System.out.println("FT::. Error en la ejecución de la grabación de facturas."+connection 
                        +"ERROR AL EJECUTAR:."+connection.getResponseMessage()+" - "+connection.getResponseCode());
                this.dialogo(FacesMessage.SEVERITY_FATAL, "NO SE HAN GRABADO NINGUNA FACTURA, DEBE REVISAR SI EXISTEN CLIENTES NUEVOS O PRODUCTOS NUEVOS. Comuniquese con Sistemas para esta verificación. "
                        +connection.getResponseMessage()+" - "+connection.getResponseCode());
            }

        } catch (Throwable e) {
            this.dialogo(FacesMessage.SEVERITY_FATAL, "NO SE HAN GRABADO NINGUNA FACTURA, DEBE REVISAR SI EXISTEN CLIENTES NUEVOS O PRODUCTOS NUEVOS. Comuniquese con Sistemas para esta verificación. "+e.getMessage());
            System.out.println("Se ha registrado con éxito");
            e.printStackTrace(System.out);
        }
    }

    public void actualizarGarantias() {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.factura/cargarfacturasbancos";
        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.clientegarantia/actualizargarantiasvencidas";
        final int SUCCESS_CODE = 200;

        try {
            // Primero crea un URI y luego lo convierte a URL
            URI uri = new URI(direcc);
            URL url = uri.toURL();
            String respuesta;

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            if (connection.getResponseCode() == SUCCESS_CODE) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JSONObject jsonResponse = new JSONObject(response.toString());
                String developerMessage = jsonResponse.optString("developerMessage", "No message provided");
                this.dialogo(FacesMessage.SEVERITY_INFO, developerMessage);
                System.out.println("Se ha registrado con éxito");
            } else {
                logErroresResponse(connection);
            }

        } catch (Throwable e) {
            System.out.println("Se ha registrado con éxito");
            e.printStackTrace(System.out);
        }
    }

    private void logErroresResponse(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        JSONObject jsonResponse = new JSONObject(response.toString());
        String developerMessage = jsonResponse.optString("developerMessage", "No message provided");
        System.out.println("Error al añadir: " + connection.getResponseCode());
        System.out.println("Error: " + connection.getErrorStream());
        System.out.println(connection.getResponseMessage());
        this.dialogo(FacesMessage.SEVERITY_ERROR, connection.getResponseMessage());
    }

}
