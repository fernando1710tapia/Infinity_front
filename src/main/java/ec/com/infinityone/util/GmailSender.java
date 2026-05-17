package ec.com.infinityone.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GmailSender {

	public static boolean enviarCorreo(String destinatario, String asunto, String cuerpoTexto) {
		// 1. Configurar las propiedades del servidor SMTP de Google
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587"); // Puerto estándar para TLS/STARTTLS
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true"); // Obligatorio para Google
		props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Forzar protocolo seguro moderno

		// Datos de autenticación
		final String USUARIO = "roberth7777@gmail.com";
		final String PASSWORD = "ysoe bahg vypt pabc"; // Tu contraseña de aplicación de 16 dígitos

		// 2. Crear la sesión autenticada
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USUARIO, PASSWORD);
			}
		});

		try {
			// 3. Construir el mensaje
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(USUARIO));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
			message.setSubject(asunto);

			// Si quieres enviar HTML en lugar de texto plano, usa:
			// message.setContent(cuerpoTexto, "text/html; charset=utf-8");
			message.setText(cuerpoTexto);

			// 4. Enviar el correo electrónico
			Transport.send(message);
			System.out.println("¡Correo enviado con éxito mediante Gmail SMTP!");
			
			return true;
		} catch (MessagingException mex) {
			System.err.println("Fallo en la conexión o autenticación SMTP: " + mex.getMessage());
			mex.printStackTrace();
			
			return false;
		}
	}

	public static void main(String[] args) {
		// Prueba de ejecución
		enviarCorreo("roberth7777@yahoo.com", "Prueba desde Java", "Hola, este es un correo de prueba.");
	}
}