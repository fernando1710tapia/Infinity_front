package ec.com.infinityone.bean;

import ec.com.infinityone.configuration.Fichero;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

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

        //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido";
        try {
        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.usuario/ingresarsoporte?"
                + "correo=" + URLEncoder.encode(this.correo, "UTF-8")
                + "&nombre=" + URLEncoder.encode(this.nombre, "UTF-8")
                + "&asunto=" + URLEncoder.encode(this.asunto, "UTF-8")
                + "&mensaje=" + URLEncoder.encode(this.mensaje, "UTF-8");

        
        // + "correo=" + URLEncoder.encode("micorreo4@gma.com", "UTF-8")
        

            URL url = new URL(direcc);

            System.out.println("FT:: SOPORTE INGRESADO - url:: " + url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
             
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());

                BufferedReader br = new BufferedReader(reader);
                String tmp = null;
                String resp = "";
                while ((tmp = br.readLine()) != null) {
                    resp += tmp;
                }
                JSONObject objetoJson = new JSONObject(resp);
                JSONArray retorno = objetoJson.getJSONArray("retorno");
                if (connection.getResponseCode() == 200) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje Enviado", "Gracias por contactarnos. Recibirás un correo con la atención a tu requerimiento de soporte!"));

                }

                // Limpiamos los campos
                nombre = "";
                correo = "";
                asunto = "";
                mensaje = "";

            } catch (Throwable t) {
                System.out.println("AS:: ERROR Inicio!!! " + t.getMessage());
                t.printStackTrace(System.out);
                System.out.println("AS:: ERROR Fin!!! " + t.getMessage());
            } 
      
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
