/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.pedidosyfacturacion;

import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.serivicio.actorcomercial.ClienteServicio;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.servicio.catalogo.TerminalServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.Consultaguiaremision;
import ec.com.infinityone.modelo.ConsultaguiaremisionPK;
import ec.com.infinityone.modelo.Terminal;
import ec.com.infinityone.reusable.ReusableBean;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class GuiasRemisionBean extends ReusableBean implements Serializable {

    @Inject
    private ComercializadoraServicio comercializadoraServicio;
    /*
    Variable para acceder a los servicios de Terminal
     */
    @Inject
    protected TerminalServicio termServicio;

    @Inject
    protected ClienteServicio cliServicio;
    /*
    Variable que almacena varios Guias
     */
    private List<Consultaguiaremision> listaConsultaGuia;
    /*
    Variable auxiliar que almacena varios Guias
     */
    private List<Consultaguiaremision> listaConsultaGuiaAux;
    /*
    Variable que almacena varios Productos
     */
    private List<ComercializadoraBean> listaComercializadora;
    /*
    Variable que almacena varios Bancos
     */
    private List<Consultaguiaremision> listaConsultaGuiaArchivoSubida;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarPrecio;
    /*
    Variable que establece true or false para el estado del Banco
     */
    private boolean estadoBanco;

    private Consultaguiaremision consulGuia;

    private ConsultaguiaremisionPK consulGuiaPK;

    private ComercializadoraBean comercializadora;

    private JSONObject consGuiaPK;

    private String codigoComer;

    private int contadorArchivosOk;
    private int contadorArchivos;
    private int contadorArchivosMal;
    private String guiaVacia;

    private boolean manejoArchivos;

    /*
    Varaible para guardar la selección del radio button
     */
    protected String tipoFecha;

    private Boolean vigente;
    /*
    Variable para validar si el precio esta vigente
     */
    private boolean soloVigente;
    /*
    Variable Comercializadora
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

    private List<Cliente> listaClientes;

    private Cliente cliente;
    /**
     * Variable que permite establecer la fecha inicial para realizar la
     * busqueda de guias
     */
    protected Date fechaI;
    /**
     * Variable que permite establecer la fecha final para realizar la busqueda
     * de guias
     */
    protected Date fechaf;
    /*
    Variable para renderizar la pantalla
     */
    protected boolean mostarGuia;
    /*
    Variable para renderizar la pantalla
     */
    protected boolean mostarPantallaInicial;

    private String numGuia;

    private String codCliente;

    private UploadedFile file;

    private UploadedFiles files;

    private List<UploadedFile> list;

    private boolean procesoNuevo;

    private List<InputStream> inputStream;
    /*
    Vairbale para almacenar el pdf generado
     */
    protected StreamedContent pdfStream;

    /**
     * Constructor por defecto
     */
    public GuiasRemisionBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        soloVigente = false;
        procesoNuevo = true;
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.consultaguiaremision";
        tipoFecha = "1";
        editarPrecio = false;
        consulGuia = new Consultaguiaremision();
        consulGuiaPK = new ConsultaguiaremisionPK();
        comercializadora = new ComercializadoraBean();
        list = new ArrayList<>();
        inputStream = new ArrayList<>();
        listaConsultaGuiaArchivoSubida = new ArrayList<>();
        vigente = false;
        mostarGuia = false;
        mostarPantallaInicial = true;
        contadorArchivosOk = 0;
        contadorArchivos = 0;
        contadorArchivosMal = 0;
        guiaVacia = "";
        manejoArchivos = true;
        cliente = new Cliente();
        numGuia = "";
        codCliente = "";
        obtenerComercializadora();
        obtenerTerminales();
        habilitarBusqueda();

    }

    public void obtenerTerminales() {
        listaTermianles = new ArrayList<>();
        listaTermianles = termServicio.obtenerTerminal();
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = this.comercializadoraServicio.obtenerComercializadorasActivas();
//        if (!listaComercializadora.isEmpty()) {
//            habilitarBusqueda();
//        }
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
                        for (int i = 0; i < listaClientes.size(); i++) {
                            if (listaClientes.get(i).getCodigo().equals(dataUser.getUser().getCodigocliente())) {
                                this.cliente = listaClientes.get(i);
                                break;
                            }
                        }
                    } else {
                        this.dialogo(FacesMessage.SEVERITY_FATAL, "La comercializadora se encuentra deshabilitada");
                    }
                    seleccionarTerminal();
                    break;

                case "agco":
                    habilitarComer = false;
                    habilitarCli = true;
                    habilitarTerminal = false;
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

    public void nuevaGuia() throws ParserConfigurationException, SAXException, IOException {
        listaConsultaGuiaArchivoSubida = new ArrayList<>();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        } else {
            seleccionarComercializdora();
        }
        if (habilitarTerminal) {
            terminal = new Terminal();
        } else {
            seleccionarTerminal();
        }
        mostarGuia = true;
        mostarPantallaInicial = false;
    }

    public void regresar() {
        mostarGuia = false;
        mostarPantallaInicial = true;
        listaConsultaGuia = new ArrayList<>();
    }

    public void seleccionarCliente() {
        if (cliente != null) {
            codCliente = cliente.getCodigo();
        }
    }

    public void actualizarTipoBusqueda() {
    }

    public void obtenerGuia() throws ParseException {
        try {
            DateFormat date = new SimpleDateFormat("yyyyMMdd");
            DateFormat dateIR = new SimpleDateFormat("yyyy-MM-dd%2000:00:00");
            DateFormat dateFR = new SimpleDateFormat("yyyy-MM-dd%2023:59:59");
            String fechaS = date.format(this.fechaI);
            String fechaF = date.format(fechaf);
            String fechaIR = dateIR.format(this.fechaI);
            String fechaFR = dateFR.format(fechaf);

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
                //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/porComerEstado?codigocomercializadora=" + codigoComer + "&activo=" + vigente);
                url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.consultaguiaremision/porFechas?codigocomercializadora=" + codigoComer
                        + "&codigoterminal=" + codTerminal + "&tipofecha=1" + "&fechaI=" + fechaS + "&fechaF=" + fechaF);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                listaConsultaGuia = new ArrayList<>();

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
                    if (!retorno.isNull(indice)) {
                        JSONObject gui = retorno.getJSONObject(indice);
                        consGuiaPK = gui.getJSONObject("consultaguiaremisionPK");
                        consulGuiaPK.setCodigocomercializadora(consGuiaPK.getString("codigocomercializadora"));
                        consulGuiaPK.setNumero(consGuiaPK.getString("numero"));
                        consulGuiaPK.setFecha(consGuiaPK.getString("fecha"));
                        Long lDateRec = consGuiaPK.getLong("fecharecepcion");
                        Date dateRec = new Date(lDateRec);
                        consulGuiaPK.setFecharecepcion(dateRec);
                        consulGuia.setConsultaguiaremisionPK(consulGuiaPK);
                        consulGuia.setCodigoterminal(gui.getString("codigoterminal"));
                        consulGuia.setNumerooe(gui.getString("numerooe"));
                        consulGuia.setCodigoareamercadeo(gui.getString("codigoareamercadeo"));
                        consulGuia.setCodigoproducto(gui.getString("codigoproducto"));
                        consulGuia.setCodigomedida(gui.getString("codigomedida"));
                        consulGuia.setMedida(gui.getString("medida"));
                        consulGuia.setProducto(gui.getString("producto"));
                        consulGuia.setVolumenentregado(gui.getBigDecimal("volumenentregado"));
                        consulGuia.setAutotanque(gui.getString("autotanque"));
                        consulGuia.setEstado(gui.getString("estado"));
                        consulGuia.setActivo(gui.getBoolean("activo"));
                        consulGuia.setUsuarioactual(gui.getString("usuarioactual"));
                        if (!gui.isNull("numerosri")) {
                            consulGuia.setNumerosri(gui.getString("numerosri"));
                        }
                        if (!gui.isNull("cedulaconductor")) {
                            consulGuia.setCedulaconductor(gui.getString("cedulaconductor"));
                        }
                        if (!gui.isNull("nombreconductor")) {
                            consulGuia.setNombreconductor(gui.getString("nombreconductor"));
                        }
                        if (!gui.isNull("observacion")) {
                            consulGuia.setObservacion(gui.getString("observacion"));
                        }
                        if (!gui.isNull("codigocliente")) {
                            consulGuia.setCodigocliente(gui.getString("codigocliente"));
                        }
                        if (!gui.isNull("compartimento1")) {
                            consulGuia.setCompartimento1(gui.getInt("compartimento1"));
                        }
                        if (!gui.isNull("compartimento2")) {
                            consulGuia.setCompartimento2(gui.getInt("compartimento2"));
                        }
                        if (!gui.isNull("compartimento3")) {
                            consulGuia.setCompartimento3(gui.getInt("compartimento3"));
                        }
                        if (!gui.isNull("compartimento4")) {
                            consulGuia.setCompartimento4(gui.getInt("compartimento4"));
                        }
                        if (!gui.isNull("compartimento5")) {
                            consulGuia.setCompartimento5(gui.getInt("compartimento5"));
                        }
                        if (!gui.isNull("compartimento6")) {
                            consulGuia.setCompartimento6(gui.getInt("compartimento6"));
                        }
                        if (!gui.isNull("selloinicial")) {
                            consulGuia.setSelloinicial((gui.getInt("selloinicial")));
                        }
                        if (!gui.isNull("sellofinal")) {
                            consulGuia.setSellofinal((gui.getInt("sellofinal")));
                        }
                        if (!gui.isNull("numerofactura")) {
                            consulGuia.setNumerofactura(gui.getString("numerofactura"));
                        }

                        if (!gui.isNull("fechafactura")) {
                            Long lDateFechaFac = gui.getLong("fechafactura");
                            Date dateFechaFac = new Date(lDateFechaFac);
                            consulGuia.setFechafactura(dateFechaFac);
                        }

                        if (!gui.isNull("horaautorizacion")) {
                            consulGuia.setHoraautorizacion(gui.getString("horaautorizacion"));
                        }

                        listaConsultaGuia.add(consulGuia);
                        consulGuia = new Consultaguiaremision();
                        consulGuiaPK = new ConsultaguiaremisionPK();
//                    if (!precPK.isNull("fechafin")) {
//                        Long lDateFin = precPK.getLong("fechafin");
//                        Date dateFin = new Date(lDateFin);
//                        precio.setFechafin(dateFin);
//                    } else {
//                        precioPK.setFechainicio(new Date());
//                    }
//                    precio.setActivo(prec.getBoolean("activo"));
//                    if (precio.getActivo()) {
////                    precio.setActivoS("S");
//                    } else {
////                   precio.setActivoS("N");
//                    }                                      
                    }

                }
                if (connection.getResponseCode() != 200) {
                    System.out.println(connection.getResponseCode());
                    System.out.println(connection.getResponseMessage());
                }
            }
        } catch (IOException e) {
            this.dialogo(FacesMessage.SEVERITY_INFO, "NO SE ENCONTRARON REGISTROS");
            e.printStackTrace();
        }
    }

    public void obtenerGuiaCli() throws ParseException {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            numGuia = params.get("form:numGuia");
            DateFormat date = new SimpleDateFormat("yyyyMMdd");
            if (!(numGuia.equals("") && fechaf == null)) {
                String fechaD = "";
                String tipoConsulta = "3";
                if (!numGuia.equals("") && fechaf != null) {
                    tipoConsulta = "3";
                    fechaD = date.format(fechaf);
                } else if (numGuia.equals("")) {
                    tipoConsulta = "2";
                    fechaD = date.format(fechaf);
                } else if (fechaf == null) {
                    tipoConsulta = "1";
                }
                if (cliente != null) {
                    url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.consultaguiaremision/paracliente?codigocomercializadora=" + codigoComer
                            + "&tipoconsulta=" + tipoConsulta + "&codigocliente=" + cliente.getCodigo() + "&numero=" + numGuia + "&fecha=" + fechaD);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");

                    listaConsultaGuia = new ArrayList<>();

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
                        if (!retorno.isNull(indice)) {
                            JSONObject gui = retorno.getJSONObject(indice);
                            consGuiaPK = gui.getJSONObject("consultaguiaremisionPK");
                            consulGuiaPK.setCodigocomercializadora(consGuiaPK.getString("codigocomercializadora"));
                            consulGuiaPK.setNumero(consGuiaPK.getString("numero"));
                            consulGuiaPK.setFecha(consGuiaPK.getString("fecha"));
                            Long lDateRec = consGuiaPK.getLong("fecharecepcion");
                            Date dateRec = new Date(lDateRec);
                            consulGuiaPK.setFecharecepcion(dateRec);
                            consulGuia.setConsultaguiaremisionPK(consulGuiaPK);
                            consulGuia.setCodigoterminal(gui.getString("codigoterminal"));
                            consulGuia.setNumerooe(gui.getString("numerooe"));
                            consulGuia.setCodigoareamercadeo(gui.getString("codigoareamercadeo"));
                            consulGuia.setCodigoproducto(gui.getString("codigoproducto"));
                            consulGuia.setCodigomedida(gui.getString("codigomedida"));
                            consulGuia.setMedida(gui.getString("medida"));
                            consulGuia.setProducto(gui.getString("producto"));
                            consulGuia.setVolumenentregado(gui.getBigDecimal("volumenentregado"));
                            consulGuia.setAutotanque(gui.getString("autotanque"));
                            consulGuia.setEstado(gui.getString("estado"));
                            consulGuia.setActivo(gui.getBoolean("activo"));
                            consulGuia.setUsuarioactual(gui.getString("usuarioactual"));
                            if (!gui.isNull("numerosri")) {
                                consulGuia.setNumerosri(gui.getString("numerosri"));
                            }
                            if (!gui.isNull("cedulaconductor")) {
                                consulGuia.setCedulaconductor(gui.getString("cedulaconductor"));
                            }
                            if (!gui.isNull("nombreconductor")) {
                                consulGuia.setNombreconductor(gui.getString("nombreconductor"));
                            }
                            if (!gui.isNull("observacion")) {
                                consulGuia.setObservacion(gui.getString("observacion"));
                            }
                            if (!gui.isNull("codigocliente")) {
                                consulGuia.setCodigocliente(gui.getString("codigocliente"));
                            }
                            if (!gui.isNull("compartimento1")) {
                                consulGuia.setCompartimento1(gui.getInt("compartimento1"));
                            }
                            if (!gui.isNull("compartimento2")) {
                                consulGuia.setCompartimento2(gui.getInt("compartimento2"));
                            }
                            if (!gui.isNull("compartimento3")) {
                                consulGuia.setCompartimento3(gui.getInt("compartimento3"));
                            }
                            if (!gui.isNull("compartimento4")) {
                                consulGuia.setCompartimento4(gui.getInt("compartimento4"));
                            }
                            if (!gui.isNull("compartimento5")) {
                                consulGuia.setCompartimento5(gui.getInt("compartimento5"));
                            }
                            if (!gui.isNull("compartimento6")) {
                                consulGuia.setCompartimento6(gui.getInt("compartimento6"));
                            }
                            if (!gui.isNull("selloinicial")) {
                                consulGuia.setSelloinicial((gui.getInt("selloinicial")));
                            }
                            if (!gui.isNull("sellofinal")) {
                                consulGuia.setSellofinal((gui.getInt("sellofinal")));
                            }
                            if (!gui.isNull("numerofactura")) {
                                consulGuia.setNumerofactura(gui.getString("numerofactura"));
                            }

                            if (!gui.isNull("fechafactura")) {
                                Long lDateFechaFac = gui.getLong("fechafactura");
                                Date dateFechaFac = new Date(lDateFechaFac);
                                consulGuia.setFechafactura(dateFechaFac);
                            }

                            if (!gui.isNull("horaautorizacion")) {
                                consulGuia.setHoraautorizacion(gui.getString("horaautorizacion"));
                            }

                            listaConsultaGuia.add(consulGuia);
                            consulGuia = new Consultaguiaremision();
                            consulGuiaPK = new ConsultaguiaremisionPK();
                        }

                    }
                    if (connection.getResponseCode() != 200) {
                        System.out.println(connection.getResponseCode());
                        System.out.println(connection.getResponseMessage());
                    }
                } else {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "Seleccione un cliente");
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "Complete un campo para la busqueda");
            }
        } catch (IOException e) {
            this.dialogo(FacesMessage.SEVERITY_INFO, "NO SE ENCONTRARON REGISTROS");
            e.printStackTrace();
        }
    }

    public void generarReporte(Consultaguiaremision guia) {
//        String path = "C:\\Users\\HOME\\Desktop\\Infinity\\Template\\Template\\guiaremision.jrxml";

        String path = Fichero.getCARPETAREPORTES() + "/guiaremision.jrxml";
        System.out.println("PATH:" + path);
        InputStream file = null;
        try {
            file = new FileInputStream(new File(path));

            JasperReport reporte = JasperCompileManager.compileReport(file);

            Map parametro = new HashMap();
            BufferedImage image = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/logopetroecuador.jpeg"));
            BufferedImage imageBar = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/barras.jpeg"));
//            BufferedImage image = ImageIO.read(new File("C:\\archivos\\Template\\logo.jpg"));
//            BufferedImage imageBar = ImageIO.read(new File("C:\\archivos\\Template\\barras.jpg"));

            parametro.put("numerogr", guia.getConsultaguiaremisionPK().getNumero());
            parametro.put("logo", image);
            parametro.put("barras", imageBar);

            //actual local
            Connection conexion = conexionJasperBD();

            JasperPrint print = JasperFillManager.fillReport(reporte, parametro, conexion);

            File directory = new File(Fichero.getCARPETAREPORTES());
//            File directory = new File("C:\\archivos");

            String nombreDocumento = "reporteGuiaRemision";

            File pdf = File.createTempFile(nombreDocumento + "_", ".pdf", directory);
            JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
            File initialFile = new File(pdf.getAbsolutePath());
            InputStream targetStream = new FileInputStream(initialFile);
            pdfStream = new DefaultStreamedContent(targetStream, "application/pdf", nombreDocumento + guia.getConsultaguiaremisionPK().getNumero() + ".pdf");
            PrimeFaces.current().executeScript("window.open(" + directory + ",'" + nombreDocumento + "','fullscreen=yes');parent.opener=top;");
            System.err.print(pdf.getAbsolutePath());
            System.out.println(pdf.getAbsolutePath());
        } catch (Exception ex) {
            System.out.println("Excepcion: " + ex);
        }
    }

    public void save() throws ParseException {
        if (editarPrecio) {
            editItems();
        }
    }

    public Boolean addItems(int i) {
        try {
            String respuesta;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            String dateI = sdf.format(new Date());
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();

            objPK.put("codigocomercializadora", listaConsultaGuiaArchivoSubida.get(i).getConsultaguiaremisionPK().getCodigocomercializadora());
            objPK.put("numero", listaConsultaGuiaArchivoSubida.get(i).getConsultaguiaremisionPK().getNumero());
            objPK.put("fecha", listaConsultaGuiaArchivoSubida.get(i).getConsultaguiaremisionPK().getFecha());
            objPK.put("fecharecepcion", dateI);

            obj.put("consultaguiaremisionPK", objPK);
            obj.put("codigoterminal", listaConsultaGuiaArchivoSubida.get(i).getCodigoterminal());
            obj.put("numerooe", listaConsultaGuiaArchivoSubida.get(i).getNumerooe());
            obj.put("codigoareamercadeo", listaConsultaGuiaArchivoSubida.get(i).getCodigoareamercadeo());
            obj.put("codigoproducto", listaConsultaGuiaArchivoSubida.get(i).getCodigoproducto());
            obj.put("codigomedida", listaConsultaGuiaArchivoSubida.get(i).getCodigomedida());
            obj.put("medida", listaConsultaGuiaArchivoSubida.get(i).getMedida());
            obj.put("producto", listaConsultaGuiaArchivoSubida.get(i).getProducto());
            obj.put("volumenentregado", listaConsultaGuiaArchivoSubida.get(i).getVolumenentregado());
            obj.put("autotanque", listaConsultaGuiaArchivoSubida.get(i).getAutotanque());
            obj.put("estado", listaConsultaGuiaArchivoSubida.get(i).getEstado());
            obj.put("activo", listaConsultaGuiaArchivoSubida.get(i).getActivo());
            obj.put("usuarioactual", listaConsultaGuiaArchivoSubida.get(i).getUsuarioactual());

            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "GUIA REGISTRADA EXITOSAMENTE");
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

    public void editItems() throws ParseException {
        try {
            String respuesta;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            String dateI = sdf.format(consulGuia.getConsultaguiaremisionPK().getFecharecepcion());
            url = new URL(direccion + "/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();

            objPK.put("codigocomercializadora", consulGuia.getConsultaguiaremisionPK().getCodigocomercializadora());
            objPK.put("numero", consulGuia.getConsultaguiaremisionPK().getNumero());
            objPK.put("fecha", consulGuia.getConsultaguiaremisionPK().getFecha());
            objPK.put("fecharecepcion", dateI);

            obj.put("consultaguiaremisionPK", objPK);
            obj.put("codigoterminal", consulGuia.getCodigoterminal());
            obj.put("numerooe", consulGuia.getNumerooe());
            obj.put("codigoareamercadeo", consulGuia.getCodigoareamercadeo());
            obj.put("codigoproducto", consulGuia.getCodigoproducto());
            obj.put("codigomedida", consulGuia.getCodigomedida());
            obj.put("medida", consulGuia.getMedida());
            obj.put("producto", consulGuia.getProducto());
            obj.put("volumenentregado", consulGuia.getVolumenentregado());
            obj.put("autotanque", consulGuia.getAutotanque());
            obj.put("estado", consulGuia.getEstado());
            obj.put("activo", consulGuia.getActivo());
            obj.put("usuarioactual", consulGuia.getUsuarioactual());
            obj.put("numerosri", consulGuia.getNumerosri());
            obj.put("cedulaconductor", consulGuia.getCedulaconductor());
            obj.put("nombreconductor", consulGuia.getNombreconductor());
            obj.put("observacion", consulGuia.getObservacion());
            obj.put("codigocliente", consulGuia.getCodigocliente());
            obj.put("compartimento1", consulGuia.getCompartimento1());
            obj.put("compartimento2", consulGuia.getCompartimento2());
            obj.put("compartimento3", consulGuia.getCompartimento3());
            obj.put("compartimento4", consulGuia.getCompartimento4());
            obj.put("compartimento5", consulGuia.getCompartimento5());
            obj.put("compartimento6", consulGuia.getCompartimento6());
            obj.put("selloinicial", consulGuia.getSelloinicial());
            obj.put("sellofinal", consulGuia.getSellofinal());
//            obj.put("cantidadsellos", consulGuia.getSellofinal() - consulGuia.getSelloinicial());
            obj.put("numerofactura", consulGuia.getNumerofactura());
            obj.put("horaautorizacion", consulGuia.getHoraautorizacion());
            obj.put("fechafactura", sdf.format(consulGuia.getFechafactura()));
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "GUIA ACUTALIZADA EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                obtenerGuia();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteItems() {
        try {
            String respuesta;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd%20hh:mm:ss.SSS");
            String fechaRecep = sdf.format(consulGuia.getConsultaguiaremisionPK().getFecharecepcion());
            url = new URL(direccion + "/porId?codigocomercializadora=" + consulGuia.getConsultaguiaremisionPK().getCodigocomercializadora() + "&numero=" + consulGuia.getConsultaguiaremisionPK().getNumero() + "&fecha=" + consulGuia.getConsultaguiaremisionPK().getFecha() + "&fecharecepcion=" + fechaRecep);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");

            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "GUIA ELIMINADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoBanco() {
        estadoBanco = true;
        editarPrecio = false;
        consulGuia = new Consultaguiaremision();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Consultaguiaremision editarGuia(Consultaguiaremision obj) {
        editarPrecio = true;
        consulGuia = obj;
        soloLectura = false;
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        listaConsultaGuiaAux = new ArrayList<>();
        listaConsultaGuiaAux.add(consulGuia);
        return consulGuia;
    }

    public Consultaguiaremision lecturaDatos(Consultaguiaremision obj) {
        editarPrecio = true;
        consulGuia = obj;
        soloLectura = true;
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        listaConsultaGuiaAux = new ArrayList<>();
        listaConsultaGuiaAux.add(consulGuia);
        return consulGuia;
    }

    public void seleccionarComer() {
        if (comercializadora != null) {
            codigoComer = comercializadora.getCodigo();
            listaClientes = new ArrayList<>();
            listaClientes = cliServicio.obtenerClientesPorComercializadora(codigoComer);
        }
    }

    public void actualizarLista() {
//        if (precioBean != null) {
//            listaPrecios = new ArrayList<>();
//            //obtenerPrecio(comercializadora.getCodigo(), true);
//            obtenerPrecios(comercializadora.getCodigo());
//        }
    }

    public void soloVigentes() {
//        listaPrecioAuxiliar = new ArrayList<>();
//        if (!listaPrecios.isEmpty()) {
//            if (soloVigente) {
//                for (int i = 0; i < listaPrecios.size(); i++) {
//                    if (listaPrecios.get(i).getActivo() == true) {
//                        listaPrecioAuxiliar.add(listaPrecios.get(i));
//                    }
//                }
//                listaPrecios = listaPrecioAuxiliar;
//            } else {
//                //obtenerPrecio(comercializadora.getCodigo(), true);
//                obtenerPrecios(comercializadora.getCodigo());
//            }
//        } else {
//            soloVigente = false;
//            this.dialogo(FacesMessage.SEVERITY_ERROR, "PARA PODER VISUALIZAR LOS PRECIOS SOLO VIGENTES, PRIMERO REALIZAR UNA BÚSQUEDA CON REGISTROS");
//        }
    }

    public void seleccionarTerminal() {
        if (terminal != null) {
            codTerminal = terminal.getCodigo();
            consulGuia.setCodigoterminal(codTerminal);
        }
    }

    public void seleccionarComercializdora() {
        if (comercializadora != null) {
            if (comercializadora.getActivo().equals("S")) {
                consulGuiaPK.setCodigocomercializadora(comercializadora.getCodigo());
                consulGuia.setConsultaguiaremisionPK(consulGuiaPK);
                codigoComer = comercializadora.getCodigo();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA SE ENCUENTRA INACTIVA");
            }
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws Throwable {
//        System.out.println("FT:: handleFileUpload handleFileUpload VERIFICAR tamaño de cada archivo. " + event.getFile().getInputStream().available());
//        System.out.println("FT:: handleFileUpload handleFileUpload VERIFICAR contadorArchivos:. " + contadorArchivos + " -contadorArchivosOk:. "+contadorArchivosOk); 

        if (procesoNuevo) {
//            System.out.println("FT:: procesoNuevoDEBE ESTAR TRUE. " + procesoNuevo);
            procesoNuevo = false;
        } else {
//            System.out.println("FT:: procesoNuevoDEBE ESTAR FALSE. " + procesoNuevo);
//            contadorArchivosOk = 0;
//            inputStream = new ArrayList<>();
//            list = new ArrayList<>();
//            contadorArchivosMal++;
//            guiaVacia = "";
            //iniciarVariables();
            //return;
        }
        if (contadorArchivosMal == 0) {
            if (event.getFile().getInputStream().available() > 0) {
                inputStream.add(event.getFile().getInputStream());
                list.add(event.getFile());
                contadorArchivosOk++;
            } else {
                guiaVacia = event.getFile().getFileName();
//            manejoArchivos = false;
//            inputStream = new ArrayList<>();
//            list = new ArrayList<>();
                contadorArchivosMal++;
//            FacesMessage message = new FacesMessage("¡ ERROR !", guiaVacia + " Está vácio! NINGÚN ARCHIVO SERÁ SUBIDO, Elimine este archivo del grupo y repita el proceso");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//            iniciarVariables();
//                System.out.println("FT: ELSE DEL TAMAÑO.HAY UN ARCHIVO VACIO.. SE VA A RETURN");
                return;

//                FacesMessage message = new FacesMessage("ERROR", event.getFile().getFileName() + " Está vácio! NINGÚN ARCHIVO SERÁ SUBIDO, Elimine este archivo del grupo y repita el proceso");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//                throw new Throwable("ERROR: " + event.getFile().getFileName() + " Está vácio! NINGÚN ARCHIVO SERÁ SUBIDO, Elimine este archivo del grupo y repita el proceso");
            }
        } else {
//            System.out.println("FT: ELSE DE CONTROL INICIAL CONTADORARCHIVOS MAL--- YA NO SE PROCESAN ARCHIVOS");
            FacesMessage message = new FacesMessage("¡ERROR!", guiaVacia + " Está vácio! Haga clic en el botón Cargar Archivos para RE INICIAR EL PROCESO");
            FacesContext.getCurrentInstance().addMessage(null, message);
//            iniciarVariables();
//            contadorArchivosMal++;
            return;
        }

        if (contadorArchivosMal > 0) {
//            System.out.println("FT:: handleFileUpload contadorArchivosMal > 0 VERIFICAR tamaño de cada archivo. " + event.getFile().getInputStream().available() + " - contadorArchivosOK:. " + contadorArchivosOk + " -contadorArchivosMAL:. " + contadorArchivosMal);
            FacesMessage message = new FacesMessage("ERROR", guiaVacia + " Está vácio! NINGÚN ARCHIVO SERÁ SUBIDO, Elimine este archivo del grupo y repita el proceso");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
//                init();
        } else {
//                FacesMessage message = new FacesMessage("ERROR", event.getFile().getFileName() + " Está vácio! NINGÚN ARCHIVO SERÁ SUBIDO, Elimine este archivo del grupo y repita el proceso");
//            System.out.println("FT:: handleFileUpload ELSE contadorArchivosMal > 0 VERIFICAR tamaño de cada archivo. " + event.getFile().getInputStream().available() + " - contadorArchivosOK:. " + contadorArchivosOk + " -contadorArchivosMAL:. " + contadorArchivosMal);
            FacesMessage message = new FacesMessage("PROCESO COMPLETO! Se han Leído y verificado todos los archivos. Ahora puede hacer clic en el botón Cargar Archivos");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

//            FacesMessage message = new FacesMessage("PROCESO COMPLETO! Se han subidos " + contadorArchivosOk +" archivos. Ahora puede hacer clic en el botón Cargar Archivos");
//            FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void lecturaXml1() throws ParserConfigurationException, FileNotFoundException, IOException, SAXException, ParseException {

        listaConsultaGuiaArchivoSubida = new ArrayList<>();
        codigoComer = comercializadora.getCodigo();
        codTerminal = terminal.getCodigo();
        String ruta_temporal = Fichero.getCARPETAREPORTES();
        String hora = "";
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String path = "";
        try{
        if (contadorArchivosOk > 0 && contadorArchivosMal == 0) {
            FacesMessage message = new FacesMessage("Se Intentará cargar a InfinityOne " + contadorArchivosOk + " archivos. con información");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (codigoComer != null || codTerminal != null) {
                for (int i = 0; i < list.size(); i++) { 
                    String fileName = list.get(i).getFileName();
                    OutputStream outputStream = null;
                    path = ruta_temporal + ("/") + fileName.replace(" ", "");

                    System.out.println("FT::.PROCESANDO ARCHIVOS DE GUIAS DE REMISION. Terminal: "+codTerminal + "Archivo a leer:. "+path);
                    
                    File file = new File(path);

                    outputStream = new FileOutputStream(file);

                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while ((read = inputStream.get(i).read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }

                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document document = db.parse(file);
                    Document doc = null;
                    Document docF = null;
                    document.getDocumentElement().normalize();
//                    System.out.println("Root Element :" + document.getDocumentElement().getNodeName());
                    NodeList nListF = document.getElementsByTagName("fechaAutorizacion");
                    for (int temp = 0; temp < nListF.getLength(); temp++) {
                        Node nNode = nListF.item(temp);
                        System.out.println("\nCurrent Element :" + nNode.getFirstChild().getTextContent());
                        hora = nNode.getFirstChild().getTextContent();
                    }
                    consulGuia.setFechaautorizacion(hora);
                    consulGuia.setHoraautorizacion((hora).substring(11));
                    NodeList nList = document.getElementsByTagName("comprobante");
//                    System.out.println("----------------------------");
                    for (int temp = 0; temp < nList.getLength(); temp++) {
                        Node nNode = nList.item(temp);
//                        System.out.println("\nCurrent Element :" + nNode.getNodeName());
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            doc = convertStringToXMLDocument(eElement.getFirstChild().getTextContent());
                        }
                    }
                    NodeList nList1 = doc.getChildNodes();
                    for (int temp = 0; temp < nList1.getLength(); temp++) {
                        Node nNode = nList1.item(temp);
//                        System.out.println("\nCurrent Element :" + nNode.getNodeName());
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            consulGuiaPK.setCodigocomercializadora(codigoComer);
                            if ((eElement.getElementsByTagName("fechaIniTransporte").item(0)== null)){
                            throw new Throwable("FT::. ERROR GENERADO AL PROCESAR AL ARCHIVO. "+path+" BUSCANDO EL TAG fechaIniTransporte:. AL PARECER NO EXISTE EN ESTE ARCHIVO. NO SE PUEDE PROCESAR ESTE ARCHIVO! ");
                            }
                            String dia = (eElement.getElementsByTagName("fechaIniTransporte").item(0).getTextContent()).replace("/", "").substring(0, 2);
                            String mes = (eElement.getElementsByTagName("fechaIniTransporte").item(0).getTextContent()).replace("/", "").substring(2, 4);
                            String anio = (eElement.getElementsByTagName("fechaIniTransporte").item(0).getTextContent()).replace("/", "").substring(4);
                            consulGuiaPK.setFecha(anio + mes + dia);
                            //<campoAdicional nombre="CodigoControlDeposito">0208164802-TERMINAL EL BEATERIO</campoAdicional> -- 8 caracteres
                            consulGuiaPK.setNumero((eElement.getElementsByTagName("campoAdicional").item(5).getTextContent()).substring(0, 8));

                            consulGuia.setConsultaguiaremisionPK(consulGuiaPK);
                            consulGuia.setNumeroautorizacion(eElement.getElementsByTagName("claveAcceso").item(0).getTextContent());
                            //consulGuia.setFechaautorizacion(eElement.getElementsByTagName("fechaAutorizacion").item(0).getTextContent());
                            consulGuia.setDirestablecimiento(eElement.getElementsByTagName("dirEstablecimiento").item(0).getTextContent());

                            //<campoAdicional nombre="PedidoFacturaFecha">0200011200102000000009610/07/2022</campoAdicional> -- 8 caracteres
                            consulGuia.setNumerooe((eElement.getElementsByTagName("campoAdicional").item(4).getTextContent()).substring(0, 8));
                            //dos primeros digitos codigoInterno                
                            consulGuia.setCodigoareamercadeo((eElement.getElementsByTagName("codigoInterno").item(0).getTextContent()).substring(0, 2));
                            consulGuia.setCodigoproducto(eElement.getElementsByTagName("codigoInterno").item(0).getTextContent());
                            //<detAdicional nombre="Unidad de Medida" valor="GALS"/> valor                                                    
                            int j;
                            for (j = 0; j < eElement.getElementsByTagName("campoAdicional").item(6).getTextContent().length(); j++) {
                                if (eElement.getElementsByTagName("campoAdicional").item(6).getTextContent().charAt(j) == '-') {
                                    break;
                                }
                            }
                            consulGuia.setCodigomedida((eElement.getElementsByTagName("campoAdicional").item(6).getTextContent()).substring(j + 1));
                            consulGuia.setMedida(consulGuia.getCodigomedida());
                            //<campoAdicional nombre="CodigoControlDeposito">0208164802-TERMINAL EL BEATERIO</campoAdicional> -- 2 caracteres despues de numero
                            consulGuia.setCodigoterminal((eElement.getElementsByTagName("campoAdicional").item(5).getTextContent()).substring(0, 2));
                            //<detAdicional nombre="Unidad de Medida" valor="GALS"/> valor

                            consulGuia.setProducto(eElement.getElementsByTagName("descripcion").item(0).getTextContent());
                            consulGuia.setVolumenentregado(new BigDecimal(eElement.getElementsByTagName("cantidad").item(0).getTextContent()));
                            consulGuia.setAutotanque(eElement.getElementsByTagName("placa").item(0).getTextContent());
                            consulGuia.setEstado("ACT");
                            consulGuia.setActivo(true);
                            consulGuia.setNumerosri(eElement.getElementsByTagName("estab").item(0).getTextContent()
                                    + eElement.getElementsByTagName("ptoEmi").item(0).getTextContent()
                                    + eElement.getElementsByTagName("secuencial").item(0).getTextContent());
                            //Pedidofacturafecha
                            String fechaFact = (eElement.getElementsByTagName("campoAdicional").item(4).getTextContent()).substring(23);
                            consulGuia.setFechafactura(formato.parse(fechaFact));
                            //codigocliente <campoAdicional nombre="Codigo Cliente">02010677  ESTACION DE SERVICIO SANTA ANA</campoAdicional> -- 8primeros
                            consulGuia.setCodigocliente((eElement.getElementsByTagName("campoAdicional").item(1).getTextContent()).substring(0, 8));
                            consulGuia.setCompartimento1(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(7).getTextContent()));
                            consulGuia.setCompartimento2(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(8).getTextContent()));
                            consulGuia.setCompartimento3(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(9).getTextContent()));
                            consulGuia.setCompartimento4(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(10).getTextContent()));
                            consulGuia.setCompartimento5(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(11).getTextContent()));
                            consulGuia.setCompartimento6(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(12).getTextContent()));
                            //numero factura <campoAdicional nombre="PedidoFacturaFecha">0200011200102000000009610/07/2022</campoAdicional> -- 15 caracteres despues de numero oe
                            consulGuia.setNumerofactura(eElement.getElementsByTagName("campoAdicional").item(4).getTextContent().substring(8, 23));
                            consulGuia.setCedulaconductor(eElement.getElementsByTagName("campoAdicional").item(0).getTextContent().substring(0, 10));
                            consulGuia.setNombreconductor((eElement.getElementsByTagName("campoAdicional").item(0).getTextContent().trim()).substring(11));
                            consulGuia.setUsuarioactual(dataUser.getUser().getNombrever());

                            listaConsultaGuiaArchivoSubida.add(consulGuia);
                            consulGuia = new Consultaguiaremision();
                            consulGuiaPK = new ConsultaguiaremisionPK();
                        }
                    }
                }
//                list = new ArrayList<>();
//                inputStream = new ArrayList<>();
 
                iniciarVariables();
                FacesMessage message1 = new FacesMessage("HA CONCLUIDO la carga de archivos a InfinityOne CORRECTAMENTE!");
                FacesContext.getCurrentInstance().addMessage(null, message1);

            } else {
                this.dialogo(FacesMessage.SEVERITY_WARN, "Seleccione una comercializadora y un terminal para poder cargar el archivo");
            }// aqui
        } else {
//            inputStream = new ArrayList<>();
//            list = new ArrayList<>();
            iniciarVariables();
            FacesMessage message = new FacesMessage("¡ERROR!", "UN archivo Está vácio! NINGÚN ARCHIVO SERÁ SUBIDO, Elimine este archivo del grupo y repita el proceso");
            FacesContext.getCurrentInstance().addMessage(null, message);
//            this.dialogo(FacesMessage.SEVERITY_WARN, "El proceso debe ser re iniciado. Uno de los archivos estuvo vacío!");
        }
        }catch (Throwable t){
            
            System.out.println("FT::.INICIO DE CAPTURA ERROR AL ESTAR PROCESANDO ARCHIVOS DE GUIAS DE REMISION. Terminal: "+codTerminal + "ARCHIVO CON ERROR:. "+path+ "EXCEPCION CAPTURADA:. " + t);
            t.printStackTrace(System.out);
            System.out.println("FT::.FIN DE CAPTURA ERROR AL ESTAR PROCESANDO ARCHIVOS DE GUIAS DE REMISION. Terminal: "+codTerminal + "ARCHIVO CON ERROR:. "+path+ "EXCEPCION CAPTURADA:. " + t);
            FacesMessage message2 = new FacesMessage("ERROR AL ESTAR PROCESANDO ARCHIVOS DE GUIAS DE REMISION. Terminal: "+codTerminal + "ARCHIVO CON ERROR:. "+path+ "EXCEPCION CAPTURADA:. " + t);
            FacesContext.getCurrentInstance().addMessage(null, message2);
         //   regresar();
        }
    }

    public void iniciarVariables() {
        System.out.println("FT: HA ENTRADO en iniciarVariables");
        contadorArchivosOk = 0;
        inputStream = new ArrayList<>();
        list = new ArrayList<>();
        contadorArchivosMal = 0;
        guiaVacia = "";
        procesoNuevo = true;

    }

    public void handleFilesUploads2(FileUploadEvent e) throws ParserConfigurationException, FileNotFoundException, IOException, SAXException {
        // Get uploaded file from the FileUploadEvent
        listaConsultaGuiaArchivoSubida = new ArrayList<>();
        if (codigoComer != null && codTerminal != null) {
            String ruta_temporal = Fichero.getCARPETAREPORTES();
            this.file = e.getFile();

            String name = file.getFileName();
// Print out the information of the file
            System.out.println("Uploaded File Name Is :: " + file.getFileName() + " :: Uploaded File Size :: " + file.getSize());

            InputStream inputStream = file.getInputStream();
            OutputStream outputStream = null;
            String path = ruta_temporal + name.replace(" ", "");

            File file = new File(path + name);

            outputStream = new FileOutputStream(file);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            Document doc = null;
            document.getDocumentElement().normalize();
            System.out.println("Root Element :" + document.getDocumentElement().getNodeName());
            NodeList nList = document.getElementsByTagName("comprobante");
            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    doc = convertStringToXMLDocument(eElement.getFirstChild().getTextContent());
                }
            }
            NodeList nList1 = doc.getChildNodes();
            for (int temp = 0; temp < nList1.getLength(); temp++) {
                Node nNode = nList1.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    consulGuiaPK.setCodigocomercializadora(codigoComer);
                    String dia = (eElement.getElementsByTagName("fechaIniTransporte").item(0).getTextContent()).replace("/", "").substring(0, 2);
                    String mes = (eElement.getElementsByTagName("fechaIniTransporte").item(0).getTextContent()).replace("/", "").substring(2, 4);
                    String anio = (eElement.getElementsByTagName("fechaIniTransporte").item(0).getTextContent()).replace("/", "").substring(4);
                    consulGuiaPK.setFecha(anio + mes + dia);
                    //<campoAdicional nombre="CodigoControlDeposito">0208164802-TERMINAL EL BEATERIO</campoAdicional> -- 8 caracteres
                    consulGuiaPK.setNumero((eElement.getElementsByTagName("campoAdicional").item(5).getTextContent()).substring(0, 8));

                    consulGuia.setConsultaguiaremisionPK(consulGuiaPK);
                    //<campoAdicional nombre="PedidoFacturaFecha">0200011200102000000009610/07/2022</campoAdicional> -- 8 caracteres
                    consulGuia.setNumerooe((eElement.getElementsByTagName("campoAdicional").item(4).getTextContent()).substring(0, 8));
                    //dos primeros digitos codigoInterno                
                    consulGuia.setCodigoareamercadeo((eElement.getElementsByTagName("codigoInterno").item(0).getTextContent()).substring(0, 2));
                    consulGuia.setCodigoproducto(eElement.getElementsByTagName("codigoInterno").item(0).getTextContent());
                    //<detAdicional nombre="Unidad de Medida" valor="GALS"/> valor                            
                    int i;
                    for (i = 0; i < eElement.getElementsByTagName("campoAdicional").item(6).getTextContent().length(); i++) {
                        if (eElement.getElementsByTagName("campoAdicional").item(6).getTextContent().charAt(i) == '-') {
                            break;
                        }
                    }
                    consulGuia.setCodigomedida((eElement.getElementsByTagName("campoAdicional").item(6).getTextContent()).substring(i + 1));
                    consulGuia.setMedida(consulGuia.getCodigomedida());
                    //<campoAdicional nombre="CodigoControlDeposito">0208164802-TERMINAL EL BEATERIO</campoAdicional> -- 2 caracteres despues de numero
                    consulGuia.setCodigoterminal((eElement.getElementsByTagName("campoAdicional").item(5).getTextContent()).substring(0, 2));
                    //<detAdicional nombre="Unidad de Medida" valor="GALS"/> valor

                    consulGuia.setProducto(eElement.getElementsByTagName("descripcion").item(0).getTextContent());
                    consulGuia.setVolumenentregado(new BigDecimal(eElement.getElementsByTagName("cantidad").item(0).getTextContent()));
                    consulGuia.setAutotanque(eElement.getElementsByTagName("placa").item(0).getTextContent());
                    consulGuia.setEstado("ACT");
                    consulGuia.setActivo(true);
                    consulGuia.setNumerosri(eElement.getElementsByTagName("estab").item(0).getTextContent()
                            + eElement.getElementsByTagName("ptoEmi").item(0).getTextContent()
                            + eElement.getElementsByTagName("secuencial").item(0).getTextContent());
                    //codigocliente <campoAdicional nombre="Codigo Cliente">02010677  ESTACION DE SERVICIO SANTA ANA</campoAdicional> -- 8primeros
                    consulGuia.setCodigocliente((eElement.getElementsByTagName("campoAdicional").item(1).getTextContent()).substring(0, 8));
                    consulGuia.setCompartimento1(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(7).getTextContent()));
                    consulGuia.setCompartimento2(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(8).getTextContent()));
                    consulGuia.setCompartimento3(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(9).getTextContent()));
                    consulGuia.setCompartimento4(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(10).getTextContent()));
                    consulGuia.setCompartimento5(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(11).getTextContent()));
                    consulGuia.setCompartimento6(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(12).getTextContent()));
                    //numero factura <campoAdicional nombre="PedidoFacturaFecha">0200011200102000000009610/07/2022</campoAdicional> -- 15 caracteres despues de numero oe
                    consulGuia.setNumerofactura(eElement.getElementsByTagName("campoAdicional").item(4).getTextContent().substring(8, 23));
                    consulGuia.setCedulaconductor(eElement.getElementsByTagName("campoAdicional").item(0).getTextContent().substring(0, 10));
                    consulGuia.setNombreconductor((eElement.getElementsByTagName("campoAdicional").item(0).getTextContent().trim()).substring(11));
                    consulGuia.setUsuarioactual(dataUser.getUser().getNombrever());

                    listaConsultaGuiaArchivoSubida.add(consulGuia);
                    consulGuia = new Consultaguiaremision();
                    consulGuiaPK = new ConsultaguiaremisionPK();
                }
            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_WARN, "Seleccione una comercializadora y un terminal para poder cargar el archivo");
        }
    }

    public void guardar() throws ParseException {
        boolean bandera = false;
        List<JSONObject> arregloJSON = new ArrayList<>();
        for (int i = 0; i < listaConsultaGuiaArchivoSubida.size(); i++) {
            if (listaConsultaGuiaArchivoSubida.get(i).getSelloinicial() != 0 && listaConsultaGuiaArchivoSubida.get(i).getSellofinal() != 0) {
                bandera = true;
            } else {
                bandera = false;
                break;
            }
        }
        if (bandera) {
            if (!listaConsultaGuiaArchivoSubida.isEmpty()) {
                arregloJSON.addAll(addItemsArregloJSON(listaConsultaGuiaArchivoSubida));
                addItemsGuias(arregloJSON);
                list = new ArrayList<>();
                inputStream = new ArrayList<>();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "Error de carga, el archivo se encuentra vacio");
            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Es necesario completar los sellos");
        }
    }

    public List<JSONObject> addItemsArregloJSON(List<Consultaguiaremision> consulGuia) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String dateI = sdf.format(new Date());

        JSONObject obj = new JSONObject();
        JSONObject objPK = new JSONObject();
        List<JSONObject> listObjEnvRest = new ArrayList<>();
        for (int i = 0; i < consulGuia.size(); i++) {//           
            objPK.put("codigocomercializadora", consulGuia.get(i).getConsultaguiaremisionPK().getCodigocomercializadora());
            objPK.put("numero", consulGuia.get(i).getConsultaguiaremisionPK().getNumero());
            objPK.put("fecha", consulGuia.get(i).getConsultaguiaremisionPK().getFecha());
            objPK.put("fecharecepcion", dateI);

            obj.put("consultaguiaremisionPK", objPK);
            obj.put("codigoterminal", consulGuia.get(i).getCodigoterminal());
            obj.put("numerooe", consulGuia.get(i).getNumerooe());
            obj.put("codigoareamercadeo", consulGuia.get(i).getCodigoareamercadeo());
            obj.put("codigoproducto", consulGuia.get(i).getCodigoproducto());
            obj.put("codigomedida", consulGuia.get(i).getCodigomedida());
            obj.put("medida", consulGuia.get(i).getMedida());
            obj.put("producto", consulGuia.get(i).getProducto());
            obj.put("volumenentregado", consulGuia.get(i).getVolumenentregado());
            obj.put("autotanque", consulGuia.get(i).getAutotanque());
            obj.put("estado", consulGuia.get(i).getEstado());
            obj.put("activo", consulGuia.get(i).getActivo());
            obj.put("usuarioactual", consulGuia.get(i).getUsuarioactual());
            obj.put("numerosri", consulGuia.get(i).getNumerosri());
            obj.put("cedulaconductor", consulGuia.get(i).getCedulaconductor());
            obj.put("nombreconductor", consulGuia.get(i).getNombreconductor());
            obj.put("observacion", consulGuia.get(i).getObservacion());
            obj.put("codigocliente", consulGuia.get(i).getCodigocliente());
            obj.put("compartimento1", consulGuia.get(i).getCompartimento1());
            obj.put("compartimento2", consulGuia.get(i).getCompartimento2());
            obj.put("compartimento3", consulGuia.get(i).getCompartimento3());
            obj.put("compartimento4", consulGuia.get(i).getCompartimento4());
            obj.put("compartimento5", consulGuia.get(i).getCompartimento5());
            obj.put("compartimento6", consulGuia.get(i).getCompartimento6());
            obj.put("selloinicial", consulGuia.get(i).getSelloinicial());
            obj.put("sellofinal", consulGuia.get(i).getSellofinal());
            obj.put("numerofactura", consulGuia.get(i).getNumerofactura());
            obj.put("horaautorizacion", consulGuia.get(i).getHoraautorizacion());
            obj.put("fechafactura", sdf.format(consulGuia.get(i).getFechafactura()));
            obj.put("numeroautorizacion", consulGuia.get(i).getNumeroautorizacion());
            obj.put("fechaautorizacion", consulGuia.get(i).getFechaautorizacion());
            obj.put("direstablecimiento", consulGuia.get(i).getDirestablecimiento());
            listObjEnvRest.add(obj);
            obj = new JSONObject();
            objPK = new JSONObject();
        }
        return listObjEnvRest;
    }

    public void addItemsGuias(List<JSONObject> arregloJSON) {
        try {
            String respuesta;
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/agregar";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.consultaguiaremision/agregar";

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
                listaConsultaGuiaArchivoSubida = new ArrayList<>();
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

    public void lecturaXml() throws ParserConfigurationException, SAXException, IOException {
        listaConsultaGuiaArchivoSubida = new ArrayList<>();
        codigoComer = comercializadora.getCodigo();
        codTerminal = terminal.getCodigo();
        if (codigoComer != null || codTerminal != null) {
            File folder = new File("C:\\archivos\\docs");
            for (File file : folder.listFiles()) {
                if (!file.isDirectory()) {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document document = db.parse(file);
                    Document doc = null;
                    document.getDocumentElement().normalize();
                    System.out.println("Root Element :" + document.getDocumentElement().getNodeName());
                    NodeList nList = document.getElementsByTagName("comprobante");
                    System.out.println("----------------------------");
                    for (int temp = 0; temp < nList.getLength(); temp++) {
                        Node nNode = nList.item(temp);
                        System.out.println("\nCurrent Element :" + nNode.getNodeName());
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            doc = convertStringToXMLDocument(eElement.getFirstChild().getTextContent());
                        }
                    }
                    NodeList nList1 = doc.getChildNodes();
                    for (int temp = 0; temp < nList1.getLength(); temp++) {
                        Node nNode = nList1.item(temp);
                        System.out.println("\nCurrent Element :" + nNode.getNodeName());
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            consulGuiaPK.setCodigocomercializadora(codigoComer);
                            String dia = (eElement.getElementsByTagName("fechaIniTransporte").item(0).getTextContent()).replace("/", "").substring(0, 2);
                            String mes = (eElement.getElementsByTagName("fechaIniTransporte").item(0).getTextContent()).replace("/", "").substring(2, 4);
                            String anio = (eElement.getElementsByTagName("fechaIniTransporte").item(0).getTextContent()).replace("/", "").substring(4);
                            consulGuiaPK.setFecha(anio + mes + dia);
                            //<campoAdicional nombre="CodigoControlDeposito">0208164802-TERMINAL EL BEATERIO</campoAdicional> -- 8 caracteres
                            consulGuiaPK.setNumero((eElement.getElementsByTagName("campoAdicional").item(5).getTextContent()).substring(0, 8));

                            consulGuia.setConsultaguiaremisionPK(consulGuiaPK);
                            //<campoAdicional nombre="PedidoFacturaFecha">0200011200102000000009610/07/2022</campoAdicional> -- 8 caracteres
                            consulGuia.setNumerooe((eElement.getElementsByTagName("campoAdicional").item(4).getTextContent()).substring(0, 8));
                            //dos primeros digitos codigoInterno                
                            consulGuia.setCodigoareamercadeo((eElement.getElementsByTagName("codigoInterno").item(0).getTextContent()).substring(0, 2));
                            consulGuia.setCodigoproducto(eElement.getElementsByTagName("codigoInterno").item(0).getTextContent());
                            //<detAdicional nombre="Unidad de Medida" valor="GALS"/> valor                            
                            int i;
                            for (i = 0; i < eElement.getElementsByTagName("campoAdicional").item(6).getTextContent().length(); i++) {
                                if (eElement.getElementsByTagName("campoAdicional").item(6).getTextContent().charAt(i) == '-') {
                                    break;
                                }
                            }
                            consulGuia.setCodigomedida((eElement.getElementsByTagName("campoAdicional").item(6).getTextContent()).substring(i + 1));
                            consulGuia.setMedida(consulGuia.getCodigomedida());
                            //<campoAdicional nombre="CodigoControlDeposito">0208164802-TERMINAL EL BEATERIO</campoAdicional> -- 2 caracteres despues de numero
                            consulGuia.setCodigoterminal((eElement.getElementsByTagName("campoAdicional").item(5).getTextContent()).substring(0, 2));
                            //<detAdicional nombre="Unidad de Medida" valor="GALS"/> valor

                            consulGuia.setProducto(eElement.getElementsByTagName("descripcion").item(0).getTextContent());
                            consulGuia.setVolumenentregado(new BigDecimal(eElement.getElementsByTagName("cantidad").item(0).getTextContent()));
                            consulGuia.setAutotanque(eElement.getElementsByTagName("placa").item(0).getTextContent());
                            consulGuia.setEstado("ACT");
                            consulGuia.setActivo(true);
                            consulGuia.setNumerosri(eElement.getElementsByTagName("estab").item(0).getTextContent()
                                    + eElement.getElementsByTagName("ptoEmi").item(0).getTextContent()
                                    + eElement.getElementsByTagName("secuencial").item(0).getTextContent());
                            //codigocliente <campoAdicional nombre="Codigo Cliente">02010677  ESTACION DE SERVICIO SANTA ANA</campoAdicional> -- 8primeros
                            consulGuia.setCodigocliente((eElement.getElementsByTagName("campoAdicional").item(1).getTextContent()).substring(0, 8));
                            consulGuia.setCompartimento1(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(7).getTextContent()));
                            consulGuia.setCompartimento2(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(8).getTextContent()));
                            consulGuia.setCompartimento3(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(9).getTextContent()));
                            consulGuia.setCompartimento4(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(10).getTextContent()));
                            consulGuia.setCompartimento5(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(11).getTextContent()));
                            consulGuia.setCompartimento6(Integer.valueOf(eElement.getElementsByTagName("campoAdicional").item(12).getTextContent()));
                            //numero factura <campoAdicional nombre="PedidoFacturaFecha">0200011200102000000009610/07/2022</campoAdicional> -- 15 caracteres despues de numero oe
                            consulGuia.setNumerofactura(eElement.getElementsByTagName("campoAdicional").item(4).getTextContent().substring(8, 23));
                            consulGuia.setCedulaconductor(eElement.getElementsByTagName("campoAdicional").item(0).getTextContent().substring(0, 10));
                            consulGuia.setNombreconductor((eElement.getElementsByTagName("campoAdicional").item(0).getTextContent().trim()).substring(11));
                            consulGuia.setUsuarioactual(dataUser.getUser().getNombrever());

                            listaConsultaGuiaArchivoSubida.add(consulGuia);
                            consulGuia = new Consultaguiaremision();
                            consulGuiaPK = new ConsultaguiaremisionPK();
                        }
                    }
                }
            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_WARN, "Seleccione una comercializadora y un terminal para poder cargar el archivo");
        }
    }

    private static Document convertStringToXMLDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String nombreterminal(String codterminal) {
        String nombre = "";
        if (codterminal != null) {
            for (int i = 0; i < listaTermianles.size(); i++) {
                if (listaTermianles.get(i).getCodigo().equals(codterminal)) {
                    nombre = listaTermianles.get(i).getNombre();
                }
            }
        }
        return nombre;
    }

    public String nombreCliente(String codcli) {
        String nombre = "";
        listaClientes = new ArrayList<>();
        listaClientes = cliServicio.obtenerClientes();
        if (codcli != null) {
            for (int i = 0; i < listaClientes.size(); i++) {
                if (listaClientes.get(i).getCodigo().equals(codcli)) {
                    nombre = listaClientes.get(i).getNombre();
                    break;
                }
            }
        }
        return nombre;
    }

    public String fecha(Date fecha) {
        String fechaS = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (fecha != null) {
            fechaS = sdf.format(fecha);
        }
        return fechaS;
    }

    public void inserarDatos(AjaxBehaviorEvent event) {
        //System.err.println("OnBlur Action");
//        for (int i = 0; i <) {
//            
//        }
    }
    
    public void calcularCantidadSellos(Consultaguiaremision guia) {
        if (guia.getSelloinicial() != 0 && guia.getSellofinal() != 0) {
            guia.setCantidadsellos(guia.getSellofinal() - guia.getSelloinicial() + 1);
        }
    }

    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    public String getCodigoComer() {
        return codigoComer;
    }

    public void setCodigoComer(String codigoComer) {
        this.codigoComer = codigoComer;
    }

    public boolean isEditarPrecio() {
        return editarPrecio;
    }

    public void setEditarPrecio(boolean editarPrecio) {
        this.editarPrecio = editarPrecio;
    }

    public boolean isEditarBanco() {
        return editarPrecio;
    }

    public void setEditarBanco(boolean editarPrecio) {
        this.editarPrecio = editarPrecio;
    }

    public boolean isEstadoBanco() {
        return estadoBanco;
    }

    public void setEstadoBanco(boolean estadoBanco) {
        this.estadoBanco = estadoBanco;
    }

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public boolean isSoloVigente() {
        return soloVigente;
    }

    public void setSoloVigente(boolean soloVigente) {
        this.soloVigente = soloVigente;
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

    public List<Consultaguiaremision> getListaConsultaGuia() {
        return listaConsultaGuia;
    }

    public void setListaConsultaGuia(List<Consultaguiaremision> listaConsultaGuia) {
        this.listaConsultaGuia = listaConsultaGuia;
    }

    public Consultaguiaremision getConsulGuia() {
        return consulGuia;
    }

    public void setConsulGuia(Consultaguiaremision consulGuia) {
        this.consulGuia = consulGuia;
    }

    public ConsultaguiaremisionPK getConsulGuiaPK() {
        return consulGuiaPK;
    }

    public void setConsulGuiaPK(ConsultaguiaremisionPK consulGuiaPK) {
        this.consulGuiaPK = consulGuiaPK;
    }

    public String getCodTerminal() {
        return codTerminal;
    }

    public void setCodTerminal(String codTerminal) {
        this.codTerminal = codTerminal;
    }

    public boolean isMostarGuia() {
        return mostarGuia;
    }

    public void setMostarGuia(boolean mostarGuia) {
        this.mostarGuia = mostarGuia;
    }

    public boolean isMostarPantallaInicial() {
        return mostarPantallaInicial;
    }

    public void setMostarPantallaInicial(boolean mostarPantallaInicial) {
        this.mostarPantallaInicial = mostarPantallaInicial;
    }

    public List<Consultaguiaremision> getListaConsultaGuiaArchivoSubida() {
        return listaConsultaGuiaArchivoSubida;
    }

    public void setListaConsultaGuiaArchivoSubida(List<Consultaguiaremision> listaConsultaGuiaArchivoSubida) {
        this.listaConsultaGuiaArchivoSubida = listaConsultaGuiaArchivoSubida;
    }

    public String getTipoFecha() {
        return tipoFecha;
    }

    public void setTipoFecha(String tipoFecha) {
        this.tipoFecha = tipoFecha;
    }

    public List<Consultaguiaremision> getListaConsultaGuiaAux() {
        return listaConsultaGuiaAux;
    }

    public void setListaConsultaGuiaAux(List<Consultaguiaremision> listaConsultaGuiaAux) {
        this.listaConsultaGuiaAux = listaConsultaGuiaAux;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UploadedFiles getFiles() {
        return files;
    }

    public void setFiles(UploadedFiles files) {
        this.files = files;
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

    public String getNumGuia() {
        return numGuia;
    }

    public void setNumGuia(String numGuia) {
        this.numGuia = numGuia;
    }

    public StreamedContent getPdfStream() {
        return pdfStream;
    }

    public void setPdfStream(StreamedContent pdfStream) {
        this.pdfStream = pdfStream;
    }

}
