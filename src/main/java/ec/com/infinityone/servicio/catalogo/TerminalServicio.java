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
import java.net.HttpURLConnection;
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
                    //term.setObjRelacionado(terminal.getString("codigo") + " - " + terminal.getString("nombre"));
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

}
