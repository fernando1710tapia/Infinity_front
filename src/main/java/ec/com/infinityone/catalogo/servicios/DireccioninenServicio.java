/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.catalogo.servicios;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Direccioninen;
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
public class DireccioninenServicio {
    /*
    Variable que almacena varios Direccions
     */
    private List<Direccioninen> listaDireccioninen;
    /*
    Objeto Direccion
     */
    private Direccioninen direccioninen;
    
    public List<Direccioninen> obtenerDireccioninen() {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.direccioninen");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.direccioninen");
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaDireccioninen = new ArrayList<>();
            direccioninen = new Direccioninen();
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
                JSONObject direcc = retorno.getJSONObject(indice);
                direccioninen.setCodigo(direcc.getString("codigo"));
                direccioninen.setNombre(direcc.getString("nombre"));
                direccioninen.setActivo(direcc.getBoolean("activo"));
                direccioninen.setUsuarioactual("usuarioactual");
                listaDireccioninen.add(direccioninen);
                direccioninen = new Direccioninen();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            
            return listaDireccioninen;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaDireccioninen;
    }

    public List<Direccioninen> getListaDireccioninen() {
        return listaDireccioninen;
    }

    public void setListaDireccioninen(List<Direccioninen> listaDireccioninen) {
        this.listaDireccioninen = listaDireccioninen;
    }

    public Direccioninen getDireccioninen() {
        return direccioninen;
    }

    public void setDireccioninen(Direccioninen direccioninen) {
        this.direccioninen = direccioninen;
    }
    
    
}
