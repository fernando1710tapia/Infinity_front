/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.servicio.catalogo;

import ec.com.infinityone.configuration.Fichero;
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
public class ProductoServicio {

    /*
    Variable que almacena varios Productos
     */
    private List<Producto> listaProductos;

    private List<Producto> listaProductosSelec;

    private Producto producto;

    public List<Producto> obtenerProducto() {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.producto");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.producto");
    
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
                JSONObject prod = retorno.getJSONObject(indice);
                JSONObject codAm = prod.getJSONObject("codigoareamercadeo");
                producto.setCodigo(prod.getString("codigo"));
                producto.setNombre(prod.getString("nombre"));
                producto.setCodigoareamercadeo(codAm.getString("codigo"));
                producto.setCodigostc(prod.getString("codigostc"));
                producto.setCodigoarch(prod.getString("codigoarch"));
                producto.setUsuarioactual(prod.getString("usuarioactual"));
                listaProductos.add(producto);
                producto = new Producto();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return soloProd(listaProductos);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return soloProd(listaProductos);
    }

    public List<Producto> soloProd(List<Producto> listaProdTodos) {
        listaProductosSelec = new ArrayList<>();
        for (int i = 0; i < listaProductos.size(); i++) {
            String sSubCadena = listaProductos.get(i).getCodigo().substring(0, 2);
            if (!sSubCadena.equals("00")) {
                listaProductosSelec.add(listaProductos.get(i));
            }
        }
        return listaProductosSelec;
    }

}
