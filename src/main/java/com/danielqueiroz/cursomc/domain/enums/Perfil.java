package com.danielqueiroz.cursomc.domain.enums;

public enum Perfil {

	ADMIN(1, "ROLE_ADMIN"), CLIENTE(2, "ROLE_CLIENTE");

	private Integer cod;
	private String descricao;

	private Perfil(Integer cod, String descriacao) {
		this.cod = cod;
		this.descricao = descriacao;
	}

	public Integer getCod() {
		return cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Perfil toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}

		for (Perfil x : Perfil.values()) {
			if (cod.equals(x.cod)) {
				return x;
			}
		}

		throw new IllegalArgumentException("Id inválido: " + cod);
	}

}
