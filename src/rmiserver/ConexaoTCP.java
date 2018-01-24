package rmiserver;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


//= Thread para tratar de cada canal de comunicação com um cliente
class ConexaoTCP extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int thread_number;
    ArrayList <DataOutputStream> lista = new ArrayList <DataOutputStream>();
    String confirma;
    int counter=0;
    int number_lines=-1;
    int id_sessao;
    
    
    
    public ConexaoTCP (Socket aClientSocket, int numero, ArrayList <DataOutputStream> lista) {
  
        try{
        	this.lista = lista;
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.id_sessao=numero;
            confirma = "SIM";
            lista.add(out);
            this.start();
        }catch(IOException e){System.out.println("Connection:" + e.getMessage());}
    }
    
    
    //=============================
    public void run() {
    	String ipAddress="localhost";
        String resposta;
        InterfaceRMI intRMI;
        int checkLogin=0;
        String nomeUser="";
        int guardaDados=0;
        int tentativas=0, total_tentativas=100;
        
        String dados="";
        String s_id_sessao="";
       
        
			try {
				dados=in.readUTF();
	            if (dados.compareToIgnoreCase("PEDIDO") == 0) {
					out.writeUTF(confirma); //SIM ou NAO, caso aceite ou nao pedidos
	                
			        //Verifica o login e efectua o registo.
			        if (confirma.compareToIgnoreCase("SIM") == 0) {


			        
				int userID=-1;
			            
	            while(tentativas<=total_tentativas){
	            	if (tentativas == total_tentativas) {
	            		out.writeUTF("A sair...");
	            		terminaThread();
	            	}
	            		
	    	        try{
	    	        	System.out.println("Tentativa n"+tentativas);
	    	        	
	    	        	// out.writeUTF("RESET");
			            
							//intRMI = (InterfaceRMI) Naming.lookup("rmi://localhost:7000/benfica");
	    	        		intRMI = (InterfaceRMI) Naming.lookup("rmi://"+ipAddress+":7000/benfica");
						
							// thread acorda todos os dias e verifica a validade
							ThreadValidade MyThread = new ThreadValidade(intRMI); 
							
							
							MyThread.start();
							
					            while(true){
					                //an echo server
						            if (checkLogin == 0) {
						            	/* ---- MENU ---- */
						                out.writeUTF("Bem vindo! Seleccione uma opcao! 1-Login; 2-Registar; 3-Consultar dados");
						                
						                String data = in.readUTF();
						                int opcao=Integer.parseInt(data);
						                
						                
						                /* LOGIN */
						                if (opcao==1) {
						                	out.writeUTF("Username:");
						                	nomeUser = in.readUTF();
						              
						                	
						                	out.writeUTF("Insira password:");
						                	String password = in.readUTF();
						                	
						                	// alterar
						                	try {
						                		userID = intRMI.verificaLogin(nomeUser, password);
						                	}catch (ConnectException e) {
						                		tentativas++;
						                	
						                		
						                	}
						                	if (userID != -1) {
						                		checkLogin=1;
						                		out.writeUTF("Login efectuado com sucesso\n");
						                		
						                	}
						                	else
						                		out.writeUTF("Login invalido.");
						                	
						                }
						                
						                /* REGISTAR */
						                else if (opcao==2) {
						                	
						                	out.writeUTF("--REGISTO--");
						                	
						                	out.writeUTF("Insira nome de Utilizador");
						                	nomeUser = in.readUTF();
						                	out.writeUTF("Insira password:");
						                	String password = in.readUTF();
						                	
						                	userID = intRMI.registaConta(nomeUser, password);
						                	checkLogin=1;
						                }
						                
						                /* CONSULTAR DADOS */
						                else if (opcao==3) {
						                	out.writeUTF("Consultar dados...\n1-Listar Projetos Actuais\n2-Listar Projetos Antigos\n3-Consultar Detalhes de um projeto\n0-Sair");
						                	data = in.readUTF();
						                	opcao=Integer.parseInt(data);
						                	
						                	if (opcao==1) {
						                		out.writeUTF("--PROJETOS ACTUAIS--");
						                		resposta = intRMI.listaProjetosActuais();
						                		out.writeUTF(resposta);
						                	}
						                	else if (opcao==2) {
						                		out.writeUTF("--PROJETOS ANTIGOS--");
						                		resposta =intRMI.listaProjetosAntigos();
						                		out.writeUTF(resposta);
						                	}
						                	
						                	else if (opcao==3) {
						                		out.writeUTF("Nome do projeto:");
						                		String nomeProjeto = in.readUTF();
						                		int projID = intRMI.procuraProjeto(nomeProjeto);
						                		resposta = intRMI.imprimeDetalhesProjeto(projID);
						                		out.writeUTF(resposta);
						                	}
						                }
						            }
						                
						            else if (checkLogin==1) {
						            	out.writeUTF("O que deseja fazer?");
						            	out.writeUTF("---CONSULTA---");
						            	out.writeUTF("1-Consultar saldo");
						            	out.writeUTF("2-Consultar recompensas");
						            	out.writeUTF("3-Consultar os seus Projetos"); // admin
						            	out.writeUTF("4-Consultar todos os projetos do sistema");
						            	out.writeUTF("5-Consultar doacoes de Projetos");
						            	
						            	out.writeUTF("---PROJETOS---");
						            	out.writeUTF("6-Criar um projeto");
						            	out.writeUTF("7-Cancelar projeto");
						            	out.writeUTF("8-Doar dinheiro a um projeto");
						            	out.writeUTF("9-Adicionar recompensas a um projeto");
						            	out.writeUTF("10-Remover recompensas a um projeto");
						            	out.writeUTF("11-Consultar inbox de um projeto");
						            	out.writeUTF("0-logout");
						            	
						            	
						            
						            	String data = in.readUTF();
						            	
						            	int opcao=Integer.parseInt(data);
						            	
						            	if (opcao == 0) {
						            		out.writeUTF("A sair...");
						            		terminaThread();
						            		checkLogin=0;
						            	}
						            	
						            	else if (opcao == 1) {
						            		resposta = intRMI.consultarSaldo(userID);
						            		out.writeUTF(resposta);
						            	}
						            	
						            	else if (opcao == 2) {
						            		resposta = intRMI.consultarRecompensas(userID);
						            		out.writeUTF(resposta);
						            	}
						            	
						            	else if (opcao == 3) {
						            		resposta = intRMI.consultarProjetos(userID); // imprime
						            		out.writeUTF(resposta);
						            	}
						            	
						            	else if (opcao==4) {
						                	out.writeUTF("Consultar dados...\n1-Listar Projetos Actuais\n2-Listar Projetos Antigos\n3-Consultar Detalhes de um projeto\n0-Sair");
						                	data = in.readUTF();
						                	opcao=Integer.parseInt(data);
						                	
						                	if (opcao==1) {
						                		out.writeUTF("--PROJETOS ACTUAIS--");
						                		resposta = intRMI.listaProjetosActuais();
						                		out.writeUTF(resposta);
						                	}
						                	else if (opcao==2) {
						                		out.writeUTF("--PROJETOS ANTIGOS--");
						                		resposta =intRMI.listaProjetosAntigos();
						                		out.writeUTF(resposta);
						                	}
						                	
						                	else if (opcao==3) {
						                		out.writeUTF("Nome do projeto:");
						                		String nomeProjeto = in.readUTF();
						                		int projID = intRMI.procuraProjeto(nomeProjeto);
						                		resposta = intRMI.imprimeDetalhesProjeto(projID);
						                		out.writeUTF(resposta);
						                	}
						                }
						            	
						            		
						            	else if (opcao == 5) {
						            		resposta = intRMI.imprimeDoacoesUser(userID);
						            		out.writeUTF(resposta);
						            	}
						            	
						            	
						            	else if (opcao == 6) { 
						            		
						            		int id=-1;
						            		String nome;
						            		do {
						            			out.writeUTF("Nome do projeto:");
						            			nome = in.readUTF();
							            		id = intRMI.procuraProjeto(nome);
							            		if (id!=-1)
							            			out.writeUTF("Ja existe um projeto com esse nome!");
						            		}while(id!=-1);
						            		
						            		try {
						            			out.writeUTF("Descricao:");
						            			String descricao = in.readUTF();
							            		// TODO: proteção - strings
							            		out.writeUTF("Valor objetivo:");
							            		String valor=in.readUTF();
							            		float valor_objetivo = Float.parseFloat(valor);
	
							            		Calendar dataInicial = new GregorianCalendar();
							            		
							            		out.writeUTF("Data final do projeto:");
							            		out.writeUTF("Dia: ");
							            		String diaLido=in.readUTF();
							            		int dia = Integer.parseInt(diaLido);
							            		
							            		out.writeUTF("Mês: ");
							            		String mesLido=in.readUTF();
							            		int mes = Integer.parseInt(mesLido);
							            		
							            		out.writeUTF("Ano: ");
							            		String anoLido=in.readUTF();
							            		int ano = Integer.parseInt(anoLido);
							            		
							            		Calendar dataFinal = new GregorianCalendar();
							            		dataFinal.set(Calendar.YEAR, ano);
							            		dataFinal.set(Calendar.MONTH, mes);
							            		dataFinal.set(Calendar.DAY_OF_MONTH, dia);
							            		
							            		intRMI.criaProjeto(userID, nome, valor_objetivo, dataFinal, descricao);
							            		
							            		
						            		}catch(NumberFormatException e) {
						            			out.writeUTF("Numero invalido.");
						            		}
						            		
						            		
						            	}
						            	
						            	else if (opcao == 7) { 
						            		int projID=-1;
						            		String nome;
						            		do {
						            			out.writeUTF("Nome do projeto:");
						            			nome = in.readUTF();
							            		projID = intRMI.procuraProjeto(nome);
							            		
							            		if (projID==-1)
							            			out.writeUTF("Nao existe um projeto com esse nome!");
						            		}while(projID==-1);
						            		
						            		intRMI.eliminaProjeto(userID, projID);
						            		
						            		
						            	}
						            	
						            	else if (opcao == 8) {
						            		float dinheiro;
						            		int projID=-1;
						            		
						            		out.writeUTF("1-Listar os projetos existentes;0-Não listar");
					            			data = in.readUTF();
					            			opcao=Integer.parseInt(data);
					            			if (opcao==1) {
					            				resposta = intRMI.listaProjetosActuais();
						                		out.writeUTF(resposta);
					            			}
						            		
						            		do {
						            			
						            			
							            		out.writeUTF("Insira o nome do Projeto a doar:");
							            		String nome = in.readUTF();
							            		projID = intRMI.procuraProjeto(nome);
							            		if (projID== -1)
							            			out.writeUTF("Nao existe um projeto com esse nome.");
						            		}while(projID==-1);
							            	
						            		do {
						            			out.writeUTF("Quantia a doar?\n");
						            			String quantia=in.readUTF();
						            			dinheiro = Float.parseFloat(quantia);
						            		}while(dinheiro<=0);
						            		
						            		if (intRMI.validaDoacao(userID, dinheiro)) {
						            		
							            		intRMI.doarDinheiro(userID, projID, dinheiro);
							            		
							            		String listaRecompensas=""; 
							            		listaRecompensas = intRMI.listaRecompensas(projID); 
							            		
							            		if (listaRecompensas.compareToIgnoreCase("")==0)
							            			out.writeUTF("Projeto nao tem recompensas.");
							            		else {
							            			out.writeUTF("Escolher uma recompensa.\n");
							            			out.writeUTF(listaRecompensas);
							            			resposta=in.readUTF();
							            			int indexRecompensa = Integer.parseInt(resposta);
							            			
							            		
					
								            		boolean sucesso = intRMI.escolherRecompensa(userID, projID, dinheiro, indexRecompensa);
								            		if (sucesso) {
								            			out.writeUTF("Recompensa adicionada.");
								            		}
								            		else
								            			out.writeUTF("Recompensa falhou.\n");
							            		}
						            		}
						            		else
						            			out.writeUTF("Saldo insuficiente.");
						            	}
						            	
						            	else if (opcao == 9) {
						            		int checkAdmin=0;
						            		int aux=0;
						            		int projID;
						            		
						            		do {
							            		out.writeUTF("Insira o nome do Projeto:");
							            		String nomeProjeto = in.readUTF();
							            		projID = intRMI.procuraProjeto(nomeProjeto);
							            		if (projID == -1)
							            			out.writeUTF("Nao existe um projeto com esse nome.");
							            		checkAdmin = intRMI.verificaAdministrador(userID, projID);
							            		if (checkAdmin == 0) {
							            			out.writeUTF("Nao é administrador desse projeto!");
							            			out.writeUTF("0-Sair\n1-Tentar de novo");
							            			resposta=in.readUTF();
							            			aux=Integer.parseInt(resposta);
							            		}
							            		else if (checkAdmin==1) {
								            		out.writeUTF("Nome da recompensa:");
								            		String nome=in.readUTF();
								            		out.writeUTF("Valor da recompensa:");
								            		resposta=in.readUTF();
								            		float valor = Float.parseFloat(resposta);
								            		intRMI.addRecompensa(userID, projID, nome, valor);
							            		}
						            		}while(projID==-1 || checkAdmin==0 || aux==1);
						            		
						            		
						            	}
						            	
						            	else if (opcao == 10) {
						            		int checkAdmin=0;
						            		int aux=0;
						            		int projID;
						            		do {
							            		out.writeUTF("Insira o nome do Projeto:");
							            		String nomeProjeto = in.readUTF();
							            		projID = intRMI.procuraProjeto(nomeProjeto);
							            		if (projID == -1)
							            			out.writeUTF("Nao existe um projeto com esse nome.");
							            		checkAdmin = intRMI.verificaAdministrador(userID, projID);
							            		if (checkAdmin == 0) {
							            			out.writeUTF("Nao é administrador desse projeto!");
							            			out.writeUTF("0-Sair\n1-Tentar de novo");
							            			resposta=in.readUTF();
							            			aux=Integer.parseInt(resposta);
							            		}
							            		else if (checkAdmin==1) {
								            		out.writeUTF("Nome da recompensa:");
								            		String nome=in.readUTF();
								            		intRMI.removeRecompensa(userID, projID, nome);
							            		}
						            		}while(projID==-1 || checkAdmin==0 || aux==1);
						            		
						            		
						            	}
						            	
						            	else if (opcao==11) {
						            		out.writeUTF("Nome do projeto:");
						            		String projNome = in.readUTF();
						            		int projID = intRMI.procuraProjeto(projNome);
						            		int checkAdmin = intRMI.verificaAdministrador(userID, projID);
						            		do {
							            		out.writeUTF("1-Deixar mensagem");
							            		out.writeUTF("2-Consultar mensagens");
							            		out.writeUTF("3-Responder a mensagem [Apenas admins]");
							            		out.writeUTF("0-Sair");
							            		data=in.readUTF();
							            		opcao=Integer.parseInt(data);
							            		
							            		if (opcao==1) {
							            			out.writeUTF("Mensagem:");
							            			String mensagem = in.readUTF();
							            			
							            			intRMI.adicionaMensagem(userID, projID, mensagem);
							            		}
							            		// TODO: mostrar apenas aquelas que são mandadas pelo user?
							            		else if (opcao==2) {
						            				resposta = intRMI.consultaMensagens(userID, projID);
						            				out.writeUTF(resposta);
							            		}
							            		else if (opcao==3) {
							            			if (checkAdmin==1) {
							            				out.writeUTF("Responder a que utilizador? (inserir ID)");
							            				resposta=in.readUTF();
							            				int id=Integer.parseInt(resposta);
							            				out.writeUTF("Resposta: ");
							            				String mensagem=in.readUTF();
							            				
							            				intRMI.respondeMensagens(userID, id, projID, mensagem);
							            				
							            			}
							            		}
						            		}while(opcao!=0);
						            	}
						            }
					                
					            }
			            
			        
		        }
	    	        catch(ConnectIOException e) {
	    		        System.out.println("A tentar outra vez...");
	    		        try {
							sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
	    	        	tentativas++;
	    	        }
	        catch(ConnectException e) {
		        System.out.println("A tentar outra vez...");
		        try {
					sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
	        	tentativas++;
	        }
	        catch(EOFException e){
	        	System.out.println("EOF:" + e);
		    }
	        catch(IOException e){
		    	System.out.println("IO:" + e);
        		terminaThread();
			} 
	        catch (NotBoundException e1) {
				System.out.println("Not bound...");
			} 
	        catch (NumberFormatException e2) {
				System.out.println("Formato invalido! " + e2);
			} 
        }
    	
    }
	            }
    } catch (IOException e3) {
		e3.printStackTrace();
	}
    }


    

	public void terminaThread() {
		try {
			this.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
    
 
    
}



