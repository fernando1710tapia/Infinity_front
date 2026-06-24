/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.preciosyfacturacion;

import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Detalleprecio;
import ec.com.infinityone.modelo.DetalleprecioPK;
import ec.com.infinityone.modelo.Gravamen;
import ec.com.infinityone.modelo.Listaprecio;
import ec.com.infinityone.modelo.ListaprecioPK;
import ec.com.infinityone.modelo.ObjetoDetallePrecio;
import ec.com.infinityone.modelo.ObjetoDetallePrecioAux;
import ec.com.infinityone.modelo.ObjetoNivel1;
import ec.com.infinityone.modelo.Precio;
import ec.com.infinityone.modelo.PrecioPK;
import ec.com.infinityone.servicio.preciosyfacturacion.ListaprecioServicio;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author David
 */
@Named
@ViewScoped
public class PrecioBean extends ReusableBean implements Serializable {

    @Inject
    private ComercializadoraServicio comercializadoraServicio;

    @Inject
    private ListaprecioServicio listaprecioServicio;
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
    /*
    Variable Lista Precio
     */
    private Listaprecio precioLista;
    /*
    Variable Lista Precio PK
     */
    private ListaprecioPK precioListaPK;

    private Gravamen gravamen;

    private PrecioBean precioBean;

    private String codigoComer;

    private Boolean vigente;

    private ObjetoNivel1 objeto1;
    /*
    Variable acuxiliar para almacerna la lista de precios vigentes
     */
    private List<Precio> listaPrecioAuxiliar;
    /*
    Variable para validar si el precio esta vigente
     */
    private boolean soloVigente;

    private String ubicacion;
    /*
    Variable Fecha Vencimiento
     */
    private Date fechaVencimiento;
    /*
    Variable para la observación
     */
    private String observacion;

    private ObjetoDetallePrecioAux objDetalle;

    private List<ObjetoDetallePrecioAux> listObjDetalle;

    /**
     * Constructor por defecto
     */
    public PrecioBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        soloVigente = false;
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio";
        ubicacion = "";
        editarPrecio = false;
        precio = new Precio();
        detallePrecio = new Detalleprecio();
        precioPK = new PrecioPK();
        detallePrecioPK = new DetalleprecioPK();
        precioBean = new PrecioBean();
        comercializadora = new ComercializadoraBean();
        vigente = false;
        fechaVencimiento = new Date();
        observacion = "";
        obtenerComercializadora();
        habilitarBusqueda();
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
            precioLista = new Listaprecio();
            precioListaPK = new ListaprecioPK();

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
                    JSONObject listPrecio = prec.getJSONObject("listaprecio");
                    JSONObject listPrecioPK = listPrecio.getJSONObject("listaprecioPK");
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
                    if (!prec.isNull("fechafin")) {
                        Long lDateFin = prec.getLong("fechafin");
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
                    /*------------------Precio Lista------------------------------*/
                    precioLista.setNombre(listPrecio.getString("nombre"));
                    precioLista.setTipo(listPrecio.getString("tipo"));
                    precioListaPK.setCodigo(listPrecioPK.getLong("codigo"));
                    precioLista.setListaprecioPK(precioListaPK);
                    precio.setListaprecio(precioLista);
                    listaPrecios.add(precio);
                    precio = new Precio();
                    precioPK = new PrecioPK();
                    precioLista = new Listaprecio();
                    precioListaPK = new ListaprecioPK();
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
                this.dialogo(FacesMessage.SEVERITY_INFO, "PRECIO ACUTALIZADO EXITOSAMENTE");
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
                this.dialogo(FacesMessage.SEVERITY_INFO, "PRECIO ELIMINADO EXITOSAMENTE");
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

    public void actualizarPreciosPyS() {
        try {
            url = new URL(direccion + "/actualizarVM?sino=true");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "PRECIOS ACUTALIZADOS");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
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

    public String nombreListaprecio(Long codListaprecio) {
        String nombre = "";
        List<Listaprecio> listaPreciosAux = new ArrayList<>();
        if (codListaprecio != null) {
            listaPreciosAux = this.listaprecioServicio.obtenerListaprecioPorComer(codigoComer);
            for (int i = 0; i < listaPreciosAux.size(); i++) {
                if (listaPreciosAux.get(i).getListaprecioPK().getCodigo() == codListaprecio) {
                    nombre = listaPreciosAux.get(i).getNombre();
                }
            }
        }
        return nombre;
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

    public void subirPrecios() {
        listObjDetalle = new ArrayList<>();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
            //ubicacion = "";
            //listaprecioterminalproductoArchivoSubida = new ArrayList<>();
        }
        PrimeFaces.current().executeScript("PF('subirPrecios').show()");
    }

    public void seleccionarComercializdora() {
        if (comercializadora != null) {
            if (comercializadora.getActivo().equals("N")) {
                codigoComer = comercializadora.getCodigo();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA DEBE ESTAR INACTIVA PARA PODER REALIZAR LA ACTUALIZACIÓN DE PRECIOS");
            }
        }
    }

    public void verificarMismoPrecio() {
        try {
            StringBuilder cadenaInfo = new StringBuilder();
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio/porPreciomismodia?";

            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            String fechaI = date.format(fechaVencimiento);

            if (!codigoComer.isEmpty()) {
                url = new URL(direcc + "codigocomercializadora=" + codigoComer
                        + "&fechainicio=" + fechaI
                        + "&activo=" + Boolean.TRUE);

                System.out.println("FT:: verificando fecha del mismo día. " + url);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                InputStreamReader reader = new InputStreamReader(connection.getInputStream());

                BufferedReader br = new BufferedReader(reader);
                String tmp = null;
                String respuesta = "";
                while ((tmp = br.readLine()) != null) {
                    respuesta += tmp;
                }
                JSONObject objetoJson = new JSONObject(respuesta);
                JSONArray retorno = objetoJson.getJSONArray("retorno");

                if (retorno.length() > 0) {

                    System.out.println("FT:: SI HAY PRECIOS del mismo día. " + retorno.length());
                    cadenaInfo.append("Existen Precios VIGENTES en la Fecha Seleccionada");
                    cadenaInfo.append("\nNO DEBERÍA crear Precios en la misma Fecha");
                    cadenaInfo.append("\nSI ES IMPRESCIBLE hacerlo COORDINE CON SUPERTECH.EC!!");

                    //this.dialogo(FacesMessage.SEVERITY_INFO, cadenaInfo.toString());
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "Existen Precios VIGENTES en la Fecha Seleccionada\nNO DEBERÍA crear Precios en la misma Fecha\nSI ES IMPRESCIBLE hacerlo COORDINE CON SUPERTECH.EC!!");
                    System.out.println("Error al añadir:" + connection.getResponseCode());
                    System.out.println("Error:" + connection.getErrorStream());
                    System.out.println(connection.getResponseMessage());
                } else {
                    System.out.println("FT:: NO HAY PRECIOS del mismo día. ");
                }
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "SELECCIONE UNA COMERCIALIZADORA");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String handleFileUpload(FileUploadEvent event) {

        UploadedFile uploadedFile = event.getFile();
        listObjDetalle = new ArrayList<>();
        boolean continuar = true;
        String primerErrorDetectado = "";
        String columnaPrimerErrorDetectado = "";
        try (InputStream inputStream = uploadedFile.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                if (!continuar) {
                    System.out.println("FT:: Error en revisión de estructura de cada columna");
                    throw new Throwable("Error Capturado en metodo handleFileUpload -> Fila con error: " + String.valueOf(rowIndex - 1) + " - Posición de la columna con error: " + columnaPrimerErrorDetectado + " - Valor con error: " + primerErrorDetectado);

                }
                Row row = sheet.getRow(rowIndex);
                objDetalle = new ObjetoDetallePrecioAux();
                if (row != null) {
                    for (int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) {
                        Cell cell = row.getCell(cellIndex);
                        String cellValue = (cell != null) ? cell.toString() : "";
                        System.out.println("cellValue: " + cellValue);
                        if (cellValue.equalsIgnoreCase("")) {
                            columnaPrimerErrorDetectado = String.valueOf(cellIndex + 1);
                            primerErrorDetectado = "VACÍO";
                            continuar = false;
                            break;
                        }
                        switch (cellIndex) {
                            case 0:
                                if (cellValue.length() != 2) {
                                    columnaPrimerErrorDetectado = String.valueOf(cellIndex + 1);
                                    primerErrorDetectado = cellValue;
                                    continuar = false;
                                    break;
                                } else {
                                    objDetalle.setCodigoTerminal(cellValue);
                                    break;
                                }
                            case 1:
                                if (cellValue.length() != 4) {
                                    columnaPrimerErrorDetectado = String.valueOf(cellIndex + 1);
                                    primerErrorDetectado = cellValue;
                                    continuar = false;
                                    break;
                                } else {
                                    objDetalle.setCodigoProd(cellValue);
                                    break;
                                }
                            case 2:
                                if (cellValue.length() != 2) {
                                    columnaPrimerErrorDetectado = String.valueOf(cellIndex + 1);
                                    primerErrorDetectado = cellValue;
                                    continuar = false;
                                    break;
                                } else {
                                    objDetalle.setCodigoMedida(cellValue);
                                    break;
                                }
                            case 3:
                                if (cellValue.indexOf('.') >= 0) {
                                    columnaPrimerErrorDetectado = String.valueOf(cellIndex + 1);
                                    primerErrorDetectado = cellValue;
                                    continuar = false;
                                    break;
                                } else {
                                    objDetalle.setCodigoLisPrecio(cellValue);
                                    break;
                                }
                            case 4:
                                objDetalle.setPvpsugerido(new BigDecimal(cellValue));
                                break;
                            case 5:
                                objDetalle.setPrecioTerminalEpp(new BigDecimal(cellValue));
                                break;
                            case 6:
                                objDetalle.setIva(new BigDecimal(cellValue));
                                break;
                            case 7:
                                objDetalle.setMargenxcliente(new BigDecimal(cellValue));
                                break;
                            case 8:
                                objDetalle.setIvaPresuntivo(new BigDecimal(cellValue));
                                break;
                            case 9:
                                objDetalle.setTresPorMil(new BigDecimal(cellValue));
                                break;
                            case 10:
                                objDetalle.setPrecioProducto(new BigDecimal(cellValue));
                                break;
                            // Agrega más casos para otros atributos
                        }

                    }
                }
                listObjDetalle.add(objDetalle);
            }
            workbook.close();
        } catch (Throwable e) {
            // Maneja las excepciones adecuadamente
            this.dialogo(FacesMessage.SEVERITY_INFO, "Existe un error en la estructura de este archivo: " + e.getMessage());
            System.out.println("FT:: ERROR DETECTADO: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    public void guardarPriceLote() {
        List<JSONObject> arregloJSON = new ArrayList<>();
        arregloJSON.addAll(addItemsArregloJSON(listObjDetalle));
        if (addItemsPriceAux(arregloJSON)) {
            PrimeFaces.current().executeScript("PF('subirPrecios').hide()");
        }
    }

    public List<JSONObject> addItemsArregloJSON(List<ObjetoDetallePrecioAux> listObjDetalle) {
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        //String fechaI = date.format(precio.get(i).getPrecioPK().getFechainicio()) + "T12:00:00";
        String fechaI = date.format(fechaVencimiento) + "T12:00:00";

        JSONObject obj = new JSONObject();
        JSONObject objPK = new JSONObject();
        JSONObject objEnvRest = new JSONObject();
        List<JSONObject> arrObj = new ArrayList<>();
        List<JSONObject> listObjEnvRest = new ArrayList<>();

        for (int j = 0; j < listObjDetalle.size(); j++) {
            objPK.put("codigocomercializadora", codigoComer);
            objPK.put("codigoterminal", listObjDetalle.get(j).getCodigoTerminal());
            objPK.put("codigoproducto", listObjDetalle.get(j).getCodigoProd());
            objPK.put("codigomedida", listObjDetalle.get(j).getCodigoMedida());
            objPK.put("codigolistaprecio", listObjDetalle.get(j).getCodigoLisPrecio());
            objPK.put("fechainicio", fechaI);
            objPK.put("secuencial", "1");
            //objPK.put("codigoPrecio", "0");

            //obj.put("codigoPrecio", observacion);
            obj.put("precioPK", objPK);
            obj.put("fechafin", "");
            obj.put("activo", true);
            obj.put("observacion", observacion);
            obj.put("precioproducto", listObjDetalle.get(j).getPrecioProducto());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            for (int i = 0; i < 6; i++) {
                arrObj.add(addItemsDetailPAux(listObjDetalle.get(j), i));
            }

            objEnvRest.put("precio", obj);
            objEnvRest.put("detalle", arrObj);
            listObjEnvRest.add(objEnvRest); 
            
            System.out.println("FT-2-FRont::. OBJETO JSON-PAGO"+obj.toString() );
            System.out.println("FT-2-FRont::. OBJETO JSON-DETALLEPAGO"+arrObj.toString() );
            System.out.println("FT-2-FRont::. OBJETO JSON-ENVIOPAGO"+objEnvRest.toString() );
            System.out.println("FT-2-FRont::. OBJETO JSON-arreglode ENVIOPAGO"+listObjEnvRest.toString() );
            
            obj = new JSONObject();
            objPK = new JSONObject();
            arrObj = new ArrayList<>();
            objEnvRest = new JSONObject();
        }

        //listaobjEnvRest.add(objEnvRest)           
        return listObjEnvRest;

    }

    public JSONObject addItemsDetailPAux(ObjetoDetallePrecioAux detalleP, int i) {

        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String fechaI = date.format(fechaVencimiento) + "T12:00:00";

        JSONObject detalle = new JSONObject();
        JSONObject detallePK = new JSONObject();

        detallePK.put("codigocomercializadora", codigoComer);
        detallePK.put("codigoterminal", detalleP.getCodigoTerminal());
        detallePK.put("codigoproducto", detalleP.getCodigoProd());
        detallePK.put("codigomedida", detalleP.getCodigoMedida());
        detallePK.put("codigolistaprecio", detalleP.getCodigoLisPrecio());
        detallePK.put("fechainicio", fechaI);
        detallePK.put("secuencial", "1");
        detallePK.put("codigo", "0");
        switch (i) {
            case 0:
                detallePK.put("codigogravamen", "0001");
                detalle.put("valor", detalleP.getPrecioTerminalEpp());
                break;
            case 1:
                detallePK.put("codigogravamen", "0002");
                detalle.put("valor", detalleP.getIva());
                break;
            case 2:
                detallePK.put("codigogravamen", "0005");
                detalle.put("valor", detalleP.getMargenxcliente());
                break;
            case 3:
                detallePK.put("codigogravamen", "0004");
                detalle.put("valor", detalleP.getIvaPresuntivo());
                break;
            case 4:
                detallePK.put("codigogravamen", "0328");
                detalle.put("valor", detalleP.getTresPorMil());
                break;
            case 5:
                detallePK.put("codigogravamen", "0009");
                detalle.put("valor", detalleP.getPrecioProducto());
                break;
        }

        detalle.put("detalleprecioPK", detallePK);
        detalle.put("usuarioactual", dataUser.getUser().getNombrever());

        return detalle;
    }

    public boolean addItemsPriceAux(List<JSONObject> arregloJSON) {
        try {

            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            String respuesta;
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/agregar";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio/agregarlote";

            url = new URL(direcc);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            //JSONObject arrObj = new JSONObject();               

            //arrObj.put("", arregloJSON);
            respuesta = arregloJSON.toString();
            writer.write(respuesta);
            writer.close();

            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "SE HA REGISTRADO CON EXITO");
                System.out.println("Se ha registrado con exito");
                return true;
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println("Error al añadir:" + connection.getResponseCode());
                System.out.println("Error:" + connection.getErrorStream());
                System.out.println(connection.getResponseMessage());
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
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

    public PrecioBean getPrecioBean() {
        return precioBean;
    }

    public void setPrecioBean(PrecioBean precioBean) {
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

    public boolean isSoloVigente() {
        return soloVigente;
    }

    public void setSoloVigente(boolean soloVigente) {
        this.soloVigente = soloVigente;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<ObjetoDetallePrecioAux> getListObjDetalle() {
        return listObjDetalle;
    }

    public void setListObjDetalle(List<ObjetoDetallePrecioAux> listObjDetalle) {
        this.listObjDetalle = listObjDetalle;
    }

}
