/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.login.bean;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Usuario;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
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
@RequestScoped
public class AyudaBean extends LoginBean implements Serializable {

    protected MenuModel modelAyuda = new DefaultMenuModel();

    /*
    Variable que almacena varios Productos
     */
    private List<MenuBean> listaMenuPadreBean;
    /*
    Variable que almacena varios Productos
     */
    private List<MenuBean> listaMenuHijoAux;
    /*
    Variable que almacena varios Productos
     */
    private List<MenuBean> listaMenuHijoBean;

    private List<MenuBean> listaMenuPermiso;

    private List<MenuBean> listaMenuPermisoPa;

    private List<MenuBean> listaMenuPermisoHi;

    private MenuBean menu;
    /*
    Variable que alamcena un usuario
     */
    private Usuario user;
    /*
    Variable que almacena el nombre del usuaio conectado
     */
    private String userConected;
    /*
    variable que alamacena los productos asfalto
     */
    private String direcMedia;

    private String descripcion;

    private String nombreMenu;

    private boolean actualizarD;

    private TreeNode root;

    @PostConstruct
    public void init() {

        actualizarD = true;
        /*direcMedia = Fichero.getCARPETAAYUDA() + "\\CLIENTE_2\\index.html";
        descripcion = "xxxxxxxxxxxxxxxxxxxxxxxx";
        nombreMenu = "Cliente";
        menu = new MenuBean();*/

 direcMedia = "https://drive.google.com/file/d/1D99SVsN9ox1hUjI2GwjaEsG2XkNU-sly/view?usp=share_link";
            descripcion = "Ayuda menu cliente";
            nombreMenu = "Cliente";
        //crearAtbol();
        //obtenerMenuPadre();
        //obtenerMenuHijo();
        //cargarMenuAyuda();
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

    public void crearAtbol() {
        root = new DefaultTreeNode("Files", null);
        TreeNode node0 = new DefaultTreeNode("Seguridad", root);
        TreeNode node1 = new DefaultTreeNode("Catálogo", root);
        TreeNode node2 = new DefaultTreeNode("Monitor Comercial", root);
        TreeNode node3 = new DefaultTreeNode("Actor Comercial", root);
        TreeNode node4 = new DefaultTreeNode("Pedidos y Facturación", root);
        TreeNode node5 = new DefaultTreeNode("Precios y Facturación", root);
        TreeNode node6 = new DefaultTreeNode("Reportes", root);

//        TreeNode node00 = new DefaultTreeNode("Menús", node0);
//        TreeNode node01 = new DefaultTreeNode("Permisos", node0);
//        TreeNode node02 = new DefaultTreeNode("Usuarios", node0);
        node0.getChildren().add(new DefaultTreeNode("Menús"));
        node0.getChildren().add(new DefaultTreeNode("Permisos"));
        node0.getChildren().add(new DefaultTreeNode("Usuarios"));

        node1.getChildren().add(new DefaultTreeNode("Tipo de Cliente"));
        node1.getChildren().add(new DefaultTreeNode("Banco"));
        node1.getChildren().add(new DefaultTreeNode("Terminal"));
        node1.getChildren().add(new DefaultTreeNode("Dirección INEN"));
        node1.getChildren().add(new DefaultTreeNode("Forma de Pago"));
        node1.getChildren().add(new DefaultTreeNode("Medida"));
        node1.getChildren().add(new DefaultTreeNode("Producto"));
        node1.getChildren().add(new DefaultTreeNode("Área Mercadeo"));

        node2.getChildren().add(new DefaultTreeNode("Monitor Comercial"));

        node3.getChildren().add(new DefaultTreeNode("Abastecedora"));
        node3.getChildren().add(new DefaultTreeNode("Cliente"));
        node3.getChildren().add(new DefaultTreeNode("Cliente Producto"));
        node3.getChildren().add(new DefaultTreeNode("Comercializadora"));
        node3.getChildren().add(new DefaultTreeNode("Comercializadora Producto"));
        node3.getChildren().add(new DefaultTreeNode("Numeración Documento"));
        node3.getChildren().add(new DefaultTreeNode("Total Garantizado"));
        node3.getChildren().add(new DefaultTreeNode("Cliente Garantia"));

        node4.getChildren().add(new DefaultTreeNode("Consulta Facturas Cliente"));
        node4.getChildren().add(new DefaultTreeNode("Pago de Factura"));
        node4.getChildren().add(new DefaultTreeNode("Notas de Pedido"));
        node4.getChildren().add(new DefaultTreeNode("Facturación"));
        node4.getChildren().add(new DefaultTreeNode("Refacturación"));
        node4.getChildren().add(new DefaultTreeNode("Nota de Crédito"));
        node4.getChildren().add(new DefaultTreeNode("Prórrogas de Facturas"));

        node5.getChildren().add(new DefaultTreeNode("Facturador Despachador"));
        node5.getChildren().add(new DefaultTreeNode("Gravamen"));
        node5.getChildren().add(new DefaultTreeNode("Lista de Precios"));
        node5.getChildren().add(new DefaultTreeNode("Precio"));
        node5.getChildren().add(new DefaultTreeNode("Rubro Tercero"));
        node5.getChildren().add(new DefaultTreeNode("Cliente Rubro Tercero"));
        node5.getChildren().add(new DefaultTreeNode("Fecha Festiva"));

        node6.getChildren().add(new DefaultTreeNode("Reporte Precios"));

        direcMedia = Fichero.getCARPETAAYUDA() + "\\CLIENTE_2\\index.html";
    }

    public void update(MenuBean menuH) {
        if (menuH != null) {
            actualizarD = true;
//            direcMedia = Fichero.getCARPETAAYUDA() + menuH.getUrlAyuda();
//            descripcion = menuH.getDescripcionAyuda();
//            nombreMenu = menuH.getNombre();
            direcMedia = "https://drive.google.com/file/d/1D99SVsN9ox1hUjI2GwjaEsG2XkNU-sly/view?usp=share_link";
            descripcion = "Ayuda menu cliente";
            nombreMenu = "Cliente";
        }
    }

    public void cargarMenuAyuda() {
        DefaultSubMenu primerNivel;
        DefaultMenuItem item2;
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
                                //.(createExpression("#{ayudaBean.update()}").getExpressionString())
                                //.onclick(menuH.getUrlAyuda())
                                //.onclick(actulizarInfo(menuH))
                                //.outcome(menuH.getUrlAccion())
                                .build();
                        //item2.setCommand("#{ayudaBean.update(" + menuH + ")}");
                        primerNivel.getElements().add(item2);
                    }
                    modelAyuda.getElements().add(primerNivel);
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
                        if (menuP.getCodigo().equals(listaMenuPermisoHi.get(i).getMenuPadre())) {
                            listaMenuHijoAux.add(listaMenuPermisoHi.get(i));
                        }
                    }
                    for (MenuBean menuH : listaMenuHijoAux) {
                        item2 = DefaultMenuItem.builder()
                                .value(menuH.getNombre())
                                .outcome(menuH.getUrlAccion())
                                .build();
                        primerNivel.getElements().add(item2);
                    }
                    modelAyuda.getElements().add(primerNivel);
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
                        if (menuP.getCodigo().equals(listaMenuPermisoHi.get(i).getMenuPadre())) {
                            listaMenuHijoAux.add(listaMenuPermisoHi.get(i));
                        }
                    }
                    for (MenuBean menuH : listaMenuHijoAux) {
                        item2 = DefaultMenuItem.builder()
                                .value(menuH.getNombre())
                                .outcome(menuH.getUrlAccion())
                                .build();
                        primerNivel.getElements().add(item2);
                    }
                    modelAyuda.getElements().add(primerNivel);
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
                        if (menuP.getCodigo().equals(listaMenuPermisoHi.get(i).getMenuPadre())) {
                            listaMenuHijoAux.add(listaMenuPermisoHi.get(i));
                        }
                    }
                    for (MenuBean menuH : listaMenuHijoAux) {
                        item2 = DefaultMenuItem.builder()
                                .value(menuH.getNombre())
                                .outcome(menuH.getUrlAccion())
                                .build();
                        primerNivel.getElements().add(item2);
                    }
                    modelAyuda.getElements().add(primerNivel);
                }
                break;
            default:
                break;
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

    public void mostarPantalla() throws IOException {
        //FacesContext.getCurrentInstance().getExternalContext().redirect("/infinity-web-1/ayuda.xhtml");        
        PrimeFaces.current().executeScript("PF('ayuda').show()");
    }

    public TreeNode getRoot() {
        return root;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getUserConected() {
        return userConected;
    }

    public void setUserConected(String userConected) {
        this.userConected = userConected;
    }

    public String getDirecMedia() {
        return direcMedia;
    }

    public void setDirecMedia(String direcMedia) {
        this.direcMedia = direcMedia;
    }

    public boolean isActualizarD() {
        return actualizarD;
    }

    public void setActualizarD(boolean actualizarD) {
        this.actualizarD = actualizarD;
    }

    public MenuModel getModelAyuda() {
        return modelAyuda;
    }

    public void setModelAyuda(MenuModel modelAyuda) {
        this.modelAyuda = modelAyuda;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }

}
