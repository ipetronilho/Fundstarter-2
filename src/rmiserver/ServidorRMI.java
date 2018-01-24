package rmiserver;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import ws.WebSocketAnnotation;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorRMI extends UnicastRemoteObject implements InterfaceRMI {
	
	
	private static final long serialVersionUID = 1L;
	AtomicInteger contadorIDProjeto = new AtomicInteger(0);
	AtomicInteger contadorIDRecompensa = new AtomicInteger(0);

	BaseDeDados bd = new BaseDeDados();
	
	public ServidorRMI() throws RemoteException {
		super();
	}
	
	public static void main(String args[]) throws IOException, ClassNotFoundException {

		try {
			
			InterfaceRMI servRMI = new ServidorRMI();
			LocateRegistry.createRegistry(1099).rebind("server", servRMI);
			System.out.println("Server ready...");
			servRMI.iniciaDados(servRMI);
			ThreadValidade MyThread = new ThreadValidade(servRMI); 
			MyThread.start();
			
		} catch (RemoteException re) {
			System.out.println("Exception: " + re);
		}

	}
	
	public void a() {
		System.out.println("Ola");
	}
	
	/* inicializa os dados iniciais */
	synchronized public void iniciaDados(InterfaceRMI servRMI) {
		Utilizador user1 = new Utilizador("Primeiro", "passe1", 100);
		Utilizador user2 = new Utilizador("Segundo", "passe2", 100);
		Utilizador user3 = new Utilizador("Terceiro", "passe3", 100);
		
		user1.id=bd.listaUtilizadores.size();
		bd.listaUtilizadores.add(user1);
		
		user2.id=bd.listaUtilizadores.size();
		bd.listaUtilizadores.add(user2);
		
		user3.id=bd.listaUtilizadores.size();
		bd.listaUtilizadores.add(user3);
		
		String descricao1="Corda que conta o n� de saltos por minuto";
		String descricao2="Cadeira que se ajusta automaticmente � altura da pessoa e da mesa";
		String descricao3="Tinta que muda de cor de acordo com a luz";
		 
		Calendar dataFinal1 = new GregorianCalendar(2016, Calendar.NOVEMBER, 15);
		Calendar dataFinal2 = new GregorianCalendar(2015, Calendar.DECEMBER, 28);
		Calendar dataFinal3 = new GregorianCalendar(2013, Calendar.MAY, 10);
		
		int projID1 = contadorIDProjeto.getAndIncrement();
		int projID2= contadorIDProjeto.getAndIncrement();
		int projID3 = contadorIDProjeto.getAndIncrement();
		
		Projeto proj1 = new Projeto(user1, "FitJump", 2, projID1, dataFinal1, descricao1);
		Projeto proj2 = new Projeto(user1, "CoolChair", 10, projID2, dataFinal2, descricao2);
		Projeto proj3 = new Projeto(user1, "GreatPaint", 50, projID3, dataFinal3, descricao3);
		
		Recompensa rec = new Recompensa(proj1, "nome na lista", 5, contadorIDRecompensa.getAndIncrement(), 0);
		Recompensa rec2 = new Recompensa(proj1, "recebe unidade", 60, contadorIDRecompensa.getAndIncrement(), 0);
		Recompensa rec3 = new Recompensa(proj1, "recebe unidades", 100, contadorIDRecompensa.getAndIncrement(), 0);
		
		Voto v = new Voto("vermelho");
		Voto v2 = new Voto("azul");
		
		proj1.addRecompensa(proj1, rec);
		proj1.addRecompensa(proj1, rec2);
		proj1.addRecompensa(proj1, rec3);
		
		Recompensa recP2 = new Recompensa(proj1, "nome na lista", 5, contadorIDRecompensa.getAndIncrement(), 0);
		Recompensa recP22 = new Recompensa(proj1, "recebe unidade", 60, contadorIDRecompensa.getAndIncrement(), 0);
		proj2.addRecompensa(proj2, recP2);
		proj2.addRecompensa(proj2, recP22); 
		
		Recompensa recP3 = new Recompensa(proj1, "nome na lista", 5, contadorIDRecompensa.getAndIncrement(), 0);
		Recompensa recP32 = new Recompensa(proj1, "recebe unidade", 60, contadorIDRecompensa.getAndIncrement(), 0);
		proj3.addRecompensa(proj3, recP3);
		proj3.addRecompensa(proj3, recP32); 
		
		
		proj1.listaVotos.add(v);
		proj1.listaVotos.add(v2);
		proj2.listaVotos.add(v);
		proj2.listaVotos.add(v2);
		proj3.listaVotos.add(v);
		proj3.listaVotos.add(v2);
		
		user1.listaIDsProjeto.add(projID1);
		user2.listaIDsProjeto.add(projID2);
		user3.listaIDsProjeto.add(projID3);
		
		
		bd.listaProjetos.add(proj1);
		bd.listaProjetos.add(proj2);
		bd.listaProjetos.add(proj3);
		
		
		//servRMI.guardaFicheiro();
	}

	/* CONSULTAR DADOS */
	public String listaProjetosActuais() {
		int i;
		String str="";
		for (i=0;i<bd.listaProjetos.size();i++) {
			if (bd.listaProjetos.get(i).verificaValidade()){
				str= str.concat(bd.listaProjetos.get(i).imprime());
				str=str.concat("RECOMPENSAS<br>");
				str = str.concat(listaRecompensas(bd.listaProjetos.get(i).id));
			}
		}
		return str;
	}
	
	public String listaProjetosAntigos() {
		int i;
		String str="";
		for (i=0;i<bd.listaProjetos.size();i++) {
			if (!bd.listaProjetos.get(i).verificaValidade()){
				str= str.concat(bd.listaProjetos.get(i).imprime()); 
				str=str.concat("RECOMPENSAS<br>");
				str = str.concat(listaRecompensas(bd.listaProjetos.get(i).id));
			}
		}
		return str;
	}
	
	/* LOGIN */
	/* adiciona um utilizador e devolve o seu ID */
	synchronized public int registaConta(String nomeUser, String password) {
		float saldoDefault=100;
		int userID = bd.listaUtilizadores.size();
		Utilizador user = new Utilizador(nomeUser, password, saldoDefault);
		user.setId(userID); // id do user � a sua posi��o na lista de users
		
		bd.listaUtilizadores.add(user);
		System.out.println("Acabei de criar o user "+nomeUser+" com id "+userID);
		return userID;
	}
	
	/* verifica se o username e password s�o v�lidos */
	public int verificaLogin(String nomeUser, String password)  {
		int i;
		for (i=0;i<bd.listaUtilizadores.size();i++) {
			if ((bd.listaUtilizadores.get(i).nome.compareToIgnoreCase(nomeUser) == 0)&& (bd.listaUtilizadores.get(i).password.compareToIgnoreCase(password) == 0))
				return i;
		}
		return -1;
	}
	
	/* FICHEIROS */
	/* carrega dados de ficheiro*/
	synchronized public void carregaFicheiro() throws IOException, ClassNotFoundException {
		FileInputStream fin = new FileInputStream("ficheiros/utilizadores.txt");
		ObjectInputStream objin = new ObjectInputStream(fin);
		bd.listaUtilizadores = (ArrayList) objin.readObject();
		
        fin = new FileInputStream("ficheiros/projetos.txt");
        objin = new ObjectInputStream(fin);
        bd.listaProjetos = (ArrayList) objin.readObject();
        
        // vai buscar o �ltimo id
        contadorIDProjeto.set(bd.listaProjetos.get(bd.listaProjetos.size()-1).id+1);
        iniciaContadorRecompensas();
	}
	
	/* guarda dados em ficheiro */
	synchronized public void guardaFicheiro() throws IOException {
		
        FileOutputStream fout = new FileOutputStream("ficheiros/projetos.txt");
        ObjectOutputStream objout = new ObjectOutputStream(fout);
        objout.writeObject(bd.listaProjetos); // escrever arrayList
        objout.close();
        
        fout = new FileOutputStream("ficheiros/utilizadores.txt");
        objout = new ObjectOutputStream(fout);

        objout.writeObject(bd.listaUtilizadores);
        objout.close();
		
	}

	/* CONSULTA */
	/* retorna o saldo */
	public String consultarSaldo(int userID) {
		return "Balance: "+bd.listaUtilizadores.get(userID).getSaldo()+"<br>";
	}
	
	/* devolve a lista de Projetos em que o user � admin */
	public String consultarProjetos(int userID) {
		int i, j;
		String str="";
		
		Utilizador user = bd.listaUtilizadores.get(userID);
		for (i=0;i<user.listaIDsProjeto.size();i++) {
			for (j=0;j<bd.listaProjetos.size();j++) {
				if (bd.listaProjetos.get(j).id == user.listaIDsProjeto.get(i))
					str = str.concat(bd.listaProjetos.get(j).imprime());
			}
		}
		return str;
	}
	
	/* devolve informa��o sobre o Projeto especificado */
	public String imprimeDetalhesProjeto(int projID) {
		int i, j;
		String str="";
		for (j=0;j<bd.listaProjetos.size();j++) {
			if (bd.listaProjetos.get(j).id == projID)
				str = str.concat(bd.listaProjetos.get(j).imprimeDetalhes(bd.listaProjetos.get(j)));
		}
		
		return str;
	}
	
	
	public String imprimeDoacoesUser(int userID) {
		String str="";
		Utilizador user = bd.listaUtilizadores.get(userID); 
		int idProjeto;
		float doacao;
		Set<Entry<Integer, Float>> set = user.listaDoacoesUser.entrySet();
		Iterator<Entry<Integer, Float>> i= set.iterator();
		
		while (i.hasNext()) {
			Map.Entry <Integer, Float> mentry = (Entry<Integer, Float>) i.next();
			idProjeto = (int) mentry.getKey();
			doacao = (float) mentry.getValue();
			Projeto proj = procuraProjetoID(idProjeto);
			if(proj!=null)
			str = str.concat("Projeto: "+proj.nome+"<br>Doacao: "+doacao+"<br>");
			
		}
		return str;
	}
	
	/* devolve a lista de recompensas do user - check! */
	public String consultarRecompensas(int userID) {
		int i, j, m;
		Projeto proj;
		Recompensa rec;
		String str="";

		for (i=0;i<bd.listaProjetos.size();i++) {
			proj = bd.listaProjetos.get(i);
			str = str.concat(procuraRecompensa(proj, userID ));
			
		}
		
		return str;
	}
	
	/* pesquisa por nome e devolve o ID do Projeto (auxiliar) */
	public int procuraProjeto(String nome) {
		int i;
		for (i=0;i<bd.listaProjetos.size();i++) {
			if (bd.listaProjetos.get(i).nome.compareToIgnoreCase(nome)==0) {
				return bd.listaProjetos.get(i).id;
			}
		}
		return -1;
	}

	/* pesquisa por ID e devolve o Projeto (auxiliar) */
	public Projeto procuraProjetoID(int projID) {
		int i;
		for (i=0;i<bd.listaProjetos.size();i++) {
			if (bd.listaProjetos.get(i).id == projID) {
				return bd.listaProjetos.get(i);
			}
		}
		return null;
	}
	
	/* devolve uma string com todas as recompensas do user (auxiliar)*/
	public String procuraRecompensa(Projeto proj, int userID) {
		int i, j;
		String str="";
		
		Utilizador user = bd.listaUtilizadores.get(userID);
		// vai a cada projeto do user e compara todas as recompensas com as do user at� chegar ao destino
		for (i=0;i<proj.listaRecompensas.size();i++) {
			for (j=0;j<user.listaIDsRecompensas.size();j++) {
				if (proj.listaRecompensas.get(i).id == user.listaIDsRecompensas.get(j)) {
					if (proj.listaRecompensas.get(i).valida==0)
						str=str.concat("(Por validar): <br>");
					else if (proj.listaRecompensas.get(i).valida==1)
						str=str.concat("(Garantida): <br>");
					str=str.concat(proj.listaRecompensas.get(i).imprimeRecompensa(i));
				}
			}
		}
		
		return str;
	}

	public String listaRecompensas(int projID) {
		int i=0;
		String str="";
		Projeto p = procuraProjetoID(projID);
		for (i=0;i<p.listaRecompensas.size();i++) {
			str = str.concat(p.listaRecompensas.get(i).imprimeRecompensa(i));
		}
		return str;
	}
	
	
	public String imprimeVotos(int projID) {
		Projeto proj = procuraProjetoID(projID);
		String str="";
		int i=0;
		for (i=0;i<proj.listaVotos.size();i++) {
			str=str.concat(proj.listaVotos.get(i).imprime(i));
		}
		return str;
	}
	
	synchronized public void escolheVoto(int userID, int projID, int index) {
		Projeto proj = procuraProjetoID(projID);
		proj.listaVotos.get(index).listaUsers.add(userID);
		proj.listaVotos.get(index).contador+=1;
	}
	
	/* OPERA��ES DE PROJETOS */
	
	/* criar um projeto - check! */
	synchronized public void criaProjeto(int userID, String nome, float valor_objetivo, Calendar dataFinal, String descricao) {
		int id = contadorIDProjeto.getAndIncrement();
		bd.listaUtilizadores.get(userID).listaIDsProjeto.add(id);
		Projeto proj = new Projeto(bd.listaUtilizadores.get(userID), nome, valor_objetivo, id, dataFinal, descricao);
		bd.listaProjetos.add(proj);
		
	}
	
	synchronized public void eliminaProjeto(int userID, int projID) {
		
		
		bd.listaUtilizadores.get(userID).listaIDsProjeto.remove((Object)projID); // retira do admin
		Projeto proj = procuraProjetoID(projID);
		
		// devolve dinheiro aos users
		devolveDinheiro(proj);
		
		retiraRecompensa(userID, proj);
		
		retiraDoacao(userID, projID);
		
		bd.listaProjetos.remove((Object)bd.listaProjetos.get(projID)); // retira da lista global de projetos
		
		try {
			guardaFicheiro();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// funciona
	public void devolveDinheiro(Projeto proj) {
		String str="";
		int userID;
		float doacao, saldo;
		Set<Entry<Integer, Float>> set = proj.listaDoacoes.entrySet();
		Iterator<Entry<Integer, Float>> i= set.iterator();
		
		while (i.hasNext()) { // para cada user que doou
			Map.Entry <Integer, Float> mentry = (Entry<Integer, Float>) i.next();
			userID = (int) mentry.getKey();
			doacao = (float) mentry.getValue();
			
			saldo = bd.listaUtilizadores.get(userID).getSaldo();
			bd.listaUtilizadores.get(userID).setSaldo(saldo+doacao);
			
			retiraRecompensa(userID, proj);
		}
		
		try {
			guardaFicheiro();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	synchronized public void retiraRecompensa(int userID, Projeto proj) {
		int i, j;
		// pesquisa a recompensa e remove
		for (i=0;i<proj.listaRecompensas.size();i++) {
			for (j=0;j<bd.listaUtilizadores.get(userID).listaIDsRecompensas.size();j++) {
				if (proj.listaRecompensas.get(i).id == bd.listaUtilizadores.get(userID).listaIDsRecompensas.get(j)) {
					bd.listaUtilizadores.get(userID).listaIDsRecompensas.remove((Object)bd.listaUtilizadores.get(userID).listaIDsRecompensas.get(j));
				}
			}
		}
		
		try {
			guardaFicheiro();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	synchronized public void retiraDoacao(int userID, int projID) {
		
		String str="";
		Utilizador user = bd.listaUtilizadores.get(userID); 
		int idProjeto;
		float doacao;
		Set<Entry<Integer, Float>> set = user.listaDoacoesUser.entrySet();
		Iterator<Entry<Integer, Float>> i= set.iterator();
		
		while (i.hasNext()) {
			Map.Entry <Integer, Float> mentry = (Entry<Integer, Float>) i.next();
			idProjeto = (int) mentry.getKey();
			doacao = (float) mentry.getValue();
			if (idProjeto == projID) {
				user.listaDoacoesUser.remove(idProjeto, doacao);
			}
		}
		
		try {
			guardaFicheiro();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean validaDoacao(int userID, float dinheiro) {
		if (dinheiro > bd.listaUtilizadores.get(userID).getSaldo())
			return false;
		return true;
	}
	
	synchronized public void doarDinheiro(int userID, int projID, float dinheiro) {
		Utilizador user = bd.listaUtilizadores.get(userID);
	
		Projeto proj = procuraProjetoID(projID);
		
		float saldo = user.getSaldo();
		proj.listaDoacoes.put(userID, dinheiro);  		// acrescenta user ao Projeto
		
		if (user.contemDoacao(projID)) {			
			dinheiro += user.listaDoacoesUser.get(projID);
		}
		
		
		user.listaDoacoesUser.put(proj.id, dinheiro);	// acrescenta projeto e doa��o ao user
		user.setSaldo(user.getSaldo()-dinheiro);					// retira dinheiro ao user
		
	}
	

	/* OPERA��ES DE RECOMPENSAS */
	synchronized public boolean escolherRecompensa(int userID, int projID, float dinheiro, int indexRecompensa) {
		int i=0;
		Projeto proj = procuraProjetoID(projID);
		Recompensa rec = proj.listaRecompensas.get(indexRecompensa);
		if (dinheiro >= rec.valor) {
			bd.listaUtilizadores.get(userID).listaIDsRecompensas.add(rec.id);
			
			try {
				guardaFicheiro();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return true;
		}
		return false;
	}
	
	
	synchronized public void addRecompensa(int userID, int projID, String nome, float valor) {
		int id = contadorIDRecompensa.getAndIncrement();
		Projeto proj = procuraProjetoID(projID); 
		Recompensa rec = new Recompensa(proj, nome, valor, id, 0);
		proj.listaRecompensas.add(rec);
		
		try {
			guardaFicheiro();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	synchronized public void removeRecompensa(int userID, int projID, String nome) {
		// e se os users j� tiverem escolhido uma recompensa e ela for cancelada? dinheiro, votos?
		
		Projeto proj = procuraProjetoID(projID); 
		Recompensa rec = encontraRecompensa(projID, nome);
		proj.listaRecompensas.remove(rec);
		
	}
	
	
	/* OPERA��ES DE INBOX */
	synchronized public void adicionaMensagem(int userID, int projID, String mensagem) {
		Projeto proj = procuraProjetoID(projID);
		Mensagem mens = new Mensagem(userID, projID, mensagem);
		ArrayList <Mensagem> listaM = new ArrayList <Mensagem>();
		if (proj.inbox.get(userID)!=null) {
			listaM = proj.inbox.get(userID);
		}
		listaM.add(mens);
		proj.inbox.put(userID, listaM);
		
		try {
			guardaFicheiro();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public int devolveIDUser(String username) {
		int i;
		for (i=0;i<bd.listaUtilizadores.size();i++) {
			if (bd.listaUtilizadores.get(i).nome.compareToIgnoreCase(username)==0) {
				return bd.listaUtilizadores.get(i).id;
			}
		}
		return -1;
	}

	
	public String consultaMensagens(int utilizadorID, int projID) {
		Projeto proj = procuraProjetoID(projID);
		String str="";
		int userID;
		int j=0;
		ArrayList <Mensagem> listaMens;
		Set<Entry<Integer, ArrayList<Mensagem>>> set = proj.inbox.entrySet();
		Iterator<Entry<Integer, ArrayList<Mensagem>>> i= set.iterator();
		str=str.concat("Mensagens:<br>");
		// enquanto houver users, imprimo as mensagens
		while (i.hasNext()) {
			Map.Entry <Integer, ArrayList<Mensagem>> mentry = (Entry<Integer, ArrayList<Mensagem>>) i.next();
			userID = (int) mentry.getKey();
			listaMens = (ArrayList<Mensagem>) mentry.getValue();
				str = str.concat("["+bd.listaUtilizadores.get(userID).nome+"]:(id="+bd.listaUtilizadores.get(userID).id+")<br>");
				for (j=0;j<listaMens.size();j++) { // se foi o admin a escrever a resposta ent�o marco
					if (listaMens.get(j).idUser==proj.admin.id) {
						
						str=str.concat("A: ");
					}
					str = str.concat(listaMens.get(j).comentario+"<br>");
				}
			
			
		}
		return str;
	}

	// admin responde a mensagens
	synchronized public void respondeMensagens(int adminID, int userID, int projID, String mensagem) {
		Projeto proj = procuraProjetoID(projID);
		Mensagem mens = new Mensagem(adminID, projID, mensagem);
		ArrayList <Mensagem> listaM = proj.inbox.get(userID);
		listaM.add(mens);
		proj.inbox.put(userID, listaM);
		
		try {
			guardaFicheiro();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* AUXILIARES */
	public int verificaAdministrador(int userID, int projID) {
		int i=0;
		for (i=0;i<bd.listaUtilizadores.get(userID).listaIDsProjeto.size();i++) {
			if (projID == bd.listaUtilizadores.get(userID).listaIDsProjeto.get(i))
				return 1;
		}
		return 0;
	}
	
	public Recompensa encontraRecompensa(int projID, String nome) {
		int i=0;
		Projeto proj = procuraProjetoID(projID);
		for (i=0;i<proj.listaRecompensas.size();i++) {
			if (proj.listaRecompensas.get(i).nome.compareToIgnoreCase(nome)==0) {
				return proj.listaRecompensas.get(i);
			}
		}
		return null;
	}
	
	// fimProjeto
	public void updateValidadeProjetos() {
		// dinheiro � enviado para o admin e as recompensas dos users ficam v�lidas 
		for(int i=0; i<bd.listaProjetos.size();i++){ 
			if(bd.listaProjetos.get(i).idade){ // se for atual
				if(!bd.listaProjetos.get(i).verificaValidade()){  // se projeto tiver acabado data
					bd.listaProjetos.get(i).idade=false;  // projeto passa para antigo
					if (bd.listaProjetos.get(i).getValorRecolhido() >= bd.listaProjetos.get(i).valor_objetivo) { // conclui com sucesso
						bd.listaProjetos.get(i).admin.saldo+=bd.listaProjetos.get(i).getValorRecolhido();
						validaRecompensas(bd.listaProjetos.get(i));
					}
					else { // devolve dinheiro aos users
						devolveDinheiro(bd.listaProjetos.get(i));
					}
				}
			}
		}
		try {
			guardaFicheiro();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void validaRecompensas(Projeto proj) {
		int i;
		for (i=0;i<proj.listaRecompensas.size();i++) {
			proj.listaRecompensas.get(i).valida=1;
		}
	}
	
	/* fun��o que diz onde come�a o id de Recompensas */
	public void iniciaContadorRecompensas() {
		int i,j, max=0;
		for (i=0;i<bd.listaProjetos.size();i++) {
			for (j=0;j<bd.listaProjetos.get(i).listaRecompensas.size();j++) {
				if (bd.listaProjetos.get(i).listaRecompensas.get(j).id>max)
					max = bd.listaProjetos.get(i).listaRecompensas.get(j).id;
			}
		}
		contadorIDRecompensa.set(max+1);
	}
	
	public String procuraIDProjectoTumblr(int projID) {
		int i;
		for (i=0;i<bd.listaProjetos.size();i++) {
			if (bd.listaProjetos.get(i).id == projID)
				return bd.listaProjetos.get(i).getTumblrID();
		}
		return null;
	}
}

class ThreadValidade extends Thread{
	InterfaceRMI intRMI;
	int dia;
	
	public ThreadValidade(InterfaceRMI intRMI) {
		this.intRMI=intRMI;
		this.dia=1000*60*60*24;
	}
	
	public void run() {
		while(true) {
			try {
				// dorme um dia e faz update da validade dos projetos
				sleep(dia);
				intRMI.updateValidadeProjetos();
				Thread.sleep(10000);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}
}