package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailService {

	@Value("${SENDGRID_API_KEY}")
	private String sendGridApiKey;

	@Value("${SENDGRID_FROM_EMAIL}")
	private String fromEmail;

	@Value("${APP_BACKEND_URL}")
	private String backendUrl;

	public void sendVerificationEmail(String toEmail, String name, String token) {

		Email from = new Email(fromEmail, "El Rincón Gaditano");
		Email to = new Email(toEmail);
		String subject = "Verifica tu cuenta en El Rincón Gaditano";

		String verificationUrl = backendUrl + "/auth/verify?token=" + token;
		String htmlContent = String.format("<h2>¡Bienvenido a El Rincón Gaditano, %s!</h2>"
				+ "<p>Para poder activar su cuenta, haga click en el siguiente enlace:</p>"
				+ "  <div style='text-align: center; margin: 30px 0;'>"
				+ "    <a href='%s' style='background-color: #FB8C00; color: white; padding: 12px 25px; text-decoration: none; font-weight: bold; border-radius: 5px; display: inline-block;'>Verificar mi cuenta</a>"
				+ "  </div>" + "<p>Este enlace de verificación caducará en 1 hora</p>", name, verificationUrl);

		Content content = new Content("text/html", htmlContent);
		Mail mail = new Mail(from, subject, to, content);

		SendGrid sendgrid = new SendGrid(sendGridApiKey);
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			sendgrid.api(request);
		} catch (Exception e) {
			throw new RuntimeException("Error al enviar el email: " + e.getMessage());
		}
	}

}
