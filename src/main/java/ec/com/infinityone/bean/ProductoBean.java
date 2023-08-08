/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.ObjetoNivel1;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
 * @author David
 */
@Named
@ViewScoped
public class ProductoBean extends ReusableBean implements Serializable {

    /*
    Variable que almacena varios Productos
     */
    private List<ProductoBean> listaProductosBean;
    /*
    Variable que almacena varios Productos
     */
    private List<ProductoBean> listaProductosBeanAux;
    /*
    Variable que almacena varios Productos
     */
    private List<ObjetoNivel1> listaAreaMercadeo;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarProducto;
    /*
    Variable que establece true or false para el estado del Producto
     */
    private boolean estadoProducto;
    /*
    Variable almacena codigo STC
     */
    private String codigoSTC;
    /*
    Variable almacena codigo ARCH
     */
    private String codigoARCH;

    private String codigoAreaM;

    private JSONObject codAm;

    private boolean permiteEdit;

    private ObjetoNivel1 objeto1;

    private ProductoBean producto;

    private BigDecimal porcentajeivapresuntivo;

    /**
     * Constructor por defecto
     */
    public ProductoBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        //direccion = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.producto";
        direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.producto";
        editarProducto = false;
        objeto = new ObjetoNivel1();
        producto = new ProductoBean();
        permiteEdit = false;
        porcentajeivapresuntivo = new BigDecimal(0);
        listaProductosBean = new ArrayList<>();
        listaProductosBeanAux = new ArrayList<>();

        obtenerProducto();
        //obtenerProductosSinImpuestos();
        obtenerListaAreaMercadeo();
        //cargarAreaMerc();
        //getURL();
    }

    public void obtenerProducto() {
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            //byte[] bytes = token.getBytes();
            //String basicAuth = "Basic : " + new String(Base64.getEncoder().encode(bytes));  
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            //connection.setRequestProperty("Authorization", token);
            //connection.setRequestProperty("Access-Control-Allow-Headers", "Authorization");
            //connection.setRequestProperty("Accept-Charset", "utf-8");
            //connection.setRequestProperty("Accept-Encoding", "gzip");
            //connection.setRequestProperty("Accept-Language", "en-US");
            //connection.setRequestProperty("Access-Control-Allow-Origin", "*");

            listaProductosBean = new ArrayList<>();
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
                JSONObject prod = retorno.getJSONObject(indice);
                codAm = prod.getJSONObject("codigoareamercadeo");
                producto.setCodigo(prod.getString("codigo"));
                producto.setNombre(prod.getString("nombre"));
                producto.setCodigoAreaM(codAm.getString("codigo"));
                producto.setCodigoSTC(prod.getString("codigostc"));
                producto.setCodigoARCH(prod.getString("codigoarch"));
                producto.setObjRelacionado(codAm.getString("codigo") + " - " + codAm.getString("nombre"));
                producto.setUsuario(prod.getString("usuarioactual"));
                producto.setPorcentajeivapresuntivo(prod.getBigDecimal("porcentajeivapresuntivo"));
                listaProductosBean.add(producto);
                producto = new ProductoBean();
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }else{
                obtenerProductosSinImpuestos();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtenerProductosSinImpuestos() {
        listaProductosBeanAux = new ArrayList<>();
        if (!listaProductosBean.isEmpty()) {
            for (int i = 0; i < listaProductosBean.size(); i++) {
                if (!listaProductosBean.get(i).getCodigo().substring(0, 2).equals("00")) {
                    listaProductosBeanAux.add(listaProductosBean.get(i));
                }
            }
        }
    }

    public void obtenerListaAreaMercadeo() {
        try {
            String token = "Infinity eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXVsIiwiaXNzIjoiZWMuY29tLmluZmluaXR5b25lIiwiaWF0IjoxNjI1ODUyOTIyLCJleHAiOjE2MjU4NTY1MjJ9.zlpXPvsZeHrmnPdQ_cINdd6SBPoNqF0Sq6Wuin3P6HdriDHoPRkhCYcJNYlfnAb8yUTrCPc9OHFIVjF35wTcaw";
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.areamercadeo");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaAreaMercadeo = new ArrayList<>();
            objeto1 = new ObjetoNivel1();
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
                JSONObject areaM = retorno.getJSONObject(indice);
                objeto1.setCodigo(areaM.getString("codigo"));
                objeto1.setObjRelacionado(areaM.getString("codigo") + " - " + areaM.getString("nombre"));
                listaAreaMercadeo.add(objeto1);
                objeto1 = new ObjetoNivel1();
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
        if (editarProducto) {
            editItems();
            obtenerProducto();
        } else {
            addItems();
            obtenerProducto();
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
            obj.put("codigo", producto.getCodigo());
            obj.put("nombre", producto.getNombre());
            obj.put("codigoareamercadeo", producto.getCodigoAreaM());
            obj.put("codigostc", producto.getCodigoSTC());
            obj.put("codigoarch", producto.getCodigoARCH());
            obj.put("porcentajeivapresuntivo", producto.getPorcentajeivapresuntivo());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            //respuesta = "{\"nombre\":\"asdfg\",\"codigo\":\"21\",\"codigoareamercadeo\":{\"codigo\": \"01\"},\"codigostc\":\"453\",\"codigoarch\":\"456\",\"usuarioactual\":\"ft\"}";
            writer.write(respuesta);
            writer.close();
            if (connection.getResponseCode() == 200) {
                PrimeFaces.current().executeScript("PF('nuevo').hide()");
                this.dialogo(FacesMessage.SEVERITY_INFO, "PRODUCTO REGISTRADO EXITOSAMENTE");
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
            obj.put("codigo", producto.getCodigo());
            obj.put("nombre", producto.getNombre());
            obj.put("codigoareamercadeo", producto.getCodigoAreaM());
            obj.put("codigostc", producto.getCodigoSTC());
            obj.put("codigoarch", producto.getCodigoARCH());
            obj.put("porcentajeivapresuntivo", producto.getPorcentajeivapresuntivo());
            obj.put("usuarioactual", dataUser.getUser().getNombrever());
            respuesta = obj.toString();
            writer.write(respuesta);
            writer.close();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "PRODUCTO ACUTALIZADO EXITOSAMENTE");
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
            url = new URL(direccion + "/porId?codigo=" + producto.getCodigo());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
            PrimeFaces.current().executeScript("PF('nuevo').hide()");
            if (connection.getResponseCode() == 200) {
                this.dialogo(FacesMessage.SEVERITY_INFO, "PRODUCTO ELIMINADO EXITOSAMENTE");
                obtenerProducto();
            } else {
                this.dialogo(FacesMessage.SEVERITY_INFO, "ERROR AL ELIMINAR");
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nuevoProducto() {
        estadoProducto = true;
        editarProducto = false;
        permiteEdit = false;
        objeto1 = new ObjetoNivel1();
        producto = new ProductoBean();
        producto.setPorcentajeivapresuntivo(new BigDecimal(100));
        PrimeFaces.current().executeScript("PF('nuevo').show()");
    }

    public ProductoBean editarProducto(ProductoBean obj) {
        editarProducto = true;
        producto = obj;
        permiteEdit = true;

        PrimeFaces.current().executeScript("PF('nuevo').show()");
        return producto;
    }

    public boolean isEditarProducto() {
        return editarProducto;
    }

    public void setEditarProducto(boolean editarProducto) {
        this.editarProducto = editarProducto;
    }

    public boolean isEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(boolean estadoProducto) {
        this.estadoProducto = estadoProducto;
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

    public boolean isPermiteEdit() {
        return permiteEdit;
    }

    public void setPermiteEdit(boolean permiteEdit) {
        this.permiteEdit = permiteEdit;
    }

    public List<ObjetoNivel1> getListaAreaMercadeo() {
        return listaAreaMercadeo;
    }

    public void setListaAreaMercadeo(List<ObjetoNivel1> listaAreaMercadeo) {
        this.listaAreaMercadeo = listaAreaMercadeo;
    }

    public String getCodigoAreaM() {
        return codigoAreaM;
    }

    public void setCodigoAreaM(String codigoAreaM) {
        this.codigoAreaM = codigoAreaM;
    }

    public ObjetoNivel1 getObjeto1() {
        return objeto1;
    }

    public void setObjeto1(ObjetoNivel1 objeto1) {
        this.objeto1 = objeto1;
    }

    public List<ProductoBean> getListaProductosBean() {
        return listaProductosBean;
    }

    public void setListaProductosBean(List<ProductoBean> listaProductosBean) {
        this.listaProductosBean = listaProductosBean;
    }

    public ProductoBean getProducto() {
        return producto;
    }

    public void setProducto(ProductoBean producto) {
        this.producto = producto;
    }

    public List<ProductoBean> getListaProductosBeanAux() {
        return listaProductosBeanAux;
    }

    public void setListaProductosBeanAux(List<ProductoBean> listaProductosBeanAux) {
        this.listaProductosBeanAux = listaProductosBeanAux;
    }

    public BigDecimal getPorcentajeivapresuntivo() {
        return porcentajeivapresuntivo;
    }

    public void setPorcentajeivapresuntivo(BigDecimal porcentajeivapresuntivo) {
        this.porcentajeivapresuntivo = porcentajeivapresuntivo;
    }

}
