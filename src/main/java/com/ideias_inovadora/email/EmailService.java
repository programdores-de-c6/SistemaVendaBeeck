package com.ideias_inovadora.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	private JavaMailSender javaMailSender;

	@Autowired
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void enviarEmail(String destinatario, String assunto, String conteudo) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(destinatario);
		message.setSubject(assunto);
		message.setText(conteudo);
		javaMailSender.send(message);
	}
}
