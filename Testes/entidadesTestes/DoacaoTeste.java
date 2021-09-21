package entidadesTestes;

import static org.junit.Assert.*;

import org.junit.*;

import entidades.*;

public class DoacaoTeste {
	
	private Doacao doacao1, doacao2;
	private Usuario doador;
	private Usuario receptor;
	
	/**
	 * Inicializa uma Doacao doacao, para os testes.
	 */
	@Before
	public void inicializaDoacao() {
		doador = new Usuario("Yudi Playsteicho", "yudi.prey@pleystetion.com", "4002-8922", "PESSOA_FISICA", "112233");
		receptor = new Usuario("Relampago Marquinhos", "relampago.marquinho@catchau.com", "2210-1022", "PESSOA_FISICA", "123456");
		doacao1 = new Doacao(doador, receptor, 3, "13/01/17", "Cobertor");
	}

	/**
	 * Testa o construtor de um objeto tipo Doacao
	 */
	@Test
	public void testDoacao() {
		assertTrue(doacao2 == null);
		
		doacao2 = new Doacao(doador, receptor, 7, "21/08/18", "Colchao");
		assertFalse(doacao2 == null);
	}

	/**
	 * Testa o metodo setQuantidadeDoada
	 */
	@Test
	public void testSetQuantidadeDoada() {
		assertEquals(doacao1.toString(), "13/01/17 - doador: Yudi Playsteicho/112233, item: Cobertor, quantidade: 3, receptor: Relampago Marquinhos/123456");
		
		doacao1.setQuantidadeDoada(10);
		assertEquals(doacao1.toString(), "13/01/17 - doador: Yudi Playsteicho/112233, item: Cobertor, quantidade: 10, receptor: Relampago Marquinhos/123456");
	}

	/**
	 * Testa o metodo getData
	 */
	@Test
	public void testGetData() {
		doacao2 = new Doacao(doador, receptor, 7, "21/08/18", "Colchao");
		
		assertEquals(doacao2.getData(), "21/08/18");
		assertEquals(doacao1.getData(), "13/01/17");
	}

	/**
	 * Testa o metodo getDescricaoItem
	 */
	@Test
	public void testGetDescricaoItem() {
		doacao2 = new Doacao(doador, receptor, 7, "21/08/18", "Colchao");
		
		assertEquals(doacao2.getDescricaoItem(), "Colchao");
		assertEquals(doacao1.getDescricaoItem(), "Cobertor");
	}
	
	/**
	 * Verifica a representacao em String da Doacao
	 */
	@Test
	public void testToString() {
		assertEquals(doacao1.toString(), "13/01/17 - doador: Yudi Playsteicho/112233, item: Cobertor, quantidade: 3, receptor: Relampago Marquinhos/123456");
		
		doacao2 = new Doacao(doador, receptor, 7, "21/08/18", "Colchao");
		assertEquals(doacao2.toString(), "21/08/18 - doador: Yudi Playsteicho/112233, item: Colchao, quantidade: 7, receptor: Relampago Marquinhos/123456");
	}

}
