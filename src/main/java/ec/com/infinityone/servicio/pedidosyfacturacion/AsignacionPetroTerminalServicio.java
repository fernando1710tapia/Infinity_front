/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.servicio.pedidosyfacturacion;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Asignacionpetroterminal;
import ec.com.infinityone.modelo.AsignacionpetroterminalPK;
import ec.com.infinityone.modelo.Terminal;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 * Servicio para consultar y gestionar la asignación de Petro Terminal
 */
@LocalBean
@Stateless
public class AsignacionPetroTerminalServicio extends ReusableBean {

    private static final Logger LOG = Logger.getLogger(AsignacionPetroTerminalServicio.class.getName());

    /**
     * Consulta la asignación para una comercializadora, terminal y fecha dados.
     *
     * @param codComer Código de comercializadora
     * @param codTerminal Código de terminal
     * @param fecha Fecha de despacho
     * @return Asignacionpetroterminal encontrada o null si no existe
     */
    public Asignacionpetroterminal obtenerAsignacion(String codComer, String codTerminal, Date fecha) {
        if (codComer == null || codTerminal == null || fecha == null) {
            return null;
        }

        try {
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String fechaStr = df.format(fecha);
            String fechaParam = URLEncoder.encode(fechaStr, "UTF-8");

            String endpoint = Fichero.getRUTASERVICIOSPERSISTENCIA().trim()
                    + "ec.com.infinity.modelo.asignacionpetroterminal/porId?codigocomercializadora="
                    + codComer.trim() + "&codigoterminal=" + codTerminal.trim() + "&fecha=" + fechaParam;

            URI uri = new URI(endpoint);
            url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                JSONObject responseJson = new JSONObject(sb.toString());
                JSONArray retorno = responseJson.optJSONArray("retorno");

                if (retorno != null && retorno.length() > 0 && !retorno.isNull(0)) {
                    JSONObject obj = retorno.getJSONObject(0);
                    return parsearAsignacion(obj);
                }
            } else {
                LOG.log(Level.WARNING, "Error al consultar asignacion petro terminal: HTTP {0}", responseCode);
            }

        } catch (Throwable e) {
            LOG.log(Level.SEVERE, "Excepcion al obtener asignacion petro terminal", e);
        }

        return null;
    }

    /**
     * Envía una asignación para ser creada o actualizada vía PUT en gestionarasignacion.
     *
     * @param asignacion Datos de asignación a persistir
     * @return Código de respuesta HTTP
     */
    public int gestionarAsignacion(Asignacionpetroterminal asignacion) {
        if (asignacion == null || asignacion.getAsignacionpetroterminalPK() == null) {
            this.dialogo(FacesMessage.SEVERITY_WARN, "No hay datos de asignación para guardar");
            return 400;
        }

        int respuestaCodigo = 500;
        try {
            asignacion.calcularTotales();

            String endpoint = Fichero.getRUTASERVICIOSPERSISTENCIA().trim()
                    + "ec.com.infinity.modelo.asignacionpetroterminal/gestionarasignacion";

            URI uri = new URI(endpoint);
            url = uri.toURL();

            JSONObject body = construirJsonAsignacion(asignacion);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");

            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8")) {
                writer.write(body.toString());
                writer.flush();
            }

            respuestaCodigo = connection.getResponseCode();
            if (respuestaCodigo == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(sb.toString());
                String devMsg = jsonResponse.optString("developerMessage", "Asignación actualizada correctamente");
                this.dialogo(FacesMessage.SEVERITY_INFO, devMsg);
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getErrorStream() != null ? connection.getErrorStream() : connection.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                String devMsg = "No se pudo actualizar la asignación de Petro Terminal (HTTP " + respuestaCodigo + ")";
                try {
                    JSONObject errJson = new JSONObject(sb.toString());
                    if (errJson.has("developerMessage") && !errJson.isNull("developerMessage")) {
                        devMsg = errJson.getString("developerMessage");
                    } else if (errJson.has("errorMessage") && !errJson.isNull("errorMessage")) {
                        devMsg = errJson.getString("errorMessage");
                    }
                } catch (Exception ex) {
                    // Mantener devMsg por defecto
                }
                this.dialogo(FacesMessage.SEVERITY_ERROR, devMsg);
            }

        } catch (Throwable e) {
            LOG.log(Level.SEVERE, "Excepcion al gestionar asignacion petro terminal", e);
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Error de comunicación con el servicio de asignación: " + e.getMessage());
        }

        return respuestaCodigo;
    }

    private JSONObject construirJsonAsignacion(Asignacionpetroterminal asignacion) {
        JSONObject body = new JSONObject();

        AsignacionpetroterminalPK pk = asignacion.getAsignacionpetroterminalPK();
        JSONObject pkJson = new JSONObject();
        pkJson.put("codigocomercializadora", pk.getCodigocomercializadora());
        pkJson.put("codigoterminal", pk.getCodigoterminal());
        pkJson.put("fecha", pk.getFecha() != null ? pk.getFecha().getTime() : System.currentTimeMillis());
        body.put("asignacionpetroterminalPK", pkJson);

        // EXTRA
        body.put("eppasiginicialsolicitadoext", valorBigDecimal(asignacion.getEppasiginicialsolicitadoext()));
        body.put("eppasiginicialautorizadoext", valorBigDecimal(asignacion.getEppasiginicialautorizadoext()));
        body.put("eppasigadicionalsolicitadoext", valorBigDecimal(asignacion.getEppasigadicionalsolicitadoext()));
        body.put("eppasigadicionalautorizadoext", valorBigDecimal(asignacion.getEppasigadicionalautorizadoext()));
        body.put("eppasigtotalsolicitadoext", valorBigDecimal(asignacion.getEppasigtotalsolicitadoext()));
        body.put("eppasigtotalautorizadoext", valorBigDecimal(asignacion.getEppasigtotalautorizadoext()));

        // DIESEL
        body.put("eppasiginicialsolicitadodie", valorBigDecimal(asignacion.getEppasiginicialsolicitadodie()));
        body.put("eppasiginicialautorizadodie", valorBigDecimal(asignacion.getEppasiginicialautorizadodie()));
        body.put("eppasigadicionalsolicitadodie", valorBigDecimal(asignacion.getEppasigadicionalsolicitadodie()));
        body.put("eppasigadicionalautorizadodie", valorBigDecimal(asignacion.getEppasigadicionalautorizadodie()));
        body.put("eppasigtotalsolicitadodie", valorBigDecimal(asignacion.getEppasigtotalsolicitadodie()));
        body.put("eppasigtotalautorizadodie", valorBigDecimal(asignacion.getEppasigtotalautorizadodie()));

        // SÚPER
        body.put("eppasiginicialsolicitadosup", valorBigDecimal(asignacion.getEppasiginicialsolicitadosup()));
        body.put("eppasiginicialautorizadosup", valorBigDecimal(asignacion.getEppasiginicialautorizadosup()));
        body.put("eppasigadicionalsolicitadosup", valorBigDecimal(asignacion.getEppasigadicionalsolicitadosup()));
        body.put("eppasigadicionalautorizadosup", valorBigDecimal(asignacion.getEppasigadicionalautorizadosup()));
        body.put("eppasigtotalsolicitadosup", valorBigDecimal(asignacion.getEppasigtotalsolicitadosup()));
        body.put("eppasigtotalautorizadosup", valorBigDecimal(asignacion.getEppasigtotalautorizadosup()));

        body.put("usuarioactual", asignacion.getUsuarioactual() != null ? asignacion.getUsuarioactual() : "FT");

        JSONObject termJson = new JSONObject();
        String codTerm = pk.getCodigoterminal();
        if (asignacion.getTerminal() != null && asignacion.getTerminal().getCodigo() != null) {
            codTerm = asignacion.getTerminal().getCodigo();
        }
        termJson.put("codigo", codTerm);
        body.put("terminal", termJson);

        return body;
    }

    private double valorBigDecimal(BigDecimal bd) {
        return bd != null ? bd.doubleValue() : 0.0;
    }

    private Asignacionpetroterminal parsearAsignacion(JSONObject obj) {
        Asignacionpetroterminal a = new Asignacionpetroterminal();

        JSONObject pkJson = obj.optJSONObject("asignacionpetroterminalPK");
        if (pkJson != null) {
            AsignacionpetroterminalPK pk = new AsignacionpetroterminalPK();
            pk.setCodigocomercializadora(pkJson.optString("codigocomercializadora", ""));
            pk.setCodigoterminal(pkJson.optString("codigoterminal", ""));
            long fechaMillis = pkJson.optLong("fecha", 0);
            if (fechaMillis > 0) {
                pk.setFecha(new Date(fechaMillis));
            }
            a.setAsignacionpetroterminalPK(pk);
        }

        // EXTRA
        a.setEppasiginicialsolicitadoext(obtenerBigDecimal(obj, "eppasiginicialsolicitadoext"));
        a.setEppasiginicialautorizadoext(obtenerBigDecimal(obj, "eppasiginicialautorizadoext"));
        a.setEppasigadicionalsolicitadoext(obtenerBigDecimal(obj, "eppasigadicionalsolicitadoext"));
        a.setEppasigadicionalautorizadoext(obtenerBigDecimal(obj, "eppasigadicionalautorizadoext"));
        a.setEppasigtotalsolicitadoext(obtenerBigDecimal(obj, "eppasigtotalsolicitadoext"));
        a.setEppasigtotalautorizadoext(obtenerBigDecimal(obj, "eppasigtotalautorizadoext"));

        // DIESEL
        a.setEppasiginicialsolicitadodie(obtenerBigDecimal(obj, "eppasiginicialsolicitadodie"));
        a.setEppasiginicialautorizadodie(obtenerBigDecimal(obj, "eppasiginicialautorizadodie"));
        a.setEppasigadicionalsolicitadodie(obtenerBigDecimal(obj, "eppasigadicionalsolicitadodie"));
        a.setEppasigadicionalautorizadodie(obtenerBigDecimal(obj, "eppasigadicionalautorizadodie"));
        a.setEppasigtotalsolicitadodie(obtenerBigDecimal(obj, "eppasigtotalsolicitadodie"));
        a.setEppasigtotalautorizadodie(obtenerBigDecimal(obj, "eppasigtotalautorizadodie"));

        // SÚPER
        a.setEppasiginicialsolicitadosup(obtenerBigDecimal(obj, "eppasiginicialsolicitadosup"));
        a.setEppasiginicialautorizadosup(obtenerBigDecimal(obj, "eppasiginicialautorizadosup"));
        a.setEppasigadicionalsolicitadosup(obtenerBigDecimal(obj, "eppasigadicionalsolicitadosup"));
        a.setEppasigadicionalautorizadosup(obtenerBigDecimal(obj, "eppasigadicionalautorizadosup"));
        a.setEppasigtotalsolicitadosup(obtenerBigDecimal(obj, "eppasigtotalsolicitadosup"));
        a.setEppasigtotalautorizadosup(obtenerBigDecimal(obj, "eppasigtotalautorizadosup"));

        a.setUsuarioactual(obj.optString("usuarioactual", ""));

        JSONObject termJson = obj.optJSONObject("terminal");
        if (termJson != null) {
            Terminal t = new Terminal();
            t.setCodigo(termJson.optString("codigo", ""));
            t.setNombre(termJson.optString("nombre", ""));
            a.setTerminal(t);
        }

        return a;
    }

    private BigDecimal obtenerBigDecimal(JSONObject obj, String campo) {
        if (obj.has(campo) && !obj.isNull(campo)) {
            return BigDecimal.valueOf(obj.optDouble(campo, 0.0));
        }
        return BigDecimal.ZERO;
    }
}
