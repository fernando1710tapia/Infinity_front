package ec.com.infinityone.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import ec.com.infinityone.configuration.Fichero;
//import javax.websocket.Session;

public class EnviarMail implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(EnviarMail.class.getName());

	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;
	private static final String SEPARATOR = FileSystems.getDefault().getSeparator();
	static StringBuilder mensaje;
	static String error = "";
	static List<String> direccion = new ArrayList<>();
	static int anio;
	static int mes;
	static int dia;
	static int hora;
	static int minuto;
	static int segundo;
	static String mesLetras;
	static String correoErrores;

	public static void inicializar() {
		Calendar fecha = Calendar.getInstance();
		anio = fecha.get(1);
		mes = fecha.get(2) + 1;
		dia = fecha.get(5);
		hora = fecha.get(11);
		minuto = fecha.get(12);
		segundo = fecha.get(13);
		// mesLetras = ArchivoUtils.mesLetras(mes);
	}

	public static boolean sendEmailSincrono(String codigoNombreCliente, Date fechaVencimientoContrato, String observacionGD, String usuario) {
		String destinatario = Fichero.getDESTINATARIOGESTIONDIRECTA(); //"roberth7777@yahoo.com";
		String asunto = "Notificación de autorización de Gestión Directa";

		try {
			String cuerpoEmail = cargarTemplateHtmlEmail("NotificacionAutorizacionCliente.html");

			// --- 1. Formateo de las fechas del SISTEMA (Igual que antes) ---
			java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
			java.util.Locale localeEspanol = new java.util.Locale("es", "EC");

			java.time.format.DateTimeFormatter formatoFechaLarga = java.time.format.DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", localeEspanol);
			java.time.format.DateTimeFormatter formatoHora = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");

			String fechaSistema = ahora.format(formatoFechaLarga);
			if (fechaSistema != null && !fechaSistema.isEmpty()) {
				fechaSistema = fechaSistema.substring(0, 1).toUpperCase() + fechaSistema.substring(1);
			}
			
			String horaSistema = ahora.format(formatoHora);

			// --- 2. NUEVO: Formateo de la FECHA DE VENCIMIENTO que llega por parámetro ---
			String fechaVencimientoFormateada = "";

			if (fechaVencimientoContrato != null) {
				// Convertimos el java.util.Date antiguo al nuevo java.time.Instant para formatearlo de forma larga
				java.time.LocalDate localDateVencimiento = fechaVencimientoContrato.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

				// Aplicamos el formato de fecha larga que definimos arriba
				fechaVencimientoFormateada = localDateVencimiento.format(formatoFechaLarga);

				// Ponemos la primera letra en mayúscula (Ej: "Viernes 22 de mayo...")
				if (fechaVencimientoFormateada != null && !fechaVencimientoFormateada.isEmpty()) {
					fechaVencimientoFormateada = fechaVencimientoFormateada.substring(0, 1).toUpperCase()+ fechaVencimientoFormateada.substring(1);
				}
			}

			// --- 3. Reemplazos en el HTML ---
			cuerpoEmail = cuerpoEmail.replace("$F_FECHA_SISTEMA", fechaSistema);
			cuerpoEmail = cuerpoEmail.replace("$F_HORA_SISTEMA", horaSistema);
			cuerpoEmail = cuerpoEmail.replace("$F_USUARIO", usuario != null ? usuario : "");
			cuerpoEmail = cuerpoEmail.replace("$F_CLIENTE", codigoNombreCliente != null ? codigoNombreCliente : "");
			cuerpoEmail = cuerpoEmail.replace("$F_OBSERVACIONGD", observacionGD != null ? observacionGD : "");

			// Inyectamos la variable que acabamos de transformar
			cuerpoEmail = cuerpoEmail.replace("$F_FECHAVMTOCONTRATO", fechaVencimientoFormateada);

			// 4. Envío del correo
			//GmailSender.enviarCorreo(destinatario, asunto, cuerpoEmail);
			generateAndSendEmailSincrono(destinatario, asunto, cuerpoEmail);

			return true;

		} catch (Exception e) {
			System.err.println("Error al cargar la plantilla o enviar el correo: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	public static void generateAndSendEmail() {
		Thread t = new Thread(new Runnable() {
			public String toString() {
				return super.toString();
			}

			public void run() {
				try {
					String currentDir = System.getProperty("user.dir");
					String cuenta = "notificaciones@petrolrios.ec";
					String password = "Murci3lag0_";
					EnviarMail.inicializar();
					EnviarMail.generaMensaje();
					EnviarMail.mailServerProperties = System.getProperties();
					EnviarMail.mailServerProperties.put("mail.smtp.port", "587");
					EnviarMail.mailServerProperties.put("mail.smtp.auth", Boolean.valueOf(true));
					EnviarMail.mailServerProperties.put("mail.smtp.starttls.enable", Boolean.valueOf(true));
					EnviarMail.mailServerProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
					EnviarMail.mailServerProperties.put("mail.smtp.ssl.trust", "mail.petrolrios.ec");
					System.out.println("Mail Server Properties have been setup successfully..");
					EnviarMail.getMailSession = Session.getDefaultInstance(EnviarMail.mailServerProperties, null);
					EnviarMail.generateMailMessage = new MimeMessage(EnviarMail.getMailSession);
					EnviarMail.generateMailMessage.addRecipients(Message.RecipientType.TO, EnviarMail.correoErrores);
					EnviarMail.generateMailMessage.setSubject("Mensaje de Infinity");
					EnviarMail.generateMailMessage.setFrom(cuenta);
					EnviarMail.generateMailMessage.setText(EnviarMail.mensaje.toString());
					
					MimeMultipart multipart = new MimeMultipart("related");
					MimeBodyPart mimeBodyPart = new MimeBodyPart();
					String htmlText = EnviarMail.mensaje.toString();
					mimeBodyPart.setContent(htmlText, "text/html");
					multipart.addBodyPart((BodyPart) mimeBodyPart);
					mimeBodyPart = new MimeBodyPart();
					
					FileDataSource fileDataSource = new FileDataSource(currentDir + EnviarMail.SEPARATOR + "src" + EnviarMail.SEPARATOR + "resources" + EnviarMail.SEPARATOR + "header.jpg");
					mimeBodyPart.setDataHandler(new DataHandler((DataSource) fileDataSource));
					mimeBodyPart.addHeader("Content-ID", "<cabecera>");
					multipart.addBodyPart((BodyPart) mimeBodyPart);
					mimeBodyPart = new MimeBodyPart();
					
					fileDataSource = new FileDataSource(currentDir + EnviarMail.SEPARATOR + "src" + EnviarMail.SEPARATOR + "resources" + EnviarMail.SEPARATOR + "infinity.png");
					mimeBodyPart.setDataHandler(new DataHandler((DataSource) fileDataSource));
					mimeBodyPart.addHeader("Content-ID", "<logo>");
					multipart.addBodyPart((BodyPart) mimeBodyPart);
					
					EnviarMail.generateMailMessage.setContent((Multipart) multipart);
					System.out.println("Mail Session has been created successfully to " + EnviarMail.correoErrores + "..");
					Transport transport = EnviarMail.getMailSession.getTransport("smtp");
					transport.connect("mail.petrolrios.ec", cuenta, password);
					transport.sendMessage((Message) EnviarMail.generateMailMessage, EnviarMail.generateMailMessage.getAllRecipients());
					transport.close();
					EnviarMail.LOG.info("Mail has been send successfully..");
				} catch (MessagingException mex) {
					LOG.severe("Error al enviar correo: " + mex);
				}
			}
		});
		t.start();
	}

	public static boolean generateAndSendEmailSincrono(String destinatario, String asunto, String cuerpoEmail) {
		try {
			String cuenta = "notificaciones@petrolrios.ec";
			String password = "Murci3lag0_";

			// 1. Control de nulidad correcto sobre el parámetro que entra al método
			if (destinatario == null || destinatario.trim().isEmpty()) {
				System.err.println("Error: El destinatario está nulo o vacío.");
				return false;
			} else {
				destinatario = destinatario.replace(";", ",");
			}

			// 2. Configuración de propiedades limpia
			Properties props = new Properties();
			props.put("mail.smtp.host", "mail.petrolrios.ec"); // ¡Faltaba mapear el host aquí!
			props.put("mail.smtp.port", "587"); // Si sigue dando "Refused", cambia a "465"
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.ssl.protocols", "TLSv1.2");
			props.put("mail.smtp.ssl.trust", "mail.petrolrios.ec");

			// 3. Crear instancia única de Sesión
			Session session = Session.getInstance(props, null);
			MimeMessage message = new MimeMessage(session);

			// 4. Configurar remitente y destinatarios estructurados por comas
			message.setFrom(new javax.mail.internet.InternetAddress(cuenta));
			message.addRecipients(Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(destinatario));
			message.setSubject(asunto != null ? asunto : "Mensaje de Infinity");

			// 5. Construcción del cuerpo Multipart (HTML + Imágenes incrustadas)
			MimeMultipart multipart = new MimeMultipart("related");

			// Bloque del Texto HTML
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(cuerpoEmail, "text/html; charset=utf-8");
			multipart.addBodyPart(htmlPart);

			// Bloque del LOGO (Carga segura desde Classpath / src/main/resources)
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			java.net.URL logoUrl = classLoader.getResource("infinity.png");

			if (logoUrl != null) {
				MimeBodyPart logoPart = new MimeBodyPart();
				logoPart.setDataHandler(new DataHandler(logoUrl));
				logoPart.addHeader("Content-ID", "<logo>");
				multipart.addBodyPart(logoPart);
			} else {
				System.out.println("Advertencia: No se encontró el archivo infinity.png en resources");
			}

			/*
			 * * NOTA: Si deseas volver a activar el Header, solo descomenta este bloque
			 *
			 * java.net.URL headerUrl = classLoader.getResource("header.jpg"); if (headerUrl
			 * != null) { MimeBodyPart headerPart = new MimeBodyPart();
			 * headerPart.setDataHandler(new DataHandler(headerUrl));
			 * headerPart.addHeader("Content-ID", "<cabecera>");
			 * multipart.addBodyPart(headerPart); }
			 */

			message.setContent(multipart);

			// 6. Conexión y envío al servidor SMTP
			Transport transport = session.getTransport("smtp");
			transport.connect("mail.petrolrios.ec", cuenta, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

			System.out.println("¡Correo enviado con éxito a: " + destinatario);
			return true;

		} catch (MessagingException mex) {
			System.err.println("Error de mensajería SMTP: " + mex.getMessage());
			mex.printStackTrace();
			return false; // CORREGIDO: Retornar falso si falla la conexión
		} catch (Exception ex) {
			System.err.println("Error general en el proceso: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
	}
	
	public static void generaMensaje() {
		mensaje = new StringBuilder("");
		mensaje.append("<div style='width:100%;' align='center'>")
				.append("<table width='100%' border='0' cellspacing='0' cellpadding='0'>").append("<tr>")
				.append("<td align='center' valign='top' style='background-color:#53636e;' bgcolor='#53636e;'>")
				.append("<br>").append("<br>").append("<table width='583' border='0' cellspacing='0' cellpadding='0'>")
				.append("<tr>")
				.append("<td align='left' valign='top' bgcolor='#FFFFFF' style='background-color:#FFFFFF;'><img src='cid:cabecera' width='583' height='118'></td>")
				.append("</tr>").append("<tr>")
				.append("<td align='left' valign='top' bgcolor='#FFFFFF' style='background-color:#FFFFFF;'><table width='100%' border='0' cellspacing='0' cellpadding='0'>")
				.append("<tr>").append("<td width='35' align='left' valign='top'>&nbsp;</td>")
				.append("<td align='left' valign='top'><table width='100%' border='0' cellspacing='0' cellpadding='0'>")
				.append("<tr>").append("<td align='center' valign='top'>")
				.append("<div style='color:#245da5; font-family:Times New Roman, Times, serif; font-size:48px;'>Notificaci&oacute;n Factura Electr&oacute;nica</div>")
				.append("<div style='font-family: Verdana, Geneva, sans-serif; color:#898989; font-size:12px;'>")
				.append(mesLetras).append(" ").append(dia).append(", ").append(anio).append("</div></td>")
				.append("</tr>").append("<tr>")
				.append("<td align='center' valign='top'><img src='cid:logo' vspace='10'></td>").append("</tr>")
				.append("<tr>")
				.append("<td align='left' valign='top' style='font-family:Arial, Helvetica, sans-serif; font-size:12px; color:#525252;'>")
				.append("<div style='color:#3482ad; font-size:19px;'>Informac&oacute;n de la aplicaci&oacute;n</div>")
				.append("<br>Mensaje generado: <font color='ff0a32'><b>").append(error)
				.append("</b></font><br><br>Favor tomar en cuenta para el correcto funcionamiento.<br>").append("<br>")
				.append("</td>").append("</tr>").append("<tr>")
				.append("<td align='left' valign='top' style='font-family:Arial, Helvetica, sans-serif; font-size:12px; color:#525252;'>&nbsp;</td>")
				.append("</tr>").append("</table></td>").append("<td width='35' align='left' valign='top'>&nbsp;</td>")
				.append("</tr>").append("</table></td>").append("</tr>").append("<tr>")
				.append("<td align='left' valign='top' bgcolor='#3d90bd' style='background-color:#3d90bd;'><table width='100%' border='0' cellspacing='0' cellpadding='0'>")
				.append("<tr>").append("<td width='35'>&nbsp;</td>")
				.append("<td height='50' valign='middle' style='color:#FFFFFF; font-size:11px; font-family:Arial, Helvetica, sans-serif;'><b>")
				.append("Sistema de Facturacion").append("</b><br>").append("Infinity One Development, 2022")
				.append("</td>").append("<td width='35'>&nbsp;</td>").append("</tr>").append("</table></td>")
				.append("</tr>").append("</table>").append("<br>").append("<br></td>").append("</tr>")
				.append("</table>").append("</div>");
	}
	
	/**
	 * Carga el template de notificación de email
	 * @param nombreTemplateHTML
	 * @return contenido
	 * @throws Exception
	 * @author ftpaia
	 * @date 2026-05-18
	 */
	public static String cargarTemplateHtmlEmail(String nombreTemplateHTML) throws Exception {
		ClassLoader classLoader = EnviarMail.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(nombreTemplateHTML);

        if (inputStream == null) {
            throw new IOException("No se encontró el archivo: " + nombreTemplateHTML);
        }

        BufferedReader reader = null;
        StringBuffer contenido = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        }catch(Exception ex) {
        	ex.printStackTrace();
        }finally {
            if (reader != null) try { reader.close(); } catch (IOException e) { /* ignorar */ }
            try { inputStream.close(); } catch (IOException e) { /* ignorar */ }
        }

        return contenido.toString();
	}
	
	public static String getError() {
		return error;
	}

	public static void setError(String error) {
		EnviarMail.error = error;
	}

	public static List<String> getDireccion() {
		return direccion;
	}

	public static void setDireccion(List<String> direccion) {
		EnviarMail.direccion = direccion;
	}

	public static String getCorreoErrores() {
		return correoErrores;
	}

	public static void setCorreoErrores(String correoErrores) {
		EnviarMail.correoErrores = correoErrores;
	}
}