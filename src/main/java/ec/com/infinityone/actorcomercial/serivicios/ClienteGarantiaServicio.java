/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.actorcomercial.serivicios;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Banco;
import ec.com.infinityone.modeloWeb.Clientegarantia;
import ec.com.infinityone.modeloWeb.ClientegarantiaPK;
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
public class ClienteGarantiaServicio {

    /*
    Variable que almacena varias Comercializaros
     */
    private List<Clientegarantia> listaClientegarantia;

    private Clientegarantia clientegarantia;

    private ClientegarantiaPK clientegarantiaPK;
    
    private  int secuencial;
    
    private Banco banco;
    
    public List<Clientegarantia> obtenerClientes(String codComer, String codCliente, String codBanco, String numero, String secuencial) {
        //urlInfinity = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.areamercadeo
        //https://www.supertech.ec:8443/infinityone1/resources/usuario/login?user=Ftapia&password=123456";
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));  
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.clientegarantia/porId?codigocomercializadora="
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.clientegarantia/porId?codigocomercializadora="
                              + codComer + "?codigocliente=" + codCliente + "?codigobanco=" + codBanco + "?numero=" + numero + "?secuencial=" + secuencial);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            //connection.setRequestProperty("Authorization", token);
            //connection.setRequestProperty("Access-Control-Allow-Headers", "Authorization");
            //connection.setRequestProperty("Accept-Charset", "utf-8");
            //connection.setRequestProperty("Accept-Encoding", "gzip");
            //connection.setRequestProperty("Accept-Language", "en-US");
            //connection.setRequestProperty("Access-Control-Allow-Origin", "*");

            listaClientegarantia = new ArrayList<>();
            clientegarantia = new Clientegarantia();
            clientegarantiaPK = new ClientegarantiaPK();

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
                JSONObject cligar = retorno.getJSONObject(indice);
                JSONObject cligarPK = cligar.getJSONObject("clientegarantiaPK");
//                JSONObject ban = cligar.getJSONObject("banco");
//                JSONObject cli = cligar.getJSONObject("cliente");

                clientegarantiaPK.setCodigocomercializadora(cligarPK.getString("codigocomercializadora"));
                clientegarantiaPK.setCodigocliente(cligarPK.getString("codigocliente"));
                clientegarantiaPK.setCodigobanco(cligarPK.getString("codigobanco"));
                clientegarantiaPK.setNumero(cligarPK.getString("numero"));
                clientegarantiaPK.setSecuencial(cligarPK.getInt("secuencial"));
                clientegarantia.setClientegarantiaPK(clientegarantiaPK);
                clientegarantia.setActivo(cligar.getBoolean("activo"));
                Long lDateIni = cligar.getLong("fechainiciovigencia");
                Date dateIni = new Date(lDateIni);
                clientegarantia.setFechainiciovigencia(dateIni);
                Long lDateFin = cligar.getLong("fechafinvigencia");
                Date dateFin = new Date(lDateFin);
                clientegarantia.setFechafinvigencia(dateFin);
                clientegarantia.setValor(cligar.getBigDecimal("valor"));
                clientegarantia.setObservacion(cligar.getString("observacion"));
                clientegarantia.setUsuarioactual(cligar.getString("usuarioactual"));
                
                listaClientegarantia.add(clientegarantia);
                clientegarantia = new Clientegarantia();
                clientegarantiaPK = new ClientegarantiaPK();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
            return listaClientegarantia;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaClientegarantia;
    }

    public List<Clientegarantia> obtenerClientesComer(String codComer, String codCliente) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.clientegarantia/Comercli?codigocomercializadora="
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.clientegarantia/Comercli?codigocomercializadora="           
                              + codComer + "&codigocliente=" + codCliente);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            listaClientegarantia = new ArrayList<>();
            clientegarantia = new Clientegarantia();
            clientegarantiaPK = new ClientegarantiaPK();
            banco = new Banco();

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
                JSONObject cligar = retorno.getJSONObject(indice);
                JSONObject cligarPK = cligar.getJSONObject("clientegarantiaPK");
                JSONObject ban = cligar.getJSONObject("banco");
//                JSONObject cli = cligar.getJSONObject("cliente");
                banco.setCodigo(ban.getString("codigo"));
                banco.setNombre(ban.getString("nombre"));
                
                clientegarantia.setBanco(banco);

                clientegarantiaPK.setCodigocomercializadora(cligarPK.getString("codigocomercializadora"));
                clientegarantiaPK.setCodigocliente(cligarPK.getString("codigocliente"));
                clientegarantiaPK.setCodigobanco(cligarPK.getString("codigobanco"));
                clientegarantiaPK.setNumero(cligarPK.getString("numero"));
                clientegarantiaPK.setSecuencial(cligarPK.getInt("secuencial"));
                clientegarantia.setClientegarantiaPK(clientegarantiaPK);
                clientegarantia.setActivo(cligar.getBoolean("activo"));
                Long lDateIni = cligar.getLong("fechainiciovigencia");
                Date dateIni = new Date(lDateIni);
                clientegarantia.setFechainiciovigencia(dateIni);
                Long lDateFin = cligar.getLong("fechafinvigencia");
                Date dateFin = new Date(lDateFin);
                clientegarantia.setFechafinvigencia(dateFin);
                clientegarantia.setValor(cligar.getBigDecimal("valor"));
                clientegarantia.setObservacion(cligar.getString("observacion"));
                clientegarantia.setUsuarioactual(cligar.getString("usuarioactual"));
                
                listaClientegarantia.add(clientegarantia);
                clientegarantia = new Clientegarantia();
                clientegarantiaPK = new ClientegarantiaPK();
                banco = new Banco();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
            return listaClientegarantia;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaClientegarantia;
    }
   
    public int obtenerUltimaSec (String codComer, String codCliente, String codBanco, String numero) {
        //urlInfinity = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.areamercadeo
        //https://www.supertech.ec:8443/infinityone1/resources/usuario/login?user=Ftapia&password=123456";
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));  
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.clientegarantia/MaxSecuencial?codigocomercializadora="
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.clientegarantia/MaxSecuencial?codigocomercializadora="            
                              + codComer + "&codigocliente=" + codCliente + "&codigobanco=" + codBanco + "&numero=" + numero);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            //connection.setRequestProperty("Authorization", token);
            //connection.setRequestProperty("Access-Control-Allow-Headers", "Authorization");
            //connection.setRequestProperty("Accept-Charset", "utf-8");
            //connection.setRequestProperty("Accept-Encoding", "gzip");
            //connection.setRequestProperty("Accept-Language", "en-US");
            //connection.setRequestProperty("Access-Control-Allow-Origin", "*");

            secuencial = 0;
            
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
                String sec = retorno.getString(indice);                
                secuencial = Integer.parseInt(sec);
            }   
            if(connection.getResponseCode() != 200){
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }                         
            return secuencial;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return secuencial;
    }

}
