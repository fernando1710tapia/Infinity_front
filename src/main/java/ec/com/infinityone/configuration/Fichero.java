/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author HP
 */
@Named(value = "ficheroConfig")
@ApplicationScoped
public class Fichero {

    private static Properties propiedades;
    private static final Logger LOG = Logger.getLogger(Fichero.class.getName());

    private static String RUTAREPORTE;
    private static String RUTAXML;
    private static String RUTAAYUDA;
    private static String RUTASERVICIOSPERSISTENCIA;
    private static String RUTADASHBOARD;
    private static String RUTAACTUALIZAR;
    private static String CARPETAREPORTES;
    private static String CARPETAXML;
    private static String CARPETAAYUDA;
    private static String PRODUCTOSINFE;
    private static String RUTACONEXIONBD;
    private static String PREGUNTAS;
    private static String COLORESPRODUCTOS;
    private static String TASAINTERES;
    private static String FECHACERTIFICADOSSL;
    private static String RUTAECONSULTAS;

    public static void propiedades() {
        InputStream in = null;
        File archivo = null;
//        FileReader fr = null;
        try {
            //Inicializa la aplicación, cargando las configuraciones iniciales
            //File confDir = new File("C:\\Users\\HP\\Documents\\David\\Infinity");
            File confDir = new File(System.getProperty("jboss.server.config.dir"));
            System.out.println("Direccion: " + confDir.getAbsolutePath());
            File fileProp = new File(confDir, "ConfiguracionInfinity.properties");
            File rep = new File(confDir, "reportes");
            RUTAREPORTE = rep.toString();
            File ayu = new File(confDir, "ayuda");
            CARPETAAYUDA = ayu.toString();
            File xml = new File(confDir, "xml");
            CARPETAXML = xml.toString();
            in = new FileInputStream(fileProp);
            propiedades = new Properties();
            propiedades.load(in);
            in.close();

            CARPETAREPORTES = (String) propiedades.get("RUTAREPORTE");
            RUTAXML = (String) propiedades.get("RUTAXML");
            RUTAAYUDA = (String) propiedades.get("RUTAAYUDA");
            RUTASERVICIOSPERSISTENCIA = (String) propiedades.get("RUTASERVICIOSPERSISTENCIA");
            RUTADASHBOARD = (String) propiedades.get("RUTADASHBOARD");
            RUTAACTUALIZAR = (String) propiedades.get("RUTAACTUALIZAR");
            PRODUCTOSINFE = (String) propiedades.get("PRODUCTOSINFE");
            RUTACONEXIONBD = (String) propiedades.get("CONEXIONBD");
            PREGUNTAS = (String) propiedades.get("PREGUNTAS");
            COLORESPRODUCTOS = (String) propiedades.get("COLORESPRODUCTOS");
            TASAINTERES = (String) propiedades.get("TASAINTERES");
            FECHACERTIFICADOSSL = (String) propiedades.get("FECHACERTIFICADOSSL");
            RUTAECONSULTAS = (String) propiedades.get("RUTAECONSULTAS");            

            //LOG.log(Level.INFO, "Ruta Reporte", RUTAREPORTE);
            //LOG.log(Level.INFO, "Ruta Servicios", RUTASERVICIOSPERSISTENCIA);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error al inicializar la aplicaci\u00f3n, {0}", e);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al inicializar la aplicaci\u00f3n, {0}", e);
        }
    }

    public Properties getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(Properties propiedades) {
        this.propiedades = propiedades;
    }

    public static String getRUTAREPORTE() {
        return RUTAREPORTE;
    }

    public static void setRUTAREPORTE(String RUTAREPORTE) {
        Fichero.RUTAREPORTE = RUTAREPORTE;
    }

    public static String getRUTASERVICIOSPERSISTENCIA() {
        return RUTASERVICIOSPERSISTENCIA;
    }

    public static void setRUTASERVICIOSPERSISTENCIA(String RUTASERVICIOSPERSISTENCIA) {
        Fichero.RUTASERVICIOSPERSISTENCIA = RUTASERVICIOSPERSISTENCIA;
    }

    public static String getRUTADASHBOARD() {
        return RUTADASHBOARD;
    }

    public static void setRUTADASHBOARD(String RUTADASHBOARD) {
        Fichero.RUTADASHBOARD = RUTADASHBOARD;
    }

    public static String getCARPETAREPORTES() {
        return CARPETAREPORTES;
    }

    public static void setCARPETAREPORTES(String CARPETAREPORTES) {
        Fichero.CARPETAREPORTES = CARPETAREPORTES;
    }

    public static String getPRODUCTOSINFE() {
        return PRODUCTOSINFE;
    }

    public static void setPRODUCTOSINFE(String PRODUCTOSINFE) {
        Fichero.PRODUCTOSINFE = PRODUCTOSINFE;
    }

    public static String getRUTACONEXIONBD() {
        return RUTACONEXIONBD;
    }

    public static void setRUTACONEXIONBD(String RUTACONEXIONBD) {
        Fichero.RUTACONEXIONBD = RUTACONEXIONBD;
    }

    public static String getPREGUNTAS() {
        return PREGUNTAS;
    }

    public static void setPREGUNTAS(String PREGUNTAS) {
        Fichero.PREGUNTAS = PREGUNTAS;
    }

    public static String getRUTAACTUALIZAR() {
        return RUTAACTUALIZAR;
    }

    public static void setRUTAACTUALIZAR(String RUTAACTUALIZAR) {
        Fichero.RUTAACTUALIZAR = RUTAACTUALIZAR;
    }

    public static String getRUTAAYUDA() {
        return RUTAAYUDA;
    }

    public static void setRUTAAYUDA(String RUTAAYUDA) {
        Fichero.RUTAAYUDA = RUTAAYUDA;
    }

    public static String getCARPETAAYUDA() {
        return CARPETAAYUDA;
    }

    public static void setCARPETAAYUDA(String CARPETAAYUDA) {
        Fichero.CARPETAAYUDA = CARPETAAYUDA;
    }

    public static String getRUTAXML() {
        return RUTAXML;
    }

    public static void setRUTAXML(String RUTAXML) {
        Fichero.RUTAXML = RUTAXML;
    }

    public static String getCARPETAXML() {
        return CARPETAXML;
    }

    public static void setCARPETAXML(String CARPETAXML) {
        Fichero.CARPETAXML = CARPETAXML;
    }

    public static String getCOLORESPRODUCTOS() {
        return COLORESPRODUCTOS;
    }

    public static void setCOLORESPRODUCTOS(String COLORESPRODUCTOS) {
        Fichero.COLORESPRODUCTOS = COLORESPRODUCTOS;
    }

    public static String getTASAINTERES() {
        return TASAINTERES;
    }

    public static void setTASAINTERES(String TASAINTERES) {
        Fichero.TASAINTERES = TASAINTERES;
    }

    public static String getFECHACERTIFICADOSSL() {
        return FECHACERTIFICADOSSL;
    }

    public static void setFECHACERTIFICADOSSL(String FECHACERTIFICADOSSL) {
        Fichero.FECHACERTIFICADOSSL = FECHACERTIFICADOSSL;
    }

    public static String getRUTAECONSULTAS() {
        return RUTAECONSULTAS;
    }

    public static void setRUTAECONSULTAS(String RUTAECONSULTAS) {
        Fichero.RUTAECONSULTAS = RUTAECONSULTAS;
    }

}
