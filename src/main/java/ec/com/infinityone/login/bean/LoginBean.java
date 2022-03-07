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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class LoginBean extends ReusableBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(LoginBean.class.getName());

    @Inject
    DatosUsuario dataUser;

    private String claveUsuario;
    private String usuarioLog;
    private String claveLog;
    private Usuario usuarioL;
    private LoginBean login;
    private String clave;

    private static final String secretKeyAES = "supertech-infinityonedigitoclave";
    private static final String saltAES = "super1234";

    @PostConstruct
    public void init() {
        String a = "1";
        Fichero.propiedades();
        login = new LoginBean();
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

            usuarioL = new Usuario();

            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject user = retorno.getJSONObject(indice);
//                usuarioL.setCodigo(user.getString("codigo"));
//                usuarioL.setNombre(user.getString("nombre"));
//                usuarioL.setNombrever(user.getString("nombrever"));
//                usuarioL.setClave(user.getString("clave"));
//                usuarioL.setNiveloperacion(user.getString("niveloperacion"));
//                usuarioL.setActivo(user.getBoolean("activo"));
//                if (!user.isNull("codigocomercializadora")) {
//                    usuarioL.setCodigocomercializadora(user.getString("codigocomercializadora"));
//                }
//                if (!user.isNull("codigocliente")) {
//                    usuarioL.setCodigocliente(user.getString("codigocliente"));
//                }
//                if (!user.isNull("codigoterminal")) {
//                    usuarioL.setCodigoterminal(user.getString("codigoterminal"));
//                }

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

    public Usuario buscar() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        usuarioLog = params.get("form:codigo");
        obtenerClave(usuarioLog);
        return usuarioL;
    }

    public void obtenerClave(String usuarioLog) {
        try {
            //url = new URL("https://www.infinity.petroleosyservicios.com:8443/infinityone1/resources/usuario/login?user=" + usuarioLog + "&password=" + clave);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.usuario/porId?codigo=" + usuarioLog);
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
            usuarioL = new Usuario();
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject user = retorno.getJSONObject(indice);
                usuarioL.setCodigo(user.getString("codigo"));                  
                usuarioL.setClave(user.getString("clave"));
            }

            if (connection.getResponseCode() == 200) {

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
            claveLog = params.get("form:password");
            obtenerUsuario(usuarioLog, claveLog);
            if (usuarioL != null) {
                if (!usuarioL.getClave().trim().equals(clave)) {
                    mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Usuario y/o Clave incorrecta");
                } else {
                    if (!usuarioL.getActivo()) {
                        mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Usuario está inactivo, favor contactar al Administrador");
                    } else {
                        x = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
                        dataUser.setUser(x);
                        dataUser.setUserConected(x.getNombrever());
                        dataUser.setProductoSinFe(Fichero.getPRODUCTOSINFE());
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("login");
                        //FacesContext.getCurrentInstance().getExternalContext().redirect("/infinityandres/dashboard.xhtml");
                        FacesContext.getCurrentInstance().getExternalContext().redirect(Fichero.getRUTADASHBOARD());
                        LOG.log(Level.INFO, "Ingresa al sistema el usuario: {0}", usuarioLog);
                        return "/dashboard.xhtml?faces-redirect=true";
                    }
                }

            }
        } catch (Exception ex) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Usuario o Contraseña Incorrecta");
            LOG.log(Level.SEVERE, "Error al verificar usuario: {0}", ex);
        }
        return null;
    }

    public void logout() {
        try {
            ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().getExternalContext().
                    redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/" + "login.xhtml");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error método logout {0}", ex);
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Ocurrio un error " + e.getMessage());
        }
    }

    public void doLogout() throws NoSuchAlgorithmException, IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/login.xhtml");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        session.setAttribute("user", login);
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

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public Usuario getUsuarioL() {
        return usuarioL;
    }

    public void setUsuarioL(Usuario usuarioL) {
        this.usuarioL = usuarioL;
    }

}
