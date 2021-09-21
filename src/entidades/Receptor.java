package entidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Receptor extends Usuario {
	
	private static final long serialVersionUID = -2090136157996976518L;

	public Receptor(String nome, String email, String celular, String classe, String id) {
		super(nome, email, celular, classe, id);
		this.status = "receptor";
	}
	
	public String listarItens() {
		List<Item> itensLista = new ArrayList<Item>(itens.values());
		Collections.sort(itensLista, Item.comparaIdItem);
		String saida = "";
		
		for (int i = 0; i < itensLista.size(); i++) {
			saida += itensLista.get(i).toString();
			saida += ", Receptor: " + nome + "/" + id;
			saida += " | ";
		}
		return saida;
	}
}
