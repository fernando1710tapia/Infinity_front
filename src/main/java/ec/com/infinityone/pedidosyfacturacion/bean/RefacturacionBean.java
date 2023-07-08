/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.pedidosyfacturacion.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.infinityone.actorcomercial.bean.ComercializadoraBean;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Cliente;
import ec.com.infinityone.modeloWeb.Comercializadoraproducto;
import ec.com.infinityone.modeloWeb.Detallefactura;
import ec.com.infinityone.modeloWeb.DetallefacturaPK;
import ec.com.infinityone.modeloWeb.Detallenotapedido;
import ec.com.infinityone.modeloWeb.DetallenotapedidoPK;
import ec.com.infinityone.modeloWeb.EnvioRefactura;
import ec.com.infinityone.modeloWeb.Factura;
import ec.com.infinityone.modeloWeb.FacturaPK;
import ec.com.infinityone.modeloWeb.Listaprecio;
import ec.com.infinityone.modeloWeb.ListaprecioPK;
import ec.com.infinityone.modeloWeb.Medida;
import ec.com.infinityone.modeloWeb.Notapedido;
import ec.com.infinityone.modeloWeb.Precio;
import ec.com.infinityone.modeloWeb.PrecioPK;
import ec.com.infinityone.modeloWeb.Producto;
import ec.com.infinityone.modeloWeb.Terminal;
import ec.com.infinityone.pedidosyfacturacion.servicios.NotaPedidoServicio;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.primefaces.PrimeFaces;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.Visibility;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author HP
 */
/**
 *
 * @author David
 */
@Named
@ViewScoped
public class RefacturacionBean extends FacturacionBean implements Serializable {

    @Inject
    private NotaPedidoServicio npServicio;
    /*
    Clase que guarda dos objetos para la refacturación
     */
    private EnvioRefactura envrefact;
    /*
    Litsa que almacena varios tipos de enviofactura
     */
    private List<EnvioRefactura> listaEnvRefac;
    /*
    Litsa que almacena varios tipos de enviofactura
     */
    private List<EnvioRefactura> listaEnvRefacNueva;
    /*
    Variable Precio
     */
    private List<Precio> listaPrecios;
    /*
    Variable Lista Precio
     */
    private Listaprecio precioLista;
    /*
    Variable Lista Precio PK
     */
    private ListaprecioPK precioListaPK;

    private Comercializadoraproducto comerProduct;

    private BigDecimal volAuxiliar = new BigDecimal(0);

    /**
     * Constructor por defecto
     */
    public RefacturacionBean() {
    }

    public void refacturar() {
        reestablecer();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        } else {
            seleccionarComercializadora();
        }
        if (habilitarTerminal) {
            terminal = new Terminal();
        } else {
            seleccionarTerminal(2);
        }
        if (habilitarCli) {
            cliente = new Cliente();
        }
        mostarFactura = true;
        mostarPantallaInicial = false;
        listaEnvRefac = new ArrayList<>();
        fechaf = null;
    }

    public void goBack() {
        reestablecer();
        mostarFactura = false;
        mostarPantallaInicial = true;
        listaEnvRefac = new ArrayList<>();
    }

    public void obtenerFacturasRefacturar() throws ParseException {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            String fechaS = date.format(this.fechaI);
            String fechaF = "";
            String dateF = "";
            String msg = "";
            long diff = new Long(0);
            long diffrence = new Long(0);
            Date secondDate = new Date();
            boolean fecha7 = true;
            /*fechas para comparar entre las dos y establecer un rango de 7 dias*/
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            String dateI = sdf.format(fechaI);
            Date firstDate = sdf.parse(dateI);
            //if para verificar que esta en la pantalla de consulta
            if (fechaf != null) {
                fechaF = date.format(fechaf);
                dateF = sdf.format(fechaf);
                if (dateF != null) {
                    secondDate = sdf.parse(dateF);
                    diff = secondDate.getTime() - firstDate.getTime();
                    TimeUnit time = TimeUnit.DAYS;
                    diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                    if (diffrence > 7) {
                        this.dialogo(FacesMessage.SEVERITY_ERROR, "LA FECHA DE FIN NO PUEDE SER MAYOR A 7 DÍAS A LA FECHA DE INICIO");
                        fecha7 = false;
                    } else {
                        //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura";
                        String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura";

                        url = new URL(direcc + "/pararefactura?codigoabastecedora=" + this.codAbas
                                + "&codigocomercializadora=" + this.codComer
                                + "&codigoterminal=" + this.codTerminal
                                + "&tipofecha=" + tipoFecha
                                + "&fechaI=" + fechaS
                                + "&fechaF=" + fechaF
                                + "&refacturada=1"
                                + "&activa=2"
                                + "&productosinfe=" + Fichero.getPRODUCTOSINFE());
                        msg = "NO SE ENCONTRARON FACTURAS REFACTURADAS";
                    }
                }
            } else {
                //para verificar que esta en la pantalla de generacion de refacturacion
                //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura";
                String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura";
                url = new URL(direcc + "/pararefactura?codigoabastecedora=" + this.codAbas
                        + "&codigocomercializadora=" + this.codComer
                        + "&codigoterminal=" + this.codTerminal
                        + "&tipofecha=" + tipoFecha
                        + "&fechaI=" + fechaS
                        + "&fechaF=" + fechaS
                        + "&refacturada=0"
                        + "&activa=1"
                        + "&productosinfe=" + Fichero.getPRODUCTOSINFE());
                msg = "NO SE ENCONTRARON FACTURAS PENDIENTES DE SER REFACTURADAS";
            }
            if (fecha7) {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                listaEnvRefac = new ArrayList<>();
                listDet = new ArrayList<>();
                envrefact = new EnvioRefactura();
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());

                BufferedReader br = new BufferedReader(reader);
                String tmp = null;
                String respuesta = "";
                while ((tmp = br.readLine()) != null) {
                    respuesta += tmp;
                }
                JSONObject objetoJson = new JSONObject(respuesta);
                JSONArray retorno = objetoJson.getJSONArray("retorno");
                if (retorno.isEmpty()) {
                    this.dialogo(FacesMessage.SEVERITY_ERROR, msg);
                } else {
                    for (int indice = 0; indice < retorno.length(); indice++) {
                        if (!retorno.isNull(indice)) {
                            JSONObject fa = retorno.getJSONObject(indice);
                            JSONObject faPK = fa.getJSONObject("facturaPK");

                            factPk.setNumero(faPK.getString("numero"));
                            factPk.setNumeronotapedido(faPK.getString("numeronotapedido"));
                            factPk.setCodigoabastecedora(faPK.getString("codigoabastecedora"));
                            factPk.setCodigocomercializadora(faPK.getString("codigocomercializadora"));
                            fact.setCodigocliente(fa.getString("codigocliente"));
                            fact.setNombrecliente(fa.getString("nombrecliente"));
                            fact.setValortotal(fa.getBigDecimal("valortotal"));
                            fact.setValorconrubro(fa.getBigDecimal("valorconrubro"));
                            fact.setOeenpetro(fa.getBoolean("oeenpetro"));
                            fact.setCodigobanco(fa.getString("codigobanco"));
                            fact.setActiva(fa.getBoolean("activa"));
                            fact.setOeanuladaenpetro(fa.getBoolean("oeanuladaenpetro"));
                            fact.setRefacturada(fa.getBoolean("refacturada"));
                            fact.setEstado(fa.getString("estado"));
                            fact.setPagada(fa.getBoolean("pagada"));
                            fact.setReliquidada(fa.getBoolean("reliquidada"));

                            Long dateStrFV = fa.getLong("fechaventa");
                            Long dateStrFD = fa.getLong("fechadespacho");
                            Date dateFV = new Date(dateStrFV);
                            Date dateFD = new Date(dateStrFD);
                            String fechaDescpacho = date.format(dateFD);
                            String fechaVenta = date.format(dateFV);

                            fact.setFechaventa(fechaVenta);
                            fact.setFechadespacho(fechaDescpacho);

                            if (fa.getBoolean("activa") == true) {
                                estadoAnulacion = false;
                                //fact.setActiva(estadoAnulacion);
                            } else {
                                estadoAnulacion = true;
                                //fact.setActiva(estadoAnulacion);
                            }
                            if (fa.getBoolean("oeenpetro") == true) {
                                oeenpetro = "S";
                                fact.setOeenpetro(true);
                            } else {
                                oeenpetro = "N";
                                fact.setOeenpetro(false);
                            }
                            if (fa.getString("estado").equalsIgnoreCase("PENDIENTE")) {
                                ensri = "P";
                                envrefact.setEnsri(ensri);
                            } else {
                                if (fa.isNull("numeroautorizacion")) {
                                    ensri = "N";
                                    envrefact.setEnsri(ensri);
                                } else {
                                    fact.setNumeroautorizacion(fa.getString("numeroautorizacion"));
                                    if (fa.getString("numeroautorizacion").length() == 49) {
                                        ensri = "S";
                                        envrefact.setEnsri(ensri);
                                    } else {
                                        ensri = "N";
                                        envrefact.setEnsri(ensri);
                                    }
                                }
                            }
                            fact.setFacturaPK(factPk);
                            envrefact.setFactura(fact);
                            JSONArray detalleList = fa.getJSONArray("detallefacturaList");
                            for (int i = 0; i < detalleList.length(); i++) {
                                JSONObject det = detalleList.getJSONObject(i);
                                JSONObject detFpk = det.getJSONObject("detallefacturaPK");
                                JSONObject product = det.getJSONObject("producto");
                                JSONObject med = det.getJSONObject("codigomedida");

                                if (!product.getString("codigo").substring(0, 2).equals("00")) {
                                    producto.setCodigo(product.getString("codigo"));
                                    producto.setNombre(product.getString("nombre"));
                                    detFacPK.setCodigoproducto(product.getString("codigo"));
                                    detFac.setVolumennaturalrequerido(det.getBigDecimal("volumennaturalrequerido"));
                                    detFac.setVolumennaturalautorizado(det.getBigDecimal("volumennaturalautorizado"));
                                    detFac.setPrecioproducto(det.getBigDecimal("precioproducto"));
                                    detFac.setSubtotal(det.getBigDecimal("subtotal"));
                                    detFac.setCodigomedida(med.getString("codigo"));
                                    if (!det.isNull("ruccomercializadora")) {
                                        detFac.setRuccomercializadora(det.getString("ruccomercializadora"));
                                    }
                                    detFac.setDetallefacturaPK(detFacPK);
                                    if (!det.isNull("nombreproducto")) {
                                        detFac.setNombreproducto(det.getString("nombreproducto"));
                                    }
                                    envrefact.setDetalleFactura(detFac);
                                    producto = new Producto();
                                    detFac = new Detallefactura();
                                    detFacPK = new DetallefacturaPK();
                                }
                            }
                            listaEnvRefac.add(envrefact);
                            envrefact = new EnvioRefactura();
                            fact = new Factura();
                            factPk = new FacturaPK();
                            listDet = new ArrayList<>();
                        }
                    }
                }
                if (connection.getResponseCode() != 200) {
                    System.out.println(connection.getResponseCode());
                    System.out.println(connection.getResponseMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void seleccionarRefactura() throws ParseException {
        if (comercializadora.getActivo().equals("S")) {
            for (int i = 0; i < listaEnvRefac.size(); i++) {
                if (listaEnvRefac.get(i).getFactura().isSeleccionar() == true) {
                    if (listaEnvRefac.get(i).getFactura().getEstado().trim().equals("PENDIENTE")) {
                        if (listaEnvRefac.get(i).getFactura().getRefacturada() == false) {
                            if (listaEnvRefac.get(i).getFactura().getActiva() == true) {
                                if (listaEnvRefac.get(i).getFactura().getPagada() == false) {
                                    if (listaEnvRefac.get(i).getFactura().getOeenpetro() == true) {
                                        if (listaEnvRefac.get(i).getFactura().getReliquidada() == false) {
                                            process = true;
                                            //listaEnvRefac.get(i).getFactura().setSeleccionar(process);
                                        } else {
                                            this.dialogo(FacesMessage.SEVERITY_ERROR, "Esta factura ha sido reliquidada (NC), NO debe ser Refacturada");
                                            listaEnvRefac.get(i).getFactura().setSeleccionar(false);
                                        }
                                    } else {
                                        this.dialogo(FacesMessage.SEVERITY_ERROR, "Esta factura No se ha enviado a Petroecuador, NO requiere ser Refacturada... podría anularla, verifique opciones con el Adm. del negocio");
                                        listaEnvRefac.get(i).getFactura().setSeleccionar(false);
                                    }
                                } else {
                                    this.dialogo(FacesMessage.SEVERITY_ERROR, "Esta factura esta PAGADA, NO puede ser Refacturada");
                                    listaEnvRefac.get(i).getFactura().setSeleccionar(false);
                                }
                            } else {
                                this.dialogo(FacesMessage.SEVERITY_ERROR, "Esta factura esta ANULADA, NO puede ser Refacturada");
                                listaEnvRefac.get(i).getFactura().setSeleccionar(false);
                            }
                        } else {
                            this.dialogo(FacesMessage.SEVERITY_ERROR, "Esta factura Ya ha sido refacturada anteriormente, NO debe refacturarse");
                            listaEnvRefac.get(i).getFactura().setSeleccionar(false);
                        }
                    } else {
                        this.dialogo(FacesMessage.SEVERITY_ERROR, "La Factura No es suceptible de Refacturación, Verifique con el Adm del sistema el estado de este documento hacia el SRI");
                        listaEnvRefac.get(i).getFactura().setSeleccionar(false);
                    }
                }
            }
            //obtenerFacturasRefacturar();
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA SE ENCUENTRA INACTIVA");
        }
    }

    public void seleccionarCliRefactura(String numFactura) {
        for (int i = 0; i < listaEnvRefac.size(); i++) {
            if (listaEnvRefac.get(i).getFactura().getFacturaPK().getNumero().equals(numFactura)) {
                listaEnvRefac.get(i).setClienteRefactura(cliente);
                break;
            }
        }
    }

    public void guardar() throws ParseException {
        if (comercializadora.getActivo().equals("S")) {
            for (int i = 0; i < listaEnvRefac.size(); i++) {
                if (listaEnvRefac.get(i).getFactura().isSeleccionar() == true) {
                    //if (!volAuxiliar.equals(0)) {
                    //if (volAuxiliar.compareTo(listaEnvRefac.get(i).getDetalleFactura().getVolumennaturalrequerido()) == 0) {
                    if (listaEnvRefac.get(i).getDetalleFactura().getVolumennaturalautorizado().compareTo(listaEnvRefac.get(i).getDetalleFactura().getVolumennaturalrequerido()) == 0) {
                        this.dialogo(FacesMessage.SEVERITY_ERROR, "EL VOLUMEN A REFACTURAR NO HA SIDO CAMBIADO, POR FAVOR MODIFIQUE EL VALOR PARA CONTINUAR");
                    } else {
                        obtenerDetalleNotaPedido(listaEnvRefac.get(i));
                    }
                    //} else {
                    //  this.dialogo(FacesMessage.SEVERITY_ERROR, "EL VOLUMEN A REFACTURAR NO TIENE NINGUN VALOR");
                    // }
                }
            }
            obtenerFacturasRefacturar();
            //obtenerFacturasRefacturar();
        } else {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "LA COMERCIALIZADORA SE ENCUENTRA INACTIVA");
        }
    }

    public void verPreciosVigentes() {
        if (codComer.isEmpty() || codTerminal.equals("-1")) {
            this.dialogo(FacesMessage.SEVERITY_WARN, "Seleccione una comercializadora y un terminal");
        } else {
            String prodSinFe = Fichero.getPRODUCTOSINFE();
            obtenerPreciosVigentes(codComer, codTerminal, prodSinFe);
            PrimeFaces.current().executeScript("PF('nuevo').show()");
        }
    }

    public void obtenerPreciosVigentes(String codComer, String codTerminal, String prodSinFe) {
        try {
            //url = new URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.precio/porComer?codigocomercializadora=" + codComer);
            url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.precio/preciovigenteproductoespecial?codigocomercializadora=" + codComer
                    + "&codigoterminal=" + codTerminal
                    + "&productosinfe=" + prodSinFe);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaPrecios = new ArrayList<>();
            precioLista = new Listaprecio();
            precioListaPK = new ListaprecioPK();
            comerProduct = new Comercializadoraproducto();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject precioJson = new JSONObject(respuesta);
            JSONArray retorno = precioJson.getJSONArray("retorno");
            for (int indice = 0; indice < retorno.length(); indice++) {
                if (!retorno.isNull(indice)) {
                    JSONObject prec = retorno.getJSONObject(indice);
                    JSONObject precPK = prec.getJSONObject("precioPK");
                    JSONObject listPrecio = prec.getJSONObject("listaprecio");
                    JSONObject listPrecioPK = listPrecio.getJSONObject("listaprecioPK");
                    JSONObject comerProd = prec.getJSONObject("comercializadoraproducto");
                    JSONObject prod = comerProd.getJSONObject("producto");
                    precioPK.setCodigocomercializadora(precPK.getString("codigocomercializadora"));
                    precioPK.setCodigoterminal(precPK.getString("codigoterminal"));
                    precioPK.setCodigoproducto(precPK.getString("codigoproducto"));
                    precioPK.setCodigomedida(precPK.getString("codigomedida"));
                    precioPK.setCodigolistaprecio(precPK.getLong("codigolistaprecio"));
                    Long lDateIni = precPK.getLong("fechainicio");
                    Date dateIni = new Date(lDateIni);
                    precioPK.setFechainicio(dateIni);
                    precioPK.setSecuencial(precPK.getInt("secuencial"));
                    precioPK.setCodigoPrecio(precPK.getLong("codigoPrecio"));
                    precio.setPrecioPK(precioPK);
                    if (!prec.isNull("fechafin")) {
                        Long lDateFin = prec.getLong("fechafin");
                        Date dateFin = new Date(lDateFin);
                        precio.setFechafin(dateFin);
                    }
                    precio.setActivo(prec.getBoolean("activo"));
                    if (precio.getActivo()) {
//                    precio.setActivoS("S");
                    } else {
//                   precio.setActivoS("N");
                    }
                    precio.setObservacion(prec.getString("observacion"));
                    precio.setPrecioproducto(prec.getBigDecimal("precioproducto"));
                    precio.setUsuarioactual(prec.getString("usuarioactual"));
                    /*------------------Precio Lista------------------------------*/
                    precioLista.setNombre(listPrecio.getString("nombre"));
                    precioLista.setTipo(listPrecio.getString("tipo"));
                    precioListaPK.setCodigo(listPrecioPK.getLong("codigo"));
                    precioLista.setListaprecioPK(precioListaPK);
                    precio.setListaprecio(precioLista);
                    /*------------------Producto------------------------------*/
                    producto.setCodigo(prod.getString("codigo"));
                    producto.setNombre(prod.getString("nombre"));
                    /*------------------Comercializadora Producto------------------------------*/
                    comerProduct.setProducto(producto);
                    precio.setComercializadoraproducto(comerProduct);
                    listaPrecios.add(precio);
                    precio = new Precio();
                    producto = new Producto();
                    comerProduct = new Comercializadoraproducto();
                    precioPK = new PrecioPK();
                    precioLista = new Listaprecio();
                    precioListaPK = new ListaprecioPK();
                }

            }
            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            } else {
                listaPrecios = obtenerPrimerPrecio(listaPrecios);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Precio> obtenerPrimerPrecio(List<Precio> listaPrecios) {
        List<Precio> listaPreciosAux = new ArrayList<>();
        HashMap<String, Precio> mapaPrecios = new HashMap<>();
        String cadena = "";

        if (!listaPrecios.isEmpty()) {
            for (int i = 0; i < listaPrecios.size(); i++) {
                cadena = listaPrecios.get(i).getPrecioPK().getCodigoproducto() + listaPrecios.get(i).getPrecioPK().getCodigomedida() + String.valueOf(listaPrecios.get(i).getPrecioPK().getCodigolistaprecio()) + String.valueOf(listaPrecios.get(i).getPrecioPK().getFechainicio().getTime());
                mapaPrecios.put(cadena, listaPrecios.get(i));
            }
            listaPreciosAux = new ArrayList<>(mapaPrecios.values());
        }
        return listaPreciosAux;
    }

    public void obtenerDetalleNotaPedido(EnvioRefactura envF) throws ParseException {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");

            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/porId?";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detallenotapedido/porId?";
            url = new URL(direcc + "codigoabastecedora=" + envF.getFactura().getFacturaPK().getCodigoabastecedora()
                    + "&codigocomercializadora=" + envF.getFactura().getFacturaPK().getCodigocomercializadora()
                    + "&numero=" + envF.getFactura().getFacturaPK().getNumeronotapedido()
                    + "&codigoproducto=" + envF.getDetalleFactura().getDetallefacturaPK().getCodigoproducto()
                    + "&codigomedida=" + envF.getDetalleFactura().getCodigomedida());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            detNP = new Detallenotapedido();
            detNPK = new DetallenotapedidoPK();
            medida = new Medida();
            producto = new Producto();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            if (retorno.isEmpty()) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL OBTENER INFORMACIÓN SOBRE EL DETALLE NOTA PEDIDO");
            } else {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject det = retorno.getJSONObject(indice);
                        JSONObject detPk = det.getJSONObject("detallenotapedidoPK");
                        JSONObject med = det.getJSONObject("medida");
                        JSONObject prod = det.getJSONObject("producto");

                        medida.setAbreviacion(med.getString("abreviacion"));
                        medida.setActivo(med.getBoolean("activo"));
                        medida.setCodigo(med.getString("codigo"));
                        medida.setNombre(med.getString("nombre"));
                        medida.setUsuarioactual(dataUser.getUser().getNombrever());

                        producto.setCodigo(prod.getString("codigo"));
                        producto.setNombre(prod.getString("nombre"));

                        detNPK.setCodigoabastecedora(detPk.getString("codigoabastecedora"));
                        detNPK.setCodigocomercializadora(detPk.getString("codigocomercializadora"));
                        detNPK.setCodigomedida(detPk.getString("codigomedida"));
                        detNPK.setCodigoproducto(detPk.getString("codigoproducto"));
                        detNPK.setNumero(detPk.getString("numero"));

                        detNP.setDetallenotapedidoPK(detNPK);
                        detNP.setMedida(medida);
                        detNP.setProducto(producto);
                        detNP.setUsuarioactual(dataUser.getUser().getNombrever());
                        detNP.setVolumennaturalautorizado(envF.getDetalleFactura().getVolumennaturalautorizado());
                        detNP.setVolumennaturalrequerido(det.getBigDecimal("volumennaturalrequerido"));
                    }
                }
                if (connection.getResponseCode() >= 200 || connection.getResponseCode() <= 200) {
                    if (envF.getClienteRefactura() != null) {
                        actualizarNotaPedido(detNP, envF);
                    }
                    actualizarVolumenRefacturar(detNP, envF);
                } else {
                    System.out.println(connection.getResponseCode());
                    System.out.println(connection.getResponseMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean actualizarNotaPedido(Detallenotapedido detNP, EnvioRefactura envF) throws ParseException {
        Notapedido np = new Notapedido();
        np = npServicio.obtenerNotaPedidos(detNP.getDetallenotapedidoPK().getCodigoabastecedora(), detNP.getDetallenotapedidoPK().getCodigocomercializadora(), detNP.getDetallenotapedidoPK().getNumero());
        np.setCodigocliente(envF.getClienteRefactura());
        np.setUsuarioactual(dataUser.getUser().getNombrever());
        try {
            String respuesta = "";
            //String trama = "";
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/porId";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido/porId";
            url = new URL(direcc);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(np);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();

            if (connection.getResponseCode() >= 200 || connection.getResponseCode() <= 200) {
                return true;
            } else {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void actualizarVolumenRefacturar(Detallenotapedido detNP, EnvioRefactura envF) throws ParseException {
        try {
            String respuesta = "";
            //String trama = "";
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.notapedido/porId";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.detallenotapedido/porId";
            url = new URL(direcc);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(detNP);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();

            if (connection.getResponseCode() >= 200 || connection.getResponseCode() <= 200) {
                generarReFacturaParametros(envF);
            } else {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generarReFacturaParametros(EnvioRefactura envF) throws ParseException {
        try {
            System.out.println("FT::-Generar Refactura: " + envNP.toString());
            String respuesta;
            String numeroFactura = "";
            String mensajeError = "";
            String usuario = dataUser.getUser().getNombrever().trim().replace(" ", "");
            url = new URL(direccion + tablaFactura + "/recrearF?"
                    + "codigoabastecedora=" + envF.getFactura().getFacturaPK().getCodigoabastecedora()
                    + "&codigocomercializadora=" + envF.getFactura().getFacturaPK().getCodigocomercializadora()
                    + "&numeronotapedido=" + envF.getFactura().getFacturaPK().getNumeronotapedido()
                    + "&numero=" + envF.getFactura().getFacturaPK().getNumero().trim()
                    + "&codigousuario=" + usuario);

            System.out.println("FT:: Generar Refactura - url:: " + url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
            try {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());

                BufferedReader br = new BufferedReader(reader);
                String tmp = null;
                String resp = "";
                while ((tmp = br.readLine()) != null) {
                    resp += tmp;
                }
                JSONObject objetoJson = new JSONObject(resp);
                JSONArray retorno = objetoJson.getJSONArray("retorno");
                if (connection.getResponseCode() == 200) {
                    for (int indice = 0; indice < retorno.length(); indice++) {
                        if (!retorno.isNull(indice)) {
                            JSONObject resultado = retorno.getJSONObject(indice);
                            JSONObject factura = resultado.getJSONObject("factura");
                            JSONObject facturaPK = factura.getJSONObject("facturaPK");
                            numeroFactura = facturaPK.getString("numero");
                        }
                    }
                    //this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA REGISTRADA EXITOSAMENTE");
                    this.dialogo(FacesMessage.SEVERITY_INFO, "La nota de pedido N. " + envF.getFactura().getFacturaPK().getNumeronotapedido() + " con factura N. " + envF.getFactura().getFacturaPK().getNumero()
                            + " ACABA DE SER REFACTURADA. NUEVA FACTURA N. " + numeroFactura);
                    //obtenerFacturasRefacturar();
                } else if (connection.getResponseCode() == 299) {
                    for (int indice = 0; indice < retorno.length(); indice++) {
                        if (!retorno.isNull(indice)) {
                            mensajeError = retorno.getString(indice);
                            System.out.println("Codigo: " + connection.getResponseCode());
                        }
                    }
                    this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR FACTURA sobre la NP: " + envNP.getNotapedido().getNotapedidoPK().getNumero() + " " + mensajeError);
                    System.out.println("Error al añadir: " + connection.getResponseCode());
                    System.out.println("ResponseMesagge: " + connection.getResponseMessage());
                    System.out.println("getErrorStream: " + connection.getErrorStream());

                }

            } catch (Throwable t) {
                System.out.println("ERROR!!! " + t.getMessage());
            }

//            if (connection.getResponseCode() == 200) {
//                this.dialogo(FacesMessage.SEVERITY_INFO, "FACTURA REGISTRADA EXITOSAMENTE");
//                enviarOrdenPetro(envNP, numeroFactura);
//            } else {
//                this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR FACTURA sobre la NP: " + envNP.getNotapedido().getNotapedidoPK().getNumero());
//                System.out.println("Error al añadir: " + connection.getResponseCode());
//                System.out.println("ResponseMesagge: " + connection.getResponseMessage());
//                System.out.println("getErrorStream: " + connection.getErrorStream());
//            }
        } catch (IOException e) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL REGISTRAR FACTURA sobre la NP: " + envNP.getNotapedido().getNotapedidoPK().getNumero());
            System.out.println("FT:: IOException Capturada: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    public void obtenerNuevaFacturaRefacturada(String codAbas, String codComer, String numNp) throws ParseException {
        try {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            //para verificar que esta en la pantalla de generacion de refacturacion
            //String direcc = "https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.factura";
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factura";
            url = new URL(direcc + "/porAbascomernp?codigoabastecedora=" + codAbas
                    + "&codigocomercializadora=" + codComer
                    + "&numeronp=" + numNp);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            listaEnvRefacNueva = new ArrayList<>();
            listDet = new ArrayList<>();
            envrefact = new EnvioRefactura();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            if (retorno.isEmpty()) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCONTRARON REGISTROS");
            } else {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject fa = retorno.getJSONObject(indice);
                        JSONObject faPK = fa.getJSONObject("facturaPK");

                        factPk.setNumero(faPK.getString("numero"));
                        factPk.setNumeronotapedido(faPK.getString("numeronotapedido"));
                        factPk.setCodigoabastecedora(faPK.getString("codigoabastecedora"));
                        factPk.setCodigocomercializadora(faPK.getString("codigocomercializadora"));
                        fact.setCodigocliente(fa.getString("codigocliente"));
                        fact.setNombrecliente(fa.getString("nombrecliente"));
                        fact.setValortotal(fa.getBigDecimal("valortotal"));
                        fact.setValorconrubro(fa.getBigDecimal("valorconrubro"));
                        fact.setOeenpetro(fa.getBoolean("oeenpetro"));
                        fact.setCodigobanco(fa.getString("codigobanco"));
                        fact.setActiva(fa.getBoolean("activa"));
                        fact.setOeanuladaenpetro(fa.getBoolean("oeanuladaenpetro"));
                        fact.setRefacturada(fa.getBoolean("refacturada"));
                        fact.setEstado(fa.getString("estado"));
                        fact.setPagada(fa.getBoolean("pagada"));
                        fact.setReliquidada(fa.getBoolean("reliquidada"));

                        Long dateStrFV = fa.getLong("fechaventa");
                        Long dateStrFD = fa.getLong("fechadespacho");
                        Date dateFV = new Date(dateStrFV);
                        Date dateFD = new Date(dateStrFD);
                        String fechaDescpacho = date.format(dateFD);
                        String fechaVenta = date.format(dateFV);

                        fact.setFechaventa(fechaVenta);
                        fact.setFechadespacho(fechaDescpacho);

                        if (fa.getBoolean("activa") == true) {
                            estadoAnulacion = false;
                            //fact.setActiva(estadoAnulacion);
                        } else {
                            estadoAnulacion = true;
                            //fact.setActiva(estadoAnulacion);
                        }
                        if (fa.getBoolean("oeenpetro") == true) {
                            oeenpetro = "S";
                            fact.setOeenpetro(true);
                        } else {
                            oeenpetro = "N";
                            fact.setOeenpetro(false);
                        }
                        if (fa.getString("estado").equals("PENDIENTE")) {
                            ensri = "P";
                            envrefact.setEnsri(ensri);
                        } else {
                            if (fa.isNull("numeroautorizacion") || (fa.getString("numeroautorizacion").isEmpty())) {
                                ensri = "N";
                                envrefact.setEnsri(ensri);
                            } else {
                                fact.setNumeroautorizacion(fa.getString("numeroautorizacion"));
                                if (fa.getString("numeroautorizacion").length() == 49) {
                                    ensri = "S";
                                    envrefact.setEnsri(ensri);
                                } else {
                                    ensri = "N";
                                    envrefact.setEnsri(ensri);
                                }
                            }
                        }
                        fact.setFacturaPK(factPk);
                        envrefact.setFactura(fact);
                        JSONArray detalleList = fa.getJSONArray("detallefacturaList");
                        for (int i = 0; i < detalleList.length(); i++) {
                            JSONObject det = detalleList.getJSONObject(i);
                            JSONObject detFpk = det.getJSONObject("detallefacturaPK");
                            JSONObject product = det.getJSONObject("producto");
                            JSONObject med = det.getJSONObject("codigomedida");

                            if (!product.getString("codigo").substring(0, 2).equals("00")) {
                                producto.setCodigo(product.getString("codigo"));
                                producto.setNombre(product.getString("nombre"));
                                detFacPK.setCodigoproducto(product.getString("codigo"));
                                detFac.setVolumennaturalrequerido(det.getBigDecimal("volumennaturalrequerido"));
                                detFac.setVolumennaturalautorizado(det.getBigDecimal("volumennaturalautorizado"));
                                detFac.setPrecioproducto(det.getBigDecimal("precioproducto"));
                                detFac.setSubtotal(det.getBigDecimal("subtotal"));
                                detFac.setCodigomedida(med.getString("codigo"));
                                if (!det.isNull("ruccomercializadora")) {
                                    detFac.setRuccomercializadora(det.getString("ruccomercializadora"));
                                }
                                detFac.setDetallefacturaPK(detFacPK);
                                if (!det.isNull("nombreproducto")) {
                                    detFac.setNombreproducto(det.getString("nombreproducto"));
                                }
                                envrefact.setDetalleFactura(detFac);
                                producto = new Producto();
                                detFac = new Detallefactura();
                                detFacPK = new DetallefacturaPK();
                            }
                        }
                        if (fa.getBoolean("activa") == true) {
                            listaEnvRefacNueva.add(envrefact);
                        }
                        envrefact = new EnvioRefactura();
                        fact = new Factura();
                        factPk = new FacturaPK();
                        listDet = new ArrayList<>();
                    }
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

    public void onRowToggle(ToggleEvent event) throws ParseException {
        if (event.getVisibility() == Visibility.VISIBLE) {
            EnvioRefactura listaERef = (EnvioRefactura) event.getData();
            if (listaERef.getFactura().getFacturaPK().getNumeronotapedido() != null) {
                listaEnvRefacNueva = new ArrayList<>();
                obtenerNuevaFacturaRefacturada(listaERef.getFactura().getFacturaPK().getCodigoabastecedora(), listaERef.getFactura().getFacturaPK().getCodigocomercializadora(), listaERef.getFactura().getFacturaPK().getNumeronotapedido());
            }
        }
    }

    public void generarReporteRef(EnvioRefactura env) {
//        String path = "C:\\archivos\\Template\\NuevaFactura.jrxml";
//        String subreport = "C:\\archivos\\Template\\SubreporteFacturaRubros.jrxml";

        String path = Fichero.getCARPETAREPORTES() + "/NuevaFactura.jrxml";
        String subreport = Fichero.getCARPETAREPORTES() + "/SubreporteFacturaRubros.jrxml";
        String subreport1 = Fichero.getCARPETAREPORTES() + "/SubreporteFacturaImpuestos.jrxml";
        System.out.println("PATH:" + path);
        InputStream file = null;
        try {
            file = new FileInputStream(new File(path));

            JasperReport reporte = JasperCompileManager.compileReport(file);
            JasperReport subreporte = JasperCompileManager.compileReport(subreport);
            JasperReport subreporte1 = JasperCompileManager.compileReport(subreport1);

            Map parametro = new HashMap();
            BufferedImage image = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/logo.jpeg"));
            BufferedImage imageBar = ImageIO.read(new File(Fichero.getCARPETAREPORTES() + "/barras.jpeg"));

//            BufferedImage image = ImageIO.read(new File("C:\\archivos\\Template\\logo.jpg"));
//            BufferedImage imageBar = ImageIO.read(new File("C:\\archivos\\Template\\barras.jpg"));
            parametro.put("numeroComercializadora", env.getFactura().getFacturaPK().getCodigocomercializadora());
            parametro.put("subReporte", subreporte);
            parametro.put("subReporte1", subreporte1);
            parametro.put("numeroFactura", env.getFactura().getFacturaPK().getNumero());
            parametro.put("logo", image);
            parametro.put("barras", imageBar);

            //actual local
            Connection conexion = conexionJasperBD();

            JasperPrint print = JasperFillManager.fillReport(reporte, parametro, conexion);

            File directory = new File(Fichero.getCARPETAREPORTES());
//            File directory = new File("C:\\archivos"); 

            String nombreDocumento = "reporteFactura";

            File pdf = File.createTempFile(nombreDocumento + "_", ".pdf", directory);
            JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
            File initialFile = new File(pdf.getAbsolutePath());
            InputStream targetStream = new FileInputStream(initialFile);
            //pdfStream = new DefaultStreamedContent();
            pdfStream = new DefaultStreamedContent(targetStream, "application/pdf", nombreDocumento + env.getFactura().getFacturaPK().getNumero() + ".pdf");
            //DefaultStreamedContent.builder().contentType("application/pdf").name(nombreDocumento + ".pdf").stream(() -> new FileInputStream(targetStream)).build();
            PrimeFaces.current().executeScript("window.open(" + directory + ",'" + nombreDocumento + "','fullscreen=yes');parent.opener=top;");
            System.err.print(pdf.getAbsolutePath());
            System.out.println(pdf.getAbsolutePath());
        } catch (Exception ex) {
            //ex.printStackTrace();
            System.out.println("Excepcion: " + ex);
        }
    }

    public void mostrarPorRefacturar() {
        try {
            List<EnvioRefactura> listaEnvRefacAuxiliar = new ArrayList<>();
            if (!listaEnvRefac.isEmpty()) {
                if (porRefacturar) {
                    for (int i = 0; i < listaEnvRefac.size(); i++) {
//                        if (listaEnvRefac.get(i).getFactura().isSeleccionar() == true) {
                        if (listaEnvRefac.get(i).getFactura().getEstado().trim().equals("PENDIENTE")) {
                            if (listaEnvRefac.get(i).getFactura().getRefacturada() == false) {
                                if (listaEnvRefac.get(i).getFactura().getActiva() == true) {
                                    if (listaEnvRefac.get(i).getFactura().getPagada() == false) {
                                        if (listaEnvRefac.get(i).getFactura().getOeenpetro() == true) {
                                            if (listaEnvRefac.get(i).getFactura().getReliquidada() == false) {
                                                listaEnvRefacAuxiliar.add(listaEnvRefac.get(i));
                                            }
                                        }
                                    }
                                }
                            }
                        }
//                        }
                    }
                    listaEnvRefac = listaEnvRefacAuxiliar;
                } else {
                    obtenerFacturasRefacturar();
                }
            } else {
                porRefacturar = false;
                this.dialogo(FacesMessage.SEVERITY_ERROR, "PARA PODER VISUALIZAR LAS FACTURAS POR REFACTURAR, PRIMERO REALIZAR UNA BÚSQUEDA CON REGISTROS");
            }
        } catch (ParseException ex) {
            Logger.getLogger(FacturacionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void regresarRefac() {
        mostarFactura = false;
        mostarPantallaInicial = true;
        buscarFacturaXCliente = false;
        listaEnvRefac = new ArrayList();
    }

    public EnvioRefactura getEnvrefact() {
        return envrefact;
    }

    public void setEnvrefact(EnvioRefactura envrefact) {
        this.envrefact = envrefact;
    }

    public List<EnvioRefactura> getListaEnvRefac() {
        return listaEnvRefac;
    }

    public void setListaEnvRefac(List<EnvioRefactura> listaEnvRefac) {
        this.listaEnvRefac = listaEnvRefac;
    }

    public List<EnvioRefactura> getListaEnvRefacNueva() {
        return listaEnvRefacNueva;
    }

    public void setListaEnvRefacNueva(List<EnvioRefactura> listaEnvRefacNueva) {
        this.listaEnvRefacNueva = listaEnvRefacNueva;
    }

    public BigDecimal getVolAuxiliar() {
        return volAuxiliar;
    }

    public void setVolAuxiliar(BigDecimal volAuxiliar) {
        this.volAuxiliar = volAuxiliar;
    }

    public List<Precio> getListaPrecios() {
        return listaPrecios;
    }

    public void setListaPrecios(List<Precio> listaPrecios) {
        this.listaPrecios = listaPrecios;
    }

}
