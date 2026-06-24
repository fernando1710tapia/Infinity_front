/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.preciosyfacturacion;

import ec.com.infinityone.bean.TerminalBean;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Comercializadoraproducto;
import ec.com.infinityone.modelo.ComercializadoraproductoPK;
import ec.com.infinityone.modelo.Listaprecio;
import ec.com.infinityone.modelo.ListaprecioPK;
import ec.com.infinityone.modelo.Listaprecioterminalproducto;
import ec.com.infinityone.modelo.ListaprecioterminalproductoPK;
import ec.com.infinityone.modelo.Medida;
import ec.com.infinityone.modelo.ObjetoNivel1;
import ec.com.infinityone.modelo.Producto;
import ec.com.infinityone.servicio.preciosyfacturacion.ListaprecioterminalproductoServicio;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author David
 */
@Named
@ViewScoped
public class ActualizarListaPrecioBean extends ReusableBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(ActualizarListaPrecioBean.class.getName());

    @Inject
    private TerminalBean terminalBean;
    @Inject
    private ListaprecioterminalproductoServicio listaprecioterminalproductoServicio;
    
    /*
    Variable que almacena varias Medidas
     */
    private List<Listaprecio> listaprecios;
    /*
    Variable que almacena productos comercializadora
     */
    private List<Comercializadoraproducto> listaProductosComer;
    /*
    Variable que almacena productos comercializadora
     */
    private List<ActualizarListaPrecioBean> listaListapreciobean;
    /*
    Variable que almacena varios Productos
     */
    private List<ObjetoNivel1> listaComercializadora;
    /*
    Variable que almacena varios Productos
     */
    private List<ObjetoNivel1> listaTerminal;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarPrecio;
    /*
    Variable que establece true or false para el estado de la Medida
     */
    private boolean estadoPrecio;

    private boolean agregarTerminal;

    private boolean mostrarTabla;

    private boolean mostrarMsj;

    private boolean guardarTerminal;

    //private Listaprecio listaprecio;
    
    private Listaprecio listaprecio1;

    private ListaprecioPK listaprecioPK;

    private Comercializadoraproducto productosComer;

    private BigDecimal margenValor;

    private ActualizarListaPrecioBean listapreciobean;

    private Comercializadoraproducto productosComerAux;

    private ComercializadoraproductoPK productosComerPK;

    private List<Listaprecioterminalproducto> precioTermProd;        

    private ListaprecioterminalproductoPK precioTermProdPK;

    private Boolean mostrarBusTrans;

    private String tipoBusquedaDocumento;

    private String tipo;

    private String codigoComer;

    private ObjetoNivel1 objeto1;

    private String comercializadoraT;
    private String comercializadoraNombreT;
    private String listaPrecioT;
    private String tipoT;

    private long codigoListaPrecioT;

    private BigDecimal valorMargen;

    private Producto producto;
    private Medida medida;

    /**
     * Constructor por defecto
     */
    public ActualizarListaPrecioBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecio";
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecio";
        editarPrecio = false;
        agregarTerminal = false;
        mostrarTabla = false;
        mostrarMsj = true;
        guardarTerminal = false;
        listaprecio1 = new Listaprecio();
        listaprecioPK = new ListaprecioPK();
        productosComer = new Comercializadoraproducto();
        productosComerAux = new Comercializadoraproducto();
        productosComerPK = new ComercializadoraproductoPK();
        precioTermProd = new ArrayList<>();
        precioTermProdPK = new ListaprecioterminalproductoPK();
        producto = new Producto();
        medida = new Medida();
        listapreciobean = new ActualizarListaPrecioBean();
        valorMargen = new BigDecimal(0);
        listaprecios = new ArrayList<>();
        listaProductosComer = new ArrayList<>();
        tipoBusquedaDocumento = "N";
        mostrarBusTrans = tipoBusquedaDocumento.equals("N");
        margenValor = new BigDecimal("0");
        obtenerListaComercializadora();
        obtenerListaTerminal();
        obtenerPrecio(listaComercializadora.get(0).getCodigo());
        //getURL();
    }

    public void actualizarTipoBusqueda() {
        mostrarBusTrans = tipoBusquedaDocumento.equals("N");
        agregarTerminal = true;
        if (tipoBusquedaDocumento.equals("N")) {
            mostrarTabla = false;
            mostrarMsj = true;
        } else {
            mostrarTabla = true;
            mostrarMsj = false;
            obtenerListaPrecioTerminalProduc(comercializadoraT);
        }

    }

    public void obtenerListaTerminal() {
        listaTerminal = new ArrayList<>();
        this.terminalBean.obtenerTermnial();
        listaTerminal = this.terminalBean.getListaTerminales();
    }

    public void obtenerListaPrecioTerminalProduc(String codigoComer) {
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));  
            
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.comercializadoraproducto/porComercializadora?codigocomercializadora=" + codigoComer);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadoraproducto/porComercializadora?codigocomercializadora=" + codigoComer);
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

            listaProductosComer = new ArrayList<>();
            listaListapreciobean = new ArrayList<>();

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
                JSONObject prodC = retorno.getJSONObject(indice);
                JSONObject comProd = prodC.getJSONObject("comercializadoraproductoPK");
                JSONObject prod = prodC.getJSONObject("producto");
                JSONObject med = prodC.getJSONObject("medida");
                productosComerPK.setCodigocomercializadora(comProd.getString("codigocomercializadora"));
                productosComerPK.setCodigoproducto(comProd.getString("codigoproducto"));
                productosComerPK.setCodigomedida(comProd.getString("codigomedida"));
                productosComer.setComercializadoraproductoPK(productosComerPK);
                productosComer.setMargencomercializacion(prodC.getBigDecimal("margencomercializacion"));
                productosComer.setPrecioepp(prodC.getBigDecimal("precioepp"));
                productosComer.setPvpsugerido(prodC.getBigDecimal("pvpsugerido"));
                productosComer.setActivo(prodC.getBoolean("activo"));
                productosComer.setSoloaplicaiva(prodC.getBoolean("soloaplicaiva"));
                productosComer.setSoloaplicaiva(prodC.getBoolean("soloaplicaiva"));
                producto.setCodigo(prod.getString("codigo"));
                producto.setNombre(prod.getString("nombre"));
                productosComer.setProducto(producto);
                medida.setCodigo(med.getString("codigo"));
                medida.setNombre(med.getString("nombre"));
                medida.setAbreviacion(med.getString("abreviacion"));
                productosComer.setMedida(medida);
                //this.setAbreviacion();
//                if (prodC.getBoolean("activo") == true) {
//                    productosComer.setActivo("S");
//                } else {
//                    productosComer.setActivo("N");
//                }
                productosComer.setUsuarioactual(prodC.getString("usuarioactual"));
//                if (productosComerAux.getComercializadoraproductoPK() != null) {
//                    if (productosComerAux.getProducto().getCodigo().equals(producto.getCodigo())) {
//                        valorMargen = productosComerAux.getMargenValorAux();
//                        if (valorMargen != null) {
//                            productosComer.setMargenValorAux(valorMargen);
//                        }
//                    }
//                }

                listapreciobean.setProductosComer(productosComer);
                listaProductosComer.add(productosComer);
                listaListapreciobean.add(listapreciobean);
                listapreciobean = new ActualizarListaPrecioBean();
                productosComerPK = new ComercializadoraproductoPK();
                productosComer = new Comercializadoraproducto();
                producto = new Producto();
                medida = new Medida();
            }
            guardarTerminal = true;
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerPrecio(String codigoComer) {
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));  
            
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecio/porComercializadora?codigocomercializadora=" + codigoComer);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecio/porComercializadora?codigocomercializadora=" + codigoComer);
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
                JSONObject listaP = retorno.getJSONObject(indice);
                JSONObject codCom = listaP.getJSONObject("listaprecioPK");
                listaprecioPK.setCodigocomercializadora(codCom.getString("codigocomercializadora"));
                listaprecioPK.setCodigo(codCom.getLong("codigo"));
                listaprecio1.setListaprecioPK(listaprecioPK);
                listaprecio1.setNombre(listaP.getString("nombre"));
                listaprecio1.setTipo(listaP.getString("tipo"));
                listaprecio1.setActivo(listaP.getBoolean("activo"));
                listaprecio1.setUsuarioactual(listaP.getString("usuarioactual"));
                listaprecios.add(listaprecio1);
                listaprecio1 = new Listaprecio();
                listaprecioPK = new ListaprecioPK();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
       
    public void actualizarLista() {
        if (listapreciobean != null) {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            codigoComer = params.get("form:comercializadora_input");
            listaprecios = new ArrayList<>();
            obtenerPrecio(codigoComer);           
        }
    }

    public void obtenerListaComercializadora() {
        //urlInfinity = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.areamercadeo
        //https://www.supertech.ec:8443/infinityone1/resources/usuario/login?user=Ftapia&password=123456";
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));  
            
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.comercializadora");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadora");
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

            listaComercializadora = new ArrayList<>();
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
                JSONObject comer = retorno.getJSONObject(indice);
                objeto1.setCodigo(comer.getString("codigo"));
                objeto1.setObjRelacionado(comer.getString("codigo") + " - " + comer.getString("nombre"));
                listaComercializadora.add(objeto1);
                objeto1 = new ObjetoNivel1();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (guardarTerminal) {
            addItemsTerminalProd();
            obtenerPrecio(listaComercializadora.get(0).getCodigo());
        } else {
            if (editarPrecio) {
                agregarTerminal = false;
                editItems();
                obtenerPrecio(listaComercializadora.get(0).getCodigo());
            } else {
                agregarTerminal = true;
                if (addItems()) {
                    PrimeFaces.current().executeScript("PF('configTermDialog').show()");                    
                }                
            }
        }
    }

    public void guardarMargen() {
        if (listapreciobean.getMargenValor() != null) {
            //obtenerListaPrecioTerminalProduc(comercializadoraT);
            for (int i = 0; i < this.listaProductosComer.size(); i++) {
                if (listapreciobean.getProductosComer().getComercializadoraproductoPK() != null) {
                    if (listapreciobean.getProductosComer().getProducto().getCodigo().equals(listaProductosComer.get(i).getProducto().getCodigo())) {
                        valorMargen = listapreciobean.getMargenValor();
                        if (valorMargen != null) {
                            this.listaListapreciobean.get(i).setMargenValor(valorMargen);
                        }
                    }
                }
            }
            PrimeFaces.current().executeScript("PF('margen').hide()");
        }
    }

    public void configTerminal(int tipo) {
        if (tipo == 1) {
            agregarTerminal = true;
            guardarTerminal = true;
            terminalProducto();
        } else {
            agregarTerminal = false;            
            obtenerPrecio(listaComercializadora.get(0).getCodigo());
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
        }
    }

    public boolean addItems() {
        try {
            String respuesta;
            listaprecioPK.setCodigocomercializadora(codigoComer);
            listaprecio1.setListaprecioPK(listaprecioPK);

            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPk = new JSONObject();
            objPk.put("codigocomercializadora", listaprecioPK.getCodigocomercializadora());
            objPk.put("codigo", 0);
            obj.put("listaprecioPK", objPk);
            obj.put("nombre", listaprecio1.getNombre());
            obj.put("tipo", listaprecio1.getTipo());
            obj.put("activo", estadoPrecio);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "LISTA PRECIO REGISTRADA EXITOSAMENTE");
                return true;
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
            JSONObject objPk = new JSONObject();
            objPk.put("codigocomercializadora", listaprecio1.getListaprecioPK().getCodigocomercializadora());
            objPk.put("codigo", listaprecio1.getListaprecioPK().getCodigo());
            obj.put("listaprecioPK", objPk);
            obj.put("nombre", listaprecio1.getNombre());
            obj.put("tipo", listaprecio1.getTipo());
            obj.put("activo", listaprecio1.getActivo());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "LISTA PRECIO ACUTALIZADA EXITOSAMENTE");
            } else {
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
            url = new URL(direccion + "/porId?codigo=" + listaprecio1.getListaprecioPK().getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            //connection.connect();

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPk = new JSONObject();
            objPk.put("codigocomercializadora", listaprecio1.getListaprecioPK().getCodigocomercializadora());
            objPk.put("codigo", listaprecio1.getListaprecioPK().getCodigo());
            obj.put("listaprecioPK", objPk);
            obj.put("nombre", listaprecio1.getNombre());
            obj.put("tipo", listaprecio1.getTipo());
            obj.put("activo", listaprecio1.getActivo());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "LISTA PRECIO ELIMINADA EXITOSAMENTE");
                obtenerPrecio(listaComercializadora.get(0).getCodigo());
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addItemsTerminalProd() {
        obtenerPrecio(comercializadoraT);
            for(int j = 0; j<listaprecios.size(); j++){
                if(listaprecios.get(j).getNombre().equals(listaPrecioT)){
                    codigoListaPrecioT = listaprecios.get(j).getListaprecioPK().getCodigo();
                }
            }
        for (int i = 0; i < listaTerminal.size(); i++) {
            for (int indice = 0; indice < listaProductosComer.size(); indice++) { 
                if(listaListapreciobean.get(indice).getMargenValor() != null){
                    addItemsTerminal(i, indice);
                }                
            }
        }
    }

    public void addItemsTerminal(int i, int indice) {
        try {
            String respuesta;                        
            
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecioterminalproducto");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecioterminalproducto");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

//            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//            List<JSONObject> listObj = new ArrayList<>();
//            JSONObject obj = new JSONObject();
//            JSONObject objPk = new JSONObject();
//            for (int i = 0; i < listaTerminal.size(); i++) {
//                for (int indice = 0; indice < listaProductosComer.size(); indice++) {
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    //List<JSONObject> listObj = new ArrayList<>();
                    JSONObject obj = new JSONObject();
                    JSONObject objPk = new JSONObject();
                    objPk.put("codigocomercializadora", listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigocomercializadora());
                    objPk.put("codigolistaprecio", codigoListaPrecioT);
                    objPk.put("codigoterminal", listaTerminal.get(i).getCodigo());
                    objPk.put("codigoproducto", listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigoproducto());
                    objPk.put("codigomedida", listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigomedida());
                    obj.put("listaprecioterminalproductoPK", objPk);
                    if (tipoT.equals("MPO - Margen sobre el precio en terminal")) {
                        if(listaListapreciobean.get(indice).getMargenValor() != null){                            
                            obj.put("margenporcentaje", listaListapreciobean.get(indice).getMargenValor());
                            obj.put("margenvalorcomercializadora", -99);
                        }                        
                    } else {
                        if(listaListapreciobean.get(indice).getMargenValor() != null){                            
                            obj.put("margenvalorcomercializadora", listaListapreciobean.get(indice).getMargenValor());
                            obj.put("margenporcentaje", -99);
                        }                           
                    }
                    obj.put("usuarioactual", dataUser.getUser().getNombrever());
                    respuesta = obj.toString();
                    writer.write(respuesta);
                    writer.close();
//                    obj = new JSONObject();
//                    objPk = new JSONObject();
//                    listObj.add(obj);
//                    objPk = new JSONObject();
//                    obj = new JSONObject();
//                }
//            }
//            respuesta = listObj.toString();
//            writer.write(respuesta);
//            writer.close();
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "LISTA PRECIO TERMINAL PRODUCTO REGISTRADA EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                guardarTerminal = false;
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
            }
            guardarTerminal = false;
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void addItemsTerminal() {
//        try {
//            String respuesta;
//            url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecioterminalproducto");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoOutput(true);
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-type", "application/json");
//
////            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
////            List<JSONObject> listObj = new ArrayList<>();
////            JSONObject obj = new JSONObject();
////            JSONObject objPk = new JSONObject();
//            for (int i = 0; i < listaTerminal.size(); i++) {
//                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//                for (int indice = 0; indice < listaListapreciobean.size(); indice++) {
//
//                    List<JSONObject> listObj = new ArrayList<>();
//                    JSONObject obj = new JSONObject();
//                    JSONObject objPk = new JSONObject();
//                    objPk.put("codigocomercializadora", listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigocomercializadora());
//                    objPk.put("codigolistaprecio", codigoListaPrecioT);
//                    objPk.put("codigoterminal", listaTerminal.get(i).getCodigo());
//                    objPk.put("codigoproducto", listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigoproducto());
//                    objPk.put("codigomedida", listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigomedida());
//                    obj.put("listaprecioterminalproductoPK", objPk);
//                    if (tipoT.equals("MPO - Margen sobre el precio en terminal")) {
//                        obj.put("margenporcentaje", listaListapreciobean.get(indice).getMargenValorAux());
//                    } else {
//                        obj.put("margenvalorcomercializadora", listaListapreciobean.get(indice).getMargenValorAux());
//                    }
//                    obj.put("usuarioactual", dataUser.getUser().getNombrever());
//                    respuesta = obj.toString();
//                    writer.write(respuesta);
//                    writer.close();
//                    obj = new JSONObject();
//                    objPk = new JSONObject();
//                    if (connection.getResponseCode() == 200) {
//                        this.dialogo(FacesMessage.SEVERITY_INFO, "LISTA PRECIO TERMINAL PRODUCTO REGISTRADA EXITOSAMENTE");
//                        PrimeFaces.current().executeScript("PF('nuevo').hide()");
//                        guardarTerminal = false;
//                    } else {
//                        this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
//                        System.out.println(connection.getResponseCode());
//                        System.out.println(connection.getResponseMessage());
//                    }
//
////                    listObj.add(obj);
////                    objPk = new JSONObject();
////                    obj = new JSONObject();
//                }
//                //writer.close();
//            }
////            respuesta = listObj.toString();
////            writer.write(respuesta);
////            writer.close();
//
//            guardarTerminal = false;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public void nuevaListaPrecio() {
        estadoPrecio = true;
        editarPrecio = false;
        this.setAbreviacion("");
        listaprecio1 = new Listaprecio();
        listaprecioPK = new ListaprecioPK();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Listaprecio editarPrecio(Listaprecio obj) {
        editarPrecio = true;
        agregarTerminal = false;
        listaprecio1 = obj;
        if (listaprecio1 != null) {
            estadoPrecio = listaprecio1.getActivo();
        }
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return listaprecio1;
    }

    public ActualizarListaPrecioBean editarMargen(ActualizarListaPrecioBean obj) {
        editarPrecio = true;
        agregarTerminal = false;
        listapreciobean = obj;
        PrimeFaces.current().executeScript("PF('margen').show()");
        return listapreciobean;
    }

    public Listaprecio terminalProducto() {
        //listaPrecio = this.getListaPrecio();
        if (listaprecio1 != null) {
            comercializadoraT = listaprecio1.getListaprecioPK().getCodigocomercializadora();
            for (int i = 0; i < listaComercializadora.size(); i++) {
                if (listaComercializadora.get(i).getCodigo().equals(listaprecio1.getListaprecioPK().getCodigocomercializadora())) {
                    comercializadoraNombreT = listaComercializadora.get(i).getObjRelacionado();
                }
            }
            codigoListaPrecioT = listaprecio1.getListaprecioPK().getCodigo();
            listaPrecioT = listaprecio1.getNombre();
            if (listaprecio1.getTipo().equals("MPO")) {
                tipoT = "MPO - Margen sobre el precio en terminal";
            } else {
                tipoT = "MCO - Margen sobre el margen de comercializacion";
            }
        }
        return listaprecio1;
    }
    
    public void onRowToggle(ToggleEvent event) {
        if (event.getVisibility() == Visibility.VISIBLE) {
            Listaprecio listaD = (Listaprecio) event.getData();
            if (listaD.getListaprecioPK().getCodigocomercializadora()!= null) {
                precioTermProd = new ArrayList<>();
                List<Listaprecioterminalproducto> precioTermProdAux;
                precioTermProdAux = listaprecioterminalproductoServicio.obtenerListaprecioterminalprod(listaD.getListaprecioPK().getCodigo());               
                for(int i=0; i<precioTermProdAux.size(); i++){
                    if(precioTermProdAux.get(i).getMargenporcentaje().longValueExact() < 0){
                        precioTermProdAux.get(i).setMargenporcentaje(new BigDecimal(0));
                    }
                    if(precioTermProdAux.get(i).getMargenvalorcomercializadora().longValueExact() < 0){
                        precioTermProdAux.get(i).setMargenvalorcomercializadora(new BigDecimal(0));
                    }
                }
                precioTermProd = precioTermProdAux;                
            }
        }
    }

    public ActualizarListaPrecioBean getListapreciobean() {
        return listapreciobean;
    }

    public void setListapreciobean(ActualizarListaPrecioBean listapreciobean) {
        this.listapreciobean = listapreciobean;
    }       

    public BigDecimal getMargenValor() {
        return margenValor;
    }

    public void setMargenValor(BigDecimal margenValor) {
        this.margenValor = margenValor;
    }

    

    public List<ActualizarListaPrecioBean> getListaListapreciobean() {
        return listaListapreciobean;
    }

    public void setListaListapreciobean(List<ActualizarListaPrecioBean> listaListapreciobean) {
        this.listaListapreciobean = listaListapreciobean;
    }

    public List<ObjetoNivel1> getListaTerminal() {
        return listaTerminal;
    }

    public void setListaTerminal(List<ObjetoNivel1> listaTerminal) {
        this.listaTerminal = listaTerminal;
    }

    public Comercializadoraproducto getProductosComerAux() {
        return productosComerAux;
    }

    public void setProductosComerAux(Comercializadoraproducto productosComerAux) {
        this.productosComerAux = productosComerAux;
    }

    public BigDecimal getValorMargen() {
        return valorMargen;
    }

    public void setValorMargen(BigDecimal valorMargen) {
        this.valorMargen = valorMargen;
    }

    public List<Listaprecioterminalproducto> getPrecioTermProd() {
        return precioTermProd;
    }

    public void setPrecioTermProd(List<Listaprecioterminalproducto> precioTermProd) {
        this.precioTermProd = precioTermProd;
    }
    
    public String getComercializadoraNombreT() {
        return comercializadoraNombreT;
    }

    public void setComercializadoraNombreT(String comercializadoraNombreT) {
        this.comercializadoraNombreT = comercializadoraNombreT;
    }

    public List<Comercializadoraproducto> getListaProductosComer() {
        return listaProductosComer;
    }

    public void setListaProductosComer(List<Comercializadoraproducto> listaProductosComer) {
        this.listaProductosComer = listaProductosComer;
    }

    public Comercializadoraproducto getProductosComer() {
        return productosComer;
    }

    public void setProductosComer(Comercializadoraproducto productosComer) {
        this.productosComer = productosComer;
    }

    public boolean isMostrarMsj() {
        return mostrarMsj;
    }

    public void setMostrarMsj(boolean mostrarMsj) {
        this.mostrarMsj = mostrarMsj;
    }

    public boolean isMostrarTabla() {
        return mostrarTabla;
    }

    public void setMostrarTabla(boolean mostrarTabla) {
        this.mostrarTabla = mostrarTabla;
    }

    public String getTipoBusquedaDocumento() {
        return tipoBusquedaDocumento;
    }

    public void setTipoBusquedaDocumento(String tipoBusquedaDocumento) {
        this.tipoBusquedaDocumento = tipoBusquedaDocumento;
    }

    public boolean isAgregarTerminal() {
        return agregarTerminal;
    }

    public void setAgregarTerminal(boolean agregarTerminal) {
        this.agregarTerminal = agregarTerminal;
    }

    public String getComercializadoraT() {
        return comercializadoraT;
    }

    public void setComercializadoraT(String comercializadoraT) {
        this.comercializadoraT = comercializadoraT;
    }

    public long getCodigoListaPrecioT() {
        return codigoListaPrecioT;
    }

    public void setCodigoListaPrecioT(long codigoListaPrecioT) {
        this.codigoListaPrecioT = codigoListaPrecioT;
    }

    public String getListaPrecioT() {
        return listaPrecioT;
    }

    public void setListaPrecioT(String listaPrecioT) {
        this.listaPrecioT = listaPrecioT;
    }

    public String getTipoT() {
        return tipoT;
    }

    public void setTipoT(String tipoT) {
        this.tipoT = tipoT;
    }

    public List<Listaprecio> getListaprecios() {
        return listaprecios;
    }

    public void setListaprecios(List<Listaprecio> listaprecios) {
        this.listaprecios = listaprecios;
    }

    public List<ObjetoNivel1> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ObjetoNivel1> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public Listaprecio getListaprecio1() {
        return listaprecio1;
    }

    public void setListaprecio1(Listaprecio listaprecio1) {
        this.listaprecio1 = listaprecio1;
    }
    
    public boolean isEditarPrecio() {
        return editarPrecio;
    }

    public void setEditarPrecio(boolean editarPrecio) {
        this.editarPrecio = editarPrecio;
    }

    public boolean isEstadoPrecio() {
        return estadoPrecio;
    }

    public void setEstadoPrecio(boolean estadoPrecio) {
        this.estadoPrecio = estadoPrecio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodigoComer() {
        return codigoComer;
    }

    public void setCodigoComer(String codigoComer) {
        this.codigoComer = codigoComer;
    }

    public ListaprecioPK getListaprecioPK() {
        return listaprecioPK;
    }

    public void setListaprecioPK(ListaprecioPK listaprecioPK) {
        this.listaprecioPK = listaprecioPK;
    }

}
