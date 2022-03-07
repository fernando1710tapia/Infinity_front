/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.preciosyfacturacion.servicios;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Facturadordespachador;
import ec.com.infinityone.modeloWeb.FacturadordespachadorPK;
import ec.com.infinityone.modeloWeb.Terminal;
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
public class FacturadorDespachadorServicio {
    /*
    Variable que almacena varios Listaprecios
     */
    private List<Facturadordespachador> listaFacturadordespachador;
    /*
    Objeto listaprecio
     */
    private Facturadordespachador facturadordespachador;
    
    private FacturadordespachadorPK facturadordespachadorPK;
    
    private Terminal terminal;
    
    
    public List<Facturadordespachador> obtenerFactDesp() {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.facturadordespachador");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.facturadordespachador");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaFacturadordespachador = new ArrayList<>();
            facturadordespachadorPK = new FacturadordespachadorPK();
            facturadordespachador = new Facturadordespachador();
            terminal = new Terminal();
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
                JSONObject listaP = retorno.getJSONObject(indice);
                JSONObject listaPK = listaP.getJSONObject("facturadordespachadorPK");
                JSONObject term = listaP.getJSONObject("terminal");
                facturadordespachadorPK.setCodigocomercializadora(listaPK.getString("codigocomercializadora"));
                facturadordespachadorPK.setCodigoterminal(listaPK.getString("codigoterminal"));
                facturadordespachadorPK.setCodigousuario(listaPK.getString("codigousuario"));
                terminal.setCodigo(term.getString("codigo"));
                terminal.setNombre(term.getString("nombre"));
                terminal.setActivo(term.getBoolean("activo"));
                terminal.setUsuarioactual(term.getString("usuarioactual"));
                facturadordespachador.setFacturadordespachadorPK(facturadordespachadorPK);                
                facturadordespachador.setActivo(listaP.getBoolean("activo"));
                facturadordespachador.setUsuarioactual(listaP.getString("usuarioactual"));
                facturadordespachador.setTerminal(terminal);
                listaFacturadordespachador.add(facturadordespachador);
                facturadordespachador = new Facturadordespachador();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
            return listaFacturadordespachador;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaFacturadordespachador;
    }
    
    
    
}
