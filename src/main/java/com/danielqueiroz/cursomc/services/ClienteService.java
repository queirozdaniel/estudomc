package com.danielqueiroz.cursomc.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.danielqueiroz.cursomc.domain.Cidade;
import com.danielqueiroz.cursomc.domain.Cliente;
import com.danielqueiroz.cursomc.domain.Endereco;
import com.danielqueiroz.cursomc.domain.enums.Perfil;
import com.danielqueiroz.cursomc.domain.enums.TipoCliente;
import com.danielqueiroz.cursomc.dto.ClienteDTO;
import com.danielqueiroz.cursomc.dto.ClienteNewDTO;
import com.danielqueiroz.cursomc.repositories.ClienteRepository;
import com.danielqueiroz.cursomc.repositories.EnderecoRepository;
import com.danielqueiroz.cursomc.security.UserSS;
import com.danielqueiroz.cursomc.services.exceptions.AuthorizationException;
import com.danielqueiroz.cursomc.services.exceptions.DataIntegrityException;
import com.danielqueiroz.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private S3Service s3Service;

	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado!");
		}
		
		
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + " ,Tipo: " + Cliente.class.getName()));
	}

	@Transactional
	public Cliente insert(Cliente cliente) {
		cliente.setId(null);
		cliente = clienteRepository.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
	}

	public Cliente update(Cliente cliente) {
		Cliente novoCliente = find(cliente.getId());
		updateData(novoCliente, cliente);
		return clienteRepository.save(novoCliente);
	}

	private void updateData(Cliente novoCliente, Cliente cliente) {
		novoCliente.setNome(cliente.getNome());
		novoCliente.setEmail(cliente.getEmail());
	}

	public void delete(Integer id) {
		find(id);
		try {
			clienteRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma cliente que tenha pedidos");
		}
	}

	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO clienteDto) {
		return new Cliente(clienteDto.getId(), clienteDto.getNome(), clienteDto.getEmail(), null, null,null);
	}

	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(),
				TipoCliente.toEnum(objDto.getTipo()),passwordEncoder.encode(objDto.getSenha()));

		Cidade cidade = new Cidade(objDto.getCidadeId(), null, null);
		
		Endereco end = new Endereco(null,objDto.getLogadouro(), objDto.getNumero(), objDto.getComplemento(),
				objDto.getBairro(), objDto.getCep(), cli, cidade);
		
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		
		if (objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		
		if (objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		
		return cli;
	}

	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		URI uri = s3Service.uploadFile(multipartFile);
		
		Cliente cli = clienteRepository.findById(user.getId()).orElse(null);
		cli.setImageURL(uri.toString());
		clienteRepository.save(cli);
		
		return uri;
	}
	
}
