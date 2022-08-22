/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.pedidosyfacturacion.servicios;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Factura;
import ec.com.infinityone.modeloWeb.FacturaPK;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author HP
 */
@LocalBean
@Stateless
public class FacturaServicio {

    /*
    Variable que almacena varias Facturas
     */
    private List<Factura> listaFacturas;

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
        //urlInfinity = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.areamercadeo
        //https://www.supertech.ec:8443/infinityone1/resources/usuario/login?user=Ftapia&password=123456";
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

}
