/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.servicio.pedidosyfacturacion;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.FacturaComercialDto;
import ec.com.infinityone.modelo.FacturaComercialPKDto;
import ec.com.infinityone.reusable.ReusableBean;
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
 * @author SonyVaio
 */
@LocalBean
@Stateless
public class FacturaComercialServicio extends ReusableBean {

    private FacturaComercialDto facturaComercialDto;

    private FacturaComercialPKDto facturaComercialPKDto;

    private List<FacturaComercialDto> listfacturaComercialDto;

    public List<FacturaComercialDto> obtenerFacturas(Date fechaIni, Date fechaFin, String codComer) {

        DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
        String fechaI = date.format(fechaIni);
        String fechaF = date.format(fechaFin);

        try {
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura/facturaBG?pcodigocomercializadora=" + codComer + "&pfechainicial=" + fechaI + "&pfechafinal=" + fechaF);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listfacturaComercialDto = new ArrayList<>();
            facturaComercialDto = new FacturaComercialDto();
            facturaComercialPKDto = new FacturaComercialPKDto();

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

                facturaComercialPKDto.setCodigoComercializadora(fact.getString("codigocomercializadora"));
                facturaComercialPKDto.setCodigoBanco(fact.getString("codigobanco"));
                facturaComercialPKDto.setTipoRegistro(fact.getString("tiporegistro"));
                facturaComercialPKDto.setNumeroOrden(fact.getString("numeroorden"));
                facturaComercialPKDto.setCodigoCliente(fact.getString("codigocliente"));
                facturaComercialPKDto.setCodigoEstablecimiento(fact.getString("codigoestablecimiento"));
                facturaComercialPKDto.setFechaVenta(fact.getString("fechaventa"));
                facturaComercialPKDto.setAgencia(fact.getString("agencia"));
                facturaComercialPKDto.setCodigoTerminal(fact.getString("codigoterminal"));
                facturaComercialPKDto.setValorTotal(fact.getString("valortotal"));
                facturaComercialPKDto.setFechaEmision(fact.getString("fechaemision"));
                facturaComercialPKDto.setFechaVencimiento(fact.getString("fechavencimiento"));
                facturaComercialPKDto.setFechaPostergacion(fact.getString("fechapostergacion"));
                facturaComercialPKDto.setNumeroSri(fact.getString("numerosri"));
                facturaComercialPKDto.setEstadoFactura(fact.getString("estadofactura"));
                facturaComercialPKDto.setClaveAcceso(fact.getString("claveacceso"));
                facturaComercialPKDto.setNumeroAutorizacionsri(fact.getString("numeroautorizacionsri"));
                facturaComercialPKDto.setFechaVigencia(fact.getString("fechavigencia"));
                facturaComercialPKDto.setFechaCaducidad(fact.getString("fechacaducidad"));
                facturaComercialPKDto.setFormaPago1(fact.getString("formapago1"));
                facturaComercialPKDto.setDescripcionFormapago1(fact.getString("descripcionformapago1"));
                facturaComercialPKDto.setNumeroCuentaformapago1(fact.getString("numerocuentaformapago1"));
                facturaComercialPKDto.setNumeroChequeformapago1(fact.getString("numerochequeformapago1"));
                facturaComercialPKDto.setCodigoBancoformapago1(fact.getString("codigobancoformapago1"));
                facturaComercialPKDto.setDescripcionBancoformapago1(fact.getString("descripcionbancoformapago1"));
                facturaComercialPKDto.setFormaPago2(fact.getString("formapago2"));
                facturaComercialPKDto.setDescripcionFormapago2(fact.getString("descripcionformapago2"));
                facturaComercialPKDto.setNumeroCuentaformapago2(fact.getString("numerocuentaformapago2"));
                facturaComercialPKDto.setNumeroChequeformapago2(fact.getString("numerochequeformapago2"));
                facturaComercialPKDto.setCodigoBancoformapago2(fact.getString("codigobancoformapago2"));
                facturaComercialPKDto.setDescripcionBancoformapago2(fact.getString("descripcionbancoformapago2"));

                facturaComercialDto.setFacturaComercialPKDto(facturaComercialPKDto);
                facturaComercialDto.setDcodigocomercializadora(fact.getString("dcodigocomercializadora"));
                facturaComercialDto.setDcodigobanco(fact.getString("dcodigobanco"));
                facturaComercialDto.setDtiporegistro(fact.getString("dtiporegistro"));
                facturaComercialDto.setDnumeroorden(fact.getString("dnumeroorden"));
                facturaComercialDto.setDcodigocliente(fact.getString("dcodigocliente"));
                facturaComercialDto.setDcodigoestablecimiento(fact.getString("dcodigoestablecimiento"));
                facturaComercialDto.setDcodigoproducto(fact.getString("dcodigoproducto"));
                facturaComercialDto.setDcantidad(fact.getString("dcantidad"));
                facturaComercialDto.setDunidadmedida(fact.getString("dunidadmedida"));
                facturaComercialDto.setDpreciounitario(fact.getString("dpreciounitario"));
                facturaComercialDto.setDtotal(fact.getString("dtotal"));
                facturaComercialDto.setDtiporubro(fact.getString("dtiporubro"));

                listfacturaComercialDto.add(facturaComercialDto);
                facturaComercialDto = new FacturaComercialDto();
                facturaComercialPKDto = new FacturaComercialPKDto();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listfacturaComercialDto;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listfacturaComercialDto;
    }

}
