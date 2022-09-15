/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.preciosyfacturacion.bean;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import ec.com.infinityone.actorcomercial.bean.ComercializadoraBean;
import ec.com.infinityone.actorcomercial.serivicios.ClienteServicio;
import ec.com.infinityone.actorcomercial.serivicios.ComercializadoraServicio;
import ec.com.infinityone.catalogo.servicios.TerminalService;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Cliente;
import ec.com.infinityone.modeloWeb.Comercializadoraproducto;
import ec.com.infinityone.modeloWeb.ComercializadoraproductoPK;
import ec.com.infinityone.modeloWeb.Detalleprecio;
import ec.com.infinityone.modeloWeb.DetalleprecioPK;
import ec.com.infinityone.modeloWeb.Gravamen;
import ec.com.infinityone.modeloWeb.GravamenPK;
import ec.com.infinityone.modeloWeb.Listaprecio;
import ec.com.infinityone.modeloWeb.ListaprecioPK;
import ec.com.infinityone.modeloWeb.Listaprecioterminalproducto;
import ec.com.infinityone.modeloWeb.ListaprecioterminalproductoPK;
import ec.com.infinityone.modeloWeb.Medida;
import ec.com.infinityone.modeloWeb.ObjetoDetallePrecio;
import ec.com.infinityone.modeloWeb.ObjetoPrecio;
import ec.com.infinityone.modeloWeb.Precio;
import ec.com.infinityone.modeloWeb.PrecioPK;
import ec.com.infinityone.modeloWeb.Producto;
import ec.com.infinityone.modeloWeb.Terminal;
import ec.com.infinityone.preciosyfacturacion.servicios.GravamenServicio;
import ec.com.infinityone.preciosyfacturacion.servicios.ListaprecioServicio;
import ec.com.infinityone.preciosyfacturacion.servicios.ListaprecioterminalproductoServicio;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author David
 */
@Named
@ViewScoped
public class ActualizarPrecioBean extends ReusableBean implements Serializable {

    @Inject
    private ComercializadoraServicio comercializadoraServicio;
    /*
    Variable para obtener los métodos de Listaprecioservicio
     */
    @Inject
    private ListaprecioServicio listaprecioServicio;
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
    Variable para acceder a los servicios de cliente servicio
     */
    @Inject
    private ClienteServicio clienteServicio;

    @Inject
    private ListaprecioterminalproductoServicio listaprecioterminalproductoServicio;

    /*
    Variable que almacena varios Bancos
     */
    private List<Precio> listaPrecios;
    /*
    Variable que alamcena un precio
     */
    private Precio precio;
    /*
    Variable que almacena un precio PK
     */
    private PrecioPK precioPK;
    /*
    Variable que almacena una comercializadora
     */
    private ComercializadoraBean comercializadora;
    /*
    Variable que almacena varias comercializadoras
     */
    private List<ComercializadoraBean> listComercializadora;
    /*
    Variable que almacena el código de la comercializadora
     */
    private String codComer;
    /*
    Variable que alamcena una comercializadora productoo
     */
    private Comercializadoraproducto comerProd;
    /*
    Variable que almacena una comercializadora producto pk
     */
    private ComercializadoraproductoPK comerProdPK;
    /*
    Lista que alamacena varias comercializadorasproducto
     */
    private List<Comercializadoraproducto> listaComercializadoraProducto;
    /*
    Variable que almacena varios Listaprecios
     */
    private List<Listaprecio> listaListaprecios;
    /*
    Variable listaprecio
     */
    private Listaprecio listaprecioselected;
    /*
    Variable listaprecioPK
     */
    private ListaprecioPK listaprecioPK;
    /*
    Variable pára almacenar el codigo de listade precio
     */
    private long codigoListaPre;
    /*
    Variable para modificar el precio sugerido
     */
    private BigDecimal pvpsugerido;
    /*
    Variable para almacenar un producto
     */
    private Producto producto;
    /*
    Variable para almacenar medida
     */
    private Medida medida;
    /*
    Variable para fecha Inicio
     */
    private Date fechaInicio;
    /*
    Variable para guardar un comnetario
     */
    private String comentario;
    /*
    Variable Terminal
     */
    private Terminal terminal;
    /*
    Variable que almacena varias terminales
     */
    private List<Terminal> listaTerminales;
    /*
    Objeto Precio
     */
    private ObjetoPrecio objprecio;
    /*
    Variable para almacenar y mostar el Detalle de Precios en el Paso 3
     */
    private ObjetoDetallePrecio objDetalle;
    /*
    Lista que almacena varios detelles precios para ser mostrados en el paso 3
     */
    private List<ObjetoDetallePrecio> listaObjDetalle;
    /*
    Variable que almacena varios Objetos Precio
     */
    private List<ObjetoPrecio> listaObjetoPrecio;
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
    Variable Gravamen
     */
    private Gravamen gravamen;
    /*
    Variable que almacena varios Gravamenes
     */
    private List<Gravamen> listaGravamen;
    /*
    Variable Lista Precio Terminal Producto
     */
    private List<Listaprecioterminalproducto> listaTerminalProd;
    /*
    Variable que almacena varias Comercializaros
     */
    private List<Cliente> listaClientes;
    /*
    Variables para mostrar o desaparecer forms
     */
    private boolean step1;
    private boolean step2;
    private boolean step3;
    /*
    Variable para habilitar o no el boton de siguiente
     */
    private boolean inhabilitar;
    /*
    Variable para identificar si un cliente se encuentra activo
     */
    private boolean esActivo;
    /*
    Variable para controlar la fecha de ingreso de la acutlizacion de precio
     */
    private Date fechaActualizada;

    /**
     * Constructor por defecto
     */
    public ActualizarPrecioBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        precio = new Precio();
        precioPK = new PrecioPK();
        comercializadora = new ComercializadoraBean();
        codComer = "";
        comerProd = new Comercializadoraproducto();
        comerProdPK = new ComercializadoraproductoPK();
        listaprecioselected = new Listaprecio();
        listaprecioPK = new ListaprecioPK();
        pvpsugerido = new BigDecimal(0);
        producto = new Producto();
        medida = new Medida();
        //fechaInicio = new Date();
        comentario = "";
        terminal = new Terminal();
        objprecio = new ObjetoPrecio();
        objDetalle = new ObjetoDetallePrecio();
        detallePrecio = new Detalleprecio();
        detallePrecioPK = new DetalleprecioPK();
        listaDetallePrecio = new ArrayList<>();
        listaObjDetalle = new ArrayList<>();
        listaTerminalProd = new ArrayList<>();
        listaComercializadoraProducto = new ArrayList<>();
        step1 = true;
        step2 = false;
        step3 = false;
        inhabilitar = false;
        obtenerListaComercializadora();
        obtenerTerminales();
    }

    public void obtenerListaComercializadora() {
        listComercializadora = new ArrayList<>();
        listComercializadora = comercializadoraServicio.obtenerComercializadorasActivas();
        if (!listComercializadora.isEmpty()) {
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
                for (int i = 0; i < listComercializadora.size(); i++) {
                    if (listComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listComercializadora.get(i);
                    }
                }
                if (comercializadora.getActivo().equals("S")) {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA DEBE ESTAR INACTIVA PARA PODER REALIZAR LA ACTUALIZACIÓN DE PRECIOS");
                }

            }
            if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listComercializadora.size(); i++) {
                    if (listComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listComercializadora.get(i);
                    }
                }
                if (comercializadora.getActivo().equals("S")) {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA DEBE ESTAR INACTIVA PARA PODER REALIZAR LA ACTUALIZACIÓN DE PRECIOS");
                }

            }
        }
    }

    public void seleccionarComercializadora() {
        if (comercializadora != null) {
            //if (comercializadora.getActivo().equals("N")) {
            //inhabilitar = true;
            codComer = comercializadora.getCodigo();
            listaGravamen = new ArrayList<>();
            listaGravamen = gravamenServicio.obtenerGravamenes(codComer);
            listaListaprecios = new ArrayList<>();
            listaListaprecios = listaprecioServicio.obtenerListaprecioEstado(codComer, true);
//            }else{
//                inhabilitar = false;
//                listaGravamen = new ArrayList<>();
//                listaListaprecios = new ArrayList<>();
//                this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA DEBE ESTAR INACTIVA PARA PODER REALIZAR LA ACTUALIZACIÓN DE PRECIOS");
//            }
        }
    }

    public void obtenerListaPrecio() {
        listaListaprecios = new ArrayList<>();
        listaListaprecios = listaprecioServicio.obtenerListaprecio();
    }

    public void seleccionarListaPrecio() {
        Date date;
        if (listaprecioselected != null) {
            codigoListaPre = listaprecioselected.getListaprecioPK().getCodigo();
            if (clienteServicio.obtenerClientePorListaPrecio(codigoListaPre) > 0) {
                inhabilitar = false;
                listaComercializadoraProducto = new ArrayList<>();
            } else {
                listaComercializadoraProducto = new ArrayList<>();
                inhabilitar = true;
                date = listaprecioServicio.obtenerUltimaFechaDePrecio(comercializadora.getCodigo(), codigoListaPre);
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, 1);
                fechaInicio = c.getTime();
            }
        }
    }

    public void obtenerTerminales() {
        listaTerminales = new ArrayList<>();
        listaTerminales = terminalServicio.obtenerTerminal();
    }

    public void obtenerListaTerminalProducto() {
        try {
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.comercializadoraproducto/enListaP?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadoraproducto/enListaP?";

            url = new URL(direcc + "codigocomercializadora=" + codComer + "&codigolistaprecio=" + codigoListaPre);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaComercializadoraProducto = new ArrayList<>();
            comerProd = new Comercializadoraproducto();
            comerProdPK = new ComercializadoraproductoPK();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject precioJson = new JSONObject(respuesta);
            JSONArray retorno = precioJson.getJSONArray("retorno");
            if (retorno.isEmpty()) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCONTRARON REGISTROS");
            } else {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject prod = retorno.getJSONObject(indice);
                        JSONObject prodPK = prod.getJSONObject("comercializadoraproductoPK");

                        comerProdPK.setCodigocomercializadora(prodPK.getString("codigocomercializadora"));
                        comerProdPK.setCodigomedida(prodPK.getString("codigomedida"));
                        comerProdPK.setCodigoproducto(prodPK.getString("codigoproducto"));
                        comerProd.setComercializadoraproductoPK(comerProdPK);
                        if (comerProd.getComercializadoraproductoPK().getCodigoproducto() != null) {
                            obtenerProducto(comerProd);
                        }
                        if (comerProd.getComercializadoraproductoPK().getCodigomedida() != null) {
                            obtenerMedida(comerProd);
                        }

                        comerProd.setActivo(prod.getBoolean("activo"));
                        comerProd.setMargencomercializacion(prod.getBigDecimal("margencomercializacion"));
                        comerProd.setPrecioepp(prod.getBigDecimal("precioepp"));
                        comerProd.setPvpsugerido(prod.getBigDecimal("pvpsugerido"));
                        comerProd.setSoloaplicaiva(prod.getBoolean("soloaplicaiva"));
                        comerProd.setProcesar(prod.getBoolean("procesar"));
                        comerProd.setProducto(producto);
                        comerProd.setMedida(medida);

                        listaComercializadoraProducto.add(comerProd);
                        comerProd = new Comercializadoraproducto();
                        comerProdPK = new ComercializadoraproductoPK();
                    }
                }
                if (connection.getResponseCode() == 200) {
                    //PrimeFaces.current().executeScript("PF('modificar').show()");
                } else {
                    System.out.println(connection.getResponseCode());
                    System.out.println(connection.getResponseMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerProducto(Comercializadoraproducto comerProd) {
        try {
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.producto/porId?codigo=" + comerProd.getComercializadoraproductoPK().getCodigoproducto();
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.producto/porId?codigo=" + comerProd.getComercializadoraproductoPK().getCodigoproducto();

            url = new URL(direcc);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            producto = new Producto();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject precioJson = new JSONObject(respuesta);
            JSONArray retorno = precioJson.getJSONArray("retorno");
            if (retorno.isEmpty()) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCONTRARON REGISTROS");
            } else {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject produc = retorno.getJSONObject(indice);
                        producto.setCodigo(produc.getString("codigo"));
                        producto.setNombre(produc.getString("nombre"));
                        producto.setPorcentajeivapresuntivo(produc.getBigDecimal("porcentajeivapresuntivo"));
                    }
                }
                if (connection.getResponseCode() != 200) {
                    System.out.println(connection.getResponseCode());
                    System.out.println(connection.getResponseMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerMedida(Comercializadoraproducto comerProd) {
        try {
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.medida/porId?codigo=" + comerProd.getComercializadoraproductoPK().getCodigomedida();
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.medida/porId?codigo=" + comerProd.getComercializadoraproductoPK().getCodigomedida();

            url = new URL(direcc);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            medida = new Medida();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject precioJson = new JSONObject(respuesta);
            JSONArray retorno = precioJson.getJSONArray("retorno");
            if (retorno.isEmpty()) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCONTRARON REGISTROS");
            } else {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject med = retorno.getJSONObject(indice);
                        medida.setCodigo(med.getString("codigo"));
                        medida.setNombre(med.getString("nombre"));
                        medida.setAbreviacion(med.getString("abreviacion"));
                    }
                }
                if (connection.getResponseCode() != 200) {
                    System.out.println(connection.getResponseCode());
                    System.out.println(connection.getResponseMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paso1() {
        step1 = false;
        step2 = true;
        step3 = false;
        listaObjetoPrecio = new ArrayList<>();
        listaPrecios = new ArrayList<>();
        for (int i = 0; i < listaComercializadoraProducto.size(); i++) {
            if (listaComercializadoraProducto.get(i).isProcesar()) {
                /*------------------PrecioPK----------------------------------*/
                precioPK.setCodigoproducto(listaComercializadoraProducto.get(i).getProducto().getCodigo());
                precioPK.setCodigomedida(listaComercializadoraProducto.get(i).getMedida().getCodigo());
                precioPK.setCodigolistaprecio(listaprecioselected.getListaprecioPK().getCodigo());
                precioPK.setFechainicio(fechaInicio);
                precioPK.setCodigocomercializadora(codComer);
                /*------------------Objeto Precio-----------------------------*/
                precio.setComercializadoraproducto(listaComercializadoraProducto.get(i));
                precio.setListaprecio(listaprecioselected);
                precio.setPrecioPK(precioPK);
                /*------------------Objeto ObjPrecio--------------------------*/
                objprecio.setPrecio(precio);
                objprecio.setPrecioepp(listaComercializadoraProducto.get(i).getPrecioepp());
                objprecio.setPvpsugerido(listaComercializadoraProducto.get(i).getPvpsugerido());
                obtenerValores(listaComercializadoraProducto.get(i), listaprecioselected);
                /*------------------Objeto ObjDetalle--------------------------*/
                objDetalle.setPrecio(precio);
                objDetalle.setPrecioepp(listaComercializadoraProducto.get(i).getPrecioepp());
                objDetalle.setPvpsugerido(listaComercializadoraProducto.get(i).getPvpsugerido());

                listaObjetoPrecio.add(objprecio);
                listaPrecios.add(precio);
            }
            producto = new Producto();
            medida = new Medida();
            precio = new Precio();
            precioPK = new PrecioPK();
            objprecio = new ObjetoPrecio();
            objDetalle = new ObjetoDetallePrecio();
        }
    }

    public void obtenerValores(Comercializadoraproducto comerP, Listaprecio listaprecioselected) {
        List<Listaprecioterminalproducto> precioTermProdAux;
        terminal = new Terminal();
        precioTermProdAux = listaprecioterminalproductoServicio.obtenerListaprecioterminalprod(listaprecioselected.getListaprecioPK().getCodigo());
        for (int i = 0; i < listaTerminales.size(); i++) {
            if (precioTermProdAux.get(0).getListaprecioterminalproductoPK().getCodigoterminal().equals(listaTerminales.get(i).getCodigo())) {
                terminal = listaTerminales.get(i);
                break;
            }
        }
        //for(int i = 0; i < listaTerminales.size(); i++){
        obtenerValorMargenCMargenP(terminal, comerP);
        //}
    }

    public void obtenerValorMargenCMargenP(Terminal term, Comercializadoraproducto comerP) {
        try {
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.listaprecioterminalproducto/porId?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.listaprecioterminalproducto/porId?";

            url = new URL(direcc + "&codigocomercializadora=" + comerP.getComercializadoraproductoPK().getCodigocomercializadora()
                    + "&codigolistaprecio=" + listaprecioselected.getListaprecioPK().getCodigo()
                    + "&codigoterminal=" + term.getCodigo()
                    + "&codigoproducto=" + comerP.getComercializadoraproductoPK().getCodigoproducto()
                    + "&codigomedida=" + comerP.getComercializadoraproductoPK().getCodigomedida());

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
            for (int indice = 0; indice < retorno.length(); indice++) {
                if (!retorno.isNull(indice)) {
                    JSONObject objprecios = retorno.getJSONObject(indice);
                    if (!objprecios.isNull("margenporcentaje")) {
                        objprecio.setMargenporcentaje(objprecios.getBigDecimal("margenporcentaje"));
                    }
                    if (!objprecios.isNull("margenvalorcomercializadora")) {
                        objprecio.setMargenvalorcomercializadora(objprecios.getBigDecimal("margenvalorcomercializadora"));
                    }
                }
            }

            if (connection.getResponseCode() != 200) {
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
        BigDecimal dpcg2 = new BigDecimal(0);
        BigDecimal dpcg3 = new BigDecimal(0);
        BigDecimal dpcg4 = new BigDecimal(0);
        BigDecimal dpcg5 = new BigDecimal(0);
        BigDecimal dpcg6 = new BigDecimal(0);
        BigDecimal dpcg9 = new BigDecimal(0);
        BigDecimal dpcg5F = new BigDecimal(0);

        for (int i = 0; i < listaObjetoPrecio.size(); i++) {
            objDetalle.setPrecio(listaObjetoPrecio.get(i).getPrecio());
            objDetalle.setPrecioepp(listaObjetoPrecio.get(i).getPrecioepp());
            objDetalle.setPvpsugerido(listaObjetoPrecio.get(i).getPvpsugerido());
            for (int j = 0; j < listaGravamen.size(); j++) {
                switch (listaGravamen.get(j).getGravamenPK().getCodigo()) {
                    //Precio Terminal EPP
                    case "0001":
                        dpcg1 = listaObjetoPrecio.get(i).getPrecioepp().divide(listaGravamen.get(j).getValordefecto(), 6, RoundingMode.HALF_UP);
                        detallePrecioPK.setCodigocomercializadora(listaObjetoPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                        detallePrecioPK.setCodigoproducto(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                        detallePrecioPK.setCodigomedida(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                        detallePrecioPK.setCodigolistaprecio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                        detallePrecioPK.setFechainicio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
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
                    case "0005":
                        if (listaObjetoPrecio.get(i).getPrecio().getListaprecio().getTipo().equals("MCO")) {
                            BigDecimal mcsiva = (listaObjetoPrecio.get(i).getPrecio().getComercializadoraproducto().getMargencomercializacion().divide(new BigDecimal(1.12), 6, RoundingMode.HALF_UP)).setScale(6, RoundingMode.HALF_UP);
                            dpcg4 = (mcsiva.multiply((listaObjetoPrecio.get(i).getMargenvalorcomercializadora().divide(new BigDecimal(100))))).setScale(6, RoundingMode.HALF_UP);
                            //dpcg4 = (listaObjetoPrecio.get(i).getPrecio().getComercializadoraproducto().getMargencomercializacion().multiply((listaObjetoPrecio.get(i).getMargenvalorcomercializadora().divide(new BigDecimal(100))))).setScale(6, RoundingMode.HALF_UP);
                            detallePrecioPK.setCodigocomercializadora(listaObjetoPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                            detallePrecioPK.setCodigoproducto(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                            detallePrecioPK.setCodigomedida(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                            detallePrecioPK.setCodigolistaprecio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                            detallePrecioPK.setFechainicio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
                            detallePrecioPK.setCodigo("");
                            detallePrecioPK.setCodigogravamen(listaGravamen.get(j).getGravamenPK().getCodigo());
                            detallePrecio.setDetalleprecioPK(detallePrecioPK);
                            detallePrecio.setValor(dpcg1);
                            detallePrecio.setUsuarioactual(dataUser.getUser().getNombrever());
                            listaDetallePrecio.add(detallePrecio);
                            detallePrecio = new Detalleprecio();
                            detallePrecioPK = new DetalleprecioPK();
                            objDetalle.setMargenxcliente(dpcg4);
                        } else {
                            dpcg4 = (dpcg1.multiply((listaObjetoPrecio.get(i).getMargenporcentaje().divide(new BigDecimal(100))))).setScale(6, RoundingMode.HALF_UP);
                            detallePrecioPK.setCodigocomercializadora(listaObjetoPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                            detallePrecioPK.setCodigoproducto(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                            detallePrecioPK.setCodigomedida(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                            detallePrecioPK.setCodigolistaprecio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                            detallePrecioPK.setFechainicio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
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
                        detallePrecioPK.setCodigocomercializadora(listaObjetoPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                        detallePrecioPK.setCodigoproducto(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                        detallePrecioPK.setCodigomedida(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                        detallePrecioPK.setCodigolistaprecio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                        detallePrecioPK.setFechainicio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
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
                        dpcg2 = (dpcg9.multiply(listaGravamen.get(j).getValordefecto())).setScale(6, RoundingMode.HALF_UP);
                        detallePrecioPK.setCodigocomercializadora(listaObjetoPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                        detallePrecioPK.setCodigoproducto(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                        detallePrecioPK.setCodigomedida(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                        detallePrecioPK.setCodigolistaprecio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                        detallePrecioPK.setFechainicio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
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
                    //Iva Presuntivo
                    case "0004":
                        if (listaObjetoPrecio.get(i).getPrecio().getComercializadoraproducto().getSoloaplicaiva()) {
                            dpcg5 = new BigDecimal(0);
                            detallePrecioPK.setCodigocomercializadora(listaObjetoPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                            detallePrecioPK.setCodigoproducto(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                            detallePrecioPK.setCodigomedida(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                            detallePrecioPK.setCodigolistaprecio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                            detallePrecioPK.setFechainicio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
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
                            //dpcg5 = ((listaObjetoPrecio.get(i).getPvpsugerido().divide(new BigDecimal(1.12), 6, RoundingMode.HALF_UP)).subtract(dpcg9)).multiply(listaGravamen.get(j).getValordefecto()).setScale(6, RoundingMode.HALF_UP);
                            dpcg5F = dpcg2.multiply(listaObjetoPrecio.get(i).getPrecio().getComercializadoraproducto().getProducto().getPorcentajeivapresuntivo()).divide(new BigDecimal(100)).setScale(6, RoundingMode.HALF_UP);
                            detallePrecioPK.setCodigocomercializadora(listaObjetoPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                            detallePrecioPK.setCodigoproducto(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                            detallePrecioPK.setCodigomedida(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                            detallePrecioPK.setCodigolistaprecio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                            detallePrecioPK.setFechainicio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
                            detallePrecioPK.setCodigo("");
                            detallePrecioPK.setCodigogravamen(listaGravamen.get(j).getGravamenPK().getCodigo());
                            detallePrecio.setDetalleprecioPK(detallePrecioPK);
                            detallePrecio.setValor(dpcg5F);
                            detallePrecio.setUsuarioactual(dataUser.getUser().getNombrever());
                            listaDetallePrecio.add(detallePrecio);
                            detallePrecio = new Detalleprecio();
                            detallePrecioPK = new DetalleprecioPK();
                            objDetalle.setIvaPresuntivo(dpcg5F);
                        }
                        break;
                    //Tres X Mil
                    case "0328":
                        if (listaObjetoPrecio.get(i).getPrecio().getComercializadoraproducto().getSoloaplicaiva()) {
                            dpcg6 = new BigDecimal(0);
                            detallePrecioPK.setCodigocomercializadora(listaObjetoPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                            detallePrecioPK.setCodigoproducto(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                            detallePrecioPK.setCodigomedida(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                            detallePrecioPK.setCodigolistaprecio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                            detallePrecioPK.setFechainicio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
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
                            detallePrecioPK.setCodigocomercializadora(listaObjetoPrecio.get(i).getPrecio().getComercializadoraproducto().getComercializadoraproductoPK().getCodigocomercializadora());
                            detallePrecioPK.setCodigoproducto(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigoproducto());
                            detallePrecioPK.setCodigomedida(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigomedida());
                            detallePrecioPK.setCodigolistaprecio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getCodigolistaprecio());
                            detallePrecioPK.setFechainicio(listaObjetoPrecio.get(i).getPrecio().getPrecioPK().getFechainicio());
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
//for (int k = 0; k < listaDetallePrecio.size(); k++) {
            objDetalle.setCodigoTerminal(terminal.getCodigo());
            listaObjDetalle.add(objDetalle);
            //}
            objDetalle = new ObjetoDetallePrecio();
            listaPrecios.get(i).setDetalleprecioList(listaDetallePrecio);
            listaPrecios.get(i).setPrecioproducto(dpcg9);
            listaDetallePrecio = new ArrayList<>();
        }
    }

    public void savePrice() {
        for (int i = 0; i < listaPrecios.size(); i++) {
            List<Terminal> listaTerminalesAux = new ArrayList<>();
            listaTerminalesAux.add(terminal);
            for (int j = 0; j < listaTerminalesAux.size(); j++) {
                addItemsPrice(i, j, listaPrecios, listaTerminalesAux);
            }
        }
        listaObjDetalle = new ArrayList<>();
    }

    public void addItemsPrice(int i, int j, List<Precio> precio, List<Terminal> listaTerminal) {
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

            objPK.put("codigocomercializadora", precio.get(i).getPrecioPK().getCodigocomercializadora());
            objPK.put("codigoterminal", listaTerminal.get(j).getCodigo());
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
            obj.put("observacion", comentario);
            obj.put("precioproducto", precio.get(i).getPrecioproducto());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());

            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "PRECIOS REGISTRADOS EXITOSAMENTE");
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
                    saveDetailP(precio.get(i).getDetalleprecioList(), codigo, listaTerminal.get(j).getCodigo());
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR PRECIOS");
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
            String fechaI = date.format(fechaInicio) + "T12:00:00";
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
                this.dialogo(FacesMessage.SEVERITY_INFO, "DETALLES DE PRECIO REGISTRADOS EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR DETALLES");
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println("Error:" + connection.getErrorStream());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String obtenerMargenPorcentaje(Comercializadoraproducto comerProducto, int val) {
        if (producto != null) {
            List<Listaprecioterminalproducto> precioTermProdAux;
            precioTermProdAux = listaprecioterminalproductoServicio.obtenerListaprecioterminalprod(listaprecioselected.getListaprecioPK().getCodigo());
            for (int i = 0; i < precioTermProdAux.size(); i++) {
                if (comerProducto.getComercializadoraproductoPK().getCodigocomercializadora().equals(precioTermProdAux.get(i).getListaprecioterminalproductoPK().getCodigocomercializadora()) &&
                    comerProducto.getComercializadoraproductoPK().getCodigomedida().equals(precioTermProdAux.get(i).getListaprecioterminalproductoPK().getCodigomedida()) &&
                    comerProducto.getComercializadoraproductoPK().getCodigoproducto().equals(precioTermProdAux.get(i).getListaprecioterminalproductoPK().getCodigoproducto())) {
                    if (val == 1) {
                        if (precioTermProdAux.get(i).getMargenporcentaje().toString().equals("-99.0")) {
                            return "0";
                        } else {
                            return precioTermProdAux.get(i).getMargenporcentaje().toString();
                        }
                    }
                    if (val == 2) {
                        if (precioTermProdAux.get(i).getMargenvalorcomercializadora().toString().equals("-99.0")) {
                            return "0";
                        } else {
                            return precioTermProdAux.get(i).getMargenvalorcomercializadora().toString();
                        }
                    }
                }
            }
        }
        return "";
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public List<ComercializadoraBean> getListComercializadora() {
        return listComercializadora;
    }

    public void setListComercializadora(List<ComercializadoraBean> listComercializadora) {
        this.listComercializadora = listComercializadora;
    }

    public List<Precio> getListaPrecios() {
        return listaPrecios;
    }

    public void setListaPrecios(List<Precio> listaPrecios) {
        this.listaPrecios = listaPrecios;
    }

    public Precio getPrecio() {
        return precio;
    }

    public void setPrecio(Precio precio) {
        this.precio = precio;
    }

    public List<Comercializadoraproducto> getListaComercializadoraProducto() {
        return listaComercializadoraProducto;
    }

    public void setListaComercializadoraProducto(List<Comercializadoraproducto> listaComercializadoraProducto) {
        this.listaComercializadoraProducto = listaComercializadoraProducto;
    }

    public List<Listaprecio> getListaListaprecios() {
        return listaListaprecios;
    }

    public void setListaListaprecios(List<Listaprecio> listaListaprecios) {
        this.listaListaprecios = listaListaprecios;
    }

    public BigDecimal getPvpsugerido() {
        return pvpsugerido;
    }

    public void setPvpsugerido(BigDecimal pvpsugerido) {
        this.pvpsugerido = pvpsugerido;
    }

    public Listaprecio getListaprecioselected() {
        return listaprecioselected;
    }

    public void setListaprecioselected(Listaprecio listaprecioselected) {
        this.listaprecioselected = listaprecioselected;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public List<ObjetoPrecio> getListaObjetoPrecio() {
        return listaObjetoPrecio;
    }

    public void setListaObjetoPrecio(List<ObjetoPrecio> listaObjetoPrecio) {
        this.listaObjetoPrecio = listaObjetoPrecio;
    }

    public List<ObjetoDetallePrecio> getListaObjDetalle() {
        return listaObjDetalle;
    }

    public void setListaObjDetalle(List<ObjetoDetallePrecio> listaObjDetalle) {
        this.listaObjDetalle = listaObjDetalle;
    }

    public boolean isStep1() {
        return step1;
    }

    public void setSetp1(boolean paso1) {
        this.step1 = paso1;
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

    public boolean isInhabilitar() {
        return inhabilitar;
    }

    public void setInhabilitar(boolean inhabilitar) {
        this.inhabilitar = inhabilitar;
    }

    public Date getFechaActualizada() {
        return fechaActualizada;
    }

    public void setFechaActualizada(Date fechaActualizada) {
        this.fechaActualizada = fechaActualizada;
    }
}
