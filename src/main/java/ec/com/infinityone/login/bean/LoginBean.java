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
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private String[] preguntas;
    private List<String> listaPreguntas;

    private static final String secretKeyAES = "supertech-infinityonedigitoclave";
    private static final String saltAES = "super1234";

    @PostConstruct
    public void init() {
        String a = "1";
        Fichero.propiedades();
        login = new LoginBean();
        usuarioL = new Usuario();
        preguntas = Fichero.getPREGUNTAS().split(",");
        cambiarP();
    }

    public void cambiarP() {
        if (dataUser.isActualizarD()) {
            usuarioL = dataUser.getUser();
        }
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
        listaPreguntas = new ArrayList<>();
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
                        LOG.log(Level.INFO, "Ingresa al sistema el usuario: {0}", usuarioLog);
                        if (usuarioL.getClave().equals(DigestUtils.sha256Hex("xxxx"))) {
                            dataUser.setActualizarD(true);
                            Set<Integer> generados = new HashSet<>();
                            for (int i = 0; i < 3; i++) {
                                int aleatorio = -1;
                                boolean generado = false;
                                while (!generado) {
                                    int posible = (int) (Math.random() * preguntas.length - 1);
                                    if (!generados.contains(posible)) {
                                        generados.add(posible);
                                        aleatorio = posible;
                                        listaPreguntas.add(preguntas[aleatorio]);
                                        if (i == 0) {
                                            usuarioL.setPregunta1(preguntas[aleatorio]);
                                        }
                                        if (i == 1) {
                                            usuarioL.setPregunta2(preguntas[aleatorio]);
                                        }
                                        if (i == 2) {
                                            usuarioL.setPregunta3(preguntas[aleatorio]);
                                        }
                                        generado = true;
                                    }
                                }
                            }
                            FacesContext.getCurrentInstance().getExternalContext().redirect(Fichero.getRUTAACTUALIZAR());
                        } else {
                            FacesContext.getCurrentInstance().getExternalContext().redirect(Fichero.getRUTADASHBOARD());
                        }

                    }
                }

            }
        } catch (Exception ex) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Usuario o Contraseña Incorrecta");
            LOG.log(Level.SEVERE, "Error al verificar usuario: {0}", ex);
        }
        return null;
    }

    public void save() {
        try {
            String respuesta = "";
            if (usuarioL.getRespuesta1() != null || usuarioL.getRespuesta2() != null || usuarioL.getRespuesta3() != null){
                if (!usuarioL.getRespuesta1().isEmpty()) {
                    respuesta = usuarioL.getRespuesta1();
                }
                if (!usuarioL.getRespuesta2().isEmpty()) {
                    respuesta = usuarioL.getRespuesta2();
                }
                if (!usuarioL.getRespuesta3().isEmpty()) {
                    respuesta = usuarioL.getRespuesta3();
                }
                if (!respuesta.isEmpty()) {
                    if (!usuarioL.getClave().equals(DigestUtils.sha256Hex("xxxx"))) {
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
                        //obj.put("hash", usuarioL.getHash());
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
                            FacesContext.getCurrentInstance().getExternalContext().redirect(Fichero.getRUTADASHBOARD());
                            this.dialogo(FacesMessage.SEVERITY_INFO, "USUARIO ACTUALIZADO EXITOSAMENTE");
                        } else {
                            this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
                        }
                        System.out.println(connection.getResponseCode());
                        System.out.println(connection.getResponseMessage());
                    } else {
                        this.dialogo(FacesMessage.SEVERITY_ERROR, "Ingrese la nueva contraseña");
                    }
                } else {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "Ingrese al menos una respuesta en las preguntas de control");
                }
            } else{
                this.dialogo(FacesMessage.SEVERITY_ERROR, "Ingrese al menos una respuesta en las preguntas de control");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void continuar() {
        String respuesta;
        respuesta = usuarioL.getRespuesta1();
        if (respuesta.isEmpty()) {
            respuesta = usuarioL.getRespuesta2();
        }
        if (respuesta.isEmpty()) {
            respuesta = usuarioL.getRespuesta3();
        }
        if (!respuesta.isEmpty()) {
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Ingrese al menos una respuesta");
        }
    }

    public void continuarClave() {
        if (!usuarioL.getClave().equals("xxxx")) {
            PrimeFaces.current().executeScript("PF('nuevoPass').hide()");
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Ingrese la nueva contraseña");
        }
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
