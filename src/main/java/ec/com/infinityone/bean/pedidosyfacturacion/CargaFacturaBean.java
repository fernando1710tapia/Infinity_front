/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.bean.pedidosyfacturacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Banco;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.Detallefactura;
import ec.com.infinityone.modelo.DetallefacturaPK;
import ec.com.infinityone.modelo.Detallepago;
import ec.com.infinityone.modelo.DetallepagoPK;
import ec.com.infinityone.modelo.EnvioFactura;
import ec.com.infinityone.modelo.Factura;
import ec.com.infinityone.modelo.FacturaPK;
import ec.com.infinityone.modelo.Medida;
import ec.com.infinityone.modelo.ObjetoDetallePrecioAux;
import ec.com.infinityone.modelo.Pagocheque;
import ec.com.infinityone.modelo.PagochequePK;
import ec.com.infinityone.modelo.Pagofactura;
import ec.com.infinityone.modelo.PagofacturaPK;
import ec.com.infinityone.modelo.Pagosbancorechazados;
import ec.com.infinityone.modelo.PagosbancorechazadosPK;
import ec.com.infinityone.modelo.Temporalparacobrar;
import ec.com.infinityone.modelo.TemporalparacobrarPK;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.servicio.catalogo.BancoServicio;
import ec.com.infinityone.servicio.pedidosyfacturacion.CargarFacturasServicio;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.WebApplicationException;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author David Ayala
 */
@Named
@ViewScoped
public class CargaFacturaBean extends ReusableBean implements Serializable {

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
    Variable para acceder a los servicios de Cargar Facturas
     */
    @Inject
    private CargarFacturasServicio cargarFacturaServicio;
    /*
    variable que llama al bean de comercializadora
     */
    private ComercializadoraBean comercializadora;

    private List<ComercializadoraBean> listaComercializadora;

    private String codigoComer;
    /*
    variable que llama a la entidad banco
     */
    private Banco banco;
    /*
    Lista Bancos
     */
    private List<Banco> listaBancos;
    /*
    varibale para almacenar la obseracion
     */
    private String observ;
    /*
    varibale para almacenar la ubicación
     */
    private String ubicacion;
    /*
    variable para mostrar la pantalla inicial
     */
    private boolean pantallaInicial;

    private Date fecha;

    private String fechaVentaStr;
    private String fechaVencimientoStr;
    short s = 0;
    private String npFija = "54000000";//"02000000"

    /*
    variable para mostrar pantalla de carga
     */
    private boolean gestionarCarga;

    /**
     * Constructor por defecto
     */
    public CargaFacturaBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.pagofactura";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.pagofactura";
        comercializadora = new ComercializadoraBean();
        banco = new Banco();
        listaBancos = new ArrayList<>();
        pantallaInicial = true;
        obtenerComercializadora();
        obtenerBancos();
    }

    private void obtenerBancos() {
        listaBancos = new ArrayList<>();
        listaBancos = bancoServicio.obtenerBanco();
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
//            listaClientes = new ArrayList<>();
//            listaClientes = clienteServicio.obtenerClientesPorComercializadora(codigoComer);
        }
    }

    public void seleccionarBanco() {
        if (banco != null) {
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
                        if (comercializadora != null) {
                            codigoComer = comercializadora.getCodigo();
                        }
                    }
                }
                //obtenerPagoFactura(comercializadora.getCodigo(), new Date());
            }
            if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                        if (comercializadora != null) {
                            codigoComer = comercializadora.getCodigo();
                        }
                    }
                }
                //obtenerPagoFactura(comercializadora.getCodigo(), new Date());
            }
        }
    }

    public void cargarFacturas() {
        observ = "";
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
            banco = new Banco();
            ubicacion = "";
            //listaDetallePagofacturaArchivoSubida = new ArrayList<>();
        }
        PrimeFaces.current().executeScript("PF('zip').show()");
    }

    public void procesar() {
        // 1. Leer los registros del archivo de texto
        // 2. Armar un conjunto de objetos EnvioFactura
        // 3. LLamar al servicio cargarFacturasBancos
        // 4. Mostar el resultado del proceso
        // 5. Ejecutar el ednpoint http://www.supertech.ec:8080/infinityone1/resources/ec.com.infinity.modelo.clientegarantia/actualizargarantiasvencidas
        // 6. Mostrar el resultado del proceso 5
    }

    public void handleFileUploadDoc(FileUploadEvent event) {
        UploadedFile archivo = event.getFile();
        codigoComer = comercializadora.getCodigo();
        if (archivo != null) {
            if (codigoComer != null && banco.getCodigo() != null) {
                try (InputStream input = archivo.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
                    StringBuilder contenido = new StringBuilder();
                    String linea;
                    List<EnvioFactura> listaEnvF = new ArrayList<>();
                    List<JSONObject> listObjEnvRest = new ArrayList<>();
                    while ((linea = br.readLine()) != null) {
                        armarJSONEnvioFacturaDesdeTxt(linea, listObjEnvRest);
                    }
                    System.out.println("FT-2-FRont::. listaEnvF " + listaEnvF.toString() + " - Items - " + listaEnvF.size());
                    cargarFacturaServicio.cargarFacturasBanco(listObjEnvRest);
                    cargarFacturaServicio.actualizarGarantias();
                } catch (IOException e) {
                    ubicacion = "Error al leer el archivo: " + e.getMessage();
                    System.err.println(ubicacion);
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_WARN, "Seleccione una comercializadora y un banco para poder cargar el archivo");
            }
        } else {
            ubicacion = "No se ha cargado ningún archivo.";
        }
    }

    public List<JSONObject> armarJSONEnvioFacturaDesdeTxt(String lineaLeida, List<JSONObject> listaEnvF) {

        System.out.println("FT::. INICI armarJSONEnvioFacturaDesdeTxt String lineaLeida :. " + lineaLeida + " ENTRA Y SALE listaEnvF:. " + listaEnvF.toString());
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        //String fechaI = date.format(precio.get(i).getPrecioPK().getFechainicio()) + "T12:00:00";
//        String fechaI = date.format(fechaVencimiento) + "T12:00:00";

        JSONObject obj = new JSONObject();
        JSONObject objPK = new JSONObject();
        JSONObject detalle = new JSONObject();
        JSONObject detallePK = new JSONObject();

        JSONObject objEnvRest = new JSONObject();
        List<JSONObject> arrObj = new ArrayList<>();

        objPK.put("codigoabastecedora", comercializadora.getAbastecedora());
        objPK.put("codigocomercializadora", comercializadora.getCodigo());
//FT. FIJAR UN NUMERO DE NP FICTICIA-NO SE DEBE USAR ESTA INFORMACION EN PROCESOS ADICIONALES AL CONTROL DE GARANTIAS            objPK.put("numeronotapedido", lineaLeida.substring(5, 13));
        objPK.put("numeronotapedido", npFija);
        objPK.put("numero", lineaLeida.substring(13, 26));
        obj.put("facturaPK", objPK);

        // FORMATO PARA FECHAS"yyyy-MM-dd HH:mm:ss"
        fechaVentaStr = lineaLeida.substring(26, 30) + "-"
                + lineaLeida.substring(30, 32) + "-"
                + lineaLeida.substring(32, 34) + "T11:00:00";
        fechaVencimientoStr = lineaLeida.substring(179, 183) + "-"
                + lineaLeida.substring(183, 185) + "-"
                + lineaLeida.substring(185, 187) + "T11:00:00";
        //VERIFICAR COMO CREAR UNA FECHA DESDE UN STRING ???

        obj.put("fechaventa", fechaVentaStr);
        obj.put("fechavencimiento", fechaVencimientoStr);
        obj.put("fechaacreditacion", fechaVencimientoStr);
        obj.put("fechadespacho", fechaVentaStr);
        switch (lineaLeida.substring(0, 1)) {
            case "1":
                obj.put("activa", true);
                break;
            case "2":
                obj.put("activa", false);
                break;
            default:
                throw new AssertionError();
        }
        obj.put("valortotal", new BigDecimal(lineaLeida.substring(64, 75)).movePointLeft(2));
        obj.put("valorconrubro", new BigDecimal(lineaLeida.substring(64, 75)).movePointLeft(2));
        obj.put("ivatotal", BigDecimal.ZERO);
        obj.put("observacion", "FACTURA MIGRADA DESDE TXT-BANCO NP: " + lineaLeida.substring(5, 13));
        obj.put("pagada", false);
        obj.put("oeenpetro", true);
        obj.put("codigocliente", lineaLeida.substring(35, 43));
        obj.put("codigoterminal", lineaLeida.substring(61, 63));
        obj.put("codigobanco", lineaLeida.substring(1, 3));
        obj.put("usuarioactual", dataUser.getUser().getNombrever());
        obj.put("nombrecomercializadora", comercializadora.getNombre());
        obj.put("ruccomercializadora", comercializadora.getRuc());
        obj.put("direccionmatrizcomercializadora", comercializadora.getDireccion());
        obj.put("nombrecliente", "");
        obj.put("ruccliente", lineaLeida.substring(43, 56));
        obj.put("valorsinimpuestos", BigDecimal.ZERO);
        obj.put("correocliente", "");
        obj.put("direccioncliente", "");
        obj.put("telefonocliente", "");
        obj.put("numeroautorizacion", "");
        obj.put("fechaautorizacion", "");
        obj.put("clienteformapago", "");
        obj.put("plazocliente", Integer.valueOf(lineaLeida.substring(57, 60)));
        obj.put("claveacceso", "0");
        obj.put("campoadicionalCampo1", "");
        obj.put("campoadicionalCampo2", "Grandes Contribuyentes NAC-GCFOIOC21-00001239-E");
        obj.put("campoadicionalCampo3", "");
        obj.put("campoadicionalCampo4", "");
        obj.put("campoadicionalCampo5", "");
        obj.put("campoadicionalCampo6", "Hola");
        obj.put("estado", "AUTORIZADA");
        obj.put("errordocumento", s);
        obj.put("hospedado", s);
        obj.put("ambientesri", "1");
        obj.put("tipoemision", "2");
        obj.put("codigodocumento", "01");
        obj.put("esagenteretencion", true);
        obj.put("escontribuyenteespacial", "si");
        obj.put("obligadocontabilidad", "si");
        obj.put("tipocomprador", "04");
        obj.put("moneda", "DOLAR");
        obj.put("seriesri", lineaLeida.substring(13, 19));
        obj.put("adelantar", false);

//FT::.. REVISAR TIPOPLAZO NO VIENE EN EL ARCHIVO DEL BANCO??????
        obj.put("tipoplazocredito", "CAL");

        obj.put("oeanuladaenpetro", false);
        obj.put("refacturada", false);
        obj.put("reliquidada", false);
        obj.put("seleccionar", false);
        obj.put("fechaacreditacionprorrogada", fechaVencimientoStr);
        obj.put("clienteformapagonosri", "03");
        obj.put("despachada", true);
        obj.put("enviadaxcobrar", false);

//FT::. CREAR DETALLE DE FACTURA 
        detallePK.put("codigoabastecedora", comercializadora.getAbastecedora());
        detallePK.put("codigocomercializadora", comercializadora.getCodigo());
        detallePK.put("numeronotapedido", npFija);
        detallePK.put("numero", lineaLeida.substring(13, 26));
        detallePK.put("codigoproducto", lineaLeida.substring(102, 106));
        detalle.put("detallefacturaPK", detallePK);

        detalle.put("volumennaturalrequerido", new BigDecimal(lineaLeida.substring(106, 116)).movePointLeft(3));
        detalle.put("volumennaturalautorizado", new BigDecimal(lineaLeida.substring(106, 116)).movePointLeft(3));
        detalle.put("precioproducto", BigDecimal.ZERO);
        detalle.put("subtotal", BigDecimal.ZERO);
        detalle.put("usuarioactual", dataUser.getUser().getNombrever());
        detalle.put("ruccomercializadora", comercializadora.getRuc());
        detalle.put("nombreproducto", "");
        detalle.put("codigoimpuesto", "");
        detalle.put("nombreimpuesto", "");
        detalle.put("codigoprecio", "3000000");
        detalle.put("seimprime", false);

        detalle.put("valordefecto", BigDecimal.ZERO);
        detalle.put("codigomedida", "01");
        arrObj.add(detalle);

        objEnvRest.put("factura", obj);
        objEnvRest.put("detalle", arrObj);

        listaEnvF.add(objEnvRest);

        System.out.println("FT-2-FRont::. OBJETO JSON-FACTURA" + obj.toString());
        System.out.println("FT-2-FRont::. OBJETO JSON-DETALLEFACTURA" + arrObj.toString());
        System.out.println("FT-2-FRont::. OBJETO JSON-ENVIOFACTURA" + objEnvRest.toString());
        System.out.println("FT-2-FRont::. OBJETO JSON-arreglode ENVIOFACTURA" + listaEnvF.toString());
        obj = new JSONObject();
        objPK = new JSONObject();
        arrObj = new ArrayList<>();
        objEnvRest = new JSONObject();

        return listaEnvF;

    }

    public BancoServicio getBancoServicio() {
        return bancoServicio;
    }

    public void setBancoServicio(BancoServicio bancoServicio) {
        this.bancoServicio = bancoServicio;
    }

    public ComercializadoraServicio getComercializadoraServicio() {
        return comercializadoraServicio;
    }

    public void setComercializadoraServicio(ComercializadoraServicio comercializadoraServicio) {
        this.comercializadoraServicio = comercializadoraServicio;
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

    public String getCodigoComer() {
        return codigoComer;
    }

    public void setCodigoComer(String codigoComer) {
        this.codigoComer = codigoComer;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getObserv() {
        return observ;
    }

    public void setObserv(String observ) {
        this.observ = observ;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isPantallaInicial() {
        return pantallaInicial;
    }

    public void setPantallaInicial(boolean pantallaInicial) {
        this.pantallaInicial = pantallaInicial;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isGestionarCarga() {
        return gestionarCarga;
    }

    public void setGestionarCarga(boolean gestionarCarga) {
        this.gestionarCarga = gestionarCarga;
    }

    public List<Banco> getListaBancos() {
        return listaBancos;
    }

    public void setListaBancos(List<Banco> listaBancos) {
        this.listaBancos = listaBancos;
    }
}
