/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.actorcomercial;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Autotanque;
import ec.com.infinityone.modelo.Conductor;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class AutotanqueBean extends ReusableBean implements Serializable {

    /*
    Variable que almacena varias autotanques
     */
    private List<Autotanque> listaAutotanque;

    private List<Conductor> listaConductores;
    /*
    Variable que almacena autotanque
     */
    private Autotanque autotanque;
    /*
    Variable que almacena autotanque
     */
    private Conductor conductor;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarAutotanque;
    /*
    Variable que establece true or false para el estado de la Medida
     */
    private boolean estadoAutotanque;

    private BigDecimal volTotal;

    private int comp;

    /**
     * Constructor por defecto
     */
    public AutotanqueBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.autotanque";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.autotanque";
        editarAutotanque = false;
        autotanque = new Autotanque();
        comp = 0;
        volTotal = new BigDecimal("0.00");
        obtenerAutotanque();
        obtenerConductores();
        //getURL();
    }

    public void obtenerConductores() {
        try {
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.conductor");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaConductores = new ArrayList<>();
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
                if (conductor.getActivo()) {
                    listaConductores.add(conductor);
                }
                conductor = new Conductor();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerAutotanque() {
        try {
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaAutotanque = new ArrayList<>();
            autotanque = new Autotanque();
            conductor = new Conductor();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject autotanqueJson = new JSONObject(respuesta);
            JSONArray retorno = autotanqueJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                comp = 0;
                JSONObject autoT = retorno.getJSONObject(indice);
                JSONObject cond = autoT.getJSONObject("cedularuc");

                autotanque.setPlaca(autoT.getString("placa"));
                autotanque.setVolumentotal(autoT.getBigDecimal("volumentotal"));
                autotanque.setCompartimento1(autoT.getBigDecimal("compartimento1"));
                autotanque.setCompartimento2(autoT.getBigDecimal("compartimento2"));
                autotanque.setCompartimento3(autoT.getBigDecimal("compartimento3"));
                autotanque.setCompartimento4(autoT.getBigDecimal("compartimento4"));
                autotanque.setCompartimento5(autoT.getBigDecimal("compartimento5"));
                autotanque.setCompartimento6(autoT.getBigDecimal("compartimento6"));
                autotanque.setCompartimento7(autoT.getBigDecimal("compartimento7"));
                autotanque.setCompartimento8(autoT.getBigDecimal("compartimento8"));
                autotanque.setCompartimento9(autoT.getBigDecimal("compartimento9"));
                autotanque.setCompartimento10(autoT.getBigDecimal("compartimento10"));
                autotanque.setActivo(autoT.getBoolean("activo"));
                autotanque.setUsuarioactual(autoT.getString("usuarioactual"));
                conductor.setCedularuc(cond.getString("cedularuc"));
                conductor.setNombre(cond.getString("nombre"));
                conductor.setActivo(cond.getBoolean("activo"));
                autotanque.setCedularuc(conductor);
                calcularVol();
                autotanque.setCompartimentos(comp);
                listaAutotanque.add(autotanque);
                autotanque = new Autotanque();
                conductor = new Conductor();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (editarAutotanque) {
            editItems();
            obtenerAutotanque();
        } else {
            addItems();
            obtenerAutotanque();
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
            JSONObject objCond = new JSONObject();
            obj.put("placa", autotanque.getPlaca());
            obj.put("volumentotal", volTotal);
            obj.put("compartimento1", autotanque.getCompartimento1());
            obj.put("compartimento2", autotanque.getCompartimento2());
            obj.put("compartimento3", autotanque.getCompartimento3());
            obj.put("compartimento4", autotanque.getCompartimento4());
            obj.put("compartimento5", autotanque.getCompartimento5());
            obj.put("compartimento6", autotanque.getCompartimento6());
            obj.put("compartimento7", autotanque.getCompartimento7());
            obj.put("compartimento8", autotanque.getCompartimento8());
            obj.put("compartimento9", autotanque.getCompartimento9());
            obj.put("compartimento10", autotanque.getCompartimento10());
            obj.put("activo", estadoAutotanque);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            objCond.put("cedularuc", conductor.getCedularuc());
            obj.put("cedularuc", objCond);
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "AUTOTANQUE REGISTRADO EXITOSAMENTE");
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
            JSONObject objCond = new JSONObject();
            obj.put("placa", autotanque.getPlaca());
            obj.put("volumentotal", volTotal);
            obj.put("compartimento1", autotanque.getCompartimento1());
            obj.put("compartimento2", autotanque.getCompartimento2());
            obj.put("compartimento3", autotanque.getCompartimento3());
            obj.put("compartimento4", autotanque.getCompartimento4());
            obj.put("compartimento5", autotanque.getCompartimento5());
            obj.put("compartimento6", autotanque.getCompartimento6());
            obj.put("compartimento7", autotanque.getCompartimento7());
            obj.put("compartimento8", autotanque.getCompartimento8());
            obj.put("compartimento9", autotanque.getCompartimento9());
            obj.put("compartimento10", autotanque.getCompartimento10());
            obj.put("activo", estadoAutotanque);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            objCond.put("cedularuc", conductor.getCedularuc());
            obj.put("cedularuc", objCond);
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "AUTOTANQUE ACUTALIZADO EXITOSAMENTE");
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
            url = new URL(direccion + "/porId?codigo=" + autotanque.getPlaca());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "AUTOTANQUE ELIMINADO EXITOSAMENTE");
                obtenerAutotanque();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoAutotanque() {
        estadoAutotanque = true;
        editarAutotanque = false;
        soloLectura = false;
        autotanque = new Autotanque();
        autotanque.setCompartimento1(new BigDecimal("-1.0"));
        autotanque.setCompartimento1(new BigDecimal("-1.0"));
        autotanque.setCompartimento2(new BigDecimal("-1.0"));
        autotanque.setCompartimento3(new BigDecimal("-1.0"));
        autotanque.setCompartimento4(new BigDecimal("-1.0"));
        autotanque.setCompartimento5(new BigDecimal("-1.0"));
        autotanque.setCompartimento6(new BigDecimal("-1.0"));
        autotanque.setCompartimento7(new BigDecimal("-1.0"));
        autotanque.setCompartimento8(new BigDecimal("-1.0"));
        autotanque.setCompartimento9(new BigDecimal("-1.0"));
        autotanque.setCompartimento10(new BigDecimal("-1.0"));
        volTotal = new BigDecimal("0.00");
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Autotanque editaAutotanque(Autotanque obj) {
        volTotal = new BigDecimal("0.00");
        editarAutotanque = true;
        autotanque = obj;
        soloLectura = true;
        conductor = obj.getCedularuc();
        estadoAutotanque = autotanque.isActivo();
        calcularVol();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return autotanque;
    }

    public void calcularVol() {
        volTotal = new BigDecimal("0.00");
        if (autotanque.getCompartimento1() != null) {
            if (!autotanque.getCompartimento1().equals(new BigDecimal("-1.0")) && !autotanque.getCompartimento1().equals(new BigDecimal("-1")) && !autotanque.getCompartimento1().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanque.getCompartimento1());
                comp++;
            }
            if (!autotanque.getCompartimento2().equals(new BigDecimal("-1.0")) && !autotanque.getCompartimento2().equals(new BigDecimal("-1")) && !autotanque.getCompartimento2().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanque.getCompartimento2());
                comp++;
            }
            if (!autotanque.getCompartimento3().equals(new BigDecimal("-1.0")) && !autotanque.getCompartimento3().equals(new BigDecimal("-1")) && !autotanque.getCompartimento3().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanque.getCompartimento3());
                comp++;
            }
            if (!autotanque.getCompartimento4().equals(new BigDecimal("-1.0")) && !autotanque.getCompartimento4().equals(new BigDecimal("-1")) && !autotanque.getCompartimento4().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanque.getCompartimento4());
                comp++;
            }
            if (!autotanque.getCompartimento5().equals(new BigDecimal("-1.0")) && !autotanque.getCompartimento5().equals(new BigDecimal("-1")) && !autotanque.getCompartimento5().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanque.getCompartimento5());
                comp++;
            }
            if (!autotanque.getCompartimento6().equals(new BigDecimal("-1.0")) && !autotanque.getCompartimento6().equals(new BigDecimal("-1")) && !autotanque.getCompartimento6().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanque.getCompartimento6());
                comp++;
            }
            if (!autotanque.getCompartimento7().equals(new BigDecimal("-1.0")) && !autotanque.getCompartimento7().equals(new BigDecimal("-1")) && !autotanque.getCompartimento7().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanque.getCompartimento7());
                comp++;
            }
            if (!autotanque.getCompartimento8().equals(new BigDecimal("-1.0")) && !autotanque.getCompartimento8().equals(new BigDecimal("-1")) && !autotanque.getCompartimento8().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanque.getCompartimento8());
                comp++;
            }
            if (!autotanque.getCompartimento9().equals(new BigDecimal("-1.0")) && !autotanque.getCompartimento9().equals(new BigDecimal("-1")) && !autotanque.getCompartimento9().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanque.getCompartimento9());
                comp++;
            }
            if (!autotanque.getCompartimento10().equals(new BigDecimal("-1.0")) && !autotanque.getCompartimento10().equals(new BigDecimal("-1")) && !autotanque.getCompartimento10().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanque.getCompartimento10());
                comp++;
            }
            volTotal.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public List<Autotanque> getListaAutotanque() {
        return listaAutotanque;
    }

    public void setListaAutotanque(List<Autotanque> listaAutotanque) {
        this.listaAutotanque = listaAutotanque;
    }

    public boolean isEditarAutotanque() {
        return editarAutotanque;
    }

    public void setEditarAutotanque(boolean editarAutotanque) {
        this.editarAutotanque = editarAutotanque;
    }

    public boolean isEstadoAutotanque() {
        return estadoAutotanque;
    }

    public void setEstadoAutotanque(boolean estadoAutotanque) {
        this.estadoAutotanque = estadoAutotanque;
    }

    public Autotanque getAutotanque() {
        return autotanque;
    }

    public void setAutotanque(Autotanque autotanque) {
        this.autotanque = autotanque;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    }

    public List<Conductor> getListaConductores() {
        return listaConductores;
    }

    public void setListaConductores(List<Conductor> listaConductores) {
        this.listaConductores = listaConductores;
    }

    public BigDecimal getVolTotal() {
        return volTotal;
    }

    public void setVolTotal(BigDecimal volTotal) {
        this.volTotal = volTotal;
    }

}
