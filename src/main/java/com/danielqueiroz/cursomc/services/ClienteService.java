package com.danielqueiroz.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danielqueiroz.cursomc.domain.Cliente;
import com.danielqueiroz.cursomc.repositories.ClienteRepository;
import com.danielqueiroz.cursomc.services.exceptions.ObjectNotFoundException;


@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	public Cliente buscar(Integer id){
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.orElseThrow(() -> 	new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id +" ,Tipo: " + Cliente.class.getName()));
	}
	
}
