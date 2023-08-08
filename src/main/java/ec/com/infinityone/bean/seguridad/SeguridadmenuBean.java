/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.seguridad;

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
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author Andres
 */
@Named
@ViewScoped
public class SeguridadmenuBean extends ReusableBean implements Serializable {

    /*
    Variable que almacena varios Menus
     */
    private List<SeguridadmenuBean> listaSeguridadmenuBean;
    /*
    Variable que almacena varios menus Padres
     */
    private List<ObjetoNivel1> listaMenuPadre;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarSeguridad;
    /*
    Variable que establece true or false para el estado del Producto
     */
    private boolean estadoMenu;
    /*
    Variable que establece true or false para la activacion del menu padre
     */
    private boolean activarMenuP;

    private boolean mostrarMenuP;
    /*
    Variable almacena url Accion
    */
    private String urlAccion;
    private String urlAyuda;
    private String descripcionAyuda;
    /*
    Variable almacena codigo ARCH
     */
    private String menuPadre;

    private String nivel;

    private JSONObject codAm;

    private boolean permiteEdit;

    private ObjetoNivel1 objeto1;

    private SeguridadmenuBean seguridad;

    private int numSecuencia;

    /**
     * Constructor por defecto
     */
    public SeguridadmenuBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.menu";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.menu";
            
        editarSeguridad = false;
        objeto = new ObjetoNivel1();
        seguridad = new SeguridadmenuBean();
        permiteEdit = false;
        activarMenuP = false;
        mostrarMenuP = true;
        obtenerSeguridadMenu();
        obtenerListaMenuPadre();
        numSecuencia = listaSeguridadmenuBean.size() + 1;
        this.seguridad.setCodigo(String.valueOf(numSecuencia));
    }

    public void obtenerSeguridadMenu() {
        try {
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaSeguridadmenuBean = new ArrayList<>();
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
                JSONObject men = retorno.getJSONObject(indice);
                seguridad.setCodigo(men.getString("codigo"));
                seguridad.setNombre(men.getString("nombre"));
                seguridad.setNivel(men.getString("nivel"));
                if (!men.isNull("menupadre")) {
                    seguridad.setMenuPadre(men.getString("menupadre"));
                }
                if (!men.isNull("urlaccion")) {
                    seguridad.setUrlAccion(men.getString("urlaccion"));
                }
                if (!men.isNull("urlayuda")) {
                    seguridad.setUrlAyuda(men.getString("urlayuda"));
                }
                if (!men.isNull("descripcionayuda")) {
                    seguridad.setDescripcionAyuda(men.getString("descripcionayuda"));
                }
                seguridad.setObjRelacionado(seguridad.getCodigo() + " - " + seguridad.getNombre());
                seguridad.setUsuario(men.getString("usuarioactual"));
                listaSeguridadmenuBean.add(seguridad);
                seguridad = new SeguridadmenuBean();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerListaMenuPadre() {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.menu/menus?nivel=AAA");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.menu/menus?nivel=AAA");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaMenuPadre = new ArrayList<>();
            objeto1 = new ObjetoNivel1();
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
                JSONObject menuP = retorno.getJSONObject(indice);
                objeto1.setCodigo(menuP.getString("codigo"));
                objeto1.setObjRelacionado(menuP.getString("codigo") + " - " + menuP.getString("nombre"));
                listaMenuPadre.add(objeto1);
                objeto1 = new ObjetoNivel1();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (editarSeguridad) {
            editItems();
            obtenerSeguridadMenu();
            obtenerListaMenuPadre();
        } else {
            addItems();
            obtenerSeguridadMenu();
            obtenerListaMenuPadre();
        }
        numSecuencia = listaSeguridadmenuBean.size() + 1;
        this.seguridad.setCodigo(String.valueOf(numSecuencia));
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
            obj.put("codigo", seguridad.getCodigo());
            obj.put("nombre", seguridad.getNombre());
            obj.put("nivel", seguridad.getNivel());
            obj.put("menupadre", seguridad.getMenuPadre());
            obj.put("urlaccion", seguridad.getUrlAccion());
            obj.put("urlayuda", seguridad.getUrlAyuda());
            obj.put("descripcionayuda", seguridad.getDescripcionAyuda());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "MENU REGISTRADO EXITOSAMENTE");
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
            obj.put("codigo", seguridad.getCodigo());
            obj.put("nombre", seguridad.getNombre());
            obj.put("nivel", seguridad.getNivel());
            obj.put("menupadre", seguridad.getMenuPadre());
            obj.put("urlaccion", seguridad.getUrlAccion());
            obj.put("urlayuda", seguridad.getUrlAyuda());
            obj.put("descripcionayuda", seguridad.getDescripcionAyuda());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "MENU ACUTALIZADO EXITOSAMENTE");
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
            url = new URL(direccion + "/porId?codigo=" + seguridad.getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "MENU ELIMINADO EXITOSAMENTE");
                obtenerSeguridadMenu();
            } else {
                this.dialogo(FacesMessage.SEVERITY_INFO, "ERROR AL ELIMINAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoMenu() {
        estadoMenu = true;
        editarSeguridad = false;
        permiteEdit = false;
        soloLectura = false;
        activarMenuP = false;
        mostrarMenuP = true;
        objeto1 = new ObjetoNivel1();
        seguridad = new SeguridadmenuBean();
        seguridad.setCodigo(String.valueOf(listaSeguridadmenuBean.size() + 1));
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public SeguridadmenuBean editarSeguridad(SeguridadmenuBean obj) {
        editarSeguridad = true;
        seguridad = obj;
        permiteEdit = true;
        activarMenuP = false;
        mostrarMenuP = true;
        soloLectura = true;
        if (seguridad.nivel.equals("CCC")) {
            activarMenuP = true;
            mostrarMenuP = false;
        }
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return seguridad;
    }

    public void validacionMenu() {
        activarMenuP = this.seguridad.nivel.equals("CCC");
        mostrarMenuP = !this.seguridad.nivel.equals("CCC");
    }

    public boolean isEditarProducto() {
        return editarSeguridad;
    }

    public void setEditarProducto(boolean editarSeguridad) {
        this.editarSeguridad = editarSeguridad;
    }

    public boolean isEstadoMenu() {
        return estadoMenu;
    }

    public void setEstadoMenu(boolean estadoMenu) {
        this.estadoMenu = estadoMenu;
    }

    public String getUrlAccion() {
        return urlAccion;
    }

    public void setUrlAccion(String urlAccion) {
        this.urlAccion = urlAccion;
    }

    public String getMenuPadre() {
        return menuPadre;
    }

    public void setMenuPadre(String menuPadre) {
        this.menuPadre = menuPadre;
    }

    public boolean isPermiteEdit() {
        return permiteEdit;
    }

    public void setPermiteEdit(boolean permiteEdit) {
        this.permiteEdit = permiteEdit;
    }

    public List<ObjetoNivel1> getListaMenuPadre() {
        return listaMenuPadre;
    }

    public void setListaMenuPadre(List<ObjetoNivel1> listaMenuPadre) {
        this.listaMenuPadre = listaMenuPadre;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public ObjetoNivel1 getObjeto1() {
        return objeto1;
    }

    public void setObjeto1(ObjetoNivel1 objeto1) {
        this.objeto1 = objeto1;
    }

    public boolean isActivarMenuP() {
        return activarMenuP;
    }

    public void setActivarMenuP(boolean activarMenuP) {
        this.activarMenuP = activarMenuP;
    }

    public List<SeguridadmenuBean> getListaSeguridadmenuBean() {
        return listaSeguridadmenuBean;
    }

    public void setListaSeguridadmenuBean(List<SeguridadmenuBean> listaSeguridadmenuBean) {
        this.listaSeguridadmenuBean = listaSeguridadmenuBean;
    }

    public SeguridadmenuBean getSeguridad() {
        return seguridad;
    }

    public void setSeguridad(SeguridadmenuBean seguridad) {
        this.seguridad = seguridad;
    }

    public boolean isMostrarMenuP() {
        return mostrarMenuP;
    }

    public void setMostrarMenuP(boolean mostrarMenuP) {
        this.mostrarMenuP = mostrarMenuP;
    }

    public String getUrlAyuda() {
        return urlAyuda;
    }

    public void setUrlAyuda(String urlAyuda) {
        this.urlAyuda = urlAyuda;
    }

    public String getDescripcionAyuda() {
        return descripcionAyuda;
    }

    public void setDescripcionAyuda(String descripcionAyuda) {
        this.descripcionAyuda = descripcionAyuda;
    }

    
    
}
