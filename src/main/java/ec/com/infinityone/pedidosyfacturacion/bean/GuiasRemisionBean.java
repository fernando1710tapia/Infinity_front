/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.pedidosyfacturacion.bean;

import ec.com.infinityone.preciosyfacturacion.bean.*;
import ec.com.infinityone.actorcomercial.bean.ComercializadoraBean;
import ec.com.infinityone.actorcomercial.serivicios.ComercializadoraServicio;
import ec.com.infinityone.bean.TerminalBean;
import ec.com.infinityone.catalogo.servicios.TerminalServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Detalleprecio;
import ec.com.infinityone.modeloWeb.DetalleprecioPK;
import ec.com.infinityone.modeloWeb.Gravamen;
import ec.com.infinityone.modeloWeb.ObjetoNivel1;
import ec.com.infinityone.modeloWeb.Precio;
import ec.com.infinityone.modeloWeb.PrecioPK;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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
 * @author Andres
 */
@Named
@ViewScoped
public class GuiasRemisionBean extends ReusableBean implements Serializable {

    @Inject
    private ComercializadoraServicio comercializadoraServicio;
    /*
    Variable para acceder a los servicios de Terminal
     */
    @Inject
    protected TerminalServicio termServicio;
    /*
    Variable que almacena varios Bancos
     */
    private List<Precio> listaPrecios;
    /*
    Variable que almacena varios Bancos
     */
    private List<Detalleprecio> listaDetallePrecios;
    /*
    Variable que almacena varios Productos
     */
    private List<ComercializadoraBean> listaComercializadora;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarPrecio;
    /*
    Variable que establece true or false para el estado del Banco
     */
    private boolean estadoBanco;

    private Precio precio;

    private ComercializadoraBean comercializadora;

    private Detalleprecio detallePrecio;

    private JSONObject precPK;

    private JSONObject detPrecPK;

    private JSONObject grav;

    private PrecioPK precioPK;

    private DetalleprecioPK detallePrecioPK;

    private Gravamen gravamen;

    private GuiasRemisionBean precioBean;

    private String codigoComer;

    private Boolean vigente;

    private ObjetoNivel1 objeto1;

    private boolean habilitarComer;
    /*
    Variable acuxiliar para almacerna la lista de precios vigentes
     */
    private List<Precio> listaPrecioAuxiliar;
    /*
    Variable para validar si el precio esta vigente
     */
    private boolean soloVigente;
    /*
    Variable Comercializadora
     */
    protected TerminalBean terminal;
    /*
    Variable que almacena el código del terminal
     */
    protected String codTerminal;
    /*
    Variable para almacenar los datos comercializadora
     */
    protected List<TerminalBean> listaTermianles;
    /**
     * Variable que permite establecer la fecha inicial para realizar la
     * busqueda de guias
     */
    protected Date fechaI;
    /**
     * Variable que permite establecer la fecha final para realizar la busqueda
     * de guias
     */
    protected Date fechaf;

    /**
     * Constructor por defecto
     */
    public GuiasRemisionBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //x = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        soloVigente = false;
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio";

        editarPrecio = false;
        precio = new Precio();
        detallePrecio = new Detalleprecio();
        precioPK = new PrecioPK();
        detallePrecioPK = new DetalleprecioPK();
        precioBean = new GuiasRemisionBean();
        comercializadora = new ComercializadoraBean();
        vigente = false;
        obtenerComercializadora();
        habilitarBusqueda();
        obtenerTerminales();
    }

    public void obtenerTerminales() {
        listaTermianles = new ArrayList<>();
        listaTermianles = termServicio.obtenerTerminal();
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = this.comercializadoraServicio.obtenerComercializadorasActivas();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda();
        }
    }

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            if (dataUser.getUser().getNiveloperacion().equals("cero")) {
                habilitarComer = true;
                //obtenerPrecio(listaComercializadora.get(0).getCodigo(), vigente);
            }
            if (dataUser.getUser().getNiveloperacion().equals("adco")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
                // obtenerPrecio(listaComercializadora.get(0).getCodigo(), vigente);
            }
            if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
                //obtenerPrecio(listaComercializadora.get(0).getCodigo(), vigente);
            }
        }
    }

    public void obtenerPrecio(String codigoComer, Boolean vigente) {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/porComerEstado?codigocomercializadora=" + codigoComer + "&activo=" + vigente);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio/porComerEstado?codigocomercializadora=" + codigoComer + "&activo=" + vigente);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaPrecios = new ArrayList<>();

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
                if (!retorno.isNull(indice)) {
                    JSONObject prec = retorno.getJSONObject(indice);
                    precPK = prec.getJSONObject("precioPK");
                    precioPK.setCodigocomercializadora(precPK.getString("codigocomercializadora"));
                    precioPK.setCodigoterminal(precPK.getString("codigoterminal"));
                    precioPK.setCodigoproducto(precPK.getString("codigoproducto"));
                    precioPK.setCodigomedida(precPK.getString("codigomedida"));
                    precioPK.setCodigolistaprecio(precPK.getLong("codigolistaprecio"));
                    Long lDateIni = precPK.getLong("fechainicio");
                    Date dateIni = new Date(lDateIni);
                    precioPK.setFechainicio(dateIni);
                    precioPK.setSecuencial(precPK.getInt("secuencial"));
                    precioPK.setCodigoPrecio(precPK.getLong("codigoPrecio"));
                    precio.setPrecioPK(precioPK);
                    if (!precPK.isNull("fechafin")) {
                        Long lDateFin = precPK.getLong("fechafin");
                        Date dateFin = new Date(lDateFin);
                        precio.setFechafin(dateFin);
                    } else {
                        precioPK.setFechainicio(new Date());
                    }
                    precio.setActivo(prec.getBoolean("activo"));
                    if (precio.getActivo()) {
//                    precio.setActivoS("S");
                    } else {
//                   precio.setActivoS("N");
                    }
                    precio.setObservacion(prec.getString("observacion"));
                    precio.setPrecioproducto(prec.getBigDecimal("precioproducto"));
                    precio.setUsuarioactual(prec.getString("usuarioactual"));
                    listaPrecios.add(precio);
                    precio = new Precio();
                    precioPK = new PrecioPK();
                }

            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerPrecios(String codComer) {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/porComer?codigocomercializadora=" + codComer);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio/porComer?codigocomercializadora=" + codComer);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaPrecios = new ArrayList<>();

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
                if (!retorno.isNull(indice)) {
                    JSONObject prec = retorno.getJSONObject(indice);
                    precPK = prec.getJSONObject("precioPK");
                    precioPK.setCodigocomercializadora(precPK.getString("codigocomercializadora"));
                    precioPK.setCodigoterminal(precPK.getString("codigoterminal"));
                    precioPK.setCodigoproducto(precPK.getString("codigoproducto"));
                    precioPK.setCodigomedida(precPK.getString("codigomedida"));
                    precioPK.setCodigolistaprecio(precPK.getLong("codigolistaprecio"));
                    Long lDateIni = precPK.getLong("fechainicio");
                    Date dateIni = new Date(lDateIni);
                    precioPK.setFechainicio(dateIni);
                    precioPK.setSecuencial(precPK.getInt("secuencial"));
                    precioPK.setCodigoPrecio(precPK.getLong("codigoPrecio"));
                    precio.setPrecioPK(precioPK);
                    if (!precPK.isNull("fechafin")) {
                        Long lDateFin = precPK.getLong("fechafin");
                        Date dateFin = new Date(lDateFin);
                        precio.setFechafin(dateFin);
                    }
                    precio.setActivo(prec.getBoolean("activo"));
                    if (precio.getActivo()) {
//                    precio.setActivoS("S");
                    } else {
//                   precio.setActivoS("N");
                    }
                    precio.setObservacion(prec.getString("observacion"));
                    precio.setPrecioproducto(prec.getBigDecimal("precioproducto"));
                    precio.setUsuarioactual(prec.getString("usuarioactual"));
                    listaPrecios.add(precio);
                    precio = new Precio();
                    precioPK = new PrecioPK();
                }

            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRowToggle(ToggleEvent event) {
        if (event.getVisibility() == Visibility.VISIBLE) {
            Precio precioD = (Precio) event.getData();
            if (precioD.getPrecioPK().getCodigoPrecio() != null) {
                obtenerDetallePrecio(precioD.getPrecioPK().getCodigoPrecio());
            }
        }
    }

    public void obtenerDetallePrecio(Long codigoPrec) {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.detalleprecio/paraFactura?codigo=" + codigoPrec);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detalleprecio/paraFactura?codigo=" + codigoPrec);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaDetallePrecios = new ArrayList<>();
            gravamen = new Gravamen();

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
                JSONObject detPrec = retorno.getJSONObject(indice);
                detPrecPK = detPrec.getJSONObject("detalleprecioPK");
                grav = detPrec.getJSONObject("gravamen");
                detallePrecioPK.setCodigocomercializadora(detPrecPK.getString("codigocomercializadora"));
                detallePrecioPK.setCodigoterminal(detPrecPK.getString("codigoterminal"));
                detallePrecioPK.setCodigoproducto(detPrecPK.getString("codigoproducto"));
                detallePrecioPK.setCodigomedida(detPrecPK.getString("codigomedida"));
                detallePrecioPK.setCodigolistaprecio(detPrecPK.getLong("codigolistaprecio"));
                Long lDateIni = detPrecPK.getLong("fechainicio");
                Date dateIni = new Date(lDateIni);
                detallePrecioPK.setFechainicio(dateIni);
                detallePrecioPK.setSecuencial(detPrecPK.getInt("secuencial"));
                detallePrecioPK.setCodigo(detPrecPK.getBigInteger("codigo").toString());
                detallePrecioPK.setCodigogravamen(detPrecPK.getString("codigogravamen"));
                gravamen.setNombre(grav.getString("nombre"));
                detallePrecio.setGravamen(gravamen);
                detallePrecio.setDetalleprecioPK(detallePrecioPK);
                detallePrecio.setValor(detPrec.getBigDecimal("valor"));
                detallePrecio.setUsuarioactual(detPrec.getString("usuarioactual"));
                listaDetallePrecios.add(detallePrecio);
                detallePrecio = new Detalleprecio();
                gravamen = new Gravamen();
                detallePrecioPK = new DetalleprecioPK();
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
        if (editarPrecio) {
            editItems();
            obtenerPrecio(comercializadora.getCodigo(), vigente);
        } else {
            addItems();
            obtenerPrecio(comercializadora.getCodigo(), vigente);
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
//            obj.put("codigo", precio.getCodigo());
//            obj.put("nombre", precio.getNombre());
            obj.put("activo", estadoBanco);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "BANCO REGISTRADO EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
            }

            if (connection.getResponseCode() != 200) {
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
            url = new URL(direccion + "/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
//            obj.put("codigo", precio.getCodigo());
//            obj.put("nombre", precio.getNombre());
            obj.put("activo", estadoBanco);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "BANCO ACUTALIZADO EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteItems() {
        try {
            String respuesta;
            url = new URL(direccion + "/porId?codigo=" + precio.getPrecioPK().getCodigoPrecio());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
//            obj.put("codigo", precio.getCodigo());
//            obj.put("nombre", precio.getNombre());
            obj.put("activo", estadoBanco);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "BANCO ELIMINADO EXITOSAMENTE");
                obtenerPrecio(comercializadora.getCodigo(), vigente);
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoBanco() {
        estadoBanco = true;
        editarPrecio = false;
        precio = new Precio();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Precio editarPrecio(Precio obj) {
        editarPrecio = true;
        precio = obj;
        if (precio.getActivo()) {
            estadoBanco = true;
        } else {
            estadoBanco = false;
        }
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return precio;
    }

    public void seleccionarComer() {
        if (comercializadora != null) {
            codigoComer = comercializadora.getCodigo();
        }
    }

    public void actualizarLista() {
        if (precioBean != null) {
            listaPrecios = new ArrayList<>();
            //obtenerPrecio(comercializadora.getCodigo(), true);
            obtenerPrecios(comercializadora.getCodigo());
        }
    }

    public void soloVigentes() {
        listaPrecioAuxiliar = new ArrayList<>();
        if (!listaPrecios.isEmpty()) {
            if (soloVigente) {
                for (int i = 0; i < listaPrecios.size(); i++) {
                    if (listaPrecios.get(i).getActivo() == true) {
                        listaPrecioAuxiliar.add(listaPrecios.get(i));
                    }
                }
                listaPrecios = listaPrecioAuxiliar;
            } else {
                //obtenerPrecio(comercializadora.getCodigo(), true);
                obtenerPrecios(comercializadora.getCodigo());
            }
        } else {
            soloVigente = false;
            this.dialogo(FacesMessage.SEVERITY_ERROR, "PARA PODER VISUALIZAR LOS PRECIOS SOLO VIGENTES, PRIMERO REALIZAR UNA BÚSQUEDA CON REGISTROS");
        }
    }

    public void seleccionarTerminal() {
        if (terminal != null) {
            codTerminal = terminal.getCodigo();
        }
    }

    public List<Detalleprecio> getListaDetallePrecios() {
        return listaDetallePrecios;
    }

    public void setListaDetallePrecios(List<Detalleprecio> listaDetallePrecios) {
        this.listaDetallePrecios = listaDetallePrecios;
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

    public boolean isEditarPrecio() {
        return editarPrecio;
    }

    public void setEditarPrecio(boolean editarPrecio) {
        this.editarPrecio = editarPrecio;
    }

    public Precio getPrecio() {
        return precio;
    }

    public void setPrecio(Precio precio) {
        this.precio = precio;
    }

    public GuiasRemisionBean getPrecioBean() {
        return precioBean;
    }

    public void setPrecioBean(GuiasRemisionBean precioBean) {
        this.precioBean = precioBean;
    }

    public List<Precio> getListaPrecios() {
        return listaPrecios;
    }

    public void setListaPrecios(List<Precio> listaPrecios) {
        this.listaPrecios = listaPrecios;
    }

    public boolean isEditarBanco() {
        return editarPrecio;
    }

    public void setEditarBanco(boolean editarPrecio) {
        this.editarPrecio = editarPrecio;
    }

    public boolean isEstadoBanco() {
        return estadoBanco;
    }

    public void setEstadoBanco(boolean estadoBanco) {
        this.estadoBanco = estadoBanco;
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

    public boolean isHabilitarComer() {
        return habilitarComer;
    }

    public void setHabilitarComer(boolean habilitarComer) {
        this.habilitarComer = habilitarComer;
    }

    public boolean isSoloVigente() {
        return soloVigente;
    }

    public void setSoloVigente(boolean soloVigente) {
        this.soloVigente = soloVigente;
    }

    public TerminalBean getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalBean terminal) {
        this.terminal = terminal;
    }

    public List<TerminalBean> getListaTermianles() {
        return listaTermianles;
    }

    public void setListaTermianles(List<TerminalBean> listaTermianles) {
        this.listaTermianles = listaTermianles;
    }

    public Date getFechaI() {
        return fechaI;
    }

    public void setFechaI(Date fechaI) {
        this.fechaI = fechaI;
    }

    public Date getFechaf() {
        return fechaf;
    }

    public void setFechaf(Date fechaf) {
        this.fechaf = fechaf;
    }

}
