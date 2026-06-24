/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean;

import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.servicio.catalogo.TerminalServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Consultagarantia;
import ec.com.infinityone.modelo.ConsultagarantiaPK;
import ec.com.infinityone.modelo.Consultaguiaremision;
import ec.com.infinityone.modelo.ConsultaguiaremisionPK;
import ec.com.infinityone.modelo.Mejorcliente;
import ec.com.infinityone.modelo.Terminal;
import ec.com.infinityone.modelo.Usuario;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class MonitorcomercialBean extends ReusableBean implements Serializable {

    /*
    Variable que llama a los servicios de terminal
     */
    @Inject
    private TerminalServicio terminalServicio;
    /*
    Vaiable que llama a los servicios de Comercializadora
     */
    @Inject
    private ComercializadoraServicio comerServicio;

    private List<Consultagarantia> listaConsultagarantia;

    private Consultagarantia consultagarantia;

    private ConsultagarantiaPK consultagarantiaPK;

    private BigDecimal usoActual;

    private BigDecimal totalGaran;

    private BigDecimal garan3Dias;

    private BigDecimal maximoAlcanzable;

    private BigDecimal utilizado;

    private BigDecimal saldoDisponible;

    private String usoActualS;

    private String totalGaranS;

    private String garan3DiasS;

    private String maximoAlcanzableS;

    private String utilizadoS;

    private String saldoDisponibleS;

    String formattedDate;

    private LocalDateTime fechaActualizacion;

    private Date fechaHoy;

    /*
    Variable para obtener terminales activos
     */
    private Terminal terminal;
    /*
    Lista para almacernar los terminales activos
     */
    private List<Terminal> listaTerminalesActivos;
    /*
    Variable para colocar la direccion de cosnulta de guías de remisión
     */
    private String direccionGuiasR;
    /*
    Variable que almacena una guia de remision
     */
    private Consultaguiaremision guiaRemision;
    /*
    Variable Pk de la entidad consultaguiaremision
     */
    private ConsultaguiaremisionPK guiaRemisionPK;
    /*
    Lista para almacenar las guías de Remisión
     */
    private List<Consultaguiaremision> listaGuiasRemision;
    /*
    Variable para almacenar una comercializadora
     */
    private ComercializadoraBean comercializadora;
    /*
    Lista para almacenar comercializadoras
    */
    private List<ComercializadoraBean> listaComercialiodoras;
    /*
    Variable para seleccionar comercializadora
    */
    private ComercializadoraBean selectedComer;
    /*
    Variable para guardar el codigo de la comercializadora
    */
    private String codComer;

    public MonitorcomercialBean() {
    }

    @PostConstruct
    public void init() {
        x = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");        
        direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.consultagarantia/actualizacion";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.consultagarantia/actualizacion";
        consultagarantia = new Consultagarantia();
        consultagarantiaPK = new ConsultagarantiaPK();
        codComer = x.getCodigocomercializadora();
        terminal = new Terminal();
        fechaHoy = new Date();
        guiaRemision = new Consultaguiaremision();
        guiaRemisionPK = new ConsultaguiaremisionPK();
        //direccionGuiasR = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.consultaguiaremision/actualizacion";
        direccionGuiasR = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.consultaguiaremision/actualizacion";
        comercializadora = new ComercializadoraBean();
        selectedComer = new ComercializadoraBean();
        obtenerlistaComercializadora();
        obtenerPrimeraGarantia();
        obtenerComercializadora();
        if (comercializadora != null) {
            obtenerTerminalesActivos();
        }

    }
    
    public void obtenerlistaComercializadora(){
        listaComercialiodoras = new ArrayList<>();
        listaComercialiodoras = comerServicio.obtenerComercializadoras();
        if(!listaComercialiodoras.isEmpty()){
            for(int i = 0; i < listaComercialiodoras.size(); i++){
                if(listaComercialiodoras.get(i).getCodigo().equals(codComer)){
                    selectedComer = listaComercialiodoras.get(i);
                }
            }
        }
    }
    
    public void seleccionarComercializadora(){
        if(selectedComer != null){
            codComer = selectedComer.getCodigo();
            obtenerPrimeraGarantia();
        }
    }

    public void obtenerTerminalesActivos() {
        listaTerminalesActivos = new ArrayList<>();
        listaTerminalesActivos = terminalServicio.obtenerTerminalesActivos();
        if (!listaTerminalesActivos.isEmpty()) {
            for (int i = 0; i < listaTerminalesActivos.size(); i++) {
                obtenerListaGuiasRemision(listaTerminalesActivos.get(i).getCodigo());
            }
        }
    }

    public void obtenerPrimeraGarantia() {
        obtenerListaGarantias();
        NumberFormat formatoNumero = NumberFormat.getNumberInstance();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        usoActual = this.listaConsultagarantia.get(0).getPorcentajeuso().setScale(2, RoundingMode.HALF_DOWN);
        totalGaran = this.listaConsultagarantia.get(0).getSumaavalizada().setScale(2, RoundingMode.HALF_DOWN);
        garan3Dias = this.listaConsultagarantia.get(0).getValorcomercializadora().setScale(2, RoundingMode.HALF_DOWN);
        maximoAlcanzable = this.listaConsultagarantia.get(0).getGarantia98().setScale(2, RoundingMode.HALF_DOWN);
        utilizado = this.listaConsultagarantia.get(0).getSaldo().multiply(new BigDecimal(-1)).setScale(2, RoundingMode.HALF_DOWN);
        saldoDisponible = this.listaConsultagarantia.get(0).getSaldodisponible().setScale(2, RoundingMode.HALF_DOWN);
        fechaActualizacion = LocalDateTime.now();

        usoActualS = formatoNumero.format(usoActual);
        totalGaranS = formatoNumero.format(totalGaran);
        garan3DiasS = formatoNumero.format(garan3Dias);
        maximoAlcanzableS = formatoNumero.format(maximoAlcanzable);
        utilizadoS = formatoNumero.format(utilizado);
        saldoDisponibleS = formatoNumero.format(saldoDisponible);
        formattedDate = fechaActualizacion.format(formatoFecha);

    }

    public int porcentajeValores(BigDecimal valor) {

        BigDecimal valorT = this.listaConsultagarantia.get(0).getValorcomercializadora();
        BigDecimal porcentaje = new BigDecimal(90);
        BigDecimal valorN = (valor.multiply(porcentaje).divide(valorT, 2, RoundingMode.HALF_DOWN));

        return valorN.intValue();
    }

    public void obtenerListaGarantias() {
        try {
            String respuesta;
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            listaConsultagarantia = new ArrayList<>();

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("cbtcodcom", "");
            obj.put("clave", "1234");
            obj.put("cbtfecemi", 0);
            obj.put("cbtcoddep", "");
            obj.put("codigoComercializadora", codComer);
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String resp = "";
            while ((tmp = br.readLine()) != null) {
                resp += tmp;
            }
            JSONObject objetoJson = new JSONObject(resp);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject garant = retorno.getJSONObject(indice);
                JSONObject garantPK = garant.getJSONObject("consultagarantiaPK");
                consultagarantiaPK.setCodigocomercializadora(garantPK.getString("codigocomercializadora"));
                Long lDateIni = garantPK.getLong("fecharecepcion");
                Date dateIni = new Date(lDateIni);
                consultagarantiaPK.setFecharecepcion(dateIni);
                consultagarantia.setConsultagarantiaPK(consultagarantiaPK);
                consultagarantia.setCodigomoneda(garant.getString("codigomoneda"));
                consultagarantia.setValorcomercializadora(garant.getBigDecimal("valorcomercializadora"));
                consultagarantia.setSumaavalizada(garant.getBigDecimal("sumaavalizada"));
                consultagarantia.setGarantia98(garant.getBigDecimal("garantia98"));
                consultagarantia.setSaldo(garant.getBigDecimal("saldo"));
                consultagarantia.setSaldodisponible(garant.getBigDecimal("saldodisponible"));
                consultagarantia.setPorcentajeuso(garant.getBigDecimal("porcentajeuso"));
                consultagarantia.setActivo(garant.getBoolean("activo"));
                consultagarantia.setUsuarioactual(garant.getString("usuarioactual"));
                listaConsultagarantia.add(consultagarantia);
                consultagarantia = new Consultagarantia();
                consultagarantiaPK = new ConsultagarantiaPK();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerListaGuiasRemision(String codTerminal) {
        try {
            SimpleDateFormat fecha = new SimpleDateFormat("yyyyMMdd");
            String fechaemi = fecha.format(fechaHoy);
            String respuesta;
            url = new URL(direccionGuiasR);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            listaGuiasRemision = new ArrayList<>();
            guiaRemision = new Consultaguiaremision();
            guiaRemisionPK = new ConsultaguiaremisionPK();

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("cbtcodcom", x.getCodigocomercializadora());
            obj.put("clave", comercializadora.getClaveWsepp());
            obj.put("cbtfecemi", fechaemi);
            obj.put("cbtcoddep", codTerminal);
            obj.put("codigoComercializadora", "");
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String resp = "";
            while ((tmp = br.readLine()) != null) {
                resp += tmp;
            }
            JSONObject objetoJson = new JSONObject(resp);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                JSONObject garant = retorno.getJSONObject(indice);
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerComercializadora() {
        comercializadora = comerServicio.obtenerComercializadora(x.getCodigocomercializadora());
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public List<Consultagarantia> getListaConsultagarantia() {
        return listaConsultagarantia;
    }

    public void setListaConsultagarantia(List<Consultagarantia> listaConsultagarantia) {
        this.listaConsultagarantia = listaConsultagarantia;
    }

    public Consultagarantia getConsultagarantia() {
        return consultagarantia;
    }

    public void setConsultagarantia(Consultagarantia consultagarantia) {
        this.consultagarantia = consultagarantia;
    }

    public BigDecimal getUsoActual() {
        return usoActual;
    }

    public void setUsoActual(BigDecimal usoActual) {
        this.usoActual = usoActual;
    }

    public BigDecimal getTotalGaran() {
        return totalGaran;
    }

    public void setTotalGaran(BigDecimal totalGaran) {
        this.totalGaran = totalGaran;
    }

    public BigDecimal getGaran3Dias() {
        return garan3Dias;
    }

    public void setGaran3Dias(BigDecimal garan3Dias) {
        this.garan3Dias = garan3Dias;
    }

    public BigDecimal getMaximoAlcanzable() {
        return maximoAlcanzable;
    }

    public void setMaximoAlcanzable(BigDecimal maximoAlcanzable) {
        this.maximoAlcanzable = maximoAlcanzable;
    }

    public BigDecimal getUtilizado() {
        return utilizado;
    }

    public void setUtilizado(BigDecimal utilizado) {
        this.utilizado = utilizado;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public String getUsoActualS() {
        return usoActualS;
    }

    public void setUsoActualS(String usoActualS) {
        this.usoActualS = usoActualS;
    }

    public String getTotalGaranS() {
        return totalGaranS;
    }

    public void setTotalGaranS(String totalGaranS) {
        this.totalGaranS = totalGaranS;
    }

    public String getGaran3DiasS() {
        return garan3DiasS;
    }

    public void setGaran3DiasS(String garan3DiasS) {
        this.garan3DiasS = garan3DiasS;
    }

    public String getMaximoAlcanzableS() {
        return maximoAlcanzableS;
    }

    public void setMaximoAlcanzableS(String maximoAlcanzableS) {
        this.maximoAlcanzableS = maximoAlcanzableS;
    }

    public String getUtilizadoS() {
        return utilizadoS;
    }

    public void setUtilizadoS(String utilizadoS) {
        this.utilizadoS = utilizadoS;
    }

    public String getSaldoDisponibleS() {
        return saldoDisponibleS;
    }

    public void setSaldoDisponibleS(String saldoDisponibleS) {
        this.saldoDisponibleS = saldoDisponibleS;
    }

    public List<ComercializadoraBean> getListaComercialiodoras() {
        return listaComercialiodoras;
    }

    public void setListaComercialiodoras(List<ComercializadoraBean> listaComercialiodoras) {
        this.listaComercialiodoras = listaComercialiodoras;
    }

    public ComercializadoraBean getSelectedComer() {
        return selectedComer;
    }

    public void setSelectedComer(ComercializadoraBean selectedComer) {
        this.selectedComer = selectedComer;
    }

    public String getCodComer() {
        return codComer;
    }

    public void setCodComer(String codComer) {
        this.codComer = codComer;
    }
    
    

}
