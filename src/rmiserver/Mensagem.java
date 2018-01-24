package rmiserver;

import java.io.Serializable;

public class Mensagem implements Serializable {
	int idUser;
	int idProjeto;
	String comentario;
	
	public Mensagem(int idUser, int idProjeto, String mensagem) {
		this.idUser=idUser;
		this.idProjeto=idProjeto;
		this.comentario=mensagem;
	}
}
