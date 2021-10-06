package controller;

import java.util.*;

import entidades.Doador;
import entidades.Item;
import entidades.Receptor;
import entidades.Usuario;
import util.StringFormat;

public class ItemController {

	private UsuarioController usuarioController;
	private TreeSet<String> descritores;

	private int identificador = 1;

	Validacao valida = new Validacao();

	public ItemController(UsuarioController usuarioController) {
		this.usuarioController = usuarioController;
		this.descritores = new TreeSet<>();

	}

	private int gerarIdItem() {
		return identificador++;
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

		descritores.add(descricao.toLowerCase());

		return item;
	}

	public Doador encontraDoadorPorIdItem(int idItem) {
		Collection<Doador> doadores = usuarioController.getDoadores();
		for (Doador doador : doadores) {
			if (doador.verificaUsuarioPossuiItem(idItem)) {
				return doador;
			}
		}
		return null;
	}

	public Receptor encontraReceptorPorIdItem(int idItem) {
		Collection<Receptor> receptores = usuarioController.getReceptores();
		for (Receptor receptor : receptores) {
			if (receptor.verificaUsuarioPossuiItem(idItem)) {
				return receptor;
			}
		}
		return null;
	}

	private Item encontraItemPorIdDoadorItem(String idDoador, int idItem) {

		Usuario doador = usuarioController.getUsuariobyId(idDoador);
		HashMap<Integer, Item> itensDoador = doador.getItens();

		verificaExistenciaItem(itensDoador, idItem);

		return itensDoador.get(idItem);
	}

	public HashMap<Integer, Item> encontraItensPorIdUsuario(String idUsuario) {
		valida.verificaIdUsuario(idUsuario);

		Usuario usuario = usuarioController.getUsuariobyId(idUsuario);

		return usuario.getItens();
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


	public int adicionaItemDoador(String idDoador, String descricao, int quantidade, String tags) {

		HashMap<Integer, Item> itensDoador = encontraItensPorIdUsuario(idDoador);

		Item item = criaItem(descricao, quantidade, tags, itensDoador);

		itensDoador.put(item.getId(), item);

		return item.getId();
	}

	public int adicionaItemReceptor(String idReceptor, String descricao, int quantidade, String tags) {

		HashMap<Integer, Item> itensReceptor = encontraItensPorIdUsuario(idReceptor);

		Item item = criaItem(descricao, quantidade, tags, itensReceptor);

		itensReceptor.put(item.getId(), item);

		return item.getId();
	}

	public String exibeItem(int idItem, String idDoador) {

		Item item = encontraItemPorIdDoadorItem(idDoador, idItem);

		return item.toString();
	}

	
	public String atualizaItem(int idItem, String idDoador, int quantidade, String tags) {
		valida.verificaIdItem(idItem);
		valida.verificaIdUsuario(idDoador);
		
		
		Item item = encontraItemPorIdDoadorItem(idDoador, idItem);
		
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
	
	public void removeItem(int idItem, String idUsuaario) {
		valida.verificaIdUsuario(idUsuaario);
		valida.verificaIdItem(idItem);
		
		
		 HashMap<Integer, Item> itensUsuario = encontraItensPorIdUsuario(idUsuaario);
		 verificaUsuarioItensVazio(itensUsuario);
		 verificaExistenciaItem(itensUsuario, idItem);

		itensUsuario.remove(idItem);
	 }

	
	private String tratamentoSaidaListaItens(String saida) {
		if (saida.equals("")) {
			throw new IllegalArgumentException("Nenhum item cadastrado.");
		}
		return saida.substring(0, saida.length() - 3);
	}
	
	public String listaItens(String status) {
		
		String saida = "";
		
		if (status.equalsIgnoreCase("receptor")) {
			saida = usuarioController.listaItensReceptores();
		}
		else if (status.equalsIgnoreCase("doador")) {
			saida = usuarioController.listaItensDoadores();
		}

		return tratamentoSaidaListaItens(saida);
	}
	
	public String listaDescritorDeItensParaDoacao() {

		List<Item> itensDoacao = usuarioController.getItensDoadores();

		StringBuilder saida = new StringBuilder();
		for (String descritor : descritores) {
			int quantidade = 0;

			for (Item item : itensDoacao) {
				if (item.getDescricao().equalsIgnoreCase(descritor)) {
					quantidade += item.getQuantidade();
				}
			}
			saida.append(quantidade);
			saida.append(" - ").append(descritor).append(" | ");
		}
		return saida.substring(0, saida.length() - 3);
	}

	private void checaSaidaVazia(StringBuilder saida) {
		if (saida.length() == 0) {
			throw new IllegalArgumentException("Nao existem descricoes com essa chave");
		}
	}

	private String getSaidaItensEncontrados(List<Item> itensEncontrados) {

		StringBuilder saida = new StringBuilder();

		for (Item item : itensEncontrados) {
			saida.append(item.toString()).append(" | ");
		}

		checaSaidaVazia(saida);
		return saida.substring(0, saida.length() - 3);
	}

	public String pesquisaItemParaDoacaoPorDescricao(String desc) {
		valida.verificaTextoPesquisa(desc);

		List<Item> itensDoacao = usuarioController.getItensDoadores();

		List<Item> itensEncontrados = new ArrayList<Item>();

		for (Item item : itensDoacao) {
			if (item.getDescricao().contains(desc.trim().toLowerCase())) {
				itensEncontrados.add(item);
			}
		}

		itensEncontrados.sort(Item.comparaDescricao);

		return getSaidaItensEncontrados(itensEncontrados);

	}

	public TreeSet<String> getDescritores() {
		return this.descritores;
	}

}
