package com.danielqueiroz.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.danielqueiroz.cursomc.domain.Cliente;
import com.danielqueiroz.cursomc.domain.enums.TipoCliente;
import com.danielqueiroz.cursomc.dto.ClienteNewDTO;
import com.danielqueiroz.cursomc.repositories.ClienteRepository;
import com.danielqueiroz.cursomc.resources.exception.FieldMessage;
import com.danielqueiroz.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO>{

	@Autowired
	private ClienteRepository clienteRepository; 
	
	@Override
	public void initialize(ClienteInsert ann) {
	}
	
	@Override
	public boolean isValid(ClienteNewDTO newDto, ConstraintValidatorContext context) {
		List<FieldMessage> lista = new ArrayList<>();
		
		if (newDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(newDto.getCpfOuCnpj())) {
			lista.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		
		if (newDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(newDto.getCpfOuCnpj())) {
			lista.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}
		
		Cliente cli = clienteRepository.findByEmail(newDto.getEmail());
		if (cli != null) {
			lista.add(new FieldMessage("email", "Email já existente"));
		}
		
		
		for (FieldMessage fieldMessage : lista) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(fieldMessage.getMessage())
					.addPropertyNode(fieldMessage.getFieldName()).addConstraintViolation();
		}
		
		return lista.isEmpty();
	}

}
