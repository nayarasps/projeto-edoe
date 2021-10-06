package entidades;

import java.io.Serializable;
import java.util.HashMap;


public abstract class Usuario implements Comparable<Usuario>, Serializable {
	protected static final long serialVersionUID = -2090136157996976518L;
	protected String nome;
	protected String email;
	protected String celular;
	protected String classe;
	protected String id;
	protected String status;

	protected HashMap<Integer, Item> itens;
	
	/**
	 * Metodo construtor de um Usuario.
	 * 
	 * @param nome que representa o nome do usuario.
	 * @param email que representa o email do usuario.
	 * @param celular que representa o celular do usuario.
	 * @param classe que representa a classe do usuario.
	 * @param id que representa o id do usuario.
	 */
	public Usuario(String nome, String email, String celular, String classe, String id) {
		this.nome = nome;
		this.email = email;
		this.celular = celular;
		this.classe = classe;
		this.id = id;
		this.itens = new HashMap<Integer, Item>();
	}
	
	/**
	 * Metodo que retorna o nome do usuario.
	 * 
	 * @return String.
	 */	
	public String getNome() {
		return nome;
	}
	
	/**
	 * Metodo que altera o nome do usuario.
	 * 
	 * @return void.
	 */	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * Metodo que altera o email do usuario.
	 * 
	 * @return void.
	 */	
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Metodo que altera o celular do usuario.
	 * 
	 * @return void.
	 */	
	public void setCelular(String celular) {
		this.celular = celular;
	}

	/**
	 * Metodo que altera a classe do usuario.
	 * 
	 * @return String.
	 */	
	public String getClasse() {
		return classe;
	}

	/**
	 * Metodo que retorna o id do usuario.
	 * 
	 * @return String.
	 */	
	public String getId() {
		return id;
	}
	
	public String getStatus() {
		return status;
	}
	
	public HashMap<Integer, Item> getItens() {
		return itens;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean verificaUsuarioPossuiItem(int idItem) {
		return itens.containsKey(idItem);
	}

	public Item getItemById(int idItem) {
		return itens.get(idItem);
	}

	public void removeItembyid(int idItem) {
		itens.remove(idItem);
	}

	@Override
	public String toString() {
			return this.nome + "/" + this.id + ", " + this.email + ", " + this.celular + ", status: " + this.status;
		}

	
	@Override
	public int compareTo(Usuario o) {
		return this.nome.compareTo(((Usuario) o).nome);
	}

}
