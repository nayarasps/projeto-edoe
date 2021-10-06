package controller;

import entidades.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DoacaoController {

    private final Validacao valida = new Validacao();
    private ItemController itemController;
    private List<Doacao> doacoes;

    public DoacaoController(ItemController itemController) {
        this.itemController = itemController;
        this.doacoes = new ArrayList<Doacao>();
    }

    private Doador verificaItemDoadoExiste(int idItem) {
        Doador doador = itemController.encontraDoadorPorIdItem(idItem);
        if (doador == null) {
            valida.itemNaoEncontrado(idItem);
        }
        return doador;
    }

    private Receptor verificaItemNecessarioExiste(int idItem) {
        Receptor receptor = itemController.encontraReceptorPorIdItem(idItem);
        if (receptor == null) {
            valida.itemNaoEncontrado(idItem);
        }
        return receptor;
    }

    // Os valores se invertem pois depois de utilizar o método modificaQuantidadeItens() os valores dos itens se alteram
    private void removeItensDoados(Item itemDoado, Item itemNecessario, Receptor receptor, Doador doador) {
        if (itemDoado.getQuantidade() < itemNecessario.getQuantidade()) {
            receptor.removeItembyid(itemNecessario.getId());
        }
        else if (itemDoado.getQuantidade() > itemNecessario.getQuantidade()) {
            doador.removeItembyid(itemDoado.getId());
        }
        else {
            receptor.removeItembyid(itemNecessario.getId());
            doador.removeItembyid(itemDoado.getId());
        }
    }

    private void modificaQuantidadeItens(Item itemDoado, Item itemNecessario, Doacao doacao, int novaQuantidadeItens) {
        if (itemDoado.getQuantidade() > itemNecessario.getQuantidade()) {
            doacao.setQuantidadeDoada(itemNecessario.getQuantidade());
            itemDoado.setQuantidade(novaQuantidadeItens);
        }
        else if (itemDoado.getQuantidade() < itemNecessario.getQuantidade()) {
            doacao.setQuantidadeDoada(itemDoado.getQuantidade());
            itemNecessario.setQuantidade(novaQuantidadeItens);
        }
        else {
            doacao.setQuantidadeDoada(itemNecessario.getQuantidade());
        }
    }


    public String realizaDoacao(int idItemNecessario, int idItemDoado, String data) {
        valida.verificaIdItem(idItemDoado);
        valida.verificaIdItem(idItemNecessario);
        valida.verificaData(data);

        Receptor receptor = verificaItemNecessarioExiste(idItemNecessario);
        Doador doador = verificaItemDoadoExiste(idItemDoado);

        Item itemDoado = doador.getItemById(idItemDoado);
        Item itemNecessario = receptor.getItemById(idItemNecessario);

        valida.validaDescricoesDoacoes(itemDoado.getDescricao(), itemNecessario.getDescricao());

        Doacao doacao = new Doacao(doador, receptor, 0, data, itemNecessario.getDescricao());

        int novaQuantidadeItens = Math.abs(itemNecessario.getQuantidade() - itemDoado.getQuantidade());

        if (itemDoado.getQuantidade() > itemNecessario.getQuantidade()) {
            doacao.setQuantidadeDoada(itemNecessario.getQuantidade());
            itemDoado.setQuantidade(novaQuantidadeItens);
            receptor.removeItembyid(idItemNecessario);
        }
        else if (itemDoado.getQuantidade() < itemNecessario.getQuantidade()) {
            doacao.setQuantidadeDoada(itemDoado.getQuantidade());
            itemNecessario.setQuantidade(novaQuantidadeItens);
            doador.removeItembyid(idItemDoado);
        }
        else {
            doacao.setQuantidadeDoada(itemNecessario.getQuantidade());
            receptor.removeItembyid(idItemNecessario);
            doador.removeItembyid(idItemDoado);
        }

        //modificaQuantidadeItens(itemDoado, itemNecessario, doacao, novaQuantidadeItens);
        //removeItensDoados(itemDoado, itemNecessario, receptor, doador);

        doacoes.add(doacao);

        return doacao.toString();
    }

    public String listaDoacoes() {
        StringBuilder saida = new StringBuilder();
        doacoes.sort(Doacao.comparaData);
        Collections.reverse(doacoes);
        for (Doacao doacao : doacoes) {
            saida.append(doacao.toString());
            saida.append(" | ");
        }
        return saida.substring(0, saida.length() - 3);
    }

}
