/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.preciosyfacturacion.bean;

import ec.com.infinityone.actorcomercial.bean.*;
import ec.com.infinityone.actorcomercial.serivicios.ComercializadoraServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Fechafestiva;
import ec.com.infinityone.modeloWeb.FechafestivaPK;
import ec.com.infinityone.preciosyfacturacion.servicios.FechaFestivaServicio;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class FechaFestivaBean extends ReusableBean implements Serializable {

    @Inject
    private FechaFestivaServicio fechaFestivaServicio;
    @Inject
    private ComercializadoraServicio comercializadoraServicio;

    private List<Fechafestiva> listaFechafestiva;

    private List<ComercializadoraBean> listaComercializadora;

    private ComercializadoraBean comercializadora;

    private Fechafestiva fechafestiva;

    private FechafestivaPK fechafestivaPK;

    private String codComer;

    private boolean estadoFechafestiva;

    private boolean editarFechafestiva;
    /*
    variable para habilitar el combo de la comerciailizadora dependiendo la accion
     */
    private boolean bandera;
    /*
    Variable para escribir el año de busqueda 
    */
    private String year;

    /**
     * Constructor por defecto
     */
    public FechaFestivaBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.fechafestiva";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.fechafestiva";

        listaFechafestiva = new ArrayList<>();
        fechafestiva = new Fechafestiva();
        fechafestivaPK = new FechafestivaPK();
        comercializadora = new ComercializadoraBean();
        editarFechafestiva = false;
        year = "";
        obtenerComercializadora();
        //obtenerFechafestiva();
    }

    public void obtenerFechafestiva() {
        listaFechafestiva = new ArrayList<>();
        listaFechafestiva = this.fechaFestivaServicio.obtenerFechafestiva(comercializadora.getCodigo(), year);
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = this.comercializadoraServicio.obtenerComercializadorasActivas();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda();
        }
    }
    
    public void seleccionarComercializadora(){
        if(comercializadora != null){
            codComer = comercializadora.getCodigo();
        }
    }

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            if (dataUser.getUser().getNiveloperacion().equals("cero")) {
                habilitarComer = true;
            }
            if (dataUser.getUser().getNiveloperacion().equals("adco")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
            }
            if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
            }
        }
    }

    public void save() {
        if (editarFechafestiva) {
            editItems();
            obtenerFechafestiva();
        } else {
            addItems();
            obtenerFechafestiva();
        }
    }

    public void addItems() {
        try {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            String fechaFes = date.format(fechafestivaPK.getFestivo());
            String respuesta;
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            objPK.put("codigocomercializadora", codComer);
            objPK.put("festivo", fechaFes);
            obj.put("fechafestivaPK", objPK);
            obj.put("activo", estadoFechafestiva);
            obj.put("descripcion", fechafestiva.getDescripcion());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "FECHA FESTIVA REGISTRADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editItems() {
        try {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            String fechaFes = date.format(fechafestivaPK.getFestivo());
            String respuesta;
            url = new URL(direccion + "/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            objPK.put("codigocomercializadora", codComer);
            objPK.put("festivo", fechaFes);
            obj.put("fechafestivaPK", objPK);
            obj.put("activo", estadoFechafestiva);
            obj.put("descripcion", fechafestiva.getDescripcion());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "FECHA FESTIVA ACTUALIZADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteItems() {
        try {
            String respuesta;
            SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            String fechaFes = date.format(fechafestiva.getFechafestivaPK().getFestivo());
            url = new URL(direccion + "/porId?codigocomercializadora=" + fechafestiva.getFechafestivaPK().getCodigocomercializadora()
                    + "&festivo=" + fechaFes);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "FECHA FESTIVA ELIMINADA EXITOSAMENTE");
                obtenerFechafestiva();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevaFecha() {
        editarFechafestiva = false;
        estadoFechafestiva = true;
        fechafestiva = new Fechafestiva();
        fechafestivaPK = new FechafestivaPK();
        if(habilitarComer){
           comercializadora = new ComercializadoraBean();
        }
        habilitarComboComer();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public void editarFecha(Fechafestiva obj) {
        editarFechafestiva = true;
        fechafestiva = obj;
        estadoFechafestiva = fechafestiva.getActivo();
        fechafestivaPK = fechafestiva.getFechafestivaPK();
        for (int i = 0; i < listaComercializadora.size(); i++) {
            if (listaComercializadora.get(i).getCodigo().equals(fechafestiva.getFechafestivaPK().getCodigocomercializadora())) {
                this.comercializadora = listaComercializadora.get(i);
            }
        }
        habilitarComboComer();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public boolean habilitarComboComer() {
        if (editarFechafestiva == false) {
            if (dataUser.getUser() != null) {
                if (dataUser.getUser().getNiveloperacion().equals("cero")) {
                    bandera = true;
                } else if (dataUser.getUser().getNiveloperacion().equals("adco")) {
                    bandera = false;
                } else if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                    bandera = false;
                }
            }
        }else {
            bandera = false;
        }
        return bandera;
    }

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public String getCodComer() {
        return codComer;
    }

    public void setCodComer(String codComer) {
        this.codComer = codComer;
    }

    public List<Fechafestiva> getListaFechafestiva() {
        return listaFechafestiva;
    }

    public void setListaFechafestiva(List<Fechafestiva> listaFechafestiva) {
        this.listaFechafestiva = listaFechafestiva;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public Fechafestiva getFechafestiva() {
        return fechafestiva;
    }

    public void setFechafestiva(Fechafestiva fechafestiva) {
        this.fechafestiva = fechafestiva;
    }

    public FechafestivaPK getFechafestivaPK() {
        return fechafestivaPK;
    }

    public void setFechafestivaPK(FechafestivaPK fechafestivaPK) {
        this.fechafestivaPK = fechafestivaPK;
    }

    public boolean isEstadoFechafestiva() {
        return estadoFechafestiva;
    }

    public void setEstadoFechafestiva(boolean estadoFechafestiva) {
        this.estadoFechafestiva = estadoFechafestiva;
    }

    public boolean isEditarFechafestiva() {
        return editarFechafestiva;
    }

    public void setEditarFechafestiva(boolean editarFechafestiva) {
        this.editarFechafestiva = editarFechafestiva;
    }

    public boolean isBandera() {
        return bandera;
    }

    public void setBandera(boolean bandera) {
        this.bandera = bandera;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    
    
}
