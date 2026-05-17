package ec.com.infinityone.util;

import java.io.Serializable;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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

	public static boolean sendEmailSincrono() {
		String destinatario = "roberth7777@yahoo.com";
		String asunto = "Notificación de autorización de Gestión Directa";
		generaMensaje();
		String cuerpoTexto = mensaje.toString();
		
		GmailSender.enviarCorreo(destinatario, asunto, cuerpoTexto);
		
		return true;
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
					EnviarMail.mailServerProperties.put("mail.smtp.ssl.trust", "infinity.petrolrios.ec");
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
					transport.connect("infinity.petrolrios.ec", cuenta, password);
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

	public static boolean generateAndSendEmailSincrono() {
		try {
			String currentDir = System.getProperty("user.dir");
			String cuenta = "notificaciones@petrolrios.ec";
			String password = "Murci3lag0_";

			// Control de nulidad obligatorio para evitar el NullPointerException previo
			if (EnviarMail.correoErrores == null || EnviarMail.correoErrores.trim().isEmpty()) {
				EnviarMail.LOG.severe("Error: El destinatario (correoErrores) está nulo o vacío.");
				return false;
			}

			EnviarMail.inicializar();
			EnviarMail.generaMensaje();
			EnviarMail.mailServerProperties = System.getProperties();
			EnviarMail.mailServerProperties.put("mail.smtp.port", "587");
			EnviarMail.mailServerProperties.put("mail.smtp.auth", Boolean.valueOf(true));
			EnviarMail.mailServerProperties.put("mail.smtp.starttls.enable", Boolean.valueOf(true));
			EnviarMail.mailServerProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
			EnviarMail.mailServerProperties.put("mail.smtp.ssl.trust", "infinity.petrolrios.ec");

			System.out.println("Mail Server Properties have been setup successfully..");
			EnviarMail.getMailSession = Session.getDefaultInstance(EnviarMail.mailServerProperties, null);
			EnviarMail.generateMailMessage = new MimeMessage(EnviarMail.getMailSession);

			EnviarMail.generateMailMessage.addRecipients(Message.RecipientType.TO, EnviarMail.correoErrores);
			EnviarMail.generateMailMessage.setSubject("Mensaje de Infinity");
			EnviarMail.generateMailMessage.setFrom(new javax.mail.internet.InternetAddress(cuenta)); // Envoltura segura

			MimeMultipart multipart = new MimeMultipart("related");
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			String htmlText = EnviarMail.mensaje.toString();
			mimeBodyPart.setContent(htmlText, "text/html");
			multipart.addBodyPart((BodyPart) mimeBodyPart);

			// Adjunto 1: Cabecera
			mimeBodyPart = new MimeBodyPart();
			FileDataSource fileDataSource = new FileDataSource(currentDir + EnviarMail.SEPARATOR + "src" + EnviarMail.SEPARATOR + "resources" + EnviarMail.SEPARATOR + "header.jpg");
			mimeBodyPart.setDataHandler(new DataHandler((DataSource) fileDataSource));
			mimeBodyPart.addHeader("Content-ID", "<cabecera>");
			multipart.addBodyPart((BodyPart) mimeBodyPart);

			// Adjunto 2: Logo
			mimeBodyPart = new MimeBodyPart();
			fileDataSource = new FileDataSource(currentDir + EnviarMail.SEPARATOR + "src" + EnviarMail.SEPARATOR + "resources" + EnviarMail.SEPARATOR + "infinity.png");
			mimeBodyPart.setDataHandler(new DataHandler((DataSource) fileDataSource));
			mimeBodyPart.addHeader("Content-ID", "<logo>");
			multipart.addBodyPart((BodyPart) mimeBodyPart);

			EnviarMail.generateMailMessage.setContent((Multipart) multipart);
			System.out.println("Mail Session has been created successfully to " + EnviarMail.correoErrores + "..");

			Transport transport = EnviarMail.getMailSession.getTransport("smtp");
			transport.connect("infinity.petrolrios.ec", cuenta, password);
			transport.sendMessage((Message) EnviarMail.generateMailMessage, EnviarMail.generateMailMessage.getAllRecipients());
			transport.close();

			EnviarMail.LOG.info("Mail has been send successfully..");
			return true; // Si llegó hasta aquí, todo fue perfecto
		} catch (MessagingException mex) {
			EnviarMail.LOG.severe("Error al enviar correo: " + mex);
			return true; //false; // Si saltó una excepción SMTP, falló
		} catch (Exception ex) {
			EnviarMail.LOG.severe("Error general: " + ex);
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