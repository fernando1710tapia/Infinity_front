/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.reusable;

import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.serivicio.actorcomercial.ClienteServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.bean.login.DatosUsuario;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.ObjetoNivel1;
import ec.com.infinityone.modelo.Usuario;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;

/**
 *
 * @author David
 */
public class ReusableBean extends ObjetoNivel1 {

    @Inject
    protected DatosUsuario dataUser;
    /*
    Variable para enviar el url
     */
    protected URL url;
    /*
    Varibale que contiene los atributos, codigo, nombre.... etc.
     */
    protected ObjetoNivel1 objeto;
    /*
    Variable de la dirección url
     */
    protected String direccion;

    protected Usuario x;
    /*
    Variable que permite solo leer los datos de las tablas
     */
    protected boolean soloLectura;
    /*
    Variable que habilita el uso de la comercializadora dependiendo del perfil
     */
    protected boolean habilitarComer;
    /*
    Variable que habilita el uso del cliente dependiendo del perfil
     */
    protected boolean habilitarCli;
    /*
    Variable que habilita el uso del terminal dependiendo del perfil
     */
    protected boolean habilitarTerminal;

    protected void dialogo(FacesMessage.Severity severityMessage,
            String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(severityMessage,
                "Información: " + mensaje, ""));
        PrimeFaces.current().ajax().update("form:messages");
    }

    protected void logErrorResponse(HttpURLConnection connection) throws IOException {
        System.out.println("Error al añadir: " + connection.getResponseCode());
        System.out.println("Error: " + connection.getErrorStream());
        System.out.println(connection.getResponseMessage());
        this.dialogo(FacesMessage.SEVERITY_ERROR, connection.getResponseMessage());
    }

    public Connection conexionJasperBD() {

        Connection conexion = null;

        try {
            Class.forName("org.postgresql.Driver");
//            conexion = DriverManager.getConnection("jdbc:postgresql://200.93.248.121/infinity_one", "postgres", "1nf1n1ty");
            conexion = DriverManager.getConnection("jdbc:postgresql://" + Fichero.getRUTACONEXIONBD() + "/infinity_one", "postgres", "1nf1n1ty");
            System.out.println("Conexion exitosa");
        } catch (Exception ex) {
            System.out.println("Excepcion: " + ex);
        }

        return conexion;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public ObjetoNivel1 getObjeto() {
        return objeto;
    }

    public void setObjeto(ObjetoNivel1 objeto) {
        this.objeto = objeto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public boolean isHabilitarComer() {
        return habilitarComer;
    }

    public void setHabilitarComer(boolean habilitarComer) {
        this.habilitarComer = habilitarComer;
    }

    public boolean isHabilitarCli() {
        return habilitarCli;
    }

    public void setHabilitarCli(boolean habilitarCli) {
        this.habilitarCli = habilitarCli;
    }

    public boolean isHabilitarTerminal() {
        return habilitarTerminal;
    }

    public void setHabilitarTerminal(boolean habilitarTerminal) {
        this.habilitarTerminal = habilitarTerminal;
    }

}
