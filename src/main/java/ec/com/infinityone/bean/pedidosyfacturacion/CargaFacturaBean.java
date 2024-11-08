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
import java.io.Serializable;
import java.math.BigDecimal;
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

    public List<EnvioFactura> armarEnvioFacturaDesdeTxt(String lineaLeida, List<EnvioFactura> listaEnvF) throws WebApplicationException {
        System.out.println("FT(1)::. Armar una coleccion de obtejos EnvioFactura, desde un archivo TXT de unBanco");
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

            fac.setValortotal(new BigDecimal(lineaLeida.substring(64, 76)).movePointLeft(2));
            fac.setValorconrubro(fac.getValortotal());
            // IVA NO SE ENTREGA EN ARCHIVO DEL BANCO. SE ASUME EL MISMO VALOR QUE EL TOTAL DE LA FACTURA
            fac.setIvatotal(fac.getValortotal());
            fac.setObservacion("FACTURA MIGRADA DESDE TXT-BANCO: ");
            fac.setPagada(false);
            fac.setOeenpetro(true);
            fac.setCodigocliente(lineaLeida.substring(35, 43));
            fac.setCodigoterminal(lineaLeida.substring(61, 63));
            fac.setCodigobanco(lineaLeida.substring(2, 4));
            fac.setAdelantar(false);
// REVISAR SE DEBE TOMAR EL USUARIO CONECTADO            
            fac.setUsuarioactual("FTFTFT");
            fac.setNombrecomercializadora(comercializadora.getNombre());
            fac.setRuccomercializadora(comercializadora.getRuc());
            fac.setDireccionmatrizcomercializadora(comercializadora.getDireccion());
            fac.setNombrecliente("");
            fac.setRuccliente(lineaLeida.substring(43, 56));
            fac.setValorsinimpuestos(new BigDecimal(lineaLeida.substring(130, 143)).movePointLeft(2));
            fac.setCorreocliente("");
            fac.setDireccioncliente("");
            fac.setTelefonocliente("");
// REVISAR LA FORMA DE PAGO EN EL BANCO SOLO ES CRÉDITO O EFECTIVO - EN INFINITY QUE DEBEMOS COLOCAR??? GESTION DIRECTA??????
            fac.setClienteformapago("");
            fac.setClienteformapagonosri("");
            // REVISAR SI EN LOS ARCHIVOS TXT VIENE LA CLAVE DE ACCESO Y/O EL NUMERO DE AUTORIZACION. SI NO DEJAR EN BLANCO??????
            fac.setNumeroautorizacion("0");
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
            detalleFactura.setUsuarioactual("ftft");

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
                try (InputStream input = archivo.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
                    StringBuilder contenido = new StringBuilder();
                    String linea;
                    List<EnvioFactura> listaEnvF = new ArrayList<>();
                    while ((linea = br.readLine()) != null) {
                        armarEnvioFacturaDesdeTxt(linea, listaEnvF);
                        contenido.append(linea).append("\n"); // Procesa cada línea
                    }
//                    ubicacion = "Archivo leído correctamente:\n" + contenido.toString();
//                    System.out.println(ubicacion); // Para ver el contenido en consola
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonListaEnvF = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(listaEnvF);
//                    System.out.println("JSON de listaEnvF:");
//                    System.out.println(jsonListaEnvF);
//                    if(!jsonListaEnvF.equals(""))                  
                       cargarFacturaServicio.actualizarGarantias(jsonListaEnvF);
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
