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
import ec.com.infinityone.servicio.pedidosyfacturacion.FacturaServicio;
import ec.com.infinityone.servicio.preciosyfacturacion.ListaprecioServicio;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.servicio.pedidosyfacturacion.FacturaComercialServicio;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class ArchivoFacturaComercialBean extends ReusableBean implements Serializable {

    /*
    Variable que trae los mètodos de lista Precios
     */
    @Inject
    private ListaprecioServicio listaPrecioServicio;
    /*
    Variable que trae los mètodos de factura
     */
    @Inject
    private FacturaComercialServicio facturaComercialServicio;
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
    private List<FacturaComercialDto> listFacturaComercialDto;

    /**
     * Constructor por defecto
     */
    public ArchivoFacturaComercialBean() {
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

    public void generarArchivo() throws ParseException, IOException, Throwable {
        listFacturaComercialDto = new ArrayList<>();
        /*fechas para comparar entre las dos y establecer un rango de 30 dias*/
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        String dateI = sdf.format(fechaI);
        String dateF = sdf.format(fechaf);

        Date firstDate = sdf.parse(dateI);
        Date secondDate = sdf.parse(dateF);

        long diff = secondDate.getTime() - firstDate.getTime();
        TimeUnit time = TimeUnit.DAYS;
        long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
        if (diffrence > 30) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "LA FECHA DE FIN NO PUEDE SER MAYOR A 30 DÍAS A LA FECHA DE INICIO");
        } else {
            listFacturaComercialDto = facturaComercialServicio.obtenerFacturas(fechaI, fechaf, codComer);
            if (!listFacturaComercialDto.isEmpty()) {
                crearArchivo(dateI, dateF, listFacturaComercialDto);
            } else {
                this.dialogo(FacesMessage.SEVERITY_INFO, "NO SE ENCONTRARON DOCUMENTOS");
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

    public String crearArchivo(String fechaDesde, String fechaHasta, List<FacturaComercialDto> listFacturaComercialDto) throws Throwable {
        FileWriter flwriter = null;
        String nombreArchivo = "";
        String lineaCabecera = "";
        String lineaCabeceraAux = "";
        try {
            nombreArchivo = "ARCHIVO_" + fechaDesde.replace("/", "") + "_" + fechaHasta.replace("/", "") + ".txt";
            flwriter = new FileWriter(Fichero.getCARPETAREPORTES() + nombreArchivo);
            String linea = "";
            long contadorFacturas = 0;
            BufferedWriter bfwriter = new BufferedWriter(flwriter);
            for (FacturaComercialDto facturaComercialDto : listFacturaComercialDto) {
                contadorFacturas++;
                lineaCabecera = generarLineaCabecera(facturaComercialDto.getFacturaComercialPKDto());
                if (!lineaCabecera.equals(lineaCabeceraAux)) {
                    bfwriter.write(lineaCabecera + "\n");
                    lineaCabeceraAux = "";
                    lineaCabeceraAux = lineaCabecera;
                }
                //Código De La Comercializadora
                linea = linea + facturaComercialDto.getDcodigocomercializadora() + ";";
                //Código Del Banco
                linea = linea + facturaComercialDto.getDcodigobanco() + ";";
                //Tipo De Registro
                linea = linea + facturaComercialDto.getDtiporegistro() + ";";
                //Numero De Orden
                linea = linea + facturaComercialDto.getDnumeroorden() + ";";
                //Código Del Cliente
                linea = linea + facturaComercialDto.getDcodigocliente() + ";";
                //Código De Establecimiento
                linea = linea + facturaComercialDto.getDcodigoestablecimiento() + ";";
                //Código Del Producto O Código Del Rubro
                linea = linea + facturaComercialDto.getDcodigoproducto() + ";";
                //Cantidad
                linea = linea + facturaComercialDto.getDcantidad() + ";";
                //Unidad De Medida
                linea = linea + facturaComercialDto.getDunidadmedida() + ";";
                //Precio Unitario
                linea = linea + facturaComercialDto.getDpreciounitario() + ";";
                //Total
                linea = linea + facturaComercialDto.getDtotal() + ";";
                //Tipo de Rubro
                linea = linea + facturaComercialDto.getDtiporubro() + ";";
                bfwriter.write(linea + "\n");
                linea = "";

            }
            bfwriter.close();
            this.dialogo(FacesMessage.SEVERITY_INFO, "Archivo creado satisfactoriamente..");
            System.out.println("Archivo creado satisfactoriamente..");
            descargar(nombreArchivo);
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

    public String generarLineaCabecera(FacturaComercialPKDto facturaComercialPKDto) throws Throwable {
        String lineaCabecera = "";
        try {
            //Código De La Comercializadora
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getCodigoComercializadora() + ";";
            //Código Del Banco            
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getCodigoBanco() + ";";
            //Tipo De Registro
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getTipoRegistro() + ";";
            //Numero De Orden
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getNumeroOrden() + ";";
            //Código Del Cliente
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getCodigoCliente() + ";";
            //Código De Establecimiento
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getCodigoEstablecimiento() + ";";
            //Fecha De Venta
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getFechaVenta() + ";";
            //Agencia
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getAgencia() + ";";
            //Código Del Terminal De Despacho
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getCodigoTerminal() + ";";
            //Valor Total De La Factura
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getValorTotal() + ";";
            //Fecha De Emision
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getFechaEmision() + ";";
            //Fecha De Vencimiento
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getFechaVencimiento() + ";";
            //Fecha De Postergación
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getFechaPostergacion() + ";";
            //Numero del sri
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getNumeroSri() + ";";
            //Estado de la factura
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getEstadoFactura() + ";";
            //Clave de Acceso
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getClaveAcceso() + ";";
            //Numero de autorización sri
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getNumeroAutorizacionsri() + ";";
            //Fecha de vigencia
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getFechaVigencia() + ";";
            //Fecha de caducidad
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getFechaCaducidad() + ";";
            //Forma de pago 1
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getFormaPago1() + ";";
            //Descripcion forma de pago 1
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getDescripcionFormapago1() + ";";
            //Numero cta. Forma de pago 1
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getNumeroCuentaformapago1() + ";";
            if (!facturaComercialPKDto.getCodigoBancoformapago1().isEmpty()) {
                String[] parts = facturaComercialPKDto.getCodigoBancoformapago1().split("-");
                //Numero de cheque Forma de pago 1
                lineaCabecera = lineaCabecera + parts[0] + ";";
                //Codigo de bco. Forma de pago 1            
                lineaCabecera = lineaCabecera + parts[1] + ";";
                //Descripcion del bco. Forma de pago 1
                lineaCabecera = lineaCabecera + parts[2] + ";";
            } else {
                //Numero de cheque Forma de pago 1
                lineaCabecera = lineaCabecera + facturaComercialPKDto.getNumeroChequeformapago1() + ";";
                //Codigo de bco. Forma de pago 1            
                lineaCabecera = lineaCabecera + facturaComercialPKDto.getCodigoBancoformapago1() + ";";
                //Descripcion del bco. Forma de pago 1
                lineaCabecera = lineaCabecera + facturaComercialPKDto.getDescripcionBancoformapago1() + ";";
            }
            //Forma de pago 225
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getFormaPago2() + ";";
            //Descripcion de la fo10rma de la Forma de pago 2
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getDescripcionBancoformapago2() + ";";
            //Numero de cta. De la Forma de pago 2
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getNumeroCuentaformapago2() + ";";
            //Numero de cheque de la Forma de pago 2
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getNumeroChequeformapago2() + ";";
            //Numero de banco de la Forma de pago 2
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getCodigoBancoformapago2() + ";";
            //Descripcion de banco de la Forma de pago 2
            lineaCabecera = lineaCabecera + facturaComercialPKDto.getDescripcionBancoformapago2() + ";";
            return lineaCabecera;
        } catch (Throwable t) {
            System.out.println("FT:: error capturado " + this.getClass() + "::" + t.getMessage());
            t.printStackTrace(System.out);
            return lineaCabecera;
        }
    }

    public void descargar(String nombre) throws FileNotFoundException {
        File initialFile = new File(Fichero.getCARPETAREPORTES() + nombre);
        InputStream targetStream = new FileInputStream(initialFile);
        docStream = new DefaultStreamedContent(targetStream, "application/txt", nombre);
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
            "IVA 12%",
            "IMPTO. 3X1000",
            "IVA PRESUNTIVO 12%",
            "Estado",
            "Oe enpetro",
            "En sri"
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
