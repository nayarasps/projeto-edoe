package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import entidades.Doador;
import entidades.Item;
import entidades.Receptor;
import entidades.Usuario;
import util.StringFormat;

public class ItemController {
	
	private UsuarioController usuarioController;
	private LinkedHashMap<String, Usuario> usuarios;
	private TreeSet<String> descritores;

	private int identificador = 1;
	
	Validacao valida = new Validacao();

	public ItemController(UsuarioController usuarioController) {
		this.usuarioController = usuarioController;
		this.usuarios = usuarioController.getUsuarios();
		this.descritores = new TreeSet<>();
	
	}
	
	
	private int gerarIdItem() {
		return identificador++;
	}
	
	private Usuario getUsuariobyId(String id) {
		Usuario usuario = usuarios.get(id);
		if (usuario == null) {
			valida.usuarioNaoEncontrado(id);
		}
		return usuario;
	}
	
	private void verificaExistenciaDescritores(String descricao) {
		
		if (descritores.contains(descricao)) {
			throw new IllegalArgumentException("Descritor de Item ja existente: " + descricao + ".");
		}
	}
	
	private void verificaExistenciaItem(HashMap<Integer, Item> itensDoador, int idItem) {
		if (!itensDoador.containsKey(idItem)) {
			valida.itemNaoEncontrado(idItem);
		}
	}

	public void adicionaDescritor(String descricao) {
		valida.verificaDescritorItem(descricao);
		
		String descricaoFormatada = StringFormat.formatacaoString(descricao);
		verificaExistenciaDescritores(descricaoFormatada);
		
		descritores.add(descricaoFormatada);
	 }
	
	
	private Item criaItem(String descricao, int quantidade, String tags, HashMap<Integer, Item> itensDoador) {
		valida.verificaAdicionaItem(descricao, quantidade);
		
		Item item = new Item(StringFormat.formatacaoString(descricao), tags, quantidade);
		int idItem = encontraIdItemJaExistente(itensDoador, item); 
		item.setId(idItem);
		
		return item;
	}
	

	
	private Item encontraItemPorId(String idDoador, int idItem) {

		Usuario doador = getUsuariobyId(idDoador);
		HashMap<Integer, Item> itensDoador = doador.getItens();
		
		verificaExistenciaItem(itensDoador, idItem);
		Item item = itensDoador.get(idItem);
		
		return item;
	}
	
	public HashMap<Integer, Item> encontraItensDoados(String idDoador) {
		valida.verificaIdUsuario(idDoador);
		
		Usuario doador = getUsuariobyId(idDoador);
		HashMap<Integer, Item> itensDoador = doador.getItens();
		
		return itensDoador;
	}
	
	private boolean comparaDescricaoTags(Item item1, Item item2) {
		return (item1.comparaDescricaoItens(item2) && item1.comparaTagsItens(item2));
	}
	
	private int encontraIdItemJaExistente(HashMap<Integer, Item> itensDoador, Item item) {
		
		for (Integer chave : itensDoador.keySet()) {
			Item itemAtual = itensDoador.get(chave);
			
			if (comparaDescricaoTags(itemAtual, item)) {
				return itemAtual.getId();
			}
		}
		return gerarIdItem();
	}


	public int adicionaItem(String idDoador, String descricao, int quantidade, String tags) {
		 
		 HashMap<Integer, Item> itensDoador = encontraItensDoados(idDoador);
		 
		 Item item = criaItem(descricao, quantidade, tags, itensDoador);
		 
		 itensDoador.put(item.getId(), item);
		 
		 return item.getId();
	 }

	public String exibeItem(int idItem, String idDoador) {
		
		Item item = encontraItemPorId(idDoador, idItem);
		
		return item.toString();
	 }
	
	
	public String atualizaItem(int idItem, String idDoador, int quantidade, String tags) {
		valida.verificaIdItem(idItem);
		valida.verificaIdUsuario(idDoador);
		
		
		Item item = encontraItemPorId(idDoador, idItem);
		
		// Atualiza Tags
		if ((quantidade <= 0) && !(tags == null || "".equals(tags.trim()))) {
			item.setTags(tags);
		}
		// Atualiza Quantidade
		if ((quantidade > 0) && (tags == null || "".equals(tags.trim()))) {
			item.setQuantidade(quantidade);
		}
		// Retorna Item sem atualizacao
		if ((quantidade <= 0) && (tags == null || "".equals(tags.trim()))) {
			return item.toString();
		}
		// Atualiza Quantidade e Tags
		if ((quantidade > 0) && !(tags == null || "".equals(tags.trim()))) {
			item.setQuantidade(quantidade);
			item.setTags(tags);
		}
		return item.toString();
	}
	 
	private void verificaUsuarioItensVazio(HashMap<Integer, Item> itensDoador) {
		if (itensDoador.isEmpty()) {
			throw new IllegalArgumentException("O Usuario nao possui itens cadastrados.");
		}
	}
	
	public void removeItem(int idItem, String idDoador) {
		valida.verificaIdUsuario(idDoador);
		valida.verificaIdItem(idItem);
		
		
		 HashMap<Integer, Item> itensDoador = encontraItensDoados(idDoador);
		 verificaUsuarioItensVazio(itensDoador);
		 verificaExistenciaItem(itensDoador, idItem);
		 
		 itensDoador.remove(idItem);
	 }

	private String listaItensReceptores() {
		String saida = "";
		for (Receptor receptor : usuarioController.getReceptores()) {
			saida += receptor.listarItens();
		}
		return saida;
	}
	
	private String listaItensDoadores() {
		String saida = "";
		for (Doador doador : usuarioController.getDoadores()) {
			saida += doador.listarItens();
		}
		return saida;
	}
	
	private String tratamentoSaidaListaItens(String saida) {
		if (saida.equals("")) {
			throw new IllegalArgumentException("Nenhum item cadastrado.");
		}
		return saida.substring(0, saida.length() - 3);
	}
	
	public String listaItens(String status) {
		
		String saida = "";
		
		if (status.equals("receptor")) {
			saida = listaItensReceptores();
		}
		else if (status.equals("doador")) {
			saida = listaItensDoadores();
		}

		return tratamentoSaidaListaItens(saida);
	}
	
	public String listaDescritorDeItensParaDoacao() {
		String saida = "";
		for (String d : descritores) {
			int quant = 0;
			for (String u : itens.keySet()) {
				for (Integer i : itens.get(u).keySet()) {
					if ( d.equals(itens.get(u).get(i).getDescricaoItem())) {
						quant += itens.get(u).get(i).getQuantidade();
					}
				}
			}
			saida += quant;
			saida +=  " - " + d + " | ";
		}
		return saida.substring(0, saida.length() - 3);
	}
	
	/**
	 * Metodo que pesquisa um Item cadastrado de acordo com uma descricao dada pelo usuario.
	 * 
	 * @param desc que representa a descricao dada pelo usuario para encontrar itens com a mesma descricao.
	 * @throws IllegalArgumentException que ocorre quando nao existem descricoes que sejam iguais a dada pelo usuario.
	 * @exception IllegalArgumentException que ocorre quando o Usuario nao existe no sistema e quando o Item nao esta associado ao Usuario do id informado.
	 * @return String com os Itens que possuem a mesma descricao que a dada pelo usuario.
	 */
	public String pesquisaItemParaDoacaoPorDescricao(String desc) {
		valida.verificaTextoPesquisa(desc);
		
		String saida = "";
		List<Item> list = new ArrayList<Item>();
		for (String u : itens.keySet()) {
			for (Integer i : itens.get(u).keySet()) {
				if (itens.get(u).get(i).getDescricaoItem().contains(desc.toLowerCase().trim())) {
					list.add(itens.get(u).get(i));
				}
			}
		}
		Collections.sort(list, Item.comparaDescricao);
		for (int i = 0; i < list.size(); i++) {
			saida += list.get(i).toString();
			saida += " | ";
		}
		if (saida.equals("")) {
			throw new IllegalArgumentException("Nao existem descricoes com essa chave");
		}
		return saida.substring(0, saida.length() - 3);
	}
	
	public TreeSet<String> getDescritores() {
		return this.descritores;
	}

}
