package com.danielqueiroz.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danielqueiroz.cursomc.domain.Pedido;
import com.danielqueiroz.cursomc.repositories.PedidoRepository;
import com.danielqueiroz.cursomc.services.exceptions.ObjectNotFoundException;


@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	public Pedido buscar(Integer id){
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		return pedido.orElseThrow(() -> 	new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id +" ,Tipo: " + Pedido.class.getName()));
	}
	
}
