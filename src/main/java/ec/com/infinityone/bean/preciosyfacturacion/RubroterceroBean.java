/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.preciosyfacturacion;

import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.modelo.Clienterubrotercero;
import ec.com.infinityone.modelo.ClienterubroterceroPK;
import ec.com.infinityone.modelo.Rubrotercero;
import ec.com.infinityone.modelo.RubroterceroPK;
import ec.com.infinityone.servicio.preciosyfacturacion.RubroterceroServicio;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.servicio.preciosyfacturacion.ClienterubroterceroServicio;
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
 * @author David
 */
@Named
@ViewScoped
public class RubroterceroBean extends ReusableBean implements Serializable {

    @Inject
    private ComercializadoraServicio comercializadoraServicio;
    @Inject
    private RubroterceroServicio rubroterceroServicio;
    @Inject
    private ClienterubroterceroServicio clienterubroterceroServicio;

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
    Variable para validar si es guardar o editar
     */
    private boolean editarRubro;
    /*
    Variable que establece true or false para el estado del Banco
     */
    private boolean estadoRubro;

    private Rubrotercero rubrotercero;

    private RubroterceroPK rubroterceroPK;

    private Clienterubrotercero clienterubrotercero;

    private ClienterubroterceroPK clienterubroterceroPK;

    private ComercializadoraBean comercializadora;

    private RubroterceroBean rubrosBean;

    private String codigoComer;

    private Boolean vigente;

    /**
     * Constructor por defecto
     */
    public RubroterceroBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.rubrotercero";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.rubrotercero";

        editarRubro = false;
        rubrotercero = new Rubrotercero();
        rubroterceroPK = new RubroterceroPK();
        rubrosBean = new RubroterceroBean();
        clienterubrotercero = new Clienterubrotercero();
        clienterubroterceroPK = new ClienterubroterceroPK();
        comercializadora = new ComercializadoraBean();
        vigente = false;
        listaComercializadora = new ArrayList<>();
        listaClienterubrotercero = new ArrayList<>();
        obtenerComercializadoras();
        habilitarBusqueda();

    }

    public void obtenerComercializadoras() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = comercializadoraServicio.obtenerComercializadorasActivas();
    }

    public void obtenerRubros(String codComer) {
        listaRubrotercero = new ArrayList<>();
        listaRubrotercero = rubroterceroServicio.obtenerRubrotercero(codComer);
    }

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            if (dataUser.getUser().getNiveloperacion().equals("cero")) {
                habilitarComer = true;
                //obtenerRubros(listaComercializadora.get(0).getCodigo());
            }
            if (dataUser.getUser().getNiveloperacion().equals("adco")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
                //obtenerRubros(listaComercializadora.get(0).getCodigo());
            }
            if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
                //obtenerRubros(listaComercializadora.get(0).getCodigo());
            }
        }
    }

    public void onRowToggle(ToggleEvent event) {
        if (event.getVisibility() == Visibility.VISIBLE) {
            Rubrotercero rubroD = (Rubrotercero) event.getData();
            if (rubroD.getRubroterceroPK().getCodigocomercializadora() != null) {
                obtenerClienteRubro(comercializadora.getCodigo(), rubroD.getRubroterceroPK().getCodigo());
            }
        }
    }

    public void obtenerClienteRubro(String codComer, Long codigoRub) {
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));              

            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.clienterubrotercero/rubro?codigorubrotercero=" + codigoRub);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.clienterubrotercero/rubro?codigocomercializadora=" + codComer + "&codigorubrotercero=" + codigoRub);


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

            listaClienterubrotercero = new ArrayList<>();

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
                JSONObject cliRub = retorno.getJSONObject(indice);
                JSONObject cliRubPK = cliRub.getJSONObject("clienterubroterceroPK");
                clienterubroterceroPK.setCodigo(cliRubPK.getInt("codigorubrotercero"));
                clienterubroterceroPK.setCodigocomercializadora(cliRubPK.getString("codigocomercializadora"));
                clienterubroterceroPK.setCodigocliente(cliRubPK.getString("codigocliente"));
                clienterubrotercero.setClienterubroterceroPK(clienterubroterceroPK);
                clienterubrotercero.setValor(cliRub.getBigDecimal("valor"));
                clienterubrotercero.setCuotas(cliRub.getInt("cuotas"));
                clienterubrotercero.setTipocobro(cliRub.getString("tipocobro"));
                Long lDateIni = cliRub.getLong("fechainiciocobro");
                Date dateIni = new Date(lDateIni);
                clienterubrotercero.setFechainiciocobro(dateIni);
                clienterubrotercero.setUsuarioactual(cliRub.getString("usuarioactual"));

                listaClienterubrotercero.add(clienterubrotercero);
                clienterubrotercero = new Clienterubrotercero();
                clienterubroterceroPK = new ClienterubroterceroPK();
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
        if (editarRubro) {
            editItems();
            obtenerRubros(comercializadora.getCodigo());
        } else {
            addItems();
            obtenerRubros(comercializadora.getCodigo());
        }
    }

    public void addItems() {
        try {
            String respuesta;
            url = new URL(direccion + "/agregar");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            objPK.put("codigocomercializadora", comercializadora.getCodigo());
            objPK.put("codigo", 0);
            obj.put("rubroterceroPK", objPK);
            obj.put("nombre", rubrotercero.getNombre());
            obj.put("activo", estadoRubro);
            obj.put("codigocontable", rubrotercero.getCodigocontable());
            obj.put("tipo", rubrotercero.getTipo());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "RUBRO TERCERO REGISTRADO EXITOSAMENTE");
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
            //rubroterceroPK.setCodigocomercializadora(comercializadora.getCodigo());

            url = new URL(direccion + "/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            objPK.put("codigocomercializadora", rubrotercero.getRubroterceroPK().getCodigocomercializadora());
            objPK.put("codigo", rubrotercero.getRubroterceroPK().getCodigo());
            obj.put("rubroterceroPK", objPK);
            obj.put("nombre", rubrotercero.getNombre());
            obj.put("activo", estadoRubro);
            obj.put("codigocontable", rubrotercero.getCodigocontable());
            obj.put("tipo", rubrotercero.getTipo());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 299) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "RUBRO TERCERO ACUTALIZADO EXITOSAMENTE");
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
            String respuesta;
            //rubroterceroPK.setCodigocomercializadora(comercializadora.getCodigo());
            url = new URL(direccion + "/porId?codigocomercializadora=" + rubrotercero.getRubroterceroPK().getCodigocomercializadora() + "&codigo=" + rubrotercero.getRubroterceroPK().getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
//            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//            JSONObject obj = new JSONObject();
//            JSONObject objPK = new JSONObject();
//            objPK.put("codigocomercializadora", rubrotercero.getRubroterceroPK().getCodigocomercializadora());
//            objPK.put("codigo", rubrotercero.getRubroterceroPK().getCodigo());
//            obj.put("rubroterceroPK", objPK);
//            obj.put("nombre", rubrotercero.getNombre());
//            obj.put("activo", estadoRubro);
//            obj.put("codigocontable", rubrotercero.getCodigocontable());
//            obj.put("tipo", rubrotercero.getTipo());
//            obj.put("usuarioactual", dataUser.getUser().getNombrever());
//            respuesta = obj.toString();
//            writer.write(respuesta);
//            writer.close();

            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "RUBRO TERCERO ELIMINADO EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                obtenerRubros(comercializadora.getCodigo());
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoRubro() {
        estadoRubro = true;
        editarRubro = false;
        soloLectura = false;
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        }
        habilitarBusqueda();
        rubrotercero = new Rubrotercero();        
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Rubrotercero editarRubro(Rubrotercero obj) {
        editarRubro = true;
        rubrotercero = obj; 
        if (clienterubroterceroServicio.obtenerClienteRubroterceroPorRubro(comercializadora.getCodigo(), rubrotercero.getRubroterceroPK().getCodigo()).isEmpty()) {
            soloLectura = false;
        } else {
            soloLectura = true;
            this.dialogo(FacesMessage.SEVERITY_INFO, "El rubro tiene asignado a uno o mas clientes");
        }
        for (int i = 0; i < listaComercializadora.size(); i++) {
            if (listaComercializadora.get(i).getCodigo().equals(rubrotercero.getRubroterceroPK().getCodigocomercializadora())) {
                this.comercializadora = listaComercializadora.get(i);
            }
        }
        habilitarBusqueda();
        estadoRubro = rubrotercero.getActivo();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return rubrotercero;
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
            listaRubrotercero = new ArrayList<>();
            obtenerRubros(comercializadora.getCodigo());
        }
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

    public RubroterceroBean getRubrosBean() {
        return rubrosBean;
    }

    public void setRubrosBean(RubroterceroBean rubrosBean) {
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

    public boolean isEditarRubro() {
        return editarRubro;
    }

    public void setEditarRubro(boolean editarRubro) {
        this.editarRubro = editarRubro;
    }

    public boolean isEditarBanco() {
        return editarRubro;
    }

    public void setEditarBanco(boolean editarPrecio) {
        this.editarRubro = editarPrecio;
    }

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }
}
