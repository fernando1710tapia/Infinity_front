/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.servicio.pedidosyfacturacion;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.Detallefactura;
import ec.com.infinityone.modelo.DetallefacturaPK;
import ec.com.infinityone.modelo.EnvioFactura;
import ec.com.infinityone.modelo.Factura;
import ec.com.infinityone.modelo.FacturaPK;
import ec.com.infinityone.modelo.Terminal;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author HP
 */
@LocalBean
@Stateless
public class FacturaServicio extends ReusableBean {

    /*
    Variable que almacena varias Facturas
     */
    private List<Factura> listaFacturas;
    /*
    Variable para guardar una listda de Factura y Detalle Factura
     */
    private List<EnvioFactura> listenvF;
    /*
    Variable para guardar una listda de Factura y Detalle Factura
     */
    private List<Factura> listFact;
    /*
    Variable para guardar una lista deDeatllesFactura
     */
    private List<Detallefactura> listDet;
    /*
    Variable que isntacia el modelo DeatlleFactura
     */
    private Detallefactura detFac;

    private DetallefacturaPK detFacPK;

    private Factura factura;

    private FacturaPK facturaPK;

    public List<Factura> obtenerFacturas(String codigoComer, String tipoFecha, String fecha, boolean petro, boolean activa, boolean pagada) {
        //urlInfinity = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.areamercadeo
        //https://www.supertech.ec:8443/infinityone1/resources/usuario/login?user=Ftapia&password=123456";
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/paraCobrar?codigocomercializadora=" + codigoComer + "&tipofecha=" + tipoFecha + "&oeenpetro=" + petro + "&activa=" + activa + "&pagada=" + pagada + "&fecha=" + fecha);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/paraCobrar?codigocomercializadora=" + codigoComer + "&tipofecha=" + tipoFecha + "&oeenpetro=" + petro + "&activa=" + activa + "&pagada=" + pagada + "&fecha=" + fecha);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaFacturas = new ArrayList<>();
            factura = new Factura();
            facturaPK = new FacturaPK();

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
                JSONObject fact = retorno.getJSONObject(indice);
                JSONObject factPK = fact.getJSONObject("facturaPK");

                facturaPK.setCodigoabastecedora(factPK.getString("codigoabastecedora"));
                facturaPK.setCodigocomercializadora(factPK.getString("codigocomercializadora"));
                facturaPK.setNumeronotapedido(factPK.getString("numeronotapedido"));
                facturaPK.setNumero(factPK.getString("numero"));
                factura.setFacturaPK(facturaPK);

                if (!fact.isNull("fechaacreditacionprorroga")) {
                    Long lDatePro = fact.getLong("fechaacreditacionprorroga");
                    Date datePro = new Date(lDatePro);
                    factura.setFechaacreditacionprorrogada(date.format(datePro));
                }
                Long lDateVen = fact.getLong("fechaventa");
                Date dateVen = new Date(lDateVen);
                factura.setFechaventa(date.format(dateVen));
                Long lDateVenc = fact.getLong("fechavencimiento");
                Date dateVenc = new Date(lDateVenc);
                factura.setFechavencimiento(date.format(dateVenc));
                Long lDateAcre = fact.getLong("fechaacreditacion");
                Date dateAcre = new Date(lDateAcre);
                factura.setFechaacreditacion(date.format(dateAcre));
                Long lDateDesp = fact.getLong("fechadespacho");
                Date dateDesp = new Date(lDateDesp);
                factura.setFechadespacho(date.format(dateDesp));
                factura.setActiva(fact.getBoolean("activa"));
                factura.setValortotal(fact.getBigDecimal("valortotal"));
                factura.setValorconrubro(fact.getBigDecimal("valorconrubro"));
                factura.setIvatotal(fact.getBigDecimal("ivatotal"));
                factura.setObservacion(fact.getString("observacion"));
                factura.setPagada(fact.getBoolean("pagada"));
                factura.setOeenpetro(fact.getBoolean("oeenpetro"));
                factura.setCodigocliente(fact.getString("codigocliente"));
                factura.setCodigoterminal(fact.getString("codigoterminal"));
                factura.setCodigobanco(fact.getString("codigobanco"));
                factura.setAdelantar(fact.getBoolean("adelantar"));
                factura.setUsuarioactual(fact.getString("usuarioactual"));
                factura.setNombrecomercializadora(fact.getString("nombrecomercializadora"));
                factura.setRuccomercializadora(fact.getString("ruccomercializadora"));
                factura.setDireccionmatrizcomercializadora(fact.getString("direccionmatrizcomercializadora"));
                factura.setNombrecliente(fact.getString("nombrecliente"));
                factura.setRuccliente(fact.getString("ruccliente"));
                factura.setValorsinimpuestos(fact.getBigDecimal("valorsinimpuestos"));
                if (!fact.isNull("correocliente")) {
                    factura.setCorreocliente(fact.getString("correocliente"));
                }
                factura.setDireccioncliente(fact.getString("direccioncliente"));
                factura.setTelefonocliente(fact.getString("telefonocliente"));
                if (!fact.isNull("numeroautorizacion")) {
                    factura.setNumeroautorizacion(fact.getString("numeroautorizacion"));
                }
                if (!fact.isNull("fechaautorizacion")) {
                    factura.setFechaautorizacion(fact.getString("fechaautorizacion"));
                }
                factura.setClienteformapago(fact.getString("clienteformapago"));
                if (!fact.isNull("clienteformapagonosri")) {
                    factura.setClienteformapagonosri(fact.getString("clienteformapagonosri"));
                }
                factura.setPlazocliente(fact.getInt("plazocliente"));
                factura.setClaveacceso(fact.getString("claveacceso"));
                if (!fact.isNull("campoadicional_campo1")) {
                    factura.setCampoadicionalCampo1(fact.getString("campoadicional_campo1"));
                }
                if (!fact.isNull("campoadicional_campo2")) {
                    factura.setCampoadicionalCampo2(fact.getString("campoadicional_campo2"));
                }
                if (!fact.isNull("campoadicional_campo3")) {
                    factura.setCampoadicionalCampo3(fact.getString("campoadicional_campo3"));
                }
                if (!fact.isNull("campoadicional_campo4")) {
                    factura.setCampoadicionalCampo4(fact.getString("campoadicional_campo4"));
                }
                if (!fact.isNull("campoadicional_campo5")) {
                    factura.setCampoadicionalCampo5(fact.getString("campoadicional_campo5"));
                }
                if (!fact.isNull("campoadicional_campo6")) {
                    factura.setCampoadicionalCampo6(fact.getString("campoadicional_campo6"));
                }
                factura.setEstado(fact.getString("estado"));
                Long error = fact.getLong("errordocumento");
                Short errorS = error.shortValue();
                factura.setErrordocumento(errorS);
                Long hospedado = fact.getLong("hospedado");
                Short hospedadoS = hospedado.shortValue();
                factura.setHospedado(hospedadoS);
                factura.setAmbientesri(fact.getString("ambientesri").charAt(0));
                if (!fact.isNull("tipoemision")) {
                    factura.setTipoemision(fact.getString("tipoemision").charAt(0));
                }
                factura.setCodigodocumento(fact.getString("codigodocumento"));
                factura.setEsagenteretencion(fact.getBoolean("esagenteretencion"));
                factura.setEscontribuyenteespacial(fact.getString("escontribuyenteespacial"));
                factura.setObligadocontabilidad(fact.getString("obligadocontabilidad"));
                factura.setTipocomprador(fact.getString("tipocomprador"));
                factura.setMoneda(fact.getString("moneda"));
                factura.setSeriesri(fact.getString("seriesri"));
                if (!fact.isNull("tipoplazocredito")) {
                    factura.setTipoplazocredito(fact.getString("tipoplazocredito"));
                }

                listaFacturas.add(factura);
                factura = new Factura();
                facturaPK = new FacturaPK();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaFacturas;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaFacturas;
    }

    public List<Factura> obtenerFacturasValidafrCobros(String codigoComer, String tipoFecha, String fecha, String cliForPago) {
        //urlInfinity = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.areamercadeo
        //https://www.supertech.ec:8443/infinityone1/resources/usuario/login?user=Ftapia&password=123456";
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/paraCobrar?codigocomercializadora=" + codigoComer + "&tipofecha=" + tipoFecha + "&oeenpetro=" + petro + "&activa=" + activa + "&pagada=" + pagada + "&fecha=" + fecha);
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura";
            URL url = new URL(direcc + "/validarCobro?codigocomercializadora=" + codigoComer + "&tipofecha=" + tipoFecha + "&clienteformapago=" + cliForPago + "&fecha=" + fecha);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaFacturas = new ArrayList<>();
            factura = new Factura();
            facturaPK = new FacturaPK();

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
                JSONObject fact = retorno.getJSONObject(indice);
                JSONObject factPK = fact.getJSONObject("facturaPK");

                facturaPK.setCodigoabastecedora(factPK.getString("codigoabastecedora"));
                facturaPK.setCodigocomercializadora(factPK.getString("codigocomercializadora"));
                facturaPK.setNumeronotapedido(factPK.getString("numeronotapedido"));
                facturaPK.setNumero(factPK.getString("numero"));
                factura.setFacturaPK(facturaPK);

                if (!fact.isNull("fechaacreditacionprorrogada")) {
                    Long lDatePro = fact.getLong("fechaacreditacionprorrogada");
                    Date datePro = new Date(lDatePro);
                    factura.setFechaacreditacionprorrogada(date.format(datePro));
                }
                Long lDateVen = fact.getLong("fechaventa");
                Date dateVen = new Date(lDateVen);
                factura.setFechaventa(date.format(dateVen));
                Long lDateVenc = fact.getLong("fechavencimiento");
                Date dateVenc = new Date(lDateVenc);
                factura.setFechavencimiento(date.format(dateVenc));
                Long lDateAcre = fact.getLong("fechaacreditacion");
                Date dateAcre = new Date(lDateAcre);
                factura.setFechaacreditacion(date.format(dateAcre));
                Long lDateDesp = fact.getLong("fechadespacho");
                Date dateDesp = new Date(lDateDesp);
                factura.setFechadespacho(date.format(dateDesp));
                factura.setActiva(fact.getBoolean("activa"));
                factura.setValortotal(fact.getBigDecimal("valortotal"));
                factura.setValorconrubro(fact.getBigDecimal("valorconrubro"));
                factura.setIvatotal(fact.getBigDecimal("ivatotal"));
                factura.setObservacion(fact.getString("observacion"));
                factura.setPagada(fact.getBoolean("pagada"));
                factura.setOeenpetro(fact.getBoolean("oeenpetro"));
                factura.setCodigocliente(fact.getString("codigocliente"));
                factura.setCodigoterminal(fact.getString("codigoterminal"));
                factura.setCodigobanco(fact.getString("codigobanco"));
                factura.setAdelantar(fact.getBoolean("adelantar"));
                factura.setUsuarioactual(fact.getString("usuarioactual"));
                factura.setNombrecomercializadora(fact.getString("nombrecomercializadora"));
                factura.setRuccomercializadora(fact.getString("ruccomercializadora"));
                factura.setDireccionmatrizcomercializadora(fact.getString("direccionmatrizcomercializadora"));
                factura.setNombrecliente(fact.getString("nombrecliente"));
                factura.setRuccliente(fact.getString("ruccliente"));
                factura.setValorsinimpuestos(fact.getBigDecimal("valorsinimpuestos"));
                if (!fact.isNull("correocliente")) {
                    factura.setCorreocliente(fact.getString("correocliente"));
                }
                factura.setDireccioncliente(fact.getString("direccioncliente"));
                factura.setTelefonocliente(fact.getString("telefonocliente"));
                if (!fact.isNull("numeroautorizacion")) {
                    factura.setNumeroautorizacion(fact.getString("numeroautorizacion"));
                }
                if (!fact.isNull("fechaautorizacion")) {
                    factura.setFechaautorizacion(fact.getString("fechaautorizacion"));
                }
                factura.setClienteformapago(fact.getString("clienteformapago"));
                if (!fact.isNull("clienteformapagonosri")) {
                    factura.setClienteformapagonosri(fact.getString("clienteformapagonosri"));
                }
                factura.setPlazocliente(fact.getInt("plazocliente"));
                factura.setClaveacceso(fact.getString("claveacceso"));
                if (!fact.isNull("campoadicional_campo1")) {
                    factura.setCampoadicionalCampo1(fact.getString("campoadicional_campo1"));
                }
                if (!fact.isNull("campoadicional_campo2")) {
                    factura.setCampoadicionalCampo2(fact.getString("campoadicional_campo2"));
                }
                if (!fact.isNull("campoadicional_campo3")) {
                    factura.setCampoadicionalCampo3(fact.getString("campoadicional_campo3"));
                }
                if (!fact.isNull("campoadicional_campo4")) {
                    factura.setCampoadicionalCampo4(fact.getString("campoadicional_campo4"));
                }
                if (!fact.isNull("campoadicional_campo5")) {
                    factura.setCampoadicionalCampo5(fact.getString("campoadicional_campo5"));
                }
                if (!fact.isNull("campoadicional_campo6")) {
                    factura.setCampoadicionalCampo6(fact.getString("campoadicional_campo6"));
                }
                factura.setEstado(fact.getString("estado"));
                Long error = fact.getLong("errordocumento");
                Short errorS = error.shortValue();
                factura.setErrordocumento(errorS);
                Long hospedado = fact.getLong("hospedado");
                Short hospedadoS = hospedado.shortValue();
                factura.setHospedado(hospedadoS);
                factura.setAmbientesri(fact.getString("ambientesri").charAt(0));
                if (!fact.isNull("tipoemision")) {
                    factura.setTipoemision(fact.getString("tipoemision").charAt(0));
                }
                factura.setCodigodocumento(fact.getString("codigodocumento"));
                factura.setEsagenteretencion(fact.getBoolean("esagenteretencion"));
                factura.setEscontribuyenteespacial(fact.getString("escontribuyenteespacial"));
                factura.setObligadocontabilidad(fact.getString("obligadocontabilidad"));
                factura.setTipocomprador(fact.getString("tipocomprador"));
                factura.setMoneda(fact.getString("moneda"));
                factura.setSeriesri(fact.getString("seriesri"));
                if (!fact.isNull("tipoplazocredito")) {
                    factura.setTipoplazocredito(fact.getString("tipoplazocredito"));
                }
                factura.setEnviadaxcobrar(fact.getBoolean("enviadaxcobrar"));

                listaFacturas.add(factura);
                factura = new Factura();
                facturaPK = new FacturaPK();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaFacturas;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaFacturas;
    }

    public List<Factura> obtenerFacturasPagos(String codigoComer, String tipoFecha, String fecha, boolean petro, boolean activa, boolean pagada, String cliForPago) {
        //urlInfinity = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.areamercadeo
        //https://www.supertech.ec:8443/infinityone1/resources/usuario/login?user=Ftapia&password=123456";
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/paraCobrar?codigocomercializadora=" + codigoComer + "&tipofecha=" + tipoFecha + "&oeenpetro=" + petro + "&activa=" + activa + "&pagada=" + pagada + "&fecha=" + fecha);
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura";
            URL url = new URL(direcc + "/paraCobrarxformapago?codigocomercializadora=" + codigoComer + "&tipofecha=" + tipoFecha + "&oeenpetro=" + petro + "&activa=" + activa + "&pagada=" + pagada + "&clienteformapago=" + cliForPago + "&fecha=" + fecha + "&codigoterminal=-1&codigocliente=-1");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaFacturas = new ArrayList<>();
            factura = new Factura();
            facturaPK = new FacturaPK();

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
                JSONObject fact = retorno.getJSONObject(indice);
                JSONObject factPK = fact.getJSONObject("facturaPK");

                facturaPK.setCodigoabastecedora(factPK.getString("codigoabastecedora"));
                facturaPK.setCodigocomercializadora(factPK.getString("codigocomercializadora"));
                facturaPK.setNumeronotapedido(factPK.getString("numeronotapedido"));
                facturaPK.setNumero(factPK.getString("numero"));
                factura.setFacturaPK(facturaPK);

                if (!fact.isNull("fechaacreditacionprorrogada")) {
                    Long lDatePro = fact.getLong("fechaacreditacionprorrogada");
                    Date datePro = new Date(lDatePro);
                    factura.setFechaacreditacionprorrogada(date.format(datePro));
                }
                Long lDateVen = fact.getLong("fechaventa");
                Date dateVen = new Date(lDateVen);
                factura.setFechaventa(date.format(dateVen));
                Long lDateVenc = fact.getLong("fechavencimiento");
                Date dateVenc = new Date(lDateVenc);
                factura.setFechavencimiento(date.format(dateVenc));
                Long lDateAcre = fact.getLong("fechaacreditacion");
                Date dateAcre = new Date(lDateAcre);
                factura.setFechaacreditacion(date.format(dateAcre));
                Long lDateDesp = fact.getLong("fechadespacho");
                Date dateDesp = new Date(lDateDesp);
                factura.setFechadespacho(date.format(dateDesp));
                factura.setActiva(fact.getBoolean("activa"));
                factura.setValortotal(fact.getBigDecimal("valortotal"));
                factura.setValorconrubro(fact.getBigDecimal("valorconrubro"));
                factura.setIvatotal(fact.getBigDecimal("ivatotal"));
                factura.setObservacion(fact.getString("observacion"));
                factura.setPagada(fact.getBoolean("pagada"));
                factura.setOeenpetro(fact.getBoolean("oeenpetro"));
                factura.setCodigocliente(fact.getString("codigocliente"));
                factura.setCodigoterminal(fact.getString("codigoterminal"));
                factura.setCodigobanco(fact.getString("codigobanco"));
                factura.setAdelantar(fact.getBoolean("adelantar"));
                factura.setUsuarioactual(fact.getString("usuarioactual"));
                factura.setNombrecomercializadora(fact.getString("nombrecomercializadora"));
                factura.setRuccomercializadora(fact.getString("ruccomercializadora"));
                factura.setDireccionmatrizcomercializadora(fact.getString("direccionmatrizcomercializadora"));
                factura.setNombrecliente(fact.getString("nombrecliente"));
                factura.setRuccliente(fact.getString("ruccliente"));
                factura.setValorsinimpuestos(fact.getBigDecimal("valorsinimpuestos"));
                if (!fact.isNull("correocliente")) {
                    factura.setCorreocliente(fact.getString("correocliente"));
                }
                factura.setDireccioncliente(fact.getString("direccioncliente"));
                factura.setTelefonocliente(fact.getString("telefonocliente"));
                if (!fact.isNull("numeroautorizacion")) {
                    factura.setNumeroautorizacion(fact.getString("numeroautorizacion"));
                }
                if (!fact.isNull("fechaautorizacion")) {
                    factura.setFechaautorizacion(fact.getString("fechaautorizacion"));
                }
                factura.setClienteformapago(fact.getString("clienteformapago"));
                if (!fact.isNull("clienteformapagonosri")) {
                    factura.setClienteformapagonosri(fact.getString("clienteformapagonosri"));
                }
                factura.setPlazocliente(fact.getInt("plazocliente"));
                factura.setClaveacceso(fact.getString("claveacceso"));
                if (!fact.isNull("campoadicional_campo1")) {
                    factura.setCampoadicionalCampo1(fact.getString("campoadicional_campo1"));
                }
                if (!fact.isNull("campoadicional_campo2")) {
                    factura.setCampoadicionalCampo2(fact.getString("campoadicional_campo2"));
                }
                if (!fact.isNull("campoadicional_campo3")) {
                    factura.setCampoadicionalCampo3(fact.getString("campoadicional_campo3"));
                }
                if (!fact.isNull("campoadicional_campo4")) {
                    factura.setCampoadicionalCampo4(fact.getString("campoadicional_campo4"));
                }
                if (!fact.isNull("campoadicional_campo5")) {
                    factura.setCampoadicionalCampo5(fact.getString("campoadicional_campo5"));
                }
                if (!fact.isNull("campoadicional_campo6")) {
                    factura.setCampoadicionalCampo6(fact.getString("campoadicional_campo6"));
                }
                factura.setEstado(fact.getString("estado"));
                Long error = fact.getLong("errordocumento");
                Short errorS = error.shortValue();
                factura.setErrordocumento(errorS);
                Long hospedado = fact.getLong("hospedado");
                Short hospedadoS = hospedado.shortValue();
                factura.setHospedado(hospedadoS);
                factura.setAmbientesri(fact.getString("ambientesri").charAt(0));
                if (!fact.isNull("tipoemision")) {
                    factura.setTipoemision(fact.getString("tipoemision").charAt(0));
                }
                factura.setCodigodocumento(fact.getString("codigodocumento"));
                factura.setEsagenteretencion(fact.getBoolean("esagenteretencion"));
                factura.setEscontribuyenteespacial(fact.getString("escontribuyenteespacial"));
                factura.setObligadocontabilidad(fact.getString("obligadocontabilidad"));
                factura.setTipocomprador(fact.getString("tipocomprador"));
                factura.setMoneda(fact.getString("moneda"));
                factura.setSeriesri(fact.getString("seriesri"));
                if (!fact.isNull("tipoplazocredito")) {
                    factura.setTipoplazocredito(fact.getString("tipoplazocredito"));
                }

                listaFacturas.add(factura);
                factura = new Factura();
                facturaPK = new FacturaPK();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaFacturas;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaFacturas;
    }

    public List<Factura> buscarFacturas(String codigoAbas, String codigoComer, String numero) {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/porAbascomernum?codigoabastecedora=" + codigoAbas + "&codigocomercializadora=" + codigoComer + "&numero=" + numero );
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/porAbascomernum?codigoabastecedora=" + codigoAbas + "&codigocomercializadora=" + codigoComer + "&numero=" + numero);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaFacturas = new ArrayList<>();
            factura = new Factura();
            facturaPK = new FacturaPK();

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
 
                JSONObject fa = retorno.getJSONObject(indice);
                JSONObject faPK = fa.getJSONObject("facturaPK");

                facturaPK.setCodigoabastecedora(faPK.getString("codigoabastecedora"));
                facturaPK.setCodigocomercializadora(faPK.getString("codigocomercializadora"));
                facturaPK.setNumeronotapedido(faPK.getString("numeronotapedido"));
                facturaPK.setNumero(faPK.getString("numero"));
                factura.setFacturaPK(facturaPK);

                if (!fa.isNull("fechaacreditacionprorrogada")) {
                    Long lDatePro = fa.getLong("fechaacreditacionprorrogada");
                    Date datePro = new Date(lDatePro);
                    factura.setFechaacreditacionprorrogada(date.format(datePro));
                }
                Long lDateVen = fa.getLong("fechaventa");
                Date dateVen = new Date(lDateVen);
                factura.setFechaventa(date.format(dateVen));
                Long lDateVenc = fa.getLong("fechavencimiento");
                Date dateVenc = new Date(lDateVenc);
                factura.setFechavencimiento(date.format(dateVenc));
                Long lDateAcre = fa.getLong("fechaacreditacion");
                Date dateAcre = new Date(lDateAcre);
                factura.setFechaacreditacion(date.format(dateAcre));
                Long lDateDesp = fa.getLong("fechadespacho");
                Date dateDesp = new Date(lDateDesp);
                factura.setFechadespacho(date.format(dateDesp));
                factura.setActiva(fa.getBoolean("activa"));
                factura.setValortotal(fa.getBigDecimal("valortotal"));
                factura.setIvatotal(fa.getBigDecimal("ivatotal"));
                factura.setObservacion(fa.getString("observacion"));
                factura.setPagada(fa.getBoolean("pagada"));
                factura.setOeenpetro(fa.getBoolean("oeenpetro"));
                factura.setCodigocliente(fa.getString("codigocliente"));
                factura.setCodigoterminal(fa.getString("codigoterminal"));
                factura.setCodigobanco(fa.getString("codigobanco"));
                factura.setAdelantar(fa.getBoolean("adelantar"));
                factura.setUsuarioactual(fa.getString("usuarioactual"));
                factura.setNombrecomercializadora(fa.getString("nombrecomercializadora"));
                factura.setRuccomercializadora(fa.getString("ruccomercializadora"));
                factura.setDireccionmatrizcomercializadora(fa.getString("direccionmatrizcomercializadora"));
                factura.setNombrecliente(fa.getString("nombrecliente"));
                factura.setRuccliente(fa.getString("ruccliente"));
                factura.setValorsinimpuestos(fa.getBigDecimal("valorsinimpuestos"));
                if (!fa.isNull("correocliente")) {
                    factura.setCorreocliente(fa.getString("correocliente"));
                }
                factura.setDireccioncliente(fa.getString("direccioncliente"));
                factura.setTelefonocliente(fa.getString("telefonocliente"));
                if (!fa.isNull("numeroautorizacion")) {
                    factura.setNumeroautorizacion(fa.getString("numeroautorizacion"));
                }
                if (!fa.isNull("fechaautorizacion")) {
                    factura.setFechaautorizacion(fa.getString("fechaautorizacion"));
                }
                factura.setClienteformapago(fa.getString("clienteformapago"));
                if (!fa.isNull("clienteformapagonosri")) {
                    factura.setClienteformapagonosri(fa.getString("clienteformapagonosri"));
                }
                factura.setPlazocliente(fa.getInt("plazocliente"));
                factura.setClaveacceso(fa.getString("claveacceso"));
                if (!fa.isNull("campoadicional_campo1")) {
                    factura.setCampoadicionalCampo1(fa.getString("campoadicional_campo1"));
                }
                if (!fa.isNull("campoadicional_campo2")) {
                    factura.setCampoadicionalCampo2(fa.getString("campoadicional_campo2"));
                }
                if (!fa.isNull("campoadicional_campo3")) {
                    factura.setCampoadicionalCampo3(fa.getString("campoadicional_campo3"));
                }
                if (!fa.isNull("campoadicional_campo4")) {
                    factura.setCampoadicionalCampo4(fa.getString("campoadicional_campo4"));
                }
                if (!fa.isNull("campoadicional_campo5")) {
                    factura.setCampoadicionalCampo5(fa.getString("campoadicional_campo5"));
                }
                if (!fa.isNull("campoadicional_campo6")) {
                    factura.setCampoadicionalCampo6(fa.getString("campoadicional_campo6"));
                }
                factura.setEstado(fa.getString("estado"));
                Long error = fa.getLong("errordocumento");
                Short errorS = error.shortValue();
                factura.setErrordocumento(errorS);
                Long hospedado = fa.getLong("hospedado");
                Short hospedadoS = hospedado.shortValue();
                factura.setHospedado(hospedadoS);
                factura.setAmbientesri(fa.getString("ambientesri").charAt(0));
                if (!fa.isNull("tipoemision")) {
                    factura.setTipoemision(fa.getString("tipoemision").charAt(0));
                }
                factura.setCodigodocumento(fa.getString("codigodocumento"));
                factura.setEsagenteretencion(fa.getBoolean("esagenteretencion"));
                factura.setEscontribuyenteespacial(fa.getString("escontribuyenteespacial"));
                factura.setObligadocontabilidad(fa.getString("obligadocontabilidad"));
                factura.setTipocomprador(fa.getString("tipocomprador"));
                factura.setMoneda(fa.getString("moneda"));
                factura.setSeriesri(fa.getString("seriesri"));
                if (!fa.isNull("tipoplazocredito")) {
                    factura.setTipoplazocredito(fa.getString("tipoplazocredito"));
                }
                factura.setValorconrubro(fa.getBigDecimal("valorconrubro"));
                factura.setOeanuladaenpetro(fa.getBoolean("oeanuladaenpetro"));
                factura.setDespachada(fa.getBoolean("despachada"));
                //factura.setRefacturada(fact.getBoolean("refacturada"));
                //factura.setReliquidada(fact.getBigDecimal("reliquidada"));                            

                if (fa.getBoolean("activa") == true) {
                    factura.setActiva(true);
                } else {
                    factura.setActiva(false);
                }
                if (fa.getBoolean("oeenpetro") == true) {
                    factura.setOeenpetro(true);
                } else {
                    factura.setOeenpetro(false);
                }                
                factura.setFacturaPK(facturaPK);
                listaFacturas.add(factura);
                factura = new Factura();
                facturaPK = new FacturaPK();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaFacturas;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaFacturas;
    }

    public List<Factura> buscarFacturasConciliarPago(String codigoComer, String numero, String codCliente) {
        //urlInfinity = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.areamercadeo
        //https://www.supertech.ec:8443/infinityone1/resources/usuario/login?user=Ftapia&password=123456";
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/porAbascomernum?codigoabastecedora=" + codigoAbas + "&codigocomercializadora=" + codigoComer + "&numero=" + numero );
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/conciliarpago?codigocomercializadora=" + codigoComer + "&numero=" + numero + "&codigocliente=" + codCliente);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaFacturas = new ArrayList<>();
            factura = new Factura();
            facturaPK = new FacturaPK();

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
                JSONObject fact = retorno.getJSONObject(indice);
                JSONObject factPK = fact.getJSONObject("facturaPK");

                facturaPK.setCodigoabastecedora(factPK.getString("codigoabastecedora"));
                facturaPK.setCodigocomercializadora(factPK.getString("codigocomercializadora"));
                facturaPK.setNumeronotapedido(factPK.getString("numeronotapedido"));
                facturaPK.setNumero(factPK.getString("numero"));
                factura.setFacturaPK(facturaPK);

                if (!fact.isNull("fechaacreditacionprorrogada")) {
                    Long lDatePro = fact.getLong("fechaacreditacionprorrogada");
                    Date datePro = new Date(lDatePro);
                    factura.setFechaacreditacionprorrogada(date.format(datePro));
                }
                Long lDateVen = fact.getLong("fechaventa");
                Date dateVen = new Date(lDateVen);
                factura.setFechaventa(date.format(dateVen));
                Long lDateVenc = fact.getLong("fechavencimiento");
                Date dateVenc = new Date(lDateVenc);
                factura.setFechavencimiento(date.format(dateVenc));
                Long lDateAcre = fact.getLong("fechaacreditacion");
                Date dateAcre = new Date(lDateAcre);
                factura.setFechaacreditacion(date.format(dateAcre));
                Long lDateDesp = fact.getLong("fechadespacho");
                Date dateDesp = new Date(lDateDesp);
                factura.setFechadespacho(date.format(dateDesp));
                factura.setActiva(fact.getBoolean("activa"));
                factura.setValortotal(fact.getBigDecimal("valortotal"));
                factura.setIvatotal(fact.getBigDecimal("ivatotal"));
                factura.setObservacion(fact.getString("observacion"));
                factura.setPagada(fact.getBoolean("pagada"));
                factura.setOeenpetro(fact.getBoolean("oeenpetro"));
                factura.setCodigocliente(fact.getString("codigocliente"));
                factura.setCodigoterminal(fact.getString("codigoterminal"));
                factura.setCodigobanco(fact.getString("codigobanco"));
                factura.setAdelantar(fact.getBoolean("adelantar"));
                factura.setUsuarioactual(fact.getString("usuarioactual"));
                factura.setNombrecomercializadora(fact.getString("nombrecomercializadora"));
                factura.setRuccomercializadora(fact.getString("ruccomercializadora"));
                factura.setDireccionmatrizcomercializadora(fact.getString("direccionmatrizcomercializadora"));
                factura.setNombrecliente(fact.getString("nombrecliente"));
                factura.setRuccliente(fact.getString("ruccliente"));
                factura.setValorsinimpuestos(fact.getBigDecimal("valorsinimpuestos"));
                if (!fact.isNull("correocliente")) {
                    factura.setCorreocliente(fact.getString("correocliente"));
                }
                factura.setDireccioncliente(fact.getString("direccioncliente"));
                factura.setTelefonocliente(fact.getString("telefonocliente"));
                if (!fact.isNull("numeroautorizacion")) {
                    factura.setNumeroautorizacion(fact.getString("numeroautorizacion"));
                }
                if (!fact.isNull("fechaautorizacion")) {
                    factura.setFechaautorizacion(fact.getString("fechaautorizacion"));
                }
                factura.setClienteformapago(fact.getString("clienteformapago"));
                if (!fact.isNull("clienteformapagonosri")) {
                    factura.setClienteformapagonosri(fact.getString("clienteformapagonosri"));
                }
                factura.setPlazocliente(fact.getInt("plazocliente"));
                factura.setClaveacceso(fact.getString("claveacceso"));
                if (!fact.isNull("campoadicional_campo1")) {
                    factura.setCampoadicionalCampo1(fact.getString("campoadicional_campo1"));
                }
                if (!fact.isNull("campoadicional_campo2")) {
                    factura.setCampoadicionalCampo2(fact.getString("campoadicional_campo2"));
                }
                if (!fact.isNull("campoadicional_campo3")) {
                    factura.setCampoadicionalCampo3(fact.getString("campoadicional_campo3"));
                }
                if (!fact.isNull("campoadicional_campo4")) {
                    factura.setCampoadicionalCampo4(fact.getString("campoadicional_campo4"));
                }
                if (!fact.isNull("campoadicional_campo5")) {
                    factura.setCampoadicionalCampo5(fact.getString("campoadicional_campo5"));
                }
                if (!fact.isNull("campoadicional_campo6")) {
                    factura.setCampoadicionalCampo6(fact.getString("campoadicional_campo6"));
                }
                factura.setEstado(fact.getString("estado"));
                Long error = fact.getLong("errordocumento");
                Short errorS = error.shortValue();
                factura.setErrordocumento(errorS);
                Long hospedado = fact.getLong("hospedado");
                Short hospedadoS = hospedado.shortValue();
                factura.setHospedado(hospedadoS);
                factura.setAmbientesri(fact.getString("ambientesri").charAt(0));
                if (!fact.isNull("tipoemision")) {
                    factura.setTipoemision(fact.getString("tipoemision").charAt(0));
                }
                factura.setCodigodocumento(fact.getString("codigodocumento"));
                factura.setEsagenteretencion(fact.getBoolean("esagenteretencion"));
                factura.setEscontribuyenteespacial(fact.getString("escontribuyenteespacial"));
                factura.setObligadocontabilidad(fact.getString("obligadocontabilidad"));
                factura.setTipocomprador(fact.getString("tipocomprador"));
                factura.setMoneda(fact.getString("moneda"));
                factura.setSeriesri(fact.getString("seriesri"));
                if (!fact.isNull("tipoplazocredito")) {
                    factura.setTipoplazocredito(fact.getString("tipoplazocredito"));
                }
                factura.setValorconrubro(fact.getBigDecimal("valorconrubro"));
                factura.setOeanuladaenpetro(fact.getBoolean("oeanuladaenpetro"));
                //factura.setRefacturada(fact.getBoolean("refacturada"));
                //factura.setReliquidada(fact.getBigDecimal("reliquidada"));
                listaFacturas.add(factura);
                factura = new Factura();
                facturaPK = new FacturaPK();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaFacturas;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaFacturas;
    }

    public List<Factura> buscarFacturasAbasComerNum(String codAbas, String codigoComer, String numero) {
        //urlInfinity = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.areamercadeo
        //https://www.supertech.ec:8443/infinityone1/resources/usuario/login?user=Ftapia&password=123456";
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura/porAbascomernum?codigoabastecedora=" + codigoAbas + "&codigocomercializadora=" + codigoComer + "&numero=" + numero );
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/porAbascomernum?codigoabastecedora=" + codAbas + "&codigocomercializadora=" + codigoComer + "&numero=" + numero);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaFacturas = new ArrayList<>();
            factura = new Factura();
            facturaPK = new FacturaPK();

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
                JSONObject fact = retorno.getJSONObject(indice);
                JSONObject factPK = fact.getJSONObject("facturaPK");

                facturaPK.setCodigoabastecedora(factPK.getString("codigoabastecedora"));
                facturaPK.setCodigocomercializadora(factPK.getString("codigocomercializadora"));
                facturaPK.setNumeronotapedido(factPK.getString("numeronotapedido"));
                facturaPK.setNumero(factPK.getString("numero"));
                factura.setFacturaPK(facturaPK);

                if (!fact.isNull("fechaacreditacionprorrogada")) {
                    Long lDatePro = fact.getLong("fechaacreditacionprorrogada");
                    Date datePro = new Date(lDatePro);
                    factura.setFechaacreditacionprorrogada(date.format(datePro));
                }
                Long lDateVen = fact.getLong("fechaventa");
                Date dateVen = new Date(lDateVen);
                factura.setFechaventa(date.format(dateVen));
                Long lDateVenc = fact.getLong("fechavencimiento");
                Date dateVenc = new Date(lDateVenc);
                factura.setFechavencimiento(date.format(dateVenc));
                Long lDateAcre = fact.getLong("fechaacreditacion");
                Date dateAcre = new Date(lDateAcre);
                factura.setFechaacreditacion(date.format(dateAcre));
                Long lDateDesp = fact.getLong("fechadespacho");
                Date dateDesp = new Date(lDateDesp);
                factura.setFechadespacho(date.format(dateDesp));
                factura.setActiva(fact.getBoolean("activa"));
                factura.setValortotal(fact.getBigDecimal("valortotal"));
                factura.setIvatotal(fact.getBigDecimal("ivatotal"));
                factura.setObservacion(fact.getString("observacion"));
                factura.setPagada(fact.getBoolean("pagada"));
                factura.setOeenpetro(fact.getBoolean("oeenpetro"));
                factura.setCodigocliente(fact.getString("codigocliente"));
                factura.setCodigoterminal(fact.getString("codigoterminal"));
                factura.setCodigobanco(fact.getString("codigobanco"));
                factura.setAdelantar(fact.getBoolean("adelantar"));
                factura.setUsuarioactual(fact.getString("usuarioactual"));
                factura.setNombrecomercializadora(fact.getString("nombrecomercializadora"));
                factura.setRuccomercializadora(fact.getString("ruccomercializadora"));
                factura.setDireccionmatrizcomercializadora(fact.getString("direccionmatrizcomercializadora"));
                factura.setNombrecliente(fact.getString("nombrecliente"));
                factura.setRuccliente(fact.getString("ruccliente"));
                factura.setValorsinimpuestos(fact.getBigDecimal("valorsinimpuestos"));
                if (!fact.isNull("correocliente")) {
                    factura.setCorreocliente(fact.getString("correocliente"));
                }
                factura.setDireccioncliente(fact.getString("direccioncliente"));
                factura.setTelefonocliente(fact.getString("telefonocliente"));
                if (!fact.isNull("numeroautorizacion")) {
                    factura.setNumeroautorizacion(fact.getString("numeroautorizacion"));
                }
                if (!fact.isNull("fechaautorizacion")) {
                    factura.setFechaautorizacion(fact.getString("fechaautorizacion"));
                }
                factura.setClienteformapago(fact.getString("clienteformapago"));
                if (!fact.isNull("clienteformapagonosri")) {
                    factura.setClienteformapagonosri(fact.getString("clienteformapagonosri"));
                }
                factura.setPlazocliente(fact.getInt("plazocliente"));
                factura.setClaveacceso(fact.getString("claveacceso"));
                if (!fact.isNull("campoadicional_campo1")) {
                    factura.setCampoadicionalCampo1(fact.getString("campoadicional_campo1"));
                }
                if (!fact.isNull("campoadicional_campo2")) {
                    factura.setCampoadicionalCampo2(fact.getString("campoadicional_campo2"));
                }
                if (!fact.isNull("campoadicional_campo3")) {
                    factura.setCampoadicionalCampo3(fact.getString("campoadicional_campo3"));
                }
                if (!fact.isNull("campoadicional_campo4")) {
                    factura.setCampoadicionalCampo4(fact.getString("campoadicional_campo4"));
                }
                if (!fact.isNull("campoadicional_campo5")) {
                    factura.setCampoadicionalCampo5(fact.getString("campoadicional_campo5"));
                }
                if (!fact.isNull("campoadicional_campo6")) {
                    factura.setCampoadicionalCampo6(fact.getString("campoadicional_campo6"));
                }
                factura.setEstado(fact.getString("estado"));
                Long error = fact.getLong("errordocumento");
                Short errorS = error.shortValue();
                factura.setErrordocumento(errorS);
                Long hospedado = fact.getLong("hospedado");
                Short hospedadoS = hospedado.shortValue();
                factura.setHospedado(hospedadoS);
                factura.setAmbientesri(fact.getString("ambientesri").charAt(0));
                if (!fact.isNull("tipoemision")) {
                    factura.setTipoemision(fact.getString("tipoemision").charAt(0));
                }
                factura.setCodigodocumento(fact.getString("codigodocumento"));
                factura.setEsagenteretencion(fact.getBoolean("esagenteretencion"));
                factura.setEscontribuyenteespacial(fact.getString("escontribuyenteespacial"));
                factura.setObligadocontabilidad(fact.getString("obligadocontabilidad"));
                factura.setTipocomprador(fact.getString("tipocomprador"));
                factura.setMoneda(fact.getString("moneda"));
                factura.setSeriesri(fact.getString("seriesri"));
                if (!fact.isNull("tipoplazocredito")) {
                    factura.setTipoplazocredito(fact.getString("tipoplazocredito"));
                }
                factura.setValorconrubro(fact.getBigDecimal("valorconrubro"));
                factura.setOeanuladaenpetro(fact.getBoolean("oeanuladaenpetro"));
                //factura.setRefacturada(fact.getBoolean("refacturada"));
                //factura.setReliquidada(fact.getBigDecimal("reliquidada"));
                listaFacturas.add(factura);
                factura = new Factura();
                facturaPK = new FacturaPK();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaFacturas;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaFacturas;
    }

    public List<EnvioFactura> obtenerFacturasObjEnv(Date fechaI, Date fechaf, String codCliente, Cliente cliente, String codTerminal, Terminal terminal, String codAbas, String codComer, String tipoFecha) throws ParseException {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            String fechaS = date.format(fechaI);
            String fechaF = date.format(fechaf);

            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura";
            if (codCliente.isEmpty() || cliente == null) {
                codCliente = "-1";
            }
            if (codTerminal.isEmpty() || terminal == null) {
                codTerminal = "-1";
            }
            url = new URL(direcc + "/paraFactura?codigoabastecedora=" + codAbas + "&codigocomercializadora=" + codComer + "&codigoterminal=" + codTerminal + "&tipofecha=" + tipoFecha + "&fechaI=" + fechaS + "&fechaF=" + fechaF + "&codigocliente=" + codCliente);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listenvF = new ArrayList<>();
            listFact = new ArrayList();
            listDet = new ArrayList<>();
            factura = new Factura();
            facturaPK = new FacturaPK();
            detFac = new Detallefactura();
            EnvioFactura envFac = new EnvioFactura();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            if (retorno.isEmpty()) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCONTRARON REGISTROS");
            } else {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject fa = retorno.getJSONObject(indice);
                        JSONObject faPK = fa.getJSONObject("facturaPK");

                        facturaPK.setCodigoabastecedora(faPK.getString("codigoabastecedora"));
                        facturaPK.setCodigocomercializadora(faPK.getString("codigocomercializadora"));
                        facturaPK.setNumeronotapedido(faPK.getString("numeronotapedido"));
                        facturaPK.setNumero(faPK.getString("numero"));
                        factura.setFacturaPK(facturaPK);

                        if (!fa.isNull("fechaacreditacionprorrogada")) {
                            Long lDatePro = fa.getLong("fechaacreditacionprorrogada");
                            Date datePro = new Date(lDatePro);
                            factura.setFechaacreditacionprorrogada(date.format(datePro));
                        }
                        Long lDateVen = fa.getLong("fechaventa");
                        Date dateVen = new Date(lDateVen);
                        factura.setFechaventa(date.format(dateVen));
                        Long lDateVenc = fa.getLong("fechavencimiento");
                        Date dateVenc = new Date(lDateVenc);
                        factura.setFechavencimiento(date.format(dateVenc));
                        Long lDateAcre = fa.getLong("fechaacreditacion");
                        Date dateAcre = new Date(lDateAcre);
                        factura.setFechaacreditacion(date.format(dateAcre));
                        Long lDateDesp = fa.getLong("fechadespacho");
                        Date dateDesp = new Date(lDateDesp);
                        factura.setFechadespacho(date.format(dateDesp));
                        factura.setActiva(fa.getBoolean("activa"));
                        factura.setValortotal(fa.getBigDecimal("valortotal"));
                        factura.setIvatotal(fa.getBigDecimal("ivatotal"));
                        factura.setObservacion(fa.getString("observacion"));
                        factura.setPagada(fa.getBoolean("pagada"));
                        factura.setOeenpetro(fa.getBoolean("oeenpetro"));
                        factura.setCodigocliente(fa.getString("codigocliente"));
                        factura.setCodigoterminal(fa.getString("codigoterminal"));
                        factura.setCodigobanco(fa.getString("codigobanco"));
                        factura.setAdelantar(fa.getBoolean("adelantar"));
                        factura.setUsuarioactual(fa.getString("usuarioactual"));
                        factura.setNombrecomercializadora(fa.getString("nombrecomercializadora"));
                        factura.setRuccomercializadora(fa.getString("ruccomercializadora"));
                        factura.setDireccionmatrizcomercializadora(fa.getString("direccionmatrizcomercializadora"));
                        factura.setNombrecliente(fa.getString("nombrecliente"));
                        factura.setRuccliente(fa.getString("ruccliente"));
                        factura.setValorsinimpuestos(fa.getBigDecimal("valorsinimpuestos"));
                        if (!fa.isNull("correocliente")) {
                            factura.setCorreocliente(fa.getString("correocliente"));
                        }
                        factura.setDireccioncliente(fa.getString("direccioncliente"));
                        factura.setTelefonocliente(fa.getString("telefonocliente"));
                        if (!fa.isNull("numeroautorizacion")) {
                            factura.setNumeroautorizacion(fa.getString("numeroautorizacion"));
                        }
                        if (!fa.isNull("fechaautorizacion")) {
                            factura.setFechaautorizacion(fa.getString("fechaautorizacion"));
                        }
                        factura.setClienteformapago(fa.getString("clienteformapago"));
                        if (!fa.isNull("clienteformapagonosri")) {
                            factura.setClienteformapagonosri(fa.getString("clienteformapagonosri"));
                        }
                        factura.setPlazocliente(fa.getInt("plazocliente"));
                        factura.setClaveacceso(fa.getString("claveacceso"));
                        if (!fa.isNull("campoadicional_campo1")) {
                            factura.setCampoadicionalCampo1(fa.getString("campoadicional_campo1"));
                        }
                        if (!fa.isNull("campoadicional_campo2")) {
                            factura.setCampoadicionalCampo2(fa.getString("campoadicional_campo2"));
                        }
                        if (!fa.isNull("campoadicional_campo3")) {
                            factura.setCampoadicionalCampo3(fa.getString("campoadicional_campo3"));
                        }
                        if (!fa.isNull("campoadicional_campo4")) {
                            factura.setCampoadicionalCampo4(fa.getString("campoadicional_campo4"));
                        }
                        if (!fa.isNull("campoadicional_campo5")) {
                            factura.setCampoadicionalCampo5(fa.getString("campoadicional_campo5"));
                        }
                        if (!fa.isNull("campoadicional_campo6")) {
                            factura.setCampoadicionalCampo6(fa.getString("campoadicional_campo6"));
                        }
                        factura.setEstado(fa.getString("estado"));
                        Long error = fa.getLong("errordocumento");
                        Short errorS = error.shortValue();
                        factura.setErrordocumento(errorS);
                        Long hospedado = fa.getLong("hospedado");
                        Short hospedadoS = hospedado.shortValue();
                        factura.setHospedado(hospedadoS);
                        factura.setAmbientesri(fa.getString("ambientesri").charAt(0));
                        if (!fa.isNull("tipoemision")) {
                            factura.setTipoemision(fa.getString("tipoemision").charAt(0));
                        }
                        factura.setCodigodocumento(fa.getString("codigodocumento"));
                        factura.setEsagenteretencion(fa.getBoolean("esagenteretencion"));
                        factura.setEscontribuyenteespacial(fa.getString("escontribuyenteespacial"));
                        factura.setObligadocontabilidad(fa.getString("obligadocontabilidad"));
                        factura.setTipocomprador(fa.getString("tipocomprador"));
                        factura.setMoneda(fa.getString("moneda"));
                        factura.setSeriesri(fa.getString("seriesri"));
                        if (!fa.isNull("tipoplazocredito")) {
                            factura.setTipoplazocredito(fa.getString("tipoplazocredito"));
                        }
                        factura.setValorconrubro(fa.getBigDecimal("valorconrubro"));
                        factura.setOeanuladaenpetro(fa.getBoolean("oeanuladaenpetro"));
                        factura.setDespachada(fa.getBoolean("despachada"));
                        //factura.setRefacturada(fact.getBoolean("refacturada"));
                        //factura.setReliquidada(fact.getBigDecimal("reliquidada"));                            

                        if (fa.getBoolean("activa") == true) {
                            factura.setActiva(true);
                        } else {
                            factura.setActiva(false);
                        }
                        if (fa.getBoolean("oeenpetro") == true) {
                            factura.setOeenpetro(true);
                        } else {
                            factura.setOeenpetro(false);
                        }
                        if (fa.getString("estado").equalsIgnoreCase("PENDIENTE")) {
                            String ensri = "P";
                            envFac.setEnsri(ensri);
                        } else {
                            if (fa.isNull("numeroautorizacion")) {
                                String ensri = "N";
                                envFac.setEnsri(ensri);
                            } else {
                                factura.setNumeroautorizacion(fa.getString("numeroautorizacion"));
                                if (fa.getString("numeroautorizacion").length() == 49) {
                                    String ensri = "S";
                                    envFac.setEnsri(ensri);
                                } else {
                                    String ensri = "N";
                                    envFac.setEnsri(ensri);
                                }
                            }
                        }
                        factura.setFacturaPK(facturaPK);
                        envFac.setFactura(factura);
                        listFact.add(factura);
                        JSONArray detalleList = fa.getJSONArray("detallefacturaList");
                        for (int i = 0; i < detalleList.length(); i++) {
                            JSONObject det = detalleList.getJSONObject(i);
                            JSONObject detFpk = det.getJSONObject("detallefacturaPK");
                            detFac.setVolumennaturalrequerido(det.getBigDecimal("volumennaturalrequerido"));
                            detFac.setVolumennaturalautorizado(det.getBigDecimal("volumennaturalautorizado"));
                            detFac.setPrecioproducto(det.getBigDecimal("precioproducto"));
                            detFac.setSubtotal(det.getBigDecimal("subtotal"));
                            detFac.setCodigoimpuesto(det.getString("codigoimpuesto"));
                            if (!det.isNull("ruccomercializadora")) {
                                detFac.setRuccomercializadora(det.getString("ruccomercializadora"));
                            }
                            detFac.setDetallefacturaPK(detFacPK);
                            if (!det.isNull("nombreproducto")) {
                                detFac.setNombreproducto(det.getString("nombreproducto"));
                            }
                            listDet.add(detFac);
                            envFac.setDetalle(listDet);
                            detFac = new Detallefactura();
                            detFacPK = new DetallefacturaPK();
                        }
                        listenvF.add(envFac);
                        envFac = new EnvioFactura();
                        factura = new Factura();
                        facturaPK = new FacturaPK();
                        listDet = new ArrayList<>();
                    }
                }
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return listenvF;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EnvioFactura> obtenerFacturasObjEnv(String codAbas, String codComer, boolean activa, boolean pagada, String ruc, String fechaAcrePro) throws ParseException {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura";
            url = new URL(direcc + "/deudaxruc?codigoabastecedora=" + codAbas + "&codigocomercializadora=" + codComer + "&activa=" + activa + "&pagada=" + pagada + "&ruccliente=" + ruc + "&fechaacreditacionprorrogada=" + fechaAcrePro);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listenvF = new ArrayList<>();
            listFact = new ArrayList();
            listDet = new ArrayList<>();
            factura = new Factura();
            facturaPK = new FacturaPK();
            detFac = new Detallefactura();
            EnvioFactura envFac = new EnvioFactura();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            if (retorno.isEmpty()) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCONTRARON REGISTROS");
            } else {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject fa = retorno.getJSONObject(indice);
                        JSONObject faPK = fa.getJSONObject("facturaPK");

                        facturaPK.setCodigoabastecedora(faPK.getString("codigoabastecedora"));
                        facturaPK.setCodigocomercializadora(faPK.getString("codigocomercializadora"));
                        facturaPK.setNumeronotapedido(faPK.getString("numeronotapedido"));
                        facturaPK.setNumero(faPK.getString("numero"));
                        factura.setFacturaPK(facturaPK);

                        if (!fa.isNull("fechaacreditacionprorrogada")) {
                            Long lDatePro = fa.getLong("fechaacreditacionprorrogada");
                            Date datePro = new Date(lDatePro);
                            factura.setFechaacreditacionprorrogada(date.format(datePro));
                        }
                        Long lDateVen = fa.getLong("fechaventa");
                        Date dateVen = new Date(lDateVen);
                        factura.setFechaventa(date.format(dateVen));
                        Long lDateVenc = fa.getLong("fechavencimiento");
                        Date dateVenc = new Date(lDateVenc);
                        factura.setFechavencimiento(date.format(dateVenc));
                        Long lDateAcre = fa.getLong("fechaacreditacion");
                        Date dateAcre = new Date(lDateAcre);
                        factura.setFechaacreditacion(date.format(dateAcre));
                        Long lDateDesp = fa.getLong("fechadespacho");
                        Date dateDesp = new Date(lDateDesp);
                        factura.setFechadespacho(date.format(dateDesp));
                        factura.setActiva(fa.getBoolean("activa"));
                        factura.setValortotal(fa.getBigDecimal("valortotal"));
                        factura.setIvatotal(fa.getBigDecimal("ivatotal"));
                        factura.setObservacion(fa.getString("observacion"));
                        factura.setPagada(fa.getBoolean("pagada"));
                        factura.setOeenpetro(fa.getBoolean("oeenpetro"));
                        factura.setCodigocliente(fa.getString("codigocliente"));
                        factura.setCodigoterminal(fa.getString("codigoterminal"));
                        factura.setCodigobanco(fa.getString("codigobanco"));
                        factura.setAdelantar(fa.getBoolean("adelantar"));
                        factura.setUsuarioactual(fa.getString("usuarioactual"));
                        factura.setNombrecomercializadora(fa.getString("nombrecomercializadora"));
                        factura.setRuccomercializadora(fa.getString("ruccomercializadora"));
                        factura.setDireccionmatrizcomercializadora(fa.getString("direccionmatrizcomercializadora"));
                        factura.setNombrecliente(fa.getString("nombrecliente"));
                        factura.setRuccliente(fa.getString("ruccliente"));
                        factura.setValorsinimpuestos(fa.getBigDecimal("valorsinimpuestos"));
                        if (!fa.isNull("correocliente")) {
                            factura.setCorreocliente(fa.getString("correocliente"));
                        }
                        factura.setDireccioncliente(fa.getString("direccioncliente"));
                        factura.setTelefonocliente(fa.getString("telefonocliente"));
                        if (!fa.isNull("numeroautorizacion")) {
                            factura.setNumeroautorizacion(fa.getString("numeroautorizacion"));
                        }
                        if (!fa.isNull("fechaautorizacion")) {
                            factura.setFechaautorizacion(fa.getString("fechaautorizacion"));
                        }
                        factura.setClienteformapago(fa.getString("clienteformapago"));
                        if (!fa.isNull("clienteformapagonosri")) {
                            factura.setClienteformapagonosri(fa.getString("clienteformapagonosri"));
                        }
                        factura.setPlazocliente(fa.getInt("plazocliente"));
                        factura.setClaveacceso(fa.getString("claveacceso"));
                        if (!fa.isNull("campoadicional_campo1")) {
                            factura.setCampoadicionalCampo1(fa.getString("campoadicional_campo1"));
                        }
                        if (!fa.isNull("campoadicional_campo2")) {
                            factura.setCampoadicionalCampo2(fa.getString("campoadicional_campo2"));
                        }
                        if (!fa.isNull("campoadicional_campo3")) {
                            factura.setCampoadicionalCampo3(fa.getString("campoadicional_campo3"));
                        }
                        if (!fa.isNull("campoadicional_campo4")) {
                            factura.setCampoadicionalCampo4(fa.getString("campoadicional_campo4"));
                        }
                        if (!fa.isNull("campoadicional_campo5")) {
                            factura.setCampoadicionalCampo5(fa.getString("campoadicional_campo5"));
                        }
                        if (!fa.isNull("campoadicional_campo6")) {
                            factura.setCampoadicionalCampo6(fa.getString("campoadicional_campo6"));
                        }
                        factura.setEstado(fa.getString("estado"));
                        Long error = fa.getLong("errordocumento");
                        Short errorS = error.shortValue();
                        factura.setErrordocumento(errorS);
                        Long hospedado = fa.getLong("hospedado");
                        Short hospedadoS = hospedado.shortValue();
                        factura.setHospedado(hospedadoS);
                        factura.setAmbientesri(fa.getString("ambientesri").charAt(0));
                        if (!fa.isNull("tipoemision")) {
                            factura.setTipoemision(fa.getString("tipoemision").charAt(0));
                        }
                        factura.setCodigodocumento(fa.getString("codigodocumento"));
                        factura.setEsagenteretencion(fa.getBoolean("esagenteretencion"));
                        factura.setEscontribuyenteespacial(fa.getString("escontribuyenteespacial"));
                        factura.setObligadocontabilidad(fa.getString("obligadocontabilidad"));
                        factura.setTipocomprador(fa.getString("tipocomprador"));
                        factura.setMoneda(fa.getString("moneda"));
                        factura.setSeriesri(fa.getString("seriesri"));
                        if (!fa.isNull("tipoplazocredito")) {
                            factura.setTipoplazocredito(fa.getString("tipoplazocredito"));
                        }
                        factura.setValorconrubro(fa.getBigDecimal("valorconrubro"));
                        factura.setOeanuladaenpetro(fa.getBoolean("oeanuladaenpetro"));
                        factura.setDespachada(fa.getBoolean("despachada"));
                        //factura.setRefacturada(fact.getBoolean("refacturada"));
                        //factura.setReliquidada(fact.getBigDecimal("reliquidada"));                            

                        if (fa.getBoolean("activa") == true) {
                            factura.setActiva(true);
                        } else {
                            factura.setActiva(false);
                        }
                        if (fa.getBoolean("oeenpetro") == true) {
                            factura.setOeenpetro(true);
                        } else {
                            factura.setOeenpetro(false);
                        }
                        if (fa.getString("estado").equalsIgnoreCase("PENDIENTE")) {
                            String ensri = "P";
                            envFac.setEnsri(ensri);
                        } else {
                            if (fa.isNull("numeroautorizacion")) {
                                String ensri = "N";
                                envFac.setEnsri(ensri);
                            } else {
                                factura.setNumeroautorizacion(fa.getString("numeroautorizacion"));
                                if (fa.getString("numeroautorizacion").length() == 49) {
                                    String ensri = "S";
                                    envFac.setEnsri(ensri);
                                } else {
                                    String ensri = "N";
                                    envFac.setEnsri(ensri);
                                }
                            }
                        }
                        factura.setFacturaPK(facturaPK);
                        envFac.setFactura(factura);
                        listFact.add(factura);
                        JSONArray detalleList = fa.getJSONArray("detallefacturaList");
                        for (int i = 0; i < detalleList.length(); i++) {
                            JSONObject det = detalleList.getJSONObject(i);
                            JSONObject detFpk = det.getJSONObject("detallefacturaPK");
                            detFac.setVolumennaturalrequerido(det.getBigDecimal("volumennaturalrequerido"));
                            detFac.setVolumennaturalautorizado(det.getBigDecimal("volumennaturalautorizado"));
                            detFac.setPrecioproducto(det.getBigDecimal("precioproducto"));
                            detFac.setSubtotal(det.getBigDecimal("subtotal"));
                            detFac.setCodigoimpuesto(det.getString("codigoimpuesto"));
                            if (!det.isNull("ruccomercializadora")) {
                                detFac.setRuccomercializadora(det.getString("ruccomercializadora"));
                            }
                            detFac.setDetallefacturaPK(detFacPK);
                            if (!det.isNull("nombreproducto")) {
                                detFac.setNombreproducto(det.getString("nombreproducto"));
                            }
                            listDet.add(detFac);
                            envFac.setDetalle(listDet);
                            detFac = new Detallefactura();
                            detFacPK = new DetallefacturaPK();
                        }
                        listenvF.add(envFac);
                        envFac = new EnvioFactura();
                        factura = new Factura();
                        facturaPK = new FacturaPK();
                        listDet = new ArrayList<>();
                    }
                }
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return listenvF;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
