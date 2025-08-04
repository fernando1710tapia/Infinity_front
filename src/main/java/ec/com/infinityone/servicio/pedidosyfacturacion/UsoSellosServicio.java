/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.servicio.pedidosyfacturacion;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author david
 */
@LocalBean
@Stateless
public class UsoSellosServicio extends ReusableBean {

    public JSONArray buscarUsoSellos(String codComer, String codTerminal, String fechaInicio, String fechaFin) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.usosello/buscarusosellofechas?codigocomercializadora=0095&codigoterminal=07&fechainicio=2025/03/01&fechafin=2025/03/20";

        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.usosello/buscarusosellofechas?";
        final int SUCCESS_CODE = 200;
        JSONArray retorno = new JSONArray();

        try {

            // Primero crea un URI y luego lo convierte a URL
            URI uri = new URI(direcc + "codigocomercializadora=" + codComer
                    + "&codigoterminal=" + codTerminal
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
            System.out.println("Error");
            e.printStackTrace(System.out);
        }

        return retorno;
    }

    public String buscarUltimoSello(String codComer, String codTerminal) {
        //String urlPath = "GET http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.usosello/buscarultimousoxterminal?codigocomercializadora=0095&codigoterminal=07";

        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.usosello/buscarultimousoxterminal?";
        final int SUCCESS_CODE = 200;
        JSONArray retorno = new JSONArray();
        String developerMessage = "";

        try {

            // Primero crea un URI y luego lo convierte a URL
            URI uri = new URI(direcc + "codigocomercializadora=" + codComer
                    + "&codigoterminal=" + codTerminal
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
            developerMessage = objetoJson.optString("developerMessage", "No message provided");
            //retorno = objetoJson.getJSONArray("retorno");

            if (connection.getResponseCode() != 200) {
                logErrorResponse(connection);
            }

        } catch (Throwable e) {
            System.out.println("Error");
            e.printStackTrace(System.out);
        }

        return developerMessage;
    }

    public int adquirirUsoSellos(JSONObject bodyCompra) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.usosello";

        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.usosello";
        final int SUCCESS_CODE = 200;
        int respuestaServicioPersistencia = 200;
        try {
            URI uri = new URI(direcc);
            url = uri.toURL();
            String respuesta;

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                respuesta = bodyCompra.toString();
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
                respuestaServicioPersistencia = connection.getResponseCode();
            } else {
                respuestaServicioPersistencia = connection.getResponseCode();
                JSONObject jsonResponse = new JSONObject(connection.getResponseCode());
                String developerMessage = jsonResponse.optString("developerMessage", "No se ha podido asignar los sellos a los pedidos, Verifique si se está duplicando la primera NP");
                this.dialogo(FacesMessage.SEVERITY_ERROR, developerMessage);

                System.out.println("FT:. ERRO AL GRABAR LA RELACION PEDIDOS-SELLOS "+connection.getResponseCode());
            }

        } catch (Throwable e) {
            System.out.println("FT:. ERRO AL GRABAR LA RELACION PEDIDOS-SELLOS ");
            e.printStackTrace(System.out);
        }
        return respuestaServicioPersistencia;
    }
    
    public int actualizarUsoSellos(JSONObject bodyCompra) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.usosello";
         
        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.usosello/porId";
        final int SUCCESS_CODE = 200;
        int respuestaServicioPersistencia = 200;
        try {
            URI uri = new URI(direcc);
            url = uri.toURL();
            String respuesta;

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                respuesta = bodyCompra.toString();
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
                respuestaServicioPersistencia = connection.getResponseCode();
            } else {
                respuestaServicioPersistencia = connection.getResponseCode();
                JSONObject jsonResponse = new JSONObject(connection.getResponseCode());
                String developerMessage = jsonResponse.optString("developerMessage", "No se ha podido asignar los sellos a los pedidos, Verifique si se está duplicando la primera NP");
                this.dialogo(FacesMessage.SEVERITY_ERROR, developerMessage);

                System.out.println("FT:. ERRO AL GRABAR LA RELACION PEDIDOS-SELLOS "+connection.getResponseCode());
            }

        } catch (Throwable e) {
            System.out.println("FT:. ERRO AL GRABAR LA RELACION PEDIDOS-SELLOS ");
            e.printStackTrace(System.out);
        }
        return respuestaServicioPersistencia;
    }
    
        public String verficarExistenciaSelloEnInventario(String codComer, String codTerminal, String sellosConcatenados) {
        
        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detalleterminalsello/validarexistencia?";
        final int SUCCESS_CODE = 200;
        JSONArray retorno = new JSONArray();
        String developerMessage = "";

        try {

            // Primero crea un URI y luego lo convierte a URL
            URI uri = new URI(direcc + "codigocomercializadora=" + codComer
                    + "&codigoterminal=" + codTerminal
                    + "&sellosconcatenados=" + sellosConcatenados
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
            developerMessage = objetoJson.optString("developerMessage", "No message provided");
            //retorno = objetoJson.getJSONArray("retorno");

            if (connection.getResponseCode() != 200) {
                logErrorResponse(connection);
            }

        } catch (Throwable e) {
            System.out.println("FT:: ERROR EN verficarExistenciaSelloEnInventario::."+e.getMessage());
            e.printStackTrace(System.out);
        }

        return developerMessage;
    }

}
