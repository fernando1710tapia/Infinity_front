/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.seguridad;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.bean.login.MenuBean;
import ec.com.infinityone.modelo.Menu;
import ec.com.infinityone.modelo.ObjetoNivel1;
import ec.com.infinityone.modelo.Permiso;
import ec.com.infinityone.modelo.PermisoPK;
import ec.com.infinityone.modelo.Usuario;
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
import javax.faces.context.FacesContext;
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
public class SeguridadpermisoBean extends ReusableBean implements Serializable {

    /*
    Variable que almacena varios Permisos
     */
    private List<Permiso> listaPermiso;

    private List<Menu> listaMenuHijoBean;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarSeguridad;

    private boolean permiteEdit;

    private Permiso permiso;

    private PermisoPK permisoPK;

    private Menu menu;

    private List<Menu> listaMenuPAux;

    /**
     * Constructor por defecto
     */
    public SeguridadpermisoBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.permiso";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.permiso";

        editarSeguridad = false;
        objeto = new ObjetoNivel1();
        permiso = new Permiso();
        permisoPK = new PermisoPK();
        menu = new Menu();
        permiteEdit = false;
        listaMenuPAux = new ArrayList<>();

        obtenerMenuHijo();
        obtenerPermiso();

        //x = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
    }

    public void obtenerPermiso() {
        try {
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaPermiso = new ArrayList<>();
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
                JSONObject per = retorno.getJSONObject(indice);
                JSONObject perPK = per.getJSONObject("permisoPK");
                JSONObject men = per.getJSONObject("menu");

                permisoPK.setNiveloperacion(perPK.getString("niveloperacion"));
                permisoPK.setCodigomenu(perPK.getString("codigomenu"));
                permiso.setPermisoPK(permisoPK);
                permiso.setUsuarioactual(per.getString("usuarioactual"));
                menu.setCodigo(men.getString("codigo"));
                menu.setNombre(men.getString("nombre"));
                menu.setNivel(men.getString("nivel"));
                if (!men.isNull("menupadre")) {
                    menu.setMenupadre(men.getString("menupadre"));
                }
                if (!men.isNull("urlaccion")) {
                    menu.setUrlaccion(men.getString("urlaccion"));
                }
                menu.setUsuarioactual(men.getString("usuarioactual"));
                permiso.setMenu(menu);
                for (int i = 0; i < listaMenuHijoBean.size(); i++) {
                    if (permiso.getPermisoPK().getCodigomenu().equals(listaMenuHijoBean.get(i).getCodigo())) {
                        listaPermiso.add(permiso);
                    }
                }                
                permiso = new Permiso();
                menu = new Menu();
                permisoPK = new PermisoPK();

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
                if (!menuHi.isNull("menupadre")) {
                    menu.setMenupadre(menuHi.getString("menupadre"));
                }
                if (!menuHi.isNull("urlaccion")) {
                    menu.setUrlaccion(menuHi.getString("urlaccion"));
                }
                listaMenuHijoBean.add(menu);
                menu = new Menu();
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

            Menu menuPadre = new Menu();

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
                menuPadre.setMenupadre(menuPa.getString("menupadre").trim());
                menuPadre.setUsuarioactual(menuPa.getString("usuarioactual"));
                menuPadre.setUrlaccion(menuPa.getString("urlaccion"));
                menuPadre.setCodigo(menuPa.getString("codigo"));
                menuPadre.setNombre(menuPa.getString("nombre"));
                menuPadre.setNivel(menuPa.getString("nivel"));
                listaMenuPAux.add(menuPadre);
                menuPadre = new Menu();
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
        if (editarSeguridad) {
            editItems();
            obtenerPermiso();
        } else {
            if (verificarMenuP()) {
                addItems(menu.getCodigo());
            } else {
                addItems(menu.getMenupadre());
                addItems(menu.getCodigo());
            }
            obtenerPermiso();
        }
    }

    public Boolean verificarMenuP() {
        if (menu != null) {
            for (int i = 0; i < listaPermiso.size(); i++) {
                if (listaPermiso.get(i).getPermisoPK().getNiveloperacion().equals(permisoPK.getNiveloperacion())) {
                    if (listaPermiso.get(i).getMenu().getMenupadre().equals(menu.getMenupadre())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void addItems(String codigoMenu) {
        try {
            String respuesta;
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            JSONObject objPK = new JSONObject();
            objPK.put("niveloperacion", permisoPK.getNiveloperacion());
            objPK.put("codigomenu", codigoMenu);
            obj.put("permisoPK", objPK);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "PERMISO REGISTRADO EXITOSAMENTE");
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
            JSONObject objPK = new JSONObject();
            objPK.put("niveloperacion", permisoPK.getNiveloperacion());
            objPK.put("codigomenu", menu.getCodigo());
            obj.put("permisoPK", objPK);
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "MENU ACUTALIZADO EXITOSAMENTE");
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
            }
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteItems() {
        try {
            String respuesta;
            System.out.println("FT:: deleteItems: "+direccion +" - "+permiso.getPermisoPK().getNiveloperacion() + " . "+permiso.getPermisoPK().getCodigomenu());
            url = new URL(direccion + "/porId?niveloperacion=" + permiso.getPermisoPK().getNiveloperacion()+ "+&codigomenu=" + permiso.getPermisoPK().getCodigomenu());
       //     url = new URL(direccion + "/porId?codigocliente=" + clienteproducto.getClienteproductoPK().getCodigocliente() + "&codigo=" + clienteproducto.getClienteproductoPK().getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "PERMISO ELIMINADO EXITOSAMENTE");
                obtenerPermiso();
            } else {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ELIMINAR");
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevaSeguridad() {
        editarSeguridad = false;
        permiteEdit = false;
        permiso = new Permiso();
        menu = new Menu();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public Permiso editarPermiso(Permiso obj) {
        editarSeguridad = true;
        permiso = obj;
        permiteEdit = true;
        this.permisoPK = permiso.getPermisoPK();
        this.menu = permiso.getMenu();
        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return permiso;
    }

    public List<Menu> getListaMenuHijoBean() {
        return listaMenuHijoBean;
    }

    public void setListaMenuHijoBean(List<Menu> listaMenuHijoBean) {
        this.listaMenuHijoBean = listaMenuHijoBean;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public boolean isEditarProducto() {
        return editarSeguridad;
    }

    public void setEditarProducto(boolean editarSeguridad) {
        this.editarSeguridad = editarSeguridad;
    }

    public boolean isPermiteEdit() {
        return permiteEdit;
    }

    public void setPermiteEdit(boolean permiteEdit) {
        this.permiteEdit = permiteEdit;
    }

    public List<Permiso> getListaPermiso() {
        return listaPermiso;
    }

    public void setListaPermiso(List<Permiso> listaPermiso) {
        this.listaPermiso = listaPermiso;
    }

    public boolean isEditarSeguridad() {
        return editarSeguridad;
    }

    public void setEditarSeguridad(boolean editarSeguridad) {
        this.editarSeguridad = editarSeguridad;
    }

    public Permiso getPermiso() {
        return permiso;
    }

    public void setPermiso(Permiso permiso) {
        this.permiso = permiso;
    }

    public PermisoPK getPermisoPK() {
        return permisoPK;
    }

    public void setPermisoPK(PermisoPK permisoPK) {
        this.permisoPK = permisoPK;
    }

}
