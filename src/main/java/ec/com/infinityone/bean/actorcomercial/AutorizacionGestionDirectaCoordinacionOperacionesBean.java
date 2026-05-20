package ec.com.infinityone.bean.actorcomercial;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.serivicio.actorcomercial.ClienteServicio;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.servicio.catalogo.BancoServicio;
import ec.com.infinityone.util.Causal;
import ec.com.infinityone.util.EnviarMail;

/**
 *
 * @author SonyVaio
 */
@Named
@ViewScoped
public class AutorizacionGestionDirectaCoordinacionOperacionesBean extends ReusableBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ComercializadoraServicio comercializadoraServicio;
	@Inject
	private ClienteServicio clienteServicio;

	private List<ComercializadoraBean> listaComercializadora;

	private List<Cliente> listaClientes;

	private Cliente cliente;

	private ComercializadoraBean comercializadora;

	private String codCliente;

	private String codComer;

	private boolean habilitarEstadoCliente;

	private boolean editarCliente;
	/*
	 * Variable para establecer la fecha de inicio de vigencia en el cliente
	 * garantia
	 */
	private Date fechaInicioVigencia;
	/*
	 * Variable para establecer la fecha de fin de vigencia en el cliente garantia
	 */
	private Date fechaFinVigencia;
	/*
	 * Variable que establece si el cliente seleccionado controla o no garantias
	 */

	private NumberFormat formatoNumero;

	// Lista que contiene todas las opciones posibles
	private List<Causal> listaCausales;

	// Lista donde PrimeFaces guardará los IDs seleccionados
	private List<String> causalesSeleccionadas;

	/**
	 * Constructor por defecto
	 */
	public AutorizacionGestionDirectaCoordinacionOperacionesBean() {
	}

	@PostConstruct
	/**
	 * Funcion para inicializar variables
	 */
	public void init() {
		direccion = Fichero.getRUTASERVICIOSPERSISTENCIA().trim() + "ec.com.infinity.modelo.cliente";
		cliente = new Cliente();
		comercializadora = new ComercializadoraBean();
		editarCliente = false;
		obtenerComercializadora();
		fechaInicioVigencia = new Date();
		fechaFinVigencia = new Date();
		formatoNumero = NumberFormat.getNumberInstance();

		causalesSeleccionadas = new ArrayList<>();
		listaCausales = new ArrayList<>();

		// Llenamos la lista
		listaCausales.add(new Causal("a", "a. Inexistencia de atención bancaria (fuera de horario o fallas en el sistema bancario)."));
		listaCausales.add(new Causal("b", "b. Asignación adicional de combustible."));
		listaCausales.add(new Causal("c", "c. Necesidad operativa de abastecimiento relacionada a la asignación de turnos."));
		listaCausales.add(new Causal("d", "d. Por cambio de precios en terminales de abastecimiento."));
	}

	public void obtenerComercializadora() {
		listaComercializadora = new ArrayList<>();
		listaComercializadora = comercializadoraServicio.obtenerComercializadoras();
		
		if (!listaComercializadora.isEmpty()) {
			habilitarBusqueda();
		}
	}

	public void habilitarBusqueda() {
		if (dataUser.getUser() != null) {
			if (dataUser.getUser().getNiveloperacion().equals("cero")) {
				habilitarComer = true;
			}
			
			if (dataUser.getUser().getNiveloperacion().equals("adco")) {
				habilitarComer = false;
				for (int i = 0; i < listaComercializadora.size(); i++) {
					if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
						this.comercializadora = listaComercializadora.get(i);
					}
				}
			}
			
			if (dataUser.getUser().getNiveloperacion().equals("usac")) {
				habilitarComer = false;
				for (int i = 0; i < listaComercializadora.size(); i++) {
					if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
						this.comercializadora = listaComercializadora.get(i);
					}
				}
			}
		}
	}

	public void seleccionarComer() {
		if (comercializadora != null) {
			codComer = comercializadora.getCodigo();
			//codAbas = comercializadora.getAbastecedora();
			
			listaClientes = clienteServicio.obtenerClientesPorComercializadora(codComer);
		}
	}

	public void seleccionarCliente() throws ParseException {
		// Inicializar o limpiar la lista de seleccionados para evitar datos residuales
		if (causalesSeleccionadas == null) {
			causalesSeleccionadas = new ArrayList<>();
		} else {
			causalesSeleccionadas.clear();
		}

		// Constantes de etiquetas fijos (Deben coincidir con actualizarObservacion)
		final String TAG_CAUSALES = "Causales:";
		final String TAG_OBSERVACION = "Observación:";

		if (cliente != null && cliente.getObservaciongd() != null && !cliente.getObservaciongd().trim().isEmpty()) {
			String observacionOriginal = cliente.getObservaciongd();
			String textoParaCausales = observacionOriginal;

			// =========================================================================
			// PASO 1: Extraer y limpiar el texto libre para el Textarea
			// =========================================================================
			if (observacionOriginal.contains(TAG_OBSERVACION)) {
				int indexObservacion = observacionOriginal.lastIndexOf(TAG_OBSERVACION);

				// Extraemos solo el texto que va DESPUÉS de "Observación:"
				String textoLimpioTextarea = observacionOriginal.substring(indexObservacion + TAG_OBSERVACION.length());

				// ACÁ ESTÁ EL TRUCO: Modificamos el campo del objeto que lee la interfaz para que el textarea muestre solo el texto limpio sin las etiquetas.
				this.cliente.setObservaciongd(textoLimpioTextarea.trim());

				// Para procesar los checkboxes en el PASO 2, usamos solo la sección de las causales
				textoParaCausales = observacionOriginal.substring(0, indexObservacion);
			}

			// Quitar la etiqueta inicial "Causales:" si existe para no interferir con el split
			if (textoParaCausales.contains(TAG_CAUSALES)) {
				textoParaCausales = textoParaCausales.replace(TAG_CAUSALES, "");
			}

			// =========================================================================
			// PASO 2: Cruzar y marcar los Checkboxes basados en el catálogo
			// =========================================================================
			String[] partesCausales = textoParaCausales.split(";");

			if (listaCausales != null && partesCausales.length > 0) {
				for (String causalTexto : partesCausales) {
					String textoLimpio = causalTexto.trim();

					if (!textoLimpio.isEmpty()) {
						for (Causal causal : listaCausales) {
							// Verificación robusta por ID (Controlando que el substring no falle si el texto es corto)
							String idCausalString = String.valueOf(causal.getId());
							if (textoLimpio.length() >= idCausalString.length()) {
								if (textoLimpio.substring(0, idCausalString.length()).equalsIgnoreCase(idCausalString)) {
									causalesSeleccionadas.add(causal.getId());
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	public void actualizarObservacion() {
		if (this.cliente == null) {
			this.cliente = new Cliente();
		}

		String textoExtraDinamico = "";
		String observacionActual = this.cliente.getObservaciongd();

		// Constantes de etiquetas para evitar errores de tipeo
		final String TAG_CAUSALES = "Causales:";
		final String TAG_OBSERVACION = "Observación:";

		// 1. Rescatar el texto dinámico usando la etiqueta fija "Observación:"
		if (observacionActual != null && !observacionActual.trim().isEmpty()) {
			if (observacionActual.contains(TAG_OBSERVACION)) {
				int indexObservacion = observacionActual.lastIndexOf(TAG_OBSERVACION) + TAG_OBSERVACION.length();
				
				if (indexObservacion < observacionActual.length()) {
					textoExtraDinamico = observacionActual.substring(indexObservacion);
					// Mantiene el texto extra digitado por el usuario intacto
				}
			} else {
				// Caso de respaldo: Si el texto viejo no tenía la etiqueta "Observación:", aplicamos tu lógica original buscando el último punto y coma.
				int ultimoPuntoYComaIndex = -1;
				
				for (Causal causal : listaCausales) {
					String descCausal = causal.getDescripcion().trim() + ";";
					if (observacionActual.contains(descCausal)) {
						int index = observacionActual.lastIndexOf(descCausal) + descCausal.length();
						if (index > ultimoPuntoYComaIndex) {
							ultimoPuntoYComaIndex = index;
						}
					}
				}
				
				if (ultimoPuntoYComaIndex != -1 && ultimoPuntoYComaIndex < observacionActual.length()) {
					textoExtraDinamico = observacionActual.substring(ultimoPuntoYComaIndex);
				} else {
					// Si no había ni causales ni etiquetas, todo el campo actual es el texto dinámico
					textoExtraDinamico = observacionActual;
				}
			}
		}

		// 2. Si el usuario desmarcó todo: Dejamos la etiqueta "Observación:" con su texto o vacío
		if (causalesSeleccionadas == null || causalesSeleccionadas.isEmpty()) {
			String resultadoVacio = textoExtraDinamico.trim().isEmpty() ? "" : TAG_OBSERVACION + " " + textoExtraDinamico;
			this.cliente.setObservaciongd(resultadoVacio);
			
			return;
		}

		// 3. Reconstruir las causales seleccionadas actualmente
		StringBuilder sb = new StringBuilder();
		sb.append(TAG_CAUSALES).append(" "); // Inicio obligatorio solicitado

		boolean tieneCausalesValidas = false;
		for (String idSeleccionado : causalesSeleccionadas) {
			if (idSeleccionado != null) {
				for (Causal causal : listaCausales) {
					if (String.valueOf(causal.getId()).equals(idSeleccionado.trim())) {
						sb.append(causal.getDescripcion().trim()).append("; ");
						tieneCausalesValidas = true;
						
						break;
					}
				}
			}
		}

		// Si por alguna razón ninguna coincidió, evitamos dejar la etiqueta "Causales:" sola
		if (!tieneCausalesValidas) {
			String resultadoVacio = textoExtraDinamico.trim().isEmpty() ? "" : TAG_OBSERVACION + " " + textoExtraDinamico;
			this.cliente.setObservaciongd(resultadoVacio);
			
			return;
		}

		// 4. Pegar la etiqueta "Observación:" y el texto dinámico al final
		sb.append(TAG_OBSERVACION + " ").append(textoExtraDinamico);

		// 5. Actualizar el componente
		this.cliente.setObservaciongd(sb.toString());
	}

	public String save() {
		editarCliente = true;
		try {
			if (editarCliente) {
				actualizarObservacion();

				if (editItems()) {
					// Ejecutamos el envío de email de forma segura
					boolean emailEnviado = enviarEmail();

					if (emailEnviado) {
						this.dialogo(FacesMessage.SEVERITY_INFO, "CLIENTE ACTUALIZADO EXITOSAMENTE Y EMAIL ENVIADO");
					} else {
						this.dialogo(FacesMessage.SEVERITY_WARN, "CLIENTE ACTUALIZADO, PERO EL EMAIL NO PUDO SER ENVIADO");
					}
				}
			}

			// Retornar null en JSF le dice al ciclo de vida que refresque la misma página actual limpiando componentes AJAX
			return null;

		} catch (Exception e) {
			// Al capturar la excepción aquí evitamos que rompa el árbol de renderizado de PrimeFaces
			System.err.println("Error crítico en el proceso de guardado de Gestión Directa: " + e.getMessage());
			e.printStackTrace();

			// Pintamos el error directamente en la interfaz del usuario para que sepa qué falló
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"Error de Sistema", "No se pudo completar la operación: " + e.getMessage()));
			
			return null;
		}
	}
	
	private boolean editItems() {
		boolean registroActualzado = false;
		try {
			//setearCampos();
			String respuesta;
			url = new URL(direccion + "/porId");
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(1000 * 60);
			connection.setReadTimeout(1000 * 60);
			connection.setDoOutput(true);
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Content-type", "application/json");
			// connection.setFixedLengthStreamingMode(1000000000);

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			ObjectMapper mapper = new ObjectMapper();
			String jsonStr = mapper.writeValueAsString(this.cliente);
			
			Gson gson = new Gson();
			String JSON = gson.toJson(this.cliente);
			
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.write(jsonStr.getBytes());
			out.flush();
			out.close();
			
			if (connection.getResponseCode() == 200) {
				PrimeFaces.current().executeScript("PF('editarDesp').hide()");
				//this.dialogo(FacesMessage.SEVERITY_INFO, "CLIENTE ACUTALIZADO EXITOSAMENTE");
				registroActualzado = true;
			} else {
				this.dialogo(FacesMessage.SEVERITY_ERROR, "ERROR AL ACTUALIZAR");
			}
			
			System.out.println(connection.getResponseCode());
			System.out.println(connection.getResponseMessage());
			
			return registroActualzado;
		} catch (IOException ioex) {
			ioex.printStackTrace();
			
			return registroActualzado;
		}
	}

	private boolean enviarEmail() {
		EnviarMail.setError("Inicia envío email con causales");
		EnviarMail.setCorreoErrores("roberth7777@gmail.com");
		
		String codigoNombreCliente = cliente.getClientePK().getCodigo() +" - " + cliente.getNombrecomercial(); 
		Date fechaVencimientoContrato = cliente.getFehavencimientocontrato();
		String observacionGD = cliente.getObservaciongd();
				
		return EnviarMail.sendEmailSincrono(codigoNombreCliente, fechaVencimientoContrato, observacionGD, dataUser.getUser().getNombrever()); //generateAndSendEmailSincrono();
	}
	
	public Boolean esMenorFechaHoy(Date fecha) throws Exception {
		Date hoy = new Date();
		Boolean resultado = false;
		
		if (fecha != null && fecha.before(hoy)) {
			resultado = true;
		}
		
		return resultado;
	}

	public void controlarFecha(SelectEvent<Date> event) {
		fechaFinVigencia = fechaInicioVigencia;
	}

	public List<ComercializadoraBean> getListaComercializadora() {
		return listaComercializadora;
	}

	public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
		this.listaComercializadora = listaComercializadora;
	}

	public String getCodComer() {
		return codComer;
	}

	public void setCodComer(String codComer) {
		this.codComer = codComer;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}

	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}

	public boolean isEditarCliente() {
		return editarCliente;
	}

	public void setEditarCliente(boolean editarCliente) {
		this.editarCliente = editarCliente;
	}

	public Date getFechaInicioVigencia() {
		return fechaInicioVigencia;
	}

	public void setFechaInicioVigencia(Date fechaInicioVigencia) {
		this.fechaInicioVigencia = fechaInicioVigencia;
	}

	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	public void setFechaFinVigencia(Date fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
	}

	public ComercializadoraBean getComercializadora() {
		return comercializadora;
	}

	public void setComercializadora(ComercializadoraBean comercializadora) {
		this.comercializadora = comercializadora;
	}

	public boolean isHabilitarEstadoCliente() {
		return habilitarEstadoCliente;
	}

	public void setHabilitarEstadoCliente(boolean habilitarEstadoCliente) {
		this.habilitarEstadoCliente = habilitarEstadoCliente;
	}

	public List<Causal> getListaCausales() {
		return listaCausales;
	}

	public void setListaCausales(List<Causal> listaCausales) {
		this.listaCausales = listaCausales;
	}

	public List<String> getCausalesSeleccionadas() {
		return causalesSeleccionadas;
	}

	public void setCausalesSeleccionadas(List<String> causalesSeleccionadas) {
		this.causalesSeleccionadas = causalesSeleccionadas;
	}
}
