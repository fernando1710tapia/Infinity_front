/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.reportes;

import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Listaprecio;
import ec.com.infinityone.servicio.preciosyfacturacion.ListaprecioServicio;
import ec.com.infinityone.reusable.ReusableBean;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author HP
 */
@Named
@ViewScoped
public class ReportepreciosBean extends ReusableBean implements Serializable {

    /*
    Variable que trae los mètodos de lista Precios
     */
    @Inject
    private ListaprecioServicio listaPrecioServicio;
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
    Variable que almacena el nombre de la comercializadora
     */
    private String nomComer;
    /*
    Vairbale para almacenar el pdf generado
     */
    private StreamedContent pdfStream;
    /*
    Variable para almacenar los datos comercializadora
     */
    private List<ComercializadoraBean> listaComercializadora;

    /**
     * Constructor por defecto
     */
    public ReportepreciosBean() {
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
            nomComer = comercializadora.getNombre();
        }
    }

    public void generarReporte() {
        //String path = "C:\\archivos\\Template\\reportePrecios.jrxml";
        String rutaGuardar = Fichero.getCARPETAREPORTES();
        String path = Fichero.getCARPETAREPORTES() + "/reportePrecios.jrxml";
        System.out.println("PATH:" + path);
        InputStream file = null;
        try {
            file = new FileInputStream(new File(path));

            JasperReport reporte = JasperCompileManager.compileReport(file);
            BufferedImage image = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/logo.jpeg"));
            //BufferedImage image = ImageIO.read(new File("C:\\archivos\\Template\\logo.jpg"));
            Map parametro = new HashMap();

            parametro.put("codComer", codComer);
            parametro.put("logo", image);
            parametro.put("Titulo", " Precios - "+nomComer.trim());
            parametro.put("Subtitulo", "Precios Activos");
            parametro.put("usuario", dataUser.getUser().getNombrever());

            //System.out.println("PARAMETROS: " + parametro);
            Connection conexion = conexionJasperBD();

            //System.out.println("CONEXIÓN: " + conexion);
            JasperPrint print = JasperFillManager.fillReport(reporte, parametro, conexion);

            //File directory = new File("C:\\Archivos");
            File directory = new File(rutaGuardar);
            String nombreDocumento = "reportePrecios";

            File pdf = File.createTempFile(nombreDocumento + "_", ".pdf", directory);
            JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
            File initialFile = new File(pdf.getAbsolutePath());
            InputStream targetStream = new FileInputStream(initialFile);
            //pdfStream = new DefaultStreamedContent();
            pdfStream = new DefaultStreamedContent(targetStream, "application/pdf", nombreDocumento + ".pdf");
            //DefaultStreamedContent.builder().contentType("application/pdf").name(nombreDocumento + ".pdf").stream(() -> new FileInputStream(targetStream)).build();
            System.err.print(pdf.getAbsolutePath());
            System.out.println(pdf.getAbsolutePath());
        } catch (Exception ex) {
            //ex.printStackTrace();
            System.out.println("Excepcion: " + ex);
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
//                        listaClientes = clienteServicio.obtenerClientesActivosPorComercializadora(comercializadora.getCodigo());
//                        for (int i = 0; i < listaClientes.size(); i++) {
//                            if (listaClientes.get(i).getCodigo().equals(dataUser.getUser().getCodigocliente())) {
//                                this.cliente = listaClientes.get(i);
//                                break;
//                            }
//                        }
                    } else {
                        this.dialogo(FacesMessage.SEVERITY_FATAL, "La comercializadora se encuentra deshabilitada");
                    }
//                    seleccionarCliente();

//                    for (int i = 0; i < listaTermianles.size(); i++) {
//                        if (listaTermianles.get(i).getCodigo().equals(cliente.getCodigoterminaldefecto().getCodigo())) {
//                            terminal = listaTermianles.get(i);
//                            break;
//                        }
//                    }
//                    seleccionarTerminal();
                    //listaClientes = clienteServicio.obtenerClientesPorComercializadora(comercializadora.getCodigo());

                    //seleccionarCliente();
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
//                    for (int i = 0; i < listaTermianles.size(); i++) {
//                        if (listaTermianles.get(i).getCodigo().equals(dataUser.getUser().getCodigoterminal())) {
//                            terminal = listaTermianles.get(i);
//                        }
//                    }
//                    seleccionarTerminal();
//                    List<Cliente> listaClientesAux = new ArrayList<>();
//                    listaClientesAux = listaClientes;
//                    for (int i = 0; i < listaClientesAux.size(); i++) {
//                        if (!listaClientes.get(i).getCodigoterminaldefecto().getCodigo().equals(codTerminal)) {
//                            listaClientes.remove(i);
//                        }
//                    }
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

    public StreamedContent getPdfStream() {
        return pdfStream;
    }

    public void setPdfStream(StreamedContent pdfStream) {
        this.pdfStream = pdfStream;
    }

}
