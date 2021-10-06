package entidades;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import util.StringFormat;

/**
 * 
 * Classe base responsavel pelo armazenamento das informacoes de um item.
 * O item esta sempre associado a um usuario, existente em suas informacoes.
 * A pontuacao total do Match do item tambem e armazenada no proprio item;
 *
 */
//TODO REMOVER USER DE ITEM DEPOIS DA REFATORAÇÃO
public class Item implements Serializable{

	private static final long serialVersionUID = 1808448751837784362L;
	private String descricao;
	private int id;
	private String tags;
	private int quantidade;
	private Usuario user;
	private int pontuacaoMatch;
	
	/**
	 * Metodo construtor de Item.
	 * 
	 * @param descricao que representa a descricao de um Item.
	 * @param tags que representa as tags que um Item possui.
	 * @param quantidade que representa a quantidade dos itens.
	 */
	public Item(String descricao, String tags, int quantidade) {
		this.descricao = descricao;
		this.tags = tags;
		this.quantidade = quantidade;
	}

	/**
	 * Metodo que retorna a descricao de um item.
	 * 
	 * @return String.
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Metodo que retorna o id de um item.
	 * 
	 * @return int.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Metodo que altera o id de um item.
	 * 
	 * @return void.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Metodo que retorna as tags de um item.
	 * 
	 * @return String.
	 */
	public String getTags() {
		return tags;
	}
	
	/**
	 * Metodo que altera as tags de um item.
	 * 
	 * @return void.
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	/**
	 * Metodo que retorna a quantidade de um item.
	 * 
	 * @return int.
	 */
	public int getQuantidade() {
		return quantidade;
	}
	
	/**
	 * Metodo que altera a quantidade de um item.
	 * 
	 * @return void.
	 */
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	
	public String tagsToString(String tags) {
		String[] tagString = tags.split(",");
		return Arrays.toString(tagString);
	}
	
	/**
	 * Representacao textual de um Item.
	 * 
	 * @return String.
	 */
	@Override
	public String toString() {
		return id + " - " + descricao + ", tags: " + tagsToString(this.tags) +
				", quantidade: " + quantidade;
	}

	/**
	 * Metodo que retorna o Usuario.
	 *
	 * @return Usuario.
	 */
	public Usuario getUser() {
		return user;
	}

	/**
	 * Metodo que altera o Usuario.
	 *
	 * @return void.
	 */
	public void setUser(Usuario user) {
		this.user = user;
	}

	/**
	 * Metodo que retorna a pontuacao de match.
	 * 
	 * @return int.
	 */
	public int getPontuacaoMatch() {
		return pontuacaoMatch;
	}

	/**
	 * Metodo que altera a pontuacao de match.
	 * 
	 * @return void.
	 */
	public void setPontuacaoMatch(int pontuacaoMatch) {
		this.pontuacaoMatch = pontuacaoMatch;
	}
	
	public boolean comparaDescricaoItens(Item item) {
		return StringFormat.formatacaoString(this.descricao).
				equals(StringFormat.formatacaoString(item.getDescricao()));
	}
	
	public boolean comparaTagsItens(Item item) {
		return StringFormat.formatacaoString(this.tags).
				equals(StringFormat.formatacaoString(item.getTags()));
	}
	
	/**
	 * Metodo estatico que compara as descricoes dos itens.
	 * 
	 * @return int.
	 */
	public static Comparator<Item> comparaDescricao = new Comparator<Item>() {
		public int compare(Item i1, Item i2) {
		   return i1.getDescricao().toLowerCase().compareTo(i2.getDescricao().toLowerCase());
		}
	};
	
	
	/**
	 * Metodo estatico que compara a quantidade dos itens.
	 * 
	 * @return int.
	 */
	public static Comparator<Item> comparaQuantidade = new Comparator<Item>() {
		//Maior para a menor
		public int compare(Item i1, Item i2) {
			if (i1.getQuantidade() > i2.getQuantidade()) {
		          return -1;
		     }
		     if (i1.getQuantidade() < i2.getQuantidade()) {
		          return 1;
		     }
		     if (i1.getQuantidade() == i2.getQuantidade()) {
		    	 return i1.getDescricao().compareTo(i2.getDescricao());
		     }
		     return 0;
		}
	};

	
	/**
	 * Metodo estatico que compara a pontuacao de match dos itens.
	 * 
	 * @return int.
	 */
	public static Comparator<Item> comparaPontuacaoMatch = new Comparator<Item>() {
		public int compare(Item i1, Item i2) {
			// Maior para o menor
			if (i1.getPontuacaoMatch() > i2.getPontuacaoMatch()) {
		          return -1;
		     }
		     if (i1.getPontuacaoMatch() < i2.getPontuacaoMatch()) {
		          return 1;
		     }
		     if (i1.getPontuacaoMatch() == i2.getPontuacaoMatch()) {
		    	 return i1.getDescricao().compareTo(i2.getDescricao());
		     }
		     return 0;
		}
	};	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		return result;
	}
}
