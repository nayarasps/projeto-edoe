package entidadesTestes;

import static org.junit.Assert.*;

import org.junit.*;

import entidades.Doador;
import entidades.Receptor;
import entidades.Usuario;

/**
 * 
 * Classe de teste de Usuario.
 *
 */
public class UsuarioTeste {
	
	private Usuario user2, user1, user3;
	
	/**
	 * Inicializa um Usuario user, para os testes.
	 */
	@Before
	public void inicializaUsuario() {
		user1 = new Doador("Relampago Marquinhos", "relampago.marquinho@catchau.com", "2210-1022", "PESSOA_FISICA", "123456");
		user2 = new Receptor("Yudi Playsteicho", "yudi.prey@pleystetion.com", "4002-8922", "PESSOA_FISICA", "112233");
	}
	
	/**
	 * Testa o construtor de um objeto tipo Usuario.
	 */
	@Test
	public void testUsuario() {
		assertTrue(user3 == null);
		
		user3 = new Receptor("Jorginho", "jorginho@gmail.com", "6598-5246", "PESSOA_FISICA", "002158");
		assertFalse(user3 == null);	
	}
	
	/**
	 * Verifica a representacao em String do Usuario
	 */
	@Test
	public void testToString() {
		assertEquals(user1.toString(), "Relampago Marquinhos/123456, relampago.marquinho@catchau.com, 2210-1022, status: doador");
	}
	
	/**
	 * Testa o metodo getNome e setNome do Usuario
	 */
	@Test
	public void testGetSetNome() {
		assertEquals(user1.getNome(), "Relampago Marquinhos");
		
		user1.setNome("Catchaum Vermelho");
		assertEquals(user1.getNome(), "Catchaum Vermelho");
	}
	
	/**
	 * Testa setEmail do Usuario
	 */
	@Test
	public void testSetEmail() {
		assertEquals(user1.toString(), "Relampago Marquinhos/123456, relampago.marquinho@catchau.com, 2210-1022, status: doador");
		
		user1.setEmail("vermelhim@catchu.com");
		assertEquals(user1.toString(), "Relampago Marquinhos/123456, vermelhim@catchu.com, 2210-1022, status: doador");
	}
	
	/**
	 * Testa setCelular do Usuario
	 */
	@Test
	public void testSetCelular() {
		assertEquals(user1.toString(), "Relampago Marquinhos/123456, relampago.marquinho@catchau.com, 2210-1022, status: doador");
		
		user1.setCelular("(88)3214-0652");
		assertEquals(user1.toString(), "Relampago Marquinhos/123456, relampago.marquinho@catchau.com, (88)3214-0652, status: doador");
	}
	
	/**
	 * Testa getClasse do Usuario
	 */
	@Test
	public void testGetClasse() {
		assertEquals(user1.getClasse(), "PESSOA_FISICA");
	}
	
	/**
	 * Testa getId do Usuario
	 */
	@Test
	public void testGetId() {
		assertEquals(user1.getId(), "123456");
	}
	
	/**
	 * Testa getStatus e setStatus do Usuario
	 */
	@Test
	public void testGetSetStatus() {
		assertTrue(user1.getStatus() != null);
		
		assertEquals(user1.getStatus(), "doador");
	}
	
	/**
	 * Testa Usuarios iguais
	 */
	@Test
	public void testUsuariosIguais() {
		Usuario user4 = new Receptor("Yudi Playsteicho", "yudi.prey@pleystetion.com", "4002-8922", "PESSOA_FISICA", "123456");
		assertTrue(user1.equals(user4));
	}
}
