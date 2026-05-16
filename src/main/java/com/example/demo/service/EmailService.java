package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${app.from-email}")
	private String fromEmail;

	@Value("${app.backend-url}")
	private String backendUrl;

	public void sendVerificationEmail(String toEmail, String name, String token) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(fromEmail, "El Rincón Gaditano");
			helper.setTo(toEmail);
			helper.setSubject("Verifica tu cuenta en El Rincón Gaditano");

			String verificationUrl = backendUrl + "/auth/verify?token=" + token;
			String htmlContent = String.format("<h2>¡Bienvenido a El Rincón Gaditano, %s!</h2>"
					+ "<p>Para poder activar su cuenta, haga click en el siguiente enlace:</p>"
					+ "  <div style='text-align: center; margin: 30px 0;'>"
					+ "    <a href='%s' style='background-color: #FB8C00; color: white; padding: 12px 25px; text-decoration: none; font-weight: bold; border-radius: 5px; display: inline-block;'>Verificar mi cuenta</a>"
					+ "  </div>" + "<p>Este enlace de verificación caducará en 1 hora</p>", name, verificationUrl);
			helper.setText(htmlContent, true);
			mailSender.send(message);

		} catch (Exception e) {
			throw new RuntimeException("Error al enviar el email: " + e.getMessage());
		}
	}

}
