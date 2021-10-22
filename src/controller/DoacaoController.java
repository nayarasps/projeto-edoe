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

        List<Item> itensDoados = itemController.getItensDoadores();
        List<Item> itensComMatch = new ArrayList<Item>();

        for (Item itemDoado: itensDoados) {
            int pontuacao = 0;

            if (itemDoado.getDescricao().equals(itemNecessario.getDescricao())) {
                pontuacao += 20;

                pontuacao += comparaTags(itemDoado, itemNecessario);

                itemDoado.setPontuacaoMatch(pontuacao);
                itensComMatch.add(itemDoado);
            }


        }

        return itensComMatch.toString();
    }

    private String formataSaidaMatches(List<Item> itensComMatch) {
        StringBuilder saida = new StringBuilder();

        Collections.sort(itensComMatch, Item.comparaPontuacaoMatch);
        for (int i = 0; i < itensComMatch.size(); i++) {
            saida.append(itensComMatch.get(i).toString()).append(", doador: ").append(itensComMatch.get(i).getUser().getNome()).append("/").append(itensComMatch.get(i).getUser().getId());
            saida.append(" | ");
        }
        if (saida.toString().equals("")) {
            return "";
        }
        return saida.substring(0, saida.length() - 3);
    }

    private int comparaTags(Item itemDoado, Item itemNecessario) {
        String[] tagsItemDoado = itemDoado.getTags().split(",");
        String[] tagsItemNecessario = itemNecessario.getTags().split(",");

        int pontos = 0;

        for (String tag : tagsItemDoado) {
            for(String tagItem : tagsItemNecessario) {
                if (tagsItemNecessario.length <= tagsItemDoado.length) {
                    for (int j = 0; j < tagsItemDoado.length; j++) {
                        if (tagItem.contentEquals(tag)) {
                            pontos += 10;
                        }
                        else if (tagItem.contains(tag)) {
                            pontos += 5;
                        }
                    }
                }else if (tagsItemNecessario.length > tagsItemDoado.length) {
                    for (int j = 0; j < tagsItemNecessario.length; j++) {
                        if (tagItem.contentEquals(tag)) {
                            pontos += 10;
                        }
                        else if (tagItem.contains(tag)) {
                            pontos += 5;
                        }
                    }
                }
            }
        }

        return pontos;
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
