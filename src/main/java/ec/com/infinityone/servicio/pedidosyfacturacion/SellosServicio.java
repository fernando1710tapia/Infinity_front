/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.servicio.pedidosyfacturacion;

import com.google.gson.JsonArray;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author David Ayala
 */
@LocalBean
@Stateless
public class SellosServicio extends ReusableBean {

    public JSONArray buscarSellos(String codComer, String codTerminal, String tipoTerminal, String fechaInicio, String fechaFin) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.factura/cargarfacturasbancos";

        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.terminalsello/paraconsultaprincipal?";
        final int SUCCESS_CODE = 200;
        JSONArray retorno = new JSONArray();

        try {

            // Primero crea un URI y luego lo convierte a URL
            URI uri = new URI(direcc + "codigocomercializadora=" + codComer + "&codigoterminal=" + codTerminal
                    + "&tipoterminal=" + tipoTerminal
                    + "&fechainicio=" + fechaInicio
                    + "&fechafin=" + fechaFin
            );
            url = uri.toURL();

            String respuesta = "";
            String tmp = null;

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            retorno = objetoJson.getJSONArray("retorno");

            if (connection.getResponseCode() != 200) {
                logErrorResponse(connection);
            }

        } catch (Throwable e) {
            System.out.println("Se ha registrado con éxito");
            e.printStackTrace(System.out);
        }

        return retorno;
    }
    
    public JSONArray cargarDetalleSellos (String codigocomercializadora, String codigoterminalentrega, String codigoterminalrecibe, BigInteger selloinicial, BigInteger sellofinal) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.factura/cargarfacturasbancos";

        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detalleterminalsello/poridpadre?";
        final int SUCCESS_CODE = 200;
        JSONArray retorno = new JSONArray();

        try {

            // Primero crea un URI y luego lo convierte a URL
            URI uri = new URI(direcc + "codigocomercializadora=" + codigocomercializadora + "&codigoterminalentrega=" + codigoterminalentrega
                    + "&codigoterminalrecibe=" + codigoterminalrecibe
                    + "&selloinicial=" + selloinicial
                    + "&sellofinal=" + sellofinal
            );
            url = uri.toURL();

            String respuesta = "";
            String tmp = null;

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            retorno = objetoJson.getJSONArray("retorno");

            if (connection.getResponseCode() != 200) {
                logErrorResponse(connection);
            }

        } catch (Throwable e) {
            System.out.println("Se ha registrado con éxito");
            e.printStackTrace(System.out);
        }

        return retorno;
    }

    private void logErrorResponse(HttpURLConnection connection) throws IOException {
        System.out.println("Error al añadir: " + connection.getResponseCode());
        System.out.println("Error: " + connection.getErrorStream());
        System.out.println(connection.getResponseMessage());
        this.dialogo(FacesMessage.SEVERITY_ERROR, connection.getResponseMessage());
    }

}
