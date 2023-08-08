/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.servicio.pedidosyfacturacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Temporalparacobrar;
import ec.com.infinityone.modelo.TemporalparacobrarPK;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author HP
 */
@LocalBean
@Stateless
public class TemporalCobrarServicios {
    /*
    Variable que almacena varios Temporalescobrar
     */
    private List<Temporalparacobrar> listaTemporales;
    /*
    Variable que almacena un Temporalcobrar
     */
    private Temporalparacobrar temporalcobros;
    /*
    Variable que almacena un TemporalcobrarPK
     */
    private TemporalparacobrarPK temporalcobrosPK;

    
    public void insertarTemporalCobros(Temporalparacobrar tempCobros){
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.temporalparacobrar");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.temporalparacobrar");
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(tempCobros);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();

            if (connection.getResponseCode() != 200) { 
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void eliminarRegistrosTemporales(String fechahoraproceso, String usuario, String codComer){
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.temporalparacobrar/deleteporUsuarioProceso?"
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.temporalparacobrar/deleteporUsuarioProceso?"
                    + "fechahoraproceso=" + fechahoraproceso + "&usuarioactual=" + usuario +"&codigocomercializadora=" + codComer);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            
//            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//            ObjectMapper mapper = new ObjectMapper();
//            String jsonStr = mapper.writeValueAsString(tempCobros);
//            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//            out.write(jsonStr.getBytes());
//            out.flush();
//            out.close();

            if (connection.getResponseCode() != 200) { 
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
