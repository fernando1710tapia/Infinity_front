/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.actorcomercial.bean;

import ec.com.infinityone.actorcomercial.serivicios.ComercializadoraServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.ObjetoNivel1;
import ec.com.infinityone.modeloWeb.Usuario;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author HP
 */
@Named
@ViewScoped
public class ComercializadoraBean extends ReusableBean implements Serializable {

    @Inject
    private ComercializadoraServicio comercializadoraServicio;

    private ObjetoNivel1 objeto1;
    /*
    Objeto Ccmercializadora
     */
    private ComercializadoraBean comercializadora;
    /*
    Variable que almacena la abastecedora
     */
    private String abastecedora;
    /*
    Variable que almacena varias Comercializaros
     */
    private List<ComercializadoraBean> listaComercializadora;
    /*
    Variable que almacena varias Comercializadoras
     */
    private List<ObjetoNivel1> listaAbastecedoraBean;
    /*
    Variable que almacena varias Comercializadoras
     */
    private List<ObjetoNivel1> listaBancoBean;

    private JSONObject codBanco;

    private JSONObject codAbas;
    /*
    Variable almacena codigo STC
     */
    private String codigoSTC;
    /*
    Variable almacena codigo ARCH
     */
    private String codigoARCH;
    /*
    Variable almacena clave STC
     */
    private String claveSTC;
    /*
    Variable almacena codigo Abastecedora
     */
    private String codigoAbas;
    /*
    Variable almacena ruc
     */
    private String ruc;
    /*
    Variable almacena nombre corto
     */
    private String nombreCorto;
    /*
    Variable almacena id representante legal
     */
    private String idRepLegal;
    /*
    Variable almacena nombre representante legal
     */
    private String nomRepLegal;
    /*
    Variable almacena contribuyente especial
     */
    private String esContriEspecial;
    /*
    Variable almacena telefono 1
     */
    private String tel1;
    /*
    Variable almacena telefono 2
     */
    private String tel2;
    /*
    Variable almacena correo1
     */
    private String correo1;
    /*
    Variable almacena correo2
     */
    private String corre2;
    /*
    Variable almacena tipo plazo credito
     */
    private String tipoPlaCred;
    /*
    Variable almacena dias plazo credito
     */
    private String diasPlaCred;
    /*
    Variable almacena codigo banco debito
     */
    private String codigoBancDeb;
    /*
    Variable almacena cuenta debito
     */
    private String cuentaDeb;
    /*
    Variable almacena tipo cuenta debito
     */
    private String tipoCuenDeb;
    /*
    Variable almacena tasa interes
     */
    private String tasaInteres;
    /*
    Variable almacena fecha vencimiento contrato
     */
    private Date fechaVencCont;
    /*
    Variable almacena fecha inicio contrato
     */
    private Date fechaIniCont;
    /*
    Variable almacena establecimiento fac
     */
    private String estabFac;
    /*
    Variable almacena punto de venta fac
     */
    private String pvFac;
    /*
    Variable almacena establecimiento ndb
     */
    private String estabNdb;
    /*
    Variable almacena punto de venta ndb
     */
    private String pvNdb;
    /*
    Variable almacena establecimiento ncr
     */
    private String estabNcr;
    /*
    Variable almacena punto de venta ncr
     */
    private String pvNcr;
    /*
    Variable almacena prefijo npe
     */
    private String prefijoNpe;
    /*
    Variable almacena clave wsepp
     */
    private String claveWsepp;
    /*
    Variable almacena es agente retencion
     */
    private boolean esAgRetencion;
    /*
    Variable almacena obligado contabilidad
     */
    private boolean obContabilidad;
    /*
    Variable almacena leyenda agente retencion
     */
    private String leyendaAgRetencion;
    /*
    Variable almacena ambiente SRI
     */
    private String ambienteSri;
    /*
    Variable almacena tipo emision
     */
    private String tipoEmision;
    /*
    Variable que establece true or false para el obliagado contabilidad
     */
    private boolean estadoComercializadora;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarComercializadora;

    public ComercializadoraBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {

        x = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.comercializadora";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadora";
        editarComercializadora = false;
        soloLectura = false;
        obtenerListaAbastecedora();
        obtenerListaBanco();
        obtenerListaComercializadora();

    }

    public void obtenerListaComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = this.comercializadoraServicio.obtenerComercializadoras();
        comercializadora = this.comercializadoraServicio.getComercializadora();
    }

    public void obtenerListaAbastecedora() {
        //urlInfinity = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.areamercadeo
        //https://www.supertech.ec:8443/infinityone1/resources/usuario/login?user=Ftapia&password=123456";
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));  
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.abastecedora");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.abastecedora");
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

            listaAbastecedoraBean = new ArrayList<>();
            objeto1 = new ObjetoNivel1();
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
                JSONObject abas = retorno.getJSONObject(indice);
                objeto1.setCodigo(abas.getString("codigo"));
                objeto1.setObjRelacionado(abas.getString("codigo") + " - " + abas.getString("nombre"));
                listaAbastecedoraBean.add(objeto1);
                objeto1 = new ObjetoNivel1();
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerListaBanco() {
        //urlInfinity = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.areamercadeo
        //https://www.supertech.ec:8443/infinityone1/resources/usuario/login?user=Ftapia&password=123456";
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));  
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.banco");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.banco");
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

            listaBancoBean = new ArrayList<>();
            objeto1 = new ObjetoNivel1();
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
                JSONObject banco = retorno.getJSONObject(indice);
                objeto1.setCodigo(banco.getString("codigo"));
                objeto1.setObjRelacionado(banco.getString("codigo") + " - " + banco.getString("nombre"));
                listaBancoBean.add(objeto1);
                objeto1 = new ObjetoNivel1();
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
        if (editarComercializadora) {
            editItems();
            obtenerListaComercializadora();
        } else {
            addItems();
            obtenerListaComercializadora();
        }
    }

    public void addItems() {
        try {
            String fechaIniContS = "";
            String fechaVenContS = "";
            String obliCont = "";
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            if (comercializadora.getFechaIniCont() != null) {
                fechaIniContS = date.format(comercializadora.getFechaIniCont());
            }
            if (comercializadora.getFechaVencCont() != null) {
                fechaVenContS = date.format(comercializadora.getFechaVencCont());
            }
            String respuesta;
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("codigo", comercializadora.getCodigo());
            obj.put("nombre", comercializadora.getNombre());
            obj.put("activo", estadoComercializadora);
            obj.put("codigoarch", comercializadora.getCodigoARCH());
            obj.put("codigostc", comercializadora.getCodigoSTC());
            obj.put("clavestc", comercializadora.getClaveSTC());
            obj.put("codigoabastecedora", comercializadora.getAbastecedora());
            obj.put("ruc", comercializadora.getRuc());
            obj.put("nombrecorto", comercializadora.getNombreCorto());
            obj.put("direccion", comercializadora.getDireccion());
            obj.put("identificacionrepresentantelega", comercializadora.getIdRepLegal());
            obj.put("nombrerepresentantelegal", comercializadora.getNomRepLegal());
            obj.put("escontribuyenteespacial", comercializadora.getEsContriEspecial());
            obj.put("telefono1", comercializadora.getTel1());
            obj.put("telefono2", comercializadora.getTel2());
            obj.put("correo1", comercializadora.getCorreo1());
            obj.put("correo2", comercializadora.getCorre2());
            obj.put("tipoplazocredito", comercializadora.getTipoPlaCred());
            obj.put("diasplazocredito", comercializadora.getDiasPlaCred());
            obj.put("codigobancodebito", comercializadora.getCodigoBancDeb());
            obj.put("cuentadebito", comercializadora.getCuentaDeb());
            obj.put("tipocuentadebito", comercializadora.getTipoCuenDeb());
            obj.put("tasainteres", comercializadora.getTasaInteres());
            obj.put("fehainiciocontrato", fechaIniContS);
            obj.put("fechavencimientocontr", fechaVenContS);
            obj.put("establecimientofac", comercializadora.getEstabFac());
            obj.put("puntoventafac", comercializadora.pvFac);
            obj.put("establecimientondb", comercializadora.getEstabNdb());
            obj.put("puntoventandb", comercializadora.getPvNdb());
            obj.put("establecimientoncr", comercializadora.getEstabNcr());
            obj.put("puntoventancr", comercializadora.getPvNcr());
            obj.put("prefijonpe", comercializadora.getPrefijoNpe());
            obj.put("clavewsepp", comercializadora.getClaveWsepp());
            obj.put("esagenteretencion", comercializadora.isEsAgRetencion());
            if (comercializadora.isObContabilidad()) {
                obliCont = "SI";
            } else {
                obliCont = "NO";
            }
            obj.put("obligadocontabilidad", obliCont);
            obj.put("leyendaagenteretencion", comercializadora.getLeyendaAgRetencion());
            obj.put("ambientesri", comercializadora.getAmbienteSri());
            obj.put("tipoemision", comercializadora.getTipoEmision());
            obj.put("usuarioactual", x.getNombrever());
            respuesta = obj.toString();
            //respuesta = "{\"nombre\":\"asdfg\",\"codigo\":\"21\",\"codigoareamercadeo\":{\"codigo\": \"01\"},\"codigostc\":\"453\",\"codigoarch\":\"456\",\"usuarioactual\":\"ft\"}";
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "COMERCIALIZADORRA REGISTRADA EXITOSAMENTE");
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
            String fechaIniContS = "";
            String fechaVenContS = "";
            String obliCont = "";
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            if (comercializadora.getFechaIniCont() != null) {
                fechaIniContS = date.format(comercializadora.getFechaIniCont());
            }
            if (comercializadora.getFechaVencCont() != null) {
                fechaVenContS = date.format(comercializadora.getFechaVencCont());
            }
            String respuesta;
            url = new URL(direccion + "/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("codigo", comercializadora.getCodigo());
            obj.put("nombre", comercializadora.getNombre());
            obj.put("activo", estadoComercializadora);
            obj.put("codigoarch", comercializadora.getCodigoARCH());
            obj.put("codigostc", comercializadora.getCodigoSTC());
            obj.put("clavestc", comercializadora.getClaveSTC());
            obj.put("codigoabastecedora", comercializadora.getCodigoAbas());
            obj.put("ruc", comercializadora.getRuc());
            obj.put("nombrecorto", comercializadora.getNombreCorto());
            obj.put("direccion", comercializadora.getDireccion());
            obj.put("identificacionrepresentantelega", comercializadora.getIdRepLegal());
            obj.put("nombrerepresentantelegal", comercializadora.getNomRepLegal());
            obj.put("escontribuyenteespacial", comercializadora.getEsContriEspecial());
            obj.put("telefono1", comercializadora.getTel1());
            obj.put("telefono2", comercializadora.getTel2());
            obj.put("correo1", comercializadora.getCorreo1());
            obj.put("correo2", comercializadora.getCorre2());
            obj.put("tipoplazocredito", comercializadora.getTipoPlaCred());
            obj.put("diasplazocredito", comercializadora.getDiasPlaCred());
            obj.put("codigobancodebito", comercializadora.getCodigoBancDeb());
            obj.put("cuentadebito", comercializadora.getCuentaDeb());
            obj.put("tipocuentadebito", comercializadora.getTipoCuenDeb());
            obj.put("tasainteres", comercializadora.getTasaInteres());
            obj.put("fehainiciocontrato", fechaIniContS);
            obj.put("fechavencimientocontr", fechaVenContS);
            obj.put("establecimientofac", comercializadora.getEstabFac());
            obj.put("puntoventafac", comercializadora.getPvFac());
            obj.put("establecimientondb", comercializadora.getEstabNdb());
            obj.put("puntoventandb", comercializadora.getPvNdb());
            obj.put("establecimientoncr", comercializadora.getEstabNcr());
            obj.put("puntoventancr", comercializadora.getPvNcr());
            obj.put("prefijonpe", comercializadora.getPrefijoNpe());
            obj.put("clavewsepp", comercializadora.getClaveWsepp());
            obj.put("esagenteretencion", comercializadora.isEsAgRetencion());
            if (comercializadora.isObContabilidad()) {
                obliCont = "SI";
            } else {
                obliCont = "NO";
            }
            obj.put("obligadocontabilidad", obliCont);
            obj.put("leyendaagenteretencion", comercializadora.getLeyendaAgRetencion());
            obj.put("ambientesri", comercializadora.getAmbienteSri());
            obj.put("tipoemision", comercializadora.getTipoEmision());
            obj.put("usuarioactual", x.getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "COMERCIALIZADORRA ACUTALIZADA EXITOSAMENTE");
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
            url = new URL(direccion + "/porId?codigo=" + comercializadora.getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();            
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "COMERCIALIZADORA ELIMINADA EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                obtenerListaComercializadora();
            } else {
                this.dialogo(FacesMessage.SEVERITY_INFO, "ERROR AL ELIMINAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoComercializadora() {
        estadoComercializadora = true;
        editarComercializadora = false;
        comercializadora = new ComercializadoraBean();
        soloLectura = false;
        objeto1 = new ObjetoNivel1();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public ComercializadoraBean editarComercializadora(ComercializadoraBean obj) {
        soloLectura = false;
        editarComercializadora = true;
        estadoComercializadora = false;
        comercializadora = obj;
        if (comercializadora != null) {
            this.setAbreviacion(obj.getAbreviacion());
            estadoComercializadora = comercializadora.getActivo().equals("S");
            //this.setTipoPlaCred(obj.tipoPlaCred.trim());
        }

        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return comercializadora;
    }

    public ComercializadoraBean lecturaDatosComercializadora(ComercializadoraBean obj) {
        soloLectura = true;
        estadoComercializadora = false;
        comercializadora = obj;
        if (comercializadora != null) {
            this.setAbreviacion(obj.getAbreviacion());
            estadoComercializadora = comercializadora.getActivo().equals("S");
            //this.setTipoPlaCred(obj.tipoPlaCred.trim());
        }

        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return comercializadora;
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

    public List<ObjetoNivel1> getListaAbastecedoraBean() {
        return listaAbastecedoraBean;
    }

    public void setListaAbastecedoraBean(List<ObjetoNivel1> listaAbastecedoraBean) {
        this.listaAbastecedoraBean = listaAbastecedoraBean;
    }

    public List<ObjetoNivel1> getListaBancoBean() {
        return listaBancoBean;
    }

    public void setListaBancoBean(List<ObjetoNivel1> listaBancoBean) {
        this.listaBancoBean = listaBancoBean;
    }

    public boolean isEstadoComercializadora() {
        return estadoComercializadora;
    }

    public void setEstadoComercializadora(boolean estadoComercializadora) {
        this.estadoComercializadora = estadoComercializadora;
    }

    public boolean isEditarComercializadora() {
        return editarComercializadora;
    }

    public void setEditarComercializadora(boolean editarComercializadora) {
        this.editarComercializadora = editarComercializadora;
    }

    public String getAbastecedora() {
        return abastecedora;
    }

    public void setAbastecedora(String abastecedora) {
        this.abastecedora = abastecedora;
    }

    public String getCodigoSTC() {
        return codigoSTC;
    }

    public void setCodigoSTC(String codigoSTC) {
        this.codigoSTC = codigoSTC;
    }

    public String getCodigoARCH() {
        return codigoARCH;
    }

    public void setCodigoARCH(String codigoARCH) {
        this.codigoARCH = codigoARCH;
    }

    public String getClaveSTC() {
        return claveSTC;
    }

    public void setClaveSTC(String claveSTC) {
        this.claveSTC = claveSTC;
    }

    public String getCodigoAbas() {
        return codigoAbas;
    }

    public void setCodigoAbas(String codigoAbas) {
        this.codigoAbas = codigoAbas;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public String getIdRepLegal() {
        return idRepLegal;
    }

    public void setIdRepLegal(String idRepLegal) {
        this.idRepLegal = idRepLegal;
    }

    public String getNomRepLegal() {
        return nomRepLegal;
    }

    public void setNomRepLegal(String nomRepLegal) {
        this.nomRepLegal = nomRepLegal;
    }

    public String getEsContriEspecial() {
        return esContriEspecial;
    }

    public void setEsContriEspecial(String esContriEspecial) {
        this.esContriEspecial = esContriEspecial;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getCorreo1() {
        return correo1;
    }

    public void setCorreo1(String correo1) {
        this.correo1 = correo1;
    }

    public String getCorre2() {
        return corre2;
    }

    public void setCorre2(String corre2) {
        this.corre2 = corre2;
    }

    public String getTipoPlaCred() {
        return tipoPlaCred;
    }

    public void setTipoPlaCred(String tipoPlaCred) {
        this.tipoPlaCred = tipoPlaCred;
    }

    public String getDiasPlaCred() {
        return diasPlaCred;
    }

    public void setDiasPlaCred(String diasPlaCred) {
        this.diasPlaCred = diasPlaCred;
    }

    public String getCodigoBancDeb() {
        return codigoBancDeb;
    }

    public void setCodigoBancDeb(String codigoBancDeb) {
        this.codigoBancDeb = codigoBancDeb;
    }

    public String getCuentaDeb() {
        return cuentaDeb;
    }

    public void setCuentaDeb(String cuentaDeb) {
        this.cuentaDeb = cuentaDeb;
    }

    public String getTipoCuenDeb() {
        return tipoCuenDeb;
    }

    public void setTipoCuenDeb(String tipoCuenDeb) {
        this.tipoCuenDeb = tipoCuenDeb;
    }

    public String getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(String tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public Date getFechaVencCont() {
        return fechaVencCont;
    }

    public void setFechaVencCont(Date fechaVencCont) {
        this.fechaVencCont = fechaVencCont;
    }

    public Date getFechaIniCont() {
        return fechaIniCont;
    }

    public void setFechaIniCont(Date fechaIniCont) {
        this.fechaIniCont = fechaIniCont;
    }

    public String getEstabFac() {
        return estabFac;
    }

    public void setEstabFac(String estabFac) {
        this.estabFac = estabFac;
    }

    public String getPvFac() {
        return pvFac;
    }

    public void setPvFac(String pvFac) {
        this.pvFac = pvFac;
    }

    public String getEstabNdb() {
        return estabNdb;
    }

    public void setEstabNdb(String estabNdb) {
        this.estabNdb = estabNdb;
    }

    public String getPvNdb() {
        return pvNdb;
    }

    public void setPvNdb(String pvNdb) {
        this.pvNdb = pvNdb;
    }

    public String getEstabNcr() {
        return estabNcr;
    }

    public void setEstabNcr(String estabNcr) {
        this.estabNcr = estabNcr;
    }

    public String getPvNcr() {
        return pvNcr;
    }

    public void setPvNcr(String pvNcr) {
        this.pvNcr = pvNcr;
    }

    public String getPrefijoNpe() {
        return prefijoNpe;
    }

    public void setPrefijoNpe(String prefijoNpe) {
        this.prefijoNpe = prefijoNpe;
    }

    public String getClaveWsepp() {
        return claveWsepp;
    }

    public void setClaveWsepp(String claveWsepp) {
        this.claveWsepp = claveWsepp;
    }

    public boolean isEsAgRetencion() {
        return esAgRetencion;
    }

    public void setEsAgRetencion(boolean esAgRetencion) {
        this.esAgRetencion = esAgRetencion;
    }

    public String getLeyendaAgRetencion() {
        return leyendaAgRetencion;
    }

    public void setLeyendaAgRetencion(String leyendaAgRetencion) {
        this.leyendaAgRetencion = leyendaAgRetencion;
    }

    public String getAmbienteSri() {
        return ambienteSri;
    }

    public void setAmbienteSri(String ambienteSri) {
        this.ambienteSri = ambienteSri;
    }

    public String getTipoEmision() {
        return tipoEmision;
    }

    public void setTipoEmision(String tipoEmision) {
        this.tipoEmision = tipoEmision;
    }

    public boolean isObContabilidad() {
        return obContabilidad;
    }

    public void setObContabilidad(boolean obContabilidad) {
        this.obContabilidad = obContabilidad;
    }

}
