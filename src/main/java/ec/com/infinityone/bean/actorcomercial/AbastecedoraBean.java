/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.actorcomercial;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.ObjetoNivel1;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
 * @author Andres
 */
@Named
@ViewScoped
public class AbastecedoraBean extends ReusableBean implements Serializable {

    /*
    Variable que almacena varios Menus
     */
    private List<AbastecedoraBean> listaAbastecedoraBean;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarAbastecedora;
    /*
    Variable que establece true or false para el estado del Abastecedora
     */
    private boolean estadoMenu;
    /*
    Variable que establece true or false para la activacion del menu padre
     */
    private boolean activarMenuP;
    /*
    Variable almacena url Accion
     */
    private String urlAccion;
    /*
    Variable almacena menu padre
     */
    private String menuPadre;
    /*
    Variable almacena codigo STC
     */
    private String codigoSTC;
    /*
    Variable almacena codigo ARCH
     */
    private String codigoARCH;
    /*
    Variable almacena clave STC
     */
    private String claveSTC;
    /*
    Variable almacena RUC
     */
    private String ruc;
    /*
    Variable almacena direccion
     */
    private String direccion;
    /*
    Variable almacena id representante legal
     */
    private String idRepLegal;
    /*
    Variable almacena nombre representante legal
     */
    private String nomRepLegal;
    /*
    Variable almacena contribuyente especial
     */
    private String esContriEspecial;
    /*
    Variable almacena telefono 1
     */
    private String tel1;
    /*
    Variable almacena telefono 2
     */
    private String tel2;
    /*
    Variable almacena correo1
     */
    private String correo1;
    /*
    Variable almacena correo2
     */
    private String corre2;

    private String nivel;

    private JSONObject codAm;

    private boolean permiteEdit;
    private boolean estadoAbastecedora;
    private boolean estadoContribuyente;

    private ObjetoNivel1 objeto1;

    private AbastecedoraBean abastecedora;

    /**
     * Constructor por defecto
     */
    public AbastecedoraBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.abastecedora";
        editarAbastecedora = false;
        objeto = new ObjetoNivel1();
        abastecedora = new AbastecedoraBean();
        permiteEdit = false;
        activarMenuP = false;
        obtenerAbastecedora();
    }

    public void obtenerAbastecedora() {
        try {
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaAbastecedoraBean = new ArrayList<>();
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
                JSONObject abas = retorno.getJSONObject(indice);
                abastecedora.setCodigo(abas.getString("codigo"));
                abastecedora.setNombre(abas.getString("nombre"));
                if (abas.getBoolean("activo") == true) {
                    abastecedora.setActivo("S");
                } else {
                    abastecedora.setActivo("N");
                }
                abastecedora.setCodigoARCH(abas.getString("codigoarch"));
                abastecedora.setCodigoSTC(abas.getString("codigostc"));
                abastecedora.setClaveSTC(abas.getString("clavestc"));
                abastecedora.setRuc(abas.getString("ruc"));
                if (!abas.isNull("direccion")) {
                    abastecedora.setDireccion(abas.getString("direccion"));
                }
                if (!abas.isNull("identificacionrepresentantelega")) {
                    abastecedora.setIdRepLegal(abas.getString("identificacionrepresentantelega"));
                }
                if (!abas.isNull("nombrerepresentantelegal")) {
                    abastecedora.setNomRepLegal(abas.getString("nombrerepresentantelegal"));
                }
                if (!abas.isNull("escontribuyenteespacial")) {
                    if (abas.getBoolean("escontribuyenteespacial") == true) {
                        abastecedora.setEsContriEspecial("S");
                    } else {
                        abastecedora.setEsContriEspecial("N");
                    }
                }
                if (!abas.isNull("telefono1")) {
                    abastecedora.setTel1(abas.getString("telefono1"));
                }
                if (!abas.isNull("telefono2")) {
                    abastecedora.setTel2(abas.getString("telefono2"));
                }
                if (!abas.isNull("correo1")) {
                    abastecedora.setCorreo1(abas.getString("correo1"));
                }
                if (!abas.isNull("correo2")) {
                    abastecedora.setCorre2(abas.getString("correo2"));
                }
                listaAbastecedoraBean.add(abastecedora);
                abastecedora = new AbastecedoraBean();
                if (connection.getResponseCode() != 200) {
                    System.out.println(connection.getResponseCode());
                    System.out.println(connection.getResponseMessage());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (editarAbastecedora) {
            editItems();
            obtenerAbastecedora();
        } else {
            addItems();
            obtenerAbastecedora();
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
            obj.put("codigo", abastecedora.getCodigo());
            obj.put("nombre", abastecedora.getNombre());
            obj.put("activo", estadoAbastecedora);
            obj.put("codigoarch", abastecedora.getCodigoARCH());
            obj.put("codigostc", abastecedora.getCodigoSTC());
            obj.put("clavestc", abastecedora.getClaveSTC());
            obj.put("ruc", abastecedora.getRuc());
            obj.put("direccion", abastecedora.getDireccion());
            obj.put("identificacionrepresentantelega", abastecedora.getIdRepLegal());
            obj.put("nombrerepresentantelegal", abastecedora.getNomRepLegal());
            obj.put("escontribuyenteespacial", estadoContribuyente);
            obj.put("telefono1", abastecedora.getTel1());
            obj.put("telefono2", abastecedora.getTel2());
            obj.put("correo1", abastecedora.getCorreo1());
            obj.put("correo2", abastecedora.getCorre2());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            //respuesta = "{\"nombre\":\"asdfg\",\"codigo\":\"21\",\"codigoareamercadeo\":{\"codigo\": \"01\"},\"codigostc\":\"453\",\"codigoarch\":\"456\",\"usuarioactual\":\"ft\"}";
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "ABASTECEDORA REGISTRADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
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
            obj.put("codigo", abastecedora.getCodigo());
            obj.put("nombre", abastecedora.getNombre());
            obj.put("activo", estadoAbastecedora);
            obj.put("codigoarch", abastecedora.getCodigoARCH());
            obj.put("codigostc", abastecedora.getCodigoSTC());
            obj.put("clavestc", abastecedora.getClaveSTC());
            obj.put("ruc", abastecedora.getRuc());
            obj.put("direccion", abastecedora.getDireccion());
            obj.put("identificacionrepresentantelega", abastecedora.getIdRepLegal());
            obj.put("nombrerepresentantelegal", abastecedora.getNomRepLegal());
            obj.put("escontribuyenteespacial", estadoContribuyente);
            obj.put("telefono1", abastecedora.getTel1());
            obj.put("telefono2", abastecedora.getTel2());
            obj.put("correo1", abastecedora.getCorreo1());
            obj.put("correo2", abastecedora.getCorre2());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "ABASTECEDORA ACUTALIZADA EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteItems() {
        try {
            String respuesta;
            url = new URL(direccion + "/porId?codigo=" + abastecedora.getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "ABASTECEDORA ELIMINADA EXITOSAMENTE");
                obtenerAbastecedora();
            } else {
                this.dialogo(FacesMessage.SEVERITY_INFO, "ERROR AL ELIMINAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevaAbastecedora() {
        estadoAbastecedora = true;
        estadoContribuyente = false;
        editarAbastecedora = false;
        permiteEdit = false;
        soloLectura = false;
        objeto1 = new ObjetoNivel1();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public AbastecedoraBean editarAbastecedora(AbastecedoraBean obj) {
        editarAbastecedora = true;
        abastecedora = obj;
        permiteEdit = true;
        soloLectura = true;
        if (abastecedora != null) {
            if ("S".equals(abastecedora.getActivo())) {
                estadoAbastecedora = true;
            } else {
                estadoAbastecedora = false;
            }
            if ("S".equals(abastecedora.getEsContriEspecial())) {
                estadoContribuyente = true;
            } else {
                estadoContribuyente = false;
            }
        }
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return abastecedora;
    }

    public boolean isEditarAbastecedora() {
        return editarAbastecedora;
    }

    public void setEditarAbastecedora(boolean editarAbastecedora) {
        this.editarAbastecedora = editarAbastecedora;
    }

    public boolean isEstadoMenu() {
        return estadoMenu;
    }

    public void setEstadoMenu(boolean estadoMenu) {
        this.estadoMenu = estadoMenu;
    }

    public String getUrlAccion() {
        return urlAccion;
    }

    public void setUrlAccion(String urlAccion) {
        this.urlAccion = urlAccion;
    }

    public String getMenuPadre() {
        return menuPadre;
    }

    public void setMenuPadre(String menuPadre) {
        this.menuPadre = menuPadre;
    }

    public boolean isPermiteEdit() {
        return permiteEdit;
    }

    public void setPermiteEdit(boolean permiteEdit) {
        this.permiteEdit = permiteEdit;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public ObjetoNivel1 getObjeto1() {
        return objeto1;
    }

    public void setObjeto1(ObjetoNivel1 objeto1) {
        this.objeto1 = objeto1;
    }

    public boolean isActivarMenuP() {
        return activarMenuP;
    }

    public void setActivarMenuP(boolean activarMenuP) {
        this.activarMenuP = activarMenuP;
    }

    public List<AbastecedoraBean> getListaAbastecedoraBean() {
        return listaAbastecedoraBean;
    }

    public void setListaAbastecedoraBean(List<AbastecedoraBean> listaAbastecedoraBean) {
        this.listaAbastecedoraBean = listaAbastecedoraBean;
    }

    public AbastecedoraBean getAbastecedora() {
        return abastecedora;
    }

    public void setAbastecedora(AbastecedoraBean abastecedora) {
        this.abastecedora = abastecedora;
    }

    public String getCodigoSTC() {
        return codigoSTC;
    }

    public void setCodigoSTC(String codigoSTC) {
        this.codigoSTC = codigoSTC;
    }

    public String getCodigoARCH() {
        return codigoARCH;
    }

    public void setCodigoARCH(String codigoARCH) {
        this.codigoARCH = codigoARCH;
    }

    public String getClaveSTC() {
        return claveSTC;
    }

    public void setClaveSTC(String claveSTC) {
        this.claveSTC = claveSTC;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getIdRepLegal() {
        return idRepLegal;
    }

    public void setIdRepLegal(String idRepLegal) {
        this.idRepLegal = idRepLegal;
    }

    public String getNomRepLegal() {
        return nomRepLegal;
    }

    public void setNomRepLegal(String nomRepLegal) {
        this.nomRepLegal = nomRepLegal;
    }

    public String getEsContriEspecial() {
        return esContriEspecial;
    }

    public void setEsContriEspecial(String esContriEspecial) {
        this.esContriEspecial = esContriEspecial;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getCorreo1() {
        return correo1;
    }

    public void setCorreo1(String correo1) {
        this.correo1 = correo1;
    }

    public String getCorre2() {
        return corre2;
    }

    public void setCorre2(String corre2) {
        this.corre2 = corre2;
    }

    public boolean isEstadoAbastecedora() {
        return estadoAbastecedora;
    }

    public void setEstadoAbastecedora(boolean estadoAbastecedora) {
        this.estadoAbastecedora = estadoAbastecedora;
    }

    public boolean isEstadoContribuyente() {
        return estadoContribuyente;
    }

    public void setEstadoContribuyente(boolean estadoContribuyente) {
        this.estadoContribuyente = estadoContribuyente;
    }

}
