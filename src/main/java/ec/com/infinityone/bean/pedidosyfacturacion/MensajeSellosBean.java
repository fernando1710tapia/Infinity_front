package ec.com.infinityone.bean.pedidosyfacturacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Autotanque;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.Terminal;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.serivicio.actorcomercial.ClienteServicio;
import ec.com.infinityone.servicio.catalogo.TerminalServicio;
import ec.com.infinityone.servicio.catalogo.UsuarioServicio;
import ec.com.infinityone.servicio.notificacion.ExpoPushService;
import ec.com.infinityone.servicio.pedidosyfacturacion.UsoSellosServicio;

/**
 * Pantalla "Mensaje de Sellos" (pedidosyfacturacion/mensajesellos.xhtml).
 *
 * Se elige fecha de traslado, terminal de partida y autotanque; con eso se
 * consulta buscarselloscliente y se llena el combo de clientes destinatarios
 * (sin repetir). "Enviar" manda un push al cliente seleccionado con el link de
 * seguimiento del pedido.
 */
@Named
@ViewScoped
public class MensajeSellosBean extends ReusableBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(MensajeSellosBean.class.getName());

    private static final Pattern PATRON_URL = Pattern.compile("^(https?://)[^\\s/$.?#].[^\\s]*$",
            Pattern.CASE_INSENSITIVE);

    @Inject
    private TerminalServicio terminalServicio;

    @Inject
    private ClienteServicio clienteServicio;

    @Inject
    private UsoSellosServicio usoSellosServicio;

    @Inject
    private UsuarioServicio usuarioServicio;

    @Inject
    private ExpoPushService expoPushService;

    /** Autotanques activos (se cargan desde el servicio REST de autotanques). */
    private List<Autotanque> listaAutotanques;

    /** Terminales de partida. */
    private List<Terminal> listaTerminales;

    /** Catálogo completo de clientes disponibles para el combo. */
    private List<ClienteDestino> listaTodosClientes;

    /** Clientes mostrados en el combo (siempre todos; primero los del autotanque si aplica). */
    private List<ClienteDestino> listaClientes;

    /** Fecha de traslado (por defecto la actual). */
    private Date fechaTraslado;

    /** Codigo del terminal de partida. */
    private String codigoTerminal;

    /** Placa del autotanque seleccionado. */
    private String placaAutotanque;

    /** Codigo del cliente destinatario seleccionado. */
    private String codigoCliente;

    /** Link de seguimiento del pedido. */
    private String linkRastreo;

    @PostConstruct
    public void init() {
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.autotanque";
        listaAutotanques = new ArrayList<>();
        listaTerminales = new ArrayList<>();
        listaTodosClientes = new ArrayList<>();
        listaClientes = new ArrayList<>();
        limpiar();
        obtenerTerminales();
        obtenerAutotanques();
        obtenerTodosLosClientes();
    }

    /**
     * Carga todos los clientes (activos de la comercializadora del usuario, o todos
     * si no tiene comercializadora fija) para que siempre estén disponibles en el
     * combo con búsqueda por texto.
     */
    public void obtenerTodosLosClientes() {
        Map<String, ClienteDestino> unicos = new LinkedHashMap<>();
        try {
            String codComer = (dataUser != null && dataUser.getUser() != null)
                    ? dataUser.getUser().getCodigocomercializadora()
                    : null;
            List<Cliente> clientes;
            if (!isVacio(codComer)) {
                clientes = clienteServicio.obtenerClientesPorComercializadoraActiva(codComer.trim());
                if (clientes == null || clientes.isEmpty()) {
                    clientes = clienteServicio.obtenerClientesActivosPorComercializadora(codComer.trim());
                }
            } else {
                clientes = clienteServicio.obtenerClientes();
            }
            if (clientes != null) {
                for (Cliente c : clientes) {
                    if (c == null || c.getClientePK() == null || isVacio(c.getClientePK().getCodigo())) {
                        continue;
                    }
                    String cod = c.getClientePK().getCodigo().trim();
                    String nom = !isVacio(c.getNombrecomercial()) ? c.getNombrecomercial().trim()
                            : (!isVacio(c.getNombre()) ? c.getNombre().trim() : "");
                    unicos.putIfAbsent(cod, new ClienteDestino(cod, nom));
                }
            }
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, "Error al cargar la lista completa de clientes", e);
            mensaje(FacesMessage.SEVERITY_ERROR, "No se pudo cargar la lista de clientes");
        }
        listaTodosClientes = new ArrayList<>(unicos.values());
        listaClientes = new ArrayList<>(listaTodosClientes);
    }

    public void obtenerTerminales() {
        try {
            List<Terminal> terminales = terminalServicio.obtenerTerminalesActivos();
            listaTerminales = terminales != null ? terminales : new ArrayList<Terminal>();
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, "Error al cargar terminales", e);
            mensaje(FacesMessage.SEVERITY_ERROR, "No se pudo cargar la lista de terminales");
        }
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
     * Consulta buscarselloscliente para prefijar el cliente destinatario del
     * autotanque/terminal/fecha seleccionados, manteniendo siempre todos los
     * clientes disponibles en el combo (con los del autotanque al inicio y sin
     * repetir).
     */
    public void cargarClientes() {
        if (listaTodosClientes == null) {
            listaTodosClientes = new ArrayList<>();
        }
        if (isVacio(codigoTerminal) || isVacio(placaAutotanque) || fechaTraslado == null) {
            listaClientes = new ArrayList<>(listaTodosClientes);
            if (isVacio(placaAutotanque)) {
                codigoCliente = null;
            }
            return;
        }
        try {
            String codComer = (dataUser != null && dataUser.getUser() != null)
                    ? dataUser.getUser().getCodigocomercializadora()
                    : null;
            if (isVacio(codComer)) {
                listaClientes = new ArrayList<>(listaTodosClientes);
                return;
            }
            String fecha = new SimpleDateFormat("yyyy/MM/dd").format(fechaTraslado);
            JSONArray items = usoSellosServicio.buscarSellosCliente(codComer.trim(), codigoTerminal, placaAutotanque,
                    fecha);
            if (items == null) {
                listaClientes = new ArrayList<>(listaTodosClientes);
                codigoCliente = null;
                mensaje(FacesMessage.SEVERITY_ERROR, "No se pudo consultar los clientes del autotanque");
                return;
            }

            Map<String, ClienteDestino> combinados = new LinkedHashMap<>();
            String primerClienteAutotanque = null;
            for (int i = 0; i < items.length(); i++) {
                if (items.isNull(i)) {
                    continue;
                }
                JSONObject it = items.getJSONObject(i);
                String cod = it.optString("codigocliente", "").trim();
                if (!cod.isEmpty()) {
                    if (primerClienteAutotanque == null) {
                        primerClienteAutotanque = cod;
                    }
                    combinados.putIfAbsent(cod, new ClienteDestino(cod, it.optString("nombrecliente", "").trim()));
                }
            }
            for (ClienteDestino c : listaTodosClientes) {
                if (c != null && !isVacio(c.getCodigo())) {
                    combinados.putIfAbsent(c.getCodigo(), c);
                }
            }
            listaClientes = new ArrayList<>(combinados.values());
            if (primerClienteAutotanque != null) {
                codigoCliente = primerClienteAutotanque;
            } else {
                codigoCliente = null;
                mensaje(FacesMessage.SEVERITY_INFO,
                        "No se encontr\u00f3 cliente asignado al autotanque para esa fecha/terminal; puede elegir uno de la lista");
            }
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, "Error al cargar clientes destinatarios", e);
            listaClientes = new ArrayList<>(listaTodosClientes);
            mensaje(FacesMessage.SEVERITY_ERROR, "No se pudo consultar los clientes del autotanque");
        }
    }

    /** Accion del boton "Enviar": push al cliente destinatario seleccionado. */
    public void enviar() {
        if (fechaTraslado == null) {
            mensaje(FacesMessage.SEVERITY_WARN, "Seleccione la fecha de traslado");
            return;
        }
        if (isVacio(codigoTerminal)) {
            mensaje(FacesMessage.SEVERITY_WARN, "Seleccione el terminal de partida");
            return;
        }
        if (isVacio(placaAutotanque)) {
            mensaje(FacesMessage.SEVERITY_WARN, "Seleccione un autotanque");
            return;
        }
        if (isVacio(codigoCliente)) {
            mensaje(FacesMessage.SEVERITY_WARN, "Seleccione el cliente destinatario");
            return;
        }
        if (isVacio(linkRastreo)) {
            mensaje(FacesMessage.SEVERITY_WARN, "Ingrese el link del rastreo satelital");
            return;
        }
        if (!PATRON_URL.matcher(linkRastreo.trim()).matches()) {
            mensaje(FacesMessage.SEVERITY_WARN, "El link debe iniciar con http:// o https://");
            return;
        }

        String token = usuarioServicio.obtenerPushTokenActivo(codigoCliente);
        if (token == null) {
            mensaje(FacesMessage.SEVERITY_WARN,
                    "El cliente " + codigoCliente + " no tiene la app registrada (sin token activo). No se envi\u00f3 el mensaje");
            return;
        }

        String link = linkRastreo.trim();
        // \u00ed = i con tilde
        String cuerpo = "seguimiento de tu pedido en l\u00ednea:\n" + link;
        expoPushService.enviarMensajeCliente(token, codigoCliente, "Seguimiento de tu pedido", cuerpo, link);
        LOG.info("MensajeSellos: push enviado a cliente " + codigoCliente + ", autotanque=" + placaAutotanque
                + ", terminal=" + codigoTerminal + ", link=" + link);
        mensaje(FacesMessage.SEVERITY_INFO, "Mensaje enviado al cliente " + codigoCliente);
    }

    /** Accion del boton "Cancelar". */
    public void cancelar() {
        limpiar();
    }

    private void limpiar() {
        fechaTraslado = new Date();
        codigoTerminal = null;
        placaAutotanque = null;
        codigoCliente = null;
        linkRastreo = null;
        listaClientes = (listaTodosClientes != null) ? new ArrayList<>(listaTodosClientes) : new ArrayList<>();
    }

    private boolean isVacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void mensaje(FacesMessage.Severity severidad, String texto) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, texto, null));
    }

    /** Elemento del combo "Cliente destinatario": codigo - nombre. */
    public static class ClienteDestino implements Serializable {

        private static final long serialVersionUID = 1L;

        private String codigo;
        private String nombre;

        public ClienteDestino() {
        }

        public ClienteDestino(String codigo, String nombre) {
            this.codigo = codigo;
            this.nombre = nombre;
        }

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDescripcion() {
            return codigo + " - " + nombre;
        }
    }

    public List<Autotanque> getListaAutotanques() {
        return listaAutotanques;
    }

    public List<Terminal> getListaTerminales() {
        return listaTerminales;
    }

    public List<ClienteDestino> getListaClientes() {
        return listaClientes;
    }

    public String getPlacaAutotanque() {
        return placaAutotanque;
    }

    public void setPlacaAutotanque(String placaAutotanque) {
        this.placaAutotanque = placaAutotanque;
    }

    public String getCodigoTerminal() {
        return codigoTerminal;
    }

    public void setCodigoTerminal(String codigoTerminal) {
        this.codigoTerminal = codigoTerminal;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getLinkRastreo() {
        return linkRastreo;
    }

    public void setLinkRastreo(String linkRastreo) {
        this.linkRastreo = linkRastreo;
    }

    public Date getFechaTraslado() {
        return fechaTraslado;
    }

    public void setFechaTraslado(Date fechaTraslado) {
        this.fechaTraslado = fechaTraslado;
    }
}
