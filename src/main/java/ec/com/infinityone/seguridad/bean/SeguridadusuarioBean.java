/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.seguridad.bean;

import ec.com.infinityone.actorcomercial.serivicios.ClienteServicio;
import ec.com.infinityone.actorcomercial.serivicios.ComercializadoraServicio;
import ec.com.infinityone.modeloWeb.Cliente;
import ec.com.infinityone.modeloWeb.ObjetoNivel1;
import ec.com.infinityone.modeloWeb.Usuario;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.actorcomercial.bean.ComercializadoraBean;
import ec.com.infinityone.catalogo.servicios.TerminalServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Terminal;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
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
 * @author Andres
 */
@Named
@ViewScoped
public class SeguridadusuarioBean extends ReusableBean implements Serializable {

    @Inject
    private ClienteServicio clienteServicio;
    @Inject
    private ComercializadoraServicio comercializadoraServicio;

    @Inject
    private TerminalServicio terminalServicio;
    /*
    Variable que almacena varios Usuarios
     */
    private List<Usuario> listaUsuario;

    private List<Cliente> listaCliente;

    private List<ComercializadoraBean> listaComercializadora;

    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarSeguridad;

    private boolean permiteEdit;

    private boolean activarComer;

    private boolean activarCli;

    private boolean estadoUsuario;

    private Usuario usuarioS;

    private Cliente cliente;

    private ComercializadoraBean comercializadora;

    private String codComer;
    /*
    Variable que trae la clase Terminal
     */
    private Terminal terminal;
    /*
    Lista que almacena varios terminales
     */
    private List<Terminal> listaTerminales;

    private String pregunta1;

    private String pregunta2;

    private String pregunta3;

    private List<String> listaPreguntas;

    private String clave;

    private String[] preguntas;

    /**
     * Constructor por defecto
     */
    public SeguridadusuarioBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.usuario";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.usuario";

        editarSeguridad = false;
        objeto = new ObjetoNivel1();
        usuarioS = new Usuario();
        cliente = new Cliente();
        terminal = new Terminal();
        permiteEdit = false;
        activarComer = false;
        activarCli = false;
        estadoUsuario = true;
        preguntas = Fichero.getPREGUNTAS().split(",");
        obtenerUsuario();
        obtenerComercializadora();
        obtenerTerminales();
    }

    public void obtenerClientes(String codComer) {
        listaCliente = new ArrayList<>();
        listaCliente = clienteServicio.obtenerClientesActivosPorComercializadora(codComer);
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = comercializadoraServicio.obtenerComercializadorasActivas();
    }

    public void obtenerTerminales() {
        listaTerminales = new ArrayList<>();
        listaTerminales = terminalServicio.obtenerTerminalesActivos();
    }

    public void obtenerUsuario() {
        try {
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaUsuario = new ArrayList<>();

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
                usuarioS.setCodigo(user.getString("codigo"));
                usuarioS.setCedula(user.getString("cedula"));
                usuarioS.setNombre(user.getString("nombre"));
                usuarioS.setNombrever(user.getString("nombrever"));
                if (!user.isNull("codigocliente")) {
                    usuarioS.setCodigocliente(user.getString("codigocliente"));
                }
                if (!user.isNull("codigocomercializadora")) {
                    usuarioS.setCodigocomercializadora(user.getString("codigocomercializadora"));
                }
                usuarioS.setActivo(user.getBoolean("activo"));
                usuarioS.setNiveloperacion(user.getString("niveloperacion"));
                if (!user.isNull("hash")) {
                    usuarioS.setHash(user.getString("hash"));
                }
                if (!user.isNull("vigenciahash")) {
                    Long lDateVig = user.getLong("vigenciahash");
                    Date dateVig = new Date(lDateVig);
                    usuarioS.setVigenciahash(dateVig);
                }
                usuarioS.setClave(user.getString("clave").trim());
                usuarioS.setUsuarioactual(user.getString("usuarioactual"));
                if (!user.isNull("codigoterminal")) {
                    usuarioS.setCodigoterminal(user.getString("codigoterminal"));
                }
                if (!user.isNull("correo")) {
                    usuarioS.setCorreo(user.getString("correo"));
                }
                if (!user.isNull("pregunta1")) {
                    usuarioS.setPregunta1(user.getString("pregunta1"));
                }
                if (!user.isNull("respuesta1")) {
                    usuarioS.setRespuesta1(user.getString("respuesta1"));
                }
                if (!user.isNull("pregunta2")) {
                    usuarioS.setPregunta2(user.getString("pregunta2"));
                }
                if (!user.isNull("respuesta2")) {
                    usuarioS.setRespuesta2(user.getString("respuesta2"));
                }
                if (!user.isNull("pregunta3")) {
                    usuarioS.setPregunta3(user.getString("pregunta3"));
                }
                if (!user.isNull("respuesta3")) {
                    usuarioS.setRespuesta3(user.getString("respuesta3"));
                }
                listaUsuario.add(usuarioS);
                usuarioS = new Usuario();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (editarSeguridad) {
            editItems();
            obtenerUsuario();
        } else {
            addItems();
            obtenerUsuario();
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

            obj.put("codigo", usuarioS.getCodigo());
            obj.put("cedula", usuarioS.getCedula());
            obj.put("nombre", usuarioS.getNombre());
            obj.put("nombrever", usuarioS.getNombrever());
            obj.put("activo", estadoUsuario);
            obj.put("niveloperacion", usuarioS.getNiveloperacion());
            if (usuarioS.getNiveloperacion().equals("adco")) {
                obj.put("codigocomercializadora", codComer);
            } else if (usuarioS.getNiveloperacion().equals("usac")) {
                obj.put("codigocomercializadora", codComer);
                obj.put("codigocliente", cliente.getCodigo());
            } else if (usuarioS.getNiveloperacion().equals("agco")) {
                obj.put("codigocomercializadora", codComer);
                obj.put("codigoterminal", terminal.getCodigo());
            }
            obj.put("hash", usuarioS.getHash());
            obj.put("vigenciahash", usuarioS.getVigenciahash());
            obj.put("clave", DigestUtils.sha256Hex(usuarioS.getClave()));
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            obj.put("correo", usuarioS.getCorreo());
            obj.put("pregunta1", usuarioS.getPregunta1());
            obj.put("respuesta1", usuarioS.getRespuesta1());
            obj.put("pregunta2", usuarioS.getPregunta2());
            obj.put("respuesta2", usuarioS.getRespuesta2());
            obj.put("pregunta3", usuarioS.getPregunta3());
            obj.put("respuesta3", usuarioS.getRespuesta3());

            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "USUARIO REGISTRADO EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
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
            obj.put("codigo", usuarioS.getCodigo());
            obj.put("cedula", usuarioS.getCedula());
            obj.put("nombre", usuarioS.getNombre());
            obj.put("nombrever", usuarioS.getNombrever());
            obj.put("codigocliente", cliente.getCodigo());
            obj.put("activo", estadoUsuario);
            obj.put("niveloperacion", usuarioS.getNiveloperacion());
            if (usuarioS.getNiveloperacion().equals("adco")) {
                obj.put("codigocomercializadora", codComer);
            } else if (usuarioS.getNiveloperacion().equals("usac")) {
                obj.put("codigocomercializadora", codComer);
                obj.put("codigocliente", cliente.getCodigo());
            } else if (usuarioS.getNiveloperacion().equals("agco")) {
                obj.put("codigocomercializadora", codComer);
                obj.put("codigoterminal", terminal.getCodigo());
            }
            obj.put("hash", usuarioS.getHash());
            obj.put("vigenciahash", usuarioS.getVigenciahash());
            if (usuarioS.getClave().equals(clave)) {
                obj.put("clave", usuarioS.getClave());
            } else {
                obj.put("clave", DigestUtils.sha256Hex(usuarioS.getClave()));
            }
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            obj.put("correo", usuarioS.getCorreo());
            obj.put("pregunta1", usuarioS.getPregunta1());
            obj.put("respuesta1", usuarioS.getRespuesta1());
            obj.put("pregunta2", usuarioS.getPregunta2());
            obj.put("respuesta2", usuarioS.getRespuesta2());
            obj.put("pregunta3", usuarioS.getPregunta3());
            obj.put("respuesta3", usuarioS.getRespuesta3());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "USUARIO ACTUALIZADO EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteItems() {
        try {
            String respuesta;
            url = new URL(direccion + "/porId?codigo=" + usuarioS.getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "USUARIO ELIMINADO EXITOSAMENTE");
                obtenerUsuario();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoUsuario() {
        editarSeguridad = false;
        estadoUsuario = true;
        permiteEdit = false;
        soloLectura = false;
        usuarioS = new Usuario();
        listaPreguntas = new ArrayList<>();
        codComer = "";
        cliente = new Cliente();
        terminal = new Terminal();
        Set<Integer> generados = new HashSet<>();
        for (int i = 0; i < 3; i++) {
//            int numero = (int)(Math.random()*2);
//            listaPreguntas.add(preguntas[numero]);
            int aleatorio = -1;
            boolean generado = false;
            while (!generado) {
                int posible = (int) (Math.random() * preguntas.length - 1);
                if (!generados.contains(posible)) {
                    generados.add(posible);
                    aleatorio = posible;
                    listaPreguntas.add(preguntas[aleatorio]);
                    if (i == 0) {
                        usuarioS.setPregunta1(preguntas[aleatorio]);
                    }
                    if (i == 1) {
                        usuarioS.setPregunta2(preguntas[aleatorio]);
                    }
                    if (i == 2) {
                        usuarioS.setPregunta3(preguntas[aleatorio]);
                    }
                    generado = true;
                }
            }
        }
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Usuario editarUsuario(Usuario obj) {
        editarSeguridad = true;
        usuarioS = obj;
        permiteEdit = true;
        soloLectura = true;
        estadoUsuario = usuarioS.getActivo();
        clave = usuarioS.getClave();
        codComer = "";
        cliente = new Cliente();
        terminal = new Terminal();
        seleccionarOperacion();
        if (usuarioS.getNiveloperacion().equals("adco")) {
            for (int i = 0; i < listaComercializadora.size(); i++) {
                if (listaComercializadora.get(i).getCodigo().equals(usuarioS.getCodigocomercializadora())) {
                    this.codComer = listaComercializadora.get(i).getCodigo();
                    break;
                }
            }
        }
        if (usuarioS.getNiveloperacion().equals("usac")) {
            obtenerClientes(usuarioS.getCodigocomercializadora());
            for (int i = 0; i < listaComercializadora.size(); i++) {
                if (listaComercializadora.get(i).getCodigo().equals(usuarioS.getCodigocomercializadora())) {
                    this.codComer = listaComercializadora.get(i).getCodigo();
                    break;
                }
            }
            for (int i = 0; i < listaCliente.size(); i++) {
                if (listaCliente.get(i).getCodigo().equals(usuarioS.getCodigocliente())) {
                    this.cliente = listaCliente.get(i);
                    break;
                }
            }
        }
        if (usuarioS.getNiveloperacion().equals("agco")) {
            for (int i = 0; i < listaComercializadora.size(); i++) {
                if (listaComercializadora.get(i).getCodigo().equals(usuarioS.getCodigocomercializadora())) {
                    this.codComer = listaComercializadora.get(i).getCodigo();
                    break;
                }
            }
            for (int i = 0; i < listaTerminales.size(); i++) {
                if (listaTerminales.get(i).getCodigo().equals(usuarioS.getCodigoterminal())) {
                    this.terminal = listaTerminales.get(i);
                    break;
                }
            }
        }
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return usuarioS;
    }

    public void seleccionarComercializdora() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        codComer = params.get("formNuevo:informacion:comercializadora_input");
        listaCliente = new ArrayList<>();
        listaCliente = clienteServicio.obtenerClientesActivosPorComercializadora(codComer);
    }

    public void seleccionarOperacion() {
//        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//        String nivelOp = params.get("formNuevo:nivelOpe_input");
        if (usuarioS.getNiveloperacion() != null) {
            if (usuarioS.getNiveloperacion().equals("cero")) {
                activarComer = false;
                activarCli = false;
                habilitarTerminal = false;
            }
            if (usuarioS.getNiveloperacion().equals("adco")) {
                activarComer = true;
                activarCli = false;
                habilitarTerminal = false;
            }
            if (usuarioS.getNiveloperacion().equals("usac")) {
                activarComer = true;
                activarCli = true;
                habilitarTerminal = false;
            }
            if (usuarioS.getNiveloperacion().equals("agco")) {
                activarComer = true;
                activarCli = false;
                habilitarTerminal = true;
            }
        }
    }

    public boolean isEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(boolean estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }

    public boolean isActivarComer() {
        return activarComer;
    }

    public void setActivarComer(boolean activarComer) {
        this.activarComer = activarComer;
    }

    public boolean isActivarCli() {
        return activarCli;
    }

    public void setActivarCli(boolean activarCli) {
        this.activarCli = activarCli;
    }

    public List<Cliente> getListaCliente() {
        return listaCliente;
    }

    public void setListaCliente(List<Cliente> listaCliente) {
        this.listaCliente = listaCliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public boolean isEditarProducto() {
        return editarSeguridad;
    }

    public void setEditarProducto(boolean editarSeguridad) {
        this.editarSeguridad = editarSeguridad;
    }

    public boolean isPermiteEdit() {
        return permiteEdit;
    }

    public void setPermiteEdit(boolean permiteEdit) {
        this.permiteEdit = permiteEdit;
    }

    public boolean isEditarSeguridad() {
        return editarSeguridad;
    }

    public void setEditarSeguridad(boolean editarSeguridad) {
        this.editarSeguridad = editarSeguridad;
    }

    public List<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public Usuario getUsuarioS() {
        return usuarioS;
    }

    public void setUsuarioS(Usuario usuarioS) {
        this.usuarioS = usuarioS;
    }

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public String getCodComer() {
        return codComer;
    }

    public void setCodComer(String codComer) {
        this.codComer = codComer;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public List<Terminal> getListaTerminales() {
        return listaTerminales;
    }

    public void setListaTerminales(List<Terminal> listaTerminales) {
        this.listaTerminales = listaTerminales;
    }

    public String getPregunta1() {
        return pregunta1;
    }

    public void setPregunta1(String pregunta1) {
        this.pregunta1 = pregunta1;
    }

    public String getPregunta2() {
        return pregunta2;
    }

    public void setPregunta2(String pregunta2) {
        this.pregunta2 = pregunta2;
    }

    public String getPregunta3() {
        return pregunta3;
    }

    public void setPregunta3(String pregunta3) {
        this.pregunta3 = pregunta3;
    }

    public List<String> getListaPreguntas() {
        return listaPreguntas;
    }

    public void setListaPreguntas(List<String> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
    }

}
