package controller;

import entidades.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DoacaoController {

    private final Validacao valida = new Validacao();
    private final ItemController itemController;
    private final List<Doacao> doacoes;

    public DoacaoController(ItemController itemController) {
        this.itemController = itemController;
        this.doacoes = new ArrayList<Doacao>();
    }

    public String realizaDoacao(int idItemNecessario, int idItemDoado, String data) {
        valida.verificaIdItem(idItemDoado);
        valida.verificaIdItem(idItemNecessario);
        valida.verificaData(data);

        Receptor receptor = encontraReceptorPorIdItem(idItemNecessario);
        Doador doador = encontraDoadorPorIdItem(idItemDoado);

        Item itemDoado = doador.getItemById(idItemDoado);
        Item itemNecessario = receptor.getItemById(idItemNecessario);

        valida.validaDescricoesDoacoes(itemDoado.getDescricao(), itemNecessario.getDescricao());

        Doacao doacao = new Doacao(doador, receptor, 0, data, itemNecessario.getDescricao());

        int novaQuantidadeItens = Math.abs(itemNecessario.getQuantidade() - itemDoado.getQuantidade());
        boolean maiorQuantidadeItemDoado = itemDoado.getQuantidade() > itemNecessario.getQuantidade();
        boolean maiorQuantidadeItemNecessario = itemDoado.getQuantidade() < itemNecessario.getQuantidade();

        if (maiorQuantidadeItemDoado) {
            doacao.setQuantidadeDoada(itemNecessario.getQuantidade());
            itemDoado.setQuantidade(novaQuantidadeItens);
            receptor.removeItembyid(idItemNecessario);
        }
        else if (maiorQuantidadeItemNecessario) {
            doacao.setQuantidadeDoada(itemDoado.getQuantidade());
            itemNecessario.setQuantidade(novaQuantidadeItens);
            doador.removeItembyid(idItemDoado);
        }
        else {
            doacao.setQuantidadeDoada(itemNecessario.getQuantidade());
            receptor.removeItembyid(idItemNecessario);
            doador.removeItembyid(idItemDoado);
        }

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

    public String match(String idReceptor, int idItemNecessario) {
        valida.verificaIdItem(idItemNecessario);
        valida.verificaIdUsuario(idReceptor);

        Receptor receptor = encontraReceptorPorIdItem(idItemNecessario);
        Item itemNecessario = receptor.getItemById(idItemNecessario);

        String saida = "";
        List<Item> list = new ArrayList<Item>();
        for (String u : itens.keySet()) {
            for (Integer i : itens.get(u).keySet()) {
                int pontuacao = 0;
                if (itens.get(u).get(i).getUser().getStatus().equals("doador")) {
                    if (itens.get(u).get(i).getDescricao().equals(itens.get(idReceptor).get(idItemNecessario).getDescricao())) {
                        pontuacao += 20;
                        for (String tag : itens.get(u).get(i).getTags().split(",")) {
                            for(String tagItem : itens.get(idReceptor).get(idItemNecessario).getTags().split(",")) {
                                if (itens.get(idReceptor).get(idItemNecessario).getTags().split(",").length <= itens.get(u).get(i).getTags().split(",").length) {
                                    for (int j = 0; j < itens.get(u).get(i).getTags().split(",").length; j++) {
                                        if (tagItem.contentEquals(tag)) {
                                            pontuacao += 10;
                                        }
                                        else if (tagItem.contains(tag)) {
                                            pontuacao += 5;
                                        }
                                    }
                                }else if (itens.get(idReceptor).get(idItemNecessario).getTags().split(",").length > itens.get(u).get(i).getTags().split(",").length) {
                                    for (int j = 0; j < itens.get(idReceptor).get(idItemNecessario).getTags().split(",").length; j++) {
                                        if (tagItem.contentEquals(tag)) {
                                            pontuacao += 10;
                                        }
                                        else if (tagItem.contains(tag)) {
                                            pontuacao += 5;
                                        }
                                    }
                                }
                            }
                        }
                        itens.get(u).get(i).setPontuacaoMatch(pontuacao);
                        list.add(itens.get(u).get(i));
                    }
                }
            }
        }
        Collections.sort(list, Item.comparaPontuacaoMatch);
        for (int i = 0; i < list.size(); i++) {
            saida += list.get(i).toString() + ", doador: " + list.get(i).getUser().getNome() + "/" + list.get(i).getUser().getId();
            saida += " | ";
        }
        if (saida.equals("")) {
            return "";
        }
        return saida.substring(0, saida.length() - 3);
    }


    private Doador encontraDoadorPorIdItem(int idItem) {
        Doador doador = itemController.encontraDoadorPorIdItem(idItem);
        if (doador == null) {
            Receptor receptor = encontraReceptorPorIdItem(idItem);
            if (receptor != null) {
                throw new IllegalArgumentException("O Usuario deve ser um doador: " + receptor.getId()+ ".");
            } else {
                valida.itemNaoEncontrado(idItem);
            }
        }
        return doador;
    }

    private Receptor encontraReceptorPorIdItem(int idItem) {
        Receptor receptor = itemController.encontraReceptorPorIdItem(idItem);
        if (receptor == null) {
            Doador doador = encontraDoadorPorIdItem(idItem);
            if (doador != null) {
                throw new IllegalArgumentException("O Usuario deve ser um receptor: " + doador.getId()+ ".");
            } else {
                valida.itemNaoEncontrado(idItem);
            }
        }
        return receptor;
    }
}
