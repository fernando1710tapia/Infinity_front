/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.pedidosyfacturacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.infinityone.serivicio.actorcomercial.ClienteServicio;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.modelo.Abastecedora;
import ec.com.infinityone.modelo.Banco;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.Comercializadora;
import ec.com.infinityone.modelo.Factura;
import ec.com.infinityone.modelo.FacturaPK;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.servicio.catalogo.BancoServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.servicio.pedidosyfacturacion.FacturaServicio;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;


/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class AnulacionEspecialBean extends ReusableBean implements Serializable {

    protected static final Logger LOG = Logger.getLogger(AnulacionEspecialBean.class.getName());

    /*
    Variable para acceder a los servicios de Comercialziadora
     */
    @Inject
    protected ComercializadoraServicio comerServicio;
    /*
    Variable para acceder a los servicios de Cliente
     */
    @Inject
    protected ClienteServicio clienteServicio;
    /*
    Variable para acceder a los servicios de Banco
     */
    @Inject
    private BancoServicio bancoServicio;
    /*
    Variable para acceder a los servicios de Banco
     */
    @Inject
    private FacturaServicio facturaServicio;
    /*
    Variable Comercializadora
     */
    protected ComercializadoraBean comercializadora;
    /*
    Variable para almacenar los datos comercializadora
     */
    protected List<ComercializadoraBean> listaComercializadora;
    /*
    Variable para almacenar los datos clientes
     */
    protected List<Cliente> listaClientes;
    /*
    Variable para almacenar los datos clientes
     */
    protected List<Banco> listaBancos;
    /*
    Variable que almacena el código de la comercializadora
     */
    protected String codComer;
    /*
    Variable que almacena el código del cliente
     */
    protected String codCliente;
    /*
    Variable que almacena el código de la abastecedora
     */
    protected String codAbas;
    /*
    Variable que isntacia el modelo Factura
     */
    protected Factura fact;
    /*
    Variable que isntacia el modelo FacturaPK
     */
    protected FacturaPK factPk;
    /*
    Variable para guardar una listda de Factura y Detalle Factura
     */
    protected List<Factura> listFact;
    /*
    Variable Cliente
     */
    protected Cliente cliente;
    /*
    Variable Banco
     */
    protected Banco banco;
    /*
    Variable Comercializadora
     */
    protected Comercializadora comerc;
    /*
    Variable Abastecedora
     */
    protected Abastecedora abas;
    /*
    Variable para almacenar el número de la factura
     */
    protected String numFactura;
    /*
    Opcion para habilitar el boton de procesar
     */
    protected boolean permiteAnulacion;

    /**
     * Constructor por defecto
     */
    public AnulacionEspecialBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        reestablecer();
        obtenerComercializadora();
        obtenerBancos();
        PrimeFaces.current().executeScript("PF('editarDesp').show()");
    }

    public void reestablecer() {
        numFactura = "";
        codComer = "";
        codCliente = "-1";
        codAbas = "";
        fact = new Factura();
        factPk = new FacturaPK();
        cliente = new Cliente();
        banco = new Banco();
        comerc = new Comercializadora();
        abas = new Abastecedora();
        listFact = new ArrayList<>();
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo";
        permiteAnulacion = false;
    }

    private void obtenerBancos() {
        listaBancos = new ArrayList<>();
        listaBancos = bancoServicio.obtenerBanco();
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = comerServicio.obtenerComercializadorasActivas();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda(1);
        }
    }

    public void seleccionarComer(int busqueda) {
        if (comercializadora != null) {
            codComer = comercializadora.getCodigo();
            codAbas = comercializadora.getAbastecedora();
        }
    }

    public void seleccionarCliente(int busqueda) {
        if (cliente != null) {
            codCliente = cliente.getCodigo();            
        } else {
            codCliente = "-1";
        }
    }

    public void habilitarBusqueda(int busqueda) {
        if (dataUser.getUser() != null) {
            switch (dataUser.getUser().getNiveloperacion()) {
                case "cero":
                    habilitarComer = true;
                    habilitarTerminal = true;
                    habilitarCli = true;                    
                    break;
                case "adco":
                    habilitarComer = false;
                    habilitarTerminal = true;
                    habilitarCli = true;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                            comercializadora = listaComercializadora.get(i);
                        }
                    }
                    seleccionarComer(busqueda);
                    listaClientes = clienteServicio.obtenerClientesPorComercializadoraActiva(comercializadora.getCodigo());
                    break;
                case "usac":
                    habilitarComer = false;
                    habilitarCli = false;
                    habilitarTerminal = false;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                            comercializadora = listaComercializadora.get(i);
                        }
                    }
                    if (comercializadora.getCodigo() != null) {
                        seleccionarComer(busqueda);
                        listaClientes = clienteServicio.obtenerClientesPorComercializadoraActiva(comercializadora.getCodigo());
                        for (int i = 0; i < listaClientes.size(); i++) {
                            if (listaClientes.get(i).getCodigo().equals(dataUser.getUser().getCodigocliente())) {
                                this.cliente = listaClientes.get(i);
                                break;
                            }
                        }
                    } else {
                        this.dialogo(FacesMessage.SEVERITY_FATAL, "La comercializadora se encuentra deshabilitada");
                    }
                    seleccionarCliente(busqueda);                    
                    break;

                case "agco":
                    habilitarComer = false;
                    habilitarCli = true;
                    habilitarTerminal = true;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                            comercializadora = listaComercializadora.get(i);
                            break;
                        }
                    }
                    seleccionarComer(busqueda);                    
                    break;
                default:
                    break;
            }
        }
    }

    public void obtenerFacturas() throws ParseException {
        if (comercializadora != null) {
        listFact = facturaServicio.buscarFacturas(codAbas, codComer, numFactura);
            if (!listFact.isEmpty()) {
                fact = listFact.get(0);
                for (int i = 0; i < listaBancos.size(); i++) {
                    if (fact.getCodigobanco().equals(listaBancos.get(i).getCodigo())) {
                        banco = listaBancos.get(i);
                        break;
                    }
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_INFO, "No se encontraron facturas");
            }
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Seleccione una comercializadora");
        }
    }

    public void editarSriFactura() throws ParseException, IOException {
        if (fact != null) {
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            if (fact.getFechaacreditacion() != null) {
                Date fechaA = formato.parse(fact.getFechadespacho().replace("/", "-"));
                fact.setFechaacreditacion(date.format(fechaA));
            }
            if (fact.getFechadespacho() != null) {
                Date fechaA = formato.parse(fact.getFechadespacho().replace("/", "-"));
                fact.setFechadespacho(date.format(fechaA));
            }
            if (fact.getFechavencimiento() != null) {
                Date fechaA = formato.parse(fact.getFechavencimiento().replace("/", "-"));
                fact.setFechavencimiento(date.format(fechaA));
            }
            if (fact.getFechaventa() != null) {
                Date fechaA = formato.parse(fact.getFechaventa().replace("/", "-"));
                fact.setFechaventa(date.format(fechaA));
            }
            if (fact.getFechaacreditacionprorrogada() != null) {
                Date fechaA = formato.parse(fact.getFechaacreditacionprorrogada().replace("/", "-"));
                fact.setFechaacreditacionprorrogada(date.format(fechaA));
            }
            fact.setNumeroautorizacion("");
            fact.setFechaautorizacion("");
            if (actualizarFactura(fact)) {
                returDash();
            }

        }
    }

    public void returDash() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(Fichero.getRUTADASHBOARD());
    }

    public boolean actualizarFactura(Factura fact) {
        try {
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/porId");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000 * 60);
            connection.setReadTimeout(1000 * 60);
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(fact);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA ACUTALIZADA EXITOSAMENTE");
                return true;
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
                return false;
            }
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }    

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public String getCodComer() {
        return codComer;
    }

    public void setCodComer(String codComer) {
        this.codComer = codComer;
    }
    
    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public String getCodAbas() {
        return codAbas;
    }

    public void setCodAbas(String codAbas) {
        this.codAbas = codAbas;
    }   

    public Factura getFact() {
        return fact;
    }

    public void setFact(Factura fact) {
        this.fact = fact;
    }
    
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }    

    public boolean isPermiteAnulacion() {
        return permiteAnulacion;
    }

    public void setPermiteAnulacion(boolean permiteAnulacion) {
        this.permiteAnulacion = permiteAnulacion;
    }
    
    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public List<Banco> getListaBancos() {
        return listaBancos;
    }

    public void setListaBancos(List<Banco> listaBancos) {
        this.listaBancos = listaBancos;
    }    

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }
}
