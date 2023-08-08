/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.preciosyfacturacion;

import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.servicio.catalogo.TerminalServicio;
import ec.com.infinityone.servicio.catalogo.UsuarioServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Facturadordespachador;
import ec.com.infinityone.modelo.FacturadordespachadorPK;
import ec.com.infinityone.modelo.Terminal;
import ec.com.infinityone.modelo.Usuario;
import ec.com.infinityone.servicio.preciosyfacturacion.FacturadorDespachadorServicio;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class FacturadorDespachadorBean extends ReusableBean implements Serializable {

    @Inject
    private ComercializadoraServicio comercializadoraServicio;
    @Inject
    private TerminalServicio terminalServicio;
    @Inject
    private FacturadorDespachadorServicio factDespServicio;
    @Inject
    private UsuarioServicio usuarioServicio;

    private List<ComercializadoraBean> listaComercializadoras;

    private List<Facturadordespachador> listaFactDesp;

    private List<Terminal> listaTerminal;

    private List<Usuario> listaUsuario;

    private ComercializadoraBean comercializadora;

    private String codComer;

    private Facturadordespachador facturadordespachador;

    private FacturadordespachadorPK facturadordespachadorPK;

    private Terminal terminal;

    private Usuario usuarioD;

    private boolean estadoDespProd;

    private boolean estadoAplicaIva;

    private String pvpSug;

    private String precioEpp;

    private String margenCom;

    private boolean editarPago;

    /**
     * Constructor por defecto
     */
    public FacturadorDespachadorBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.facturadordespachador";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.facturadordespachador";

        listaComercializadoras = new ArrayList<>();
        listaFactDesp = new ArrayList<>();
        listaTerminal = new ArrayList<>();
        listaUsuario = new ArrayList<>();
        comercializadora = new ComercializadoraBean();
        terminal = new Terminal();
        usuarioD = new Usuario();
        facturadordespachador = new Facturadordespachador();
        facturadordespachadorPK = new FacturadordespachadorPK();
        estadoDespProd = false;
        estadoAplicaIva = false;
        obtenerFactDesp();
        obtenerComercializadora();
        obtenerTerminal();
        obtenerUsuario();

    }

    public void obtenerFactDesp() {
        listaFactDesp = new ArrayList<>();
        listaFactDesp = this.factDespServicio.obtenerFactDesp();
    }

    public void obtenerComercializadora() {
        listaComercializadoras = new ArrayList<>();
        listaComercializadoras = this.comercializadoraServicio.obtenerComercializadoras();
    }

    public void obtenerTerminal() {
        listaTerminal = new ArrayList<>();
        listaTerminal = this.terminalServicio.obtenerTerminal();
    }

    public void obtenerUsuario() {
        listaUsuario = new ArrayList<>();
        listaUsuario = this.usuarioServicio.obtenerUsuario();
    }

    public void actualizarLista() {
        if (comercializadora != null) {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            codComer = params.get("form:comercializadora_input");
            //codComer = comercializadora.getCodigo();
//            listaComerProductos = new ArrayList<>();
//            listaComerProductos = comerProdServicio.obtenerProductos(codComer);
        }
    }

    public void save() {
        if (editarPago) {
            editItems();
            obtenerFactDesp();
        } else {
            addItems();
            obtenerFactDesp();
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
            JSONObject fac = new JSONObject();
            JSONObject facPK = new JSONObject();
            facPK.put("codigoterminal", terminal.getCodigo());
            facPK.put("codigocomercializadora", comercializadora.getCodigo());
            facPK.put("codigousuario", usuarioD.getCodigo());
            fac.put("facturadordespachadorPK", facPK);
            fac.put("activo", estadoDespProd);
            fac.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = fac.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "PRODUCTO REGISTRADO EXITOSAMENTE");
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
            JSONObject objPK = new JSONObject();
            JSONObject term = new JSONObject();
//            obj.put("codigo", precio.getCodigo());
//            obj.put("nombre", precio.getNombre());

            objPK.put("codigocomercializadora", comercializadora.getCodigo());
            objPK.put("codigoterminal", terminal.getCodigo());
            objPK.put("codigousuario", usuarioD.getCodigo());
            term.put("codigo", terminal.getCodigo());
            term.put("nombre", terminal.getNombre());
            term.put("activo", terminal.isActivo());
            term.put("usuarioactual", terminal.getUsuarioactual());
            obj.put("terminal", term);
            obj.put("facturadordespachadorPK", objPK);
            obj.put("activo", estadoDespProd);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();                        
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "PRODUCTO ACUTALIZADO EXITOSAMENTE");
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
            url = new URL(direccion + "/porId?codigocomercializadora=" + facturadordespachador.getFacturadordespachadorPK().getCodigocomercializadora()
                    + "&codigoterminal=" + facturadordespachador.getFacturadordespachadorPK().getCodigoterminal()
                    + "&codigousuario=" + facturadordespachador.getFacturadordespachadorPK().getCodigousuario());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
//            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//            JSONObject obj = new JSONObject();
//            JSONObject objPK = new JSONObject();
//            JSONObject term = new JSONObject();
////            obj.put("codigo", precio.getCodigo());
////            obj.put("nombre", precio.getNombre());
//
//            objPK.put("codigocomercializadora", facturadordespachador.getFacturadordespachadorPK().getCodigocomercializadora());
//            objPK.put("codigoterminal", facturadordespachador.getFacturadordespachadorPK().getCodigoterminal());
//            objPK.put("codigousuario", facturadordespachador.getFacturadordespachadorPK().getCodigousuario());
//            term.put("codigo", terminal.getCodigo());
//            term.put("nombre", terminal.getNombre());
//            term.put("activo", terminal.getActivo());
//            term.put("usuarioactual", terminal.getUsuario());
//            obj.put("terminal", term);
//            obj.put("facturadordespachadorPK", objPK);
//            obj.put("activo", estadoDespProd);
//            obj.put("usuarioactual", dataUser.getUser().getNombrever());
//            respuesta = obj.toString();
//            writer.write(respuesta);
//            writer.close();            
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURADOR ELIMINADO EXITOSAMENTE");
                obtenerFactDesp();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoFacDesp() {
        estadoDespProd = true;
        editarPago = false;
        facturadordespachador = new Facturadordespachador();
        soloLectura = false;
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Facturadordespachador editarDespa(Facturadordespachador obj) {
        editarPago = true;
        soloLectura = true;

        facturadordespachador = obj;
        for (int i = 0; i < listaComercializadoras.size(); i++) {
            if (listaComercializadoras.get(i).getCodigo().equals(facturadordespachador.getFacturadordespachadorPK().getCodigocomercializadora())) {
                this.comercializadora = listaComercializadoras.get(i);
            }
        }
        for (int i = 0; i < listaTerminal.size(); i++) {
            if (listaTerminal.get(i).getCodigo().equals(facturadordespachador.getFacturadordespachadorPK().getCodigoterminal())) {
                this.terminal = listaTerminal.get(i);
            }
        }
        for (int i = 0; i < listaUsuario.size(); i++) {
            if (listaUsuario.get(i).getCodigo().equals(facturadordespachador.getFacturadordespachadorPK().getCodigousuario())) {
                this.usuarioD = listaUsuario.get(i);
            }
        }
        estadoDespProd = facturadordespachador.getActivo();

        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return facturadordespachador;
    }

    public List<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public boolean isEstadoDespProd() {
        return estadoDespProd;
    }

    public void setEstadoDespProd(boolean estadoDespProd) {
        this.estadoDespProd = estadoDespProd;
    }

    public boolean isEstadoAplicaIva() {
        return estadoAplicaIva;
    }

    public void setEstadoAplicaIva(boolean estadoAplicaIva) {
        this.estadoAplicaIva = estadoAplicaIva;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
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

    public boolean isEditarPago() {
        return editarPago;
    }

    public void setEditarPago(boolean editarPago) {
        this.editarPago = editarPago;
    }

    public List<Facturadordespachador> getListaFactDesp() {
        return listaFactDesp;
    }

    public void setListaFactDesp(List<Facturadordespachador> listaFactDesp) {
        this.listaFactDesp = listaFactDesp;
    }

    public List<Terminal> getListaTerminal() {
        return listaTerminal;
    }

    public void setListaTerminal(List<Terminal> listaTerminal) {
        this.listaTerminal = listaTerminal;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public Facturadordespachador getFacturadordespachador() {
        return facturadordespachador;
    }

    public void setFacturadordespachador(Facturadordespachador facturadordespachador) {
        this.facturadordespachador = facturadordespachador;
    }

    public FacturadordespachadorPK getFacturadordespachadorPK() {
        return facturadordespachadorPK;
    }

    public void setFacturadordespachadorPK(FacturadordespachadorPK facturadordespachadorPK) {
        this.facturadordespachadorPK = facturadordespachadorPK;
    }

    public Usuario getUsuarioD() {
        return usuarioD;
    }

    public void setUsuarioD(Usuario usuarioD) {
        this.usuarioD = usuarioD;
    }

    public String getPvpSug() {
        return pvpSug;
    }

    public void setPvpSug(String pvpSug) {
        this.pvpSug = pvpSug;
    }

    public String getPrecioEpp() {
        return precioEpp;
    }

    public void setPrecioEpp(String precioEpp) {
        this.precioEpp = precioEpp;
    }

    public String getMargenCom() {
        return margenCom;
    }

    public void setMargenCom(String margenCom) {
        this.margenCom = margenCom;
    }

}
