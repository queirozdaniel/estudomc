package com.danielqueiroz.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.danielqueiroz.cursomc.domain.PagamentoComBoleto;

@Service
public class BoletoService {

	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instante) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(instante);
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		pagto.setDataVencimento(calendar.getTime());
		
	}

}
