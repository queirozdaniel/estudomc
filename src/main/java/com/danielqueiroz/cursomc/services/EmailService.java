package com.danielqueiroz.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.danielqueiroz.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);
}
