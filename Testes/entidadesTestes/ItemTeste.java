package entidadesTestes;

import static org.junit.Assert.*;

import org.junit.*;

import entidades.*;

public class ItemTeste {
	
	private Item item1, item2;
	
	/**
	 * Inicializa um Item item, para os testes.
	 */
	@Before
	public void inicializaItem() {
		item1 = new Item("Pelucia", "fofo,coelho", 4);
	}
	
	/**
	 * Testa o construtor de um objeto tipo Item.
	 */
	@Test
	public void testItem() {
		assertTrue(item2 == null);
		
		item2 = new Item("Casaco", "pele,adulto", 2);
		assertFalse(item2 == null);
	}
	
	/**
	 * Testa o metodo getDescricaoItem
	 */
	@Test
	public void testGetDescricaoItem() {
		assertEquals(item1.getDescricaoItem(), "Pelucia");
	}

	/**
	 * Testa os metodos getIdItem e SetIdItem
	 */
	@Test
	public void testGetSetIdItem() {
		assertTrue(item1.getIdItem() == 0);
		
		item1.setIdItem(5);
		assertTrue(item1.getIdItem() == 5);
	}

	/**
	 * Testa metodos getTags e setTags
	 */
	@Test
	public void testGetSetTags() {
		assertEquals(item1.getTags(), "fofo,coelho");
		
		item1.setTags("usado,vaca");
		assertEquals(item1.getTags(), "usado,vaca");
	}

	/**
	 * Testa os metodos getQuantidade e setQuantidade
	 */
	@Test
	public void testGetSetQuantidade() {
		assertTrue(item1.getQuantidade() == 4);
		
		item1.setQuantidade(10);
		assertTrue(item1.getQuantidade() == 10);
	}
	
	/**
	 * Testa o metodo tagsToString;
	 */
	@Test
	public void testTagsToString() {
		assertEquals(item1.tagsToString(item1.getTags()), "[fofo, coelho]");
	}

	/**
	 * Verifica a representacao em String do Item
	 */
	@Test
	public void testToString() {
		item1.setIdItem(1);
		assertEquals(item1.toString(), "1 - Pelucia, tags: [fofo, coelho], quantidade: 4");
	}

	/**
	 * Testa os metodos getUser e setUser
	 */
	@Test
	public void testGetSetUser() {
		assertTrue(item1.getUser() == null);
		
		Usuario user = new Doador("Yudi Playsteicho", "yudi.prey@pleystetion.com", "4002-8922", "PESSOA_FISICA", "112233");
		item1.setUser(user);
		assertFalse(item1.getUser() == null);
	}

	/**
	 * Testa os metodos getPontuacaoMatch e setPontuacaoMatch
	 */
	@Test
	public void testGetSetPontuacaoMatch() {
		assertTrue(item1.getPontuacaoMatch() == 0);
		
		item1.setPontuacaoMatch(15);
		assertTrue(item1.getPontuacaoMatch() == 15);
	}

}
