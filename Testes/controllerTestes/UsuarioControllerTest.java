package controllerTestes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import controller.UsuarioController;

public class UsuarioControllerTest {

	private UsuarioController usuarioController;
	
	@Before
	public void inicializaController() {
		usuarioController = new UsuarioController();
		
		usuarioController.adicionaDoador("123456", "Relampago Marquinhos", "relampago.marquinho@catchau.com", "2210-1022", "PESSOA_FISICA");
		usuarioController.adicionaDoador("112233", "Yudi Playsteicho", "yudi.prey@pleystetion.com", "4002-8922", "PESSOA_FISICA");

	
	}

	/**
	 * Testa o metodo AdicionarDoador do Controller
	 */
	@Test
	public void testAdicionaDoador() {
		assertEquals("845662", usuarioController.adicionaDoador("845662", "Dexter", "dexter.genio@dominacao.com", "(86)8450-1022", "PESSOA_FISICA"));
		assertEquals("452169", usuarioController.adicionaDoador("452169", "Mandrake", "mandrake.muahaha@dominacao.com", "(88)8783-5315", "PESSOA_FISICA"));
	}
	/**
	 * Testa os casos de erro do metodo AdicionaDoador do Controller
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaAdicionaDoador() {
		// Usuario ja existente
		usuarioController.adicionaDoador("123456", "Relampago Marquinhos", "relampago.marquinho@catchau.com", "2210-1022", "PESSOA_FISICA");
		
		// Verifica Adicionar Usuario
			// Id Usuario Invalido 
		usuarioController.adicionaDoador("-1", "Relampago Marquinhos", "relampago.marquinho@catchau.com", "2210-1022", "PESSOA_FISICA");
			// Nome Usuario vazio ou nulo
		usuarioController.adicionaDoador("1", null, "relampago.marquinho@catchau.com", "2210-1022", "PESSOA_FISICA");
		usuarioController.adicionaDoador("1", "", "relampago.marquinho@catchau.com", "2210-1022", "PESSOA_FISICA");
			// Email Usuario vazio ou nulo
		usuarioController.adicionaDoador("1", "Relampago Marquinhos", null, "2210-1022", "PESSOA_FISICA");
		usuarioController.adicionaDoador("1", "Relampago Marquinhos", "", "2210-1022", "PESSOA_FISICA");
			// Celular Usuario vazio ou nulo
		usuarioController.adicionaDoador("1", "Relampago Marquinhos", "relampago.marquinho@catchau.com", null, "PESSOA_FISICA");
		usuarioController.adicionaDoador("1", "Relampago Marquinhos", "relampago.marquinho@catchau.com", "", "PESSOA_FISICA");
			// Classe Usuario vazio ou nulo
		usuarioController.adicionaDoador("1", "Relampago Marquinhos", "relampago.marquinho@catchau.com", "2210-1022", null);
		usuarioController.adicionaDoador("1", "Relampago Marquinhos", "relampago.marquinho@catchau.com", "2210-1022", "");
			// Classe Usuario Invalida
		usuarioController.adicionaDoador("1", "Relampago Marquinhos", "relampago.marquinho@catchau.com", "2210-1022", "CARRO_VERMELHO");
	}

	/**
	 * Testa o metodo PesquisaUsuarioPorId
	 */
	@Test
	public void testPesquisaUsuarioPorId() {
		assertEquals(usuarioController.pesquisaUsuarioPorId("123456"), "Relampago Marquinhos/123456, relampago.marquinho@catchau.com, 2210-1022, status: doador");
		assertEquals(usuarioController.pesquisaUsuarioPorId("112233"), "Yudi Playsteicho/112233, yudi.prey@pleystetion.com, 4002-8922, status: doador");
	}

	/**
	 * Testa os casos de erro do metodo PesquisaUsuarioPorId
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaPesquisaUsuarioPorId() {
		// Valida Id do Usuario vazio ou nulo
		usuarioController.pesquisaUsuarioPorId(null);
		usuarioController.pesquisaUsuarioPorId("");
		
		// Valida Existencia do Usuario
		usuarioController.pesquisaUsuarioPorId("4512385");
	}
	
	/**
	 * Testa o metodo PesquisaUsuarioPorNome
	 */
	@Test
	public void testPesquisaUsuarioPorNome() {
		assertEquals(usuarioController.pesquisaUsuarioPorNome("Relampago Marquinhos"), "Relampago Marquinhos/123456, relampago.marquinho@catchau.com, 2210-1022, status: doador");
		assertEquals(usuarioController.pesquisaUsuarioPorNome("Yudi Playsteicho"), "Yudi Playsteicho/112233, yudi.prey@pleystetion.com, 4002-8922, status: doador");
	}

	/**
	 * Testa os casos de erro do metodo PesquisaUsuarioPorNome
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaPesquisaUsuarioPorNome() {
		// Valida Nome do Usuario vazio ou nulo
		usuarioController.pesquisaUsuarioPorNome(null);
		usuarioController.pesquisaUsuarioPorNome("");
		
		// Valida Existencia do Usuario
		usuarioController.pesquisaUsuarioPorNome("Calvin Klarckson");
	}
	
	/**
	 * Testa o metodo AtualizaUsuario
	 */
	@Test
	public void testAtualizaUsuario() {
		// Nao Atualiza Usuario
		assertEquals(usuarioController.atualizaUsuario("123456", null, null, null), "Relampago Marquinhos/123456, relampago.marquinho@catchau.com, 2210-1022, status: doador");
		assertEquals(usuarioController.atualizaUsuario("123456", "", "", ""), "Relampago Marquinhos/123456, relampago.marquinho@catchau.com, 2210-1022, status: doador");
		
		// Atualiza Nome do Usuario
		assertEquals(usuarioController.atualizaUsuario("123456", "Marquisson", null, null), "Marquisson/123456, relampago.marquinho@catchau.com, 2210-1022, status: doador");
		assertEquals(usuarioController.atualizaUsuario("123456", "Marquisson", "", ""), "Marquisson/123456, relampago.marquinho@catchau.com, 2210-1022, status: doador");
		assertEquals(usuarioController.atualizaUsuario("123456", "Marquisson", null, ""), "Marquisson/123456, relampago.marquinho@catchau.com, 2210-1022, status: doador");
		assertEquals(usuarioController.atualizaUsuario("123456", "Marquisson", "", null), "Marquisson/123456, relampago.marquinho@catchau.com, 2210-1022, status: doador");
		
		// Atualiza Email do Usuario
		assertEquals(usuarioController.atualizaUsuario("123456", null, "marquisson@catchau.com", null), "Marquisson/123456, marquisson@catchau.com, 2210-1022, status: doador");
		assertEquals(usuarioController.atualizaUsuario("123456", null, "marquisson@catchau.com", ""), "Marquisson/123456, marquisson@catchau.com, 2210-1022, status: doador");
		assertEquals(usuarioController.atualizaUsuario("123456", "", "marquisson@catchau.com", ""), "Marquisson/123456, marquisson@catchau.com, 2210-1022, status: doador");
		assertEquals(usuarioController.atualizaUsuario("123456", "", "marquisson@catchau.com", null), "Marquisson/123456, marquisson@catchau.com, 2210-1022, status: doador");
		
		// Atualiza Celular do Usuario
		assertEquals(usuarioController.atualizaUsuario("123456", null, null, "5465-3231"), "Marquisson/123456, marquisson@catchau.com, 5465-3231, status: doador");
		assertEquals(usuarioController.atualizaUsuario("123456", null, "", "5465-3231"), "Marquisson/123456, marquisson@catchau.com, 5465-3231, status: doador");
		assertEquals(usuarioController.atualizaUsuario("123456", "", null, "5465-3231"), "Marquisson/123456, marquisson@catchau.com, 5465-3231, status: doador");
		assertEquals(usuarioController.atualizaUsuario("123456", "", "", "5465-3231"), "Marquisson/123456, marquisson@catchau.com, 5465-3231, status: doador");
	}

	/**
	 * Testa os casos de erro do metodo AtualizaUsuario
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaAtualizaUsuario() {
		// Valida Id do Usuario vazio ou nulo
		usuarioController.atualizaUsuario(null, "Carlos", "carlim.iai@blabla.com", "(88) 4517-8951");
		usuarioController.atualizaUsuario("", "Carlos", "carlim.iai@blabla.com", "(88) 4517-8951");
		
		// Valida Existencia do Usuario
		usuarioController.atualizaUsuario("347568", "Carlos", "carlim.iai@blabla.com", "(88) 4517-8951");
	}
	
	/**
	 * Testa o metodo removeUsuario
	 */
	@Test
	public void testRemoveUsuario() {
		assertTrue(usuarioController.getUsuarios().containsKey("123456"));
		
		usuarioController.removeUsuario("123456");
		assertFalse(usuarioController.getUsuarios().containsKey("123456"));
	}
	
	/**
	 * Testa os casos de erro do metodo RemoveUsuario
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testValidaRemoveUsuario() {
		// Valida Id do Usuario vazio ou nulo
		usuarioController.removeUsuario(null);
		usuarioController.removeUsuario("");
		
		// Valida Existencia do Usuario
		usuarioController.removeUsuario("854762");
	}

}
