/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.actorcomercial;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ec.com.infinityone.serivicio.actorcomercial.ClienteProductoServicio;
import ec.com.infinityone.serivicio.actorcomercial.ClienteServicio;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.servicio.catalogo.ProductoServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.Clienteproducto;
import ec.com.infinityone.modelo.ClienteproductoPK;
import ec.com.infinityone.modelo.Producto;
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
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class ClienteProductoBean extends ReusableBean implements Serializable {

    @Inject
    private ComercializadoraServicio comercializadoraServicio;
    @Inject
    private ClienteServicio clienteServicio;
    @Inject
    private ProductoServicio productoServicio;
    @Inject
    private ClienteProductoServicio cliProdServicio;

    private List<Cliente> listaClientes;

    private List<Clienteproducto> listaCliProductos;

    private List<Producto> listaProducto;

    private List<Producto> listaProductoCliente;

    private List<Producto> listaProductoFinal;

    private List<ComercializadoraBean> listaComercializadoras;

    private Cliente cliente;

    private ComercializadoraBean comercializadora;

    private String codCliente;

    private String codComer;

    private Clienteproducto clienteproducto;

    private ClienteproductoPK clienteproductoPK;

    private Producto producto;

    private boolean estadoClienteProducto;

    private boolean editarPago;

    /**
     * Constructor por defecto
     */
    public ClienteProductoBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //x = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.clienteproducto";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.clienteproducto";
        listaClientes = new ArrayList<>();
        listaCliProductos = new ArrayList<>();
        listaProducto = new ArrayList<>();
        listaComercializadoras = new ArrayList<>();
        cliente = new Cliente();
        producto = new Producto();
        clienteproducto = new Clienteproducto();
        clienteproductoPK = new ClienteproductoPK();
        estadoClienteProducto = false;
        listaClientes = new ArrayList<>();
        listaProductoCliente = new ArrayList<>();
        listaProductoFinal = new ArrayList<>();
        obtenerComercializadora();
        obtenerProductos();
    }

    public void obtenerClientes() {
        listaClientes = new ArrayList<>();
        listaClientes = this.clienteServicio.obtenerClientes();
    }

    public void obtenerProductos() {
        listaProductoFinal = new ArrayList<>();
        listaProducto = new ArrayList<>();
        listaProducto = this.productoServicio.obtenerProducto();
        listaProductoFinal = this.productoServicio.obtenerProducto();
    }

    public void obtenerComercializadora() {
        listaComercializadoras = new ArrayList<>();
        listaComercializadoras = this.comercializadoraServicio.obtenerComercializadorasActivas();
        if (!listaComercializadoras.isEmpty()) {
            habilitarBusqueda();
        }
    }

    public void actualizarLista() {
        if (cliente != null) {
            codCliente = cliente.getCodigo();
            listaCliProductos = new ArrayList<>();
            listaCliProductos = cliProdServicio.obtenerClienteProductos(codCliente);
        }
    }

    public void seleccionarComercializdora() {
        listaClientes = new ArrayList<>();
        if (comercializadora == null) {
            listaClientes = new ArrayList<>();
        } else {
            listaClientes = clienteServicio.obtenerClientesActivosPorComercializadora(comercializadora.getCodigo());
        }
        listaCliProductos = new ArrayList<>();
    }

    public void seleccionarCliente() {
        obtenerProductos();
        if (cliente != null) {
            codCliente = cliente.getCodigo();
            if (!listaProducto.isEmpty()) {
                listaProductoCliente = new ArrayList<>();
                listaProductoCliente = cliProdServicio.obtenerProductos(codCliente);
                for (int i = 0; i < listaProducto.size(); i++) {
                    for (int j = 0; j < listaProductoCliente.size(); j++) {
                        if (listaProducto.get(i).getCodigo().equals(listaProductoCliente.get(j).getCodigo())) {
                            listaProductoFinal.remove(listaProductoCliente.get(j));
                        }
                    }
                }
            }
        }
    }

    public void save() {
        if (editarPago) {
            editItems();
            actualizarLista();
        } else {
            addItems();
            actualizarLista();
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
            JSONObject objPK = new JSONObject();
            objPK.put("codigo", clienteproducto.getProducto().getCodigo());
            objPK.put("codigocliente", cliente.getCodigo());
            obj.put("clienteproductoPK", objPK);
            obj.put("activo", estadoClienteProducto);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "PRODUCTO REGISTRADO EXITOSAMENTE");
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
            clienteproducto.setActivo(estadoClienteProducto);
            url = new URL(direccion + "/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(this.clienteproducto);
            Gson gson = new Gson();
            String JSON = gson.toJson(this.clienteproducto);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "PRODUCTO ACUTALIZADO EXITOSAMENTE");
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
            url = new URL(direccion + "/porId?codigocliente=" + clienteproducto.getClienteproductoPK().getCodigocliente() + "&codigo=" + clienteproducto.getClienteproductoPK().getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "CLIENTE PRODUCTO ELIMINADO EXITOSAMENTE");
                actualizarLista();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoClienteProducto() {
        editarPago = false;
        soloLectura = false;
        estadoClienteProducto = true;
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        }
        if (habilitarCli) {
            cliente = new Cliente();
            listaClientes = new ArrayList<>();
            clienteproducto = new Clienteproducto();
        } else {
            seleccionarCliente();
        }
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Clienteproducto editarPagoFactura(Clienteproducto obj) {
        editarPago = true;
        soloLectura = true;
        clienteproducto = obj;
        estadoClienteProducto = clienteproducto.getActivo();
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).getCodigo().equals(clienteproducto.getClienteproductoPK().getCodigocliente())) {
                this.clienteproducto.setCliente(listaClientes.get(i));
            }
        }
        for (int i = 0; i < listaProducto.size(); i++) {
            if (listaProducto.get(i).getCodigo().equals(clienteproducto.getClienteproductoPK().getCodigo())) {
                this.clienteproducto.setProducto(listaProducto.get(i));
            }
        }
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return clienteproducto;
    }

    public void habilitarBusqueda() {
        comercializadora = new ComercializadoraBean();
        if (dataUser.getUser() != null) {
            if (dataUser.getUser().getNiveloperacion().equals("cero")) {
                habilitarComer = true;
                habilitarCli = true;
                //listaClientes = clienteServicio.obtenerClientesPorComercializadora(listaComercializadoras.get(0).getCodigo());
            }
            if (dataUser.getUser().getNiveloperacion().equals("adco")) {
                habilitarComer = false;
                habilitarCli = true;
                for (int i = 0; i < listaComercializadoras.size(); i++) {
                    if (listaComercializadoras.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        comercializadora = listaComercializadoras.get(i);
                    }
                }
                listaClientes = clienteServicio.obtenerClientesPorComercializadora(comercializadora.getCodigo());
            }
            if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                habilitarCli = false;
                for (int i = 0; i < listaComercializadoras.size(); i++) {
                    if (listaComercializadoras.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadoras.get(i);
                    }
                }
                listaClientes = clienteServicio.obtenerClientesPorComercializadora(comercializadora.getCodigo());
                for (int i = 0; i < listaClientes.size(); i++) {
                    if (listaClientes.get(i).getCodigo().equals(dataUser.getUser().getCodigocliente())) {
                        this.cliente = listaClientes.get(i);
                    }
                }
            }
        }
    }

    public List<ComercializadoraBean> getListaComercializadoras() {
        return listaComercializadoras;
    }

    public void setListaComercializadoras(List<ComercializadoraBean> listaComercializadoras) {
        this.listaComercializadoras = listaComercializadoras;
    }

    public String getCodComer() {
        return codComer;
    }

    public void setCodComer(String codComer) {
        this.codComer = codComer;
    }

    public boolean isEstadoClienteProducto() {
        return estadoClienteProducto;
    }

    public void setEstadoClienteProducto(boolean estadoClienteProducto) {
        this.estadoClienteProducto = estadoClienteProducto;
    }

    public List<Producto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(List<Producto> listaProducto) {
        this.listaProducto = listaProducto;
    }

    public List<Clienteproducto> getListaCliProductos() {
        return listaCliProductos;
    }

    public void setListaCliProductos(List<Clienteproducto> listaCliProductos) {
        this.listaCliProductos = listaCliProductos;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Clienteproducto getClienteproducto() {
        return clienteproducto;
    }

    public void setClienteproducto(Clienteproducto clienteproducto) {
        this.clienteproducto = clienteproducto;
    }

    public ClienteproductoPK getClienteproductoPK() {
        return clienteproductoPK;
    }

    public void setClienteproductoPK(ClienteproductoPK clienteproductoPK) {
        this.clienteproductoPK = clienteproductoPK;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public boolean isEditarPago() {
        return editarPago;
    }

    public void setEditarPago(boolean editarPago) {
        this.editarPago = editarPago;
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

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public List<Producto> getListaProductoFinal() {
        return listaProductoFinal;
    }

    public void setListaProductoFinal(List<Producto> listaProductoFinal) {
        this.listaProductoFinal = listaProductoFinal;
    }

}
