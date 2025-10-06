/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.serivicio.actorcomercial;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Clienteproducto;
import ec.com.infinityone.modelo.ClienteproductoPK;
import ec.com.infinityone.modelo.Producto;
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
public class ClienteProductoServicio {

    /*
    Varible Producto
    */
    private Producto producto;
    /*
    Lista Productos
    */
    private List<Producto> listaProductos;
    
    private Clienteproducto cliProd;
    
    private ClienteproductoPK clienteproductoPK;
    
    private List<Clienteproducto> listacliProd;
    
    public List<Producto> obtenerProductos(String codigoComercializadora, String codigoCliente) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.clienteproducto/porCliente?codigocliente=" + codigoCliente);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.clienteproducto/porCliente?codigocomercializadora="+codigoComercializadora+"&codigocliente=" + codigoCliente);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaProductos = new ArrayList<>();
            producto = new Producto();
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
                JSONObject clienteProd = retorno.getJSONObject(indice);
                JSONObject prod = clienteProd.getJSONObject("producto");
                producto.setCodigo(prod.getString("codigo"));
                producto.setNombre(prod.getString("nombre"));
                listaProductos.add(producto);      
                producto = new Producto();
            }
            return listaProductos;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaProductos;
    }
    
    public List<Clienteproducto> obtenerClienteProductos(String codigoComercializadora, String codigoCliente) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.clienteproducto/porCliente?codigocliente=" + codigoCliente);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.clienteproducto/porCliente?codigocomercializadora="+codigoComercializadora+"&codigocliente=" + codigoCliente);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listacliProd = new ArrayList<>();
            cliProd = new Clienteproducto();
            producto = new Producto();
            clienteproductoPK = new ClienteproductoPK();
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
                JSONObject clienteProd = retorno.getJSONObject(indice);
                JSONObject prod = clienteProd.getJSONObject("producto");
                JSONObject prodPK = clienteProd.getJSONObject("clienteproductoPK");
                
                producto.setCodigo(prod.getString("codigo"));
                producto.setNombre(prod.getString("nombre"));
                producto.setCodigoarch(prod.getString("codigoarch"));
                producto.setCodigostc(prod.getString("codigostc"));
                clienteproductoPK.setCodigo(prodPK.getString("codigo"));
                clienteproductoPK.setCodigocliente(prodPK.getString("codigocliente"));
                clienteproductoPK.setCodigocomercializadora(prodPK.getString("codigocomercializadora"));
                cliProd.setProducto(producto);
                cliProd.setClienteproductoPK(clienteproductoPK);
                cliProd.setActivo(clienteProd.getBoolean("activo"));
                cliProd.setUsuarioactual(clienteProd.getString("usuarioactual"));
                //cliProd.setCodigo(areaM.getString("codigo"));
                //cliProd.setNombre(areaM.getString("nombre"));
                listacliProd.add(cliProd);
                producto = new Producto();
                cliProd = new Clienteproducto();
                clienteproductoPK = new ClienteproductoPK();

            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
            return listacliProd;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listacliProd;
    }
    
    
}
