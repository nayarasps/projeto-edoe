package backend;

import java.io.IOException;

import controller.Controller;
import controller.ItemController;
import controller.UsuarioController;
import easyaccept.EasyAccept;

public class Facade {
	
	private Controller controller;
	private ItemController itemController;
	private UsuarioController usuarioController;
	private Persistencia persistencia;
	
	public Facade() {
		controller = new Controller();
		usuarioController = new UsuarioController();
		itemController = new ItemController(usuarioController);
		 
	}
	
	public void iniciaSistema()  {
		persistencia = new Persistencia();
		persistencia.carregar(controller);
	}
	
	public void finalizaSistema() {
		Persistencia persistencia = new Persistencia();
		persistencia.salvar(controller.getDadosUsuarios(), controller.getDadosItens(), controller.getDadosDoacoes(), controller.getDadosDescritores());
	}
	
	public String adicionaDoador(String id, String nome, String email, String celular, String classe) {
		return usuarioController.adicionaDoador(id, nome, email, celular, classe);
	}
	
	public void lerReceptores(String caminho) throws IOException {
		usuarioController.lerReceptores(caminho);
	}
	
	public String pesquisaUsuarioPorId(String id) {
		return usuarioController.pesquisaUsuarioPorId(id);
	}
	
	public String pesquisaUsuarioPorNome(String nome) {
		return usuarioController.pesquisaUsuarioPorNome(nome);
	}
	
	public String atualizaUsuario(String id, String novoNome, String novoEmail, String novoCelular) {
		return usuarioController.atualizaUsuario(id, novoNome, novoEmail, novoCelular);
	}
	
	public void removeUsuario(String id) {
		usuarioController.removeUsuario(id);
	}
	
	//Item Doado Facade
	public void adicionaDescritor(String descricao) {
		itemController.adicionaDescritor(descricao);
	 }
	
	public int adicionaItemParaDoacao(String idDoador, String descricaoItem, int quantidade, String tags) {
		return itemController.adicionaItem(idDoador, descricaoItem, quantidade, tags);
	}
	
	public String exibeItem(int idItem, String idDoador) {
		return itemController.exibeItem(idItem, idDoador);
	}
	
	public String atualizaItemParaDoacao(int id, String idDoador, int quantidade, String tags) {
		return itemController.atualizaItem(id, idDoador, quantidade, tags);
	}
	
	public void removeItemParaDoacao(int id, String idDoador) {
		itemController.removeItem(id, idDoador);
	}
	
	public String listaDescritorDeItensParaDoacao() {
		return itemController.listaDescritorDeItensParaDoacao();
	}
	
	public String pesquisaItemParaDoacaoPorDescricao(String desc) {
		return itemController.pesquisaItemParaDoacaoPorDescricao(desc);
	}
	
	public String listaItensParaDoacao() {
		return itemController.listaItens("doador");
	}
	
	//Item Necessario Facade
	public int adicionaItemNecessario(String idReceptor, String descricaoItem, int quantidade, String tags) {
		return controller.adicionaItem(idReceptor, descricaoItem, quantidade, tags);
	}
	
	public String atualizaItemNecessario(String idReceptor, int idItem, int quantidade, String tags) {
		return controller.atualizaItem(idItem, idReceptor, quantidade, tags);
	}
	
	public String listaItensNecessarios() {
		 return controller.listaItens("Receptor");
	}
	 
	public void removeItemNecessario(String idReceptor, int idItem) {
		controller.removeItem(idItem, idReceptor);
	}
	
	public String match(String idReceptor, int idItemNecessario) {
		return controller.match(idReceptor, idItemNecessario);
	}
	
	public String realizaDoacao(int idItemNecessario, int idItemDoado, String data) {
		return controller.realizaDoacao(idItemNecessario, idItemDoado, data);
	}
	
	public String listaDoacoes() {
		return controller.listaDoacoes();
	}
	
	public static void main(String[] args) {
		Facade f = new Facade();
		f.iniciaSistema();
		args = new String[] { "backend.Facade", "acceptance_test/use_case_1.txt", "acceptance_test/use_case_2.txt"
				, "acceptance_test/use_case_3.txt", "acceptance_test/use_case_4.txt", "acceptance_test/use_case_5.txt"
				, "acceptance_test/use_case_6.txt", "acceptance_test/use_case_7.txt"};
		EasyAccept.main(args);
	}
	
}