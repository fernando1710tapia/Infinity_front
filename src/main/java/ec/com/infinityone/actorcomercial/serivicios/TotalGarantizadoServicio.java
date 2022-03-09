/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.actorcomercial.serivicios;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Cliente;
import ec.com.infinityone.modeloWeb.Totalgarantizado;
import ec.com.infinityone.modeloWeb.TotalgarantizadoPK;
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
public class TotalGarantizadoServicio {

    /*
    Variable que alamcena un total garantizado
     */
    private Totalgarantizado totalGaran;
    /*
    Variable que almacena una lista de totales garantizados
     */
    private List<Totalgarantizado> listaTotalG;
    /*
    Variable que almacena un total garantizado PK
     */
    private TotalgarantizadoPK totalGaranPK;
    /*
    Variable quue alamacena un cliente
     */
    private Cliente cliente;

    public List<Totalgarantizado> obtenerTotalesGarantizado() {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.totalgarantizado");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.totalgarantizado");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaTotalG = new ArrayList<>();
            totalGaran = new Totalgarantizado();
            totalGaranPK = new TotalgarantizadoPK();
            cliente = new Cliente();
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
                JSONObject totalG = retorno.getJSONObject(indice);
                JSONObject totalGPK = totalG.getJSONObject("totalgarantizadoPK");
                JSONObject cli = totalG.getJSONObject("cliente");

                totalGaranPK.setCodigocomercializadora(totalGPK.getString("codigocomercializadora"));
                totalGaranPK.setCodigocliente(totalGPK.getString("codigocliente"));
                totalGaran.setTotalgarantizadoPK(totalGaranPK);

                cliente.setNombre(cli.getString("nombre"));
                totalGaran.setCliente(cliente);

                totalGaran.setTotaldeuda(totalG.getBigDecimal("totaldeuda"));
                totalGaran.setTotalgarantizado(totalG.getBigDecimal("totalgarantizado"));
                //totalGaran.setUsuarioactual(totalG.getString("usuarioactual"));

                listaTotalG.add(totalGaran);
                totalGaran = new Totalgarantizado();
                totalGaranPK = new TotalgarantizadoPK();
                cliente = new Cliente();

            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaTotalG;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaTotalG;
    }

    public List<Totalgarantizado> obtenerTotalesGarantizadoPorId(String codComer, String codCli) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.totalgarantizado/porId?codigocomercializadora=" + codComer + "&codigocliente=" + codCli);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.totalgarantizado/porId?codigocomercializadora=" + codComer + "&codigocliente=" + codCli);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaTotalG = new ArrayList<>();
            totalGaran = new Totalgarantizado();
            totalGaranPK = new TotalgarantizadoPK();
            cliente = new Cliente();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            if (!retorno.isNull(0)) {

                for (int indice = 0; indice < retorno.length(); indice++) {
                    JSONObject totalG = retorno.getJSONObject(indice);
                    JSONObject totalGPK = totalG.getJSONObject("totalgarantizadoPK");
                    JSONObject cli = totalG.getJSONObject("cliente");

                    totalGaranPK.setCodigocomercializadora(totalGPK.getString("codigocomercializadora"));
                    totalGaranPK.setCodigocliente(totalGPK.getString("codigocliente"));
                    totalGaran.setTotalgarantizadoPK(totalGaranPK);

                    cliente.setNombre(cli.getString("nombre"));
                    totalGaran.setCliente(cliente);

                    totalGaran.setTotaldeuda(totalG.getBigDecimal("totaldeuda"));
                    totalGaran.setTotalgarantizado(totalG.getBigDecimal("totalgarantizado"));
                    //totalGaran.setUsuarioactual(totalG.getString("usuarioactual"));

                    listaTotalG.add(totalGaran);
                    totalGaran = new Totalgarantizado();
                    totalGaranPK = new TotalgarantizadoPK();
                    cliente = new Cliente();

                }
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaTotalG;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaTotalG;
    }

}
