/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.preciosyfacturacion;

import ec.com.infinityone.serivicio.actorcomercial.ClienteServicio;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.Clienterubrotercero;
import ec.com.infinityone.modelo.ClienterubroterceroPK;
import ec.com.infinityone.modelo.Cuotarubroterceros;
import ec.com.infinityone.modelo.CuotarubrotercerosPK;
import ec.com.infinityone.modelo.Rubrotercero;
import ec.com.infinityone.modelo.RubroterceroPK;
import ec.com.infinityone.servicio.preciosyfacturacion.ClienterubroterceroServicio;
import ec.com.infinityone.servicio.preciosyfacturacion.RubroterceroServicio;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.configuration.Fichero;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class ClienterubroterceroBean extends ReusableBean implements Serializable {

    @Inject
    private ComercializadoraServicio comercializadoraServicio;
    @Inject
    private ClienterubroterceroServicio clienterubroterceroServicio;
    @Inject
    private ClienteServicio clienteServicio;
    @Inject
    private RubroterceroServicio rubroterceroServicio;

    /*
    Variable que almacena varios Bancos
     */
    private List<Rubrotercero> listaRubrotercero;
    /*
    Variable que almacena varios Bancos
     */
    private List<Clienterubrotercero> listaClienterubrotercero;
    /*
    Variable que almacena varios Bancos
     */
    private List<ComercializadoraBean> listaComercializadora;
    /*
    Variable que almacena varios Bancos
     */
    private List<Cliente> listaCliente;
    /*
     */
    private List<Cuotarubroterceros> listaCuotarubro;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarCliRubro;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarCuota;
    /*
    Variable que establece true or false para el estado del Banco
     */
    private boolean estadoRubro;

    private boolean botonGuardar;

    private Rubrotercero rubrotercero;

    private RubroterceroPK rubroterceroPK;

    private Clienterubrotercero clienterubrotercero;

    private ClienterubroterceroPK clienterubroterceroPK;

    private Cuotarubroterceros cuotarubroterceros;

    private CuotarubrotercerosPK cuotarubrotercerosPK;

    private Cliente cliente;

    private ComercializadoraBean comercializadora;

    private ClienterubroterceroBean rubrosBean;

    private String codigoComer;

    private Boolean vigente;

    private boolean desactivarCuota;
    /*
    vairable para validar combo comercializadora
     */
    private boolean bandera;
    /*
    Variable que muestra la pantalla inicial
     */
    private boolean mostrarPantallaInicial;
    /*
    Vairbale para mostrar la pantalla de clienteRubro
     */
    private boolean clienteRubro;
    /*
    Variable para mostrar la pantalla de cuotas
     */
    private boolean pantallaCuotas;

    BigDecimal valorN;

    /**
     * Constructor por defecto
     */
    public ClienterubroterceroBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.clienterubrotercero";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.clienterubrotercero";

        editarCliRubro = false;
        editarCuota = false;
        rubrotercero = new Rubrotercero();
        rubroterceroPK = new RubroterceroPK();
        rubrosBean = new ClienterubroterceroBean();
        clienterubrotercero = new Clienterubrotercero();
        clienterubroterceroPK = new ClienterubroterceroPK();
        comercializadora = new ComercializadoraBean();
        cuotarubroterceros = new Cuotarubroterceros();
        cuotarubrotercerosPK = new CuotarubrotercerosPK();
        vigente = false;
        botonGuardar = true;
        desactivarCuota = false;
        listaComercializadora = new ArrayList<>();
        listaClienterubrotercero = new ArrayList<>();
        listaRubrotercero = new ArrayList<>();
        listaCliente = new ArrayList<>();
        listaCuotarubro = new ArrayList<>();
        obtenerComercializadoras();
        habilitarBusqueda();
        mostrarPantallaInicial = true;
        clienteRubro = false;
        pantallaCuotas = false;
        valorN = new BigDecimal(0);
        //obtenerRubros(listaComercializadora.get(0).getCodigo());
        //obtenerClientes(listaComercializadora.get(0).getCodigo());

    }

    public void cancelar() {
        mostrarPantallaInicial = true;
        clienteRubro = false;
        pantallaCuotas = false;
        editarCliRubro = false;
        rubrotercero = new Rubrotercero();
        rubroterceroPK = new RubroterceroPK();
        rubrosBean = new ClienterubroterceroBean();
        clienterubrotercero = new Clienterubrotercero();
        clienterubroterceroPK = new ClienterubroterceroPK();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        }
        cuotarubroterceros = new Cuotarubroterceros();
        cuotarubrotercerosPK = new CuotarubrotercerosPK();
        vigente = false;
        botonGuardar = true;
        desactivarCuota = false;
        listaClienterubrotercero = new ArrayList<>();
        listaRubrotercero = new ArrayList<>();
        listaCliente = new ArrayList<>();
        listaCuotarubro = new ArrayList<>();
    }

    public void obtenerComercializadoras() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = comercializadoraServicio.obtenerComercializadorasActivas();
    }

    public void obtenerClientesRubros(String codComer) {
        listaClienterubrotercero = new ArrayList<>();
        listaClienterubrotercero = clienterubroterceroServicio.obtenerClienteRubrotercero(codComer);
    }

    public void obtenerRubros(String codComer) {
        listaRubrotercero = new ArrayList<>();
        listaRubrotercero = rubroterceroServicio.obtenerRubrotercero(codComer);
    }

    public void obtenerClientes(String codComer) {
        listaCliente = new ArrayList<>();
        listaCliente = clienteServicio.obtenerClientesPorComercializadora(codComer);
    }

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            if (dataUser.getUser().getNiveloperacion().equals("cero")) {
                habilitarComer = true;
                //obtenerClientesRubros(listaComercializadora.get(0).getCodigo());
                //obtenerClientes(listaComercializadora.get(0).getCodigo());
            }
            if (dataUser.getUser().getNiveloperacion().equals("adco")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
                obtenerClientesRubros(comercializadora.getCodigo());
                obtenerClientes(comercializadora.getCodigo());
                obtenerRubros(comercializadora.getCodigo());
            }
            if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
                obtenerClientesRubros(comercializadora.getCodigo());
                obtenerClientes(comercializadora.getCodigo());
                obtenerRubros(comercializadora.getCodigo());
                listaCliente = clienteServicio.obtenerClientesPorComercializadora(comercializadora.getCodigo());
                for (int i = 0; i < listaCliente.size(); i++) {
                    if (listaCliente.get(i).getCodigo().equals(dataUser.getUser().getCodigocliente())) {
                        this.cliente = listaCliente.get(i);
                    }
                }
            }
        }
    }

    public void onRowToggle(ToggleEvent event) {
        if (event.getVisibility() == Visibility.VISIBLE) {
            Clienterubrotercero rubroD = (Clienterubrotercero) event.getData();
            if (rubroD.getClienterubroterceroPK().getCodigocomercializadora() != null) {
                obtenerCuotasClienteRubro(rubroD.getClienterubroterceroPK().getCodigocliente(), rubroD.getClienterubroterceroPK().getCodigocomercializadora(), rubroD.getClienterubroterceroPK().getCodigo());
            }
        }
    }

    public void obtenerCuotasClienteRubro(String codigoCli, String codigoComer, Long codRubro) {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.cuotarubroterceros/comercli?codigocomercializadora=" + codigoComer + "&codigocliente=" + codigoCli + "&codigorubrotercero=" + codRubro);url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.cuotarubroterceros/comercli?codigocomercializadora=" + codigoComer + "&codigocliente=" + codigoCli + "&codigorubrotercero=" + codRubro);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.cuotarubroterceros/comercli?codigocomercializadora=" + codigoComer + "&codigocliente=" + codigoCli + "&codigorubrotercero=" + codRubro);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaCuotarubro = new ArrayList<>();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject precioJson = new JSONObject(respuesta);
            JSONArray retorno = precioJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject cuotaRub = retorno.getJSONObject(indice);
                JSONObject cuotaRubPK = cuotaRub.getJSONObject("cuotarubrotercerosPK");
                cuotarubrotercerosPK.setCodigocomercializadora(cuotaRubPK.getString("codigocomercializadora"));
                cuotarubrotercerosPK.setCodigorubrotercero(cuotaRubPK.getLong("codigorubrotercero"));
                cuotarubrotercerosPK.setCodigocliente(cuotaRubPK.getString("codigocliente"));
                cuotarubrotercerosPK.setCuota(cuotaRubPK.getInt("cuota"));
                cuotarubroterceros.setCuotarubrotercerosPK(cuotarubrotercerosPK);
                cuotarubroterceros.setPagada(cuotaRub.getBoolean("pagada"));
                Long lDateCob = cuotaRub.getLong("fechacobro");
                Date dateCob = new Date(lDateCob);
                cuotarubroterceros.setFechacobro(dateCob);
                cuotarubroterceros.setValor(cuotaRub.getBigDecimal("valor"));
                cuotarubroterceros.setUsuarioactual(cuotaRub.getString("usuarioactual"));
                cuotarubroterceros.setTipocobro(cuotaRub.getString("tipocobro"));
                Long lDateIni = cuotaRub.getLong("fechainiciocobro");
                Date dateIni = new Date(lDateIni);
                cuotarubroterceros.setFechainiciocobro(dateIni);

                listaCuotarubro.add(cuotarubroterceros);
                cuotarubroterceros = new Cuotarubroterceros();
                cuotarubrotercerosPK = new CuotarubrotercerosPK();
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void regresarPantallaPrincipal() {
        mostrarPantallaInicial = true;
        clienteRubro = false;
        pantallaCuotas = false;
    }

    public void regresarPantallaCliente() {
        mostrarPantallaInicial = false;
        clienteRubro = true;
        pantallaCuotas = false;
        listaCuotarubro = new ArrayList<>();
    }

    public void save() {
        mostrarPantallaInicial = false;
        clienteRubro = false;
        pantallaCuotas = true;
//        if (editarCliRubro) {
//            editItems();
//            obtenerClientesRubros(comercializadora.getCodigo());
//        } else {
        //addItems();
        if (!clienterubrotercero.getTipocobro().equals("FAC")) {
            mostrarListaCuotas(clienterubrotercero);
            botonGuardar = true;
        }
        //obtenerClientesRubros(comercializadora.getCodigo());
        //}
    }

    public void saveCuota() {
        if (editarCuota) {
            editCuotaRubro();
            obtenerClientesRubros(comercializadora.getCodigo());
        } else {
            if (addCuotaRubro()) {
                //editItems();
                obtenerClientesRubros(comercializadora.getCodigo());
            }
        }
    }

    public void agregarCuotaRubro() {
        editarCuota = false;
        obtenerCuotasClienteRubro(clienterubrotercero.getClienterubroterceroPK().getCodigocliente(), clienterubrotercero.getClienterubroterceroPK().getCodigocomercializadora(), clienterubrotercero.getClienterubroterceroPK().getCodigo());
        cuotarubrotercerosPK.setCodigocliente(clienterubrotercero.getClienterubroterceroPK().getCodigocliente());
        cuotarubrotercerosPK.setCodigocomercializadora(clienterubrotercero.getClienterubroterceroPK().getCodigocomercializadora());
        cuotarubrotercerosPK.setCodigorubrotercero(clienterubrotercero.getClienterubroterceroPK().getCodigo());
        Collections.sort(listaCuotarubro, new Comparator<Cuotarubroterceros>() {
            @Override
            public int compare(Cuotarubroterceros lp1, Cuotarubroterceros lp2) {
                return new BigDecimal(lp1.getCuotarubrotercerosPK().getCuota()).compareTo(new BigDecimal(lp2.getCuotarubrotercerosPK().getCuota()));
            }
        });
        if (!listaCuotarubro.isEmpty()) {
            cuotarubrotercerosPK.setCuota(listaCuotarubro.get(listaCuotarubro.size() - 1).getCuotarubrotercerosPK().getCuota() + 1);
        } else {
            cuotarubrotercerosPK.setCuota(1);
        }
        cuotarubroterceros.setCuotarubrotercerosPK(cuotarubrotercerosPK);
        cuotarubroterceros.setTipocobro(clienterubrotercero.getTipocobro());
        cuotarubroterceros.setFechainiciocobro(clienterubrotercero.getFechainiciocobro());
        clienterubrotercero.setCuotas(cuotarubrotercerosPK.getCuota());
        PrimeFaces.current().executeScript("PF('cuotaEdicion').show()");
    }

//    public void siguienteCuotas(){
//        mostrarPantallaInicial = false;
//        clienteRubro = false;
//        pantallaCuotas = true;
//    }
    public void addItems() {
        try {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            String fechaS = date.format(clienterubrotercero.getFechainiciocobro());
            String respuesta;
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            objPK.put("codigorubrotercero", rubrotercero.getRubroterceroPK().getCodigo());
            objPK.put("codigocomercializadora", comercializadora.getCodigo());
            objPK.put("codigocliente", cliente.getCodigo());
            obj.put("clienterubroterceroPK", objPK);
            obj.put("valor", clienterubrotercero.getValor());
            obj.put("tipocobro", clienterubrotercero.getTipocobro());
            if (clienterubrotercero.getTipocobro().equals("FAC")) {
                obj.put("cuotas", "-1");
            } else {
                obj.put("cuotas", clienterubrotercero.getCuotas());
            }
            obj.put("activo", estadoRubro);
            obj.put("fechainiciocobro", fechaS);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                //PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "RUBRO CLIENTE REGISTRADO EXITOSAMENTE");
//                botonGuardar = false;
//                if (!clienterubrotercero.getTipocobro().equals("FAC")) {
//                    mostrarListaCuotas(clienterubrotercero);
//                    botonGuardar = true;
//                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editItems() {
        try {
            String respuesta;
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            String fechaS = date.format(clienterubrotercero.getFechainiciocobro());
            clienterubroterceroPK.setCodigocomercializadora(comercializadora.getCodigo());
            clienterubroterceroPK.setCodigo(rubrotercero.getRubroterceroPK().getCodigo());
            clienterubroterceroPK.setCodigocliente(cliente.getCodigo());

            url = new URL(direccion + "/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            objPK.put("codigorubrotercero", clienterubroterceroPK.getCodigo());
            objPK.put("codigocomercializadora", clienterubroterceroPK.getCodigocomercializadora());
            objPK.put("codigocliente", clienterubroterceroPK.getCodigocliente());
            obj.put("clienterubroterceroPK", objPK);
            obj.put("valor", clienterubrotercero.getValor());
            obj.put("cuotas", clienterubrotercero.getCuotas());
            obj.put("tipocobro", clienterubrotercero.getTipocobro());
            obj.put("activo", clienterubrotercero.getActivo());
            obj.put("fechainiciocobro", fechaS);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200 || connection.getResponseCode() == 204) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "RUBRO CLIENTE ACUTALIZADO EXITOSAMENTE");
                if (!valorN.equals(clienterubrotercero.getValor())) {
                    this.dialogo(FacesMessage.SEVERITY_WARN, "Se debe calcular las cuotas");
                }
                obtenerClientesRubros(comercializadora.getCodigo());
                botonGuardar = false;
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteItems() {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.clienterubrotercero/porId?"
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.clienterubrotercero/porId?"
                    + "codigocomercializadora=" + clienterubrotercero.getClienterubroterceroPK().getCodigocomercializadora()
                    + "&codigorubrotercero=" + clienterubrotercero.getClienterubroterceroPK().getCodigo()
                    + "&codigocliente=" + clienterubrotercero.getClienterubroterceroPK().getCodigocliente());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "RUBRO CLIENTE ELIMINADO EXITOSAMENTE");
                obtenerClientesRubros(comercializadora.getCodigo());
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR RUBRO CLIENTE");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoClienteRubro() {
        mostrarPantallaInicial = false;
        clienteRubro = true;
        pantallaCuotas = false;

        estadoRubro = true;
        editarCliRubro = false;
        botonGuardar = true;
        desactivarCuota = false;
        clienterubrotercero = new Clienterubrotercero();
        clienterubroterceroPK = new ClienterubroterceroPK();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
            listaClienterubrotercero = new ArrayList<>();
            listaRubrotercero = new ArrayList<>();
            listaCliente = new ArrayList<>();
        }
        if (habilitarCli) {
            cliente = new Cliente();
        }
        rubrotercero = new Rubrotercero();
        listaCuotarubro = new ArrayList<>();
        habilitarComboComer();
    }

    public Clienterubrotercero editarRubro() {
        if (true) {
            //clienterubrotercero = obj;
            valorN = clienterubrotercero.getValor();
            estadoRubro = clienterubrotercero.getActivo();
            for (int i = 0; i < listaComercializadora.size(); i++) {
                if (listaComercializadora.get(i).getCodigo().equals(clienterubrotercero.getClienterubroterceroPK().getCodigocomercializadora())) {
                    this.comercializadora = listaComercializadora.get(i);

                }
            }
            obtenerRubros(comercializadora.getCodigo());
            obtenerClientes(comercializadora.getCodigo());
            for (int i = 0; i < listaRubrotercero.size(); i++) {
                if (listaRubrotercero.get(i).getRubroterceroPK().getCodigo() == clienterubrotercero.getClienterubroterceroPK().getCodigo()) {
                    this.rubrotercero = listaRubrotercero.get(i);
                }
            }
            for (int i = 0; i < listaCliente.size(); i++) {
                if (listaCliente.get(i).getCodigo().equals(clienterubrotercero.getClienterubroterceroPK().getCodigocliente())) {
                    this.cliente = listaCliente.get(i);
                }
            }
            if (clienterubrotercero.getTipocobro().equals("FAC")) {
                desactivarCuota = true;
            }
            //habilitarComboComer();
            PrimeFaces.current().executeScript("PF('nuevo').show()");
        }
        return clienterubrotercero;
    }

    public void editarCuotas(Cuotarubroterceros cuota) {
        editarCuota = true;
        cuotarubroterceros = cuota;
        if (cuotarubroterceros.getPagada() == true) {
            this.dialogo(FacesMessage.SEVERITY_WARN, "No se puede editar esta cuota, ya que se encuentra pagada");
        } else {
            PrimeFaces.current().executeScript("PF('cuotaEdicion').show()");
        }
    }

    public boolean habilitarComboComer() {
        if (editarCliRubro == false) {
            if (dataUser.getUser() != null) {
                if (dataUser.getUser().getNiveloperacion().equals("cero")) {
                    bandera = true;
                    cliente = new Cliente();
                } else if (dataUser.getUser().getNiveloperacion().equals("adco")) {
                    bandera = false;
                    cliente = new Cliente();
                } else if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                    bandera = false;
                }
            }
        } else {
            bandera = false;
        }
        return bandera;
    }

    public void seleccionarComer() {
        if (comercializadora != null) {
            codigoComer = comercializadora.getCodigo();
        }
    }

    public void actualizarLista() {
        if (comercializadora.getCodigo() != null) {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            //codigoComer = params.get("form:comercializadora_input");
            listaClienterubrotercero = new ArrayList<>();
            obtenerClientesRubros(comercializadora.getCodigo());
        }
    }

    public void actualizarElementosNuevo() {
        if (comercializadora != null) {
            listaClienterubrotercero = new ArrayList<>();
            obtenerRubros(comercializadora.getCodigo());
            //obtenerClientes(comercializadora.getCodigo());
        }
    }

    public void actualizarElementosClienteXRubro() {
        if (comercializadora != null) {
            if (rubrotercero != null) {
                List<Clienterubrotercero> listaCliAux = new ArrayList<>();
                listaCliAux = clienterubroterceroServicio.obtenerClienteRubroterceroPorRubro(comercializadora.getCodigo(), rubrotercero.getRubroterceroPK().getCodigo());
                listaClienterubrotercero = new ArrayList<>();
                List<Cliente> listaClienteAux = new ArrayList<>();
                obtenerClientes(comercializadora.getCodigo());
                for (int i = 0; i < listaCliente.size(); i++) {
                    for (int j = 0; j < listaCliAux.size(); j++) {
                        if (listaCliente.get(i).getCodigo().equals(listaCliAux.get(j).getClienterubroterceroPK().getCodigocliente())) {
                            listaClienteAux.add(listaCliente.get(i));
                        }
                    }
                }
                listaCliente.removeAll(listaClienteAux);
            }
        }
    }

    public void actualizarCuotas() {
        desactivarCuota = false;
        if (clienterubrotercero.getTipocobro() != null) {
            if (clienterubrotercero.getTipocobro().equals("FAC")) {
                desactivarCuota = true;
            }
        }
    }

    public void mostrarListaCuotas(Clienterubrotercero cliRubTer) {
        BigDecimal valorCuota = new BigDecimal("0");
        BigDecimal valorUltimaCuota = new BigDecimal("0");
        Calendar c = Calendar.getInstance();
        valorCuota = cliRubTer.getValor().divide(new BigDecimal(cliRubTer.getCuotas()), 6, RoundingMode.HALF_DOWN);
        for (int i = 1; i <= cliRubTer.getCuotas(); i++) {
            cuotarubrotercerosPK.setCodigocomercializadora(comercializadora.getCodigo());
            cuotarubrotercerosPK.setCodigorubrotercero(rubrotercero.getRubroterceroPK().getCodigo());
            cuotarubrotercerosPK.setCodigocliente(cliente.getCodigo());
            cuotarubrotercerosPK.setCuota(i);
            cuotarubroterceros.setCuotarubrotercerosPK(cuotarubrotercerosPK);
            cuotarubroterceros.setPagada(false);
            cuotarubroterceros.setTipocobro(cliRubTer.getTipocobro());
            cuotarubroterceros.setFechainiciocobro(cliRubTer.getFechainiciocobro());
            if (cliRubTer.getCuotas() == i) {
                valorUltimaCuota = cliRubTer.getValor().subtract(valorCuota.multiply(new BigDecimal(i - 1)));
                cuotarubroterceros.setValor(valorUltimaCuota);
            } else {
                cuotarubroterceros.setValor(valorCuota);
            }

            if (cliRubTer.getTipocobro().equals("MEN")) {
                if (i == 1) {
                    cuotarubroterceros.setFechacobro(cliRubTer.getFechainiciocobro());
                } else {
                    c.setTime(cliRubTer.getFechainiciocobro());
                    c.add(Calendar.MONTH, i - 1);
                    cuotarubroterceros.setFechacobro(c.getTime());
                }
            } else if (cliRubTer.getTipocobro().equals("LIB")) {
                cuotarubroterceros.setFechacobro(cliRubTer.getFechainiciocobro());
            } else {
                cuotarubroterceros.setFechacobro(null);
            }

            listaCuotarubro.add(cuotarubroterceros);
            cuotarubroterceros = new Cuotarubroterceros();
            cuotarubrotercerosPK = new CuotarubrotercerosPK();
        }
    }

    public void guardarFechaCobro() {
        if (cuotarubroterceros.getFechacobro() != null) {
            for (int i = 0; i < this.listaCuotarubro.size(); i++) {
                if (cuotarubroterceros.getCuotarubrotercerosPK().getCuota() == listaCuotarubro.get(i).getCuotarubrotercerosPK().getCuota()) {
                    this.listaCuotarubro.get(i).setFechacobro(cuotarubroterceros.getFechacobro());
                }
            }
            PrimeFaces.current().executeScript("PF('fechaCobro').hide()");
        } else {
            this.dialogo(FacesMessage.SEVERITY_WARN, "No se puede ingresar fechas de cobro");
        }
    }

    public Cuotarubroterceros editarFechaCobro(Cuotarubroterceros obj) {
        editarCliRubro = true;
        cuotarubroterceros = obj;
        PrimeFaces.current().executeScript("PF('fechaCobro').show()");
        return cuotarubroterceros;
    }

    public void addCuotaRubroTerceros() {
        mostrarPantallaInicial = true;
        clienteRubro = false;
        pantallaCuotas = false;
        addItems();
        for (int i = 0; i < listaCuotarubro.size(); i++) {
            addCuotaRubro(i);
        }
        obtenerClientesRubros(comercializadora.getCodigo());
    }

    public void addCuotaRubro(int i) {
        try {
            String respuesta;
            String fechaS = "";
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            if (listaCuotarubro.get(i).getFechacobro() != null) {
                fechaS = date.format(listaCuotarubro.get(i).getFechacobro());
            }
            String fechaIniS = date.format(listaCuotarubro.get(i).getFechainiciocobro());
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.cuotarubroterceros");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.cuotarubroterceros");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPk = new JSONObject();
            objPk.put("codigocomercializadora", listaCuotarubro.get(i).getCuotarubrotercerosPK().getCodigocomercializadora());
            objPk.put("codigorubrotercero", listaCuotarubro.get(i).getCuotarubrotercerosPK().getCodigorubrotercero());
            objPk.put("codigocliente", listaCuotarubro.get(i).getCuotarubrotercerosPK().getCodigocliente());
            objPk.put("cuota", listaCuotarubro.get(i).getCuotarubrotercerosPK().getCuota());
            obj.put("cuotarubrotercerosPK", objPk);
            obj.put("pagada", listaCuotarubro.get(i).getPagada());
            if (listaCuotarubro.get(i).getFechacobro() != null) {
                obj.put("fechacobro", fechaS);
            } else {
                obj.put("fechacobro", "-99");
            }
            obj.put("valor", listaCuotarubro.get(i).getValor());
            obj.put("tipocobro", listaCuotarubro.get(i).getTipocobro());
            obj.put("fechainiciocobro", fechaIniS);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "CUOTAS REGISTRADAS EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR CUOTA");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean addCuotaRubro() {
        try {
            String respuesta;
            String fechaS = "";
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            if (cuotarubroterceros.getFechacobro() != null) {
                fechaS = date.format(cuotarubroterceros.getFechacobro());
            }
            String fechaIniS = date.format(cuotarubroterceros.getFechainiciocobro());
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.cuotarubroterceros");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.cuotarubroterceros");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPk = new JSONObject();
            objPk.put("codigocomercializadora", cuotarubroterceros.getCuotarubrotercerosPK().getCodigocomercializadora());
            objPk.put("codigorubrotercero", cuotarubroterceros.getCuotarubrotercerosPK().getCodigorubrotercero());
            objPk.put("codigocliente", cuotarubroterceros.getCuotarubrotercerosPK().getCodigocliente());
            objPk.put("cuota", cuotarubroterceros.getCuotarubrotercerosPK().getCuota());
            obj.put("cuotarubrotercerosPK", objPk);
            obj.put("pagada", false);
            if (cuotarubroterceros.getFechacobro() != null) {
                obj.put("fechacobro", fechaS);
            } else {
                obj.put("fechacobro", "-99");
            }
            obj.put("valor", cuotarubroterceros.getValor());
            obj.put("tipocobro", cuotarubroterceros.getTipocobro());
            obj.put("fechainiciocobro", fechaIniS);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "CUOTAS REGISTRADAS EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('cuotaEdicion').hide()");
                return true;
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR CUOTA");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void editCuotaRubro() {
        try {

            String respuesta;
            String fechaS = "";
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");

            fechaS = date.format(cuotarubroterceros.getFechacobro());

            String fechaIniS = date.format(cuotarubroterceros.getFechainiciocobro());

            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.cuotarubroterceros/porId");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.cuotarubroterceros/porId");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPk = new JSONObject();
            objPk.put("codigocomercializadora", cuotarubroterceros.getCuotarubrotercerosPK().getCodigocomercializadora());
            objPk.put("codigorubrotercero", cuotarubroterceros.getCuotarubrotercerosPK().getCodigorubrotercero());
            objPk.put("codigocliente", cuotarubroterceros.getCuotarubrotercerosPK().getCodigocliente());
            objPk.put("cuota", cuotarubroterceros.getCuotarubrotercerosPK().getCuota());
            obj.put("cuotarubrotercerosPK", objPk);
            obj.put("pagada", cuotarubroterceros.getPagada());
            if (cuotarubroterceros.getFechacobro() != null) {
                obj.put("fechacobro", fechaS);
            } else {
                obj.put("fechacobro", "-99");
            }
            obj.put("valor", cuotarubroterceros.getValor());
            obj.put("tipocobro", cuotarubroterceros.getTipocobro());
            obj.put("fechainiciocobro", fechaIniS);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() >= 200 || connection.getResponseCode() <= 299) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "CUOTA ACTUALIZADA EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('cuotaEdicion').hide()");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR CUOTA");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (Throwable e) {
            System.out.println("DA ERROR DETECTADO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteCuota() {
        try {
            if (cuotarubroterceros.getPagada() == true) {
                this.dialogo(FacesMessage.SEVERITY_WARN, "No se puede eliminar esta cuota, ya que se encuentra pagada");
            } else {
                String respuesta = "";
                //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.cuotarubroterceros/porId?"
                url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.cuotarubroterceros/porId?"
                        + "codigocomercializadora=" + cuotarubroterceros.getCuotarubrotercerosPK().getCodigocomercializadora() + "&codigorubrotercero=" + cuotarubroterceros.getCuotarubrotercerosPK().getCodigorubrotercero()
                        + "&codigocliente=" + cuotarubroterceros.getCuotarubrotercerosPK().getCodigocliente() + "&cuota=" + cuotarubroterceros.getCuotarubrotercerosPK().getCuota());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("DELETE");
                connection.setRequestProperty("Content-type", "application/json");
                connection.connect();

                if (connection.getResponseCode() == 200) {
                    this.dialogo(FacesMessage.SEVERITY_INFO, "CUOTA ELIMINADA EXITOSAMENTE");
                    PrimeFaces.current().executeScript("PF('nuevo').hide()");
                    obtenerRubros(comercializadora.getCodigo());
                } else {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR CUOTA");
                    System.out.println(connection.getResponseCode());
                    System.out.println(connection.getResponseMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String nombreCliente(String codcli) {
        String nombre = "";
        listaCliente = new ArrayList<>();
        listaCliente = clienteServicio.obtenerClientes();
        if (codcli != null) {
            for (int i = 0; i < listaCliente.size(); i++) {
                if (listaCliente.get(i).getCodigo().equals(codcli)) {
                    nombre = listaCliente.get(i).getNombrecomercial();
                    break;
                }
            }
        }
        return nombre;
    }

    public boolean isDesactivarCuota() {
        return desactivarCuota;
    }

    public void setDesactivarCuota(boolean desactivarCuota) {
        this.desactivarCuota = desactivarCuota;
    }

    public boolean isBotonGuardar() {
        return botonGuardar;
    }

    public void setBotonGuardar(boolean botonGuardar) {
        this.botonGuardar = botonGuardar;
    }

    public Cuotarubroterceros getCuotarubroterceros() {
        return cuotarubroterceros;
    }

    public void setCuotarubroterceros(Cuotarubroterceros cuotarubroterceros) {
        this.cuotarubroterceros = cuotarubroterceros;
    }

    public CuotarubrotercerosPK getCuotarubrotercerosPK() {
        return cuotarubrotercerosPK;
    }

    public void setCuotarubrotercerosPK(CuotarubrotercerosPK cuotarubrotercerosPK) {
        this.cuotarubrotercerosPK = cuotarubrotercerosPK;
    }

    public List<Cuotarubroterceros> getListaCuotarubro() {
        return listaCuotarubro;
    }

    public void setListaCuotarubro(List<Cuotarubroterceros> listaCuotarubro) {
        this.listaCuotarubro = listaCuotarubro;
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

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public List<Clienterubrotercero> getListaClienterubrotercero() {
        return listaClienterubrotercero;
    }

    public void setListaClienterubrotercero(List<Clienterubrotercero> listaClienterubrotercero) {
        this.listaClienterubrotercero = listaClienterubrotercero;
    }

    public Clienterubrotercero getClienterubrotercero() {
        return clienterubrotercero;
    }

    public void setClienterubrotercero(Clienterubrotercero clienterubrotercero) {
        this.clienterubrotercero = clienterubrotercero;
    }

    public ClienterubroterceroPK getClienterubroterceroPK() {
        return clienterubroterceroPK;
    }

    public void setClienterubroterceroPK(ClienterubroterceroPK clienterubroterceroPK) {
        this.clienterubroterceroPK = clienterubroterceroPK;
    }

    public RubroterceroPK getRubroterceroPK() {
        return rubroterceroPK;
    }

    public void setRubroterceroPK(RubroterceroPK rubroterceroPK) {
        this.rubroterceroPK = rubroterceroPK;
    }

    public boolean isEstadoRubro() {
        return estadoRubro;
    }

    public void setEstadoRubro(boolean estadoRubro) {
        this.estadoRubro = estadoRubro;
    }

    public ClienterubroterceroBean getRubrosBean() {
        return rubrosBean;
    }

    public void setRubrosBean(ClienterubroterceroBean rubrosBean) {
        this.rubrosBean = rubrosBean;
    }

    public List<Rubrotercero> getListaRubrotercero() {
        return listaRubrotercero;
    }

    public void setListaRubrotercero(List<Rubrotercero> listaRubrotercero) {
        this.listaRubrotercero = listaRubrotercero;
    }

    public Rubrotercero getRubrotercero() {
        return rubrotercero;
    }

    public void setRubrotercero(Rubrotercero rubrotercero) {
        this.rubrotercero = rubrotercero;
    }

    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    public String getCodigoComer() {
        return codigoComer;
    }

    public void setCodigoComer(String codigoComer) {
        this.codigoComer = codigoComer;
    }

    public boolean isEditarCliRubro() {
        return editarCliRubro;
    }

    public void setEditarCliRubro(boolean editarCliRubro) {
        this.editarCliRubro = editarCliRubro;
    }

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public boolean isBandera() {
        return bandera;
    }

    public void setBandera(boolean bandera) {
        this.bandera = bandera;
    }

    public boolean isMostrarPantallaInicial() {
        return mostrarPantallaInicial;
    }

    public void setMostrarPantallaInicial(boolean mostrarPantallaInicial) {
        this.mostrarPantallaInicial = mostrarPantallaInicial;
    }

    public boolean isClienteRubro() {
        return clienteRubro;
    }

    public void setClienteRubro(boolean clienteRubro) {
        this.clienteRubro = clienteRubro;
    }

    public boolean isPantallaCuotas() {
        return pantallaCuotas;
    }

    public void setPantallaCuotas(boolean pantallaCuotas) {
        this.pantallaCuotas = pantallaCuotas;
    }

}
