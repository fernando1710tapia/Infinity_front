/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.pedidosyfacturacion.bean;

import ec.com.infinityone.actorcomercial.bean.ComercializadoraBean;
import ec.com.infinityone.actorcomercial.serivicios.ComercializadoraServicio;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.configuration.Fichero;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;

import org.primefaces.model.StreamedContent;

/**
 *
 * @author David
 */
@Named
@ViewScoped
public class Descargaxml extends ReusableBean implements Serializable {

    protected static final Logger LOG = Logger.getLogger(Descargaxml.class.getName());

    /*
    Variable para acceder a los servicios de Comercialziadora
     */
    @Inject
    private ComercializadoraServicio comerServicio;

    /*
    Variable para renderizar la pantalla
     */
    protected boolean mostarFactura;
    /*
    Variable para renderizar la pantalla
     */
    protected boolean mostarPantallaInicial;
    /*
     Vairbale para almacenar el pdf generado
     */
    protected StreamedContent pdfStream;
    private StreamedContent txtStream;
    private String rutaArchivos = "";

    private ComercializadoraBean comercializadora;

    private List<ComercializadoraBean> listaComercializadora;

    private String codComer;

    protected Date fechaB;

    private InputStream targetStream;

    /**
     * Constructor por defecto
     */
    public Descargaxml() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {

        mostarFactura = false;
        mostarPantallaInicial = true;
        fechaB = new Date();
        obtenerComercializadora();
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = comerServicio.obtenerComercializadoras();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda();
        }
    }

    public void seleccionarComercializdora() {
        if (comercializadora != null) {
            codComer = comercializadora.getCodigo();
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
                    }
                }
            }
            if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
            }
        }
    }

    public StreamedContent getPdfStream() {
        return pdfStream;
    }

    public void setPdfStream(StreamedContent pdfStream) {
        this.pdfStream = pdfStream;
    }

    public void descargarxml() {
        try {
            if (comercializadora != null) {
                //Date date = new Date();
                SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
                SimpleDateFormat getMonthFormat = new SimpleDateFormat("MM");
                String currentYear = getYearFormat.format(fechaB);
                String currentMonth = getMonthFormat.format(fechaB);
                //rutaArchivos= "C:\\archivos\\docs";
                //rutaArchivos= Fichero.getCARPETAXML(); 
                rutaArchivos = Fichero.getRUTAXML() + "/" + currentYear + "/" + currentMonth + "/" + comercializadora.getRuc();
                //rutaArchivos = Fichero.getRUTAXML();
                System.out.println("FT::(Direccion: " + rutaArchivos);
                System.out.println("FT::(0100)-INICIO descargarxml");
                lecturaXml();
            } else {
                this.dialogo(FacesMessage.SEVERITY_WARN, "Seleccione una comercializadora");
            }
        } catch (Throwable t) {
            System.out.println("FT::::(0100)-error " + t.getMessage());
            t.printStackTrace(System.out);
        }

    }

    public void lecturaXml() throws Throwable {
        String fe = "";
        SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy"); 
        Calendar cArchivos = Calendar.getInstance();
        Calendar cFechaE = Calendar.getInstance();
        cFechaE.setTime(fechaB);
        File folder = new File(rutaArchivos);
        List<String> listaArchivos = new ArrayList<>();
        for (File file : folder.listFiles()) {
            if (!file.isDirectory()) {
                int i = file.getName().lastIndexOf('.');
                if (i > 0) {
                    fe = file.getName().substring(i + 1);
                }
                System.out.println("AS: Archivo antes de if xml" + file.getName() + file.lastModified());
                if (fe.equals("xml")) {
                    System.out.println("AS: Archivo despues" + file.getName() + file.lastModified());

                    String dateArchivo = file.getName().substring(0,8);
                    cArchivos.setTime(formato.parse(dateArchivo));
                    //String date = cArchivos.get(Calendar.DAY_OF_MONTH) + "/" + cArchivos.get(Calendar.MONTH) + "/" + cArchivos.get(Calendar.YEAR);
                    //System.out.println("----" + date);
                    if (cArchivos.get(Calendar.DAY_OF_MONTH) == cFechaE.get(Calendar.DAY_OF_MONTH)
                            && cArchivos.get(Calendar.MONTH) == cFechaE.get(Calendar.MONTH)
                            && cArchivos.get(Calendar.YEAR) == cFechaE.get(Calendar.YEAR)) {
                        listaArchivos.add(file.getName());
                    }
                }
            }
        }
        if (!listaArchivos.isEmpty()) {
            zip(listaArchivos);
        } else {
            this.dialogo(FacesMessage.SEVERITY_WARN, "No se encontraron archivos");
            if (targetStream != null) {
                targetStream.close();
            }
        }
    }

    public void zip(List<String> listaArchivos) {
        byte[] buffer = new byte[1024];
        String nombreArchivo = "facturasautorizadas.zip";
        try {
            FileOutputStream fos = new FileOutputStream(rutaArchivos + nombreArchivo);
            System.out.println("ZIP. CREADO.:" + fos.toString());
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (int i = 0; i < listaArchivos.size(); i++) {
                ZipEntry ze = new ZipEntry(listaArchivos.get(i));
                zos.putNextEntry(ze);
                //FileInputStream in = new FileInputStream(Fichero.getCARPETAREPORTES() + listaArchivos.get(i));
                FileInputStream in = new FileInputStream(rutaArchivos + "/" + listaArchivos.get(i));
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
            }
            zos.closeEntry();
            zos.close();
            System.out.println("ZIP. LISTO");
            descargar(nombreArchivo);
            fos.close();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public void descargar(String nombre) throws Throwable {
        try {
            File initialFile = new File(rutaArchivos + nombre);
            System.out.println("FT:: descargar . AbsolutePath" + initialFile.getAbsolutePath() + "CanonicalPath" + initialFile.getCanonicalPath());
            targetStream = new FileInputStream(initialFile);
            txtStream = new DefaultStreamedContent(targetStream, "application/txt", nombre);
        } catch (Throwable t) {
            System.out.println("Error: " + t);
        }
    }

    public StreamedContent getTxtStream() {
        return txtStream;
    }

    public void setTxtStream(StreamedContent txtStream) {
        this.txtStream = txtStream;
    }

    public Date getFechaB() {
        return fechaB;
    }

    public void setFechaB(Date fechaB) {
        this.fechaB = fechaB;
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

}
