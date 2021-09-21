package backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeSet;

import controller.Controller;
import entidades.*;

/**
 * Classe responsavel por salvar os dados da execucao atual e carregar os dados
 * de execucoes passadas.
 *
 */
public class Persistencia {

	/**
	 * Salva os dados da atual execucao na memoria da maquina para que possam ser
	 * usadas em execucoes futuras.
	 * 
	 * @param usuarios
	 *            o mapa de usuarios que foram cadastrado no sistema.
	 * @param itens
	 *            o mapa dos itens que foram cadastrados no sistema.
	 * @param doacoes
	 *            a lista de doaces que foram cadastrados no sistema.
	 * @param descritores
	 *            a arvore de descritores que foram cadastrados no sistema.
	 */
	public void salvar(LinkedHashMap<String, Usuario> usuarios, HashMap<String, HashMap<Integer, Item>> itens, 
			List<Doacao> doacoes, TreeSet<String> descritores) {
		try {
			FileOutputStream fos = new FileOutputStream("dados.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(usuarios);
			oos.writeObject(itens);
			oos.writeObject(doacoes);
			oos.writeObject(descritores);

			oos.close();
		}

		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}


	
	@SuppressWarnings("unchecked")
	public void carregar(Controller c) {
		ObjectInputStream ois = null;
		try {
			if (!new File("dados.txt").exists()) {
				FileOutputStream fos = new FileOutputStream("dados.txt");
				fos.close();
			}

			FileInputStream fis = new FileInputStream("dados.txt");

			if (fis.available() > 0) {
				ois = new ObjectInputStream(fis);
				c.setDadosUsuarios((LinkedHashMap<String, Usuario>) ois.readObject());
				c.setDadosItens((HashMap<String, HashMap<Integer, Item>>) ois.readObject());
				c.setDadosDoacoes((List<Doacao>) ois.readObject());
				c.setDadosDescritores((TreeSet<String>) ois.readObject());
				ois.close();
			}

		} catch (IOException | ClassNotFoundException ioecnfe) {
			ioecnfe.printStackTrace();
		}

	}

}