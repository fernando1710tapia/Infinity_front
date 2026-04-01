/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.servicio.pedidosyfacturacion;

import com.google.gson.JsonArray;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Detalleterminalsello;
import ec.com.infinityone.modelo.DetalleterminalselloPK;
import ec.com.infinityone.modelo.TerminalSello;
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
import java.util.ArrayList;
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
    
/*
    Variable que almacena varios TerminalSello
     */
    private List<Detalleterminalsello> listaDetalleTerminalSello;    

    public JSONArray buscarSellos(int tipoConsulta, String codComer, String codTerminal, String tipoTerminal, String fechaInicio, String fechaFin) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.factura/cargarfacturasbancos";
        String nombreConsulta = "ec.com.infinity.modelo.terminalsello/paraconsultaprincipal?";
        if(2 == tipoConsulta){
        nombreConsulta = "ec.com.infinity.modelo.terminalsello/paraentregarsolovalidos?";
        }
        
        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + nombreConsulta;
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
    public JSONArray buscarUsoSellosTerminal(String codComer, String codTerminal, String fechaInicio, String fechaFin) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.factura/cargarfacturasbancos";

        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.usosello/buscarusosellosterminal?";
        final int SUCCESS_CODE = 200;
        JSONArray retorno = new JSONArray();

        try {

            // Primero crea un URI y luego lo convierte a URL
            URI uri = new URI(direcc + "codigocomercializadora=" + codComer + "&codigoterminal=" + codTerminal
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
            System.out.println("FT::. Error en metodo buscarUsoSellosTerminal "+ e.getMessage());
            e.printStackTrace(System.out);
        }

        return retorno;
    }
    
    public JSONArray buscarUsoSellosTerminalInformados(String codComer, String codTerminal, String fechaInicio, String fechaFin) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.factura/cargarfacturasbancos";

        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.usosello/buscarusosellosterminalinformados?";
        final int SUCCESS_CODE = 200;
        JSONArray retorno = new JSONArray();

        try {

            // Primero crea un URI y luego lo convierte a URL
            URI uri = new URI(direcc + "codigocomercializadora=" + codComer + "&codigoterminal=" + codTerminal
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
            System.out.println("FT::. Error en metodo buscarUsoSellosTerminal "+ e.getMessage());
            e.printStackTrace(System.out);
        }

        return retorno;
    }

    public JSONArray cargarDetalleSellos(String codigocomercializadora, String codigoterminalentrega, String codigoterminalrecibe, BigInteger selloinicial, BigInteger sellofinal) {
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

    public void comprarSellos(JSONObject bodyCompra) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.factura/cargarfacturasbancos";

        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.terminalsello/comprar";
        final int SUCCESS_CODE = 200;
        String developerMessage ="";
        try {
            URI uri = new URI(direcc);
            url = uri.toURL();
            String respuesta;

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            StringBuilder response = new StringBuilder();
             
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                respuesta = bodyCompra.toString();
                writer.write(respuesta);
                writer.close();
            }

            if (connection.getResponseCode() == SUCCESS_CODE) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); 
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JSONObject jsonResponse = new JSONObject(response.toString());
                developerMessage = jsonResponse.optString("developerMessage", "No message provided");
                this.dialogo(FacesMessage.SEVERITY_INFO, developerMessage);
                System.out.println("FT:. comprarSellos(JSONObject bodyCompra) - Se ha registrado con éxito");
            } else {
                logErrorResponse(connection);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); 
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JSONObject jsonResponse = new JSONObject(response.toString());
                developerMessage = jsonResponse.optString("developerMessage", " Error producido al intentar grabar");
                this.dialogo(FacesMessage.SEVERITY_ERROR, developerMessage);
                System.out.println("FT:.Error en comprarSellos(JSONObject bodyCompra). "+ response.toString());
                
            }

        } catch (Throwable e) {
            System.out.println("FT:. ERROR EN METODO.comprarSellos(JSONObject bodyCompra):. "+e.getMessage());
            this.dialogo(FacesMessage.SEVERITY_ERROR, developerMessage);
            e.printStackTrace(System.out);
        }
    }

    public Boolean actualizarSellos(List<JSONObject> bodySello) {                //(List<JSONObject> bodySello) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.factura/cargarfacturasbancos";

        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.terminalsello/actualizarlote";
        final int SUCCESS_CODE = 200;
        Boolean valido = false;
        System.out.println("FT:: INICIA METODO-actualizarSellos. SERVICIO A USAR::ec.com.infinity.modelo.terminalsello/actualizarlote");
        try {
            URI uri = new URI(direcc);
            url = uri.toURL();
            String respuesta;

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                respuesta = bodySello.toString();
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
                System.out.println("FT:. METODO.actualizarSellos() Se ha actualizado con éxito");
                valido = true;
            } else {
                logErrorResponse(connection);
                valido = false;
            }

        } catch (Throwable e) {
            System.out.println("FT:: Error en actualizarSellos. "+e.getMessage());
            e.printStackTrace(System.out);
            valido = false;
        }
        return valido;
    }

    // metodo para actualizar TERMINALSELLO CON LA CANTIDAD REAL DE SELLOSVALIDOS
     
    public void actualizarTerminalSelloNueva(JSONObject bodySello) {                //(List<JSONObject> bodySello) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.factura/cargarfacturasbancos";

        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.terminalsello/actualizarsellosvalidosreal";
        final int SUCCESS_CODE = 200;
        Boolean valido = false;
        System.out.println("FT:: INICIA METODO-actualizarTerminalSelloNueva. SERVICIO A USAR::ec.com.infinity.modelo.terminalsello/actualizarsellosvalidosreal");
        try {
            URI uri = new URI(direcc);
            url = uri.toURL();
            String respuesta;

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                respuesta = bodySello.toString();
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
                System.out.println("FT:. METODO.actualizarTerminalSelloNueva(). Se ha actualizado con éxito");
                valido = true;
            } else {
                logErrorResponse(connection);
                valido = false;
            }

        } catch (Throwable e) {
            System.out.println("FT:: Error en METODO.actualizarTerminalSelloNueva(). "+e.getMessage());
            e.printStackTrace(System.out);
            valido = false;
        }
//        return valido;
    }
    
    // CONSULTA PARA ENCONTRAR LA SERIE DE TERMINALSELLO
    public JSONArray entregaRecepcionConsulta(String codigocomercializadora, String codigoterminal, Boolean activot, Boolean activo, int selloinicial, int sellofinal) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.factura/cargarfacturasbancos";

        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detalleterminalsello/paraserieentrec?";
        final int SUCCESS_CODE = 200;
        JSONArray retorno = new JSONArray();

        try {

            // Primero crea un URI y luego lo convierte a URL
            URI uri = new URI(direcc + "codigocomercializadora=" + codigocomercializadora + "&codigoterminal=" + codigoterminal
                    + "&activot=" + activot
                    + "&activo=" + activo
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

    public Boolean entregaRecpcionSellos(List<JSONObject> bodySello) {
        //String urlPath = "http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.factura/cargarfacturasbancos";

        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.terminalsello/entregarecepcion";
        final int SUCCESS_CODE = 200;
        Boolean valido = false;
        try {
            URI uri = new URI(direcc);
            url = uri.toURL();

            String respuesta;

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                respuesta = bodySello.toString();
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
                System.out.println("Se ha procesado con exito la entrega recepción");
                valido = true;
            } else {
                logErrorResponse(connection);
                valido = false;
            }

        } catch (Throwable e) {
            System.out.println("Error");
            e.printStackTrace(System.out);
            valido = false;
        }
        
        return valido;
    }



    // CONSULTA PARA ENCONTRAR LA SERIE DE TERMINALSELLO A USARSE EN LA GENERACIÓN DE UNA NOTA DE PEDIDO
    public List<Detalleterminalsello> sellosValidosParaNP(String codigocomercializadora, String codigoterminal, Integer selloinicial, Integer sellofinal) {
 
        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detalleterminalsello/sellosvalidosparanp?";
        final int SUCCESS_CODE = 200;
        JSONArray retorno = new JSONArray();
        listaDetalleTerminalSello = new ArrayList<>();
        Detalleterminalsello unDetalleTerminalSello = new Detalleterminalsello();
        DetalleterminalselloPK unDetalleTerminalSelloPK = new DetalleterminalselloPK();
        try {

            // Primero crea un URI y luego lo convierte a URL
            URI uri = new URI(direcc + "codigocomercializadora=" + codigocomercializadora + "codigoterminal=" + codigoterminal
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

            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject detSello = retorno.getJSONObject(indice);
                JSONObject detselloPK = detSello.getJSONObject("detalleterminalselloPK");
                unDetalleTerminalSelloPK.setCodigocomercializadora(detselloPK.getString("codigocomercializadora"));
                unDetalleTerminalSelloPK.setCodigoterminalrecibe(detselloPK.getString("codigoterminal"));
                unDetalleTerminalSelloPK.setSello(detselloPK.getBigInteger("sello"));
                
                unDetalleTerminalSello.setDetalleterminalselloPK(unDetalleTerminalSelloPK);
                unDetalleTerminalSello.setActivo(detSello.getBoolean("activo"));
             
                listaDetalleTerminalSello.add(unDetalleTerminalSello);
                unDetalleTerminalSello = new Detalleterminalsello();
                unDetalleTerminalSelloPK = new DetalleterminalselloPK();
            }
            
            if (connection.getResponseCode() != 200) {
                logErrorResponse(connection);
            }

        } catch (Throwable e) {
            System.out.println("FT:: ERROR EN SellosServicio.sellosValidosParaNP():: "+e.getMessage());
            e.printStackTrace(System.out);
        }
        return listaDetalleTerminalSello;
    }
    
    
}
