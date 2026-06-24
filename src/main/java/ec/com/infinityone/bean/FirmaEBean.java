/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.FirmaE;
import ec.com.infinityone.modelo.ObjetoNivel1;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author David
 */
@Named
@ViewScoped
public class FirmaEBean extends ReusableBean implements Serializable {

    /*
    Variable que almacena varios Bancos
     */
    private List<FirmaE> listaFirmaE;

    private FirmaE firmaE;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarFirma;

    /**
     * Constructor por defecto
     */
    public FirmaEBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.banco";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.firmae";
        editarFirma = false;
        objeto = new ObjetoNivel1();
        obtenerFirma();
        //getURL();
    }

    public void obtenerFirma() {
        try {
            url = new URL(direccion);
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaFirmaE = new ArrayList<>();
            firmaE = new FirmaE();
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
                JSONObject firm = retorno.getJSONObject(indice);
                firmaE.setFir_codigo(firm.getLong("firCodigo"));
                firmaE.setEmp_codigoempresa(firm.getString("empCodigoempresa"));
                firmaE.setFir_tipo(firm.getInt("firTipo"));
                firmaE.setFir_descripcion(firm.getString("firDescripcion"));
                firmaE.setFir_dirgenerada(firm.getString("firDirgenerada"));
                firmaE.setFir_dirfirmada(firm.getString("firDirfirmada"));
                firmaE.setFir_dirautorizada(firm.getString("firDirautorizada"));
                firmaE.setFir_dirrechazada(firm.getString("firDirrechazada"));
                firmaE.setFir_clave(firm.getString("firClave"));
                firmaE.setFir_estado(firm.getString("firEstado"));
                Long lFechaCad = firm.getLong("firFechacaduca");
                Date fechaCad = new Date(lFechaCad);
                firmaE.setFir_fechacaduca(date.format(fechaCad));
                firmaE.setFir_usuariocrea(firm.getString("firUsuariocrea"));
                Long lFechaCre = firm.getLong("firFechacrea");
                Date fechaCre = new Date(lFechaCre);
                firmaE.setFir_fechacrea(date.format(fechaCre));
                firmaE.setFir_ipcrea(firm.getString("firIpcrea"));
                firmaE.setFir_timer(firm.getInt("firTimer"));
                firmaE.setFir_correoalertas(firm.getString("firCorreoalertas"));
                listaFirmaE.add(firmaE);
                firmaE = new FirmaE();
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
        if (editarFirma) {
            editItems();
            obtenerFirma();
        } else {
            addItems();
            obtenerFirma();
        }
    }

    public void addItems() {
        try {
            String respuesta;
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("firCodigo", firmaE.getFir_codigo());
            obj.put("empCodigoempresa", firmaE.getEmp_codigoempresa());
            obj.put("firTipo", firmaE.getFir_tipo());
            obj.put("firDescripcion", firmaE.getFir_descripcion());
            obj.put("firUbicacion", firmaE.getFir_ubicacion());
            obj.put("firDirgenerada", firmaE.getFir_dirgenerada());
            obj.put("firDirfirmada", firmaE.getFir_dirfirmada());
            obj.put("firDirautorizada", firmaE.getFir_dirautorizada());
            obj.put("firDirrechazada", firmaE.getFir_dirrechazada());
            obj.put("firClave", firmaE.getFir_clave());
            obj.put("firEstado", firmaE.getFir_estado());
            obj.put("firFechacaduca", firmaE.getFir_fechacaduca());
            obj.put("firUsuariocrea", dataUser.getUser().getNombrever());
            obj.put("firFechacrea", firmaE.getFir_fechacrea());
            obj.put("firIpcrea", firmaE.getFir_ipcrea());
            obj.put("firTimer", firmaE.getFir_timer());
            obj.put("firCorreoalertas", firmaE.getFir_correoalertas());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "FIRMAE REGISTRADO EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editItems() {
        try {
            String respuesta;
            url = new URL(direccion + "/porId");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("firCodigo", firmaE.getFir_codigo());
            obj.put("empCodigoempresa", firmaE.getEmp_codigoempresa());
            obj.put("firTipo", firmaE.getFir_tipo());
            obj.put("firDescripcion", firmaE.getFir_descripcion());
            obj.put("firUbicacion", firmaE.getFir_ubicacion());
            obj.put("firDirgenerada", firmaE.getFir_dirgenerada());
            obj.put("firDirfirmada", firmaE.getFir_dirfirmada());
            obj.put("firDirautorizada", firmaE.getFir_dirautorizada());
            obj.put("firDirrechazada", firmaE.getFir_dirrechazada());
            obj.put("firClave", firmaE.getFir_clave());
            obj.put("firEstado", firmaE.getFir_estado());
            obj.put("firFechacaduca", firmaE.getFir_fechacaduca());
            obj.put("firUsuariocrea", dataUser.getUser().getNombrever());
            obj.put("firFechacrea", firmaE.getFir_fechacrea());
            obj.put("firIpcrea", firmaE.getFir_ipcrea());
            obj.put("firTimer", firmaE.getFir_timer());
            obj.put("firCorreoalertas", firmaE.getFir_correoalertas());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "FIRMAE ACUTALIZADO EXITOSAMENTE");
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public void deleteItems() {
        try {
            String respuesta;
            url = new URL(direccion + "/porId?codigo=" + objeto.getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("codigo", objeto.getCodigo());
            obj.put("nombre", objeto.getNombre());
            obj.put("activo", estadoBanco);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "BANCO ELIMINADO EXITOSAMENTE");
                obtenerBanco();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void nuevoFirma() {        
        editarFirma = false;
        objeto = new ObjetoNivel1();
        soloLectura = false;
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public FirmaE editarFirma(FirmaE obj) {
        firmaE = obj;
        soloLectura = true;       
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return firmaE;
    }

    public List<FirmaE> getListaFirmaE() {
        return listaFirmaE;
    }

    public void setListaFirmaE(List<FirmaE> listaFirmaE) {
        this.listaFirmaE = listaFirmaE;
    }

    public FirmaE getFirmaE() {
        return firmaE;
    }

    public void setFirmaE(FirmaE firmaE) {
        this.firmaE = firmaE;
    }

    public boolean isEditarFirma() {
        return editarFirma;
    }

    public void setEditarFirma(boolean editarFirma) {
        this.editarFirma = editarFirma;
    }

    

}
