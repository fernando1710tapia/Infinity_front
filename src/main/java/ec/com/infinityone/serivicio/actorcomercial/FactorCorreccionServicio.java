package ec.com.infinityone.serivicio.actorcomercial;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.FactorCorreccion;
import ec.com.infinityone.modelo.Producto;
import ec.com.infinityone.modelo.Terminal;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 * Service for Factor de Corrección
 * @author Antigravity
 */
@LocalBean
@Stateless
public class FactorCorreccionServicio {

    public List<FactorCorreccion> obtenerFactores(String codComer, String codTerm, Date fechaIni, Date fechaFin) {
        List<FactorCorreccion> lista = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String fIni = sdf.format(fechaIni);
            String fFin = sdf.format(fechaFin);

            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factorcorreccion/buscarfechas?";
            String params = "codigocomercializadora=" + codComer + "&codigoterminal=" + codTerm + "&fechainicial=" + fIni + "&fechafinal=" + fFin;
            
            URL url = new URL(direcc + params);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                BufferedReader br = new BufferedReader(reader);
                String tmp;
                StringBuilder respuesta = new StringBuilder();
                while ((tmp = br.readLine()) != null) {
                    respuesta.append(tmp);
                }
                
                JSONObject objetoJson = new JSONObject(respuesta.toString());
                JSONArray retorno = objetoJson.getJSONArray("retorno");
                
                SimpleDateFormat sdfParse = new SimpleDateFormat("yyyy-MM-dd");

                for (int i = 0; i < retorno.length(); i++) {
                    JSONObject obj = retorno.getJSONObject(i);
                    FactorCorreccion factor = new FactorCorreccion();
                    
                    // Caso 1: Estructura Plana
                    if (!obj.isNull("fecha")) {
                        Object fechaObj = obj.get("fecha");
                        if (fechaObj instanceof Long) {
                            factor.setFecha(new Date((Long) fechaObj));
                        } else {
                            factor.setFecha(sdfParse.parse(obj.getString("fecha")));
                        }
                    }
                    if (!obj.isNull("factor")) {
                        factor.setFactor(obj.getBigDecimal("factor"));
                    }
                    Producto p = new Producto();
                    if (!obj.isNull("producto")) {
                        parsearProducto(p, obj.get("producto"));
                    } else if (!obj.isNull("codigoProducto")) {
                        parsearProducto(p, obj.get("codigoProducto"));
                    } else if (!obj.isNull("codigoproducto")) {
                        parsearProducto(p, obj.get("codigoproducto"));
                    }
                    
                    // Caso 2: Estructura Anidada (factorcorreccionPK)
                    if (!obj.isNull("factorcorreccionPK")) {
                        JSONObject pkJson = obj.getJSONObject("factorcorreccionPK");
                        // Fecha nested
                        if (!pkJson.isNull("fecha") && factor.getFecha() == null) {
                            Object fechaObj = pkJson.get("fecha");
                            if (fechaObj instanceof Long) {
                                factor.setFecha(new Date((Long) fechaObj));
                            } else {
                                factor.setFecha(sdfParse.parse(pkJson.getString("fecha")));
                            }
                        }
                        // Producto nested
                        if (!pkJson.isNull("codigoproducto") && p.getCodigo() == null) {
                            parsearProducto(p, pkJson.get("codigoproducto"));
                        }
                        // Terminal nested
                        if (!pkJson.isNull("codigoterminal")) {
                            Terminal t = new Terminal();
                            parsearTerminal(t, pkJson.get("codigoterminal"));
                            factor.setTerminal(t);
                        }
                    }

                    if (p.getCodigo() != null) {
                        factor.setProducto(p);
                    }
                    lista.add(factor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<FactorCorreccion> buscarProductosPorTerminal(String codTerm) {
        List<FactorCorreccion> lista = new ArrayList<>();
        try {
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factorcorreccion/buscarterminalproductoxterminal?";
            String params = "pcodigoterminal=" + codTerm;
            
            URL url = new URL(direcc + params);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder respuesta = new StringBuilder();
                String tmp;
                while ((tmp = br.readLine()) != null) { respuesta.append(tmp); }
                
                JSONObject objetoJson = new JSONObject(respuesta.toString());
                JSONArray retorno = objetoJson.getJSONArray("retorno");
                for (int i = 0; i < retorno.length(); i++) {
                    JSONObject obj = retorno.getJSONObject(i);
                    
                    Producto p = new Producto();
                    if (!obj.isNull("producto")) {
                        parsearProducto(p, obj.get("producto"));
                    } else if (!obj.isNull("codigoProducto")) {
                        parsearProducto(p, obj.get("codigoProducto"));
                    } else if (!obj.isNull("codigoproducto")) {
                        parsearProducto(p, obj.get("codigoproducto"));
                    }

                    if (p.getCodigo() != null) {
                        FactorCorreccion factor = new FactorCorreccion();
                        factor.setProducto(p);
                        factor.setFactor(BigDecimal.ZERO);
                        lista.add(factor);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean guardar(List<FactorCorreccion> lista, String codComer, String codTerm, Date fecha, String usuarioActual) {
        try {
            String direcc = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.factorcorreccion";
            URL url = new URL(direcc);
            
            for (FactorCorreccion f : lista) {
                if (f.getFactor().compareTo(BigDecimal.ZERO) == 0) continue;

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                JSONObject json = new JSONObject();
                
                // Estructura según imagen del usuario
                JSONObject pkJson = new JSONObject();
                pkJson.put("codigocomercializadora", codComer);
                pkJson.put("codigoterminal", codTerm);
                pkJson.put("codigoproducto", f.getProducto().getCodigo());
                pkJson.put("fecha", fecha.getTime());
                
                json.put("factorcorreccionPK", pkJson);
                json.put("factor", f.getFactor());
                json.put("usuarioactual", usuarioActual != null ? usuarioActual : "SISTEMA");

                java.io.OutputStream os = connection.getOutputStream();
                os.write(json.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                if (connection.getResponseCode() != 200 && connection.getResponseCode() != 201) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void parsearTerminal(Terminal t, Object valor) {
        if (valor == null) return;
        
        if (valor instanceof JSONObject) {
            JSONObject termJson = (JSONObject) valor;
            t.setCodigo(termJson.optString("codigo", ""));
            t.setNombre(termJson.optString("nombre", ""));
        } else {
            String valStr = String.valueOf(valor).trim();
            if (valStr.contains("-")) {
                String[] parts = valStr.split("-", 2);
                t.setCodigo(parts[0].trim());
                t.setNombre(parts[1].trim());
            } else {
                t.setCodigo(valStr);
                t.setNombre(valStr);
            }
        }
    }

    private void parsearProducto(Producto p, Object valor) {
        if (valor == null) return;
        
        if (valor instanceof JSONObject) {
            JSONObject prodJson = (JSONObject) valor;
            p.setCodigo(prodJson.optString("codigo", ""));
            p.setNombre(prodJson.optString("nombre", ""));
        } else {
            String valStr = String.valueOf(valor).trim();
            if (valStr.contains("-")) {
                String[] parts = valStr.split("-", 2);
                p.setCodigo(parts[0].trim());
                p.setNombre(parts[1].trim());
            } else {
                p.setCodigo(valStr);
                p.setNombre(valStr);
            }
        }
    }
}
