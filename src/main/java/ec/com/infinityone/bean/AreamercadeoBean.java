/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean;

import ec.com.infinityone.bean.HttpServlet;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.ObjetoNivel1;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
//import net.sf.jasperreports.engine.JasperCompileManager;
//import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author David
 */
@Named
@ViewScoped
public class AreamercadeoBean extends ReusableBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(AreamercadeoBean.class.getName());
    /*
    Variable que almacena varias Areamercadeo
     */
    private List<ObjetoNivel1> listAreaMercadeo;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarArea;
    /*
    Variable que establece true or false para el estado del Area
     */
    private boolean estadoArea;
    /*
    Variable para obtener la url
     */
    private HttpServlet http;

    private StreamedContent pdfStream;

    private int numSecuencia;

    /**
     * Constructor por defecto
     */
    public AreamercadeoBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.areamercadeo";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.areamercadeo";
        editarArea = false;
        objeto = new ObjetoNivel1();
        obtenerAreaMercadeo();        
        //getURL();
    }

    public void getURL() {
        //String scheme = http.getScheme();             // http
        //String ur = http.getRequestURL().toString();
        //StringBuffer a = req.getRequestURL(); 
        String serverName = http.getServerName();     // hostname.com
        int serverPort = http.getServerPort();        // 80
        String contextPath = http.getContextPath();   // /mywebapp
        String servletPath = http.getServletPath();   // /servlet/MyServlet
        String pathInfo = http.getPathInfo();         // /a/b ;c=123
        String queryString = http.getQueryString();          // d=789
        // Reconstruct original requesting URL
        StringBuilder url = new StringBuilder();
        //url.append(scheme).append("://").append(serverName);

        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }

        url.append(contextPath).append(servletPath);

        if (pathInfo != null) {
            url.append(pathInfo);
        }
        if (queryString != null) {
            url.append("?").append(queryString);
        }
    }

    public void obtenerAreaMercadeo() {
        //urlInfinity = "http://192.168.121.10:8080/infinityweb1/resources/ec.com.infinity.modelo.areamercadeo
        //http://192.168.121.10:8080/infinityone1/resources/usuario/login?user=Ftapia&password=123456";
        try {

            System.out.println("AREA MERCADEO");
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));              
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            System.out.println(url);
            //connection.setRequestProperty("Authorization", token);
            //connection.setRequestProperty("Access-Control-Allow-Headers", "Authorization");
            //connection.setRequestProperty("Accept-Charset", "utf-8");
            //connection.setRequestProperty("Accept-Encoding", "gzip");
            //connection.setRequestProperty("Accept-Language", "en-US");
            //connection.setRequestProperty("Access-Control-Allow-Origin", "*");

            listAreaMercadeo = new ArrayList<>();
            objeto = new ObjetoNivel1();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject areaM = retorno.getJSONObject(indice);
                objeto.setCodigo(areaM.getString("codigo"));
                objeto.setNombre(areaM.getString("nombre"));
                if (areaM.getBoolean("activo") == true) {
                    objeto.setActivo("S");
                } else {
                    objeto.setActivo("N");
                }
                objeto.setUsuario(areaM.getString("usuarioactual"));
                listAreaMercadeo.add(objeto);
                objeto = new ObjetoNivel1();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (editarArea) {
            editItems();
            obtenerAreaMercadeo();
        } else {
            addItems();
            obtenerAreaMercadeo();
        }
    }

    public void addItems() {
        try {
            String respuesta;
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("codigo", objeto.getCodigo());
            obj.put("nombre", objeto.getNombre());
            obj.put("activo", estadoArea);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevaAreaM').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "ÁREA MERCADEO REGISTRADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editItems() {
        try {
            String respuesta;
            url = new URL(direccion + "/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("codigo", objeto.getCodigo());
            obj.put("nombre", objeto.getNombre());
            obj.put("activo", estadoArea);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevaAreaM').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "ÁREA MERCADEO ACUTALIZADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
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
            url = new URL(direccion + "/porId?codigo=" + objeto.getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
            PrimeFaces.current().executeScript("PF('nuevaAreaM').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "ÁREA MERCADEO ELIMINADA EXITOSAMENTE");
                obtenerAreaMercadeo();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevaAreamercadeo() {
        estadoArea = true;
        editarArea = false;
        soloLectura = false;        
        objeto = new ObjetoNivel1();       
        PrimeFaces.current().executeScript("PF('nuevaAreaM').show()");
    }

    public ObjetoNivel1 editarAreamercadeo(ObjetoNivel1 obj) {
        editarArea = true;
        soloLectura = true;
        objeto = obj;
        if (objeto.getActivo() == "S") {
            estadoArea = true;
        } else {
            estadoArea = false;
        }
        PrimeFaces.current().executeScript("PF('nuevaAreaM').show()");
        return objeto;
    }

    public void generarReporte() {
//        String path = "C:\\Users\\HP\\JaspersoftWorkspace\\ReportesInfinity\\Prueba.jrxml";
//        InputStream file = null;
//        try {
//            file = new FileInputStream(new File(path));
//            // ventaSeleccionada = ventasDAO.obtenerUltimaVenta();
//
//            JasperReport reporte = JasperCompileManager.compileReport(file);
//
//            Map parametro = new HashMap();
//
//           // parametro.put("idVenta", ventaSelected.getIdVenta());
//            
//           // parametro.put("nBoletos", nBoletos);
//            
//            Connection conexion = conexionJasperBD();
//
//            JasperPrint print = JasperFillManager.fillReport(reporte, parametro, conexion);
//
//            File directory = new File("C:\\Users\\HP\\Documents\\David\\Espe\\9no Semestre\\Arquitectura de Software\\Segundo Parcial\\PROYECTO\\proyectoMonster");
//
//            String nombreDocumento = "MonsterTicket";
//
//            File pdf = File.createTempFile(nombreDocumento + "_", ".pdf", directory);
//            JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
//            File initialFile = new File(pdf.getAbsolutePath());
//            InputStream targetStream = new FileInputStream(initialFile);
//            pdfStream = new DefaultStreamedContent();
//            System.err.print(pdf.getAbsolutePath());
//            System.out.println(pdf.getAbsolutePath());
//        } catch (Exception ex) {
//            //ex.printStackTrace();
//            System.out.println("Excepcion: " + ex);
//        }
    }

    public Connection conexionJasperBD() {

        Connection conexion = null;
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://200.93.248.121/infinity_one", "postgres", "1nf1n1ty");
            System.out.println("Conexion exitosa");

        } catch (Exception ex) {
            System.out.println("Excepcion: " + ex);
        }

        return conexion;
    }

    public ObjetoNivel1 getObjeto() {
        return objeto;
    }

    public void setObjeto(ObjetoNivel1 objeto) {
        this.objeto = objeto;
    }

    public List<ObjetoNivel1> getListAreaMercadeo() {
        return listAreaMercadeo;
    }

    public void setListAreaMercadeo(List<ObjetoNivel1> listAreaMercadeo) {
        this.listAreaMercadeo = listAreaMercadeo;
    }

    public boolean isEstadoArea() {
        return estadoArea;
    }

    public void setEstadoArea(boolean estadoArea) {
        this.estadoArea = estadoArea;
    }
}
