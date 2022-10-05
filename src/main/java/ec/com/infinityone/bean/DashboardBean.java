/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Mejorcliente;
import ec.com.infinityone.modeloWeb.ObjetoNivel1;
import ec.com.infinityone.modeloWeb.Usuario;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class DashboardBean extends ReusableBean implements Serializable {

    /*
    Variable que almacena varios Bancos
     */
    private List<Mejorcliente> listaMejorcliente;

    private Mejorcliente mejorcliente;

    private String nombrecliente;

    private BigDecimal sumatotal;

    private Integer facturas;

    private Usuario x;
    private String direccionVentaVsDespacho_Venta;
    private String direccionVentaVsDespacho_Despacho;

    private BarChartModel barModel;

    public DashboardBean() {
    }

    @PostConstruct
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/mejorCliente?activo=true";        
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/mejorCliente?activo=true";
        direccionVentaVsDespacho_Venta = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/ventaTotal";
        direccionVentaVsDespacho_Despacho = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.consultaguiaremision/despachoTotal";
        mejorcliente = new Mejorcliente();

        obtenerListamejorCliente();
        obtenerprimerCliente();
        createBarModel();

        x = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
    }

    public void obtenerprimerCliente() {
        if (!listaMejorcliente.isEmpty()) {
            nombrecliente = this.listaMejorcliente.get(0).getNombrecliente();
            sumatotal = this.listaMejorcliente.get(0).getSumatotal();
            facturas = this.listaMejorcliente.get(0).getFacturas();
        }
    }

    public void obtenerListamejorCliente() {
        try {
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaMejorcliente = new ArrayList<>();
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
                JSONObject mejorCli = retorno.getJSONObject(indice);
                mejorcliente.setNombrecliente(mejorCli.getString("nombrecliente"));
                mejorcliente.setSumatotal(mejorCli.getBigDecimal("sumatotal"));
                mejorcliente.setFacturas(mejorCli.getInt("facturas"));
                listaMejorcliente.add(mejorcliente);
                mejorcliente = new Mejorcliente();
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries boys = new ChartSeries();
        boys.setLabel("Boys");
        boys.set("2004", 120);
        boys.set("2005", 100);
        boys.set("2006", 44);
        boys.set("2007", 150);
        boys.set("2008", 25);

        ChartSeries girls = new ChartSeries();
        girls.setLabel("Girls");
        girls.set("2004", 52);
        girls.set("2005", 60);
        girls.set("2006", 110);
        //girls.set("
        girls.set("2007", 135);
        girls.set("2008", 120);

        model.addSeries(boys);
        model.addSeries(girls);

        return model;
    }

    private void createBarModel() {
        barModel = initBarModel();

        barModel.setTitle("Bar Chart");
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Gender");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Births");
        yAxis.setMin(0);
        yAxis.setMax(200);
    }

    public List<Mejorcliente> getListaMejorcliente() {
        return listaMejorcliente;
    }

    public void setListaMejorcliente(List<Mejorcliente> listaMejorcliente) {
        this.listaMejorcliente = listaMejorcliente;
    }

    public Mejorcliente getMejorcliente() {
        return mejorcliente;
    }

    public void setMejorcliente(Mejorcliente mejorcliente) {
        this.mejorcliente = mejorcliente;
    }

    public String getNombrecliente() {
        return nombrecliente;
    }

    public void setNombrecliente(String nombrecliente) {
        this.nombrecliente = nombrecliente;
    }

    public BigDecimal getSumatotal() {
        return sumatotal;
    }

    public void setSumatotal(BigDecimal sumatotal) {
        this.sumatotal = sumatotal;
    }

    public Integer getFacturas() {
        return facturas;
    }

    public void setFacturas(Integer facturas) {
        this.facturas = facturas;
    }

    public Usuario getX() {
        return x;
    }

    public void setX(Usuario x) {
        this.x = x;
    }

    public String getDireccionVentaVsDespacho_Venta() {
        return direccionVentaVsDespacho_Venta;
    }

    public void setDireccionVentaVsDespacho_Venta(String direccionVentaVsDespacho_Venta) {
        this.direccionVentaVsDespacho_Venta = direccionVentaVsDespacho_Venta;
    }

    public String getDireccionVentaVsDespacho_Despacho() {
        return direccionVentaVsDespacho_Despacho;
    }

    public void setDireccionVentaVsDespacho_Despacho(String direccionVentaVsDespacho_Despacho) {
        this.direccionVentaVsDespacho_Despacho = direccionVentaVsDespacho_Despacho;
    }
    
    
    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
