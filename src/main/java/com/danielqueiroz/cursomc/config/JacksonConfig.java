package com.danielqueiroz.cursomc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.danielqueiroz.cursomc.domain.PagamentoComBoleto;
import com.danielqueiroz.cursomc.domain.PagamentoComCartao;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JacksonConfig {

	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
			@Override
			public void configure(ObjectMapper objMapper) {
				objMapper.registerSubtypes(PagamentoComBoleto.class);
				objMapper.registerSubtypes(PagamentoComCartao.class);
				super.configure(objMapper);
			}
		};
		
		return builder;
	}
	
}
