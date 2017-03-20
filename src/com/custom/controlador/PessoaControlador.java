package com.custom.controlador;

@Controlador
public class PessoaControlador {

	@Get(url = "/pessoa/cadastro")
	public String cadastrar() {
		return "/cadastroPessoa.jsp";
	}
	
	@Post(url = "/pessoa/adicionaPessoa")
	public String adicionaPessoa(String nome, String email) {
		return "/exibePessoa.jsp";
	}
}
