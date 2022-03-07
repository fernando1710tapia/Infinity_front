/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.actorcomercial.serivicios;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Comercializadoraproducto;
import ec.com.infinityone.modeloWeb.ComercializadoraproductoPK;
import ec.com.infinityone.modeloWeb.Medida;
import ec.com.infinityone.modeloWeb.Producto;
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
public class ComercializadoraProductoServicio {
    /*
    Variable que almacena varias Comercializadoras Producto
     */
    private List<Comercializadoraproducto> listacomerProd;
    /*
    Objeto Comercializadora Producto
     */
    private Comercializadoraproducto comerProd;
    /*
    Objeto Producto
    */
    private Producto producto;
    /*
    Objeto Medida
    */
    private Medida medida;
    /*
    Objeto Comercializadora Producto PK
    */
    private ComercializadoraproductoPK comerPK;
    
    
    public List<Comercializadoraproducto> obtenerProductos(String codigoComer) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.comercializadoraproducto/porComercializadora?codigocomercializadora=" + codigoComer);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadoraproducto/porComercializadora?codigocomercializadora=" + codigoComer);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listacomerProd = new ArrayList<>();
            comerProd = new Comercializadoraproducto();
            producto = new Producto();
            medida = new Medida();
            comerPK = new ComercializadoraproductoPK();
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
                JSONObject comercializadoraProd = retorno.getJSONObject(indice);
                JSONObject comercProdPK= comercializadoraProd.getJSONObject("comercializadoraproductoPK");
                JSONObject prod = comercializadoraProd.getJSONObject("producto");
                JSONObject med = comercializadoraProd.getJSONObject("medida");
                
                /*------------Objeto Producto----------------------------*/
                producto.setCodigo(prod.getString("codigo"));
                producto.setNombre(prod.getString("nombre"));
                producto.setCodigoarch(prod.getString("codigoarch"));
                producto.setCodigostc(prod.getString("codigostc"));  
                producto.setUsuarioactual(prod.getString("usuarioactual"));
                /*------------Objeto Medida----------------------------*/
                medida.setCodigo(med.getString("codigo"));
                medida.setNombre(med.getString("nombre"));
                medida.setAbreviacion(med.getString("abreviacion"));
                medida.setUsuarioactual(med.getString("usuarioactual"));
                /*------------Objeto Comercializadora Producto PK-----------------*/
                comerPK.setCodigocomercializadora(comercProdPK.getString("codigocomercializadora"));
                comerPK.setCodigomedida(comercProdPK.getString("codigomedida"));
                comerPK.setCodigoproducto(comercProdPK.getString("codigoproducto"));
                
                /*------------Objeto Comercializadora Producto----------------------------*/
                comerProd.setMargencomercializacion(comercializadoraProd.getBigDecimal("margencomercializacion"));
                comerProd.setPrecioepp(comercializadoraProd.getBigDecimal("precioepp"));
                comerProd.setPvpsugerido(comercializadoraProd.getBigDecimal("pvpsugerido"));
                comerProd.setSoloaplicaiva(comercializadoraProd.getBoolean("soloaplicaiva"));
                comerProd.setProcesar(comercializadoraProd.getBoolean("procesar"));
                comerProd.setUsuarioactual(comercializadoraProd.getString("usuarioactual"));
                comerProd.setActivo(comercializadoraProd.getBoolean("activo"));
                comerProd.setComercializadoraproductoPK(comerPK);
                comerProd.setProducto(producto);
                comerProd.setMedida(medida);
                //cliProd.setCodigo(areaM.getString("codigo"));
                //cliProd.setNombre(areaM.getString("nombre"));
                listacomerProd.add(comerProd);
                producto = new Producto();
                comerProd = new Comercializadoraproducto();
                medida = new Medida();
                comerPK = new ComercializadoraproductoPK();
                
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
            return listacomerProd;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listacomerProd;
    }
    
}
