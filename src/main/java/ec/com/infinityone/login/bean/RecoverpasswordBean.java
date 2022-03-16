/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.login.bean;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Usuario;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
    private String respuesta;
    private String claveNueva;    

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

    public void validarUsuario() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            usuarioLog = params.get("form:login");
            correoLog = params.get("form:correo");
            obtenerUsuarioCorreo(usuarioLog, correoLog);
            if (usuarioL.getCodigo() != null) {               
                PrimeFaces.current().executeScript("PF('nuevo').show()");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "Usuario y/o Correo Incorrecto");
                //mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Usuario y/o Correo incorrecto");
            }
        } catch (Exception ex) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "No se encuentra el usuario");
            LOG.log(Level.SEVERE, "Error al verificar usuario: {0}", ex);
        }
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
                usuarioL.setCedula(user.getString("cedula"));
                usuarioL.setNombre(user.getString("nombre"));
                usuarioL.setNombrever(user.getString("nombrever"));
                if (!user.isNull("codigocliente")) {
                    usuarioL.setCodigocliente(user.getString("codigocliente"));
                }
                if (!user.isNull("codigocomercializadora")) {
                    usuarioL.setCodigocomercializadora(user.getString("codigocomercializadora"));
                }
                usuarioL.setActivo(user.getBoolean("activo"));
                usuarioL.setNiveloperacion(user.getString("niveloperacion"));
                if (!user.isNull("hash")) {
                    usuarioL.setHash(user.getString("hash"));
                }
                if (!user.isNull("vigenciahash")) {
                    Long lDateVig = user.getLong("vigenciahash");
                    Date dateVig = new Date(lDateVig);
                    usuarioL.setVigenciahash(dateVig);
                }
                usuarioL.setClave(user.getString("clave").trim());
                usuarioL.setUsuarioactual(user.getString("usuarioactual"));
                if (!user.isNull("codigoterminal")) {
                    usuarioL.setCodigoterminal(user.getString("codigoterminal"));
                }
                if (!user.isNull("correo")) {
                    usuarioL.setCorreo(user.getString("correo"));
                }
                if (!user.isNull("pregunta1")) {
                    usuarioL.setPregunta1(user.getString("pregunta1"));
                }
                if (!user.isNull("respuesta1")) {
                    usuarioL.setRespuesta1(user.getString("respuesta1"));
                }
                if (!user.isNull("pregunta2")) {
                    usuarioL.setPregunta2(user.getString("pregunta2"));
                }
                if (!user.isNull("respuesta2")) {
                    usuarioL.setRespuesta2(user.getString("respuesta2"));
                }
                if (!user.isNull("pregunta3")) {
                    usuarioL.setPregunta3(user.getString("pregunta3"));
                }
                if (!user.isNull("respuesta3")) {
                    usuarioL.setRespuesta3(user.getString("respuesta3"));
                }
            }
            Usuario obj = (Usuario) usuarioL;
            if (connection.getResponseCode() == 200) {
                //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", obj);
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
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        respuesta = params.get("formNuevoPass:respuesta1");
        if (respuesta.isEmpty()) {
            respuesta = params.get("formNuevoPass:respuesta2");
        }
        if (respuesta.isEmpty()) {
            respuesta = params.get("formNuevoPass:respuesta3");
        }
        if (!respuesta.isEmpty()) {
            if (respuesta.equals(usuarioL.getRespuesta1()) || respuesta.equals(usuarioL.getRespuesta2()) || respuesta.equals(usuarioL.getRespuesta3())) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                PrimeFaces.current().executeScript("PF('nuevoPass').show()");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "Respuesta incorrecta");
            }

        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Ingrese una respuesta");
        }
    }

    public void save() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        claveNueva = params.get("formNuevo:pwd1");
        if (!claveNueva.isEmpty()) {
            usuarioL.setClave(claveNueva);
            editItems();
            usuarioLog = "";
            correoLog = "";
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Ingrese una respuesta");
        }
    }

    public void editItems() {
        try {
            String respuesta;
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.usuario/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("codigo", usuarioL.getCodigo());
            obj.put("cedula", usuarioL.getCedula());
            obj.put("nombre", usuarioL.getNombre());
            obj.put("nombrever", usuarioL.getNombrever());
            obj.put("codigocliente", usuarioL.getCodigocliente());
            obj.put("activo", usuarioL.getActivo());
            obj.put("niveloperacion", usuarioL.getNiveloperacion());
            obj.put("codigocomercializadora", usuarioL.getCodigocomercializadora());
            obj.put("codigoterminal", usuarioL.getCodigoterminal());
            obj.put("hash", usuarioL.getHash());
            obj.put("vigenciahash", usuarioL.getVigenciahash());
            obj.put("clave", DigestUtils.sha256Hex(usuarioL.getClave()));
            obj.put("usuarioactual", usuarioL.getNombrever());
            obj.put("correo", usuarioL.getCorreo());
            obj.put("pregunta1", usuarioL.getPregunta1());
            obj.put("respuesta1", usuarioL.getRespuesta1());
            obj.put("pregunta2", usuarioL.getPregunta2());
            obj.put("respuesta2", usuarioL.getRespuesta2());
            obj.put("pregunta3", usuarioL.getPregunta3());
            obj.put("respuesta3", usuarioL.getRespuesta3());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevoPass').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "USUARIO ACTUALIZADO EXITOSAMENTE");
                init();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
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

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getClaveNueva() {
        return claveNueva;
    }

    public void setClaveNueva(String claveNueva) {
        this.claveNueva = claveNueva;
    }   

}
