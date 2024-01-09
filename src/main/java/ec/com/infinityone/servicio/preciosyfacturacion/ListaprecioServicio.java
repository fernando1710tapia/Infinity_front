/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.servicio.preciosyfacturacion;

import com.lowagie.text.pdf.PdfName;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.EnvioClientePrecio;
import ec.com.infinityone.modelo.Listaprecio;
import ec.com.infinityone.modelo.ListaprecioPK;
import ec.com.infinityone.modelo.Precio;
import ec.com.infinityone.modelo.PrecioPK;
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
public class ListaprecioServicio {

    /*
    Variable que almacena varios Listaprecios
     */
    private List<Listaprecio> listaListaprecios;
    /*
    Objeto listaprecio
     */
    private Listaprecio listaprecio;

    private ListaprecioPK listaprecioPK;

    private List<Precio> listaPrecios;

    private Precio precio;

    private PrecioPK precioPK;
    
    // lista de clientes y sus precios
    private List<EnvioClientePrecio> listenvCliPre;
    
    private EnvioClientePrecio unEnvioClientePrecio;

    /*
    variable para devolver una fecha
     */
    private Date dateF;

    public List<Listaprecio> obtenerListaprecio() {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecio");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecio");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaListaprecios = new ArrayList<>();
            listaprecioPK = new ListaprecioPK();
            listaprecio = new Listaprecio();
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
                JSONObject listaPK = listaP.getJSONObject("listaprecioPK");
                listaprecioPK.setCodigo(listaPK.getLong("codigo"));
                listaprecioPK.setCodigocomercializadora(listaPK.getString("codigocomercializadora"));
                listaprecio.setListaprecioPK(listaprecioPK);
                listaprecio.setNombre(listaP.getString("nombre"));
                listaprecio.setTipo(listaP.getString("tipo"));
                listaprecio.setActivo(listaP.getBoolean("activo"));
                listaprecio.setUsuarioactual(listaP.getString("usuarioactual"));
                listaListaprecios.add(listaprecio);
                listaprecio = new Listaprecio();
                listaprecioPK = new ListaprecioPK();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return listaListaprecios;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaListaprecios;
    }

    public List<Listaprecio> obtenerListaprecioPorComer(String codComer) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecio/enPrecio?codigocomercializadora=" + codComer);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecio/enPrecio?codigocomercializadora=" + codComer);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaListaprecios = new ArrayList<>();
            listaprecioPK = new ListaprecioPK();
            listaprecio = new Listaprecio();
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
                JSONObject listaPK = listaP.getJSONObject("listaprecioPK");
                listaprecioPK.setCodigo(listaPK.getLong("codigo"));
                listaprecioPK.setCodigocomercializadora(listaPK.getString("codigocomercializadora"));
                listaprecio.setListaprecioPK(listaprecioPK);
                listaprecio.setNombre(listaP.getString("nombre"));
                listaprecio.setTipo(listaP.getString("tipo"));
                listaprecio.setActivo(listaP.getBoolean("activo"));
                listaprecio.setUsuarioactual(listaP.getString("usuarioactual"));
                listaListaprecios.add(listaprecio);
                listaprecio = new Listaprecio();
                listaprecioPK = new ListaprecioPK();
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaListaprecios;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaListaprecios;
    }

    public List<Listaprecio> obtenerListaprecioPorEstado(String codComer, String estado) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecio/enPrecio?codigocomercializadora=" + codComer);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecio/enPrecioestado?codigocomercializadora=" + codComer + "&estado=" + estado);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaListaprecios = new ArrayList<>();
            listaprecioPK = new ListaprecioPK();
            listaprecio = new Listaprecio();
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
                JSONObject listaPK = listaP.getJSONObject("listaprecioPK");
                listaprecioPK.setCodigo(listaPK.getLong("codigo"));
                listaprecioPK.setCodigocomercializadora(listaPK.getString("codigocomercializadora"));
                listaprecio.setListaprecioPK(listaprecioPK);
                listaprecio.setNombre(listaP.getString("nombre"));
                listaprecio.setTipo(listaP.getString("tipo"));
                listaprecio.setActivo(listaP.getBoolean("activo"));
                listaprecio.setUsuarioactual(listaP.getString("usuarioactual"));
                listaListaprecios.add(listaprecio);
                listaprecio = new Listaprecio();
                listaprecioPK = new ListaprecioPK();
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaListaprecios;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaListaprecios;
    }

    public List<Listaprecio> obtenerListaprecioEstado(String codComer, boolean estado) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecio/enPrecio?codigocomercializadora=" + codComer);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecio/porComercializadoraestado?codigocomercializadora=" + codComer + "&activo=" + estado);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaListaprecios = new ArrayList<>();
            listaprecioPK = new ListaprecioPK();
            listaprecio = new Listaprecio();
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
                JSONObject listaPK = listaP.getJSONObject("listaprecioPK");
                listaprecioPK.setCodigo(listaPK.getLong("codigo"));
                listaprecioPK.setCodigocomercializadora(listaPK.getString("codigocomercializadora"));
                listaprecio.setListaprecioPK(listaprecioPK);
                listaprecio.setNombre(listaP.getString("nombre"));
                listaprecio.setTipo(listaP.getString("tipo"));
                listaprecio.setActivo(listaP.getBoolean("activo"));
                listaprecio.setUsuarioactual(listaP.getString("usuarioactual"));
                listaListaprecios.add(listaprecio);
                listaprecio = new Listaprecio();
                listaprecioPK = new ListaprecioPK();
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaListaprecios;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaListaprecios;
    }

    public List<Precio> obtenerListaprecioPorComer(String codComer, Long codLista) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecio/enPrecio?codigocomercializadora=" + codComer);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio/porListaprecioComer?codigocomercializadora=" + codComer + "&codigolistaprecio=" + codLista);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaPrecios = new ArrayList<>();
            precioPK = new PrecioPK();
            precio = new Precio();
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
                JSONObject prec = retorno.getJSONObject(indice);
                JSONObject precPK = prec.getJSONObject("precioPK");
                precioPK.setCodigocomercializadora(precPK.getString("codigocomercializadora"));
                precioPK.setCodigoterminal(precPK.getString("codigoterminal"));
                precioPK.setCodigoproducto(precPK.getString("codigoproducto"));
                precioPK.setCodigomedida(precPK.getString("codigomedida"));
                precioPK.setCodigolistaprecio(precPK.getLong("codigolistaprecio"));                                           
                precio.setPrecioPK(precioPK);                
                precio.setActivo(prec.getBoolean("activo"));                
                listaPrecios.add(precio);
                precio = new Precio();
                precioPK = new PrecioPK();
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaPrecios;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaPrecios;
    }

    public List<Listaprecio> getListaprecioPorComer(String codComer) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecio/porComercializadora?codigocomercializadora=" + codComer);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecio/porComercializadora?codigocomercializadora=" + codComer);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaListaprecios = new ArrayList<>();
            listaprecioPK = new ListaprecioPK();
            listaprecio = new Listaprecio();
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
                JSONObject listaPK = listaP.getJSONObject("listaprecioPK");
                listaprecioPK.setCodigo(listaPK.getLong("codigo"));
                listaprecioPK.setCodigocomercializadora(listaPK.getString("codigocomercializadora"));
                listaprecio.setListaprecioPK(listaprecioPK);
                listaprecio.setNombre(listaP.getString("nombre"));
                listaprecio.setTipo(listaP.getString("tipo"));
                listaprecio.setActivo(listaP.getBoolean("activo"));
                listaprecio.setUsuarioactual(listaP.getString("usuarioactual"));
                if (listaprecio.getActivo() == true) {
                    listaListaprecios.add(listaprecio);
                }
                listaprecio = new Listaprecio();
                listaprecioPK = new ListaprecioPK();
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaListaprecios;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaListaprecios;
    }

    public Date obtenerUltimaFechaDePrecio(String codComer, Long codigoListaP) {
        try {
            dateF = new Date();
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/porComerListap?codigocomercializadora=" + codComer
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio/porComerListap?codigocomercializadora=" + codComer
                    + "&codigolistaprecio=" + codigoListaP);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

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
                Long fecha = retorno.getLong(indice);
                dateF = new Date(fecha);
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return dateF;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dateF;
    }

    public Listaprecio getListaprecio() {
        return listaprecio;
    }

    public void setListaprecio(Listaprecio listaprecio) {
        this.listaprecio = listaprecio;
    }

    public List<EnvioClientePrecio> obtenerClientePrecio(String codComer) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecio/porComercializadora?codigocomercializadora=" + codComer);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio/clienteprecio?codigocomercializadora=" + codComer);
            System.out.println("FT:: obtenerClientePrecio. "+url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listenvCliPre = new ArrayList<>(); 
            unEnvioClientePrecio = new EnvioClientePrecio();
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
                unEnvioClientePrecio.setCliente(listaP.getString("cliente"));
                unEnvioClientePrecio.setListaprecio(listaP.getString("listaprecio"));
                unEnvioClientePrecio.setTerminal(listaP.getString("terminal"));
                unEnvioClientePrecio.setProducto(listaP.getString("producto"));
                unEnvioClientePrecio.setCodigoprecio(listaP.getString("codigoprecio"));
                unEnvioClientePrecio.setActivo(listaP.getString("activo"));
                unEnvioClientePrecio.setFechainicio(listaP.getString("fechainicio")); 
                
                unEnvioClientePrecio.setPrecioterminalepp(listaP.getString("precioterminalepp").substring(0,8)); 
                unEnvioClientePrecio.setIva(listaP.getString("iva").substring(0,8)); 
//                unEnvioClientePrecio.setMargencomercializacion(listaP.getString("margencomercializacion")); 
                unEnvioClientePrecio.setIvapresuntivo(listaP.getString("ivapresuntivo").substring(0,8)); 
                unEnvioClientePrecio.setMargenxcliente(listaP.getString("margenxcliente").substring(0,8)); 
                unEnvioClientePrecio.setPrecioproducto(listaP.getString("precioproducto").substring(0,8)); 
                unEnvioClientePrecio.setTrexmil(listaP.getString("trexmil").substring(0,8)); 
               
//                unEnvioClientePrecio.setGravamen(listaP.getString("gravamen")); 
//                unEnvioClientePrecio.setValor(listaP.getString("valor")); 
//                
                listenvCliPre.add(unEnvioClientePrecio);
                unEnvioClientePrecio = new EnvioClientePrecio();
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

//            return listenvCliPre;
        } catch (Throwable e) {
            System.out.println("FT:: Error en obtenerClientePrecio " + e.getMessage());
            e.printStackTrace(System.out);
        }
        return listenvCliPre;
    }

}
