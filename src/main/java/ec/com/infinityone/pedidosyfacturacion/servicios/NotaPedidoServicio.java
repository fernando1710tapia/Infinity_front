/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.pedidosyfacturacion.servicios;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Abastecedora;
import ec.com.infinityone.modeloWeb.Banco;
import ec.com.infinityone.modeloWeb.Cliente;
import ec.com.infinityone.modeloWeb.Comercializadora;
import ec.com.infinityone.modeloWeb.Formapago;
import ec.com.infinityone.modeloWeb.Notapedido;
import ec.com.infinityone.modeloWeb.NotapedidoPK;
import ec.com.infinityone.modeloWeb.Terminal;
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
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
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
            Cliente clienteNP = new Cliente();
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

                        /*----Varaibles para transformar formate json en fechas-----*/
                        Long dateStrFV = nt.getLong("fechaventa");
                        Long dateStrFD = nt.getLong("fechadespacho");
                        Date dateFV = new Date(dateStrFV);
                        Date dateFD = new Date(dateStrFD);
                        String fechaDescpacho = date.format(dateFD);
                        String fechaVencimiento = date.format(dateFV);
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
                        clienteNP.setCodigo(cli.getString("codigo"));
                        clienteNP.setNombrecomercial(cli.getString("nombrecomercial"));
                        clienteNP.setNombre(cli.getString("nombre"));
                        clienteNP.setRuc(cli.getString("ruc"));
                        clienteNP.setCorreo1(cli.getString("correo1"));
                        clienteNP.setTelefono1(cli.getString("telefono1"));
                        clienteNP.setDireccion(cli.getString("direccion"));
                        if (!cli.isNull("tipoplazocredito")) {
                            clienteNP.setTipoplazocredito(cli.getString("tipoplazocredito"));
                        }
                        clienteNP.setCodigolistaprecio(cli.getLong("codigolistaprecio"));
                        clienteNP.setCodigoformapago(formap);
                        clienteNP.setControldespacho(cli.getInt("controldespacho"));

                        /*----Objeto Terminal----*/
                        terminalT.setCodigo(term.getString("codigo"));

                        /*----Objeto Banco----*/
                        banco.setCodigo(ban.getString("codigo"));

                        /*------Variable trasnformar de int a short-----*/
                        int dp = cli.getInt("diasplazocredito");
                        short diasplazo = (short) dp;
                        clienteNP.setDiasplazocredito(diasplazo);

                        /*----Guardando el cliente, termina y banco en Nota pedido---*/
                        np.setCodigocliente(clienteNP);
                        np.setCodigoterminal(terminalT);
                        np.setCodigobanco(banco);
                        np.setComercializadora(comerc);
                        np.setAbastecedora(abas);

                        np.setNumerofacturasri(nt.getString("numerofacturasri"));
                        npPK.setNumero(ntPK.getString("numero"));
                        npPK.setCodigoabastecedora(ntPK.getString("codigoabastecedora"));
                        npPK.setCodigocomercializadora(ntPK.getString("codigocomercializadora"));
                        np.setFechaventa(fechaVencimiento);
                        np.setFechadespacho(fechaDescpacho);
                        np.setAdelantar(true);
                        np.setActiva(nt.getBoolean("activa"));
                        try {
                            np.setPrefijo(nt.getString("prefijo"));
                            //System.out.println("NOTA DE PEDIDO OK: " + npPK.getNumero());
                        } catch (Throwable e) {
                            System.out.println("ERROR PREFIJO" + e.getMessage() + npPK.getNumero().getClass());
                        }

                        np.setNotapedidoPK(npPK);
                    }
                }
            }

            if (connection.getResponseCode() != 200) {                
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
                System.out.println("ERROR CON LA NOTA DE PEDIDO: " + np.getNotapedidoPK().getNumero());
                return np;
            }

        } catch (IOException e) {
            System.out.println("FT:: ERROR EN obtenerNotaPedidos " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return null;
    }

}
