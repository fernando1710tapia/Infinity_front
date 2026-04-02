package ec.com.infinityone.bean;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class SoporteBean implements Serializable {

    private String nombre;
    private String correo;
    private String asunto;
    private String mensaje;

    public SoporteBean() {
    }

    @PostConstruct
    public void init() {
        // Inicialización si es proyectada
    }

    public void enviar() {
        // Aquí se implementaría la lógica de envío de correo
        // Por ahora, solo mostramos un mensaje de éxito
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje Enviado", "Gracias por contactarnos. Responderemos lo antes posible."));
        
        // Limpiamos los campos
        nombre = "";
        correo = "";
        asunto = "";
        mensaje = "";
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
