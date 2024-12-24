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
import jdk.internal.org.jline.terminal.TerminalBuilder;
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
public class CargaPagoBean extends ReusableBean implements Serializable {

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
    public CargaPagoBean() {
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

    public List<EnvioFactura> armarEnvioFacturaDesdeTxt(String lineaLeida, List<EnvioFactura> listaEnvF) throws WebApplicationException {
        System.out.println("FT(1)::. FRONT-END Armar una coleccion de obtejos EnvioFactura, desde un archivo TXT de unBanco");
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Factura fac = new Factura();
        Cliente cli = new Cliente();
        FacturaPK fpk = new FacturaPK();
        Detallefactura dtF = new Detallefactura();
        Detallefactura detalleFactura = new Detallefactura();
        DetallefacturaPK detalleFacturaPK = new DetallefacturaPK();
        BigDecimal subtotal = new BigDecimal(0);
        //List<Detallefactura> listDetF = new ArrayList<>();
        EnvioFactura envF = new EnvioFactura();
        DateFormat fechaemision = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
        envF.setDetalle(new ArrayList<>());
        // EXPLICAR QUE ESTA VARIABLE DEBERÁ TOMARSE DEL COMBO DE COMERCIALIZADORAS DE LA PANTALLA
        //Comercializadora COMBOComercializadora = new Comercializadora();

        //DESDE  AQUI DEBERÍA ESTAR EL FOR(){  PARA IR LEYENDO LAS LINEAS DEL ARCHIVO TXT
        //String lineaLeida = "13600269857120010060985712202410072130102691190016532001000511600000066315020241003115028    000000001012100039600000100000000000000000005654180000000000000000000000000000000000002024101411151078290310202401179128229900120010060009857120000000115 0310202401179128229900120010060009857120000000115 20241003";
        String fechaVentaStr = "";
        String fechaVencimientoStr = "";
        //REGISTRO TXT BANCO DE GUAYAQUIL
        //13600269857120010060985712202410072130102691190016532001000511600000066315020241003115028    000000001012100039600000100000000000000000005654180000000000000000000000000000000000002024101411151078290310202401179128229900120010060009857120000000115 0310202401179128229900120010060009857120000000115 20241003
        //ESTADO DE LA FACTURA 1 ACTIVA 2 ANULADA	CODIGO DEL BANCO 36 BANCO INTERNACIONAL	INCIALES DE LA CIUDAD DONDE SE FACTURA	NUMERO DE ORDEN	NUMERO DE FACTURA SRI	FECHA DE VENTA
        //
        try {
            short s = 0;
            // ESTA LINEA DEBERÍA SALIR DEL COMBO ; LA SIGUIENTE LINEA NO!!!! fpk.setCodigoabastecedora(COMBOComercializadora.getComercializadora.getCodigoAbastecedora());
            fpk.setCodigoabastecedora(comercializadora.getAbastecedora());//COMBOComercializadora.getCodigoabastecedora().getCodigo());
            fpk.setCodigocomercializadora(comercializadora.getCodigo());//COMBOComercializadora.getCodigo());

            fpk.setNumeronotapedido(lineaLeida.substring(5, 13));
            ////FTFT-> CAMBIO A NP FIJA 54000000(DESARROLLO) --> 02000000 (PRODUCCION) fpk.setNumeronotapedido(lineaLeida.substring(5, 13));
            fpk.setNumeronotapedido(npFija);

            fpk.setNumero(lineaLeida.substring(13, 26));
            fac.setFacturaPK(fpk);
            //String fd = date.format(envNP.getNotapedido().getFechadespacho());
            //String fv = date.format(envNP.getNotapedido().getFechaventa());
            //String fvc = date.format(calcularFechaV(envNP.getNotapedido()));

            fechaVentaStr = lineaLeida.substring(26, 30) + "/" + lineaLeida.substring(30, 32) + "/" + lineaLeida.substring(32, 34);
            fechaVencimientoStr = lineaLeida.substring(179, 183) + "/" + lineaLeida.substring(183, 185) + "/" + lineaLeida.substring(185, 187);
            //VERIFICAR COMO CREAR UNA FECHA DESDE UN STRING ???
            fac.setFechaventa(fechaVentaStr);
            fac.setFechavencimiento(fechaVencimientoStr);
            fac.setFechaacreditacion(fechaVencimientoStr);
            fac.setFechaacreditacionprorrogada(fechaVencimientoStr);

            // VERIFICAR LA FECHA DE DESPACHO EN UN BANCO ES LA MISMA QUE LA FECHA DE VENTA ????
            fac.setFechadespacho(fechaVentaStr);
            switch (lineaLeida.substring(0, 1)) {
                case "1":
                    fac.setActiva(true);
                    break;
                case "2":
                    fac.setActiva(false);
                    break;
                default:
                    throw new AssertionError();
            }

            fac.setValortotal(new BigDecimal(lineaLeida.substring(64, 75)).movePointLeft(2));
            fac.setValorconrubro(fac.getValortotal());
            // IVA NO SE ENTREGA EN ARCHIVO DEL BANCO. SE ASUME EL MISMO VALOR QUE EL TOTAL DE LA FACTURA
            fac.setIvatotal(fac.getValortotal());
            fac.setObservacion("FACTURA MIGRADA DESDE TXT-BANCO NP: " + lineaLeida.substring(5, 13));
            fac.setPagada(false);
            fac.setOeenpetro(true);
            fac.setCodigocliente(lineaLeida.substring(35, 43));
            fac.setCodigoterminal(lineaLeida.substring(61, 63));
            fac.setCodigobanco(lineaLeida.substring(1, 3));
            fac.setAdelantar(false);
// REVISAR SE DEBE TOMAR EL USUARIO CONECTADO            
            fac.setUsuarioactual(dataUser.getUser().getNombrever());
            fac.setNombrecomercializadora(comercializadora.getNombre());
            fac.setRuccomercializadora(comercializadora.getRuc());
            fac.setDireccionmatrizcomercializadora(comercializadora.getDireccion());
            fac.setNombrecliente("");
            fac.setRuccliente(lineaLeida.substring(43, 56));
            fac.setValorsinimpuestos(BigDecimal.ZERO);
            fac.setCorreocliente("");
            fac.setDireccioncliente("");
            fac.setTelefonocliente("");
// REVISAR LA FORMA DE PAGO EN EL BANCO SOLO ES CRÉDITO O EFECTIVO - EN INFINITY QUE DEBEMOS COLOCAR??? GESTION DIRECTA??????
            fac.setClienteformapago("");
            fac.setClienteformapagonosri("");
            // REVISAR SI EN LOS ARCHIVOS TXT VIENE LA CLAVE DE ACCESO Y/O EL NUMERO DE AUTORIZACION. SI NO DEJAR EN BLANCO??????
            fac.setNumeroautorizacion("");
            fac.setFechaautorizacion("");
            fac.setPlazocliente(Integer.valueOf(lineaLeida.substring(57, 60)));
            fac.setClaveacceso("0");

            fac.setCampoadicionalCampo1("");
            fac.setCampoadicionalCampo2("");
            fac.setCampoadicionalCampo2("Grandes Contribuyentes NAC-GCFOIOC21-00001239-E");
            fac.setCampoadicionalCampo3("");
            fac.setCampoadicionalCampo4("");
            fac.setCampoadicionalCampo5("");
            fac.setCampoadicionalCampo6("Contribuyente Especial según resolución del 07/05/1996");
            fac.setEstado("AUTORIZADA");
            fac.setErrordocumento(s);
            fac.setHospedado(s);
            fac.setAmbientesri('1');
            fac.setTipoemision('2');
            fac.setCodigodocumento("01");
            fac.setEsagenteretencion(true);
            fac.setEscontribuyenteespacial("si");
            fac.setObligadocontabilidad("si");
            fac.setTipocomprador("04");
            fac.setMoneda("DOLAR");
            fac.setSeriesri(lineaLeida.substring(13, 19));

            // REVISAR TIPOPLAZO NO VIENE EN EL ARCHIVO DEL BANCO??????
            fac.setTipoplazocredito("CAL");
//            envF.setFactura(fac);

// CREAR DETALLE DE FACTURA
            detalleFacturaPK.setCodigoabastecedora(fpk.getCodigoabastecedora());
            detalleFacturaPK.setCodigocomercializadora(fpk.getCodigocomercializadora());
            detalleFacturaPK.setNumeronotapedido(fpk.getNumeronotapedido());
            detalleFacturaPK.setNumero(fpk.getNumero());
            detalleFacturaPK.setCodigoproducto(lineaLeida.substring(102, 106));
            detalleFactura.setNombreproducto("");
            detalleFactura.setDetallefacturaPK(detalleFacturaPK);

// VERIFICAR CREAR DOS OBJETOS Medida y ponerlos en un MAP key->codigo; value->objeto y AQUÍ TOMAR DESDE EL MAPA
            //ftft detalleFactura.setCodigomedida(lineaLeida.substring(116, 118));
            //detalleFactura.setCodigomedida(new Medida("01","galones","gls",true,"ft"));
            detalleFactura.setCodigomedida("01");
//            
            detalleFactura.setVolumennaturalautorizado(new BigDecimal(lineaLeida.substring(106, 116)).movePointLeft(3));
            detalleFactura.setVolumennaturalrequerido(new BigDecimal(lineaLeida.substring(106, 116)).movePointLeft(3));
// REVISAR QUE CODIGO DE PRECIO COLOCAR ???? 
            detalleFactura.setCodigoprecio("3000000");

// REVISAR QUE CODIGO DE PRECIO COLOCAR ????             
            detalleFactura.setPrecioproducto(BigDecimal.ZERO);
            detalleFactura.setSubtotal(BigDecimal.ZERO);

//ftft            detalleFactura.setUsuarioactual(usuarioActualPantalla);
            detalleFactura.setUsuarioactual(dataUser.getUser().getNombrever());

            detalleFactura.setRuccomercializadora(comercializadora.getRuc());
            detalleFactura.setCodigoimpuesto("");
            detalleFactura.setNombreimpuesto("");
            detalleFactura.setSeimprime(false);
            detalleFactura.setValordefecto(new BigDecimal(0));

            fac.setValorsinimpuestos(detalleFactura.getSubtotal());
            envF.setFactura(fac);
            envF.getDetalle().add(detalleFactura);

            listaEnvF.add(envF);

            System.out.println("");

// FIN DE CREAR DETALLE DE FACTURA
            System.out.println("FT::-ENVIOFACTURA CREADA!:" + envF.getFactura().getFacturaPK().getNumeronotapedido());
            return listaEnvF;
        } catch (AssertionError | NumberFormatException t) {
            System.out.println("FT:: ERROR EN " + this.toString() + "::Crear EnvioFactura desde archivoTXT " + t.getMessage());
            t.printStackTrace(System.out);
            return listaEnvF;
// ftft           throw new Throwable("ERROR EN crearEnvioFactura: " + t.getMessage());
        }
//HASTA AQUI DEBERÍA ESTAR EL FOR(){  PARA IR LEYENDO LAS LINEAS DEL ARCHIVO TXT
    }

    public void handleFileUploadDoc(FileUploadEvent event) {
        UploadedFile archivo = event.getFile();
        codigoComer = comercializadora.getCodigo();
        if (archivo != null) {
            if (codigoComer != null && banco.getCodigo() != null) {

                switch (banco.getCodigo().trim()) {
                    case "00":
                        cargarPagosGuayaquil(archivo);
                        break;
                    case "36":
                        cargarPagosInternacional(archivo);
                        break;
                    case "37":
                        cargarPagosGuayaquil(archivo);
                        break;
                    default:
                        throw new AssertionError();
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_WARN, "Seleccione una comercializadora y un banco para poder cargar el archivo");
            }
        } else {
            ubicacion = "No se ha cargado ningún archivo.";
        }
    }

    public void cargarPagosGuayaquil(UploadedFile archivo) {

        int suma = 0;
        try (InputStream input = archivo.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            List<EnvioFactura> listaEnvF = new ArrayList<>();
            List<JSONObject> listObjEnvRest = new ArrayList<>();
            while ((linea = br.readLine()) != null) {
                armarJSONEnvioPagosDesdeTxt37(linea, listObjEnvRest);
            }
            System.out.println("FT-2-FRont::. listaEnvF " + listaEnvF.toString() + " - Items - " + listaEnvF.size());
            guardarPagosLoteJSON(listObjEnvRest);

        } catch (IOException e) {
            ubicacion = "Error al leer el archivo: " + e.getMessage();
            System.err.println(ubicacion);
        }
    }
    
    
//    public List<JSONObject> armarJSONEnvioPagosDesdeTxt37abc(String lineaLeida, List<JSONObject> listaEnvF) throws Throwable {
//        String respuesta;
//        DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
//        JSONObject obj = new JSONObject();
//        JSONObject objPK = new JSONObject();
//        JSONObject objEnvRest = new JSONObject();
//        List<JSONObject> arrObj = new ArrayList<>();
//        List<JSONObject> listObjEnvRest = new ArrayList<>();
//
//        objPK.put("codigoabastecedora", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getCodigoabastecedora());
//        objPK.put("codigocomercializadora", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getCodigocomercializadora());
//        objPK.put("numero", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getNumero());
//        objPK.put("codigobanco", listaPagofacturaArchivoSubida.get(0).getPagofacturaPK().getCodigobanco());
//        obj.put("pagofacturaPK", objPK);
//        String fechaS = date.format(listaPagofacturaArchivoSubida.get(0).getFecha());
//        obj.put("fecha", fechaS);
//        obj.put("activo", listaPagofacturaArchivoSubida.get(0).getActivo());
//        obj.put("valor", suma);
//        obj.put("observacion", observacion);
//        String fechaR = date.format(listaPagofacturaArchivoSubida.get(0).getFecharegistro());
//        obj.put("fecharegistro", fechaR);
//        obj.put("usuarioactual", listaPagofacturaArchivoSubida.get(0).getUsuarioactual());
//
//        for (int indice = 0; indice < listaPagosBancoRechazado.size(); indice++) {
//            arrObj.add(addItemsDetailPAux(listaPagosBancoRechazado.get(indice)));
//        }
//
//        objEnvRest.put("pago", obj);
//        objEnvRest.put("detallepago", arrObj);
//        listObjEnvRest.add(objEnvRest);
//        obj = new JSONObject();
//        objPK = new JSONObject();
//        arrObj = new ArrayList<>();
//        objEnvRest = new JSONObject();
//
//        return listObjEnvRest;
//
//    }
    
    
    public List<JSONObject> armarJSONEnvioPagosDesdeTxt37(String lineaLeida, List<JSONObject> listaEnvF) {

        System.out.println("FT::. INICI armarJSONEnvioPagosDesdeTxt37 String lineaLeida :. " + lineaLeida + " ENTRA Y SALE listaEnvF:. " + listaEnvF.toString());
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        //String fechaI = date.format(precio.get(i).getPrecioPK().getFechainicio()) + "T12:00:00";
//        String fechaI = date.format(fechaVencimiento) + "T12:00:00";

        JSONObject obj = new JSONObject();
        JSONObject objPK = new JSONObject();
        JSONObject detalle = new JSONObject();
        JSONObject detallePK = new JSONObject();

        JSONObject objEnvRest = new JSONObject();
        List<JSONObject> arrObj = new ArrayList<>();

        // LINEA BG e INFINITY 00002 037 371145980 2011726003 20241121 000000014579.44
        //                     01234 567 890123456 7890123456 78901234 567890123456789  35 47 48 50
        objPK.put("codigoabastecedora", comercializadora.getAbastecedora());
        objPK.put("codigocomercializadora", comercializadora.getCodigo());
        objPK.put("numero", lineaLeida.substring(5, 17));
        objPK.put("codigobanco", lineaLeida.substring(6, 8));
        obj.put("pagofacturaPK", objPK);

        // FORMATO PARA FECHAS"yyyy-MM-dd HH:mm:ss"
        fechaVentaStr = lineaLeida.substring(27, 31) + "-"
                + lineaLeida.substring(31, 33) + "-"
                + lineaLeida.substring(33, 35) + "T11:00:00";

        obj.put("fecha", fechaVentaStr);

        obj.put("activo", true);

        obj.put("valor", new BigDecimal((lineaLeida.substring(35, 50).replace(".", ""))).movePointLeft(2));
        obj.put("observacion", "CARGA DE PAGOS DESDE ARCHIVO #-NP:. " + lineaLeida.substring(8, 16));
        obj.put("fecharegistro", fechaVentaStr);
        obj.put("usuarioactual", dataUser.getUser().getNombrever());
        obj.put("codigobancocheque", "");
        obj.put("cuentacheque", "");
        obj.put("numerocheque", "");

//FT::. CREAR DETALLE DE PAGO
        // LINEA BG e INFINITY 00002 037 371145980 2011726003 20241121 000000014579.44
        //                     01234 567 890123456 7890123456 78901234 567890123456789  35 47 48 50

        detallePK.put("codigoabastecedora", comercializadora.getAbastecedora());
        detallePK.put("codigocomercializadora", comercializadora.getCodigo());
        detallePK.put("numeronotapedido", npFija);
        detallePK.put("numero", lineaLeida.substring(5, 17));
        detallePK.put("codigobanco", lineaLeida.substring(6, 8));
        detallePK.put("numerofactura", ("0000000"+lineaLeida.substring(8, 16)));
        detalle.put("detallepagoPK", detallePK);

        detalle.put("valor", new BigDecimal((lineaLeida.substring(35, 50).replace(".", ""))).movePointLeft(2));
        
                detalle.put("activo", true);
        
        detalle.put("usuarioactual", dataUser.getUser().getNombrever());
        detalle.put("claveacceso", "00000000000000000000000000000000000000000000000000");
        detalle.put("valormora", BigDecimal.ZERO);
        detalle.put("tasainteres", BigDecimal.ZERO);
        detalle.put("fechaventa", fechaVentaStr);
        detalle.put("fechaacreditacionprorrogada", fechaVentaStr);
        detalle.put("diasretraso", BigDecimal.ZERO);
        detalle.put("diasretraso", BigDecimal.ZERO);
        detalle.put("valorconrubro", new BigDecimal((lineaLeida.substring(35, 50).replace(".", ""))).movePointLeft(2));

        arrObj.add(detalle);
//         -------
        objEnvRest.put("pago", obj);
        objEnvRest.put("detallepago", arrObj);

//////////            listObjEnvRest.add(objEnvRest);
        listaEnvF.add(objEnvRest);

        System.out.println("FT-2-FRont::. OBJETO JSON-PAGOFACTURA" + obj.toString());
        System.out.println("FT-2-FRont::. OBJETO JSON-DETALLEPAGO" + arrObj.toString());
        System.out.println("FT-2-FRont::. OBJETO JSON-ENVIOPAGO" + objEnvRest.toString());
//////////            System.out.println("FT-2-FRont::. OBJETO JSON-arreglode ENVIOFACTURA"+listObjEnvRest.toString() );
        System.out.println("FT-2-FRont::. OBJETO JSON-arreglode ENVIOPAGO" + listaEnvF.toString());
        obj = new JSONObject();
        objPK = new JSONObject();
        arrObj = new ArrayList<>();
        objEnvRest = new JSONObject();
////////////////////        }

////////        return listObjEnvRest;
        return listaEnvF;

    }

    public void cargarPagosInternacional(UploadedFile archivo) {


        try (InputStream input = archivo.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            List<EnvioFactura> listaEnvF = new ArrayList<>();
            List<JSONObject> listObjEnvRest = new ArrayList<>();
                for (int i = 1; i < 6; i++) {
                    linea = br.readLine();
                    System.out.println("FT-cargarPagosInternacional. Leyendo lineas iniciales " + linea);
                }
            while ((linea = br.readLine()) != null) {
                armarJSONEnvioPagosDesdeTxt36(linea, listObjEnvRest);
            }
            System.out.println("FT-2-FRont::. listaEnvF " + listaEnvF.toString() + " - Items - " + listaEnvF.size());
            guardarPagosLoteJSON(listObjEnvRest);

        } catch (IOException e) {
            ubicacion = "FT:. cargarPagosInternacional- Error al leer el archivo : " + e.getMessage();
            System.err.println(ubicacion);
        }
    }

    public List<JSONObject> armarJSONEnvioPagosDesdeTxt36(String lineaLeida, List<JSONObject> listaEnvF) {

        System.out.println("FT::. INICI armarJSONEnvioPagosDesdeTxt36 String lineaLeida :. " + lineaLeida + " ENTRA Y SALE listaEnvF:. " + listaEnvF.toString());
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        //String fechaI = date.format(precio.get(i).getPrecioPK().getFechainicio()) + "T12:00:00";
//        String fechaI = date.format(fechaVencimiento) + "T12:00:00";
        int posicionMoneda = 0;
        int posicionPuntodecimal = 0;
        JSONObject obj = new JSONObject();
        JSONObject objPK = new JSONObject();
        JSONObject detalle = new JSONObject();
        JSONObject detallePK = new JSONObject();

        JSONObject objEnvRest = new JSONObject();
        List<JSONObject> arrObj = new ArrayList<>();

        //31/OCT/2024			E/S PETROFLAVIO                         	26993169		       $12,037.91 	21/NOV/2024	14/NOV/2024

        //01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890 
        objPK.put("codigoabastecedora", comercializadora.getAbastecedora());
        objPK.put("codigocomercializadora", comercializadora.getCodigo());
        objPK.put("numero", lineaLeida.substring(78, 97));
        objPK.put("codigobanco", banco.getCodigo());
        obj.put("pagofacturaPK", objPK);

        // FORMATO PARA FECHAS"yyyy-MM-dd HH:mm:ss"
        fechaVentaStr = lineaLeida.substring(123, 127) + "-"
                + lineaLeida.substring(119, 122) + "-"
                + lineaLeida.substring(16, 118) + "T11:00:00";

        obj.put("fecha", fechaVentaStr);
 
        obj.put("activo", true);
        
        posicionMoneda = lineaLeida.indexOf("$");
        posicionPuntodecimal = lineaLeida.indexOf(".", posicionMoneda);
        System.out.println("FT:. VALOR LEIDO "+(lineaLeida.substring(posicionMoneda+1,posicionPuntodecimal)+lineaLeida.substring(posicionPuntodecimal+1, posicionPuntodecimal+2)));
        obj.put("valor", new BigDecimal((lineaLeida.substring(posicionMoneda+1,posicionPuntodecimal)+lineaLeida.substring(posicionPuntodecimal+1, posicionPuntodecimal+2))).movePointLeft(2));
        obj.put("observacion", "CARGA DE PAGOS DESDE ARCHIVO NP:. " + lineaLeida.substring(78, 97));
        obj.put("fecharegistro", fechaVentaStr);
        obj.put("usuarioactual", dataUser.getUser().getNombrever());
        obj.put("codigobancocheque", "");
        obj.put("cuentacheque", "");
        obj.put("numerocheque", "");

//FT::. CREAR DETALLE DE PAGO
        detallePK.put("codigoabastecedora", comercializadora.getAbastecedora());
        detallePK.put("codigocomercializadora", comercializadora.getCodigo());
        detallePK.put("numeronotapedido", npFija);
        detallePK.put("numero", lineaLeida.substring(78, 97));
        detallePK.put("codigobanco", banco.getCodigo());
        detallePK.put("numerofactura", lineaLeida.substring(13, 26));
        detalle.put("detallepagoPK", detallePK);

        detalle.put("valor", new BigDecimal((lineaLeida.substring(posicionMoneda+1,posicionPuntodecimal)+lineaLeida.substring(posicionPuntodecimal+1, posicionPuntodecimal+2))).movePointLeft(2));
         
                detalle.put("activo", true);
          
        detalle.put("usuarioactual", dataUser.getUser().getNombrever());
        detalle.put("claveacceso", lineaLeida.substring(13, 26));
        detalle.put("valormora", BigDecimal.ZERO);
        detalle.put("tasainteres", BigDecimal.ZERO);
        detalle.put("fechaventa", fechaVentaStr);
        detalle.put("fechaacreditacionprorrogada", fechaVentaStr);
        detalle.put("diasretraso", BigDecimal.ZERO);
        detalle.put("diasretraso", BigDecimal.ZERO);
        detalle.put("valorconrubro", new BigDecimal((lineaLeida.substring(posicionMoneda+1,posicionPuntodecimal)+lineaLeida.substring(posicionPuntodecimal+1, posicionPuntodecimal+2))).movePointLeft(2));

        arrObj.add(detalle);
//         -------
        objEnvRest.put("pago", obj);
        objEnvRest.put("detallepago", arrObj);

//////////            listObjEnvRest.add(objEnvRest);
        listaEnvF.add(objEnvRest);

        System.out.println("FT-2-FRont::. OBJETO JSON-PAGOFACTURA" + obj.toString());
        System.out.println("FT-2-FRont::. OBJETO JSON-DETALLEPAGO" + arrObj.toString());
        System.out.println("FT-2-FRont::. OBJETO JSON-ENVIOPAGO" + objEnvRest.toString());
//////////            System.out.println("FT-2-FRont::. OBJETO JSON-arreglode ENVIOFACTURA"+listObjEnvRest.toString() );
        System.out.println("FT-2-FRont::. OBJETO JSON-arreglode ENVIOPAGO" + listaEnvF.toString());
        obj = new JSONObject();
        objPK = new JSONObject();
        arrObj = new ArrayList<>();
        objEnvRest = new JSONObject();
////////////////////        }

////////        return listObjEnvRest;
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

    public void guardarPagosLoteJSON(List<JSONObject> listaEnvF1) {
        List<JSONObject> arregloJSON = new ArrayList<>();
        System.out.println("FT::. INICIA guardarPagosLoteJSON List<JSONObject> listaEnvF1 TAMAÑO" + listaEnvF1.size());
//        arregloJSON.addAll(addItemsArregloJSON(listaEnvF1));
        if (addItemsFacturaAux(listaEnvF1)) {
            PrimeFaces.current().executeScript("PF('subirPrecios').hide()");
        }
    }

    public boolean addItemsFacturaAux(List<JSONObject> arregloJSON) {
        try {

            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            String respuesta;
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/agregar";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.pagofactura/agregarlotearchivo";

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
                this.dialogo(FacesMessage.SEVERITY_INFO, connection.getResponseMessage());
                System.out.println("Se ha registrado con exito");
                return true;
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, connection.getResponseMessage());
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
}
