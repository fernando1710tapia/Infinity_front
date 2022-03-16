/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.actorcomercial.serivicios;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Banco;
import ec.com.infinityone.modeloWeb.Cliente;
import ec.com.infinityone.modeloWeb.Direccioninen;
import ec.com.infinityone.modeloWeb.Formapago;
import ec.com.infinityone.modeloWeb.Terminal;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author HP
 */
@LocalBean
@Stateless
public class ClienteServicio {

    /*
    Variable que almacena varias Comercializaros
     */
    private List<Cliente> listaClientes;

    private Cliente cli;

    private Terminal termi;

    private Formapago fpago;

    private Banco banco;

    private Direccioninen dinen;

    private int esActivo;

    public List<Cliente> obtenerClientes() {

        try {

            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.cliente");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.cliente");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaClientes = new ArrayList<>();
            cli = new Cliente();
            termi = new Terminal();
            fpago = new Formapago();
            banco = new Banco();
            dinen = new Direccioninen();
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
                JSONObject cliente = retorno.getJSONObject(indice);
                JSONObject terminal = cliente.getJSONObject("codigoterminaldefecto");
                JSONObject fp = cliente.getJSONObject("codigoformapago");
                JSONObject bn = cliente.getJSONObject("codigobancodebito");
                JSONObject di = cliente.getJSONObject("codigodireccioninen");
                //JSONObject tc = cliente.getJSONObject("codigotipocliente");

                termi.setCodigo(terminal.getString("codigo"));
                termi.setNombre(terminal.getString("nombre"));
                cli.setCodigoterminaldefecto(termi);
                cli.setCodigo(cliente.getString("codigo"));
                cli.setNombre(cliente.getString("nombre"));
                if (!cliente.isNull("nombrecomercial")) {
                    cli.setNombrecomercial(cliente.getString("nombrecomercial"));
                }
                cli.setCodigoarch(cliente.getString("codigoarch"));
                cli.setCodigostc(cliente.getString("codigostc"));
                cli.setClavestc(cliente.getString("clavestc"));
                cli.setCodigocomercializadora(cliente.getString("codigocomercializadora"));
                cli.setRuc(cliente.getString("ruc"));
                cli.setDireccion(cliente.getString("direccion"));
                cli.setEstado(cliente.getBoolean("estado"));
                if (!cliente.isNull("identificacionrepresentantelega")) {
                    cli.setIdentificacionrepresentantelega(cliente.getString("identificacionrepresentantelega"));
                }
                if (!cliente.isNull("nombrearrendatario")) {
                    cli.setNombrearrendatario(cliente.getString("nombrearrendatario"));
                }
                if (!cliente.isNull("nombrerepresentantelegal")) {
                    cli.setNombrerepresentantelegal(cliente.getString("nombrerepresentantelegal"));
                }
                if (!cliente.isNull("escontribuyenteespacial")) {
                    cli.setEscontribuyenteespacial(cliente.getString("escontribuyenteespacial"));
                }
                if (!cliente.isNull("telefono1")) {
                    cli.setTelefono1(cliente.getString("telefono1"));
                }
                if (!cliente.isNull("telefono2")) {
                    cli.setTelefono2(cliente.getString("telefono2"));
                }
                if (!cliente.isNull("correo1")) {
                    cli.setCorreo1(cliente.getString("correo1"));
                }
                if (!cliente.isNull("correo2")) {
                    cli.setCorreo2(cliente.getString("correo2"));
                }
                if (!cliente.isNull("tipoplazocredito")) {
                    cli.setTipoplazocredito(cliente.getString("tipoplazocredito"));
                }
                if (!cliente.isNull("diasplazocredito")) {
                    Long lDiasP = cliente.getLong("diasplazocredito");
                    cli.setDiasplazocredito(lDiasP.shortValue());
                }
                if (!cliente.isNull("tasainteres")) {
                    cli.setTasainteres(cliente.getBigDecimal("tasainteres"));
                }
                if (!cliente.isNull("cuentadebito")) {
                    cli.setCuentadebito(cliente.getString("cuentadebito"));
                }
                if (!cliente.isNull("tipocuentadebito")) {
                    cli.setTipocuentadebito(cliente.getString("tipocuentadebito"));
                }
                if (!cliente.isNull("controlagarantia")) {
                    cli.setControlagarantia(cliente.getBoolean("controlagarantia"));
                }

                cli.setCodigolistaprecio(cliente.getLong("codigolistaprecio"));

                if (!cliente.isNull("codigolistaflete")) {
                    cli.setCodigolistaflete(cliente.getString("codigolistaflete"));
                }
                if (!cliente.isNull("aplicasubsidio2")) {
                    cli.setAplicasubsidio2(cliente.getBoolean("aplicasubsidio2"));
                }
                if (!cliente.isNull("centrocosto")) {
                    cli.setCentrocosto(cliente.getString("centrocosto"));
                }
                if (!cliente.isNull("fehainscripcion")) {
                    Long lDateIns = cliente.getLong("fehainscripcion");
                    Date dateIns = new Date(lDateIns);
                    cli.setFehainscripcion(dateIns);
                } else {
                    cli.setFehainscripcion(new Date());
                }
                if (!cliente.isNull("fehainiciooperacion")) {
                    Long lDateIniO = cliente.getLong("fehainiciooperacion");
                    Date dateIniO = new Date(lDateIniO);
                    cli.setFehainiciooperacion(dateIniO);
                } else {
                    cli.setFehainiciooperacion(new Date());
                }
                if (!cliente.isNull("feharegistroarch")) {
                    Long lDateReg = cliente.getLong("feharegistroarch");
                    Date dateReg = new Date(lDateReg);
                    cli.setFeharegistroarch(dateReg);
                } else {
                    cli.setFeharegistroarch(new Date());
                }
                if (!cliente.isNull("fehavencimientocontrato")) {
                    Long lDateVen = cliente.getLong("fehavencimientocontrato");
                    Date dateVen = new Date(lDateVen);
                    cli.setFehavencimientocontrato(dateVen);
                } else {
                    cli.setFehavencimientocontrato(new Date());
                }
                if (!cliente.isNull("codigosupervisorzonal")) {
                    cli.setCodigosupervisorzonal(cliente.getString("codigosupervisorzonal"));
                }
                if (!cliente.isNull("usuarioactual")) {
                    cli.setUsuarioactual(cliente.getString("usuarioactual"));
                }
                if (!cliente.isNull("codigobancodebito")) {
                    banco.setCodigo(bn.getString("codigo"));
                    cli.setCodigobancodebito(banco.getCodigo());
                }
                if (!cliente.isNull("codigodireccioninen")) {
                    dinen.setCodigo(di.getString("codigo"));
                    cli.setCodigodireccioninen(dinen.getCodigo());
                }
                if (!cliente.isNull("codigoformapago")) {
                    fpago.setCodigo(fp.getString("codigo"));
                    cli.setCodigoformapago(fpago);
                }
               // if (!cliente.isNull("codigotipocliente")) {
                 //   cli.setCodigotipocliente(tc.getString("codigo"));
               // }
                //cli.setObjRelacionado(cliente.getString("codigo") + " - " + cliente.getString("nombre"));                                    

//                if (cli.isEstado(true)) {
//                        listaClientes.add(cli);
//                }   
                listaClientes.add(cli);
                cli = new Cliente();
                termi = new Terminal();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return listaClientes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaClientes;
    }

    public List<Cliente> obtenerClientesPorComercializadora(String codComer) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.cliente/porComercializadora?codigocomercializadora="+codComer);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.cliente/porComercializadora?codigocomercializadora=" + codComer);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaClientes = new ArrayList<>();
            cli = new Cliente();
            termi = new Terminal();
            fpago = new Formapago();
            banco = new Banco();
            dinen = new Direccioninen();
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
                JSONObject cliente = retorno.getJSONObject(indice);
                JSONObject terminal = cliente.getJSONObject("codigoterminaldefecto");
                JSONObject fp = cliente.getJSONObject("codigoformapago");
                JSONObject bn = cliente.getJSONObject("codigobancodebito");
                JSONObject di = cliente.getJSONObject("codigodireccioninen");
                JSONObject tc = cliente.getJSONObject("codigotipocliente");

                termi.setCodigo(terminal.getString("codigo"));
                termi.setNombre(terminal.getString("nombre"));
                cli.setCodigoterminaldefecto(termi);

                cli.setCodigo(cliente.getString("codigo"));
                cli.setNombre(cliente.getString("nombre"));
                if (!cliente.isNull("nombrecomercial")) {
                    cli.setNombrecomercial(cliente.getString("nombrecomercial"));
                }
                cli.setCodigoarch(cliente.getString("codigoarch"));
                cli.setCodigostc(cliente.getString("codigostc"));
                cli.setClavestc(cliente.getString("clavestc"));
                cli.setCodigocomercializadora(cliente.getString("codigocomercializadora"));
                cli.setRuc(cliente.getString("ruc"));
                cli.setDireccion(cliente.getString("direccion"));
                cli.setEstado(cliente.getBoolean("estado"));

                if (!cliente.isNull("identificacionrepresentantelega")) {
                    cli.setIdentificacionrepresentantelega(cliente.getString("identificacionrepresentantelega"));
                }
                if (!cliente.isNull("nombrearrendatario")) {
                    cli.setNombrearrendatario(cliente.getString("nombrearrendatario"));
                }
                if (!cliente.isNull("nombrerepresentantelegal")) {
                    cli.setNombrerepresentantelegal(cliente.getString("nombrerepresentantelegal"));
                }
                if (!cliente.isNull("escontribuyenteespacial")) {
                    cli.setEscontribuyenteespacial(cliente.getString("escontribuyenteespacial"));
                }
                if (!cliente.isNull("telefono1")) {
                    cli.setTelefono1(cliente.getString("telefono1"));
                }
                if (!cliente.isNull("telefono2")) {
                    cli.setTelefono2(cliente.getString("telefono2"));
                }
                if (!cliente.isNull("correo1")) {
                    cli.setCorreo1(cliente.getString("correo1"));
                }
                if (!cliente.isNull("correo2")) {
                    cli.setCorreo2(cliente.getString("correo2"));
                }
                if (!cliente.isNull("tipoplazocredito")) {
                    cli.setTipoplazocredito(cliente.getString("tipoplazocredito"));
                }
                if (!cliente.isNull("diasplazocredito")) {
                    Long lDiasP = cliente.getLong("diasplazocredito");
                    cli.setDiasplazocredito(lDiasP.shortValue());
                }
                if (!cliente.isNull("tasainteres")) {
                    cli.setTasainteres(cliente.getBigDecimal("tasainteres"));
                }
                if (!cliente.isNull("cuentadebito")) {
                    cli.setCuentadebito(cliente.getString("cuentadebito"));
                }
                if (!cliente.isNull("tipocuentadebito")) {
                    cli.setTipocuentadebito(cliente.getString("tipocuentadebito"));
                }
                if (!cliente.isNull("controlagarantia")) {
                    cli.setControlagarantia(cliente.getBoolean("controlagarantia"));
                }

                cli.setCodigolistaprecio(cliente.getLong("codigolistaprecio"));

                if (!cliente.isNull("codigolistaflete")) {
                    cli.setCodigolistaflete(cliente.getString("codigolistaflete"));
                }
                if (!cliente.isNull("aplicasubsidio2")) {
                    cli.setAplicasubsidio2(cliente.getBoolean("aplicasubsidio2"));
                }
                if (!cliente.isNull("centrocosto")) {
                    cli.setCentrocosto(cliente.getString("centrocosto"));
                }
                if (!cliente.isNull("fehainscripcion")) {
                    Long lDateIns = cliente.getLong("fehainscripcion");
                    Date dateIns = new Date(lDateIns);
                    cli.setFehainscripcion(dateIns);
                } else {
                    cli.setFehainscripcion(new Date());
                }
                if (!cliente.isNull("fehainiciooperacion")) {
                    Long lDateIniO = cliente.getLong("fehainiciooperacion");
                    Date dateIniO = new Date(lDateIniO);
                    cli.setFehainiciooperacion(dateIniO);
                } else {
                    cli.setFehainiciooperacion(new Date());
                }
                if (!cliente.isNull("feharegistroarch")) {
                    Long lDateReg = cliente.getLong("feharegistroarch");
                    Date dateReg = new Date(lDateReg);
                    cli.setFeharegistroarch(dateReg);
                } else {
                    cli.setFeharegistroarch(new Date());
                }
                if (!cliente.isNull("fehavencimientocontrato")) {
                    Long lDateVen = cliente.getLong("fehavencimientocontrato");
                    Date dateVen = new Date(lDateVen);
                    cli.setFehavencimientocontrato(dateVen);
                } else {
                    cli.setFehavencimientocontrato(new Date());
                }
                if (!cliente.isNull("codigosupervisorzonal")) {
                    cli.setCodigosupervisorzonal(cliente.getString("codigosupervisorzonal"));
                }
                if (!cliente.isNull("usuarioactual")) {
                    cli.setUsuarioactual(cliente.getString("usuarioactual"));
                }
                if (!cliente.isNull("codigobancodebito")) {
                    banco.setCodigo(bn.getString("codigo"));
                    cli.setCodigobancodebito(banco.getCodigo());
                }
                if (!cliente.isNull("codigodireccioninen")) {
                    dinen.setCodigo(di.getString("codigo"));
                    cli.setCodigodireccioninen(dinen.getCodigo());
                }
                if (!cliente.isNull("codigoformapago")) {
                    fpago.setCodigo(fp.getString("codigo"));
                    cli.setCodigoformapago(fpago);
                }
                if (!cliente.isNull("codigotipocliente")) {
                    cli.setCodigotipocliente(tc.getString("codigo"));
                }

                listaClientes.add(cli);
                cli = new Cliente();
                termi = new Terminal();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return listaClientes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaClientes;
    }

    public int obtenerClientePorListaPrecio(Long codigoListaP) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.cliente/porComercializadora?codigocomercializadora="+codComer);
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.cliente/porListaprecio?codigolistaprecio="+codigoListaP);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.cliente/porListaprecio?codigolistaprecio=" + codigoListaP);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            cli = new Cliente();
            esActivo = 0;
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
                JSONObject cliente = retorno.getJSONObject(indice);

                cli.setCodigo(cliente.getString("codigo"));
                cli.setNombre(cliente.getString("nombre"));
                cli.setEstado(cliente.getBoolean("estado"));
                if (cli.isEstado() == true) {
                    esActivo++;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Información: ", "El cliente " + cli.getNombre() + " con código: " + cli.getCodigo()
                            + " se encuentra activo, por favor desactivar el cliente para continuar con la actualización personalizada"));
                } else {
                    esActivo = 0;
                }
                //listaClientes.add(cli);
                cli = new Cliente();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return esActivo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return esActivo;
    }

    public int obtenerClientePorListaPrecioNoMsg(Long codigoListaP) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.cliente/porComercializadora?codigocomercializadora="+codComer);
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.cliente/porListaprecio?codigolistaprecio="+codigoListaP);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.cliente/porListaprecio?codigolistaprecio=" + codigoListaP);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            cli = new Cliente();
            esActivo = 0;
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
                JSONObject cliente = retorno.getJSONObject(indice);

                cli.setCodigo(cliente.getString("codigo"));
                cli.setNombre(cliente.getString("nombre"));
                cli.setEstado(cliente.getBoolean("estado"));
                if (cli.isEstado() == true) {
                    esActivo++;
                } else {
                    esActivo = 0;
                }
                //listaClientes.add(cli);
                cli = new Cliente();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return esActivo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return esActivo;
    }

    public List<Cliente> obtenerClientesActivosPorComercializadora(String codComer) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.cliente/porComercializadora?codigocomercializadora="+codComer);
            //URL url = new URL("http://200.93.248.121:8080/infinityone1/resources/ec.com.infinity.modelo.cliente/porComercializadora?codigocomercializadora=" + codComer);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.cliente/porComercializadora?codigocomercializadora=" + codComer);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaClientes = new ArrayList<>();
            cli = new Cliente();
            termi = new Terminal();
            fpago = new Formapago();
            banco = new Banco();
            dinen = new Direccioninen();
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
                JSONObject cliente = retorno.getJSONObject(indice);
                JSONObject terminal = cliente.getJSONObject("codigoterminaldefecto");
                JSONObject fp = cliente.getJSONObject("codigoformapago");
                JSONObject bn = cliente.getJSONObject("codigobancodebito");
                JSONObject di = cliente.getJSONObject("codigodireccioninen");

                termi.setCodigo(terminal.getString("codigo"));
                termi.setNombre(terminal.getString("nombre"));
                cli.setCodigoterminaldefecto(termi);

                cli.setCodigo(cliente.getString("codigo"));
                cli.setNombre(cliente.getString("nombre"));
                if (!cliente.isNull("nombrecomercial")) {
                    cli.setNombrecomercial(cliente.getString("nombrecomercial"));
                }
                cli.setCodigoarch(cliente.getString("codigoarch"));
                cli.setCodigostc(cliente.getString("codigostc"));
                cli.setClavestc(cliente.getString("clavestc"));
                cli.setCodigocomercializadora(cliente.getString("codigocomercializadora"));
                cli.setRuc(cliente.getString("ruc"));
                cli.setDireccion(cliente.getString("direccion"));
                cli.setEstado(cliente.getBoolean("estado"));

                if (!cliente.isNull("identificacionrepresentantelega")) {
                    cli.setIdentificacionrepresentantelega(cliente.getString("identificacionrepresentantelega"));
                }
                if (!cliente.isNull("nombrearrendatario")) {
                    cli.setNombrearrendatario(cliente.getString("nombrearrendatario"));
                }
                if (!cliente.isNull("nombrerepresentantelegal")) {
                    cli.setNombrerepresentantelegal(cliente.getString("nombrerepresentantelegal"));
                }
                if (!cliente.isNull("escontribuyenteespacial")) {
                    cli.setEscontribuyenteespacial(cliente.getString("escontribuyenteespacial"));
                }
                if (!cliente.isNull("telefono1")) {
                    cli.setTelefono1(cliente.getString("telefono1"));
                }
                if (!cliente.isNull("telefono2")) {
                    cli.setTelefono2(cliente.getString("telefono2"));
                }
                if (!cliente.isNull("correo1")) {
                    cli.setCorreo1(cliente.getString("correo1"));
                }
                if (!cliente.isNull("correo2")) {
                    cli.setCorreo2(cliente.getString("correo2"));
                }
                if (!cliente.isNull("tipoplazocredito")) {
                    cli.setTipoplazocredito(cliente.getString("tipoplazocredito"));
                }
                if (!cliente.isNull("diasplazocredito")) {
                    Long lDiasP = cliente.getLong("diasplazocredito");
                    cli.setDiasplazocredito(lDiasP.shortValue());
                }
                if (!cliente.isNull("tasainteres")) {
                    cli.setTasainteres(cliente.getBigDecimal("tasainteres"));
                }
                if (!cliente.isNull("cuentadebito")) {
                    cli.setCuentadebito(cliente.getString("cuentadebito"));
                }
                if (!cliente.isNull("tipocuentadebito")) {
                    cli.setTipocuentadebito(cliente.getString("tipocuentadebito"));
                }
                if (!cliente.isNull("controlagarantia")) {
                    cli.setControlagarantia(cliente.getBoolean("controlagarantia"));
                }

                cli.setCodigolistaprecio(cliente.getLong("codigolistaprecio"));

                if (!cliente.isNull("codigolistaflete")) {
                    cli.setCodigolistaflete(cliente.getString("codigolistaflete"));
                }
                if (!cliente.isNull("aplicasubsidio2")) {
                    cli.setAplicasubsidio2(cliente.getBoolean("aplicasubsidio2"));
                }
                if (!cliente.isNull("centrocosto")) {
                    cli.setCentrocosto(cliente.getString("centrocosto"));
                }
                if (!cliente.isNull("fehainscripcion")) {
                    Long lDateIns = cliente.getLong("fehainscripcion");
                    Date dateIns = new Date(lDateIns);
                    cli.setFehainscripcion(dateIns);
                } else {
                    cli.setFehainscripcion(new Date());
                }
                if (!cliente.isNull("fehainiciooperacion")) {
                    Long lDateIniO = cliente.getLong("fehainiciooperacion");
                    Date dateIniO = new Date(lDateIniO);
                    cli.setFehainiciooperacion(dateIniO);
                } else {
                    cli.setFehainiciooperacion(new Date());
                }
                if (!cliente.isNull("feharegistroarch")) {
                    Long lDateReg = cliente.getLong("feharegistroarch");
                    Date dateReg = new Date(lDateReg);
                    cli.setFeharegistroarch(dateReg);
                } else {
                    cli.setFeharegistroarch(new Date());
                }
                if (!cliente.isNull("fehavencimientocontrato")) {
                    Long lDateVen = cliente.getLong("fehavencimientocontrato");
                    Date dateVen = new Date(lDateVen);
                    cli.setFehavencimientocontrato(dateVen);
                } else {
                    cli.setFehavencimientocontrato(new Date());
                }
                if (!cliente.isNull("codigosupervisorzonal")) {
                    cli.setCodigosupervisorzonal(cliente.getString("codigosupervisorzonal"));
                }
                if (!cliente.isNull("usuarioactual")) {
                    cli.setUsuarioactual(cliente.getString("usuarioactual"));
                }
                if (!cliente.isNull("codigobancodebito")) {
                    banco.setCodigo(bn.getString("codigo"));
                    cli.setCodigobancodebito(banco.getCodigo());
                }
                if (!cliente.isNull("codigodireccioninen")) {
                    dinen.setCodigo(di.getString("codigo"));
                    cli.setCodigodireccioninen(dinen.getCodigo());
                }
                if (!cliente.isNull("codigoformapago")) {
                    fpago.setCodigo(fp.getString("codigo"));
                    cli.setCodigoformapago(fpago);
                }
                if (cliente.getBoolean("estado") == true) {
                    listaClientes.add(cli);
                }
                cli = new Cliente();
                termi = new Terminal();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return listaClientes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaClientes;
    }

    public Cliente getCli() {
        return cli;
    }

    public void setCli(Cliente cli) {
        this.cli = cli;
    }

}
