package controllerTestes;

import controller.DoacaoController;
import controller.ItemController;
import controller.UsuarioController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DoacaoControllerTest {

    private ItemController itemController;
    private DoacaoController doacaoController;
    private UsuarioController usuarioController;


    @Before
    public void inicializaController() {
        usuarioController = new UsuarioController();
        itemController = new ItemController(usuarioController);
        doacaoController = new DoacaoController(itemController);

        usuarioController.adicionaReceptor("123456", "Relampago Marquinhos", "relampago.marquinho@catchau.com", "2210-1022", "PESSOA_FISICA");
        usuarioController.adicionaDoador("112233", "Yudi Playsteicho", "yudi.prey@pleystetion.com", "4002-8922", "PESSOA_FISICA");

        itemController.adicionaItemDoador("112233", "Pelucia", 4, "fofo,coelho");
        itemController.adicionaItemReceptor("123456", "Casaco", 2, "pele,adulto");
    }

    /**
     * Testa os casos de erros do metodo RealizaDoacao
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidaRealizaDoacao() {
        // Verifica Id do Item Necessario nao pode ser nulo ou vazio
        doacaoController.realizaDoacao(-1, 1, "12/10/18");
        doacaoController.realizaDoacao(0, 1, "12/10/18");
        // Verifica Id do Item Doado nao pode ser nulo ou vazio
        doacaoController.realizaDoacao(2, -1, "12/06/18");
        doacaoController.realizaDoacao(2, 0, "12/06/18");
        // Verifica Data nao pode ser nula ou vazia
        doacaoController.realizaDoacao(1, 2, null);
        doacaoController.realizaDoacao(1, 2, "");
        // Verifica Descricao igual
        doacaoController.realizaDoacao(1, 2, "11/04/18");
    }

    /**
     * Testa o metodo RealizaDoacao
     */
    @Test
    public void testRealizaDoacao() {

        itemController.adicionaItemReceptor("123456", "pelucia", 3, "fofo, coelho");

        assertEquals(doacaoController.realizaDoacao(3, 1, "12/01/18"), "12/01/18 - doador: Yudi Playsteicho/112233, item: pelucia, quantidade: 3, receptor: Relampago Marquinhos/123456");
        assertEquals(1, itemController.encontraItensPorIdUsuario("112233").get(1).getQuantidade());

        assertFalse(itemController.encontraItensPorIdUsuario("123456").containsKey(3));
    }

    /**
     * Testa o metodo ListaDoacoes
     */
    @Test
    public void testListaDoacoes() {

        itemController.adicionaItemDoador("112233", "vestido", 2, "curto, vermelho");
        itemController.adicionaItemReceptor("123456", "vestido", 3, "azul, longo");
        doacaoController.realizaDoacao(4, 3, "14/08/18");

        itemController.adicionaItemDoador("112233", "Livro", 7, "classico");
        itemController.adicionaItemReceptor("123456", "livro", 4, "");
        doacaoController.realizaDoacao(6, 5, "14/08/18");

        itemController.adicionaItemDoador("112233", "Colchao", 2, "molas");
        itemController.adicionaItemReceptor("123456", "colchao", 3, "infantil, espuma");
        doacaoController.realizaDoacao(8, 7, "15/10/16");

        assertEquals("15/10/16 - doador: Yudi Playsteicho/112233, item: colchao, quantidade: 2, receptor: Relampago Marquinhos/123456 | 14/08/18 - doador: Yudi Playsteicho/112233, item: livro, quantidade: 4, receptor: Relampago Marquinhos/123456 | 14/08/18 - doador: Yudi Playsteicho/112233, item: vestido, quantidade: 2, receptor: Relampago Marquinhos/123456", doacaoController.listaDoacoes());
    }

    @Test
    public void testMatch() {
        usuarioController.getUsuarios().get("123456");
        itemController.adicionaItemReceptor("123456", "Pelucia", 4, "fofo,coelho");

        itemController.adicionaItemDoador("112233", "pelucia", 3, "fofo, gato");
        itemController.adicionaItemDoador("112233", "pelucia", 3, "gato, fofo");
        itemController.adicionaItemDoador("112233", "pelucia", 3, "coelho, fofo");
        itemController.adicionaItemDoador("112233", "pelucia", 3, "algodao, macaco");

        assertEquals("1 - pelucia, tags: [fofo, coelho], quantidade: 4, doador: Yudi Playsteicho/112233 | 4 - pelucia, tags: [fofo,  gato], quantidade: 3, doador: Yudi Playsteicho/112233 | 6 - pelucia, tags: [coelho,  fofo], quantidade: 3, doador: Yudi Playsteicho/112233 | 5 - pelucia, tags: [gato,  fofo], quantidade: 3, doador: Yudi Playsteicho/112233 | 7 - pelucia, tags: [algodao,  macaco], quantidade: 3, doador: Yudi Playsteicho/112233", doacaoController.match("123456", 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidaMatch() {
        // Valida IdReceptor nao deve ser vazio ou nulo
        doacaoController.match(null, 1);
        doacaoController.match("", 1);
        // Valida IdItem nao pode ser negativo ou igual a zero
        doacaoController.match("123456", -1);
        doacaoController.match("123456", 0);
        // Valida Usuario no encontrado
        usuarioController.getUsuarios().containsKey("4515654");
        // Valida Usuario deve ser um receptor
        doacaoController.match("123456", 1);

        usuarioController.getUsuarios().get("123456");
        // Valida Item nao encontrado
        itemController.getItensDoadores().get(45);

    }
}
