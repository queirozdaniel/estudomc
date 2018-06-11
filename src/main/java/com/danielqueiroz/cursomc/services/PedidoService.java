package com.danielqueiroz.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danielqueiroz.cursomc.domain.Cliente;
import com.danielqueiroz.cursomc.domain.ItemPedido;
import com.danielqueiroz.cursomc.domain.PagamentoComBoleto;
import com.danielqueiroz.cursomc.domain.Pedido;
import com.danielqueiroz.cursomc.domain.enums.EstadoPagamento;
import com.danielqueiroz.cursomc.repositories.ItemPedidoRepository;
import com.danielqueiroz.cursomc.repositories.PagamentoRepository;
import com.danielqueiroz.cursomc.repositories.PedidoRepository;
import com.danielqueiroz.cursomc.security.UserSS;
import com.danielqueiroz.cursomc.services.exceptions.AuthorizationException;
import com.danielqueiroz.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService ClienteService;
	@Autowired
	private BoletoService boletoService;
	@Autowired
	private ProdutoService produtoService;
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		return pedido.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + " ,Tipo: " + Pedido.class.getName()));
	}

	@Transactional
	public Pedido insert(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.setCliente(ClienteService.find(pedido.getCliente().getId()));
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		if (pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, pedido.getInstante());
		}
		pedido = pedidoRepository.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());

		for (ItemPedido item : pedido.getItens()) {
			item.setDesconto(0.0);
			item.setProduto(produtoService.find(item.getProduto().getId()));
			item.setPreco(item.getProduto().getPreco());
			item.setPedido(pedido);
		}

		itemPedidoRepository.saveAll(pedido.getItens());
		emailService.sendOrderConfirmationHtmlEmail(pedido);
		return pedido;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = ClienteService.find(user.getId());
		
		return pedidoRepository.findByCliente(cliente, pageRequest);
	}

}
