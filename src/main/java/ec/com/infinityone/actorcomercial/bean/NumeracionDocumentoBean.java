/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.actorcomercial.bean;

import ec.com.infinityone.actorcomercial.serivicios.ComercializadoraServicio;
import ec.com.infinityone.actorcomercial.serivicios.NumeracionServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Numeracion;
import ec.com.infinityone.modeloWeb.Usuario;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
public class NumeracionDocumentoBean extends ReusableBean implements Serializable {

    @Inject
    private ComercializadoraServicio comercializadoraServicio;
    @Inject
    private NumeracionServicio numeracionServicio;

    private List<ComercializadoraBean> listaComercializadoras;

    private List<Numeracion> listaNumeracion;

    private ComercializadoraBean comercializadora;

    private Numeracion numeracion;

    private String codComer;

    private boolean estadoNumeracion;

    private boolean estadoAplicaIva;

    private String pvpSug;

    private String precioEpp;

    private boolean editarNumeracion;

    private boolean habilitarComer;

    /**
     * Constructor por defecto
     */
    public NumeracionDocumentoBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        x = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.numeracion";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.numeracion";
        listaComercializadoras = new ArrayList<>();
        listaNumeracion = new ArrayList<>();
        comercializadora = new ComercializadoraBean();
        numeracion = new Numeracion();
        estadoNumeracion = false;
        estadoAplicaIva = false;

        obtenerComercializadora();
        //obtenerProdComer(listaComercializadoras.get(0).getCodigo());

        habilitarBusqueda();
    }

    public void obtenerProdComer(String codComer) {
        listaNumeracion = new ArrayList<>();
        listaNumeracion = numeracionServicio.obtenerNumeracion(codComer);
    }

    public void obtenerComercializadora() {
        listaComercializadoras = new ArrayList<>();
        listaComercializadoras = this.comercializadoraServicio.obtenerComercializadorasActivas();
    }

    public void seleccionarComer() {
        if (comercializadora != null) {
            codComer = comercializadora.getCodigo();
        }
    }

    public void habilitarBusqueda() {
        if (x != null) {
            if (x.getNiveloperacion().equals("cero")) {
                habilitarComer = true;
                //listaNumeracion = numeracionServicio.obtenerNumeracion(listaComercializadoras.get(0).getCodigo());
            }
            if (x.getNiveloperacion().equals("adco")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadoras.size(); i++) {
                    if (listaComercializadoras.get(i).getCodigo().equals(x.getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadoras.get(i);
                    }
                }
                //listaNumeracion = numeracionServicio.obtenerNumeracion(listaComercializadoras.get(0).getCodigo());
            }
            if (x.getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadoras.size(); i++) {
                    if (listaComercializadoras.get(i).getCodigo().equals(x.getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadoras.get(i);
                    }
                }
                //listaNumeracion = numeracionServicio.obtenerNumeracion(listaComercializadoras.get(0).getCodigo());
            }
        }
    }

    public void actualizarLista() {
        if (comercializadora != null) {
//            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//            codComer = params.get("form:comercializadora_input");
            //codComer = comercializadora.getCodigo();
            listaNumeracion = new ArrayList<>();
            listaNumeracion = numeracionServicio.obtenerNumeracion(comercializadora.getCodigo());
        }
    }

    public void save() {
        if (editarNumeracion) {
            editItems();
            obtenerProdComer(codComer);
        } else {
            addItems();
            obtenerProdComer(codComer);
        }
    }

    public void addItems() {
        try {
            String respuesta;
            url = new URL(direccion + "/agregar");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();

            //obj.put("id", "1");
            obj.put("tipodocumento", numeracion.getTipodocumento());
            obj.put("activo", estadoNumeracion);
            obj.put("ultimonumero", numeracion.getUltimonumero());
            obj.put("version", numeracion.getUltimonumero());
            obj.put("codigocomercializadora", comercializadora.getCodigo());
            obj.put("usuarioactual", x.getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "NUMERACIOn REGISTRADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Numeracion editarNumeracion(Numeracion obj) {
        editarNumeracion = true;
        numeracion = obj;
        //comercializadora.setCodigo(comercializadoraproducto.getComercializadoraproductoPK().getCodigocomercializadora());
        for (int i = 0; i < listaComercializadoras.size(); i++) {
            if (listaComercializadoras.get(i).getCodigo().equals(numeracion.getCodigocomercializadora().getCodigo())) {
                this.comercializadora = listaComercializadoras.get(i);
            }
        }
        estadoNumeracion = numeracion.getActivo();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return numeracion;
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

            obj.put("id", numeracion.getId());
            obj.put("tipodocumento", numeracion.getTipodocumento());
            obj.put("activo", estadoNumeracion);
            obj.put("ultimonumero", numeracion.getUltimonumero());
            obj.put("version", numeracion.getUltimonumero());
            obj.put("codigocomercializadora", comercializadora.getCodigo());
            obj.put("usuarioactual", x.getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "NUMERACION ACUTALIZADA EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
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
//        try {
//            String respuesta;
//            url = new URL(direccion + "/porId?codigocomercializadora=" + comercializadoraproducto.getComercializadoraproductoPK().getCodigocomercializadora()
//                    + "&codigoproducto=" + comercializadoraproducto.getComercializadoraproductoPK().getCodigoproducto()
//                    + "&codigomedida=" + comercializadoraproducto.getComercializadoraproductoPK().getCodigomedida());
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoOutput(true);
//            connection.setRequestMethod("DELETE");
//            connection.setRequestProperty("Content-type", "application/json");
//
//            
//            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//            JSONObject obj = new JSONObject();
//            JSONObject objPK = new JSONObject();
//            comerproductoPK.setCodigocomercializadora(comercializadora.getCodigo());
//            comerproductoPK.setCodigomedida(medida.getCodigo());
//            comerproductoPK.setCodigoproducto(producto.getCodigo());
//            objPK.put("codigocomercializadora", comerproductoPK.getCodigocomercializadora());
//            objPK.put("codigoproducto", comerproductoPK.getCodigoproducto());
//            objPK.put("codigomedida", comerproductoPK.getCodigomedida());
//            obj.put("comercializadoraproductoPK", objPK);
//            obj.put("activo", estadoComerProducto);
//            obj.put("margencomercializacion", comercializadoraproducto.getMargencomercializacion());
//            obj.put("precioepp", comercializadoraproducto.getPrecioepp());
//            obj.put("pvpsugerido", comercializadoraproducto.getPvpsugerido());
//            obj.put("soloaplicaiva", estadoAplicaIva);
//            obj.put("activo", estadoComerProducto);
//            obj.put("procesar", false);
//            obj.put("usuarioactual", "ft");
//            respuesta = obj.toString();
//            writer.write(respuesta);
//            writer.close();            
//            if (connection.getResponseCode() == 200) {
//                this.dialogo(FacesMessage.SEVERITY_INFO, "PRODUCTO ELIMINADO EXITOSAMENTE");
//                PrimeFaces.current().executeScript("PF('nuevo').hide()");
//                obtenerProdComer();
//            } else {
//                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
//            }
//
//            System.out.println(connection.getResponseCode());
//            System.out.println(connection.getResponseMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void nuevoNumeracion() {
        editarNumeracion = false;
        estadoNumeracion = true;
        numeracion = new Numeracion();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        }
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public boolean isEstadoNumeracion() {
        return estadoNumeracion;
    }

    public void setEstadoNumeracion(boolean estadoNumeracion) {
        this.estadoNumeracion = estadoNumeracion;
    }

    public boolean isEstadoAplicaIva() {
        return estadoAplicaIva;
    }

    public void setEstadoAplicaIva(boolean estadoAplicaIva) {
        this.estadoAplicaIva = estadoAplicaIva;
    }

    public List<Numeracion> getListaNumeracion() {
        return listaNumeracion;
    }

    public void setListaNumeracion(List<Numeracion> listaNumeracion) {
        this.listaNumeracion = listaNumeracion;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public Numeracion getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(Numeracion numeracion) {
        this.numeracion = numeracion;
    }

    public boolean isEditarNumeracion() {
        return editarNumeracion;
    }

    public void setEditarNumeracion(boolean editarNumeracion) {
        this.editarNumeracion = editarNumeracion;
    }

    public String getCodComer() {
        return codComer;
    }

    public void setCodComer(String codComer) {
        this.codComer = codComer;
    }

    public List<ComercializadoraBean> getListaComercializadoras() {
        return listaComercializadoras;
    }

    public void setListaComercializadoras(List<ComercializadoraBean> listaComercializadoras) {
        this.listaComercializadoras = listaComercializadoras;
    }

    public boolean isHabilitarComer() {
        return habilitarComer;
    }

    public void setHabilitarComer(boolean habilitarComer) {
        this.habilitarComer = habilitarComer;
    }

}
