/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.pedidosyfacturacion;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
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
                    this.dialogo(FacesMessage.SEVERITY_ERROR,
                            "NO SE ENCONTRARON " + getTituloPantallaPlural().toUpperCase());
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
                            if (!com.isNull("generapedidodirecto")) {
                                comerc.setGenerapedidodirecto(com.getBoolean("generapedidodirecto"));
                            }

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
                                            pk.setCodigocomercializadora(
                                                    pkJson.optString("codigocomercializadora", ""));
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
                                            dp.setVolumennaturalautorizado(
                                                    det.getBigDecimal("volumennaturalautorizado"));
                                        } else {
                                            dp.setVolumennaturalautorizado(BigDecimal.ZERO);
                                        }
                                        if (det.has("activo") && !det.isNull("activo")) {
                                            dp.setActivo(det.getBoolean("activo"));
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
                                if (det.has("activo") && !det.isNull("activo")) {
                                    dp.setActivo(det.getBoolean("activo"));
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
                                    if (nt.has("activo") && !nt.isNull("activo")) {
                                        dp.setActivo(nt.getBoolean("activo"));
                                    }
                                    detallesParseados.add(dp);
                                }
                            }

                            String numNotaBackend = "0";
                            try {
                                if (nt.has("numeronotapedidogenerada") && !nt.isNull("numeronotapedidogenerada")) {
                                    numNotaBackend = nt.optString("numeronotapedidogenerada", "0");
                                } else if (nt.has("numeroNotaPedidoGenerada")
                                        && !nt.isNull("numeroNotaPedidoGenerada")) {
                                    numNotaBackend = nt.optString("numeroNotaPedidoGenerada", "0");
                                } else if (nt.has("numeronotapedido") && !nt.isNull("numeronotapedido")) {
                                    numNotaBackend = nt.optString("numeronotapedido", "0");
                                } else if (nt.has("notapedido") && !nt.isNull("notapedido")) {
                                    Object objNp = nt.get("notapedido");
                                    if (objNp instanceof String) {
                                        numNotaBackend = (String) objNp;
                                    } else if (objNp instanceof JSONObject) {
                                        JSONObject joNp = (JSONObject) objNp;
                                        if (joNp.has("notapedidoPK") && !joNp.isNull("notapedidoPK")) {
                                            numNotaBackend = joNp.getJSONObject("notapedidoPK").optString("numero",
                                                    "0");
                                        } else if (joNp.has("numero") && !joNp.isNull("numero")) {
                                            numNotaBackend = joNp.optString("numero", "0");
                                        }
                                    }
                                } else if (nt.has("notapedidogenerada") && !nt.isNull("notapedidogenerada")) {
                                    numNotaBackend = nt.optString("notapedidogenerada", "0");
                                }
                                if (numNotaBackend == null || numNotaBackend.trim().isEmpty()
                                        || numNotaBackend.equals("null")) {
                                    numNotaBackend = "0";
                                }
                            } catch (Exception e) {
                                LOG.log(java.util.logging.Level.WARNING, "Error parseando nota pedido generada", e);
                            }

                            if (!detallesParseados.isEmpty()) {
                                int idx = 0;
                                JSONArray arr = nt.optJSONArray("detalleprepedidoList");
                                for (Detalleprepedido dpParsed : detallesParseados) {
                                    PrepedidoSolicitud row = new PrepedidoSolicitud();
                                    row.setPrepedido(np);
                                    java.util.List<Detalleprepedido> singleList = new java.util.ArrayList<>();
                                    singleList.add(dpParsed);
                                    row.setDetalle(singleList);
                                    row.setEstadoForzado(row.getEstadoAutorizado());

                                    String finalNumNp = numNotaBackend;
                                    try {
                                        if (arr != null && idx < arr.length()) {
                                            JSONObject detJson = arr.getJSONObject(idx);
                                            if (detJson.has("numeronp") && !detJson.isNull("numeronp")) {
                                                String val = detJson.optString("numeronp", "0");
                                                if (val != null && !val.trim().isEmpty() && !val.equals("null")
                                                        && !val.equals("0")) {
                                                    finalNumNp = val;
                                                }
                                            }
                                        } else if (nt.has("detalle") && !nt.isNull("detalle")) {
                                            JSONObject detJson = nt.getJSONObject("detalle");
                                            if (detJson.has("numeronp") && !detJson.isNull("numeronp")) {
                                                String val = detJson.optString("numeronp", "0");
                                                if (val != null && !val.trim().isEmpty() && !val.equals("null")
                                                        && !val.equals("0")) {
                                                    finalNumNp = val;
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }

                                    row.setNumeroNotaPedidoGenerada(finalNumNp);
                                    listPrepedido.add(row);
                                    idx++;
                                }
                            } else {
                                envioPedido.setPrepedido(np);
                                envioPedido.setEstadoForzado(envioPedido.getEstadoAutorizado());
                                envioPedido.setNumeroNotaPedidoGenerada(numNotaBackend);
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

                this.listPrepedidoCompleta = new java.util.ArrayList<>(listPrepedido);
                filtrarAutorizados();
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
        if ("SI".equals(envNP.getEstadoAutorizado())) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "No se puede anular un prepedido que ya está autorizado.");
            return;
        }
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

    public void dialogoAutorizacionPrepedido(PrepedidoSolicitud envNP) {
        if (!envNP.getPrepedido().isActiva()) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "No se puede autorizar porque el prepedido está anulado.");
            return;
        }
        if (envNP.getDetalle() == null || envNP.getDetalle().isEmpty() ||
                envNP.getDetalle().get(0).getVolumennaturalautorizado() == null ||
                envNP.getDetalle().get(0).getVolumennaturalautorizado().compareTo(java.math.BigDecimal.ZERO) == 0) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "El volumen autorizado debe ser diferente de 0.");
            return;
        }
        this.envNP = envNP;
        PrimeFaces.current().executeScript("PF('autorizarDialog').show()");
    }

    public void autorizarPrepedido() {
        if (this.envNP != null) {
            for (PrepedidoSolicitud p : listPrepedido) {
                p.setAutorizar(false);
            }
            this.envNP.setAutorizar(true);
            procesarAutorizacion();
            PrimeFaces.current().executeScript("PF('autorizarDialog').hide()");
        }
    }

    public void cancelarAutorizacion() {
        if (this.envNP != null) {
            this.envNP.setAutorizar(false);
            if (this.envNP.getDetalle() != null) {
                for (ec.com.infinityone.modelo.Detalleprepedido det : this.envNP.getDetalle()) {
                    det.setVolumennaturalautorizado(java.math.BigDecimal.ZERO);
                }
            }
            PrimeFaces.current().executeScript("PF('autorizarDialog').hide()");
        }
    }

    public static class FilaGenerarNota {
        private PrepedidoSolicitud prepedidoSolicitud;
        private String codigoProductoClienteSeleccionado;
        private java.util.List<Producto> listaProductosFiltrada;
        private String numeroNotaPedidoGenerada;

        public FilaGenerarNota(PrepedidoSolicitud ps) {
            this.prepedidoSolicitud = ps;
            this.listaProductosFiltrada = new java.util.ArrayList<>();
            if (ps != null && ps.getNumeroNotaPedidoGenerada() != null && !ps.getNumeroNotaPedidoGenerada().trim().isEmpty() && !ps.getNumeroNotaPedidoGenerada().equals("0")) {
                this.numeroNotaPedidoGenerada = ps.getNumeroNotaPedidoGenerada();
            } else {
                this.numeroNotaPedidoGenerada = "00";
            }
        }

        public String getNumeroNotaPedidoGenerada() {
            return numeroNotaPedidoGenerada;
        }

        public void setNumeroNotaPedidoGenerada(String numeroNotaPedidoGenerada) {
            this.numeroNotaPedidoGenerada = numeroNotaPedidoGenerada;
        }

        public java.util.List<Producto> getListaProductosFiltrada() {
            return listaProductosFiltrada;
        }

        public void setListaProductosFiltrada(java.util.List<Producto> listaProductosFiltrada) {
            this.listaProductosFiltrada = listaProductosFiltrada;
        }

        public PrepedidoSolicitud getPrepedidoSolicitud() {
            return prepedidoSolicitud;
        }

        public void setPrepedidoSolicitud(PrepedidoSolicitud ps) {
            this.prepedidoSolicitud = ps;
        }

        public String getCodigoProductoClienteSeleccionado() {
            return codigoProductoClienteSeleccionado;
        }

        public void setCodigoProductoClienteSeleccionado(String codigo) {
            this.codigoProductoClienteSeleccionado = codigo;
        }

        public boolean isBloqueado() {
            if (numeroNotaPedidoGenerada != null && !"00".equals(numeroNotaPedidoGenerada)
                    && !"0".equals(numeroNotaPedidoGenerada)) {
                return true;
            }
            if (prepedidoSolicitud == null || prepedidoSolicitud.getDetalle() == null
                    || prepedidoSolicitud.getDetalle().isEmpty()) {
                return true;
            }
            java.math.BigDecimal volAutorizado = prepedidoSolicitud.getDetalle().get(0).getVolumennaturalautorizado();
            return volAutorizado == null || volAutorizado.compareTo(java.math.BigDecimal.ZERO) <= 0;
        }
    }

    private java.util.List<FilaGenerarNota> listaDetallesGenerar;
    private java.util.List<Producto> listaProductosClienteModal;

    public java.util.List<FilaGenerarNota> getListaDetallesGenerar() {
        return listaDetallesGenerar;
    }

    public void setListaDetallesGenerar(java.util.List<FilaGenerarNota> listaDetallesGenerar) {
        this.listaDetallesGenerar = listaDetallesGenerar;
    }

    public java.util.List<Producto> getListaProductosClienteModal() {
        return listaProductosClienteModal;
    }

    public void setListaProductosClienteModal(java.util.List<Producto> listaProductosClienteModal) {
        this.listaProductosClienteModal = listaProductosClienteModal;
    }

    private String filtroAutorizado = "";
    private java.util.List<PrepedidoSolicitud> listPrepedidoCompleta;

    public String getFiltroAutorizado() {
        return filtroAutorizado;
    }

    public void setFiltroAutorizado(String filtroAutorizado) {
        this.filtroAutorizado = filtroAutorizado;
    }

    public java.util.List<PrepedidoSolicitud> getListPrepedidoCompleta() {
        return listPrepedidoCompleta;
    }

    public void setListPrepedidoCompleta(java.util.List<PrepedidoSolicitud> listPrepedidoCompleta) {
        this.listPrepedidoCompleta = listPrepedidoCompleta;
    }

    public void filtrarAutorizados() {
        if (listPrepedidoCompleta == null)
            return;
        if (filtroAutorizado == null || filtroAutorizado.isEmpty()) {
            this.listPrepedido = new java.util.ArrayList<>(listPrepedidoCompleta);
        } else {
            this.listPrepedido = listPrepedidoCompleta.stream()
                    .filter(p -> filtroAutorizado.equals(p.getEstadoAutorizado()))
                    .collect(java.util.stream.Collectors.toList());
        }
    }

    public void generarNotaPedido(PrepedidoSolicitud envNP) {
        if (!"SI".equals(envNP.getEstadoAutorizado())) {
            this.dialogo(FacesMessage.SEVERITY_ERROR,
                    "El prepedido debe estar autorizado para generar la nota de pedido.");
            return;
        }

        this.envNP = envNP;

        // Cargar productos del cliente
        try {
            if (envNP.getPrepedido() != null && envNP.getPrepedido().getCodigocliente() != null) {
                String codCliente = envNP.getPrepedido().getCodigocliente().getClientePK().getCodigo();
                String codComer = envNP.getPrepedido().getCodigocliente().getClientePK().getCodigocomercializadora();
                listaProductosClienteModal = cliProdServicio.obtenerProductos(codComer, codCliente);
            } else {
                listaProductosClienteModal = new java.util.ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            listaProductosClienteModal = new java.util.ArrayList<>();
        }

        // Cargar todos los detalles del mismo prepedido para el modal
        listaDetallesGenerar = new java.util.ArrayList<>();
        if (listPrepedido != null && envNP != null && envNP.getPrepedido() != null) {
            String targetNumero = envNP.getPrepedido().getPrepedidoPK().getNumero();
            if (targetNumero != null) {
                targetNumero = targetNumero.trim();
                for (PrepedidoSolicitud ps : listPrepedido) {
                    if (ps.getPrepedido() != null && ps.getPrepedido().getPrepedidoPK() != null) {
                        String currentNumero = ps.getPrepedido().getPrepedidoPK().getNumero();
                        if (currentNumero != null && targetNumero.equals(currentNumero.trim())) {
                            if (!ps.isRegistroActivo()) {
                                continue;
                            }
                            FilaGenerarNota fila = new FilaGenerarNota(ps);

                            java.util.List<Producto> filtrada = new java.util.ArrayList<>();
                            if (ps.getDetalle() != null && !ps.getDetalle().isEmpty()
                                    && ps.getDetalle().get(0).getProducto() != null) {
                                String prodRef = ps.getDetalle().get(0).getProducto().getCodigo();
                                for (Producto p : listaProductosClienteModal) {
                                    if (prodRef != null && prodRef.equals(p.getProductogenerico())) {
                                        filtrada.add(p);
                                    }
                                }
                            }
                            fila.setListaProductosFiltrada(filtrada);

                            listaDetallesGenerar.add(fila);
                        }
                    }
                }
            }
        }

        org.primefaces.PrimeFaces.current().ajax().update("formGenerar");
        org.primefaces.PrimeFaces.current().executeScript("PF('generarDialog').show();");
    }

    public void procesarGenerarNotaFila(FilaGenerarNota fila) {
        if (fila == null || fila.getPrepedidoSolicitud() == null) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "No se encontró el detalle de la fila.");
            return;
        }

        String codProductoCliente = fila.getCodigoProductoClienteSeleccionado();
        if (codProductoCliente == null || codProductoCliente.isEmpty()) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un producto cliente.");
            return;
        }

        PrepedidoSolicitud ps = fila.getPrepedidoSolicitud();
        Prepedido prep = ps.getPrepedido();
        Detalleprepedido detPrep = ps.getDetalle().get(0);

        try {
            ec.com.infinityone.modelo.Notapedido np = new ec.com.infinityone.modelo.Notapedido();
            ec.com.infinityone.modelo.NotapedidoPK npPK = new ec.com.infinityone.modelo.NotapedidoPK();

            npPK.setNumero("");
            npPK.setCodigoabastecedora(prep.getPrepedidoPK().getCodigoabastecedora());
            npPK.setCodigocomercializadora(prep.getPrepedidoPK().getCodigocomercializadora());
            np.setNotapedidoPK(npPK);

            np.setCodigoterminal(prep.getCodigoterminal());
            np.setCodigocliente(prep.getCodigocliente());
            if (prep.getCodigocliente() != null && prep.getCodigocliente().getClientePK() != null) {
                np.setCodigoclienteId(prep.getCodigocliente().getClientePK().getCodigo());
            } else {
                np.setCodigoclienteId(prep.getCodigoclienteId() != null ? prep.getCodigoclienteId() : "");
            }
            np.setCodigobanco(prep.getCodigobanco());
            np.setComercializadora(prep.getComercializadora());
            np.setAbastecedora(prep.getAbastecedora());

            np.setActiva(true);
            np.setFacturada("NO");

            SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");

            Date fVenta = null;
            Date fDespacho = null;
            try {
                fVenta = sdfIn.parse(prep.getFechaventa());
                fDespacho = sdfIn.parse(prep.getFechadespacho());
                np.setFechaventa(sdfOut.format(fVenta));
                np.setFechadespacho(sdfOut.format(fDespacho));
            } catch (Exception e) {
                // Fallback a los valores originales
                np.setFechaventa(prep.getFechaventa());
                np.setFechadespacho(prep.getFechadespacho());
            }

            np.setCodigoautotanque(prep.getCodigoautotanque());
            np.setCedulaconductor(prep.getCedulaconductor());
            np.setNumerofacturasri("0");
            np.setRespuestageneracionoeepp(
                    prep.getRespuestageneracionoeepp() != null ? prep.getRespuestageneracionoeepp() : "");
            np.setAdelantar(prep.isAdelantar());
            np.setRespuestaanulacionoeepp(
                    prep.getRespuestaanulacionoeepp() != null ? prep.getRespuestaanulacionoeepp() : "");
            np.setTramaenviadagoe(prep.getTramaenviadagoe() != null ? prep.getTramaenviadagoe() : "");
            np.setTramarecibidagoe(prep.getTramarecibidagoe() != null ? prep.getTramarecibidagoe() : "");
            np.setTramarenviadaaoe(prep.getTramarenviadaaoe() != null ? prep.getTramarenviadaaoe() : "");
            np.setTramarecibidaaoe(prep.getTramarecibidaaoe() != null ? prep.getTramarecibidaaoe() : "");
            np.setUsuarioactual(
                    prep.getUsuarioactual() != null ? prep.getUsuarioactual() : dataUser.getUser().getNombrever());
            np.setPrefijo(prep.getPrefijo() != null ? prep.getPrefijo() : "");
            np.setObservacion("PREPEDIDO-" + prep.getPrepedidoPK().getNumero());

            ec.com.infinityone.modelo.Detallenotapedido detNP = new ec.com.infinityone.modelo.Detallenotapedido();
            ec.com.infinityone.modelo.DetallenotapedidoPK detNPK = new ec.com.infinityone.modelo.DetallenotapedidoPK();

            detNPK.setNumero("");
            detNPK.setCodigoabastecedora(prep.getPrepedidoPK().getCodigoabastecedora());
            detNPK.setCodigocomercializadora(prep.getPrepedidoPK().getCodigocomercializadora());
            detNPK.setCodigoproducto(codProductoCliente);
            detNPK.setCodigomedida(detPrep.getDetalleprepedidoPK().getCodigomedida());

            detNP.setDetallenotapedidoPK(detNPK);

            ec.com.infinityone.modelo.Producto prodFinal = new ec.com.infinityone.modelo.Producto(codProductoCliente);
            for (ec.com.infinityone.modelo.Producto p : fila.getListaProductosFiltrada()) {
                if (p.getCodigo().equals(codProductoCliente)) {
                    prodFinal = p;
                    break;
                }
            }
            detNP.setProducto(prodFinal);

            ec.com.infinityone.modelo.Medida medida = new ec.com.infinityone.modelo.Medida();
            medida.setCodigo(detPrep.getDetalleprepedidoPK().getCodigomedida());
            detNP.setMedida(medida);

            detNP.setVolumennaturalrequerido(
                    detPrep.getVolumennaturalrequerido() != null ? detPrep.getVolumennaturalrequerido()
                            : java.math.BigDecimal.ZERO);
            detNP.setVolumennaturalautorizado(
                    detPrep.getVolumennaturalautorizado() != null ? detPrep.getVolumennaturalautorizado()
                            : java.math.BigDecimal.ZERO);
            detNP.setUsuarioactual(dataUser.getUser().getNombrever());
            detNP.setCompartimento1(BigDecimal.ZERO);
            detNP.setCompartimento2(BigDecimal.ZERO);
            detNP.setCompartimento3(BigDecimal.ZERO);
            detNP.setCompartimento4(BigDecimal.ZERO);
            detNP.setCompartimento5(BigDecimal.ZERO);
            detNP.setCompartimento6(BigDecimal.ZERO);
            detNP.setCompartimento7(BigDecimal.ZERO);
            detNP.setCompartimento8(BigDecimal.ZERO);
            detNP.setCompartimento9(BigDecimal.ZERO);
            detNP.setCompartimento10(BigDecimal.ZERO);
            detNP.setSelloinicial(Integer.valueOf("0"));
            detNP.setSellofinal(Integer.valueOf("0"));

            ec.com.infinityone.modelo.EnvioPedido envioPedido = new ec.com.infinityone.modelo.EnvioPedido();
            envioPedido.setNotapedido(np);
            envioPedido.setDetalle(detNP);

            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim();
            if (prep.getComercializadora() != null
                    && Boolean.TRUE.equals(prep.getComercializadora().getGenerapedidodirecto())) {
                direcc += "ec.com.infinity.modelo.notapedido/crearyenviar";
            } else {
                direcc += "ec.com.infinity.modelo.notapedido";
            }
            URL url = new URL(direcc);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(envioPedido);
            System.out.println("FT:: JSON a enviar crearyenviar: " + jsonStr);

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

            String respuesta = contentBuilder.toString();
            JSONObject objetoJson = new JSONObject(respuesta);
            String devMsg = objetoJson.optString("developerMessage", "");
            String numeroNotaPedio = "";
            String tramaGrabada = "";

            if (devMsg != null && !devMsg.isEmpty()) {
                String[] parts = devMsg.split(";");
                if (parts.length > 0) {
                    numeroNotaPedio = parts[0];
                }
                if (parts.length > 1) {
                    tramaGrabada = parts[1];
                }
            }

            // Fallback en caso de que el backend haya cambiado y envíe el objeto en
            // "retorno"
            if (numeroNotaPedio.isEmpty() && objetoJson.has("retorno") && !objetoJson.isNull("retorno")) {
                Object retornoRaw = objetoJson.get("retorno");
                if (retornoRaw instanceof JSONObject) {
                    JSONObject retornoObj = (JSONObject) retornoRaw;
                    if (retornoObj.has("notapedidoPK") && !retornoObj.isNull("notapedidoPK")) {
                        JSONObject npPKObj = retornoObj.getJSONObject("notapedidoPK");
                        if (npPKObj.has("numero") && !npPKObj.isNull("numero")) {
                            numeroNotaPedio = npPKObj.getString("numero");
                        }
                    }
                }
            }

            if ((connection.getResponseCode() == 200 || connection.getResponseCode() == 201)
                    && !numeroNotaPedio.isEmpty()) {
                fila.setNumeroNotaPedidoGenerada(numeroNotaPedio);
                fila.getPrepedidoSolicitud().setNumeroNotaPedidoGenerada(numeroNotaPedio);
                this.dialogo(FacesMessage.SEVERITY_INFO, "NOTA DE PEDIDO REGISTRADA EXITOSAMENTE: " + numeroNotaPedio);

                if (prep.getComercializadora() != null
                        && Boolean.TRUE.equals(prep.getComercializadora().getGenerapedidodirecto())
                        && !tramaGrabada.isEmpty()) {
                    envioPedido.getNotapedido().getNotapedidoPK().setNumero(numeroNotaPedio);
                    envioPedido.getNotapedido().setTramaenviadagoe(tramaGrabada);
                    enviarOrdenEntreEpp(envioPedido, tramaGrabada);
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR NOTA DE PEDIDO");
            }

        } catch (Exception e) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL PROCESAR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getErrorStreamContent(HttpURLConnection connection) {
        StringBuilder content = new StringBuilder();
        try (java.io.InputStream errorStream = connection.getErrorStream()) {
            if (errorStream != null) {
                try (java.io.BufferedReader br = new java.io.BufferedReader(
                        new java.io.InputStreamReader(errorStream))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        content.append(line);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content.toString();
    }

    public void enviarOrdenEntreEpp(ec.com.infinityone.modelo.EnvioPedido envioPedido, String trama) {
        String codigoabastecedora = envioPedido.getNotapedido().getAbastecedora().getCodigo();
        String codigocomercializadora = envioPedido.getNotapedido().getComercializadora().getCodigo();
        String numero = envioPedido.getNotapedido().getNotapedidoPK().getNumero();
        String cadena = trama;
        try {
            String codigoPetroGenerado = "";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido/envio";
            URL url = new URL(direcc);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("codigoabastecedora", codigoabastecedora);
            obj.put("codigocomercializadora", codigocomercializadora);
            obj.put("numero", numero);
            obj.put("cadena", cadena);
            writer.write(obj.toString());
            writer.close();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String resp = "";
            while ((tmp = br.readLine()) != null) {
                resp += tmp;
            }
            JSONObject objetoJson = new JSONObject(resp);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                if (!retorno.isNull(indice)) {
                    codigoPetroGenerado = retorno.getString(indice);
                }
            }

            if (connection.getResponseCode() == 200) {
                if (codigoPetroGenerado.substring(0, 2).equals("00")) {
                    this.dialogo(FacesMessage.SEVERITY_INFO,
                            "ORDEN ENVIADA A PETRO. RESPUESTA: 00 RECIBIDA CORRECTAMENTE!");
                } else {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "ORDEN ENVIADA A PETRO. RESPUESTA: "
                            + codigoPetroGenerado.substring(0, 2) + " NO HA SIDO RECIBIDA!");
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR: NO SE HA PODIDO ENVIAR LA ORDEN A PETROECUADOR");
            }
        } catch (Throwable e) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR: NO SE HA PODIDO ENVIAR LA ORDEN A PETROECUADOR");
            e.printStackTrace();
        }
    }

    public void anularPrepedido() {
        try {
            if (this.envNP == null || this.envNP.getDetalle() == null || this.envNP.getDetalle().isEmpty()) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "No se encontró el detalle del prepedido para anular.");
                return;
            }

            ec.com.infinityone.modelo.Detalleprepedido detalle = this.envNP.getDetalle().get(0);
            Boolean estadoAnterior = detalle.getActivo();
            detalle.setActivo(false);

            if (detalle.getCompartimento1() == null)
                detalle.setCompartimento1(java.math.BigDecimal.ZERO);
            if (detalle.getCompartimento2() == null)
                detalle.setCompartimento2(java.math.BigDecimal.ZERO);
            if (detalle.getCompartimento3() == null)
                detalle.setCompartimento3(java.math.BigDecimal.ZERO);
            if (detalle.getCompartimento4() == null)
                detalle.setCompartimento4(java.math.BigDecimal.ZERO);
            if (detalle.getCompartimento5() == null)
                detalle.setCompartimento5(java.math.BigDecimal.ZERO);
            if (detalle.getCompartimento6() == null)
                detalle.setCompartimento6(java.math.BigDecimal.ZERO);
            if (detalle.getCompartimento7() == null)
                detalle.setCompartimento7(java.math.BigDecimal.ZERO);
            if (detalle.getCompartimento8() == null)
                detalle.setCompartimento8(java.math.BigDecimal.ZERO);
            if (detalle.getCompartimento9() == null)
                detalle.setCompartimento9(java.math.BigDecimal.ZERO);
            if (detalle.getCompartimento10() == null)
                detalle.setCompartimento10(java.math.BigDecimal.ZERO);
            if (detalle.getSelloinicial() == null)
                detalle.setSelloinicial(0);
            if (detalle.getSellofinal() == null)
                detalle.setSellofinal(0);

            if (detalle.getUsuarioactual() == null || detalle.getUsuarioactual().isEmpty()) {
                detalle.setUsuarioactual(
                        dataUser != null && dataUser.getUser() != null ? dataUser.getUser().getNombrever() : "ADMIN");
            }

            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim()
                    + "ec.com.infinity.modelo.detalleprepedido/porId";
            URL url = new URL(direcc);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String jsonStr = mapper.writeValueAsString(detalle);

            try (java.io.OutputStreamWriter out = new java.io.OutputStreamWriter(connection.getOutputStream(),
                    "UTF-8")) {
                out.write(jsonStr);
                out.flush();
            }

            if (connection.getResponseCode() == 200 || connection.getResponseCode() == 204) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "REGISTRO ANULADO EXITOSAMENTE");
            } else {
                String errorInfo = getErrorStreamContent(connection);
                String errorMsg = "ERROR AL ANULAR REGISTRO: HTTP " + connection.getResponseCode() + " " + errorInfo;
                this.dialogo(FacesMessage.SEVERITY_ERROR, errorMsg);
                System.out.println("FT:: ERROR EN anularPrepedido RESPONSECODE " + connection.getResponseCode());
                // Revertir estado si falló
                detalle.setActivo(estadoAnterior);
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Error al anular: " + e.getMessage());
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
                        if (!com.isNull("generapedidodirecto")) {
                            comerc.setGenerapedidodirecto(com.getBoolean("generapedidodirecto"));
                        }

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

    public void imprimirNotaPedido(PrepedidoSolicitud envP) {
        if (envP == null || envP.getNumeroNotaPedidoGenerada() == null || envP.getNumeroNotaPedidoGenerada().equals("0")
                || envP.getNumeroNotaPedidoGenerada().equals("00") || envP.getNumeroNotaPedidoGenerada().isEmpty()) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "No se ha generado la nota de pedido para este prepedido.");
            return;
        }
        try {
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim()
                    + "ec.com.infinity.modelo.notapedido/poridimpresion?";
            url = new URL(direcc + "codigoabastecedora="
                    + envP.getPrepedido().getPrepedidoPK().getCodigoabastecedora()
                    + "&codigocomercializadora=" + envP.getPrepedido().getPrepedidoPK().getCodigocomercializadora()
                    + "&numero=" + envP.getNumeroNotaPedidoGenerada());
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

            if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201) {
                JSONObject objetoJson = new JSONObject(respuesta);
                JSONArray retorno = objetoJson.getJSONArray("retorno");
                if (retorno.length() > 0) {
                    JSONObject envioPedidoJson = retorno.getJSONObject(0);

                    try {
                        if (envioPedidoJson.has("notapedido") && !envioPedidoJson.isNull("notapedido")) {
                            JSONObject npJson = envioPedidoJson.getJSONObject("notapedido");
                            if (npJson.has("codigocliente") && !npJson.isNull("codigocliente")) {
                                JSONObject cliJson = npJson.getJSONObject("codigocliente");
                                
                                if (cliJson.optJSONObject("codigodireccioninen") != null) {
                                    cliJson.put("codigodireccioninen", cliJson.getJSONObject("codigodireccioninen").optString("codigo"));
                                }
                                if (cliJson.optJSONObject("codigotipocliente") != null) {
                                    cliJson.put("codigotipocliente", cliJson.getJSONObject("codigotipocliente").optString("codigo"));
                                }
                                if (cliJson.optJSONObject("codigobancodebito") != null) {
                                    cliJson.put("codigobancodebito", cliJson.getJSONObject("codigobancodebito").optString("codigo"));
                                }
                            }
                        }

                        if (envioPedidoJson.has("detalle") && !envioPedidoJson.isNull("detalle")) {
                            JSONObject detalleJson = envioPedidoJson.getJSONObject("detalle");
                            if (detalleJson.has("producto") && !detalleJson.isNull("producto")) {
                                JSONObject prodJson = detalleJson.getJSONObject("producto");
                                if (prodJson.optJSONObject("codigoareamercadeo") != null) {
                                    prodJson.put("codigoareamercadeo", prodJson.getJSONObject("codigoareamercadeo").optString("codigo"));
                                }
                                if (prodJson.optJSONObject("codigostc") != null) {
                                    prodJson.put("codigostc", prodJson.getJSONObject("codigostc").optString("codigo"));
                                }
                                if (prodJson.optJSONObject("codigoarch") != null) {
                                    prodJson.put("codigoarch", prodJson.getJSONObject("codigoarch").optString("codigo"));
                                }
                                if (prodJson.optJSONObject("productogenerico") != null) {
                                    prodJson.put("productogenerico", prodJson.getJSONObject("productogenerico").optString("codigo"));
                                }
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("Error preprocesando JSON en imprimirNotaPedido: " + ex.getMessage());
                    }

                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                            false);
                    ec.com.infinityone.modelo.EnvioPedido envioPedido = mapper.readValue(envioPedidoJson.toString(),
                            ec.com.infinityone.modelo.EnvioPedido.class);
                    generarReporteNp(envioPedido);
                } else {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "No se encontró la información de la Nota de Pedido.");
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR,
                        "Error al consultar la Nota de Pedido HTTP " + connection.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Error de conexión al obtener Nota de Pedido: " + e.getMessage());
        }
    }

    public String imprimirNotaPedidoPreview(PrepedidoSolicitud prepedidoItem) {
        imprimirNotaPedido(prepedidoItem);
        if (pdfStream != null && pdfStream.getStream() != null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/pdf");
            externalContext.setResponseHeader("Content-Disposition", "inline; filename=\"" + pdfStream.getName() + "\"");
            try {
                InputStream is = pdfStream.getStream();
                java.io.OutputStream os = externalContext.getResponseOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
                facesContext.responseComplete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void generarReporteNp(ec.com.infinityone.modelo.EnvioPedido envP) {
        String rutaGuardar = Fichero.getCARPETAREPORTES();
        String subreport = Fichero.getCARPETAREPORTES() + "/notapedidoext.jrxml";
        String path = Fichero.getCARPETAREPORTES() + "/FormatoNotaPedido.jrxml";
        InputStream file = null;
        try {
            file = new FileInputStream(new File(path));
            net.sf.jasperreports.engine.JasperReport reporte = net.sf.jasperreports.engine.JasperCompileManager
                    .compileReport(file);
            net.sf.jasperreports.engine.JasperReport subreporte = net.sf.jasperreports.engine.JasperCompileManager
                    .compileReport(subreport);
            BufferedImage image = javax.imageio.ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/logo"
                    + envP.getNotapedido().getNotapedidoPK().getCodigocomercializadora() + ".jpeg"));
            Map parametro = new HashMap();
            parametro.put("subReporte", subreporte);
            parametro.put("codComer", envP.getNotapedido().getNotapedidoPK().getCodigocomercializadora());
            parametro.put("numeroNotaPedido", envP.getNotapedido().getNotapedidoPK().getNumero());
            parametro.put("logo", image);
            Connection conexion = conexionJasperBD();
            net.sf.jasperreports.engine.JasperPrint print = net.sf.jasperreports.engine.JasperFillManager
                    .fillReport(reporte, parametro, conexion);
            File directory = new File(rutaGuardar);
            String nombreDocumento = "reporteNotaPedido";
            File pdf = File.createTempFile(nombreDocumento + "_", ".pdf", directory);
            net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
            File initialFile = new File(pdf.getAbsolutePath());
            InputStream targetStream = new FileInputStream(initialFile);
            pdfStream = new org.primefaces.model.DefaultStreamedContent(targetStream, "application/pdf",
                    nombreDocumento + envP.getNotapedido().getNotapedidoPK().getNumero() + ".pdf");
        } catch (Exception ex) {
            System.out.println("Excepcion Reporte: " + ex);
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
                                if (det.getVolumennaturalautorizado() == null
                                        || det.getVolumennaturalautorizado().compareTo(BigDecimal.ZERO) <= 0) {
                                    // Simplemente ignoramos los productos a los que no se les asignó volumen, no es
                                    // un error
                                    continue;
                                }
                                String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim()
                                        + "ec.com.infinity.modelo.detalleprepedido/porId";
                                URL url = new URL(direcc);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setDoOutput(true);
                                connection.setRequestMethod("PUT");
                                connection.setRequestProperty("Content-type", "application/json");

                                JSONObject jsonPayload = new JSONObject();
                                JSONObject pkJson = new JSONObject();
                                pkJson.put("codigoabastecedora",
                                        pSolicitud.getPrepedido().getPrepedidoPK().getCodigoabastecedora());
                                pkJson.put("codigocomercializadora",
                                        pSolicitud.getPrepedido().getPrepedidoPK().getCodigocomercializadora());
                                pkJson.put("numero", pSolicitud.getPrepedido().getPrepedidoPK().getNumero());
                                pkJson.put("codigoproducto", det.getProducto().getCodigo());

                                String codigoMedida = "01";
                                if (det.getDetalleprepedidoPK() != null
                                        && det.getDetalleprepedidoPK().getCodigomedida() != null) {
                                    codigoMedida = det.getDetalleprepedidoPK().getCodigomedida();
                                }
                                pkJson.put("codigomedida", codigoMedida);
                                jsonPayload.put("detalleprepedidoPK", pkJson);

                                jsonPayload.put("volumennaturalrequerido",
                                        det.getVolumennaturalrequerido() != null ? det.getVolumennaturalrequerido()
                                                : BigDecimal.ZERO);
                                jsonPayload.put("volumennaturalautorizado",
                                        det.getVolumennaturalautorizado() != null ? det.getVolumennaturalautorizado()
                                                : BigDecimal.ZERO);

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

                                // Nuevos campos requeridos por el servicio web
                                jsonPayload.put("activo", pSolicitud.getPrepedido().isActiva());
                                jsonPayload.put("autorizado", "SI");
                                jsonPayload.put("numeronp", "0");

                                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                                out.write(jsonPayload.toString());
                                out.close();

                                if (connection.getResponseCode() == 200 || connection.getResponseCode() == 204) {
                                    processados = true;
                                } else {
                                    String errResponse = getErrorStreamContent(connection);
                                    this.dialogo(FacesMessage.SEVERITY_ERROR,
                                            "ERROR HTTP " + connection.getResponseCode() + " al autorizar "
                                                    + det.getProducto().getNombre() + ": " + errResponse);
                                    System.out.println("Error procesando autorización: " + connection.getResponseCode()
                                            + " " + connection.getResponseMessage() + " " + errResponse);
                                }
                            }
                        }
                    }
                }
            }
            if (processados) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "REGISTROS PROCESADOS EXITOSAMENTE");
                if (this.envNP != null) {
                    this.envNP.setEstadoForzado("SI");
                    this.envNP.setAutorizar(false);
                }
                // No recargar la tabla entera para que las filas no se muevan de su posición
            } else {
                this.dialogo(FacesMessage.SEVERITY_WARN,
                        "NO SE PROCESÓ NINGÚN REGISTRO DEBIDO A ERRORES O PORQUE NO SE ASIGNARON VOLÚMENES.");
                if (this.envNP != null) {
                    this.envNP.setAutorizar(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL PROCESAR");
        }
    }
}
