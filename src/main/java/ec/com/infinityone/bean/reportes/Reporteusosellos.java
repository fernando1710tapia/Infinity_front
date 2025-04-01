package ec.com.infinityone.bean.reportes;

import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.bean.enums.ImpuestosEnum;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Detallefactura;
import ec.com.infinityone.modelo.EnvioFactura;
import ec.com.infinityone.modelo.Factura;
import ec.com.infinityone.modelo.FacturaComercialDto;
import ec.com.infinityone.modelo.FacturaComercialPKDto;
import ec.com.infinityone.modelo.Listaprecio;
import ec.com.infinityone.modelo.Terminal;
import ec.com.infinityone.modelo.TerminalSello;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.servicio.catalogo.TerminalServicio;
import ec.com.infinityone.servicio.pedidosyfacturacion.FacturaComercialServicio;
import ec.com.infinityone.servicio.pedidosyfacturacion.SellosServicio;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class Reporteusosellos extends ReusableBean implements Serializable {

    /*
    Variable que trae los mètodos de sellos
     */
    @Inject
    private SellosServicio selloServicio;

    /*
    Variable para acceder a los servicios de Comercialziadora
     */
    @Inject
    private ComercializadoraServicio comerServicio;
    /*
    
    /*
    Variable para acceder a los servicios de Comercialziadora
     */
    @Inject
    private TerminalServicio termServicio;
    /*
     
    /*
    Variable Comercializadora
     */
    private ComercializadoraBean comercializadora;
    /*
    Variable que almacena el código de la comercializadora
     */
    private String codComer;
    /*
    Variable que almacena el código del terminal
     */
    private String codTerminal;
    /*
    Variable que almacena el nombre del terminal
     */
    private String nomTerminal;
    /*
    Variable que almacena el código de la abastecedora
     */
    private String codAbas;
    /*
    Variable que almacena el nombre de la comercializadora
     */
    private String nombComer;
    /*
    Vairbale para almacenar el pdf generado
     */
    private StreamedContent docStream;
    /*
    Variable para almacenar los datos comercializadora
     */
    private List<ComercializadoraBean> listaComercializadora;
    /*
    Variable para almacenar los datos terminales
     */
    private List<Terminal> listaTerminal;
    /*
    Variable Comercializadora
     */
    private Terminal terminal;
    /**
     * Variable que permite establecer la fecha inicial para realizar la
     * busqueda de facturas
     */
    private Date fechaI;
    /**
     * Variable que permite establecer la fecha final para realizar la busqueda
     * de facturas
     */
    private Date fechaf;
    /*
    Variable para guardar una listda de Factura y Detalle Factura
     */
    private List<FacturaComercialDto> listFacturaComercialDto;

    /*
    Variable para guardar una lista de Terminalsello
     */
    private JSONArray listTerminalSelloDto;

    /*
    Variable para guardar un Mapa de los nobres de los Terminales
     */
    private HashMap<String, String> nombreTerminales;

    private HSSFRow headerRow;
    private String header;
    private HSSFCell cell;
    private String[] headers;
    private HSSFRow dataRow;
    private int contadorCeldaCabecera;
    private int contadorCeldaDato;
    private int contadorFilaDato;
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private CellStyle headerStyle; 
    private Long dateStrFV;
    private Date dateFechaMovimiento;
    private CellStyle fechacortaStyle;
    
     /*
    Vairbale para almacenar el pdf generado
     */
    private StreamedContent pdfStream;
    
    /**
     * Constructor por defecto
     */
    public Reporteusosellos() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        obtenerTerminal();
        obtenerComercializadora();
        nombreTerminales = new HashMap<>();
        //    obtenerTerminales();
        contadorCeldaCabecera = 0;

    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = comerServicio.obtenerComercializadorasActivas();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda();
        }
    }

    public void obtenerTerminales() {
        listaTerminal = new ArrayList<>();
        listaTerminal = termServicio.obtenerTerminal();
        mapearTerminales();
    }

    public void mapearTerminales() {
        if (!listaTerminal.isEmpty()) {

            for (Terminal element : listaTerminal) {
                nombreTerminales.put(element.getCodigo(), element.getNombre());
            }
        }
    }

    public void obtenerTerminal() {
        listaTerminal = new ArrayList<>();
        listaTerminal = termServicio.obtenerTerminalesActivos();
        if (!listaTerminal.isEmpty()) {
            habilitarBusqueda();
        }
    }

    public void seleccionarComerc() {
        if (comercializadora != null) {
            codComer = comercializadora.getCodigo();
            codAbas = comercializadora.getCodigoAbas();
            nombComer = comercializadora.getNombre();
        }
    }

    public void seleccionarTerminal() {
        if (terminal != null) {
            codTerminal = terminal.getCodigo();
            nomTerminal = terminal.getNombre();
        }

    }

    public void generarArchivo() throws ParseException, IOException, Throwable {
        listTerminalSelloDto = new JSONArray();;
        /*fechas para comparar entre las dos y establecer un rango de 30 dias*/
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

        String dateI = sdf.format(fechaI);
        String dateF = sdf.format(fechaf);

        Date firstDate = sdf.parse(dateI);
        Date secondDate = sdf.parse(dateF);

        long diff = secondDate.getTime() - firstDate.getTime();
        TimeUnit time = TimeUnit.DAYS;
        long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
        if (diffrence > 365) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "LA FECHA DE FIN NO PUEDE SER MAYOR A UN AÑO A LA FECHA DE INICIO");
        } else {
            listTerminalSelloDto = selloServicio.buscarUsoSellosTerminal(codComer, codTerminal, dateI, dateF);
            if (!listTerminalSelloDto.isEmpty()) {
                crearArchivoNuevo(dateI, dateF, listTerminalSelloDto);
            } else {
                this.dialogo(FacesMessage.SEVERITY_INFO, "NO SE ENCONTRARON REGISTROS DE USO DE SELLOS. "
                        + "Por favor,verfique las fechas de busqueda o contactese con Sistemas!");
            }

        }
    }

    public void descargar(String nombre) throws FileNotFoundException {
        File initialFile = new File(Fichero.getCARPETAREPORTES() + nombre);
        InputStream targetStream = new FileInputStream(initialFile);
        docStream = new DefaultStreamedContent(targetStream, "application/txt", nombre);
    }

    public void crearArchivo(String fechaDesde, String fechaHasta, JSONArray unalistTerminalSelloDto) throws Throwable {

        JSONObject unTerminalSello = new JSONObject();
        JSONObject unTerminalSelloPK = new JSONObject();
        String nombreDoc = "SellosAdministrados_" + fechaDesde.replace("/", "") + "_" + fechaHasta.replace("/", "");

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date dateFechaMovimiento;
        Long dateStrFV;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "UsoSellos");
        String[] headers = new String[]{
            "Movimiento",
            "Fecha Mov.",
            "Term. entrega",
            "Term. recibe",
            "Sello inicial",
            "Sello final",
            "Saldo de sellos",
            "Observación"
        };

        CellStyle numerosStyle = workbook.createCellStyle();
        numerosStyle.setDataFormat(workbook.createDataFormat().getFormat("####################"));
        numerosStyle.setAlignment(HorizontalAlignment.RIGHT);

        // formato para celda tipo fecha corta
        CellStyle fechacortaStyle = workbook.createCellStyle();
        fechacortaStyle.setDataFormat(workbook.createDataFormat().getFormat("YYY-MM-DD"));
        fechacortaStyle.setAlignment(HorizontalAlignment.RIGHT);

        CellStyle numeros2Style = workbook.createCellStyle();
        numeros2Style.setDataFormat(workbook.createDataFormat().getFormat("####################"));

        HSSFFont font = workbook.createFont();
        font.setBold(true);
        numeros2Style.setFont(font);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(font);

        HSSFRow headerRow = sheet.createRow(3);
        for (int i = 0; i < headers.length; ++i) {
            String header = headers[i];
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header);
            sheet.autoSizeColumn(cell.getColumnIndex());
        }

        HSSFRow titulo = sheet.createRow(0);
        HSSFCell cellFiltros = titulo.createCell(0);
        cellFiltros.setCellStyle(headerStyle);
        cellFiltros.setCellValue(nombComer);

        HSSFRow desdeHasta = sheet.createRow(1);
        cellFiltros = desdeHasta.createCell(0);
        cellFiltros.setCellValue(
                "DETALLE SOBRE EL USO DE SELLOS: " + fechaDesde
                + ", HASTA: " + fechaHasta + ".");

//        List<String> listaIdFacturas = listenvF.stream().map(p -> p.getFactura().getFacturaPK().getNumero())
//                .collect(Collectors.toList());
//        System.out.println("Valores recibidos: " + fechaDesde + " " + fechaHasta);
//        System.out.println("Total id facturas: " + listaIdFacturas.toArray().length);
        for (int i = 0; i < unalistTerminalSelloDto.length(); ++i) {
            try {
                unTerminalSello = unalistTerminalSelloDto.getJSONObject(i);
                unTerminalSelloPK = unTerminalSello.getJSONObject("terminalselloPK");
                HSSFRow dataRow = sheet.createRow(i + 4);
                //"Movimiento", "Fecha Mov.", "Term. entrega", "Term. recibe","Sello inicial","Sello final", "Saldo de sellos"

                if ("00".equalsIgnoreCase(unTerminalSelloPK.getString("codigoterminalentrega"))
                        && "00".equalsIgnoreCase(unTerminalSelloPK.getString("codigoterminalrecibe"))) {
                    dataRow.createCell(0).setCellValue("Compra B. central");
                } else {
                    dataRow.createCell(0).setCellValue("Entrega");
                }
                
                dateStrFV = unTerminalSello.getLong("fecha");
                dateFechaMovimiento = new Date(dateStrFV);
                dataRow.createCell(1).setCellValue(dateFechaMovimiento);
                dataRow.getCell(1).setCellStyle(fechacortaStyle);

                dataRow.createCell(2).setCellValue(nombreTerminales.get(unTerminalSelloPK.getString("codigoterminalentrega")));
                dataRow.createCell(3).setCellValue(nombreTerminales.get(unTerminalSelloPK.getString("codigoterminalrecibe")));

                dataRow.createCell(4).setCellValue(unTerminalSelloPK.getLong("selloinicial"));
                dataRow.getCell(4).setCellStyle(numerosStyle);

                dataRow.createCell(5).setCellValue(unTerminalSelloPK.getLong("sellofinal"));
                dataRow.getCell(5).setCellStyle(numerosStyle);

                dataRow.createCell(6).setCellValue(unTerminalSello.getLong("sellosvalidos"));
                dataRow.getCell(6).setCellStyle(numerosStyle);

                dataRow.createCell(7).setCellValue(unTerminalSello.getString("observacion"));

                //               }
            } catch (Throwable ex) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL CREAR EXCEL:" + ex);
            }
        }

        CellStyle cellStyleTotal = workbook.createCellStyle();
        cellStyleTotal.setFont(font);
        cellStyleTotal.setAlignment(HorizontalAlignment.RIGHT);

        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException ex) {
            System.out.println("FT:: error en crearArchivo Excepcion: " + ex);
        }
        if (outputStream.size() != 0) {
            byte[] bytes = outputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            docStream = new DefaultStreamedContent(inputStream, "application/xls", nombreDoc + ".xls");
            System.out.println("¡El archivo Excel se ha generado exitosamente!");
            outputStream.close();
        }
    }
    
    public void crearArchivoNuevo(String fechaDesde, String fechaHasta, JSONArray unalistTerminalSelloDto) throws Throwable {

        JSONObject unUsoSello = new JSONObject();
        String nombreDoc = "UsoSellos" + fechaDesde.replace("/", "") + "_" + fechaHasta.replace("/", "");

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date dateFechaMovimiento;
        Long dateStrFV;
        String pedidosConcatenados="";
        String sellosConcatenados="";
        String clienteTransporteConcatenados="";
        String numeroNP;
        String pedidoListo;
        String concatenador = "\n";
        int    indiceNP;
        int indiceSello;
        String numeroSello;
        String selloListo;
        HSSFCell celdaInformacion;
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        sheet.autoSizeColumn(0);
        workbook.setSheetName(0, "UsoSellos");
        String[] headers = new String[]{
            "Fecha uso",
            "Cliente-Transporte",
            "Pedidos",
            "Sellos utilizados"
        };

        CellStyle numerosStyle = workbook.createCellStyle();
        numerosStyle.setDataFormat(workbook.createDataFormat().getFormat("####################"));
        numerosStyle.setAlignment(HorizontalAlignment.RIGHT);

        // formato para celda tipo fecha corta
        CellStyle fechacortaStyle = workbook.createCellStyle();
        fechacortaStyle.setDataFormat(workbook.createDataFormat().getFormat("YYY-MM-DD"));
        fechacortaStyle.setAlignment(HorizontalAlignment.RIGHT);

        CellStyle numeros2Style = workbook.createCellStyle();
        numeros2Style.setDataFormat(workbook.createDataFormat().getFormat("####################"));

        HSSFFont font = workbook.createFont();
        font.setBold(true);
        numeros2Style.setFont(font);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(font);
        
        // estilo de celda para ajustar al alto de la información
        CellStyle altoAutomatico = workbook.createCellStyle();
        altoAutomatico.setWrapText(true);
         
         
        HSSFRow headerRow = sheet.createRow(3);
        for (int i = 0; i < headers.length; ++i) {
            String header = headers[i];
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header);
            sheet.autoSizeColumn(cell.getColumnIndex());
        }

        HSSFRow titulo = sheet.createRow(0);
        HSSFCell cellFiltros = titulo.createCell(0);
        cellFiltros.setCellStyle(headerStyle);
        cellFiltros.setCellValue(nombComer);

        HSSFRow desdeHasta = sheet.createRow(1);
        cellFiltros = desdeHasta.createCell(0);
        cellFiltros.setCellValue(
                "DETALLE SOBRE EL USO DE SELLOS: " + fechaDesde
                + ", HASTA: " + fechaHasta + ".");
        
        for (int i = 0; i < unalistTerminalSelloDto.length(); ++i) {
            try {
                unUsoSello = unalistTerminalSelloDto.getJSONObject(i);
                indiceNP=1;
                numeroNP = "np";
                indiceSello=1;
                numeroSello = "sello";
                HSSFRow dataRow = sheet.createRow(i + 4);
                //""

////                dateStrFV = unUsoSello.getLong("ufecha");
////                dateFechaMovimiento = new Date(dateStrFV);
////                dataRow.createCell(1).setCellValue(dateFechaMovimiento);
////                dataRow.getCell(1).setCellStyle(fechacortaStyle);
                    dataRow.createCell(0).setCellValue(unUsoSello.getString("fecha"));

                clienteTransporteConcatenados = (unUsoSello.getString("nombrecliente"))+concatenador
                        +(unUsoSello.getString("placa"))+concatenador
                        +(unUsoSello.getString("nombreconductor"));
                
                celdaInformacion = dataRow.createCell(1);
                celdaInformacion.setCellValue(clienteTransporteConcatenados);
                celdaInformacion.setCellStyle(altoAutomatico);
                dataRow.setHeight((short)-1);
                
                pedidosConcatenados ="";
                for (int j = 0; j < 6; j++) { 
                    numeroNP = numeroNP+String.valueOf(indiceNP); 
                    pedidoListo = unUsoSello.getString(numeroNP).trim().replaceAll("null", "");
                    if (!pedidoListo.isEmpty()){
                        pedidosConcatenados = pedidosConcatenados+pedidoListo+concatenador;
                    }
                    indiceNP=indiceNP+1;
                    numeroNP = "np";
                }
                
                celdaInformacion = dataRow.createCell(2);
                celdaInformacion.setCellValue(pedidosConcatenados);
                celdaInformacion.setCellStyle(altoAutomatico);
                dataRow.setHeight((short)-1);
                 
                sellosConcatenados ="";
                for (int j = 0; j < 16; j++) { 
                    numeroSello = numeroSello+String.valueOf(indiceSello); 
                    selloListo = unUsoSello.getString(numeroSello).trim().replaceAll("null", "");
                    if (!selloListo.isEmpty()){
                        sellosConcatenados = sellosConcatenados+selloListo+concatenador;
                    }
                    indiceSello=indiceSello+1;
                    numeroSello = "sello";
                }
//                 dataRow.createCell(3).setCellValue(sellosConcatenados);
                
                 celdaInformacion = dataRow.createCell(3);
                celdaInformacion.setCellValue(sellosConcatenados);
                celdaInformacion.setCellStyle(altoAutomatico);
                dataRow.setHeight((short)-1);

                //               }
            } catch (Throwable ex) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL CREAR EXCEL:" + ex);
            }
        }

        CellStyle cellStyleTotal = workbook.createCellStyle();
        cellStyleTotal.setFont(font);
        cellStyleTotal.setAlignment(HorizontalAlignment.RIGHT);

        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException ex) {
            System.out.println("FT:: error en crearArchivo Excepcion: " + ex);
        }
        if (outputStream.size() != 0) {
            byte[] bytes = outputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            docStream = new DefaultStreamedContent(inputStream, "application/xls", nombreDoc + ".xls");
            System.out.println("¡El archivo Excel se ha generado exitosamente!");
            outputStream.close();
        }
        
        System.out.println("FT:: JUSTO ANTES DE GENERAR FORMATO DE USO DE SELLOS");
                /// solo temporal para probar la impresion del formato de uso de sellos
                    generarReporte(clienteTransporteConcatenados,sellosConcatenados);
                /// solo temporal para probar la impresion del formato de uso de sellos
                System.out.println("FT:: JUSTO DESPUES DE GENERAR FORMATO DE USO DE SELLOS");
    }

    // metodo para crear columnas en excel bajo demanda de la cantidad de datos
    public void crearArchivoMultiple(String fechaDesde, String fechaHasta, JSONArray unalistTerminalSelloDto) throws Throwable {

        JSONObject unUsoSello = new JSONObject();
        JSONObject unUsoSelloPK = new JSONObject();
        JSONObject unUsoSelloInicial = new JSONObject();
        JSONObject unUsoSelloInicialPK = new JSONObject();
        contadorCeldaCabecera = 0;
        contadorCeldaDato = 0;
        contadorFilaDato  = 0;
        String nombreDoc = "UsoSellos-" + fechaDesde.replace("/", "") + "_" + fechaHasta.replace("/", "");

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date dateFechaMovimiento;
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();
        workbook.setSheetName(0, "UsoSellos");
        CellStyle numerosStyle = workbook.createCellStyle();
        numerosStyle.setDataFormat(workbook.createDataFormat().getFormat("####################"));
        numerosStyle.setAlignment(HorizontalAlignment.RIGHT);
        // formato para celda tipo fecha corta
        fechacortaStyle = workbook.createCellStyle();
        fechacortaStyle.setDataFormat(workbook.createDataFormat().getFormat("YYY-MM-DD"));
        fechacortaStyle.setAlignment(HorizontalAlignment.RIGHT);

        CellStyle numeros2Style = workbook.createCellStyle();
        numeros2Style.setDataFormat(workbook.createDataFormat().getFormat("####################"));

        HSSFFont font = workbook.createFont();
        font.setBold(true);
        numeros2Style.setFont(font);

        headerStyle = workbook.createCellStyle();
        headerStyle.setFont(font);

        HSSFRow titulo = sheet.createRow(0);
        HSSFCell cellFiltros = titulo.createCell(0);
        cellFiltros.setCellStyle(headerStyle);
        cellFiltros.setCellValue(nombComer);

        HSSFRow desdeHasta = sheet.createRow(1);
        cellFiltros = desdeHasta.createCell(0);
        cellFiltros.setCellValue(
                "DETALLE SOBRE EL USO DE SELLOS: " + fechaDesde
                + ", HASTA: " + fechaHasta + ".");

        headers = new String[]{
            "Fecha y NP.",
            "Sellos usados",
            "EDS y Transporte"
        };
        // ESTA FILA (3) SERÁ LOS TITULOS
        headerRow = sheet.createRow(3);
        unUsoSelloInicial = unalistTerminalSelloDto.getJSONObject(0);
        imprimirCabecera();
        imprimirPrimerRegistro(unUsoSelloInicial);
        
        for (int i = 1; i < unalistTerminalSelloDto.length(); ++i) {
            unUsoSello = unalistTerminalSelloDto.getJSONObject(i);
            if (unUsoSelloInicial.getString("selloinicial").equalsIgnoreCase(unUsoSello.getString("selloinicial"))) {
                System.out.println("FT:: dentro de IF unUsoSelloInicial == unUsoSello");
                contadorCeldaDato = contadorCeldaDato + i;
                imprimirPrimerRegistro(unUsoSello);
            } else {
                unUsoSelloInicial = unUsoSello;
                contadorCeldaCabecera = contadorCeldaCabecera + 3;
                imprimirCabecera();
                imprimirPrimerRegistro(unUsoSelloInicial);
            }
        }

        if (outputStream.size() != 0) {
            byte[] bytes = outputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            docStream = new DefaultStreamedContent(inputStream, "application/xls", nombreDoc + ".xls");
            System.out.println("¡El archivo Excel se ha generado exitosamente!");
            outputStream.close();
        }

        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException ex) {
            System.out.println("FT:: error en crearArchivo Excepcion: " + ex);
        }

        if (outputStream.size() != 0) {
            byte[] bytes = outputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            docStream = new DefaultStreamedContent(inputStream, "application/xls", nombreDoc + ".xls");
            System.out.println("¡El archivo Excel se ha generado exitosamente!");
            outputStream.close();
        }

    }

    public void imprimirCabecera() {

        for (int i = 0; i < headers.length; ++i) {
            contadorCeldaCabecera = contadorCeldaCabecera + i;
            header = headers[i];
            cell = headerRow.createCell(contadorCeldaCabecera);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header);
            sheet.autoSizeColumn(cell.getColumnIndex());
            
        }
    }

    public void imprimirPrimerRegistro(JSONObject unUsoSelloInicial) {

        try {

            dataRow = sheet.createRow(contadorFilaDato + 4);
             
            //"Movimiento", "Fecha Mov.", "Term. entrega", "Term. recibe","Sello inicial","Sello final", "Saldo de sellos"

//            dateStrFV = unUsoSelloInicial.getLong("ufecha");
//            dateFechaMovimiento = new Date(dateStrFV);
//   ftftft         dataRow.createCell(contadorCeldaDato).setCellValue(unUsoSelloInicial.getString("ufecha"));
//            dataRow.getCell(contadorCeldaDato).setCellStyle(fechacortaStyle);

            dataRow.createCell(contadorCeldaDato + 1).setCellValue(unUsoSelloInicial.getString("sello1"));
            dataRow.createCell(contadorCeldaDato + 2).setCellValue(unUsoSelloInicial.getString("nombrecliente"));
            contadorFilaDato++;
        } catch (Throwable ex) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL CREAR EXCEL:" + ex);
        }
    }

public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            switch (dataUser.getUser().getNiveloperacion()) {
                case "cero":
                    habilitarComer = true;
                    habilitarTerminal = true;
                    break;
                case "adco":
                    habilitarComer = false; 
                    habilitarTerminal = true;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                            this.comercializadora = listaComercializadora.get(i);
                        }
                    }
                    seleccionarComerc();
                    //listaClientes = clienteServicio.obtenerClientesPorComercializadora(comercializadora.getCodigo());
                    break;
                case "usac":
                    habilitarComer = false; 
                    habilitarTerminal = false;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                            this.comercializadora = listaComercializadora.get(i);
                        }

                    }
                    if (comercializadora.getCodigo() != null) {
                        seleccionarComerc();
                    } else {
                        this.dialogo(FacesMessage.SEVERITY_FATAL, "La comercializadora se encuentra deshabilitada");
                    }
                    break;
                case "agco":
                    habilitarComer = false; 
                    habilitarTerminal = false;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                            comercializadora = listaComercializadora.get(i);
                            break;
                        }
                    }
                    seleccionarComerc(); 
                    for (int i = 0; i < listaTerminal.size(); i++) {
                        if (listaTerminal.get(i).getCodigo().equals(dataUser.getUser().getCodigoterminal())) {
                            terminal = listaTerminal.get(i);
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


    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public String getCodComer() {
        return codComer;
    }

    public void setCodComer(String codComer) {
        this.codComer = codComer;
    }

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public StreamedContent getDocStream() {
        return docStream;
    }

    public void setDocStream(StreamedContent docStream) {
        this.docStream = docStream;
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
    
     public String getCodTerminal() {
        return codTerminal;
    }

    public void setCodTerminal(String codTerminal) {
        this.codTerminal = codTerminal;
    }

    public List<Terminal> getListaTerminal() {
        return listaTerminal;
    }

    public void setListaTerminal(List<Terminal> listaTerminal) {
        this.listaTerminal = listaTerminal;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    // CODIGO PARA LA IMPRESION DE USOSELLOS EN JASPER
    
        public void generarReporte(String actor, String sellosUsados) {
//        String path = "C:\\archivos\\Template\\notapedido.jrxml";
        String rutaGuardar = Fichero.getCARPETAREPORTES();
        String path = Fichero.getCARPETAREPORTES() + "/usosellosinbdd.jrxml";
        System.out.println("FT:: metodo.generarReporte PATH:" + path);
        InputStream file = null;
        try {
            file = new FileInputStream(new File(path));

            JasperReport reporte = JasperCompileManager.compileReport(file);
            BufferedImage image = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/logo.jpeg"));
//            BufferedImage image = ImageIO.read(new File("C:\\archivos\\Template\\logo.jpg"));
            Map parametro = new HashMap();

            parametro.put("sellos", sellosUsados);
            parametro.put("actorcomercial", actor);
            parametro.put("logo", image);

            //System.out.println("PARAMETROS: " + parametro);

//////////  ftftftft          Connection conexion = conexionJasperBD();

            JRDataSource conexion = new JREmptyDataSource();

            //System.out.println("CONEXIÓN: " + conexion);
            JasperPrint print = JasperFillManager.fillReport(reporte, parametro, conexion);

//            File directory = new File("C:\\Archivos");
            File directory = new File(rutaGuardar);
            String nombreDocumento = "SellosUsados";
            String fechaArchivo = (new Date().toString());

            File pdf = File.createTempFile(nombreDocumento + "_"+fechaArchivo, ".pdf", directory);
            JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
            File initialFile = new File(pdf.getAbsolutePath());
            InputStream targetStream = new FileInputStream(initialFile);
            //pdfStream = new DefaultStreamedContent();
            pdfStream = new DefaultStreamedContent(targetStream, "application/pdf", nombreDocumento + (new Date()).toString() + ".pdf");
            //DefaultStreamedContent.builder().contentType("application/pdf").name(nombreDocumento + ".pdf").stream(() -> new FileInputStream(targetStream)).build();
            System.out.println("FT::. CREANDO ARCHIVO. "+pdf.getAbsolutePath());
        } catch (Exception ex) {
            //ex.printStackTrace();
            System.out.println("Excepcion: " + ex);
        }
    }
    
    
        
    // FINFINFIN CODIGO PARA LA IMPRESION DE USOSELLOS EN JASPER

    public StreamedContent getPdfStream() {
        return pdfStream;
    }

    public void setPdfStream(StreamedContent pdfStream) {
        this.pdfStream = pdfStream;
    }
    
}
