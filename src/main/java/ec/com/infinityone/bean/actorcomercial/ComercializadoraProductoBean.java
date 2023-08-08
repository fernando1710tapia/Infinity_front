/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.actorcomercial;

import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraProductoServicio;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.servicio.catalogo.MedidaServicio;
import ec.com.infinityone.servicio.catalogo.ProductoServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Comercializadoraproducto;
import ec.com.infinityone.modelo.ComercializadoraproductoPK;
import ec.com.infinityone.modelo.Medida;
import ec.com.infinityone.modelo.Producto;
import ec.com.infinityone.modelo.Usuario;
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
public class ComercializadoraProductoBean extends ReusableBean implements Serializable {

    @Inject
    private ComercializadoraServicio comercializadoraServicio;
    @Inject
    private ProductoServicio productoServicio;
    @Inject
    private ComercializadoraProductoServicio comerProdServicio;
    @Inject
    private MedidaServicio medidaServicio;

    private List<ComercializadoraBean> listaComercializadoras;

    private List<Comercializadoraproducto> listaComerProductos;

    private List<Producto> listaProducto;

    private List<Medida> listaMedida;

    private ComercializadoraBean comercializadora;

    private String codComer;

    private Comercializadoraproducto comercializadoraproducto;
    
    private Comercializadoraproducto comercializadoraproductoBean;

    private ComercializadoraproductoPK comerproductoPK;

    private Producto producto;

    private Medida medida;

    private boolean estadoComerProducto;

    private boolean estadoAplicaIva;

    private String pvpSug;

    private String precioEpp;

    private String margenCom;

    private boolean editarPago;
    /*
    Variable para habilitar cliente
     */
    protected boolean habilitarCli;
    

    /**
     * Constructor por defecto
     */
    public ComercializadoraProductoBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        
        x = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.comercializadoraproducto";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadoraproducto";
        listaComercializadoras = new ArrayList<>();
        listaComerProductos = new ArrayList<>();
        listaProducto = new ArrayList<>();
        listaMedida = new ArrayList<>();
        comercializadora = new ComercializadoraBean();
        producto = new Producto();
        medida = new Medida();
        comercializadoraproducto = new Comercializadoraproducto();
        comerproductoPK = new ComercializadoraproductoPK();
        estadoComerProducto = false;
        estadoAplicaIva = false;

        obtenerComercializadora();
        obtenerProductos();
        obtenerMedida();   
        habilitarBusqueda();        
    }
    
    public void obtenerProdComer(){
        listaComerProductos = new ArrayList<>();
        listaComerProductos = comerProdServicio.obtenerProductos(comercializadora.getCodigo());
    }

    public void obtenerComercializadora() {
        listaComercializadoras = new ArrayList<>();
        listaComercializadoras = this.comercializadoraServicio.obtenerComercializadorasActivas();
    }

    public void obtenerProductos() {
        listaProducto = new ArrayList<>();
        listaProducto = this.productoServicio.obtenerProducto();
    }

    public void obtenerMedida() {
        listaMedida = new ArrayList<>();
        listaMedida = this.medidaServicio.obtenerMedida();
    }
    
    public void habilitarBusqueda() {
        if (x != null) {
            if (x.getNiveloperacion().equals("cero")) {
                habilitarComer = true;
                //listaComerProductos = comerProdServicio.obtenerProductos(listaComercializadoras.get(0).getCodigo());                
            }
            if (x.getNiveloperacion().equals("adco")) {
                habilitarComer = false;                
                for (int i = 0; i < listaComercializadoras.size(); i++) {
                    if (listaComercializadoras.get(i).getCodigo().equals(x.getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadoras.get(i);
                    }
                }
                //listaComerProductos = comerProdServicio.obtenerProductos(comercializadora.getCodigo());                
            }
            if (x.getNiveloperacion().equals("usac")) {
                habilitarComer = false;               
                for (int i = 0; i < listaComercializadoras.size(); i++) {
                    if (listaComercializadoras.get(i).getCodigo().equals(x.getCodigocomercializadora())) {
                         this.comercializadora = listaComercializadoras.get(i);
                    }
                }
                //listaComerProductos = comerProdServicio.obtenerProductos(comercializadora.getCodigo());                                
            }
        }
    }
    
    public void seleccionarComer() {
        if (comercializadora != null) {
            codComer = comercializadora.getCodigo();
        }
    }

    public void actualizarLista() {
        if (comercializadora != null) {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            //codComer = params.get("form:comercializadora_input");
           // codComer = comercializadora.getCodigo();
            listaComerProductos = new ArrayList<>();
            listaComerProductos = comerProdServicio.obtenerProductos(comercializadora.getCodigo());
        }
    }

    public void save() {
        if (editarPago) {
            editItems();
            obtenerProdComer();
        } else {
            addItems();
            obtenerProdComer();
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
            JSONObject objPK = new JSONObject();
            objPK.put("codigoproducto", producto.getCodigo());
            objPK.put("codigocomercializadora", comercializadora.getCodigo());
            objPK.put("codigomedida", medida.getCodigo());
            obj.put("comercializadoraproductoPK", objPK);
            obj.put("activo", estadoComerProducto);
            obj.put("margencomercializacion", comercializadoraproducto.getMargencomercializacion());
            obj.put("precioepp", comercializadoraproducto.getPrecioepp());
            obj.put("pvpsugerido", comercializadoraproducto.getPvpsugerido());
            obj.put("soloaplicaiva", estadoAplicaIva);
            obj.put("activo", estadoComerProducto);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            obj.put("procesar", false);
            respuesta = obj.toString();
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
    
    public Comercializadoraproducto editarProdComer(Comercializadoraproducto obj) {
        editarPago = true;
        soloLectura = true;
        comercializadoraproducto = obj;
        //comercializadora.setCodigo(comercializadoraproducto.getComercializadoraproductoPK().getCodigocomercializadora());
        for (int i = 0; i < listaComercializadoras.size(); i++) {
            if (listaComercializadoras.get(i).getCodigo().equals(comercializadoraproducto.getComercializadoraproductoPK().getCodigocomercializadora())) {
                this.comercializadora = listaComercializadoras.get(i);
            }
        }
        for (int i = 0; i < listaProducto.size(); i++) {
            if (listaProducto.get(i).getCodigo().equals(comercializadoraproducto.getComercializadoraproductoPK().getCodigoproducto())) {
                this.producto = listaProducto.get(i);
            }
        }
        for (int i = 0; i < listaMedida.size(); i++) {
            if (listaMedida.get(i).getCodigo().equals(comercializadoraproducto.getComercializadoraproductoPK().getCodigomedida())) {
                this.medida = listaMedida.get(i);
            }
        }
        estadoComerProducto = comercializadoraproducto.getActivo();
        estadoAplicaIva = comercializadoraproducto.getSoloaplicaiva();        
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return comercializadoraproducto;
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
//            obj.put("codigo", precio.getCodigo());
//            obj.put("nombre", precio.getNombre());
            comerproductoPK.setCodigocomercializadora(comercializadora.getCodigo());
            comerproductoPK.setCodigomedida(medida.getCodigo());
            comerproductoPK.setCodigoproducto(producto.getCodigo());
            objPK.put("codigocomercializadora", comerproductoPK.getCodigocomercializadora());
            objPK.put("codigoproducto", comerproductoPK.getCodigoproducto());
            objPK.put("codigomedida", comerproductoPK.getCodigomedida());
            obj.put("comercializadoraproductoPK", objPK);
            obj.put("activo", estadoComerProducto);
            obj.put("margencomercializacion", comercializadoraproducto.getMargencomercializacion());
            obj.put("precioepp", comercializadoraproducto.getPrecioepp());
            obj.put("pvpsugerido", comercializadoraproducto.getPvpsugerido());
            obj.put("soloaplicaiva", estadoAplicaIva);
            obj.put("activo", estadoComerProducto);
            obj.put("procesar", false);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "PRODUCTO ACUTALIZADO EXITOSAMENTE");
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
        try {
            String respuesta;
            url = new URL(direccion + "/porId?codigocomercializadora=" + comercializadoraproducto.getComercializadoraproductoPK().getCodigocomercializadora()
                    + "&codigoproducto=" + comercializadoraproducto.getComercializadoraproductoPK().getCodigoproducto()
                    + "&codigomedida=" + comercializadoraproducto.getComercializadoraproductoPK().getCodigomedida());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");

            
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            comerproductoPK.setCodigocomercializadora(comercializadora.getCodigo());
            comerproductoPK.setCodigomedida(medida.getCodigo());
            comerproductoPK.setCodigoproducto(producto.getCodigo());
            objPK.put("codigocomercializadora", comerproductoPK.getCodigocomercializadora());
            objPK.put("codigoproducto", comerproductoPK.getCodigoproducto());
            objPK.put("codigomedida", comerproductoPK.getCodigomedida());
            obj.put("comercializadoraproductoPK", objPK);
            obj.put("activo", estadoComerProducto);
            obj.put("margencomercializacion", comercializadoraproducto.getMargencomercializacion());
            obj.put("precioepp", comercializadoraproducto.getPrecioepp());
            obj.put("pvpsugerido", comercializadoraproducto.getPvpsugerido());
            obj.put("soloaplicaiva", estadoAplicaIva);
            obj.put("activo", estadoComerProducto);
            obj.put("procesar", false);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();            
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "PRODUCTO ELIMINADO EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                obtenerProdComer();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoComerProducto() {
        editarPago = false; 
        soloLectura = false;
        estadoComerProducto = true;
        if(habilitarComer){
            comercializadora = new ComercializadoraBean();
        }
        comercializadoraproducto = new Comercializadoraproducto();        
        producto = new Producto();
        medida = new Medida();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public boolean isEstadoComerProducto() {
        return estadoComerProducto;
    }

    public void setEstadoComerProducto(boolean estadoComerProducto) {
        this.estadoComerProducto = estadoComerProducto;
    }

    public boolean isEstadoAplicaIva() {
        return estadoAplicaIva;
    }

    public void setEstadoAplicaIva(boolean estadoAplicaIva) {
        this.estadoAplicaIva = estadoAplicaIva;
    }

    public List<Producto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(List<Producto> listaProducto) {
        this.listaProducto = listaProducto;
    }

    public List<Comercializadoraproducto> getListaComerProductos() {
        return listaComerProductos;
    }

    public void setListaComerProductos(List<Comercializadoraproducto> listaComerProductos) {
        this.listaComerProductos = listaComerProductos;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public Comercializadoraproducto getComercializadoraproducto() {
        return comercializadoraproducto;
    }

    public void setComercializadoraproducto(Comercializadoraproducto comercializadoraproducto) {
        this.comercializadoraproducto = comercializadoraproducto;
    }

    public ComercializadoraproductoPK getComerproductoPK() {
        return comerproductoPK;
    }

    public void setComerproductoPK(ComercializadoraproductoPK comerproductoPK) {
        this.comerproductoPK = comerproductoPK;
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

    public List<Medida> getListaMedida() {
        return listaMedida;
    }

    public void setListaMedida(List<Medida> listaMedida) {
        this.listaMedida = listaMedida;
    }

    public Medida getMedida() {
        return medida;
    }

    public void setMedida(Medida medida) {
        this.medida = medida;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public boolean isEditarPago() {
        return editarPago;
    }

    public void setEditarPago(boolean editarPago) {
        this.editarPago = editarPago;
    }

    public Comercializadoraproducto getComercializadoraproductoBean() {
        return comercializadoraproductoBean;
    }

    public void setComercializadoraproductoBean(Comercializadoraproducto comercializadoraproductoBean) {
        this.comercializadoraproductoBean = comercializadoraproductoBean;
    }

    public boolean isHabilitarComer() {
        return habilitarComer;
    }

    public void setHabilitarComer(boolean habilitarComer) {
        this.habilitarComer = habilitarComer;
    }

    public boolean isHabilitarCli() {
        return habilitarCli;
    }

    public void setHabilitarCli(boolean habilitarCli) {
        this.habilitarCli = habilitarCli;
    }
    
    

}
