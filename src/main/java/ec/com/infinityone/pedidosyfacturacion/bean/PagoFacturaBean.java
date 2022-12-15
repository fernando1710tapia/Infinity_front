/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.pedidosyfacturacion.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ec.com.infinityone.actorcomercial.serivicios.ComercializadoraServicio;
import ec.com.infinityone.catalogo.servicios.BancoServicio;
import ec.com.infinityone.modeloWeb.Banco;
import ec.com.infinityone.modeloWeb.Detallepago;
import ec.com.infinityone.modeloWeb.DetallepagoPK;
import ec.com.infinityone.modeloWeb.Factura;
import ec.com.infinityone.modeloWeb.FacturaPK;
import ec.com.infinityone.modeloWeb.ObjFactura;
import ec.com.infinityone.modeloWeb.Pagofactura;
import ec.com.infinityone.modeloWeb.PagofacturaPK;
import ec.com.infinityone.pedidosyfacturacion.servicios.FacturaServicio;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.actorcomercial.bean.ComercializadoraBean;
import ec.com.infinityone.actorcomercial.serivicios.ClienteServicio;
import ec.com.infinityone.catalogo.servicios.TerminalServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Cliente;
import ec.com.infinityone.modeloWeb.Pagosbancorechazados;
import ec.com.infinityone.modeloWeb.PagosbancorechazadosPK;
import ec.com.infinityone.modeloWeb.Temporalparacobrar;
import ec.com.infinityone.modeloWeb.TemporalparacobrarPK;
import ec.com.infinityone.modeloWeb.Terminal;
import ec.com.infinityone.modeloWeb.TotalParaCobrar;
import ec.com.infinityone.pedidosyfacturacion.servicios.TemporalCobrarServicios;
import ec.com.infinityone.pedidosyfacturacion.servicios.TotalCobrarServicios;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class PagoFacturaBean extends ReusableBean implements Serializable {

    /*
    Variable para acceder a los servicios de Banco
     */
    @Inject
    private BancoServicio bancoServicio;
    /*
    Variable para acceder a los servicios de Comercializadora
     */
    @Inject
    private ComercializadoraServicio comercializadoraServicio;
    /*
    Variable para acceder a los servicios de factura
     */
    @Inject
    private FacturaServicio facturaServicio;
    /*
    Variable para acceder a los servicios temporalcobrarServicios
     */
    @Inject
    private TemporalCobrarServicios temporalServicios;
    /*
    Variable para acceder a los servicios de totalcobros
     */
    @Inject
    private TotalCobrarServicios totalCobroServicio;
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
    Variable que almacena varios Bancos
     */
    private List<Pagofactura> listaPagofactura;

    private List<Pagofactura> listaPagofacturaArchivoSubida;

    private List<Detallepago> listaDetallePagofacturaArchivoSubida;

    private List<Pagosbancorechazados> listaPagosbancorechazados;
    /*
    Variable que almacena varios Bancos
     */
    private List<Detallepago> listaDetallePago;

    private List<ComercializadoraBean> listaComercializadora;

    private List<ComercializadoraBean> listaComercializadoraAux;

    private List<Factura> listaFactura;

    private List<Factura> listaFacturaSeleccionada;

    private List<Factura> listaFacturaUnida;

    private List<ObjFactura> listaobjFactura;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarPago;
    /*
    Variable que establece true or false para el estado del Banco
     */
    private boolean estadoPago;

    private Pagofactura pagofactura;

    private Detallepago detallepago;

    private ComercializadoraBean comercializadora;

    private String tipoBusquedaDocumento;

    private PagofacturaPK pagofacturaPK;

    private DetallepagoPK detallepagoPK;

    private PagoFacturaBean pagoFacturaBean;

    private Factura factura;

    private FacturaPK facturaPK;

    private Pagosbancorechazados pagosbancorechazados;

    private PagosbancorechazadosPK pagosbancorechazadosPK;

    private String codigoComer;
    /*
    varibale para guardar la observacion
     */
    private String observacion;

    private String numero;

    private Date fecha;

    private Date fecha1;

    private Date fechaDep;
    /*
    Variable Banco
     */
    private Banco banco;
    /*
    Lista Bancos
     */
    private List<Banco> listaBancos;
    /*
    Variable Terminal
     */
    protected Terminal terminal;
    /*
    Variable que almacena el código del terminal
     */
    protected String codTerminal;
    /*
    Variable para almacenar los datos comercializadora
     */
    protected List<Terminal> listaTermianles;
    /*
    Variable Cliente
     */
    protected Cliente cliente;
    /*
    Variable que almacena el código del cliente
     */
    protected String codCliente;
    /*
    Variable para almacenar los datos clientes
     */
    protected List<Cliente> listaClientes;

    private File fileLeer;

    private String ubicacion;

    private BigDecimal suma;

    /*
    variable para mostrar pantalla gestionarcobro
     */
    private boolean gestionarCobro;
    /*
    variable para mostrar pantalla pagoDirecto
     */
    private boolean pagoDirecto;
    /*
    variable para mostrar pantalla consultarFactura
     */
    private boolean consultarFactura;
    /*
    variable para mostrar la pantalla inicial
     */
    private boolean pantallaInicial;
    /*
    variable para mostrar la pantalla de valores generados
     */
    private boolean valoresGeneredos;
    /*
    Variable que instancia la entidad temporalparacobrar
     */
    private Temporalparacobrar tempCobros;
    /*
    Variable que instancia la entidad temporalparacobrarPK
     */
    private TemporalparacobrarPK tempCobrosPK;
    /*
    Varibale para almacenar una lista de temporalparacobrar
     */
    private List<TotalParaCobrar> listaTotalCobros;
    /*
    variable para almacerna fecha convertida
     */
    private String fechaConvertida;
    /*
    varibale para almacenar la obseracion
     */
    private String observ;
    /*
    Vairbale para almacenar el pdf generado
     */
    private StreamedContent txtStream;

    private List<Factura> listaFacturaAux;
    private List<Factura> listaFacturaPagadasAux;

    private List<Pagosbancorechazados> listaPagosBancoRechazadoAux;

    private HashMap<String, String> codigos;

    /**
     * Constructor por defecto
     */
    public PagoFacturaBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        eliminarTemporalesCorbo();
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.pagofactura";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.pagofactura";
        editarPago = false;
        pagofactura = new Pagofactura();
        detallepago = new Detallepago();
        pagofacturaPK = new PagofacturaPK();
        detallepagoPK = new DetallepagoPK();
        pagoFacturaBean = new PagoFacturaBean();
        comercializadora = new ComercializadoraBean();
        factura = new Factura();
        facturaPK = new FacturaPK();
        tipoBusquedaDocumento = "1";
        banco = new Banco();
        listaBancos = new ArrayList<>();
        listaFacturaSeleccionada = new ArrayList<>();
        listaFacturaUnida = new ArrayList<>();
        listaobjFactura = new ArrayList();
        listaPagofacturaArchivoSubida = new ArrayList<>();
        listaPagofactura = new ArrayList<>();
        gestionarCobro = false;
        consultarFactura = false;
        pagoDirecto = false;
        pantallaInicial = true;
        valoresGeneredos = false;
        tempCobros = new Temporalparacobrar();
        tempCobrosPK = new TemporalparacobrarPK();
        pagosbancorechazados = new Pagosbancorechazados();
        pagosbancorechazadosPK = new PagosbancorechazadosPK();
        listaTotalCobros = new ArrayList<>();
        listaFacturaAux = new ArrayList<>();
        listaFacturaPagadasAux = new ArrayList<>();
        obtenerTerminales();
        obtenerComercializadora();
        obtenerBancos();
        obtenerCodigosResultados();
        observ = "";
        numero = "";
        codCliente = "-1";
        codTerminal = "-1";
        //habilitarBusqueda();
        //obtenerPagoFactura(listaComercializadora.get(0).getCodigo(), new Date());        
    }

    public void cancelar() {
        eliminarTemporalesCorbo();
        editarPago = false;
        pagofactura = new Pagofactura();
        detallepago = new Detallepago();
        pagofacturaPK = new PagofacturaPK();
        detallepagoPK = new DetallepagoPK();
        pagoFacturaBean = new PagoFacturaBean();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        }
        factura = new Factura();
        facturaPK = new FacturaPK();
        fecha = new Date();
        tipoBusquedaDocumento = "1";
        banco = new Banco();
        listaBancos = new ArrayList<>();
        listaFacturaSeleccionada = new ArrayList<>();
        listaFacturaUnida = new ArrayList<>();
        listaobjFactura = new ArrayList();
        listaPagofacturaArchivoSubida = new ArrayList<>();
        listaPagofactura = new ArrayList<>();
        gestionarCobro = false;
        consultarFactura = false;
        pagoDirecto = false;
        pantallaInicial = true;
        valoresGeneredos = false;
        cliente = new Cliente();
        tempCobros = new Temporalparacobrar();
        tempCobrosPK = new TemporalparacobrarPK();
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = this.comercializadoraServicio.obtenerComercializadorasActivas();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda();
        }
    }

    public void seleccionarComer() {
        if (comercializadora != null) {
            codigoComer = comercializadora.getCodigo();
            listaClientes = new ArrayList<>();
            listaClientes = clienteServicio.obtenerClientesPorComercializadora(codigoComer);
        }
    }

    public void eliminarTemporalesCorbo() {
        if (fechaConvertida != null) {
            if (!fechaConvertida.equals("")) {
                if (!dataUser.getUser().getNombrever().equals("")) {
                    if (!codigoComer.equals("")) {
                        temporalServicios.eliminarRegistrosTemporales(fechaConvertida, dataUser.getUser().getNombrever().replace(" ", ""), codigoComer);
                    }
                }
            }
        }
    }

    public void obtenerCodigosResultados() {
        codigos = new HashMap<>();
        codigos.put("0", "Debito en cuenta exitoso");
        codigos.put("1", "Saldo insuficiente");
        codigos.put("2", "Cuenta inactiva");
        codigos.put("3", "Registro Cancelado");
        codigos.put("4", "Proceso incompleto");
        codigos.put("5", "Cuenta Cancelada o cerrada");
        codigos.put("6", "xxxxxxxxxxx");
        codigos.put("7", "Débito no autorizado");
        codigos.put("8", "Transferencia sin cupo");
        codigos.put("9", "En tránsito Transacción Interbancaria");
        codigos.put("10", "Registro eliminado");
        codigos.put("11", "Error tipo de cuenta y relación");
        codigos.put("12", "Cuenta no existe");
        codigos.put("13", "Tipo de cuenta invalida");
        codigos.put("14", "Registro reversado");
        codigos.put("15", "Proceso pendiente de ejecución");
        codigos.put("95", "Debito Parcial exitoso");
        codigos.put("99", "Error técnico o indeterminado");

    }

    private void obtenerBancos() {
        listaBancos = new ArrayList<>();
        listaBancos = bancoServicio.obtenerBanco();
    }

    public void obtenerTerminales() {
        listaTermianles = new ArrayList<>();
        listaTermianles = termServicio.obtenerTerminal();
    }

    public void seleccionarBanco() {
        if (banco != null) {
        }
    }

    public void seleccionarTerminal() {
        if (terminal != null) {
            codTerminal = terminal.getCodigo();
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

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            if (dataUser.getUser().getNiveloperacion().equals("cero")) {
                habilitarComer = true;
                //obtenerPagoFactura(listaComercializadora.get(0).getCodigo(), new Date());
            }
            if (dataUser.getUser().getNiveloperacion().equals("adco")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
                obtenerPagoFactura(comercializadora.getCodigo(), new Date());
            }
            if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
                obtenerPagoFactura(comercializadora.getCodigo(), new Date());
            }
        }
    }

    public void obtenerPagoFactura(String codigoComer, Date fechaBusqueda) {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            String fechaS = date.format(fechaBusqueda);
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.pagofactura/porComerFecha?codigocomercializadora=" + codigoComer + "&fecha=" + fechaS);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.pagofactura/porComerFecha?codigocomercializadora=" + codigoComer + "&fecha=" + fechaS);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaPagofactura = new ArrayList<>();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject precioJson = new JSONObject(respuesta);
            JSONArray retorno = precioJson.getJSONArray("retorno");
            if (!retorno.isEmpty()) {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    JSONObject pagofac = retorno.getJSONObject(indice);
                    JSONObject pagoPK = pagofac.getJSONObject("pagofacturaPK");
                    pagofacturaPK.setCodigoabastecedora(pagoPK.getString("codigoabastecedora"));
                    pagofacturaPK.setCodigocomercializadora(pagoPK.getString("codigocomercializadora"));
                    pagofacturaPK.setNumero(pagoPK.getString("numero"));
                    pagofacturaPK.setCodigobanco(pagoPK.getString("codigobanco"));
                    pagofactura.setPagofacturaPK(pagofacturaPK);
                    Long lDateIni = pagofac.getLong("fecha");
                    Date dateIni = new Date(lDateIni);
                    pagofactura.setFecha(dateIni);
                    pagofactura.setActivo(pagofac.getBoolean("activo"));
                    if (pagofactura.getActivo()) {
                        pagofactura.setActivoS("S");
                    } else {
                        pagofactura.setActivoS("N");
                    }
                    pagofactura.setValor(pagofac.getBigDecimal("valor"));
                    pagofactura.setObservacion(pagofac.getString("observacion"));
                    Long lDateReg = pagofac.getLong("fecharegistro");
                    Date dateReg = new Date(lDateReg);
                    pagofactura.setFecharegistro(dateReg);
                    pagofactura.setUsuarioactual(pagofac.getString("usuarioactual"));

                    listaPagofactura.add(pagofactura);
                    pagofactura = new Pagofactura();
                    pagofacturaPK = new PagofacturaPK();
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_INFO, "NO SE HAN ENCONTRADO REGISTROS");
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRowToggle(ToggleEvent event) {
        if (event.getVisibility() == Visibility.VISIBLE) {
            Pagofactura pagofacturaD = (Pagofactura) event.getData();
            if (pagofacturaD.getPagofacturaPK().getNumero() != null) {
                obtenerDetallePago(pagofacturaD.getPagofacturaPK().getNumero());
            }
        }
    }

    public String nombreCliente(String numero) {
        obtenerDetallePago(numero);
        if (listaDetallePago.isEmpty()) {
            return "";
        } else {
            return listaDetallePago.get(0).getFactura().getNombrecliente();
        }

    }

    public void obtenerDetallePago(String codigoPrec) {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.detallepago/porNumero?numero=" + codigoPrec);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detallepago/porNumero?numero=" + codigoPrec);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaDetallePago = new ArrayList<>();
            DateFormat date = new SimpleDateFormat("dd/MM/yyyy");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject precioJson = new JSONObject(respuesta);
            JSONArray retorno = precioJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject detPago = retorno.getJSONObject(indice);
                JSONObject detPagoPK = detPago.getJSONObject("detallepagoPK");
                JSONObject fact = detPago.getJSONObject("factura");
                detallepagoPK.setCodigoabastecedora(detPagoPK.getString("codigoabastecedora"));
                detallepagoPK.setCodigocomercializadora(detPagoPK.getString("codigocomercializadora"));
                detallepagoPK.setNumeronotapedido(detPagoPK.getString("numeronotapedido"));
                detallepagoPK.setNumero(detPagoPK.getString("numero"));
                detallepagoPK.setCodigobanco(detPagoPK.getString("codigobanco"));
                detallepagoPK.setNumerofactura(detPagoPK.getString("numerofactura"));
                detallepago.setDetallepagoPK(detallepagoPK);
                factura.setNombrecliente(fact.getString("nombrecliente"));
                Long lDateVent = fact.getLong("fechaventa");
                Date dateVent = new Date(lDateVent);
                factura.setFechaventa(date.format(dateVent));
                Long lDateVen = fact.getLong("fechavencimiento");
                Date dateVen = new Date(lDateVen);
                factura.setFechavencimiento(date.format(dateVen));
                Long lDateAcre = fact.getLong("fechaacreditacionprorrogada");
                Date dateAcre = new Date(lDateAcre);
                factura.setFechaacreditacionprorrogada(date.format(dateAcre));
                factura.setValortotal(fact.getBigDecimal("valortotal"));
                factura.setValorconrubro(fact.getBigDecimal("valorconrubro"));
                detallepago.setFactura(factura);
                detallepago.setValor(detPago.getBigDecimal("valor"));
                detallepago.setActivo(detPago.getBoolean("activo"));
                if (detallepago.getActivo()) {
                    detallepago.setActivoS("S");
                } else {
                    detallepago.setActivoS("N");
                }
                detallepago.setUsuarioactual(detPago.getString("usuarioactual"));
                listaDetallePago.add(detallepago);
                detallepago = new Detallepago();
                detallepagoPK = new DetallepagoPK();
                factura = new Factura();
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cambiarEstadoFactura(Factura fact) {
        try {
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
                //PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA ACUTALIZADO EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actualizarTipoBusqueda() {
    }

    public void generarValores() throws ParseException {
        gestionarCobro = false;
        consultarFactura = false;
        pagoDirecto = false;
        pantallaInicial = false;
        valoresGeneredos = true;

        String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
        SimpleDateFormat fech2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
        Date fechaHoy = new Date();
        fechaConvertida = fech2.format(fechaHoy) + timeStamp;

        String usuario = dataUser.getUser().getNombrever().replace(" ", "");

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String codFacSelec = params.get("formNuevo:dt-cobros_selection");
        String[] parts = codFacSelec.split(",");
        String[] campos = new String[4];
        listaFacturaSeleccionada = new ArrayList<>();
        listaTotalCobros = new ArrayList<>();
        listaFacturaUnida = new ArrayList<>();
        BigDecimal suma = new BigDecimal(0);
        for (int i = 0; i < listaFactura.size(); i++) {
            for (int cont = 0; cont < parts.length; cont++) {
                campos = parts[cont].split("-");
                if (campos[0].equals(listaFactura.get(i).getFacturaPK().getCodigoabastecedora()) && campos[1].equals(listaFactura.get(i).getFacturaPK().getCodigocomercializadora()) && campos[2].equals(listaFactura.get(i).getFacturaPK().getNumero()) && campos[3].equals(listaFactura.get(i).getFacturaPK().getNumeronotapedido())) {
                    listaFacturaSeleccionada.add(listaFactura.get(i));
                }
            }
        }
        if (!listaFacturaSeleccionada.isEmpty()) {
            for (int j = 0; j < listaFacturaSeleccionada.size(); j++) {
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaVenta = formato.parse(listaFacturaSeleccionada.get(j).getFechaventa().replace("/", "-"));
                Date fechaVencimiento = formato.parse(listaFacturaSeleccionada.get(j).getFechavencimiento().replace("/", "-"));
                Date fechaAcreditacionProrrogada = formato.parse(listaFacturaSeleccionada.get(j).getFechaacreditacionprorrogada().replace("/", "-"));
                //String dateI = sdf.format(fechaVenta);
                //String dateF = sdf.format(fechaVencimiento);

                tempCobrosPK.setFechahoraproceso(fechaConvertida);
                tempCobrosPK.setUsuarioactual(usuario);
                tempCobrosPK.setCodigocomercializadora(codigoComer);
                tempCobrosPK.setNumerofactura(listaFacturaSeleccionada.get(j).getFacturaPK().getNumero());

                tempCobros.setTemporalparacobrarPK(tempCobrosPK);
                tempCobros.setCodigobanco(listaFacturaSeleccionada.get(j).getCodigobanco());
                tempCobros.setFechavencimiento(sdf.format(fechaAcreditacionProrrogada));
                tempCobros.setFechaventa(sdf.format(fechaVenta));

                tempCobros.setValorconrubro(listaFacturaSeleccionada.get(j).getValorconrubro());
                tempCobros.setValortotal(listaFacturaSeleccionada.get(j).getValortotal());
                tempCobros.setCodigocliente(listaFacturaSeleccionada.get(j).getCodigocliente());

                temporalServicios.insertarTemporalCobros(tempCobros);

                tempCobros = new Temporalparacobrar();
                tempCobrosPK = new TemporalparacobrarPK();
            }
            listaTotalCobros = totalCobroServicio.obtenerTemporalParaCobrar(fechaConvertida, usuario, codigoComer);
            if (!listaTotalCobros.isEmpty()) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "Se han generado los valores de las facturas seleccionadas correctamente");
            } else {
                this.dialogo(FacesMessage.SEVERITY_WARN, "Se ha producido un error al momento de generar lso valores de las facturas seleccionadas");
            }
//            Collections.sort(listaFacturaSeleccionada, new CompareByProductID());
//            listaFacturaUnida = new ArrayList<>();
//            for (Iterator iterator = listaFacturaSeleccionada.iterator(); iterator.hasNext();) {
//                Factura fac = (Factura) iterator.next();
//                listaFacturaOrdenada.add(fac);
//            }
//
//            objFactura.setCodigobanco(listaFacturaOrdenada.get(0).getCodigobanco());
//            objFactura.setFechavencimiento(listaFacturaOrdenada.get(0).getFechavencimiento());
//            System.out.println("Linea0" + listaFacturaOrdenada.get(0).getCodigobanco() + "-" + listaFacturaOrdenada.get(0).getFechavencimiento() + "-" + listaFacturaOrdenada.get(0).getValortotal().toString());
//            objFactura.setValortotal(listaFacturaOrdenada.get(0).getValortotal());
//            objFactura.setNumerofacturas(1);
//            suma = listaFacturaOrdenada.get(0).getValortotal();
//            listaobjFactura.add(objFactura);
//            for (int i = 1; i < listaFacturaOrdenada.size(); i++) {
//                if (objFactura.getCodigobanco().equals(listaFacturaOrdenada.get(i).getCodigobanco())) {
//                    if (objFactura.getFechavencimiento().equals(listaFacturaOrdenada.get(i).getFechavencimiento())) {
//                        suma = suma.add(listaFacturaOrdenada.get(i).getValortotal());
//                        cant++;
//                        System.out.println("Linea" + listaFacturaOrdenada.get(i).getCodigobanco() + "-" + listaFacturaOrdenada.get(i).getFechavencimiento() + "-" + listaFacturaOrdenada.get(i).getValortotal().toString());
//                        objFactura.setValortotal(suma);
//                        objFactura.setNumerofacturas(cant);
            //listaobjFactura.add(objFactura);
//                        System.out.println(listaobjFactura.get(i).getCodigobanco());
//                        System.out.println(listaobjFactura.get(i).getFechavencimiento());
//                        System.out.println(listaobjFactura.get(i).getNumerofacturas());
//                        System.out.println(listaobjFactura.get(i).getValortotal());
//                    } else {
//                        objFactura.setValortotal(suma);
//                        objFactura.setNumerofacturas(cant);
//                        System.out.println("Vencimiento" + objFactura.getCodigobanco() + "-" + objFactura.getFechavencimiento() + "-" + objFactura.getValortotal().toString());
//                        listaobjFactura.add(objFactura);

//                    System.out.println(listaobjFactura.get(i).getCodigobanco());
//                    System.out.println(listaobjFactura.get(i).getFechavencimiento());
//                    System.out.println(listaobjFactura.get(i).getNumerofacturas());
//                    System.out.println(listaobjFactura.get(i).getValortotal());
//                        objFactura = new ObjFactura();
//                        objFactura.setCodigobanco(listaFacturaOrdenada.get(i).getCodigobanco());
//                        objFactura.setFechavencimiento(listaFacturaOrdenada.get(i).getFechavencimiento());
//                        suma = listaFacturaOrdenada.get(i).getValortotal();
//                        cant = 0;
//                    }
//
//                } else {
//                    objFactura.setValortotal(suma);
//                    objFactura.setNumerofacturas(cant);
//                    System.out.println("Banco" + objFactura.getCodigobanco() + "-" + objFactura.getFechavencimiento() + "-" + objFactura.getValortotal().toString());
//                    listaobjFactura.add(objFactura);
//                System.out.println(listaobjFactura.get(i).getCodigobanco());
//                System.out.println(listaobjFactura.get(i).getFechavencimiento());
//                System.out.println(listaobjFactura.get(i).getNumerofacturas());
//                System.out.println(listaobjFactura.get(i).getValortotal());
//                    objFactura = new ObjFactura();
//                    objFactura.setCodigobanco(listaFacturaOrdenada.get(i).getCodigobanco());
//                    objFactura.setFechavencimiento(listaFacturaOrdenada.get(i).getFechavencimiento());
//                    suma = listaFacturaOrdenada.get(i).getValortotal();
//                    cant = 0;
//                }
//            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_WARN, "Seleccione una factura");
        }
    }

    public void generarValoresPagoDirecto() {
        observ = "";
        suma = new BigDecimal(0);
        if (!listaFacturaSeleccionada.isEmpty()) {
            if (habilitarComer) {
                banco = new Banco();
            }
            for (int i = 0; i < listaFacturaSeleccionada.size(); i++) {
                suma = suma.add(listaFacturaSeleccionada.get(i).getValorconrubro());
            }
            PrimeFaces.current().executeScript("PF('ingresoDatos').show()");
        } else {
            this.dialogo(FacesMessage.SEVERITY_WARN, "Seleccione una factura");
        }
    }

    class CompareByProductID implements Comparator<Factura> {

        @Override
        public int compare(Factura p1, Factura p2) {
            if (p1.getFechavencimiento().compareTo(p2.getFechavencimiento()) > 0) {
                return 1;
            }
            if (p1.getFechavencimiento().compareTo(p2.getFechavencimiento()) < 0) {
                return -1;
            }
            // at this point all a.b,c,d are equal... so return "equal"
            return 0;
        }
    }

//    public void construirArchivos() {
//        List<TotalParaCobrar> listaTotalAux = new ArrayList<>();
//        if (!listaTotalCobros.isEmpty()) {
//            for (int i = 0; i < listaTotalCobros.size(); i++) {
//
//            }
//        }
//    }
    public void generarArchivos() throws Throwable {
        String nombreArchivoGenerado = "";
        List<Factura> listaFacturaMapa = new ArrayList<>();
        List<String> listaArchivos = new ArrayList<>();
        HashMap<String, List<Factura>> mapaFacturas = new HashMap<>();
        List<JSONObject> arregloJSON = new ArrayList<>();
        int numeroRegistros = 0;
        BigDecimal valorTotalArchivo = new BigDecimal("0");
        if (listaFacturaSeleccionada != null) {
            if (!listaFacturaSeleccionada.isEmpty()) {
                for (int i = 0; i < listaBancos.size(); i++) {
                    listaFacturaMapa = new ArrayList<>();
                    mapaFacturas = new HashMap<>();
                    for (int j = 0; j < listaFacturaSeleccionada.size(); j++) {
                        if (listaFacturaSeleccionada.get(j).getCodigobanco().equals(listaBancos.get(i).getCodigo())) {
                            //System.out.println("FT::. RECORRIENDO LISTA DE FACTURAS "+ j +" - "+listaFacturaSeleccionada.get(j).getFacturaPK().getNumero()+ " - " + listaFacturaSeleccionada.get(j).getFechaacreditacionprorrogada());
                            if (!mapaFacturas.containsKey(listaFacturaSeleccionada.get(j).getFechaacreditacionprorrogada())) {
                                //System.out.println("FT::. INICIALIZA NUEVO ARREGLO listaFacturaMapa");
                                listaFacturaMapa = new ArrayList<>();
                            }
                            listaFacturaMapa.add(listaFacturaSeleccionada.get(j));
                            mapaFacturas.put(listaFacturaSeleccionada.get(j).getFechaacreditacionprorrogada(), listaFacturaMapa);

//                            listaFacturaBancos.add(listaFacturaSeleccionada.get(j));
                            valorTotalArchivo.add(listaFacturaSeleccionada.get(j).getValorconrubro());
                            numeroRegistros++;
                        }
                    }
//                    for (Map.Entry<String, List<Factura>> entry : mapaFacturas.entrySet()) {
//                        System.out.println("FT::clave=" + entry.getKey() + ", valor=" + entry.getValue());
//                    }
//     FT               if (!listaFacturaBancos.isEmpty()) {
//                        nombreArchivoGenerado = crearArchivo(listaFacturaBancos, listaBancos.get(i).getCodigo(), numeroRegistros, valorTotalArchivo);
//                        listaArchivos.add(nombreArchivoGenerado);
//                    }

                    if (!mapaFacturas.isEmpty()) {

                        int contadorArchivos = 0;

                        for (Map.Entry<String, List<Factura>> entry : mapaFacturas.entrySet()) {
                            contadorArchivos++;
                            System.out.println("FT::- CREANDO ARCHIVO - clave=" + entry.getKey() + ", valor=" + entry.getValue());
                            nombreArchivoGenerado = crearArchivo(entry.getValue(), entry.getKey(), listaBancos.get(i).getCodigo(), numeroRegistros, valorTotalArchivo, contadorArchivos);
                            listaArchivos.add(nombreArchivoGenerado);
                        }
                    }

                }
                zip(listaArchivos);
                arregloJSON.addAll(factEnviadasXCobrar(listaFacturaSeleccionada));
                addItemsPriceAux(arregloJSON, 3);

                temporalServicios.eliminarRegistrosTemporales(fechaConvertida, dataUser.getUser().getNombrever().replace(" ", ""), codigoComer);
            } else {
                this.dialogo(FacesMessage.SEVERITY_WARN, "No existen facturas");
            }
        }
    }

    public void descargar(String nombre) throws FileNotFoundException {
        File initialFile = new File(Fichero.getCARPETAREPORTES() + nombre);
        InputStream targetStream = new FileInputStream(initialFile);
        txtStream = new DefaultStreamedContent(targetStream, "application/txt", nombre);
    }

    public void zip(List<String> listaArchivos) {
        byte[] buffer = new byte[1024];
        String nombreArchivo = "cobrosBancos.zip";
        try {
            FileOutputStream fos = new FileOutputStream(Fichero.getCARPETAREPORTES() + nombreArchivo);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (int i = 0; i < listaArchivos.size(); i++) {
                ZipEntry ze = new ZipEntry(listaArchivos.get(i));
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(Fichero.getCARPETAREPORTES() + listaArchivos.get(i));
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
            }
            zos.closeEntry();
            zos.close();
            System.out.println("Hecho");
            descargar(nombreArchivo);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String crearArchivo(List<Factura> listaFactura, String codBanco, int cantidadRegsitros, BigDecimal valorTotal) throws Throwable {
        String nombreArchivoGenerado = "";
        switch (codBanco) {
            case "36":
                nombreArchivoGenerado = crearArchivo36(listaFactura, codBanco, cantidadRegsitros, valorTotal);
                break;
            case "37":
                nombreArchivoGenerado = crearArchivo37(listaFactura, codBanco, cantidadRegsitros, valorTotal);
                break;
            default:
                throw new Throwable("Error Capturado: PagoFacturaBean.crearArchivo Banco: " + codBanco + " NO tiene configuración para creación de archivo de pagos! ");
        }
        return nombreArchivoGenerado;
    }

    public String crearArchivo(List<Factura> listaFactura, String fechaAcreditacionProrrogada, String codBanco, int cantidadRegsitros, BigDecimal valorTotal, int contadorArchivos) throws Throwable {
        String nombreArchivoGenerado = "";
        switch (codBanco) {
            case "36":
                nombreArchivoGenerado = crearArchivo36(listaFactura, fechaAcreditacionProrrogada, codBanco, cantidadRegsitros, valorTotal);
                break;
            case "37":
                nombreArchivoGenerado = crearArchivo37(listaFactura, fechaAcreditacionProrrogada, codBanco, cantidadRegsitros, valorTotal, contadorArchivos);
                break;
            default:
                throw new Throwable("Error Capturado: PagoFacturaBean.crearArchivo Banco: " + codBanco + " NO tiene configuración para creación de archivo de pagos! ");
        }
        return nombreArchivoGenerado;
    }

    /*
    
    Nombre      	Tipo        Contenido	Longitud	Pos ini	Pos fin	Descripción                                                                     Req
Tipo registro           Alfanumérico	01          2               1	2	Indica el tipo de registro.  01 Encabezado o Control.                           Obl
Identificación archivo	Alfanumérico	REC         3               3	5	Es el tipo de proceso: Recadaciones Empresariales archivo de Cobros.            Obl
Código Banco            Alfanumérico	00017       5               6	10	Especifica entidad originadora del archivo, Banco de Guayaquil 00017            Obl
Cod Empresa             Alfanumérico                5               11	15	Entidad receptora del archivo, Código empresa; lo suministra el banco.          Obl
Contenido archivo	Numérico	01          2               16	17	Indica que es un archivo de Cobros o de facturación con novedades.              Obl
Fecha generacion	Numérico	AAAAMMDD                    8	18	25	Fecha en la cual la Empresa genera el archivo                           Obl
Fecha aplicación	Numérico	AAAAMMDD                    8	26	33	Fecha en la cual se debe cargar el archivo en el sistema del banco.	Obl
Nro registros           Numérico                    8               34	41	Número total de registros tipo detalle enviados en el archivo.                  Obl
Valor Total cobros	Numérico                    15              42	56	Valor total de todos los registros de cobros. 13 enteros y 2 decimales          Obl
Espacios                Alfanumérico                68              57	124	Espacios.  Valores en "blanco".                                                 Obl

    
        
        
     */
 /*

Campo	Nombre                 Tipo             Contenido	Longitud	Pos ini	Pos fin	Descripción                                                                                                                     Req
1	Tipo registro           Alfanumérico	02                  2               1       2	Indica el tipo de registro.  02 Registro de detalle.                                                                            Obl
2	Novedad                 Alfanumérico	01                  2               3       4	Tipo de novedad que afecta al registro.  01: Ingreso.  02: Modificación                                                         Obl
3	Obligación Cliente	Alfanumérico                        15              5       19	Identificación u obligación del cliente ante la empresa.                                                                        Obl
4	Nombre                  Alfanumérico                        40              20      59	Nombre del propietario del bien o del servicio ante la empresa                                                                  Obl
5	Valor cobro             Numérico                            10              60      69	Valor total de la factura.  8 enteros, 2 decimales.                                                                             Obl
6	Fecha Maxima de pago	Numérico	AAAAMMDD            8               70      77	Fecha máxima de pago en Banco                                                                                                   Obl
7	Valor mínimo            Numérico                            10              78      87	Valor de pago mínimo.  8 enteros, 2 decimales.                                                                                  Op
8	Valor RET               Numérico                            10              88      97	Valor con Base imponible o con Vlr a retener.  8 enteros, 2 decimales. Llenar con ceros en caso de no enviar el valor.          Obl
9	Referencia      	Alfanumérico                        15              98      112	Campo opcional.  Podrá contener: código del cliente, nro de cédula, RUC, etc.                                                   Op
9	Periodo                 Numérico	AAAAMM              6               113     118	Indica el periodo de recaudación. Formado por el año y el mes.                                                                  Obl
10	Secuencia periodo	Numérico                            2               119     120	Es la secuencia de la Obligación dentro del periodo. Inicia en 01                                                               Obl
11	Espacios                Alfanumérico                        4               121     124	Espacios.  Valores en "blanco".                                                                                                 Obl


        Nombre del archivo.						
        Es: REM_AAAAMMDD_CCCCC.TXT						
        REM: indica Recaudaciones EMpresariales.						
        AAAA: Año.  MM: Mes.  DD: Día.  Fecha de carga del archivo.						
        CCCCC: Código de identificación de la empresa.  Asignado por el Banco (motivo).						

        Ejemplo						
        Archivo de facturación de la Empresa con código 980. Fecha de carga: 27 de febrero del 2012						
        Es: REM_20120227_980.TXT						
     */
    public String crearArchivo37(List<Factura> listaFactura, String fechaAcreditacionProrrogada, String codBanco, int cantidadRegsitros, BigDecimal valorTotal, int contadorArchivos) throws Throwable {
        FileWriter flwriter = null;

//        COBROSRET_AAAAAMMDD_NN_XX.TXT
//        COBROSRET_AAAAAMMDD_NN_XXX.TXT
//        Donde NN es un secuencial
        String usuario = dataUser.getUser().getNombrever().replace(" ", "");
        String fechaHora = (fechaConvertida.replace(":", "")).substring(0, 16);
        String nombreArchivo = "";
        String lineaCabecera = "";
        String separador = "\t";
        Cliente cliAux = new Cliente();
        List<Cliente> listaClientesAux = new ArrayList<>();
        String fechaAcr = fechaAcreditacionProrrogada.substring(0, 4) + fechaAcreditacionProrrogada.substring(5, 7) + fechaAcreditacionProrrogada.substring(8, 10);
        String fechaHoy = fechaHora.substring(0, 4) + fechaHora.substring(5, 7) + fechaHora.substring(8, 10);
        String contadorArchivosS = String.format("%2s", String.valueOf(contadorArchivos)).replace(' ', '0');
        try {
            nombreArchivo = "/COBROSRET_" + fechaHoy + "_" + contadorArchivosS + "_KSZ-" + fechaAcr + ".txt";

            //crea el flujo para escribir en el archivo
            //flwriter = new FileWriter("C:\\archivos\\Facturas_Banco" + codBanco + "_" + fechaHora + "_" + usuario + ".txt");
            flwriter = new FileWriter(Fichero.getCARPETAREPORTES() + nombreArchivo);
            String linea = "";
            String lineaIva = "";
            ArrayList<String> lineasIVA = new ArrayList<>();
            int contadorFacturas = 0;
            //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
            BufferedWriter bfwriter = new BufferedWriter(flwriter);
            // Escribir la linea de Cabecera
//            lineaCabecera = generarLineaCabecera(listaFactura, codBanco, cantidadRegsitros, valorTotal);
            // Escribir la linea de Cabecera

            System.out.println("FT:: grabando lineacabecera.. " + nombreArchivo);
            //bfwriter.write(lineaCabecera + "\n");
            for (Factura factura : listaFactura) {
                listaClientesAux = clienteServicio.obtenerClientesPorID(factura.getCodigocliente());
                for (int i = 0; i < listaClientesAux.size(); i++) {
                    if (listaClientesAux.get(i).getCodigo().equals(factura.getCodigocliente())) {
                        cliAux = listaClientesAux.get(i);
                        break;
                    }
                }
                contadorFacturas++;
                //1	Código Orientación	Carácter 	2
                linea = linea + "CO" + separador;
                //2	Cuenta Empresa	Numérico 	20
                linea = linea + String.format("%20s", "6193390") + separador;
                //3	Secuencial	Numérico	16
                linea = linea + String.format("%16s", String.valueOf(contadorFacturas)) + separador;
                //4	Comprobante de Cobro	Carácter	20
                linea = linea + String.format("%20s", factura.getFacturaPK().getNumero().trim()) + separador;
                //5	Contrapartida	Carácter 	20
                linea = linea + String.format("%20s", factura.getFacturaPK().getNumero().trim()) + separador;
                //6	Moneda	Carácter
                linea = linea + "USD" + separador;
                //7	Valor	Numérico	13
                //System.out.println("FT::linea "+linea);
                //System.out.println("FT::factura.getValorconrubro() "+factura.getValorconrubro());
                DecimalFormat myFormatter = new DecimalFormat("00000000000.00");
                //System.out.println("FT::myFormatter "+myFormatter.toString());
                String output = myFormatter.format(factura.getValorconrubro().doubleValue());
                String dato = output.substring(0, 11) + output.substring(12, 14);
                //System.out.println("FT::dato "+dato);
                linea = linea + dato + separador;
                //8	Forma de Cobro 	Carácter	3
                linea = linea + "CTA" + separador;
                //9	Codigo de Banco	Numérico	10
                linea = linea + String.format("%10s", "0017") + separador;

                //10	Tipo de Cuenta	Carácter	3
                linea = linea + cliAux.getTipocuentadebito() + separador;

                //11	Numero de Cuenta	Numérico 
                linea = linea + String.format("%34s", cliAux.getCuentadebito()).replace(' ', '0') + separador;

                //12	Tipo ID Cliente Beneficiario 
                linea = linea + "R" + separador;

//                13	Numero ID Cliente
                linea = linea + String.format("%14s", cliAux.getRuc()) + separador;

//                14	Nombre del Cliente  
                linea = linea + String.format("%40s", cliAux.getNombrecomercial()) + separador;

//                15	Dirección Beneficiario / Deudor	Carácter	40
                linea = linea + String.format("%40s", cliAux.getDireccion()) + separador;

//                16	CiudadBeneficiario / Deudor	Carácter	20	Ciudad del Beneficiario
                linea = linea + String.format("%20s", "") + separador;
//                17	Teléfono Beneficiario / Deudor	Carácter	20
                linea = linea + String.format("%20s", cliAux.getTelefono1()) + separador;

//                18	Localidad de pago / cobro	Carácter	20
                linea = linea + String.format("%20s", "") + separador;

//                19	Referencia 	Carácter	200	Referencia del cobro
                linea = linea + String.format("%-200s", (factura.getFacturaPK().getNumeronotapedido()) + factura.getFacturaPK().getNumero()).replace(' ', '0') + separador;

//                20	Referencia Adicional | Email 	Carácter	100
                linea = linea + String.format("%20s", cliAux.getCorreo1()) + separador;

//                21	Número Factura	Numérico	200
                linea = linea + String.format("%200s", factura.getFacturaPK().getNumero()) + separador;

                // ESCRIBIR LINEA DE DESGLOSE DE RUBROS
//                1	TipoProceso	Carácter 	2
                lineaIva = lineaIva + "DE" + separador;

//                2	Secuencial	Numérico	7
                lineaIva = lineaIva + String.format("%7s", String.valueOf(contadorFacturas)) + separador;

//                3	Tiporubro	Carácter 	20
                lineaIva = lineaIva + String.format("%20s", "IVA_BIEN") + separador;

//                4	Concepto	Carácter 	50
                lineaIva = lineaIva + String.format("%20s", "IVA_FACTURA: " + factura.getFacturaPK().getNumero()) + separador;

//                5	Valorbase	Numérico 	13
                DecimalFormat myFormatterIVA = new DecimalFormat("00000000000.00");
                //System.out.println("FT::myFormatter "+myFormatter.toString());
                String outputIVA = myFormatterIVA.format(factura.getValorsinimpuestos().doubleValue());
                String datoIVA = outputIVA.substring(0, 11) + outputIVA.substring(12, 14);
                //System.out.println("FT::dato "+dato);
                lineaIva = lineaIva + datoIVA + separador;

//                6	Porcentaje	Numérico 	13 
                BigDecimal porcentajeIVA = new BigDecimal("0");
                System.out.println("ANTES DE DIVIDIR FT::myFormatterPorcentajeIVA FAC:" + factura.getFacturaPK().getNumero() + " - IVA: " + factura.getIvatotal() + " - VALOR SIN IVA: " + factura.getValorsinimpuestos());
                porcentajeIVA = (factura.getIvatotal().divide(factura.getValorsinimpuestos(), 3, RoundingMode.CEILING)).movePointRight(2);
                System.out.println("DESPUES DE DIVIDIR FT::myFormatterPorcentajeIVA FAC:" + factura.getFacturaPK().getNumero() + " - " + porcentajeIVA);
                DecimalFormat myFormatterPorcentajeIVA = new DecimalFormat("00000000000.00");

//                String outputPorcentajeIVA = myFormatterPorcentajeIVA.format(factura.getIvatotal().divide(factura.getValorsinimpuestos()).doubleValue());
                String outputPorcentajeIVA = myFormatterPorcentajeIVA.format(porcentajeIVA);
                String datoPorcentajeIVA = outputPorcentajeIVA.substring(0, 11) + "00";// + outputPorcentajeIVA.substring(12, 14);
                //System.out.println("FT::dato "+dato);
                lineaIva = lineaIva + datoPorcentajeIVA + separador;

//                7	Valorneto	Numérico 	13 
                DecimalFormat myFormatterIVAValor = new DecimalFormat("00000000000.00");
                //System.out.println("FT::myFormatter "+myFormatter.toString());
                String outputIVAValor = myFormatterIVAValor.format(factura.getIvatotal().doubleValue());
                String datoIVAValor = outputIVAValor.substring(0, 11) + outputIVAValor.substring(12, 14);
                //System.out.println("FT::dato "+dato);
                lineaIva = lineaIva + datoIVAValor + separador;
                lineasIVA.add(lineaIva);
                lineaIva = "";
                System.out.println("FT::linea: " + linea + "aqui se acaba la linea");
                //escribe los datos en el archivo
                bfwriter.write(linea + "\n");
                linea = "";

            }

            System.out.println("FT::ESCRIBIENDO LINEAS DE DESGLOSE: ");
            for (int i = 0; i < contadorFacturas; i++) {

                //escribe los datos en el archivo
                bfwriter.write(lineasIVA.get(i) + "\n");
//                linea = "";

            }

            //cierra el buffer intermedio
            bfwriter.close();
            this.dialogo(FacesMessage.SEVERITY_INFO, "Archivo creado satisfactoriamente..");
            System.out.println("Archivo creado satisfactoriamente..");
            return nombreArchivo;
        } catch (Throwable e) {
            System.out.println("FT:: error capturado " + this.getClass() + "::" + e.getMessage());
            e.printStackTrace(System.out);
        } finally {
            if (flwriter != null) {
                try {//cierra el flujo principal
                    flwriter.close();
                } catch (IOException e) {
                    System.out.println("FT:: error capturado " + this.getClass() + "::" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return nombreArchivo;
    }

    public String crearArchivo37(List<Factura> listaFactura, String codBanco, int cantidadRegsitros, BigDecimal valorTotal) throws Throwable {
        FileWriter flwriter = null;

        String usuario = dataUser.getUser().getNombrever().replace(" ", "");
        String fechaHora = (fechaConvertida.replace(":", "")).substring(0, 16);
        String nombreArchivo = "";
        String lineaCabecera = "";
        try {
            nombreArchivo = "/BANGYQ_REM_" + fechaHora + "_" + "CCCC AQUI DEBE ESTAR CODIGOPYSSEGUNBANCO" + ".txt";

            //crea el flujo para escribir en el archivo
            //flwriter = new FileWriter("C:\\archivos\\Facturas_Banco" + codBanco + "_" + fechaHora + "_" + usuario + ".txt");
            flwriter = new FileWriter(Fichero.getCARPETAREPORTES() + nombreArchivo);
            String linea = "";
            long contadorFacturas = 0;
            //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
            BufferedWriter bfwriter = new BufferedWriter(flwriter);
            // Escribir la linea de Cabecera
            lineaCabecera = generarLineaCabecera(listaFactura, codBanco, cantidadRegsitros, valorTotal);
            // Escribir la linea de Cabecera

            System.out.println("FT:: grabando lineacabecera.. " + nombreArchivo);
            bfwriter.write(lineaCabecera + "\n");
            for (Factura factura : listaFactura) {
                contadorFacturas++;
                linea = linea + "0201";
                linea = linea + String.format("%15s", factura.getRuccliente()).replace(' ', '0');
                linea = linea + String.format("%40s", factura.getNombrecliente()).replace(' ', ' ');
                //System.out.println("FT::linea "+linea);
                //System.out.println("FT::factura.getValorconrubro() "+factura.getValorconrubro());
                DecimalFormat myFormatter = new DecimalFormat("00000000.00");
                //System.out.println("FT::myFormatter "+myFormatter.toString());
                String output = myFormatter.format(factura.getValorconrubro().doubleValue());
                String dato = output.substring(0, 8) + output.substring(9, 11);
                //System.out.println("FT::dato "+dato);
                linea = linea + dato;
                //System.out.println("FT::linea + dato"+linea);
                //fecha
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                //System.out.println("FT::factura.getFechavencimiento() "+factura.getFechavencimiento());
                String f = factura.getFechavencimiento().replaceAll("/", "");
                //System.out.println("FT:: f = sdf.format(factura.getFechavencimiento()) "+f);
                linea = linea + f;
                //System.out.println("FT::linea + f "+f);
                //Valor mínimo            
                linea = linea + dato;
                //System.out.println("FT::linea + Valor mínimo dato"+linea);
                //Valor RET            
                linea = linea + dato;
                //System.out.println("FT::linea + Valor RET dato"+linea);
                //Referencia(15)
                linea = linea + "NotaPe-" + factura.getFacturaPK().getNumeronotapedido();
                //System.out.println("FT::linea + referencia"+linea);
                //Periodo	Numérico	AAAAMM
                linea = linea + f.substring(0, 6);
                //System.out.println("FT::linea + Periodo	Numérico	AAAAMM: "+linea);
                //Secuencia periodo	Numérico		2
                DecimalFormat myFormatter1 = new DecimalFormat("00");
                String output1 = myFormatter1.format(contadorFacturas);
                //String dato1 = output1.substring(0, 8) + output.substring(9, 11);
                linea = linea + output1;
                //System.out.println("FT::linea + Secuencia periodo	Numérico		2 (linea + output1;): "+linea);
                // Espacios	Alfanumérico		4
                linea = linea + "    ";
                System.out.println("FT::linea: " + linea + "aqui se acaba la linea");
                //escribe los datos en el archivo
                bfwriter.write(linea + "\n");
                linea = "";
                contadorFacturas++;
            }
            //cierra el buffer intermedio
            bfwriter.close();
            this.dialogo(FacesMessage.SEVERITY_INFO, "Archivo creado satisfactoriamente..");
            System.out.println("Archivo creado satisfactoriamente..");
            return nombreArchivo;
        } catch (Throwable e) {
            System.out.println("FT:: error capturado " + this.getClass() + "::" + e.getMessage());
            e.printStackTrace(System.out);
        } finally {
            if (flwriter != null) {
                try {//cierra el flujo principal
                    flwriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return nombreArchivo;
    }

    public String generarLineaCabecera(List<Factura> listaFactura, String codBanco, int cantidadRegsitros, BigDecimal valorTotal) throws Throwable {
        String lineaCabecera = "";
        try {
            lineaCabecera = lineaCabecera + "01REC00017-PYS-01";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateI = sdf.format(new Date());
            lineaCabecera = lineaCabecera + dateI + dateI;
            DecimalFormat myFormatter1 = new DecimalFormat("00000000");
            String output1 = myFormatter1.format(cantidadRegsitros);
            lineaCabecera = lineaCabecera + output1;
            DecimalFormat myFormatter = new DecimalFormat("0000000000000.00");
            //System.out.println("FT::myFormatter "+myFormatter.toString());
            String output = myFormatter.format(valorTotal.doubleValue());
            String dato = output.substring(0, 13) + output.substring(14, 16);
            lineaCabecera = lineaCabecera + output;
            lineaCabecera = lineaCabecera + "00000000000000000000000000000000000000000000000000000000000000000000";
            return lineaCabecera;
        } catch (Throwable t) {
            System.out.println("FT:: error capturado " + this.getClass() + "::" + t.getMessage());
            t.printStackTrace(System.out);
            return lineaCabecera;
        }
    }

    public String crearArchivo36Lote(List<Factura> listaFactura, String codBanco, int cantidadRegsitros, BigDecimal valorTotal) throws Throwable {
        FileWriter flwriter = null;
        String usuario = dataUser.getUser().getNombrever().replace(" ", "");
        String fechaHora = (fechaConvertida.replace(":", "")).substring(0, 16);
        String nombreArchivo = "";
        String separador = "\t";
        Cliente cliAux = new Cliente();
        List<Cliente> listaClientesAux = new ArrayList<>();
        listaClientesAux = clienteServicio.obtenerClientesPorComercializadora(listaFactura.get(0).getFacturaPK().getCodigocomercializadora());
        try {
            nombreArchivo = "/BANINTER_REM_" + fechaHora + "_" + "CCCC AQUI DEBE ESTAR CODIGOPYSSEGUNBANCO" + ".txt";
            //crea el flujo para escribir en el archivo
            //flwriter = new FileWriter("C:\\archivos\\Facturas_Banco" + codBanco + "_" + fechaHora + "_" + usuario + ".txt");
            //flwriter = new FileWriter(Fichero.getCARPETAREPORTES() + "/Facturas_Banco" + codBanco + "_" + fechaHora + "_" + usuario + ".txt");
            flwriter = new FileWriter(Fichero.getCARPETAREPORTES() + nombreArchivo);
            //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
            BufferedWriter bfwriter = new BufferedWriter(flwriter);
            for (Factura factura : listaFactura) {
                for (int i = 0; i < listaClientesAux.size(); i++) {
                    if (listaClientesAux.get(i).getCodigo().equals(factura.getCodigocliente())) {
                        cliAux = listaClientesAux.get(i);
                        break;
                    }
                }
                //escribe los datos en el archivo               
                bfwriter.write(
                        //1. CÓDIGO DE ORIENTACIÓN
                        "CO" + separador
                        //2. CUENTA DE LA EMPRESA
                        + String.format("%20s", cliAux.getCuentadebito()).replace(' ', '0') + separador
                        //3. SECUENCIAL
                        + "0000000" + separador
                        //4. COMPROBANTE DE COBRO
                        + String.format("%20s", factura.getFacturaPK().getNumero().trim()).replace(' ', '0') + separador
                        //5. CONTRAPARTIDA
                        + String.format("%-20s", cliAux.getCodigo()).replace(' ', '0') + separador
                        //6. MONEDA
                        + "USD" + separador
                        //7. VALOR
                        + String.format("%14s", factura.getValorconrubro()).replace(' ', '0').replace(".", "") + separador
                        //8. FORMA DE COBRO
                        + "CTA" + separador
                        //9. CÓDIGO DE BANCO
                        + String.format("%10s", "36").replace(' ', '0') + separador
                        //10. TIPO DE CUENTA
                        + cliAux.getTipocuentadebito() + separador
                        //11. NÚMERO DE CUENTA
                        + String.format("%20s", cliAux.getCuentadebito()).replace(' ', '0') + separador
                        //12. Tipo ID Beneficiario/Deudor
                        + "R" + separador
                        //13. Número de ID Beneficiario/Deudor
                        + String.format("%15s", cliAux.getRuc()) + separador
                        //14. NOMBRE DEL DEUDOR
                        + String.format("%-41s", cliAux.getNombrecomercial()).replace(' ', '0') + separador
                        //15. DIRECCIÓN DEL DEUDOR
                        + String.format("%-38s", cliAux.getDireccion()).replace(' ', '0') + separador
                        //16. CIUDAD DEL DEUDOR
                        + String.format("%-20s", " ").replace(' ', '0') + separador
                        //17. TELÉFONO DEL DEUDOR
                        + String.format("%20s", cliAux.getTelefono1()).replace(' ', '0') + separador
                        //18. LOCALIDAD DEL COBRO
                        + String.format("%-20s", " ").replace(' ', '0') + separador
                        //19. REFERENCIA
                        + String.format("%-1000s", (factura.getFacturaPK().getNumeronotapedido())
                                + factura.getFacturaPK().getNumero()).replace(' ', '0') + separador
                        //                        + factura.getCodigoterminal() + "|"
                        //                        + factura.getObservacion()
                        //20. Referencia Adicional |dirección email |Operadora celular número de celular
                        + String.format("%-100s", factura.getFechaacreditacionprorrogada() + "|"
                                + cliAux.getCorreo1()).replace(' ', '0') + separador
                        //21. BASE IMPONIBLE 
                        + String.format("%-13s", factura.getValorsinimpuestos()).replace(' ', '0').replace(".", "") + separador
                        //IVA BIENES                        
                        + String.format("%-13s", factura.getIvatotal()).replace(' ', '0').replace(".", "") + separador
                        //BASE IMPONIBLE SERVICIOS                        
                        + String.format("%-13s", factura.getValorconrubro()).replace(' ', '0').replace(".", "") + separador
                        //IVA SERVICIOS                        
                        + String.format("%-13s", "0").replace(' ', '0').replace(".", "") + separador
                        //ICE                        
                        + String.format("%13s", "").replace(' ', '0').replace(".", "") + separador
                        //REFERENCIA FACTURACION                       
                        + String.format("%-500s", "").replace(' ', '0').replace(".", "") + separador
                        //CAMPO OPCIONAL                        
                        + String.format("%-200s", "").replace(' ', '0').replace(".", "") + "\n"
                );
            }
            //cierra el buffer intermedio
            bfwriter.close();
            this.dialogo(FacesMessage.SEVERITY_INFO, "Archivo creado satisfactoriamente..");
            System.out.println("Archivo creado satisfactoriamente..");
            return nombreArchivo;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (flwriter != null) {
                try {//cierra el flujo principal
                    flwriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return nombreArchivo;
    }

    public String crearArchivo36(List<Factura> listaFactura, String fechaAcreditacionProrrogada, String codBanco, int cantidadRegsitros, BigDecimal valorTotal) throws Throwable {
        FileWriter flwriter = null;
        String usuario = dataUser.getUser().getNombrever().replace(" ", "");
        String fechaHora = (fechaConvertida.replace(":", "")).substring(0, 16);
        String fechaAcr = fechaAcreditacionProrrogada.substring(0, 4) + fechaAcreditacionProrrogada.substring(5, 7) + fechaAcreditacionProrrogada.substring(8, 10);
        String nombreArchivo = "";
        String separador = "\t";
        Cliente cliAux = new Cliente();
        List<Cliente> listaClientesAux = new ArrayList<>();

        try {
            nombreArchivo = "/BANINTER_G_" + fechaHora + "_" + "COBRAR-EL_" + fechaAcr + ".txt";
            //crea el flujo para escribir en el archivo
            //flwriter = new FileWriter("C:\\archivos\\Facturas_Banco" + codBanco + "_" + fechaHora + "_" + usuario + ".txt");
            //flwriter = new FileWriter(Fichero.getCARPETAREPORTES() + "/Facturas_Banco" + codBanco + "_" + fechaHora + "_" + usuario + ".txt");
            flwriter = new FileWriter(Fichero.getCARPETAREPORTES() + nombreArchivo);
            //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
            BufferedWriter bfwriter = new BufferedWriter(flwriter);
            for (Factura factura : listaFactura) {
                listaClientesAux = clienteServicio.obtenerClientesPorID(factura.getCodigocliente());
                for (int i = 0; i < listaClientesAux.size(); i++) {
                    if (listaClientesAux.get(i).getCodigo().equals(factura.getCodigocliente())) {
                        cliAux = listaClientesAux.get(i);
                        break;
                    }
                }
                //escribe los datos en el archivo               
                bfwriter.write(
                        //1. CÓDIGO DE ORIENTACIÓN
                        "CO" + separador
                        //2. CUENTA DE LA EMPRESA
                        + String.format("%20s", cliAux.getCuentadebito()).replace(' ', '0') + separador
                        //3. SECUENCIAL
                        + "0000000" + separador
                        //4. COMPROBANTE DE COBRO
                        + String.format("%20s", factura.getFacturaPK().getNumero().trim()).replace(' ', '0') + separador
                        //5. CONTRAPARTIDA
                        + String.format("%-20s", cliAux.getCodigo()).replace(' ', '0') + separador
                        //6. MONEDA
                        + "USD" + separador
                        //7. VALOR
                        + String.format("%14s", factura.getValorconrubro()).replace(' ', '0').replace(".", "") + separador
                        //8. FORMA DE COBRO
                        + "CTA" + separador
                        //9. CÓDIGO DE BANCO
                        + String.format("%10s", "36").replace(' ', '0') + separador
                        //10. TIPO DE CUENTA
                        + cliAux.getTipocuentadebito() + separador
                        //11. NÚMERO DE CUENTA
                        + String.format("%20s", cliAux.getCuentadebito()).replace(' ', '0') + separador
                        //12. Tipo ID Beneficiario/Deudor
                        + "R" + separador
                        //13. Número de ID Beneficiario/Deudor
                        + String.format("%15s", cliAux.getRuc()) + separador
                        //14. NOMBRE DEL DEUDOR
                        + String.format("%-41s", cliAux.getNombrecomercial()).replace(' ', '0') + separador
                        //15. DIRECCIÓN DEL DEUDOR
                        + String.format("%-38s", cliAux.getDireccion()).replace(' ', '0') + separador
                        //16. CIUDAD DEL DEUDOR
                        + String.format("%-20s", " ").replace(' ', '0') + separador
                        //17. TELÉFONO DEL DEUDOR
                        + String.format("%20s", cliAux.getTelefono1()).replace(' ', '0') + separador
                        //18. LOCALIDAD DEL COBRO
                        + String.format("%-20s", " ").replace(' ', '0') + separador
                        //19. REFERENCIA
                        + String.format("%-1000s", (factura.getFacturaPK().getNumeronotapedido())
                                + factura.getFacturaPK().getNumero()).replace(' ', '0') + separador
                        //                        + factura.getCodigoterminal() + "|"
                        //                        + factura.getObservacion()
                        //20. Referencia Adicional |dirección email |Operadora celular número de celular
                        + String.format("%-100s", factura.getFechaacreditacionprorrogada() + "|"
                                + cliAux.getCorreo1()).replace(' ', '0') + separador
                        //21. BASE IMPONIBLE 
                        + String.format("%-13s", factura.getValorsinimpuestos()).replace(' ', '0').replace(".", "") + separador
                        //IVA BIENES                        
                        + String.format("%-13s", factura.getIvatotal()).replace(' ', '0').replace(".", "") + separador
                        //BASE IMPONIBLE SERVICIOS                        
                        + String.format("%-13s", factura.getValorconrubro()).replace(' ', '0').replace(".", "") + separador
                        //IVA SERVICIOS                        
                        + String.format("%-13s", "0").replace(' ', '0').replace(".", "") + separador
                        //ICE                        
                        + String.format("%13s", "").replace(' ', '0').replace(".", "") + separador
                        //REFERENCIA FACTURACION                       
                        + String.format("%-500s", "").replace(' ', '0').replace(".", "") + separador
                        //CAMPO OPCIONAL                        
                        + String.format("%-200s", "").replace(' ', '0').replace(".", "") + "\n"
                );
            }
            //cierra el buffer intermedio
            bfwriter.close();
            this.dialogo(FacesMessage.SEVERITY_INFO, "Archivo creado satisfactoriamente..");
            System.out.println("Archivo creado satisfactoriamente..");
            return nombreArchivo;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (flwriter != null) {
                try {//cierra el flujo principal
                    flwriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return nombreArchivo;
    }

    public String crearArchivo36(List<Factura> listaFactura, String codBanco, int cantidadRegsitros, BigDecimal valorTotal) throws Throwable {
        FileWriter flwriter = null;
        String usuario = dataUser.getUser().getNombrever().replace(" ", "");
        String fechaHora = (fechaConvertida.replace(":", "")).substring(0, 16);
        String nombreArchivo = "";
        String separador = "\t";
        Cliente cliAux = new Cliente();
        List<Cliente> listaClientesAux = new ArrayList<>();
        listaClientesAux = clienteServicio.obtenerClientesPorComercializadora(listaFactura.get(0).getFacturaPK().getCodigocomercializadora());
        try {
            nombreArchivo = "/BANINTER_REM_" + fechaHora + "_" + "CCCC AQUI DEBE ESTAR CODIGOPYSSEGUNBANCO" + ".txt";
            //crea el flujo para escribir en el archivo
            //flwriter = new FileWriter("C:\\archivos\\Facturas_Banco" + codBanco + "_" + fechaHora + "_" + usuario + ".txt");
            //flwriter = new FileWriter(Fichero.getCARPETAREPORTES() + "/Facturas_Banco" + codBanco + "_" + fechaHora + "_" + usuario + ".txt");
            flwriter = new FileWriter(Fichero.getCARPETAREPORTES() + nombreArchivo);
            //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
            BufferedWriter bfwriter = new BufferedWriter(flwriter);
            for (Factura factura : listaFactura) {
                for (int i = 0; i < listaClientesAux.size(); i++) {
                    if (listaClientesAux.get(i).getCodigo().equals(factura.getCodigocliente())) {
                        cliAux = listaClientesAux.get(i);
                        break;
                    }
                }
                //escribe los datos en el archivo               
                bfwriter.write(
                        //1. CÓDIGO DE ORIENTACIÓN
                        "CO" + separador
                        //2. CUENTA DE LA EMPRESA
                        + String.format("%20s", cliAux.getCuentadebito()).replace(' ', '0') + separador
                        //3. SECUENCIAL
                        + "0000000" + separador
                        //4. COMPROBANTE DE COBRO
                        + String.format("%20s", factura.getFacturaPK().getNumero().trim()).replace(' ', '0') + separador
                        //5. CONTRAPARTIDA
                        + String.format("%-20s", cliAux.getCodigo()).replace(' ', '0') + separador
                        //6. MONEDA
                        + "USD" + separador
                        //7. VALOR
                        + String.format("%14s", factura.getValorconrubro()).replace(' ', '0').replace(".", "") + separador
                        //8. FORMA DE COBRO
                        + "CTA" + separador
                        //9. CÓDIGO DE BANCO
                        + String.format("%10s", "36").replace(' ', '0') + separador
                        //10. TIPO DE CUENTA
                        + String.format("%3s", cliAux.getTipocuentadebito()) + separador
                        //11. NÚMERO DE CUENTA
                        + String.format("%20s", cliAux.getCuentadebito()).replace(' ', '0') + separador
                        //12. Tipo ID Beneficiario/Deudor
                        + "R" + separador
                        //13. Número de ID Beneficiario/Deudor
                        + String.format("%15s", cliAux.getRuc()) + separador
                        //14. NOMBRE DEL DEUDOR
                        + String.format("%-41s", cliAux.getNombrecomercial()).replace(' ', '0') + separador
                        //15. DIRECCIÓN DEL DEUDOR
                        + String.format("%-38s", cliAux.getDireccion()).replace(' ', '0') + separador
                        //16. CIUDAD DEL DEUDOR
                        + String.format("%-20s", " ").replace(' ', '0') + separador
                        //17. TELÉFONO DEL DEUDOR
                        + String.format("%20s", cliAux.getTelefono1()).replace(' ', '0') + separador
                        //18. LOCALIDAD DEL COBRO
                        + String.format("%-20s", " ").replace(' ', '0') + separador
                        //19. REFERENCIA
                        + String.format("%-1000s", (factura.getFacturaPK().getNumeronotapedido())
                                + factura.getFacturaPK().getNumero()).replace(' ', '0') + separador
                        //                        + factura.getCodigoterminal() + "|"
                        //                        + factura.getObservacion()
                        //20. Referencia Adicional |dirección email |Operadora celular número de celular
                        + String.format("%-100s", factura.getFechaacreditacionprorrogada() + "|"
                                + cliAux.getCorreo1()).replace(' ', '0') + separador
                        //21. BASE IMPONIBLE 
                        + String.format("%-13s", factura.getValorsinimpuestos()).replace(' ', '0').replace(".", "") + separador
                        //IVA BIENES                        
                        + String.format("%-13s", factura.getIvatotal()).replace(' ', '0').replace(".", "") + separador
                        //BASE IMPONIBLE SERVICIOS                        
                        + String.format("%-13s", factura.getValorconrubro()).replace(' ', '0').replace(".", "") + separador
                        //IVA SERVICIOS                        
                        + String.format("%-13s", "0").replace(' ', '0').replace(".", "") + separador
                        //ICE                        
                        + String.format("%13s", "").replace(' ', '0').replace(".", "") + separador
                        //REFERENCIA FACTURACION                       
                        + String.format("%-500s", "").replace(' ', '0').replace(".", "") + separador
                        //CAMPO OPCIONAL                        
                        + String.format("%-200s", "").replace(' ', '0').replace(".", "") + "\n"
                );
            }
            //cierra el buffer intermedio
            bfwriter.close();
            this.dialogo(FacesMessage.SEVERITY_INFO, "Archivo creado satisfactoriamente..");
            System.out.println("Archivo creado satisfactoriamente..");
            return nombreArchivo;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (flwriter != null) {
                try {//cierra el flujo principal
                    flwriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return nombreArchivo;
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
//            obj.put("codigo", precio.getCodigo());
//            obj.put("nombre", precio.getNombre());
            obj.put("activo", estadoPago);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "BANCO REGISTRADO EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String handleFileUpload(FileUploadEvent event) {
        DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat date1 = new SimpleDateFormat("ddMMyyyy");
        DateFormat date2 = new SimpleDateFormat("yyyyMMdd");
        DateFormat horaFormat = new SimpleDateFormat("hhmmss");
        String ruta_temporal = Fichero.getCARPETAREPORTES();
        StringBuilder cadenaInfo = new StringBuilder();
        StringBuilder cadenaErro = new StringBuilder();
        List<Pagosbancorechazados> facturaNoCoincide = new ArrayList<>();
        listaPagosBancoRechazadoAux = new ArrayList<>();
        suma = new BigDecimal(0);
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

            listaPagosbancorechazados = new ArrayList<>();
            listaDetallePago = new ArrayList<>();
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            codigoComer = comercializadora.getCodigo();
            if (codigoComer != null || banco.getCodigo() != null) {
                observacion = params.get("recibopago:observacion");
                listaComercializadoraAux = new ArrayList<>();
                listaComercializadoraAux = comercializadoraServicio.obtenerComercializadoraId(codigoComer);
                comercializadora = listaComercializadoraAux.get(0);
                switch (banco.getCodigo()) {
                    case "36": {
                        while (scanner.hasNextLine()) {
                            // el objeto scanner lee linea a linea desde el archivo
                            String linea = scanner.nextLine();
                            //Scanner delimitar = new Scanner(linea);
                            //se usa una expresión regular
                            //que valida que antes o despues de una coma (,) exista cualquier cosa
                            //parte la cadena recibida cada vez que encuentre una coma				
                            //delimitar.useDelimiter("\\s*,\\s*");                  
                            //pagofacturaPK.setNumero(delimitar.next());                                      

                            //Id_sobre
                            detallepago.setId_sobre(linea.substring(0, 12));
                            //Id_itemd
                            detallepago.setId_item(linea.substring(12, 24));
                            //Contrapartida
                            detallepago.setContrapartida(linea.substring(24, 44));
                            //Moneda   
                            detallepago.setMoneda(linea.substring(44, 47));
                            //Valor enviado
                            BigDecimal valEnv = new BigDecimal(linea.substring(47, 60));
                            detallepago.setValorEnviado((valEnv.movePointLeft(2)).toString());
                            //Valor procesado
                            BigDecimal valPro = new BigDecimal(linea.substring(60, 73));
                            detallepago.setValorProcesado((valPro.movePointLeft(2)).toString());
                            //Forma de pago procesada
                            detallepago.setFormPago(linea.substring(73, 76));
                            //Código de banco
                            detallepago.setCodBancoProc(linea.substring(76, 80));
                            //Tipo de cuenta
                            detallepago.setTipCuenta(linea.substring(80, 83));
                            //Número de cuenta
                            detallepago.setNumCuenta(linea.substring(83, 103));
                            //Tipo ID cliente beneficiario
                            detallepago.setTipIdCliente(linea.substring(103, 104));
                            //Número de ID cliente beneficiario
                            detallepago.setNumIdCliente(linea.substring(104, 124));
                            //Nombre del beneficiario
                            detallepago.setNomBeneficiario(linea.substring(124, 164));
                            //Referencia
                            detallepago.setReferencia(linea.substring(164, 204));
                            //Fecha proceso
                            detallepago.setFechaProc(linea.substring(204, 212));
                            //Hora de proceso
                            detallepago.setHoraProc(linea.substring(212, 218));
                            //Condición de proceso
                            detallepago.setCondProc(linea.substring(218, 222));
                            //Mensaje de proceso
                            detallepago.setMensProc(linea.substring(222, 262));
                            //Número de documento
                            detallepago.setNumDocu(linea.substring(262, 274));
                            //Canal
                            detallepago.setCanal(linea.substring(274, 277));
                            //Número SRI
                            detallepago.setNumSRI(linea.substring(277, 297));

                            detallepago.setValor(new BigDecimal(detallepago.getValorProcesado()));

                            //suma = suma.add(new BigDecimal(detallepago.getValorProcesado()));
                            detallepago.setActivo(true);
                            detallepago.setUsuarioactual(dataUser.getUser().getNombrever());

                            String numFact = detallepago.getReferencia().substring(8).trim();
                            String numNoPed = detallepago.getReferencia().substring(0, 8).trim();
                            String codCliente = detallepago.getContrapartida().substring(0, 8).trim();
                            String codBanco = detallepago.getCodBancoProc().trim();

                            detallepagoPK.setCodigoabastecedora(comercializadora.getAbastecedora());
                            detallepagoPK.setCodigocomercializadora(comercializadora.getCodigo());
                            detallepagoPK.setNumeronotapedido(numNoPed);
                            detallepagoPK.setNumerofactura(numFact);
                            detallepagoPK.setCodigobanco(banco.getCodigo());
                            detallepagoPK.setNumero(detallepago.getId_sobre());
                            detallepago.setDetallepagoPK(detallepagoPK);

                            pagofacturaPK.setCodigobanco(banco.getCodigo());
                            pagofacturaPK.setCodigoabastecedora(comercializadora.getAbastecedora());
                            pagofacturaPK.setCodigocomercializadora(comercializadora.getCodigo());
                            pagofacturaPK.setNumero(detallepago.getId_sobre());
                            pagofactura.setPagofacturaPK(pagofacturaPK);
                            pagofactura.setFecha(date1.parse(detallepago.getFechaProc()));
                            pagofactura.setActivo(true);
                            pagofactura.setObservacion(observacion);
                            pagofactura.setFecharegistro(new Date());
                            pagofactura.setUsuarioactual(dataUser.getUser().getNombrever());
                            //pagofactura.setValor(suma);
                            detallepago.setPagofactura(pagofactura);

                            List<Factura> fact = facturaServicio.buscarFacturasConciliarPago(codigoComer, numFact.trim(), codCliente);
                            pagosbancorechazadosPK.setBcoCodigocliente(codCliente);
                            pagosbancorechazadosPK.setBcoNumerofactura(numFact);
                            pagosbancorechazadosPK.setBcoCodigobanco(codBanco);
                            pagosbancorechazadosPK.setFechaactual(new Date());
                            pagosbancorechazados.setPagosbancorechazadosPK(pagosbancorechazadosPK);
                            pagosbancorechazados.setBcoFechaproceso(detallepago.getFechaProc());
                            pagosbancorechazados.setBcoNombrebanco(codBanco);
                            pagosbancorechazados.setBcoNombrecliente(detallepago.getNomBeneficiario());
                            pagosbancorechazados.setBcoRuccliente(detallepago.getNumIdCliente().trim());
                            pagosbancorechazados.setBcoValorconrubro(new BigDecimal(detallepago.getValorProcesado()));
                            pagosbancorechazados.setBcoCondicionProceso(detallepago.getCondProc());
                            pagosbancorechazados.setBcoMensajeProceso(detallepago.getMensProc());
                            pagosbancorechazados.setRegistrook(false);
                            if (!fact.isEmpty()) {
                                pagosbancorechazados.setPysCodigobanco(fact.get(0).getCodigobanco());
                                pagosbancorechazados.setPysCodigocliente(fact.get(0).getCodigocliente());
                                pagosbancorechazados.setPysFechaacreditacionprorrogada(date.parse(fact.get(0).getFechaacreditacion()));
                                pagosbancorechazados.setPysNombrebanco(fact.get(0).getCodigobanco());
                                pagosbancorechazados.setPysNombrecliente(fact.get(0).getNombrecliente());
                                pagosbancorechazados.setPysNumerofactura(fact.get(0).getFacturaPK().getNumero().trim());
                                pagosbancorechazados.setPysRuccliente(fact.get(0).getRuccliente());
                                pagosbancorechazados.setPysValorconrubro(fact.get(0).getValorconrubro());
                                pagosbancorechazados.setPysNumeronotapedido(numNoPed);
                                if (pagosbancorechazados.getBcoCondicionProceso().equals("0000")) {
                                    if (pagosbancorechazados.getBcoValorconrubro().compareTo(pagosbancorechazados.getPysValorconrubro()) == 0) {
                                        pagosbancorechazados.setRegistrook(true);
                                    }
                                }
                            }

                            listaPagosbancorechazados.add(pagosbancorechazados);
                            listaPagofacturaArchivoSubida.add(pagofactura);
                            listaDetallePago.add(detallepago);
                            pagosbancorechazados = new Pagosbancorechazados();
                            pagosbancorechazadosPK = new PagosbancorechazadosPK();
                            detallepago = new Detallepago();
                            detallepagoPK = new DetallepagoPK();
                            pagofactura = new Pagofactura();
                            pagofacturaPK = new PagofacturaPK();
                        }
                        break;
                    }
                    case "37": {
                        // el objeto scanner lee linea a linea desde el archiv   
                        String linea = scanner.nextLine();
                        //Scanner delimitar = new Scanner(linea);
                        //se usa una expresión regular
                        //que valida que antes o despues de una coma (,) exista cualquier cosa
                        //parte la cadena recibida cada vez que encuentre una coma				
                        //delimitar.useDelimiter("\\s*,\\s*");                                                

                        //Constante
                        detallepago.setConstanteCabe_10(linea.substring(0, 10));
                        //Motivo Bancario
                        detallepago.setMotivo_bancario(linea.substring(10, 15));
                        //Constante
                        detallepago.setConstanteCabe_2(linea.substring(15, 17));
                        //Fecha Creación   
                        detallepago.setFechaCrea(linea.substring(17, 25));
                        //Fecha Inicio de Proceso
                        Date fecha = date2.parse(linea.substring(25, 33));
                        detallepago.setFechaProc(date1.format(fecha));
                        //Cantidad de ítems cargados
                        detallepago.setCantItemsCarga(linea.substring(33, 39));
                        //Cantidad de ítems ok
                        detallepago.setCantItemsOk(linea.substring(39, 45));
                        //Valor de ítems ok
                        detallepago.setValItemsOk(linea.substring(45, 60));
                        //Valor Total Retenido Renta Bienes
                        detallepago.setValTotRetRentBienes(linea.substring(60, 70));
                        //Valor Total Retenido Renta Servicios
                        detallepago.setValTotRetRentServicios(linea.substring(70, 80));
                        //Valor Total Retenido Iva Bienes
                        detallepago.setValTotRetIvaBienes(linea.substring(80, 90));
                        //Valor Total Retenido Iva Servicios
                        detallepago.setValTotRetIvaServicios(linea.substring(90, 100));
                        //Valor Total Retenido Iva Servicios
                        detallepago.setConstanteCabe_6(linea.substring(100, 106));
                        while (scanner.hasNextLine()) {

                            linea = scanner.nextLine();
                            //Constante
                            detallepago.setConstanteDet_2(linea.substring(0, 2));
                            //Tipo de Cuenta                           
                            detallepago.setTipCuenta(linea.substring(2, 3));
                            //Número de cuenta
                            detallepago.setNumCuenta(linea.substring(3, 37));
                            //Valor Procesado
                            BigDecimal valPro = new BigDecimal(linea.substring(37, 52));
                            detallepago.setValorProcesado((valPro.movePointLeft(2)).toString());
                            //Motivo Bancario
                            detallepago.setMotBancario(linea.substring(52, 57));
                            //Constante
                            detallepago.setConstanteDet_3(linea.substring(57, 60));
                            //Contrapartida
                            detallepago.setContrapartida(linea.substring(60, 88));
                            //Código de Proceso
                            detallepago.setCondProc(linea.substring(88, 90));
                            //Valor Retenido Renta Bienes
                            detallepago.setValRetRentBienes(linea.substring(90, 100));
                            //Valor Retenido Renta Servicios
                            detallepago.setValRetRentServicios(linea.substring(100, 110));
                            //Valor Retenido Iva Bienes
                            detallepago.setValRetIvaBienes(linea.substring(110, 120));
                            //Valor Retenido Iva Servicios
                            detallepago.setValRetIvaServicios(linea.substring(120, 130));

                            String numFact = detallepago.getContrapartida().trim();
                            String numDet = detallepago.getMotivo_bancario().trim() + detallepago.getFechaProc().trim() + horaFormat.format(new Date());

                            List<Factura> fact = facturaServicio.buscarFacturasAbasComerNum(comercializadora.getAbastecedora(), comercializadora.getCodigo(), numFact.trim());
                            String codBanco = "";
                            String numNoPed = "";
                            if (!fact.isEmpty()) {
                                codBanco = fact.get(0).getCodigobanco().trim();
                                numNoPed = fact.get(0).getFacturaPK().getNumeronotapedido();
                            }
                            detallepagoPK.setCodigoabastecedora(comercializadora.getAbastecedora());
                            detallepagoPK.setCodigocomercializadora(comercializadora.getCodigo());
                            detallepagoPK.setNumeronotapedido(numNoPed);
                            detallepagoPK.setNumero(numDet);
                            detallepagoPK.setCodigobanco(banco.getCodigo());
                            detallepagoPK.setNumerofactura(numFact);

                            detallepago.setDetallepagoPK(detallepagoPK);
                            detallepago.setValor(new BigDecimal(detallepago.getValorProcesado()));
                            detallepago.setActivo(true);
                            detallepago.setUsuarioactual(dataUser.getUser().getNombrever());

                            pagofacturaPK.setCodigobanco(banco.getCodigo());
                            pagofacturaPK.setCodigoabastecedora(comercializadora.getAbastecedora());
                            pagofacturaPK.setCodigocomercializadora(comercializadora.getCodigo());
                            pagofacturaPK.setNumero(numDet);

                            pagofactura.setPagofacturaPK(pagofacturaPK);
                            pagofactura.setFecha(date1.parse(detallepago.getFechaProc()));
                            pagofactura.setActivo(true);
                            pagofactura.setObservacion(observacion);
                            pagofactura.setFecharegistro(new Date());
                            pagofactura.setUsuarioactual(dataUser.getUser().getNombrever());
                            //pagofactura.setValor(suma);
                            detallepago.setPagofactura(pagofactura);

                            pagosbancorechazadosPK.setBcoCodigocliente("N/D");
                            pagosbancorechazadosPK.setBcoNumerofactura(numFact);
                            pagosbancorechazadosPK.setBcoCodigobanco(codBanco);
                            pagosbancorechazadosPK.setFechaactual(new Date());
                            pagosbancorechazados.setPagosbancorechazadosPK(pagosbancorechazadosPK);
                            pagosbancorechazados.setBcoFechaproceso(detallepago.getFechaProc());
                            pagosbancorechazados.setBcoNombrebanco(codBanco);
                            pagosbancorechazados.setBcoNombrecliente("N/D");
                            pagosbancorechazados.setBcoRuccliente("N/D");
                            pagosbancorechazados.setBcoValorconrubro(new BigDecimal(detallepago.getValorProcesado()));
                            pagosbancorechazados.setBcoCondicionProceso(detallepago.getCondProc());
                            int num = Integer.parseInt(detallepago.getCondProc());
                            String mns = codigos.get(Integer.toString(num));
                            pagosbancorechazados.setBcoMensajeProceso(mns);
                            pagosbancorechazados.setRegistrook(false);
                            if (!fact.isEmpty()) {
                                pagosbancorechazados.setPysCodigobanco(fact.get(0).getCodigobanco());
                                pagosbancorechazados.setPysCodigocliente(fact.get(0).getCodigocliente());
                                pagosbancorechazados.setPysFechaacreditacionprorrogada(date.parse(fact.get(0).getFechaacreditacion()));
                                pagosbancorechazados.setPysNombrebanco(fact.get(0).getCodigobanco());
                                pagosbancorechazados.setPysNombrecliente(fact.get(0).getNombrecliente());
                                pagosbancorechazados.setPysNumerofactura(fact.get(0).getFacturaPK().getNumero().trim());
                                pagosbancorechazados.setPysRuccliente(fact.get(0).getRuccliente());
                                pagosbancorechazados.setPysValorconrubro(fact.get(0).getValorconrubro());
                                pagosbancorechazados.setPysNumeronotapedido(numNoPed);
                                if (pagosbancorechazados.getBcoCondicionProceso() != null) {
                                    if (pagosbancorechazados.getBcoCondicionProceso().equals("00")) {
                                        if (pagosbancorechazados.getBcoValorconrubro().compareTo(pagosbancorechazados.getPysValorconrubro()) == 0) {
                                            pagosbancorechazados.setRegistrook(true);
                                        }
                                    }
                                }
                            }

                            listaPagosbancorechazados.add(pagosbancorechazados);
                            listaPagofacturaArchivoSubida.add(pagofactura);
                            listaDetallePago.add(detallepago);
                            pagosbancorechazados = new Pagosbancorechazados();
                            pagosbancorechazadosPK = new PagosbancorechazadosPK();
                            //detallepago = new Detallepago();
                            detallepagoPK = new DetallepagoPK();
                            pagofactura = new Pagofactura();
                            pagofacturaPK = new PagofacturaPK();
                        }
                        break;
                    }
                }
                //se cierra el ojeto scanner
                scanner.close();
                FacesContext context = FacesContext.getCurrentInstance();
                if (!listaPagosbancorechazados.isEmpty()) {
                    for (int i = 0; i < listaPagosbancorechazados.size(); i++) {
                        if (listaPagosbancorechazados.get(i).getRegistrook()) {
                            listaPagosBancoRechazadoAux.add(listaPagosbancorechazados.get(i));
                            suma = suma.add(listaPagosbancorechazados.get(i).getPysValorconrubro());
                        } else {
                            facturaNoCoincide.add(listaPagosbancorechazados.get(i));
                        }
                    }
                    if (!listaPagosbancorechazados.isEmpty()) {
                        cadenaInfo.append("Facturas recibidas para procesar: " + listaPagosbancorechazados.size()).toString();
                    }
                    if (!listaPagosBancoRechazadoAux.isEmpty()) {
                        cadenaInfo.append("\nFacturas procesadas correctamente: " + listaPagosBancoRechazadoAux.size()).toString();
                    } else {
                        cadenaInfo.append("\nFacturas procesadas correctamente: 0").toString();
                        this.dialogo(FacesMessage.SEVERITY_INFO, "Facturas pagadas correctamente: 0");
                    }
                    this.dialogo(FacesMessage.SEVERITY_INFO, cadenaInfo.toString());
                }
                return ubicacion;
            } else {
                this.dialogo(FacesMessage.SEVERITY_WARN, "Seleccione una comercializadora y un banco para poder cargar el archivo");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public void guardarPagosBancoRechazado() throws ParseException {
        listaFacturaAux = new ArrayList<>();
        List<JSONObject> arregloJSON = new ArrayList<>();
        if (!listaPagosbancorechazados.isEmpty()) {
//            for (int i = 0; i < listaPagosbancorechazados.size(); i++) {
//                if (!addPagoFacturabancoRechazado(listaPagosbancorechazados.get(i))) {
//                    this.dialogo(FacesMessage.SEVERITY_ERROR, "Error de guardado detalle pago N." + i + 1);
//                }
//            }
            arregloJSON.addAll(addPagoFacturaRechazadosArregloJSON(listaPagosbancorechazados));
            addItemsPriceAux(arregloJSON, 2);
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Error de carga, el archivo se encuentra vacio");
        }

    }

    public List<JSONObject> addPagoFacturaRechazadosArregloJSON(List<Pagosbancorechazados> listaPagosbancorechazados) {
        List<JSONObject> arrObj = new ArrayList<>();
        for (int indice = 0; indice < listaPagosbancorechazados.size(); indice++) {
            arrObj.add(addIPagoFacturaRechazadosDetail(listaPagosbancorechazados.get(indice)));
        }
        return arrObj;
    }

    public JSONObject addIPagoFacturaRechazadosDetail(Pagosbancorechazados pagosbancorechazados) {

        DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        JSONObject detalle = new JSONObject();
        JSONObject detallePK = new JSONObject();

        detallePK.put("bcoCodigobanco", pagosbancorechazados.getPagosbancorechazadosPK().getBcoCodigobanco());
        detallePK.put("bcoCodigocliente", pagosbancorechazados.getPagosbancorechazadosPK().getBcoCodigocliente());
        detallePK.put("bcoNumerofactura", pagosbancorechazados.getPagosbancorechazadosPK().getBcoNumerofactura());
        detallePK.put("fechaactual", date.format(pagosbancorechazados.getPagosbancorechazadosPK().getFechaactual()));
        detalle.put("pagosbancorechazadosPK", detallePK);

        detalle.put("bcoValorconrubro", pagosbancorechazados.getBcoValorconrubro());
        detalle.put("bcoRuccliente", pagosbancorechazados.getBcoRuccliente());
        detalle.put("bcoNombrecliente", pagosbancorechazados.getBcoNombrecliente());
        detalle.put("bcoNombrebanco", pagosbancorechazados.getBcoNombrebanco());
        detalle.put("bcoFechaproceso", pagosbancorechazados.getBcoFechaproceso());
        detalle.put("registrook", pagosbancorechazados.getRegistrook());
        if (pagosbancorechazados.getPysFechaacreditacionprorrogada() != null) {
            detalle.put("pysCodigobanco", pagosbancorechazados.getPysCodigobanco());
            detalle.put("pysCodigocliente", pagosbancorechazados.getPysCodigocliente());
            detalle.put("pysFechaacreditacionprorrogada", date.format(pagosbancorechazados.getPysFechaacreditacionprorrogada()));
            detalle.put("pysNombrebanco", pagosbancorechazados.getPysNombrebanco());
            detalle.put("pysNombrecliente", pagosbancorechazados.getPysNombrecliente());
            detalle.put("pysNumerofactura", pagosbancorechazados.getPysNumerofactura());
            detalle.put("pysRuccliente", pagosbancorechazados.getPysRuccliente());
            detalle.put("pysValorconrubro", pagosbancorechazados.getPysValorconrubro());
        } else {
            detalle.put("pysCodigobanco", " ");
            detalle.put("pysCodigocliente", " ");
            detalle.put("pysFechaacreditacionprorrogada", date.format(new Date()));
            detalle.put("pysNombrebanco", " ");
            detalle.put("pysNombrecliente", " ");
            detalle.put("pysNumerofactura", " ");
            detalle.put("pysRuccliente", " ");
            detalle.put("pysValorconrubro", -1);
        }

        return detalle;
    }

    public List<JSONObject> factEnviadasXCobrar(List<Factura> listaFacturas) {
        List<JSONObject> arrObj = new ArrayList<>();
        for (int indice = 0; indice < listaFacturas.size(); indice++) {
            arrObj.add(addFactEnviadasXCobrarDetail(listaFacturas.get(indice)));
        }
        return arrObj;
    }

    public JSONObject addFactEnviadasXCobrarDetail(Factura listaFacturas) {

        JSONObject obj = new JSONObject();

        obj.put("codigocomercializadora", listaFacturas.getFacturaPK().getCodigocomercializadora());
        obj.put("numerofactura", listaFacturas.getFacturaPK().getNumero());
        obj.put("usuarioactual", listaFacturas.getUsuarioactual());

        return obj;
    }

    public Boolean addPagoFacturabancoRechazado(Pagosbancorechazados pagosbancorechazados) {
        try {
            String respuesta;
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.pagosbancorechazados");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            objPK.put("bcoCodigobanco", pagosbancorechazados.getPagosbancorechazadosPK().getBcoCodigobanco());
            objPK.put("bcoCodigocliente", pagosbancorechazados.getPagosbancorechazadosPK().getBcoCodigocliente());
            objPK.put("bcoNumerofactura", pagosbancorechazados.getPagosbancorechazadosPK().getBcoNumerofactura());
            objPK.put("fechaactual", date.format(pagosbancorechazados.getPagosbancorechazadosPK().getFechaactual()));
            obj.put("pagosbancorechazadosPK", objPK);

            obj.put("bcoValorconrubro", pagosbancorechazados.getBcoValorconrubro());
            obj.put("bcoRuccliente", pagosbancorechazados.getBcoRuccliente());
            obj.put("bcoNombrecliente", pagosbancorechazados.getBcoNombrecliente());
            obj.put("bcoNombrebanco", pagosbancorechazados.getBcoNombrebanco());
            obj.put("bcoFechaproceso", pagosbancorechazados.getBcoFechaproceso());
            obj.put("registrook", pagosbancorechazados.getRegistrook());
            if (pagosbancorechazados.getPysFechaacreditacionprorrogada() != null) {
                obj.put("pysCodigobanco", pagosbancorechazados.getPysCodigobanco());
                obj.put("pysCodigocliente", pagosbancorechazados.getPysCodigocliente());
                obj.put("pysFechaacreditacionprorrogada", date.format(pagosbancorechazados.getPysFechaacreditacionprorrogada()));
                obj.put("pysNombrebanco", pagosbancorechazados.getPysNombrebanco());
                obj.put("pysNombrecliente", pagosbancorechazados.getPysNombrecliente());
                obj.put("pysNumerofactura", pagosbancorechazados.getPysNumerofactura());
                obj.put("pysRuccliente", pagosbancorechazados.getPysRuccliente());
                obj.put("pysValorconrubro", pagosbancorechazados.getPysValorconrubro());
            } else {
                obj.put("pysCodigobanco", " ");
                obj.put("pysCodigocliente", " ");
                obj.put("pysFechaacreditacionprorrogada", date.format(new Date()));
                obj.put("pysNombrebanco", " ");
                obj.put("pysNombrecliente", " ");
                obj.put("pysNumerofactura", " ");
                obj.put("pysRuccliente", " ");
                obj.put("pysValorconrubro", -1);
            }
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                System.out.println("Registro Pagos Banco Rechazado");
                return true;
            } else {
//                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void guardar() throws ParseException {
        int detOk = 0;
        int detError = 0;
        List<JSONObject> arregloJSON = new ArrayList<>();
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        observacion = params.get("recibopago:observacion");
        if (!observacion.isEmpty()) {
            if (!listaPagosBancoRechazadoAux.isEmpty()) {
                arregloJSON.addAll(addItemsArregloJSON(listaPagosBancoRechazadoAux));
//                if (addPagoFactura()) {
//                    for (int indice = 0; indice < listaPagosBancoRechazadoAux.size(); indice++) {
//                        if (!addDetPago(listaPagosBancoRechazadoAux.get(indice))) {
//                            detError++;
//                        } else {
//                            detOk++;
//                        }
//                    }
//                }                
//                if (detError > 0) {
//                    this.dialogo(FacesMessage.SEVERITY_ERROR, "Detalles no registrados: " + detError);
//                }
//                if (detOk > 0) {
//                    this.dialogo(FacesMessage.SEVERITY_INFO, "Detalles registrados: " + detOk);
//                }
                addItemsPriceAux(arregloJSON, 1);
                guardarPagosBancoRechazado();
                listaPagosbancorechazados = new ArrayList<>();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "Los pagos no coinciden");
                guardarPagosBancoRechazado();
                listaPagosbancorechazados = new ArrayList<>();
            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Ingrese una observación");
        }

    }

    public List<JSONObject> addItemsArregloJSON(List<Pagosbancorechazados> listaPagosBancoRechazado) {
        String respuesta;
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
        JSONObject obj = new JSONObject();
        JSONObject objPK = new JSONObject();
        JSONObject objEnvRest = new JSONObject();
        List<JSONObject> arrObj = new ArrayList<>();
        List<JSONObject> listObjEnvRest = new ArrayList<>();

        objPK.put("codigoabastecedora", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getCodigoabastecedora());
        objPK.put("codigocomercializadora", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getCodigocomercializadora());
        objPK.put("numero", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getNumero());
        objPK.put("codigobanco", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getCodigobanco());
        obj.put("pagofacturaPK", objPK);
        String fechaS = date.format(listaPagofacturaArchivoSubida.get(0).getFecha());
        obj.put("fecha", fechaS);
        obj.put("activo", listaPagofacturaArchivoSubida.get(0).getActivo());
        obj.put("valor", suma);
        obj.put("observacion", observacion);
        String fechaR = date.format(listaPagofacturaArchivoSubida.get(0).getFecharegistro());
        obj.put("fecharegistro", fechaR);
        obj.put("usuarioactual", listaPagofacturaArchivoSubida.get(0).getUsuarioactual());

        for (int indice = 0; indice < listaPagosBancoRechazado.size(); indice++) {
            arrObj.add(addItemsDetailPAux(listaPagosBancoRechazado.get(indice)));
        }

        objEnvRest.put("pago", obj);
        objEnvRest.put("detallepago", arrObj);
        listObjEnvRest.add(objEnvRest);
        obj = new JSONObject();
        objPK = new JSONObject();
        arrObj = new ArrayList<>();
        objEnvRest = new JSONObject();

        return listObjEnvRest;

    }

    public JSONObject addItemsDetailPAux(Pagosbancorechazados pagosbancorechazados) {

        JSONObject detalle = new JSONObject();
        JSONObject detallePK = new JSONObject();
        detallePK.put("codigoabastecedora", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getCodigoabastecedora());
        detallePK.put("codigocomercializadora", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getCodigocomercializadora());
        detallePK.put("numeronotapedido", pagosbancorechazados.getPysNumeronotapedido());
        detallePK.put("numero", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getNumero());
        detallePK.put("codigobanco", pagosbancorechazados.getPysCodigobanco());
        detallePK.put("numerofactura", pagosbancorechazados.getPysNumerofactura());
        detalle.put("detallepagoPK", detallePK);
        detalle.put("valor", pagosbancorechazados.getPysValorconrubro());
        detalle.put("activo", true);
        detalle.put("usuarioactual", listaPagofacturaArchivoSubida.get(0).getUsuarioactual());

        return detalle;
    }
//proceso: 1=pagos; 2=pagos rechazados

    public void addItemsPriceAux(List<JSONObject> arregloJSON, int proceso) {
        try {

            String respuesta;
            String direcc = "";

            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/agregar";
            switch (proceso) {
                case 1:
                    direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.pagofactura/agregarlote";
                    break;
                case 2:
                    direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.pagosbancorechazados/crearLote";
                    break;
                case 3:
                    direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/actualizarenviadaxcobrarlote";
                    break;
            }

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

    public Boolean addPagoFactura() {
        try {
            String respuesta;
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            objPK.put("codigoabastecedora", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getCodigoabastecedora());
            objPK.put("codigocomercializadora", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getCodigocomercializadora());
            objPK.put("numero", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getNumero());
            objPK.put("codigobanco", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getCodigobanco());
            obj.put("pagofacturaPK", objPK);
            String fechaS = date.format(listaPagofacturaArchivoSubida.get(0).getFecha());
            obj.put("fecha", fechaS);
            obj.put("activo", listaPagofacturaArchivoSubida.get(0).getActivo());
            obj.put("valor", suma);
            obj.put("observacion", observacion);
            String fechaR = date.format(listaPagofacturaArchivoSubida.get(0).getFecharegistro());
            obj.put("fecharegistro", fechaR);
            obj.put("usuarioactual", listaPagofacturaArchivoSubida.get(0).getUsuarioactual());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                //this.dialogo(FacesMessage.SEVERITY_INFO, "PAGO FACTURA REGISTRADO EXITOSAMENTE");
                return true;
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR PAGO FACTURA");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR PAGO FACTURA");
            return false;
        }
    }

    public Boolean addDetPago(Pagosbancorechazados pagosbancorechazados) {
        try {
            String respuesta;

            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.detallepago");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detallepago");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            //List<JSONObject> listObj = new ArrayList<>();
            JSONObject obj = new JSONObject();
            JSONObject objPk = new JSONObject();

            objPk.put("codigoabastecedora", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getCodigoabastecedora());
            objPk.put("codigocomercializadora", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getCodigocomercializadora());
            objPk.put("numeronotapedido", pagosbancorechazados.getPysNumeronotapedido());
            objPk.put("numero", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getNumero());
            objPk.put("codigobanco", pagosbancorechazados.getPysCodigobanco());
            objPk.put("numerofactura", pagosbancorechazados.getPysNumerofactura());
            obj.put("detallepagoPK", objPk);
            obj.put("valor", pagosbancorechazados.getPysValorconrubro());
            obj.put("activo", true);
            obj.put("usuarioactual", listaPagofacturaArchivoSubida.get(0).getUsuarioactual());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                //this.dialogo(FacesMessage.SEVERITY_INFO, "LISTA DETALLES DE PAGOS REGISTRADA EXITOSAMENTE");                
                return true;
            } else {
                //this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean addPagoFacturaGestionDirecta() {
        try {
            String respuesta;
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            DateFormat dateNum = new SimpleDateFormat("yyyyMMddhhmmss");
            String fechaS = date.format(fechaDep);
            String fechaNum = dateNum.format(new Date());
            String fechaR = date.format(new Date());
            numero = banco.getCodigo() + fechaNum;
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            objPK.put("codigoabastecedora", listaFacturaSeleccionada.get(0).getFacturaPK().getCodigoabastecedora());
            objPK.put("codigocomercializadora", listaFacturaSeleccionada.get(0).getFacturaPK().getCodigocomercializadora());
            //String fechaAct = date.format(listaFacturaSeleccionada.get(0).getFechaventa());
            objPK.put("numero", numero);
            objPK.put("codigobanco", banco.getCodigo());
            obj.put("pagofacturaPK", objPK);
            obj.put("fecha", fechaS);
            obj.put("activo", true);
            obj.put("valor", suma);
            obj.put("observacion", observ);
            obj.put("fecharegistro", fechaR);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "PAGO FACTURA REGISTRADO EXITOSAMENTE");
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

    public Boolean addDetPagoGestionDirecta(int indice) {
        try {
            String respuesta;
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.detallepago");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detallepago");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPk = new JSONObject();
            objPk.put("codigoabastecedora", listaFacturaSeleccionada.get(indice).getFacturaPK().getCodigoabastecedora());
            objPk.put("codigocomercializadora", listaFacturaSeleccionada.get(indice).getFacturaPK().getCodigocomercializadora());
            objPk.put("numeronotapedido", listaFacturaSeleccionada.get(indice).getFacturaPK().getNumeronotapedido());
            //String fechaAct = date.format(listaFacturaSeleccionada.get(0).getFechaventa());
            objPk.put("numero", numero);
            objPk.put("codigobanco", banco.getCodigo());
            objPk.put("numerofactura", listaFacturaSeleccionada.get(indice).getFacturaPK().getNumero());
            obj.put("detallepagoPK", objPk);
            obj.put("valor", listaFacturaSeleccionada.get(indice).getValorconrubro());
            obj.put("activo", true);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "LISTA DETALLES DE PAGOS REGISTRADA EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('ingresoDatos').hide()");
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
//            obj.put("codigo", precio.getCodigo());
//            obj.put("nombre", precio.getNombre());
            obj.put("activo", estadoPago);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "PAGO ACUTALIZADO EXITOSAMENTE");
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
            url = new URL(direccion + "/porId?codigoabastecedora=" + pagofactura.getPagofacturaPK().getCodigoabastecedora()
                    + "&codigocomercializadora=" + pagofactura.getPagofacturaPK().getCodigocomercializadora()
                    + "&codigobanco=" + pagofactura.getPagofacturaPK().getCodigobanco()
                    + "&numero=" + pagofactura.getPagofacturaPK().getNumero());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "PAGO ELIMINADO EXITOSAMENTE");
                obtenerPagoFactura(comercializadora.getCodigo(), fecha);
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editDetallePagoItems() throws ParseException {
        try {
            String respuesta;
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.detallepago/porId");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detallepago/porId");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPk = new JSONObject();

            objPk.put("codigoabastecedora", detallepago.getDetallepagoPK().getCodigoabastecedora());
            objPk.put("codigocomercializadora", detallepago.getDetallepagoPK().getCodigocomercializadora());
            objPk.put("numeronotapedido", detallepago.getDetallepagoPK().getNumeronotapedido());
            objPk.put("numero", detallepago.getDetallepagoPK().getNumero());
            objPk.put("codigobanco", detallepago.getDetallepagoPK().getCodigobanco());
            objPk.put("numerofactura", detallepago.getDetallepagoPK().getNumerofactura());
            obj.put("detallepagoPK", objPk);
            obj.put("valor", detallepago.getValor());
            obj.put("activo", false);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());

            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "DETALLE PAGO ACUTALIZADO EXITOSAMENTE");
                editarEstadoFactura();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editarEstadoFactura() throws ParseException {
        List<Factura> facBus = new ArrayList<>();
        facBus = facturaServicio.buscarFacturas(detallepago.getDetallepagoPK().getCodigoabastecedora(), detallepago.getDetallepagoPK().getCodigocomercializadora(), detallepago.getDetallepagoPK().getNumerofactura());
        if (!facBus.isEmpty()) {
            for (int i = 0; i < facBus.size(); i++) {
                DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                if (facBus.get(i).getFechaacreditacion() != null) {
                    Date fechaA = formato.parse(facBus.get(i).getFechadespacho().replace("/", "-"));
                    facBus.get(i).setFechaacreditacion(date.format(fechaA));
                }
                if (facBus.get(i).getFechadespacho() != null) {
                    Date fechaA = formato.parse(facBus.get(i).getFechadespacho().replace("/", "-"));
                    facBus.get(i).setFechadespacho(date.format(fechaA));
                }
                if (facBus.get(i).getFechavencimiento() != null) {
                    Date fechaA = formato.parse(facBus.get(i).getFechavencimiento().replace("/", "-"));
                    facBus.get(i).setFechavencimiento(date.format(fechaA));
                }
                if (facBus.get(i).getFechaventa() != null) {
                    Date fechaA = formato.parse(facBus.get(i).getFechaventa().replace("/", "-"));
                    facBus.get(i).setFechaventa(date.format(fechaA));
                }
                if (facBus.get(i).getFechaacreditacionprorrogada() != null) {
                    Date fechaA = formato.parse(facBus.get(i).getFechaacreditacionprorrogada().replace("/", "-"));
                    facBus.get(i).setFechaacreditacionprorrogada(date.format(fechaA));
                }
                facBus.get(i).setPagada(false);
                actualizarFactura(facBus.get(i));
            }
        }
    }

    public void actualizarFactura(Factura fact) {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/porId");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/porId");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000 * 60);
            connection.setReadTimeout(1000 * 60);
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");
            //connection.setFixedLengthStreamingMode(1000000000);

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

    public void deleteDetallePago() {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.detallepago/porId?"
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detallepago/porId?"
                    + "codigoabastecedora=" + detallepago.getDetallepagoPK().getCodigoabastecedora() + "&codigocomercializadora=" + detallepago.getDetallepagoPK().getCodigocomercializadora()
                    + "&codigobanco=" + detallepago.getDetallepagoPK().getCodigobanco() + "&numero=" + detallepago.getDetallepagoPK().getNumero()
                    + "&numeronotapedido=" + detallepago.getDetallepagoPK().getNumeronotapedido() + "&numerofactura=" + detallepago.getDetallepagoPK().getNumerofactura());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "DETALLE PAGO ELIMINADO EXITOSAMENTE");
                obtenerPagoFactura(comercializadora.getCodigo(), fecha);
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR PAGO");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoPagoFactura() {
        gestionarCobro = true;
        consultarFactura = false;
        pagoDirecto = false;
        pantallaInicial = false;
        valoresGeneredos = false;
        estadoPago = true;
        editarPago = false;
        pagofactura = new Pagofactura();
        pagosbancorechazados = new Pagosbancorechazados();
        pagosbancorechazadosPK = new PagosbancorechazadosPK();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        }
        listaFactura = new ArrayList<>();
        listaFacturaSeleccionada = new ArrayList<>();
        listaTotalCobros = new ArrayList<>();
        tipoBusquedaDocumento = "1";
        fecha = new Date();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Pagofactura editarPagoFactura(Pagofactura obj) {
        editarPago = true;
        gestionarCobro = false;
        consultarFactura = false;
        pagoDirecto = false;
        pagofactura = obj;
        if (pagofactura.getActivo()) {
            estadoPago = true;
        } else {
            estadoPago = false;
        }
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return pagofactura;
    }

    public void actualizarLista() {
        if (comercializadora != null) {
            if (comercializadora.getCodigo() != null) {
                listaPagofactura = new ArrayList<>();
                obtenerPagoFactura(comercializadora.getCodigo(), fecha);
            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "SELECCIONE LA COMERCIALIZADORA");
        }
    }

    public void actualizarListaCobros(int tipo) {
        if (comercializadora != null) {
            if (pagoFacturaBean != null) {
                SimpleDateFormat fechV = new SimpleDateFormat("yyyy/MM/dd");
                String fechaU = fechV.format(this.fecha);
                listaFactura = new ArrayList<>();
                if (tipo == 1) {
                    listaFactura = facturaServicio.obtenerFacturasPagos(codigoComer, tipoBusquedaDocumento, fechaU, true, true, false, "01");
                } else {
                    listaFactura = facturaServicio.obtenerFacturasValidafrCobros(codigoComer, tipoBusquedaDocumento, fechaU, "01");
                }
                if (listaFactura.isEmpty()) {
                    this.dialogo(FacesMessage.SEVERITY_WARN, "No existen registros en la fecha seleccionada");
                }
            }
        }
    }

    public void obtenerFacturas() throws ParseException {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            String fechaS = date.format(this.fecha);

            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura";

            url = new URL(direcc + "/paraCobrarxformapago?codigocomercializadora=" + this.codigoComer + "&tipofecha=" + tipoBusquedaDocumento + "&oeenpetro=" + true + "&activa=" + true + "&pagada=" + false + "&clienteformapago=03" + "&fecha=" + fechaS + "&codigoterminal=" + this.codTerminal + "&codigocliente=" + this.codCliente);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaFactura = new ArrayList<>();
            factura = new Factura();
            facturaPK = new FacturaPK();

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
                JSONObject fact = retorno.getJSONObject(indice);
                JSONObject factPK = fact.getJSONObject("facturaPK");

                facturaPK.setCodigoabastecedora(factPK.getString("codigoabastecedora"));
                facturaPK.setCodigocomercializadora(factPK.getString("codigocomercializadora"));
                facturaPK.setNumeronotapedido(factPK.getString("numeronotapedido"));
                facturaPK.setNumero(factPK.getString("numero"));
                factura.setFacturaPK(facturaPK);

                if (!fact.isNull("fechaacreditacionprorrogada")) {
                    Long lDatePro = fact.getLong("fechaacreditacionprorrogada");
                    Date datePro = new Date(lDatePro);
                    factura.setFechaacreditacionprorrogada(date.format(datePro));
                }
                Long lDateVen = fact.getLong("fechaventa");
                Date dateVen = new Date(lDateVen);
                factura.setFechaventa(date.format(dateVen));
                Long lDateVenc = fact.getLong("fechavencimiento");
                Date dateVenc = new Date(lDateVenc);
                factura.setFechavencimiento(date.format(dateVenc));
                Long lDateAcre = fact.getLong("fechaacreditacion");
                Date dateAcre = new Date(lDateAcre);
                factura.setFechaacreditacion(date.format(dateAcre));
                Long lDateDesp = fact.getLong("fechadespacho");
                Date dateDesp = new Date(lDateDesp);
                factura.setFechadespacho(date.format(dateDesp));
                factura.setActiva(fact.getBoolean("activa"));
                factura.setValortotal(fact.getBigDecimal("valortotal"));
                factura.setIvatotal(fact.getBigDecimal("ivatotal"));
                factura.setObservacion(fact.getString("observacion"));
                factura.setPagada(fact.getBoolean("pagada"));
                factura.setOeenpetro(fact.getBoolean("oeenpetro"));
                factura.setCodigocliente(fact.getString("codigocliente"));
                factura.setCodigoterminal(fact.getString("codigoterminal"));
                factura.setCodigobanco(fact.getString("codigobanco"));
                factura.setAdelantar(fact.getBoolean("adelantar"));
                factura.setUsuarioactual(fact.getString("usuarioactual"));
                factura.setNombrecomercializadora(fact.getString("nombrecomercializadora"));
                factura.setRuccomercializadora(fact.getString("ruccomercializadora"));
                factura.setDireccionmatrizcomercializadora(fact.getString("direccionmatrizcomercializadora"));
                factura.setNombrecliente(fact.getString("nombrecliente"));
                factura.setRuccliente(fact.getString("ruccliente"));
                factura.setValorsinimpuestos(fact.getBigDecimal("valorsinimpuestos"));
                if (!fact.isNull("correocliente")) {
                    factura.setCorreocliente(fact.getString("correocliente"));
                }
                factura.setDireccioncliente(fact.getString("direccioncliente"));
                factura.setTelefonocliente(fact.getString("telefonocliente"));
                if (!fact.isNull("numeroautorizacion")) {
                    factura.setNumeroautorizacion(fact.getString("numeroautorizacion"));
                }
                if (!fact.isNull("fechaautorizacion")) {
                    factura.setFechaautorizacion(fact.getString("fechaautorizacion"));
                }
                factura.setClienteformapago(fact.getString("clienteformapago"));
                if (!fact.isNull("clienteformapagonosri")) {
                    factura.setClienteformapagonosri(fact.getString("clienteformapagonosri"));
                }
                factura.setPlazocliente(fact.getInt("plazocliente"));
                factura.setClaveacceso(fact.getString("claveacceso"));
                if (!fact.isNull("campoadicional_campo1")) {
                    factura.setCampoadicionalCampo1(fact.getString("campoadicional_campo1"));
                }
                if (!fact.isNull("campoadicional_campo2")) {
                    factura.setCampoadicionalCampo2(fact.getString("campoadicional_campo2"));
                }
                if (!fact.isNull("campoadicional_campo3")) {
                    factura.setCampoadicionalCampo3(fact.getString("campoadicional_campo3"));
                }
                if (!fact.isNull("campoadicional_campo4")) {
                    factura.setCampoadicionalCampo4(fact.getString("campoadicional_campo4"));
                }
                if (!fact.isNull("campoadicional_campo5")) {
                    factura.setCampoadicionalCampo5(fact.getString("campoadicional_campo5"));
                }
                if (!fact.isNull("campoadicional_campo6")) {
                    factura.setCampoadicionalCampo6(fact.getString("campoadicional_campo6"));
                }
                factura.setEstado(fact.getString("estado"));
                Long error = fact.getLong("errordocumento");
                Short errorS = error.shortValue();
                factura.setErrordocumento(errorS);
                Long hospedado = fact.getLong("hospedado");
                Short hospedadoS = hospedado.shortValue();
                factura.setHospedado(hospedadoS);
                factura.setAmbientesri(fact.getString("ambientesri").charAt(0));
                if (!fact.isNull("tipoemision")) {
                    factura.setTipoemision(fact.getString("tipoemision").charAt(0));
                }
                factura.setCodigodocumento(fact.getString("codigodocumento"));
                factura.setEsagenteretencion(fact.getBoolean("esagenteretencion"));
                factura.setEscontribuyenteespacial(fact.getString("escontribuyenteespacial"));
                factura.setObligadocontabilidad(fact.getString("obligadocontabilidad"));
                factura.setTipocomprador(fact.getString("tipocomprador"));
                factura.setMoneda(fact.getString("moneda"));
                factura.setSeriesri(fact.getString("seriesri"));
                if (!fact.isNull("tipoplazocredito")) {
                    factura.setTipoplazocredito(fact.getString("tipoplazocredito"));
                }
                factura.setValorconrubro(fact.getBigDecimal("valorconrubro"));
                factura.setOeanuladaenpetro(fact.getBoolean("oeanuladaenpetro"));
                //factura.setRefacturada(fact.getBoolean("refacturada"));
                //factura.setReliquidada(fact.getBigDecimal("reliquidada"));
                listaFactura.add(factura);
                factura = new Factura();
                facturaPK = new FacturaPK();

            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recibirPagos() {
        observ = "";
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
            banco = new Banco();
            ubicacion = "";
            listaDetallePagofacturaArchivoSubida = new ArrayList<>();
        }
        PrimeFaces.current().executeScript("PF('zip').show()");
    }

    public void nuevoPagoDirecto() {
        gestionarCobro = false;
        consultarFactura = false;
        pagoDirecto = true;
        pantallaInicial = false;
        valoresGeneredos = false;
        estadoPago = true;
        editarPago = false;
        pagofactura = new Pagofactura();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        }
        listaFactura = new ArrayList<>();
        listaFacturaSeleccionada = new ArrayList<>();
        listaTotalCobros = new ArrayList<>();
        tipoBusquedaDocumento = "1";
        fecha = new Date();
        obtenerBancos();
        PrimeFaces.current().executeScript("PF('pagoDirecto').show()");
    }

    public void regresarPantallaInicial() {
        pantallaInicial = true;
        valoresGeneredos = false;
        gestionarCobro = false;
        consultarFactura = false;
        pagoDirecto = false;
        fecha = new Date();
        listaPagofactura = new ArrayList<>();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        }
    }

    public void regresarGestionCobros() {
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        }
        pantallaInicial = false;
        valoresGeneredos = false;
        gestionarCobro = true;
        consultarFactura = false;
        pagoDirecto = false;
        temporalServicios.eliminarRegistrosTemporales(fechaConvertida, dataUser.getUser().getNombrever().replace(" ", ""), codigoComer);
        listaFactura = new ArrayList<>();
        listaFacturaSeleccionada = new ArrayList<>();
        listaPagofactura = new ArrayList<>();
        listaTotalCobros = new ArrayList<>();
        tipoBusquedaDocumento = "1";
        fecha = new Date();
    }

    public void guardarPagoDirecto() throws ParseException {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        observ = params.get("ingresoDatos:obs");
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        listaFacturaAux = new ArrayList<>();
        suma = new BigDecimal(0);
        if (!listaFacturaSeleccionada.isEmpty()) {
            for (int i = 0; i < listaFacturaSeleccionada.size(); i++) {
                suma = suma.add(listaFacturaSeleccionada.get(i).getValorconrubro());
            }
            if (addPagoFacturaGestionDirecta()) {
                for (int indice = 0; indice < listaFacturaSeleccionada.size(); indice++) {
                    if (addDetPagoGestionDirecta(indice)) {
                        if (listaFacturaSeleccionada.get(indice).getFechaacreditacion() != null) {
                            Date fechaA = formato.parse(listaFacturaSeleccionada.get(indice).getFechaacreditacion().replace("/", "-"));
                            listaFacturaSeleccionada.get(indice).setFechaacreditacion(date.format(fechaA));
                        }
                        if (listaFacturaSeleccionada.get(indice).getFechadespacho() != null) {
                            Date fechaA = formato.parse(listaFacturaSeleccionada.get(indice).getFechadespacho().replace("/", "-"));
                            listaFacturaSeleccionada.get(indice).setFechadespacho(date.format(fechaA));
                        }
                        if (listaFacturaSeleccionada.get(indice).getFechavencimiento() != null) {
                            Date fechaA = formato.parse(listaFacturaSeleccionada.get(indice).getFechavencimiento().replace("/", "-"));
                            listaFacturaSeleccionada.get(indice).setFechavencimiento(date.format(fechaA));
                        }
                        if (listaFacturaSeleccionada.get(indice).getFechaventa() != null) {
                            Date fechaA = formato.parse(listaFacturaSeleccionada.get(indice).getFechaventa().replace("/", "-"));
                            listaFacturaSeleccionada.get(indice).setFechaventa(date.format(fechaA));
                        }
                        if (listaFacturaSeleccionada.get(indice).getFechaacreditacionprorrogada() != null) {
                            Date fechaA = formato.parse(listaFacturaSeleccionada.get(indice).getFechaacreditacionprorrogada().replace("/", "-"));
                            listaFacturaSeleccionada.get(indice).setFechaacreditacionprorrogada(date.format(fechaA));
                        }
                        listaFacturaSeleccionada.get(indice).setPagada(true);
                        cambiarEstadoFactura(listaFacturaSeleccionada.get(indice));
                    }
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "No se ha registrado el detalle pago");
            }
            obtenerFacturas();
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "No existen facturas");
        }
//        if (!listaDetallePagofacturaArchivoSubida.isEmpty()) {
//            cadenaInfo.append("Facturas recibidas para pagar: " + listaDetallePagofacturaArchivoSubida.size()).toString();
//            //this.dialogo(FacesMessage.SEVERITY_INFO, "Facturas recibidas para pagar: " + listaDetallePagofacturaArchivoSubida.size());
//        }
//        if (!listaFacturaAux.isEmpty()) {
//            cadenaInfo.append("\nFacturas pagadas correctamente: " + listaFacturaAux.size()).toString();
//            //this.dialogo(FacesMessage.SEVERITY_INFO, "Facturas pagadas correctamente: " + listaFacturaAux.size());
//        } else {
//            cadenaInfo.append("\nFacturas pagadas correctamente: 0").toString();
//            this.dialogo(FacesMessage.SEVERITY_INFO, "Facturas pagadas correctamente: 0");
//        }
//        if (!listaFacturaPagadasAux.isEmpty()) {
//            for (int indice = 0; indice < listaFacturaPagadasAux.size(); indice++) {
//                cadenaErro.append("Factura N." + listaFacturaPagadasAux.get(indice).getFacturaPK().getNumero() + "ya se encuentra pagada\n").toString();
//                //this.dialogo(FacesMessage.SEVERITY_ERROR, "Factura N." + listaFacturaPagadasAux.get(indice).getFacturaPK().getNumero() + "ya se encuentra pagada");
//            }
//        }
//        if (!facturaNoActiva.isEmpty()) {
//            for (int indice = 0; indice < facturaNoActiva.size(); indice++) {
//                cadenaErro.append("Factura N." + facturaNoActiva.get(indice).getFacturaPK().getNumero() + "no se encuentra activa\n").toString();
//                //this.dialogo(FacesMessage.SEVERITY_ERROR, "Factura N." + facturaNoActiva.get(indice).getFacturaPK().getNumero() + "no se encuentra activa");
//            }
//        }
//        if (!facturaNoValor.isEmpty()) {
//            for (int indice = 0; indice < facturaNoValor.size(); indice++) {
//                cadenaErro.append("Factura N." + facturaNoValor.get(indice).getFacturaPK().getNumero() + "no concuerda el valor\n").toString();
//                //this.dialogo(FacesMessage.SEVERITY_ERROR, "Factura N." + facturaNoValor.get(indice).getFacturaPK().getNumero() + "no concuerda el valor");
//            }
//        }
//        if (!facturaNoEcuentra.isEmpty()) {
//            for (int indice = 0; indice < facturaNoEcuentra.size(); indice++) {
//                cadenaErro.append("Factura N." + facturaNoEcuentra.get(indice).getDetallepagoPK().getNumerofactura() + "no se encuentra\n").toString();
//                //this.dialogo(FacesMessage.SEVERITY_ERROR, "Factura N." + facturaNoEcuentra.get(indice).getDetallepagoPK().getNumerofactura() + "no se encuentra");
//            }
//        }
//        this.dialogo(FacesMessage.SEVERITY_INFO, cadenaInfo.toString());
//        this.dialogo(FacesMessage.SEVERITY_ERROR, cadenaErro.toString());

    }

    public String formatoFecha(String cadena) {
        String fechaFormat = "";
        fechaFormat = cadena.substring(0, 2) + "/" + cadena.substring(2, 4) + "/" + cadena.substring(4);
        return fechaFormat;
    }

    public void nuevoConsultaFactura() {
        gestionarCobro = false;
        consultarFactura = true;
        pagoDirecto = false;
        pantallaInicial = false;
        valoresGeneredos = false;
        estadoPago = true;
        editarPago = false;
        pagofactura = new Pagofactura();
        pagosbancorechazados = new Pagosbancorechazados();
        pagosbancorechazadosPK = new PagosbancorechazadosPK();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        }
        listaFactura = new ArrayList<>();
        listaFacturaSeleccionada = new ArrayList<>();
        listaTotalCobros = new ArrayList<>();
        tipoBusquedaDocumento = "1";
        fecha = new Date();
    }

    public List<Detallepago> getListaDetallePagofacturaArchivoSubida() {
        return listaDetallePagofacturaArchivoSubida;
    }

    public void setListaDetallePagofacturaArchivoSubida(List<Detallepago> listaDetallePagofacturaArchivoSubida) {
        this.listaDetallePagofacturaArchivoSubida = listaDetallePagofacturaArchivoSubida;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<Pagofactura> getListaPagofacturaArchivoSubida() {
        return listaPagofacturaArchivoSubida;
    }

    public void setListaPagofacturaArchivoSubida(List<Pagofactura> listaPagofacturaArchivoSubida) {
        this.listaPagofacturaArchivoSubida = listaPagofacturaArchivoSubida;
    }

    public List<ObjFactura> getListaobjFactura() {
        return listaobjFactura;
    }

    public void setListaobjFactura(List<ObjFactura> listaobjFactura) {
        this.listaobjFactura = listaobjFactura;
    }

    public List<Factura> getListaFacturaUnida() {
        return listaFacturaUnida;
    }

    public void setListaFacturaUnida(List<Factura> listaFacturaUnida) {
        this.listaFacturaUnida = listaFacturaUnida;
    }

    public List<Factura> getListaFacturaSeleccionada() {
        return listaFacturaSeleccionada;
    }

    public void setListaFacturaSeleccionada(List<Factura> listaFacturaSeleccionada) {
        this.listaFacturaSeleccionada = listaFacturaSeleccionada;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public List<Pagofactura> getListaPagofactura() {
        return listaPagofactura;
    }

    public void setListaPagofactura(List<Pagofactura> listaPagofactura) {
        this.listaPagofactura = listaPagofactura;
    }

    public List<Detallepago> getListaDetallePago() {
        return listaDetallePago;
    }

    public void setListaDetallePago(List<Detallepago> listaDetallePago) {
        this.listaDetallePago = listaDetallePago;
    }

    public boolean isEditarPago() {
        return editarPago;
    }

    public void setEditarPago(boolean editarPago) {
        this.editarPago = editarPago;
    }

    public boolean isEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(boolean estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Pagofactura getPagofactura() {
        return pagofactura;
    }

    public void setPagofactura(Pagofactura pagofactura) {
        this.pagofactura = pagofactura;
    }

    public Detallepago getDetallepago() {
        return detallepago;
    }

    public void setDetallepago(Detallepago detallepago) {
        this.detallepago = detallepago;
    }

    public PagofacturaPK getPagofacturaPK() {
        return pagofacturaPK;
    }

    public void setPagofacturaPK(PagofacturaPK pagofacturaPK) {
        this.pagofacturaPK = pagofacturaPK;
    }

    public DetallepagoPK getDetallepagoPK() {
        return detallepagoPK;
    }

    public void setDetallepagoPK(DetallepagoPK detallepagoPK) {
        this.detallepagoPK = detallepagoPK;
    }

    public PagoFacturaBean getPagoFacturaBean() {
        return pagoFacturaBean;
    }

    public void setPagoFacturaBean(PagoFacturaBean pagoFacturaBean) {
        this.pagoFacturaBean = pagoFacturaBean;
    }

    public String getCodigoComer() {
        return codigoComer;
    }

    public void setCodigoComer(String codigoComer) {
        this.codigoComer = codigoComer;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
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

    public List<Factura> getListaFactura() {
        return listaFactura;
    }

    public void setListaFactura(List<Factura> listaFactura) {
        this.listaFactura = listaFactura;
    }

    public String getTipoBusquedaDocumento() {
        return tipoBusquedaDocumento;
    }

    public void setTipoBusquedaDocumento(String tipoBusquedaDocumento) {
        this.tipoBusquedaDocumento = tipoBusquedaDocumento;
    }

    public Date getFecha1() {
        return fecha1;
    }

    public void setFecha1(Date fecha1) {
        this.fecha1 = fecha1;
    }

    public File getFileLeer() {
        return fileLeer;
    }

    public void setFileLeer(File fileLeer) {
        this.fileLeer = fileLeer;
    }

    public boolean isGestionarCobro() {
        return gestionarCobro;
    }

    public void setGestionarCobro(boolean gestionarCobro) {
        this.gestionarCobro = gestionarCobro;
    }

    public boolean isConsultarFactura() {
        return consultarFactura;
    }

    public void setConsultarFactura(boolean consultarFactura) {
        this.consultarFactura = consultarFactura;
    }

    public boolean isPantallaInicial() {
        return pantallaInicial;
    }

    public void setPantallaInicial(boolean pantallaInicial) {
        this.pantallaInicial = pantallaInicial;
    }

    public boolean isValoresGeneredos() {
        return valoresGeneredos;
    }

    public void setValoresGeneredos(boolean valoresGeneredos) {
        this.valoresGeneredos = valoresGeneredos;
    }

    public List<TotalParaCobrar> getListaTotalCobros() {
        return listaTotalCobros;
    }

    public void setListaTotalCobros(List<TotalParaCobrar> listaTotalCobros) {
        this.listaTotalCobros = listaTotalCobros;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getObserv() {
        return observ;
    }

    public void setObserv(String observ) {
        this.observ = observ;
    }

    public StreamedContent getTxtStream() {
        return txtStream;
    }

    public void setTxtStream(StreamedContent txtStream) {
        this.txtStream = txtStream;
    }

    public boolean isPagoDirecto() {
        return pagoDirecto;
    }

    public void setPagoDirecto(boolean pagoDirecto) {
        this.pagoDirecto = pagoDirecto;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public List<Terminal> getListaTermianles() {
        return listaTermianles;
    }

    public void setListaTermianles(List<Terminal> listaTermianles) {
        this.listaTermianles = listaTermianles;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getFechaDep() {
        return fechaDep;
    }

    public void setFechaDep(Date fechaDep) {
        this.fechaDep = fechaDep;
    }

    public BigDecimal getSuma() {
        return suma;
    }

    public void setSuma(BigDecimal suma) {
        this.suma = suma;
    }

    public List<Pagosbancorechazados> getListaPagosbancorechazados() {
        return listaPagosbancorechazados;
    }

    public void setListaPagosbancorechazados(List<Pagosbancorechazados> listaPagosbancorechazados) {
        this.listaPagosbancorechazados = listaPagosbancorechazados;
    }

}
