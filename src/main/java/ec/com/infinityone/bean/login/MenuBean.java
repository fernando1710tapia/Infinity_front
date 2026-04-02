/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.login;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.FirmaE;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class MenuBean extends LoginBean implements Serializable {

    protected MenuModel model = new DefaultMenuModel();
    /*
    Variable que almacena varios Productos
     */
    private List<MenuBean> listaMenuPadreBean;
    /*
    Variable que almacena varios Productos
     */
    private List<MenuBean> listaMenuHijoBean;

    private List<MenuBean> listaMenuPermiso;

    private List<MenuBean> listaMenuPermisoPa;

    private List<MenuBean> listaMenuPermisoHi;

    /*
    Variable que almacena varios Productos
     */
    private List<MenuBean> listaMenuHijoAux;

    private MenuBean menu;

    private FirmaE firmae;
    /*
    Variable almacena url Accion
     */
    private String urlAccion;
    /*
    Variable almacena codigo ARCH
     */
    private String menuPadre;

    private String nivel;
    private String urlAyuda;
    private String descripcionAyuda;

    /*
    Lista auxiliar para validar la existencia del menu padre
     */
    private List<MenuBean> listaMenuPAux;

    private String fechaC;

    private String rutaEConsulta;
    
    private String menuXeliminar;

    @PostConstruct
    public void init() {
        menu = new MenuBean();
        listaMenuPAux = new ArrayList<>();
        fechaC = Fichero.getFECHACERTIFICADOSSL();
        rutaEConsulta = Fichero.getRUTAECONSULTAS();
        menuXeliminar = Fichero.getPERMISOSXUSUARIO();
        
        if (dataUser.getUser() != null) {
            obtenerMenuPadre();
            obtenerMenuHijo();
        }
        
        cargarMenu();        
        
        if (dataUser.getUser() != null) {
            obtenerFirmae();
        }
    }

    public void obtenerMenuPadre() {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.menu/menus?nivel=AAA");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.menu/menus?nivel=AAA");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaMenuPadreBean = new ArrayList<>();
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
                JSONObject menuPa = retorno.getJSONObject(indice);
                menu.setCodigo(menuPa.getString("codigo"));
                menu.setNombre(menuPa.getString("nombre"));
                menu.setNivel(menuPa.getString("nivel"));
                if (!menuPa.isNull("menupadre")) {
                    menu.setMenuPadre(menuPa.getString("menupadre"));
                }
                if (!menuPa.isNull("urlaccion")) {
                    menu.setUrlAccion(menuPa.getString("urlaccion"));
                }
                if (!menuPa.isNull("urlayuda")) {
                    menu.setUrlAyuda(menuPa.getString("urlayuda"));
                }
                if (!menuPa.isNull("descripcionayuda")) {
                    menu.setDescripcionAyuda(menuPa.getString("descripcionayuda"));
                }
                listaMenuPadreBean.add(menu);
                menu = new MenuBean();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerMenuHijo() {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.menu/menus?nivel=CCC");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.menu/menus?nivel=CCC");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaMenuHijoBean = new ArrayList<>();
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
                JSONObject menuHi = retorno.getJSONObject(indice);
                menu.setCodigo(menuHi.getString("codigo"));
                menu.setNombre(menuHi.getString("nombre"));
                menu.setNivel(menuHi.getString("nivel"));
                if (!menuHi.isNull("urlayuda")) {
                    menu.setUrlAyuda(menuHi.getString("urlayuda"));
                }
                if (!menuHi.isNull("descripcionayuda")) {
                    menu.setDescripcionAyuda(menuHi.getString("descripcionayuda"));
                }
                if (!menuHi.isNull("menupadre")) {
                    menu.setMenuPadre(menuHi.getString("menupadre"));
                }
                if (!menuHi.isNull("urlaccion")) {
                    menu.setUrlAccion(menuHi.getString("urlaccion"));
                }
                listaMenuHijoBean.add(menu);
                menu = new MenuBean();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerMenuPermiso(String permiso) {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.permiso/accesos?niveloperacion=" + permiso);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.permiso/accesos?niveloperacion=" + permiso);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaMenuPermiso = new ArrayList<>();
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
                JSONObject menuHi = retorno.getJSONObject(indice);
                JSONObject menuJ = menuHi.getJSONObject("menu");

                menu.setCodigo(menuJ.getString("codigo"));
                menu.setNombre(menuJ.getString("nombre"));
                menu.setNivel(menuJ.getString("nivel"));
                if (!menuJ.isNull("menupadre")) {
                    menu.setMenuPadre(menuJ.getString("menupadre"));
                }
                if (!menuJ.isNull("urlaccion")) {
                    menu.setUrlAccion(menuJ.getString("urlaccion"));
                }
                listaMenuPermiso.add(menu);
                menu = new MenuBean();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void consultarporIdMenu(String codigo) {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.menu/porId?codigo=" + codigo);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.menu/porId?codigo=" + codigo);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            MenuBean menuPadre = new MenuBean();

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
                JSONObject menuPa = retorno.getJSONObject(indice);
                menuPadre.setCodigo(menuPa.getString("codigo"));
                menuPadre.setNombre(menuPa.getString("nombre"));
                menuPadre.setNivel(menuPa.getString("nivel"));

                listaMenuPAux.add(menuPadre);
                menuPadre = new MenuBean();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerFirmae() {
        DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.menu/menus?nivel=AAA");
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.firmae");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            firmae = new FirmaE();
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
                JSONObject fir = retorno.getJSONObject(indice);
                if (!fir.isNull("firFechacaduca")) {
                    Long lDatePro = fir.getLong("firFechacaduca");
                    Date datePro = new Date(lDatePro);
                    firmae.setFir_fechacaduca(date.format(datePro));
                }
                if (!fir.isNull("firFechacrea")) {
                    Long lDatePro = fir.getLong("firFechacrea");
                    Date datePro = new Date(lDatePro);
                    firmae.setFir_fechacrea(date.format(datePro));
                }
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void caragarRutaEConsulta() {
//        if (dataUser.getUser().getCodigocomercializadora() != null) {
//            switch (dataUser.getUser().getCodigocomercializadora()) {
//                case "0002":
//                    rutaEConsulta = Fichero.getRUTAECONSULTASPYS();
//                    break;
//                case "0095":
//                    rutaEConsulta = Fichero.getRUTAECONSULTASFENAPET();
//                    break;
//
//                default:
//                    rutaEConsulta = Fichero.getRUTAECONSULTASPYS();
//                    break;
//            }
//        } else {
//            rutaEConsulta = Fichero.getRUTAECONSULTASPYS();
//        }
    }

    public void cargarMenu() {
        DefaultSubMenu primerNivel;
        DefaultMenuItem item2;
        
        if (dataUser.getUser() == null) {
            // Menú básico para invitados/público
            DefaultMenuItem loginItem = DefaultMenuItem.builder()
                    .value("Módulo de Clientes")
                    .icon("pi pi-sign-in")
                    .outcome("/login.xhtml")
                    .build();
            model.getElements().add(loginItem);
            
            DefaultMenuItem soporteItemPublico = DefaultMenuItem.builder()
                    .value("Soporte")
                    .icon("pi pi-question-circle")
                    .outcome("/soporte.xhtml")
                    .build();
            model.getElements().add(soporteItemPublico);
            return; 
        }

        switch (dataUser.getUser().getNiveloperacion()) {
            case "cero":
                for (MenuBean menuP : listaMenuPadreBean) {
                    listaMenuHijoAux = new ArrayList<>();
                    primerNivel = DefaultSubMenu.builder()
                            .label(menuP.getNombre())
                            .icon(menuP.getUrlAccion())
                            .build();
                    //Carga el árbol de opciones
                    for (int i = 0; i < listaMenuHijoBean.size(); i++) {
                        if (menuP.getCodigo().equals(listaMenuHijoBean.get(i).getMenuPadre())) {
                            listaMenuHijoAux.add(listaMenuHijoBean.get(i));
                        }
                    }
                    for (MenuBean menuH : listaMenuHijoAux) {
                        item2 = DefaultMenuItem.builder()
                                .value(menuH.getNombre())
                                .outcome(menuH.getUrlAccion())
                                .build();
                        primerNivel.getElements().add(item2);
                    }
                    model.getElements().add(primerNivel);
                }
                break;
            case "adco":
                obtenerMenuPermiso(dataUser.getUser().getNiveloperacion());
                listaMenuPermisoHi = new ArrayList<>();
                listaMenuPermisoPa = new ArrayList<>();
                for (int i = 0; i < listaMenuPermiso.size(); i++) {
                    if (listaMenuPermiso.get(i).getNivel().equals("AAA")) {
                        listaMenuPermisoPa.add(listaMenuPermiso.get(i));
                    } else {
                        listaMenuPermisoHi.add(listaMenuPermiso.get(i));
                    }
                }
                for (MenuBean menuP : listaMenuPermisoPa) {
                    listaMenuHijoAux = new ArrayList<>();
                    primerNivel = DefaultSubMenu.builder()
                            .label(menuP.getNombre())
                            .icon(menuP.getUrlAccion())
                            .build();
                    //Carga el árbol de opciones
                    for (int i = 0; i < listaMenuPermisoHi.size(); i++) { 
                        if (!Fichero.getPERMISOSXUSUARIO().contains(dataUser.getUser().getCodigo()+"_"+listaMenuPermisoHi.get(i).getCodigo().trim()) ) {
                        if (menuP.getCodigo().equals(listaMenuPermisoHi.get(i).getMenuPadre())) {
                            listaMenuHijoAux.add(listaMenuPermisoHi.get(i));
                        }
                        }
                    }
                    for (MenuBean menuH : listaMenuHijoAux) {
                        item2 = DefaultMenuItem.builder()
                                .value(menuH.getNombre())
                                .outcome(menuH.getUrlAccion())
                                .build();
                        primerNivel.getElements().add(item2);
                    }
                    model.getElements().add(primerNivel);
                }
                break;
            case "usac":
                obtenerMenuPermiso(dataUser.getUser().getNiveloperacion());
                listaMenuPermisoHi = new ArrayList<>();
                listaMenuPermisoPa = new ArrayList<>();
                for (int i = 0; i < listaMenuPermiso.size(); i++) {
                    if (listaMenuPermiso.get(i).getNivel().equals("AAA")) {
                        listaMenuPermisoPa.add(listaMenuPermiso.get(i));
                    } else {
                        listaMenuPermisoHi.add(listaMenuPermiso.get(i));
                    }
                }
                for (MenuBean menuP : listaMenuPermisoPa) {
                    listaMenuHijoAux = new ArrayList<>();
                    primerNivel = DefaultSubMenu.builder()
                            .label(menuP.getNombre())
                            .icon(menuP.getUrlAccion())
                            .build();
                    //Carga el árbol de opciones
                    for (int i = 0; i < listaMenuPermisoHi.size(); i++) {
                        if (!Fichero.getPERMISOSXUSUARIO().contains(dataUser.getUser().getCodigo()+"_"+listaMenuPermisoHi.get(i).getCodigo().trim()) ) {                        
                        if (menuP.getCodigo().equals(listaMenuPermisoHi.get(i).getMenuPadre())) {
                            listaMenuHijoAux.add(listaMenuPermisoHi.get(i));
                        }
                        
                        }
                    }
                    for (MenuBean menuH : listaMenuHijoAux) {
                        item2 = DefaultMenuItem.builder()
                                .value(menuH.getNombre())
                                .outcome(menuH.getUrlAccion())
                                .build();
                        primerNivel.getElements().add(item2);
                    }
                    model.getElements().add(primerNivel);
                }
                break;
            case "agco":
                obtenerMenuPermiso(dataUser.getUser().getNiveloperacion());
                listaMenuPermisoHi = new ArrayList<>();
                listaMenuPermisoPa = new ArrayList<>();
                for (int i = 0; i < listaMenuPermiso.size(); i++) {
                    if (listaMenuPermiso.get(i).getNivel().equals("AAA")) {
                        listaMenuPermisoPa.add(listaMenuPermiso.get(i));
                    } else {
                        listaMenuPermisoHi.add(listaMenuPermiso.get(i));
                    }
                }
                for (MenuBean menuP : listaMenuPermisoPa) {
                    listaMenuHijoAux = new ArrayList<>();
                    primerNivel = DefaultSubMenu.builder()
                            .label(menuP.getNombre())
                            .icon(menuP.getUrlAccion())
                            .build();
                    //Carga el árbol de opciones
                    for (int i = 0; i < listaMenuPermisoHi.size(); i++) {
                    if (!Fichero.getPERMISOSXUSUARIO().contains(dataUser.getUser().getCodigo()+"_"+listaMenuPermisoHi.get(i).getCodigo().trim()) ) {
                 
                        if (menuP.getCodigo().equals(listaMenuPermisoHi.get(i).getMenuPadre())) {
                            listaMenuHijoAux.add(listaMenuPermisoHi.get(i));
                        }
                    }
                    }
                    for (MenuBean menuH : listaMenuHijoAux) {
                        item2 = DefaultMenuItem.builder()
                                .value(menuH.getNombre())
                                .outcome(menuH.getUrlAccion())
                                .build();
                        primerNivel.getElements().add(item2);
                    }
                    model.getElements().add(primerNivel);
                }
                break;
        }

        // Agregar ítem de Soporte al final para todos los usuarios
        DefaultMenuItem soporteItem = DefaultMenuItem.builder()
                .value("Soporte")
                .icon("pi pi-question-circle")
                .outcome("/soporte.xhtml")
                .build();
        model.getElements().add(soporteItem);
    }

    public List<MenuBean> getListaMenuPadreBean() {
        return listaMenuPadreBean;
    }

    public void setListaMenuPadreBean(List<MenuBean> listaMenuPadreBean) {
        this.listaMenuPadreBean = listaMenuPadreBean;
    }

    public List<MenuBean> getListaMenuHijoBean() {
        return listaMenuHijoBean;
    }

    public void setListaMenuHijoBean(List<MenuBean> listaMenuHijoBean) {
        this.listaMenuHijoBean = listaMenuHijoBean;
    }

    public MenuBean getMenu() {
        return menu;
    }

    public void setMenu(MenuBean menu) {
        this.menu = menu;
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

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }

    public String getUrlAyuda() {
        return urlAyuda;
    }

    public void setUrlAyuda(String urlAyuda) {
        this.urlAyuda = urlAyuda;
    }

    public String getDescripcionAyuda() {
        return descripcionAyuda;
    }

    public void setDescripcionAyuda(String descripcionAyuda) {
        this.descripcionAyuda = descripcionAyuda;
    }

    public FirmaE getFirmae() {
        return firmae;
    }

    public void setFirmae(FirmaE firmae) {
        this.firmae = firmae;
    }

    public String getFechaC() {
        return fechaC;
    }

    public void setFechaC(String fechaC) {
        this.fechaC = fechaC;
    }

    public String getRutaEConsulta() {
        return rutaEConsulta;
    }

    public void setRutaEConsulta(String rutaEConsulta) {
        this.rutaEConsulta = rutaEConsulta;
    }

}
