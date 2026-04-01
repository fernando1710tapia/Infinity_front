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
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.ClientePK;
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
public class NotapedidoBeanDirecto extends ReusableBean implements Serializable {

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

    private boolean todosClientes;

    private String nomBanco;

    private String numCuenta;

    private String numCheque;

    /**
     * Constructor por defecto
     */
    public NotapedidoBeanDirecto() {
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
        obtenerTerminales();
        obtenerComercializadora();
        todosClientes = false;

        //habilitarBusqueda();
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
        nomBanco = "";
        numCuenta = "";
        numCheque = "";
        fecha = new Date();
        oeenpetro = "";
        np = new Notapedido();
        npPK = new NotapedidoPK();
        detNP = new Detallenotapedido();
        detNPK = new DetallenotapedidoPK();
        envNP = new EnvioPedido();
        notaPedidoAuxiliar = new Notapedido();
        notaPedidoAuxiliarPK = new NotapedidoPK();
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
        if (cliente != null) {
            codCliente = cliente.getClientePK().getCodigo();
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
            codCliente = cliente.getClientePK().getCodigo();
            listaProductos = new ArrayList<>();
            listaProductos = cliProdServicio.obtenerProductos(codComer, codCliente);
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
                            if (listaClientes.get(i).getClientePK().getCodigo().equals(dataUser.getUser().getCodigocliente())) {
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
                            
                            
//////FTFT                            cliente.setCodigo(cli.getString("codigo"));

                            cliente.setClientePK(new ClientePK());
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
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");        
        SimpleDateFormat fechFormat = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
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
//                np.setCodigoautotanque(autotanque.getPlaca());
//                np.setCedulaconductor(autotanque.getCedularuc().getCedularuc());

                detNPK.setNumero("");

                detNP.setDetallenotapedidoPK(detNPK);
                detNP.setVolumennaturalautorizado(detNP.getVolumennaturalrequerido());
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
                detNP.setSelloinicial(Integer.valueOf("0") );
                detNP.setSellofinal(Integer.valueOf("0"));


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
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido/crearyenviar";
            url = new URL(direcc);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(envNP);
            
            System.out.println("FT:: addItems OBJETO NOTAPEDIDO "+ jsonStr);
            
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
//FTFT                 enviarOrdenPetro(envNP, numeroFactura);
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println("FT:: ERROR EN addItems RESPONSECODE "+connection.getResponseCode());
                System.out.println("FT:: ERROR EN addItems RESPONSEMESSAGE "+connection.getResponseMessage());
            }
            
            //enviarOrdenPetro(envNP, numeroFactura);

        } catch (IOException e) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR NOTA DE PEDIDO: " + e);
            e.printStackTrace();
        }
    }
    
    public void enviarOrdenPetro(EnvioPedido envNP, String numFact) {
        if (envNP.getNotapedido().getFechadespacho().equals(envNP.getNotapedido().getFechaventa())) {
            obtenerTramaOrdenEntrega(envNP, numFact);
        } else if (!envNP.getNotapedido().getFechadespacho().equals(envNP.getNotapedido().getFechaventa()) && envNP.getNotapedido().isAdelantar()) {
            obtenerTramaOrdenEntrega(envNP, numFact);
        }
    }
    
        public void obtenerTramaOrdenEntrega(EnvioPedido envNP, String numFact) {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd 12:00:00");
        Notapedido notaPedido = new Notapedido();
        NotapedidoPK notaPedidoPK = new NotapedidoPK();
        Detallenotapedido detalleNotaP = new Detallenotapedido();
        DetallenotapedidoPK detalleNotaPK = new DetallenotapedidoPK();
        EnvioPedido envioPedido = new EnvioPedido();
        String fechad = envNP.getNotapedido().getFechadespacho();
        String feachaDespacho = fechad.replace('/', '-') + "T12:00:00";
        String fechav = envNP.getNotapedido().getFechaventa();
        String feachaVenta = fechav.replace('/', '-') + "T12:00:00";
        notaPedidoPK.setCodigoabastecedora(envNP.getNotapedido().getAbastecedora().getCodigo());
        notaPedidoPK.setCodigocomercializadora(envNP.getNotapedido().getComercializadora().getCodigo());
        notaPedidoPK.setNumero(envNP.getNotapedido().getNotapedidoPK().getNumero());
        notaPedido.setNotapedidoPK(notaPedidoPK);
        notaPedido.setCodigoclienteId(envNP.getNotapedido().getCodigocliente().getClientePK().getCodigo());
        notaPedido.setFechaventa(feachaVenta);
        notaPedido.setFechadespacho(feachaDespacho);
        notaPedido.setActiva(envNP.getNotapedido().isActiva());
        notaPedido.setCodigoautotanque(envNP.getNotapedido().getCodigoautotanque());
        notaPedido.setCedulaconductor(envNP.getNotapedido().getCedulaconductor());
        notaPedido.setNumerofacturasri("0");
        notaPedido.setRespuestageneracionoeepp("");
        notaPedido.setObservacion(envNP.getNotapedido().getObservacion());
        notaPedido.setAdelantar(envNP.getNotapedido().isAdelantar());
        notaPedido.setRespuestaanulacionoeepp("");
        notaPedido.setTramaenviadagoe("");
        notaPedido.setTramarecibidagoe("");
        notaPedido.setTramarenviadaaoe("");
        notaPedido.setTramarecibidaaoe("");
        notaPedido.setUsuarioactual(dataUser.getUser().getNombrever());
        notaPedido.setPrefijo(envNP.getNotapedido().getPrefijo());
        notaPedido.setAbastecedora(envNP.getNotapedido().getAbastecedora());
        notaPedido.setCodigobanco(envNP.getNotapedido().getCodigobanco());
        notaPedido.setCodigocliente(envNP.getNotapedido().getCodigocliente());
        notaPedido.setComercializadora(envNP.getNotapedido().getComercializadora());
        notaPedido.setCodigoterminal(envNP.getNotapedido().getCodigoterminal());

        detalleNotaPK.setCodigoabastecedora(envNP.getNotapedido().getAbastecedora().getCodigo());
        detalleNotaPK.setCodigocomercializadora(envNP.getNotapedido().getComercializadora().getCodigo());
        detalleNotaPK.setNumero(envNP.getNotapedido().getNotapedidoPK().getNumero());
        detalleNotaPK.setCodigoproducto(envNP.getDetalle().getDetallenotapedidoPK().getCodigoproducto());
        detalleNotaPK.setCodigomedida(envNP.getDetalle().getDetallenotapedidoPK().getCodigomedida());
        detalleNotaP.setDetallenotapedidoPK(detalleNotaPK);

        detalleNotaP.setVolumennaturalrequerido(envNP.getDetalle().getVolumennaturalrequerido());
        detalleNotaP.setVolumennaturalautorizado(envNP.getDetalle().getVolumennaturalautorizado());
        detalleNotaP.setMedida(envNP.getDetalle().getMedida());
        detalleNotaP.setProducto(envNP.getDetalle().getProducto());
        detalleNotaP.setUsuarioactual(dataUser.getUser().getNombrever());
        detalleNotaP.setSelloinicial(envNP.getDetalle().getSelloinicial());
        detalleNotaP.setSellofinal(envNP.getDetalle().getSellofinal());

        detalleNotaP.setCompartimento1(envNP.getDetalle().getCompartimento1());
        detalleNotaP.setCompartimento2(envNP.getDetalle().getCompartimento2());
        detalleNotaP.setCompartimento3(envNP.getDetalle().getCompartimento3());
        detalleNotaP.setCompartimento4(envNP.getDetalle().getCompartimento4());
        detalleNotaP.setCompartimento5(envNP.getDetalle().getCompartimento5());
        detalleNotaP.setCompartimento6(envNP.getDetalle().getCompartimento6());
        detalleNotaP.setCompartimento7(envNP.getDetalle().getCompartimento7());
        detalleNotaP.setCompartimento8(envNP.getDetalle().getCompartimento8());
        detalleNotaP.setCompartimento9(envNP.getDetalle().getCompartimento9());
        detalleNotaP.setCompartimento10(envNP.getDetalle().getCompartimento10());

        envioPedido.setNotapedido(notaPedido);
        envioPedido.setDetalle(detalleNotaP);
        getTrama(envioPedido, numFact);
    }

    public void getTrama(EnvioPedido envioPedido, String numFact) {
        try {
            String respuesta = "";
            String trama = "";
            int facturasProrrogadasCaidas = 0;
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/tramaOE?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido/tramaOE?";
            url = new URL(direcc + "nfactura=" + numFact + "&clave=" + envioPedido.getNotapedido().getComercializadora().getClavewsepp());
            //envioPedido.getNotapedido().getNumerofacturasri()
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            Gson gson = new Gson();
            String JSON = gson.toJson(envioPedido);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(JSON.getBytes());
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
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                if (!retorno.isNull(indice)) {
                    String trm = retorno.getString(indice);
                    trama = trm;
                }
            }

            if (connection.getResponseCode() == 200) {
                //this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA REGISTRADA EXITOSAMENTE");
                envioPedido.getNotapedido().setTramaenviadagoe(trama);
                envioPedido.getNotapedido().setNumerofacturasri(numFact);
                envioPedido.getNotapedido().setActiva(true);
                editarNotaPedido(envioPedido.getNotapedido());
                
//////////////                System.out.println("FT:. ENVIANDO ORIGINALMENTE getTrama:. envioPedido.getNotapedido().getCodigocliente().getControlaprorroga(). "+envioPedido.getNotapedido().getCodigocliente().getControlaprorroga());
//////////////                if (envioPedido.getNotapedido().getCodigocliente().getControlaprorroga()) {
//////////////                    facturasProrrogadasCaidas = controlarProrroga(envioPedido.getNotapedido().getNotapedidoPK().getCodigocomercializadora(), envioPedido.getNotapedido().getCodigocliente().getClientePK().getCodigo());
//////////////                } else {
//////////////                    facturasProrrogadasCaidas = 0;
//////////////                }
//////////////                if (0 == facturasProrrogadasCaidas) {
//////////////                    enviarOrdenEntreEpp(envioPedido, trama);
//////////////                } else {
//////////////
//////////////                    this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ENVIAR ESTA FACTURA HACIA PETROECUADOR: . SE HAN ENCONTRADO " + facturasProrrogadasCaidas + " FACTURAS PRORROGADAS VENCIDAS Y NO PAGADAS - CONSULTE CON LOS ADMINISTRADORES DEL SISTEMA");
//////////////
//////////////                }

            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE LOGRÓ GENERAR LA TRAMA DE LA ORDEN DE ENTREGA PARA PETROECUADOR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public void editarNotaPedido(Notapedido notaPedidoAuxiliar) {
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
                //this.dialogo(FacesMessage.SEVERITY_INFO, "TRAMA INGRESADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL INGRESAR TRAMA");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
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
            Logger.getLogger(NotapedidoBeanDirecto.class.getName()).log(Level.SEVERE, null, ex);
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
                        
//////FTFT                        cliente.setCodigo(cli.getString("codigo"));
                        
                        cliente.setClientePK(new ClientePK());
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
        String path = Fichero.getCARPETAREPORTES() + "/notapedido.jrxml";
        System.out.println("PATH:" + path);
        InputStream file = null;
        try {
            file = new FileInputStream(new File(path));

            JasperReport reporte = JasperCompileManager.compileReport(file);
            BufferedImage image = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/logo"+envP.getNotapedido().getNotapedidoPK().getCodigocomercializadora()+".jpeg"));
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
////ftftft        String subreport = Fichero.getCARPETAREPORTES() + "/notapedido.jrxml";
        String subreport = Fichero.getCARPETAREPORTES() + "/notapedidoext.jrxml";
        String path = Fichero.getCARPETAREPORTES() + "/FormatoNotaPedido.jrxml";
        System.out.println("PATH:" + path);
        InputStream file = null;
        try {
            file = new FileInputStream(new File(path));

            JasperReport reporte = JasperCompileManager.compileReport(file);
            JasperReport subreporte = JasperCompileManager.compileReport(subreport);
            BufferedImage image = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/logo"+envP.getNotapedido().getNotapedidoPK().getCodigocomercializadora()+".jpeg"));
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

}
