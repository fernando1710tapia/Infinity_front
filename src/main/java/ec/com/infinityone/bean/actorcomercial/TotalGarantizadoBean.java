/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.actorcomercial;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.infinityone.serivicio.actorcomercial.ClienteServicio;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.serivicio.actorcomercial.TotalGarantizadoServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.Totalgarantizado;
import ec.com.infinityone.modelo.TotalgarantizadoPK;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.DataOutputStream;
import java.io.IOException;
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

/**
 *
 * @author HP
 */
@Named
@ViewScoped
public class TotalGarantizadoBean extends ReusableBean implements Serializable {

    /*
    Variable que obtiene los métodos de totalgarantizado
     */
    @Inject
    private TotalGarantizadoServicio totalgaranServicio;
    /*
    Variable que obtiene los métodos de cliente
     */
    @Inject
    private ClienteServicio clienteServicio;
    /*
    Variable que obtiene los métodos de comercializadora
     */
    @Inject
    private ComercializadoraServicio comerServicio;
    /*
    Variable que alamcena un total garantizado
     */
    private Totalgarantizado totalGaran;
    /*
    Variable que alamcena un total garantizado
     */
    private TotalgarantizadoPK totalGaranPK;
    /*
    Variable que almacena una lista de totales garantizados
     */
    private List<Totalgarantizado> listaTotalG;
    /*
    Variable para realizar la función de editar o de ingresar un total garantizado
     */
    private boolean editTotalG;
    /*
    Variable para almacenar una comercializadora
     */
    private ComercializadoraBean comercializadora;
    /*
    Variable que alamcena varios clientes
     */
    private List<ComercializadoraBean> listaComercializadora;
    /*
    Variable que almacena un cliente
     */
    private Cliente cliente;
    /*
    Variable que alamcena varios clientes
     */
    private List<Cliente> listaClientes;
    /*
    Variable que alamcena el código de la comercializadora
     */
    private String codComer;
    /*
    Variable que alamcena el código del cliente
     */
    private String codCli;

    /**
     * Constructor por defecto
     */
    public TotalGarantizadoBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        totalGaran = new Totalgarantizado();
        totalGaranPK = new TotalgarantizadoPK();
        listaTotalG = new ArrayList<>();
        comercializadora = new ComercializadoraBean();
        cliente = new Cliente();
        listaComercializadora = new ArrayList<>();
        listaClientes = new ArrayList<>();
        //obtenerTotalsG();
        obtenerComercializadoras();
        //obtenerClientes();
    }

    public void obtenerTotalsG() {
        listaTotalG = new ArrayList<>();
        listaTotalG = totalgaranServicio.obtenerTotalesGarantizado();
    }

    public void obtenerTotaleGarantizado() {
        listaTotalG = new ArrayList<>();
        listaTotalG = totalgaranServicio.obtenerTotalesGarantizadoPorId(codComer, codCli);
        if (listaTotalG.isEmpty()) {
            this.dialogo(FacesMessage.SEVERITY_INFO, "No se han encontrado datos");
        }
    }

    public void obtenerComercializadoras() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = comerServicio.obtenerComercializadorasActivas();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda();
        }
    }

    public void seleccionarComercializadora() {
        if (comercializadora != null) {
            codComer = comercializadora.getCodigo();
            totalGaranPK.setCodigocomercializadora(comercializadora.getCodigo());
            totalGaran.setTotalgarantizadoPK(totalGaranPK);
            listaClientes = new ArrayList<>();
            listaClientes = clienteServicio.obtenerClientesActivosPorComercializadora(comercializadora.getCodigo());
        }
    }

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            if (dataUser.getUser().getNiveloperacion().equals("cero")) {
                habilitarComer = true;
                habilitarCli = true;
            }
            if (dataUser.getUser().getNiveloperacion().equals("adco")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                        codComer = comercializadora.getCodigo();
                    }
                }
                listaClientes = clienteServicio.obtenerClientesActivosPorComercializadora(comercializadora.getCodigo());
            }
            if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                        codComer = comercializadora.getCodigo();
                    }
                }
                listaClientes = clienteServicio.obtenerClientesActivosPorComercializadora(comercializadora.getCodigo());
                for (int i = 0; i < listaClientes.size(); i++) {
                    if (listaClientes.get(i).getCodigo().equals(dataUser.getUser().getCodigocliente())) {
                        this.cliente = listaClientes.get(i);
                        codCli = cliente.getCodigo();
                    }
                }
            }
        }
    }

    public void obtenerClientes() {
        listaClientes = new ArrayList<>();
        listaClientes = clienteServicio.obtenerClientes();
    }

    public void seleccionarCliente() {
        if (cliente != null) {
            codCli = cliente.getCodigo();
            totalGaranPK.setCodigocliente(cliente.getCodigo());
            totalGaran.setTotalgarantizadoPK(totalGaranPK);
            totalGaran.setCliente(cliente);
        }
    }

    public void save() {
        if (editTotalG) {
            editarTotalG();
            obtenerTotaleGarantizado();
        } else {
            ingresarTotalG();
            obtenerTotaleGarantizado();
        }
    }

    public void ingresarTotalG() {
        try {
            String respuesta;
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.totalgarantizado";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.totalgarantizado";
            url = new URL(direcc);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(totalGaran);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevoTotal').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "TOTAL GARANTIZADO REGISTRADO EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editarTotalG() {
        try {
            String respuesta;
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.totalgarantizado";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.totalgarantizado";
            url = new URL(direcc + "/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(totalGaran);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevoTotal').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "TOTAL GARANTIZADO ACTUALIZADO EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void eliminarTotalG() {
        try {
            String respuesta;
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.totalgarantizado";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.totalgarantizado";
            url = new URL(direcc + "/porId?codigocomercializadora=" + totalGaran.getTotalgarantizadoPK().getCodigocomercializadora() + "&codigocliente=" + totalGaran.getTotalgarantizadoPK().getCodigocliente());
            //url = new URL(direccion + "/porId?codigo=" + totalG.getTotalgarantizadoPK().getCodigocliente());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "TOTAL GARANTIZADO ELIMINADO EXITOSAMENTE");
                obtenerTotaleGarantizado();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevaTotalG() {
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
            listaClientes = new ArrayList<>();
        }
        if (habilitarCli) {
            cliente = new Cliente();
        } else {
            seleccionarCliente();
        }
        totalGaran = new Totalgarantizado();
        totalGaranPK = new TotalgarantizadoPK();
        editTotalG = false;
        soloLectura = false;
        PrimeFaces.current().executeScript("PF('nuevoTotal').show()");
    }

    public void editarTotalGarantizado(Totalgarantizado totalG) {
        soloLectura = true;
        editTotalG = true;
        totalGaran = totalG;
        //obtenerClientes();
        if (totalGaran != null) {
            if (!listaComercializadora.isEmpty()) {
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(totalGaran.getTotalgarantizadoPK().getCodigocomercializadora())) {
                        comercializadora = listaComercializadora.get(i);
                        if (comercializadora != null) {
                            listaClientes = clienteServicio.obtenerClientesActivosPorComercializadora(comercializadora.getCodigo());
                        }

                    }
                }
                if (!listaClientes.isEmpty()) {
                    for (int j = 0; j < listaClientes.size(); j++) {
                        if (listaClientes.get(j).getCodigo().equals(totalGaran.getTotalgarantizadoPK().getCodigocliente())) {
                            cliente = listaClientes.get(j);
                            if (cliente != null) {
                                totalGaran.setCliente(cliente);
                            }
                        }
                    }
                }
            }
        }
        PrimeFaces.current().executeScript("PF('nuevoTotal').show()");
    }

    public Totalgarantizado getTotalGaran() {
        return totalGaran;
    }

    public void setTotalGaran(Totalgarantizado totalGaran) {
        this.totalGaran = totalGaran;
    }

    public TotalgarantizadoPK getTotalGaranPK() {
        return totalGaranPK;
    }

    public void setTotalGaranPK(TotalgarantizadoPK totalGaranPK) {
        this.totalGaranPK = totalGaranPK;
    }

    public List<Totalgarantizado> getListaTotalG() {
        return listaTotalG;
    }

    public void setListaTotalG(List<Totalgarantizado> listaTotalG) {
        this.listaTotalG = listaTotalG;
    }

    public boolean isEditTotalG() {
        return editTotalG;
    }

    public void setEditTotalG(boolean editTotalG) {
        this.editTotalG = editTotalG;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

}
