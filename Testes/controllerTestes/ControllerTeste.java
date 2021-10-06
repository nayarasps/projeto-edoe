package controllerTestes;

import static org.junit.Assert.*;

import controller.DoacaoController;
import controller.ItemController;
import org.junit.*;

import controller.Controller;
import controller.UsuarioController;

public class ControllerTeste {
	
	private Controller controller;
	private UsuarioController usuarioController;
	private ItemController itemController;
	private DoacaoController doacaoController;

	@Before
	public void inicializaController() {
		controller = new Controller();
		usuarioController = new UsuarioController();
		itemController = new ItemController(usuarioController);
		doacaoController = new DoacaoController(itemController);
		
		usuarioController.adicionaDoador("123456", "Relampago Marquinhos", "relampago.marquinho@catchau.com", "2210-1022", "PESSOA_FISICA");
		usuarioController.adicionaDoador("112233", "Yudi Playsteicho", "yudi.prey@pleystetion.com", "4002-8922", "PESSOA_FISICA");

		itemController.adicionaItemDoador("123456", "Pelucia", 4, "fofo,coelho");
		itemController.adicionaItemDoador("112233", "Casaco", 2, "pele,adulto");
	}

	
	/**
	 * Testa o metodo AdicionarDescritor
	 */
	@Test
	public void testAdicionaDescritor() {
		assertFalse(controller.getDadosDescritores().contains("Papel"));
		
		controller.adicionaDescritor("Papel");
		assertTrue(controller.getDadosDescritores().contains("papel"));
	}

	/**
	 * Testa os casos de erro do metodo AdicionaDescritor
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaAdicionaDescritor() {
		// Valida descricao vazia ou nula
		controller.adicionaDescritor(null);
		controller.adicionaDescritor("");
		
		// Valida descricao ja existente
		controller.adicionaDescritor("casaco");
	}
	/**
	 * Testa o metodo AdicionarItem
	 */
	@Test
	public void testAdicionaItem() {
		assertEquals(3, itemController.adicionaItemDoador("123456", "Papelao", 5, "reciclavel"));
		assertEquals(4, itemController.adicionaItemDoador("112233", "Livro", 3, "classico,estrangeiro"));
		assertEquals(5, itemController.adicionaItemDoador("112233", "Mochila", 4, ""));
		
		// Item com o mesmo descritor e tags, porem quantidade diferente
		assertEquals(1, itemController.adicionaItemDoador("123456", "Pelucia", 8, "fofo,coelho"));
	}
	
	/**
	 * Testa os casos de erro do metodo AdicionaItem
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaAdicionaItem() {
			// Valida IdDoador vazio ou nulo
		itemController.adicionaItemDoador(null, "Papelao", 5, "reciclavel");
		itemController.adicionaItemDoador("", "Papelao", 5, "reciclavel");
			// Valida descricaoItem vazia ou nula
		itemController.adicionaItemDoador("123456", null, 5, "reciclavel");
		itemController.adicionaItemDoador("123456", "", 5, "reciclavel");
			// Valida quantidade invalida (quantidade <= 0)
		itemController.adicionaItemDoador("123456", "Papelao", -1, "reciclavel");
		itemController.adicionaItemDoador("123456", "Papelao", 0, "reciclavel");
			// Valida tags nulas
		itemController.adicionaItemDoador("123456", "Papelao", 5, null);
	}

	/**
	 * Testa o metodo ExibeItem
	 */
	@Test
	public void testExibeItem() {
		assertEquals(controller.exibeItem(1, "123456"), "1 - pelucia, tags: [fofo, coelho], quantidade: 4");
		assertEquals(controller.exibeItem(2, "112233"), "2 - casaco, tags: [pele, adulto], quantidade: 2");

		itemController.adicionaItemDoador("123456", "Geladeira", 1, "");
		assertEquals(controller.exibeItem(3, "123456"), "3 - geladeira, tags: [], quantidade: 1");
	}

	/**
	 * Testa os casos de erro do metodo ExibeItem
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaExibeItem() {
		// Valida Usuario nao encontrado
		controller.exibeItem(1, "548425");
		
		// Valida Item nao encontrado
		controller.exibeItem(85, "123456");
	}
	/**
	 * Testa o metodo AtualizaItem
	 */
	@Test
	public void testAtualizaItem() {
			// Atualiza Tags
		assertEquals(controller.atualizaItem(1, "123456", -1, "girafa,usado"), "1 - pelucia, tags: [girafa, usado], quantidade: 4");
		assertEquals(controller.atualizaItem(1, "123456", 0, "girafa,usado"), "1 - pelucia, tags: [girafa, usado], quantidade: 4");
			// Atualiza Quantidade
		assertEquals(controller.atualizaItem(1, "123456", 5, null), "1 - pelucia, tags: [girafa, usado], quantidade: 5");
		assertEquals(controller.atualizaItem(1, "123456", 5, ""), "1 - pelucia, tags: [girafa, usado], quantidade: 5");
			// Nao Atualiza
		assertEquals(controller.atualizaItem(1, "123456", -1, null), "1 - pelucia, tags: [girafa, usado], quantidade: 5");
		assertEquals(controller.atualizaItem(1, "123456", 0, null), "1 - pelucia, tags: [girafa, usado], quantidade: 5");
		assertEquals(controller.atualizaItem(1, "123456", -1, ""), "1 - pelucia, tags: [girafa, usado], quantidade: 5");
		assertEquals(controller.atualizaItem(1, "123456", 0, ""), "1 - pelucia, tags: [girafa, usado], quantidade: 5");
			// Atualiza Tags e Quantidade
		assertEquals(controller.atualizaItem(1, "123456", 8, "algodao,urso"), "1 - pelucia, tags: [algodao, urso], quantidade: 8");
	}

	/**
	 * Testa os casos de erro do metodo AtualizaItem
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaAtualizaItem() {
			// Valida IdItem
		controller.atualizaItem(-1, "123456", 5, "normal");
			// Valida IdUsuario
		controller.atualizaItem(1, null, 0, "novo");
		controller.atualizaItem(1, "", 0, "novo");
			// Valida Usuario nao encontrado
		controller.atualizaItem(2, "485514", 12, "grande, peluda");
			// Valida Item nao encontrado
		controller.atualizaItem(89, "112233", 4, "pequena");
	}
	/**
	 * Testa o metodo RemoveItem
	 */
	@Test
	public void testRemoveItem() {
		assertTrue(controller.getDadosItens().get("123456").containsKey(1));
		
		controller.removeItem(1, "123456");
		assertFalse(controller.getDadosItens().get("123456").containsKey(1));
	}
	
	/**
	 * Testa os casos de erro do metodo RemoveItem
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaRemoveItem() {
			// Valida IdItem
		controller.removeItem(0, "123456");
			// Valida IdUsuario
		controller.removeItem(1, null);
		controller.removeItem(1, "");
			// Valida Usuario nao encontrado
		controller.removeItem(1, "324512");
			// Valida Item nao encontrado
		controller.removeItem(2, "123456");
			// Valida Usuario nao possui itens cadastrados
		assertTrue(controller.getDadosItens().get("123456").containsKey(2));
	}

	/**
	 * Testa o metodo ListaDescritorDeItensParaDoacao
	 */
	@Test
	public void testListaDescritorDeItensParaDoacao() {
		assertEquals(controller.listaDescritorDeItensParaDoacao(), "2 - casaco | 4 - pelucia");

		itemController.adicionaItemDoador("112233", "CasAco", 8, "infantil,colorido");
		assertEquals(controller.listaDescritorDeItensParaDoacao(), "10 - casaco | 4 - pelucia");
	}

	/**
	 * Testa o metodo PesquisaItemParaDoacaoPorDescricao
	 */
	@Test
	public void testPesquisaItemParaDoacaoPorDescricao() {
		itemController.adicionaItemDoador("112233", "CasAco", 8, "infantil,colorido");
		assertEquals(controller.pesquisaItemParaDoacaoPorDescricao("cAsaCO"), "2 - casaco, tags: [pele, adulto], quantidade: 2 | 3 - casaco, tags: [infantil, colorido], quantidade: 8");
	}

	/**
	 * Testa o caso de erro do metodo PesquisaItemParaDoacaoPorDescricao
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaPesquisaItemParaDoacaoPorDescricao() {
		controller.pesquisaItemParaDoacaoPorDescricao("Colchao");
	}
	
	/**
	 * Testa o metodo Match
	 */
	@Test
	public void testMatch() {
		usuarioController.getUsuarios().get("123456");
		itemController.adicionaItemDoador("112233", "pelucia", 3, "fofo, coelho");
		itemController.adicionaItemDoador("112233", "pelucia", 3, "fofo, gato");
		itemController.adicionaItemDoador("112233", "pelucia", 3, "gato, fofo");
		itemController.adicionaItemDoador("112233", "pelucia", 3, "coelho, fofo");
		itemController.adicionaItemDoador("112233", "pelucia", 3, "algodao, macaco");
		
		assertEquals(controller.match("123456", 1), "3 - pelucia, tags: [fofo,  coelho], quantidade: 3, doador: Yudi Playsteicho/112233 | 4 - pelucia, tags: [fofo,  gato], quantidade: 3, doador: Yudi Playsteicho/112233 | 6 - pelucia, tags: [coelho,  fofo], quantidade: 3, doador: Yudi Playsteicho/112233 | 5 - pelucia, tags: [gato,  fofo], quantidade: 3, doador: Yudi Playsteicho/112233 | 7 - pelucia, tags: [algodao,  macaco], quantidade: 3, doador: Yudi Playsteicho/112233");
	}
	
	/**
	 * Testa o caso de erro do metodo Match
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaMatch() {
			// Valida IdReceptor nao deve ser vazio ou nulo
		controller.match(null, 1);
		controller.match("", 1);
			// Valida IdItem nao pode ser negativo ou igual a zero
		controller.match("123456", -1);
		controller.match("123456", 0);
			// Valida Usuario no encontrado
		usuarioController.getUsuarios().containsKey("4515654");
			// Valida Usuario deve ser um receptor
		controller.match("123456", 1);
		
		usuarioController.getUsuarios().get("123456");
			// Valida Item nao encontrado
		controller.getDadosItens().get("123456").get(45);
		
	}

	/**
	 * Testa o metodo RealizaDoacao
	 */
	@Test
	public void testRealizaDoacao() {
		usuarioController.getUsuarios().get("123456");
		itemController.adicionaItemDoador("112233", "pelucia", 3, "fofo, coelho");
		
		assertEquals(controller.realizaDoacao(1, 3, "12/01/18"), "12/01/18 - doador: Yudi Playsteicho/112233, item: pelucia, quantidade: 3, receptor: Relampago Marquinhos/123456");
		assertEquals(controller.getDadosItens().get("123456").get(1).getQuantidade(), 1);
		
		assertFalse(controller.getDadosItens().get("112233").containsKey(3));
	}
	
	/**
	 * Testa os casos de erros do metodo RealizaDoacao
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaRealizaDoacao() {
			// Verifica Id do Item Necessario nao pode ser nulo ou vazio
		controller.realizaDoacao(-1, 1, "12/10/18");
		controller.realizaDoacao(0, 1, "12/10/18");
			// Verifica Id do Item Doado nao pode ser nulo ou vazio
		controller.realizaDoacao(2, -1, "12/06/18");
		controller.realizaDoacao(2, 0, "12/06/18");
			// Verifica Data nao pode ser nula ou vazia
		controller.realizaDoacao(1, 2, null);
		controller.realizaDoacao(1, 2, "");
			// Verifica Descricao igual
		usuarioController.getUsuarios().get("123456");
		controller.realizaDoacao(1, 2, "11/04/18");
	}
}
