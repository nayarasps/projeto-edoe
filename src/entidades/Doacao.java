package entidades;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 
 * Classe responsavel pelo armazenamento das informacoes de uma doacao realizada pelo sistema.
 *
 */
public class Doacao implements Serializable {
	private static final long serialVersionUID = 2478088179251099985L;
	private Usuario usuarioDoador;
	private Usuario usuarioReceptor;
	private int quantidadeDoada;
	private String data;
	private final String descricaoItem;
	
	/**
	 * Metodo construtor de Doacao.
	 *
	 * @param usuarioDoador que representa o usuario doador do item.
	 * @param usuarioReceptor que representa o usuario receptor do item.
	 * @param quantidadeDoada que representa a quantidade doada.
	 * @param data que representa a data da doacao.
	 * @param descricaoItem que representa a descricao do item.
	 */
	public Doacao(Usuario usuarioDoador, Usuario usuarioReceptor, int quantidadeDoada, String data, String descricaoItem) {
		this.usuarioDoador = usuarioDoador;
		this.usuarioReceptor = usuarioReceptor;
		this.quantidadeDoada = quantidadeDoada;
		this.data = data;
		this.descricaoItem = descricaoItem;
	}

	/**
	 * Metodo que altera a quantidade doada.
	 * 
	 * @return void.
	 */
	public void setQuantidadeDoada(int quantidadeDoada) {
		this.quantidadeDoada = quantidadeDoada;
	}

	/**
	 * Metodo que retorna a data.
	 * 
	 * @return String.
	 */
	public String getData() {
		return data;
	}

	/**
	 * Metodo que retorna a descricao de um item.
	 * 
	 * @return String.
	 */
	public String getDescricaoItem() {
		return descricaoItem;
	}

	/**
	 * Representacao textual de uma doacao.
	 * 
	 * @return String.
	 */
	@Override
	public String toString() {
		return this.data + " - doador: " + this.usuarioDoador.getNome() + "/" + this.usuarioDoador.getId() + ", item: " + this.descricaoItem
				+ ", quantidade: " + this.quantidadeDoada + ", receptor: " + this.usuarioReceptor.getNome() + "/" + this.usuarioReceptor.getId();
	}
	
	/**
	 * Metodo estatico que compara as doacoes de acordo com suas datas.
	 * 
	 * @return int.
	 */
	public static Comparator<Doacao> comparaData = new Comparator<Doacao>() {
		public int compare(Doacao d1, Doacao d2) {
			// Antiga para a mais Nova
			if (d1.getData().compareTo(d2.getData()) == -1) {
		         return -1;
		    }
		    if (d1.getData().compareTo(d2.getData()) == 1) {
		         return 1;
		    }
		    if (d1.getData().compareTo(d2.getData()) == 0) {
		    	return d2.getDescricaoItem().compareTo(d1.getDescricaoItem());
		    }
		    return 0;
		}
	};
}
