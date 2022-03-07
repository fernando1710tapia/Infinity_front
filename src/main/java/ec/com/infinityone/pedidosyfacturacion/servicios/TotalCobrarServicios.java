/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.pedidosyfacturacion.servicios;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.TotalParaCobrar;
import java.io.BufferedReader;
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
public class TotalCobrarServicios {
    /*
    Variable que almacena varios TotalParaCobrar
     */
    private List<TotalParaCobrar> listaTotalCobro;
    /*
    Variable que almacena un TotalParaCobrar
     */
    private TotalParaCobrar totalCobro;
    
    public List<TotalParaCobrar> obtenerTemporalParaCobrar(String fechahoraproceso, String usuario, String codComer){
        try {

                //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.temporalparacobrar/Totalparacobrar?"
                URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.temporalparacobrar/Totalparacobrar?"
                    + "fechahoraproceso=" + fechahoraproceso + "&usuarioactual=" + usuario + "&codigocomercializadora=" + codComer);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaTotalCobro = new ArrayList<>();
            totalCobro = new TotalParaCobrar();
  

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
                JSONObject totalC = retorno.getJSONObject(indice);
                
                totalCobro.setBanco(totalC.getString("banco"));
                totalCobro.setFechavencimiento(totalC.getString("fechavencimiento"));
                totalCobro.setValortotal(totalC.getBigDecimal("valortotal"));
                if(!totalC.isNull("valortotalconrubro")){
                    totalCobro.setValortotalconrubro(totalC.getBigDecimal("valortotalconrubro"));
                }
                totalCobro.setFacturas(totalC.getInt("facturas"));
                                
                listaTotalCobro.add(totalCobro);
                
                totalCobro = new TotalParaCobrar();
            }
            
            if (connection.getResponseCode() != 200) { 
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (Throwable e) {
            System.out.println("Error:" + e);
        }
        return listaTotalCobro;
    }
    
    
    
}
