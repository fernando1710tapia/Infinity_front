package ec.com.infinityone.bean.pedidosyfacturacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Autotanque;
import ec.com.infinityone.reusable.ReusableBean;

/**
 * Pantalla "Mensaje de Sellos" (pedidosyfacturacion/mensajesellos.xhtml).
 *
 * Permite elegir un autotanque y una solicitud / nota de pedido e ingresar el
 * link del rastreo satelital. La accion de "Enviar" aun esta por definir.
 */
@Named
@ViewScoped
public class MensajeSellosBean extends ReusableBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(MensajeSellosBean.class.getName());

    private static final Pattern PATRON_URL = Pattern.compile("^(https?://)[^\\s/$.?#].[^\\s]*$",
            Pattern.CASE_INSENSITIVE);

    /** Autotanques activos (se cargan desde el servicio REST de autotanques). */
    private List<Autotanque> listaAutotanques;

    /** Solicitudes / notas de pedido. Pendiente de definir su origen. */
    private List<SolicitudNotaPedidoItem> listaSolicitudes;

    /** Placa del autotanque seleccionado. */
    private String placaAutotanque;

    /** Codigo de la solicitud / nota de pedido seleccionada. */
    private String codigoSolicitud;

    /** Link del rastreo satelital. */
    private String linkRastreo;

    @PostConstruct
    public void init() {
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.autotanque";
        listaAutotanques = new ArrayList<>();
        listaSolicitudes = new ArrayList<>();
        limpiar();
        obtenerAutotanques();
        obtenerSolicitudes();
    }

    /**
     * Carga los autotanques activos con el mismo servicio REST que usa
     * AutotanqueBean.
     */
    public void obtenerAutotanques() {
        listaAutotanques = new ArrayList<>();
        HttpURLConnection connection = null;
        try {
            url = new URL(direccion);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            StringBuilder respuesta = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String tmp;
                while ((tmp = br.readLine()) != null) {
                    respuesta.append(tmp);
                }
            }

            JSONObject json = new JSONObject(respuesta.toString());
            if (json.has("retorno") && !json.isNull("retorno")) {
                JSONArray retorno = json.getJSONArray("retorno");
                for (int i = 0; i < retorno.length(); i++) {
                    JSONObject o = retorno.getJSONObject(i);
                    if (o.optBoolean("activo", true)) {
                        Autotanque a = new Autotanque();
                        a.setPlaca(o.optString("placa", "").trim());
                        a.setActivo(true);
                        if (!a.getPlaca().isEmpty()) {
                            listaAutotanques.add(a);
                        }
                    }
                }
            }
        } catch (IOException | RuntimeException e) {
            LOG.log(Level.SEVERE, "Error al cargar autotanques", e);
            mensaje(FacesMessage.SEVERITY_ERROR, "No se pudo cargar la lista de autotanques");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Carga las solicitudes / notas de pedido. Por ahora queda vacio: el origen
     * de la informacion aun no esta definido.
     */
    public void obtenerSolicitudes() {
        listaSolicitudes = new ArrayList<>();
    }

    /**
     * Accion del boton "Enviar". El comportamiento real aun no esta definido; por
     * ahora solo valida los datos.
     */
    public void enviar() {
        if (placaAutotanque == null || placaAutotanque.trim().isEmpty()) {
            mensaje(FacesMessage.SEVERITY_WARN, "Seleccione un autotanque");
            return;
        }
        if (codigoSolicitud == null || codigoSolicitud.trim().isEmpty()) {
            mensaje(FacesMessage.SEVERITY_WARN, "Seleccione una solicitud / nota de pedido");
            return;
        }
        if (linkRastreo == null || linkRastreo.trim().isEmpty()) {
            mensaje(FacesMessage.SEVERITY_WARN, "Ingrese el link del rastreo satelital");
            return;
        }
        if (!PATRON_URL.matcher(linkRastreo.trim()).matches()) {
            mensaje(FacesMessage.SEVERITY_WARN, "El link debe iniciar con http:// o https://");
            return;
        }

        // TODO: definir la accion de envio.
        LOG.info("MensajeSellos: autotanque=" + placaAutotanque + ", solicitud=" + codigoSolicitud + ", link="
                + linkRastreo.trim());
        mensaje(FacesMessage.SEVERITY_INFO, "Datos validados. La accion de envio aun no esta definida");
    }

    /** Accion del boton "Cancelar". */
    public void cancelar() {
        limpiar();
    }

    private void limpiar() {
        placaAutotanque = null;
        codigoSolicitud = null;
        linkRastreo = null;
    }

    private void mensaje(FacesMessage.Severity severidad, String texto) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, texto, null));
    }

    /** Elemento del combo "Solicitud / Nota de pedido". */
    public static class SolicitudNotaPedidoItem implements Serializable {

        private static final long serialVersionUID = 1L;

        private String codigo;
        private String descripcion;

        public SolicitudNotaPedidoItem() {
        }

        public SolicitudNotaPedidoItem(String codigo, String descripcion) {
            this.codigo = codigo;
            this.descripcion = descripcion;
        }

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }

    public List<Autotanque> getListaAutotanques() {
        return listaAutotanques;
    }

    public List<SolicitudNotaPedidoItem> getListaSolicitudes() {
        return listaSolicitudes;
    }

    public String getPlacaAutotanque() {
        return placaAutotanque;
    }

    public void setPlacaAutotanque(String placaAutotanque) {
        this.placaAutotanque = placaAutotanque;
    }

    public String getCodigoSolicitud() {
        return codigoSolicitud;
    }

    public void setCodigoSolicitud(String codigoSolicitud) {
        this.codigoSolicitud = codigoSolicitud;
    }

    public String getLinkRastreo() {
        return linkRastreo;
    }

    public void setLinkRastreo(String linkRastreo) {
        this.linkRastreo = linkRastreo;
    }
}
