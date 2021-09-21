package controller;

public class Validacao {
	
	public void verificaNome(String nome) {
		if ((nome == null) || ("".equals(nome.trim()))) {
			throw new IllegalArgumentException("Entrada invalida: nome nao pode ser vazio ou nulo.");
		}
	}
	
	public void verificaIdUsuario(String id) {
		if ((id == null) || "".equals(id.trim())) {
			throw new IllegalArgumentException("Entrada invalida: id do usuario nao pode ser vazio ou nulo.");
		}
	}
	
	public void verificaIdItem(int idItem) {
		if (idItem < 0) {
			throw new IllegalArgumentException("Entrada invalida: id do item nao pode ser negativo.");
		}
	}

	public void verificaDescritorItem(String descricao) {
		if ((descricao == null) || ("".equals(descricao.trim()))) {
			throw new IllegalArgumentException("Entrada invalida: descricao nao pode ser vazia ou nula.");
		}
	}
	
	public void usuarioNaoEncontrado(String idUsuario) {
		throw new IllegalArgumentException("Usuario nao encontrado: " + idUsuario + ".");
	}
	
	public void itemNaoEncontrado(int idItem) {
		throw new IllegalArgumentException("Item nao encontrado: " + idItem + ".");
	}
	
	public void validaClasseUsuario(String classe) {
		if (!("PESSOA_FISICA".equals(classe.toUpperCase().trim()) || "IGREJA".equals(classe.toUpperCase().trim()) || "ORGAO_PUBLICO_MUNICIPAL".equals(classe.toUpperCase().trim())
				|| "ORGAO_PUBLICO_ESTADUAL".equals(classe.toUpperCase().trim()) || "ORGAO_PUBLICO_FEDERAL".equals(classe.toUpperCase().trim()) 
				|| "ONG".equals(classe.toUpperCase().trim()) || "ASSOCIACAO".equals(classe.toUpperCase().trim()) || "SOCIEDADE".equals(classe.toUpperCase().trim()))) {
			throw new IllegalArgumentException("Entrada invalida: opcao de classe invalida.");
		}
	}
	
	public void verificaAdicionaItem(String descricaoItem, int quantidade) {
		verificaDescritorItem(descricaoItem);
		if (quantidade <= 0) {
			 throw new IllegalArgumentException("Entrada invalida: quantidade deve ser maior que zero.");
		 }
	}
	
	public void verificaAdicionaUsuario(String nome, String email, String celular, String classe, String id) {
		verificaNome(nome);
		verificaIdUsuario(id);
		if ((email == null) || ("".equals(email.trim()))) {
			throw new IllegalArgumentException("Entrada invalida: email nao pode ser vazio ou nulo.");
		}
		if ((celular == null) || "".equals(celular.trim())) {
			throw new IllegalArgumentException("Entrada invalida: celular nao pode ser vazio ou nulo.");
		}
		if ((classe == null) || ("".equals(classe.trim()))) {
			 throw new IllegalArgumentException("Entrada invalida: classe nao pode ser vazia ou nula.");
		 }
	}
	
	public void verificaTextoPesquisa(String descricao) {
		if ((descricao == null) || ("".equals(descricao.trim()))) {
			throw new IllegalArgumentException("Entrada invalida: texto da pesquisa nao pode ser vazio ou nulo.");
		}
	}
	
	public void verificaData(String data) {
		if ((data == null) || ("".equals(data.trim()))) {
			throw new IllegalArgumentException("Entrada invalida: data nao pode ser vazia ou nula.");
		}
	}
}
