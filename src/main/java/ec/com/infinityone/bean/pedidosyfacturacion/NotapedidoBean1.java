/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.pedidosyfacturacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.infinityone.serivicio.actorcomercial.ClienteProductoServicio;
import ec.com.infinityone.serivicio.actorcomercial.ClienteServicio;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.servicio.catalogo.BancoServicio;
import ec.com.infinityone.servicio.catalogo.MedidaServicio;
import ec.com.infinityone.servicio.catalogo.TerminalServicio;
import ec.com.infinityone.modelo.Abastecedora;
import ec.com.infinityone.modelo.Banco;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.Comercializadora;
import ec.com.infinityone.modelo.Detallenotapedido;
import ec.com.infinityone.modelo.DetallenotapedidoPK;
import ec.com.infinityone.modelo.EnvioPedido;
import ec.com.infinityone.modelo.Formapago;
import ec.com.infinityone.modelo.Medida;
import ec.com.infinityone.modelo.Notapedido;
import ec.com.infinityone.modelo.NotapedidoPK;
import ec.com.infinityone.modelo.Producto;
import ec.com.infinityone.modelo.Terminal;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.bean.enums.CodigoComerEnum;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Autotanque;
import ec.com.infinityone.modelo.Conductor;
import ec.com.infinityone.modelo.Detalleterminalsello;
import ec.com.infinityone.servicio.pedidosyfacturacion.SellosServicio;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author HP
 */
@Named
@ViewScoped
public class NotapedidoBean1 extends ReusableBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(FacturacionBean.class.getName());

    /*
    Variable para renderizar la pantalla
     */
    private boolean mostarNotaPedido;
    /*
    Variable para renderizar la pantalla
     */
    private boolean mostarPantallaInicial;
    /*
    Variable para acceder a los servicios de Comercialziadora
     */
    @Inject
    private ComercializadoraServicio comerServicio;
    /*
    Variable para acceder a los servicios de Banco
     */
    @Inject
    private BancoServicio bancoServicio;
    /*
    Variable para acceder a los servicios de Medida
     */
    @Inject
    private MedidaServicio medidaServicio;
    /*
    Variable para acceder a los servicios de Terminal
     */
    @Inject
    private TerminalServicio termServicio;
    /*
    Variable para acceder a los servicios de Cliente
     */
    @Inject
    private ClienteServicio clienteServicio;
    /*
    Variable para acceder a los servicios de Cliente
     */
    @Inject
    private ClienteProductoServicio cliProdServicio;
    
    /*
    Variable para acceder a los servicios de TerminalSello
     */
    @Inject
    private SellosServicio sellosServicio;
    
    /*
    Variable Comercializadora
     */
    private ComercializadoraBean comercializadora;
    /*
    Variable Comercializadora
     */
    private Terminal terminal;
    /*
    Variable que almacena el código de la comercializadora
     */
    private String codComer;
    /*
    Variable que almacena el código del terminal
     */
    private String codTerminal;
    /*
    Variable que almacena el código del cliente
     */
    private String codCliente;
    /*
    Variable que almacena el código de la abastecedora
     */
    private String codAbas;
    /*
    Varaible para guardar la selección del radio button
     */
    private String tipoFecha;
    /**
     * Variable que permite establecer la fecha
     */
    private Date fecha;
    /*
    Variable para establoecer el valor de oeenpetro
     */
    private String oeenpetro;
    /*
    Variable Nota Pedido
     */
    private Notapedido np;
    /*
    Varibale Nota Pedido PK
     */
    private NotapedidoPK npPK;
    /*
    Variable que isntacia el modelo NotapedidoPK
     */
    private Detallenotapedido detNP;
    /*
    Variable para guardar una lista deDeatllesFactura
     */
    private List<Detallenotapedido> listDetNP;
    /*
    Variable que isntacia el modelo DetallenotapedidoPK
     */
    private DetallenotapedidoPK detNPK;
    /*
    Variable para guardar Nota y Detalle Pedido
     */
    private transient EnvioPedido envNP;
    /*
    Variable para guardar una lista de Nota y Detalle Pedido
     */
    private List<EnvioPedido> listenvNP; 
    /*
    Variable Cliente
     */
    private Cliente cliente;
    /*
    Variable terminal 
     */
    private Terminal terminalT;
    /*
    Variable Banco
     */
    private Banco banco;
    /*
    Variable Comercializadora
     */
    private Comercializadora comerc;
    /*
    Variable Abastecedora
     */
    private Abastecedora abas;
    /*
    Variable para almacenar los datos comercializadora
     */
    private List<ComercializadoraBean> listaComercializadora;
    /*
    Variable para almacenar los datos comercializadora
     */
    private List<Terminal> listaTermianles;
    /*
    Variable para almacenar los datos clientes
     */
    private List<Cliente> listaClientes;
    /*
    Variable Formapago
     */
    private Formapago formap;
    /*
    Variable fecha venta
     */
    private Date fechaVenta;
    /*
    Variable fechaDespacho
     */
    private Date fechaDespacho;
    /*
    Lista de Productos
     */
    private List<Producto> listaProd;
    /*
    Lista de Bancos
     */
    private List<Banco> listaBancos;
    /*
    Lista de Medidas
     */
    private List<Medida> listaMedida;
    
    /*
    Lista de DetalleTerminalSellos
     */
    private List<Detalleterminalsello> listaDetalleTerminalSellos;
    
    /*
    Variable Medida
     */
    private Medida medida;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarNotapedido;
    /*
    Variable Producto
     */
    private Producto productoSeleccionado;
    /*
    Variable Producto
     */
    private List<Producto> listaProductos;
    /*
    Varible prefijo
     */
    private String prefijo;
    /*
    Variable numero nota pedido
     */
    private String numeroNotaPedio;
    /*
    Variable para establecer la fecha mínima
     */
    private Date fechaMin;
    /*
    Vairbale para almacenar el pdf generado
     */
    private StreamedContent pdfStream;
    /*
    variable para establecer la fecha inicial para la busqueda de notas de pedido
     */
    private Date fechaInicial;
    /*
    variable para establecer la fecha final para la busqueda de notas de pedido
     */
    private Date fechaFinal;
    /*
    variable para realizar la anulación de notapedido
     */
    private Notapedido notaPedidoAuxiliar;
    /*
    variable para realizar la anulación de notapedidoPK
     */
    private NotapedidoPK notaPedidoAuxiliarPK;
    /*
    ariable para realizar la anulación de notapedido
     */
    private EnvioPedido envioPedidoAuxiliar;

    private boolean todosClientes;

    private List<Autotanque> listaAutotanque;
    
    private List<Conductor> listaConductores;

    private Autotanque autotanque;

    private Autotanque autotanqueAux;

    private Conductor conductor;

    private BigDecimal volTotal;

    private BigDecimal compartimento1;
    private BigDecimal compartimento2;
    private BigDecimal compartimento3;
    private BigDecimal compartimento4;
    private BigDecimal compartimento5;
    private BigDecimal compartimento6;
    private BigDecimal compartimento7;
    private BigDecimal compartimento8;
    private BigDecimal compartimento9;
    private BigDecimal compartimento10;
    private Integer selloinicial;
    private Integer sellofinal;
    private Integer cantidadSellos;

    /*
    Variables para almacenar la imfoanción de forma de pago y guardar en observacion
     */
    private String nomBanco;

    private String numCuenta;

    private String numCheque;

    /*
    Variable para renderizar o no el panel forma de pago, dependiedo la comercializadora
     */
    private boolean mostarFormaPago;
    /*
    Variable para renderizar o no el panel de observacion, dependiedo la comercializadora
     */
    private boolean mostarObservacion;

    /**
     * Constructor por defecto
     */
    public NotapedidoBean1() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        reestablecer();
        mostarNotaPedido = false;
        mostarPantallaInicial = true;
        cliente = new Cliente();
        inicializarValTanques();
        obtenerTerminales();
        obtenerComercializadora();
        obtenerAutotanque();
        obtenerConductores();
        todosClientes = false;
        mostarFormaPago = false;
        mostarObservacion = false;
        //habilitarBusqueda();
    }

    public void inicializarValTanques() {
        autotanqueAux = new Autotanque();
        autotanqueAux.setCompartimento1(new BigDecimal(0));
        autotanqueAux.setCompartimento2(new BigDecimal(0));
        autotanqueAux.setCompartimento3(new BigDecimal(0));
        autotanqueAux.setCompartimento4(new BigDecimal(0));
        autotanqueAux.setCompartimento5(new BigDecimal(0));
        autotanqueAux.setCompartimento6(new BigDecimal(0));
        autotanqueAux.setCompartimento7(new BigDecimal(0));
        autotanqueAux.setCompartimento8(new BigDecimal(0));
        autotanqueAux.setCompartimento9(new BigDecimal(0));
        autotanqueAux.setCompartimento10(new BigDecimal(0));
    }

    public void nuevaNotaPedido() {
        reestablecer();
        obtenerBanco();
        obtenerMedida();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        } else {
            seleccionarComercializdora();
        }
        if (habilitarCli) {
            cliente = new Cliente();
            codTerminal = "";
            codCliente = "";
            listaProductos = new ArrayList<>();
        } else {
            seleccionarCliente();
        }
        habilitarBusqueda(2);
//        if (habilitarTerminal) {
//            terminal = new TerminalBean();
//        }
        mostarNotaPedido = true;
        mostarPantallaInicial = false;

    }

    public void reestablecer() {
        editarNotapedido = false;
        codComer = "";
        //codTerminal = "";
        codCliente = "";
        numeroNotaPedio = "";
        codAbas = "";
        tipoFecha = "";
        prefijo = "";
        fecha = new Date();
        oeenpetro = "";
        np = new Notapedido();
        npPK = new NotapedidoPK();
        detNP = new Detallenotapedido();
        detNPK = new DetallenotapedidoPK();
        envNP = new EnvioPedido();
        notaPedidoAuxiliar = new Notapedido();
        notaPedidoAuxiliarPK = new NotapedidoPK();
        envioPedidoAuxiliar = new EnvioPedido();
        //cliente = new Cliente();
        terminalT = new Terminal();
        banco = new Banco();
        comerc = new Comercializadora();
        abas = new Abastecedora();
        formap = new Formapago();
        listenvNP = new ArrayList<>();
        fechaVenta = new Date();
        fechaMin = new Date();
        productoSeleccionado = new Producto();
        medida = new Medida();
        listaProd = new ArrayList<>();
        listaBancos = new ArrayList<>();
        listaMedida = new ArrayList<>();
        listaDetalleTerminalSellos = new ArrayList<>();
        nomBanco = "";
        numCuenta = "";
        numCheque = "";
        mostarFormaPago = false;
        mostarObservacion = false;
        //listaProductos = new ArrayList<>();
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = comerServicio.obtenerComercializadorasActivas();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda(1);
        }
    }

    public void obtenerTerminales() {
        listaTermianles = new ArrayList<>();
        listaTermianles = termServicio.obtenerTerminal();
    }

    public void obtenerClientes() {
        listaClientes = new ArrayList<>();
        listaClientes = clienteServicio.obtenerClientes();
    }

    public void obtenerBanco() {
        listaBancos = new ArrayList<>();
        listaBancos = bancoServicio.obtenerBanco();
    }

    public void obtenerMedida() {
        listaMedida = new ArrayList<>();
        listaMedida = medidaServicio.obtenerMedida();
    }

    public void obtenerSellosParaNP(String codigoComercializadora, String codigoTerminal, Integer selloInicial, Integer selloFinal) {
        listaDetalleTerminalSellos = new ArrayList<>();
        listaDetalleTerminalSellos = sellosServicio.sellosValidosParaNP(codigoComercializadora, codigoTerminal, selloInicial, selloFinal);
    }
    
    public void obtenerAutotanque() {
        try {
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.autotanque");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaAutotanque = new ArrayList<>();
            autotanque = new Autotanque();
            conductor = new Conductor();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject autotanqueJson = new JSONObject(respuesta);
            JSONArray retorno = autotanqueJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject autoT = retorno.getJSONObject(indice);
                JSONObject cond = autoT.getJSONObject("cedularuc");

                autotanque.setPlaca(autoT.getString("placa"));
                autotanque.setVolumentotal(autoT.getBigDecimal("volumentotal"));
                autotanque.setCompartimento1(autoT.getBigDecimal("compartimento1"));
                autotanque.setCompartimento2(autoT.getBigDecimal("compartimento2"));
                autotanque.setCompartimento3(autoT.getBigDecimal("compartimento3"));
                autotanque.setCompartimento4(autoT.getBigDecimal("compartimento4"));
                autotanque.setCompartimento5(autoT.getBigDecimal("compartimento5"));
                autotanque.setCompartimento6(autoT.getBigDecimal("compartimento6"));
                autotanque.setCompartimento7(autoT.getBigDecimal("compartimento7"));
                autotanque.setCompartimento8(autoT.getBigDecimal("compartimento8"));
                autotanque.setCompartimento9(autoT.getBigDecimal("compartimento9"));
                autotanque.setCompartimento10(autoT.getBigDecimal("compartimento10"));
                autotanque.setActivo(autoT.getBoolean("activo"));
                autotanque.setUsuarioactual(autoT.getString("usuarioactual"));
                conductor.setCedularuc(cond.getString("cedularuc"));
                conductor.setNombre(cond.getString("nombre"));
                conductor.setActivo(cond.getBoolean("activo"));
                autotanque.setCedularuc(conductor);
                listaAutotanque.add(autotanque);
                autotanque = new Autotanque();
                conductor = new Conductor();
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerConductores() {
        try {
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.conductor");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaConductores = new ArrayList<>();
            conductor = new Conductor();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject conductorJson = new JSONObject(respuesta);
            JSONArray retorno = conductorJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject autoT = retorno.getJSONObject(indice);
                conductor.setCedularuc(autoT.getString("cedularuc"));
                conductor.setNombre(autoT.getString("nombre"));
                conductor.setActivo(autoT.getBoolean("activo"));
                conductor.setUsuarioactual(autoT.getString("usuarioactual"));
                if (conductor.getActivo()) {
                    listaConductores.add(conductor);
                }
                conductor = new Conductor();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void seleccionarComerc(int busqueda) {
        if (comercializadora != null) {
            codComer = comercializadora.getCodigo();
            codAbas = comercializadora.getAbastecedora();
            listaClientes = new ArrayList<>();
            if (busqueda == 1) {
                listaClientes = clienteServicio.obtenerClientesPorComercializadora(codComer);
            } else {
                listaClientes = clienteServicio.obtenerClientesPorComercializadoraActiva(codComer);
            }
        }
    }

    public void seleccionarComercializdora() {
        if (comercializadora != null) {
            if (comercializadora.getActivo().equals("S")) {
                prefijo = comercializadora.getPrefijoNpe();
                codComer = comercializadora.getCodigo();
                codAbas = comercializadora.getAbastecedora();
                abas.setCodigo(codAbas);
                comerc.setCodigo(codComer);
                npPK.setCodigoabastecedora(codAbas);
                npPK.setCodigocomercializadora(codComer);
                np.setNotapedidoPK(npPK);
                np.setAbastecedora(abas);
                np.setComercializadora(comerc);
                detNPK.setCodigoabastecedora(codAbas);
                detNPK.setCodigocomercializadora(codComer);
                detNP.setDetallenotapedidoPK(detNPK);
                if (habilitarComer) {
                    listaClientes = new ArrayList<>();
                    listaClientes = clienteServicio.obtenerClientesPorComercializadoraActiva(codComer);
                }
//                listaClientes = new ArrayList<>();
//                listaClientes = clienteServicio.obtenerClientesPorComercializadora(codComer);
//                if (dataUser.getUser().getNiveloperacion().equals("usac")) {
//                    for (int i = 0; i < listaClientes.size(); i++) {
//                        if (listaClientes.get(i).getCodigo().equals(dataUser.getUser().getCodigocliente())) {
//                            this.cliente = listaClientes.get(i);
//                        }
//                    }
//                }
                if (codComer.equals(CodigoComerEnum.PETROL_RIOS.getCodigo())) {
                    mostarFormaPago = true;
                    mostarObservacion = false;
                } else {
                    mostarFormaPago = false;
                    mostarObservacion = true;
                }
            } else {
                mostarFormaPago = false;
                mostarObservacion = false;
                listaClientes = new ArrayList<>();
                this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA SE ENCUENTRA INACTIVA");
            }
        }
    }

    public void seleccionarTerminal(int busqueda) {
        if (terminal != null) {
            codTerminal = terminal.getCodigo();
            List<Cliente> listaClientesAux = new ArrayList<>();
            listaClientes = new ArrayList<>();
            if (busqueda == 1) {
                listaClientesAux = clienteServicio.obtenerClientesPorComercializadora(codComer);
            } else {
                listaClientesAux = clienteServicio.obtenerClientesPorComercializadoraActiva(codComer);
                todosClientes = false;
            }
            for (int i = 0; i < listaClientesAux.size(); i++) {
                if (listaClientesAux.get(i).getCodigoterminaldefecto().getCodigo().equals(codTerminal)) {
                    listaClientes.add(listaClientesAux.get(i));
                }
            }
        } else {
            listaClientes = new ArrayList<>();
            if (busqueda == 1) {
                listaClientes = clienteServicio.obtenerClientesPorComercializadora(codComer);
            } else {
                listaClientes = clienteServicio.obtenerClientesPorComercializadoraActiva(codComer);
            }
        }
    }

    public void seleccCliente(int busqueda) {
        if (cliente != null) {
            codCliente = cliente.getCodigo();
            for (int i = 0; i < listaTermianles.size(); i++) {
                if (listaTermianles.get(i).getCodigo().equals(cliente.getCodigoterminaldefecto().getCodigo())) {
                    terminal = listaTermianles.get(i);
                    break;
                }
            }
            seleccionarTerminal(busqueda);
        }

    }

    public void seleccionarCliente() {
        if (cliente != null) {
            codCliente = cliente.getCodigo();
            listaProductos = new ArrayList<>();
            listaProductos = cliProdServicio.obtenerProductos(codCliente);
            np.setCodigocliente(cliente);
            if (cliente.getCodigoterminaldefecto() != null) {
//                for (int i = 0; i < listaTermianles.size(); i++) {
//                    if (cliente.getCodigoterminaldefecto().equals(listaTermianles.get(i).getCodigo())) {
//                        terminal = listaTermianles.get(i);
                //codTerminal = cliente.getCodigoterminaldefecto().getCodigo() + " - " + cliente.getCodigoterminaldefecto().getNombre();
//                    }
                //np.setCodigoterminal(cliente.getCodigoterminaldefecto());
//                }
            }

            if (cliente.getCodigobancodebito() != null) {
                for (int i = 0; i < listaBancos.size(); i++) {
                    if (listaBancos.get(i).getCodigo().equals(cliente.getCodigobancodebito())) {
                        banco = listaBancos.get(i);
                        np.setCodigobanco(banco);
                    }
                }
            }
        }
    }

    public void seleccionarMedida() {
        if (medida != null) {
            detNPK.setCodigomedida(medida.getCodigo());
            detNP.setDetallenotapedidoPK(detNPK);
            detNP.setMedida(medida);
        }
    }

    public void seleccionarBanco() {
        if (banco != null) {
            np.setCodigobanco(banco);
        }
    }

    public void seleccionarProducto() {
        if (productoSeleccionado != null) {
            detNPK.setCodigoproducto(productoSeleccionado.getCodigo());
            detNP.setDetallenotapedidoPK(detNPK);
            detNP.setProducto(productoSeleccionado);
        }
    }

    public void habilitarBusqueda(int busqueda) {
        if (dataUser.getUser() != null) {
            switch (dataUser.getUser().getNiveloperacion()) {
                case "cero":
                    habilitarComer = true;
                    habilitarCli = true;
                    habilitarTerminal = true;
                    terminal = new Terminal();
                    break;
                case "adco":
                    habilitarComer = false;
                    habilitarCli = true;
                    habilitarTerminal = true;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                            this.comercializadora = listaComercializadora.get(i);
                        }
                    }
                    seleccionarComerc(busqueda);
                    //listaClientes = clienteServicio.obtenerClientesPorComercializadora(comercializadora.getCodigo());
                    break;
                case "usac":
                    habilitarComer = false;
                    habilitarCli = false;
                    habilitarTerminal = false;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                            this.comercializadora = listaComercializadora.get(i);
                        }

                    }
                    if (comercializadora.getCodigo() != null) {
                        seleccionarComerc(busqueda);
                        listaClientes = clienteServicio.obtenerClientesPorComercializadoraActiva(comercializadora.getCodigo());
                        for (int i = 0; i < listaClientes.size(); i++) {
                            if (listaClientes.get(i).getCodigo().equals(dataUser.getUser().getCodigocliente())) {
                                this.cliente = listaClientes.get(i);
                                break;
                            }
                        }
                    } else {
                        this.dialogo(FacesMessage.SEVERITY_FATAL, "La comercializadora se encuentra deshabilitada");
                    }
                    seleccionarCliente();

                    for (int i = 0; i < listaTermianles.size(); i++) {
                        if (listaTermianles.get(i).getCodigo().equals(cliente.getCodigoterminaldefecto().getCodigo())) {
                            terminal = listaTermianles.get(i);
                            break;
                        }
                    }
                    seleccionarTerminal(busqueda);
                    //listaClientes = clienteServicio.obtenerClientesPorComercializadora(comercializadora.getCodigo());

                    //seleccionarCliente();
                    break;
                case "agco":
                    habilitarComer = false;
                    habilitarCli = true;
                    habilitarTerminal = true;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                            comercializadora = listaComercializadora.get(i);
                            break;
                        }
                    }
                    seleccionarComerc(busqueda);
                    for (int i = 0; i < listaTermianles.size(); i++) {
                        if (listaTermianles.get(i).getCodigo().equals(dataUser.getUser().getCodigoterminal())) {
                            terminal = listaTermianles.get(i);
                            break;
                        }
                    }
                    seleccionarTerminal(busqueda);
//                    List<Cliente> listaClientesAux = new ArrayList<>();
//                    listaClientesAux = clienteServicio.obtenerClientesPorComercializadora(codComer);
//                    listaClientes = new ArrayList<>();
//                    for (int i = 0; i < listaClientesAux.size(); i++) {
//                        if (listaClientesAux.get(i).getCodigoterminaldefecto().getCodigo().equals(codTerminal)) {
//                            listaClientes.add(listaClientesAux.get(i));
//                        }
//                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void obtenerNotasPedido() throws ParseException {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            /*fechas para hacer la consulta*/
            String fechaI = date.format(fechaInicial);
            String fechaF = date.format(fechaFinal);
            /*fechas para comparar entre las dos y establecer un rango de 7 dias*/
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            String dateI = sdf.format(fechaInicial);
            String dateF = sdf.format(fechaFinal);

            Date firstDate = sdf.parse(dateI);
            Date secondDate = sdf.parse(dateF);

            long diff = secondDate.getTime() - firstDate.getTime();
            TimeUnit time = TimeUnit.DAYS;
            long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
            if (diffrence > 7) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "LA FECHA DE FIN NO PUEDE SER MAYOR A 7 DÍAS A LA FECHA DE INICIO");
            } else {
                //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/Comerterminal?";
                String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido/Comerterminal?";
                if (codCliente.isEmpty()) {
                    url = new URL(direcc + "codigoabastecedora=" + codAbas + "&codigocomercializadora=" + codComer + "&codigoterminal=" + codTerminal
                            + "&fechaI=" + fechaI + "&fechaF=" + fechaF + "&tipofecha=" + tipoFecha + "&codigocliente=-1");
                } else {
                    url = new URL(direcc + "codigoabastecedora=" + codAbas + "&codigocomercializadora=" + codComer + "&codigoterminal=" + codTerminal
                            + "&fechaI=" + fechaI + "&fechaF=" + fechaF + "&tipofecha=" + tipoFecha + "&codigocliente=" + this.codCliente);
                }
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                listenvNP = new ArrayList<>();
                listDetNP = new ArrayList<>();
                cliente = new Cliente();
                EnvioPedido envioPedido = new EnvioPedido();
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());

                BufferedReader br = new BufferedReader(reader);
                String tmp = null;
                String respuesta = "";
                while ((tmp = br.readLine()) != null) {
                    respuesta += tmp;
                }
                JSONObject objetoJson = new JSONObject(respuesta);
                JSONArray retorno = objetoJson.getJSONArray("retorno");
                if (retorno.isEmpty()) {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCONTRARON NOTAS DE PEDIDO");
                } else {

                    for (int indice = 0; indice < retorno.length(); indice++) {
                        if (!retorno.isNull(indice)) {
                            JSONObject nt = retorno.getJSONObject(indice);
                            JSONObject ntPK = nt.getJSONObject("notapedidoPK");
                            JSONObject cli = nt.getJSONObject("codigocliente");
                            JSONObject term = nt.getJSONObject("codigoterminal");
                            JSONObject ban = nt.getJSONObject("codigobanco");
                            JSONObject formPago = cli.getJSONObject("codigoformapago");
                            JSONObject com = nt.getJSONObject("comercializadora");
                            JSONObject abastecedora = nt.getJSONObject("abastecedora");

                            /*----Varaibles para transformar formate json en fechas-----*/
                            Long dateStrFV = nt.getLong("fechaventa");
                            Long dateStrFD = nt.getLong("fechadespacho");
                            Date dateFV = new Date(dateStrFV);
                            Date dateFD = new Date(dateStrFD);
                            String fechaDescpacho = date.format(dateFD);
                            String fechaVencimiento = date.format(dateFV);
                            /*----Objeto Abastecedora----*/
                            abas.setCodigo(abastecedora.getString("codigo"));

                            /*----Objeto comercializadora----*/
                            comerc.setCodigo(com.getString("codigo"));
                            comerc.setNombre(com.getString("nombre"));
                            comerc.setRuc(com.getString("ruc"));
                            comerc.setDireccion(com.getString("direccion"));
                            comerc.setAmbientesri(com.getString("ambientesri").charAt(0));
                            comerc.setEsagenteretencion(com.getBoolean("esagenteretencion"));
                            comerc.setEscontribuyenteespacial(com.getString("escontribuyenteespacial"));
                            comerc.setTipoemision(com.getString("tipoemision").charAt(0));
                            comerc.setObligadocontabilidad(com.getString("obligadocontabilidad"));
                            comerc.setEstablecimientofac(com.getString("establecimientofac"));
                            comerc.setPuntoventafac(com.getString("puntoventafac"));
                            comerc.setClavewsepp(com.getString("clavewsepp"));

                            /*----Objeto Fromapago----*/
                            formap.setCodigo(formPago.getString("codigo"));

                            /*----Objeto Cliente----*/
                            cliente.setCodigo(cli.getString("codigo"));
                            cliente.setNombre(cli.getString("nombre"));
                            cliente.setNombrecomercial(cli.getString("nombrecomercial"));
                            cliente.setRuc(cli.getString("ruc"));
                            cliente.setCorreo1(cli.getString("correo1"));
                            cliente.setTelefono1(cli.getString("telefono1"));
                            cliente.setDireccion(cli.getString("direccion"));
                            if (!cli.isNull("tipoplazocredito")) {
                                cliente.setTipoplazocredito(cli.getString("tipoplazocredito"));
                            }
                            cliente.setCodigolistaprecio(cli.getLong("codigolistaprecio"));
                            cliente.setCodigoformapago(formap);

                            /*----Objeto Terminal----*/
                            terminalT.setCodigo(term.getString("codigo"));

                            /*----Objeto Banco----*/
                            banco.setCodigo(ban.getString("codigo"));

                            /*----Guardando el cliente, termina y banco en Nota pedido---*/
                            np.setCodigocliente(cliente);
                            np.setCodigoterminal(terminalT);
                            np.setCodigobanco(banco);
                            np.setComercializadora(comerc);
                            np.setAbastecedora(abas);

                            np.setNumerofacturasri(nt.getString("numerofacturasri"));
                            np.setActiva(nt.getBoolean("activa"));
                            npPK.setNumero(ntPK.getString("numero"));
                            npPK.setCodigoabastecedora(ntPK.getString("codigoabastecedora"));
                            npPK.setCodigocomercializadora(ntPK.getString("codigocomercializadora"));
                            np.setFechaventa(fechaVencimiento);
                            np.setFechadespacho(fechaDescpacho);
                            if (!nt.isNull("codigoautotanque")) {
                                np.setCodigoautotanque(nt.getString("codigoautotanque"));
                            }
                            if (!nt.isNull("cedulaconductor")) {
                                np.setCedulaconductor(nt.getString("cedulaconductor"));
                            }
                            np.setNotapedidoPK(npPK);
                            envioPedido.setNotapedido(np);
                            listenvNP.add(envioPedido);
                            envioPedido = new EnvioPedido();
                            np = new Notapedido();
                            npPK = new NotapedidoPK();
                            listDetNP = new ArrayList<>();
                            cliente = new Cliente();
                            banco = new Banco();
                            comerc = new Comercializadora();
                            terminalT = new Terminal();
                            abas = new Abastecedora();
                            formap = new Formapago();
                        }
                    }
                }
                if (connection.getResponseCode() != 200) {
                    System.out.println(connection.getResponseCode());
                    System.out.println(connection.getResponseMessage());
                }
            }
            //habilitarBusqueda();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() throws ParseException {
        System.out.println("FT:: NOTAPEDIDOBEAN1 - INICIA EL GUARDADO DE NP save()");
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fechV = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat fechD = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
        Calendar c = Calendar.getInstance();
        c.setTime(fechaVenta);
        //c.add(Calendar.D, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        String dateV = sdf.format(fechaVenta);
        String dateD = sdf.format(fechaDespacho);

//SimpleDateFormat fechaS = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
//SimpleDateFormat fechP = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
//String dateP = fechaS.format(new Date());        
//fechP.parse(dateP);
        Date firstDate = sdf.parse(dateV);
        Date secondDate = sdf.parse(dateD);

        long diff = secondDate.getTime() - firstDate.getTime();
        TimeUnit time = TimeUnit.DAYS;
        long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);

        System.out.println("The difference in days is : " + diffrence);
        if (comercializadora.getActivo().equals("S")) {
            if (diffrence > 7) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "LA FECHA DE DESPACHO NO PUEDE SER MAYOR A 7 DÍAS A LA FECHA DE VENTA");
            } else {
                npPK.setNumero("");
                np.setNotapedidoPK(npPK);
                np.setCodigoterminal(terminal);
                np.setActiva(true);
                np.setFechaventa(fechV.format(fechaVenta));
                np.setFechadespacho(fechD.format(fechaDespacho));
                np.setCodigoautotanque("");
                np.setCedulaconductor("");
                np.setNumerofacturasri("0");
                np.setRespuestageneracionoeepp("");
                np.setAdelantar(false);
                np.setRespuestaanulacionoeepp("");
                np.setTramaenviadagoe("");
                np.setTramarecibidagoe("");
                np.setTramarenviadaaoe("");
                np.setTramarecibidaaoe("");
                np.setUsuarioactual(dataUser.getUser().getNombrever());
                np.setPrefijo(prefijo);
                if (codComer.equals(CodigoComerEnum.PETROL_RIOS.getCodigo())) {
                     np.setObservacion("Bco: " + nomBanco + " - Cta: " + numCuenta + " - Ch: " + numCheque);
                }
                np.setCodigoautotanque(autotanque.getPlaca());
                np.setCedulaconductor(autotanque.getCedularuc().getCedularuc());
                //}

                detNPK.setNumero("");

                detNP.setVolumennaturalrequerido(volTotal);
                detNP.setDetallenotapedidoPK(detNPK);
                detNP.setVolumennaturalautorizado(detNP.getVolumennaturalrequerido());
                detNP.setUsuarioactual(dataUser.getUser().getNombrever());
                detNP.setCompartimento1(compartimento1);
                detNP.setCompartimento2(compartimento2);
                detNP.setCompartimento3(compartimento3);
                detNP.setCompartimento4(compartimento4);
                detNP.setCompartimento5(compartimento5);
                detNP.setCompartimento6(compartimento6);
                detNP.setCompartimento7(compartimento7);
                detNP.setCompartimento8(compartimento8);
                detNP.setCompartimento9(compartimento9);
                detNP.setCompartimento10(compartimento10);
//                detNP.setSelloinicial(selloinicial);
//                detNP.setSellofinal(sellofinal);

                envNP.setNotapedido(np);
                envNP.setDetalle(detNP);
                if (editarNotapedido) {
                    //editItems(envNP);
                } else {
                    addItems(envNP);
                }
            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA SE ENCUENTRA INACTIVA");
        }
    }

    public void addItems(EnvioPedido envNP) {
        try {
            String respuesta = "";
            //String trama = "";
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido";
            url = new URL(direcc);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();

            String jsonStr = mapper.writeValueAsString(envNP);

            System.out.println("FT:: addItems OBJETO NOTAPEDIDO " + jsonStr);

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String resp = "";
            while ((tmp = br.readLine()) != null) {
                resp += tmp;
            }
            JSONObject objetoJson = new JSONObject(resp);
            numeroNotaPedio = objetoJson.getString("developerMessage");

            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "NOTA DE PEDIDO REGISTRADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println("FT:: ERROR EN addItems RESPONSECODE " + connection.getResponseCode());
                System.out.println("FT:: ERROR EN addItems RESPONSEMESSAGE " + connection.getResponseMessage());
            }

        } catch (IOException e) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR NOTA DE PEDIDO: " + e);
            e.printStackTrace();
        }
    }

    public void dialogoAnulacionNotaPedido(EnvioPedido envNP) {
        try {
            notaPedidoAuxiliar = new Notapedido();
            notaPedidoAuxiliarPK = new NotapedidoPK();
            notaPedidoAuxiliar = envNP.getNotapedido();
            if (!notaPedidoAuxiliar.isActiva()) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE PUEDE ANULAR ESTA NOTA DE PEDIDO, PORQUE YA SE ENCUENTRA ANULADA");
            } else {
                if (notaPedidoAuxiliar.getNumerofacturasri().trim().equals("0")) {
                    if (notaPedidoAuxiliar != null) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        SimpleDateFormat formatoJS = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
                        /*-----------------------Fecha descpaho---------------------------------------------*/
                        Date dateD = format.parse(notaPedidoAuxiliar.getFechadespacho());
                        String fechaD = formatoJS.format(dateD);
                        /*-----------------------Fecha Venta------------------------------------------------*/
                        Date dateV = format.parse(notaPedidoAuxiliar.getFechaventa());
                        String fechaV = formatoJS.format(dateV);
                        notaPedidoAuxiliar.setFechadespacho(fechaD);
                        notaPedidoAuxiliar.setFechaventa(fechaV);
                        notaPedidoAuxiliar.setActiva(false);
                        consultaNotaPedidoPorId(envNP);

                    }
                } else {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE PUEDE ANULAR ESTA NOTA DE PEDIDO, PORQUE YA SE ENCUENTRA FACTURADA");
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(NotapedidoBean1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultaNotaPedidoPorId(EnvioPedido envNP) {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            cliente = new Cliente();

            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/porId?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido/porId?";
            url = new URL(direcc + "codigoabastecedora=" + envNP.getNotapedido().getNotapedidoPK().getCodigoabastecedora()
                    + "&codigocomercializadora=" + envNP.getNotapedido().getNotapedidoPK().getCodigocomercializadora()
                    + "&numero=" + envNP.getNotapedido().getNotapedidoPK().getNumero());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
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
            if (retorno.isEmpty()) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL OBTENER INFORMACIÓN SOBRE LA NOTA DE PEDIDO PARA LA ANULACIÓN");
            } else {

                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject nt = retorno.getJSONObject(indice);
                        JSONObject ntPK = nt.getJSONObject("notapedidoPK");
                        JSONObject cli = nt.getJSONObject("codigocliente");
                        JSONObject term = nt.getJSONObject("codigoterminal");
                        JSONObject ban = nt.getJSONObject("codigobanco");
                        JSONObject formPago = cli.getJSONObject("codigoformapago");
                        JSONObject com = nt.getJSONObject("comercializadora");
                        JSONObject abastecedora = nt.getJSONObject("abastecedora");

                        /*----Objeto Abastecedora----*/
                        abas.setCodigo(abastecedora.getString("codigo"));

                        /*----Objeto comercializadora----*/
                        comerc.setCodigo(com.getString("codigo"));
                        comerc.setNombre(com.getString("nombre"));
                        comerc.setRuc(com.getString("ruc"));
                        comerc.setDireccion(com.getString("direccion"));
                        comerc.setAmbientesri(com.getString("ambientesri").charAt(0));
                        comerc.setEsagenteretencion(com.getBoolean("esagenteretencion"));
                        comerc.setEscontribuyenteespacial(com.getString("escontribuyenteespacial"));
                        comerc.setTipoemision(com.getString("tipoemision").charAt(0));
                        comerc.setObligadocontabilidad(com.getString("obligadocontabilidad"));
                        comerc.setEstablecimientofac(com.getString("establecimientofac"));
                        comerc.setPuntoventafac(com.getString("puntoventafac"));
                        comerc.setClavewsepp(com.getString("clavewsepp"));

                        /*----Objeto Fromapago----*/
                        formap.setCodigo(formPago.getString("codigo"));

                        /*----Objeto Cliente----*/
                        cliente.setCodigo(cli.getString("codigo"));
                        cliente.setNombre(cli.getString("nombre"));
                        cliente.setRuc(cli.getString("ruc"));
                        cliente.setCorreo1(cli.getString("correo1"));
                        cliente.setTelefono1(cli.getString("telefono1"));
                        cliente.setDireccion(cli.getString("direccion"));
                        if (!cli.isNull("tipoplazocredito")) {
                            cliente.setTipoplazocredito(cli.getString("tipoplazocredito"));
                        }
                        cliente.setCodigolistaprecio(cli.getLong("codigolistaprecio"));
                        cliente.setCodigoformapago(formap);

                        /*----Objeto Terminal----*/
                        terminalT.setCodigo(term.getString("codigo"));

                        /*----Objeto Banco----*/
                        banco.setCodigo(ban.getString("codigo"));

                        /*----Guardando el cliente, termina y banco en Nota pedido---*/
                        notaPedidoAuxiliar.setCodigocliente(cliente);
                        notaPedidoAuxiliar.setCodigoterminal(terminalT);
                        notaPedidoAuxiliar.setCodigobanco(banco);
                        notaPedidoAuxiliar.setComercializadora(comerc);
                        notaPedidoAuxiliar.setAbastecedora(abas);
                        if (!nt.isNull("codigoautotanque")) {
                            notaPedidoAuxiliar.setCodigoautotanque(nt.getString("codigoautotanque"));
                        } else {
                            notaPedidoAuxiliar.setCodigoautotanque("");
                        }
                        if (!nt.isNull("cedulaconductor")) {
                            notaPedidoAuxiliar.setCedulaconductor(nt.getString("cedulaconductor"));
                        } else {
                            notaPedidoAuxiliar.setCedulaconductor("");
                        }
                        if (!nt.isNull("respuestageneracionoeepp")) {
                            notaPedidoAuxiliar.setRespuestageneracionoeepp(nt.getString("respuestageneracionoeepp"));
                        } else {
                            notaPedidoAuxiliar.setRespuestageneracionoeepp("");
                        }
                        if (!nt.isNull("observacion")) {
                            notaPedidoAuxiliar.setObservacion(nt.getString("observacion"));
                        } else {
                            notaPedidoAuxiliar.setObservacion("");
                        }
                        if (!nt.isNull("adelantar")) {
                            notaPedidoAuxiliar.setAdelantar(nt.getBoolean("adelantar"));
                        } else {
                            notaPedidoAuxiliar.setAdelantar(true);
                        }
                        if (!nt.isNull("respuestaanulacionoeepp")) {
                            notaPedidoAuxiliar.setRespuestaanulacionoeepp(nt.getString("respuestaanulacionoeepp"));
                        } else {
                            notaPedidoAuxiliar.setRespuestaanulacionoeepp("");
                        }
                        if (!nt.isNull("tramaenviadagoe")) {
                            notaPedidoAuxiliar.setTramaenviadagoe(nt.getString("tramaenviadagoe"));
                        } else {
                            notaPedidoAuxiliar.setTramaenviadagoe("");
                        }
                        if (!nt.isNull("tramarecibidagoe")) {
                            notaPedidoAuxiliar.setTramarecibidagoe(nt.getString("tramarecibidagoe"));
                        } else {
                            notaPedidoAuxiliar.setTramarecibidagoe("");
                        }
                        if (!nt.isNull("tramarecibidaaoe")) {
                            notaPedidoAuxiliar.setTramarecibidaaoe(nt.getString("tramarecibidaaoe"));
                        } else {
                            notaPedidoAuxiliar.setTramarecibidaaoe("");
                        }
                        if (!nt.isNull("tramarecibidaaoe")) {
                            notaPedidoAuxiliar.setTramarenviadaaoe(nt.getString("tramarenviadaaoe"));
                        } else {
                            notaPedidoAuxiliar.setTramarenviadaaoe("");
                        }

                        notaPedidoAuxiliar.setUsuarioactual(nt.getString("usuarioactual"));

                        if (!nt.isNull("prefijo")) {
                            notaPedidoAuxiliar.setPrefijo(nt.getString("prefijo"));
                        } else {
                            notaPedidoAuxiliar.setPrefijo("");
                        }
                        notaPedidoAuxiliar.setProcesar(nt.getBoolean("procesar"));

                        notaPedidoAuxiliar.setNumerofacturasri(nt.getString("numerofacturasri"));
                        notaPedidoAuxiliarPK.setNumero(ntPK.getString("numero"));
                        notaPedidoAuxiliarPK.setCodigoabastecedora(ntPK.getString("codigoabastecedora"));
                        notaPedidoAuxiliarPK.setCodigocomercializadora(ntPK.getString("codigocomercializadora"));
                        notaPedidoAuxiliar.setNotapedidoPK(notaPedidoAuxiliarPK);
                        notaPedidoAuxiliarPK = new NotapedidoPK();
                        listDetNP = new ArrayList<>();
                        cliente = new Cliente();
                        banco = new Banco();
                        comerc = new Comercializadora();
                        terminalT = new Terminal();
                        abas = new Abastecedora();
                        formap = new Formapago();
                    }
                }
            }
            if (connection.getResponseCode() >= 200 || connection.getResponseCode() <= 200) {
                PrimeFaces.current().executeScript("PF('deleteProductDialog').show()");
            } else {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void anularNotaPedido() {
        try {
            String respuesta = "";
            //String trama = "";
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/porId";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido/porId";
            url = new URL(direcc);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(notaPedidoAuxiliar);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();

            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "NOTA DE PEDIDO ANULADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ANULAR NOTA PEDIDO");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generarReporte(EnvioPedido envP) {
//        String path = "C:\\archivos\\Template\\notapedido.jrxml";
        String rutaGuardar = Fichero.getCARPETAREPORTES();
        String path = Fichero.getCARPETAREPORTES() + "/notapedidoext.jrxml";
        System.out.println("PATH:" + path);
        InputStream file = null;
        try {
            file = new FileInputStream(new File(path));

            JasperReport reporte = JasperCompileManager.compileReport(file);
            BufferedImage image = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/logo.jpeg"));
//            BufferedImage image = ImageIO.read(new File("C:\\archivos\\Template\\logo.jpg"));
            Map parametro = new HashMap();

            parametro.put("codComer", envP.getNotapedido().getNotapedidoPK().getCodigocomercializadora());
            parametro.put("numeroNotaPedido", envP.getNotapedido().getNotapedidoPK().getNumero());
            parametro.put("logo", image);

            //System.out.println("PARAMETROS: " + parametro);
            Connection conexion = conexionJasperBD();

            //System.out.println("CONEXIÓN: " + conexion);
            JasperPrint print = JasperFillManager.fillReport(reporte, parametro, conexion);

//            File directory = new File("C:\\Archivos");
            File directory = new File(rutaGuardar);
            String nombreDocumento = "reporteNotaPedido";

            File pdf = File.createTempFile(nombreDocumento + "_", ".pdf", directory);
            JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
            File initialFile = new File(pdf.getAbsolutePath());
            InputStream targetStream = new FileInputStream(initialFile);
            //pdfStream = new DefaultStreamedContent();
            pdfStream = new DefaultStreamedContent(targetStream, "application/pdf", nombreDocumento + envP.getNotapedido().getNotapedidoPK().getNumero() + ".pdf");
            //DefaultStreamedContent.builder().contentType("application/pdf").name(nombreDocumento + ".pdf").stream(() -> new FileInputStream(targetStream)).build();
            System.err.print(pdf.getAbsolutePath());
            System.out.println(pdf.getAbsolutePath());
        } catch (Exception ex) {
            //ex.printStackTrace();
            System.out.println("Excepcion: " + ex);
        }
    }

    public void generarReporteAux(EnvioPedido envP) {
//        String path = "C:\\archivos\\Template\\FormatoNotaPedido.jrxml";
//        String subreport = "C:\\archivos\\Template\\notapedido.jrxml";
        String rutaGuardar = Fichero.getCARPETAREPORTES();
        String subreport = Fichero.getCARPETAREPORTES() + "/notapedidoext.jrxml";
        String path = Fichero.getCARPETAREPORTES() + "/FormatoNotaPedido.jrxml";
        System.out.println("PATH:" + path);
        InputStream file = null;
        try {
            file = new FileInputStream(new File(path));

            JasperReport reporte = JasperCompileManager.compileReport(file);
            JasperReport subreporte = JasperCompileManager.compileReport(subreport);
            BufferedImage image = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/logo.jpeg"));
//            BufferedImage image = ImageIO.read(new File("C:\\archivos\\Template\\logo.jpg"));
            Map parametro = new HashMap();

            parametro.put("subReporte", subreporte);
            parametro.put("codComer", envP.getNotapedido().getNotapedidoPK().getCodigocomercializadora());
            parametro.put("numeroNotaPedido", envP.getNotapedido().getNotapedidoPK().getNumero());
            parametro.put("logo", image);

            //System.out.println("PARAMETROS: " + parametro);
            Connection conexion = conexionJasperBD();

            //System.out.println("CONEXIÓN: " + conexion);
            JasperPrint print = JasperFillManager.fillReport(reporte, parametro, conexion);

//            File directory = new File("C:\\Archivos");
            File directory = new File(rutaGuardar);
            String nombreDocumento = "reporteNotaPedido";

            File pdf = File.createTempFile(nombreDocumento + "_", ".pdf", directory);
            JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
            File initialFile = new File(pdf.getAbsolutePath());
            InputStream targetStream = new FileInputStream(initialFile);
            //pdfStream = new DefaultStreamedContent();
            pdfStream = new DefaultStreamedContent(targetStream, "application/pdf", nombreDocumento + envP.getNotapedido().getNotapedidoPK().getNumero() + ".pdf");
            //DefaultStreamedContent.builder().contentType("application/pdf").name(nombreDocumento + ".pdf").stream(() -> new FileInputStream(targetStream)).build();
            System.err.print(pdf.getAbsolutePath());
            System.out.println(pdf.getAbsolutePath());
        } catch (Exception ex) {
            //ex.printStackTrace();
            System.out.println("Excepcion: " + ex);
        }
    }

    public void todosCli() {
        if (todosClientes) {
            listaClientes = new ArrayList<>();
            listaClientes = clienteServicio.obtenerClientesPorComercializadoraActiva(codComer);
        } else {
            seleccionarTerminal(2);
        }
    }

    public void cambiarConductor() {
        PrimeFaces.current().executeScript("PF('elegirconductor').show()");
    }

    public void calcularVol() {
        volTotal = new BigDecimal("0.00");
        if (autotanqueAux.getCompartimento1() != null) {
            if (!autotanqueAux.getCompartimento1().equals(new BigDecimal("-1.0")) && !autotanqueAux.getCompartimento1().equals(new BigDecimal("-1")) && !autotanqueAux.getCompartimento1().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanqueAux.getCompartimento1());
                compartimento1 = autotanqueAux.getCompartimento1();
            }
            if (!autotanqueAux.getCompartimento2().equals(new BigDecimal("-1.0")) && !autotanqueAux.getCompartimento2().equals(new BigDecimal("-1")) && !autotanqueAux.getCompartimento2().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanqueAux.getCompartimento2());
                compartimento2 = autotanqueAux.getCompartimento2();
            }
            if (!autotanqueAux.getCompartimento3().equals(new BigDecimal("-1.0")) && !autotanqueAux.getCompartimento3().equals(new BigDecimal("-1")) && !autotanqueAux.getCompartimento3().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanqueAux.getCompartimento3());
                compartimento3 = autotanqueAux.getCompartimento3();
            }
            if (!autotanqueAux.getCompartimento4().equals(new BigDecimal("-1.0")) && !autotanqueAux.getCompartimento4().equals(new BigDecimal("-1")) && !autotanqueAux.getCompartimento4().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanqueAux.getCompartimento4());
                compartimento4 = autotanqueAux.getCompartimento4();
            }
            if (!autotanqueAux.getCompartimento5().equals(new BigDecimal("-1.0")) && !autotanqueAux.getCompartimento5().equals(new BigDecimal("-1")) && !autotanqueAux.getCompartimento5().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanqueAux.getCompartimento5());
                compartimento5 = autotanqueAux.getCompartimento5();
            }
            if (!autotanqueAux.getCompartimento6().equals(new BigDecimal("-1.0")) && !autotanqueAux.getCompartimento6().equals(new BigDecimal("-1")) && !autotanqueAux.getCompartimento6().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanqueAux.getCompartimento6());
                compartimento6 = autotanqueAux.getCompartimento6();
            }
            if (!autotanqueAux.getCompartimento7().equals(new BigDecimal("-1.0")) && !autotanqueAux.getCompartimento7().equals(new BigDecimal("-1")) && !autotanqueAux.getCompartimento7().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanqueAux.getCompartimento7());
                compartimento7 = autotanqueAux.getCompartimento7();
            }
            if (!autotanqueAux.getCompartimento8().equals(new BigDecimal("-1.0")) && !autotanqueAux.getCompartimento8().equals(new BigDecimal("-1")) && !autotanqueAux.getCompartimento8().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanqueAux.getCompartimento8());
                compartimento8 = autotanqueAux.getCompartimento8();
            }
            if (!autotanqueAux.getCompartimento9().equals(new BigDecimal("-1.0")) && !autotanqueAux.getCompartimento9().equals(new BigDecimal("-1")) && !autotanqueAux.getCompartimento9().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanqueAux.getCompartimento9());
                compartimento9 = autotanqueAux.getCompartimento9();
            }
            if (!autotanqueAux.getCompartimento10().equals(new BigDecimal("-1.0")) && !autotanqueAux.getCompartimento10().equals(new BigDecimal("-1")) && !autotanqueAux.getCompartimento10().equals(new BigDecimal("-1.00"))) {
                volTotal = volTotal.add(autotanqueAux.getCompartimento10());
                compartimento10 = autotanqueAux.getCompartimento10();
            }
            volTotal.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public boolean validarTanque(BigDecimal valor) {
        if (valor != null) {
            if (valor.equals(new BigDecimal("-1.0")) && !valor.equals(new BigDecimal("-1")) && !valor.equals(new BigDecimal("-1.00"))) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void actualizarAutotanque() {
        Autotanque autoAux = new Autotanque();
        autoAux = this.autotanque;
//FT 20240723 NO ACTUALIZAR EL AUTOTANQUE CON EL CONDUCTOR CAMBIADO
//        editAutotanque();
//        obtenerAutotanque();
//FT 20240723 EL AUTOTANQUE SELECCIONADO TOMA EL CONDUCTOR SELECCIONADO SOLO PARA ESTE CICLO DE VIDA DE LA PAGINA 
          this.autotanque.getCedularuc().setCedularuc(conductor.getCedularuc());  
          this.autotanque.setCedularuc(conductor);
          PrimeFaces.current().executeScript("PF('elegirconductor').hide()");

//        for (int i = 0; i < listaAutotanque.size(); i++) {
//            System.out.println("FT:: actualizarAutotanque ubicar el autotanque seleccionado anteriormente " + autoAux.getPlaca() + " -i " + i + " - " + listaAutotanque.get(i).getPlaca());
//            if (autoAux.getPlaca().equals(listaAutotanque.get(i).getPlaca())) {
//                System.out.println("FT:: actualizarAutotanque autotanque ENCONTRADO " + autoAux.getPlaca() + " -i " + i + " - " + listaAutotanque.get(i).getPlaca());
//                this.autotanque = listaAutotanque.get(i);
//                break;
//            }
//        }

    }

    public void editAutotanque() {
        try {
            String respuesta;
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.autotanque/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objCond = new JSONObject();
            obj.put("placa", autotanque.getPlaca());
            obj.put("volumentotal", autotanque.getVolumentotal());
            obj.put("compartimento1", autotanque.getCompartimento1());
            obj.put("compartimento2", autotanque.getCompartimento2());
            obj.put("compartimento3", autotanque.getCompartimento3());
            obj.put("compartimento4", autotanque.getCompartimento4());
            obj.put("compartimento5", autotanque.getCompartimento5());
            obj.put("compartimento6", autotanque.getCompartimento6());
            obj.put("compartimento7", autotanque.getCompartimento7());
            obj.put("compartimento8", autotanque.getCompartimento8());
            obj.put("compartimento9", autotanque.getCompartimento9());
            obj.put("compartimento10", autotanque.getCompartimento10());
            obj.put("activo", autotanque.isActivo());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            objCond.put("cedularuc", conductor.getCedularuc());
            obj.put("cedularuc", objCond);
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "AUTOTANQUE ACUTALIZADO EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('elegirconductor').hide()");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void calcularCantidadSellos(NotapedidoBean1 np) {
        int contadorSellosValidos = 0;
        int contadorSellosSolicitados = 0;
        if (np.detNP.getSelloinicial() != 0 && np.detNP.getSellofinal() != 0) {
            if(np.detNP.getSelloinicial() < np.detNP.getSellofinal()){
                obtenerSellosParaNP(codComer, codTerminal, np.detNP.getSelloinicial(), np.detNP.getSellofinal()); 
                
                contadorSellosSolicitados = np.detNP.getSellofinal() - np.detNP.getSelloinicial() + 1;
                
                for (int i = 0; i < listaDetalleTerminalSellos.size(); i++) {
                    if (listaDetalleTerminalSellos.get(i).getActivo()) {
                        contadorSellosValidos++;
                    }
                }
                if (contadorSellosValidos != contadorSellosSolicitados) {
                    
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "ffffffffffffffffLA FECHA DE FIN NO PUEDE SER MAYOR A 7 DÍAS A LA FECHA DE INICIO");
                    
                    this.dialogo(FacesMessage.SEVERITY_ERROR, " Metodo.calcularCantidadSellos: Usted ha solicitado " +contadorSellosSolicitados+" sellos para esta NP, sin embargo, se han encontrado "+contadorSellosValidos+" sellos validos"+"Esta divergecia de datos - NO PERMITIRÁ GRABAR ESTA NOTA DE PEDIDO!");
                    System.out.println(" Metodo.calcularCantidadSellos: Usted ha solicitado " +contadorSellosSolicitados+" sellos para esta NP, sin embargo, se han encontrado "+contadorSellosValidos+" sellos validos"+"Esta divergecia de datos - NO PERMITIRÁ GRABAR ESTA NOTA DE PEDIDO!");
                }
                this.setCantidadSellos(contadorSellosValidos);
                System.out.println("FT:: VERIFICACION DE CANTIDAD DE SELLOS Y VALIDOS:. "+this.getCantidadSellos() +" VALIDOS:. "+contadorSellosValidos);
            }else{
                this.dialogo(FacesMessage.SEVERITY_ERROR, "EL SELLO FINAL DEBE SER MAYOR AL SELLO INICIAL - NO PODRÁ GRABAR ESTA NOTA DE PEDIDO!");
            }
        }else{
            this.dialogo(FacesMessage.SEVERITY_ERROR, "LOS SELLOS DEBEN SER NUMÉRICOS - NO PODRÁ GRABAR ESTA NOTA DE PEDIDO!");
        }
    }

    public boolean isMostarNotaPedido() {
        return mostarNotaPedido;
    }

    public void setMostarNotaPedido(boolean mostarNotaPedido) {
        this.mostarNotaPedido = mostarNotaPedido;
    }

    public boolean isMostarPantallaInicial() {
        return mostarPantallaInicial;
    }

    public void setMostarPantallaInicial(boolean mostarPantallaInicial) {
        this.mostarPantallaInicial = mostarPantallaInicial;
    }

    public ComercializadoraServicio getComerServicio() {
        return comerServicio;
    }

    public void setComerServicio(ComercializadoraServicio comerServicio) {
        this.comerServicio = comerServicio;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public TerminalServicio getTermServicio() {
        return termServicio;
    }

    public void setTermServicio(TerminalServicio termServicio) {
        this.termServicio = termServicio;
    }

    public ClienteServicio getClienteServicio() {
        return clienteServicio;
    }

    public void setClienteServicio(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

    public String getCodComer() {
        return codComer;
    }

    public void setCodComer(String codComer) {
        this.codComer = codComer;
    }

    public String getCodTerminal() {
        return codTerminal;
    }

    public void setCodTerminal(String codTerminal) {
        this.codTerminal = codTerminal;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getCodAbas() {
        return codAbas;
    }

    public void setCodAbas(String codAbas) {
        this.codAbas = codAbas;
    }

    public String getTipoFecha() {
        return tipoFecha;
    }

    public void setTipoFecha(String tipoFecha) {
        this.tipoFecha = tipoFecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getOeenpetro() {
        return oeenpetro;
    }

    public void setOeenpetro(String oeenpetro) {
        this.oeenpetro = oeenpetro;
    }

    public Notapedido getNp() {
        return np;
    }

    public void setNp(Notapedido np) {
        this.np = np;
    }

    public NotapedidoPK getNpPK() {
        return npPK;
    }

    public void setNpPK(NotapedidoPK npPK) {
        this.npPK = npPK;
    }

    public Detallenotapedido getDetNP() {
        return detNP;
    }

    public void setDetNP(Detallenotapedido detNP) {
        this.detNP = detNP;
    }

    public List<Detallenotapedido> getListDetNP() {
        return listDetNP;
    }

    public void setListDetNP(List<Detallenotapedido> listDetNP) {
        this.listDetNP = listDetNP;
    }

    public DetallenotapedidoPK getDetNPK() {
        return detNPK;
    }

    public void setDetNPK(DetallenotapedidoPK detNPK) {
        this.detNPK = detNPK;
    }

    public EnvioPedido getEnvNP() {
        return envNP;
    }

    public void setEnvNP(EnvioPedido envNP) {
        this.envNP = envNP;
    }

    public List<EnvioPedido> getListenvNP() {
        return listenvNP;
    }

    public void setListenvNP(List<EnvioPedido> listenvNP) {
        this.listenvNP = listenvNP;
    }

    public Terminal getTerminalT() {
        return terminalT;
    }

    public void setTerminalT(Terminal terminalT) {
        this.terminalT = terminalT;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Comercializadora getComerc() {
        return comerc;
    }

    public void setComerc(Comercializadora comerc) {
        this.comerc = comerc;
    }

    public Abastecedora getAbas() {
        return abas;
    }

    public void setAbas(Abastecedora abas) {
        this.abas = abas;
    }

    public List<Terminal> getListaTermianles() {
        return listaTermianles;
    }

    public void setListaTermianles(List<Terminal> listaTermianles) {
        this.listaTermianles = listaTermianles;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Date getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(Date fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public List<Banco> getListaBancos() {
        return listaBancos;
    }

    public void setListaBancos(List<Banco> listaBancos) {
        this.listaBancos = listaBancos;
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

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public String getNumeroNotaPedio() {
        return numeroNotaPedio;
    }

    public void setNumeroNotaPedio(String numeroNotaPedio) {
        this.numeroNotaPedio = numeroNotaPedio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public List<Producto> getListaProd() {
        return listaProd;
    }

    public void setListaProd(List<Producto> listaProd) {
        this.listaProd = listaProd;
    }

    public boolean isEditarNotapedido() {
        return editarNotapedido;
    }

    public void setEditarNotapedido(boolean editarNotapedido) {
        this.editarNotapedido = editarNotapedido;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public Date getFechaMin() {
        return fechaMin;
    }

    public void setFechaMin(Date fechaMin) {
        this.fechaMin = fechaMin;
    }

    public StreamedContent getPdfStream() {
        return pdfStream;
    }

    public void setPdfStream(StreamedContent pdfStream) {
        this.pdfStream = pdfStream;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public boolean isTodosClientes() {
        return todosClientes;
    }

    public void setTodosClientes(boolean todosClientes) {
        this.todosClientes = todosClientes;
    }

    public List<Autotanque> getListaAutotanque() {
        return listaAutotanque;
    }

    public void setListaAutotanque(List<Autotanque> listaAutotanque) {
        this.listaAutotanque = listaAutotanque;
    }

    public Autotanque getAutotanque() {
        return autotanque;
    }

    public void setAutotanque(Autotanque autotanque) {
        this.autotanque = autotanque;
    }

    public List<Conductor> getListaConductores() {
        return listaConductores;
    }

    public void setListaConductores(List<Conductor> listaConductores) {
        this.listaConductores = listaConductores;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    }

    public BigDecimal getVolTotal() {
        return volTotal;
    }

    public void setVolTotal(BigDecimal volTotal) {
        this.volTotal = volTotal;
    }

    public Autotanque getAutotanqueAux() {
        return autotanqueAux;
    }

    public void setAutotanqueAux(Autotanque autotanqueAux) {
        this.autotanqueAux = autotanqueAux;
    }

    public Integer getCantidadSellos() {
        return cantidadSellos;
    }

    public void setCantidadSellos(Integer cantidadSellos) {
        this.cantidadSellos = cantidadSellos;
    }

    public String getNomBanco() {
        return nomBanco;
    }

    public void setNomBanco(String nomBanco) {
        this.nomBanco = nomBanco;
    }

    public String getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    public String getNumCheque() {
        return numCheque;
    }

    public void setNumCheque(String numCheque) {
        this.numCheque = numCheque;
    }

    public boolean isMostarFormaPago() {
        return mostarFormaPago;
    }

    public void setMostarFormaPago(boolean mostarFormaPago) {
        this.mostarFormaPago = mostarFormaPago;
    }

    public boolean isMostarObservacion() {
        return mostarObservacion;
    }

    public void setMostarObservacion(boolean mostarObservacion) {
        this.mostarObservacion = mostarObservacion;
    }
    
}
