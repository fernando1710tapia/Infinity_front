package ec.com.infinityone.bean.reportes;

import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.bean.enums.ImpuestosEnum;
import ec.com.infinityone.modelo.Detallefactura;
import ec.com.infinityone.modelo.EnvioClientePrecio;
import ec.com.infinityone.modelo.EnvioFactura;
import ec.com.infinityone.modelo.Factura;
import ec.com.infinityone.modelo.Listaprecio;
import ec.com.infinityone.servicio.pedidosyfacturacion.FacturaServicio;
import ec.com.infinityone.servicio.preciosyfacturacion.ListaprecioServicio;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.servicio.pedidosyfacturacion.NotaPedidoServicio;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
public class ReportefacturacionBean extends ReusableBean implements Serializable {

    /*
    Variable que trae los mètodos de lista Precios
     */
    @Inject
    private ListaprecioServicio listaPrecioServicio;
    /*
    Variable que trae los mètodos de factura
     */
    @Inject
    private FacturaServicio facturaServicio;

    /*
    Variable que trae los mètodos de NOTA DE PEDIDO
     */
    @Inject
    private NotaPedidoServicio notapedidoServicio;

    /*
    Variable para acceder a los servicios de Comercialziadora
     */
    @Inject
    private ComercializadoraServicio comerServicio;
    /*
    Varaibale Lista Precio
     */
    private List<Listaprecio> listaListaprecios;
    /*
    Varaibale Lista Precio
     */
    private Listaprecio listaPrecio;
    /*
    Varaibale para almacenar el codigo de la listaPrecio
     */
    private long codigoListaprecio;
    /*
    Variable Comercializadora
     */
    private ComercializadoraBean comercializadora;
    /*
    Variable que almacena el código de la comercializadora
     */
    private String codComer;
    /*
    Variable que almacena el código de la comercializadora
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
    private List<EnvioFactura> listenvF;

    /*
    Variable para guardar una listda de pedidos en json
     */
    private JSONArray listenPedidos;

    /*
    Variable para guardar una lista clientes y sus precios
     */
    private List<EnvioClientePrecio> listenvCliPre;

    private EnvioClientePrecio unEnvioClientePrecio;

    /**
     * Constructor por defecto
     */
    public ReportefacturacionBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        listaPrecio = new Listaprecio();
        codigoListaprecio = 0;
        obtenerListaPrecio();
        obtenerComercializadora();
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = comerServicio.obtenerComercializadorasActivas();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda();
        }
    }

    public void obtenerListaPrecio() {
        listaListaprecios = new ArrayList<>();
        listaListaprecios = this.listaPrecioServicio.obtenerListaprecio();
    }

    public void selecionarListaPrecio() {
        if (listaPrecio != null) {
            codigoListaprecio = listaPrecio.getListaprecioPK().getCodigo();
        }
    }

    public void seleccionarComerc() {
        if (comercializadora != null) {
            codComer = comercializadora.getCodigo();
            codAbas = comercializadora.getCodigoAbas();
            nombComer = comercializadora.getNombre();
        }
    }

    public void generarReporte() throws ParseException, IOException {
        listenvF = new ArrayList<>();
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
            listenvF = facturaServicio.obtenerFacturasObjEnv(fechaI, fechaf, "-1", null, "-1", null, codAbas, codComer, "1");
            if (!listenvF.isEmpty()) {
                generarExcelFacturacion(dateI, dateF, listenvF);
            } else {
                this.dialogo(FacesMessage.SEVERITY_INFO, "NO SE ENCONTRARON DOCUMENTOS");
            }

        }
    }

    public void generarReportelf() throws ParseException, IOException {
        listenvF = new ArrayList<>();
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
            listenvF = facturaServicio.obtenerFacturasObjEnv(fechaI, fechaf, "-1", null, "-1", null, codAbas, codComer, "1");
            if (!listenvF.isEmpty()) {
                generarExcelFacturacionlf(dateI, dateF, listenvF);
            } else {
                this.dialogo(FacesMessage.SEVERITY_INFO, "NO SE ENCONTRARON DOCUMENTOS");
            }

        }
    }

    public void generarReportelfc() throws ParseException, IOException {
        listenvF = new ArrayList<>();
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
            listenvF = facturaServicio.obtenerFacturasObjEnv(fechaI, fechaf, "-1", null, "-1", null, codAbas, codComer, "1");
            if (!listenvF.isEmpty()) {
                generarExcelFacturacionlfc(dateI, dateF, listenvF);
            } else {
                this.dialogo(FacesMessage.SEVERITY_INFO, "NO SE ENCONTRARON DOCUMENTOS");
            }

        }
    }

    public void generarReportePedidosAbastec() throws ParseException, IOException {
//        listenPedidos; = new ArrayList<>();
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

            listenPedidos = notapedidoServicio.obtenerNotaPedidosReporte(fechaI, fechaf, codComer, codAbas);
            if (!listenPedidos.isEmpty()) {
                generarExcelPedidosAbastec(dateI, dateF, listenPedidos);
            } else {
                this.dialogo(FacesMessage.SEVERITY_INFO, "NO SE ENCONTRARON PEDIDOS PARA ABASTEC");
            }

        }
    }

    public String retornarValor(String codProducto, List<Detallefactura> df) {
        for (int i = 0; i < df.size(); i++) {
            if (codProducto.equals(df.get(i).getCodigoimpuesto())) {
                return df.get(i).getSubtotal().setScale(2, RoundingMode.HALF_UP).toString();
            }
        }
        return "";
    }

    public void generarExcelFacturacion(String fechaDesde, String fechaHasta, List<EnvioFactura> listenvF) throws IOException {
        String nombreDoc = "REPORTE_" + fechaDesde.replace("/", "") + "_" + fechaHasta.replace("/", "");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "ARCHIVO");
        String[] headers = new String[]{
            "Emision",
            "Identificación",
            "NumeroFactura",
            "TotalFactura",
            "NombreCliente",
            "Codigo",
            "Volumen",
            "Costo Unitario",
            "Valor Parcial",
            "IVA ",
            "IMPTO. 3X1000",
            "IVA PRESUNTIVO ",
            "Estado",
            "Oe enpetro",
            "En sri",
            "Fecha-Hora autorización SRI"
        };

        CellStyle numerosStyle = workbook.createCellStyle();
        numerosStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
        numerosStyle.setAlignment(HorizontalAlignment.RIGHT);

        CellStyle numeros2Style = workbook.createCellStyle();
        numeros2Style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

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
                "FACTURAS POR FECHA DE EMISION DESDE: " + fechaDesde
                + ", HASTA: " + fechaHasta + ".");

        List<String> listaIdFacturas = listenvF.stream().map(p -> p.getFactura().getFacturaPK().getNumero())
                .collect(Collectors.toList());
        System.out.println("Valores recibidos: " + fechaDesde + " " + fechaHasta);
        System.out.println("Total id facturas: " + listaIdFacturas.toArray().length);

        for (int i = 0; i < listenvF.size(); ++i) {
            try {
                HSSFRow dataRow = sheet.createRow(i + 4);
                Factura f = listenvF.get(i).getFactura();
                List<Detallefactura> df = listenvF.get(i).getDetalle();
                String sri = listenvF.get(i).getEnsri();

                if (df != null) {
                    dataRow.createCell(0).setCellValue(f.getFechaventa());
                    dataRow.createCell(1).setCellValue(f.getRuccliente());
                    dataRow.createCell(2).setCellValue(f.getFacturaPK().getNumero());
                    dataRow.createCell(3).setCellValue(f.getValorconrubro().setScale(2, RoundingMode.HALF_UP).toString());
                    dataRow.getCell(3).setCellStyle(numerosStyle);
                    dataRow.createCell(4).setCellValue(f.getNombrecliente());
                    dataRow.createCell(5).setCellValue(f.getCodigocliente());
                    dataRow.createCell(6).setCellValue(df.get(0).getVolumennaturalautorizado().setScale(2, RoundingMode.HALF_UP).toString());
                    dataRow.getCell(6).setCellStyle(numerosStyle);
                    dataRow.createCell(7).setCellValue(df.get(0).getPrecioproducto().toString());
                    dataRow.getCell(7).setCellStyle(numerosStyle);
                    dataRow.createCell(8).setCellValue(retornarValor("", df));
                    dataRow.getCell(8).setCellStyle(numerosStyle);
                    dataRow.createCell(9).setCellValue(retornarValor(ImpuestosEnum.IVA.getCodigo(), df));
                    dataRow.getCell(9).setCellStyle(numerosStyle);
                    dataRow.createCell(10).setCellValue(retornarValor(ImpuestosEnum.TRES_X_MIL.getCodigo(), df));
                    dataRow.getCell(10).setCellStyle(numerosStyle);
                    dataRow.createCell(11).setCellValue(retornarValor(ImpuestosEnum.RETENCION_IVA_PRESUNTIVO.getCodigo(), df));
                    dataRow.getCell(11).setCellStyle(numerosStyle);
                    dataRow.createCell(12).setCellValue(f.getActiva() == true ? "Activa" : "Anulada");
                    dataRow.createCell(13).setCellValue(f.getOeenpetro() == true ? "Si" : "No");
                    dataRow.createCell(14).setCellValue(sri.equals("S") ? "Si" : "No");
                    dataRow.createCell(15).setCellValue(f.getFechaautorizacion());
                }
            } catch (Exception ex) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL CREAR EXCEL:" + ex);
            }
        }

        CellStyle cellStyleTotal = workbook.createCellStyle();
        cellStyleTotal.setFont(font);
        cellStyleTotal.setAlignment(HorizontalAlignment.RIGHT);

//        HSSFRow rowTotales = sheet.createRow(9 + listenvF.size());
//        HSSFCell total = rowTotales.createCell(4);
//        total.setCellStyle(cellStyleTotal);
//        total.setCellValue("Total:");
//        total = rowTotales.createCell(9);
//        total.setCellType(CellType.STRING);
//        total.setCellFormula(String.format("SUM(J10:J%d)", 9 + transaccionesActuales.size()));
//        total.setCellStyle(numeros2Style);
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException ex) {
            System.out.println("Excepcion: " + ex);
        }
        if (outputStream.size() != 0) {
            byte[] bytes = outputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            docStream = new DefaultStreamedContent(inputStream, "application/xls", nombreDoc + ".xls");
            System.out.println("¡El archivo Excel se ha generado exitosamente!");
            outputStream.close();
        }
    }

    public void generarExcelFacturacionlf(String fechaDesde, String fechaHasta, List<EnvioFactura> listenvF) throws IOException {
        String nombreDoc = "LISTADOFACTURAS_" + fechaDesde.replace("/", "") + "_" + fechaHasta.replace("/", "");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "ARCHIVO");
        String[] headers = new String[]{
            "Status",
            "No. de Orden",
            "Nombre del Cliente",
            "Nombre Comercial",
            "Código",
            "Cod.Estab",
            "Oficina",
            "Fecha de Emisión",
            "Fecha de Vencimiento",
            "Fecha de Venta",
            "Volumen",
            "Producto",
            "Valor",
            "No. de Factura",
            "No. de Autorización",
            "Fecha de Autorización",
            "Clave de Acceso"
        };

        CellStyle numerosStyle = workbook.createCellStyle();
        numerosStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
        numerosStyle.setAlignment(HorizontalAlignment.RIGHT);

        CellStyle numeros2Style = workbook.createCellStyle();
        numeros2Style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

        HSSFFont font = workbook.createFont();
        font.setBold(true);
        numeros2Style.setFont(font);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(font);

        HSSFRow headerRow = sheet.createRow(2);
        for (int i = 0; i < headers.length; ++i) {
            String header = headers[i];
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header);
            sheet.autoSizeColumn(cell.getColumnIndex());
        }

//        HSSFRow titulo = sheet.createRow(0);
//        HSSFCell cellFiltros = titulo.createCell(0);
//        cellFiltros.setCellStyle(headerStyle);
//        cellFiltros.setCellValue(nombComer);
        HSSFRow desdeHasta = sheet.createRow(0);
        HSSFCell cellFiltros = desdeHasta.createCell(0);
        cellFiltros.setCellValue(
                "PETROLRIOS C.A.: Listado de Facturas: " + fechaDesde
                + " - " + fechaHasta);

        List<String> listaIdFacturas = listenvF.stream().map(p -> p.getFactura().getFacturaPK().getNumero())
                .collect(Collectors.toList());
        System.out.println("Valores recibidos: " + fechaDesde + " " + fechaHasta);
        System.out.println("Total id facturas: " + listaIdFacturas.toArray().length);

        for (int i = 0; i < listenvF.size(); ++i) {
            try {
                HSSFRow dataRow = sheet.createRow(i + 3);
                Factura f = listenvF.get(i).getFactura();
                List<Detallefactura> df = listenvF.get(i).getDetalle();
                String sri = listenvF.get(i).getEnsri();

                if (df != null) {
                    dataRow.createCell(0).setCellValue(f.getActiva() == true ? "LIQU" : "ANUL");
                    dataRow.createCell(1).setCellValue(f.getFacturaPK().getNumeronotapedido());
                    dataRow.createCell(2).setCellValue(f.getNombrecliente());
                    dataRow.createCell(3).setCellValue(f.getCampoadicionalCampo3().trim());
                    dataRow.createCell(4).setCellValue(f.getCodigocliente());
                    dataRow.createCell(5).setCellValue(f.getSeriesri().substring(0, 3));
                    dataRow.createCell(6).setCellValue(f.getCodigoterminal());
                    dataRow.createCell(7).setCellValue(f.getFechaventa());
                    dataRow.createCell(8).setCellValue(f.getFechaacreditacionprorrogada());
                    dataRow.createCell(9).setCellValue(f.getFechaventa());
                    dataRow.createCell(10).setCellValue(df.get(0).getVolumennaturalautorizado().setScale(2, RoundingMode.HALF_UP).toString());
                    dataRow.getCell(10).setCellStyle(numerosStyle);
                    for (int c = 0; c < df.size(); c++) {
                        if (df.get(c).getNombreproducto() != null) {
                            dataRow.createCell(11).setCellValue(df.get(c).getNombreproducto());
                        }
                    }
                    dataRow.createCell(12).setCellValue(f.getValorconrubro().toString());
                    dataRow.getCell(12).setCellStyle(numerosStyle);
                    dataRow.createCell(13).setCellValue(f.getFacturaPK().getNumero());
                    dataRow.createCell(14).setCellValue(f.getNumeroautorizacion());
                    dataRow.createCell(15).setCellValue(f.getFechaautorizacion());
                    dataRow.createCell(16).setCellValue(f.getClaveacceso());
                }
            } catch (Exception ex) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL CREAR EXCEL:" + ex);
            }
        }

        CellStyle cellStyleTotal = workbook.createCellStyle();
        cellStyleTotal.setFont(font);
        cellStyleTotal.setAlignment(HorizontalAlignment.RIGHT);

//        HSSFRow rowTotales = sheet.createRow(9 + listenvF.size());
//        HSSFCell total = rowTotales.createCell(4);
//        total.setCellStyle(cellStyleTotal);
//        total.setCellValue("Total:");
//        total = rowTotales.createCell(9);
//        total.setCellType(CellType.STRING);
//        total.setCellFormula(String.format("SUM(J10:J%d)", 9 + transaccionesActuales.size()));
//        total.setCellStyle(numeros2Style);
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException ex) {
            System.out.println("Excepcion: " + ex);
        }
        if (outputStream.size() != 0) {
            byte[] bytes = outputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            docStream = new DefaultStreamedContent(inputStream, "application/xls", nombreDoc + ".xls");
            System.out.println("¡El archivo Excel se ha generado exitosamente!");
            outputStream.close();
        }
    }

    public void generarExcelPedidosAbastec(String fechaDesde, String fechaHasta, JSONArray pedidos) throws IOException {
        String nombreDoc = "OP-ABASTEC-" + fechaDesde.replace("/", "") + "-001";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "Orden_Pedido");
        String[] headers = new String[]{
            "Número de orden",
            "Fecha de emisión",
            "Número de línea",
            "ID único de línea",
            "Estado de línea",
            "Estación de servicio",
            "Cantón",
            "Provincia",
            "Comercializadora",
            "RUC de la estación de servicio",
            "N. de establecimiento",
            "Producto",
            "Unidad de medida",
            "Volumen solicitado",
            "Compartimento 1(gal)",
            "Compartimento 2(gal)",
            "Compartimento 3(gal)",
            "Compartimento 4(gal)",
            "Compartimento 5(gal)",
            "Número factura de comercializadora a Cliente Final",
            "Fechafactura de comercializadora a Cliente Final",
            "Sellos requeridos",
            "Terminal",
            "Placa del autotanque",
            "Transportista",
            "RUC del Transportista",
            "Cédula del conductor",
            "Nombre del conductor",
            "Apellido del conductor",
            "Teléfono del conductor",
            "Observaciones Comercializadora",
            "Alertas"

        };

        CellStyle numerosStyle = workbook.createCellStyle();
        numerosStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
        numerosStyle.setAlignment(HorizontalAlignment.RIGHT);

        CellStyle numeros2Style = workbook.createCellStyle();
        numeros2Style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

        HSSFFont font = workbook.createFont();
        font.setBold(true);
        numeros2Style.setFont(font);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(font);

        HSSFRow headerRow = sheet.createRow(2);
        for (int i = 0; i < headers.length; ++i) {
            String header = headers[i];
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header);
            sheet.autoSizeColumn(cell.getColumnIndex());
        }

//        HSSFRow titulo = sheet.createRow(0);
//        HSSFCell cellFiltros = titulo.createCell(0);
//        cellFiltros.setCellStyle(headerStyle);
//        cellFiltros.setCellValue(nombComer);
        HSSFRow desdeHasta = sheet.createRow(0);
        HSSFCell cellFiltros = desdeHasta.createCell(0);
        cellFiltros.setCellValue("OP-ABASTEC-" + fechaDesde.replace("/", "") + "-001");

//        List<String> listaIdFacturas = listenvF.stream().map(p -> p.getFactura().getFacturaPK().getNumero())
//                .collect(Collectors.toList());
//        System.out.println("Valores recibidos: " + fechaDesde + " " + fechaHasta);
//        System.out.println("Total id facturas: " + listaIdFacturas.toArray().length);
        
            try {

                for (int indice = 0; indice < pedidos.length(); indice++) {
                    if (!pedidos.isNull(indice)) {
                        JSONObject nt = pedidos.getJSONObject(indice);

                        JSONObject objNotapedido = nt.getJSONObject("notapedido");
                        JSONObject objDetalle = nt.getJSONObject("detalle");

                        HSSFRow dataRow = sheet.createRow(indice + 3);
//                Factura f = listenvF.get(i).getFactura();
//                List<Detallefactura> df = listenvF.get(i).getDetalle();
//                String sri = listenvF.get(i).getEnsri();

                        dataRow.createCell(0).setCellValue("OP-ABASTEC-" + fechaDesde.replace("/", "") + "-001");

                        dataRow.createCell(1).setCellValue("-");
                        dataRow.createCell(2).setCellValue("-");
                        dataRow.createCell(3).setCellValue("-");
                        dataRow.createCell(4).setCellValue("SOLICITADA");

                        // CELDA 5 CAMPO CLIENTE.NOMBRECOMERCIAL
                        JSONObject npCliente = objNotapedido.getJSONObject("codigocliente");
//                    String ruc = npCliente.getString("nombrecomercial");
                        dataRow.createCell(5).setCellValue(npCliente.getString("nombrecomercial"));

                        dataRow.createCell(6).setCellValue("-");
                        dataRow.createCell(7).setCellValue("-");
                        dataRow.createCell(8).setCellValue("-");
                        dataRow.createCell(9).setCellValue("-");
                        dataRow.createCell(10).setCellValue("-");

                        // CELDA 11 CAMPO detalleNP.NOMBREPRODUCTO
                        JSONObject producto = objDetalle.getJSONObject("producto");
//                  String codigoProducto = producto.getString("codigo");
                        dataRow.createCell(11).setCellValue(producto.getString("nombre"));

                        dataRow.createCell(12).setCellValue("GALONES");

                        // CELDA 12 CAMPO detalleNP.VOLUMENAUTORIZADO
                        dataRow.createCell(13).setCellValue(objDetalle.getBigDecimal("volumennaturalautorizado").setScale(2, RoundingMode.HALF_UP).toString());

                        // CELDAS 6 PARA LOS COMPARTIMENTOS
                        dataRow.createCell(14).
                                setCellValue(objDetalle.getBigDecimal("compartimento1").setScale(2, RoundingMode.HALF_UP).toString());

                        dataRow.createCell(15).
                                setCellValue(objDetalle.getBigDecimal("compartimento2").setScale(2, RoundingMode.HALF_UP).toString());

                        dataRow.createCell(16).
                                setCellValue(objDetalle.getBigDecimal("compartimento3").setScale(2, RoundingMode.HALF_UP).toString());

                        dataRow.createCell(17).
                                setCellValue(objDetalle.getBigDecimal("compartimento4").setScale(2, RoundingMode.HALF_UP).toString());

                        dataRow.createCell(18).
                                setCellValue(objDetalle.getBigDecimal("compartimento5").setScale(2, RoundingMode.HALF_UP).toString());

                        dataRow.createCell(19).setCellValue(objNotapedido.getString("numerofacturasri"));

                        // CAMPO FECHAVENTA TIPO DATE
                        long fechaMillis = objNotapedido.getLong("fechaventa");

                        Date fechaVenta = new Date(fechaMillis);

                        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

                        String fechaVentaString = formato.format(fechaVenta);

                        dataRow.createCell(20).setCellValue(fechaVentaString);

                        //CALCULO DE CANTIDAD DE SELLOS
                        int sellosTotales
                                = 0 +(objDetalle.getBigDecimal("compartimento1").compareTo(BigDecimal.ZERO) != 0 ? 2 : 0)
                                + (objDetalle.getBigDecimal("compartimento2").compareTo(BigDecimal.ZERO) != 0 ? 2 : 0)
                                + (objDetalle.getBigDecimal("compartimento3").compareTo(BigDecimal.ZERO) != 0 ? 2 : 0)
                                + (objDetalle.getBigDecimal("compartimento4").compareTo(BigDecimal.ZERO) != 0 ? 2 : 0)
                                + (objDetalle.getBigDecimal("compartimento5").compareTo(BigDecimal.ZERO) != 0 ? 2 : 0);

                        dataRow.createCell(21).setCellValue(String.valueOf(sellosTotales));

                        // CAMPO 22 NP.codigoterminal.NOMBRE
                        JSONObject npTerminal = objNotapedido.getJSONObject("codigoterminal");
//                    String ruc = npCliente.getString("nombrecomercial");
                        dataRow.createCell(22).setCellValue(npTerminal.getString("nombre"));

                        dataRow.createCell(23).setCellValue(objNotapedido.getString("codigoautotanque"));
                        dataRow.createCell(24).setCellValue("-");
                        dataRow.createCell(25).setCellValue("-");
                        dataRow.createCell(26).setCellValue(objNotapedido.getString("cedulaconductor"));
                        dataRow.createCell(27).setCellValue("-");
                        dataRow.createCell(28).setCellValue("-");
                        dataRow.createCell(29).setCellValue("-");
                        dataRow.createCell(30).setCellValue("CARGA COMPARTIDA");
                        dataRow.createCell(31).setCellValue("-");

                    }
                }
            } catch (Exception ex) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL CREAR EXCEL:" + ex);
            } 

        CellStyle cellStyleTotal = workbook.createCellStyle();
        cellStyleTotal.setFont(font);
        cellStyleTotal.setAlignment(HorizontalAlignment.RIGHT);

//        HSSFRow rowTotales = sheet.createRow(9 + listenvF.size());
//        HSSFCell total = rowTotales.createCell(4);
//        total.setCellStyle(cellStyleTotal);
//        total.setCellValue("Total:");
//        total = rowTotales.createCell(9);
//        total.setCellType(CellType.STRING);
//        total.setCellFormula(String.format("SUM(J10:J%d)", 9 + transaccionesActuales.size()));
//        total.setCellStyle(numeros2Style);
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException ex) {
            System.out.println("Excepcion: " + ex);
        }
        if (outputStream.size() != 0) {
            byte[] bytes = outputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            docStream = new DefaultStreamedContent(inputStream, "application/xls", nombreDoc + ".xls");
            System.out.println("¡El archivo Excel se ha generado exitosamente!");
            outputStream.close();
        }
    }

    public void generarExcelFacturacionlfc(String fechaDesde, String fechaHasta, List<EnvioFactura> listenvF) throws IOException {
        String nombreDoc = "LISTADOFACTURASCOBRANZA_" + fechaDesde.replace("/", "") + "_" + fechaHasta.replace("/", "");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "ARCHIVO");
        String[] headers = new String[]{
            "Status",
            "No. de Orden",
            "No. de Factura",
            "Nombre del Cliente",
            "Nombre Comercial",
            "Código",
            "Cod.Estab",
            "Código Propio",
            "Valor",
            "Forma de Pago",
            "Fecha de Emisión",
            "Fecha de Vencimiento",
            "Fecha de Pago",
            "Oficina "
        };

        CellStyle numerosStyle = workbook.createCellStyle();
        numerosStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
        numerosStyle.setAlignment(HorizontalAlignment.RIGHT);

        CellStyle numeros2Style = workbook.createCellStyle();
        numeros2Style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

        HSSFFont font = workbook.createFont();
        font.setBold(true);
        numeros2Style.setFont(font);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(font);

        HSSFRow headerRow = sheet.createRow(2);
        for (int i = 0; i < headers.length; ++i) {
            String header = headers[i];
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header);
            sheet.autoSizeColumn(cell.getColumnIndex());
        }

//        HSSFRow titulo = sheet.createRow(0);
//        HSSFCell cellFiltros = titulo.createCell(0);
//        cellFiltros.setCellStyle(headerStyle);
//        cellFiltros.setCellValue(nombComer);
        HSSFRow desdeHasta = sheet.createRow(0);
        HSSFCell cellFiltros = desdeHasta.createCell(0);
        cellFiltros.setCellValue(
                "PETROLRIOS C.A.: Listado de Facturas Cobranzas: " + fechaDesde
                + " - " + fechaHasta);

        List<String> listaIdFacturas = listenvF.stream().map(p -> p.getFactura().getFacturaPK().getNumero())
                .collect(Collectors.toList());
        System.out.println("Valores recibidos: " + fechaDesde + " " + fechaHasta);
        System.out.println("Total id facturas: " + listaIdFacturas.toArray().length);

        for (int i = 0; i < listenvF.size(); ++i) {
            try {
                HSSFRow dataRow = sheet.createRow(i + 3);
                Factura f = listenvF.get(i).getFactura();
                List<Detallefactura> df = listenvF.get(i).getDetalle();
                String sri = listenvF.get(i).getEnsri();

                if (df != null) {
                    dataRow.createCell(0).setCellValue(f.getPagada() == true ? "LIQU" : "Pendiente");
                    dataRow.createCell(1).setCellValue(f.getFacturaPK().getNumeronotapedido());
                    dataRow.createCell(2).setCellValue(f.getFacturaPK().getNumero());
                    dataRow.createCell(3).setCellValue(f.getNombrecliente());
                    dataRow.createCell(4).setCellValue(f.getCampoadicionalCampo3().trim());
                    dataRow.createCell(5).setCellValue("E - " + f.getCodigocliente());
                    dataRow.createCell(6).setCellValue(f.getSeriesri().substring(0, 3));
                    dataRow.createCell(7).setCellValue("");
                    dataRow.createCell(8).setCellValue(f.getValorconrubro().toString());
                    dataRow.createCell(9).setCellValue(f.getCampoadicionalCampo1());
                    dataRow.createCell(10).setCellValue(f.getFechaventa());
                    dataRow.createCell(11).setCellValue(f.getFechaacreditacionprorrogada());
                    dataRow.createCell(12).setCellValue("");
                    dataRow.createCell(13).setCellValue(f.getCodigoterminal());
                }
            } catch (Exception ex) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL CREAR EXCEL:" + ex);
            }
        }

        CellStyle cellStyleTotal = workbook.createCellStyle();
        cellStyleTotal.setFont(font);
        cellStyleTotal.setAlignment(HorizontalAlignment.RIGHT);

//        HSSFRow rowTotales = sheet.createRow(9 + listenvF.size());
//        HSSFCell total = rowTotales.createCell(4);
//        total.setCellStyle(cellStyleTotal);
//        total.setCellValue("Total:");
//        total = rowTotales.createCell(9);
//        total.setCellType(CellType.STRING);
//        total.setCellFormula(String.format("SUM(J10:J%d)", 9 + transaccionesActuales.size()));
//        total.setCellStyle(numeros2Style);
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException ex) {
            System.out.println("Excepcion: " + ex);
        }
        if (outputStream.size() != 0) {
            byte[] bytes = outputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            docStream = new DefaultStreamedContent(inputStream, "application/xls", nombreDoc + ".xls");
            System.out.println("¡El archivo Excel se ha generado exitosamente!");
            outputStream.close();
        }
    }

// REPORTE DE CLIENTE Y SUS PRECIOS
    public void generarReporteClientePrecio() throws Throwable {
        listenvCliPre = new ArrayList<>();
        unEnvioClientePrecio = new EnvioClientePrecio();
        System.out.println("FT:: (1 en) generarReporteClientePrecio");
        listenvCliPre = listaPrecioServicio.obtenerClientePrecio(codComer);
        System.out.println("FT:: (1 en) generarReporteClientePrecio - SALIÓ DE listaPrecioServicio.obtenerClientePrecio(codComer) CANTIDAD DE CLIENTES Y PRECIOS RECUPERADOS. " + listenvCliPre.size());
        if (!listenvCliPre.isEmpty()) {
            generarExcelClientePrecio(listenvCliPre);
        } else {
            this.dialogo(FacesMessage.SEVERITY_INFO, "NO SE ENCONTRARON REGISTROS");
        }
    }

    public void generarExcelClientePrecio(List<EnvioClientePrecio> unalistenvCliPre) throws Throwable {

        System.out.println("FT:: ENTRA EN METODO generarExcelClientePrecio(List<EnvioClientePrecio> unalistenvCliPre)");
        String nombreDoc = "LISTADO_CLIENTE_PRECIOSACTIVOS-" + new Date();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "ARCHIVO");
        String[] headers = new String[]{
            "CLIENTE",
            "CLIENTE",
            "LISTA DE PRECIO",
            "TERMINAL",
            "PRODUCTO",
            "CODIGO PRECIO",
            "FECHA INICIO",
            "PRECIO DEL TERMINAL EPP",
            "IVA",
            "IVA PRESUNTIVO",
            "MARGEN / PROCENTAJE",
            "PRECIO PRODUCTO",
            "TRES X MIL",};

        CellStyle numerosStyle = workbook.createCellStyle();
        numerosStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
        numerosStyle.setAlignment(HorizontalAlignment.RIGHT);

        CellStyle numeros2Style = workbook.createCellStyle();
        numeros2Style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

        HSSFFont font = workbook.createFont();
        font.setBold(true);
        numeros2Style.setFont(font);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(font);

        HSSFRow headerRow = sheet.createRow(2);
        for (int i = 0; i < headers.length; ++i) {
            String header = headers[i];
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header);
            sheet.autoSizeColumn(cell.getColumnIndex());
        }

//        HSSFRow titulo = sheet.createRow(0);
//        HSSFCell cellFiltros = titulo.createCell(0);
//        cellFiltros.setCellStyle(headerStyle);
//        cellFiltros.setCellValue(nombComer);
        HSSFRow desdeHasta = sheet.createRow(0);
        HSSFCell cellFiltros = desdeHasta.createCell(0);
        cellFiltros.setCellValue("Listado de Clientes y sus Precios activos");

        List<String> listaIdCliente = listenvCliPre.stream().map(p -> p.getCliente())
                .collect(Collectors.toList());
        System.out.println("Total de clientes: " + listaIdCliente.toArray().length);

        for (int i = 0; i < listenvCliPre.size(); ++i) {
            try {
                HSSFRow dataRow = sheet.createRow(i + 3);
                EnvioClientePrecio f = listenvCliPre.get(i);

                if (f != null) {
                    dataRow.createCell(0).setCellValue(f.getCliente().substring(0, 8));
                    dataRow.createCell(1).setCellValue(f.getCliente().substring(9));
                    dataRow.createCell(2).setCellValue(f.getListaprecio());
                    dataRow.createCell(3).setCellValue(f.getTerminal());
                    dataRow.createCell(4).setCellValue(f.getProducto());
                    dataRow.createCell(5).setCellValue(f.getCodigoprecio());
//                    dataRow.createCell(5).setCellValue(  f.getActivo().trim());
                    dataRow.createCell(6).setCellValue(f.getFechainicio());
//                    dataRow.createCell(7).setCellValue(f.getGravamen());
//                    dataRow.createCell(8).setCellValue(f.getValor());

                    dataRow.createCell(7).setCellValue(f.getPrecioterminalepp());
                    dataRow.createCell(8).setCellValue(f.getIva());
//                    dataRow.createCell(7).setCellValue(f.getMargencomercializacion());
                    dataRow.createCell(9).setCellValue(f.getIvapresuntivo());
                    dataRow.createCell(10).setCellValue(f.getMargenxcliente());
                    dataRow.createCell(11).setCellValue(f.getPrecioproducto());
                    dataRow.createCell(12).setCellValue(f.getTrexmil());
                }
            } catch (Throwable ex) {
                System.out.println("FT:: Error en metodo generarExcelClientePrecio (1): " + ex);
                ex.printStackTrace(System.out);
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL CREAR EXCEL:" + ex);
            }
        }

        CellStyle cellStyleTotal = workbook.createCellStyle();
        cellStyleTotal.setFont(font);
        cellStyleTotal.setAlignment(HorizontalAlignment.RIGHT);
        try {
            workbook.write(outputStream);
            workbook.close();

            if (outputStream.size() != 0) {
                byte[] bytes = outputStream.toByteArray();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                docStream = new DefaultStreamedContent(inputStream, "application/xls", nombreDoc + ".xls");
                System.out.println("¡El archivo Excel se ha generado exitosamente!");
                outputStream.close();
            }
        } catch (Throwable ex) {
            System.out.println("FT:: Error en metodo generarExcelClientePrecio (2): " + ex);
            ex.printStackTrace(System.out);
        }
    }

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            switch (dataUser.getUser().getNiveloperacion()) {
                case "cero":
                    habilitarComer = true;
                    habilitarCli = true;
                    habilitarTerminal = true;
                    break;
                case "adco":
                    habilitarComer = false;
                    habilitarCli = true;
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
                    habilitarCli = false;
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
                    habilitarCli = true;
                    habilitarTerminal = false;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                            comercializadora = listaComercializadora.get(i);
                            break;
                        }
                    }
                    seleccionarComerc();
                    break;
                default:
                    break;
            }
        }
    }

    public List<Listaprecio> getListaListaprecios() {
        return listaListaprecios;
    }

    public void setListaListaprecios(List<Listaprecio> listaListaprecios) {
        this.listaListaprecios = listaListaprecios;
    }

    public Listaprecio getListaPrecio() {
        return listaPrecio;
    }

    public void setListaPrecio(Listaprecio listaPrecio) {
        this.listaPrecio = listaPrecio;
    }

    public ListaprecioServicio getListaPrecioServicio() {
        return listaPrecioServicio;
    }

    public void setListaPrecioServicio(ListaprecioServicio listaPrecioServicio) {
        this.listaPrecioServicio = listaPrecioServicio;
    }

    public long getCodigoListaprecio() {
        return codigoListaprecio;
    }

    public void setCodigoListaprecio(long codigoListaprecio) {
        this.codigoListaprecio = codigoListaprecio;
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

}
