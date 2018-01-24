package rmiserver;
import java.io.Serializable;

public class Recompensa implements Serializable {
	
	String nome;
	float valor;
	Projeto proj;
	int id;
	Utilizador user;
	int valida;	// se o projeto acabou com sucesso, fica a 1 e o user tem direito
	// data estimada
	public Recompensa(Projeto proj, String nome, float valor, int id, int valida) {
		this.nome = nome;
		this.valor = valor;
		this.proj = proj;
		this.id = id;
		this.valida=valida;
		/* id � sempre o indice em que est� na lista de recompensas do projeto */
	}
	
	
	public String imprimeRecompensa(int i) {
		return (i+"-"+this.nome+"<br>Valor: "+this.valor+"<br>Id: "+id+"<br>");
	}
}
