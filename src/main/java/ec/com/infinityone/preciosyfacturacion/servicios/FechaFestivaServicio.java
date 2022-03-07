/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.preciosyfacturacion.servicios;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Fechafestiva;
import ec.com.infinityone.modeloWeb.FechafestivaPK;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
public class FechaFestivaServicio {

    /*
    Variable que almacena varios Gravamenes
     */
    private List<Fechafestiva> listaFechafestiva;
    /*
    Objeto Gravamen
     */
    private Fechafestiva fechafestiva;
    /*
    Objeto GravamenPK
     */
    private FechafestivaPK fechafestivaPK;

    public List<Fechafestiva> obtenerFechafestiva(String codComer, String anio) {
        try {
            //URL url = new URL("http://200.93.248.121:8080/infinityone1/resources/ec.com.infinity.modelo.fechafestiva/porComerAnio?"
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.fechafestiva/porComerAnio?"
                    + "codigocomercializadora=" + codComer + "&anio=" + anio);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaFechafestiva = new ArrayList<>();
            fechafestiva = new Fechafestiva();
            fechafestivaPK = new FechafestivaPK();
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
                JSONObject ff = retorno.getJSONObject(indice);
                JSONObject ffPK = ff.getJSONObject("fechafestivaPK");

                fechafestivaPK.setCodigocomercializadora(ffPK.getString("codigocomercializadora"));
                Long lDateFes = ffPK.getLong("festivo");
                Date dateFes = new Date(lDateFes);
                fechafestivaPK.setFestivo(dateFes);

                fechafestiva.setFechafestivaPK(fechafestivaPK);

                fechafestiva.setActivo(ff.getBoolean("activo"));
                fechafestiva.setDescripcion(ff.getString("descripcion"));
                fechafestiva.setUsuarioactual(ff.getString("usuarioactual"));

                listaFechafestiva.add(fechafestiva);
                fechafestiva = new Fechafestiva();
                fechafestivaPK = new FechafestivaPK();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return listaFechafestiva;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaFechafestiva;
    }
}
