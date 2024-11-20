/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.servicio.pedidosyfacturacion;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.IOException;
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

    public void actualizarGarantias(String arregloJSON) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.clientegarantia/actualizargarantiasvencidas";
        String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.factura/cargarfacturasbancos";
        //String urlPath = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio/agregarlote";
        final int SUCCESS_CODE = 200;

        try {
            // Primero crea un URI y luego lo convierte a URL
            URI uri = new URI(urlPath);
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            // Convertir arregloJSON a String y escribirlo en el cuerpo de la solicitud
            //String jsonResponse = arregloJSON.toString();
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(arregloJSON);
                writer.close();
            }

            if (connection.getResponseCode() == SUCCESS_CODE) {
                //showDialog(FacesMessage.SEVERITY_INFO, "SE HA REGISTRADO CON ÉXITO");
                System.out.println("Se ha registrado con éxito");
            } else {
                logErrorResponse(connection);
            }

        } catch (Throwable e) {
            System.out.println("Se ha registrado con éxito");
            e.printStackTrace(System.out);
        }
    }

    private void logErrorResponse(HttpURLConnection connection) throws IOException {
        System.out.println("Error al añadir: " + connection.getResponseCode());
        System.out.println("Error: " + connection.getErrorStream());
        System.out.println(connection.getResponseMessage());
        //showDialog(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
    }

}
