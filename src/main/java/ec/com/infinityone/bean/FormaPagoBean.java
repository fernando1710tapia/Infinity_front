/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Formapago;
import ec.com.infinityone.modelo.Formapago;
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
 * @author David
 */
@Named
@ViewScoped
public class FormaPagoBean extends ReusableBean implements Serializable {

    /*
    Variable que almacena varias formas de pago
     */
    private List<Formapago> listaFormaP;
    /*
    Variable que almacena varias formas de pago
     */
    private Formapago formaP;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarFormaP;
    /*
    Variable que establece true or false para el estado de la Medida
     */
    private boolean estadoFormaP;
    /**
     * Constructor por defecto
     */
    public FormaPagoBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.formapago";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.formapago";
        editarFormaP = false;
        formaP = new Formapago();
        obtenerFormaPago();
        //getURL();
    }

    public void obtenerFormaPago() {
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

            listaFormaP = new ArrayList<>();
            formaP = new Formapago();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject formaPJson = new JSONObject(respuesta);
            JSONArray retorno = formaPJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject areaM = retorno.getJSONObject(indice);
                formaP.setCodigo(areaM.getString("codigo"));
                formaP.setNombre(areaM.getString("nombre"));
                formaP.setCodigosri(areaM.getString("codigosri"));
                formaP.setActivo(areaM.getBoolean("activo"));               
                formaP.setUsuarioactual(areaM.getString("usuarioactual"));
                listaFormaP.add(formaP);
                formaP = new Formapago();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (editarFormaP) {
            editItems();
            obtenerFormaPago();
        } else {
            addItems();
            obtenerFormaPago();
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
            obj.put("codigo", formaP.getCodigo());
            obj.put("nombre", formaP.getNombre());
            obj.put("codigosri", formaP.getCodigosri());
            obj.put("activo", estadoFormaP);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "FORMA PAGO REGISTRADA EXITOSAMENTE");
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
            obj.put("codigo", formaP.getCodigo());
            obj.put("nombre", formaP.getNombre());
            obj.put("codigosri", formaP.getCodigosri());
            obj.put("activo", estadoFormaP);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "FORMA PAGO ACUTALIZADA EXITOSAMENTE");
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
            url = new URL(direccion + "/porId?codigo=" + formaP.getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "FORMA PAGO ELIMINADA EXITOSAMENTE");
                obtenerFormaPago();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevaFormaP() {
        estadoFormaP = true;
        editarFormaP = false;
        soloLectura = false;
        formaP = new Formapago();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Formapago editaFormaP(Formapago obj) {
        editarFormaP = true;
        formaP = obj;
        soloLectura = true;        
        estadoFormaP = formaP.getActivo();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return formaP;
    }

    public List<Formapago> getListaFormaP() {
        return listaFormaP;
    }

    public void setListaFormaP(List<Formapago> listaFormaP) {
        this.listaFormaP = listaFormaP;
    }

    public boolean isEditarFormaP() {
        return editarFormaP;
    }

    public void setEditarFormaP(boolean editarFormaP) {
        this.editarFormaP = editarFormaP;
    }

    public boolean isEstadoFormaP() {
        return estadoFormaP;
    }

    public void setEstadoFormaP(boolean estadoFormaP) {
        this.estadoFormaP = estadoFormaP;
    }

    public Formapago getFormaP() {
        return formaP;
    }

    public void setFormaP(Formapago formaP) {
        this.formaP = formaP;
    } 

}
