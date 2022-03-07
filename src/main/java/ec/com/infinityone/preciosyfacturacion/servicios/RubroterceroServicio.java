/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.preciosyfacturacion.servicios;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Rubrotercero;
import ec.com.infinityone.modeloWeb.RubroterceroPK;
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
public class RubroterceroServicio {

    /*
    Variable que almacena varios Listaprecios
     */
    private List<Rubrotercero> listaRubrotercero;
    /*
    Objeto listaprecio
     */
    private Rubrotercero rubrotercero;

    private RubroterceroPK rubroterceroPK;

    public List<Rubrotercero> obtenerRubrotercero(String codComer) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.rubrotercero/comer?codigocomercializadora=" + codComer);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.rubrotercero/comer?codigocomercializadora=" + codComer);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaRubrotercero = new ArrayList<>();
            rubroterceroPK = new RubroterceroPK();
            rubrotercero = new Rubrotercero();

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
                JSONObject rubro = retorno.getJSONObject(indice);
                JSONObject rubroPK = rubro.getJSONObject("rubroterceroPK");

                rubroterceroPK.setCodigo(rubroPK.getInt("codigo"));
                rubroterceroPK.setCodigocomercializadora(rubroPK.getString("codigocomercializadora"));
                rubrotercero.setRubroterceroPK(rubroterceroPK);
                rubrotercero.setNombre(rubro.getString("nombre"));
                rubrotercero.setActivo(rubro.getBoolean("activo"));
                rubrotercero.setCodigocontable(rubro.getString("codigocontable"));
                rubrotercero.setTipo(rubro.getString("tipo"));
                rubrotercero.setUsuarioactual(rubro.getString("usuarioactual"));

                listaRubrotercero.add(rubrotercero);
                rubrotercero = new Rubrotercero();
                rubroterceroPK = new RubroterceroPK();
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaRubrotercero;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaRubrotercero;
    }

}
