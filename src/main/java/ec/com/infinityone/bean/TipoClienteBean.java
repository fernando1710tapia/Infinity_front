/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.ObjetoNivel1;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author ft
 */
@Named
@ViewScoped
public class TipoClienteBean extends ReusableBean implements Serializable{
    private static final Logger LOG = Logger.getLogger(TipoClienteBean.class.getName());
    
    /*
    Variable que almacena varias Tipoclientes
     */
    private List<ObjetoNivel1> listaTipoclientes;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarTipocliente;
    /*
    Variable que establece true or false para el estado del Tipocliente
     */
    private boolean estadoTipocliente;
    /**
     * Constructor por defecto
     */
    public TipoClienteBean() {
    }
    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.tipocliente";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.tipocliente";
        editarTipocliente = false;
        objeto = new ObjetoNivel1();
        System.out.println("FT:: INIT()antes de obtenerTipocliente()");
        obtenerTipocliente();
        //getURL();
    }
    
    public void obtenerTipocliente() {
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));  
            url = new URL(direccion);
            System.out.println("FT:: obtenerTipocliente() URL del servicio: "+url.getPath());
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

            listaTipoclientes = new ArrayList<>();
            objeto = new ObjetoNivel1();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                 System.out.println("FT:: obtenerTipocliente() dentro del while SI HAY DATOS "+url.getPath());
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            System.out.println("FT:: obtenerTipocliente() json retorno: "+retorno.length());
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject areaM = retorno.getJSONObject(indice);
                objeto.setCodigo(areaM.getString("codigo"));
                objeto.setNombre(areaM.getString("nombre"));
                if (areaM.getBoolean("activo") == true) {
                    objeto.setActivo("S");
                } else {
                    objeto.setActivo("N");
                }
                objeto.setUsuario(areaM.getString("usuarioactual"));
                listaTipoclientes.add(objeto);
                objeto = new ObjetoNivel1();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            System.out.println("FT:: Error al obtener tipos de clientes: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void save() {
        if (editarTipocliente) {
            editItems();
            obtenerTipocliente();
        } else {
            addItems();
            obtenerTipocliente();
        }
    }

    public void addItems() {
        try {
            String respuesta;
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("codigo", objeto.getCodigo());
            obj.put("nombre", objeto.getNombre());
            obj.put("activo", estadoTipocliente);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if(connection.getResponseCode() == 200){
                this.dialogo(FacesMessage.SEVERITY_INFO, "TIPO DE CLIENTE REGISTRADO EXITOSAMENTE");
            }else{
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR UN TIPO DE CLIENTE ");
            }
            

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editItems() {
        try {
            String respuesta;
            url = new URL(direccion + "/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("codigo", objeto.getCodigo());
            obj.put("nombre", objeto.getNombre());
            obj.put("activo", estadoTipocliente);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if(connection.getResponseCode() == 200){
                this.dialogo(FacesMessage.SEVERITY_INFO, "TIPO DE CLIENTE ACUTALIZADO EXITOSAMENTE");
            }else{
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR UN TIPO DE CLIENTE");
            }
            
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteItems() {
        try {
            String respuesta;
            url = new URL(direccion + "/porId?codigo="+objeto.getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if(connection.getResponseCode() == 200){
                this.dialogo(FacesMessage.SEVERITY_INFO, "TIPO DE CLIENTE ELIMINADO EXITOSAMENTE");
                obtenerTipocliente();
            }else{
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL EIMINAR UN TIPO DE CLIENTE ");
            }
            
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoTipocliente() {
        estadoTipocliente = true;
        editarTipocliente = false;
        soloLectura = false;
        objeto = new ObjetoNivel1();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public ObjetoNivel1 editarTipocliente(ObjetoNivel1 obj) {
        editarTipocliente = true;
        objeto = obj;
        soloLectura = true  ;
        if (objeto.getActivo() == "S") {
            estadoTipocliente = true;
        } else {
            estadoTipocliente = false;
        }
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return objeto;
    }

    public List<ObjetoNivel1> getListaTipoclientes() {
        return listaTipoclientes;
    }

    public void setListaTipoclientes(List<ObjetoNivel1> listaTipoclientes) {
        this.listaTipoclientes = listaTipoclientes;
    }

    public boolean isEditarTipocliente() {
        return editarTipocliente;
    }

    public void setEditarTipocliente(boolean editarTipocliente) {
        this.editarTipocliente = editarTipocliente;
    }

    public boolean isEstadoTipocliente() {
        return estadoTipocliente;
    }

    public void setEstadoTipocliente(boolean estadoTipocliente) {
        this.estadoTipocliente = estadoTipocliente;
    }

   
    
    
}
