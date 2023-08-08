/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.ObjetoNivel1;
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
 * @author David
 */
@Named
@ViewScoped
public class MedidaBean extends ReusableBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(MedidaBean.class.getName());

    /*
    Variable que almacena varias Medidas
     */
    private List<ObjetoNivel1> listaMedida;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarMedida;
    /*
    Variable que establece true or false para el estado de la Medida
     */
    private boolean estadoMedida;
    /*
    Variable almacena la abreviacion
     */
    private String abreviacion;

    /**
     * Constructor por defecto
     */
    public MedidaBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.medida";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.medida";
        editarMedida = false;
        objeto = new ObjetoNivel1();
        obtenerMedida();
        //getURL();
    }

    public void obtenerMedida() {
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));  
            url = new URL(direccion);
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

            listaMedida = new ArrayList<>();
            objeto = new ObjetoNivel1();
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
                JSONObject areaM = retorno.getJSONObject(indice);
                objeto.setCodigo(areaM.getString("codigo"));
                objeto.setNombre(areaM.getString("nombre"));
                objeto.setAbreviacion(areaM.getString("abreviacion"));
                //this.setAbreviacion();
                if (areaM.getBoolean("activo") == true) {
                    objeto.setActivo("S");
                } else {
                    objeto.setActivo("N");
                }
                objeto.setUsuario(areaM.getString("usuarioactual"));
                listaMedida.add(objeto);
                objeto = new ObjetoNivel1();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (editarMedida) {
            editItems();
            obtenerMedida();
        } else {
            addItems();
            obtenerMedida();
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
            obj.put("abreviacion", objeto.getAbreviacion());
            obj.put("activo", estadoMedida);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if(connection.getResponseCode() == 200){
                this.dialogo(FacesMessage.SEVERITY_INFO, "MEDIDA REGISTRADA EXITOSAMENTE");
            }else{
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
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
            obj.put("abreviacion", objeto.getAbreviacion());
            obj.put("activo", estadoMedida);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if(connection.getResponseCode() == 200){
                this.dialogo(FacesMessage.SEVERITY_INFO, "MEDIDA ACUTALIZADA EXITOSAMENTE");
            }else{
                this.dialogo(FacesMessage.SEVERITY_INFO, "ERROR AL ACTUALIZAR");
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
                this.dialogo(FacesMessage.SEVERITY_INFO, "MEDIDA ELIMINADA EXITOSAMENTE");
                obtenerMedida();
            }else{
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }
            
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevaMedida() {
        estadoMedida = true;
        editarMedida = false;
        soloLectura = false;
        this.setAbreviacion("");
        objeto = new ObjetoNivel1();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public ObjetoNivel1 editarMedida(ObjetoNivel1 obj) {
        editarMedida = true;
        objeto = obj;
        soloLectura = true;
        if (objeto != null) {
            this.setAbreviacion(obj.getAbreviacion());
            if ("S".equals(objeto.getActivo())) {
                estadoMedida = true;
            } else {
                estadoMedida = false;
            }
        }
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return objeto;
    }

    public List<ObjetoNivel1> getListaMedida() {
        return listaMedida;
    }

    public void setListaMedida(List<ObjetoNivel1> listaMedida) {
        this.listaMedida = listaMedida;
    }

    public boolean isEditarMedida() {
        return editarMedida;
    }

    public void setEditarMedida(boolean editarMedida) {
        this.editarMedida = editarMedida;
    }

    public boolean isEstadoMedida() {
        return estadoMedida;
    }

    public void setEstadoMedida(boolean estadoMedida) {
        this.estadoMedida = estadoMedida;
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

}
