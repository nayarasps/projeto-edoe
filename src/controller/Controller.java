package controller;

import java.io.*;
import java.util.*;

import entidades.*;

public class Controller {
	
	/**
	 * 
	 * Classe responsavel pelo armazenamento dos usuarios,itens, doacoes e descritores
	 * quando cadastrados.
	 * Possui um contador que gera um inteiro identificador de cada item cadastrado;
	 * Possui Validacao que verifica as informacoes oferecidas pelo usuario do sistema.
	 * 
	 */
	private UsuarioController usuarioController;
	private HashMap<String, HashMap<Integer, Item>> itens;
	private TreeSet<String> descritores;
	private List<Doacao> doacoes;
	private int identificador = 1;
	Validacao valida = new Validacao();
	private LinkedHashMap<String, Usuario> usuarios;
	
	
	/**
	 * Metodo construtor do Controller.
	 * 
	 * @param usuarios que representa um LinkedHashMap dos usuarios cadastrados, possuindo como key o id string do usuario.
	 * @param itens que representa um HashMap dos itens cadastrados, tendo como key o identificador inteiro gerado em seu cadastro.
	 * @param descritores que representa uma TreeSet de descritores string cadastrados pelo usuario.
	 * @param doacoes que representa uma ArrayList contendo doacoes realizadas pelo sistema.
	 */
	public Controller() {
		this.usuarioController = new UsuarioController();
		this.usuarios = usuarioController.getUsuarios();
		this.itens = new HashMap<>();
		this.descritores = new TreeSet<>();
		this.doacoes = new ArrayList<>();
	}
	
	
	private String formatacaoString(String string) {
		return string.toLowerCase().trim();
	}
	

	 
	// Itens
	
	private void verificaExistenciaDescritores(String descricao) {
		
		if (descritores.contains(descricao)) {
			throw new IllegalArgumentException("Descritor de Item ja existente: " + descricao + ".");
		}
	}
	
	/**
	 * Metodo que adiciona um descritor no TreeSet descritores.
	 * 
	 * @param descricao que representa a String descricao que deve existir para um Item ser cadastrado.
	 * @throws IllegalArgumentException que ocorre quando o descritor de Item ja existe em descritores.
	 */
	public void adicionaDescritor(String descricao) {
		valida.verificaDescritorItem(descricao);
		
		String descricaoFormatada = formatacaoString(descricao);
		verificaExistenciaDescritores(descricaoFormatada);
		
		descritores.add(descricaoFormatada);
	 }
	
	
	private Item criaItem(String descricao, int quantidade, String tags) {
		valida.verificaAdicionaItem(descricao, quantidade);
		
		verificaExistenciaDescritores(descricao);
		
		Item item = new Item(formatacaoString(descricao), tags, quantidade);
		return item;
	}
	
	private boolean verificaDoadorPossuiItem(Usuario doador, Item item) {
		if (doador.getItens().contains(item)) {
			return true;
		}
		return false;
	}
	

	public int adicionaItem(String idDoador, String descricaoItem, int quantidade, String tags) {
		 valida.verificaIdUsuario(idDoador);
		

		 int key = identificador;
		 
		 Item item = criaItem(descricaoItem, quantidade, tags);
		 item.setIdItem(identificador);
		 
		 Usuario doador = getUsuariobyId(idDoador);
		 
		 List<Item> doadorItens = doador.getItens();
		 
		 doadorItens.add(item);
		 
		 if (usuarios.containsKey(idDoador)) {
			if (!(itens.containsKey(idDoador))) {
				itens.put(idDoador, new HashMap<Integer, Item>());
				itens.get(idDoador).put(identificador, item);
				itens.get(idDoador).get(identificador).setUser(usuarios.get(idDoador));
				identificador++; 
			}else {
				Set<Integer> keys = itens.get(idDoador).keySet();
				for (Integer o : keys) {
					if (itens.get(idDoador).get(o).getDescricaoItem().equals(item.getDescricaoItem().toLowerCase().trim()) 
							&& itens.get(idDoador).get(o).getTags().equals(item.getTags()) ) {
						key = o;
					}
				}
				identificador++; 
				item.setIdItem(key);
				itens.get(idDoador).put(key, item);
				itens.get(idDoador).get(key).setUser(usuarios.get(idDoador));
			}
		 }else {
			 valida.usuarioNaoEncontrado(idDoador);;
		 }
		 return item.getIdItem();
	 }
	
	/**
	 * Metodo que exibe um Item a partir de seu id e do id do seu Usuario associado.
	 *  
	 * @param idItem que representa o id do Item.
	 * @param idDoador que representa o id do Usuario que cadastrou o Item.
	 * @exception IllegalArgumentException que ocorre quando o Usuario nao existe no sistema e quando o Item nao esta associado ao Usuario do id informado.
	 * @return toString do Item.
	 */
	public String exibeItem(int idItem, String idDoador) {
		if (!(usuarios.containsKey(idDoador))) {
			valida.usuarioNaoEncontrado(idDoador);;
		}
		if (!(itens.get(idDoador).containsKey(idItem))) {
			valida.itemNaoEncontrado(idItem);
		}	
		return itens.get(idDoador).get(idItem).toString();
	 }
	
	/**
	 * Metodo que atualiza um item existente em itens.
	 * 
	 * @param id que representa o id do Item.
	 * @param idDoador que representa o id do Usuario que cadastrou o Item.
	 * @param quantidade que representa a nova quantidade do Item a ser atualizado, se nulo ou vazio, a quantidade nao e atualizada.
	 * @param tags que representa as novas tags do Item a ser atualizada, se nulo ou vazio, as tags nao sao atualizadas.
	 * @exception IllegalArgumentException que ocorre quando o Usuario nao existe no sistema e quando o Item nao esta associado ao Usuario do id informado.
	 * @return toString do Item atualizado.
	 */
	public String atualizaItem(int id, String idDoador, int quantidade, String tags) {
		valida.verificaIdItem(id);
		valida.verificaIdUsuario(idDoador);
		if (!(usuarios.containsKey(idDoador))) {
			valida.usuarioNaoEncontrado(idDoador);
		}
		if (!(itens.get(idDoador).containsKey(id))) {
			valida.itemNaoEncontrado(id);
		}
		
		Item i = itens.get(idDoador).get(id);
		// Atualiza Tags
		if ((quantidade <= 0) && !(tags == null || "".equals(tags.trim()))) {
			i.setTags(tags);
		}
		// Atualiza Quantidade
		if ((quantidade > 0) && (tags == null || "".equals(tags.trim()))) {
			i.setQuantidade(quantidade);
		}
		// Retorna Item sem atualizacao
		if ((quantidade <= 0) && (tags == null || "".equals(tags.trim()))) {
			return i.toString();
		}
		// Atualiza Quantidade e Tags
		if ((quantidade > 0) && !(tags == null || "".equals(tags.trim()))) {
			i.setQuantidade(quantidade);
			i.setTags(tags);
		}
		return i.toString();
	}
	 
	/**
	 * Metodo que remove um determinado Item em itens.
	 * 
	 * @param id que representa o id do Item a ser removido.
	 * @param idDoador que representa o id do Usuario que cadastrou o Item.
	 * @exception IllegalArgumentException que ocorre quando o Usuario nao existe no sistema e quando o Item nao esta associado ao Usuario do id informado.
	 * @throws IllegalArgumentException quando o Usuario esta cadastrado, porem nao possui itens em seu Id.
	 */
	public void removeItem(int id, String idDoador) {
		valida.verificaIdUsuario(idDoador);
		valida.verificaIdItem(id);
		if (!(usuarios.containsKey(idDoador))) {
			valida.usuarioNaoEncontrado(idDoador);
		}
		if (!(itens.containsKey(idDoador))) {
			throw new IllegalArgumentException("O Usuario nao possui itens cadastrados.");
		}
		if (!(itens.get(idDoador).containsKey(id))) {
			valida.itemNaoEncontrado(id);
		}
		itens.get(idDoador).remove(id);
	 }

	/**
	 * Metodo que lista os Itens contidos em itens.
	 * Os Itens sao diferenciados pelo status de seus Usuarios, podendo ser Itens doados ou necessarios.
	 * 
	 * @param status que identifica o tipo de Itens a serem listados, entre os doados e os necessarios.
	 * @return String que possui a representacao em string dos Itens de acordo com o status dos seus usuarios associados.
	 */
	public String listaItens(String status) {
		String saida = "";
		List<Item> list = new ArrayList<Item>();
		for (String u : itens.keySet()) {
			if (usuarios.get(u).getStatus().equals(status.toLowerCase())) {
				for (Integer i : itens.get(u).keySet()) {
					list.add(itens.get(u).get(i));
				}
			}
		}
		if (status.equals("Receptor")) {
			Collections.sort(list, Item.comparaIdItem);
			for (int i = 0; i < list.size(); i++) {
				saida += list.get(i).toString();
				saida += ", Receptor: " + list.get(i).getUser().getNome() + "/" + list.get(i).getUser().getId();
				saida += " | ";
			}
		}else if (status.equals("doador")) {
			Collections.sort(list, Item.comparaQuantidade);
			for (int i = 0; i < list.size(); i++) {
				saida += list.get(i).toString();
				saida += ", doador: " + list.get(i).getUser().getNome() + "/" + list.get(i).getUser().getId();
				saida += " | ";
			}
		}
		if (saida.equals("")) {
			throw new IllegalArgumentException("Nenhum item cadastrado.");
		}
		return saida.substring(0, saida.length() - 3);
	}
		
	/**
	 * Metodo que lista os Strings descritores contidos em descritores, junto com suas quantidades de acordo com os Itens cadastrados.
	 * 
	 * @return String contendo a quantidade de itens de acordo com sua descricao.
	 */
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
	
	/**
	 * Metodo que identifica quando existe um match entre Itens a serem doados e a serem recebidos.
	 * A funcao deve ser iniciada pelo usuario receptor.
	 * O descritor do Item e suas tags sao fatores de ordenacao de combinacao do match, que quanto mais parecidos eles aparecerao primeiro.
	 * 
	 * @param idReceptor que representa o id do Usuario receptor.
	 * @param idItemNecessario que representa o id do Item necessario em que deve-se procura os Itens doadores que dao match nele.
	 * @throws IllegalArgumentException que ocorre quando o id do Usuario nao e de um Usuario receptor.
	 * @exception IllegalArgumentException que ocorre quando o Usuario nao existe no sistema e quando o Item nao esta associado ao Usuario do id informado.
	 * @return String com os matches do Item necessario.
	 */
	public String match(String idReceptor, int idItemNecessario) {
		valida.verificaIdItem(idItemNecessario);
		valida.verificaIdUsuario(idReceptor);
		if (!(usuarios.containsKey(idReceptor))) {
			valida.usuarioNaoEncontrado(idReceptor);
		}
		if (!(usuarios.get(idReceptor).getStatus().equals("receptor"))) {
			throw new IllegalArgumentException("O Usuario deve ser um receptor: " + idReceptor + ".");
		}
		if (!(itens.get(idReceptor).containsKey(idItemNecessario))) {
			valida.itemNaoEncontrado(idItemNecessario);
		}
		
		String saida = "";
		List<Item> list = new ArrayList<Item>();
		for (String u : itens.keySet()) {
			for (Integer i : itens.get(u).keySet()) {
				int pontuacao = 0;
				if (itens.get(u).get(i).getUser().getStatus().equals("doador")) {
					if (itens.get(u).get(i).getDescricaoItem().equals(itens.get(idReceptor).get(idItemNecessario).getDescricaoItem())) {
						pontuacao += 20;
						for (String tag : itens.get(u).get(i).getTags().split(",")) {
							for(String tagItem : itens.get(idReceptor).get(idItemNecessario).getTags().split(",")) {
								if (itens.get(idReceptor).get(idItemNecessario).getTags().split(",").length <= itens.get(u).get(i).getTags().split(",").length) {
									for (int j = 0; j < itens.get(u).get(i).getTags().split(",").length; j++) {
										if (tagItem.contentEquals(tag)) {
											pontuacao += 10;
										}
										else if (tagItem.contains(tag)) {
											pontuacao += 5;
										}
									}
								}else if (itens.get(idReceptor).get(idItemNecessario).getTags().split(",").length > itens.get(u).get(i).getTags().split(",").length) {
									for (int j = 0; j < itens.get(idReceptor).get(idItemNecessario).getTags().split(",").length; j++) {
										if (tagItem.contentEquals(tag)) {
											pontuacao += 10;
										}
										else if (tagItem.contains(tag)) {
											pontuacao += 5;
										}
									}
								}
							}
						}
						itens.get(u).get(i).setPontuacaoMatch(pontuacao);
						list.add(itens.get(u).get(i));
					}
				}
			}
		}
		Collections.sort(list, Item.comparaPontuacaoMatch);
		for (int i = 0; i < list.size(); i++) {
			saida += list.get(i).toString() + ", doador: " + list.get(i).getUser().getNome() + "/" + list.get(i).getUser().getId();
			saida += " | ";
		}
		if (saida.equals("")) {
			return "";
		}
		return saida.substring(0, saida.length() - 3);
	}

	/**
	 * Metodo que cadastra a doacao de Itens necessarios e Itens doados.
	 * Uma Doacao e inserida em doacoes.
	 * A quantidade de itens que possui doados e reduzida assim com a de Itens necessario.
	 * Se um Item chega a quantidade 0, ele e removido de itens.
	 * 
	 * @param idItemNecessario que representa o id do Item necessario.
	 * @param idItemDoado que representa o id do Item doado.
	 * @param data que representa a data que a doacao foi realizada.
	 * @exception IllegalArgumentException que ocorre quando o Item necessario ou doado nao existe no sistema.
	 * @throws IllegalArgumentException que ocorre quando os Itens nao possuem descricoes iguais.
	 * @return toString de Doacao.
	 */
	public String realizaDoacao(int idItemNecessario, int idItemDoado, String data) {
		valida.verificaIdItem(idItemDoado);
		valida.verificaIdItem(idItemNecessario);
		valida.verificaData(data);
		String idDoador = "";
		String idReceptor = "";
		
		for (String user : itens.keySet()) {
			if (itens.get(user).containsKey(idItemDoado)) {
				idDoador = user;
			}
			if (itens.get(user).containsKey(idItemNecessario)) {
				idReceptor = user;
			}
		}
		if (idReceptor.equals("")) {
			valida.itemNaoEncontrado(idItemNecessario);
		}
		if (idDoador.equals("")) {
			valida.itemNaoEncontrado(idItemDoado);
		}
		if (!(itens.get(idDoador).get(idItemDoado).getDescricaoItem().equals(itens.get(idReceptor).get(idItemNecessario).getDescricaoItem()))) {
			throw new IllegalArgumentException("Os itens nao tem descricoes iguais.");
		}
		
		Doacao doacao = new Doacao(usuarios.get(idDoador), usuarios.get(idReceptor), 0, data, itens.get(idReceptor).get(idItemNecessario).getDescricaoItem());
		
		if (itens.get(idDoador).get(idItemDoado).getQuantidade() > itens.get(idReceptor).get(idItemNecessario).getQuantidade()) {
			doacao.setQuantidadeDoada(itens.get(idReceptor).get(idItemNecessario).getQuantidade());
			itens.get(idDoador).get(idItemDoado).setQuantidade(itens.get(idDoador).get(idItemDoado).getQuantidade() - itens.get(idReceptor).get(idItemNecessario).getQuantidade());
			itens.get(idReceptor).remove(idItemNecessario);
		}
		else if (itens.get(idDoador).get(idItemDoado).getQuantidade() < itens.get(idReceptor).get(idItemNecessario).getQuantidade()) {
			doacao.setQuantidadeDoada(itens.get(idDoador).get(idItemDoado).getQuantidade());
			itens.get(idReceptor).get(idItemNecessario).setQuantidade(itens.get(idReceptor).get(idItemNecessario).getQuantidade() - itens.get(idDoador).get(idItemDoado).getQuantidade());
			itens.get(idDoador).remove(idItemDoado);
		}
		else if (itens.get(idDoador).get(idItemDoado).getQuantidade() == itens.get(idReceptor).get(idItemNecessario).getQuantidade()) {
			doacao.setQuantidadeDoada(itens.get(idReceptor).get(idItemNecessario).getQuantidade());
			itens.get(idReceptor).remove(idItemNecessario);
			itens.get(idDoador).remove(idItemDoado);
		}
		
		doacoes.add(doacao);
		return doacao.toString();
	}
	
	/**
	 * Metodo que lista as Doacoes realizadas, armazenadas externamente.
	 * 
	 * @return String que contem as doacoes ordenadas pela data, da mais velho para a mais nova.
	 */
	public String listaDoacoes() {
		String saida = "";
		Collections.sort(doacoes, Doacao.comparaData);
		Collections.reverse(doacoes);
		for(int i = 0; i < doacoes.size(); i++) {
			saida += doacoes.get(i).toString();
			saida += " | ";
		}
		return saida.substring(0, saida.length() - 3);
	}

	/**
	 * Metodo que insere os dados dos Usuarios que foram armazenados externamente.
	 * 
	 * @param usuarios que representa o LinkedHashMap que deverao ser colocados esses dados.
	 */
	public void setDadosUsuarios(LinkedHashMap<String, Usuario> usuarios) {
		this.usuarios = usuarios;	
	}
	
	/**
	 * Metodo que insere os dados dos Itens que foram armazenados externamente.
	 * 
	 * @param itens que representa o HashMap que deverao ser colocados esses dados.
	 */
	public void setDadosItens(HashMap<String, HashMap<Integer, Item>> itens) {
		this.itens = itens;
	}
	
	/**
	 * Metodo que insere os dados das Doacoes que foram armazenados externamente.
	 * 
	 * @param doacoes que representa a Lista que deverao ser colocados esses dados.
	 */
	public void setDadosDoacoes(List<Doacao> doacoes) {
		this.doacoes = doacoes;
	}
	
	/**
	 * Metodo que insere os dados dos dscritores que foram armazenados externamente.
	 * 
	 * @param descritores que representa o TreeSet que deverao ser colocados esses dados.
	 */
	public void setDadosDescritores(TreeSet<String> descritores) {
		this.descritores = descritores;
	}
	
	/**
	 * Metodo que retorna o LinkedHashMap dos Usuarios.
	 * 
	 * @return LinkedHashMap.
	 */
	public LinkedHashMap<String, Usuario> getDadosUsuarios() {
		return this.usuarios;
	}
	
	/**
	 * Metodo que retorna o HashMap de Itens.
	 * 
	 * @return HashMap.
	 */
	public HashMap<String, HashMap<Integer, Item>> getDadosItens() {
		return this.itens;
	}
	
	/**
	 * Metodo que retorna a List de Doacoes.
	 * 
	 * @return List.
	 */
	public List<Doacao> getDadosDoacoes() {
		Collections.reverse(doacoes);
		return this.doacoes;
	}
	
	/**
	 * Metodo que retorna o TreeSet de Descritores.
	 * 
	 * @return TreeSet.
	 */
	public TreeSet<String> getDadosDescritores() {
		return this.descritores;
	}
		
}
