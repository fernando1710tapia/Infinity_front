/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.servicio.catalogo;

import ec.com.infinityone.bean.TerminalBean;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.ObjetoNivel1;
import ec.com.infinityone.modelo.Usuario;
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
public class UsuarioServicio {
    
    /*
    Variable para instanciar al objeto nivel 1
    */
    private List<Usuario> listaUsuario;
    
    public List<Usuario> obtenerUsuario(){
         try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.usuario");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.usuario");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaUsuario = new ArrayList<>();
            Usuario usuario = new Usuario();
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
                JSONObject terminal = retorno.getJSONObject(indice);
                usuario.setCodigo(terminal.getString("codigo"));
                usuario.setNombre(terminal.getString("nombre"));                
                listaUsuario.add(usuario);
                usuario = new Usuario();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
            return listaUsuario;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaUsuario;
    }
    
}
