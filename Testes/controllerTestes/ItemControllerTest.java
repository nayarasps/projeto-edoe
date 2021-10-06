package controllerTestes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import controller.Controller;
import controller.ItemController;
import controller.UsuarioController;

public class ItemControllerTest {

	private UsuarioController usuarioController;
	private ItemController itemController;
	
	@Before
	public void inicializaController() {
		Controller controller = new Controller();
		usuarioController = new UsuarioController();
		itemController = new ItemController(usuarioController);
		
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
		assertFalse(itemController.getDescritores().contains("Papel"));
		
		itemController.adicionaDescritor("Papel");
		assertTrue(itemController.getDescritores().contains("papel"));
	}

	/**
	 * Testa os casos de erro do metodo AdicionaDescritor
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaAdicionaDescritor() {
		// Valida descricao vazia ou nula
		itemController.adicionaDescritor(null);
		itemController.adicionaDescritor("");
		
		// Valida descricao ja existente
		itemController.adicionaDescritor("casaco");
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
		assertEquals(itemController.exibeItem(1, "123456"), "1 - pelucia, tags: [fofo, coelho], quantidade: 4");
		assertEquals(itemController.exibeItem(2, "112233"), "2 - casaco, tags: [pele, adulto], quantidade: 2");
		
		itemController.adicionaItemDoador("123456", "Geladeira", 1, "");
		assertEquals(itemController.exibeItem(3, "123456"), "3 - geladeira, tags: [], quantidade: 1");
	}

	/**
	 * Testa os casos de erro do metodo ExibeItem
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaExibeItem() {
		// Valida Usuario nao encontrado
		itemController.exibeItem(1, "548425");
		
		// Valida Item nao encontrado
		itemController.exibeItem(85, "123456");
	}
	/**
	 * Testa o metodo AtualizaItem
	 */
	@Test
	public void testAtualizaItem() {
			// Atualiza Tags
		assertEquals(itemController.atualizaItem(1, "123456", -1, "girafa,usado"), "1 - pelucia, tags: [girafa, usado], quantidade: 4");
		assertEquals(itemController.atualizaItem(1, "123456", 0, "girafa,usado"), "1 - pelucia, tags: [girafa, usado], quantidade: 4");
			// Atualiza Quantidade
		assertEquals(itemController.atualizaItem(1, "123456", 5, null), "1 - pelucia, tags: [girafa, usado], quantidade: 5");
		assertEquals(itemController.atualizaItem(1, "123456", 5, ""), "1 - pelucia, tags: [girafa, usado], quantidade: 5");
			// Nao Atualiza
		assertEquals(itemController.atualizaItem(1, "123456", -1, null), "1 - pelucia, tags: [girafa, usado], quantidade: 5");
		assertEquals(itemController.atualizaItem(1, "123456", 0, null), "1 - pelucia, tags: [girafa, usado], quantidade: 5");
		assertEquals(itemController.atualizaItem(1, "123456", -1, ""), "1 - pelucia, tags: [girafa, usado], quantidade: 5");
		assertEquals(itemController.atualizaItem(1, "123456", 0, ""), "1 - pelucia, tags: [girafa, usado], quantidade: 5");
			// Atualiza Tags e Quantidade
		assertEquals(itemController.atualizaItem(1, "123456", 8, "algodao,urso"), "1 - pelucia, tags: [algodao, urso], quantidade: 8");
	}

	/**
	 * Testa os casos de erro do metodo AtualizaItem
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaAtualizaItem() {
			// Valida IdItem
		itemController.atualizaItem(-1, "123456", 5, "normal");
			// Valida IdUsuario
		itemController.atualizaItem(1, null, 0, "novo");
		itemController.atualizaItem(1, "", 0, "novo");
			// Valida Usuario nao encontrado
		itemController.atualizaItem(2, "485514", 12, "grande, peluda");
			// Valida Item nao encontrado
		itemController.atualizaItem(89, "112233", 4, "pequena");
	}
	/**
	 * Testa o metodo RemoveItem
	 */
	@Test
	public void testRemoveItem() {
		assertTrue(itemController.encontraItensPorIdUsuario("123456").containsKey(1));
		
		itemController.removeItem(1, "123456");
		assertFalse(itemController.encontraItensPorIdUsuario("123456").containsKey(1));
	}
	
	/**
	 * Testa os casos de erro do metodo RemoveItem
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaRemoveItem() {
			// Valida IdItem
		itemController.removeItem(0, "123456");
			// Valida IdUsuario
		itemController.removeItem(1, null);
		itemController.removeItem(1, "");
			// Valida Usuario nao encontrado
		itemController.removeItem(1, "324512");
			// Valida Item nao encontrado
		itemController.removeItem(2, "123456");
			// Valida Usuario nao possui itens cadastrados
		assertTrue(itemController.encontraItensPorIdUsuario("123456").containsKey(2));
	}

	/**
	 * Testa o metodo ListaDescritorDeItensParaDoacao
	 */
	@Test
	public void testListaDescritorDeItensParaDoacao() {
		assertEquals(itemController.listaDescritorDeItensParaDoacao(), "2 - casaco | 4 - pelucia");

		itemController.adicionaItemDoador("112233", "CasAco", 8, "infantil,colorido");
		assertEquals(itemController.listaDescritorDeItensParaDoacao(), "10 - casaco | 4 - pelucia");
	}

	/**
	 * Testa o metodo PesquisaItemParaDoacaoPorDescricao
	 */
	@Test
	public void testPesquisaItemParaDoacaoPorDescricao() {
		itemController.adicionaItemDoador("112233", "CasAco", 8, "infantil,colorido");
		assertEquals(itemController.pesquisaItemParaDoacaoPorDescricao("cAsaCO"), "2 - casaco, tags: [pele, adulto], quantidade: 2 | 3 - casaco, tags: [infantil, colorido], quantidade: 8");
	}

	/**
	 * Testa o caso de erro do metodo PesquisaItemParaDoacaoPorDescricao
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaPesquisaItemParaDoacaoPorDescricao() {
		itemController.pesquisaItemParaDoacaoPorDescricao("Colchao");
	}
	
	/**
	 * Testa o metodo ListarItens
	 */
	@Test
	public void testListaItens() {
		
		assertEquals(itemController.listaItens("doador"), "1 - pelucia, tags: [fofo, coelho], quantidade: 4, doador: Relampago Marquinhos/123456 | 2 - casaco, tags: [pele, adulto], quantidade: 2, doador: Yudi Playsteicho/112233");
		
		usuarioController.removeUsuario("123456");
		usuarioController.removeUsuario("112233"); 
		usuarioController.adicionaReceptor("123456", "Relampago Marquinhos", "relampago.marquinho@catchau.com", "2210-1022", "PESSOA_FISICA");
		usuarioController.adicionaReceptor("112233", "Yudi Playsteicho", "yudi.prey@pleystetion.com", "4002-8922", "PESSOA_FISICA");
		itemController.adicionaItemDoador("123456", "Pelucia", 4, "fofo,coelho");
		itemController.adicionaItemDoador("112233", "Casaco", 2, "pele,adulto");
		
		assertEquals(itemController.listaItens("receptor"), "3 - pelucia, tags: [fofo, coelho], quantidade: 4, Receptor: Relampago Marquinhos/123456 | 4 - casaco, tags: [pele, adulto], quantidade: 2, Receptor: Yudi Playsteicho/112233");
	}
}
