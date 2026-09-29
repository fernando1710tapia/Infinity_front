/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.servicio.catalogo;

import ec.com.infinityone.bean.TerminalBean;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.ObjetoNivel1;
import ec.com.infinityone.modelo.Terminal;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author HP
 */
@LocalBean
@Stateless
public class TerminalServicio {

    /*
    Variable para instanciar al objeto nivel 1
     */
    private List<Terminal> listaTerminales;
    /*
    Varibale para guardar terminales activos
     */
    private List<Terminal> listaTerminalesActivos;

    public List<Terminal> obtenerTerminal() {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.terminal");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.terminal");
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaTerminales = new ArrayList<>();
            Terminal term = new Terminal();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                if (!retorno.isNull(indice)) {
                    JSONObject terminal = retorno.getJSONObject(indice);
                    term.setCodigo(terminal.getString("codigo"));
                    term.setNombre(terminal.getString("nombre"));
                    term.setActivo(terminal.optBoolean("activo", false));
                    term.setUsuarioactual(terminal.optString("usuarioactual", ""));
                    term.setRecibirsolicitud(terminal.optBoolean("recibirsolicitud", true));
                    listaTerminales.add(term);
                    term = new Terminal();
                }
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            
            return listaTerminales;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaTerminales;
    }

    public List<Terminal> obtenerTerminalesActivos() {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.terminal");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.terminal");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaTerminalesActivos = new ArrayList<>();
            Terminal terminal = new Terminal();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                if (!retorno.isNull(indice)) {
                    JSONObject term = retorno.getJSONObject(indice);
                    if (term.getBoolean("activo")) {
                        terminal.setCodigo(term.getString("codigo"));
                        terminal.setNombre(term.getString("nombre"));
                        //terminal.setObjRelacionado(term.getString("codigo")+ " - " + term.getString("nombre"));
                        listaTerminalesActivos.add(terminal);
                        terminal = new Terminal();
                    }
                }

            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaTerminalesActivos;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaTerminalesActivos;
    }

    /**
     * Actualiza el campo recibirsolicitud de una terminal via PUT.
     * @param terminal objeto Terminal con todos los campos necesarios
     * @param recibirSolicitud nuevo valor del campo
     * @return código de respuesta HTTP (200 = éxito)
     */
    public int actualizarRecibirSolicitud(Terminal terminal, boolean recibirSolicitud) {
        // Intentar primero con /porId, si falla intentar con el endpoint raíz
        String direccPorId = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.terminal/porId";
        String direccRaiz  = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.terminal";
        final int SUCCESS_CODE = 200;
        int respuesta = -1;
        try {
            JSONObject body = new JSONObject();
            body.put("codigo", terminal.getCodigo());
            body.put("nombre", terminal.getNombre());
            body.put("activo", terminal.isActivo());
            body.put("usuarioactual", terminal.getUsuarioactual() != null ? terminal.getUsuarioactual() : "");
            body.put("recibirsolicitud", recibirSolicitud);

            System.out.println("FT::TerminalServicio - PUT recibirsolicitud=" + recibirSolicitud
                    + " terminal=" + terminal.getCodigo()
                    + " body=" + body.toString());

            // Intento 1: endpoint /porId
            URI uri = new URI(direccPorId);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(body.toString());
            }
            respuesta = connection.getResponseCode();
            System.out.println("FT::TerminalServicio - /porId HTTP " + respuesta);

            // Si /porId no funciona (404/405), intentar con el endpoint raíz
            if (respuesta == 404 || respuesta == 405) {
                System.out.println("FT::TerminalServicio - /porId falló, intentando endpoint raíz...");
                URI uri2 = new URI(direccRaiz);
                URL url2 = uri2.toURL();
                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                connection2.setDoOutput(true);
                connection2.setRequestMethod("PUT");
                connection2.setRequestProperty("Content-type", "application/json");
                try (OutputStreamWriter writer2 = new OutputStreamWriter(connection2.getOutputStream())) {
                    writer2.write(body.toString());
                }
                respuesta = connection2.getResponseCode();
                System.out.println("FT::TerminalServicio - endpoint raíz HTTP " + respuesta);
                if (respuesta != SUCCESS_CODE) {
                    // Leer el body del error
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection2.getErrorStream() != null ? connection2.getErrorStream() : connection2.getInputStream()))) {
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) sb.append(line);
                        System.out.println("FT::TerminalServicio - respuesta error: " + sb.toString());
                    }
                }
            } else if (respuesta != SUCCESS_CODE) {
                // Leer el body del error de /porId
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream() != null ? connection.getErrorStream() : connection.getInputStream()))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line);
                    System.out.println("FT::TerminalServicio - respuesta error /porId: " + sb.toString());
                }
            }

            System.out.println("FT::TerminalServicio - resultado final HTTP " + respuesta);
        } catch (Throwable e) {
            System.out.println("FT::TerminalServicio - EXCEPCION actualizarRecibirSolicitud terminal="
                    + (terminal != null ? terminal.getCodigo() : "null"));
            e.printStackTrace(System.out);
        }
        return respuesta;
    }
}
