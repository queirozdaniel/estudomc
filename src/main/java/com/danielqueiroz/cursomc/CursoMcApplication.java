package com.danielqueiroz.cursomc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.danielqueiroz.cursomc.services.S3Service;

@SpringBootApplication
public class CursoMcApplication implements CommandLineRunner{

	@Autowired
	private S3Service s3Service;

	public static void main(String[] args) {
		SpringApplication.run(CursoMcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		s3Service.uploadFile("/home/daniel/Downloads/Atlas.jpg");
	}
}
