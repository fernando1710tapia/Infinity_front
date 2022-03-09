/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.login.bean;

import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Usuario;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class RecoverpasswordBean extends ReusableBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(RecoverpasswordBean.class.getName());

    @Inject
    DatosUsuario dataUser;

    private String claveUsuario;
    private String usuarioLog;
    private String correoLog;
    private String claveLog;
    private String claveRepLog;
    private Usuario usuarioL;
    private RecoverpasswordBean recoverpassword;
    private String clave;

    @PostConstruct
    public void init() {
        String a = "1";
        Fichero.propiedades();
        recoverpassword = new RecoverpasswordBean();
        usuarioL = new Usuario();
    }

    public void mostrarMensaje(FacesMessage.Severity severityMessage, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(severityMessage, "Información: " + mensaje, ""));
    }

    public void obtenerUsuario(String usuarioLog, String claveLog) {
        try {
            clave = DigestUtils.sha256Hex(claveLog);
            //url = new URL("https://www.infinity.petroleosyservicios.com:8443/infinityone1/resources/usuario/login?user=" + usuarioLog + "&password=" + clave);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "usuario/login?user=" + usuarioLog + "&password=" + clave);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
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
                JSONObject user = retorno.getJSONObject(indice);
                usuarioL.setCodigo(user.getString("codigo"));
                usuarioL.setNombre(user.getString("nombre"));
                usuarioL.setNombrever(user.getString("nombrever"));
                usuarioL.setClave(user.getString("clave"));
                usuarioL.setNiveloperacion(user.getString("niveloperacion"));
                usuarioL.setActivo(user.getBoolean("activo"));
                if (!user.isNull("codigocomercializadora")) {
                    usuarioL.setCodigocomercializadora(user.getString("codigocomercializadora"));
                }
                if (!user.isNull("codigocliente")) {
                    usuarioL.setCodigocliente(user.getString("codigocliente"));
                }
                if (!user.isNull("codigoterminal")) {
                    usuarioL.setCodigoterminal(user.getString("codigoterminal"));
                }
            }
            Usuario obj = (Usuario) usuarioL;
            if (connection.getResponseCode() == 200) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", obj);
            } else {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Usuario o Contraseña Incorrecta");
        }
    }

    public String validarUsuario() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            usuarioLog = params.get("form:login");
            correoLog = params.get("form:correo");
            obtenerUsuarioCorreo(usuarioLog, correoLog);
            if (usuarioL != null) {
                PrimeFaces.current().executeScript("PF('nuevo').show()");
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Usuario y/o Correo incorrecto");
            }
        } catch (Exception ex) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "No se encuentra el usuario");
            LOG.log(Level.SEVERE, "Error al verificar usuario: {0}", ex);
        }
        return null;
    }

    public void obtenerUsuarioCorreo(String usuarioLog, String correoLog) {
        try {
            //url = new URL("https://www.infinity.petroleosyservicios.com:8443/infinityone1/resources/usuario/login?user=" + usuarioLog + "&password=" + clave);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.usuario/porUsuarioCorreo?codigo=" + usuarioLog + "&correo=" + correoLog);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
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
                JSONObject user = retorno.getJSONObject(indice);
                usuarioL.setCodigo(user.getString("codigo"));
                usuarioL.setNombre(user.getString("nombre"));
                usuarioL.setNombrever(user.getString("nombrever"));
                usuarioL.setClave(user.getString("clave"));
                usuarioL.setNiveloperacion(user.getString("niveloperacion"));
                usuarioL.setActivo(user.getBoolean("activo"));
                if (!user.isNull("codigocomercializadora")) {
                    usuarioL.setCodigocomercializadora(user.getString("codigocomercializadora"));
                }
                if (!user.isNull("codigocliente")) {
                    usuarioL.setCodigocliente(user.getString("codigocliente"));
                }
                if (!user.isNull("codigoterminal")) {
                    usuarioL.setCodigoterminal(user.getString("codigoterminal"));
                }
            }
            Usuario obj = (Usuario) usuarioL;
            if (connection.getResponseCode() == 200) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", obj);
            } else {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Usuario o Correo Incorrecto");
        }
    }

    public void continuar() {
        if (true) {
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            PrimeFaces.current().executeScript("PF('nuevoPass').show()");
        }
    }

    public void save() {
        if (true) {
            PrimeFaces.current().executeScript("PF('nuevoPass').hide()");
        }
    }

    public String getUsuarioLog() {
        return usuarioLog;
    }

    public void setUsuarioLog(String usuarioLog) {
        this.usuarioLog = usuarioLog;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    public String getClaveLog() {
        return claveLog;
    }

    public void setClaveLog(String claveLog) {
        this.claveLog = claveLog;
    }

    public RecoverpasswordBean getRecoverpassword() {
        return recoverpassword;
    }

    public void setRecoverpassword(RecoverpasswordBean recoverpassword) {
        this.recoverpassword = recoverpassword;
    }

    public Usuario getUsuarioL() {
        return usuarioL;
    }

    public void setUsuarioL(Usuario usuarioL) {
        this.usuarioL = usuarioL;
    }

    public String getCorreoLog() {
        return correoLog;
    }

    public void setCorreoLog(String correoLog) {
        this.correoLog = correoLog;
    }

    public String getClaveRepLog() {
        return claveRepLog;
    }

    public void setClaveRepLog(String claveRepLog) {
        this.claveRepLog = claveRepLog;
    }

}
