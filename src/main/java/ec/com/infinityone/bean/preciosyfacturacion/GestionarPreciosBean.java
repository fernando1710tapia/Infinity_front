/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.preciosyfacturacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraProductoServicio;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.servicio.catalogo.TerminalService;
import ec.com.infinityone.modelo.Comercializadora;
import ec.com.infinityone.modelo.Comercializadoraproducto;
import ec.com.infinityone.modelo.ComercializadoraproductoPK;
import ec.com.infinityone.modelo.Detalleprecio;
import ec.com.infinityone.modelo.DetalleprecioPK;
import ec.com.infinityone.modelo.Gravamen;
import ec.com.infinityone.modelo.GravamenPK;
import ec.com.infinityone.modelo.Listaprecio;
import ec.com.infinityone.modelo.ListaprecioPK;
import ec.com.infinityone.modelo.Listaprecioterminalproducto;
import ec.com.infinityone.modelo.ListaprecioterminalproductoPK;
import ec.com.infinityone.modelo.Medida;
import ec.com.infinityone.modelo.ObjetoDetallePrecio;
import ec.com.infinityone.modelo.ObjetoPrecio;
import ec.com.infinityone.modelo.Precio;
import ec.com.infinityone.modelo.PrecioPK;
import ec.com.infinityone.modelo.Producto;
import ec.com.infinityone.modelo.Terminal;
import ec.com.infinityone.servicio.preciosyfacturacion.GravamenServicio;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.configuration.Fichero;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author HP
 */
@Named
@ViewScoped
public class GestionarPreciosBean extends ReusableBean implements Serializable {

    /*
    Variable para acceder a los servicios de Comercialziadora
     */
    @Inject
    private ComercializadoraServicio comerServicio;
    /*
    Variable para acceder a los servicios de Comercialziadora Producto Sevicio
     */
    @Inject
    private ComercializadoraProductoServicio comerProdServicio;
    /*
    Variable para acceder a los servicios de Gravamen Sevicio
     */
    @Inject
    private GravamenServicio gravamenServicio;
    /*
    Variable para acceder a los servicios de Terminal
     */
    @Inject
    private TerminalService terminalServicio;
    /*
    Variable Comercializadora
     */
    private ComercializadoraBean comercializadora;
    /*
    Variable para almacenar los datos comercializadora
     */
    private List<ComercializadoraBean> listaComercializadora;
    /*
    Variable Comercializadora
     */
    private Comercializadora comerc;
    /*
    Variable Gravamen
     */
    private Gravamen gravamen;
    /*
    Variable que almacena varios Gravamenes
     */
    private List<Gravamen> listaGravamen;
    /*
    Variable Gravamen PK
     */
    private GravamenPK gravamenPK;
    /*
    Variable Fecha Vencimiento
     */
    private Date fechaVencimiento;
    /*
    Variable para la configuración del Terminal
     */
    private String confTerminal;
    /*
    Variable codigo comercializadora
     */
    private String codComer;
    /*
    Variable que almacena varias Comercialziadoras Producto
     */
    private List<Comercializadoraproducto> listaComerProductos;
    /*
    Variabale que guarda una Comercializadora Producto
     */
    private Comercializadoraproducto comercializadoraProducto;
    /*
    Variable que guarda Comercializadora Pruducto PK
     */
    private ComercializadoraproductoPK comercializadoraProductoPK;
    /*
    Variable para la observación
     */
    private String observacion;
    /*
    Variabale para mostrar el paso 1
     */
    private boolean mostarPaso1;
    /*
    Variabale para mostrar el paso 2
     */
    private boolean mostarPaso2;
    /*
    Variable para almacenar el nombre de la comercializadora
     */
    private String nombreComercializadora;
    /*
    Variabel para colocar valor del terminal
     */
    private String terminalValor;
    /*
    Objeto Precio
     */
    private ObjetoPrecio objprecio;
    /*
    Variable que almacena varios Objetos Precio
     */
    private List<ObjetoPrecio> listaPrecio;
    /*
    Variable Precio
     */
    private Precio precio;
    /*
    Variable que alamcena varios precios
     */
    private List<Precio> listPrice;
    /*
    Variable Precio PK
     */
    private PrecioPK precioPK;
    /*
    Variable Producto
     */
    private Producto product;
    /*
    Variable Medida
     */
    private Medida medida;
    /*
    Variable Lista Precio
     */
    private Listaprecio precioLista;
    /*
    Variable Lista Precio PK
     */
    private ListaprecioPK precioListaPK;
    /*
    Objeto Detalle Precio
     */
    private Detalleprecio detallePrecio;
    /*
    Objeto Detalle Precio PK
     */
    private DetalleprecioPK detallePrecioPK;
    /*
    Variable que guarda varios Detalles Preciu
     */
    private List<Detalleprecio> listaDetallePrecio;
    /*
    Variable que almacena varias terminales
     */
    private List<Terminal> listaTerminales;
    /*
    Variable Lista Precio Terminal Producto
     */
    private List<Listaprecioterminalproducto> listaTerminalProd;
    /*
    Variable Lista Precio Terminal Producto
     */
    private List<Listaprecioterminalproducto> listaTerminalProdAux;
    /*
    Varibale 
     */
    private Listaprecioterminalproducto terminalProducto;
    /*
    Variable terminal producto pk
     */
    private ListaprecioterminalproductoPK terminalProductoPK;
    /*
    Vraible Terminal
     */
    private Terminal terminal;
    /*
    Variable para almacenar y mostar el Detalle de Precios en el Paso 3
     */
    private ObjetoDetallePrecio objDetalle;
    /*
    Lista que almacena varios detelles precios para ser mostrados en el paso 3
     */
    private List<ObjetoDetallePrecio> listaObjDetalle;

    private List<Precio> precioError;

    private List<Precio> precioGuardado;

    private List<Detalleprecio> detPrecioError;

    private List<Detalleprecio> detPrecioGuardado;
    /*
    Variable para verificar si se realiza o no la consultarPorIdPrecios
     */
    private boolean contsultaPrecios;
    /*
    Variable para inhabilitar botones en caso de que la comercializadora se encuentre activa
     */
    private boolean inhabilitar;
    /*
    Variables para mostrar o desaparecer forms
     */
    private boolean step1;
    private boolean step2;
    private boolean step3;

    /**
     * Constructor por defecto
     */
    public GestionarPreciosBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        codComer = "";
        comercializadoraProducto = new Comercializadoraproducto();
        comercializadoraProductoPK = new ComercializadoraproductoPK();
        nombreComercializadora = "";
        terminalValor = "";
        confTerminal = "1";
        objprecio = new ObjetoPrecio();
        listaPrecio = new ArrayList<>();
        listPrice = new ArrayList<>();
        precio = new Precio();
        precioPK = new PrecioPK();
        product = new Producto();
        medida = new Medida();
        precioLista = new Listaprecio();
        precioListaPK = new ListaprecioPK();
        observacion = "";
        fechaVencimiento = new Date();
        listaGravamen = new ArrayList<>();
        gravamen = new Gravamen();
        gravamenPK = new GravamenPK();
        detallePrecio = new Detalleprecio();
        detallePrecioPK = new DetalleprecioPK();
        listaDetallePrecio = new ArrayList<>();
        listaTerminales = new ArrayList<>();
        terminalProducto = new Listaprecioterminalproducto();
        terminalProductoPK = new ListaprecioterminalproductoPK();
        listaTerminalProd = new ArrayList<>();
        listaTerminalProdAux = new ArrayList<>();
        listaComerProductos = new ArrayList<>();
        terminal = new Terminal();
        objDetalle = new ObjetoDetallePrecio();
        listaObjDetalle = new ArrayList<>();
        contsultaPrecios = false;
        inhabilitar = false;
        step1 = true;
        step2 = false;
        step3 = false;
        obtenerComercializadora();
    }

    public void reestablecer() {
        codComer = "";
        comercializadoraProducto = new Comercializadoraproducto();
        comercializadoraProductoPK = new ComercializadoraproductoPK();
        nombreComercializadora = "";
        terminalValor = "";
        objprecio = new ObjetoPrecio();
        listaPrecio = new ArrayList<>();
        listPrice = new ArrayList<>();
        precio = new Precio();
        precioPK = new PrecioPK();
        product = new Producto();
        medida = new Medida();
        precioLista = new Listaprecio();
        precioListaPK = new ListaprecioPK();
        observacion = "";
        fechaVencimiento = new Date();
        listaGravamen = new ArrayList<>();
        gravamen = new Gravamen();
        gravamenPK = new GravamenPK();
        detallePrecio = new Detalleprecio();
        detallePrecioPK = new DetalleprecioPK();
        listaDetallePrecio = new ArrayList<>();
        listaTerminales = new ArrayList<>();
        terminalProducto = new Listaprecioterminalproducto();
        terminalProductoPK = new ListaprecioterminalproductoPK();
        listaTerminalProd = new ArrayList<>();
        listaTerminalProdAux = new ArrayList<>();
        terminal = new Terminal();
        objDetalle = new ObjetoDetallePrecio();
        listaObjDetalle = new ArrayList<>();
    }

    public void regresarPaso1() {
        step1 = true;
        step2 = false;
        step3 = false;
    }

    public void regresarPaso2() {
        step1 = false;
        step2 = true;
        step3 = false;
        listaObjDetalle = new ArrayList<>();
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = comerServicio.obtenerComercializadoras();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda();
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
                if (comercializadora.getActivo().equals("S")) {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA DEBE ESTAR INACTIVA PARA PODER REALIZAR LA ACTUALIZACIÓN DE PRECIOS");
                } else {
                    seleccionarComercializdora();
                }
            }
            if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
                if (comercializadora.getActivo().equals("S")) {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA DEBE ESTAR INACTIVA PARA PODER REALIZAR LA ACTUALIZACIÓN DE PRECIOS");
                } else {
                    seleccionarComercializdora();
                }
            }
        }
    }

    public void seleccionarComercializdora() {
        if (comercializadora != null) {
            if (comercializadora.getActivo().equals("N")) {
                inhabilitar = true;
                codComer = comercializadora.getCodigo();
                listaComerProductos = new ArrayList<>();
                listaComerProductos = comerProdServicio.obtenerProductos(codComer);
                listaGravamen = new ArrayList<>();
                listaGravamen = gravamenServicio.obtenerGravamenes(codComer);
            } else {
                inhabilitar = false;
                listaComerProductos = new ArrayList<>();
                listaGravamen = new ArrayList<>();
                this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA DEBE ESTAR INACTIVA PARA PODER REALIZAR LA ACTUALIZACIÓN DE PRECIOS");
            }
        }
    }

    public void configuracionTerminal() {
        if (!confTerminal.equals("1")) {
            PrimeFaces.current().executeScript("PF('termialDialogo').show()");
        } else {
            terminalValor = "Para todos los terminales";
            listaTerminales = terminalServicio.obtenerTerminal();
        }
    }

    public void paso1() {
        step1 = false;
        step2 = true;
        step3 = false;
        ArrayList<Comercializadoraproducto> listaComerProductosAux = new ArrayList<>();
        try {
            if (comercializadora.getActivo().equals("N")) {
                listaTerminalProd = new ArrayList<>();
                listaPrecio = new ArrayList<>();
                listPrice = new ArrayList<>();
                for (int i = 0; i < listaComerProductos.size(); i++) {
                    if (listaComerProductos.get(i).isProcesar()) {
//                    actualizarDatosComerProd(listaComerProductos.get(i));
                        listaComerProductosAux.add(listaComerProductos.get(i));
//                    consultarPorIdPrecios(listaComerProductos.get(i));
                    }
                }
                if (!listaComerProductosAux.isEmpty()) {
                    actualizarDatosComerProdLote(listaComerProductosAux);
                } else {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "Error!. No se ha seleccionado ningún producto!");
                }
                if (!listaComerProductosAux.isEmpty()) {
                    for (int i = 0; i < listaComerProductosAux.size(); i++) {
                        obtenerPrecioscomprobacion(listaComerProductosAux.get(i));
                        consultarPorIdPrecios(listaComerProductosAux.get(i));
                    }
                } else {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "Error!. No se ha seleccionado ningún producto!");
                }

            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA DEBE ESTAR INCATIVA PARA PODER REALIZAR LA ACTUALIZACIÓN DE PRECIOS");
            }
        } catch (Throwable t) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, t.getMessage());
            System.out.println("FT:: Error capturado paso1 " + t.getMessage());
            t.printStackTrace(System.out);
            System.out.println("FT:: Error capturado TERMINA t.printStackTrace paso1 ----------------------");
        }
    }

    public void actualizarDatosComerProd(Comercializadoraproducto comerP) {
        try {
            comerP.setProcesar(false);
            String respuesta;
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.comercializadoraproducto";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadoraproducto";

            url = new URL(direcc + "/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000 * 60);
            connection.setReadTimeout(1000 * 60);
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(comerP);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();

            if (connection.getResponseCode() == 200) {
                //this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA REGISTRADA EXITOSAMENTE");
                obtenerPrecioscomprobacion(comerP);
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACUTALIZAR PRECIOS BASES PRODUCTOS");
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println("Error:" + connection.getErrorStream());
                System.out.println(connection.getResponseMessage());
            }
            //obtenerPreciosModificados(comerP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actualizarDatosComerProdLote(ArrayList<Comercializadoraproducto> listaComerProductosAux) throws Throwable {
//        listaFacturaAux = new ArrayList<>();
        List<JSONObject> arregloJSON = new ArrayList<>();
        JSONObject comercializadoraProducto = new JSONObject();
        JSONObject comercializadoraProductoPK = new JSONObject();

        try {
            int longitud = listaComerProductosAux.size();
            for (int i = 0; i < longitud; i++) {

                comercializadoraProductoPK.put("codigocomercializadora", ((Comercializadoraproducto) listaComerProductosAux.get(i)).getComercializadoraproductoPK().getCodigocomercializadora());
                comercializadoraProductoPK.put("codigoproducto", ((Comercializadoraproducto) listaComerProductosAux.get(i)).getComercializadoraproductoPK().getCodigoproducto());
                comercializadoraProductoPK.put("codigomedida", ((Comercializadoraproducto) listaComerProductosAux.get(i)).getComercializadoraproductoPK().getCodigomedida());
                comercializadoraProducto.put("comercializadoraproductoPK", comercializadoraProductoPK);

                comercializadoraProducto.put("activo", ((Comercializadoraproducto) listaComerProductosAux.get(i)).getActivo());
                comercializadoraProducto.put("margencomercializacion", ((Comercializadoraproducto) listaComerProductosAux.get(i)).getMargencomercializacion());
                comercializadoraProducto.put("precioepp", ((Comercializadoraproducto) listaComerProductosAux.get(i)).getPrecioepp());
                comercializadoraProducto.put("pvpsugerido", ((Comercializadoraproducto) listaComerProductosAux.get(i)).getPvpsugerido());
                comercializadoraProducto.put("soloaplicaiva", ((Comercializadoraproducto) listaComerProductosAux.get(i)).getSoloaplicaiva());
                comercializadoraProducto.put("usuarioactual", ((Comercializadoraproducto) listaComerProductosAux.get(i)).getUsuarioactual());

//            arregloJSON.addAll(addPagoFacturaRechazadosArregloJSON(listaComerProductosAux));
                arregloJSON.add(comercializadoraProducto);
                comercializadoraProducto = new JSONObject();
                comercializadoraProductoPK = new JSONObject();
            }
            //------ACTUALIZAR COMERPROD EN LA BDD------------------

            String respuesta;
            String direcc = "";

//            direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadoraproducto/actualizarLote";
            direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadoraproducto/updateLote";

            url = new URL(direcc);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            //JSONObject arrObj = new JSONObject();               

            //arrObj.put("", arregloJSON); 
            respuesta = arregloJSON.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                BufferedReader br = new BufferedReader(reader);
                String tmp = null;
                String resp = "";
                while ((tmp = br.readLine()) != null) {
                    resp += tmp;
                }
                JSONObject objetoJson = new JSONObject(resp);
                String cod = objetoJson.getString("developerMessage");
//                this.dialogo(FacesMessage.SEVERITY_INFO, cod);
                System.out.println(cod);
//                return true;
            } else {
//                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println("Error:" + connection.getErrorStream());
                System.out.println(connection.getResponseMessage());
                throw new Throwable("Error capturado en actualizarDatosComerProdLote. " + connection.getResponseCode());

            }

            //------------------------------------------------------
        } catch (Throwable t) {
            System.out.println("FT:: error capturado en actualizarDatosComerProdLote ." + t.getMessage());
            throw new Throwable("FT:: error capturado en actualizarDatosComerProdLote ." + t.getMessage());
        }

    }

    public boolean obtenerPrecioscomprobacion(Comercializadoraproducto comerP) {
        try {
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecioterminalproducto/paraPrecioUno?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecioterminalproducto/paraPrecioUno?";

            url = new URL(direcc + "codigocomercializadora=" + comerP.getComercializadoraproductoPK().getCodigocomercializadora()
                    + "&codigoproducto=" + comerP.getProducto().getCodigo()
                    + "&codigomedida=" + comerP.getMedida().getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            objprecio = new ObjetoPrecio();
            terminalProducto = new Listaprecioterminalproducto();
            terminalProductoPK = new ListaprecioterminalproductoPK();
            listaTerminalProd = new ArrayList<>();
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
                if (!retorno.isNull(indice)) {
                    JSONObject objTerminalP = retorno.getJSONObject(indice);
                    JSONObject objTerminalPK = objTerminalP.getJSONObject("listaprecioterminalproductoPK");

                    terminalProductoPK.setCodigocomercializadora(objTerminalPK.getString("codigocomercializadora"));
                    terminalProductoPK.setCodigolistaprecio(objTerminalPK.getLong("codigolistaprecio"));
                    terminalProductoPK.setCodigoterminal(objTerminalPK.getString("codigoterminal"));
                    terminalProductoPK.setCodigoproducto(objTerminalPK.getString("codigoproducto"));
                    terminalProductoPK.setCodigomedida(objTerminalPK.getString("codigomedida"));
                    terminalProducto.setListaprecioterminalproductoPK(terminalProductoPK);

                    listaTerminalProd.add(terminalProducto);
                    terminalProducto = new Listaprecioterminalproducto();
                    terminalProductoPK = new ListaprecioterminalproductoPK();

                }
            }
            if (connection.getResponseCode() == 200) {
                //this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA REGISTRADA EXITOSAMENTE");
                contsultaPrecios = true;
                //consultarPorIdPrecios(comerP);
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println("Error:" + connection.getErrorStream());
                System.out.println(connection.getResponseMessage());
                contsultaPrecios = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return contsultaPrecios;
    }

    public boolean verificarMismoPrecio() {
        try {
            StringBuilder cadenaInfo = new StringBuilder();
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio/porPreciomismodia?";

            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            String fechaI = date.format(fechaVencimiento);

            if (!codComer.isEmpty()) {
                url = new URL(direcc + "codigocomercializadora=" + codComer
                        + "&fechainicio=" + fechaI
                        + "&activo=" + Boolean.TRUE);

                System.out.println("FT:: verificando fecha del mismo día. " + url);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                InputStreamReader reader = new InputStreamReader(connection.getInputStream());

                BufferedReader br = new BufferedReader(reader);
                String tmp = null;
                String respuesta = "";
                while ((tmp = br.readLine()) != null) {
                    respuesta += tmp;
                }
                JSONObject objetoJson = new JSONObject(respuesta);
                JSONArray retorno = objetoJson.getJSONArray("retorno");

                if (retorno.length() > 0) {

                    System.out.println("FT:: SI HAY PRECIOS del mismo día. " + retorno.length());
                    cadenaInfo.append("Existen Precios VIGENTES en la Fecha Seleccionada");
                    cadenaInfo.append("\nNO DEBERÍA crear Precios en la misma Fecha");
                    cadenaInfo.append("\nSI ES IMPRESCIBLE hacerlo COORDINE CON SUPERTECH.EC!!");

                    //this.dialogo(FacesMessage.SEVERITY_INFO, cadenaInfo.toString());
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "Existen Precios VIGENTES en la Fecha Seleccionada\nNO DEBERÍA crear Precios en la misma Fecha\nSI ES IMPRESCIBLE hacerlo COORDINE CON SUPERTECH.EC!!");
                    System.out.println("Error al añadir:" + connection.getResponseCode());
                    System.out.println("Error:" + connection.getErrorStream());
                    System.out.println(connection.getResponseMessage());
                    contsultaPrecios = false;
                } else {
                    System.out.println("FT:: NO HAY PRECIOS del mismo día. ");
                    contsultaPrecios = true;
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "SELECCIONE UNA COMERCIALIZADORA");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return contsultaPrecios;
    }

    public void obtenerTerminalesPrecioProd(int i, List<ObjetoPrecio> listPrecio) {
        try {
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecioterminalproducto/paraPrecioUno?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecioterminalproducto/porIdSinterminal?";

            url = new URL(direcc + "codigocomercializadora=" + listPrecio.get(i).getPrecio().getPrecioPK().getCodigocomercializadora()
                    + "&codigoproducto=" + listPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto()
                    + "&codigolistaprecio=" + listPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio()
                    + "&codigomedida=" + listPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            objprecio = new ObjetoPrecio();
            terminalProducto = new Listaprecioterminalproducto();
            terminalProductoPK = new ListaprecioterminalproductoPK();
            listaTerminalProdAux = new ArrayList<>();
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
                if (!retorno.isNull(indice)) {
                    JSONObject objTerminalP = retorno.getJSONObject(indice);
                    JSONObject objTerminalPK = objTerminalP.getJSONObject("listaprecioterminalproductoPK");

                    terminalProductoPK.setCodigocomercializadora(objTerminalPK.getString("codigocomercializadora"));
                    terminalProductoPK.setCodigolistaprecio(objTerminalPK.getLong("codigolistaprecio"));
                    terminalProductoPK.setCodigoterminal(objTerminalPK.getString("codigoterminal"));
                    terminalProductoPK.setCodigoproducto(objTerminalPK.getString("codigoproducto"));
                    terminalProductoPK.setCodigomedida(objTerminalPK.getString("codigomedida"));
                    terminalProducto.setListaprecioterminalproductoPK(terminalProductoPK);

                    listaTerminalProdAux.add(terminalProducto);
                    terminalProducto = new Listaprecioterminalproducto();
                    terminalProductoPK = new ListaprecioterminalproductoPK();

                }
            }
            if (connection.getResponseCode() == 200) {
                //this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA REGISTRADA EXITOSAMENTE");
                //contsultaPrecios = true;
                //consultarPorIdPrecios(comerP);
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println("Error:" + connection.getErrorStream());
                System.out.println(connection.getResponseMessage());
                //contsultaPrecios = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void consultarPorIdPrecios(Comercializadoraproducto comerP) {
        for (int i = 0; i < listaTerminalProd.size(); i++) {

            obtenerPreciosModificados(i, listaTerminalProd, comerP);
        }
    }

    public void obtenerPreciosModificados(int i, List<Listaprecioterminalproducto> listTermP, Comercializadoraproducto comerP) {
        try {
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecioterminalproducto/porId?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecioterminalproducto/porId?";

            url = new URL(direcc + "&codigocomercializadora=" + listTermP.get(i).getListaprecioterminalproductoPK().getCodigocomercializadora()
                    + "&codigolistaprecio=" + listTermP.get(i).getListaprecioterminalproductoPK().getCodigolistaprecio()
                    + "&codigoterminal=" + listTermP.get(i).getListaprecioterminalproductoPK().getCodigoterminal()
                    + "&codigoproducto=" + listTermP.get(i).getListaprecioterminalproductoPK().getCodigoproducto()
                    + "&codigomedida=" + listTermP.get(i).getListaprecioterminalproductoPK().getCodigomedida());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            objprecio = new ObjetoPrecio();
            product = new Producto();
            medida = new Medida();
            comercializadoraProducto = new Comercializadoraproducto();
            comercializadoraProductoPK = new ComercializadoraproductoPK();
            precio = new Precio();
            precioPK = new PrecioPK();
            precioLista = new Listaprecio();
            precioListaPK = new ListaprecioPK();
            terminal = new Terminal();
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
                if (!retorno.isNull(indice)) {
                    JSONObject objprecios = retorno.getJSONObject(indice);
                    JSONObject comerProducto = objprecios.getJSONObject("comercializadoraproducto");
                    JSONObject comerProductoPK = comerProducto.getJSONObject("comercializadoraproductoPK");
                    JSONObject producto = comerProducto.getJSONObject("producto");
                    JSONObject med = comerProducto.getJSONObject("medida");
                    JSONObject listPrecio = objprecios.getJSONObject("listaprecio");
                    JSONObject listPrecioPK = listPrecio.getJSONObject("listaprecioPK");
                    JSONObject termi = objprecios.getJSONObject("terminal");
                    /*------------------Objeto Terminal----------------------------*/
                    terminal.setCodigo(termi.getString("codigo"));
                    /*------------------Objeto Producto---------------------------*/
                    product.setNombre(producto.getString("nombre"));
                    product.setCodigo(producto.getString("codigo"));
                    product.setPorcentajeivapresuntivo(producto.getBigDecimal("porcentajeivapresuntivo"));
                    /*------------------Objeto Medida-----------------------------*/
                    medida.setCodigo(med.getString("codigo"));
                    medida.setAbreviacion(med.getString("abreviacion"));
                    /*------------------Comercilizadora Producto------------------*/
                    comercializadoraProductoPK.setCodigocomercializadora(comerProductoPK.getString("codigocomercializadora"));
                    comercializadoraProductoPK.setCodigomedida(comerProductoPK.getString("codigomedida"));
                    comercializadoraProductoPK.setCodigoproducto(comerProductoPK.getString("codigoproducto"));
                    comercializadoraProducto.setActivo(comerProducto.getBoolean("activo"));
                    comercializadoraProducto.setMargencomercializacion(comerProducto.getBigDecimal("margencomercializacion"));
                    comercializadoraProducto.setPrecioepp(comerProducto.getBigDecimal("precioepp"));
                    comercializadoraProducto.setPvpsugerido(comerProducto.getBigDecimal("pvpsugerido"));
                    comercializadoraProducto.setSoloaplicaiva(comerProducto.getBoolean("soloaplicaiva"));
                    comercializadoraProducto.setUsuarioactual(comerProducto.getString("usuarioactual"));
                    comercializadoraProducto.setProcesar(comerProducto.getBoolean("procesar"));
                    comercializadoraProducto.setProducto(product);
                    comercializadoraProducto.setMedida(medida);

                    comercializadoraProducto.setComercializadoraproductoPK(comercializadoraProductoPK);
                    /*------------------Precio Lista------------------------------*/
                    precioLista.setNombre(listPrecio.getString("nombre"));
                    precioLista.setTipo(listPrecio.getString("tipo"));
                    precioListaPK.setCodigo(listPrecioPK.getLong("codigo"));
                    precioLista.setListaprecioPK(precioListaPK);
                    /*------------------PrecioPK----------------------------------*/
                    precioPK.setCodigoproducto(product.getCodigo());
                    precioPK.setCodigomedida(medida.getCodigo());
                    precioPK.setCodigolistaprecio(precioListaPK.getCodigo());
                    precioPK.setFechainicio(fechaVencimiento);
                    precioPK.setCodigocomercializadora(comercializadoraProductoPK.getCodigocomercializadora());
                    precioPK.setCodigoterminal(terminal.getCodigo());
                    /*------------------Objeto Precio-----------------------------*/
                    precio.setComercializadoraproducto(comercializadoraProducto);
                    precio.setListaprecio(precioLista);
                    precio.setPrecioPK(precioPK);
                    precio.setTerminal(terminal);
                    /*------------------Objeto ObjPrecio--------------------------*/
                    objprecio.setPrecio(precio);
                    objprecio.setPrecioepp(comerP.getPrecioepp());
                    objprecio.setPvpsugerido(comerP.getPvpsugerido());

                    objDetalle.setPrecio(precio);
                    objDetalle.setPrecioepp(comerP.getPrecioepp());
                    objDetalle.setPvpsugerido(comerP.getPvpsugerido());
                    if (!objprecios.isNull("margenporcentaje")) {
                        objprecio.setMargenporcentaje(objprecios.getBigDecimal("margenporcentaje"));
                    }
                    if (!objprecios.isNull("margenvalorcomercializadora")) {
                        objprecio.setMargenvalorcomercializadora(objprecios.getBigDecimal("margenvalorcomercializadora"));
                    }
                    listaPrecio.add(objprecio);
                    //listaObjDetalle.add(objDetalle);
                    listPrice.add(precio);
                    product = new Producto();
                    medida = new Medida();
                    comercializadoraProducto = new Comercializadoraproducto();
                    comercializadoraProductoPK = new ComercializadoraproductoPK();
                    precio = new Precio();
                    objprecio = new ObjetoPrecio();
                    objDetalle = new ObjetoDetallePrecio();
                    terminal = new Terminal();
                    precioLista = new Listaprecio();
                    precioListaPK = new ListaprecioPK();
                    precioPK = new PrecioPK();
                }
            }

            if (connection.getResponseCode() == 200) {
                //this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA REGISTRADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println("Error:" + connection.getErrorStream());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paso2() {
        step1 = false;
        step2 = false;
        step3 = true;
        calcularPreciosProd();
    }

    public void calcularPreciosProd() {
        BigDecimal dpcg1 = new BigDecimal(0);
        BigDecimal dpcg1Iva = new BigDecimal(0);
        BigDecimal dpcg2 = new BigDecimal(0);
        BigDecimal dpcg3 = new BigDecimal(0);
        BigDecimal dpcg4 = new BigDecimal(0);
        BigDecimal dpcg5 = new BigDecimal(0);
        BigDecimal dpcg6 = new BigDecimal(0);
        BigDecimal dpcg9 = new BigDecimal(0);
        BigDecimal dpcg5F = new BigDecimal(0);
        BigDecimal valorIvaDividir = new BigDecimal(1);
        BigDecimal ivaLocalGlobal = new BigDecimal(0);

        for (int i = 0; i < listaGravamen.size(); i++) {
            if (listaGravamen.get(i).getGravamenPK().getCodigo().equals("0002")) {
                valorIvaDividir = valorIvaDividir.add(listaGravamen.get(i).getValordefecto());
            }
        }

        for (int i = 0; i < listaPrecio.size(); i++) {
            obtenerTerminalesPrecioProd(i, listaPrecio);
            for (int k = 0; k < listaTerminalProdAux.size(); k++) {
                objDetalle.setPrecio(listaPrecio.get(i).getPrecio());
                objDetalle.setPrecioepp(listaPrecio.get(i).getPrecioepp());
                objDetalle.setPvpsugerido(listaPrecio.get(i).getPvpsugerido());

                //for (int a = 0; a < listaTerminalProdAux.size(); a++) {
                for (int j = 0; j < listaGravamen.size(); j++) {
                    switch (listaGravamen.get(j).getGravamenPK().getCodigo()) {
                        //Precio Terminal EPP
                        case "0001":
                            dpcg1 = listaPrecio.get(i).getPrecioepp().divide(listaGravamen.get(j).getValordefecto(), 6, RoundingMode.HALF_UP);
                            dpcg1Iva = listaPrecio.get(i).getPrecioepp();
                            detallePrecioPK.setCodigocomercializadora(listaPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                            detallePrecioPK.setCodigoterminal(listaTerminalProdAux.get(k).getListaprecioterminalproductoPK().getCodigoterminal());
                            detallePrecioPK.setCodigoproducto(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                            detallePrecioPK.setCodigomedida(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                            detallePrecioPK.setCodigolistaprecio(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                            detallePrecioPK.setFechainicio(listaPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
                            detallePrecioPK.setCodigo("");
                            detallePrecioPK.setCodigogravamen(listaGravamen.get(j).getGravamenPK().getCodigo());
                            detallePrecio.setDetalleprecioPK(detallePrecioPK);
                            detallePrecio.setValor(dpcg1);
                            detallePrecio.setUsuarioactual(dataUser.getUser().getNombrever());
                            listaDetallePrecio.add(detallePrecio);
                            detallePrecio = new Detalleprecio();
                            detallePrecioPK = new DetalleprecioPK();
                            objDetalle.setPrecioTerminalEpp(dpcg1);
                            break;
                        //Margen X cliente
                        // FT:: 2025-07-31 Cambio de formula de margen tipo MCO para PETROLRIOS 
                        case "0005":
                            if (listaPrecio.get(i).getPrecio().getListaprecio().getTipo().equals("MCO")) {
                                
                                BigDecimal mcsiva = (listaPrecio.get(i).getPrecio().getComercializadoraproducto().getMargencomercializacion().divide(valorIvaDividir, 6, RoundingMode.HALF_UP)).setScale(6, RoundingMode.HALF_UP);
                                dpcg4 = (mcsiva.multiply((listaPrecio.get(i).getMargenvalorcomercializadora().divide(new BigDecimal(100))))).setScale(6, RoundingMode.HALF_UP);
 
                                if (listaPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora().equalsIgnoreCase("0008")) {
                                    System.out.println("FT::. CALCULO DE MARGEN DE CLIENTE PARA PETROLRIOS CAMBIO DE FORMULA PVP - PRETER * % ACORDADO");
                                    BigDecimal pvpSinIva = (listaPrecio.get(i).getPrecio().getComercializadoraproducto().getPvpsugerido().divide(valorIvaDividir, 6, RoundingMode.HALF_UP)).setScale(6, RoundingMode.HALF_UP);
                                    dpcg4 = (pvpSinIva.subtract(dpcg1)).multiply((listaPrecio.get(i).getMargenvalorcomercializadora().divide(new BigDecimal(100)))).setScale(6, RoundingMode.HALF_UP);
                                 
                                }
                                
                                //dpcg4 = (listaPrecio.get(i).getPrecio().getComercializadoraproducto().getMargencomercializacion().multiply((listaPrecio.get(i).getMargenvalorcomercializadora().divide(new BigDecimal(100))))).setScale(6, RoundingMode.HALF_UP);
                                detallePrecioPK.setCodigocomercializadora(listaPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                                detallePrecioPK.setCodigoterminal(listaTerminalProdAux.get(k).getListaprecioterminalproductoPK().getCodigoterminal());
                                detallePrecioPK.setCodigoproducto(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                                detallePrecioPK.setCodigomedida(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                                detallePrecioPK.setCodigolistaprecio(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                                detallePrecioPK.setFechainicio(listaPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
                                detallePrecioPK.setCodigo("");
                                detallePrecioPK.setCodigogravamen(listaGravamen.get(j).getGravamenPK().getCodigo());
                                detallePrecio.setDetalleprecioPK(detallePrecioPK);
                                detallePrecio.setValor(dpcg4);
                                detallePrecio.setUsuarioactual(dataUser.getUser().getNombrever());
                                listaDetallePrecio.add(detallePrecio);
                                detallePrecio = new Detalleprecio();
                                detallePrecioPK = new DetalleprecioPK();
                                objDetalle.setMargenxcliente(dpcg4);
                            } else {
                                if (listaPrecio.get(i).getPrecio().getListaprecio().getTipo().equals("MPO")) {
                                    dpcg4 = (dpcg1.multiply((listaPrecio.get(i).getMargenporcentaje().divide(new BigDecimal(100))))).setScale(6, RoundingMode.HALF_UP);
                                } else {
                                    dpcg4 = (dpcg1Iva.multiply((listaPrecio.get(i).getMargenporcentaje().divide(new BigDecimal(100))))).setScale(6, RoundingMode.HALF_UP);
                                }
                                detallePrecioPK.setCodigocomercializadora(listaPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                                detallePrecioPK.setCodigoterminal(listaTerminalProdAux.get(k).getListaprecioterminalproductoPK().getCodigoterminal());
                                detallePrecioPK.setCodigoproducto(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                                detallePrecioPK.setCodigomedida(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                                detallePrecioPK.setCodigolistaprecio(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                                detallePrecioPK.setFechainicio(listaPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
                                detallePrecioPK.setCodigo("");
                                detallePrecioPK.setCodigogravamen(listaGravamen.get(j).getGravamenPK().getCodigo());
                                detallePrecio.setDetalleprecioPK(detallePrecioPK);
                                detallePrecio.setValor(dpcg4);
                                detallePrecio.setUsuarioactual(dataUser.getUser().getNombrever());
                                listaDetallePrecio.add(detallePrecio);
                                detallePrecio = new Detalleprecio();
                                detallePrecioPK = new DetalleprecioPK();
                                objDetalle.setMargenxcliente(dpcg4);
                            }
                            break;
                        //Precio Producto
                        case "0009":
                            dpcg9 = (dpcg1.add(dpcg4)).setScale(6, RoundingMode.HALF_UP);
                            detallePrecioPK.setCodigocomercializadora(listaPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                            detallePrecioPK.setCodigoterminal(listaTerminalProdAux.get(k).getListaprecioterminalproductoPK().getCodigoterminal());
                            detallePrecioPK.setCodigoproducto(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                            detallePrecioPK.setCodigomedida(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                            detallePrecioPK.setCodigolistaprecio(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                            detallePrecioPK.setFechainicio(listaPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
                            detallePrecioPK.setCodigo("");
                            detallePrecioPK.setCodigogravamen(listaGravamen.get(j).getGravamenPK().getCodigo());
                            detallePrecio.setDetalleprecioPK(detallePrecioPK);
                            detallePrecio.setValor(dpcg9);
                            detallePrecio.setUsuarioactual(dataUser.getUser().getNombrever());
                            listaDetallePrecio.add(detallePrecio);
                            detallePrecio = new Detalleprecio();
                            detallePrecioPK = new DetalleprecioPK();
                            objDetalle.setPrecioProducto(dpcg9);
                            break;
                        //Iva
                        case "0002":
                            ivaLocalGlobal = listaGravamen.get(j).getValordefecto();
                            dpcg2 = (dpcg9.multiply(listaGravamen.get(j).getValordefecto())).setScale(6, RoundingMode.HALF_UP);
                            detallePrecioPK.setCodigocomercializadora(listaPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                            detallePrecioPK.setCodigoterminal(listaTerminalProdAux.get(k).getListaprecioterminalproductoPK().getCodigoterminal());
                            detallePrecioPK.setCodigoproducto(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                            detallePrecioPK.setCodigomedida(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                            detallePrecioPK.setCodigolistaprecio(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                            detallePrecioPK.setFechainicio(listaPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
                            detallePrecioPK.setCodigo("");
                            detallePrecioPK.setCodigogravamen(listaGravamen.get(j).getGravamenPK().getCodigo());
                            detallePrecio.setDetalleprecioPK(detallePrecioPK);
                            detallePrecio.setValor(dpcg2);
                            detallePrecio.setUsuarioactual(dataUser.getUser().getNombrever());
                            listaDetallePrecio.add(detallePrecio);
                            detallePrecio = new Detalleprecio();
                            detallePrecioPK = new DetalleprecioPK();
                            objDetalle.setIva(dpcg2);
                            break;
                        //(componente cambiado 2022-02-01 pedido pys)Retencion Iva Presuntivo
                        case "0004":
                            if (listaPrecio.get(i).getPrecio().getComercializadoraproducto().getSoloaplicaiva()) {
                                dpcg5 = new BigDecimal(0);
                                detallePrecioPK.setCodigocomercializadora(listaPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                                detallePrecioPK.setCodigoterminal(listaTerminalProdAux.get(k).getListaprecioterminalproductoPK().getCodigoterminal());
                                detallePrecioPK.setCodigoproducto(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                                detallePrecioPK.setCodigomedida(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                                detallePrecioPK.setCodigolistaprecio(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                                detallePrecioPK.setFechainicio(listaPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
                                detallePrecioPK.setCodigo("");
                                detallePrecioPK.setCodigogravamen(listaGravamen.get(j).getGravamenPK().getCodigo());
                                detallePrecio.setDetalleprecioPK(detallePrecioPK);
                                detallePrecio.setValor(dpcg5);
                                detallePrecio.setUsuarioactual(dataUser.getUser().getNombrever());
                                listaDetallePrecio.add(detallePrecio);
                                detallePrecio = new Detalleprecio();
                                detallePrecioPK = new DetalleprecioPK();
                                objDetalle.setIvaPresuntivo(dpcg5);
                            } else {

                                //dpcg5 = ((listaPrecio.get(i).getPvpsugerido().divide(new BigDecimal(1.12), 6, RoundingMode.HALF_UP)).subtract(dpcg9)).multiply(listaGravamen.get(j).getValordefecto()).setScale(6, RoundingMode.HALF_UP);
                                //dpcg5F = dpcg5.multiply((listaPrecio.get(i).getPrecio().getComercializadoraproducto().getProducto().getPorcentajeivapresuntivo().divide(new BigDecimal(100)))).setScale(6, RoundingMode.HALF_UP);
                                //iva * listaPrecio.get(i).getPrecio().getComercializadoraproducto().getProducto().getPorcentajeivapresuntivo().divide(new BigDecimal(100)
                                dpcg5F = dpcg2.multiply(listaPrecio.get(i).getPrecio().getComercializadoraproducto().getProducto().getPorcentajeivapresuntivo()).divide(new BigDecimal(100)).setScale(6, RoundingMode.HALF_UP);

                                detallePrecioPK.setCodigocomercializadora(listaPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                                System.out.println("FT::Calculando PROCENTAJE DE IVA PRESUNTIVO- " + detallePrecioPK.getCodigocomercializadora() + " - " + dpcg5F.toString());

                                //(componente PARA FENAPET cambiado 2023-05-31 pedido ing. Cruz, sr. Rambay)Retencion Iva Presuntivo MANTENER EL CALCULO DE IVA PRESUNTIVO
                                if (detallePrecioPK.getCodigocomercializadora().equalsIgnoreCase("0095")) {
                                    System.out.println("FT::CALCULANDO PROCENTAJE DE IVA PRESUNTIVO- DENTRO DE IF " + detallePrecioPK.getCodigocomercializadora() + " - " + dpcg5F.toString());
                                    dpcg5F = ((listaPrecio.get(i).getPvpsugerido().multiply(ivaLocalGlobal)).setScale(6, RoundingMode.HALF_UP)).subtract(dpcg2).setScale(6, RoundingMode.HALF_UP);
                                    System.out.println("FT::SE HA CALCULADO PROCENTAJE DE IVA PRESUNTIVO- DENTRO DE IF - FORMULA FENAPET "+detallePrecioPK.getCodigocomercializadora()+ " - PVP: "+listaPrecio.get(i).getPvpsugerido().toString()+"% IVA:. "+ivaLocalGlobal.toString()+" - IVA DEL PVP:-"+ ((listaPrecio.get(i).getPvpsugerido().multiply(ivaLocalGlobal)).setScale(6, RoundingMode.HALF_UP))+ " - VALOR ORIGINAL IVA:- "+dpcg2.toString() +" - RESULTADO:- "+dpcg5F.toString());
                                }

                                detallePrecioPK.setCodigoterminal(listaTerminalProdAux.get(k).getListaprecioterminalproductoPK().getCodigoterminal());
                                detallePrecioPK.setCodigoproducto(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                                detallePrecioPK.setCodigomedida(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                                detallePrecioPK.setCodigolistaprecio(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                                detallePrecioPK.setFechainicio(listaPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
                                detallePrecioPK.setCodigo("");
                                detallePrecioPK.setCodigogravamen(listaGravamen.get(j).getGravamenPK().getCodigo());
                                detallePrecio.setDetalleprecioPK(detallePrecioPK);
                                detallePrecio.setValor(dpcg5F);
                                detallePrecio.setUsuarioactual(dataUser.getUser().getNombrever());
                                listaDetallePrecio.add(detallePrecio);
                                detallePrecio = new Detalleprecio();
                                detallePrecioPK = new DetalleprecioPK();
                                objDetalle.setIvaPresuntivo(dpcg5F);
                                System.out.println("FT::Calculando PROCENTAJE DE IVA PRESUNTIVO- VALOR DEFINITIVO - " + dpcg5F.toString());

                            }
                            break;
                        //Tres X Mil
                        case "0328":
                            if (listaPrecio.get(i).getPrecio().getComercializadoraproducto().getSoloaplicaiva()) {
                                dpcg6 = new BigDecimal(0);
                                detallePrecioPK.setCodigocomercializadora(listaPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                                detallePrecioPK.setCodigoterminal(listaTerminalProdAux.get(k).getListaprecioterminalproductoPK().getCodigoterminal());
                                detallePrecioPK.setCodigoproducto(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                                detallePrecioPK.setCodigomedida(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                                detallePrecioPK.setCodigolistaprecio(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                                detallePrecioPK.setFechainicio(listaPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
                                detallePrecioPK.setCodigo("");
                                detallePrecioPK.setCodigogravamen(listaGravamen.get(j).getGravamenPK().getCodigo());
                                detallePrecio.setDetalleprecioPK(detallePrecioPK);
                                detallePrecio.setValor(dpcg6);
                                detallePrecio.setUsuarioactual(dataUser.getUser().getNombrever());
                                listaDetallePrecio.add(detallePrecio);
                                detallePrecio = new Detalleprecio();
                                detallePrecioPK = new DetalleprecioPK();
                                objDetalle.setTresPorMil(dpcg6);
                            } else {
                                dpcg6 = (dpcg9.multiply(listaGravamen.get(j).getValordefecto())).setScale(6, RoundingMode.HALF_UP);
                                detallePrecioPK.setCodigocomercializadora(listaPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                                detallePrecioPK.setCodigoterminal(listaTerminalProdAux.get(k).getListaprecioterminalproductoPK().getCodigoterminal());
                                detallePrecioPK.setCodigoproducto(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                                detallePrecioPK.setCodigomedida(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                                detallePrecioPK.setCodigolistaprecio(listaPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                                detallePrecioPK.setFechainicio(listaPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
                                detallePrecioPK.setCodigo("");
                                detallePrecioPK.setCodigogravamen(listaGravamen.get(j).getGravamenPK().getCodigo());
                                detallePrecio.setDetalleprecioPK(detallePrecioPK);
                                detallePrecio.setValor(dpcg6);
                                detallePrecio.setUsuarioactual(dataUser.getUser().getNombrever());
                                listaDetallePrecio.add(detallePrecio);
                                detallePrecio = new Detalleprecio();
                                detallePrecioPK = new DetalleprecioPK();
                                objDetalle.setTresPorMil(dpcg6);
                            }
                            break;
                        default:
                            break;
                    }
                }
                //terminal = new Terminal();
                //}

                //DAVID AYALA verificar que el terminal se cambie en el objetoDetalle
                terminal.setCodigo(listaTerminalProdAux.get(k).getListaprecioterminalproductoPK().getCodigoterminal());
                objDetalle.setCodigoTerminal(terminal.getCodigo());
                listaObjDetalle.add(objDetalle);
                terminal = new Terminal();
                objDetalle = new ObjetoDetallePrecio();
            }

            listPrice.get(i).setDetalleprecioList(listaDetallePrecio);
            listPrice.get(i).setPrecioproducto(dpcg9);
            listaDetallePrecio = new ArrayList<>();
        }
        //savePrice(listPrice, listaTerminales);
    }

    public void obtenerTerminalesProducto(int i, List<Precio> precio) {
        try {
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecioterminalproducto/paraPrecioUno?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecioterminalproducto/porIdSinterminal?";

            url = new URL(direcc + "codigocomercializadora=" + precio.get(i).getPrecioPK().getCodigocomercializadora()
                    + "&codigoproducto=" + precio.get(i).getPrecioPK().getCodigoproducto()
                    + "&codigolistaprecio=" + precio.get(i).getPrecioPK().getCodigolistaprecio()
                    + "&codigomedida=" + precio.get(i).getPrecioPK().getCodigomedida());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            objprecio = new ObjetoPrecio();
            terminalProducto = new Listaprecioterminalproducto();
            terminalProductoPK = new ListaprecioterminalproductoPK();
            listaTerminalProdAux = new ArrayList<>();
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
                if (!retorno.isNull(indice)) {
                    JSONObject objTerminalP = retorno.getJSONObject(indice);
                    JSONObject objTerminalPK = objTerminalP.getJSONObject("listaprecioterminalproductoPK");

                    terminalProductoPK.setCodigocomercializadora(objTerminalPK.getString("codigocomercializadora"));
                    terminalProductoPK.setCodigolistaprecio(objTerminalPK.getLong("codigolistaprecio"));
                    terminalProductoPK.setCodigoterminal(objTerminalPK.getString("codigoterminal"));
                    terminalProductoPK.setCodigoproducto(objTerminalPK.getString("codigoproducto"));
                    terminalProductoPK.setCodigomedida(objTerminalPK.getString("codigomedida"));
                    terminalProducto.setListaprecioterminalproductoPK(terminalProductoPK);

                    listaTerminalProdAux.add(terminalProducto);
                    terminalProducto = new Listaprecioterminalproducto();
                    terminalProductoPK = new ListaprecioterminalproductoPK();

                }
            }
            if (connection.getResponseCode() == 200) {
                //this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA REGISTRADA EXITOSAMENTE");
                //contsultaPrecios = true;
                //consultarPorIdPrecios(comerP);
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR");
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println("Error:" + connection.getErrorStream());
                System.out.println(connection.getResponseMessage());
                //contsultaPrecios = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prueba() {
        this.dialogo(FacesMessage.SEVERITY_INFO, "Se va a iniciar la creacion de precios, esto tomara un momento, por favor espere");
    }

    public void guardarPrice() {
        //this.dialogo(FacesMessage.SEVERITY_INFO, "Se va a iniciar la creacion de precios, esto tomara un momento, por favor espere");
        precioError = new ArrayList<>();
        precioGuardado = new ArrayList<>();
        detPrecioError = new ArrayList<>();
        detPrecioGuardado = new ArrayList<>();
        StringBuilder cadenaErro = new StringBuilder();
        for (int i = 0; i < listPrice.size(); i++) {
            obtenerTerminalesProducto(i, listPrice);
            for (int j = 0; j < listaTerminalProdAux.size(); j++) {
                //arregloJSON.add(addItemsArregloJSON(i, j, listPrice, listaTerminalProdAux));
                addItemsPrice(i, j, listPrice, listaTerminalProdAux);
            }
        }
        //addItemsPriceAux(arregloJSON);
//mandar vector al servicio
        if (!precioGuardado.isEmpty()) {
            this.dialogo(FacesMessage.SEVERITY_INFO, "SE HAN REGISTRADOS " + precioGuardado.size() + " PRECIOS, Y " + detPrecioGuardado.size() + " DETALLES PRECIOS EXITOSAMENTE");
        }
        if (!precioError.isEmpty()) {
            cadenaErro.append("ERROR AL REGISTRAR PRECIOS\n").toString();
            for (int i = 0; i < precioError.size(); i++) {
                cadenaErro.append("Precio N." + precioError.get(i).getPrecioPK().getCodigoPrecio() + "\n").toString();
            }
            for (int i = 0; i < detPrecioError.size(); i++) {
                cadenaErro.append("Detalle Precio N." + detPrecioError.get(i).getDetalleprecioPK().getCodigo() + "\n").toString();
            }
            this.dialogo(FacesMessage.SEVERITY_ERROR, cadenaErro.toString());
        }
        reestablecer();
    }

    public void guardarPriceLote() {
        //this.dialogo(FacesMessage.SEVERITY_INFO, "Se va a iniciar la creacion de precios, esto tomara un momento, por favor espere");
        precioError = new ArrayList<>();
        precioGuardado = new ArrayList<>();
        detPrecioError = new ArrayList<>();
        detPrecioGuardado = new ArrayList<>();
        List<JSONObject> arregloJSON = new ArrayList<>();
        StringBuilder cadenaErro = new StringBuilder();
        for (int i = 0; i < listPrice.size(); i++) {
            obtenerTerminalesProducto(i, listPrice);
            //for (int j = 0; j < listaTerminalProdAux.size(); j++) {
            arregloJSON.addAll(addItemsArregloJSON(i, listPrice, listaTerminalProdAux));
            //addItemsPrice(i, j, listPrice, listaTerminalProdAux);
            //}
        }

        addItemsPriceAux(arregloJSON);
//mandar vector al servicio
        if (!precioGuardado.isEmpty()) {
            this.dialogo(FacesMessage.SEVERITY_INFO, "SE HAN REGISTRADOS " + precioGuardado.size() + " PRECIOS, Y " + detPrecioGuardado.size() + " DETALLES PRECIOS EXITOSAMENTE");
        }
        if (!precioError.isEmpty()) {
            cadenaErro.append("ERROR AL REGISTRAR PRECIOS\n").toString();
            for (int i = 0; i < precioError.size(); i++) {
                cadenaErro.append("Precio N." + precioError.get(i).getPrecioPK().getCodigoPrecio() + "\n").toString();
            }
            for (int i = 0; i < detPrecioError.size(); i++) {
                cadenaErro.append("Detalle Precio N." + detPrecioError.get(i).getDetalleprecioPK().getCodigo() + "\n").toString();
            }
            this.dialogo(FacesMessage.SEVERITY_ERROR, cadenaErro.toString());
        }
        reestablecer();
    }

    public void addItemsPriceAux(List<JSONObject> arregloJSON) {
        try {

            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            String respuesta;
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/agregar";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio/agregarlote";

            url = new URL(direcc);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            //JSONObject arrObj = new JSONObject();               

            //arrObj.put("", arregloJSON);
            respuesta = arregloJSON.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "SE HA REGISTRADO CON EXITO");
                System.out.println("Se ha registrado con exito");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println("Error:" + connection.getErrorStream());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//public void addItemsPriceAux(int i, int j, List<Precio> precio, List<Listaprecioterminalproducto> listaTerminalP) {
//        try {
//
//            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
//            String fechaI = date.format(precio.get(i).getPrecioPK().getFechainicio()) + "T12:00:00";
//            String respuesta;
//            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/agregar";
//            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio/agregarlote";
//
//            url = new URL(direcc);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoOutput(true);
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-type", "application/json");
//
//            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//            JSONObject obj = new JSONObject();
//            JSONObject objPK = new JSONObject();            
//            JSONObject objEnvRest = new JSONObject();
//            List<JSONObject> arrObj = new ArrayList<>();
//            Precio precioAux = new Precio();
//
//            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
//            String codigo = "";
//            BufferedReader br = new BufferedReader(reader);
//            String tmp = null;
//            String resp = "";
//            while ((tmp = br.readLine()) != null) {
//                resp += tmp;
//            }
//            JSONObject objetoJson = new JSONObject(resp);
//            String cod = objetoJson.getString("developerMessage");
//            codigo = cod;
//
//            objPK.put("codigocomercializadora", precio.get(i).getPrecioPK().getCodigocomercializadora());
//            objPK.put("codigoterminal", listaTerminalP.get(j).getListaprecioterminalproductoPK().getCodigoterminal());
//            objPK.put("codigoproducto", precio.get(i).getPrecioPK().getCodigoproducto());
//            objPK.put("codigomedida", precio.get(i).getPrecioPK().getCodigomedida());
//            objPK.put("codigolistaprecio", precio.get(i).getPrecioPK().getCodigolistaprecio());
//            objPK.put("fechainicio", fechaI);
//            objPK.put("secuencial", "1");
//            //objPK.put("codigoPrecio", "0");
//
//            //obj.put("codigoPrecio", observacion);
//            obj.put("precioPK", objPK);
//            obj.put("fechafin", "");
//            obj.put("activo", true);
//            obj.put("observacion", observacion);
//            obj.put("precioproducto", precio.get(i).getPrecioproducto());
//            obj.put("usuarioactual", dataUser.getUser().getNombrever());
//
//            for (int k = 0; k < precio.get(i).getDetalleprecioList().size(); k++) {
//                arrObj.add(addItemsDetailPAux(k, precio.get(i).getDetalleprecioList(), codigo, listaTerminalP.get(j).getListaprecioterminalproductoPK().getCodigoterminal()));
//            }
//            
//            objEnvRest.put("precio", obj);
//            objEnvRest.put("detalle", arrObj);
//
//            //listaobjEnvRest.add(objEnvRest)
//
//            respuesta = objEnvRest.toString();
//            writer.write(respuesta);
//            writer.close();
//
//            if (connection.getResponseCode() == 200) {
//                precioAux = precio.get(i);
//                precioGuardado.add(precioAux);
//            } else {
//                precioAux = precio.get(i);
//                precioError.add(precioAux);
//                System.out.println("Error al añadir:" + connection.getResponseCode());
//                System.out.println("Error:" + connection.getErrorStream());
//                System.out.println(connection.getResponseMessage());
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public List<JSONObject> addItemsArregloJSON(int i, List<Precio> precio, List<Listaprecioterminalproducto> listaTerminalP) {
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String fechaI = date.format(precio.get(i).getPrecioPK().getFechainicio()) + "T12:00:00";

        JSONObject obj = new JSONObject();
        JSONObject objPK = new JSONObject();
        JSONObject objEnvRest = new JSONObject();
        List<JSONObject> arrObj = new ArrayList<>();
        List<JSONObject> listObjEnvRest = new ArrayList<>();

        for (int j = 0; j < listaTerminalP.size(); j++) {
            objPK.put("codigocomercializadora", precio.get(i).getPrecioPK().getCodigocomercializadora());
            objPK.put("codigoterminal", listaTerminalP.get(j).getListaprecioterminalproductoPK().getCodigoterminal());
            objPK.put("codigoproducto", precio.get(i).getPrecioPK().getCodigoproducto());
            objPK.put("codigomedida", precio.get(i).getPrecioPK().getCodigomedida());
            objPK.put("codigolistaprecio", precio.get(i).getPrecioPK().getCodigolistaprecio());
            objPK.put("fechainicio", fechaI);
            objPK.put("secuencial", "1");
            //objPK.put("codigoPrecio", "0");

            //obj.put("codigoPrecio", observacion);
            obj.put("precioPK", objPK);
            obj.put("fechafin", "");
            obj.put("activo", true);
            obj.put("observacion", observacion);
            obj.put("precioproducto", precio.get(i).getPrecioproducto());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());

            for (int k = 0; k < precio.get(i).getDetalleprecioList().size(); k++) {
                if (listaTerminalP.get(j).getListaprecioterminalproductoPK().getCodigoterminal().equals(precio.get(i).getDetalleprecioList().get(k).getDetalleprecioPK().getCodigoterminal())) {
                    arrObj.add(addItemsDetailPAux(k, precio.get(i).getDetalleprecioList(), "0"));
                }
            }

            objEnvRest.put("precio", obj);
            objEnvRest.put("detalle", arrObj);
            listObjEnvRest.add(objEnvRest);
            obj = new JSONObject();
            objPK = new JSONObject();
            arrObj = new ArrayList<>();
            objEnvRest = new JSONObject();
        }

        //listaobjEnvRest.add(objEnvRest)           
        return listObjEnvRest;

    }

    public JSONObject addItemsDetailPAux(int j, List<Detalleprecio> detalleP, String codigo) {

        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String fechaI = date.format(fechaVencimiento) + "T12:00:00";

        JSONObject detalle = new JSONObject();
        JSONObject detallePK = new JSONObject();

        detallePK.put("codigocomercializadora", detalleP.get(j).getDetalleprecioPK().getCodigocomercializadora());
        detallePK.put("codigoterminal", detalleP.get(j).getDetalleprecioPK().getCodigoterminal());
        detallePK.put("codigoproducto", detalleP.get(j).getDetalleprecioPK().getCodigoproducto());
        detallePK.put("codigomedida", detalleP.get(j).getDetalleprecioPK().getCodigomedida());
        detallePK.put("codigolistaprecio", detalleP.get(j).getDetalleprecioPK().getCodigolistaprecio());
        detallePK.put("fechainicio", fechaI);
        detallePK.put("secuencial", "1");
        detallePK.put("codigo", codigo);
        detallePK.put("codigogravamen", detalleP.get(j).getDetalleprecioPK().getCodigogravamen());

        detalle.put("detalleprecioPK", detallePK);
        detalle.put("valor", detalleP.get(j).getValor());
        detalle.put("usuarioactual", dataUser.getUser().getNombrever());

        return detalle;
    }

    public void addItemsPrice(int i, int j, List<Precio> precio, List<Listaprecioterminalproducto> listaTerminalP) {
        try {

            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            String fechaI = date.format(precio.get(i).getPrecioPK().getFechainicio()) + "T12:00:00";
            String respuesta;
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/agregar";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio/agregar";

            url = new URL(direcc);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            Precio precioAux = new Precio();

            objPK.put("codigocomercializadora", precio.get(i).getPrecioPK().getCodigocomercializadora());
            objPK.put("codigoterminal", listaTerminalP.get(j).getListaprecioterminalproductoPK().getCodigoterminal());
            objPK.put("codigoproducto", precio.get(i).getPrecioPK().getCodigoproducto());
            objPK.put("codigomedida", precio.get(i).getPrecioPK().getCodigomedida());
            objPK.put("codigolistaprecio", precio.get(i).getPrecioPK().getCodigolistaprecio());
            objPK.put("fechainicio", fechaI);
            objPK.put("secuencial", "1");
            //objPK.put("codigoPrecio", "0");

            //obj.put("codigoPrecio", observacion);
            obj.put("precioPK", objPK);
            obj.put("fechafin", "");
            obj.put("activo", true);
            obj.put("observacion", observacion);
            obj.put("precioproducto", precio.get(i).getPrecioproducto());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());

            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                precioAux = precio.get(i);
                precioGuardado.add(precioAux);
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                String codigo = "";
                BufferedReader br = new BufferedReader(reader);
                String tmp = null;
                String resp = "";
                while ((tmp = br.readLine()) != null) {
                    resp += tmp;
                }
                JSONObject objetoJson = new JSONObject(resp);
                String cod = objetoJson.getString("developerMessage");
                codigo = cod;
                if (!cod.isEmpty() && !cod.equalsIgnoreCase("-1")) {
                    saveDetailP(precio.get(i).getDetalleprecioList(), codigo, listaTerminalP.get(j).getListaprecioterminalproductoPK().getCodigoterminal());
                }
            } else {
                precioAux = precio.get(i);
                precioError.add(precioAux);
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println("Error:" + connection.getErrorStream());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDetailP(List<Detalleprecio> detalleP, String codigo, String codTerminal) {
        for (int j = 0; j < detalleP.size(); j++) {
            addItemsDetailP(j, detalleP, codigo, codTerminal);
        }
    }

    public void addItemsDetailP(int j, List<Detalleprecio> detalleP, String codigo, String codTerminal) {
        try {
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            String fechaI = date.format(fechaVencimiento) + "T12:00:00";
            String respuesta;
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.detalleprecio";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detalleprecio";

            url = new URL(direcc);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject detalle = new JSONObject();
            JSONObject detallePK = new JSONObject();
            Detalleprecio detPrecioAux = new Detalleprecio();

            detallePK.put("codigocomercializadora", detalleP.get(j).getDetalleprecioPK().getCodigocomercializadora());
            detallePK.put("codigoterminal", codTerminal);
            detallePK.put("codigoproducto", detalleP.get(j).getDetalleprecioPK().getCodigoproducto());
            detallePK.put("codigomedida", detalleP.get(j).getDetalleprecioPK().getCodigomedida());
            detallePK.put("codigolistaprecio", detalleP.get(j).getDetalleprecioPK().getCodigolistaprecio());
            detallePK.put("fechainicio", fechaI);
            detallePK.put("secuencial", "1");
            detallePK.put("codigo", codigo);
            detallePK.put("codigogravamen", detalleP.get(j).getDetalleprecioPK().getCodigogravamen());

            detalle.put("detalleprecioPK", detallePK);
            detalle.put("valor", detalleP.get(j).getValor());
            detalle.put("usuarioactual", dataUser.getUser().getNombrever());

            respuesta = detalle.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                detPrecioAux = detalleP.get(j);
                detPrecioGuardado.add(detPrecioAux);
            } else {
                detPrecioAux = detalleP.get(j);
                detPrecioError.add(detPrecioAux);
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println("Error:" + connection.getErrorStream());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public Comercializadora getComerc() {
        return comerc;
    }

    public void setComerc(Comercializadora comerc) {
        this.comerc = comerc;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getConfTerminal() {
        return confTerminal;
    }

    public void setConfTerminal(String confTerminal) {
        this.confTerminal = confTerminal;
    }

    public List<Comercializadoraproducto> getListaComerProductos() {
        return listaComerProductos;
    }

    public void setListaComerProductos(List<Comercializadoraproducto> listaComerProductos) {
        this.listaComerProductos = listaComerProductos;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public boolean isMostarPaso1() {
        return mostarPaso1;
    }

    public void setMostarPaso1(boolean mostarPaso1) {
        this.mostarPaso1 = mostarPaso1;
    }

    public boolean isMostarPaso2() {
        return mostarPaso2;
    }

    public void setMostarPaso2(boolean mostarPaso2) {
        this.mostarPaso2 = mostarPaso2;
    }

    public String getTerminalValor() {
        return terminalValor;
    }

    public void setTerminalValor(String terminalValor) {
        this.terminalValor = terminalValor;
    }

    public List<ObjetoPrecio> getListaPrecio() {
        return listaPrecio;
    }

    public void setListaPrecio(List<ObjetoPrecio> listaPrecio) {
        this.listaPrecio = listaPrecio;
    }

    public List<Precio> getListPrice() {
        return listPrice;
    }

    public void setListPrice(List<Precio> listPrice) {
        this.listPrice = listPrice;
    }

    public List<Detalleprecio> getListaDetallePrecio() {
        return listaDetallePrecio;
    }

    public void setListaDetallePrecio(List<Detalleprecio> listaDetallePrecio) {
        this.listaDetallePrecio = listaDetallePrecio;
    }

    public List<ObjetoDetallePrecio> getListaObjDetalle() {
        return listaObjDetalle;
    }

    public void setListaObjDetalle(List<ObjetoDetallePrecio> listaObjDetalle) {
        this.listaObjDetalle = listaObjDetalle;
    }

    public boolean isInhabilitar() {
        return inhabilitar;
    }

    public void setInhabilitar(boolean inhabilitar) {
        this.inhabilitar = inhabilitar;
    }

    public boolean isStep1() {
        return step1;
    }

    public void setStep1(boolean step1) {
        this.step1 = step1;
    }

    public boolean isStep2() {
        return step2;
    }

    public void setStep2(boolean step2) {
        this.step2 = step2;
    }

    public boolean isStep3() {
        return step3;
    }

    public void setStep3(boolean step3) {
        this.step3 = step3;
    }

}
