package entidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Doador extends Usuario {

	private static final long serialVersionUID = -2090136157996976518L;

	public Doador(String nome, String email, String celular, String classe, String id) {
		super(nome, email, celular, classe, id);
		this.status = "doador";
	}

	public HashMap<Item, String> listarItens() {

		HashMap<Item, String> itensDoador = new HashMap<>();

		for (Item item : itens.values()) {

			String saida = item.toString() +
					", doador: " + nome + "/" + id +
					" | ";
			itensDoador.put(item, saida);
		}
		return itensDoador;
	}
}
