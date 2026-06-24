/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.servicio.preciosyfacturacion;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Gravamen;
import ec.com.infinityone.modelo.GravamenPK;
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
public class GravamenServicio {
    /*
    Variable que almacena varios Gravamenes
     */
    private List<Gravamen> listaGravamen;
    /*
    Objeto Gravamen
     */
    private Gravamen gravamen;
    /*
    Objeto GravamenPK
    */
    private GravamenPK gravamenPK;
    
    public List<Gravamen> obtenerGravamenes(String codComer) {
        try { 
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.gravamen/comer?codigocomercializadora=" + codComer);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.gravamen/comer?codigocomercializadora=" + codComer);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaGravamen = new ArrayList<>();
            gravamen = new Gravamen();
            gravamenPK = new GravamenPK();
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
                JSONObject grav = retorno.getJSONObject(indice);
                JSONObject gravPK = grav.getJSONObject("gravamenPK");
                
                gravamenPK.setCodigocomercializadora(gravPK.getString("codigocomercializadora"));
                gravamenPK.setCodigo(gravPK.getString("codigo"));
                
                gravamen.setGravamenPK(gravamenPK);
                
                gravamen.setNombre(grav.getString("nombre"));
                gravamen.setActivo(grav.getBoolean("activo"));
                gravamen.setSeimprime(grav.getBoolean("seimprime"));
                gravamen.setFormulavalor(grav.getString("formulavalor"));
                gravamen.setValordefecto(grav.getBigDecimal("valordefecto"));
                gravamen.setSecuencial(grav.getInt("secuencial"));
                gravamen.setUsuarioactual(grav.getString("usuarioactual"));                
                
                listaGravamen.add(gravamen);
                gravamen = new Gravamen();
                gravamenPK = new GravamenPK();
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return listaGravamen;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaGravamen;
    }
}
