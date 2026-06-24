/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.servicio.catalogo;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Formapago;
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
 * @author SonyVaio
 */
@LocalBean
@Stateless
public class FormapagoServicio {
    /*
    Variable que almacena varios Formapagos
     */
    private List<Formapago> listaFormapagos;
    /*
    Objeto formapago
     */
    private Formapago formapago;
    
    public List<Formapago> obtenerFormapago() {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.formapago");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.formapago");
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaFormapagos = new ArrayList<>();
            formapago = new Formapago();
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
                JSONObject fpago = retorno.getJSONObject(indice);
                formapago.setCodigo(fpago.getString("codigo"));
                formapago.setNombre(fpago.getString("nombre"));
                formapago.setActivo(fpago.getBoolean("activo"));
                formapago.setUsuarioactual(fpago.getString("usuarioactual"));
                listaFormapagos.add(formapago);
                formapago = new Formapago();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            
            return listaFormapagos;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaFormapagos;
    }

    public List<Formapago> getListaFormapagos() {
        return listaFormapagos;
    }

    public void setListaFormapagos(List<Formapago> listaFormapagos) {
        this.listaFormapagos = listaFormapagos;
    }

    public Formapago getFormapago() {
        return formapago;
    }

    public void setFormapago(Formapago formapago) {
        this.formapago = formapago;
    }
    
    
}
