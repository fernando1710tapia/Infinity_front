/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.actorcomercial.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ec.com.infinityone.actorcomercial.serivicios.ClienteServicio;
import ec.com.infinityone.actorcomercial.serivicios.ComercializadoraServicio;
import ec.com.infinityone.catalogo.servicios.BancoServicio;
import ec.com.infinityone.catalogo.servicios.DireccioninenServicio;
import ec.com.infinityone.catalogo.servicios.FormapagoServicio;
import ec.com.infinityone.catalogo.servicios.TerminalService;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Banco;
import ec.com.infinityone.modeloWeb.Cliente;
import ec.com.infinityone.modeloWeb.Direccioninen;
import ec.com.infinityone.modeloWeb.Formapago;
import ec.com.infinityone.modeloWeb.Listaprecio;
import ec.com.infinityone.modeloWeb.ListaprecioPK;
import ec.com.infinityone.modeloWeb.ObjetoNivel1;
import ec.com.infinityone.modeloWeb.Terminal;
import ec.com.infinityone.preciosyfacturacion.servicios.ListaprecioServicio;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author David
 */
@Named
@ViewScoped
public class ClienteBean extends ReusableBean implements Serializable {

    @Inject
    private ClienteServicio clienteServicio;
    @Inject
    private ComercializadoraServicio comercializadoraServicio;
    @Inject
    private DireccioninenServicio direccioninenServicio;
    @Inject
    private FormapagoServicio formapagoServicio;
    @Inject
    private BancoServicio bancoServicio;
    @Inject
    private TerminalService terminalServicio;
    @Inject
    private ListaprecioServicio listaPrecioServicio;

    private ComercializadoraBean comercializadora;

    private Cliente cliente;

    private ListaprecioPK listaprecioPK;
    /*
    Objeto Direccion
     */
    private Direccioninen direccioninen;
    /*
    Variable que almacena varios clientes
     */
    private List<Cliente> listaClientes;
//    /*
//    Variable que almacena varias Comercializaros
//     */
    private List<ComercializadoraBean> listaComercializadora;
    /*
    Variable que almacena varios Direccions
     */
    private List<Direccioninen> listaDireccioninen;
    /*
    Variable que almacena varios Formapagos
     */
    private List<Formapago> listaFormapagos;
    /*
    Variable que almacena varios Terminales
     */
    private List<Terminal> listaTerminales;

    private List<Listaprecio> listaListaprecios;

    private List<ObjetoNivel1> listaTipoclientes;
    /*
    Objeto formapago
     */
    private Formapago formapago;
    /*
    Variable que almacena varios Bancos
     */
    private List<Banco> listaBancos;
    /*
    Objeto banco
     */
    private Banco banco;

    private Terminal terminal;

    private Listaprecio listaprecio;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarCliente;
    /*
    Variable que establece true or false para el estado del Banco
     */
    private boolean estadoCliente;
    private String esContribuyente;
    private boolean controlaGarantia;
    private boolean aplicaSubsidio;
    /*
    Variable para almacenar el código de la comercializadora
     */
    private String codComer;

    /**
     * Constructor por defecto
     */
    public ClienteBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //x = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.cliente";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.cliente";
        editarCliente = false;
        esContribuyente = "";
        codComer = "";
        controlaGarantia = false;
        aplicaSubsidio = false;
        soloLectura = false;
        cliente = new Cliente();
        formapago = new Formapago();
        banco = new Banco();
        terminal = new Terminal();
        listaprecio = new Listaprecio();
        direccioninen = new Direccioninen();
        listaprecioPK = new ListaprecioPK();
        //obtenerClientes();
        obtenerListaComercializadora();
        obtenerDireccioninen();
        obtenerFormapago();
        obtenerBanco();
        obtenerTerminal();
        obtenerTipocliente();
        //obtenerPrecio();
        //getURL();
    }

    public void obtenerClientes() {
        listaClientes = new ArrayList<>();
        listaClientes = this.clienteServicio.obtenerClientes();
    }

    public void obtenerListaComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = this.comercializadoraServicio.obtenerComercializadorasActivas();
        //comercializadora = this.comercializadoraServicio.getComercializadora();
    }

    public void obtenerDireccioninen() {
        listaDireccioninen = new ArrayList<>();
        listaDireccioninen = this.direccioninenServicio.obtenerDireccioninen();
    }

    public void obtenerFormapago() {
        listaFormapagos = new ArrayList<>();
        listaFormapagos = this.formapagoServicio.obtenerFormapago();
    }

    public void obtenerBanco() {
        listaBancos = new ArrayList<>();
        listaBancos = this.bancoServicio.obtenerBanco();
    }

    public void obtenerTerminal() {
        listaTerminales = new ArrayList<>();
        listaTerminales = this.terminalServicio.obtenerTerminal();
    }

    public void obtenerPrecio() {
        if (comercializadora != null) {
            codComer = comercializadora.getCodigo();          
            listaListaprecios = new ArrayList<>();
            listaListaprecios = this.listaPrecioServicio.obtenerListaprecioEstado(codComer, true);
        }        
    }

    public void seleccionarComercializadora() {
        if (comercializadora != null) {
            codComer = comercializadora.getCodigo();
            cliente.setCodigocomercializadora(codComer);
            listaListaprecios = new ArrayList<>();
            listaListaprecios = this.listaPrecioServicio.getListaprecioPorComer(codComer);
        }
    }

    public void seleccionarComerParaBusqueda() {
        if (comercializadora != null) {
            listaClientes = new ArrayList<>();
            listaClientes = this.clienteServicio.obtenerClientesPorComercializadora(comercializadora.getCodigo());
        }
    }

    public void obtenerTipocliente() {
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));  
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.tipocliente");
            System.out.println("FT:: obtenerTipocliente() URL del servicio: " + url.getPath());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            //connection.setRequestProperty("Authorization", token);
            //connection.setRequestProperty("Access-Control-Allow-Headers", "Authorization");
            //connection.setRequestProperty("Accept-Charset", "utf-8");
            //connection.setRequestProperty("Accept-Encoding", "gzip");
            //connection.setRequestProperty("Accept-Language", "en-US");
            //connection.setRequestProperty("Access-Control-Allow-Origin", "*");

            listaTipoclientes = new ArrayList<>();
            objeto = new ObjetoNivel1();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                System.out.println("FT:: obtenerTipocliente() dentro del while SI HAY DATOS " + url.getPath());
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            System.out.println("FT:: obtenerTipocliente() json retorno: " + retorno.length());
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject areaM = retorno.getJSONObject(indice);
                objeto.setCodigo(areaM.getString("codigo"));
                objeto.setNombre(areaM.getString("nombre"));
                if (areaM.getBoolean("activo") == true) {
                    objeto.setActivo("S");
                } else {
                    objeto.setActivo("N");
                }
                objeto.setUsuario(areaM.getString("usuarioactual"));
                listaTipoclientes.add(objeto);
                objeto = new ObjetoNivel1();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            System.out.println("FT:: Error al obtener tipos de clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void save() {
        if (editarCliente) {
            editItems();
            seleccionarComerParaBusqueda();
        } else {
            addItems();
            seleccionarComerParaBusqueda();
        }
    }

    public void setearCampos() {
        this.cliente.setCodigoformapago(formapago);
        this.cliente.setCodigobancodebito(banco.getCodigo());
        this.cliente.setCodigoterminaldefecto(terminal);
        this.cliente.setCodigolistaprecio(listaprecio.getListaprecioPK().getCodigo());
        this.cliente.setCodigodireccioninen(direccioninen.getCodigo());
        this.cliente.setUsuarioactual(dataUser.getUser().getNombrever());
    }

    public void addItems() {
        try {
            setearCampos();
            String respuesta;
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000 * 60);
            connection.setReadTimeout(1000 * 60);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            //connection.setFixedLengthStreamingMode(1000000000);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(this.cliente);
            Gson gson = new Gson();
            String JSON = gson.toJson(this.cliente);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "CLIENTE REGISTRADO EXITOSAMENTE");
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
            setearCampos();
            String respuesta;
            url = new URL(direccion + "/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000 * 60);
            connection.setReadTimeout(1000 * 60);
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");
            //connection.setFixedLengthStreamingMode(1000000000);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(this.cliente);
            Gson gson = new Gson();
            String JSON = gson.toJson(this.cliente);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "CLIENTE ACUTALIZADO EXITOSAMENTE");
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
            url = new URL(direccion + "/porId?codigo=" + cliente.getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "CLIENTE ELIMINADO EXITOSAMENTE");
                this.comercializadoraServicio.obtenerComercializadoras();
                obtenerClientes();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            if (dataUser.getUser().getNiveloperacion().equals("cero")) {
                habilitarComer = true;
            } else if (dataUser.getUser().getNiveloperacion().equals("adco")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        cliente.setCodigocomercializadora(listaComercializadora.get(i).getCodigo());
                        listaListaprecios = new ArrayList<>();
                        listaListaprecios = this.listaPrecioServicio.obtenerListaprecioEstado(cliente.getCodigocomercializadora(), true);
                    }
                }
            } else if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        cliente.setCodigocomercializadora(listaComercializadora.get(i).getCodigo());
                        listaListaprecios = new ArrayList<>();
                        listaListaprecios = this.listaPrecioServicio.obtenerListaprecioEstado(cliente.getCodigocomercializadora(), true);
                    }
                }
            }
        }
    }

    public void nuevoCliente() {
        estadoCliente = true;
        editarCliente = false;
        soloLectura = false;
        cliente = new Cliente();
        habilitarBusqueda();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        }
        direccioninen = new Direccioninen();
        cliente.setClavestc("");
        cliente.setCodigostc("");
        cliente.setEstado(true);
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Cliente editarCliente(Cliente obj) {
        listaListaprecios = new ArrayList<>();
        listaListaprecios = this.listaPrecioServicio.obtenerListaprecioEstado(obj.getCodigocomercializadora(), true);
        soloLectura = false;
        editarCliente = true;
        cliente = obj;
        habilitarBusqueda();
        if (cliente != null) {
            estadoCliente = cliente.isEstado();
            esContribuyente = cliente.getEscontribuyenteespacial();
            controlaGarantia = cliente.getControlagarantia();
            aplicaSubsidio = cliente.getAplicasubsidio2();
            formapago = cliente.getCodigoformapago();
            banco.setCodigo(cliente.getCodigobancodebito());
            terminal = cliente.getCodigoterminaldefecto();
//        listaprecioPK.setCodigo(cliente.getCodigolistaprecio());
//        listaprecio.setListaprecioPK(listaprecioPK);
            if (!listaDireccioninen.isEmpty()) {
                for (int i = 0; i < listaDireccioninen.size(); i++) {
                    if (listaDireccioninen.get(i).getCodigo().equals(cliente.getCodigodireccioninen())) {
                        this.direccioninen = listaDireccioninen.get(i);
                        break;
                    }
                }
            }
            if (!listaListaprecios.isEmpty()) {
                for (int i = 0; i < listaListaprecios.size(); i++) {
                    if (listaListaprecios.get(i).getListaprecioPK().getCodigo() == cliente.getCodigolistaprecio()) {
                        this.listaprecio = listaListaprecios.get(i);
                        break;
                    }
                }
            }
        }

        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return cliente;
    }

    public Cliente lecturaDatos(Cliente obj) {
        listaListaprecios = new ArrayList<>();
        listaListaprecios = this.listaPrecioServicio.getListaprecioPorComer(obj.getCodigocomercializadora());
        soloLectura = true;
        cliente = obj;
        estadoCliente = cliente.isEstado();
        esContribuyente = cliente.getEscontribuyenteespacial();
        controlaGarantia = cliente.getControlagarantia();
        aplicaSubsidio = cliente.getAplicasubsidio2();
        formapago = cliente.getCodigoformapago();
        banco.setCodigo(cliente.getCodigobancodebito());
        terminal = cliente.getCodigoterminaldefecto();
//        listaprecioPK.setCodigo(cliente.getCodigolistaprecio());
//        listaprecio.setListaprecioPK(listaprecioPK);
        for (int i = 0; i < listaDireccioninen.size(); i++) {
            if (listaDireccioninen.get(i).getCodigo().equals(cliente.getCodigodireccioninen())) {
                this.direccioninen = listaDireccioninen.get(i);
            }
        }
        for (int i = 0; i < listaListaprecios.size(); i++) {
            if (listaListaprecios.get(i).getListaprecioPK().getCodigo() == cliente.getCodigolistaprecio()) {
                this.listaprecio = listaListaprecios.get(i);
            }
        }
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return cliente;
    }

    public void controlaGarantia() {
        if (cliente.getControlagarantia() == true) {
            this.dialogo(FacesMessage.SEVERITY_INFO, "Control de garantía acaba de ser encendido, verificar la \n"
                    + "inicialización adecuada de información en el módulo TOTAL GARANTIZADO. Recordar que desde este momento la faturación será contolada por lo valores que se ingresen,\n"
                    + "y se modifiquen en este módulo");
        } else {
            this.dialogo(FacesMessage.SEVERITY_INFO, "Control de garantía acaba de ser apagado, ningun valor de garntías, facturación o pagos serán controlados\n"
                    + "para este cliente");
        }
    }

    public Direccioninen getDireccioninen() {
        return direccioninen;
    }

    public void setDireccioninen(Direccioninen direccioninen) {
        this.direccioninen = direccioninen;
    }

    public Listaprecio getListaprecio() {
        return listaprecio;
    }

    public void setListaprecio(Listaprecio listaprecio) {
        this.listaprecio = listaprecio;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Formapago getFormapago() {
        return formapago;
    }

    public void setFormapago(Formapago formapago) {
        this.formapago = formapago;
    }

    public List<Listaprecio> getListaListaprecios() {
        return listaListaprecios;
    }

    public void setListaListaprecios(List<Listaprecio> listaListaprecios) {
        this.listaListaprecios = listaListaprecios;
    }

    public List<Terminal> getListaTerminales() {
        return listaTerminales;
    }

    public void setListaTerminales(List<Terminal> listaTerminales) {
        this.listaTerminales = listaTerminales;
    }

    public List<Banco> getListaBancos() {
        return listaBancos;
    }

    public void setListaBancos(List<Banco> listaBancos) {
        this.listaBancos = listaBancos;
    }

    public List<Formapago> getListaFormapagos() {
        return listaFormapagos;
    }

    public void setListaFormapagos(List<Formapago> listaFormapagos) {
        this.listaFormapagos = listaFormapagos;
    }

    public List<Direccioninen> getListaDireccioninen() {
        return listaDireccioninen;
    }

    public void setListaDireccioninen(List<Direccioninen> listaDireccioninen) {
        this.listaDireccioninen = listaDireccioninen;
    }

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public boolean isEditarCliente() {
        return editarCliente;
    }

    public void setEditarCliente(boolean editarCliente) {
        this.editarCliente = editarCliente;
    }

    public boolean isEstadoCliente() {
        return estadoCliente;
    }

    public void setEstadoCliente(boolean estadoCliente) {
        this.estadoCliente = estadoCliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public List<ObjetoNivel1> getListaTipoclientes() {
        return listaTipoclientes;
    }

    public void setListaTipoclientes(List<ObjetoNivel1> listaTipoclientes) {
        this.listaTipoclientes = listaTipoclientes;
    }

}
