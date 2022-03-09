/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.preciosyfacturacion.servicios;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Clienterubrotercero;
import ec.com.infinityone.modeloWeb.ClienterubroterceroPK;
import ec.com.infinityone.modeloWeb.Rubrotercero;
import ec.com.infinityone.modeloWeb.RubroterceroPK;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
public class ClienterubroterceroServicio {

    /*
    Variable que almacena varios listaclienterubrotercero
     */
    private List<Clienterubrotercero> listaclienterubrotercero;
    /*
    Objeto clienterubrotercero
     */
    private Clienterubrotercero clienterubrotercero;

    private ClienterubroterceroPK clienterubroterceroPK;
    /*
    Variable que alamacena un objeto rubrotercero
     */
    private Rubrotercero rubroTercero;
    /*
    Variable que alamacena un objeto rubroterceroPK
     */
    private RubroterceroPK rubroTerceroPK;

    public List<Clienterubrotercero> obtenerClienteRubrotercero(String codComer) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.clienterubrotercero/comer?codigocomercializadora=" + codComer);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.clienterubrotercero/comer?codigocomercializadora=" + codComer);
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaclienterubrotercero = new ArrayList<>();
            clienterubroterceroPK = new ClienterubroterceroPK();
            clienterubrotercero = new Clienterubrotercero();
            rubroTercero = new Rubrotercero();

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

                JSONObject cliRubro = retorno.getJSONObject(indice);
                JSONObject cliRubroPK = cliRubro.getJSONObject("clienterubroterceroPK");
                JSONObject rubro = cliRubro.getJSONObject("rubrotercero");
                //JSONObject rubroPK = rubro.getJSONObject("rubroterceroPK");

                rubroTercero.setNombre(rubro.getString("nombre"));
                clienterubroterceroPK.setCodigo(cliRubroPK.getInt("codigorubrotercero"));
                clienterubroterceroPK.setCodigocomercializadora(cliRubroPK.getString("codigocomercializadora"));
                clienterubroterceroPK.setCodigocliente(cliRubroPK.getString("codigocliente"));
                clienterubrotercero.setClienterubroterceroPK(clienterubroterceroPK);
                clienterubrotercero.setValor(cliRubro.getBigDecimal("valor"));
                clienterubrotercero.setCuotas(cliRubro.getInt("cuotas"));
                clienterubrotercero.setTipocobro(cliRubro.getString("tipocobro"));
                Long dateStrIC = cliRubro.getLong("fechainiciocobro");
                Date dateIC = new Date(dateStrIC);
                String fechaIniCobro = date.format(dateIC);
                clienterubrotercero.setFechainiciocobro(dateIC);
                clienterubrotercero.setActivo(cliRubro.getBoolean("activo"));
                clienterubrotercero.setUsuarioactual(cliRubro.getString("usuarioactual"));
                clienterubrotercero.setRubrotercero(rubroTercero);
                listaclienterubrotercero.add(clienterubrotercero);
                clienterubrotercero = new Clienterubrotercero();
                clienterubroterceroPK = new ClienterubroterceroPK();
                rubroTercero = new Rubrotercero();
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaclienterubrotercero;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaclienterubrotercero;
    }

    public List<Clienterubrotercero> obtenerClienteRubroterceroPorRubro(String codComer, long codRubro) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.clienterubrotercero/rubro?codigorubrotercero=" + codRubro);            
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.clienterubrotercero/rubro?codigocomercializadora=" + codComer + "&codigorubrotercero=" + codRubro);            

            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaclienterubrotercero = new ArrayList<>();
            clienterubroterceroPK = new ClienterubroterceroPK();
            clienterubrotercero = new Clienterubrotercero();
            rubroTercero = new Rubrotercero();

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

                JSONObject cliRubro = retorno.getJSONObject(indice);
                JSONObject cliRubroPK = cliRubro.getJSONObject("clienterubroterceroPK");
                JSONObject rubro = cliRubro.getJSONObject("rubrotercero");
                //JSONObject rubroPK = rubro.getJSONObject("rubroterceroPK");

                rubroTercero.setNombre(rubro.getString("nombre"));
                clienterubroterceroPK.setCodigo(cliRubroPK.getInt("codigorubrotercero"));
                clienterubroterceroPK.setCodigocomercializadora(cliRubroPK.getString("codigocomercializadora"));
                clienterubroterceroPK.setCodigocliente(cliRubroPK.getString("codigocliente"));
                clienterubrotercero.setClienterubroterceroPK(clienterubroterceroPK);
                clienterubrotercero.setValor(cliRubro.getBigDecimal("valor"));
                clienterubrotercero.setCuotas(cliRubro.getInt("cuotas"));
                clienterubrotercero.setTipocobro(cliRubro.getString("tipocobro"));
                Long dateStrIC = cliRubro.getLong("fechainiciocobro");
                Date dateIC = new Date(dateStrIC);
                String fechaIniCobro = date.format(dateIC);
                clienterubrotercero.setFechainiciocobro(dateIC);
                clienterubrotercero.setActivo(cliRubro.getBoolean("activo"));
                clienterubrotercero.setUsuarioactual(cliRubro.getString("usuarioactual"));
                clienterubrotercero.setRubrotercero(rubroTercero);
                listaclienterubrotercero.add(clienterubrotercero);
                clienterubrotercero = new Clienterubrotercero();
                clienterubroterceroPK = new ClienterubroterceroPK();
                rubroTercero = new Rubrotercero();
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaclienterubrotercero;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaclienterubrotercero;
    }

}
