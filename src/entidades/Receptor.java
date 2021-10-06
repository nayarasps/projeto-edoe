package entidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Receptor extends Usuario {
	
	private static final long serialVersionUID = -2090136157996976518L;

	public Receptor(String nome, String email, String celular, String classe, String id) {
		super(nome, email, celular, classe, id);
		this.status = "receptor";
	}
	
	public HashMap<Integer, String> listarItens() {

		HashMap<Integer, String> itensReceptor = new HashMap<>();

		for (Item item : itens.values()) {

			String saida = item.toString() +
					", Receptor: " + nome + "/" + id +
					" | ";
			itensReceptor.put(item.getId(), saida);
		}
		return itensReceptor;
	}
}
