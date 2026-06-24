/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.actorcomercial;

import ec.com.infinityone.serivicio.actorcomercial.ClienteGarantiaServicio;
import ec.com.infinityone.serivicio.actorcomercial.ClienteServicio;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.servicio.catalogo.BancoServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Banco;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.Clientegarantia;
import ec.com.infinityone.modelo.ClientegarantiaPK;
import ec.com.infinityone.modelo.EnvioFactura;
import ec.com.infinityone.modelo.Totalgarantizado;
import ec.com.infinityone.modelo.TotalgarantizadoPK;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.servicio.pedidosyfacturacion.FacturaServicio;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class ClienteGarantiaBean extends ReusableBean implements Serializable {

    @Inject
    private ClienteGarantiaServicio cliGarantiaServicio;
    @Inject
    private ComercializadoraServicio comercializadoraServicio;
    @Inject
    private ClienteServicio clienteServicio;
    @Inject
    private BancoServicio bancoServicio;
    @Inject
    private FacturaServicio facturaServicio;

    private List<Clientegarantia> listaClientegarantia;

    private List<ComercializadoraBean> listaComercializadora;

    private List<Cliente> listaClientes;

    private List<Banco> listaBancos;

    private List<Totalgarantizado> listaTotalgarantizado;

    private Totalgarantizado totalgarantizado;

    private TotalgarantizadoPK totalgarantizadoPK;

    private Clientegarantia clientegarantia;

    private ClientegarantiaPK clientegarantiaPK;

    private Cliente cliente;

    private ComercializadoraBean comercializadora;

    private Banco banco;

    private String codCliente;

    private String codComer;

    private String codAbas;

    private BigDecimal totalGaran;

    private BigDecimal totalDeuda;

    private BigDecimal saldoDisp;

    private String totalGaranS;

    private String totalDeudaS;

    private String saldoDispS;

    String formattedDate;

    private LocalDateTime fechaActualizacion;

    private boolean estadoClienteGarantia;
    
    private boolean habilitarEstadoCliente;

    private boolean editarCliente;
    /*
    Variable para establecer la fecha de inicio de vigencia en el cliente garantia
     */
    private Date fechaInicioVigencia;
    /*
    Variable para establecer la fecha de fin de vigencia en el cliente garantia
     */
    private Date fechaFinVigencia;
    /*
    Variable que establece si el cliente seleccionado controla o no garantias
     */
    private boolean controlaGarantia;

    private NumberFormat formatoNumero;

    /**
     * Constructor por defecto
     */
    public ClienteGarantiaBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.clientegarantia";
        listaClientegarantia = new ArrayList<>();
        listaTotalgarantizado = new ArrayList<>();
        clientegarantia = new Clientegarantia();
        clientegarantiaPK = new ClientegarantiaPK();
        cliente = new Cliente();
        comercializadora = new ComercializadoraBean();
        banco = new Banco();
        editarCliente = false;
        obtenerComercializadora();
        obtenerBanco();
        fechaInicioVigencia = new Date();
        fechaFinVigencia = new Date();
        controlaGarantia = true;
        formatoNumero = NumberFormat.getNumberInstance();
        totalDeuda = new BigDecimal(0);
        totalDeudaS = formatoNumero.format(totalDeuda);
    }

    public void obtenerClientes() {
        listaClientes = new ArrayList<>();
        listaClientes = this.clienteServicio.obtenerClientes();
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = comercializadoraServicio.obtenerComercializadoras();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda();
        }
    }

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            if (dataUser.getUser().getNiveloperacion().equals("cero")) {
                habilitarComer = true;
            }
            if (dataUser.getUser().getNiveloperacion().equals("adco")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
            }
            if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
            }
        }
    }

    public void obtenerBanco() {
        listaBancos = new ArrayList<>();
        listaBancos = this.bancoServicio.obtenerBanco();
    }

    public void actualizarLista() {
        if (cliente != null) {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            codCliente = params.get("form:cliente_input");
            listaClientegarantia = new ArrayList<>();
            listaClientegarantia = cliGarantiaServicio.obtenerClientesComer(codComer, codCliente);
        }
    }

    public void seleccionarComer() {
        if (comercializadora != null) {
            codComer = comercializadora.getCodigo();
            codAbas = comercializadora.getAbastecedora();
            listaClientes = new ArrayList<>();
            listaClientes = new ArrayList<>();
            listaClientes = clienteServicio.obtenerClientesActivosPorComercializadora(codComer);
        }
    }

    public void seleccionarCliente() throws ParseException {
        if (cliente != null) {
            if (cliente.getControlagarantia() == false) {
                controlaGarantia = false;
                totalDeuda = new BigDecimal(0);
                totalDeudaS = formatoNumero.format(totalDeuda);
                this.dialogo(FacesMessage.SEVERITY_WARN, "El cliente seleccionado no tiene activada la opción para el CONTROL DE GARANTÍA");
            } else {
                controlaGarantia = true;
                totalDeudaS = deudaTotal();
            }
        }
    }

    public String deudaTotal() throws ParseException {
        DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
        String fechaBus = date.format(new Date());
        totalDeuda = new BigDecimal(0);
        List<EnvioFactura> fact = facturaServicio.obtenerFacturasObjEnv(codAbas, codComer, true, false, cliente.getRuc(), fechaBus);
        if (!fact.isEmpty()) {
            for (int i = 0; i < fact.size(); i++) {
                totalDeuda = fact.get(i).getFactura().getValorconrubro().add(totalDeuda);
            }
        }
        totalDeudaS = formatoNumero.format(totalDeuda);
        return totalDeudaS;
    }

    public void save() throws Exception {
        if (editarCliente) {
            editItems();
            actualizarLista();
        } else {
            //if (!esMenorFechaHoy(clientegarantia.getFechafinvigencia()) && (clientegarantia.getFechafinvigencia().after(clientegarantia.getFechainiciovigencia()) || clientegarantia.getFechafinvigencia().equals(clientegarantia.getFechainiciovigencia()))) {
            addItems();
            actualizarLista();
//            //} else {               
//                this.dialogo(FacesMessage.SEVERITY_WARN, "Seleccione un rango de fechas correcto");
//            }
        }
    }

    public void addItems() {
        try {
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            String fechaIni = date.format(fechaInicioVigencia);
            String fechaFin = date.format(clientegarantia.getFechafinvigencia());
            String respuesta;
            int sec = this.cliGarantiaServicio.obtenerUltimaSec(codComer, cliente.getClientePK().getCodigo(), banco.getCodigo(), clientegarantiaPK.getNumero());
            clientegarantiaPK.setSecuencial(sec);
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            objPK.put("codigocomercializadora", codComer);
            objPK.put("codigocliente", cliente.getClientePK().getCodigo());
            objPK.put("codigobanco", banco.getCodigo());
            objPK.put("numero", clientegarantiaPK.getNumero());
            objPK.put("secuencial", clientegarantiaPK.getSecuencial());
            obj.put("clientegarantiaPK", objPK);
            obj.put("activo", estadoClienteGarantia);
            obj.put("fechainiciovigencia", fechaIni);
            obj.put("fechafinvigencia", fechaFin);
            obj.put("valor", clientegarantia.getValor());
            obj.put("observacion", clientegarantia.getObservacion());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();

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
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            String fechaIni = date.format(clientegarantia.getFechainiciovigencia());
            String fechaFin = date.format(clientegarantia.getFechafinvigencia());
            String respuesta;
            url = new URL(direccion + "/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            objPK.put("codigocomercializadora", clientegarantia.getClientegarantiaPK().getCodigocomercializadora());
            objPK.put("codigocliente", clientegarantia.getClientegarantiaPK().getCodigocliente());
            objPK.put("codigobanco", clientegarantia.getClientegarantiaPK().getCodigobanco());
            objPK.put("numero", clientegarantia.getClientegarantiaPK().getNumero());
            objPK.put("secuencial", clientegarantia.getClientegarantiaPK().getSecuencial());
            obj.put("clientegarantiaPK", objPK);
            obj.put("activo", estadoClienteGarantia);
            obj.put("fechainiciovigencia", fechaIni);
            obj.put("fechafinvigencia", fechaFin);
            obj.put("valor", clientegarantia.getValor());
            obj.put("observacion", clientegarantia.getObservacion());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "CLIENTE ACTUALIZADO EXITOSAMENTE");
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
            url = new URL(direccion + "/porId?codigocomercializadora=" + clientegarantia.getClientegarantiaPK().getCodigocomercializadora()
                    + "&codigocliente=" + clientegarantia.getClientegarantiaPK().getCodigocliente()
                    + "&codigobanco=" + clientegarantia.getClientegarantiaPK().getCodigobanco()
                    + "&numero=" + clientegarantia.getClientegarantiaPK().getNumero()
                    + "&secuencial=" + clientegarantia.getClientegarantiaPK().getSecuencial());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            objPK.put("codigocomercializadora", codComer);
            objPK.put("codigocliente", cliente.getClientePK().getCodigo());
            objPK.put("codigobanco", banco.getCodigo());
            objPK.put("numero", clientegarantiaPK.getNumero());
            objPK.put("secuencial", clientegarantiaPK.getSecuencial());
            obj.put("clientegaratiaPK", objPK);
            obj.put("activo", estadoClienteGarantia);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "CLIENTE ELIMINADO EXITOSAMENTE");
                actualizarLista();
            } else {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoClienteGarantia() {
        editarCliente = false;
        estadoClienteGarantia = true;
        habilitarEstadoCliente = true;
        clientegarantia = new Clientegarantia();
        clientegarantiaPK = new ClientegarantiaPK();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Clientegarantia editarClientegarantia(Clientegarantia obj) throws ParseException {
        editarCliente = true;
        clientegarantia = obj;
        estadoClienteGarantia = clientegarantia.getActivo();
        habilitarEstadoCliente = false;
        for (int i = 0; i < listaComercializadora.size(); i++) {
            if (listaComercializadora.get(i).getCodigo().equals(clientegarantia.getClientegarantiaPK().getCodigocomercializadora())) {
                this.comercializadora = listaComercializadora.get(i);
            }
        }
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).getClientePK().getCodigo().equals(clientegarantia.getClientegarantiaPK().getCodigocliente())) {
                this.cliente = listaClientes.get(i);
            }
        }
        for (int i = 0; i < listaBancos.size(); i++) {
            if (listaBancos.get(i).getCodigo().equals(clientegarantia.getClientegarantiaPK().getCodigobanco())) {
                this.banco = listaBancos.get(i);
            }
        }
        totalDeudaS = deudaTotal();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return clientegarantia;
    }

    public void mostrarGarantia() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        codCliente = params.get("form:cliente_input");
        codComer = comercializadora.getCodigo();
        if (!codComer.isEmpty() && !codCliente.isEmpty()) {
            obtenerGarantias(codComer, codCliente);
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            totalGaran = this.listaTotalgarantizado.get(0).getTotalgarantizado().setScale(2, RoundingMode.HALF_DOWN);
            totalDeuda = this.listaTotalgarantizado.get(0).getTotaldeuda().setScale(2, RoundingMode.HALF_DOWN);
            saldoDisp = (this.listaTotalgarantizado.get(0).getTotalgarantizado().subtract(this.listaTotalgarantizado.get(0).getTotaldeuda())).setScale(2, RoundingMode.HALF_DOWN);
            fechaActualizacion = LocalDateTime.now();

            totalGaranS = formatoNumero.format(totalGaran);
            totalDeudaS = formatoNumero.format(totalDeuda);
            saldoDispS = formatoNumero.format(saldoDisp);

            formattedDate = fechaActualizacion.format(formatoFecha);
            PrimeFaces.current().executeScript("PF('garantia').show()");
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "Seleccione un cliente");
        }
    }

    public int porcentajeValores(BigDecimal valor) {
        if (!this.listaTotalgarantizado.isEmpty()) {
            System.out.println("FT:: ENTRA EN EL PRIMER IF porcentajeValores- " + valor.toString());
            if (valor.intValue() == 0) {
                System.out.println("FT:: ENTRA EN EL IF valor.intValue()== 0 porcentajeValores- " + valor.toString());
                this.dialogo(FacesMessage.SEVERITY_ERROR, "Este Cliente NO tiene un valor de garantía asignado!");
                return 0;
            } else {
                System.out.println("FT:: ENTRA A MOSTRAR porcentajeValores- " + valor.toString());
                BigDecimal valorT = this.listaTotalgarantizado.get(0).getTotalgarantizado();

                if (valor.intValue() == 0) {
                    System.out.println("FT:: ENTRA EN EL IF valorT.intValue()== 0 porcentajeValores- " + valor.toString() + " . valorT:. " + valorT.intValue());
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "Este Cliente NO tiene un valor de garantía asignado!");
                    return 0;
                } else {
                    BigDecimal valorN;
                    BigDecimal porcentaje = new BigDecimal(90);
                    if (valorT.intValue() == 0) {
                        valorN = (valor.multiply(porcentaje).divide(valor, 2, RoundingMode.HALF_DOWN));
                    } else {
                        valorN = (valor.multiply(porcentaje).divide(valorT, 2, RoundingMode.HALF_DOWN));
                    }
                    System.out.println("FT:: ENTRA A MOSTRAR porcentajeValores- " + valor.toString() + " RESULTADO: ." + valorN.intValue());
                    return valorN.intValue();
                }
            }
        } else {
            return 0;
        }

    }

    public List<Totalgarantizado> obtenerGarantias(String codCom, String codCli) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.totalgarantizado/porId?"              
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.totalgarantizado/porId?"
                    + "codigocomercializadora=" + codCom + "&codigocliente=" + codCli);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaTotalgarantizado = new ArrayList<>();
            totalgarantizado = new Totalgarantizado();
            totalgarantizadoPK = new TotalgarantizadoPK();
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
                JSONObject garan = retorno.getJSONObject(indice);
                JSONObject garanPK = garan.getJSONObject("totalgarantizadoPK");
                totalgarantizadoPK.setCodigocomercializadora(garanPK.getString("codigocomercializadora"));
                totalgarantizadoPK.setCodigocliente(garanPK.getString("codigocliente"));
                totalgarantizado.setTotalgarantizadoPK(totalgarantizadoPK);
                totalgarantizado.setTotaldeuda(garan.getBigDecimal("totaldeuda"));
                totalgarantizado.setTotalgarantizado(garan.getBigDecimal("totalgarantizado"));
                listaTotalgarantizado.add(totalgarantizado);
                totalgarantizado = new Totalgarantizado();
                totalgarantizadoPK = new TotalgarantizadoPK();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
            return listaTotalgarantizado;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaTotalgarantizado;
    }

    public Boolean esMenorFechaHoy(Date fecha) throws Exception {
        Date hoy = new Date();
        Boolean resultado = false;
        if (fecha != null && fecha.before(hoy)) {
            resultado = true;
        }
        return resultado;
    }

//    public void actualizarFecha(SelectEvent<Date> event, int tipo) throws Exception {
//        if (clientegarantia != null) {
//            switch (tipo) {
//                case 1:
//                    if (esMenorFechaHoy(clientegarantia.getFechainiciovigencia())) {
//                        this.dialogo(FacesMessage.SEVERITY_WARN, "Ingrese una fecha menor a la actual");
//                    }
//                    break;
//                case 2:
//                    if (esMenorFechaHoy(clientegarantia.getFechafinvigencia())) {
//                        this.dialogo(FacesMessage.SEVERITY_WARN, "Ingrese una fecha menor a la actual");
//                    }
//                    if (clientegarantia.getFechainiciovigencia() != null) {
//                        if (clientegarantia.getFechafinvigencia().before(clientegarantia.getFechainiciovigencia())) {
//                            this.dialogo(FacesMessage.SEVERITY_WARN, "Ingrese una fecha no menor a la inicial");
//                        }
//                    }
//                    break;
//                default:
//                    if (!esMenorFechaHoy(clientegarantia.getFechafinvigencia())) {
//                        this.dialogo(FacesMessage.SEVERITY_WARN, "Ingrese una fecha menor a la actual");
//                    }
//                    break;
//            }
//        }
//    }
    public void controlarFecha(SelectEvent<Date> event) {
        fechaFinVigencia = fechaInicioVigencia;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public List<Clientegarantia> getListaClientegarantia() {
        return listaClientegarantia;
    }

    public void setListaClientegarantia(List<Clientegarantia> listaClientegarantia) {
        this.listaClientegarantia = listaClientegarantia;
    }

    public Clientegarantia getClientegarantia() {
        return clientegarantia;
    }

    public void setClientegarantia(Clientegarantia clientegarantia) {
        this.clientegarantia = clientegarantia;
    }

    public boolean isEstadoClienteGarantia() {
        return estadoClienteGarantia;
    }

    public void setEstadoClienteGarantia(boolean estadoClienteGarantia) {
        this.estadoClienteGarantia = estadoClienteGarantia;
    }

    public List<Banco> getListaBancos() {
        return listaBancos;
    }

    public void setListaBancos(List<Banco> listaBancos) {
        this.listaBancos = listaBancos;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public ClientegarantiaPK getClientegarantiaPK() {
        return clientegarantiaPK;
    }

    public void setClientegarantiaPK(ClientegarantiaPK clientegarantiaPK) {
        this.clientegarantiaPK = clientegarantiaPK;
    }

    public boolean isEditarCliente() {
        return editarCliente;
    }

    public void setEditarCliente(boolean editarCliente) {
        this.editarCliente = editarCliente;
    }

    public BigDecimal getTotalGaran() {
        return totalGaran;
    }

    public void setTotalGaran(BigDecimal totalGaran) {
        this.totalGaran = totalGaran;
    }

    public BigDecimal getTotalDeuda() {
        return totalDeuda;
    }

    public void setTotalDeuda(BigDecimal totalDeuda) {
        this.totalDeuda = totalDeuda;
    }

    public BigDecimal getSaldoDisp() {
        return saldoDisp;
    }

    public void setSaldoDisp(BigDecimal saldoDisp) {
        this.saldoDisp = saldoDisp;
    }

    public String getTotalGaranS() {
        return totalGaranS;
    }

    public void setTotalGaranS(String totalGaranS) {
        this.totalGaranS = totalGaranS;
    }

    public String getTotalDeudaS() {
        return totalDeudaS;
    }

    public void setTotalDeudaS(String totalDeudaS) {
        this.totalDeudaS = totalDeudaS;
    }

    public String getSaldoDispS() {
        return saldoDispS;
    }

    public void setSaldoDispS(String saldoDispS) {
        this.saldoDispS = saldoDispS;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public Date getFechaInicioVigencia() {
        return fechaInicioVigencia;
    }

    public void setFechaInicioVigencia(Date fechaInicioVigencia) {
        this.fechaInicioVigencia = fechaInicioVigencia;
    }

    public Date getFechaFinVigencia() {
        return fechaFinVigencia;
    }

    public void setFechaFinVigencia(Date fechaFinVigencia) {
        this.fechaFinVigencia = fechaFinVigencia;
    }

    public boolean isControlaGarantia() {
        return controlaGarantia;
    }

    public void setControlaGarantia(boolean controlaGarantia) {
        this.controlaGarantia = controlaGarantia;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public boolean isHabilitarEstadoCliente() {
        return habilitarEstadoCliente;
    }

    public void setHabilitarEstadoCliente(boolean habilitarEstadoCliente) {
        this.habilitarEstadoCliente = habilitarEstadoCliente;
    }
}
