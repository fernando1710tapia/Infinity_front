/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.servicio.preciosyfacturacion;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Listaprecioterminalproducto;
import ec.com.infinityone.modelo.ListaprecioterminalproductoPK;
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
public class ListaprecioterminalproductoServicio {
    /*
    Variable que almacena varios Listaprecios
     */
    private List<Listaprecioterminalproducto> listaListaprecioterminalproducto;
    /*
    Objeto listaprecio
     */
    private Listaprecioterminalproducto listaprecioterminalproducto;
    
    private ListaprecioterminalproductoPK listaprecioterminalproductoK;
    
    
    public List<Listaprecioterminalproducto> obtenerListaprecioterminalprod(long codListprec) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecioterminalproducto/porLista?codigolistaprecio=" + codListprec);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecioterminalproducto/porLista?codigolistaprecio=" + codListprec);
    
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaListaprecioterminalproducto = new ArrayList<>();
            listaprecioterminalproductoK = new ListaprecioterminalproductoPK();
            listaprecioterminalproducto = new Listaprecioterminalproducto();
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
                JSONObject listaPK = listaP.getJSONObject("listaprecioterminalproductoPK");                
                listaprecioterminalproductoK.setCodigocomercializadora(listaPK.getString("codigocomercializadora"));
                listaprecioterminalproductoK.setCodigolistaprecio(listaPK.getLong("codigolistaprecio"));
                listaprecioterminalproductoK.setCodigoterminal(listaPK.getString("codigoterminal"));
                listaprecioterminalproductoK.setCodigoproducto(listaPK.getString("codigoproducto"));
                listaprecioterminalproductoK.setCodigomedida(listaPK.getString("codigomedida"));
                listaprecioterminalproducto.setListaprecioterminalproductoPK(listaprecioterminalproductoK);
                listaprecioterminalproducto.setMargenporcentaje(listaP.getBigDecimal("margenporcentaje"));
                listaprecioterminalproducto.setMargenvalorcomercializadora(listaP.getBigDecimal("margenvalorcomercializadora"));
                listaprecioterminalproducto.setUsuarioactual(listaP.getString("usuarioactual"));
                
                listaListaprecioterminalproducto.add(listaprecioterminalproducto);
                listaprecioterminalproducto = new Listaprecioterminalproducto();
                listaprecioterminalproductoK = new ListaprecioterminalproductoPK();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
            return listaListaprecioterminalproducto;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaListaprecioterminalproducto;
    }
   
}
