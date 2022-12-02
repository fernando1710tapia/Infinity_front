/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean;

import ec.com.infinityone.catalogo.servicios.TerminalServicio;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.DespachoTotalDto;
import ec.com.infinityone.modeloWeb.Mejorcliente;
import ec.com.infinityone.modeloWeb.Terminal;
import ec.com.infinityone.modeloWeb.Usuario;
import ec.com.infinityone.modeloWeb.VentaTotalDto;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.tooltip.Tooltip;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class DashboardBean extends ReusableBean implements Serializable {

    @Inject
    protected TerminalServicio termServicio;

    /*
    Variable que almacena varios Bancos
     */
    private List<Mejorcliente> listaMejorcliente;

    private List<VentaTotalDto> listaVentaTotalDto;

    private List<DespachoTotalDto> listaDespachoTotalDto;

    private Mejorcliente mejorcliente;

    private VentaTotalDto ventaTotalDto;

    private DespachoTotalDto despachoTotalDto;

    private String nombrecliente;

    private String sumatotal;

    private Integer facturas;

    private Usuario x;
    private String direccionVentaVsDespacho_Venta;
    private String direccionVentaVsDespacho_Despacho;

    private BarChartModel barModel;

    private Date date;
    private Date today;

    private String[] codProd;

    private HashMap<String, String> codigos;

    private List<Terminal> listaTermianles;

    public DashboardBean() {
    }

    @PostConstruct
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/mejorCliente?activo=true";        
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim();
        direccionVentaVsDespacho_Venta = "ec.com.infinity.modelo.factura/ventaTotal";
        direccionVentaVsDespacho_Despacho = "ec.com.infinity.modelo.consultaguiaremision/despachoTotal";
        mejorcliente = new Mejorcliente();
        date = new Date();
        today = new Date();
        codProd = Fichero.getCOLORESPRODUCTOS().split(",");

        obtenerTerminales();

        obtenerCodigosColores();
        obtenerListamejorCliente();
        obtenerprimerCliente();
        actualizarGrafico();

        x = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
    }

    public void obtenerTerminales() {
        listaTermianles = new ArrayList<>();
        listaTermianles = termServicio.obtenerTerminal();
    }

    public void obtenerCodigosColores() {
        codigos = new HashMap<>();
        for (int i = 0; i < codProd.length; i++) {
            String prod = codProd[i].substring(0, 4);
            String cod = codProd[i].substring(4);
            codigos.put(prod, cod);
        }
    }

    public void obtenerprimerCliente() {
        if (!listaMejorcliente.isEmpty()) {
            nombrecliente = this.listaMejorcliente.get(0).getNombrecliente();
            DecimalFormat formato = new DecimalFormat("###,##0.00");
            sumatotal = formato.format(this.listaMejorcliente.get(0).getSumatotal());
            facturas = this.listaMejorcliente.get(0).getFacturas();
        }
    }

    public void obtenerListamejorCliente() {
        try {
            url = new URL(direccion + "ec.com.infinity.modelo.factura/mejorCliente?activo=true");
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

    public void obtenerVentaTotal() {
        try {
            String pfecha;
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            pfecha = dateFormat.format(this.date);
            url = new URL(direccion + "ec.com.infinity.modelo.factura/ventatotal1?pfecha=" + pfecha);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            ventaTotalDto = new VentaTotalDto();
            listaVentaTotalDto = new ArrayList<>();
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
                JSONObject ventTot = retorno.getJSONObject(indice);
                ventaTotalDto.setNombreTerminal(ventTot.getString("nombreTerminal"));
                ventaTotalDto.setNombreProducto(ventTot.getString("nombreProducto"));
                ventaTotalDto.setVolumenTotal(ventTot.getBigDecimal("volumenTotal"));
                ventaTotalDto.setFacturas(ventTot.getInt("facturas"));
                ventaTotalDto.setVolumenTotalD(ventTot.getBigDecimal("volumenTotalD"));
                ventaTotalDto.setGuias(ventTot.getInt("guias"));
                listaVentaTotalDto.add(ventaTotalDto);
                ventaTotalDto = new VentaTotalDto();
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerDespachoTotal() {
        try {
            String pfecha;
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            pfecha = dateFormat.format(this.date);
            url = new URL(direccion + "ec.com.infinity.modelo.consultaguiaremision/despachototal?pfecha=" + pfecha);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            despachoTotalDto = new DespachoTotalDto();
            listaDespachoTotalDto = new ArrayList<>();
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
                JSONObject despTot = retorno.getJSONObject(indice);
                despachoTotalDto.setNombreTerminal(despTot.getString("nombreTerminal"));
                despachoTotalDto.setNombreProducto(despTot.getString("nombreProducto"));
                despachoTotalDto.setVolumenTotal(despTot.getBigDecimal("volumenTotal"));
                despachoTotalDto.setGuias(despTot.getInt("guias"));
                listaDespachoTotalDto.add(despachoTotalDto);
                despachoTotalDto = new DespachoTotalDto();
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
//
//        ChartSeries boys = new ChartSeries();
//        boys.setLabel("Despacho Total");
//        for (int i = 0; i < listaDespachoTotalDto.size(); i++) {
//            boys.set(listaDespachoTotalDto.get(i).getNombreTerminal(), listaDespachoTotalDto.get(i).getVolumenTotal());
//        }
////        boys.set("2004", 52);
////        boys.set("2005", 60);
////        boys.set("2006", 110);
////        boys.set("2007", 135);
//
//        ChartSeries girls = new ChartSeries();
//        girls.setLabel("Venta Total");
//        for (int i = 0; i < listaVentaTotalDto.size(); i++) {
//            girls.set(listaVentaTotalDto.get(i).getNombreTerminal(), listaVentaTotalDto.get(i).getVolumenTotal());
//        }
////
////        girls.set("2004", 52);
////        girls.set("2005", 60);
////        girls.set("2006", 110);
////        //girls.set("
////        girls.set("2007", 135);
////        girls.set("2008", 120);
//
//        model.addSeries(boys);
//        model.addSeries(girls);

        return model;
    }

//    private void createBarModel() {
//        barModel = initBarModel();
//
//        barModel.setTitle("Bar Chart");
//        barModel.setLegendPosition("ne");
//
//        Axis xAxis = barModel.getAxis(AxisType.X);
//        xAxis.setLabel("Gender");
//
//        Axis yAxis = barModel.getAxis(AxisType.Y);
//        yAxis.setLabel("Births");
//        yAxis.setMin(0);
//        yAxis.setMax(200);
//    }
    public String[] coloresProducto(String cod, int tipo) {
        String r = "";
        String g = "";
        String b = "";
        String[] producto = new String[2];
        switch (tipo) {
//Despachos
            case 1:
//                if (codigos.containsKey(cod.substring(6, 10))) {
//                    r = codigos.get(cod.substring(6, 10));
//                } else {
//                    r = "255";
//                }
//                g = "255";
//                b = "0";
                
                //
//                System.out.println("FT:: DESPACHOS-PRODU-COLOR. "+cod);
                if (codigos.containsKey(cod.substring(6, 10))) {
                    
                    r = (codigos.get(cod.substring(6, 10))).substring(0, 3);
                    g = (codigos.get(cod.substring(6, 10))).substring(3, 6);
                    b = (codigos.get(cod.substring(6, 10))).substring(6);
//                    System.out.println("FT:: COLOR. "+r+" - "+g+" - "+b );
                } else {
                    r = "255";
                    g = "255";
                    b = "255";
                } 
//                g = "255";
//                b = "0";
                //
                
                break;
//ventas
            case 2:
//                r = "0";
//                if (codigos.containsKey(cod.substring(6, 10))) {
//                    g = codigos.get(cod.substring(6, 10));
//                } else {
//                    g = "255";
//                }
//                b = "255";
                
//                System.out.println("FT:: VENTAS-PRODU-COLOR. "+cod);
                if (codigos.containsKey(cod.substring(6, 10))) {
                                    
                    r = (codigos.get(cod.substring(6, 10))).substring(9, 12);
                    g = (codigos.get(cod.substring(6, 10))).substring(12, 15);
                    b = (codigos.get(cod.substring(6, 10))).substring(15, 18);
//                    System.out.println("FT:: COLOR. "+r+" - "+g+" - "+b );
                } else {
                    r = "255";
                    g = "255";
                    b = "255";
                } 
                break;
        }
        producto[0] = "rgba(" + r + ", " + g + ", " + b + ")";
        producto[1] = "rgba(" + r + ", " + g + ", " + b + ")";
//        String[] extra = {"rgba(222, 162, 66, .7)", "rgb(222, 162, 66)"};
//        String[] diesel = {"rgba(57, 163, 244, .7)", "rgb(57, 163, 244)"};
//        if (!cod.isEmpty()) {
//            if (cod.substring(0, 4).equals("0101")) {
//                return extra;
//            }
//            if (cod.substring(0, 4).equals("0121")) {
//                return diesel;
//            }
//        }
        return producto;
    }

    public void createBarModel() {
        boolean bandera = false;
        barModel = new BarChartModel();
        ChartData data = new ChartData();
        List<BarChartDataSet> barDataSetList = new ArrayList<>();
        BarChartDataSet barDataSet = new BarChartDataSet();
        BarChartDataSet barDataSet2 = new BarChartDataSet();
//        List<Number> values = new ArrayList<>();
//        List<Number> values2 = new ArrayList<>();
        HashMap<String, List<VentaTotalDto>> mapaVentas = new HashMap<>();
        List<VentaTotalDto> VentaTotalDtoAux = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        VentaTotalDto ventaTotalDtoAux = new VentaTotalDto();

        mapaVentas = new HashMap<>();
        for (int i = 0; i < listaTermianles.size(); i++) {
            for (int j = 0; j < listaVentaTotalDto.size(); j++) {
                String cod = listaVentaTotalDto.get(j).getNombreTerminal().substring(0, 1);
                if (listaVentaTotalDto.get(j).getNombreTerminal().substring(0, 2).equals(listaTermianles.get(i).getCodigo())) {
                    if (!mapaVentas.containsKey(listaVentaTotalDto.get(j).getNombreTerminal())) {
                        VentaTotalDtoAux = new ArrayList<>();
                    }
                    VentaTotalDtoAux.add(listaVentaTotalDto.get(j));
                    mapaVentas.put(listaVentaTotalDto.get(j).getNombreTerminal(), VentaTotalDtoAux);
                }
            }
        }

        List<String> nomProd = new ArrayList<>();
        for (int i = 0; i < listaVentaTotalDto.size(); i++) {
            nomProd.add(listaVentaTotalDto.get(i).getNombreProducto());
        }
        Set nuevaLista = new HashSet<>(nomProd);
        nomProd.clear();
        nomProd.addAll(nuevaLista);

        List<String> nombres = new ArrayList<>();
        for (int i = 0; i < listaVentaTotalDto.size(); i++) {
            nombres.add(listaVentaTotalDto.get(i).getNombreTerminal());
        }
        Set nuevaListaT = new HashSet<>(nombres);
        nombres.clear();
        nombres.addAll(nuevaListaT);

        if (!mapaVentas.isEmpty()) {

            for (int i = 0; i < nomProd.size(); i++) {
                for (int j = 0; j < listaVentaTotalDto.size(); j++) {
                    if (listaVentaTotalDto.get(j).getNombreProducto().equals(nomProd.get(i))) {
                        ventaTotalDtoAux = listaVentaTotalDto.get(j);
                        break;
                    }
                }
                //ventas
                barDataSet2 = new BarChartDataSet();
                 barDataSet2.setLabel(ventaTotalDtoAux.getNombreProducto()); // +" -Docs: "+ventaTotalDtoAux.getFacturas()+" -Vol: "
                barDataSet2.setBackgroundColor(coloresProducto(ventaTotalDtoAux.getNombreProducto(), 2)[0]);
                barDataSet2.setBorderColor(coloresProducto(ventaTotalDtoAux.getNombreProducto(), 2)[1]);
                barDataSet2.setStack("Ventas");
                barDataSet2.setBorderWidth(1);
                List<Number> values2 = new ArrayList<>();
                for (Map.Entry<String, List<VentaTotalDto>> entry : mapaVentas.entrySet()) {
                    bandera = false;
                    for (int k = 0; k < listaVentaTotalDto.size(); k++) {
                        if (listaVentaTotalDto.get(k).getNombreProducto().equals(nomProd.get(i)) && listaVentaTotalDto.get(k).getNombreTerminal().equals(entry.getKey())) {
                                
//                            System.out.println("FT::valores y etiquetas: "+listaVentaTotalDto.get(k).getNombreProducto()+" - "+listaVentaTotalDto.get(k).getVolumenTotal());
                            values2.add(listaVentaTotalDto.get(k).getVolumenTotal());
//                            if(listaVentaTotalDto.get(k).getVolumenTotal().compareTo(BigDecimal.ZERO)!=0){
//                                System.out.println("FT::valores y etiquetas dentro del IF: "+listaVentaTotalDto.get(k).getNombreProducto()+" - "+listaVentaTotalDto.get(k).getVolumenTotal());
//                                barDataSet2.setLabel(ventaTotalDtoAux.getNombreProducto());
//                                 
//                            }
                            bandera = true;
                        }
                    }
                    if (!bandera) {
//                        System.out.println("FT::  if (!bandera)");
                        values2.add(new BigDecimal(0)); 
//                        barDataSet2.setLabel(" --- ");
                    }
                }
                
                barDataSet2.setData(values2);
                barDataSetList.add(barDataSet2);

                //despachos
                barDataSet = new BarChartDataSet();
                barDataSet.setLabel("DES-" + ventaTotalDtoAux.getNombreProducto().substring(5)); //+" -Docs: "+ventaTotalDtoAux.getGuias()+" -Vol: "
                barDataSet.setBackgroundColor(coloresProducto(ventaTotalDtoAux.getNombreProducto(), 1)[0]);
                barDataSet.setBorderColor(coloresProducto(ventaTotalDtoAux.getNombreProducto(), 1)[1]);
                barDataSet.setStack("Despachos");
                barDataSet.setBorderWidth(1);
                List<Number> values = new ArrayList<>();
                for (Map.Entry<String, List<VentaTotalDto>> entry : mapaVentas.entrySet()) {
                    bandera = false;
                    for (int k = 0; k < listaVentaTotalDto.size(); k++) {
                        if (listaVentaTotalDto.get(k).getNombreProducto().equals(nomProd.get(i)) && listaVentaTotalDto.get(k).getNombreTerminal().equals(entry.getKey())) {
                            values.add(listaVentaTotalDto.get(k).getVolumenTotalD());
                            System.out.println("FT::valores y etiquetas: "+listaVentaTotalDto.get(k).getNombreProducto()+" - "+listaVentaTotalDto.get(k).getVolumenTotal());
//                            if(listaVentaTotalDto.get(k).getVolumenTotalD().compareTo(BigDecimal.ZERO)==0){
//                                barDataSet.setLabel("DES-" + ventaTotalDtoAux.getNombreProducto().substring(5));
//                                barDataSet.setLabel("DES-" + ventaTotalDtoAux.getNombreProducto().substring(5)+" -SIN DATOS EN ESTE TERMINAL- ");
//                                System.out.println("FT::valores y etiquetas DENTRO DEL IF: "+listaVentaTotalDto.get(k).getNombreProducto()+" - "+listaVentaTotalDto.get(k).getVolumenTotal());
//                            }

                            bandera = true;
                        }
                    }
                    if (!bandera) {
                        values.add(new BigDecimal(0));
//                        barDataSet.setLabel(" --- ");
                    }
                }
                barDataSet.setData(values);
                barDataSetList.add(barDataSet);
            }

            for (Map.Entry<String, List<VentaTotalDto>> entry : mapaVentas.entrySet()) {
                labels.add(entry.getKey());
            }

            for (int i = 0; i < barDataSetList.size(); i++) {
                data.addChartDataSet(barDataSetList.get(i));
            }

        }

        data.setLabels(labels);
        barModel.setData(data);
        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        //linearAxes.setBeginAtZero(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        linearAxes.setTicks(ticks);
        linearAxes.setStacked(true);
        cScales.addXAxesData(linearAxes);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

//        Title title = new Title();
//        title.setDisplay(true);
//        title.setText("Bar Chart");
//        options.setTitle(title);
        Tooltip tooltip = new Tooltip();
        tooltip.setMode("index");
        tooltip.setIntersect(false);
        options.setTooltip(tooltip);

        barModel.setOptions(options);
    }
    //public void createBarModel() {
    //        barModel = new BarChartModel();
    //        ChartData data = new ChartData();
    //        List<BarChartDataSet> barDataSetList = new ArrayList<>();
    //        BarChartDataSet barDataSet = new BarChartDataSet();
    //        BarChartDataSet barDataSet2 = new BarChartDataSet();
    //        HashMap<String, List<VentaTotalDto>> mapaVentas = new HashMap<>();
    //        List<VentaTotalDto> VentaTotalDtoAux = new ArrayList<>();
    //        List<String> labels = new ArrayList<>();
    //
    //        mapaVentas = new HashMap<>();
    //        for (int i = 0; i < listaTermianles.size(); i++) {
    //            for (int j = 0; j < listaVentaTotalDto.size(); j++) {
    //                String cod = listaVentaTotalDto.get(j).getNombreTerminal().substring(0, 1);
    //                if (listaVentaTotalDto.get(j).getNombreTerminal().substring(0, 2).equals(listaTermianles.get(i).getCodigo())) {
    //                    if (!mapaVentas.containsKey(listaVentaTotalDto.get(j).getNombreTerminal())) {
    //                        VentaTotalDtoAux = new ArrayList<>();
    //                    }
    //                    VentaTotalDtoAux.add(listaVentaTotalDto.get(j));
    //                    mapaVentas.put(listaVentaTotalDto.get(j).getNombreTerminal(), VentaTotalDtoAux);
    //                }
    //            }
    //        }
    //
    //        if (!mapaVentas.isEmpty()) {
    //            for (Map.Entry<String, List<VentaTotalDto>> entry : mapaVentas.entrySet()) {
    //                for (int i = 0; i < entry.getValue().size(); i++) {
    //                    //ventas
    //                    barDataSet2 = new BarChartDataSet();
    //                    barDataSet2.setLabel(entry.getValue().get(i).getNombreProducto());
    //                    barDataSet2.setBackgroundColor(coloresProducto(entry.getValue().get(i).getNombreProducto(), 2)[0]);
    //                    barDataSet2.setBorderColor(coloresProducto(entry.getValue().get(i).getNombreProducto(), 2)[1]);
    //                    barDataSet2.setStack("Ventas");
    //                    barDataSet2.setBorderWidth(1);
    //                    List<Number> values2 = new ArrayList<>();
    //                    values2.add(entry.getValue().get(i).getVolumenTotal());
    //                    barDataSet2.setData(values2);
    //                    barDataSetList.add(barDataSet2);
    //                    //despachos
    //                    barDataSet = new BarChartDataSet();
    //                    barDataSet.setLabel(entry.getValue().get(i).getNombreProducto());
    //                    barDataSet.setBackgroundColor(coloresProducto(entry.getValue().get(i).getNombreProducto(), 1)[0]);
    //                    barDataSet.setBorderColor(coloresProducto(entry.getValue().get(i).getNombreProducto(), 1)[1]);
    //                    barDataSet.setStack("Despachos");
    //                    barDataSet.setBorderWidth(1);
    //                    List<Number> values = new ArrayList<>();
    //                    values.add(entry.getValue().get(i).getVolumenTotalD());
    //                    barDataSet.setData(values);
    //                    barDataSetList.add(barDataSet);
    //                }
    //                for (int i = 0; i < barDataSetList.size(); i++) {
    //                    data.addChartDataSet(barDataSetList.get(i));
    //                }                
    //                labels.add(entry.getKey());
    //                data.setLabels(labels);
    //                
    //                //data = new ChartData();
    //            }
    //            barModel.setData(data);
    //            barDataSetList = new ArrayList<>();
    //        }
    //
    ////        for (int i = 0; i < listaVentaTotalDto.size(); i++) {
    ////            barDataSet2 = new BarChartDataSet();
    ////            barDataSet2.setLabel(listaVentaTotalDto.get(i).getNombreProducto());
    ////            barDataSet2.setBackgroundColor(coloresProducto(listaVentaTotalDto.get(i).getNombreProducto(), 2)[0]);
    ////            barDataSet2.setBorderColor(coloresProducto(listaVentaTotalDto.get(i).getNombreProducto(), 2)[1]);
    ////            barDataSet2.setStack("Stack 0");
    ////            barDataSet2.setBorderWidth(1);
    ////            List<Number> values2 = new ArrayList<>();
    ////            values2.add(listaVentaTotalDto.get(i).getVolumenTotal());
    ////            barDataSet2.setData(values2);
    ////            barDataSetList.add(barDataSet2);
    ////        }
    ////        for (int i = 0; i < listaDespachoTotalDto.size(); i++) {
    ////            barDataSet = new BarChartDataSet();
    ////            barDataSet.setLabel(listaDespachoTotalDto.get(i).getNombreProducto());
    ////            barDataSet.setBackgroundColor(coloresProducto(listaDespachoTotalDto.get(i).getNombreProducto(), 1)[0]);
    ////            barDataSet.setBorderColor(coloresProducto(listaDespachoTotalDto.get(i).getNombreProducto(), 1)[1]);
    ////            barDataSet.setStack("Stack 1");
    ////            barDataSet.setBorderWidth(1);
    ////            List<Number> values = new ArrayList<>();
    ////            values.add(listaDespachoTotalDto.get(i).getVolumenTotal());
    ////            barDataSet.setData(values);
    ////            barDataSetList.add(barDataSet);
    ////        }
    ////        for (int i = 0; i < barDataSetList.size(); i++) {
    ////            data.addChartDataSet(barDataSetList.get(i));
    ////        }
    ////
    ////        List<String> nombres = new ArrayList<>();
    ////        for (int i = 0; i < listaVentaTotalDto.size(); i++) {
    ////            nombres.add(listaVentaTotalDto.get(i).getNombreTerminal());
    ////        }
    ////        Set nuevaLista = new HashSet<>(nombres);
    ////        nombres.clear();
    ////        nombres.addAll(nuevaLista);
    ////
    ////        for (int i = 0; i < nombres.size(); i++) {
    ////            labels.add(nombres.get(i));
    ////        }
    ////        if (!listaVentaTotalDto.isEmpty()) {
    ////            labels.add(listaVentaTotalDto.get(0).getNombreTerminal());
    ////        }
    ////        data.setLabels(labels);
    ////        barModel.setData(data);
    //
    //        //Options
    //        BarChartOptions options = new BarChartOptions();
    //        CartesianScales cScales = new CartesianScales();
    //        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
    //        linearAxes.setOffset(true);
    //        //linearAxes.setBeginAtZero(true);
    //        CartesianLinearTicks ticks = new CartesianLinearTicks();
    //        linearAxes.setTicks(ticks);
    //        linearAxes.setStacked(true);
    //        cScales.addXAxesData(linearAxes);
    //        cScales.addYAxesData(linearAxes);
    //        options.setScales(cScales);
    //
    ////        Title title = new Title();
    ////        title.setDisplay(true);
    ////        title.setText("Bar Chart");
    ////        options.setTitle(title);
    //        Tooltip tooltip = new Tooltip();
    //        tooltip.setMode("index");
    //        tooltip.setIntersect(false);
    //        options.setTooltip(tooltip);
    //
    //        barModel.setOptions(options);
    //    }

    public void actualizarGrafico() {
        obtenerVentaTotal();
        //obtenerDespachoTotal();
        createBarModel();
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

    public String getSumatotal() {
        return sumatotal;
    }

    public void setSumatotal(String sumatotal) {
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

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

}
