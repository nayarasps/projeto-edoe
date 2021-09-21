package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import entidades.Doacao;
import entidades.Doador;
import entidades.Item;
import entidades.Receptor;
import entidades.Usuario;

public class UsuarioController {
	

	private LinkedHashMap<String, Usuario> usuarios;
	Validacao valida = new Validacao();
	

	public UsuarioController() {
		this.usuarios = new LinkedHashMap<>();
	}

	public void lerReceptores(String caminho) throws IOException {
		Scanner sc = new Scanner(new File(caminho));
		String linha = null;
		while (sc.hasNextLine()) {
			linha = sc.nextLine();
			if (linha.equals("id,nome,e-mail,celular,classe")) {
				continue;
			}
			String[] dadosReceptores = linha.split(",");
			if (dadosReceptores.length != 5) {
				sc.close();
				throw new IOException("Campos invalidos");
				
			}
			valida.verificaAdicionaUsuario(dadosReceptores[1], dadosReceptores[2], dadosReceptores[3], dadosReceptores[4], dadosReceptores[0]);
			valida.validaClasseUsuario(dadosReceptores[4]);
		
			Usuario usuario = new Receptor(dadosReceptores[1], dadosReceptores[2], dadosReceptores[3], dadosReceptores[4], dadosReceptores[0]);
			this.usuarios.put(dadosReceptores[0], usuario);
		}
		sc.close();
	}
	
	
	public String adicionaDoador(String id, String nome, String email, String celular, String classe) {
		valida.verificaAdicionaUsuario(nome, email, celular, classe, id);
		valida.validaClasseUsuario(classe);
		
		if (usuarios.containsKey(id)) {
			throw new IllegalArgumentException("Usuario ja existente: " + id + ".");
		}
		
		Usuario usuario = new Doador(nome, email, celular, classe, id);
		this.usuarios.put(id, usuario);
		return id;
	}

	// Adicionado para fins de teste
	public String adicionaReceptor(String id, String nome, String email, String celular, String classe) {
		valida.verificaAdicionaUsuario(nome, email, celular, classe, id);
		valida.validaClasseUsuario(classe);
		
		if (usuarios.containsKey(id)) {
			throw new IllegalArgumentException("Usuario ja existente: " + id + ".");
		}
		
		Usuario usuario = new Receptor(nome, email, celular, classe, id);
		this.usuarios.put(id, usuario);
		return id;
	}
	
	public String pesquisaUsuarioPorId(String id) {
		valida.verificaIdUsuario(id);
		if (!(usuarios.containsKey(id))) {
			valida.usuarioNaoEncontrado(id);
		}
		return usuarios.get(id).toString();
	}
	
	public String pesquisaUsuarioPorNome(String nome) {
		valida.verificaNome(nome);
		String saida = "";
	
		for (Usuario o : usuarios.values()) {
			if (o.getNome().equals(nome)) {
				saida += o.toString();
				saida += " | ";
				}
			}
		if("".equals(saida)) {
			valida.usuarioNaoEncontrado(nome);
		}
		return saida.substring(0, saida.length() - 3);
	}
	
	/**
	 * Metodo que atualiza um Usuario existente em usuarios.
	 * 
	 * @param id que representa o id de um Usuario, utilizado para encontra-lo em usuarios.
	 * @param novoNome que representa o novo nome do Usuario a ser atualizado, se nulo ou vazio, o nome nao e atualizado.
	 * @param novoEmail que representa o novo email do Usuario a ser atualizado, se nulo ou vazio, o email nao e atualizado.
	 * @param novoCelular que representa o novo celular do Usuario a ser atualizado, se nulo ou vazio, o celular nao e atualizado.
	 * @exception IllegalArgumentException que ocorre quando o id key nao existe em usuarios.
	 * @return toString do Usuario atualizado.
	 */
	public String atualizaUsuario(String id, String novoNome, String novoEmail, String novoCelular) {
		valida.verificaIdUsuario(id);
		if (!(usuarios.containsKey(id))) {
			valida.usuarioNaoEncontrado(id);
		}
		
		// Nao atualiza o Usuario, retornando-o normalmente
		if  (("".equals(novoNome) || novoNome == null) && ("".equals(novoCelular) || novoCelular == null) && ("".equals(novoEmail) || novoEmail == null)) {
			return usuarios.get(id).toString();
		}
		// Atualiza o nome do Usuario
		else if (("".equals(novoEmail) || novoEmail == null) && ("".equals(novoCelular) || novoCelular == null)) {
			usuarios.get(id).setNome(novoNome);
		}
		// Atualiza o email do Usuario
		else if (("".equals(novoNome) || novoNome == null) && ("".equals(novoCelular) || novoCelular == null)) {
			usuarios.get(id).setEmail(novoEmail);
		}
		// Atualiza o celular do Usuario
		else if (("".equals(novoNome) || novoNome == null) && ("".equals(novoEmail) || novoEmail == null)) {
			usuarios.get(id).setCelular(novoCelular);
		}
		return usuarios.get(id).toString();
	}
	
	
	public void removeUsuario(String id) {
		valida.verificaIdUsuario(id);
		if (!(usuarios.containsKey(id))) {
			valida.usuarioNaoEncontrado(id);
		}
		
		usuarios.remove(id);
	}
	
	public LinkedHashMap<String, Usuario> getUsuarios(){
		return usuarios;
	}
	
	public List<Receptor> getReceptores() {
		List<Receptor> receptores = new ArrayList<Receptor>();
		
		for (String id : usuarios.keySet()) {
			if (usuarios.get(id).getStatus().equals("receptor")) {
				receptores.add((Receptor) usuarios.get(id));
			}
		}
		return receptores;
	}
	
	public List<Doador> getDoadores() {
		List<Doador> doadores = new ArrayList<Doador>();
		
		for (String id : usuarios.keySet()) {
			if (usuarios.get(id).getStatus().equals("doador")) {
				doadores.add((Doador) usuarios.get(id));
			}
		}
		return doadores;
	}
	 
}
