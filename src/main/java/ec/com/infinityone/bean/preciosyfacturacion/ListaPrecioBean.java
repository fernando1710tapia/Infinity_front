/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.preciosyfacturacion;

import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.serivicio.actorcomercial.ClienteServicio;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.bean.TerminalBean;
import ec.com.infinityone.bean.enums.ListaPreciosEnum;
import ec.com.infinityone.servicio.catalogo.MedidaServicio;
import ec.com.infinityone.servicio.catalogo.ProductoServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Comercializadoraproducto;
import ec.com.infinityone.modelo.ComercializadoraproductoPK;
import ec.com.infinityone.modelo.Listaprecio;
import ec.com.infinityone.modelo.ListaprecioPK;
import ec.com.infinityone.modelo.Listaprecioterminalproducto;
import ec.com.infinityone.modelo.ListaprecioterminalproductoPK;
import ec.com.infinityone.modelo.Medida;
import ec.com.infinityone.modelo.ObjetoNivel1;
import ec.com.infinityone.modelo.Precio;
import ec.com.infinityone.modelo.Producto;
import ec.com.infinityone.servicio.preciosyfacturacion.ListaprecioServicio;
import ec.com.infinityone.servicio.preciosyfacturacion.ListaprecioterminalproductoServicio;
import ec.com.infinityone.reusable.ReusableBean;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.primefaces.PrimeFaces;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import java.util.Scanner;
import javax.faces.context.FacesContext;

/**
 *
 * @author David
 */
@Named
@ViewScoped
public class ListaPrecioBean extends ReusableBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(ListaPrecioBean.class.getName());

    @Inject
    private TerminalBean terminalBean;
    @Inject
    private ClienteServicio clienteServicio;
    @Inject
    private ListaprecioterminalproductoServicio listaprecioterminalproductoServicio;
    @Inject
    private ListaprecioServicio listaprecioServicio;
    @Inject
    private ComercializadoraServicio comercializadoraServicio;
    @Inject
    private MedidaServicio medidaServicio;
    @Inject
    private ProductoServicio productoServicio;

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
    private List<ListaPrecioBean> listaListapreciobean;
    /*
    Variable que almacena varios Productos
     */
    private List<ComercializadoraBean> listaComercializadora;
    /*
    Variable que almacena varios Productos
     */
    private List<ObjetoNivel1> listaTerminal;

    private List<Medida> listaMedida;

    private List<Producto> listaProducto;
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

    private ListaPrecioBean listapreciobean;

    private Comercializadoraproducto productosComerAux;

    private ComercializadoraproductoPK productosComerPK;

    private List<Listaprecioterminalproducto> precioTermProd;

    private ListaprecioterminalproductoPK precioTermProdPK;

    private String tipoBusquedaDocumento;

    private String tipo;

    private String codigoComer;

    private String codigoTerm;

    private ComercializadoraBean comercializadora;

    private ObjetoNivel1 terminal;

    private String comercializadoraT;
    private String comercializadoraNombreT;
    private String listaPrecioT;
    private String tipoT;

    private long codigoListaPrecioT;

    private BigDecimal valorMargen;

    private int edit;

    private Producto producto;
    private Medida medida;

    private boolean listaGuardada;
    /*
    Variable para mostrar pantalla incial
     */
    private boolean mostrarPantallaIncial;
    /*
    Variable para configurar terminal
     */
    private boolean configurarTerminal;
    /*
    Variable para mostrar la panntalla de nueva lista precio
     */
    private boolean mostarListaPrecio;

    /*
    Vairbale para almacenar el pdf generado
     */
    private StreamedContent pdfStream;

    private int contOk;
    private int contError;

    private String ubicacion;

    private List<Listaprecioterminalproducto> listaprecioterminalproductoArchivoSubida;

    private List<ComercializadoraBean> listaComercializadoraAux;

    private Listaprecioterminalproducto unaListaPrecioTerminalProducto;
    private ListaprecioterminalproductoPK unaListaPrecioTerminalProductoPk;

    /**
     * Constructor por defecto
     */
    public ListaPrecioBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {

        //x = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");        
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecio";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecio";

        editarPrecio = false;
        agregarTerminal = false;
        mostrarTabla = false;
        mostrarMsj = true;
        guardarTerminal = false;
        listaGuardada = false;
        comercializadora = new ComercializadoraBean();
        listaprecio1 = new Listaprecio();
        listaprecioPK = new ListaprecioPK();
        productosComer = new Comercializadoraproducto();
        productosComerAux = new Comercializadoraproducto();
        productosComerPK = new ComercializadoraproductoPK();
        precioTermProd = new ArrayList<>();
        precioTermProdPK = new ListaprecioterminalproductoPK();
        producto = new Producto();
        medida = new Medida();
        terminal = new ObjetoNivel1();
        listapreciobean = new ListaPrecioBean();
        valorMargen = new BigDecimal(0);
        listaprecios = new ArrayList<>();
        listaProductosComer = new ArrayList<>();
        tipoBusquedaDocumento = "N";
        margenValor = new BigDecimal("0");
        mostrarPantallaIncial = true;
        configurarTerminal = false;
        mostarListaPrecio = false;
        contOk = 0;
        contError = 0;
        unaListaPrecioTerminalProducto = new Listaprecioterminalproducto();
        unaListaPrecioTerminalProductoPk = new ListaprecioterminalproductoPK();

        obtenerProductos();
        obtenerMedida();
        obtenerComercializadora();
        obtenerListaTerminal();
        //habilitarBusqueda();
        //obtenerPrecio(listaComercializadora.get(0).getCodigo());
        //getURL();
    }

    public void cancelar() {
        editarPrecio = false;
        agregarTerminal = false;
        mostrarTabla = false;
        mostrarMsj = true;
        guardarTerminal = false;
        listaGuardada = false;
        //comercializadora = new ComercializadoraBean();
        listaprecio1 = new Listaprecio();
        listaprecioPK = new ListaprecioPK();
        productosComer = new Comercializadoraproducto();
        productosComerAux = new Comercializadoraproducto();
        productosComerPK = new ComercializadoraproductoPK();
        precioTermProd = new ArrayList<>();
        precioTermProdPK = new ListaprecioterminalproductoPK();
        producto = new Producto();
        medida = new Medida();
        listapreciobean = new ListaPrecioBean();
        valorMargen = new BigDecimal(0);
        //listaprecios = new ArrayList<>();
        listaProductosComer = new ArrayList<>();
        tipoBusquedaDocumento = "N";
        margenValor = new BigDecimal("0");
        mostrarPantallaIncial = true;
        configurarTerminal = false;
        mostarListaPrecio = false;
        contOk = 0;
        contError = 0;
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = this.comercializadoraServicio.obtenerComercializadorasActivas();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda();
        }
    }

    public void obtenerProductos() {
        listaProducto = new ArrayList<>();
        listaProducto = this.productoServicio.obtenerProducto();
    }

    public void obtenerMedida() {
        listaMedida = new ArrayList<>();
        listaMedida = this.medidaServicio.obtenerMedida();
    }

    public void regresar() {
        mostrarPantallaIncial = true;
        configurarTerminal = false;
        mostarListaPrecio = false;
    }

    public void actualizarTipoBusqueda() {
        agregarTerminal = true;
        if (editarPrecio) {
            if (tipoBusquedaDocumento.equals("N")) {
                mostrarTabla = false;
                mostrarMsj = true;
            } else {
                mostrarTabla = true;
                mostrarMsj = false;
                if (edit == 1) {
                    obtenerListaPrecioTerminalProducEdit(comercializadora.getCodigo(), listaprecio1.getListaprecioPK().getCodigo());
                    List<Listaprecioterminalproducto> precioTermProdAux;
                    precioTermProdAux = listaprecioterminalproductoServicio.obtenerListaprecioterminalprod(listaprecio1.getListaprecioPK().getCodigo());
                    for (int j = 0; j < listaListapreciobean.size(); j++) {
                        for (int i = 0; i < precioTermProdAux.size(); i++) {
                            if (listaListapreciobean.get(j).getProductosComer().getComercializadoraproductoPK().getCodigoproducto().equals(precioTermProdAux.get(i).getListaprecioterminalproductoPK().getCodigoproducto())) {
                                if (precioTermProdAux.get(i).getMargenporcentaje().floatValue() > 0) {
                                    this.listaListapreciobean.get(j).setMargenValor(precioTermProdAux.get(i).getMargenporcentaje());
                                }
                                if (precioTermProdAux.get(i).getMargenvalorcomercializadora().floatValue() > 0) {
                                    this.listaListapreciobean.get(j).setMargenValor(precioTermProdAux.get(i).getMargenvalorcomercializadora());
                                }
                            }
                        }
                    }
                } else {
                    obtenerListaPrecioTerminalProducNuev(comercializadora.getCodigo(), listaprecio1.getListaprecioPK().getCodigo());
                    for (int j = 0; j < listaListapreciobean.size(); j++) {
                        this.listaListapreciobean.get(j).setMargenValor(null);
                    }

                }
            }
        } else {
            if (tipoBusquedaDocumento.equals("N")) {
                mostrarTabla = false;
                mostrarMsj = true;
            } else {
                mostrarTabla = true;
                mostrarMsj = false;
                obtenerListaPrecioTerminalProduc(comercializadoraT);
                for (int j = 0; j < listaListapreciobean.size(); j++) {
                    this.listaListapreciobean.get(j).setMargenValor(null);
                }
            }
        }

    }

    public Listaprecio lecturaDatosListaPrecio(Listaprecio obj) {
        soloLectura = true;
        estadoPrecio = false;
        listaprecio1 = obj;
        if (listaprecio1 != null) {
            //this.setAbreviacion(obj.getAbreviacion());
            estadoPrecio = listaprecio1.getActivo();
            //this.setTipoPlaCred(obj.tipoPlaCred.trim());
        }
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return listaprecio1;
    }

    public void obtenerListaTerminal() {
        listaTerminal = new ArrayList<>();
        this.terminalBean.obtenerTermnial();
        listaTerminal = this.terminalBean.getListaTerminales();
    }

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            switch (dataUser.getUser().getNiveloperacion()) {
                case "cero":
                    habilitarComer = true;
                    //obtenerPrecio(listaComercializadora.get(0).getCodigo());
                    break;
                case "adco":
                    habilitarComer = false;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                            this.comercializadora = listaComercializadora.get(i);
                        }
                    }
                    //obtenerPrecio(comercializadora.getCodigo());
                    break;
                case "usac":
                    habilitarComer = false;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                            this.comercializadora = listaComercializadora.get(i);
                        }
                    }
                    //obtenerPrecio(comercializadora.getCodigo());
                    break;
                default:
                    break;
            }
        }

    }

    public void obtenerListaPrecioTerminalProduc(String codigoComer) {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.comercializadoraproducto/porComercializadora?codigocomercializadora=" + codigoComer);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadoraproducto/porComercializadora?codigocomercializadora=" + codigoComer);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

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
                listapreciobean = new ListaPrecioBean();
                productosComerPK = new ComercializadoraproductoPK();
                productosComer = new Comercializadoraproducto();
                producto = new Producto();
                medida = new Medida();
            }
            guardarTerminal = true;
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerListaPrecioTerminalProducEdit(String codigoComer, Long codigoLista) {
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));  

            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.comercializadoraproducto/enListaP?codigocomercializadora=" + codigoComer + "&codigolistaprecio=" + codigoLista);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadoraproducto/enListaP?codigocomercializadora=" + codigoComer + "&codigolistaprecio=" + codigoLista);

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
                productosComerPK.setCodigocomercializadora(comProd.getString("codigocomercializadora"));
                productosComerPK.setCodigoproducto(comProd.getString("codigoproducto"));
                productosComerPK.setCodigomedida(comProd.getString("codigomedida"));
                productosComer.setComercializadoraproductoPK(productosComerPK);
                productosComer.setMargencomercializacion(prodC.getBigDecimal("margencomercializacion"));
                productosComer.setPrecioepp(prodC.getBigDecimal("precioepp"));
                productosComer.setPvpsugerido(prodC.getBigDecimal("pvpsugerido"));
                productosComer.setActivo(prodC.getBoolean("activo"));
                productosComer.setSoloaplicaiva(prodC.getBoolean("soloaplicaiva"));
                productosComer.setUsuarioactual(prodC.getString("usuarioactual"));

                for (int i = 0; i < listaProducto.size(); i++) {
                    if (productosComerPK.getCodigoproducto().equals(listaProducto.get(i).getCodigo())) {
                        producto.setCodigo(listaProducto.get(i).getCodigo());
                        producto.setNombre(listaProducto.get(i).getNombre());
                        productosComer.setProducto(producto);
                    }
                }

                for (int i = 0; i < listaMedida.size(); i++) {
                    if (productosComerPK.getCodigomedida().equals(listaMedida.get(i).getCodigo())) {
                        medida.setCodigo(listaMedida.get(i).getCodigo());
                        medida.setNombre(listaMedida.get(i).getNombre());
                        medida.setAbreviacion(listaMedida.get(i).getAbreviacion());
                        productosComer.setMedida(medida);
                    }
                }

                listapreciobean.setProductosComer(productosComer);
                listaProductosComer.add(productosComer);
                listaListapreciobean.add(listapreciobean);
                listapreciobean = new ListaPrecioBean();
                productosComerPK = new ComercializadoraproductoPK();
                productosComer = new Comercializadoraproducto();
                producto = new Producto();
                medida = new Medida();
            }
            guardarTerminal = false;
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerListaPrecioTerminalProducNuev(String codigoComer, Long codigoLista) {
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));  

            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.comercializadoraproducto/sinListaP?codigocomercializadora=" + codigoComer + "&codigolistaprecio=" + codigoLista);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadoraproducto/sinListaP?codigocomercializadora=" + codigoComer + "&codigolistaprecio=" + codigoLista);

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
                productosComerPK.setCodigocomercializadora(comProd.getString("codigocomercializadora"));
                productosComerPK.setCodigoproducto(comProd.getString("codigoproducto"));
                productosComerPK.setCodigomedida(comProd.getString("codigomedida"));
                productosComer.setComercializadoraproductoPK(productosComerPK);
                productosComer.setMargencomercializacion(prodC.getBigDecimal("margencomercializacion"));
                productosComer.setPrecioepp(prodC.getBigDecimal("precioepp"));
                productosComer.setPvpsugerido(prodC.getBigDecimal("pvpsugerido"));
                productosComer.setActivo(prodC.getBoolean("activo"));
                productosComer.setSoloaplicaiva(prodC.getBoolean("soloaplicaiva"));
                productosComer.setUsuarioactual(prodC.getString("usuarioactual"));

                for (int i = 0; i < listaProducto.size(); i++) {
                    if (productosComerPK.getCodigoproducto().equals(listaProducto.get(i).getCodigo())) {
                        producto.setCodigo(listaProducto.get(i).getCodigo());
                        producto.setNombre(listaProducto.get(i).getNombre());
                        productosComer.setProducto(producto);
                    }
                }

                for (int i = 0; i < listaMedida.size(); i++) {
                    if (productosComerPK.getCodigomedida().equals(listaMedida.get(i).getCodigo())) {
                        medida.setCodigo(listaMedida.get(i).getCodigo());
                        medida.setNombre(listaMedida.get(i).getNombre());
                        medida.setAbreviacion(listaMedida.get(i).getAbreviacion());
                        productosComer.setMedida(medida);
                    }
                }

                listapreciobean.setProductosComer(productosComer);
                listaProductosComer.add(productosComer);
                listaListapreciobean.add(listapreciobean);
                listapreciobean = new ListaPrecioBean();
                productosComerPK = new ComercializadoraproductoPK();
                productosComer = new Comercializadoraproducto();
                producto = new Producto();
                medida = new Medida();
            }
            guardarTerminal = true;
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
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
            System.out.println("AS: Error al obtener lista de precio");
        }
    }

    public List<Listaprecio> obtenerListasPrecio() {

        Listaprecio listaprecioAux = new Listaprecio();
        ListaprecioPK listaprecioPKAux = new ListaprecioPK();
        List<Listaprecio> listapreciosAux = new ArrayList<>();
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));  

            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecio");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecio");

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
                listaprecioPKAux.setCodigocomercializadora(codCom.getString("codigocomercializadora"));
                listaprecioPKAux.setCodigo(codCom.getLong("codigo"));
                listaprecioAux.setListaprecioPK(listaprecioPKAux);
                listaprecioAux.setNombre(listaP.getString("nombre"));
                listaprecioAux.setTipo(listaP.getString("tipo"));
                listaprecioAux.setActivo(listaP.getBoolean("activo"));
                listaprecioAux.setUsuarioactual(listaP.getString("usuarioactual"));
                listapreciosAux.add(listaprecioAux);
                listaprecioAux = new Listaprecio();
                listaprecioPKAux = new ListaprecioPK();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return listapreciosAux;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listapreciosAux;
    }

    public void seleccionarComer() {
        if (comercializadora != null) {
            codigoComer = comercializadora.getCodigo();
        }
    }

    public void seleccionarTerm() {
        if (terminal.getCodigo() != null) {
            codigoTerm = terminal.getCodigo();
        }
    }

    public void actualizarLista() {
        if (listapreciobean != null) {
            listaprecios = new ArrayList<>();
            obtenerPrecio(comercializadora.getCodigo());
        }
    }

    public void save() {
        listaGuardada = false;
        if (editarPrecio) {
            agregarTerminal = false;
            editItems();
            listaprecios = new ArrayList<>();
            terminalProducto();
        } else {
            listaprecioPK.setCodigocomercializadora(comercializadora.getCodigo());
            listaprecio1.setListaprecioPK(listaprecioPK);
            agregarTerminal = true;
            configTerminal(1);
            //PrimeFaces.current().executeScript("PF('configTermDialog').show()");
        }
        contOk = 0;
        contError = 0;
    }

    public void saveLista() {
        if (guardarTerminal) {
            if (terminal.getCodigo() != null) {
                if (edit == 0) {
                    addItemsTerminalProdPlus();
                } else {
                    addItemsTerminalProd();
                }

            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "Seleccione un terminal");
                editarPrecio = false;
            }
        } else {
            editItemsTerminalProd();
        }
        if (mostrarPantallaIncial) {
            listaprecios = new ArrayList<>();
            obtenerPrecio(comercializadora.getCodigo());
            contOk = 0;
            contError = 0;
        }

    }

    public void guardarMargen() {
        if (listapreciobean.getMargenValor() != null) {
            //obtenerListaPrecioTerminalProduc(comercializadoraT);
//            for (int i = 0; i < this.listaProductosComer.size(); i++) {
//                if (listapreciobean.getProductosComer().getComercializadoraproductoPK() != null) {
//                    if (listapreciobean.getProductosComer().getComercializadoraproductoPK().getCodigoproducto().equals(listaProductosComer.get(i).getComercializadoraproductoPK().getCodigoproducto())) {
//                        valorMargen = listapreciobean.getMargenValor();
//                        if (valorMargen != null) {
//                            this.listaListapreciobean.get(i).setMargenValor(valorMargen);
//                        }
//                    }
//                }
//            }
            PrimeFaces.current().executeScript("PF('margen').hide()");
        }
    }

    public void configTerminal(int tipo) {
        if (tipo == 1) {
            mostrarPantallaIncial = false;
            mostarListaPrecio = false;
            configurarTerminal = true;
            agregarTerminal = true;
            guardarTerminal = true;
            mostrarTabla = false;
            mostrarMsj = true;
            tipoBusquedaDocumento = "N";
            terminalProducto();
        } else {
            mostrarPantallaIncial = true;
            mostarListaPrecio = false;
            configurarTerminal = false;
            agregarTerminal = false;
            listaprecios = new ArrayList<>();
            if (addItems()) {
                listaGuardada = true;
                obtenerPrecio(comercializadora.getCodigo());

            }

            //PrimeFaces.current().executeScript("PF('nuevo').hide()");
        }
    }

    public boolean addItems() {
        try {
            String respuesta;
            listaprecioPK.setCodigocomercializadora(comercializadora.getCodigo());
            listaprecio1.setListaprecioPK(listaprecioPK);

            url = new URL(direccion + "/agregar");
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
            obj.put("activo", estadoPrecio);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nombre').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "LISTA PRECIO ACUTALIZADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
            }

            if (connection.getResponseCode() != 200) {
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
            url = new URL(direccion + "/porId?codigocomercializadora=" + listaprecio1.getListaprecioPK().getCodigocomercializadora()
                    + "&codigo=" + listaprecio1.getListaprecioPK().getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "LISTA PRECIO ELIMINADA EXITOSAMENTE");
                listaprecios = new ArrayList<>();
                obtenerPrecio(comercializadora.getCodigo());
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<JSONObject> addItemsArregloJSON(List<Comercializadoraproducto> listaProductosComer, ObjetoNivel1 terminal) {
        JSONObject obj = new JSONObject();
        JSONObject objPK = new JSONObject();
        JSONObject objEnvRest = new JSONObject();
        List<JSONObject> arrObj = new ArrayList<>();
        List<JSONObject> listObjEnvRest = new ArrayList<>();

        objPK.put("codigocomercializadora", listaprecio1.getListaprecioPK().getCodigocomercializadora());
        objPK.put("codigo", listaprecio1.getListaprecioPK().getCodigo());
        obj.put("listaprecioPK", objPK);
        obj.put("nombre", listaprecio1.getNombre());
        obj.put("tipo", listaprecio1.getTipo());
        obj.put("activo", estadoPrecio);
        obj.put("usuarioactual", dataUser.getUser().getNombrever());

        for (int indice = 0; indice < listaProductosComer.size(); indice++) {
            if (listaListapreciobean.get(indice).getMargenValor() != null) {
                arrObj.add(addItemsDetailPAux(indice, terminal));
            }
        }

        objEnvRest.put("listaPrecio", obj);
        objEnvRest.put("detalleListaPrecio", arrObj);
        listObjEnvRest.add(objEnvRest);
        obj = new JSONObject();
        objPK = new JSONObject();
        arrObj = new ArrayList<>();
        objEnvRest = new JSONObject();

        return listObjEnvRest;

    }

    public JSONObject addItemsDetailPAux(int indice, ObjetoNivel1 terminal) {

        JSONObject detalle = new JSONObject();
        JSONObject detallePK = new JSONObject();
        detallePK.put("codigocomercializadora", listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigocomercializadora());
        detallePK.put("codigolistaprecio", codigoListaPrecioT);
        detallePK.put("codigoterminal", terminal.getCodigo());
        detallePK.put("codigoproducto", listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigoproducto());
        detallePK.put("codigomedida", listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigomedida());
        detalle.put("listaprecioterminalproductoPK", detallePK);
        if (tipoT.equals(ListaPreciosEnum.MPO.getCodigo()) || tipoT.equals((ListaPreciosEnum.MTI.getCodigo()))) {        
            if (listaListapreciobean.get(indice).getMargenValor() != null) {
                detalle.put("margenporcentaje", listaListapreciobean.get(indice).getMargenValor());
                detalle.put("margenvalorcomercializadora", -99);
            }
        } else {
            if (listaListapreciobean.get(indice).getMargenValor() != null) {
                detalle.put("margenvalorcomercializadora", listaListapreciobean.get(indice).getMargenValor());
                detalle.put("margenporcentaje", -99);
            }
        }
        detalle.put("usuarioactual", dataUser.getUser().getNombrever());

        return detalle;
    }

    public boolean addItemsPriceAux(List<JSONObject> arregloJSON) {
        try {

            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            String respuesta;
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/agregar";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecio/agregarlote";

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
                return true;
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println("Error:" + connection.getErrorStream());
                System.out.println(connection.getResponseMessage());
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addItemsTerminalProd() {
        listaprecios = new ArrayList<>();
        List<JSONObject> arregloJSON = new ArrayList<>();
        List<ObjetoNivel1> listaTerminalAux = new ArrayList<>();
        if (listaGuardada) {
            obtenerPrecio(comercializadoraT);
            Collections.sort(listaprecios, new Comparator<Listaprecio>() {
                @Override
                public int compare(Listaprecio lp1, Listaprecio lp2) {
                    return new Long(lp1.getListaprecioPK().getCodigo()).compareTo(lp2.getListaprecioPK().getCodigo());
                }
            });
            codigoListaPrecioT = listaprecios.get(listaprecios.size() - 1).getListaprecioPK().getCodigo();
        }
        listaTerminalAux.add(terminal);
        for (int i = 0; i < listaTerminalAux.size(); i++) {
            arregloJSON.addAll(addItemsArregloJSON(listaProductosComer, terminal));
        }
        if (addItemsPriceAux(arregloJSON)) {
            mostrarPantallaIncial = true;
            configurarTerminal = false;
            mostarListaPrecio = false;
        }

    }

    public void addItemsTerminalProdPlus() {
        listaprecios = new ArrayList<>();
        List<ObjetoNivel1> listaTerminalAux = new ArrayList<>();
        obtenerPrecio(comercializadoraT);
        contOk = 0;
        contError = 0;
        StringBuilder cadenaInfo = new StringBuilder();
        StringBuilder cadenaErro = new StringBuilder();
        if (listaGuardada) {
            Collections.sort(listaprecios, new Comparator<Listaprecio>() {
                @Override
                public int compare(Listaprecio lp1, Listaprecio lp2) {
                    return new Long(lp1.getListaprecioPK().getCodigo()).compareTo(lp2.getListaprecioPK().getCodigo());
                }
            });
//            int tam = obtenerListasPrecio().size();
//            List<Listaprecio> lpAux = obtenerListasPrecio();
//            codigoListaPrecioT = lpAux.get(tam).getListaprecioPK().getCodigo();

            codigoListaPrecioT = listaprecios.get(listaprecios.size() - 1).getListaprecioPK().getCodigo();
        }
        listaTerminalAux.add(terminal);
        for (int i = 0; i < listaTerminalAux.size(); i++) {
            for (int indice = 0; indice < listaProductosComer.size(); indice++) {                
                if (listaListapreciobean.get(indice).getMargenValor() != null) {
                    if (addItemsTerminal(indice, terminal)) {
                        contOk++;
                    } else {
                        contError++;
                    }
                }
            }
        }
        cadenaInfo.append("\nListas de precios insertadas correctamente: ").append(contOk).toString();
        cadenaErro.append("\nListas de precios no insertadas: ").append(contError).append(". Contacte con el Administrador del sistema").toString();
        this.dialogo(FacesMessage.SEVERITY_INFO, cadenaInfo.toString());
        if (contError != 0) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, cadenaErro.toString());
        }
        mostrarPantallaIncial = true;
        configurarTerminal = false;
        mostarListaPrecio = false;
    }

    public Boolean addItemsTerminal(int indice, ObjetoNivel1 terminal) {
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
            objPk.put("codigoterminal", terminal.getCodigo());
            objPk.put("codigoproducto", listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigoproducto());
            objPk.put("codigomedida", listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigomedida());
            obj.put("listaprecioterminalproductoPK", objPk);            
            if (tipoT.equals(ListaPreciosEnum.MPO.getCodigo()) || tipoT.equals((ListaPreciosEnum.MTI.getCodigo()))) {
                if (listaListapreciobean.get(indice).getMargenValor() != null) {
                    obj.put("margenporcentaje", listaListapreciobean.get(indice).getMargenValor());
                    obj.put("margenvalorcomercializadora", -99);
                }
            } else {
                if (listaListapreciobean.get(indice).getMargenValor() != null) {
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
                //this.dialogo(FacesMessage.SEVERITY_INFO, "LISTA PRECIO TERMINAL PRODUCTO REGISTRADA EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                guardarTerminal = false;
                return true;
            } else {
                if (connection.getResponseCode() != 200) {
                    System.out.println(connection.getResponseCode());
                    System.out.println(connection.getResponseMessage());
                }
                //this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");    
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void editItemsTerminalProd() {
        System.out.println("AS: 1 Ingresando a edicion");
        obtenerPrecio(comercializadoraT);
        System.out.println("AS: 2 Ingresando a edicion");
        List<ObjetoNivel1> listaTerminalAux = new ArrayList<>();
        listaTerminalAux.add(terminal);
        System.out.println("AS: 3 Ingresando a edicion");
        for (int i = 0; i < listaTerminalAux.size(); i++) {
        System.out.println("AS: 4 Ingresando a edicion");
            for (int indice = 0; indice < listaProductosComer.size(); indice++) {
                System.out.println("AS: 5 Ingresando a edicion");
                if (listaListapreciobean.get(indice).getMargenValor() != null) {
                    System.out.println("AS: 6 Ingresando a edicion");
                    editItemsTerminal(i, indice, terminal);
                }
            }
        }
        mostrarPantallaIncial = true;
        configurarTerminal = false;
        mostarListaPrecio = false;
    }

    public void editItemsTerminal(int i, int indice, ObjetoNivel1 terminal) {
        try {

            String respuesta = "";
            int value = 0;
            String margenporcentaje = "";
            String margenvalorcomercializadora = "";
            if (tipoT.equals(ListaPreciosEnum.MPO.getCodigo()) || tipoT.equals((ListaPreciosEnum.MTI.getCodigo()))) {            
                System.out.println("AS: 7 Ingresando a edicion");
                if (listaListapreciobean.get(indice).getMargenValor() != null) {
                    margenporcentaje = listaListapreciobean.get(indice).getMargenValor().toString();
                    margenvalorcomercializadora = "-99";
                }
            } else {
                if (listaListapreciobean.get(indice).getMargenValor() != null) {
                    margenvalorcomercializadora = listaListapreciobean.get(indice).getMargenValor().toString();
                    margenporcentaje = "-99";
                }
            }

            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecioterminalproducto/porId");
            //url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecioterminalproducto/porId");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecioterminalproducto/actualizar?codigocomercializadora=" + listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigocomercializadora()
                    + "&codigoproducto=" + listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigoproducto()
                    + "&codigomedida=" + listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigomedida()
                    + "&codigoterminal=" + terminal.getCodigo()
                    + "&codigolistaprecio=" + codigoListaPrecioT
                    + "&margenporcentaje=" + margenporcentaje
                    + "&margenvalorcomercializadora=" + margenvalorcomercializadora
                    + "&usuario=" + (dataUser.getUser().getNombrever()).trim());

            System.out.println("AS: Url: " + url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();

//            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
//
//            BufferedReader br = new BufferedReader(reader);
//            String tmp = null;
//            String resp = "";
//            while ((tmp = br.readLine()) != null) {
//                resp += tmp;
//            }
//            JSONObject objetoJson = new JSONObject(resp);
//            JSONArray retorno = objetoJson.getJSONArray("retorno");
//            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//
//            JSONObject obj = new JSONObject();
//            JSONObject objPk = new JSONObject();
//            objPk.put("codigocomercializadora", listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigocomercializadora());
//            objPk.put("codigolistaprecio", codigoListaPrecioT);
//            objPk.put("codigoterminal", terminal.getCodigo());
//            objPk.put("codigoproducto", listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigoproducto());
//            objPk.put("codigomedida", listaProductosComer.get(indice).getComercializadoraproductoPK().getCodigomedida());
//            obj.put("listaprecioterminalproductoPK", objPk);
//            if (tipoT.equals("MPO - Margen sobre el precio en terminal")) {
//                if (listaListapreciobean.get(indice).getMargenValor() != null) {
//                    obj.put("margenporcentaje", listaListapreciobean.get(indice).getMargenValor());
//                    obj.put("margenvalorcomercializadora", -99);
//                }
//            } else {
//                if (listaListapreciobean.get(indice).getMargenValor() != null) {
//                    obj.put("margenvalorcomercializadora", listaListapreciobean.get(indice).getMargenValor());
//                    obj.put("margenporcentaje", -99);
//                }
//            }
//            obj.put("usuarioactual", dataUser.getUser().getNombrever());
//            respuesta = obj.toString();
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
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevaListaPrecio() {
        mostrarPantallaIncial = false;
        mostarListaPrecio = true;
        configurarTerminal = false;
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        }
        estadoPrecio = true;
        editarPrecio = false;
        this.setAbreviacion("");
        listaprecio1 = new Listaprecio();
        listaprecioPK = new ListaprecioPK();
        terminal = new ObjetoNivel1();
        edit = 1;
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Listaprecio editarPrecio(Listaprecio obj, int edit) {
        editarPrecio = true;
        soloLectura = false;
        this.edit = edit;
        mostrarTabla = false;
        mostrarMsj = true;
        agregarTerminal = true;
        tipoBusquedaDocumento = "N";
        listaprecio1 = obj;
        configurarTerminal = true;
        mostrarPantallaIncial = false;
        listaGuardada = false;
        List<Listaprecioterminalproducto> precioTermProdAux;
        precioTermProdAux = listaprecioterminalproductoServicio.obtenerListaprecioterminalprod(listaprecio1.getListaprecioPK().getCodigo());
        if (!precioTermProdAux.isEmpty()) {
            for (int i = 0; i < listaTerminal.size(); i++) {
                if (precioTermProdAux.get(0).getListaprecioterminalproductoPK().getCodigoterminal().equals(listaTerminal.get(i).getCodigo())) {
                    terminal = listaTerminal.get(i);
                    break;
                }
            }
        } else {
            if (edit == 0) {
                editarPrecio = false;
            }
        }
        if (listaprecio1 != null) {
            estadoPrecio = listaprecio1.getActivo();
        }
        terminalProducto();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return listaprecio1;
    }

    public void eliminarPrecio(Listaprecio obj) {
        listaprecio1 = obj;
        List<Precio> listaPrecioAux;
        listaPrecioAux = listaprecioServicio.obtenerListaprecioPorComer(codigoComer, listaprecio1.getListaprecioPK().getCodigo());
        int clienteListPrecio = clienteServicio.obtenerClientePorListaPrecioNoMsg(listaprecio1.getListaprecioPK().getCodigo());
        if (listaPrecioAux.isEmpty()) {
            if (clienteListPrecio == 0) {
                PrimeFaces.current().executeScript("PF('deleteProductDialog').show()");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "La lista de precios tiene clientes registrados");
            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "La lista de precios tiene productos registrados");
        }
    }

    public ListaPrecioBean editarMargen(ListaPrecioBean obj) {
        editarPrecio = true;
        agregarTerminal = false;
        listapreciobean = obj;
        PrimeFaces.current().executeScript("PF('margen').show()");
        return listapreciobean;
    }

    public void editarNombre() {
        editarPrecio = true;
        agregarTerminal = false;
        PrimeFaces.current().executeScript("PF('nombre').show()");
    }

    public Listaprecio terminalProducto() {
        //listaPrecio = this.getListaPrecio();
        if (listaprecio1 != null) {
            comercializadoraT = listaprecio1.getListaprecioPK().getCodigocomercializadora();
            for (int i = 0; i < listaComercializadora.size(); i++) {
                if (listaComercializadora.get(i).getCodigo().equals(listaprecio1.getListaprecioPK().getCodigocomercializadora())) {
                    comercializadoraNombreT = listaComercializadora.get(i).getObjRelacionado();
                    break;
                }
            }
            codigoListaPrecioT = listaprecio1.getListaprecioPK().getCodigo();
            listaPrecioT = listaprecio1.getNombre();
            if (listaprecio1.getTipo().equals("MPO")) {
                tipoT = ListaPreciosEnum.MPO.getCodigo();
            } 
            if (listaprecio1.getTipo().equals("MTI")) {
                tipoT = ListaPreciosEnum.MTI.getCodigo();
            }
            if (listaprecio1.getTipo().equals("MCO")) {
                tipoT = ListaPreciosEnum.MCO.getCodigo();
            }
        }
        return listaprecio1;
    }

    public void onRowToggle(ToggleEvent event) {
        if (event.getVisibility() == Visibility.VISIBLE) {
            Listaprecio listaD = (Listaprecio) event.getData();
            if (listaD.getListaprecioPK().getCodigocomercializadora() != null) {
                precioTermProd = new ArrayList<>();
                List<Listaprecioterminalproducto> precioTermProdAux;
                precioTermProdAux = listaprecioterminalproductoServicio.obtenerListaprecioterminalprod(listaD.getListaprecioPK().getCodigo());
                for (int i = 0; i < precioTermProdAux.size(); i++) {
                    if (precioTermProdAux.get(i).getMargenporcentaje().floatValue() < 0) {
                        precioTermProdAux.get(i).setMargenporcentaje(new BigDecimal(0));
                    }
                    if (precioTermProdAux.get(i).getMargenvalorcomercializadora().floatValue() < 0) {
                        precioTermProdAux.get(i).setMargenvalorcomercializadora(new BigDecimal(0));
                    }
                }
                precioTermProd = precioTermProdAux;
            }
        }
    }

    public void generarReporte(Listaprecio envLP) {
        //String path = "C:\\archivos\\Template\\listaprecio.jrxml";
        String path = Fichero.getCARPETAREPORTES() + "/listaprecio.jrxml";
        String rutaGuardar = Fichero.getCARPETAREPORTES();
        System.out.println("PATH:" + path);
        InputStream file = null;
        try {
            file = new FileInputStream(new File(path));

            JasperReport reporte = JasperCompileManager.compileReport(file);

            Map parametro = new HashMap();
            BufferedImage image = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/logo"+envLP.getListaprecioPK().getCodigocomercializadora()+".jpeg"));
            //BufferedImage image = ImageIO.read(new File("C:\\archivos\\Template\\logo.jpg"));

            parametro.put("codComer", envLP.getListaprecioPK().getCodigocomercializadora());
            parametro.put("codigoListaPrecio", envLP.getListaprecioPK().getCodigo());
            parametro.put("Titulo", "Lista de Precios");
            parametro.put("Subtitulo", "Lista Individual de Precios");
            parametro.put("usuario", dataUser.getUser().getNombrever());
            parametro.put("logo", image);

            Connection conexion = conexionJasperBD();

            JasperPrint print = JasperFillManager.fillReport(reporte, parametro, conexion);

            //File directory = new File("C:\\archivos");
            File directory = new File(rutaGuardar);
            String nombreDocumento = "reporteListaPrecio";

            File pdf = File.createTempFile(nombreDocumento + "_", ".pdf", directory);
            JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
            File initialFile = new File(pdf.getAbsolutePath());
            InputStream targetStream = new FileInputStream(initialFile);
            //pdfStream = new DefaultStreamedContent();
            pdfStream = new DefaultStreamedContent(targetStream, "application/pdf", nombreDocumento + ".pdf");
            //DefaultStreamedContent.builder().contentType("application/pdf").name(nombreDocumento + ".pdf").stream(() -> new FileInputStream(targetStream)).build();
            System.err.print(pdf.getAbsolutePath());
            System.out.println(pdf.getAbsolutePath());
        } catch (Exception ex) {
            //ex.printStackTrace();
            System.out.println("Excepcion: " + ex);
        }
    }

    public ListaPrecioBean getListapreciobean() {
        return listapreciobean;
    }

    public void setListapreciobean(ListaPrecioBean listapreciobean) {
        this.listapreciobean = listapreciobean;
    }

    public BigDecimal getMargenValor() {
        return margenValor;
    }

    public void setMargenValor(BigDecimal margenValor) {
        this.margenValor = margenValor;
    }

    public List<ListaPrecioBean> getListaListapreciobean() {
        return listaListapreciobean;
    }

    public void setListaListapreciobean(List<ListaPrecioBean> listaListapreciobean) {
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

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
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

    public boolean isMostrarPantallaIncial() {
        return mostrarPantallaIncial;
    }

    public void setMostrarPantallaIncial(boolean mostrarPantallaIncial) {
        this.mostrarPantallaIncial = mostrarPantallaIncial;
    }

    public boolean isConfigurarTerminal() {
        return configurarTerminal;
    }

    public void setConfigurarTerminal(boolean configurarTerminal) {
        this.configurarTerminal = configurarTerminal;
    }

    public boolean isMostarListaPrecio() {
        return mostarListaPrecio;
    }

    public void setMostarListaPrecio(boolean mostarListaPrecio) {
        this.mostarListaPrecio = mostarListaPrecio;
    }

    public StreamedContent getPdfStream() {
        return pdfStream;
    }

    public void setPdfStream(StreamedContent pdfStream) {
        this.pdfStream = pdfStream;
    }

    public ObjetoNivel1 getTerminal() {
        return terminal;
    }

    public void setTerminal(ObjetoNivel1 terminal) {
        this.terminal = terminal;
    }

    public void actualizarLoteLista() {

        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
            ubicacion = "";
            listaprecioterminalproductoArchivoSubida = new ArrayList<>();
        }
        PrimeFaces.current().executeScript("PF('zip').show()");
    }

    public List<Listaprecioterminalproducto> getListaprecioterminalproductoArchivoSubida() {
        return listaprecioterminalproductoArchivoSubida;
    }

    public void setListaprecioterminalproductoArchivoSubida(List<Listaprecioterminalproducto> listaprecioterminalproductoArchivoSubida) {
        this.listaprecioterminalproductoArchivoSubida = listaprecioterminalproductoArchivoSubida;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String handleFileUpload(FileUploadEvent event) {
        DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat date1 = new SimpleDateFormat("ddMMyyyy");
        DateFormat date2 = new SimpleDateFormat("yyyyMMdd");
        DateFormat horaFormat = new SimpleDateFormat("hhmmss");
        String ruta_temporal = Fichero.getCARPETAREPORTES();
        StringBuilder cadenaInfo = new StringBuilder();
        StringBuilder cadenaErro = new StringBuilder();

        //String ruta_temporal = "C:\\archivos\\";
        UploadedFile uploadedFile = event.getFile();
        ubicacion = "";
        Scanner scanner;
        String fileName = uploadedFile.getFileName();
        byte[] contents = uploadedFile.getContent();
        try {
            FileOutputStream fos = new FileOutputStream(ruta_temporal + fileName.replace(" ", ""));
            fos.write(contents);
            ubicacion = ruta_temporal + fileName.replace(" ", "");
            fos.close();
            File file = new File(ubicacion);
            //se pasa el flujo al objeto scanner
            scanner = new Scanner(file);

            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            codigoComer = comercializadora.getCodigo();
            if (codigoComer != null) {

                listaComercializadoraAux = new ArrayList<>();
                listaComercializadoraAux = comercializadoraServicio.obtenerComercializadoraId(codigoComer);
                comercializadora = listaComercializadoraAux.get(0);

                while (scanner.hasNextLine()) {
                    // el objeto scanner lee linea a linea desde el archivo
                    String linea = scanner.nextLine();
                    System.out.println("FT:: linea a actualizar: " + linea);
                    //Scanner delimitar = new Scanner(linea);
                    //se usa una expresión regular
                    //que valida que antes o despues de una coma (,) exista cualquier cosa
                    //parte la cadena recibida cada vez que encuentre una coma				
                    //delimitar.useDelimiter("\\s*,\\s*");                  
                    //pagofacturaPK.setNumero(delimitar.next());                                      

                    //Comercializadora
                    unaListaPrecioTerminalProductoPk.setCodigocomercializadora(comercializadora.getCodigo());

                    //codigo lista de precio
                    unaListaPrecioTerminalProductoPk.setCodigolistaprecio(new Long(linea.substring(0, 2)));
                    //codigo terminal
                    unaListaPrecioTerminalProductoPk.setCodigoterminal(linea.substring(2, 4));
                    //codigo producto
                    unaListaPrecioTerminalProductoPk.setCodigoproducto(linea.substring(4, 8));
                    //codigo medida
                    unaListaPrecioTerminalProductoPk.setCodigomedida(linea.substring(8, 10));

                    //% MPO
                    BigDecimal valmpo = new BigDecimal(linea.substring(10, 19));
                    valmpo = valmpo.movePointLeft(6);

                    if (valmpo.compareTo(new BigDecimal("999")) == 0) {
                        valmpo = new BigDecimal("-99");
                    }
                    unaListaPrecioTerminalProducto.setMargenporcentaje(valmpo);

                    //% MCO
                    BigDecimal valmco = new BigDecimal(linea.substring(19, 28));
                    valmco = valmco.movePointLeft(6);

                    if (valmco.compareTo(new BigDecimal("999")) == 0) {
                        valmco = new BigDecimal("-99");
                    }
                    unaListaPrecioTerminalProducto.setMargenvalorcomercializadora(valmco);

                    unaListaPrecioTerminalProducto.setListaprecioterminalproductoPK(unaListaPrecioTerminalProductoPk);

                    listaprecioterminalproductoArchivoSubida.add(unaListaPrecioTerminalProducto);

                    unaListaPrecioTerminalProducto = new Listaprecioterminalproducto();
                    unaListaPrecioTerminalProductoPk = new ListaprecioterminalproductoPK();

                }

                //se cierra el ojeto scanner
                scanner.close();
                FacesContext context = FacesContext.getCurrentInstance();

                this.dialogo(FacesMessage.SEVERITY_INFO, "Se va a actualizar los Margenes de : " + String.valueOf(listaprecioterminalproductoArchivoSubida.size()) + " Listas de Precios ");

                return ubicacion;
            } else {
                this.dialogo(FacesMessage.SEVERITY_WARN, "Seleccione una comercializadora para poder subir el archivo");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
