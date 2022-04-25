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
import ec.com.infinityone.modeloWeb.Usuario;
import ec.com.infinityone.pedidosyfacturacion.servicios.FacturaServicio;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.actorcomercial.bean.ComercializadoraBean;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Temporalparacobrar;
import ec.com.infinityone.modeloWeb.TemporalparacobrarPK;
import ec.com.infinityone.modeloWeb.TotalParaCobrar;
import ec.com.infinityone.pedidosyfacturacion.servicios.TemporalCobrarServicios;
import ec.com.infinityone.pedidosyfacturacion.servicios.TotalCobrarServicios;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
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
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
import sun.awt.image.PixelConverter;

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
    Variable que almacena varios Bancos
     */
    private List<Pagofactura> listaPagofactura;

    private List<Pagofactura> listaPagofacturaArchivoSubida;

    private List<Detallepago> listaDetallePagofacturaArchivoSubida;
    /*
    Variable que almacena varios Bancos
     */
    private List<Detallepago> listaDetallePago;

    private List<ComercializadoraBean> listaComercializadora;

    private List<ComercializadoraBean> listaComercializadoraAux;

    private List<Factura> listaFactura;

    private List<Factura> listaFacturaSeleccionada;

    private List<Factura> listaFacturaUnida;

    private List<Factura> listaFacturaOrdenada;

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

    private ObjFactura objFactura;

    private ComercializadoraBean comercializadora;

    private String tipoBusquedaDocumento;

    private Boolean mostrarBusTrans;

    private PagofacturaPK pagofacturaPK;

    private DetallepagoPK detallepagoPK;

    private PagoFacturaBean pagoFacturaBean;

    private Factura factura;

    private FacturaPK facturaPK;

    private String codigoComer;
    /*
    varibale para guardar la observacion
     */
    private String observacion;

    private Date fecha;

    private Date fecha1;
    /*
    Variable Banco
     */
    private Banco banco;
    /*
    Lista Bancos
     */
    private List<Banco> listaBancos;

    private File fileLeer;

    private String ubicacion;

    private Usuario x;

    private BigDecimal suma;

    /*
    variable para mostrar pantalla gestionarcobro
     */
    private boolean gestionarCobro;
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
        //fecha = new Date();
        objFactura = new ObjFactura();
        tipoBusquedaDocumento = "A";
        mostrarBusTrans = tipoBusquedaDocumento.equals("A");
        banco = new Banco();
        listaBancos = new ArrayList<>();
        listaFacturaSeleccionada = new ArrayList<>();
        listaFacturaUnida = new ArrayList<>();
        listaobjFactura = new ArrayList();
        listaPagofacturaArchivoSubida = new ArrayList<>();
        gestionarCobro = false;
        pantallaInicial = true;
        valoresGeneredos = false;
        tempCobros = new Temporalparacobrar();
        tempCobrosPK = new TemporalparacobrarPK();
        listaTotalCobros = new ArrayList<>();
        listaFacturaAux = new ArrayList<>();
        listaFacturaPagadasAux = new ArrayList<>();
        obtenerComercializadora();
        obtenerBancos();
        observ = "";
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
        objFactura = new ObjFactura();
        tipoBusquedaDocumento = "A";
        mostrarBusTrans = tipoBusquedaDocumento.equals("A");
        banco = new Banco();
        listaBancos = new ArrayList<>();
        listaFacturaSeleccionada = new ArrayList<>();
        listaFacturaUnida = new ArrayList<>();
        listaobjFactura = new ArrayList();
        listaPagofacturaArchivoSubida = new ArrayList<>();
        gestionarCobro = false;
        pantallaInicial = true;
        valoresGeneredos = false;
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

    private void obtenerBancos() {
        listaBancos = new ArrayList<>();
        listaBancos = bancoServicio.obtenerBanco();
    }

    public void seleccionarBanco() {
        if (banco != null) {
        }
    }

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            if (dataUser.getUser().getNiveloperacion().equals("cero")) {
                habilitarComer = true;
                obtenerPagoFactura(listaComercializadora.get(0).getCodigo(), new Date());
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

    public void seleccionarComer() {
        if (comercializadora != null) {
            codigoComer = comercializadora.getCodigo();
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
                Long lDateReg = pagofac.getLong("fecharegistro");
                Date dateReg = new Date(lDateReg);
                pagofactura.setFecharegistro(dateReg);
                pagofactura.setUsuarioactual(pagofac.getString("usuarioactual"));

                listaPagofactura.add(pagofactura);
                pagofactura = new Pagofactura();
                pagofacturaPK = new PagofacturaPK();
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

    public void obtenerDetallePago(String codigoPrec) {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.detallepago/porNumero?numero=" + codigoPrec);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detallepago/porNumero?numero=" + codigoPrec);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaDetallePago = new ArrayList<>();

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
                detallepagoPK.setCodigoabastecedora(detPagoPK.getString("codigoabastecedora"));
                detallepagoPK.setCodigocomercializadora(detPagoPK.getString("codigocomercializadora"));
                detallepagoPK.setNumeronotapedido(detPagoPK.getString("numeronotapedido"));
                detallepagoPK.setNumero(detPagoPK.getString("numero"));
                detallepagoPK.setCodigobanco(detPagoPK.getString("codigobanco"));
                detallepagoPK.setNumerofactura(detPagoPK.getString("numerofactura"));
                detallepago.setDetallepagoPK(detallepagoPK);
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
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cambiarEstadoFactura(Factura fact) {
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
        mostrarBusTrans = tipoBusquedaDocumento.equals("A");
    }

    public void generarValores() throws ParseException {
        gestionarCobro = false;
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
        listaFacturaOrdenada = new ArrayList<>();
        objFactura = new ObjFactura();
        BigDecimal suma = new BigDecimal(0);
        int cant = 0;

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
                //String dateI = sdf.format(fechaVenta);
                //String dateF = sdf.format(fechaVencimiento);

                tempCobrosPK.setFechahoraproceso(fechaConvertida);
                tempCobrosPK.setUsuarioactual(usuario);
                tempCobrosPK.setCodigocomercializadora(codigoComer);
                tempCobrosPK.setNumerofactura(listaFacturaSeleccionada.get(j).getFacturaPK().getNumero());

                tempCobros.setTemporalparacobrarPK(tempCobrosPK);
                tempCobros.setCodigobanco(listaFacturaSeleccionada.get(j).getCodigobanco());
                tempCobros.setFechavencimiento(sdf.format(fechaVencimiento));
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

    public void construirArchivos() {
        List<TotalParaCobrar> listaTotalAux = new ArrayList<>();
        if (!listaTotalCobros.isEmpty()) {
            for (int i = 0; i < listaTotalCobros.size(); i++) {

            }
        }
    }

//    public void crearArchivo2(List<TotalParaCobrar> listaTotalC, String codBanco) {
//        FileWriter flwriter = null;
//        try {
//            //crea el flujo para escribir en el archivo
//            flwriter = new FileWriter("C:\\archivos\\Facturas-Banco" + codBanco + ".txt");
//            //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
//            BufferedWriter bfwriter = new BufferedWriter(flwriter);
//            for (TotalParaCobrar totalCobro : listaTotalC) {
//                //escribe los datos en el archivo
//                bfwriter.write("Banco: " + totalCobro.getBanco() + "\n"
//                        + "Numero Factura: " + factura.getFacturaPK().getNumero() + "\n"
//                        + "Fecha de Venta: " + factura.getFechaventa() + "\n"
//                        + "Fecha de Vencimiento: " + factura.getFechavencimiento() + "\n"
//                        + "Total: " + factura.getValortotal() + "\n"
//                        + "=================================" + "\n");
//            }
//            //cierra el buffer intermedio
//            bfwriter.close();
//            this.dialogo(FacesMessage.SEVERITY_INFO, "Archivo creado satisfactoriamente..");
//            System.out.println("Archivo creado satisfactoriamente..");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (flwriter != null) {
//                try {//cierra el flujo principal
//                    flwriter.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public void generarArchivos() throws Throwable {

        String usuario = dataUser.getUser().getNombrever().replace(" ", "");
        String nombreArchivoGenerado = "";
        List<Factura> listaFacturaBancos = new ArrayList<>();
        InputStream file = null;
        String rutaGuardar = Fichero.getCARPETAREPORTES();
        String fechaHora = (fechaConvertida.replace(":", "")).substring(0, 16);
        int numeroRegistros = 0;
        BigDecimal valorTotalArchivo = new BigDecimal("0");
        if (listaFacturaSeleccionada != null) {
            if (!listaFacturaSeleccionada.isEmpty()) {
                for (int i = 0; i < listaBancos.size(); i++) {
                    listaFacturaBancos = new ArrayList<>();
                    for (int j = 0; j < listaFacturaSeleccionada.size(); j++) {
                        if (listaFacturaSeleccionada.get(j).getCodigobanco().equals(listaBancos.get(i).getCodigo())) {
                            listaFacturaBancos.add(listaFacturaSeleccionada.get(j));
                            valorTotalArchivo.add(listaFacturaSeleccionada.get(j).getValorconrubro());
                            numeroRegistros++;
                        }
                    }
                    if (!listaFacturaBancos.isEmpty()) {
                        nombreArchivoGenerado = crearArchivo(listaFacturaBancos, listaBancos.get(i).getCodigo(), numeroRegistros, valorTotalArchivo);   
                    }
                }
                temporalServicios.eliminarRegistrosTemporales(fechaConvertida, dataUser.getUser().getNombrever().replace(" ", ""), codigoComer);
                for (int j = 0; j < listaFacturaSeleccionada.size(); j++) {
                    if (j == 0) {
                        
                        
                        //FT file = new FileInputStream(new File(rutaGuardar + "/Facturas_Banco" + listaFacturaSeleccionada.get(j).getCodigobanco() + "_" + fechaHora + "_" + usuario + ".txt"));
                        file = new FileInputStream(new File(nombreArchivoGenerado));
                        
                        
                        File directory = new File(rutaGuardar);
                        //File txt = File.createTempFile("Facturas_Banco" + listaFacturaSeleccionada.get(j).getCodigobanco() + "_" + fechaHora + "_" + usuario, ".txt", directory);
                        File txt = File.createTempFile("Facturas_Banco" + listaFacturaSeleccionada.get(j).getCodigobanco() + "_" + fechaHora + "_" + usuario, ".txt", directory);
                        if (copyFile(nombreArchivoGenerado,txt.getAbsolutePath())) {  // copyFile(rutaGuardar + "/Facturas_Banco" + listaFacturaSeleccionada.get(j).getCodigobanco() + "_" + fechaHora + "_" + usuario + ".txt", txt.getAbsolutePath()))
                            File initialFile = new File(txt.getAbsolutePath());
                            InputStream targetStream = new FileInputStream(initialFile);
                            txtStream = new DefaultStreamedContent(targetStream, "application/txt", "Facturas-Banco" + listaFacturaSeleccionada.get(j).getCodigobanco() + "_" + fechaHora + "_" + usuario + ".txt");
                        }

//                        file = new FileInputStream(new File("C:\\archivos\\Facturas_Banco" + listaFacturaSeleccionada.get(j).getCodigobanco() + "_" + fechaHora + "_" + usuario +".txt"));
//                        File directory = new File("C:\\archivos");
//                        File txt = File.createTempFile("Facturas_Banco" + listaFacturaSeleccionada.get(j).getCodigobanco() + "_" + fechaHora + "_" + usuario, ".txt", directory);
//                        if (copyFile("C:\\archivos\\Facturas_Banco" + listaFacturaSeleccionada.get(j).getCodigobanco() + "_" + fechaHora + "_" + usuario +".txt", txt.getAbsolutePath())) {
//                            File initialFile = new File(txt.getAbsolutePath());
//                            InputStream targetStream = new FileInputStream(initialFile);
//                            txtStream = new DefaultStreamedContent(targetStream, "application/txt", "Facturas_Banco" + listaFacturaSeleccionada.get(j).getCodigobanco() + "_" + fechaHora + "_" + usuario +".txt");
//                        }
                    } else if (j + 1 < listaFacturaSeleccionada.size()) {
                        if (listaFacturaSeleccionada.get(j).getCodigobanco().compareTo(listaFacturaSeleccionada.get(j + 1).getCodigobanco()) != 0) {
                            file = new FileInputStream(new File(rutaGuardar + "/Facturas_Banco" + listaFacturaSeleccionada.get(j).getCodigobanco() + "_" + fechaHora + "_" + usuario + ".txt"));
                            File directory = new File(rutaGuardar);
                            File txt = File.createTempFile("Facturas_Banco" + listaFacturaSeleccionada.get(j).getCodigobanco() + "_" + fechaHora + "_" + usuario, ".txt", directory);
                            if (copyFile(rutaGuardar + "/Facturas_Banco" + listaFacturaSeleccionada.get(j).getCodigobanco() + "_" + fechaHora + "_" + usuario + ".txt", txt.getAbsolutePath())) {
                                File initialFile = new File(txt.getAbsolutePath());
                                InputStream targetStream = new FileInputStream(initialFile);
                                txtStream = new DefaultStreamedContent(targetStream, "application/txt", "Facturas-Banco" + listaFacturaSeleccionada.get(j).getCodigobanco() + "_" + fechaHora + "_" + usuario + ".txt");
                            }
                        }
                    }

                }

            } else {
                this.dialogo(FacesMessage.SEVERITY_WARN, "No existen facturas");
            }
        }
    }

    public boolean copyFile(String fromFile, String toFile) {
        File origin = new File(fromFile);
        File destination = new File(toFile);
        if (origin.exists()) {
            try {
                InputStream in = new FileInputStream(origin);
                OutputStream out = new FileOutputStream(destination);
                // We use a buffer for the copy (Usamos un buffer para la copia).
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                return true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

//    public void crearArchivo(List<Factura> listaFactura, String codBanco) {
//        FileWriter flwriter = null;
//        String rutaGuardar = Fichero.getCARPETAREPORTES();        
//        InputStream file = null;
//        try {
//            //crea el flujo para escribir en el archivo
//            flwriter = new FileWriter(rutaGuardar + "Facturas-Banco" + codBanco + ".txt");
//            //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
//            BufferedWriter bfwriter = new BufferedWriter(flwriter);
//            for (Factura factura : listaFactura) {
//                //escribe los datos en el archivo
//                bfwriter.write("Banco: " + factura.getCodigobanco() + "\n"
//                        + "Numero Factura: " + factura.getFacturaPK().getNumero() + "\n"
//                        + "Fecha de Venta: " + factura.getFechaventa() + "\n"
//                        + "Fecha de Vencimiento: " + factura.getFechavencimiento() + "\n"
//                        + "Total: " + factura.getValortotal() + "\n"
//                        + "=================================" + "\n");
//            }
//            //cierra el buffer intermedio
//            bfwriter.close();
////            File txt = File.createTempFile("_", ".pdf", directory);
////            JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
////            File initialFile = new File(pdf.getAbsolutePath());
////            InputStream targetStream = new FileInputStream(initialFile);
////            //pdfStream = new DefaultStreamedContent();
////            pdfStream = new DefaultStreamedContent(targetStream, "application/pdf", nombreDocumento + ".pdf");
////            //DefaultStreamedContent.builder().contentType("application/pdf").name(nombreDocumento + ".pdf").stream(() -> new FileInputStream(targetStream)).build();
////            System.err.print(pdf.getAbsolutePath());
////            System.out.println(pdf.getAbsolutePath());
//            this.dialogo(FacesMessage.SEVERITY_INFO, "Archivo creado satisfactoriamente..");
//            System.out.println("Archivo creado satisfactoriamente..");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (flwriter != null) {
//                try {//cierra el flujo principal
//                    flwriter.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
    
        public String crearArchivo(List<Factura> listaFactura, String codBanco, int cantidadRegsitros, BigDecimal valorTotal) throws Throwable {
        
        String nombreArchivoGenerado = "";
        
        switch (codBanco){
            case "36": nombreArchivoGenerado = crearArchivo36(listaFactura,  codBanco, cantidadRegsitros, valorTotal);
                break;
            case "37": nombreArchivoGenerado = crearArchivo37(listaFactura,  codBanco, cantidadRegsitros, valorTotal);
                break;
            default: throw new Throwable ("Error Capturado: PagoFacturaBean.crearArchivo Banco: "+codBanco + " NO tiene configuración para creación de archivo de pagos! ");
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
        
        public String crearArchivo37(List<Factura> listaFactura, String codBanco, int cantidadRegsitros, BigDecimal valorTotal) throws Throwable {
        FileWriter flwriter = null;

        String usuario = dataUser.getUser().getNombrever().replace(" ", "");
        String fechaHora = (fechaConvertida.replace(":", "")).substring(0, 16);
        String nombreArchivo = "";
        String lineaCabecera = "";
        try {
            nombreArchivo = Fichero.getCARPETAREPORTES() + "/REM_" + fechaHora + "_" + "CCCC AQUI DEBE ESTAR CODIGOPYSSEGUNBANCO" + ".txt";
            
            //crea el flujo para escribir en el archivo
            //flwriter = new FileWriter("C:\\archivos\\Facturas_Banco" + codBanco + "_" + fechaHora + "_" + usuario + ".txt");
              flwriter = new FileWriter(nombreArchivo);
            String linea = "";
            long contadorFacturas = 1;
            //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
            BufferedWriter bfwriter = new BufferedWriter(flwriter);
            // Escribir la linea de Cabecera
            lineaCabecera =  generarLineaCabecera(listaFactura, codBanco, cantidadRegsitros, valorTotal);
            // Escribir la linea de Cabecera
            
            System.out.println("FT:: grabando lineacabecera.. "+nombreArchivo);
            bfwriter.write(lineaCabecera+ "\n");
            
            for (Factura factura : listaFactura) {
                
                linea = linea+"0201";
                linea = linea + String.format("%15s", factura.getRuccliente()).replace(' ','0');
                linea = linea + String.format("%40s", factura.getNombrecliente()).replace(' ',' ');  
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
                linea = linea + "NotaPe-"+factura.getFacturaPK().getNumeronotapedido();
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
                 System.out.println("FT::linea: "+linea +"aqui se acaba la linea");
                
                //escribe los datos en el archivo
                bfwriter.write(linea+ "\n");
                contadorFacturas++;
            }
            //cierra el buffer intermedio
            bfwriter.close();
            this.dialogo(FacesMessage.SEVERITY_INFO, "Archivo creado satisfactoriamente..");
            System.out.println("Archivo creado satisfactoriamente..");
            return nombreArchivo;
        } catch (Throwable e) {
            System.out.println("FT:: error capturado "+this.getClass()+"::"+e.getMessage());
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
        }catch (Throwable t){
            System.out.println("FT:: error capturado "+this.getClass()+"::"+t.getMessage());
            t.printStackTrace(System.out);
             return lineaCabecera;
        }
    }    
        
    public String crearArchivo36(List<Factura> listaFactura, String codBanco, int cantidadRegsitros, BigDecimal valorTotal) throws Throwable { 
        FileWriter flwriter = null;

        String usuario = dataUser.getUser().getNombrever().replace(" ", "");
        String fechaHora = (fechaConvertida.replace(":", "")).substring(0, 16);
        String nombreArchivo = "";
        try {
            nombreArchivo = Fichero.getCARPETAREPORTES() + "/REM_" + fechaHora + "_" + "CCCC AQUI DEBE ESTAR CODIGOPYSSEGUNBANCO" + ".txt";

            //crea el flujo para escribir en el archivo
            //flwriter = new FileWriter("C:\\archivos\\Facturas_Banco" + codBanco + "_" + fechaHora + "_" + usuario + ".txt");
            flwriter = new FileWriter(Fichero.getCARPETAREPORTES() + "/Facturas_Banco" + codBanco + "_" + fechaHora + "_" + usuario + ".txt");

            //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
            BufferedWriter bfwriter = new BufferedWriter(flwriter);
            for (Factura factura : listaFactura) {
                //escribe los datos en el archivo
                bfwriter.write("Banco: " + factura.getCodigobanco() + "\n"
                        + "Numero Factura: " + factura.getFacturaPK().getNumero() + "\n"
                        + "Fecha de Venta: " + factura.getFechaventa() + "\n"
                        + "Fecha de Vencimiento: " + factura.getFechavencimiento() + "\n"
                        + "Total: " + factura.getValortotal() + "\n"
                        + "=================================" + "\n");
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
        String ruta_temporal = Fichero.getCARPETAREPORTES();
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

            listaPagofacturaArchivoSubida = new ArrayList<>();
            listaDetallePagofacturaArchivoSubida = new ArrayList<>();
            suma = new BigDecimal(0);
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            codigoComer = comercializadora.getCodigo();
            if (codigoComer != null || banco.getCodigo() != null) {
                observacion = params.get("recibopago:observacion");
                listaComercializadoraAux = new ArrayList<>();
                listaComercializadoraAux = comercializadoraServicio.obtenerComercializadoraId(codigoComer);
                comercializadora = listaComercializadoraAux.get(0);

                while (scanner.hasNextLine()) {
                    // el objeto scanner lee linea a linea desde el archivo
                    String linea = scanner.nextLine();
                    Scanner delimitar = new Scanner(linea);
                    //se usa una expresión regular
                    //que valida que antes o despues de una coma (,) exista cualquier cosa
                    //parte la cadena recibida cada vez que encuentre una coma				
                    delimitar.useDelimiter("\\s*,\\s*");

                    pagofacturaPK.setCodigoabastecedora(comercializadora.getAbastecedora());
                    pagofacturaPK.setCodigocomercializadora(comercializadora.getCodigo());
                    pagofacturaPK.setNumero(delimitar.next());
                    pagofacturaPK.setCodigobanco(banco.getCodigo());

                    detallepagoPK.setNumerofactura(delimitar.next());

                    pagofactura.setPagofacturaPK(pagofacturaPK);
                    pagofactura.setValor(new BigDecimal(delimitar.next()));
                    Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse(delimitar.next());
                    pagofactura.setFecha(date1);
                    pagofactura.setActivo(true);
                    pagofactura.setObservacion(observacion);
                    pagofactura.setFecharegistro(new Date());
                    pagofactura.setUsuarioactual(dataUser.getUser().getNombrever());

                    detallepagoPK.setCodigoabastecedora(comercializadora.getAbastecedora());
                    detallepagoPK.setCodigocomercializadora(comercializadora.getCodigo());
                    detallepagoPK.setNumero(pagofacturaPK.getNumero());
                    detallepagoPK.setNumeronotapedido(delimitar.next());
                    detallepagoPK.setCodigobanco(banco.getCodigo());
                    detallepago.setDetallepagoPK(detallepagoPK);
                    detallepago.setValor(pagofactura.getValor());
                    suma = suma.add(detallepago.getValor());
                    detallepago.setActivo(true);
                    detallepago.setUsuarioactual(dataUser.getUser().getNombrever());

                    listaPagofacturaArchivoSubida.add(pagofactura);
                    listaDetallePagofacturaArchivoSubida.add(detallepago);
                    detallepago = new Detallepago();
                    detallepagoPK = new DetallepagoPK();
                    pagofactura = new Pagofactura();
                    pagofacturaPK = new PagofacturaPK();

                }
                //se cierra el ojeto scanner
                scanner.close();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage("recibopago", new FacesMessage(FacesMessage.SEVERITY_INFO, "CARGA CORRECTA", event.getFile().getFileName() + " cargado al sistema"));
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

    public void guardar() throws ParseException {
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        listaFacturaAux = new ArrayList<>();
        List<Factura> facturaAux = new ArrayList<>();
        List<Detallepago> facturaNoEcuentra = new ArrayList<>();
        List<Factura> facturaNoValor = new ArrayList<>();
        List<Factura> facturaNoActiva = new ArrayList<>();
        suma = new BigDecimal(0);
        StringBuilder cadenaInfo = new StringBuilder();
        StringBuilder cadenaErro = new StringBuilder();
        if (!listaPagofacturaArchivoSubida.isEmpty()) {
            for (int i = 0; i < listaDetallePagofacturaArchivoSubida.size(); i++) {
                facturaAux = facturaServicio.buscarFacturas(listaDetallePagofacturaArchivoSubida.get(i).getDetallepagoPK().getCodigoabastecedora(),
                        listaDetallePagofacturaArchivoSubida.get(i).getDetallepagoPK().getCodigocomercializadora(),
                        listaDetallePagofacturaArchivoSubida.get(i).getDetallepagoPK().getNumerofactura());
                if (!facturaAux.isEmpty()) {
                    if (facturaAux.get(0).getValortotal().equals(listaDetallePagofacturaArchivoSubida.get(i).getValor())) {
                        if (facturaAux.get(0).getActiva()) {
                            if (!facturaAux.get(0).getPagada()) {
                                listaFacturaAux.add(facturaAux.get(0));
                                suma = suma.add(listaFacturaAux.get(0).getValortotal());
                                facturaAux = new ArrayList();
                            } else {
                                listaFacturaPagadasAux.add(facturaAux.get(0));
                                facturaAux = new ArrayList();
                            }
                        } else {
                            facturaNoActiva.add(facturaAux.get(0));
                            facturaAux = new ArrayList();
                        }
                    } else {
                        facturaNoValor.add(facturaAux.get(0));
                        facturaAux = new ArrayList();
                        //this.dialogo(FacesMessage.SEVERITY_ERROR, "No concuerda el valor de la factura N." + listaDetallePagofacturaArchivoSubida.get(i).getDetallepagoPK().getNumerofactura());
                    }
                } else {
                    facturaNoEcuentra.add(listaDetallePagofacturaArchivoSubida.get(i));
                    facturaAux = new ArrayList();
                    //this.dialogo(FacesMessage.SEVERITY_ERROR, "No se encontaron faturas con el N." + listaDetallePagofacturaArchivoSubida.get(i).getDetallepagoPK().getNumerofactura());
                }
            }

            if (!listaFacturaAux.isEmpty()) {
                if (addPagoFactura()) {
                    for (int indice = 0; indice < listaFacturaAux.size(); indice++) {
                        if (addDetPago(indice)) {
                            if (listaFacturaAux.get(indice).getFechaacreditacion() != null) {
                                Date fechaA = formato.parse(listaFacturaAux.get(indice).getFechaacreditacion().replace("/", "-"));                                
                                listaFacturaAux.get(indice).setFechaacreditacion(date.format(fechaA));
                            }
                            if (listaFacturaAux.get(indice).getFechaautorizacion() != null) {
                                Date fechaA = formato.parse(listaFacturaAux.get(indice).getFechaautorizacion().replace("/", "-"));                                                                
                                listaFacturaAux.get(indice).setFechaautorizacion(date.format(fechaA));
                            }
                            if (listaFacturaAux.get(indice).getFechadespacho() != null) {
                                Date fechaA = formato.parse(listaFacturaAux.get(indice).getFechadespacho().replace("/", "-"));                                                                
                                listaFacturaAux.get(indice).setFechadespacho(date.format(fechaA));
                            }
                            if (listaFacturaAux.get(indice).getFechavencimiento() != null) {
                                Date fechaA = formato.parse(listaFacturaAux.get(indice).getFechavencimiento().replace("/", "-"));
                                listaFacturaAux.get(indice).setFechavencimiento(date.format(fechaA));
                            }
                            if (listaFacturaAux.get(indice).getFechaventa() != null) {
                                Date fechaA = formato.parse(listaFacturaAux.get(indice).getFechaventa().replace("/", "-"));
                                listaFacturaAux.get(indice).setFechaventa(date.format(fechaA));
                            }
                            listaFacturaAux.get(indice).setActiva(false);
                            cambiarEstadoFactura(listaFacturaAux.get(indice));
                        }
                    }
                }
            }
            if (!listaDetallePagofacturaArchivoSubida.isEmpty()) {
                cadenaInfo.append("Facturas recibidas para pagar: " + listaDetallePagofacturaArchivoSubida.size()).toString();
                //this.dialogo(FacesMessage.SEVERITY_INFO, "Facturas recibidas para pagar: " + listaDetallePagofacturaArchivoSubida.size());
            }
            if (!listaFacturaAux.isEmpty()) {
                cadenaInfo.append("\nFacturas pagadas correctamente: " + listaFacturaAux.size()).toString();
                //this.dialogo(FacesMessage.SEVERITY_INFO, "Facturas pagadas correctamente: " + listaFacturaAux.size());
            } else {
                cadenaInfo.append("\nFacturas pagadas correctamente: 0").toString();
                this.dialogo(FacesMessage.SEVERITY_INFO, "Facturas pagadas correctamente: 0");
            }
            if (!listaFacturaPagadasAux.isEmpty()) {
                for (int indice = 0; indice < listaFacturaPagadasAux.size(); indice++) {
                    cadenaErro.append("Factura N." + listaFacturaPagadasAux.get(indice).getFacturaPK().getNumero() + "ya se encuentra pagada\n").toString();
                    //this.dialogo(FacesMessage.SEVERITY_ERROR, "Factura N." + listaFacturaPagadasAux.get(indice).getFacturaPK().getNumero() + "ya se encuentra pagada");
                }
            }
            if (!facturaNoActiva.isEmpty()) {
                for (int indice = 0; indice < facturaNoActiva.size(); indice++) {
                    cadenaErro.append("Factura N." + facturaNoActiva.get(indice).getFacturaPK().getNumero() + "no se encuentra activa\n").toString();
                    //this.dialogo(FacesMessage.SEVERITY_ERROR, "Factura N." + facturaNoActiva.get(indice).getFacturaPK().getNumero() + "no se encuentra activa");
                }
            }
            if (!facturaNoValor.isEmpty()) {
                for (int indice = 0; indice < facturaNoValor.size(); indice++) {
                    cadenaErro.append("Factura N." + facturaNoValor.get(indice).getFacturaPK().getNumero() + "no concuerda el valor\n").toString();
                    //this.dialogo(FacesMessage.SEVERITY_ERROR, "Factura N." + facturaNoValor.get(indice).getFacturaPK().getNumero() + "no concuerda el valor");
                }
            }
            if (!facturaNoEcuentra.isEmpty()) {
                for (int indice = 0; indice < facturaNoEcuentra.size(); indice++) {
                    cadenaErro.append("Factura N." + facturaNoEcuentra.get(indice).getDetallepagoPK().getNumerofactura() + "no se encuentra\n").toString();
                    //this.dialogo(FacesMessage.SEVERITY_ERROR, "Factura N." + facturaNoEcuentra.get(indice).getDetallepagoPK().getNumerofactura() + "no se encuentra");
                }
            }
            this.dialogo(FacesMessage.SEVERITY_INFO, cadenaInfo.toString());
            this.dialogo(FacesMessage.SEVERITY_ERROR, cadenaErro.toString());
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Error de carga, el archivo se encuentra vacio");
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
            obj.put("observacion", listaPagofacturaArchivoSubida.get(0).getObservacion());
            String fechaR = date.format(listaPagofacturaArchivoSubida.get(0).getFecharegistro());
            obj.put("fecharegistro", fechaR);
            obj.put("usuarioactual", listaPagofacturaArchivoSubida.get(0).getUsuarioactual());
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

    public Boolean addDetPago(int indice) {
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
//            objPk.put("codigoabastecedora", listaDetallePagofacturaArchivoSubida.get(indice).getDetallepagoPK().getCodigoabastecedora());
//            objPk.put("codigocomercializadora", listaDetallePagofacturaArchivoSubida.get(indice).getDetallepagoPK().getCodigocomercializadora());
//            objPk.put("numeronotapedido", listaDetallePagofacturaArchivoSubida.get(indice).getDetallepagoPK().getNumeronotapedido());
//            objPk.put("numero", listaDetallePagofacturaArchivoSubida.get(indice).getDetallepagoPK().getNumero());
//            objPk.put("codigobanco", listaDetallePagofacturaArchivoSubida.get(indice).getDetallepagoPK().getCodigobanco());
//            objPk.put("numerofactura", listaDetallePagofacturaArchivoSubida.get(indice).getDetallepagoPK().getNumerofactura());
//            obj.put("detallepagoPK", objPk);
//            obj.put("valor", listaDetallePagofacturaArchivoSubida.get(indice).getValor());
//            obj.put("activo", listaDetallePagofacturaArchivoSubida.get(indice).getActivo());
//            obj.put("usuarioactual", listaDetallePagofacturaArchivoSubida.get(indice).getUsuarioactual());

            objPk.put("codigoabastecedora", listaFacturaAux.get(indice).getFacturaPK().getCodigoabastecedora());
            objPk.put("codigocomercializadora", listaFacturaAux.get(indice).getFacturaPK().getCodigocomercializadora());
            objPk.put("numeronotapedido", listaFacturaAux.get(indice).getFacturaPK().getNumeronotapedido());
            objPk.put("numero", listaDetallePagofacturaArchivoSubida.get(indice).getDetallepagoPK().getNumero());
            objPk.put("codigobanco", listaFacturaAux.get(indice).getCodigobanco());
            objPk.put("numerofactura", listaFacturaAux.get(indice).getFacturaPK().getNumero());
            obj.put("detallepagoPK", objPk);
            obj.put("valor", listaFacturaAux.get(indice).getValortotal());
            obj.put("activo", listaFacturaAux.get(indice).getActiva());
            obj.put("usuarioactual", listaPagofacturaArchivoSubida.get(0).getUsuarioactual());
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
                this.dialogo(FacesMessage.SEVERITY_INFO, "LISTA DETALLES DE PAGOS REGISTRADA EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('recibirPag').hide()");
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
            String respuesta = "";
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

    public void editDetallePagoItems() {
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
            String respuesta = "";
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
        tipoBusquedaDocumento = "";
        fecha = new Date();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Pagofactura editarPagoFactura(Pagofactura obj) {
        editarPago = true;
        gestionarCobro = false;
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
        if (comercializadora.getCodigo() != null) {
            listaPagofactura = new ArrayList<>();
            obtenerPagoFactura(comercializadora.getCodigo(), fecha);
        }
    }

    public void actualizarListaCobros() {
        if (comercializadora != null) {
            if (pagoFacturaBean != null) {
                SimpleDateFormat fechV = new SimpleDateFormat("yyyy/MM/dd");
                String fechaU = fechV.format(this.fecha);
                listaFactura = new ArrayList<>();
                listaFactura = facturaServicio.obtenerFacturas(codigoComer, tipoBusquedaDocumento, fechaU, true, true, false);
                if (listaFactura.isEmpty()) {
                    this.dialogo(FacesMessage.SEVERITY_WARN, "No existen registros en la fecha seleccionada");
                }
            }
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
        PrimeFaces.current().executeScript("PF('recibirPag').show()");
    }

    public void regresarPantallaInicial() {
        pantallaInicial = true;
        valoresGeneredos = false;
        gestionarCobro = false;
        fecha = new Date();
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
        temporalServicios.eliminarRegistrosTemporales(fechaConvertida, dataUser.getUser().getNombrever().replace(" ", ""), codigoComer);
        listaFactura = new ArrayList<>();
        listaFacturaSeleccionada = new ArrayList<>();
        listaTotalCobros = new ArrayList<>();
        tipoBusquedaDocumento = "";
        fecha = new Date();
    }

    public boolean isHabilitarComer() {
        return habilitarComer;
    }

    public void setHabilitarComer(boolean habilitarComer) {
        this.habilitarComer = habilitarComer;
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

}
