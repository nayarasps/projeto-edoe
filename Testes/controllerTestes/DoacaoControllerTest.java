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


    @Before
    public void inicializaController() {
        UsuarioController usuarioController = new UsuarioController();
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
        assertEquals(itemController.encontraItensPorIdUsuario("112233").get(1).getQuantidade(), 1);

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

        assertEquals(doacaoController.listaDoacoes(), "15/10/16 - doador: Yudi Playsteicho/112233, item: colchao, quantidade: 2, receptor: Relampago Marquinhos/123456 | 14/08/18 - doador: Yudi Playsteicho/112233, item: livro, quantidade: 4, receptor: Relampago Marquinhos/123456 | 14/08/18 - doador: Yudi Playsteicho/112233, item: vestido, quantidade: 2, receptor: Relampago Marquinhos/123456");
    }

}
