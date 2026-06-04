/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.pedidosyfacturacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
import ec.com.infinityone.modelo.Detalleprepedido;
import ec.com.infinityone.modelo.DetalleprepedidoPK;
import ec.com.infinityone.modelo.PrepedidoSolicitud;
import ec.com.infinityone.modelo.Formapago;
import ec.com.infinityone.modelo.Medida;
import ec.com.infinityone.modelo.Prepedido;
import ec.com.infinityone.modelo.PrepedidoPK;
import ec.com.infinityone.modelo.Producto;
import ec.com.infinityone.modelo.Terminal;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.ClientePK;
import ec.com.infinityone.modelo.NotaPedidoSOAP;
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
import java.util.StringTokenizer;
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
@Named("prepedidoSolicitudBean")
@ViewScoped
public class PrepedidoSolicitudBean extends ReusableBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(PrepedidoSolicitudBean.class.getName());

    /*
     * Variable para renderizar la pantalla
     */
    private boolean mostarPrepedido;
    /*
     * Variable para renderizar la pantalla
     */
    private boolean mostarPantallaInicial;
    /*
     * Variable para acceder a los servicios de Comercialziadora
     */
    @Inject
    private ComercializadoraServicio comerServicio;
    /*
     * Variable para acceder a los servicios de Banco
     */
    @Inject
    private BancoServicio bancoServicio;
    /*
     * Variable para acceder a los servicios de Medida
     */
    @Inject
    private MedidaServicio medidaServicio;
    /*
     * Variable para acceder a los servicios de Terminal
     */
    @Inject
    private TerminalServicio termServicio;
    /*
     * Variable para acceder a los servicios de Cliente
     */
    @Inject
    private ClienteServicio clienteServicio;
    /*
     * Variable para acceder a los servicios de Cliente
     */
    @Inject
    private ClienteProductoServicio cliProdServicio;
    /*
     * Variable Comercializadora
     */
    private ComercializadoraBean comercializadora;
    /*
     * Variable Comercializadora
     */
    private Terminal terminal;
    /*
     * Variable que almacena el cÃƒÆ’Ã‚Â³digo de la comercializadora
     */
    private String codComer;
    /*
     * Variable que almacena el cÃƒÆ’Ã‚Â³digo del terminal
     */
    private String codTerminal;
    /*
     * Variable que almacena el cÃƒÆ’Ã‚Â³digo del cliente
     */
    private String codCliente;
    /*
     * Variable que almacena el cÃƒÆ’Ã‚Â³digo de la abastecedora
     */
    private String codAbas;
    /*
     * Varaible para guardar la selecciÃƒÆ’Ã‚Â³n del radio button
     */
    private String tipoFecha;
    /**
     * Variable que permite establecer la fecha
     */
    private Date fecha;
    /*
     * Variable para establoecer el valor de oeenpetro
     */
    private String oeenpetro;
    /*
     * Variable Nota Pedido
     */
    private Prepedido np;
    /*
     * Varibale Nota Pedido PK
     */
    private PrepedidoPK npPK;
    /*
     * Variable que isntacia el modelo PrepedidoPK
     */
    private Detalleprepedido detNP;
    /*
     * Variable para guardar una lista deDeatllesFactura
     */
    private List<Detalleprepedido> listDetNP;
    /*
     * Variable que isntacia el modelo DetalleprepedidoPK
     */
    private DetalleprepedidoPK detNPK;
    /* Detalle fila 1 - EXTRA 9901 */
    private Detalleprepedido detNP1;
    private Medida medida1;
    /* Detalle fila 2 - SUPER 9903 */
    private Detalleprepedido detNP2;
    private Medida medida2;
    /* Detalle fila 3 - DIESEL 9904 */
    private Detalleprepedido detNP3;
    private Medida medida3;
    /*
     * Variable para guardar Nota y Detalle Pedido
     */
    private transient PrepedidoSolicitud envNP;
    /*
     * Variable para guardar una lista de Nota y Detalle Pedido
     */
    private List<PrepedidoSolicitud> listPrepedido;
    /*
     * Variable Cliente
     */
    private Cliente cliente;
    /*
     * Variable terminal
     */
    private Terminal terminalT;
    /*
     * Variable Banco
     */
    private Banco banco;
    /*
     * Variable Comercializadora
     */
    private Comercializadora comerc;
    /*
     * Variable Abastecedora
     */
    private Abastecedora abas;
    /*
     * Variable para almacenar los datos comercializadora
     */
    private List<ComercializadoraBean> listaComercializadora;
    /*
     * Variable para almacenar los datos comercializadora
     */
    private List<Terminal> listaTermianles;
    /*
     * Variable para almacenar los datos clientes
     */
    private List<Cliente> listaClientes;
    /*
     * Variable Formapago
     */
    private Formapago formap;
    /*
     * Variable fecha venta
     */
    private Date fechaVenta;
    /*
     * Variable fechaDespacho
     */
    private Date fechaDespacho;
    /*
     * Lista de Productos
     */
    private List<Producto> listaProd;
    /*
     * Lista de Bancos
     */
    private List<Banco> listaBancos;
    /*
     * Lista de Medidas
     */
    private List<Medida> listaMedida;
    /*
     * Variable Medida
     */
    private Medida medida;

    /*
     * Variable para reenvÃƒÆ’Ã‚Â­o de orden
     */
    private PrepedidoSolicitud envioPedidoAuxiliar;
    /*
     * Variable para validar si es guardar o editar
     */
    private boolean editarPrepedido;
    /*
     * Variable Producto
     */
    private Producto productoSeleccionado;
    /*
     * Variable Producto
     */
    private List<Producto> listaProductos;
    /*
     * Varible prefijo
     */
    private String prefijo;
    /*
     * Variable numero nota pedido
     */
    private String numeroNotaPedio;
    /*
     * Variable trama grabada en creary enviar de una np usada para envÃƒÆ’Ã‚Â­o a
     * EPP
     */
    private String tramaGrabada;
    /*
     * Variable para establecer la fecha mÃƒÆ’Ã‚Â­nima
     */
    private Date fechaMin;
    /*
     * Vairbale para almacenar el pdf generado
     */
    private StreamedContent pdfStream;
    /*
     * variable para establecer la fecha inicial para la busqueda de notas de pedido
     */
    private Date fechaInicial;
    /*
     * variable para establecer la fecha final para la busqueda de notas de pedido
     */
    private Date fechaFinal;
    /*
     * variable para realizar la anulaciÃƒÆ’Ã‚Â³n de prepedido
     */
    private Prepedido prepedidoAuxiliar;
    /*
     * variable para realizar la anulaciÃƒÆ’Ã‚Â³n de prepedidoPK
     */
    private PrepedidoPK prepedidoAuxiliarPK;

    private boolean todosClientes;

    private String nomBanco;

    private String numCuenta;

    private String numCheque;

    /**
     * Constructor por defecto
     */
    public PrepedidoSolicitudBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        try {
            reestablecer();
            mostarPrepedido = false;
            mostarPantallaInicial = true;
            cliente = new Cliente();
            obtenerTerminales();
            obtenerComercializadora();
            todosClientes = false;
        } catch (Exception e) {
            mostarPantallaInicial = true;
            mostarPrepedido = false;
            LOG.log(Level.SEVERE, "Error en init de PrepedidoSolicitudBean: " + e.getMessage(), e);
        }
        // habilitarBusqueda();
    }

    public void nuevaPrepedido() {
        reestablecer();
        habilitarBusqueda(2);
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
        // if (habilitarTerminal) {
        // terminal = new TerminalBean();
        // }
        mostarPrepedido = true;
        mostarPantallaInicial = false;

    }

    public void reestablecer() {
        editarPrepedido = false;
        codComer = "";
        // codTerminal = "";
        codCliente = "";
        numeroNotaPedio = "";
        tramaGrabada = "";
        codAbas = "";
        tipoFecha = "1";
        prefijo = "";
        nomBanco = "";
        numCuenta = "";
        numCheque = "";
        fecha = new Date();
        oeenpetro = "";
        np = new Prepedido();
        npPK = new PrepedidoPK();
        detNP = new Detalleprepedido();
        detNPK = new DetalleprepedidoPK();
        envNP = new PrepedidoSolicitud();
        prepedidoAuxiliar = new Prepedido();
        prepedidoAuxiliarPK = new PrepedidoPK();
        terminalT = new Terminal();
        banco = new Banco();
        comerc = new Comercializadora();
        abas = new Abastecedora();
        formap = new Formapago();
        listPrepedido = new ArrayList<>();
        fechaVenta = new Date();
        fechaMin = new Date();
        productoSeleccionado = new Producto();
        medida = new Medida();
        listaProd = new ArrayList<>();
        listaBancos = new ArrayList<>();
        listaMedida = new ArrayList<>();
        // listaProductos = new ArrayList<>();
        detNP1 = new Detalleprepedido();
        medida1 = new Medida();
        detNP2 = new Detalleprepedido();
        medida2 = new Medida();
        detNP3 = new Detalleprepedido();
        medida3 = new Medida();
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
                comerc.setClavewsepp(comercializadora.getClaveWsepp());
                comerc.setEstablecimientofac(comercializadora.getEstabFac());
                comerc.setPuntoventafac(comercializadora.getPvFac());
                npPK.setCodigoabastecedora(codAbas);
                npPK.setCodigocomercializadora(codComer);
                np.setPrepedidoPK(npPK);
                np.setAbastecedora(abas);
                np.setComercializadora(comerc);
                detNPK.setCodigoabastecedora(codAbas);
                detNPK.setCodigocomercializadora(codComer);
                detNP.setDetalleprepedidoPK(detNPK);
                if (habilitarComer) {
                    listaClientes = new ArrayList<>();
                    listaClientes = clienteServicio.obtenerClientesPorComercializadoraActiva(codComer);
                }
                // listaClientes = new ArrayList<>();
                // listaClientes = clienteServicio.obtenerClientesPorComercializadora(codComer);
                // if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                // for (int i = 0; i < listaClientes.size(); i++) {
                // if
                // (listaClientes.get(i).getCodigo().equals(dataUser.getUser().getCodigocliente()))
                // {
                // this.cliente = listaClientes.get(i);
                // }
                // }
                // }
            } else {
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
        // ABRIR CONTROL COORDINANDO CON PYS boolean pysGDValido = false;
        boolean pysGDValido = true;
        if (cliente != null) {
            if ("0002".equalsIgnoreCase(codComer)) {

                if ("03".equalsIgnoreCase(cliente.getCodigoformapago().getCodigo())) {

                    if (new Date().before(cliente.getFehavencimientocontrato())) {

                        pysGDValido = true;
                    }
                }
            }

            if (pysGDValido) {

                codCliente = cliente.getClientePK().getCodigo();
                for (int i = 0; i < listaTermianles.size(); i++) {
                    if (listaTermianles.get(i).getCodigo().equals(cliente.getCodigoterminaldefecto().getCodigo())) {
                        terminal = listaTermianles.get(i);
                        break;
                    }
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_FATAL,
                        "Este cliente NO estÃƒÆ’Ã‚Â¡ autorizado para generar NP. Consulte con el administrador para verificar su condiciÃƒÆ’Ã‚Â³n");
            }
            seleccionarTerminal(busqueda);
        }

    }

    public void seleccionarCliente() {
        boolean pysGDValido = true;
        if (cliente != null) {

            if ("0002".equalsIgnoreCase(codComer)) {

                if ("03".equalsIgnoreCase(cliente.getCodigoformapago().getCodigo())) {

                    if (new Date().after(cliente.getFehavencimientocontrato())) {

                        pysGDValido = false;
                    }
                }
            }

            if (pysGDValido) {

                codCliente = cliente.getClientePK().getCodigo();
                listaProductos = new ArrayList<>();
                listaProductos = cliProdServicio.obtenerProductos(codComer, codCliente);
                np.setCodigocliente(cliente);
                if (cliente.getCodigoterminaldefecto() != null) {
                    // for (int i = 0; i < listaTermianles.size(); i++) {
                    // if
                    // (cliente.getCodigoterminaldefecto().equals(listaTermianles.get(i).getCodigo()))
                    // {
                    // terminal = listaTermianles.get(i);
                    // codTerminal = cliente.getCodigoterminaldefecto().getCodigo() + " - " +
                    // cliente.getCodigoterminaldefecto().getNombre();
                    // }
                    // np.setCodigoterminal(cliente.getCodigoterminaldefecto());
                    // }
                }

                if (cliente.getCodigobancodebito() != null) {
                    for (int i = 0; i < listaBancos.size(); i++) {
                        if (listaBancos.get(i).getCodigo().equals(cliente.getCodigobancodebito())) {
                            banco = listaBancos.get(i);
                            np.setCodigobanco(banco);
                        }
                    }
                }

            } else {

                this.dialogo(FacesMessage.SEVERITY_FATAL,
                        "Este cliente NO estÃƒÆ’Ã‚Â¡ autorizado para generar NP. Consulte con el administrador para verificar su condiciÃƒÆ’Ã‚Â³n");

                // nuevaPrepedido();
                listaProductos = new ArrayList<>();
            }

        }
    }

    public void seleccionarMedida() {
        if (medida != null) {
            detNPK.setCodigomedida(medida.getCodigo());
            detNP.setDetalleprepedidoPK(detNPK);
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
            detNP.setDetalleprepedidoPK(detNPK);
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
                        if (listaComercializadora.get(i).getCodigo()
                                .equals(dataUser.getUser().getCodigocomercializadora())) {
                            this.comercializadora = listaComercializadora.get(i);
                        }
                    }
                    seleccionarComerc(busqueda);
                    // listaClientes =
                    // clienteServicio.obtenerClientesPorComercializadora(comercializadora.getCodigo());
                    break;
                case "usac":
                    habilitarComer = false;
                    habilitarCli = false;
                    habilitarTerminal = false;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo()
                                .equals(dataUser.getUser().getCodigocomercializadora())) {
                            this.comercializadora = listaComercializadora.get(i);
                        }

                    }
                    if (comercializadora.getCodigo() != null) {
                        seleccionarComerc(busqueda);
                        listaClientes = clienteServicio
                                .obtenerClientesPorComercializadoraActiva(comercializadora.getCodigo());
                        for (int i = 0; i < listaClientes.size(); i++) {
                            if (listaClientes.get(i).getClientePK().getCodigo()
                                    .equals(dataUser.getUser().getCodigocliente())) {
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
                    // listaClientes =
                    // clienteServicio.obtenerClientesPorComercializadora(comercializadora.getCodigo());

                    // seleccionarCliente();
                    break;
                case "agco":
                    habilitarComer = false;
                    habilitarCli = true;
                    habilitarTerminal = true;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo()
                                .equals(dataUser.getUser().getCodigocomercializadora())) {
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
                    // List<Cliente> listaClientesAux = new ArrayList<>();
                    // listaClientesAux =
                    // clienteServicio.obtenerClientesPorComercializadora(codComer);
                    // listaClientes = new ArrayList<>();
                    // for (int i = 0; i < listaClientesAux.size(); i++) {
                    // if
                    // (listaClientesAux.get(i).getCodigoterminaldefecto().getCodigo().equals(codTerminal))
                    // {
                    // listaClientes.add(listaClientesAux.get(i));
                    // }
                    // }
                    break;
                default:
                    break;
            }
        }
    }

    public void obtenerPrepedidos() throws ParseException {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            /* fechas para hacer la consulta */
            String fechaI = date.format(fechaInicial);
            String fechaF = date.format(fechaFinal);
            /* fechas para comparar entre las dos y establecer un rango de 7 dias */
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            String dateI = sdf.format(fechaInicial);
            String dateF = sdf.format(fechaFinal);

            Date firstDate = sdf.parse(dateI);
            Date secondDate = sdf.parse(dateF);

            long diff = secondDate.getTime() - firstDate.getTime();
            TimeUnit time = TimeUnit.DAYS;
            long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
            if (diffrence > 7) {
                this.dialogo(FacesMessage.SEVERITY_ERROR,
                        "LA FECHA DE FIN NO PUEDE SER MAYOR A 7 DÃƒÆ’Ã‚ÂAS A LA FECHA DE INICIO");
            } else {
                // String direcc =
                // "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.prepedido/paraFactura?";
                String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim()
                        + "ec.com.infinity.modelo.prepedido/paraFactura?";
                if (codCliente.isEmpty()) {
                    url = new URL(direcc + "codigoabastecedora=" + codAbas + "&codigocomercializadora=" + codComer
                            + "&codigoterminal=" + codTerminal
                            + "&tipofecha=" + tipoFecha + "&fecha=" + fechaI);
                } else {
                    url = new URL(direcc + "codigoabastecedora=" + codAbas + "&codigocomercializadora=" + codComer
                            + "&codigoterminal=" + codTerminal
                            + "&tipofecha=" + tipoFecha + "&fecha=" + fechaI + "&codigocliente="
                            + this.codCliente);
                }
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(30000);

                listPrepedido = new ArrayList<>();
                listDetNP = new ArrayList<>();
                cliente = new Cliente();
                PrepedidoSolicitud envioPedido = new PrepedidoSolicitud();

                StringBuilder content = new StringBuilder();
                try (InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                        BufferedReader br = new BufferedReader(isr)) {
                    String tmp;
                    while ((tmp = br.readLine()) != null) {
                        content.append(tmp);
                    }
                }
                String respuesta = content.toString();
                LOG.info("API Response (Comerterminal): " + respuesta);

                JSONObject objetoJson = new JSONObject(respuesta);
                JSONArray retorno = objetoJson.getJSONArray("retorno");
                if (retorno.isEmpty()) {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCONTRARON " + getTituloPantallaPlural().toUpperCase());
                } else {

                    for (int indice = 0; indice < retorno.length(); indice++) {
                        if (!retorno.isNull(indice)) {
                            JSONObject nt = retorno.getJSONObject(indice);
                            JSONObject ntPK = nt.getJSONObject("prepedidoPK");
                            JSONObject cli = nt.getJSONObject("codigocliente");

                            JSONObject cliPK = cli.getJSONObject("clientePK");

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
                            ////// FTFT cliente.setCodigo(cli.getString("codigo"));
                            cliente.setClientePK(new ClientePK());
                            cliente.getClientePK().setCodigocomercializadora(cliPK.getString("codigocomercializadora"));
                            cliente.getClientePK().setCodigo(cliPK.getString("codigo"));

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
                            np.setCodigoclienteId(cliente.getClientePK().getCodigo().trim());
                            np.setCodigoterminal(terminalT);
                            np.setCodigobanco(banco);
                            np.setComercializadora(comerc);
                            np.setAbastecedora(abas);

                            if (!nt.isNull("tramaenviadagoe")) {
                                np.setTramaenviadagoe(nt.getString("tramaenviadagoe"));
                            } else {
                                np.setTramaenviadagoe("");
                            }

                            if (!nt.isNull("tramarecibidagoe")) {
                                np.setTramarecibidagoe(nt.getString("tramarecibidagoe"));
                            } else {
                                np.setTramarecibidagoe("");
                            }

                            if (!nt.isNull("tramarenviadaaoe")) {
                                np.setTramarenviadaaoe(nt.getString("tramarenviadaaoe"));
                            } else {
                                np.setTramarenviadaaoe("");
                            }

                            if (!nt.isNull("tramarecibidaaoe")) {
                                np.setTramarecibidaaoe(nt.getString("tramarecibidaaoe"));
                            } else {
                                np.setTramarecibidaaoe("");
                            }

                            np.setNumerofacturasri(nt.getString("numerofacturasri"));
                            np.setActiva(nt.getBoolean("activa"));
                            np.setFacturada(nt.getString("facturada"));
                            String respGen = nt.optString("respuestageneracionoeepp", "");
                            String respAnu = nt.optString("respuestaanulacionoeepp", "");
                            np.setRespuestageneracionoeepp(respGen);
                            np.setRespuestaanulacionoeepp(respAnu);
                            np.setOeenpetro(respGen);
                            np.setOeanuladaenpetro(respAnu);
                            npPK.setNumero(ntPK.getString("numero"));
                            npPK.setCodigoabastecedora(ntPK.getString("codigoabastecedora"));
                            npPK.setCodigocomercializadora(ntPK.getString("codigocomercializadora"));
                            np.setFechaventa(fechaVencimiento);
                            np.setFechadespacho(fechaDescpacho);
                            np.setPrepedidoPK(npPK);
                            np.setUsuarioactual(nt.getString("usuarioactual"));
                            if (!nt.isNull("observacion")) {
                                np.setObservacion(nt.getString("observacion"));
                            } else {
                                np.setObservacion("");
                            }

                            if (!nt.isNull("prefijo")) {
                                np.setPrefijo(nt.getString("prefijo"));
                            } else {
                                np.setPrefijo("");
                            }
                            if (!nt.isNull("codigoautotanque")) {
                                np.setCodigoautotanque(nt.getString("codigoautotanque"));
                            } else {
                                np.setCodigoautotanque("");
                            }
                            if (!nt.isNull("cedulaconductor")) {
                                np.setCedulaconductor(nt.getString("cedulaconductor"));
                            } else {
                                np.setCedulaconductor("");
                            }

                            /*----Parse Detail if exists for Producto and Volume columns----*/
                            List<Detalleprepedido> detallesParseados = new ArrayList<>();
                            JSONArray detList = null;
                            if (!nt.isNull("detallesNP")) {
                                detList = nt.getJSONArray("detallesNP");
                            } else if (!nt.isNull("detalleprepedidoList")) {
                                detList = nt.getJSONArray("detalleprepedidoList");
                            }

                            if (detList != null) {
                                for (int d = 0; d < detList.length(); d++) {
                                    if (!detList.isNull(d)) {
                                        JSONObject det = detList.getJSONObject(d);
                                        Detalleprepedido dp = new Detalleprepedido();
                                        if (!det.isNull("detalleprepedidoPK")) {
                                            JSONObject pkJson = det.getJSONObject("detalleprepedidoPK");
                                            DetalleprepedidoPK pk = new DetalleprepedidoPK();
                                            pk.setCodigoabastecedora(pkJson.optString("codigoabastecedora", ""));
                                            pk.setCodigocomercializadora(pkJson.optString("codigocomercializadora", ""));
                                            pk.setNumero(pkJson.optString("numero", ""));
                                            pk.setCodigoproducto(pkJson.optString("codigoproducto", ""));
                                            pk.setCodigomedida(pkJson.optString("codigomedida", ""));
                                            dp.setDetalleprepedidoPK(pk);
                                        }
                                        if (!det.isNull("producto")) {
                                            JSONObject prodJson = det.getJSONObject("producto");
                                            Producto p = new Producto();
                                            p.setCodigo(prodJson.optString("codigo", ""));
                                            p.setNombre(prodJson.optString("nombre", ""));
                                            dp.setProducto(p);
                                        } else if (!det.isNull("nombreproducto")) {
                                            Producto p = new Producto();
                                            p.setCodigo(det.optString("codigoproducto", ""));
                                            p.setNombre(det.optString("nombreproducto", ""));
                                            dp.setProducto(p);
                                        }

                                        if (!det.isNull("volumennaturalrequerido")) {
                                            dp.setVolumennaturalrequerido(det.getBigDecimal("volumennaturalrequerido"));
                                        }
                                        if (!det.isNull("volumennaturalautorizado")) {
                                            dp.setVolumennaturalautorizado(det.getBigDecimal("volumennaturalautorizado"));
                                        } else {
                                            dp.setVolumennaturalautorizado(BigDecimal.ZERO);
                                        }
                                        detallesParseados.add(dp);
                                    }
                                }
                            } else if (!nt.isNull("detalle")) {
                                JSONObject det = nt.getJSONObject("detalle");
                                Detalleprepedido dp = new Detalleprepedido();
                                if (!det.isNull("detalleprepedidoPK")) {
                                    JSONObject pkJson = det.getJSONObject("detalleprepedidoPK");
                                    DetalleprepedidoPK pk = new DetalleprepedidoPK();
                                    pk.setCodigoabastecedora(pkJson.optString("codigoabastecedora", ""));
                                    pk.setCodigocomercializadora(pkJson.optString("codigocomercializadora", ""));
                                    pk.setNumero(pkJson.optString("numero", ""));
                                    pk.setCodigoproducto(pkJson.optString("codigoproducto", ""));
                                    pk.setCodigomedida(pkJson.optString("codigomedida", ""));
                                    dp.setDetalleprepedidoPK(pk);
                                }
                                if (!det.isNull("producto")) {
                                    JSONObject prodJson = det.getJSONObject("producto");
                                    Producto p = new Producto();
                                    p.setCodigo(prodJson.optString("codigo", ""));
                                    p.setNombre(prodJson.optString("nombre", ""));
                                    dp.setProducto(p);
                                } else if (!det.isNull("nombreproducto")) {
                                    Producto p = new Producto();
                                    p.setCodigo(det.optString("codigoproducto", ""));
                                    p.setNombre(det.optString("nombreproducto", ""));
                                    dp.setProducto(p);
                                }
                                if (!det.isNull("volumennaturalrequerido")) {
                                    dp.setVolumennaturalrequerido(det.getBigDecimal("volumennaturalrequerido"));
                                }
                                if (!det.isNull("volumennaturalautorizado")) {
                                    dp.setVolumennaturalautorizado(det.getBigDecimal("volumennaturalautorizado"));
                                } else {
                                    dp.setVolumennaturalautorizado(BigDecimal.ZERO);
                                }
                                detallesParseados.add(dp);
                            } else {
                                // Check for flat structure in nt directly
                                if (!nt.isNull("nombreproducto") || !nt.isNull("volumennaturalautorizado")) {
                                    Detalleprepedido dp = new Detalleprepedido();
                                    Producto p = new Producto();
                                    p.setCodigo(nt.optString("codigoproducto", ""));
                                    p.setNombre(nt.optString("nombreproducto", ""));
                                    dp.setProducto(p);
                                    if (!nt.isNull("volumennaturalrequerido")) {
                                        dp.setVolumennaturalrequerido(nt.getBigDecimal("volumennaturalrequerido"));
                                    }
                                    if (!nt.isNull("volumennaturalautorizado")) {
                                        dp.setVolumennaturalautorizado(nt.getBigDecimal("volumennaturalautorizado"));
                                    } else {
                                        dp.setVolumennaturalautorizado(BigDecimal.ZERO);
                                    }
                                    detallesParseados.add(dp);
                                }
                            }

                            if (!detallesParseados.isEmpty()) {
                                for (Detalleprepedido dpParsed : detallesParseados) {
                                    PrepedidoSolicitud row = new PrepedidoSolicitud();
                                    row.setPrepedido(np);
                                    java.util.List<Detalleprepedido> singleList = new java.util.ArrayList<>();
                                    singleList.add(dpParsed);
                                    row.setDetalle(singleList);
                                    listPrepedido.add(row);
                                }
                            } else {
                                envioPedido.setPrepedido(np);
                                listPrepedido.add(envioPedido);
                            }
                            envioPedido = new PrepedidoSolicitud();
                            np = new Prepedido();
                            npPK = new PrepedidoPK();
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
            // habilitarBusqueda();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() throws ParseException {
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fechFormat = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
        Calendar c = Calendar.getInstance();
        c.setTime(fechaVenta);
        // c.add(Calendar.D, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        String dateV = sdf.format(fechaVenta);
        String dateD = sdf.format(fechaDespacho);

        // SimpleDateFormat fechaS = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        // SimpleDateFormat fechP = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
        // String dateP = fechaS.format(new Date());
        // fechP.parse(dateP);
        Date firstDate = sdf.parse(dateV);
        Date secondDate = sdf.parse(dateD);

        long diff = secondDate.getTime() - firstDate.getTime();
        TimeUnit time = TimeUnit.DAYS;
        long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);

        System.out.println("The difference in days is : " + diffrence);
        if (comercializadora.getActivo().equals("S")) {
            if (diffrence > 7) {
                this.dialogo(FacesMessage.SEVERITY_ERROR,
                        "LA FECHA DE DESPACHO NO PUEDE SER MAYOR A 7 DÍAS A LA FECHA DE VENTA");
            } else {
                npPK.setNumero(null);
                np.setPrepedidoPK(npPK);
                np.setCodigoterminal(terminal);
                np.setActiva(true);
                np.setFacturada("NO");
                np.setCodigoclienteId(cliente.getClientePK().getCodigo());
                np.setFechaventa(fechFormat.format(fechaVenta));
                np.setFechadespacho(fechFormat.format(fechaDespacho));
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
                np.setObservacion("Bco: " + nomBanco + " - Cta: " + numCuenta + " - Ch: " + numCheque);

                // Armar lista de detalles para los 3 productos fijos
                List<Detalleprepedido> listaDetalles = new ArrayList<>();
                String usuario = dataUser.getUser().getNombrever();
                Medida medidaFija = new Medida();
                medidaFija.setCodigo("01");

                // Fila 1 - EXTRA 9901
                if (detNP1.getVolumennaturalrequerido() == null) {
                    detNP1.setVolumennaturalrequerido(BigDecimal.ZERO);
                }
                Producto prod1 = new Producto();
                prod1.setCodigo("9901");
                prod1.setNombre("EXTRA");
                DetalleprepedidoPK pk1 = new DetalleprepedidoPK();
                pk1.setNumero(null);
                pk1.setCodigoabastecedora(codAbas);
                pk1.setCodigocomercializadora(codComer);
                pk1.setCodigoproducto("9901");
                pk1.setCodigomedida("01");
                detNP1.setDetalleprepedidoPK(pk1);
                detNP1.setProducto(prod1);
                detNP1.setMedida(medidaFija);
                detNP1.setVolumennaturalautorizado(BigDecimal.ZERO);
                detNP1.setUsuarioactual(usuario);
                detNP1.setCompartimento1(BigDecimal.ZERO);
                detNP1.setCompartimento2(BigDecimal.ZERO);
                detNP1.setCompartimento3(BigDecimal.ZERO);
                detNP1.setCompartimento4(BigDecimal.ZERO);
                detNP1.setCompartimento5(BigDecimal.ZERO);
                detNP1.setCompartimento6(BigDecimal.ZERO);
                detNP1.setCompartimento7(BigDecimal.ZERO);
                detNP1.setCompartimento8(BigDecimal.ZERO);
                detNP1.setCompartimento9(BigDecimal.ZERO);
                detNP1.setCompartimento10(BigDecimal.ZERO);
                detNP1.setSelloinicial(0);
                detNP1.setSellofinal(0);
                listaDetalles.add(detNP1);

                // Fila 2 - SUPER 9903
                if (detNP2.getVolumennaturalrequerido() == null) {
                    detNP2.setVolumennaturalrequerido(BigDecimal.ZERO);
                }
                Producto prod2 = new Producto();
                prod2.setCodigo("9903");
                prod2.setNombre("SUPER");
                DetalleprepedidoPK pk2 = new DetalleprepedidoPK();
                pk2.setNumero(null);
                pk2.setCodigoabastecedora(codAbas);
                pk2.setCodigocomercializadora(codComer);
                pk2.setCodigoproducto("9903");
                pk2.setCodigomedida("01");
                detNP2.setDetalleprepedidoPK(pk2);
                detNP2.setProducto(prod2);
                detNP2.setMedida(medidaFija);
                detNP2.setVolumennaturalautorizado(BigDecimal.ZERO);
                detNP2.setUsuarioactual(usuario);
                detNP2.setCompartimento1(BigDecimal.ZERO);
                detNP2.setCompartimento2(BigDecimal.ZERO);
                detNP2.setCompartimento3(BigDecimal.ZERO);
                detNP2.setCompartimento4(BigDecimal.ZERO);
                detNP2.setCompartimento5(BigDecimal.ZERO);
                detNP2.setCompartimento6(BigDecimal.ZERO);
                detNP2.setCompartimento7(BigDecimal.ZERO);
                detNP2.setCompartimento8(BigDecimal.ZERO);
                detNP2.setCompartimento9(BigDecimal.ZERO);
                detNP2.setCompartimento10(BigDecimal.ZERO);
                detNP2.setSelloinicial(0);
                detNP2.setSellofinal(0);
                listaDetalles.add(detNP2);

                // Fila 3 - DIESEL 9904
                if (detNP3.getVolumennaturalrequerido() == null) {
                    detNP3.setVolumennaturalrequerido(BigDecimal.ZERO);
                }
                Producto prod3 = new Producto();
                prod3.setCodigo("9904");
                prod3.setNombre("DIESEL");
                DetalleprepedidoPK pk3 = new DetalleprepedidoPK();
                pk3.setNumero(null);
                pk3.setCodigoabastecedora(codAbas);
                pk3.setCodigocomercializadora(codComer);
                pk3.setCodigoproducto("9904");
                pk3.setCodigomedida("01");
                detNP3.setDetalleprepedidoPK(pk3);
                detNP3.setProducto(prod3);
                detNP3.setMedida(medidaFija);
                detNP3.setVolumennaturalautorizado(BigDecimal.ZERO);
                detNP3.setUsuarioactual(usuario);
                detNP3.setCompartimento1(BigDecimal.ZERO);
                detNP3.setCompartimento2(BigDecimal.ZERO);
                detNP3.setCompartimento3(BigDecimal.ZERO);
                detNP3.setCompartimento4(BigDecimal.ZERO);
                detNP3.setCompartimento5(BigDecimal.ZERO);
                detNP3.setCompartimento6(BigDecimal.ZERO);
                detNP3.setCompartimento7(BigDecimal.ZERO);
                detNP3.setCompartimento8(BigDecimal.ZERO);
                detNP3.setCompartimento9(BigDecimal.ZERO);
                detNP3.setCompartimento10(BigDecimal.ZERO);
                detNP3.setSelloinicial(0);
                detNP3.setSellofinal(0);
                listaDetalles.add(detNP3);

                if (listaDetalles.isEmpty()) {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "Debe ingresar volumen en al menos un producto.");
                    return;
                }

                envNP.setPrepedido(np);
                envNP.setDetalle(listaDetalles);
                if (editarPrepedido) {
                    // editItems(envNP);
                } else {
                    addItems(envNP);
                }
            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA SE ENCUENTRA INACTIVA");
        }
    }

    public void addItems(PrepedidoSolicitud envNP) {
        try {
            String respuesta = "";
            // String trama = "";
            // String direcc =
            // "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.prepedido";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim()
                    + "ec.com.infinity.modelo.prepedido";
            url = new URL(direcc);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "PostmanRuntime/7.28.4");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(envNP);

            System.out.println("FT:: addItems OBJETO PREPEDIDO " + jsonStr);

            try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
                out.write(jsonStr.getBytes("UTF-8"));
                out.flush();
            }

            StringBuilder contentBuilder = new StringBuilder();
            if (connection.getResponseCode() >= 400) {
                InputStream errorStream = connection.getErrorStream();
                if (errorStream != null) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(errorStream, "UTF-8"))) {
                        String tmp;
                        while ((tmp = br.readLine()) != null) {
                            contentBuilder.append(tmp);
                        }
                    }
                }
                String errStr = contentBuilder.toString();
                System.out.println("FT:: ERROR HTTP " + connection.getResponseCode() + " BODY: " + errStr);
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR HTTP " + connection.getResponseCode() + ": " + errStr);
                return;
            } else {
                try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
                        BufferedReader br = new BufferedReader(isr)) {
                    String tmp;
                    while ((tmp = br.readLine()) != null) {
                        contentBuilder.append(tmp);
                    }
                }
            }
            respuesta = contentBuilder.toString();
            JSONObject objetoJson = new JSONObject(respuesta);
            String devMsg = objetoJson.optString("developerMessage", "");
            String[] parts = devMsg.split(";");
            if (parts.length > 0) {
                numeroNotaPedio = parts[0];
            }
            if (parts.length > 1) {
                tramaGrabada = parts[1];
            }

            if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201) {
                envNP.getPrepedido().getPrepedidoPK().setNumero(numeroNotaPedio);
                this.dialogo(FacesMessage.SEVERITY_INFO, "PREPEDIDO REGISTRADO EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR PREPEDIDO");
                System.out.println("FT:: ERROR EN addItems RESPONSECODE " + connection.getResponseCode());
                System.out.println("FT:: ERROR EN addItems RESPONSEMESSAGE " + connection.getResponseMessage());
            }

        } catch (IOException e) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR PREPEDIDO: " + e);
            e.printStackTrace();
        }
    }

    public void editarPrepedido(Prepedido prepedidoAuxiliar) {
        try {
            String respuesta = "";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.prepedido/porId";
            url = new URL(direcc);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(prepedidoAuxiliar);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();

            if (connection.getResponseCode() == 200) {
                // this.dialogo(FacesMessage.SEVERITY_INFO, "TRAMA INGRESADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL INGRESAR TRAMA");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dialogoAnulacionPrepedido(PrepedidoSolicitud envNP) {
        this.prepedidoAuxiliar = envNP.getPrepedido();
        if (!this.prepedidoAuxiliar.isActiva()) {
            this.dialogo(FacesMessage.SEVERITY_ERROR,
                    "NO SE PUEDE ANULAR ESTA NOTA DE PEDIDO, PORQUE YA SE ENCUENTRA ANULADA");
        } else if ("SI".equalsIgnoreCase(this.prepedidoAuxiliar.getFacturada().trim())) {
            this.dialogo(FacesMessage.SEVERITY_ERROR,
                    "NO SE PUEDE ANULAR ESTA NOTA DE PEDIDO, PORQUE SE ENCUENTRA FACTURADA");
        } else {
            PrimeFaces.current().executeScript("PF('deleteProductDialog').show()");
        }
    }

    public void anularPrepedido() {
        try {
            String respuesta = "";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim()
                    + "ec.com.infinity.modelo.prepedido/anulacion/";
            url = new URL(direcc + "numero=" + prepedidoAuxiliar.getPrepedidoPK().getNumero() + "&comercializadora="
                    + prepedidoAuxiliar.getPrepedidoPK().getCodigocomercializadora() + "&abastecedora="
                    + prepedidoAuxiliar.getPrepedidoPK().getCodigoabastecedora());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "NOTA DE PEDIDO ANULADA EXITOSAMENTE");
                init();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ANULAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarData(PrepedidoSolicitud envNP) {
        System.out.println("FT:. " + envNP);
        envioPedidoAuxiliar = envNP;
    }

    public void consultaPrepedidoPorId(PrepedidoSolicitud envNP) {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            cliente = new Cliente();

            // String direcc =
            // "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.prepedido/porId?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.prepedido/porId?";
            url = new URL(direcc + "codigoabastecedora="
                    + envNP.getPrepedido().getPrepedidoPK().getCodigoabastecedora()
                    + "&codigocomercializadora=" + envNP.getPrepedido().getPrepedidoPK().getCodigocomercializadora()
                    + "&numero=" + envNP.getPrepedido().getPrepedidoPK().getNumero());
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
                this.dialogo(FacesMessage.SEVERITY_ERROR,
                        "ERROR AL OBTENER INFORMACIÓN SOBRE LA NOTA DE PEDIDO PARA LA ANULACIÓN");
            } else {

                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject nt = retorno.getJSONObject(indice);
                        JSONObject ntPK = nt.getJSONObject("prepedidoPK");
                        JSONObject cli = nt.getJSONObject("codigocliente");

                        JSONObject cliPK = cli.getJSONObject("clientePK");

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
                        ////// FTFT cliente.setCodigo(cli.getString("codigo"));
                        cliente.setClientePK(new ClientePK());
                        cliente.getClientePK().setCodigocomercializadora(cliPK.getString("codigocomercializadora"));
                        cliente.getClientePK().setCodigo(cliPK.getString("codigo"));

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
                        prepedidoAuxiliar.setCodigocliente(cliente);
                        prepedidoAuxiliar.setCodigoterminal(terminalT);
                        prepedidoAuxiliar.setCodigobanco(banco);
                        prepedidoAuxiliar.setComercializadora(comerc);
                        prepedidoAuxiliar.setAbastecedora(abas);
                        if (!nt.isNull("codigoautotanque")) {
                            prepedidoAuxiliar.setCodigoautotanque(nt.getString("codigoautotanque"));
                        } else {
                            prepedidoAuxiliar.setCodigoautotanque("");
                        }
                        if (!nt.isNull("cedulaconductor")) {
                            prepedidoAuxiliar.setCedulaconductor(nt.getString("cedulaconductor"));
                        } else {
                            prepedidoAuxiliar.setCedulaconductor("");
                        }
                        if (!nt.isNull("respuestageneracionoeepp")) {
                            prepedidoAuxiliar.setRespuestageneracionoeepp(nt.getString("respuestageneracionoeepp"));
                        } else {
                            prepedidoAuxiliar.setRespuestageneracionoeepp("");
                        }
                        if (!nt.isNull("observacion")) {
                            prepedidoAuxiliar.setObservacion(nt.getString("observacion"));
                        } else {
                            prepedidoAuxiliar.setObservacion("");
                        }
                        if (!nt.isNull("adelantar")) {
                            prepedidoAuxiliar.setAdelantar(nt.getBoolean("adelantar"));
                        } else {
                            prepedidoAuxiliar.setAdelantar(true);
                        }
                        if (!nt.isNull("respuestaanulacionoeepp")) {
                            prepedidoAuxiliar.setRespuestaanulacionoeepp(nt.getString("respuestaanulacionoeepp"));
                        } else {
                            prepedidoAuxiliar.setRespuestaanulacionoeepp("");
                        }
                        if (!nt.isNull("tramaenviadagoe")) {
                            prepedidoAuxiliar.setTramaenviadagoe(nt.getString("tramaenviadagoe"));
                        } else {
                            prepedidoAuxiliar.setTramaenviadagoe("");
                        }
                        if (!nt.isNull("tramarecibidagoe")) {
                            prepedidoAuxiliar.setTramarecibidagoe(nt.getString("tramarecibidagoe"));
                        } else {
                            prepedidoAuxiliar.setTramarecibidagoe("");
                        }
                        if (!nt.isNull("tramarecibidaaoe")) {
                            prepedidoAuxiliar.setTramarecibidaaoe(nt.getString("tramarecibidaaoe"));
                        } else {
                            prepedidoAuxiliar.setTramarecibidaaoe("");
                        }
                        if (!nt.isNull("tramarecibidaaoe")) {
                            prepedidoAuxiliar.setTramarenviadaaoe(nt.getString("tramarenviadaaoe"));
                        } else {
                            prepedidoAuxiliar.setTramarenviadaaoe("");
                        }

                        prepedidoAuxiliar.setUsuarioactual(nt.getString("usuarioactual"));

                        if (!nt.isNull("prefijo")) {
                            prepedidoAuxiliar.setPrefijo(nt.getString("prefijo"));
                        } else {
                            prepedidoAuxiliar.setPrefijo("");
                        }
                        prepedidoAuxiliar.setProcesar(nt.getBoolean("procesar"));

                        prepedidoAuxiliar.setNumerofacturasri(nt.getString("numerofacturasri"));
                        prepedidoAuxiliarPK.setNumero(ntPK.getString("numero"));
                        prepedidoAuxiliarPK.setCodigoabastecedora(ntPK.getString("codigoabastecedora"));
                        prepedidoAuxiliarPK.setCodigocomercializadora(ntPK.getString("codigocomercializadora"));
                        prepedidoAuxiliar.setPrepedidoPK(prepedidoAuxiliarPK);
                        prepedidoAuxiliarPK = new PrepedidoPK();
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

    public void generarReporte(PrepedidoSolicitud envP) {
        // String path = "C:\\archivos\\Template\\prepedido.jrxml";
        String rutaGuardar = Fichero.getCARPETAREPORTES();
        String path = Fichero.getCARPETAREPORTES() + "/prepedido.jrxml";
        System.out.println("PATH:" + path);
        InputStream file = null;
        try {
            file = new FileInputStream(new File(path));

            JasperReport reporte = JasperCompileManager.compileReport(file);
            BufferedImage image = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/logo"
                    + envP.getPrepedido().getPrepedidoPK().getCodigocomercializadora() + ".jpeg"));
            // BufferedImage image = ImageIO.read(new
            // File("C:\\archivos\\Template\\logo.jpg"));
            Map parametro = new HashMap();

            parametro.put("codComer", envP.getPrepedido().getPrepedidoPK().getCodigocomercializadora());
            parametro.put("numeroPrepedido", envP.getPrepedido().getPrepedidoPK().getNumero());
            parametro.put("logo", image);

            // System.out.println("PARAMETROS: " + parametro);
            Connection conexion = conexionJasperBD();

            // System.out.println("CONEXIÃƒÆ’Ã¢â‚¬Å“N: " + conexion);
            JasperPrint print = JasperFillManager.fillReport(reporte, parametro, conexion);

            // File directory = new File("C:\\Archivos");
            File directory = new File(rutaGuardar);
            String nombreDocumento = "reportePrepedido";

            File pdf = File.createTempFile(nombreDocumento + "_", ".pdf", directory);
            JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
            File initialFile = new File(pdf.getAbsolutePath());
            InputStream targetStream = new FileInputStream(initialFile);
            // pdfStream = new DefaultStreamedContent();
            pdfStream = new DefaultStreamedContent(targetStream, "application/pdf",
                    nombreDocumento + envP.getPrepedido().getPrepedidoPK().getNumero() + ".pdf");
            // DefaultStreamedContent.builder().contentType("application/pdf").name(nombreDocumento
            // + ".pdf").stream(() -> new FileInputStream(targetStream)).build();
            System.err.print(pdf.getAbsolutePath());
            System.out.println(pdf.getAbsolutePath());
        } catch (Exception ex) {
            // ex.printStackTrace();
            System.out.println("Excepcion: " + ex);
        }
    }

    public void generarReporteAux(PrepedidoSolicitud envP) {
        // String path = "C:\\archivos\\Template\\FormatoPrepedido.jrxml";
        // String subreport = "C:\\archivos\\Template\\prepedido.jrxml";
        String rutaGuardar = Fichero.getCARPETAREPORTES();
        //// ftftft String subreport = Fichero.getCARPETAREPORTES() +
        //// "/prepedido.jrxml";
        String subreport = Fichero.getCARPETAREPORTES() + "/prepedidoext.jrxml";
        String path = Fichero.getCARPETAREPORTES() + "/FormatoPrepedido.jrxml";
        System.out.println("PATH:" + path);
        InputStream file = null;
        try {
            file = new FileInputStream(new File(path));

            JasperReport reporte = JasperCompileManager.compileReport(file);
            JasperReport subreporte = JasperCompileManager.compileReport(subreport);
            BufferedImage image = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/logo"
                    + envP.getPrepedido().getPrepedidoPK().getCodigocomercializadora() + ".jpeg"));
            // BufferedImage image = ImageIO.read(new
            // File("C:\\archivos\\Template\\logo.jpg"));
            Map parametro = new HashMap();

            parametro.put("subReporte", subreporte);
            parametro.put("codComer", envP.getPrepedido().getPrepedidoPK().getCodigocomercializadora());
            parametro.put("numeroPrepedido", envP.getPrepedido().getPrepedidoPK().getNumero());
            parametro.put("logo", image);

            // System.out.println("PARAMETROS: " + parametro);
            Connection conexion = conexionJasperBD();

            // System.out.println("CONEXIÃƒÆ’Ã¢â‚¬Å“N: " + conexion);
            JasperPrint print = JasperFillManager.fillReport(reporte, parametro, conexion);

            // File directory = new File("C:\\Archivos");
            File directory = new File(rutaGuardar);
            String nombreDocumento = "reportePrepedido";

            File pdf = File.createTempFile(nombreDocumento + "_", ".pdf", directory);
            JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
            File initialFile = new File(pdf.getAbsolutePath());
            InputStream targetStream = new FileInputStream(initialFile);
            // pdfStream = new DefaultStreamedContent();
            pdfStream = new DefaultStreamedContent(targetStream, "application/pdf",
                    nombreDocumento + envP.getPrepedido().getPrepedidoPK().getNumero() + ".pdf");
            // DefaultStreamedContent.builder().contentType("application/pdf").name(nombreDocumento
            // + ".pdf").stream(() -> new FileInputStream(targetStream)).build();
            System.err.print(pdf.getAbsolutePath());
            System.out.println(pdf.getAbsolutePath());
        } catch (Exception ex) {
            // ex.printStackTrace();
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

    public boolean isMostarPrepedido() {
        return mostarPrepedido;
    }

    public void setMostarPrepedido(boolean mostarPrepedido) {
        this.mostarPrepedido = mostarPrepedido;
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

    public Prepedido getNp() {
        return np;
    }

    public void setNp(Prepedido np) {
        this.np = np;
    }

    public PrepedidoPK getNpPK() {
        return npPK;
    }

    public void setNpPK(PrepedidoPK npPK) {
        this.npPK = npPK;
    }

    public Detalleprepedido getDetNP() {
        return detNP;
    }

    public void setDetNP(Detalleprepedido detNP) {
        this.detNP = detNP;
    }

    public List<Detalleprepedido> getListDetNP() {
        return listDetNP;
    }

    public void setListDetNP(List<Detalleprepedido> listDetNP) {
        this.listDetNP = listDetNP;
    }

    public DetalleprepedidoPK getDetNPK() {
        return detNPK;
    }

    public void setDetNPK(DetalleprepedidoPK detNPK) {
        this.detNPK = detNPK;
    }

    public PrepedidoSolicitud getEnvNP() {
        return envNP;
    }

    public void setEnvNP(PrepedidoSolicitud envNP) {
        this.envNP = envNP;
    }

    public List<PrepedidoSolicitud> getListenvNP() {
        return listPrepedido;
    }

    public void setListenvNP(List<PrepedidoSolicitud> listPrepedido) {
        this.listPrepedido = listPrepedido;
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

    public boolean isEditarPrepedido() {
        return editarPrepedido;
    }

    public void setEditarPrepedido(boolean editarPrepedido) {
        this.editarPrepedido = editarPrepedido;
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

    public PrepedidoSolicitud getPrepedidoSolicitudAuxiliar() {
        return envioPedidoAuxiliar;
    }

    public void setPrepedidoSolicitudAuxiliar(PrepedidoSolicitud envioPedidoAuxiliar) {
        this.envioPedidoAuxiliar = envioPedidoAuxiliar;
    }

    public String getTituloPantalla() {
        if (dataUser != null && dataUser.getUser() != null) {
            String cod = dataUser.getUser().getCodigocomercializadora();
            if ("0008".equals(cod)) {
                return "Prepedido";
            } else if ("0002".equals(cod)) {
                return "Solicitud";
            }
        }
        return "Nota Pedido";
    }

    public String getTituloPantallaPlural() {
        if (dataUser != null && dataUser.getUser() != null) {
            String cod = dataUser.getUser().getCodigocomercializadora();
            if ("0008".equals(cod)) {
                return "Prepedidos";
            } else if ("0002".equals(cod)) {
                return "Solicitudes";
            }
        }
        return "Notas de Pedido";
    }

    public String getTituloNumeroFactura() {
        if (dataUser != null && dataUser.getUser() != null) {
            String cod = dataUser.getUser().getCodigocomercializadora();
            if ("0008".equals(cod)) {
                return "Número Prepedido";
            } else if ("0002".equals(cod)) {
                return "Número Solicitud";
            }
        }
        return "Número Factura";
    }

    public Detalleprepedido getDetNP1() {
        return detNP1;
    }

    public void setDetNP1(Detalleprepedido detNP1) {
        this.detNP1 = detNP1;
    }

    public Medida getMedida1() {
        return medida1;
    }

    public void setMedida1(Medida medida1) {
        this.medida1 = medida1;
    }

    public Detalleprepedido getDetNP2() {
        return detNP2;
    }

    public void setDetNP2(Detalleprepedido detNP2) {
        this.detNP2 = detNP2;
    }

    public Medida getMedida2() {
        return medida2;
    }

    public void setMedida2(Medida medida2) {
        this.medida2 = medida2;
    }

    public Detalleprepedido getDetNP3() {
        return detNP3;
    }

    public void setDetNP3(Detalleprepedido detNP3) {
        this.detNP3 = detNP3;
    }

    public Medida getMedida3() {
        return medida3;
    }

    public void setMedida3(Medida medida3) {
        this.medida3 = medida3;
    }
    public void procesarAutorizacion() {
        boolean processados = false;
        try {
            if (listPrepedido != null) {
                for (PrepedidoSolicitud pSolicitud : listPrepedido) {
                    if (pSolicitud.isAutorizar()) {
                        if (pSolicitud.getDetalle() != null) {
                            for (Detalleprepedido det : pSolicitud.getDetalle()) {
                                if (det.getVolumennaturalautorizado() == null || det.getVolumennaturalautorizado().compareTo(BigDecimal.ZERO) <= 0) {
                                    this.dialogo(javax.faces.application.FacesMessage.SEVERITY_WARN, "EL VOLUMEN AUTORIZADO DEBE SER MAYOR A 0 PARA AUTORIZAR EL PRODUCTO " + (det.getProducto() != null ? det.getProducto().getNombre() : ""));
                                    continue;
                                }
                                String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detalleprepedido/porId";
                                URL url = new URL(direcc);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setDoOutput(true);
                                connection.setRequestMethod("PUT");
                                connection.setRequestProperty("Content-type", "application/json");

                                JSONObject jsonPayload = new JSONObject();
                                JSONObject pkJson = new JSONObject();
                                pkJson.put("codigoabastecedora", pSolicitud.getPrepedido().getPrepedidoPK().getCodigoabastecedora());
                                pkJson.put("codigocomercializadora", pSolicitud.getPrepedido().getPrepedidoPK().getCodigocomercializadora());
                                pkJson.put("numero", pSolicitud.getPrepedido().getPrepedidoPK().getNumero());
                                pkJson.put("codigoproducto", det.getProducto().getCodigo());
                                
                                String codigoMedida = "01";
                                if (det.getDetalleprepedidoPK() != null && det.getDetalleprepedidoPK().getCodigomedida() != null) {
                                    codigoMedida = det.getDetalleprepedidoPK().getCodigomedida();
                                }
                                pkJson.put("codigomedida", codigoMedida);
                                jsonPayload.put("detalleprepedidoPK", pkJson);

                                jsonPayload.put("volumennaturalrequerido", det.getVolumennaturalrequerido() != null ? det.getVolumennaturalrequerido() : BigDecimal.ZERO);
                                jsonPayload.put("volumennaturalautorizado", det.getVolumennaturalautorizado() != null ? det.getVolumennaturalautorizado() : BigDecimal.ZERO);

                                String usuario = "UsuarioWeb";
                                if (dataUser != null && dataUser.getUser() != null) {
                                    usuario = dataUser.getUser().getNombrever();
                                }
                                jsonPayload.put("usuarioactual", usuario);

                                jsonPayload.put("selloinicial", 0);
                                jsonPayload.put("sellofinal", 0);
                                jsonPayload.put("compartimento1", 0.0);
                                jsonPayload.put("compartimento2", 0.0);
                                jsonPayload.put("compartimento3", 0.0);
                                jsonPayload.put("compartimento4", 0.0);
                                jsonPayload.put("compartimento5", 0.0);
                                jsonPayload.put("compartimento6", 0.0);
                                jsonPayload.put("compartimento7", 0.0);
                                jsonPayload.put("compartimento8", 0.0);
                                jsonPayload.put("compartimento9", 0.0);
                                jsonPayload.put("compartimento10", 0.0);

                                JSONObject medidaJson = new JSONObject();
                                medidaJson.put("codigo", codigoMedida);
                                jsonPayload.put("medida", medidaJson);

                                JSONObject productoJson = new JSONObject();
                                productoJson.put("codigo", det.getProducto().getCodigo());
                                JSONObject areaMercadeoJson = new JSONObject();
                                areaMercadeoJson.put("codigo", "01");
                                productoJson.put("codigoareamercadeo", areaMercadeoJson);
                                jsonPayload.put("producto", productoJson);

                                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                                out.write(jsonPayload.toString());
                                out.close();

                                if (connection.getResponseCode() == 200 || connection.getResponseCode() == 204) {
                                    processados = true;
                                } else {
                                    System.out.println("Error procesando autorización: " + connection.getResponseCode() + " " + connection.getResponseMessage());
                                }
                            }
                        }
                    }
                }
            }
            if (processados) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "REGISTROS PROCESADOS EXITOSAMENTE");
                obtenerPrepedidos(); // Recargar la tabla
            } else {
                this.dialogo(FacesMessage.SEVERITY_WARN, "NO SE PROCESÓ NINGÚN REGISTRO. VERIFIQUE SELECCIÓN.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL PROCESAR");
        }
    }
}
