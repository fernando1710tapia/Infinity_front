/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.servicio.pedidosyfacturacion;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Abastecedora;
import ec.com.infinityone.modelo.Banco;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.Comercializadora;
import ec.com.infinityone.modelo.Formapago;
import ec.com.infinityone.modelo.Notapedido;
import ec.com.infinityone.modelo.NotapedidoPK;
import ec.com.infinityone.modelo.Terminal;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class NotaPedidoServicio {

    /*
    Variable Nota Pedido
     */
    private Notapedido np;
    /*
    Varibale Nota Pedido PK
     */
    private NotapedidoPK npPK;

    private Abastecedora abas;

    private Comercializadora comerc;

    private Formapago formap;

    private Terminal terminalT;

    private Banco banco;

    public Notapedido obtenerNotaPedidos(String codAbas, String codComer, String numero) throws ParseException {
        try {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'11:00:00'Z'");
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.notapedido/porId?";

            URL url = new URL(direcc + "codigoabastecedora=" + codAbas + "&codigocomercializadora=" + codComer + "&numero=" + numero);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            np = new Notapedido();
            npPK = new NotapedidoPK();
            abas = new Abastecedora();
            comerc = new Comercializadora();
            formap = new Formapago();
            terminalT = new Terminal();
            banco = new Banco();
            Cliente cliente = new Cliente();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String tmp = null;
            String respuesta = "";
            while ((tmp = br.readLine()) != null) {
                respuesta += tmp;
            }
            JSONObject objetoJson = new JSONObject(respuesta);
            JSONArray retorno = objetoJson.getJSONArray("retorno");
            if (!retorno.isEmpty()) {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject nt = retorno.getJSONObject(indice);
                        JSONObject ntPK = nt.getJSONObject("notapedidoPK");
                        JSONObject cli = nt.getJSONObject("codigocliente");
                        JSONObject term = nt.getJSONObject("codigoterminal");
                        JSONObject ban = nt.getJSONObject("codigobanco");
                        JSONObject formPago = cli.getJSONObject("codigoformapago");
                        JSONObject com = nt.getJSONObject("comercializadora");
                        JSONObject abastecedora = nt.getJSONObject("abastecedora");

                        /*----Objeto Abastecedora----*/
                        abas.setCodigo(abastecedora.getString("codigo"));

                        /*----Objeto comercializadora----*/
                        comerc.setCodigo(com.getString("codigo"));
                        comerc.setNombre(com.getString("nombre"));
                        comerc.setRuc(com.getString("ruc"));
                        comerc.setDireccion(com.getString("direccion"));
                        comerc.setAmbientesri(com.getString("ambientesri").charAt(0));
                        comerc.setEsagenteretencion(com.getBoolean("esagenteretencion"));
                        comerc.setEscontribuyenteespacial(com.getString("escontribuyenteespacial"));
                        comerc.setTipoemision(com.getString("tipoemision").charAt(0));
                        comerc.setObligadocontabilidad(com.getString("obligadocontabilidad"));
                        comerc.setEstablecimientofac(com.getString("establecimientofac"));
                        comerc.setPuntoventafac(com.getString("puntoventafac"));
                        comerc.setClavewsepp(com.getString("clavewsepp"));

                        /*----Objeto Fromapago----*/
                        formap.setCodigo(formPago.getString("codigo"));

                        /*----Objeto Cliente----*/
                        cliente.setCodigo(cli.getString("codigo"));
                        cliente.setNombre(cli.getString("nombre"));
                        cliente.setRuc(cli.getString("ruc"));
                        cliente.setCorreo1(cli.getString("correo1"));
                        cliente.setTelefono1(cli.getString("telefono1"));
                        cliente.setDireccion(cli.getString("direccion"));
                        if (!cli.isNull("tipoplazocredito")) {
                            cliente.setTipoplazocredito(cli.getString("tipoplazocredito"));
                        }
                        cliente.setCodigolistaprecio(cli.getLong("codigolistaprecio"));
                        cliente.setCodigoformapago(formap);

                        /*----Objeto Terminal----*/
                        terminalT.setCodigo(term.getString("codigo"));

                        /*----Objeto Banco----*/
                        banco.setCodigo(ban.getString("codigo"));

                        /*----Guardando el cliente, termina y banco en Nota pedido---*/
                        np.setCodigocliente(cliente);
                        np.setCodigoterminal(terminalT);
                        np.setCodigobanco(banco);
                        np.setComercializadora(comerc);
                        np.setAbastecedora(abas);
                        /*----Varaibles para transformar formate json en fechas-----*/
                        Long dateStrFV = nt.getLong("fechaventa");
                        Long dateStrFD = nt.getLong("fechadespacho");
                        Date dateFV = new Date(dateStrFV);
                        Date dateFD = new Date(dateStrFD);
                        String fechaDescpacho = date.format(dateFD);
                        String fechaVencimiento = date.format(dateFV);
                        np.setFechaventa(fechaVencimiento);
                        np.setFechadespacho(fechaDescpacho);
                        np.setActiva(nt.getBoolean("activa"));
                        if (!nt.isNull("codigoautotanque")) {
                            np.setCodigoautotanque(nt.getString("codigoautotanque"));
                        } else {
                            np.setCodigoautotanque("");
                        }
                        if (!nt.isNull("cedulaconductor")) {
                            np.setCedulaconductor(nt.getString("cedulaconductor"));
                        } else {
                            np.setCedulaconductor("");
                        }
                        if (!nt.isNull("respuestageneracionoeepp")) {
                            np.setRespuestageneracionoeepp(nt.getString("respuestageneracionoeepp"));
                        } else {
                            np.setRespuestageneracionoeepp("");
                        }
                        if (!nt.isNull("observacion")) {
                            np.setObservacion(nt.getString("observacion"));
                        } else {
                            np.setObservacion("");
                        }
                        if (!nt.isNull("adelantar")) {
                            np.setAdelantar(nt.getBoolean("adelantar"));
                        } else {
                            np.setAdelantar(true);
                        }
                        if (!nt.isNull("respuestaanulacionoeepp")) {
                            np.setRespuestaanulacionoeepp(nt.getString("respuestaanulacionoeepp"));
                        } else {
                            np.setRespuestaanulacionoeepp("");
                        }
                        if (!nt.isNull("tramaenviadagoe")) {
                            np.setTramaenviadagoe(nt.getString("tramaenviadagoe"));
                        } else {
                            np.setTramaenviadagoe("");
                        }
                        if (!nt.isNull("tramarecibidagoe")) {
                            np.setTramarecibidagoe(nt.getString("tramarecibidagoe"));
                        } else {
                            np.setTramarecibidagoe("");
                        }
                        if (!nt.isNull("tramarecibidaaoe")) {
                            np.setTramarecibidaaoe(nt.getString("tramarecibidaaoe"));
                        } else {
                            np.setTramarecibidaaoe("");
                        }
                        if (!nt.isNull("tramarecibidaaoe")) {
                            np.setTramarenviadaaoe(nt.getString("tramarenviadaaoe"));
                        } else {
                            np.setTramarenviadaaoe("");
                        }
                        np.setUsuarioactual(nt.getString("usuarioactual"));
                        if (!nt.isNull("prefijo")) {
                            np.setPrefijo(nt.getString("prefijo"));
                        } else {
                            np.setPrefijo("");
                        }
                        np.setProcesar(nt.getBoolean("procesar"));
                        np.setNumerofacturasri(nt.getString("numerofacturasri"));
                        npPK.setNumero(ntPK.getString("numero"));
                        npPK.setCodigoabastecedora(ntPK.getString("codigoabastecedora"));
                        npPK.setCodigocomercializadora(ntPK.getString("codigocomercializadora"));
                        np.setNotapedidoPK(npPK);
                        npPK = new NotapedidoPK();
                    }
                }
            }

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
                return null;
            }

        } catch (IOException e) {
            System.out.println("FT:: ERROR EN obtenerNotaPedidos " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return np;
    }

}
