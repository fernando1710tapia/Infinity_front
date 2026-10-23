/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.servicio.catalogo;

import ec.com.infinityone.bean.TerminalBean;
import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.ObjetoNivel1;
import ec.com.infinityone.modelo.Usuario;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
public class UsuarioServicio {

	/**
	 * Obtiene el Expo Push Token activo de un usuario consultando
	 * ec.com.infinity.modelo.usuario/porUsuario?codigo=XXX, cuyo "retorno" es un
	 * arreglo con pushToken y push_token_activo.
	 * Devuelve null si no existe, no está activo o falla la consulta.
	 */
	public String obtenerPushTokenActivo(String codigoUsuario) {
		if (codigoUsuario == null || codigoUsuario.trim().isEmpty()) {
			return null;
		}
		try {
			URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim()
					+ "ec.com.infinity.modelo.usuario/porUsuario?codigo="
					+ java.net.URLEncoder.encode(codigoUsuario.trim(), "UTF-8"));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			if (connection.getResponseCode() != 200) {
				return null;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String tmp;
			while ((tmp = br.readLine()) != null) {
				sb.append(tmp);
			}
			JSONObject json = new JSONObject(sb.toString());
			if (json.isNull("retorno")) {
				return null;
			}
			JSONArray retorno = json.getJSONArray("retorno");
			if (retorno.length() == 0) {
				return null;
			}
			JSONObject ret = retorno.getJSONObject(0);
			String token = ret.optString("pushToken", null);
			if (token == null || token.isEmpty() || "null".equalsIgnoreCase(token)) {
				token = ret.optString("push_token", null);
			}
			if (token == null || token.trim().isEmpty() || "null".equalsIgnoreCase(token)) {
				System.out.println("[ExpoPush] Usuario " + codigoUsuario + " no tiene pushToken registrado.");
				return null;
			}
			token = token.trim();
			System.out.println("[ExpoPush] Usuario " + codigoUsuario + " token encontrado: " + token
					+ " (push_token_activo=" + ret.opt("push_token_activo") + ")");
			return token;
		} catch (Exception e) {
			System.out.println("[ExpoPush] No se pudo obtener push token de " + codigoUsuario + ": " + e.getMessage());
			return null;
		}
	}

	/*
	 * Variable para instanciar al objeto nivel 1
	 */
	private List<Usuario> listaUsuario;

	public List<Usuario> obtenerUsuario() {
		try {
			// URL url = new
			// URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.usuario");
			URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.usuario");

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");

			listaUsuario = new ArrayList<>();
			Usuario usuario = new Usuario();
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
				JSONObject terminal = retorno.getJSONObject(indice);
				usuario.setCodigo(terminal.getString("codigo"));
				usuario.setNombre(terminal.getString("nombre"));
				listaUsuario.add(usuario);
				usuario = new Usuario();
			}

			System.out.println(connection.getResponseCode());
			System.out.println(connection.getResponseMessage());
			return listaUsuario;
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}

		return listaUsuario;
	}

	public List<Usuario> obtenerUsuarioPorComercializadora(String codComer) {
		try {
			// URL url = new
			// URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.usuario/porusuarioadm?codigocomercializadora");
			URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim()
					+ "ec.com.infinity.modelo.usuario/porusuarioadm?codigocomercializadora=" + codComer);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");

			listaUsuario = new ArrayList<>();
			Usuario usuario = new Usuario();
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
				JSONObject terminal = retorno.getJSONObject(indice);
				usuario.setCodigo(terminal.getString("codigo"));
				usuario.setNombre(terminal.getString("nombre"));
				listaUsuario.add(usuario);
				usuario = new Usuario();
			}

			System.out.println(connection.getResponseCode());
			System.out.println(connection.getResponseMessage());
			return listaUsuario;
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}

		return listaUsuario;
	}

	public List<Usuario> obtenerUsuarioXTerminal(String codComer, String codigoterminal) {
		try {
			// URL url = new
			// URL("https://www.supertech.ec:8443/infinityone1/resources/ec.com.infinity.modelo.usuario/porusuarioadm?codigocomercializadora");
			URL url = new URL(Fichero.getRUTASERVICIOSPERSISTENCIA().trim()
					+ "ec.com.infinity.modelo.usuario/usuarioxterminal?codigocomercializadora=" + codComer
					+ "&codigoterminal=" + codigoterminal);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");

			listaUsuario = new ArrayList<>();
			Usuario usuario = new Usuario();
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
				JSONObject terminal = retorno.getJSONObject(indice);
				usuario.setCodigo(terminal.getString("codigo"));
				usuario.setNombre(terminal.getString("nombre"));
				usuario.setCorreo(terminal.getString("correo"));
				listaUsuario.add(usuario);
				usuario = new Usuario();
			}

			System.out.println(connection.getResponseCode());
			System.out.println(connection.getResponseMessage());
			return listaUsuario;
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}

		return listaUsuario;
	}
}
