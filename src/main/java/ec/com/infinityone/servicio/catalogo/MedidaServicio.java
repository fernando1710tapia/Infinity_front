/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.servicio.catalogo;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Medida;
import java.util.List;
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
public class MedidaServicio {
    /*
    Variable que almacena varios Bancos
     */
    private List<Medida> listaMedidas;
    /*
    Objeto banco
     */
    private Medida medida;
    
    public List<Medida> obtenerMedida() {
        try {

            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.medida");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.medida");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            listaMedidas = new ArrayList<>();
            medida = new Medida();
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
                JSONObject areaM = retorno.getJSONObject(indice);
                medida.setCodigo(areaM.getString("codigo"));
                medida.setNombre(areaM.getString("nombre"));
                medida.setAbreviacion(areaM.getString("abreviacion"));
                listaMedidas.add(medida);
                medida = new Medida();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return listaMedidas;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaMedidas;
    }
    
}
