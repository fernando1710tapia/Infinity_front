/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.pedidosyfacturacion.bean;

import ec.com.infinityone.actorcomercial.bean.ComercializadoraBean;
import ec.com.infinityone.actorcomercial.serivicios.ComercializadoraServicio;
import ec.com.infinityone.bean.TerminalBean;
import ec.com.infinityone.catalogo.servicios.TerminalServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Consultaguiaremision;
import ec.com.infinityone.modeloWeb.ConsultaguiaremisionPK;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
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
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author Andres
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
    protected TerminalBean terminal;
    /*
    Variable que almacena el código del terminal
     */
    protected String codTerminal;
    /*
    Variable para almacenar los datos comercializadora
     */
    protected List<TerminalBean> listaTermianles;
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
    /*
    Variable nombre de archivo 
     */
    private String nombre;

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
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.consultaguiaremision";

        editarPrecio = false;
        consulGuia = new Consultaguiaremision();
        consulGuiaPK = new ConsultaguiaremisionPK();
        comercializadora = new ComercializadoraBean();
        vigente = false;
        mostarGuia = false;
        mostarPantallaInicial = true;
        obtenerComercializadora();
        habilitarBusqueda();
        obtenerTerminales();
    }

    public void obtenerTerminales() {
        listaTermianles = new ArrayList<>();
        listaTermianles = termServicio.obtenerTerminal();
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = this.comercializadoraServicio.obtenerComercializadorasActivas();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda();
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

    public void nuevaGuia() {
        //reestablecer();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        } else {
            seleccionarComercializdora();
        }
        if (habilitarTerminal) {
            terminal = new TerminalBean();
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
                        + "&codigoterminal=" + codTerminal + "&tipofecha=" + tipoFecha + "&fechaI=" + fechaS + "&fechaF=" + fechaF + "&fechaIR=" + fechaIR + "&fechaFR=" + fechaFR);

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

//    public void onRowToggle(ToggleEvent event) {
//        if (event.getVisibility() == Visibility.VISIBLE) {
//            Precio precioD = (Precio) event.getData();
//            if (precioD.getPrecioPK().getCodigoPrecio() != null) {
//                obtenerDetallePrecio(precioD.getPrecioPK().getCodigoPrecio());
//            }
//        }
//    }   
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
        return consulGuia;
    }

    public Consultaguiaremision lecturaDatos(Consultaguiaremision obj) {
        editarPrecio = true;
        consulGuia = obj;
        soloLectura = true;
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return consulGuia;
    }

    public void seleccionarComer() {
        if (comercializadora != null) {
            codigoComer = comercializadora.getCodigo();
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

    public String handleFileUpload(FileUploadEvent event) throws ParseException {

        String ruta_temporal = Fichero.getCARPETAREPORTES();
        //String ruta_temporal = "C:\\archivos\\";
        UploadedFile uploadedFile = event.getFile();
        String ubicacion;
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

            listaConsultaGuiaArchivoSubida = new ArrayList<>();
            codigoComer = comercializadora.getCodigo();
            codTerminal = terminal.getCodigo();

            if (codigoComer != null || codTerminal != null) {
                while (scanner.hasNextLine()) {
                    // el objeto scanner lee linea a linea desde el archivo
                    String linea = scanner.nextLine();
                    Scanner delimitar = new Scanner(linea);
                    //se usa una expresión regular
                    //que valida que antes o despues de una coma (,) exista cualquier cosa
                    //parte la cadena recibida cada vez que encuentre una coma				
                    delimitar.useDelimiter("\\s*,\\s*");

                    consulGuiaPK.setCodigocomercializadora(codigoComer);
                    consulGuiaPK.setFecha(delimitar.next());
                    consulGuiaPK.setNumero(delimitar.next());

                    consulGuia.setConsultaguiaremisionPK(consulGuiaPK);
                    consulGuia.setNumerooe(delimitar.next());
                    consulGuia.setCodigoareamercadeo(delimitar.next());
                    consulGuia.setCodigoproducto(delimitar.next());
                    consulGuia.setCodigomedida(delimitar.next());
                    consulGuia.setCodigoterminal(codTerminal);
                    consulGuia.setMedida(delimitar.next());
                    consulGuia.setProducto(delimitar.next());
                    consulGuia.setVolumenentregado(new BigDecimal(delimitar.next()));
                    consulGuia.setAutotanque(delimitar.next());
                    consulGuia.setEstado(delimitar.next());
                    consulGuia.setActivo(true);
                    consulGuia.setUsuarioactual(dataUser.getUser().getNombrever());

                    listaConsultaGuiaArchivoSubida.add(consulGuia);
                    consulGuia = new Consultaguiaremision();
                    consulGuiaPK = new ConsultaguiaremisionPK();
                }
                //se cierra el ojeto scanner
                scanner.close();
                FacesContext context = FacesContext.getCurrentInstance();
                nombre = event.getFile().getFileName();
                context.addMessage("Guía Garantía", new FacesMessage(FacesMessage.SEVERITY_INFO, "CARGA CORRECTA", event.getFile().getFileName() + " cargado al sistema"));
                return nombre;
            } else {
                this.dialogo(FacesMessage.SEVERITY_WARN, "Seleccione una comercializadora y un terminal para poder cargar el archivo");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void guardar() throws ParseException {

//        StringBuilder cadenaInfo = new StringBuilder();
//        StringBuilder cadenaErro = new StringBuilder();
        if (!listaConsultaGuiaArchivoSubida.isEmpty()) {
            for (int i = 0; i < listaConsultaGuiaArchivoSubida.size(); i++) {
                if (!addItems(i)) {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR GUIA" + listaConsultaGuiaArchivoSubida.get(i).getConsultaguiaremisionPK().getNumero());
//                    this.dialogo(FacesMessage.SEVERITY_INFO, cadenaInfo.toString());
//                    this.dialogo(FacesMessage.SEVERITY_ERROR, cadenaErro.toString());
                }
            }

            listaConsultaGuiaArchivoSubida = new ArrayList<>();
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Error de carga, el archivo se encuentra vacio");
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

    public TerminalBean getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalBean terminal) {
        this.terminal = terminal;
    }

    public List<TerminalBean> getListaTermianles() {
        return listaTermianles;
    }

    public void setListaTermianles(List<TerminalBean> listaTermianles) {
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

}
