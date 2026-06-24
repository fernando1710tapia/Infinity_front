/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.serivicio.actorcomercial;

import ec.com.infinityone.modelo.ObjetoNivel1;
import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.configuration.Fichero;
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
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author HP
 */
@LocalBean
@Stateless
public class ComercializadoraServicio {

    /*
    Variable que almacena varias Comercializaros
     */
    private List<ComercializadoraBean> listaComercializadora;
    /*
    Objeto comercializadora
     */
    private ComercializadoraBean comercializadora;

    public List<ComercializadoraBean> obtenerComercializadoras() {

        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.comercializadora");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadora");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaComercializadora = new ArrayList<>();
            comercializadora = new ComercializadoraBean();
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
                if (!retorno.isNull(indice)) {
                    JSONObject comer = retorno.getJSONObject(indice);
                    JSONObject abas = comer.getJSONObject("codigoabastecedora");
                    JSONObject codBanco = comer.getJSONObject("codigobancodebito");
                    comercializadora.setCodigo(comer.getString("codigo"));
                    comercializadora.setNombre(comer.getString("nombre"));
                    if (comer.getBoolean("activo") == true) {
                        comercializadora.setActivo("S");
                    } else {
                        comercializadora.setActivo("N");
                    }
                    comercializadora.setCodigoARCH(comer.getString("codigoarch"));
                    comercializadora.setCodigoSTC(comer.getString("codigostc"));
                    comercializadora.setClaveSTC(comer.getString("clavestc"));
                    comercializadora.setCodigoAbas(abas.getString("codigo"));
                    comercializadora.setRuc(comer.getString("ruc"));
                    if (!comer.isNull("telefono1")) {
                        comercializadora.setTel1(comer.getString("telefono1"));
                    }
                    if (!comer.isNull("nombrecorto")) {
                        comercializadora.setNombreCorto(comer.getString("nombrecorto"));
                    }
                    if (!comer.isNull("direccion")) {
                        comercializadora.setDireccion(comer.getString("direccion"));
                    }
                    if (!comer.isNull("identificacionrepresentantelega")) {
                        comercializadora.setIdRepLegal(comer.getString("identificacionrepresentantelega"));
                    }
                    if (!comer.isNull("nombrerepresentantelegal")) {
                        comercializadora.setNomRepLegal(comer.getString("nombrerepresentantelegal"));
                    }
                    if (!comer.isNull("escontribuyenteespacial")) {
                        comercializadora.setEsContriEspecial(comer.getString("escontribuyenteespacial"));
                    }
                    if (!comer.isNull("telefono2")) {
                        comercializadora.setTel2(comer.getString("telefono2"));
                    }
                    if (!comer.isNull("correo1")) {
                        comercializadora.setCorreo1(comer.getString("correo1"));
                    }
                    if (!comer.isNull("correo2")) {
                        comercializadora.setCorre2(comer.getString("correo2"));
                    }
                    if (!comer.isNull("tipoplazocredito")) {
                        comercializadora.setTipoPlaCred(comer.getString("tipoplazocredito"));
                    }
                    if (!comer.isNull("diasplazocredito")) {
                        Long diasPlazo = comer.getLong("diasplazocredito");
                        String sDiasPlazo = diasPlazo.toString();
                        comercializadora.setDiasPlaCred(sDiasPlazo);
                    }
                    if (!codBanco.isNull("codigo")) {
                        comercializadora.setCodigoBancDeb(codBanco.getString("codigo"));
                    }
                    if (!comer.isNull("cuentadebito")) {
                        comercializadora.setCuentaDeb(comer.getString("cuentadebito"));
                    }
                    if (!comer.isNull("tipocuentadebito")) {
                        comercializadora.setTipoCuenDeb(comer.getString("tipocuentadebito"));
                    }
                    if (!comer.isNull("tasainteres")) {
                        Long tasa = comer.getLong("tasainteres");
                        String sTasa = tasa.toString();
                        comercializadora.setTasaInteres(sTasa);
                    }
                    if (!comer.isNull("fehainiciocontrato")) {
                        Long lDateIni = comer.getLong("fehainiciocontrato");
                        Date dateIni = new Date(lDateIni);
                        comercializadora.setFechaIniCont(dateIni);
                    } else {
                        comercializadora.setFechaIniCont(new Date());
                    }
                    if (!comer.isNull("fechavencimientocontr")) {
                        Long lDateFin = comer.getLong("fechavencimientocontr");
                        Date dateFin = new Date(lDateFin);
                        comercializadora.setFechaVencCont(dateFin);
                    } else {
                        comercializadora.setFechaVencCont(new Date());
                    }
                    if (!comer.isNull("establecimientofac")) {
                        comercializadora.setEstabFac(comer.getString("establecimientofac"));
                    }
                    if (!comer.isNull("puntoventafac")) {
                        comercializadora.setPvFac(comer.getString("puntoventafac"));
                    }
                    if (!comer.isNull("establecimientondb")) {
                        comercializadora.setEstabNdb(comer.getString("establecimientondb"));
                    }
                    if (!comer.isNull("puntoventandb")) {
                        comercializadora.setPvNdb(comer.getString("puntoventandb"));
                    }
                    if (!comer.isNull("establecimientoncr")) {
                        comercializadora.setEstabNcr(comer.getString("establecimientoncr"));
                    }
                    if (!comer.isNull("puntoventancr")) {
                        comercializadora.setPvNcr(comer.getString("puntoventancr"));
                    }
                    if (!comer.isNull("esagenteretencion")) {
                        comercializadora.setEsAgRetencion(comer.getBoolean("esagenteretencion"));
                    }

                    if (!comer.isNull("obligadocontabilidad")) {
                        if (comer.getString("obligadocontabilidad").equals("SI")) {
                            comercializadora.setObContabilidad(true);
                        } else {
                            comercializadora.setObContabilidad(false);
                        }
                    }
                    if (!comer.isNull("leyendaagenteretencion")) {
                        comercializadora.setLeyendaAgRetencion(comer.getString("leyendaagenteretencion"));
                    }
                    comercializadora.setUsuario(comer.getString("usuarioactual"));
                    comercializadora.setPrefijoNpe(comer.getString("prefijonpe"));
                    comercializadora.setClaveWsepp(comer.getString("clavewsepp"));
                    comercializadora.setAmbienteSri(comer.getString("ambientesri"));
                    comercializadora.setTipoEmision(comer.getString("tipoemision"));
                    comercializadora.setObjRelacionado(comer.getString("codigo") + " - " + comer.getString("nombre"));
                    comercializadora.setAbastecedora(abas.getString("codigo"));
                    listaComercializadora.add(comercializadora);
                    comercializadora = new ComercializadoraBean();
                }

            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaComercializadora;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaComercializadora;
    }

    public List<ComercializadoraBean> obtenerComercializadorasActivas() {

        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.comercializadora");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadora");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaComercializadora = new ArrayList<>();
            comercializadora = new ComercializadoraBean();
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
                if (!retorno.isNull(indice)) {
                    JSONObject comer = retorno.getJSONObject(indice);
                    JSONObject abas = comer.getJSONObject("codigoabastecedora");
                    JSONObject codBanco = comer.getJSONObject("codigobancodebito");
                    comercializadora.setCodigo(comer.getString("codigo"));
                    comercializadora.setNombre(comer.getString("nombre"));
                    if (comer.getBoolean("activo") == true) {
                        comercializadora.setActivo("S");
                    } else {
                        comercializadora.setActivo("N");
                    }
                    comercializadora.setCodigoARCH(comer.getString("codigoarch"));
                    comercializadora.setCodigoSTC(comer.getString("codigostc"));
                    comercializadora.setClaveSTC(comer.getString("clavestc"));
                    comercializadora.setCodigoAbas(abas.getString("codigo"));
                    comercializadora.setRuc(comer.getString("ruc"));
                    if (!comer.isNull("telefono1")) {
                        comercializadora.setTel1(comer.getString("telefono1"));
                    }
                    if (!comer.isNull("nombrecorto")) {
                        comercializadora.setNombreCorto(comer.getString("nombrecorto"));
                    }
                    if (!comer.isNull("direccion")) {
                        comercializadora.setDireccion(comer.getString("direccion"));
                    }
                    if (!comer.isNull("identificacionrepresentantelega")) {
                        comercializadora.setIdRepLegal(comer.getString("identificacionrepresentantelega"));
                    }
                    if (!comer.isNull("nombrerepresentantelegal")) {
                        comercializadora.setNomRepLegal(comer.getString("nombrerepresentantelegal"));
                    }
                    if (!comer.isNull("escontribuyenteespacial")) {
                        comercializadora.setEsContriEspecial(comer.getString("escontribuyenteespacial"));
                    }
                    if (!comer.isNull("telefono2")) {
                        comercializadora.setTel2(comer.getString("telefono2"));
                    }
                    if (!comer.isNull("correo1")) {
                        comercializadora.setCorreo1(comer.getString("correo1"));
                    }
                    if (!comer.isNull("correo2")) {
                        comercializadora.setCorre2(comer.getString("correo2"));
                    }
                    if (!comer.isNull("tipoplazocredito")) {
                        comercializadora.setTipoPlaCred(comer.getString("tipoplazocredito"));
                    }
                    if (!comer.isNull("diasplazocredito")) {
                        Long diasPlazo = comer.getLong("diasplazocredito");
                        String sDiasPlazo = diasPlazo.toString();
                        comercializadora.setDiasPlaCred(sDiasPlazo);
                    }
                    if (!codBanco.isNull("codigo")) {
                        comercializadora.setCodigoBancDeb(codBanco.getString("codigo"));
                    }
                    if (!comer.isNull("cuentadebito")) {
                        comercializadora.setCuentaDeb(comer.getString("cuentadebito"));
                    }
                    if (!comer.isNull("tipocuentadebito")) {
                        comercializadora.setTipoCuenDeb(comer.getString("tipocuentadebito"));
                    }
                    if (!comer.isNull("tasainteres")) {
                        Long tasa = comer.getLong("tasainteres");
                        String sTasa = tasa.toString();
                        comercializadora.setTasaInteres(sTasa);
                    }
                    if (!comer.isNull("fehainiciocontrato")) {
                        Long lDateIni = comer.getLong("fehainiciocontrato");
                        Date dateIni = new Date(lDateIni);
                        comercializadora.setFechaIniCont(dateIni);
                    } else {
                        comercializadora.setFechaIniCont(new Date());
                    }
                    if (!comer.isNull("fechavencimientocontr")) {
                        Long lDateFin = comer.getLong("fechavencimientocontr");
                        Date dateFin = new Date(lDateFin);
                        comercializadora.setFechaVencCont(dateFin);
                    } else {
                        comercializadora.setFechaVencCont(new Date());
                    }
                    if (!comer.isNull("establecimientofac")) {
                        comercializadora.setEstabFac(comer.getString("establecimientofac"));
                    }
                    if (!comer.isNull("puntoventafac")) {
                        comercializadora.setPvFac(comer.getString("puntoventafac"));
                    }
                    if (!comer.isNull("establecimientondb")) {
                        comercializadora.setEstabNdb(comer.getString("establecimientondb"));
                    }
                    if (!comer.isNull("puntoventandb")) {
                        comercializadora.setPvNdb(comer.getString("puntoventandb"));
                    }
                    if (!comer.isNull("establecimientoncr")) {
                        comercializadora.setEstabNcr(comer.getString("establecimientoncr"));
                    }
                    if (!comer.isNull("puntoventancr")) {
                        comercializadora.setPvNcr(comer.getString("puntoventancr"));
                    }
                    if (!comer.isNull("esagenteretencion")) {
                        comercializadora.setEsAgRetencion(comer.getBoolean("esagenteretencion"));
                    }

                    if (!comer.isNull("obligadocontabilidad")) {
                        if (comer.getString("obligadocontabilidad").equals("SI")) {
                            comercializadora.setObContabilidad(true);
                        } else {
                            comercializadora.setObContabilidad(false);
                        }
                    }
                    if (!comer.isNull("leyendaagenteretencion")) {
                        comercializadora.setLeyendaAgRetencion(comer.getString("leyendaagenteretencion"));
                    }
                    comercializadora.setUsuario(comer.getString("usuarioactual"));
                    comercializadora.setPrefijoNpe(comer.getString("prefijonpe"));
                    comercializadora.setClaveWsepp(comer.getString("clavewsepp"));
                    comercializadora.setAmbienteSri(comer.getString("ambientesri"));
                    comercializadora.setTipoEmision(comer.getString("tipoemision"));
                    comercializadora.setObjRelacionado(comer.getString("codigo") + " - " + comer.getString("nombre"));
                    comercializadora.setAbastecedora(abas.getString("codigo"));
                    if (comer.getBoolean("activo") == true) {
                        listaComercializadora.add(comercializadora);
                    }                    
                    comercializadora = new ComercializadoraBean();
                }

            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaComercializadora;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaComercializadora;
    }

    public List<ComercializadoraBean> obtenerComercializadoraId(String codigo) {

        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.comercializadora");
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadora/porId?codigo=" + codigo);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaComercializadora = new ArrayList<>();
            comercializadora = new ComercializadoraBean();
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
                if (!retorno.isNull(indice)) {
                    JSONObject comer = retorno.getJSONObject(indice);
                    JSONObject abas = comer.getJSONObject("codigoabastecedora");
                    JSONObject codBanco = comer.getJSONObject("codigobancodebito");
                    comercializadora.setCodigo(comer.getString("codigo"));
                    comercializadora.setNombre(comer.getString("nombre"));
                    if (comer.getBoolean("activo") == true) {
                        comercializadora.setActivo("S");
                    } else {
                        comercializadora.setActivo("N");
                    }
                    comercializadora.setCodigoARCH(comer.getString("codigoarch"));
                    comercializadora.setCodigoSTC(comer.getString("codigostc"));
                    comercializadora.setClaveSTC(comer.getString("clavestc"));
                    comercializadora.setCodigoAbas(abas.getString("codigo"));
                    comercializadora.setRuc(comer.getString("ruc"));
                    if (!comer.isNull("telefono1")) {
                        comercializadora.setTel1(comer.getString("telefono1"));
                    }
                    if (!comer.isNull("nombrecorto")) {
                        comercializadora.setNombreCorto(comer.getString("nombrecorto"));
                    }
                    if (!comer.isNull("direccion")) {
                        comercializadora.setDireccion(comer.getString("direccion"));
                    }
                    if (!comer.isNull("identificacionrepresentantelega")) {
                        comercializadora.setIdRepLegal(comer.getString("identificacionrepresentantelega"));
                    }
                    if (!comer.isNull("nombrerepresentantelegal")) {
                        comercializadora.setNomRepLegal(comer.getString("nombrerepresentantelegal"));
                    }
                    if (!comer.isNull("escontribuyenteespacial")) {
                        comercializadora.setEsContriEspecial(comer.getString("escontribuyenteespacial"));
                    }
                    if (!comer.isNull("telefono2")) {
                        comercializadora.setTel2(comer.getString("telefono2"));
                    }
                    if (!comer.isNull("correo1")) {
                        comercializadora.setCorreo1(comer.getString("correo1"));
                    }
                    if (!comer.isNull("correo2")) {
                        comercializadora.setCorre2(comer.getString("correo2"));
                    }
                    if (!comer.isNull("tipoplazocredito")) {
                        comercializadora.setTipoPlaCred(comer.getString("tipoplazocredito"));
                    }
                    if (!comer.isNull("diasplazocredito")) {
                        Long diasPlazo = comer.getLong("diasplazocredito");
                        String sDiasPlazo = diasPlazo.toString();
                        comercializadora.setDiasPlaCred(sDiasPlazo);
                    }
                    if (!codBanco.isNull("codigo")) {
                        comercializadora.setCodigoBancDeb(codBanco.getString("codigo"));
                    }
                    if (!comer.isNull("cuentadebito")) {
                        comercializadora.setCuentaDeb(comer.getString("cuentadebito"));
                    }
                    if (!comer.isNull("tipocuentadebito")) {
                        comercializadora.setTipoCuenDeb(comer.getString("tipocuentadebito"));
                    }
                    if (!comer.isNull("tasainteres")) {
                        Long tasa = comer.getLong("tasainteres");
                        String sTasa = tasa.toString();
                        comercializadora.setTasaInteres(sTasa);
                    }
                    if (!comer.isNull("fehainiciocontrato")) {
                        Long lDateIni = comer.getLong("fehainiciocontrato");
                        Date dateIni = new Date(lDateIni);
                        comercializadora.setFechaIniCont(dateIni);
                    } else {
                        comercializadora.setFechaIniCont(new Date());
                    }
                    if (!comer.isNull("fechavencimientocontr")) {
                        Long lDateFin = comer.getLong("fechavencimientocontr");
                        Date dateFin = new Date(lDateFin);
                        comercializadora.setFechaVencCont(dateFin);
                    } else {
                        comercializadora.setFechaVencCont(new Date());
                    }
                    if (!comer.isNull("establecimientofac")) {
                        comercializadora.setEstabFac(comer.getString("establecimientofac"));
                    }
                    if (!comer.isNull("puntoventafac")) {
                        comercializadora.setPvFac(comer.getString("puntoventafac"));
                    }
                    if (!comer.isNull("establecimientondb")) {
                        comercializadora.setEstabNdb(comer.getString("establecimientondb"));
                    }
                    if (!comer.isNull("puntoventandb")) {
                        comercializadora.setPvNdb(comer.getString("puntoventandb"));
                    }
                    if (!comer.isNull("establecimientoncr")) {
                        comercializadora.setEstabNcr(comer.getString("establecimientoncr"));
                    }
                    if (!comer.isNull("puntoventancr")) {
                        comercializadora.setPvNcr(comer.getString("puntoventancr"));
                    }
                    if (!comer.isNull("esagenteretencion")) {
                        comercializadora.setEsAgRetencion(comer.getBoolean("esagenteretencion"));
                    }

                    if (!comer.isNull("obligadocontabilidad")) {
                        if (comer.getString("obligadocontabilidad").equals("SI")) {
                            comercializadora.setObContabilidad(true);
                        } else {
                            comercializadora.setObContabilidad(false);
                        }
                    }
                    if (!comer.isNull("leyendaagenteretencion")) {
                        comercializadora.setLeyendaAgRetencion(comer.getString("leyendaagenteretencion"));
                    }
                    comercializadora.setUsuario(comer.getString("usuarioactual"));
                    comercializadora.setPrefijoNpe(comer.getString("prefijonpe"));
                    comercializadora.setClaveWsepp(comer.getString("clavewsepp"));
                    comercializadora.setAmbienteSri(comer.getString("ambientesri"));
                    comercializadora.setTipoEmision(comer.getString("tipoemision"));
                    comercializadora.setObjRelacionado(comer.getString("codigo") + " - " + comer.getString("nombre"));
                    comercializadora.setAbastecedora(abas.getString("codigo"));
                    listaComercializadora.add(comercializadora);
                    comercializadora = new ComercializadoraBean();
                }

            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

            return listaComercializadora;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaComercializadora;
    }

    public ComercializadoraBean obtenerComercializadora(String codigo) {
        try {
            //URL url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.comercializadora/porId?codigo=" + codigo);
            URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.comercializadora/porId?codigo=" + codigo);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaComercializadora = new ArrayList<>();
            comercializadora = new ComercializadoraBean();
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
                if (!retorno.isNull(indice)) {
                    JSONObject comer = retorno.getJSONObject(indice);
                    JSONObject abas = comer.getJSONObject("codigoabastecedora");
                    JSONObject codBanco = comer.getJSONObject("codigobancodebito");
                    comercializadora.setCodigo(comer.getString("codigo"));
                    comercializadora.setNombre(comer.getString("nombre"));
                    if (comer.getBoolean("activo") == true) {
                        comercializadora.setActivo("S");
                    } else {
                        comercializadora.setActivo("N");
                    }
                    comercializadora.setCodigoARCH(comer.getString("codigoarch"));
                    comercializadora.setCodigoSTC(comer.getString("codigostc"));
                    comercializadora.setClaveSTC(comer.getString("clavestc"));
                    comercializadora.setCodigoAbas(abas.getString("codigo"));
                    comercializadora.setRuc(comer.getString("ruc"));
                    if (!comer.isNull("telefono1")) {
                        comercializadora.setTel1(comer.getString("telefono1"));
                    }
                    if (!comer.isNull("nombrecorto")) {
                        comercializadora.setNombreCorto(comer.getString("nombrecorto"));
                    }
                    if (!comer.isNull("direccion")) {
                        comercializadora.setDireccion(comer.getString("direccion"));
                    }
                    if (!comer.isNull("identificacionrepresentantelega")) {
                        comercializadora.setIdRepLegal(comer.getString("identificacionrepresentantelega"));
                    }
                    if (!comer.isNull("nombrerepresentantelegal")) {
                        comercializadora.setNomRepLegal(comer.getString("nombrerepresentantelegal"));
                    }
                    if (!comer.isNull("escontribuyenteespacial")) {
                        comercializadora.setEsContriEspecial(comer.getString("escontribuyenteespacial"));
                    }
                    if (!comer.isNull("telefono2")) {
                        comercializadora.setTel2(comer.getString("telefono2"));
                    }
                    if (!comer.isNull("correo1")) {
                        comercializadora.setCorreo1(comer.getString("correo1"));
                    }
                    if (!comer.isNull("correo2")) {
                        comercializadora.setCorre2(comer.getString("correo2"));
                    }
                    if (!comer.isNull("tipoplazocredito")) {
                        comercializadora.setTipoPlaCred(comer.getString("tipoplazocredito"));
                    }
                    if (!comer.isNull("diasplazocredito")) {
                        Long diasPlazo = comer.getLong("diasplazocredito");
                        String sDiasPlazo = diasPlazo.toString();
                        comercializadora.setDiasPlaCred(sDiasPlazo);
                    }
                    if (!codBanco.isNull("codigo")) {
                        comercializadora.setCodigoBancDeb(codBanco.getString("codigo"));
                    }
                    if (!comer.isNull("cuentadebito")) {
                        comercializadora.setCuentaDeb(comer.getString("cuentadebito"));
                    }
                    if (!comer.isNull("tipocuentadebito")) {
                        comercializadora.setTipoCuenDeb(comer.getString("tipocuentadebito"));
                    }
                    if (!comer.isNull("tasainteres")) {
                        Long tasa = comer.getLong("tasainteres");
                        String sTasa = tasa.toString();
                        comercializadora.setTasaInteres(sTasa);
                    }
                    if (!comer.isNull("fehainiciocontrato")) {
                        Long lDateIni = comer.getLong("fehainiciocontrato");
                        Date dateIni = new Date(lDateIni);
                        comercializadora.setFechaIniCont(dateIni);
                    } else {
                        comercializadora.setFechaIniCont(new Date());
                    }
                    if (!comer.isNull("fechavencimientocontr")) {
                        Long lDateFin = comer.getLong("fechavencimientocontr");
                        Date dateFin = new Date(lDateFin);
                        comercializadora.setFechaVencCont(dateFin);
                    } else {
                        comercializadora.setFechaVencCont(new Date());
                    }
                    if (!comer.isNull("establecimientofac")) {
                        comercializadora.setEstabFac(comer.getString("establecimientofac"));
                    }
                    if (!comer.isNull("puntoventafac")) {
                        comercializadora.setPvFac(comer.getString("puntoventafac"));
                    }
                    if (!comer.isNull("establecimientondb")) {
                        comercializadora.setEstabNdb(comer.getString("establecimientondb"));
                    }
                    if (!comer.isNull("puntoventandb")) {
                        comercializadora.setPvNdb(comer.getString("puntoventandb"));
                    }
                    if (!comer.isNull("establecimientoncr")) {
                        comercializadora.setEstabNcr(comer.getString("establecimientoncr"));
                    }
                    if (!comer.isNull("puntoventancr")) {
                        comercializadora.setPvNcr(comer.getString("puntoventancr"));
                    }
                    if (!comer.isNull("esagenteretencion")) {
                        comercializadora.setEsAgRetencion(comer.getBoolean("esagenteretencion"));
                    }

                    if (!comer.isNull("obligadocontabilidad")) {
                        if (comer.getString("obligadocontabilidad").equals("SI")) {
                            comercializadora.setObContabilidad(true);
                        } else {
                            comercializadora.setObContabilidad(false);
                        }
                    }
                    if (!comer.isNull("leyendaagenteretencion")) {
                        comercializadora.setLeyendaAgRetencion(comer.getString("leyendaagenteretencion"));
                    }
                    comercializadora.setUsuario(comer.getString("usuarioactual"));
                    comercializadora.setPrefijoNpe(comer.getString("prefijonpe"));
                    comercializadora.setClaveWsepp(comer.getString("clavewsepp"));
                    comercializadora.setAmbienteSri(comer.getString("ambientesri"));
                    comercializadora.setTipoEmision(comer.getString("tipoemision"));
                    comercializadora.setObjRelacionado(comer.getString("codigo") + " - " + comer.getString("nombre"));
                    comercializadora.setAbastecedora(abas.getString("codigo"));
                }
            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
            return comercializadora;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comercializadora;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

}
