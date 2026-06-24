
package ec.com.infinityone.ejemplo;


import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.VentaDespachoTotal;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.apollo.view.ChartDemoView;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.polar.PolarAreaChartDataSet;
import org.primefaces.model.charts.polar.PolarAreaChartModel;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
 
/**
 *
 * @author Mauricio
 */
@Named
@ViewScoped
public class DashboardBeanejemplo extends ChartDemoView implements Serializable {
    
    VentaDespachoTotal unaVentaDespachoTotal;
    ArrayList<VentaDespachoTotal> listaVentaDespachoTotal;
    
    private BarChartModel barra;
    public void graficarVentasDespachos(){
        /**
//         *         List<VentasProductoDTO> listaAux = movimientoServicio.ventaPorProducto(this.obtenerFechaInicioMes(fechaSeleccionada), this.obtenerFechaFinMes(fechaSeleccionada));
//
//        for (int i = 0; i < listaAux.size(); i++) {
//
//            labels.add(listaAux.get(i).getArticulo().toUpperCase().trim());
//            values.add(listaAux.get(i).getVentaTotal());
//
//            switch (listaAux.get(i).getArticulo().toUpperCase().trim()) {
//                case "EXTRA":
//                    bgColors.add("rgb(52,152,219)"); //color azul
//                    break;
//                case "DIESEL PREMIUM":
//                    bgColors.add("rgb(248,243,43)"); //color amarillo
//                    break;
//                case "SUPER":
//                    bgColors.add("rgb(235,225,201)"); //color blanco
//                    break;
//                default:
//                    int r = (int) (Math.random() * 254) + 1;
//                    int g = (int) (Math.random() * 254) + 1;
//                    int b = (int) (Math.random() * 254) + 1;
//                    bgColors.add("rgb(" + r + "," + g + "," + b + ")");
//                    break;
//            }
//
//        }
//
//        dataSet.setData(values);
//        dataSet.setBackgroundColor(bgColors);
//
//        data.addChartDataSet(dataSet);
//
//        data.setLabels(labels);
//
//        this.pieModel.setData(data);

         */
                 
        barra = new BarChartModel();
        System.out.println("FT:: ENTRA A graficarVentasDespachos");
        actualizarGrafico();
        for (int i = 0; i < 12; i++) {
            ChartSeries serie = new ChartSeries();
            serie.setLabel(String.valueOf(i));
            serie.set(String.valueOf(i), (i+100));
            barra.addSeries(serie);
        }
        barra.setTitle("Gráfico UNO");
        barra.setLegendPosition("ne");
        barra.setAnimate(true);
        
        Axis xAxis = barra.getAxis(AxisType.X);
        xAxis.setLabel("Meses");
        Axis yAxis = barra.getAxis(AxisType.Y);
        yAxis.setLabel("Venta total");
        yAxis.setMin(1000);
        yAxis.setMax(50000);
        
    }
    
    public void actualizarGrafico() {
//        if (comercializadora.getCodigo() != null) {
//            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            //codigoComer = params.get("form:comercializadora_input");
            ArrayList<Object> lista = new ArrayList<>();
            obtenerVentasDespachos();
//        }
    }

    public List<VentaDespachoTotal> obtenerVentasDespachos() {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.clienterubrotercero/comer?codigocomercializadora=" + codComer);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/ventatotal");
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaVentaDespachoTotal = new ArrayList<>();
            unaVentaDespachoTotal = new VentaDespachoTotal();

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

                JSONObject venDes = retorno.getJSONObject(indice);
                unaVentaDespachoTotal.setTerminal(venDes.getString("nombreTerminal"));
                unaVentaDespachoTotal.setContador(venDes.getInt("facturas"));
                unaVentaDespachoTotal.setCantidadTotal(venDes.getBigInteger("volumenTotal")); 
                listaVentaDespachoTotal.add(unaVentaDespachoTotal);
                unaVentaDespachoTotal = new VentaDespachoTotal();

            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaVentaDespachoTotal;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaVentaDespachoTotal;
    }
    
    /**
     * Variable para instanciar la clase MovimientoSurtidorServicio
      
    @Inject
    private PmqV1MovimientosurtidorServicio movimientoServicio;

    /**
     * Variable para manejar la fecha seleccionada por el usuario
     */
    private Date fechaSeleccionada;

    /**
     * Funcion para inicializar el bean
     */
    @PostConstruct
    @Override
    public void init() {
        fechaSeleccionada = new Date();
        graficarVentasDespachos();
    }

    /**
     * Funcion para actualizar el cambio de fecha
     *
     * @param event
     */
    public void onDateSelect(SelectEvent event) {
        fechaSeleccionada = (Date) event.getObject();
        graficarVentasDespachos();
    }

    /**
     * Funcion para graficar un grafico de pie del estado de los documentos del
     * mes ventaProductos()
     */

    /**
     * método para realizar un gráfico de barras de las ventas de los empleados
     */
//    public void ventasMes() {
//        this.lineModel = new LineChartModel();
//        ChartData data = new ChartData();
//
//        LineChartDataSet dataSet = new LineChartDataSet();
//        
//        List<Object> values = new ArrayList<>();
//        YearMonth yearMonthObject = YearMonth.of(fechaSeleccionada.getYear() + 1900, fechaSeleccionada.getMonth() + 1);
//        int diasMes = yearMonthObject.lengthOfMonth();
//        List<String> labels = new ArrayList<>();
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.MONTH, fechaSeleccionada.getMonth());
//        calendar.set(Calendar.YEAR, fechaSeleccionada.getYear() + 1900);
//        String nombreMesLabel = this.getMonthForInt(fechaSeleccionada.getMonth());
//        dataSet.setLabel("Ventas de "+nombreMesLabel);
//        for (int i = 0; i < diasMes; i++) {
//
//            calendar.set(Calendar.DATE, i + 1);
//
//            BigDecimal suma = movimientoServicio.ventasMesDia(this.fechaDB(calendar.getTime()));
//            values.add(suma.setScale(2));
//
//            labels.add(Integer.toString(i + 1));
//        }
//        dataSet.setData(values);
//        dataSet.setFill(false);
//        int r = (int) (Math.random() * 254) + 1;
//        int g = (int) (Math.random() * 254) + 1;
//        int b = (int) (Math.random() * 254) + 1;
//        dataSet.setBorderColor("rgb(" + r + "," + g + "," + b + ")");
//        data.addChartDataSet(dataSet);
//
//        data.setLabels(labels);
//
//        //Options
//        LineChartOptions options = new LineChartOptions();
//        Title title = new Title();
//        title.setDisplay(true);
//        title.setText("Ventas En El Mes");
//        options.setTitle(title);
//
//        this.lineModel.setOptions(options);
//        this.lineModel.setData(data);
//
//    }

    /**
     * método para realizar un gráfico de barras de los mejores clientes en el
     * mes
     */
//    public void mejoresClientes() {
//        this.polarAreaModel = new PolarAreaChartModel();
//        ChartData data = new ChartData();
//
//        PolarAreaChartDataSet dataSet = new PolarAreaChartDataSet();
//
//        List<MejoresClientesDTO> list = movimientoServicio.mejoresClientes(this.obtenerFechaInicioMes(fechaSeleccionada), this.obtenerFechaFinMes(fechaSeleccionada));
//
//        List<Number> values = new ArrayList<>();
//        List<String> bgColor = new ArrayList<>();
//        List<String> labels = new ArrayList<>();
//        //List<String> borderColor = new ArrayList<>();
//
//        for (int i = 0; i < list.size(); i++) {
//
//            labels.add(list.get(i).getNombreCliente());
//            int r = (int) (Math.random() * 254) + 1;
//            int g = (int) (Math.random() * 254) + 1;
//            int b = (int) (Math.random() * 254) + 1;
//
//            bgColor.add("rgb(" + r + "," + g + "," + b + ")");
//            //bgColor.add("rgba(" + r + "," + g + "," + b + "," + "0.2)");
//            //borderColor.add("rgb(" + r + "," + g + "," + b + ")");
//            values.add(list.get(i).getVentaTotal());
//        }
//
//        dataSet.setData(values);
//        dataSet.setBackgroundColor(bgColor);
//        //dataSet.setBorderColor(borderColor);
//        //dataSet.setBorderWidth(1);
//        data.addChartDataSet(dataSet);
//        data.setLabels(labels);
//        this.polarAreaModel.setData(data);
//    }

    /**
     * Getters y Setters
     *
     * @return
     */
    public Date getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public void setFechaSeleccionada(Date fechaSeleccionada) {
        this.fechaSeleccionada = fechaSeleccionada;
    }

//    @Override
//    public PolarAreaChartModel getPolarAreaModel() {
//        return polarAreaModel;
//    }
//
//    @Override
//    public void setPolarAreaModel(PolarAreaChartModel polarAreaModel) {
//        this.polarAreaModel = polarAreaModel;
//    }

    public BarChartModel getBarra() {
        return barra;
    }

    public void setBarra(BarChartModel barra) {
        this.barra = barra;
    }

}
