/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.pedidosyfacturacion.bean;


import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.configuration.Fichero;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.primefaces.PrimeFaces;
import org.primefaces.apollo.domain.Document;
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
    Variable para renderizar la pantalla
     */
    protected boolean mostarFactura;
    /*
    Variable para renderizar la pantalla
     */
    protected boolean mostarPantallaInicial;
     /*
    Varaible para guardar la selección del radio button
    /*
    Vairbale para almacenar el pdf generado
     */
    protected StreamedContent pdfStream;
    private StreamedContent txtStream;
    private String rutaArchivos = "";
    
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
    
    }
    public StreamedContent getPdfStream() {
        return pdfStream;
    }

    public void setPdfStream(StreamedContent pdfStream) {
        this.pdfStream = pdfStream;
    }

    
    public void descargarxml(){
        try {
            //rutaArchivos= "C:\\archivos\\docs";
            rutaArchivos= Fichero.getCARPETAREPORTES()+"\\xml"; 
        System.out.println("FT::(0100)-INICIO descargarxml");
        lecturaXml();
        } catch(Throwable t){
            System.out.println("FT::::(0100)-error "+t.getMessage());
            t.printStackTrace(System.out);
        }
            
    }
    
    public void lecturaXml() throws Throwable {
        File folder = new File(rutaArchivos);
        List<String> listaArchivos = new ArrayList<>();  
        for (File file : folder.listFiles()) {
            if (!file.isDirectory()) {
//                System.out.println("----------------------------");
                listaArchivos.add(file.getName());

            }
        }       
        zip(listaArchivos);
    }
    
    
    
    public void zip(List<String> listaArchivos) {        
        byte[] buffer = new byte[1024];
        String nombreArchivo = "facturasautorizadas.zip";
        try {
            FileOutputStream fos = new FileOutputStream(rutaArchivos+ nombreArchivo);
            System.out.println("ZIP. CREADO.:"+fos.toString());
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (int i = 0; i < listaArchivos.size(); i++) {
                ZipEntry ze = new ZipEntry(listaArchivos.get(i));
                zos.putNextEntry(ze);
                //FileInputStream in = new FileInputStream(Fichero.getCARPETAREPORTES() + listaArchivos.get(i));
                FileInputStream in = new FileInputStream(rutaArchivos+listaArchivos.get(i));
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
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
    public void descargar(String nombre) throws Throwable {
        File initialFile = new File(rutaArchivos + nombre);
        System.out.println("FT:: descargar . AbsolutePath"+initialFile.getAbsolutePath()+"CanonicalPath"+initialFile.getCanonicalPath());
        InputStream targetStream = new FileInputStream(initialFile);
        txtStream = new DefaultStreamedContent(targetStream, "application/txt", nombre);
    }
}
