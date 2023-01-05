/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.actorcomercial.bean;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Conductor;
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
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class ConductorBean extends ReusableBean implements Serializable {

    /*
    Variable que almacena varias formas de pago
     */
    private List<Conductor> listaConductor;
    /*
    Variable que almacena varias formas de pago
     */
    private Conductor conductor;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarConductor;
    /*
    Variable que establece true or false para el estado de la Medida
     */
    private boolean estadoConductor;
    /**
     * Constructor por defecto
     */
    public ConductorBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.conductor";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.conductor";
        editarConductor = false;
        conductor = new Conductor();
        obtenerConductor();
        //getURL();
    }

    public void obtenerConductor() {
        try {                        
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");           

            listaConductor = new ArrayList<>();
            conductor = new Conductor();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject conductorJson = new JSONObject(respuesta);
            JSONArray retorno = conductorJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject autoT = retorno.getJSONObject(indice);
                conductor.setCedularuc(autoT.getString("cedularuc"));
                conductor.setNombre(autoT.getString("nombre"));                
                conductor.setActivo(autoT.getBoolean("activo"));               
                conductor.setUsuarioactual(autoT.getString("usuarioactual"));
                listaConductor.add(conductor);
                conductor = new Conductor();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (editarConductor) {
            editItems();
            obtenerConductor();
        } else {
            addItems();
            obtenerConductor();
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
            obj.put("cedularuc", conductor.getCedularuc());
            obj.put("nombre", conductor.getNombre());            
            obj.put("activo", estadoConductor);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "CONDUCTOR REGISTRADO EXITOSAMENTE");
            } else {
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
            obj.put("cedularuc", conductor.getCedularuc());
            obj.put("nombre", conductor.getNombre());            
            obj.put("activo", estadoConductor);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "CONDUCTOR ACUTALIZADO EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
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
            url = new URL(direccion + "/porId?cedularuc=" + conductor.getCedularuc());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "CONDUCTOR ELIMINADO EXITOSAMENTE");
                obtenerConductor();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoConductor() {
        estadoConductor = true;
        editarConductor = false;
        soloLectura = false;
        conductor = new Conductor();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Conductor editaConductor(Conductor obj) {
        editarConductor = true;
        conductor = obj;
        soloLectura = true;        
        estadoConductor = conductor.getActivo();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return conductor;
    }

    public List<Conductor> getListaConductor() {
        return listaConductor;
    }

    public void setListaConductor(List<Conductor> listaConductor) {
        this.listaConductor = listaConductor;
    }

    public boolean isEditarConductor() {
        return editarConductor;
    }

    public void setEditarConductor(boolean editarConductor) {
        this.editarConductor = editarConductor;
    }

    public boolean isEstadoConductor() {
        return estadoConductor;
    }

    public void setEstadoConductor(boolean estadoConductor) {
        this.estadoConductor = estadoConductor;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    } 

}
