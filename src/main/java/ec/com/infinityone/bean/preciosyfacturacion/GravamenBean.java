/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.preciosyfacturacion;

import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.bean.TerminalBean;
import ec.com.infinityone.servicio.catalogo.TerminalServicio;
import ec.com.infinityone.servicio.catalogo.UsuarioServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Gravamen;
import ec.com.infinityone.modelo.GravamenPK;
import ec.com.infinityone.modelo.Usuario;
import ec.com.infinityone.servicio.preciosyfacturacion.GravamenServicio;
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
public class GravamenBean extends ReusableBean implements Serializable {

    @Inject
    private ComercializadoraServicio comercializadoraServicio;
    @Inject
    private TerminalServicio terminalServicio;
    @Inject
    private GravamenServicio gravamenServicio;
    @Inject
    private UsuarioServicio usuarioServicio;

    private List<ComercializadoraBean> listaComercializadora;

    private List<Gravamen> listaGravamen;

    private List<TerminalBean> listaTerminal;

    private List<Usuario> listaUsuario;

    private ComercializadoraBean comercializadora;

    private String codigoComer;

    private Gravamen gravamen;

    private GravamenPK gravamenPK;

    private TerminalBean terminal;

    private Usuario usuarioD;

    private boolean estadoGravamen;

    private boolean seImprime;

    private String pvpSug;

    private String precioEpp;

    private String margenCom;        

    private boolean editarPago;
    
    private boolean habilitarComer;

    /**
     * Constructor por defecto
     */
    public GravamenBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //x = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.gravamen";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.gravamen";

        listaComercializadora = new ArrayList<>();
        listaGravamen = new ArrayList<>();
        listaTerminal = new ArrayList<>();
        listaUsuario = new ArrayList<>();        
        comercializadora = new ComercializadoraBean();
        terminal = new TerminalBean();
        usuarioD = new Usuario();
        gravamen = new Gravamen();
        gravamenPK = new GravamenPK();
        estadoGravamen = false;
        seImprime = false;
        obtenerComercializadora();     
    }

    public void obtenerGravamen(String codComer) {
        listaGravamen = new ArrayList<>();
        listaGravamen = this.gravamenServicio.obtenerGravamenes(codComer);
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = this.comercializadoraServicio.obtenerComercializadorasActivas();
        if(!listaComercializadora.isEmpty()){
            habilitarBusqueda();
        }
    } 
    
    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            if (dataUser.getUser() .getNiveloperacion().equals("cero")) {
                habilitarComer = true;
                //listaGravamen = this.gravamenServicio.obtenerGravamenes(listaComercializadora.get(0).getCodigo());                
            }
            if (dataUser.getUser() .getNiveloperacion().equals("adco")) {
                habilitarComer = false;                
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser() .getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
                //listaGravamen = this.gravamenServicio.obtenerGravamenes(comercializadora.getCodigo());                
            }
            if (dataUser.getUser() .getNiveloperacion().equals("usac")) {
                habilitarComer = false;               
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser() .getCodigocomercializadora())) {
                         this.comercializadora = listaComercializadora.get(i);
                    }
                }
                //listaGravamen = this.gravamenServicio.obtenerGravamenes(comercializadora.getCodigo());                
            }
        }
    }
    
    public void seleccionarComer() {
        if (comercializadora != null) {
            codigoComer = comercializadora.getCodigo();
        }
    }

    public void actualizarLista() {
        if (comercializadora.getCodigo() != null) {
            obtenerGravamen(comercializadora.getCodigo());            
        }
    }

    public void save() {
        if (editarPago) {
            editItems();
            obtenerGravamen(comercializadora.getCodigo());            
        } else {
            addItems();
            obtenerGravamen(comercializadora.getCodigo());
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
            JSONObject grav = new JSONObject();
            JSONObject gravPK = new JSONObject();
            gravPK.put("codigo", gravamenPK.getCodigo());
            gravPK.put("codigocomercializadora", comercializadora.getCodigo());     
            grav.put("gravamenPK", gravPK);
            grav.put("nombre", gravamen.getNombre());            
            grav.put("activo", estadoGravamen);
            grav.put("seimprime", seImprime);
            grav.put("formulavalor", gravamen.getFormulavalor());
            grav.put("valordefecto", gravamen.getValordefecto());
            grav.put("secuencial", gravamen.getSecuencial());
            grav.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = grav.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "GRAVAMEN REGISTRADO EXITOSAMENTE");
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
            JSONObject grav = new JSONObject();
            JSONObject gravPK = new JSONObject();
            gravPK.put("codigo", gravamenPK.getCodigo());
            gravPK.put("codigocomercializadora", comercializadora.getCodigo());            
            grav.put("gravamenPK", gravPK);
            grav.put("nombre", gravamen.getNombre());            
            grav.put("activo", estadoGravamen);
            grav.put("seimprime", seImprime);
            grav.put("formulavalor", gravamen.getFormulavalor());
            grav.put("valordefecto", gravamen.getValordefecto());
            grav.put("secuencial", gravamen.getSecuencial());
            grav.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = grav.toString();
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
            url = new URL(direccion + "/porId?codigocomercializadora=" + gravamen.getGravamenPK().getCodigocomercializadora() 
                    + "&codigo=" + gravamen.getGravamenPK().getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject grav = new JSONObject();
            JSONObject gravPK = new JSONObject();
            gravPK.put("codigo", gravamen.getGravamenPK().getCodigo());
            gravPK.put("codigocomercializadora", gravamen.getGravamenPK().getCodigocomercializadora());            
            grav.put("gravamenPK", gravPK);
            grav.put("nombre", gravamen.getNombre());            
            grav.put("activo", gravamen.getActivo());
            grav.put("seimprime", gravamen.getSeimprime());
            grav.put("formulavalor", gravamen.getFormulavalor());
            grav.put("valordefecto", gravamen.getValordefecto());
            grav.put("secuencial", gravamen.getSecuencial());
            grav.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = grav.toString();
            writer.write(respuesta);
            writer.close();
            
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURADOR ELIMINADO EXITOSAMENTE");
                obtenerGravamen(comercializadora.getCodigo());
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
        editarPago = false;
        soloLectura = false;
        estadoGravamen = true;
        seImprime = false;
        gravamen = new Gravamen();
        gravamenPK = new GravamenPK();   
        if(habilitarComer){
            comercializadora = new ComercializadoraBean();
        }        
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Gravamen editarGravamen(Gravamen obj) {
        editarPago = true;
        soloLectura = true;
        gravamen = obj;
        gravamenPK.setCodigo(gravamen.getGravamenPK().getCodigo());
        for (int i = 0; i < listaComercializadora.size(); i++) {
            if (listaComercializadora.get(i).getCodigo().equals(gravamen.getGravamenPK().getCodigocomercializadora())) {
                this.comercializadora = listaComercializadora.get(i);
            }
        }        
        estadoGravamen = gravamen.getActivo();
        seImprime = gravamen.getSeimprime();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return gravamen;
    }

    public boolean isEstadoGravamen() {
        return estadoGravamen;
    }

    public void setEstadoGravamen(boolean estadoGravamen) {
        this.estadoGravamen = estadoGravamen;
    }

    public Gravamen getGravamen() {
        return gravamen;
    }

    public void setGravamen(Gravamen gravamen) {
        this.gravamen = gravamen;
    }

    public GravamenPK getGravamenPK() {
        return gravamenPK;
    }

    public void setGravamenPK(GravamenPK gravamenPK) {
        this.gravamenPK = gravamenPK;
    }
        
    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public List<Gravamen> getListaGravamen() {
        return listaGravamen;
    }

    public void setListaGravamen(List<Gravamen> listaGravamen) {
        this.listaGravamen = listaGravamen;
    }

    public String getCodigoComer() {
        return codigoComer;
    }

    public void setCodigoComer(String codigoComer) {
        this.codigoComer = codigoComer;
    }        

    public List<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }  

    public boolean isSeImprime() {
        return seImprime;
    }

    public void setSeImprime(boolean seImprime) {
        this.seImprime = seImprime;
    }   

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }
    
    public boolean isEditarPago() {
        return editarPago;
    }

    public void setEditarPago(boolean editarPago) {
        this.editarPago = editarPago;
    }
   
    public List<TerminalBean> getListaTerminal() {
        return listaTerminal;
    }

    public void setListaTerminal(List<TerminalBean> listaTerminal) {
        this.listaTerminal = listaTerminal;
    }

    public TerminalBean getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalBean terminal) {
        this.terminal = terminal;
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

    public boolean isHabilitarComer() {
        return habilitarComer;
    }

    public void setHabilitarComer(boolean habilitarComer) {
        this.habilitarComer = habilitarComer;
    }        

}
