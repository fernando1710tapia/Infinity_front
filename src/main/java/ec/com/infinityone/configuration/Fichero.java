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
    private static String RUTASERVICIOSPERSISTENCIA;
    private static String RUTADASHBOARD;
    private static String CARPETAREPORTES;
    private static String PRODUCTOSINFE;
    private static String RUTACONEXIONBD;
    

    public static void propiedades() {
        InputStream in = null;
        File archivo = null;
//        FileReader fr = null;
        try {
            //Inicializa la aplicación, cargando las configuraciones iniciales
            //File confDir = new File("C:\\Users\\HP\\Documents\\David\\Infinity");
            File confDir = new File(System.getProperty("jboss.server.config.dir"));
            System.out.println("FT::RUTA DE ARCHIVO DE CONFIGURACIONES CONFDIR::" +confDir.toString());
            File fileProp = new File(confDir, "ConfiguracionInfinity.properties");
            File rep= new File(confDir, "reportes");
            CARPETAREPORTES = rep.toString();
            in = new FileInputStream(fileProp);
            propiedades = new Properties();
            propiedades.load(in);
            in.close();

            RUTAREPORTE = (String) propiedades.get("RUTAREPORTE");
            RUTASERVICIOSPERSISTENCIA = (String) propiedades.get("RUTASERVICIOSPERSISTENCIA");
            RUTADASHBOARD = (String) propiedades.get("RUTADASHBOARD");
            PRODUCTOSINFE = (String) propiedades.get("PRODUCTOSINFE");
            RUTACONEXIONBD = (String) propiedades.get("CONEXIONBD");

            //LOG.log(Level.INFO, "Ruta Reporte", RUTAREPORTE);
            //LOG.log(Level.INFO, "Ruta Servicios", RUTASERVICIOSPERSISTENCIA);

        } catch (Throwable e) {
            System.out.println("FT::ERROR EN propiedades " +e.getMessage());
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
    
    

}

