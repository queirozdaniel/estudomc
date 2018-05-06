package com.danielqueiroz.cursomc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.danielqueiroz.cursomc.domain.Categoria;
import com.danielqueiroz.cursomc.domain.Cidade;
import com.danielqueiroz.cursomc.domain.Cliente;
import com.danielqueiroz.cursomc.domain.Endereco;
import com.danielqueiroz.cursomc.domain.Estado;
import com.danielqueiroz.cursomc.domain.Produto;
import com.danielqueiroz.cursomc.domain.enums.TipoCliente;
import com.danielqueiroz.cursomc.repositories.CategoriaRepository;
import com.danielqueiroz.cursomc.repositories.CidadeRepository;
import com.danielqueiroz.cursomc.repositories.ClienteRepository;
import com.danielqueiroz.cursomc.repositories.EnderecoRepository;
import com.danielqueiroz.cursomc.repositories.EstadoRepository;
import com.danielqueiroz.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursoMcApplication implements CommandLineRunner{

	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(CursoMcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		
		Produto p1 = new Produto(null, "Computador", 2000.0);
		Produto p2 = new Produto(null, "Impressora", 800.0);
		Produto p3 = new Produto(null, "Mouse", 80.0);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().add(p2);

		p1.getCategorias().add(cat1);
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().add(cat1);
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2,c3));
		
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail", "123.456.678-00", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("30052205","9391192324"));
		
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apt 203", "Jardim", "30220834",cli1 ,c1);
		Endereco e2 = new Endereco(null, "Av. Matos", "105", "Sala 800", "Centro", "38777012",cli1 ,c2);

		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		
		estadoRepository.saveAll(Arrays.asList(est1,est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));
		categoriaRepository.saveAll(Arrays.asList(cat1,cat2));
		produtoRepository.saveAll(Arrays.asList(p1,p2,p3));
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1,e2));
	
	}
}
