/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.bean.pedidosyfacturacion;

import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Autotanque;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.Conductor;
import ec.com.infinityone.modelo.EnvioPedido;
import ec.com.infinityone.modelo.EnvioUsoSello;
import ec.com.infinityone.modelo.UsoSello;
import ec.com.infinityone.modelo.Terminal;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.serivicio.actorcomercial.ClienteServicio;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.servicio.catalogo.TerminalServicio;
import ec.com.infinityone.servicio.pedidosyfacturacion.UsoSellosServicio;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author david
 */
@Named("usosellosBean")
@ViewScoped
public class UsoSellosBean extends ReusableBean implements Serializable {

    /*
    Variable para acceder a los servicios de Comercialziadora
     */
    @Inject
    private ComercializadoraServicio comerServicio;
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
    Variable para acceder a los servicios de UsoSello
     */
    @Inject
    private UsoSellosServicio usoSellosServicio;
    /*
    Variable Comercializadora
     */
    private ComercializadoraBean comercializadora;
    /*
    Variable para almacenar los datos comercializadora
     */
    private List<ComercializadoraBean> listaComercializadora;
    /*
    Variable Terminal
     */
    private Terminal terminal;
    /*
    Variable para almacenar los datos comercializadora
     */
    private List<Terminal> listaTermianles;
    /*
    Variable Cliente
     */
    private Cliente cliente;
    /*
    Variable para almacenar los datos clientes
     */
    private List<Cliente> listaClientes;
    /*
    Variable para almacenar el autotaqneu
     */
    private Autotanque autotanque;
    /*
    Variable para almacenar un temporal autontaque
     */
    private Autotanque autotanqueAux;
    /*
    Variable para almacenar una lista de autotanques
     */
    private List<Autotanque> listaAutotanque;
    /*
    Variable para almacenar los datos del conductor
     */
    private Conductor conductor;
    /*
    Variable para almacenar una lista de conductores
     */
    private List<Conductor> listaConductores;
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
    variable para establecer la fecha inicial para la busqueda 
     */
    private Date fechaInicial;
    /*
    variable para establecer la fecha final para la busqueda 
     */
    private Date fechaFinal;
    /*
    Variable para renderizar la pantalla
     */
    private boolean mostrarPantallaInicial;
    /*
    Variable para renderizar la pantalla
     */
    private boolean mostrarPantallaAsignar;

    private EnvioUsoSello envioUsoSello;

    private List<EnvioUsoSello> listaEnvioUsoSellos;

    private UsoSello usoSello;

    private String ultimoSello;
        /*
    Vairbale para almacenar el pdf generado
     */
    private StreamedContent pdfStream;

    /**
     * Constructor por defecto
     */
    public UsoSellosBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        reestablecer();
        mostrarPantallaInicial = true;
        mostrarPantallaAsignar = false;
        obtenerTerminales();
        obtenerComercializadora();
        obtenerAutotanque();
        obtenerConductores();
    }

    public void inicializarValTanques() {
        autotanqueAux = new Autotanque();
        autotanqueAux.setCompartimento1(new BigDecimal(0));
        autotanqueAux.setCompartimento2(new BigDecimal(0));
        autotanqueAux.setCompartimento3(new BigDecimal(0));
        autotanqueAux.setCompartimento4(new BigDecimal(0));
        autotanqueAux.setCompartimento5(new BigDecimal(0));
        autotanqueAux.setCompartimento6(new BigDecimal(0));
        autotanqueAux.setCompartimento7(new BigDecimal(0));
        autotanqueAux.setCompartimento8(new BigDecimal(0));
        autotanqueAux.setCompartimento9(new BigDecimal(0));
        autotanqueAux.setCompartimento10(new BigDecimal(0));
        calcularSellosUsados();
    }

    public void reestablecer() {
        codComer = "";
        codTerminal = "";
        codCliente = "";
        ultimoSello = "";
        mostrarPantallaAsignar = false;
        //codTerminal = "";
        comercializadora = new ComercializadoraBean();
        terminal = new Terminal();
        cliente = new Cliente();
        autotanque = new Autotanque();
        autotanqueAux = new Autotanque();
        conductor = new Conductor();
        listaEnvioUsoSellos = new ArrayList<>();
        envioUsoSello = new EnvioUsoSello();
        usoSello = new UsoSello();
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = comerServicio.obtenerComercializadorasActivas();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda();
        }
    }

    public void seleccionarComerc(int busqueda) {
        if (comercializadora != null) {
            codComer = comercializadora.getCodigo();
        }
    }

    public void obtenerTerminales() {
        listaTermianles = new ArrayList<>();
        listaTermianles = termServicio.obtenerTerminal();
    }

    public void seleccionarTerminal(int busqueda) {
        if (terminal != null) {
            codTerminal = terminal.getCodigo();
            List<Cliente> listaClientesAux = new ArrayList<>();
            listaClientes = new ArrayList<>();
            if (busqueda == 1) {
                listaClientesAux = clienteServicio.obtenerClientesActivosPorComercializadora(codComer);
            } else {
                listaClientesAux = clienteServicio.obtenerClientesPorComercializadoraActiva(codComer);
            }
            for (int i = 0; i < listaClientesAux.size(); i++) {
                if (listaClientesAux.get(i).getCodigoterminaldefecto().getCodigo().equals(codTerminal)) {
                    listaClientes.add(listaClientesAux.get(i));
                }
            }
            //factura.setCodTerminal(terminal.getCodigo());
        } else {
            codTerminal = "-1";
            listaClientes = new ArrayList<>();
            if (busqueda == 1) {
                listaClientes = clienteServicio.obtenerClientesActivosPorComercializadora(codComer);
            } else {
                listaClientes = clienteServicio.obtenerClientesPorComercializadoraActiva(codComer);
            }
        }
    }

    public void obtenerAutotanque() {
        try {
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.autotanque");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaAutotanque = new ArrayList<>();
            autotanque = new Autotanque();
            conductor = new Conductor();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject autotanqueJson = new JSONObject(respuesta);
            JSONArray retorno = autotanqueJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject autoT = retorno.getJSONObject(indice);
                JSONObject cond = autoT.getJSONObject("cedularuc");

                autotanque.setPlaca(autoT.getString("placa"));
                autotanque.setVolumentotal(autoT.getBigDecimal("volumentotal"));
                autotanque.setCompartimento1(autoT.getBigDecimal("compartimento1"));
                autotanque.setCompartimento2(autoT.getBigDecimal("compartimento2"));
                autotanque.setCompartimento3(autoT.getBigDecimal("compartimento3"));
                autotanque.setCompartimento4(autoT.getBigDecimal("compartimento4"));
                autotanque.setCompartimento5(autoT.getBigDecimal("compartimento5"));
                autotanque.setCompartimento6(autoT.getBigDecimal("compartimento6"));
                autotanque.setCompartimento7(autoT.getBigDecimal("compartimento7"));
                autotanque.setCompartimento8(autoT.getBigDecimal("compartimento8"));
                autotanque.setCompartimento9(autoT.getBigDecimal("compartimento9"));
                autotanque.setCompartimento10(autoT.getBigDecimal("compartimento10"));
                autotanque.setActivo(autoT.getBoolean("activo"));
                autotanque.setUsuarioactual(autoT.getString("usuarioactual"));
                conductor.setCedularuc(cond.getString("cedularuc"));
                conductor.setNombre(cond.getString("nombre"));
                conductor.setActivo(cond.getBoolean("activo"));
                autotanque.setCedularuc(conductor);
                listaAutotanque.add(autotanque);
                autotanque = new Autotanque();
                conductor = new Conductor();
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerConductores() {
        try {
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.conductor");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaConductores = new ArrayList<>();
            conductor = new Conductor();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject conductorJson = new JSONObject(respuesta);
            JSONArray retorno = conductorJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject autoT = retorno.getJSONObject(indice);
                conductor.setCedularuc(autoT.getString("cedularuc"));
                conductor.setNombre(autoT.getString("nombre"));
                conductor.setActivo(autoT.getBoolean("activo"));
                conductor.setUsuarioactual(autoT.getString("usuarioactual"));
                if (conductor.getActivo()) {
                    listaConductores.add(conductor);
                }
                conductor = new Conductor();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void seleccionarCliente(int busqueda) {
        if (cliente != null) {
            codCliente = cliente.getCodigo();
            for (int i = 0; i < listaTermianles.size(); i++) {
                if (listaTermianles.get(i).getCodigo().equals(cliente.getCodigoterminaldefecto().getCodigo())) {
                    terminal = listaTermianles.get(i);
                    break;
                }
            }
            seleccionarTerminal(busqueda);
        } else {
            codCliente = "-1";
        }
    }

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            if (dataUser.getUser().getNiveloperacion().equals("cero")) {
                habilitarComer = true;
            }
            if (dataUser.getUser().getNiveloperacion().equals("adco")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                        if (comercializadora != null) {
                            codComer = comercializadora.getCodigo();
                        }
                    }
                }
            }
            if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                        if (comercializadora != null) {
                            codComer = comercializadora.getCodigo();
                        }
                    }
                }
            }
        }
    }

    public void asignacion() {
        reestablecer();
        mostrarPantallaAsignar = true;
        mostrarPantallaInicial = false;
    }

    public void consultarUsoSellos() {
        if (comercializadora != null && terminal != null && fechaInicial != null && fechaFinal != null) {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            /*fechas para hacer la consulta*/
            String fechaI = date.format(fechaInicial);
            String fechaF = date.format(fechaFinal);
            listaEnvioUsoSellos = new ArrayList<>();
            JSONArray retorno = usoSellosServicio.buscarUsoSellos(codComer, codTerminal, fechaI, fechaF);
            if (retorno.isEmpty()) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCONTRARON SELLOS");
            } else {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject envioUso = retorno.getJSONObject(indice);

                        envioUsoSello.setCodigocomercializadora(envioUso.getString("codigocomercializadora"));
                        envioUsoSello.setCodigoterminal(envioUso.getString("codigoterminal"));
                        envioUsoSello.setCodigocliente(envioUso.getString("codigocliente"));
                        envioUsoSello.setPlaca(envioUso.getString("placa"));
                        envioUsoSello.setFecha(envioUso.getString("fecha"));
                        envioUsoSello.setNombreconductor(envioUso.getString("nombreconductor"));
                        envioUsoSello.setNombrecliente(envioUso.getString("nombrecliente"));
                        envioUsoSello.setNp1(envioUso.getString("np1"));
                        envioUsoSello.setSelloconcatenado(envioUso.getString("selloconcatenado"));
                        envioUsoSello.setNpconcatenada(envioUso.getString("npconcatenada"));

                        listaEnvioUsoSellos.add(envioUsoSello);

                        envioUsoSello = new EnvioUsoSello();
                    }
                }
            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_WARN, "Debe seleccionar una comercializadora, un terminal y establecer fechas para realizar la búsqueda");
        }
    }

    public void obtenerUltimoUsoSello() {
        if (!codComer.isEmpty() && !codTerminal.isEmpty()) {
            ultimoSello = usoSellosServicio.buscarUltimoSello(codComer, codTerminal);
        } else {
            this.dialogo(FacesMessage.SEVERITY_WARN, "Debe seleccionar una comercializadora y un terminal, para encontrar el último sello");
        }
    }

    public void calcularSellosUsados() {
        if (!ultimoSello.isEmpty() && !autotanque.getPlaca().isEmpty()) {
            int sello = Integer.parseInt(ultimoSello);
            int numeroSellos = 0;
            BigDecimal menosUno = BigDecimal.valueOf(-1.0);
            int contador = 0;
            usoSello = new UsoSello();
            if (!autotanque.getCompartimento1().equals(menosUno)) {
                contador++;
            }
            if (!autotanque.getCompartimento2().equals(menosUno)) {
                contador++;
            }
            if (!autotanque.getCompartimento3().equals(menosUno)) {
                contador++;
            }
            if (!autotanque.getCompartimento4().equals(menosUno)) {
                contador++;
            }
            if (!autotanque.getCompartimento5().equals(menosUno)) {
                contador++;
            }
            if (!autotanque.getCompartimento6().equals(menosUno)) {
                contador++;
            }
            if (!autotanque.getCompartimento7().equals(menosUno)) {
                contador++;
            }
            if (!autotanque.getCompartimento8().equals(menosUno)) {
                contador++;
            }
            numeroSellos = contador * 2;
            for (int i = 0; i < numeroSellos; i++) {
                sello += 1;
                if (usoSello.getSello1() == null) {
                    usoSello.setSello1(BigInteger.valueOf(sello));
                } else if (usoSello.getSello2() == null) {
                    usoSello.setSello2(BigInteger.valueOf(sello));
                } else if (usoSello.getSello3() == null) {
                    usoSello.setSello3(BigInteger.valueOf(sello));
                } else if (usoSello.getSello4() == null) {
                    usoSello.setSello4(BigInteger.valueOf(sello));
                } else if (usoSello.getSello5() == null) {
                    usoSello.setSello5(BigInteger.valueOf(sello));
                } else if (usoSello.getSello6() == null) {
                    usoSello.setSello6(BigInteger.valueOf(sello));
                } else if (usoSello.getSello7() == null) {
                    usoSello.setSello7(BigInteger.valueOf(sello));
                } else if (usoSello.getSello8() == null) {
                    usoSello.setSello8(BigInteger.valueOf(sello));
                } else if (usoSello.getSello9() == null) {
                    usoSello.setSello9(BigInteger.valueOf(sello));
                } else if (usoSello.getSello10() == null) {
                    usoSello.setSello10(BigInteger.valueOf(sello));
                } else if (usoSello.getSello11() == null) {
                    usoSello.setSello11(BigInteger.valueOf(sello));
                } else if (usoSello.getSello12() == null) {
                    usoSello.setSello12(BigInteger.valueOf(sello));
                } else if (usoSello.getSello13() == null) {
                    usoSello.setSello13(BigInteger.valueOf(sello));
                } else if (usoSello.getSello14() == null) {
                    usoSello.setSello14(BigInteger.valueOf(sello));
                } else if (usoSello.getSello15() == null) {
                    usoSello.setSello15(BigInteger.valueOf(sello));
                } else if (usoSello.getSello16() == null) {
                    usoSello.setSello16(BigInteger.valueOf(sello));
                }
            }
        }
    }

    public void adquirirUsoSello() {
        if (!codComer.isEmpty() && !codTerminal.isEmpty() && !codCliente.isEmpty() && !autotanque.getPlaca().isEmpty()) {
            JSONObject usoSelloPost = new JSONObject();
            JSONObject usoSelloPK = new JSONObject();
            LocalDateTime fechaActual = LocalDateTime.now().withHour(12);
            long timestamp = fechaActual.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            usoSelloPK.put("codigocomercializadora", codComer);
            usoSelloPK.put("codigoterminal", codTerminal);
            usoSelloPK.put("codigocliente", codCliente);
            usoSelloPK.put("placa", autotanque.getPlaca());
            usoSelloPK.put("np1", usoSello.getNp1());

            usoSelloPost.put("usoselloPK", usoSelloPK);
            usoSelloPost.put("fecha", timestamp);
            usoSelloPost.put("nombreconductor", autotanque.getCedularuc().getNombre());
            usoSelloPost.put("nombrecliente", cliente.getNombre());
            usoSelloPost.put("np6", usoSello.getNp6());
            usoSelloPost.put("np5", usoSello.getNp5());
            usoSelloPost.put("np4", usoSello.getNp4());
            usoSelloPost.put("np3", usoSello.getNp3());
            usoSelloPost.put("np2", usoSello.getNp2());
            usoSelloPost.put("sello16", usoSello.getSello16());
            usoSelloPost.put("sello15", usoSello.getSello15());
            usoSelloPost.put("sello14", usoSello.getSello14());
            usoSelloPost.put("sello13", usoSello.getSello13());
            usoSelloPost.put("sello12", usoSello.getSello12());
            usoSelloPost.put("sello11", usoSello.getSello11());
            usoSelloPost.put("sello10", usoSello.getSello10());
            usoSelloPost.put("sello9", usoSello.getSello9());
            usoSelloPost.put("sello8", usoSello.getSello8());
            usoSelloPost.put("sello7", usoSello.getSello7());
            usoSelloPost.put("sello6", usoSello.getSello6());
            usoSelloPost.put("sello5", usoSello.getSello5());
            usoSelloPost.put("sello4", usoSello.getSello4());
            usoSelloPost.put("sello3", usoSello.getSello3());
            usoSelloPost.put("sello2", usoSello.getSello2());
            usoSelloPost.put("sello1", usoSello.getSello1());
            usoSelloPost.put("usuarioactual", dataUser.getUser().getNombrever());

            usoSellosServicio.adquirirUsoSellos(usoSelloPost);
        } else {
            this.dialogo(FacesMessage.SEVERITY_WARN, "Los campos comercializadora, terminal, cliente y autotanque son requeridos, por favor complete la información solicitada");
        }
    }

    public void generarReporteAux(EnvioPedido envP) {
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

    public boolean isMostrarPantallaInicial() {
        return mostrarPantallaInicial;
    }

    public void setMostrarPantallaInicial(boolean mostrarPantallaInicial) {
        this.mostrarPantallaInicial = mostrarPantallaInicial;
    }

    public boolean isMostrarPantallaAsignar() {
        return mostrarPantallaAsignar;
    }

    public void setMostrarPantallaAsignar(boolean mostrarPantallaAsignar) {
        this.mostrarPantallaAsignar = mostrarPantallaAsignar;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public Autotanque getAutotanque() {
        return autotanque;
    }

    public void setAutotanque(Autotanque autotanque) {
        this.autotanque = autotanque;
    }

    public Autotanque getAutotanqueAux() {
        return autotanqueAux;
    }

    public void setAutotanqueAux(Autotanque autotanqueAux) {
        this.autotanqueAux = autotanqueAux;
    }

    public List<Autotanque> getListaAutotanque() {
        return listaAutotanque;
    }

    public void setListaAutotanque(List<Autotanque> listaAutotanque) {
        this.listaAutotanque = listaAutotanque;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    }

    public List<Conductor> getListaConductores() {
        return listaConductores;
    }

    public void setListaConductores(List<Conductor> listaConductores) {
        this.listaConductores = listaConductores;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public EnvioUsoSello getEnvioUsoSello() {
        return envioUsoSello;
    }

    public void setEnvioUsoSello(EnvioUsoSello envioUsoSello) {
        this.envioUsoSello = envioUsoSello;
    }

    public List<EnvioUsoSello> getListaEnvioUsoSellos() {
        return listaEnvioUsoSellos;
    }

    public void setListaEnvioUsoSellos(List<EnvioUsoSello> listaEnvioUsoSellos) {
        this.listaEnvioUsoSellos = listaEnvioUsoSellos;
    }

    public String getUltimoSello() {
        return ultimoSello;
    }

    public void setUltimoSello(String ultimoSello) {
        this.ultimoSello = ultimoSello;
    }

    public UsoSello getUsoSello() {
        return usoSello;
    }

    public void setUsoSello(UsoSello usoSello) {
        this.usoSello = usoSello;
    }

    public StreamedContent getPdfStream() {
        return pdfStream;
    }

    public void setPdfStream(StreamedContent pdfStream) {
        this.pdfStream = pdfStream;
    }
    
    

}
