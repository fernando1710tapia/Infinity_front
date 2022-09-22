/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.pedidosyfacturacion.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ec.com.infinityone.actorcomercial.serivicios.ClienteServicio;
import ec.com.infinityone.actorcomercial.serivicios.ComercializadoraServicio;
import ec.com.infinityone.bean.TerminalBean;
import ec.com.infinityone.catalogo.servicios.TerminalServicio;
import ec.com.infinityone.modeloWeb.Abastecedora;
import ec.com.infinityone.modeloWeb.Banco;
import ec.com.infinityone.modeloWeb.Cliente;
import ec.com.infinityone.modeloWeb.Comercializadora;
import ec.com.infinityone.modeloWeb.Detallefactura;
import ec.com.infinityone.modeloWeb.DetallefacturaPK;
import ec.com.infinityone.modeloWeb.Detallenotapedido;
import ec.com.infinityone.modeloWeb.DetallenotapedidoPK;
import ec.com.infinityone.modeloWeb.Detalleprecio;
import ec.com.infinityone.modeloWeb.DetalleprecioPK;
import ec.com.infinityone.modeloWeb.EnvioFactura;
import ec.com.infinityone.modeloWeb.EnvioPedido;
import ec.com.infinityone.modeloWeb.Factura;
import ec.com.infinityone.modeloWeb.FacturaPK;
import ec.com.infinityone.modeloWeb.Formapago;
import ec.com.infinityone.modeloWeb.Medida;
import ec.com.infinityone.modeloWeb.NotaPedidoSOAP;
import ec.com.infinityone.modeloWeb.Notapedido;
import ec.com.infinityone.modeloWeb.NotapedidoPK;
import ec.com.infinityone.modeloWeb.Precio;
import ec.com.infinityone.modeloWeb.PrecioPK;
import ec.com.infinityone.modeloWeb.Producto;
import ec.com.infinityone.modeloWeb.Terminal;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.actorcomercial.bean.ComercializadoraBean;
import ec.com.infinityone.catalogo.servicios.BancoServicio;
import ec.com.infinityone.catalogo.servicios.FormapagoServicio;
import ec.com.infinityone.configuration.Fichero;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
import javax.faces.context.FacesContext;
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
 * @author David
 */
@Named
@ViewScoped
public class FacturacionBean extends ReusableBean implements Serializable {

    protected static final Logger LOG = Logger.getLogger(FacturacionBean.class.getName());

    /*
    Variable para acceder a los servicios de Comercialziadora
     */
    @Inject
    protected ComercializadoraServicio comerServicio;
    /*
    Variable para acceder a los servicios de Terminal
     */
    @Inject
    protected TerminalServicio termServicio;
    /*
    Variable para acceder a los servicios de Cliente
     */
    @Inject
    protected ClienteServicio clienteServicio;
    /*
    Variable para acceder a los servicios de Forma de pago
     */
    @Inject
    private FormapagoServicio formapagoServicio;
    /*
    Variable para acceder a los servicios de Banco
     */
    @Inject
    private BancoServicio bancoServicio;
    /*
    Variable Comercializadora
     */
    protected ComercializadoraBean comercializadora;
    /*
    Variable Comercializadora
     */
    protected TerminalBean terminal;
    /*
    Variable para renderizar la pantalla
     */
    protected boolean mostarFactura;
    /*
    Variable para renderizar la pantalla
     */
    protected boolean mostarPantallaInicial;
    /*
    Variable para almacenar los datos comercializadora
     */
    protected List<ComercializadoraBean> listaComercializadora;
    /*
    Variable para almacenar los datos comercializadora
     */
    protected List<TerminalBean> listaTermianles;
    /*
    Variable para almacenar los datos clientes
     */
    protected List<Cliente> listaClientes;
    /*
    Variable que almacena el código de la comercializadora
     */
    protected String codComer;
    /*
    Variable que almacena el código del terminal
     */
    protected String codTerminal;
    /*
    Variable que almacena el código del cliente
     */
    protected String codCliente;
    /*
    Variable que almacena el código de la abastecedora
     */
    protected String codAbas;

    /*
    Variable para almacenar las notas de pedido
     */
    protected List<FacturacionBean> listaFacturas;
    /*
    Varaible para guardar la selección del radio button
     */
    protected String tipoFecha;
    /**
     * Variable que permite establecer la fecha inicial para realizar la
     * busqueda de facturas
     */
    protected Date fechaI;
    /**
     * Variable que permite establecer la fecha final para realizar la busqueda
     * de facturas
     */
    protected Date fechaf;
    /**
     * Variable que permite establecer la fecha prorroga para realizar la
     * busqueda de facturas
     */
    protected Date fechaPro;
    /*
    Variable que isntacia el modelo Factura
     */
    protected Factura fact;
    /*
    Variable que isntacia el modelo FacturaPK
     */
    protected FacturaPK factPk;
    /*
    Variable que isntacia el modelo DeatlleFactura
     */
    protected Detallefactura detFac;
    /*
    Variable que isntacia el modelo DeatlleFacturaPK
     */
    protected DetallefacturaPK detFacPK;
    /*
    Variable para guardar Factura y Detalle Factura
     */
    protected EnvioFactura envF;
    /*
    Variable auxiliar para guardar Factura y Detalle Factura
     */
    protected EnvioFactura envFauxiliar;
    /*
    Variable para guardar una listda de Factura y Detalle Factura
     */
    protected List<EnvioFactura> listenvF;
    /*
    Variable para guardar una listda de Factura y Detalle Factura
     */
    protected List<Factura> listFact;
    /*
    Variable para guardar una listda de Factura y Detalle Factura
     */
    protected List<Factura> listFactSelec;
    /*
    Variable para guardar una lista deDeatllesFactura
     */
    protected List<Detallefactura> listDet;
    /*
    Variable para establoecer el valor de oeenpetro
     */
    protected String oeenpetro;
    /*
    Variable Nota Pedido
     */
    protected Notapedido np;
    /*
    Varibale Nota Pedido PK
     */
    protected NotapedidoPK npPK;
    /*
    Variable que isntacia el modelo NotapedidoPK
     */
    protected Detallenotapedido detNP;
    /*
    Variable para guardar una lista deDeatllesFactura
     */
    protected List<Detallenotapedido> listDetNP;
    /*
    Variable que isntacia el modelo DetallenotapedidoPK
     */
    protected DetallenotapedidoPK detNPK;
    /*
    Variable para guardar Nota y Detalle Pedido
     */
    protected EnvioPedido envNP;
    /*
    Variable para guardar una lista de Nota y Detalle Pedido
     */
    protected List<EnvioPedido> listenvNP;
    /*
    Variable auxiliar para guardar notas de pedido que aún no estan procesadas
     */
    protected List<EnvioPedido> listenvNPAuxilia;
    /*
    Variable para identificar la fila de una DataTable
     */
    protected int rowId;
    /*
    Variable para identificar la acciòn de adelantar en la DataTable
     */
    protected boolean adelantar;
    /*
    Variable para identificar la acciòn de procesar en la DataTable
     */
    protected boolean procesar;
    /*
    Variable Cliente
     */
    protected Cliente cliente;
    /*
    Variable terminal 
     */
    protected Terminal terminalT;
    /*
    Variable Banco
     */
    protected Banco banco;
    /*
    Variable Formapago
     */
    protected Formapago formap;
    /*
    Variable Comercializadora
     */
    protected Comercializadora comerc;
    /*
    Variable Abastecedora
     */
    protected Abastecedora abas;
    /*
    Variable precio
     */
    protected Precio precio;
    /*
    Variable PrecioPk
     */
    protected PrecioPK precioPK;
    /*
    Variable Detalleprecio
     */
    protected Detalleprecio detallePrecio;
    /*
    Variable DetalleprecioPK
     */
    protected DetalleprecioPK detallePrecioPK;
    /*
    Variable Medida
     */
    protected Medida medida;
    /*
    Variable producto
     */
    protected Producto producto;

    /*
    Variable TablaFactura para URL
     */
    protected String tablaFactura = ".factura";

    /*
    Variable TablaNotaPedido para URL
     */
    protected String tablaNotaPedido = ".notapedido";
    /*
    Variable para identificar si esta en Sri o no
     */
    protected String ensri;
    /*
    Variable para guardar el error de envío a petro
     */
    protected String errorPetro;
    /*
    Variable para mostrar el mensaje en sri
     */
    protected String sriMensaje;
    /*
    Varaibale para guardar los datos de anulación
     */
    protected NotaPedidoSOAP anulacion;
    /*
    Variable para ejecutar la acciión solo por procesar
     */
    protected boolean soloporProcesar;
    /*
    Variable para almacenar el mensaje de anulación de facturas
     */
    protected String mensajeAnulacion;
    /*
    Variable para ver el estado de anulación de una factura
     */
    protected boolean estadoAnulacion;
    /*
    Variable para guardar una factura consultada para la anulación
     */
    protected Factura facturaauxiliar;
    /*
    Variable para guardar una facturaPK consultada para la anulación
     */
    protected FacturaPK facturaauxiliarPK;
    /*
    Vairbale para almacenar el pdf generado
     */
    protected StreamedContent pdfStream;
    /*
    Variable para almacenar el número de la factura
     */
    protected String numFactura;
    /*
    Variable para activar la pantalla de buscar facturas por cliente
     */
    protected boolean buscarFacturaXCliente;
    /*
    Vaiable auxiliar de envio pedido para el reenvio a petro
     */
    protected EnvioPedido envioPedidoAuxiliar;
    /*
    Opcion para habilitar el boton de procesar en refactura
     */
    protected boolean process;
    /*
    Objeto formapago
     */
    private Formapago formapago;
    /*
    Variable que almacena varios Formapagos
     */
    private List<Formapago> listaFormapagos;
    /*
    Lista Bancos
     */
    private List<Banco> listaBancos;

    protected Date fechaActual;

    /**
     * Constructor por defecto
     */
    public FacturacionBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        reestablecer();
        mostarFactura = false;
        mostarPantallaInicial = true;
        buscarFacturaXCliente = false;
        obtenerTerminales();
        obtenerComercializadora();
        obtenerFormapago();
        obtenerBancos();
        fechaProrroga();
        //obtenerClientes();
    }

    public void nuevaFactura() {
        reestablecer();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        } else {
            seleccionarComercializdora();
        }
//        if (terminal != null) {
//            terminal = new TerminalBean();
//        } else {
//            seleccionarTerminal();
//        }
        if (habilitarCli) {
            cliente = new Cliente();
        }

        mostarFactura = true;
        mostarPantallaInicial = false;
    }

    public void buscarFacturaCliente() {
        reestablecer();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        }
        if (habilitarCli) {
            cliente = new Cliente();
        }
        mostarFactura = false;
        mostarPantallaInicial = false;
        buscarFacturaXCliente = true;
    }

    public void regresar() {
        mostarFactura = false;
        mostarPantallaInicial = true;
        buscarFacturaXCliente = false;
    }

    public void reestablecer() {
        numFactura = "";
        codComer = "";
        codTerminal = "-1";
        codCliente = "-1";
        codAbas = "";
        tipoFecha = "1";
        ensri = "";
        //fechaI = new Date();
        oeenpetro = "";
        errorPetro = "";
        sriMensaje = "";
        mensajeAnulacion = "";
        fact = new Factura();
        factPk = new FacturaPK();
        envF = new EnvioFactura();
        envFauxiliar = new EnvioFactura();
        detFac = new Detallefactura();
        detFacPK = new DetallefacturaPK();
        np = new Notapedido();
        npPK = new NotapedidoPK();
        detNP = new Detallenotapedido();
        detNPK = new DetallenotapedidoPK();
        envNP = new EnvioPedido();
        envioPedidoAuxiliar = new EnvioPedido();
        cliente = new Cliente();
        terminalT = new Terminal();
        formap = new Formapago();
        banco = new Banco();
        comerc = new Comercializadora();
        abas = new Abastecedora();
        precio = new Precio();
        precioPK = new PrecioPK();
        detallePrecio = new Detalleprecio();
        detallePrecioPK = new DetalleprecioPK();
        medida = new Medida();
        producto = new Producto();
        listenvNP = new ArrayList<>();
        listenvNPAuxilia = new ArrayList<>();
        listenvF = new ArrayList<>();
        listFact = new ArrayList<>();
        listFactSelec = new ArrayList<>();
        anulacion = new NotaPedidoSOAP();
        soloporProcesar = false;
        estadoAnulacion = false;
        facturaauxiliar = new Factura();
        facturaauxiliarPK = new FacturaPK();
        formapago = new Formapago();
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo";
        process = false;
    }

    private void fechaProrroga() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1);
        fechaActual = c.getTime();
    }

    private void obtenerBancos() {
        listaBancos = new ArrayList<>();
        listaBancos = bancoServicio.obtenerBanco();
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = comerServicio.obtenerComercializadorasActivas();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda();
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

    public void obtenerFormapago() {
        listaFormapagos = new ArrayList<>();
        listaFormapagos = this.formapagoServicio.obtenerFormapago();
    }

    public void seleccionarComer() {
        if (comercializadora != null) {
            codComer = comercializadora.getCodigo();
            codAbas = comercializadora.getAbastecedora();
            listaClientes = new ArrayList<>();
            listaClientes = clienteServicio.obtenerClientesPorComercializadora(codComer);
        }
    }

    public void seleccionarComercializdora() {
        if (comercializadora != null) {
            if (comercializadora.getActivo().equals("S")) {
                factPk.setCodigocomercializadora(comercializadora.getCodigo());
                factPk.setCodigoabastecedora(comercializadora.getAbastecedora());
                fact.setFacturaPK(factPk);
                codComer = comercializadora.getCodigo();
                codAbas = comercializadora.getAbastecedora();
                //factura.setCodComer(comercializadora.getCodigo());
                //factura.setCodAbas(comercializadora.getAbastecedora());
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA SE ENCUENTRA INACTIVA");
            }
        }
    }

    public void seleccionarTerminal() {
        if (terminal != null) {
            fact.setCodigoterminal(terminal.getCodigo());
            codTerminal = terminal.getCodigo();
            List<Cliente> listaClientesAux = new ArrayList<>();
            listaClientesAux = clienteServicio.obtenerClientesPorComercializadora(codComer);
            listaClientes = new ArrayList<>();
            for (int i = 0; i < listaClientesAux.size(); i++) {
                if (listaClientesAux.get(i).getCodigoterminaldefecto().getCodigo().equals(codTerminal)) {
                    listaClientes.add(listaClientesAux.get(i));
                }
            }
            //factura.setCodTerminal(terminal.getCodigo());
        } else {
            codTerminal = "-1";
        }
    }

    public void seleccionarCliente() {
        if (cliente != null) {
            codCliente = cliente.getCodigo();
            for (int i = 0; i < listaTermianles.size(); i++) {
                if (listaTermianles.get(i).getCodigo().equals(cliente.getCodigoterminaldefecto().getCodigo())) {
                    terminal = listaTermianles.get(i);
                    break;
                }
            }
            seleccionarTerminal();
        } else {
            codCliente = "-1";
        }
    }

    public void seleccionarTipoFecha() {
        if (cliente != null) {
            codCliente = cliente.getCodigo();
            for (int i = 0; i < listaTermianles.size(); i++) {
                if (listaTermianles.get(i).getCodigo().equals(cliente.getCodigoterminaldefecto().getCodigo())) {
                    terminal = listaTermianles.get(i);
                    break;
                }
            }
            seleccionarTerminal();
        } else {
            codCliente = "-1";
        }
    }

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            switch (dataUser.getUser().getNiveloperacion()) {
                case "cero":
                    habilitarComer = true;
                    habilitarTerminal = true;
                    habilitarCli = true;
                    break;
                case "adco":
                    habilitarComer = false;
                    habilitarTerminal = true;
                    habilitarCli = true;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                            comercializadora = listaComercializadora.get(i);
                        }
                    }
                    seleccionarComer();
                    listaClientes = clienteServicio.obtenerClientesActivosPorComercializadora(comercializadora.getCodigo());
                    break;
                case "usac":
                    habilitarComer = false;
                    habilitarCli = false;
                    habilitarTerminal = false;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                            comercializadora = listaComercializadora.get(i);
                        }
                    }
                    if (comercializadora.getCodigo() != null) {
                        seleccionarComer();
                        listaClientes = clienteServicio.obtenerClientesActivosPorComercializadora(comercializadora.getCodigo());
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
                    seleccionarTerminal();
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
                    seleccionarComer();
                    for (int i = 0; i < listaTermianles.size(); i++) {
                        if (listaTermianles.get(i).getCodigo().equals(dataUser.getUser().getCodigoterminal())) {
                            terminal = listaTermianles.get(i);
                            break;
                        }
                    }
                    seleccionarTerminal();
                    break;
                default:
                    break;
            }
        }
    }

    public void obtenerFacturas() throws ParseException {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            String fechaS = date.format(this.fechaI);
            String fechaF = date.format(fechaf);

            /*fechas para comparar entre las dos y establecer un rango de 7 dias*/
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            String dateI = sdf.format(fechaI);
            String dateF = sdf.format(fechaf);

            Date firstDate = sdf.parse(dateI);
            Date secondDate = sdf.parse(dateF);

            long diff = secondDate.getTime() - firstDate.getTime();
            TimeUnit time = TimeUnit.DAYS;
            long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
            if (diffrence > 7) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "LA FECHA DE FIN NO PUEDE SER MAYOR A 7 DÍAS A LA FECHA DE INICIO");
            } else {

                //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura";
                String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura";
                if (codCliente.isEmpty()) {
                    codCliente = "-1";
                }
                if (codTerminal.isEmpty()) {
                    codTerminal = "-1";
                }
                url = new URL(direcc + "/paraFactura?codigoabastecedora=" + this.codAbas + "&codigocomercializadora=" + this.codComer + "&codigoterminal=" + this.codTerminal + "&tipofecha=" + tipoFecha + "&fechaI=" + fechaS + "&fechaF=" + fechaF + "&codigocliente=" + this.codCliente);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                listenvF = new ArrayList<>();
                listFact = new ArrayList();
                listDet = new ArrayList<>();
                EnvioFactura envFac = new EnvioFactura();
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
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCONTRARON REGISTROS");
                } else {
                    for (int indice = 0; indice < retorno.length(); indice++) {
                        if (!retorno.isNull(indice)) {
                            JSONObject fa = retorno.getJSONObject(indice);
                            JSONObject faPK = fa.getJSONObject("facturaPK");

                            factPk.setCodigoabastecedora(faPK.getString("codigoabastecedora"));
                            factPk.setCodigocomercializadora(faPK.getString("codigocomercializadora"));
                            factPk.setNumeronotapedido(faPK.getString("numeronotapedido"));
                            factPk.setNumero(faPK.getString("numero"));
                            fact.setFacturaPK(factPk);

                            if (!fa.isNull("fechaacreditacionprorrogada")) {
                                Long lDatePro = fa.getLong("fechaacreditacionprorrogada");
                                Date datePro = new Date(lDatePro);
                                fact.setFechaacreditacionprorrogada(date.format(datePro));
                            }
                            Long lDateVen = fa.getLong("fechaventa");
                            Date dateVen = new Date(lDateVen);
                            fact.setFechaventa(date.format(dateVen));
                            Long lDateVenc = fa.getLong("fechavencimiento");
                            Date dateVenc = new Date(lDateVenc);
                            fact.setFechavencimiento(date.format(dateVenc));
                            Long lDateAcre = fa.getLong("fechaacreditacion");
                            Date dateAcre = new Date(lDateAcre);
                            fact.setFechaacreditacion(date.format(dateAcre));
                            Long lDateDesp = fa.getLong("fechadespacho");
                            Date dateDesp = new Date(lDateDesp);
                            fact.setFechadespacho(date.format(dateDesp));
                            fact.setActiva(fa.getBoolean("activa"));
                            fact.setValortotal(fa.getBigDecimal("valortotal"));
                            fact.setIvatotal(fa.getBigDecimal("ivatotal"));
                            fact.setObservacion(fa.getString("observacion"));
                            fact.setPagada(fa.getBoolean("pagada"));
                            fact.setOeenpetro(fa.getBoolean("oeenpetro"));
                            fact.setCodigocliente(fa.getString("codigocliente"));
                            fact.setCodigoterminal(fa.getString("codigoterminal"));
                            fact.setCodigobanco(fa.getString("codigobanco"));
                            fact.setAdelantar(fa.getBoolean("adelantar"));
                            fact.setUsuarioactual(fa.getString("usuarioactual"));
                            fact.setNombrecomercializadora(fa.getString("nombrecomercializadora"));
                            fact.setRuccomercializadora(fa.getString("ruccomercializadora"));
                            fact.setDireccionmatrizcomercializadora(fa.getString("direccionmatrizcomercializadora"));
                            fact.setNombrecliente(fa.getString("nombrecliente"));
                            fact.setRuccliente(fa.getString("ruccliente"));
                            fact.setValorsinimpuestos(fa.getBigDecimal("valorsinimpuestos"));
                            if (!fa.isNull("correocliente")) {
                                fact.setCorreocliente(fa.getString("correocliente"));
                            }
                            fact.setDireccioncliente(fa.getString("direccioncliente"));
                            fact.setTelefonocliente(fa.getString("telefonocliente"));
                            if (!fa.isNull("numeroautorizacion")) {
                                fact.setNumeroautorizacion(fa.getString("numeroautorizacion"));
                            }
                            if (!fa.isNull("fechaautorizacion")) {
                                fact.setFechaautorizacion(fa.getString("fechaautorizacion"));
                            }
                            fact.setClienteformapago(fa.getString("clienteformapago"));
                            if (!fa.isNull("clienteformapagonosri")) {
                                fact.setClienteformapagonosri(fa.getString("clienteformapagonosri"));
                            }
                            fact.setPlazocliente(fa.getInt("plazocliente"));
                            fact.setClaveacceso(fa.getString("claveacceso"));
                            if (!fa.isNull("campoadicional_campo1")) {
                                fact.setCampoadicionalCampo1(fa.getString("campoadicional_campo1"));
                            }
                            if (!fa.isNull("campoadicional_campo2")) {
                                fact.setCampoadicionalCampo2(fa.getString("campoadicional_campo2"));
                            }
                            if (!fa.isNull("campoadicional_campo3")) {
                                fact.setCampoadicionalCampo3(fa.getString("campoadicional_campo3"));
                            }
                            if (!fa.isNull("campoadicional_campo4")) {
                                fact.setCampoadicionalCampo4(fa.getString("campoadicional_campo4"));
                            }
                            if (!fa.isNull("campoadicional_campo5")) {
                                fact.setCampoadicionalCampo5(fa.getString("campoadicional_campo5"));
                            }
                            if (!fa.isNull("campoadicional_campo6")) {
                                fact.setCampoadicionalCampo6(fa.getString("campoadicional_campo6"));
                            }
                            fact.setEstado(fa.getString("estado"));
                            Long error = fa.getLong("errordocumento");
                            Short errorS = error.shortValue();
                            fact.setErrordocumento(errorS);
                            Long hospedado = fa.getLong("hospedado");
                            Short hospedadoS = hospedado.shortValue();
                            fact.setHospedado(hospedadoS);
                            fact.setAmbientesri(fa.getString("ambientesri").charAt(0));
                            if (!fa.isNull("tipoemision")) {
                                fact.setTipoemision(fa.getString("tipoemision").charAt(0));
                            }
                            fact.setCodigodocumento(fa.getString("codigodocumento"));
                            fact.setEsagenteretencion(fa.getBoolean("esagenteretencion"));
                            fact.setEscontribuyenteespacial(fa.getString("escontribuyenteespacial"));
                            fact.setObligadocontabilidad(fa.getString("obligadocontabilidad"));
                            fact.setTipocomprador(fa.getString("tipocomprador"));
                            fact.setMoneda(fa.getString("moneda"));
                            fact.setSeriesri(fa.getString("seriesri"));
                            if (!fa.isNull("tipoplazocredito")) {
                                fact.setTipoplazocredito(fa.getString("tipoplazocredito"));
                            }
                            fact.setValorconrubro(fa.getBigDecimal("valorconrubro"));
                            fact.setOeanuladaenpetro(fa.getBoolean("oeanuladaenpetro"));
                            fact.setDespachada(fa.getBoolean("despachada"));
                            //factura.setRefacturada(fact.getBoolean("refacturada"));
                            //factura.setReliquidada(fact.getBigDecimal("reliquidada"));                            

                            if (fa.getBoolean("activa") == true) {
                                estadoAnulacion = false;
                                //fact.setActiva(estadoAnulacion);
                            } else {
                                estadoAnulacion = true;
                                //fact.setActiva(estadoAnulacion);
                            }
                            if (fa.getBoolean("oeenpetro") == true) {
                                oeenpetro = "S";
                                fact.setOeenpetro(true);
                            } else {
                                oeenpetro = "N";
                                fact.setOeenpetro(false);
                            }
                            if (fa.getString("estado").equalsIgnoreCase("PENDIENTE")) {
                                ensri = "P";
                                envFac.setEnsri(ensri);
                            } else {
                                if (fa.isNull("numeroautorizacion")) {
                                    ensri = "N";
                                    envFac.setEnsri(ensri);
                                } else {
                                    fact.setNumeroautorizacion(fa.getString("numeroautorizacion"));
                                    if (fa.getString("numeroautorizacion").length() == 49) {
                                        ensri = "S";
                                        envFac.setEnsri(ensri);
                                    } else {
                                        ensri = "N";
                                        envFac.setEnsri(ensri);
                                    }
                                }
                            }
                            fact.setFacturaPK(factPk);
                            envFac.setFactura(fact);
                            listFact.add(fact);
                            JSONArray detalleList = fa.getJSONArray("detallefacturaList");
                            for (int i = 0; i < detalleList.length(); i++) {
                                JSONObject det = detalleList.getJSONObject(i);
                                JSONObject detFpk = det.getJSONObject("detallefacturaPK");
                                detFac.setVolumennaturalrequerido(det.getBigDecimal("volumennaturalrequerido"));
                                detFac.setVolumennaturalautorizado(det.getBigDecimal("volumennaturalautorizado"));
                                detFac.setPrecioproducto(det.getBigDecimal("precioproducto"));
                                detFac.setSubtotal(det.getBigDecimal("subtotal"));
                                if (!det.isNull("ruccomercializadora")) {
                                    detFac.setRuccomercializadora(det.getString("ruccomercializadora"));
                                }
                                detFac.setDetallefacturaPK(detFacPK);
                                if (!det.isNull("nombreproducto")) {
                                    detFac.setNombreproducto(det.getString("nombreproducto"));
                                }
                                listDet.add(detFac);
                                envFac.setDetalle(listDet);
                                detFac = new Detallefactura();
                                detFacPK = new DetallefacturaPK();
                            }
                            listenvF.add(envFac);
                            envFac = new EnvioFactura();
                            fact = new Factura();
                            factPk = new FacturaPK();
                            listDet = new ArrayList<>();
                        }
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

    public void obtenerFacturasProrrogas() throws ParseException {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            String fechaS = date.format(this.fechaI);

            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura";
            //https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/paraCobrarxformapago?codigocomercializadora=0002&tipofecha=2&oeenpetro=true&activa=true&pagada=false&clienteformapago=03&fecha=2022/05/21&codigoterminal=02&codigocliente=02010995
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura";

            url = new URL(direcc + "/paraCobrarxformapago?codigocomercializadora=" + this.codComer + "&tipofecha=" + tipoFecha + "&oeenpetro=true&activa=true&pagada=false&clienteformapago=03&fecha=" + fechaS + "&codigoterminal=" + this.codTerminal + "&codigocliente=" + this.codCliente);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listenvF = new ArrayList<>();
            listFact = new ArrayList();
            listDet = new ArrayList<>();
            EnvioFactura envFac = new EnvioFactura();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            if (!retorno.isEmpty()) {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject fa = retorno.getJSONObject(indice);
                        JSONObject faPK = fa.getJSONObject("facturaPK");

                        factPk.setCodigoabastecedora(faPK.getString("codigoabastecedora"));
                        factPk.setCodigocomercializadora(faPK.getString("codigocomercializadora"));
                        factPk.setNumeronotapedido(faPK.getString("numeronotapedido"));
                        factPk.setNumero(faPK.getString("numero"));
                        fact.setFacturaPK(factPk);

                        if (!fa.isNull("fechaacreditacionprorrogada")) {
                            Long lDatePro = fa.getLong("fechaacreditacionprorrogada");
                            Date datePro = new Date(lDatePro);
                            fact.setFechaacreditacionprorrogada(date.format(datePro));
                        }
                        Long lDateVen = fa.getLong("fechaventa");
                        Date dateVen = new Date(lDateVen);
                        fact.setFechaventa(date.format(dateVen));
                        Long lDateVenc = fa.getLong("fechavencimiento");
                        Date dateVenc = new Date(lDateVenc);
                        fact.setFechavencimiento(date.format(dateVenc));
                        Long lDateAcre = fa.getLong("fechaacreditacion");
                        Date dateAcre = new Date(lDateAcre);
                        fact.setFechaacreditacion(date.format(dateAcre));
                        Long lDateDesp = fa.getLong("fechadespacho");
                        Date dateDesp = new Date(lDateDesp);
                        fact.setFechadespacho(date.format(dateDesp));
                        fact.setActiva(fa.getBoolean("activa"));
                        fact.setValortotal(fa.getBigDecimal("valortotal"));
                        fact.setIvatotal(fa.getBigDecimal("ivatotal"));
                        fact.setObservacion(fa.getString("observacion"));
                        fact.setPagada(fa.getBoolean("pagada"));
                        fact.setOeenpetro(fa.getBoolean("oeenpetro"));
                        fact.setCodigocliente(fa.getString("codigocliente"));
                        fact.setCodigoterminal(fa.getString("codigoterminal"));
                        fact.setCodigobanco(fa.getString("codigobanco"));
                        fact.setAdelantar(fa.getBoolean("adelantar"));
                        fact.setUsuarioactual(fa.getString("usuarioactual"));
                        fact.setNombrecomercializadora(fa.getString("nombrecomercializadora"));
                        fact.setRuccomercializadora(fa.getString("ruccomercializadora"));
                        fact.setDireccionmatrizcomercializadora(fa.getString("direccionmatrizcomercializadora"));
                        fact.setNombrecliente(fa.getString("nombrecliente"));
                        fact.setRuccliente(fa.getString("ruccliente"));
                        fact.setValorsinimpuestos(fa.getBigDecimal("valorsinimpuestos"));
                        if (!fa.isNull("correocliente")) {
                            fact.setCorreocliente(fa.getString("correocliente"));
                        }
                        fact.setDireccioncliente(fa.getString("direccioncliente"));
                        fact.setTelefonocliente(fa.getString("telefonocliente"));
                        if (!fa.isNull("numeroautorizacion")) {
                            fact.setNumeroautorizacion(fa.getString("numeroautorizacion"));
                        }
                        if (!fa.isNull("fechaautorizacion")) {
                            fact.setFechaautorizacion(fa.getString("fechaautorizacion"));
                        }
                        fact.setClienteformapago(fa.getString("clienteformapago"));
                        if (!fa.isNull("clienteformapagonosri")) {
                            fact.setClienteformapagonosri(fa.getString("clienteformapagonosri"));
                        }
                        fact.setPlazocliente(fa.getInt("plazocliente"));
                        fact.setClaveacceso(fa.getString("claveacceso"));
                        if (!fa.isNull("campoadicional_campo1")) {
                            fact.setCampoadicionalCampo1(fa.getString("campoadicional_campo1"));
                        }
                        if (!fa.isNull("campoadicional_campo2")) {
                            fact.setCampoadicionalCampo2(fa.getString("campoadicional_campo2"));
                        }
                        if (!fa.isNull("campoadicional_campo3")) {
                            fact.setCampoadicionalCampo3(fa.getString("campoadicional_campo3"));
                        }
                        if (!fa.isNull("campoadicional_campo4")) {
                            fact.setCampoadicionalCampo4(fa.getString("campoadicional_campo4"));
                        }
                        if (!fa.isNull("campoadicional_campo5")) {
                            fact.setCampoadicionalCampo5(fa.getString("campoadicional_campo5"));
                        }
                        if (!fa.isNull("campoadicional_campo6")) {
                            fact.setCampoadicionalCampo6(fa.getString("campoadicional_campo6"));
                        }
                        fact.setEstado(fa.getString("estado"));
                        Long error = fa.getLong("errordocumento");
                        Short errorS = error.shortValue();
                        fact.setErrordocumento(errorS);
                        Long hospedado = fa.getLong("hospedado");
                        Short hospedadoS = hospedado.shortValue();
                        fact.setHospedado(hospedadoS);
                        fact.setAmbientesri(fa.getString("ambientesri").charAt(0));
                        if (!fa.isNull("tipoemision")) {
                            fact.setTipoemision(fa.getString("tipoemision").charAt(0));
                        }
                        fact.setCodigodocumento(fa.getString("codigodocumento"));
                        fact.setEsagenteretencion(fa.getBoolean("esagenteretencion"));
                        fact.setEscontribuyenteespacial(fa.getString("escontribuyenteespacial"));
                        fact.setObligadocontabilidad(fa.getString("obligadocontabilidad"));
                        fact.setTipocomprador(fa.getString("tipocomprador"));
                        fact.setMoneda(fa.getString("moneda"));
                        fact.setSeriesri(fa.getString("seriesri"));
                        if (!fa.isNull("tipoplazocredito")) {
                            fact.setTipoplazocredito(fa.getString("tipoplazocredito"));
                        }
                        fact.setValorconrubro(fa.getBigDecimal("valorconrubro"));
                        fact.setOeanuladaenpetro(fa.getBoolean("oeanuladaenpetro"));
                        fact.setDespachada(fa.getBoolean("despachada"));
                        //factura.setRefacturada(fact.getBoolean("refacturada"));
                        //factura.setReliquidada(fact.getBigDecimal("reliquidada"));                            

                        if (fa.getBoolean("activa") == true) {
                            estadoAnulacion = false;
                            //fact.setActiva(estadoAnulacion);
                        } else {
                            estadoAnulacion = true;
                            //fact.setActiva(estadoAnulacion);
                        }
                        if (fa.getBoolean("oeenpetro") == true) {
                            oeenpetro = "S";
                            fact.setOeenpetro(true);
                        } else {
                            oeenpetro = "N";
                            fact.setOeenpetro(false);
                        }
                        if (fa.getString("estado").equalsIgnoreCase("PENDIENTE")) {
                            ensri = "P";
                            envFac.setEnsri(ensri);
                        } else {
                            if (fa.isNull("numeroautorizacion")) {
                                ensri = "N";
                                envFac.setEnsri(ensri);
                            } else {
                                fact.setNumeroautorizacion(fa.getString("numeroautorizacion"));
                                if (fa.getString("numeroautorizacion").length() == 49) {
                                    ensri = "S";
                                    envFac.setEnsri(ensri);
                                } else {
                                    ensri = "N";
                                    envFac.setEnsri(ensri);
                                }
                            }
                        }
                        fact.setFacturaPK(factPk);
                        envFac.setFactura(fact);
                        listFact.add(fact);
                        JSONArray detalleList = fa.getJSONArray("detallefacturaList");
                        for (int i = 0; i < detalleList.length(); i++) {
                            JSONObject det = detalleList.getJSONObject(i);
                            JSONObject detFpk = det.getJSONObject("detallefacturaPK");
                            detFac.setVolumennaturalrequerido(det.getBigDecimal("volumennaturalrequerido"));
                            detFac.setVolumennaturalautorizado(det.getBigDecimal("volumennaturalautorizado"));
                            detFac.setPrecioproducto(det.getBigDecimal("precioproducto"));
                            detFac.setSubtotal(det.getBigDecimal("subtotal"));
                            if (!det.isNull("ruccomercializadora")) {
                                detFac.setRuccomercializadora(det.getString("ruccomercializadora"));
                            }
                            detFac.setDetallefacturaPK(detFacPK);
                            if (!det.isNull("nombreproducto")) {
                                detFac.setNombreproducto(det.getString("nombreproducto"));
                            }
                            listDet.add(detFac);
                            envFac.setDetalle(listDet);
                            detFac = new Detallefactura();
                            detFacPK = new DetallefacturaPK();
                        }
                        listenvF.add(envFac);
                        envFac = new EnvioFactura();
                        fact = new Factura();
                        factPk = new FacturaPK();
                        listDet = new ArrayList<>();
                    }
                }
            }
            obtenerFacturasProrrogas2();
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerFacturasProrrogas2() throws ParseException {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            String fechaS = date.format(this.fechaI);

            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura";
            //https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/paraCobrarxformapago?codigocomercializadora=0002&tipofecha=2&oeenpetro=true&activa=true&pagada=false&clienteformapago=03&fecha=2022/05/21&codigoterminal=02&codigocliente=02010995
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura";

            url = new URL(direcc + "/paraCobrarxformapago?codigocomercializadora=" + this.codComer + "&tipofecha=" + tipoFecha + "&oeenpetro=true&activa=true&pagada=false&clienteformapago=01&fecha=" + fechaS + "&codigoterminal=" + this.codTerminal + "&codigocliente=" + this.codCliente);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            EnvioFactura envFac = new EnvioFactura();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            if (!retorno.isEmpty()) {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject fa = retorno.getJSONObject(indice);
                        JSONObject faPK = fa.getJSONObject("facturaPK");

                        factPk.setCodigoabastecedora(faPK.getString("codigoabastecedora"));
                        factPk.setCodigocomercializadora(faPK.getString("codigocomercializadora"));
                        factPk.setNumeronotapedido(faPK.getString("numeronotapedido"));
                        factPk.setNumero(faPK.getString("numero"));
                        fact.setFacturaPK(factPk);

                        if (!fa.isNull("fechaacreditacionprorrogada")) {
                            Long lDatePro = fa.getLong("fechaacreditacionprorrogada");
                            Date datePro = new Date(lDatePro);
                            fact.setFechaacreditacionprorrogada(date.format(datePro));
                        }
                        Long lDateVen = fa.getLong("fechaventa");
                        Date dateVen = new Date(lDateVen);
                        fact.setFechaventa(date.format(dateVen));
                        Long lDateVenc = fa.getLong("fechavencimiento");
                        Date dateVenc = new Date(lDateVenc);
                        fact.setFechavencimiento(date.format(dateVenc));
                        Long lDateAcre = fa.getLong("fechaacreditacion");
                        Date dateAcre = new Date(lDateAcre);
                        fact.setFechaacreditacion(date.format(dateAcre));
                        Long lDateDesp = fa.getLong("fechadespacho");
                        Date dateDesp = new Date(lDateDesp);
                        fact.setFechadespacho(date.format(dateDesp));
                        fact.setActiva(fa.getBoolean("activa"));
                        fact.setValortotal(fa.getBigDecimal("valortotal"));
                        fact.setIvatotal(fa.getBigDecimal("ivatotal"));
                        fact.setObservacion(fa.getString("observacion"));
                        fact.setPagada(fa.getBoolean("pagada"));
                        fact.setOeenpetro(fa.getBoolean("oeenpetro"));
                        fact.setCodigocliente(fa.getString("codigocliente"));
                        fact.setCodigoterminal(fa.getString("codigoterminal"));
                        fact.setCodigobanco(fa.getString("codigobanco"));
                        fact.setAdelantar(fa.getBoolean("adelantar"));
                        fact.setUsuarioactual(fa.getString("usuarioactual"));
                        fact.setNombrecomercializadora(fa.getString("nombrecomercializadora"));
                        fact.setRuccomercializadora(fa.getString("ruccomercializadora"));
                        fact.setDireccionmatrizcomercializadora(fa.getString("direccionmatrizcomercializadora"));
                        fact.setNombrecliente(fa.getString("nombrecliente"));
                        fact.setRuccliente(fa.getString("ruccliente"));
                        fact.setValorsinimpuestos(fa.getBigDecimal("valorsinimpuestos"));
                        if (!fa.isNull("correocliente")) {
                            fact.setCorreocliente(fa.getString("correocliente"));
                        }
                        fact.setDireccioncliente(fa.getString("direccioncliente"));
                        fact.setTelefonocliente(fa.getString("telefonocliente"));
                        if (!fa.isNull("numeroautorizacion")) {
                            fact.setNumeroautorizacion(fa.getString("numeroautorizacion"));
                        }
                        if (!fa.isNull("fechaautorizacion")) {
                            fact.setFechaautorizacion(fa.getString("fechaautorizacion"));
                        }
                        fact.setClienteformapago(fa.getString("clienteformapago"));
                        if (!fa.isNull("clienteformapagonosri")) {
                            fact.setClienteformapagonosri(fa.getString("clienteformapagonosri"));
                        }
                        fact.setPlazocliente(fa.getInt("plazocliente"));
                        fact.setClaveacceso(fa.getString("claveacceso"));
                        if (!fa.isNull("campoadicional_campo1")) {
                            fact.setCampoadicionalCampo1(fa.getString("campoadicional_campo1"));
                        }
                        if (!fa.isNull("campoadicional_campo2")) {
                            fact.setCampoadicionalCampo2(fa.getString("campoadicional_campo2"));
                        }
                        if (!fa.isNull("campoadicional_campo3")) {
                            fact.setCampoadicionalCampo3(fa.getString("campoadicional_campo3"));
                        }
                        if (!fa.isNull("campoadicional_campo4")) {
                            fact.setCampoadicionalCampo4(fa.getString("campoadicional_campo4"));
                        }
                        if (!fa.isNull("campoadicional_campo5")) {
                            fact.setCampoadicionalCampo5(fa.getString("campoadicional_campo5"));
                        }
                        if (!fa.isNull("campoadicional_campo6")) {
                            fact.setCampoadicionalCampo6(fa.getString("campoadicional_campo6"));
                        }
                        fact.setEstado(fa.getString("estado"));
                        Long error = fa.getLong("errordocumento");
                        Short errorS = error.shortValue();
                        fact.setErrordocumento(errorS);
                        Long hospedado = fa.getLong("hospedado");
                        Short hospedadoS = hospedado.shortValue();
                        fact.setHospedado(hospedadoS);
                        fact.setAmbientesri(fa.getString("ambientesri").charAt(0));
                        if (!fa.isNull("tipoemision")) {
                            fact.setTipoemision(fa.getString("tipoemision").charAt(0));
                        }
                        fact.setCodigodocumento(fa.getString("codigodocumento"));
                        fact.setEsagenteretencion(fa.getBoolean("esagenteretencion"));
                        fact.setEscontribuyenteespacial(fa.getString("escontribuyenteespacial"));
                        fact.setObligadocontabilidad(fa.getString("obligadocontabilidad"));
                        fact.setTipocomprador(fa.getString("tipocomprador"));
                        fact.setMoneda(fa.getString("moneda"));
                        fact.setSeriesri(fa.getString("seriesri"));
                        if (!fa.isNull("tipoplazocredito")) {
                            fact.setTipoplazocredito(fa.getString("tipoplazocredito"));
                        }
                        fact.setValorconrubro(fa.getBigDecimal("valorconrubro"));
                        fact.setOeanuladaenpetro(fa.getBoolean("oeanuladaenpetro"));
                        fact.setDespachada(fa.getBoolean("despachada"));
                        //factura.setRefacturada(fact.getBoolean("refacturada"));
                        //factura.setReliquidada(fact.getBigDecimal("reliquidada"));                            

                        if (fa.getBoolean("activa") == true) {
                            estadoAnulacion = false;
                            //fact.setActiva(estadoAnulacion);
                        } else {
                            estadoAnulacion = true;
                            //fact.setActiva(estadoAnulacion);
                        }
                        if (fa.getBoolean("oeenpetro") == true) {
                            oeenpetro = "S";
                            fact.setOeenpetro(true);
                        } else {
                            oeenpetro = "N";
                            fact.setOeenpetro(false);
                        }
                        if (fa.getString("estado").equalsIgnoreCase("PENDIENTE")) {
                            ensri = "P";
                            envFac.setEnsri(ensri);
                        } else {
                            if (fa.isNull("numeroautorizacion")) {
                                ensri = "N";
                                envFac.setEnsri(ensri);
                            } else {
                                fact.setNumeroautorizacion(fa.getString("numeroautorizacion"));
                                if (fa.getString("numeroautorizacion").length() == 49) {
                                    ensri = "S";
                                    envFac.setEnsri(ensri);
                                } else {
                                    ensri = "N";
                                    envFac.setEnsri(ensri);
                                }
                            }
                        }
                        fact.setFacturaPK(factPk);
                        envFac.setFactura(fact);
                        listFact.add(fact);
                        JSONArray detalleList = fa.getJSONArray("detallefacturaList");
                        for (int i = 0; i < detalleList.length(); i++) {
                            JSONObject det = detalleList.getJSONObject(i);
                            JSONObject detFpk = det.getJSONObject("detallefacturaPK");
                            detFac.setVolumennaturalrequerido(det.getBigDecimal("volumennaturalrequerido"));
                            detFac.setVolumennaturalautorizado(det.getBigDecimal("volumennaturalautorizado"));
                            detFac.setPrecioproducto(det.getBigDecimal("precioproducto"));
                            detFac.setSubtotal(det.getBigDecimal("subtotal"));
                            if (!det.isNull("ruccomercializadora")) {
                                detFac.setRuccomercializadora(det.getString("ruccomercializadora"));
                            }
                            detFac.setDetallefacturaPK(detFacPK);
                            if (!det.isNull("nombreproducto")) {
                                detFac.setNombreproducto(det.getString("nombreproducto"));
                            }
                            listDet.add(detFac);
                            envFac.setDetalle(listDet);
                            detFac = new Detallefactura();
                            detFacPK = new DetallefacturaPK();
                        }
                        listenvF.add(envFac);
                        envFac = new EnvioFactura();
                        fact = new Factura();
                        factPk = new FacturaPK();
                        listDet = new ArrayList<>();
                    }
                }
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerNotaPedidos() throws ParseException {
        try {
            System.out.println("FT:: ENTRA A obtenerNotaPedidos::");
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            //DateFormat date2 = new SimpleDateFormat("yyyy-MM-dd");
            String fechaS = date.format(this.fechaI);
            //String ur = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/paraFactura?codigoabastecedora=0001&codigocomercializadora=0002&codigoterminal=07&tipofecha=1&fecha=2021/6/18";
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/paraFactura?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido/paraFactura?";
            url = new URL(direcc + "codigoabastecedora=" + this.codAbas + "&codigocomercializadora=" + this.codComer + "&codigoterminal=" + this.codTerminal + "&tipofecha=" + tipoFecha + "&fecha=" + fechaS);

            System.out.println("FT:: obtenerNotaPedidos:: URL: " + url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listenvNP = new ArrayList<>();
            listDetNP = new ArrayList<>();
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
                this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCONTRARON REGISTROS");
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
                        cliente.setNombrecomercial(cli.getString("nombrecomercial"));
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

                        /*------Variable trasnformar de int a short-----*/
                        int dp = cli.getInt("diasplazocredito");
                        short diasplazo = (short) dp;
                        cliente.setDiasplazocredito(diasplazo);

                        /*----Guardando el cliente, termina y banco en Nota pedido---*/
                        np.setCodigocliente(cliente);
                        np.setCodigoterminal(terminalT);
                        np.setCodigobanco(banco);
                        np.setComercializadora(comerc);
                        np.setAbastecedora(abas);

                        np.setNumerofacturasri(nt.getString("numerofacturasri"));
                        npPK.setNumero(ntPK.getString("numero"));
                        npPK.setCodigoabastecedora(ntPK.getString("codigoabastecedora"));
                        npPK.setCodigocomercializadora(ntPK.getString("codigocomercializadora"));
                        np.setFechaventa(fechaVencimiento);
                        np.setFechadespacho(fechaDescpacho);
                        np.setAdelantar(true);
                        np.setActiva(nt.getBoolean("activa"));
                        try {
                            np.setPrefijo(nt.getString("prefijo"));
                            //System.out.println("NOTA DE PEDIDO OK: " + npPK.getNumero());
                        } catch (Throwable e) {
                            System.out.println("ERROR PREFIJO" + e.getMessage() + npPK.getNumero().getClass());
                        }

                        np.setNotapedidoPK(npPK);
                        envioPedido.setNotapedido(np);
                        JSONArray detalleNP = nt.getJSONArray("detallesNP");
                        for (int i = 0; i < detalleNP.length(); i++) {
                            JSONObject det = detalleNP.getJSONObject(i);
                            JSONObject detnPK = det.getJSONObject("detallenotapedidoPK");
                            JSONObject med = det.getJSONObject("medida");
                            JSONObject prod = det.getJSONObject("producto");
                            detNP.setVolumennaturalautorizado(det.getBigDecimal("volumennaturalautorizado"));
                            detNP.setVolumennaturalrequerido(det.getBigDecimal("volumennaturalrequerido"));

                            medida.setCodigo(med.getString("codigo"));
                            medida.setNombre(med.getString("nombre"));
                            detNP.setMedida(medida);

                            producto.setCodigo(prod.getString("codigo"));
                            producto.setNombre(prod.getString("nombre"));
                            detNP.setProducto(producto);

                            detNPK.setCodigoproducto(detnPK.getString("codigoproducto"));
                            detNPK.setCodigomedida(detnPK.getString("codigomedida"));
                            detNP.setDetallenotapedidoPK(detNPK);

                            listDetNP.add(detNP);
                            envioPedido.setDetalle(detNP);
                            detNP = new Detallenotapedido();
                            detNPK = new DetallenotapedidoPK();
                        }
                        if (np.isActiva()) {
                            listenvNP.add(envioPedido);
                        }
                        envioPedido = new EnvioPedido();
                        np = new Notapedido();
                        npPK = new NotapedidoPK();
                        abas = new Abastecedora();
                        comerc = new Comercializadora();
                        formap = new Formapago();
                        cliente = new Cliente();
                        terminalT = new Terminal();
                        banco = new Banco();
                        listDetNP = new ArrayList<>();
                    }
                }
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
                System.out.println("ERROR CON LA NOTA DE PEDIDO: " + np.getNotapedidoPK().getNumero());
            } else {
                soloporProcesar = false;
            }

        } catch (IOException e) {
            System.out.println("FT:: ERROR EN obtenerNotaPedidos " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void save() throws ParseException {
        //DAVID AYALA VERIFICAR LISTA DE PRODUCTOS Y MANEJAR VECTOR

//hacer un laso, recorrer prodSInFe y validar que el deyalle nota producto no este en la lista
        String[] prodSinFe = Fichero.getPRODUCTOSINFE().split(",");
//        if (comercializadora.getActivo().equals("S")) {
//            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//            for (int i = 0; i < listenvNP.size(); i++) {
//                //generarFactura(listenvNP.get(i));
//                if (listenvNP.get(i).getNotapedido().isProcesar() == true && listenvNP.get(i).getNotapedido().getNumerofacturasri().trim().equals("0")) {
//                    //generarFactura(listenvNP.get(i));
//                    if (listenvNP.get(i).getDetalle().getDetallenotapedidoPK().getCodigoproducto().equals(prodSinFe[0]) || listenvNP.get(i).getDetalle().getDetallenotapedidoPK().getCodigoproducto().equals(prodSinFe[1])) {
//                        obtenerDetalleNotaPedidoF(listenvNP.get(i), "-1");
//                    } else {
//                        obtenerDetalleNotaPedidoF(listenvNP.get(i), "0");
//                    }
//
//                    //generarFacturaParametros(listenvNP.get(i));
//                }
//            }
//            obtenerNotaPedidos();
//        } else {
//            this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA SE ENCUENTRA INACTIVA");
//        }

        if (comercializadora.getActivo().equals("S")) {

            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            for (int i = 0; i < listenvNP.size(); i++) {
                boolean bandera = false;
                //generarFactura(listenvNP.get(i));
                if (listenvNP.get(i).getNotapedido().isProcesar() == true && listenvNP.get(i).getNotapedido().getNumerofacturasri().trim().equals("0")) {
                    //generarFactura(listenvNP.get(i));
                    for (int j = 0; j < prodSinFe.length; j++) {
                        if (listenvNP.get(i).getDetalle().getDetallenotapedidoPK().getCodigoproducto().equals(prodSinFe[j])) {
                            bandera = true;
                        }
                    }
                    if (bandera) {
                        obtenerDetalleNotaPedidoF(listenvNP.get(i), "-1");
                    } else {
                        obtenerDetalleNotaPedidoF(listenvNP.get(i), "0");
                    }
                    //generarFacturaParametros(listenvNP.get(i));
                }
            }
            obtenerNotaPedidos();
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA SE ENCUENTRA INACTIVA");
        }

    }

    public void obtenerDetalleNotaPedidoF(EnvioPedido envP, String num) throws ParseException {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");

            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/porId?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detallenotapedido/porId?";
            url = new URL(direcc + "codigoabastecedora=" + envP.getNotapedido().getNotapedidoPK().getCodigoabastecedora()
                    + "&codigocomercializadora=" + envP.getNotapedido().getNotapedidoPK().getCodigocomercializadora()
                    + "&numero=" + envP.getNotapedido().getNotapedidoPK().getNumero()
                    + "&codigoproducto=" + envP.getDetalle().getDetallenotapedidoPK().getCodigoproducto()
                    + "&codigomedida=" + envP.getDetalle().getDetallenotapedidoPK().getCodigomedida());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            detNP = new Detallenotapedido();
            detNPK = new DetallenotapedidoPK();
            medida = new Medida();
            producto = new Producto();

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
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL OBTENER INFORMACIÓN SOBRE EL DETALLE NOTA PEDIDO");
            } else {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject det = retorno.getJSONObject(indice);
                        JSONObject detPk = det.getJSONObject("detallenotapedidoPK");
                        JSONObject med = det.getJSONObject("medida");
                        JSONObject prod = det.getJSONObject("producto");

                        medida.setAbreviacion(med.getString("abreviacion"));
                        medida.setActivo(med.getBoolean("activo"));
                        medida.setCodigo(med.getString("codigo"));
                        medida.setNombre(med.getString("nombre"));
                        medida.setUsuarioactual(dataUser.getUser().getNombrever());

                        producto.setCodigo(prod.getString("codigo"));
                        producto.setNombre(prod.getString("nombre"));

                        detNPK.setCodigoabastecedora(detPk.getString("codigoabastecedora"));
                        detNPK.setCodigocomercializadora(detPk.getString("codigocomercializadora"));
                        detNPK.setCodigomedida(detPk.getString("codigomedida"));
                        detNPK.setCodigoproducto(detPk.getString("codigoproducto"));
                        detNPK.setNumero(detPk.getString("numero"));

                        detNP.setDetallenotapedidoPK(detNPK);
                        detNP.setMedida(medida);
                        detNP.setProducto(producto);
                        detNP.setUsuarioactual(dataUser.getUser().getNombrever());
                        detNP.setVolumennaturalautorizado(envP.getDetalle().getVolumennaturalautorizado());
                        detNP.setVolumennaturalrequerido(det.getBigDecimal("volumennaturalrequerido"));
                    }
                }
                if (connection.getResponseCode() >= 200 || connection.getResponseCode() <= 200) {
                    actualizarVolumenFactura(detNP, envP, num);
                } else {
                    System.out.println(connection.getResponseCode());
                    System.out.println(connection.getResponseMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actualizarVolumenFactura(Detallenotapedido detNP, EnvioPedido envP, String num) throws ParseException {
        try {
            String respuesta = "";
            //String trama = "";
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/porId";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detallenotapedido/porId";
            url = new URL(direcc);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(detNP);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();

            if (connection.getResponseCode() >= 200 || connection.getResponseCode() <= 200) {
                generarFacturaParametros(envP, num);
            } else {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void soloPorProcesar() {
        try {
            listenvNPAuxilia = new ArrayList<>();
            if (!listenvNP.isEmpty()) {
                if (soloporProcesar) {
                    for (int i = 0; i < listenvNP.size(); i++) {
                        if (listenvNP.get(i).getNotapedido().getNumerofacturasri().trim().equals("0")) {
                            listenvNPAuxilia.add(listenvNP.get(i));
                        }
                    }
                    listenvNP = listenvNPAuxilia;
                } else {
                    obtenerNotaPedidos();
                }
            } else {
                soloporProcesar = false;
                this.dialogo(FacesMessage.SEVERITY_ERROR, "PARA PODER VISUALIZAR LAS FACTURAS POR PROCESAR, PRIMERO REALIZAR UNA BÚSQUEDA CON REGISTROS");
            }
        } catch (ParseException ex) {
            Logger.getLogger(FacturacionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarOrdenPetro(EnvioPedido envNP, String numFact) {
        if (envNP.getNotapedido().getFechadespacho().equals(envNP.getNotapedido().getFechaventa())) {
            obtenerTramaOrdenEntrega(envNP, numFact);
        } else if (!envNP.getNotapedido().getFechadespacho().equals(envNP.getNotapedido().getFechaventa()) && envNP.getNotapedido().isAdelantar()) {
            obtenerTramaOrdenEntrega(envNP, numFact);
        }
    }

    public void generarFacturaParametros(EnvioPedido envNP, String num) throws ParseException {
        try {
            System.out.println("FT::-generarFacturaP: " + envNP.toString());
            String respuesta;
            String numeroFactura = "";
            String mensajeError = "";
            String usuario = dataUser.getUser().getNombrever().trim().replace(" ", "");
            url = new URL(direccion + tablaFactura + "/crearF?"
                    + "codigoabastecedora=" + envNP.getNotapedido().getNotapedidoPK().getCodigoabastecedora()
                    + "&codigocomercializadora=" + envNP.getNotapedido().getNotapedidoPK().getCodigocomercializadora()
                    + "&numeronotapedido=" + envNP.getNotapedido().getNotapedidoPK().getNumero()
                    + "&numero=" + num
                    + "&codigousuario=" + usuario);

            System.out.println("FT:: generarFacturaP - url:: " + url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
            try {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());

                BufferedReader br = new BufferedReader(reader);
                String tmp = null;
                String resp = "";
                while ((tmp = br.readLine()) != null) {
                    resp += tmp;
                }
                JSONObject objetoJson = new JSONObject(resp);
                JSONArray retorno = objetoJson.getJSONArray("retorno");
                if (connection.getResponseCode() == 200) {
                    for (int indice = 0; indice < retorno.length(); indice++) {
                        if (!retorno.isNull(indice)) {
                            JSONObject resultado = retorno.getJSONObject(indice);
                            JSONObject factura = resultado.getJSONObject("factura");
                            JSONObject facturaPK = factura.getJSONObject("facturaPK");
                            numeroFactura = facturaPK.getString("numero");
                        }
                    }
                    this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA REGISTRADA EXITOSAMENTE");
                    enviarOrdenPetro(envNP, numeroFactura);
                } else if (connection.getResponseCode() == 299) {
                    for (int indice = 0; indice < retorno.length(); indice++) {
                        if (!retorno.isNull(indice)) {
                            mensajeError = retorno.getString(indice);
                            System.out.println("Codigo: " + connection.getResponseCode());
                        }
                    }
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR FACTURA sobre la NP: " + envNP.getNotapedido().getNotapedidoPK().getNumero() + " " + mensajeError);
                    System.out.println("Error al añadir: " + connection.getResponseCode());
                    System.out.println("ResponseMesagge: " + connection.getResponseMessage());
                    System.out.println("getErrorStream: " + connection.getErrorStream());
                }

            } catch (Throwable t) {
                System.out.println("AS:: ERROR Inicio!!! " + t.getMessage());
                t.printStackTrace(System.out);
                System.out.println("AS:: ERROR Fin!!! " + t.getMessage());
            }

//            if (connection.getResponseCode() == 200) {
//                this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA REGISTRADA EXITOSAMENTE");
//                enviarOrdenPetro(envNP, numeroFactura);
//            } else {
//                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR FACTURA sobre la NP: " + envNP.getNotapedido().getNotapedidoPK().getNumero());
//                System.out.println("Error al añadir: " + connection.getResponseCode());
//                System.out.println("ResponseMesagge: " + connection.getResponseMessage());
//                System.out.println("getErrorStream: " + connection.getErrorStream());
//            }
        } catch (IOException e) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR FACTURA sobre la NP: " + envNP.getNotapedido().getNotapedidoPK().getNumero());
            System.out.println("FT:: IOException Capturada: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    public void generarFactura(EnvioPedido envNP) {

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Factura fac = new Factura();
        Cliente cli = new Cliente();
        FacturaPK fpk = new FacturaPK();
        Detallefactura dtF = new Detallefactura();
        List<Detallefactura> listDetF = new ArrayList<>();
        EnvioFactura envF = new EnvioFactura();
        short s = 0;
        fpk.setCodigoabastecedora(envNP.getNotapedido().getNotapedidoPK().getCodigoabastecedora());
        fpk.setCodigocomercializadora(envNP.getNotapedido().getNotapedidoPK().getCodigocomercializadora());
        fpk.setNumeronotapedido(envNP.getNotapedido().getNotapedidoPK().getNumero());
        fpk.setNumero("0");
        fac.setFacturaPK(fpk);
        //String fd = date.format(envNP.getNotapedido().getFechadespacho());
        //String fv = date.format(envNP.getNotapedido().getFechaventa());
        //String fvc = date.format(calcularFechaV(envNP.getNotapedido()));

        fac.setFechaventa(envNP.getNotapedido().getFechaventa());
        fac.setFechavencimiento(calcularFechaV(envNP.getNotapedido()));
        fac.setFechaacreditacion(fac.getFechavencimiento());
        fac.setFechadespacho(envNP.getNotapedido().getFechadespacho());
        fac.setActiva(envNP.getNotapedido().isActiva());
        fac.setValortotal(new BigDecimal(0));
        fac.setIvatotal(new BigDecimal(0));
        fac.setObservacion("Generación Automática con NP: " + envNP.getNotapedido().getNotapedidoPK().getNumero());
        fac.setPagada(false);
        fac.setOeenpetro(false);
        fac.setCodigocliente(envNP.getNotapedido().getCodigocliente().getCodigo());
        fac.setCodigoterminal(envNP.getNotapedido().getCodigoterminal().getCodigo());
        fac.setCodigobanco(envNP.getNotapedido().getCodigobanco().getCodigo());
        fac.setAdelantar(envNP.getNotapedido().isAdelantar());
        fac.setUsuarioactual(dataUser.getUser().getNombrever());
        fac.setNombrecomercializadora(envNP.getNotapedido().getComercializadora().getNombre());
        fac.setRuccomercializadora(envNP.getNotapedido().getComercializadora().getRuc());
        fac.setDireccionmatrizcomercializadora(envNP.getNotapedido().getComercializadora().getDireccion());
        fac.setNombrecliente(envNP.getNotapedido().getCodigocliente().getNombre());
        fac.setRuccliente(envNP.getNotapedido().getCodigocliente().getRuc());
        fac.setValorsinimpuestos(new BigDecimal(0));
        fac.setCorreocliente(envNP.getNotapedido().getCodigocliente().getCorreo1());
        fac.setDireccioncliente(envNP.getNotapedido().getCodigocliente().getDireccion());
        fac.setTelefonocliente(envNP.getNotapedido().getCodigocliente().getTelefono1());
        fac.setClienteformapago(envNP.getNotapedido().getCodigocliente().getCodigoformapago().getCodigo());
        fac.setNumeroautorizacion("");
        fac.setFechaautorizacion("");
        fac.setPlazocliente(envNP.getNotapedido().getCodigocliente().getDiasplazocredito().intValue());
        fac.setClaveacceso("0");
        fac.setCampoadicionalCampo1("");
        fac.setCampoadicionalCampo2("");
        fac.setCampoadicionalCampo3("");
        fac.setCampoadicionalCampo4("");
        fac.setCampoadicionalCampo5("");
        fac.setCampoadicionalCampo6("");
        fac.setEstado("NUEVA");
        fac.setErrordocumento(s);
        fac.setHospedado(s);
        fac.setAmbientesri(envNP.getNotapedido().getComercializadora().getAmbientesri());
        fac.setTipoemision(envNP.getNotapedido().getComercializadora().getTipoemision());
        fac.setCodigodocumento("01");
        fac.setEsagenteretencion(envNP.getNotapedido().getComercializadora().getEsagenteretencion());
        fac.setEscontribuyenteespacial(envNP.getNotapedido().getComercializadora().getEscontribuyenteespacial());
        fac.setObligadocontabilidad(envNP.getNotapedido().getComercializadora().getObligadocontabilidad());
        fac.setTipocomprador("04");
        fac.setMoneda("DOLAR");
        fac.setSeriesri(envNP.getNotapedido().getComercializadora().getEstablecimientofac() + envNP.getNotapedido().getComercializadora().getPuntoventafac());
        envF.setFactura(fac);
        envF.setDetalle(listDetF);
        obtenerPrecio(envNP, envF, fac);

    }

    public void obtenerPrecio(EnvioPedido envNP, EnvioFactura envF, Factura fac) {
        try {
            Detallefactura detalleFactura = new Detallefactura();
            DetallefacturaPK detalleFacturaPK = new DetallefacturaPK();
            BigDecimal subtotal = new BigDecimal(0);

            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            //String fechaS = date.format(envNP.getNotapedido().getFechadespacho());
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/paraFactura?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio/paraFactura?";
            url = new URL(direcc + "codigocomercializadora=" + envNP.getNotapedido().getNotapedidoPK().getCodigocomercializadora()
                    + "&codigoterminal=" + envNP.getNotapedido().getCodigoterminal().getCodigo() + "&codigoproducto=" + envNP.getDetalle().getDetallenotapedidoPK().getCodigoproducto()
                    + "&codigomedida=" + envNP.getDetalle().getDetallenotapedidoPK().getCodigomedida()
                    + "&fechainicio=" + envNP.getNotapedido().getFechadespacho() + "&codigolistaprecio=" + envNP.getNotapedido().getCodigocliente().getCodigolistaprecio());
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/paraFactura?codigocomercializadora=0002&codigoterminal=02&codigoproducto=0101&codigomedida=01&fechainicio=2021/06/25&codigolistaprecio=a0000001");
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
            for (int indice = 0; indice < retorno.length(); indice++) {
                if (!retorno.isNull(indice)) {
                    JSONObject price = retorno.getJSONObject(indice);
                    JSONObject pricePK = price.getJSONObject("precioPK");

                    precioPK.setCodigoPrecio(pricePK.getLong("codigoPrecio"));
                    precio.setPrecioPK(precioPK);
                    precio.setPrecioproducto(price.getBigDecimal("precioproducto"));
                }
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());

            detalleFacturaPK.setCodigoabastecedora(envNP.getNotapedido().getNotapedidoPK().getCodigoabastecedora());
            detalleFacturaPK.setCodigocomercializadora(envNP.getNotapedido().getNotapedidoPK().getCodigocomercializadora());
            detalleFacturaPK.setNumeronotapedido(envNP.getNotapedido().getNotapedidoPK().getNumero());
            detalleFacturaPK.setNumero("0");
            detalleFacturaPK.setCodigoproducto(envNP.getDetalle().getDetallenotapedidoPK().getCodigoproducto());

            detalleFactura.setDetallefacturaPK(detalleFacturaPK);
            detalleFactura.setCodigomedida(envNP.getDetalle().getDetallenotapedidoPK().getCodigomedida());
            detalleFactura.setVolumennaturalautorizado(envNP.getDetalle().getVolumennaturalautorizado());
            detalleFactura.setVolumennaturalrequerido(envNP.getDetalle().getVolumennaturalrequerido());
            detalleFactura.setCodigoprecio(String.valueOf(precio.getPrecioPK().getCodigoPrecio()));
            detalleFactura.setPrecioproducto(precio.getPrecioproducto());
            subtotal = envNP.getDetalle().getVolumennaturalautorizado().multiply(precio.getPrecioproducto());
            detalleFactura.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
            detalleFactura.setUsuarioactual(dataUser.getUser().getNombrever());
            detalleFactura.setRuccomercializadora(fac.getRuccomercializadora());
            detalleFactura.setCodigoimpuesto("");
            detalleFactura.setNombreimpuesto("");
            detalleFactura.setSeimprime(false);
            detalleFactura.setValordefecto(new BigDecimal(0));

            fac.setValorsinimpuestos(detalleFactura.getSubtotal());
            envF.setFactura(fac);

            if (envNP.getNotapedido().getFechadespacho().equals(envNP.getNotapedido().getFechaventa())) {
                actionFactura(fac, envNP, detalleFactura, detalleFactura.getCodigoprecio(), envF);
                //obtenerTramaOrdenEntrega(envNP);
            } else if (!envNP.getNotapedido().getFechadespacho().equals(envNP.getNotapedido().getFechaventa()) && envNP.getNotapedido().isAdelantar()) {
                actionFactura(fac, envNP, detalleFactura, detalleFactura.getCodigoprecio(), envF);
                //obtenerTramaOrdenEntrega(envNP);
            } else if (!envNP.getNotapedido().getFechadespacho().equals(envNP.getNotapedido().getFechaventa()) && !envNP.getNotapedido().isAdelantar()) {
                actionFactura(fac, envNP, detalleFactura, detalleFactura.getCodigoprecio(), envF);
            }

            precio = new Precio();
            precioPK = new PrecioPK();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionFactura(Factura fac, EnvioPedido envNP, Detallefactura detalleFactura, String codigoPecio, EnvioFactura envF) {
        List<Detallefactura> detFact = new ArrayList<>();
        Detallefactura detalleFact = new Detallefactura();
        DetallefacturaPK detalleFactPK = new DetallefacturaPK();
        BigDecimal totalimpuestos = new BigDecimal(0);
        BigDecimal subtotal = new BigDecimal(0);
        int count = 1;
        try {
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.detalleprecio/paraFactura?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detalleprecio/paraFactura?";
            url = new URL(direcc + "codigo=" + codigoPecio);
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/paraFactura?codigocomercializadora=0002&codigoterminal=02&codigoproducto=0101&codigomedida=01&fechainicio=2021/06/25&codigolistaprecio=a0000001");
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
            for (int indice = 0; indice < retorno.length(); indice++) {
                String bandera = "000" + count;
                if (!retorno.isNull(indice)) {
                    JSONObject detailP = retorno.getJSONObject(indice);
                    JSONObject detailPePK = detailP.getJSONObject("detalleprecioPK");
                    JSONObject grv = detailP.getJSONObject("gravamen");
                    JSONObject grvPK = grv.getJSONObject("gravamenPK");
                    if (grvPK.getString("codigo").equals("0001") || grvPK.getString("codigo").equals("0009")) {
                        System.out.println("Impuesto no usado" + grvPK.getString("codigo"));
                    } else {
                        detalleFactPK.setCodigoabastecedora(fac.getFacturaPK().getCodigoabastecedora());
                        detalleFactPK.setCodigocomercializadora(fac.getFacturaPK().getCodigocomercializadora());
                        detalleFactPK.setNumeronotapedido(fac.getFacturaPK().getNumeronotapedido());
                        detalleFactPK.setNumero(fac.getFacturaPK().getNumero());
                        detalleFactPK.setCodigoproducto(bandera);
                        detalleFact.setDetallefacturaPK(detalleFactPK);
                        detalleFact.setCodigomedida("01");
                        detalleFact.setVolumennaturalautorizado(envNP.getDetalle().getVolumennaturalautorizado());
                        detalleFact.setVolumennaturalrequerido(envNP.getDetalle().getVolumennaturalrequerido());
                        detalleFact.setCodigoprecio(detalleFactura.getCodigoprecio());
                        detalleFact.setPrecioproducto(detalleFactura.getPrecioproducto());
                        subtotal = detalleFactura.getVolumennaturalautorizado().multiply(detailP.getBigDecimal("valor"));
                        detalleFact.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
                        detalleFact.setUsuarioactual(dataUser.getUser().getNombrever());
                        detalleFact.setRuccomercializadora(fac.getRuccomercializadora());
                        detalleFact.setCodigoimpuesto(detailPePK.getString("codigogravamen"));
                        detalleFact.setNombreimpuesto(grv.getString("nombre"));
                        detalleFact.setSeimprime(grv.getBoolean("seimprime"));
                        detalleFact.setValordefecto(new BigDecimal(0));
                        if (detalleFact.getCodigoimpuesto().equals("0002")) {
                            fac.setIvatotal(detalleFact.getSubtotal());
                        }
                        totalimpuestos = totalimpuestos.add(detalleFact.getSubtotal());
                        detFact.add(detalleFact);
                        count++;
                        detalleFact = new Detallefactura();
                        detalleFactPK = new DetallefacturaPK();
                    }
                }
            }
            fac.setValortotal(totalimpuestos.add(fac.getValorsinimpuestos()).setScale(2, RoundingMode.HALF_UP));
            detFact.add(detalleFactura);

            envF.setDetalle(detFact);
            detFact = new ArrayList<>();
            this.addItems(envF);
            //this.getTrama2(envF);
            //this.getTrama3(envF);
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addItems(EnvioFactura envF) {
        try {
            String respuesta;
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura";
            url = new URL(direcc);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000 * 60);
            connection.setReadTimeout(1000 * 60);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setFixedLengthStreamingMode(1000000000);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(envF);
            Gson gson = new Gson();
            String JSON = gson.toJson(envF);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();
            /*JSONObject obj = new JSONObject();
            obj.put("factura", JSON);
            obj.put("detalle", envF.getDetalle());*/

            //respuesta = JSON.toString();
            //writer.write(jsonStr);
            //writer.close();
            //PrimeFaces.current().executeScript("PF('nuevaAreaM').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA REGISTRADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
            }
            System.out.println("Error al añadir:" + connection.getResponseCode());
            System.out.println("Error:" + connection.getErrorStream());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hospedarDocumento(EnvioFactura envF) {
        StringBuilder respuesta = new StringBuilder();

        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000 * 60);
            conn.setReadTimeout(1000 * 60);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = conn.getOutputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                String jsonStr = mapper.writeValueAsString(envF);
                os.write(jsonStr.getBytes());
                os.flush();

                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

                String output;

                //Recibe respuesta del servidor
                while ((output = br.readLine()) != null) {
                    respuesta.append(output);
                }
            }
            conn.disconnect();

        } catch (MalformedURLException ex) {
            System.err.println("Error hospedarDocumento MalformedURLException. {}" + ex);
            respuesta.append("error");
        } catch (IOException ex) {
            System.err.println("Error hospedarDocumento IOException. {}" + ex);
            respuesta.append("error");
        }

        System.err.println("Respuesta servidor hospedaje " + respuesta.toString());
        //return respuesta.toString();
    }

    public String calcularFechaV(Notapedido ntp) {
        Date fechaFestiva = new Date();
        String fechav = "";
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            //String fechaS = date.format(ntp.getFechaventa());
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.fechafestiva/fechafinal?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.fechafestiva/fechafinal?";
            url = new URL(direcc + "codigocomercializadora=" + ntp.getNotapedidoPK().getCodigocomercializadora() + "&fechainicial=" + ntp.getFechaventa() + "&tipoplazo=" + ntp.getCodigocliente().getTipoplazocredito() + "&plazo=" + ntp.getCodigocliente().getDiasplazocredito());
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
            for (int indice = 0; indice < retorno.length(); indice++) {
                if (!retorno.isNull(indice)) {
                    Long fecha = retorno.getLong(indice);
                    fechaFestiva = new Date(fecha);
                }
            }
            fechav = date.format(fechaFestiva);
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
            return fechav;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fechav;
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
        notaPedido.setFechaventa(feachaVenta);
        notaPedido.setFechadespacho(feachaDespacho);
        notaPedido.setActiva(envNP.getNotapedido().isActiva());
        notaPedido.setCodigoautotanque("");
        notaPedido.setCedulaconductor("");
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

        envioPedido.setNotapedido(notaPedido);
        envioPedido.setDetalle(detalleNotaP);
        getTrama(envioPedido, numFact);
    }

    public void getTrama3(EnvioFactura enviofactura) {
        try {
            String respuesta = "";
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/crear");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/crear");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            Gson gson = new Gson();
            String JSON = gson.toJson(enviofactura);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(JSON.getBytes());
            out.flush();
            out.close();
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA REGISTRADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
            }
            //enviarOrdenEntreEpp(envioPedido);
            System.out.println("Error al añadir:" + connection.getResponseCode());
            //System.out.println("Error:" + connection.getErrorStream());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getTrama(EnvioPedido envioPedido, String numFact) {
        try {
            String respuesta = "";
            String trama = "";
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
                enviarOrdenEntreEpp(envioPedido, trama);
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE LOGRÓ GENERAR LA TRAMA DE LA ORDEN DE ENTREGA PARA PETROECUADOR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarOrdenEntreEpp(EnvioPedido envioPedido, String trama) {
        String codigoabastecedora = "";
        String codigocomercializadora = "";
        String numero = "";
        String cadena = "";

        codigoabastecedora = envioPedido.getNotapedido().getAbastecedora().getCodigo();
        codigocomercializadora = envioPedido.getNotapedido().getComercializadora().getCodigo();
        numero = envioPedido.getNotapedido().getNotapedidoPK().getNumero();
        cadena = trama;
        try {
            String codigoPetroGenerado = "";
            String respuesta = "";
            String respuestaPetro = "";
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/envio";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido/envio";
            //http://190.152.15.66:9080/SCI_WS_GOEA_SrvPrv/services/GeneracionOEAbasPrv
            url = new URL(direcc);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(500);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            //connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("codigoabastecedora", codigoabastecedora);
            obj.put("codigocomercializadora", codigocomercializadora);
            obj.put("numero", numero);
            obj.put("cadena", cadena);
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();

//            connection.setDoInput(true);
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Accept", "application/json");
//            //connection.setRequestMethod("HEAD");
//            System.out.println("Conexión petro" + connection.getConnectTimeout());
//            connection.setReadTimeout(5000);
//            System.out.println("Itentando conexión petro" + connection.getReadTimeout());
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            System.err.println("Conexion exitosa");
            connection.setReadTimeout(5000);

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
                    String codigoGenerado = retorno.getString(indice);
                    codigoPetroGenerado = codigoGenerado;
                }
            }

            if (connection.getResponseCode() == 200) {
                if (codigoPetroGenerado.substring(0, 2).equals("00")) {
                    respuestaPetro = "ORDEN ENVIADA A PETRO. RESPUESTA: 00 RECIBIDA CORRECTAMENTE!";
                    this.dialogo(FacesMessage.SEVERITY_INFO, respuestaPetro);
                } else {
                    respuestaPetro = "ORDEN ENVIADA A PETRO. RESPUESTA: " + codigoPetroGenerado.substring(0, 2) + " NO HA SIDO RECIBIDA!";
                    this.dialogo(FacesMessage.SEVERITY_ERROR, respuestaPetro);
                }
                //this.dialogo(FacesMessage.SEVERITY_INFO, "ORDEN ENVIADA A PETROECUADOR SATISFACTORIAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR: NO SE HA PODIDO ENVIAR LA ORDEN A PETROECUADOR");
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (Throwable e) {
            System.out.println("Error envio a petro: " + e.getMessage());
            this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR: NO SE HA PODIDO ENVIAR LA ORDEN A PETROECUADOR");
            e.printStackTrace(System.out);
        }
    }

    public void verificarOeenpetro(EnvioFactura envioFactura) {
        try {
            String respuestaPetro = "";
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/porId?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido/porId?";
            url = new URL(direcc + "&codigoabastecedora=" + envioFactura.getFactura().getFacturaPK().getCodigoabastecedora() + "&codigocomercializadora=" + envioFactura.getFactura().getFacturaPK().getCodigocomercializadora() + "&numero=" + envioFactura.getFactura().getFacturaPK().getNumeronotapedido());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
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
                    JSONObject resp = retorno.getJSONObject(indice);
                    if (!resp.isNull("respuestageneracionoeepp")) {
                        respuestaPetro = resp.getString("respuestageneracionoeepp");
                    }
                }
            }

            if (connection.getResponseCode() == 200) {
                if (respuestaPetro.equals("")) {
                    errorPetro = "LA FACTURA NO HA SIDO ENVIADA A PETROECUADOR";
                    PrimeFaces.current().executeScript("PF('verificarPetro').show()");
                } else {
                    obetnerRespuestaOeenpetro(respuestaPetro);
                }
            } else {
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obetnerRespuestaOeenpetro(String codigoPetro) {
        try {
            String descripcion = "";
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.errorpetro/porId?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.errorpetro/porId?";
            url = new URL(direcc + "proceso=GOEA&codigo=" + codigoPetro);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");

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
                    JSONObject descri = retorno.getJSONObject(indice);
                    if (!descri.isNull("descripcion")) {
                        descripcion = descri.getString("descripcion");
                    }
                }
            }

            if (connection.getResponseCode() == 200) {
                if (codigoPetro.equals("00") || codigoPetro.equals("20")) {
                    errorPetro = "CÓDIGO: " + codigoPetro + " " + descripcion;
                } else {
                    errorPetro = "ERROR " + codigoPetro + " " + descripcion;
                }
                PrimeFaces.current().executeScript("PF('verificarPetro').show()");
            } else {
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (Throwable e) {
            System.err.println("Eror de conexión:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void dialogoReenvioOrdenPetro(EnvioFactura envioFactura) {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");

            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/porId?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido/porId?";
            url = new URL(direcc + "codigoabastecedora=" + envioFactura.getFactura().getFacturaPK().getCodigoabastecedora()
                    + "&codigocomercializadora=" + envioFactura.getFactura().getFacturaPK().getCodigocomercializadora()
                    + "&numero=" + envioFactura.getFactura().getFacturaPK().getNumeronotapedido());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            envioPedidoAuxiliar = new EnvioPedido();
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
                        JSONObject com = nt.getJSONObject("comercializadora");
                        JSONObject abastecedora = nt.getJSONObject("abastecedora");
                        /*----Objeto Abastecedora----*/
                        abas.setCodigo(abastecedora.getString("codigo"));

                        /*----Objeto comercializadora----*/
                        comerc.setCodigo(com.getString("codigo"));

                        /*----Guardando el cliente, termina y banco en Nota pedido---*/
                        np.setComercializadora(comerc);
                        np.setAbastecedora(abas);
                        /*---------------Trama--------------------------------------*/
                        if (!nt.isNull("tramaenviadagoe")) {
                            np.setTramaenviadagoe(nt.getString("tramaenviadagoe"));
                        }
                        np.setNumerofacturasri(nt.getString("numerofacturasri"));
                        np.setActiva(nt.getBoolean("activa"));
                        npPK.setNumero(ntPK.getString("numero"));
                        npPK.setCodigoabastecedora(ntPK.getString("codigoabastecedora"));
                        npPK.setCodigocomercializadora(ntPK.getString("codigocomercializadora"));
                        np.setNotapedidoPK(npPK);
                        envioPedidoAuxiliar.setNotapedido(np);
                        np = new Notapedido();
                        npPK = new NotapedidoPK();
                        comerc = new Comercializadora();
                        abas = new Abastecedora();
                    }
                }
            }
            if (connection.getResponseCode() >= 200 || connection.getResponseCode() <= 200) {
                PrimeFaces.current().executeScript("PF('reenvioPetro').show()");
            } else {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reenvioOrdenPetro() {
        if (envioPedidoAuxiliar != null) {
            try {
                enviarOrdenEntreEpp(envioPedidoAuxiliar, envioPedidoAuxiliar.getNotapedido().getTramaenviadagoe());
                obtenerFacturas();
            } catch (ParseException ex) {
                Logger.getLogger(FacturacionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "HUBO UN ERROR EN LA OBTENCIÓN DE LOS DATOS PARA REALIZAR EL REENVIO");
        }
    }

    public void diaglogoAnulacion(EnvioFactura enviofactura) {
        envFauxiliar = enviofactura;
        try {
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/porId?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/porId?";
            url = new URL(direcc + "codigoabastecedora=" + enviofactura.getFactura().getFacturaPK().getCodigoabastecedora()
                    + "&codigocomercializadora=" + enviofactura.getFactura().getFacturaPK().getCodigocomercializadora()
                    + "&numeronotapedido=" + enviofactura.getFactura().getFacturaPK().getNumeronotapedido()
                    + "&numero=" + enviofactura.getFactura().getFacturaPK().getNumero());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            facturaauxiliar = new Factura();
            facturaauxiliarPK = new FacturaPK();
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
                    JSONObject fac = retorno.getJSONObject(indice);
                    JSONObject facPK = fac.getJSONObject("facturaPK");

                    /*-----------------FACTURA PK-------------------------------------------*/
                    facturaauxiliarPK.setCodigoabastecedora(facPK.getString("codigoabastecedora"));
                    facturaauxiliarPK.setCodigocomercializadora(facPK.getString("codigocomercializadora"));
                    facturaauxiliarPK.setNumero(facPK.getString("numero"));
                    facturaauxiliarPK.setNumeronotapedido(facPK.getString("numeronotapedido"));
                    facturaauxiliar.setFacturaPK(facturaauxiliarPK);
                    /*-----------------------------------------------------------------------*/
                    if (!fac.isNull("activa")) {
                        facturaauxiliar.setActiva(fac.getBoolean("activa"));
                    }
                    if (!fac.isNull("adelantar")) {
                        facturaauxiliar.setAdelantar(fac.getBoolean("adelantar"));
                    }
                    if (!fac.isNull("ambientesri")) {
                        facturaauxiliar.setAmbientesri(fac.getString("ambientesri").charAt(0));
                    }
                    if (!fac.isNull("campoadicionalCampo1")) {
                        facturaauxiliar.setCampoadicionalCampo1(fac.getString("campoadicionalCampo1"));
                    }
                    if (!fac.isNull("campoadicionalCampo2")) {
                        facturaauxiliar.setCampoadicionalCampo2(fac.getString("campoadicionalCampo2"));
                    }
                    if (!fac.isNull("campoadicionalCampo3")) {
                        facturaauxiliar.setCampoadicionalCampo3(fac.getString("campoadicionalCampo3"));
                    }
                    if (!fac.isNull("campoadicionalCampo4")) {
                        facturaauxiliar.setCampoadicionalCampo4(fac.getString("campoadicionalCampo4"));
                    }
                    if (!fac.isNull("campoadicionalCampo5")) {
                        facturaauxiliar.setCampoadicionalCampo5(fac.getString("campoadicionalCampo5"));
                    }
                    if (!fac.isNull("campoadicionalCampo6")) {
                        facturaauxiliar.setCampoadicionalCampo6(fac.getString("campoadicionalCampo6"));
                    }
                    if (!fac.isNull("claveacceso")) {
                        facturaauxiliar.setClaveacceso(fac.getString("claveacceso"));
                    }
                    if (!fac.isNull("clienteformapago")) {
                        facturaauxiliar.setClienteformapago(fac.getString("clienteformapago"));
                    }
                    if (!fac.isNull("clienteformapagonosri")) {
                        facturaauxiliar.setClienteformapagonosri(fac.getString("clienteformapagonosri"));
                    }
                    if (!fac.isNull("codigobanco")) {
                        facturaauxiliar.setCodigobanco(fac.getString("codigobanco"));
                    }
                    if (!fac.isNull("codigocliente")) {
                        facturaauxiliar.setCodigocliente(fac.getString("codigocliente"));
                    }
                    if (!fac.isNull("codigodocumento")) {
                        facturaauxiliar.setCodigodocumento(fac.getString("codigodocumento"));
                    }
                    if (!fac.isNull("codigoterminal")) {
                        facturaauxiliar.setCodigoterminal(fac.getString("codigoterminal"));
                    }
                    facturaauxiliar.setCorreocliente(fac.getString("correocliente"));
                    facturaauxiliar.setDireccioncliente(fac.getString("direccioncliente"));
                    facturaauxiliar.setDireccionmatrizcomercializadora(fac.getString("direccionmatrizcomercializadora"));

                    Number errorDoc = fac.getNumber("errordocumento");
                    facturaauxiliar.setErrordocumento(errorDoc.shortValue());
                    facturaauxiliar.setEsagenteretencion(fac.getBoolean("esagenteretencion"));
                    facturaauxiliar.setEscontribuyenteespacial(fac.getString("escontribuyenteespacial"));
                    facturaauxiliar.setEstado(fac.getString("estado"));
                    /*----------------Fecha Acreditacion------------------------*/
                    Long fechaAcred = fac.getLong("fechaacreditacion");
                    Date dateAcerd = new Date(fechaAcred);
                    SimpleDateFormat dateAc = new SimpleDateFormat("yyyy-MM-dd");
                    String fechaAcreditacion = dateAc.format(dateAcerd);
                    facturaauxiliar.setFechaacreditacion(fechaAcreditacion);
                    /*------------------Fecha Venta-----------------------------------*/
                    Long fechaVen = fac.getLong("fechaventa");
                    Date dateVen = new Date(fechaVen);
                    String fechaVenta = dateAc.format(dateVen);
                    facturaauxiliar.setFechaventa(fechaVenta);
                    /*------------------Fecha Vencimiento------------------------------*/
                    Long fechaVenci = fac.getLong("fechavencimiento");
                    Date dateVenci = new Date(fechaVenci);
                    String fechaVencimiento = dateAc.format(dateVenci);
                    facturaauxiliar.setFechavencimiento(fechaVencimiento);
                    /*-----------------Fecha Despacho-----------------------------------*/
                    Long fechaDes = fac.getLong("fechadespacho");
                    Date dateDes = new Date(fechaDes);
                    String fechaDespacho = dateAc.format(dateDes);
                    facturaauxiliar.setFechadespacho(fechaDespacho);
                    /*-----------------Fecha Acreditacion Prorrogada--------------------*/
                    Long fechaAcredPr = fac.getLong("fechaacreditacionprorrogada");
                    Date dateAcredPr = new Date(fechaAcredPr);
                    String fechaProrro = dateAc.format(dateAcredPr);
                    facturaauxiliar.setFechaacreditacionprorrogada(fechaProrro);
                    facturaauxiliar.setFechaautorizacion(fac.getString("fechaautorizacion"));

                    Number hospedado = fac.getNumber("hospedado");
                    facturaauxiliar.setHospedado(hospedado.shortValue());

                    facturaauxiliar.setIvatotal(fac.getBigDecimal("ivatotal"));
                    facturaauxiliar.setMoneda(fac.getString("moneda"));
                    facturaauxiliar.setNombrecliente(fac.getString("nombrecliente"));
                    facturaauxiliar.setNombrecomercializadora(fac.getString("nombrecomercializadora"));
                    facturaauxiliar.setNumeroautorizacion(fac.getString("numeroautorizacion"));
                    facturaauxiliar.setObligadocontabilidad(fac.getString("obligadocontabilidad"));
                    facturaauxiliar.setObservacion(fac.getString("observacion"));
                    facturaauxiliar.setOeenpetro(fac.getBoolean("oeenpetro"));
                    facturaauxiliar.setPagada(fac.getBoolean("pagada"));
                    facturaauxiliar.setPlazocliente(fac.getInt("plazocliente"));
                    facturaauxiliar.setRuccliente(fac.getString("ruccliente"));
                    facturaauxiliar.setRuccomercializadora(fac.getString("ruccomercializadora"));
                    facturaauxiliar.setSeriesri(fac.getString("seriesri"));
                    facturaauxiliar.setTelefonocliente(fac.getString("telefonocliente"));
                    facturaauxiliar.setTipocomprador(fac.getString("tipocomprador"));

                    facturaauxiliar.setTipoplazocredito(fac.getString("tipoplazocredito"));
                    facturaauxiliar.setUsuarioactual(fac.getString("usuarioactual"));
                    facturaauxiliar.setValorconrubro(fac.getBigDecimal("valorconrubro"));
                    facturaauxiliar.setValorsinimpuestos(fac.getBigDecimal("valorsinimpuestos"));
                    facturaauxiliar.setValortotal(fac.getBigDecimal("valortotal"));
                }

            }
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('deleteProductDialog').show()");
            } else {
                System.out.println("Error al obtener factura:" + connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void verficarAnulacion() {
        if (facturaauxiliar != null) {
            if (facturaauxiliar.getPagada() == false) {
                if (facturaauxiliar.getNumeroautorizacion().equals("")) {
                    anularFactura(facturaauxiliar);
                    try {
                        obtenerFacturas();
                    } catch (ParseException ex) {
                        Logger.getLogger(FacturacionBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "LA FACTURA NO PUEDE SER ANULADA");
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "LA FACTURA NO SE PUEDE ANULAR DEBIDO A QUE YA ESTÁ PAGADA");
            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "HUBO UN ERROR EN LA ANULACIÓN");
        }
    }

    public void anularFactura(Factura fac) {
        //B350002141234560000AAA000000000000000000
        //B3600025400010112345678000000000000000000
        //WPE.CODBCO	char(2)
        //WPE CODCOM	numeric(4.0)
        //WPE NUMFAC	numeric(8.0)
        //WPE CLAENV	char(8)  agregar ceros al inicio o al final 
        //FL	X(18)
        //360002021234560000AAAA000000000000000000
        String fl = "000000000000000000";

        String cadena = facturaauxiliar.getCodigobanco().trim() + facturaauxiliar.getFacturaPK().getCodigocomercializadora().trim()
                + facturaauxiliar.getFacturaPK().getNumeronotapedido().trim() + comercializadora.getClaveWsepp().trim() + fl;
        try {
            String codigoanulacion = "";
            String respuesta = "";
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/cancelacion";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido/cancelacion";
            url = new URL(direcc);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            anulacion.setCodigoabastecedora(facturaauxiliar.getFacturaPK().getCodigoabastecedora().trim());
            anulacion.setCodigocomercializadora(facturaauxiliar.getFacturaPK().getCodigocomercializadora().trim());
            anulacion.setNumero(facturaauxiliar.getFacturaPK().getNumeronotapedido().trim());
            anulacion.setCadena(cadena);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            Gson gson = new Gson();
            String JSON = gson.toJson(anulacion);
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
                    String codanul = retorno.getString(indice);
                    codigoanulacion = codanul;
                }
            }

            if (connection.getResponseCode() == 200) {
                if (codigoanulacion.substring(0, 2).equals("00") || codigoanulacion.substring(0, 2).equals("01") || codigoanulacion.substring(0, 2).equals("03")) {
                    this.dialogo(FacesMessage.SEVERITY_INFO, codigoanulacion.substring(0, 2) + " LA ANULACIÓN SE PROCESO CORRECTAMENTE");
                    //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/porId");
                    url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/porId");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("PUT");
                    connection.setRequestProperty("Content-type", "application/json");
                    connection.connect();
                    facturaauxiliar.setFechaacreditacion(facturaauxiliar.getFechaacreditacion() + "T12:00:00");
                    facturaauxiliar.setFechaacreditacionprorrogada(facturaauxiliar.getFechaacreditacionprorrogada() + "T12:00:00");
                    facturaauxiliar.setFechadespacho(facturaauxiliar.getFechadespacho() + "T12:00:00");
                    facturaauxiliar.setFechavencimiento(facturaauxiliar.getFechavencimiento() + "T12:00:00");
                    facturaauxiliar.setFechaventa(facturaauxiliar.getFechaventa() + "T12:00:00");
                    facturaauxiliar.setActiva(false);
                    facturaauxiliar.setEstado("ANULADA");
                    facturaauxiliar.setOeanuladaenpetro(true);
                    facturaauxiliar.setUsuarioactual(dataUser.getUser().getNombrever());
                    writer = new OutputStreamWriter(connection.getOutputStream());
                    gson = new Gson();
                    JSON = gson.toJson(facturaauxiliar);
                    out = new DataOutputStream(connection.getOutputStream());
                    out.write(JSON.getBytes());
                    out.flush();
                    out.close();

                    if (connection.getResponseCode() == 200) {
                        for (int i = 0; i < listenvF.size(); i++) {
                            if (listenvF.get(i).getFactura().getFacturaPK().getNumero().equals(facturaauxiliar.getFacturaPK().getNumero())) {
                                listenvF.get(i).setFactura(facturaauxiliar);
                            }
                        }
                        System.out.println("Factura actualizada");
                    } else {
                        System.out.println("Error al añadir: " + connection.getResponseCode());
                        System.out.println("ResponseMesagge: " + connection.getResponseMessage());
                        System.out.println("getErrorStream: " + connection.getErrorStream());
                    }
                } else {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, codigoanulacion.substring(0, 2) + " ANULACIÓN OE NO PERMITIDA EN EPP");
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR EN LA ANULACIÓN");
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (Throwable e) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR EN LA ANULACIÓN");
            System.out.println("Error" + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    public void verificarAnulacion(EnvioFactura envioFactura) {
        try {
            String respuestaAnulacion = "";
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/porId?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido/porId?";
            url = new URL(direcc
                    + "&codigoabastecedora=" + envioFactura.getFactura().getFacturaPK().getCodigoabastecedora()
                    + "&codigocomercializadora=" + envioFactura.getFactura().getFacturaPK().getCodigocomercializadora()
                    + "&numero=" + envioFactura.getFactura().getFacturaPK().getNumeronotapedido());
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
            for (int indice = 0; indice < retorno.length(); indice++) {
                if (!retorno.isNull(indice)) {
                    JSONObject resp = retorno.getJSONObject(indice);
                    if (!resp.isNull("respuestaanulacionoeepp")) {
                        respuestaAnulacion = resp.getString("respuestaanulacionoeepp");
                    }
                }
            }

            if (connection.getResponseCode() == 200) {
                if (respuestaAnulacion.equals("")) {
                    mensajeAnulacion = "EPP NO HA ANULADO ESTA ORDEN";
                    PrimeFaces.current().executeScript("PF('verificarAnulacion').show()");
                } else {
                    obetnerRespuestaAnulacion(respuestaAnulacion);
                }
            } else {
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obetnerRespuestaAnulacion(String codigoAnulacion) {
        try {
            String descripcionA = "";
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.errorpetro/porId?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.errorpetro/porId?";
            url = new URL(direcc + "proceso=AOEA&codigo=" + codigoAnulacion);
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
            for (int indice = 0; indice < retorno.length(); indice++) {
                if (!retorno.isNull(indice)) {
                    JSONObject descri = retorno.getJSONObject(indice);
                    if (!descri.isNull("descripcion")) {
                        descripcionA = descri.getString("descripcion");
                    }
                }
            }
            if (connection.getResponseCode() == 200) {
                if (codigoAnulacion.equals("00") || codigoAnulacion.equals("01") || codigoAnulacion.equals("03")) {
                    errorPetro = "LA FACTURA ESTÁ ANULADA CÓDIGO: " + codigoAnulacion + " " + descripcionA;
                } else {
                    errorPetro = "ERROR " + codigoAnulacion + " " + descripcionA;
                }
                PrimeFaces.current().executeScript("PF('verificarPetro').show()");
            } else {
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    public void verificarSri(EnvioFactura envioFactura) {
        if (envioFactura != null) {
            if (envioFactura.getFactura().getNumeroautorizacion() != null) {
                if (envioFactura.getFactura().getNumeroautorizacion().equals("")) {
                    sriMensaje = "La factura no se encuentra en el SRI";
                } else {
                    if (envioFactura.getFactura().getNumeroautorizacion().length() == 49) {
                        sriMensaje = "La factura se encuentra en el SRI con número de autorización: " + envioFactura.getFactura().getNumeroautorizacion();
                    }
                }
            } else {
                sriMensaje = "La factura no se encuentra en el SRI";
            }
            PrimeFaces.current().executeScript("PF('verificarSri').show()");
        }
    }

    public void generarReporte(EnvioFactura env) {
//        String path = "C:\\archivos\\Template\\NuevaFactura.jrxml";
//        String subreport = "C:\\archivos\\Template\\SubreporteFacturaRubros.jrxml";
//        String subreport1 = "C:\\archivos\\Template\\SubreporteFacturaImpuestos.jrxml";

        String path = Fichero.getCARPETAREPORTES() + "/NuevaFactura.jrxml";
        String subreport = Fichero.getCARPETAREPORTES() + "/SubreporteFacturaRubros.jrxml";
        String subreport1 = Fichero.getCARPETAREPORTES() + "/SubreporteFacturaImpuestos.jrxml";
        System.out.println("PATH:" + path);
        InputStream file = null;
        try {
            file = new FileInputStream(new File(path));

            JasperReport reporte = JasperCompileManager.compileReport(file);
            JasperReport subreporte = JasperCompileManager.compileReport(subreport);
            JasperReport subreporte1 = JasperCompileManager.compileReport(subreport1);

            Map parametro = new HashMap();
            BufferedImage image = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/logo.jpeg"));
            BufferedImage imageBar = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/barras.jpeg"));

//            BufferedImage image = ImageIO.read(new File("C:\\archivos\\Template\\logo.jpg"));
//            BufferedImage imageBar = ImageIO.read(new File("C:\\archivos\\Template\\barras.jpg"));
            parametro.put("numeroComercializadora", env.getFactura().getFacturaPK().getCodigocomercializadora());
            parametro.put("subReporte", subreporte);
            parametro.put("subReporte1", subreporte1);
            parametro.put("numeroFactura", env.getFactura().getFacturaPK().getNumero());
            parametro.put("logo", image);
            parametro.put("barras", imageBar);

            //actual local
            Connection conexion = conexionJasperBD();

            JasperPrint print = JasperFillManager.fillReport(reporte, parametro, conexion);

            File directory = new File(Fichero.getCARPETAREPORTES());
//            File directory = new File("C:\\archivos");

            String nombreDocumento = "reporteFactura";

            File pdf = File.createTempFile(nombreDocumento + "_", ".pdf", directory);
            JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
            File initialFile = new File(pdf.getAbsolutePath());
            InputStream targetStream = new FileInputStream(initialFile);
            pdfStream = new DefaultStreamedContent();
            pdfStream = new DefaultStreamedContent(targetStream, "application/pdf", nombreDocumento + env.getFactura().getFacturaPK().getNumero() + ".pdf");
            //DefaultStreamedContent.builder().contentType("application/pdf").name(nombreDocumento + ".pdf").stream(() -> new FileInputStream(targetStream)).build();
            PrimeFaces.current().executeScript("window.open(" + directory + ",'" + nombreDocumento + "','fullscreen=yes');parent.opener=top;");
            System.err.print(pdf.getAbsolutePath());
            System.out.println(pdf.getAbsolutePath());
        } catch (Exception ex) {
            //ex.printStackTrace();
            System.out.println("Excepcion: " + ex);
        }
    }

    public void generarReporteNp(EnvioPedido envP) {
        //        String path = "C:\\archivos\\Template\\FormatoNotaPedido.jrxml";
//        String subreport = "C:\\archivos\\Template\\notapedido.jrxml";
        String rutaGuardar = Fichero.getCARPETAREPORTES();
        String subreport = Fichero.getCARPETAREPORTES() + "/notapedido.jrxml";
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

    public EnvioFactura editarFactura(EnvioFactura obj, int n) {
        envF = obj;
        if (envF.getFactura().getActiva()) {
            if (!envF.getFactura().getPagada()) {
                if (envF.getFactura().getOeenpetro()) {
                    for (int i = 0; i < listaFormapagos.size(); i++) {
                        if (envF.getFactura().getClienteformapagonosri().equals(listaFormapagos.get(i).getCodigo())) {
                            formapago = listaFormapagos.get(i);
                            break;
                        }
                    }
                    for (int i = 0; i < listaBancos.size(); i++) {
                        if (envF.getFactura().getCodigobanco().equals(listaBancos.get(i).getCodigo())) {
                            banco = listaBancos.get(i);
                            break;
                        }
                    }
                    if (n == 1) {
                        PrimeFaces.current().executeScript("PF('editar').show()");
                    } else {
                        PrimeFaces.current().executeScript("PF('editarDesp').show()");
                    }
                    return envF;
                } else {
                    this.dialogo(FacesMessage.SEVERITY_WARN, "La factura no se encuentra en Petro");
                    return null;
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_WARN, "La factura se encuentra pagada");
                return null;
            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_WARN, "La factura no se encuentra Activa");
            return null;
        }
    }

    public void editarPagoFactura() throws ParseException {
        if (envF != null) {
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            if (envF.getFactura().getFechaacreditacion() != null) {
                Date fechaA = formato.parse(envF.getFactura().getFechadespacho().replace("/", "-"));
                envF.getFactura().setFechaacreditacion(date.format(fechaA));
            }
            if (envF.getFactura().getFechadespacho() != null) {
                Date fechaA = formato.parse(envF.getFactura().getFechadespacho().replace("/", "-"));
                envF.getFactura().setFechadespacho(date.format(fechaA));
            }
            if (envF.getFactura().getFechavencimiento() != null) {
                Date fechaA = formato.parse(envF.getFactura().getFechavencimiento().replace("/", "-"));
                envF.getFactura().setFechavencimiento(date.format(fechaA));
            }
            if (envF.getFactura().getFechaventa() != null) {
                Date fechaA = formato.parse(envF.getFactura().getFechaventa().replace("/", "-"));
                envF.getFactura().setFechaventa(date.format(fechaA));
            }
            if (envF.getFactura().getFechaacreditacionprorrogada() != null) {
                Date fechaA = formato.parse(envF.getFactura().getFechaacreditacionprorrogada().replace("/", "-"));
                envF.getFactura().setFechaacreditacionprorrogada(date.format(fechaA));
            }
            envF.getFactura().setClienteformapagonosri(formapago.getCodigo());
            actualizarFactura(envF.getFactura());
            obtenerFacturas();
        }
    }

    public void seleccionarBanco() {
        if (banco != null) {
        }
    }

    public void actualizarFactura(Factura fact) {
        try {
            String respuesta;
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/porId");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/porId");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000 * 60);
            connection.setReadTimeout(1000 * 60);
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");
            //connection.setFixedLengthStreamingMode(1000000000);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(fact);
            Gson gson = new Gson();
            String JSON = gson.toJson(fact);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('editar').hide()");
                PrimeFaces.current().executeScript("PF('editarDesp').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA ACUTALIZADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int controlDespacho(String codClienteNP) {
        try {
            String respuesta = "";
            int value = 0;
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/porId");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/controlDespacho?pcodigocliente=" + codClienteNP);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            if (retorno.isEmpty()) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCONTRARON REGISTROS");
            } else {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        value = retorno.getInt(indice);
                    }
                }
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return value;
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        }
    }

    public void buscarFacturaPorNp(EnvioPedido envP) {
        if (!envP.getNotapedido().getNumerofacturasri().equals("0               ")) {
            EnvioFactura env = new EnvioFactura();
            Factura factura = new Factura();
            FacturaPK facturaPK = new FacturaPK();
            facturaPK.setCodigocomercializadora(envP.getNotapedido().getNotapedidoPK().getCodigocomercializadora());
            facturaPK.setNumero(envP.getNotapedido().getNumerofacturasri());
            factura.setFacturaPK(facturaPK);
            env.setFactura(factura);
            generarReporte(env);
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE HA GENERADO LA FACTURA");
        }
    }

    public void editarFechaFactura() {
        boolean bandera = true;
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String codFacSelec = params.get("form:dt-products_selection");
        String[] parts = codFacSelec.split(",");
        String[] campos = new String[4];
        listFactSelec = new ArrayList<>();
        for (int i = 0; i < listFact.size(); i++) {
            for (int cont = 0; cont < parts.length; cont++) {
                campos = parts[cont].split("-");
                if (campos[0].equals(listFact.get(i).getFacturaPK().getCodigoabastecedora()) && campos[1].equals(listFact.get(i).getFacturaPK().getCodigocomercializadora()) && campos[2].equals(listFact.get(i).getFacturaPK().getNumero()) && campos[3].equals(listFact.get(i).getFacturaPK().getNumeronotapedido())) {
                    listFactSelec.add(listFact.get(i));
                }
            }
        }
        if (!listFactSelec.isEmpty()) {
            for (int i = 0; i < listFactSelec.size(); i++) {
                if (bandera) {
                    if (listFactSelec.get(i).getActiva()) {
                        if (!listFactSelec.get(i).getPagada()) {
                            if (listFactSelec.get(i).getOeenpetro()) {
                                if (fechaPro != null) {
                                    bandera = true;
                                } else {
                                    bandera = false;
                                    this.dialogo(FacesMessage.SEVERITY_ERROR, "Selecione una fecha de Prorroga");
                                }
                            } else {
                                bandera = false;
                                this.dialogo(FacesMessage.SEVERITY_WARN, "La factura N." + listFactSelec.get(i).getFacturaPK().getNumero() + " no se encuentra en Petro");
                            }
                        } else {
                            bandera = false;
                            this.dialogo(FacesMessage.SEVERITY_WARN, "La factura N." + listFactSelec.get(i).getFacturaPK().getNumero() + " se encuentra pagada");
                        }
                    } else {
                        bandera = false;
                        this.dialogo(FacesMessage.SEVERITY_WARN, "La factura N." + listFactSelec.get(i).getFacturaPK().getNumero() + " no se encuentra Activa");
                    }
                }
            }
            if (bandera) {
                PrimeFaces.current().executeScript("PF('editFechaDialog').show()");
            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "No se han seleccionado facturas");
        }
    }

    public void editarFechaProFactura() throws ParseException {
        if (!listFactSelec.isEmpty()) {
            for (int i = 0; i < listFactSelec.size(); i++) {
                DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                if (listFactSelec.get(i).getFechaacreditacion() != null) {
                    Date fechaA = formato.parse(listFactSelec.get(i).getFechaacreditacion().replace("/", "-"));
                    listFactSelec.get(i).setFechaacreditacion(date.format(fechaA));
                }
                if (listFactSelec.get(i).getFechadespacho() != null) {
                    Date fechaA = formato.parse(listFactSelec.get(i).getFechadespacho().replace("/", "-"));
                    listFactSelec.get(i).setFechadespacho(date.format(fechaA));
                }
                if (listFactSelec.get(i).getFechavencimiento() != null) {
                    Date fechaA = formato.parse(listFactSelec.get(i).getFechavencimiento().replace("/", "-"));
                    listFactSelec.get(i).setFechavencimiento(date.format(fechaA));
                }
                if (listFactSelec.get(i).getFechaventa() != null) {
                    Date fechaA = formato.parse(listFactSelec.get(i).getFechaventa().replace("/", "-"));
                    listFactSelec.get(i).setFechaventa(date.format(fechaA));
                }
                listFactSelec.get(i).setFechaacreditacionprorrogada(date.format(fechaPro));
                actualizarFactura(listFactSelec.get(i));
            }
            obtenerFacturasProrrogas();
            listFactSelec = new ArrayList<>();
        }
    }

    public void validarNumFacturasCliente(EnvioPedido envP) {
        if (envP != null) {
            String codClienteNP = envP.getNotapedido().getCodigocliente().getCodigo();
            int value = controlDespacho(codClienteNP);
            if (value > -1) {
                if (envP.getNotapedido().getCodigocliente().getControldespacho() < value) {
                    envP.getNotapedido().setProcesar(false);
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "Este cliente tiene mas facturas sin despachar que las permitidas");
                }
            }
        }
    }

    public boolean isMostarFactura() {
        return mostarFactura;
    }

    public void setMostarFactura(boolean mostarFactura) {
        this.mostarFactura = mostarFactura;
    }

    public boolean isMostarPantallaInicial() {
        return mostarPantallaInicial;
    }

    public void setMostarPantallaInicial(boolean mostarPantallaInicial) {
        this.mostarPantallaInicial = mostarPantallaInicial;
    }

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public String getCodComer() {
        return codComer;
    }

    public void setCodComer(String codComer) {
        this.codComer = codComer;
    }

    public List<TerminalBean> getListaTermianles() {
        return listaTermianles;
    }

    public void setListaTermianles(List<TerminalBean> listaTermianles) {
        this.listaTermianles = listaTermianles;
    }

    public String getCodTerminal() {
        return codTerminal;
    }

    public void setCodTerminal(String codTerminal) {
        this.codTerminal = codTerminal;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
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

    public TerminalBean getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalBean terminal) {
        this.terminal = terminal;
    }

    public List<EnvioFactura> getListenvF() {
        return listenvF;
    }

    public void setListenvF(List<EnvioFactura> listenvF) {
        this.listenvF = listenvF;
    }

    public String getOeenpetro() {
        return oeenpetro;
    }

    public void setOeenpetro(String oeenpetro) {
        this.oeenpetro = oeenpetro;
    }

    public List<EnvioPedido> getListenvNP() {
        return listenvNP;
    }

    public void setListenvNP(List<EnvioPedido> listenvNP) {
        this.listenvNP = listenvNP;
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

    public EnvioPedido getEnvNP() {
        return envNP;
    }

    public void setEnvNP(EnvioPedido envNP) {
        this.envNP = envNP;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public boolean isAdelantar() {
        return adelantar;
    }

    public void setAdelantar(boolean adelantar) {
        this.adelantar = adelantar;
    }

    public boolean isProcesar() {
        return procesar;
    }

    public void setProcesar(boolean procesar) {
        this.procesar = procesar;
    }

    public String getEnsri() {
        return ensri;
    }

    public void setEnsri(String ensri) {
        this.ensri = ensri;
    }

    public Factura getFact() {
        return fact;
    }

    public void setFact(Factura fact) {
        this.fact = fact;
    }

    public EnvioFactura getEnvF() {
        return envF;
    }

    public void setEnvF(EnvioFactura envF) {
        this.envF = envF;
    }

    public String getErrorPetro() {
        return errorPetro;
    }

    public void setErrorPetro(String errorPetro) {
        this.errorPetro = errorPetro;
    }

    public boolean isSoloporProcesar() {
        return soloporProcesar;
    }

    public void setSoloporProcesar(boolean soloporProcesar) {
        this.soloporProcesar = soloporProcesar;
    }

    public String getMensajeAnulacion() {
        return mensajeAnulacion;
    }

    public void setMensajeAnulacion(String mensajeAnulacion) {
        this.mensajeAnulacion = mensajeAnulacion;
    }

    public boolean isEstadoAnulacion() {
        return estadoAnulacion;
    }

    public void setEstadoAnulacion(boolean estadoAnulacion) {
        this.estadoAnulacion = estadoAnulacion;
    }

    public StreamedContent getPdfStream() {
        return pdfStream;
    }

    public void setPdfStream(StreamedContent pdfStream) {
        this.pdfStream = pdfStream;
    }

    public boolean isBuscarFacturaXCliente() {
        return buscarFacturaXCliente;
    }

    public void setBuscarFacturaXCliente(boolean buscarFacturaXCliente) {
        this.buscarFacturaXCliente = buscarFacturaXCliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getFechaI() {
        return fechaI;
    }

    public void setFechaI(Date fechaI) {
        this.fechaI = fechaI;
    }

    public Date getFechaf() {
        return fechaf;
    }

    public void setFechaf(Date fechaf) {
        this.fechaf = fechaf;
    }

    public String getSriMensaje() {
        return sriMensaje;
    }

    public void setSriMensaje(String sriMensaje) {
        this.sriMensaje = sriMensaje;
    }

    public boolean isProcess() {
        return process;
    }

    public void setProcess(boolean process) {
        this.process = process;
    }

    public Formapago getFormapago() {
        return formapago;
    }

    public void setFormapago(Formapago formapago) {
        this.formapago = formapago;
    }

    public List<Formapago> getListaFormapagos() {
        return listaFormapagos;
    }

    public void setListaFormapagos(List<Formapago> listaFormapagos) {
        this.listaFormapagos = listaFormapagos;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public List<Banco> getListaBancos() {
        return listaBancos;
    }

    public void setListaBancos(List<Banco> listaBancos) {
        this.listaBancos = listaBancos;
    }

    public List<Factura> getListFactSelec() {
        return listFactSelec;
    }

    public void setListFactSelec(List<Factura> listFactSelec) {
        this.listFactSelec = listFactSelec;
    }

    public List<Factura> getListFact() {
        return listFact;
    }

    public void setListFact(List<Factura> listFact) {
        this.listFact = listFact;
    }

    public Date getFechaPro() {
        return fechaPro;
    }

    public void setFechaPro(Date fechaPro) {
        this.fechaPro = fechaPro;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

}
