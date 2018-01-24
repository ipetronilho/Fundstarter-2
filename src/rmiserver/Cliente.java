/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver;
import java.net.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Properties;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {
	
	static int TIMEOUT = 600000;
    static boolean DEBUG = true;
    static int tentativas = 3, falhou = 10;
    
    // tem de ter 2 ips e dois portos distintos
    static String serverAddress = "localhost";
    static String nomeUser="";
    static int[] serversockets = {6000, 8000};	// default
    //static String serverAddress = "169.254.143.125";
    //static String serverAddress = "169.254.36.100";
    // static String serverAddresses={"localhost", "169.254.132.125"};
    static String filename_login="";
    static String filename_backup="";
    static String filename_operacao="";
    
    static int id_sessao=-1;
    static String s_id_sessao="";
    
    public static void main(String args[]) throws InterruptedException {
        // args[0] <- hostname of destination
        if (args.length == 0) {
	         System.out.println("java TCPClient hostname");
	         System.exit(0);
         }
        
        int i, j, fail_counter = 0;
        String msg = "";
        
        // carrega os portos que correspondem a cada servidor
        carregaPortosFicheiro(); 
        
        getIDSessao();
        s_id_sessao = Integer.toString(id_sessao);
        
     // alterna constantemente entre servidores para se ligar
        while (true) {
        	
            for (i = 0; i < 2; i++) {

                if (DEBUG == true) {
                    System.out.println("Nova ligacao ao servidor: " + (i + 1));
                }
                /*
                 * tenta ligar-se 1 vez ao servidor. 
                 * - se n�o consegue conectar-se recebe "TROCA" e muda de servidor
                 * - se consegue, recebe outra mensagem e volta a tentar
                 */
                msg = conexaoServidor(serverAddress, serversockets[i], i);
                
                // tenta de novo ao mesmo servidor
                if (msg.compareToIgnoreCase("TROCA") != 0 && msg.length() > 0) {
                    for (j = 0; j < tentativas; j++) { // tenta 3 vezes
                        if (msg.compareToIgnoreCase("TROCA") != 0 && msg.length() > 0) {
                            Thread.sleep(1000);
                            msg = conexaoServidor(serverAddress, serversockets[i], i);
                            
                        } 
                        /* tenta de novo a um servidor diferente */
                        else {
                            j = tentativas;
                            System.out.println("Vou trocar!");
                        }
                    }
                }

            }
            
         // se tentou ligar-se 3x10 vezes a cada servidor e mesmo assim falhou as 10, desiste
            if (fail_counter == falhou) { 
                System.out.println("Servidores em baixo. Tente mais tarde.");
                System.exit(-1); // erro
            }
            fail_counter++;
        }//while
        

    }
    
   
    
    // retorna "" se conseguir ligar-se e "TROCA" se for preciso tentar outra vez
    public static String conexaoServidor(String serverAddress, int serversocket, int i) { 
        String str = "";
        Socket s = null;
        String data;
        
        try {
            System.out.println("\nHost � '" + serverAddress + "' e socket" + serversocket);
            try {
                s = new Socket(serverAddress, serversocket);	// conectou-se ao servidor
                
            } catch (SocketException e) { 						// n�o consegue ligar-se ao servidor
                return "TROCA";
            }
            
            // ligou-se com sucesso
            guardaPortosFicheiro(i);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
           
            out.writeUTF("PEDIDO");
          
            s.setSoTimeout(TIMEOUT);	// tempo limitado
            data = in.readUTF();

            if (data.compareToIgnoreCase("SIM") == 0) { //pedido aceite
            	
                str = "";
                
                // l� o id da sess�o
                
                data = in.readUTF();
                if (data.compareToIgnoreCase("SESSAO")==0) {
                	out.writeUTF(s_id_sessao);
                }
                
                
	            Receiver MyThread = new Receiver(in, s_id_sessao, out); // l� de teclado
                MyThread.start();
                
                
                String texto = "";
                InputStreamReader input = new InputStreamReader(System.in);
                BufferedReader reader = new BufferedReader(input);
                
                
                while (true) {
                    try {
                        texto = reader.readLine(); // l� de teclado
                    } catch (Exception e) {
                    }
                    try {
                        out.writeUTF(texto); // escreve
                    } catch (SocketException e) {
                    
                    	
                        System.out.println("A conectar-se a outro servidor...");
                        
                        MyThread.salvaFicheiroBackup();
                        MyThread.guardaFicheiro(texto);
                        
                        return "TROCA";
                    }
                }
            }
            else if (data.compareToIgnoreCase("NAO") == 0) { // n�o consegue ligar-se ao servidor
                str = "TROCA";
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } 
        // fechar o socket
        finally {	
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    System.out.println("close:" + e.getMessage());
                }
            }
        }
        return str;

    }
    
    // ---- OPERA��ES DE FICHEIROS
    
    /* se o user perdeu sess�o, tem de recuperar o login e a opera��o
     * que estava a fazer quando foi interrompido
     */

    
    // guarda os portos. recebe o index do porto prim�rio
    public static void guardaPortosFicheiro(int i) {
    	PrintWriter writer;
		try {
			writer = new PrintWriter("ficheiros/portos.txt", "UTF-8");
			
			if (i==1) {
				System.out.println("Guardo "+serversockets[i]+" como prim�rio");
				writer.println(serversockets[i]);	//8000
				writer.println(serversockets[i-1]);	//6000
			}
			else {
				System.out.println("Guardo "+serversockets[i]+" como prim�rio");
				writer.println(serversockets[i]);	//6000
				writer.println(serversockets[i+1]);	//8000
			}
		    writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
    }
    
    // carrega os portos
    public static void carregaPortosFicheiro() {
    	String fileName = "ficheiros/portos.txt";

        try {
        	/* l� opera��es */
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            String line = bufferedReader.readLine();
            serversockets[0]= Integer.parseInt(line);
            
            line = bufferedReader.readLine();
            serversockets[1]= Integer.parseInt(line);
            
            bufferedReader.close();
                
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");                  

        }
    }


    /* vai buscar o ID da sua sess�o ao ficheiro id_sessao.txt */
    public static void getIDSessao() {
    	String filename="ficheiros/id_sessao.txt";
    	String line="";
    	
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            line = bufferedReader.readLine();
            id_sessao = Integer.parseInt(line);
            bufferedReader.close();
            
            int id=id_sessao+1;
            line=Integer.toString(id);
            
            
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
			writer.println(line);
		    writer.close();
                
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filename_login + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + filename_login + "'");                  

        }
  
    }
	


}

class Receiver extends Thread {
	ArrayList <Integer> listaOperacoes = new ArrayList <Integer>();
    DataInputStream in;
    DataOutputStream out;
    // dados gravados no cliente em caso de falha de rede
    int userID=-1;
    int imprime=1;
    String s_id_sessao;
	String filename_login="";
    String filename_backup="";
    String filename_operacao="";
	
    
    

    public Receiver(DataInputStream ain, String s_id_sessao, DataOutputStream aout) {
        this.in = ain;
        this.s_id_sessao=s_id_sessao;
        this.out=aout;
        filename_login="ficheiros/"+s_id_sessao+"_infologin.txt";
        filename_backup="ficheiros/"+s_id_sessao+"_backup.txt";
        filename_operacao="ficheiros/"+s_id_sessao+"_operacao.txt";
    }
    
    
    public void setImprime(int imprime) {
    	this.imprime=imprime;
    }

    //=============================
    public void run() {
    	
    	String msg=recuperaSessao(this.out);
    	
        while (true) {
            // READ FROM SOCKET
            try {
                String data = in.readUTF(); // l� o que foi escrito
                //System.out.println("Recebi " + data);
                if (data.compareToIgnoreCase("IMPRIME")==0) {
                	setImprime(1);
                }
                
                else if (data.compareToIgnoreCase("RESET")==0) {
                	salvaFicheiroBackup();
                	recuperaSessao(this.out);
                }
               
                else if (imprime==1)
                	System.out.println("> " + data); // print o que o serv. escreveu
                
            } catch (SocketException e) {
            	
                System.out.print("O Socket Servidor fechou"); //Caso o socket de conec��o ao cliente se fechar este imprime o erro
                
                break;
            } catch (Exception e) {
                System.out.println("Sock:" + e.getMessage());
                break;	
            }
        }
        //System.out.println("Fim.");
        
    }
    
    public String recuperaSessao(DataOutputStream out) {
    	/*
         * l� o ficheiro login.txt
         */
        File f = new File(filename_login);
		if(f.exists() && !f.isDirectory()) {
            if (!ficheiroVazio(filename_login)) {
            	setImprime(0);
            	leFicheiro(filename_login, out);
            }
		}
        
		/*
		 * l� a opera��o de backup.txt
		 */
		File f2 = new File(filename_backup);
		if(f2.exists() && !f2.isDirectory()) {
            if (!ficheiroVazio(filename_backup)) {
            	setImprime(0);
            	leFicheiro(filename_backup, out);
            }
		}
		return "sucesso";
    }
    
    // verifica se o ficheiro est� vazio
	public static boolean ficheiroVazio(String filename){
		BufferedReader br; 
		try {
			br= new BufferedReader(new FileReader(filename));  
			if (br.readLine() == null) {
				br.close();
			    return true;
			}
			br.close();
			
		} catch (FileNotFoundException e1) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
    }
    
    
    /* l� de ficheiro e envia ao servidor */
    public static void leFicheiro(String filename, DataOutputStream out) {
    	
        String line = null;


        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
           
            while((line = bufferedReader.readLine()) != null) { // l� user e passw
                out.writeUTF(line);
            }
            bufferedReader.close();
                
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filename + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + filename + "'");                  

        }
    	
    }
    
    public void salvaFicheiroBackup() {

    	filename_operacao="ficheiros/"+s_id_sessao+"_operacao.txt";
    	File f = new File(filename_login);
		if(!f.exists() && !f.isDirectory()) {
			criaFicheiros(filename_backup);
		}
    	
    	
    	apagaConteudoFicheiro(filename_backup); // apaga o conteudo do backup, se ja existir
    	
    	BufferedReader br;
		try {
			
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename_backup, true)));
		    
			br = new BufferedReader(new FileReader(filename_operacao));
			String linha=br.readLine();
	    	while(linha!=null) { // escreve no ficheiro de Backup o que l� de Opera��o
	    		writer.println(linha);
	    		linha=br.readLine();
	    	}
	    	br.close();
	    	writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
    	
		apagaConteudoFicheiro(filename_operacao); // apaga as Opera��es
    }
    
    public static void criaFicheiros(String filepath) {
    	File file = new File(filepath);
	      
	      try {
			if (file.createNewFile()){
			    System.out.println("File is created!");
			  }else{
			    System.out.println("File already exists.");
			  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void apagaConteudoFicheiro(String filepath) {

		try {
			PrintWriter writer = new PrintWriter(filepath, "UTF-8");
			
		    writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    // guarda uma frase em ficheiro
    public void guardaFicheiro(String st) {
    	//System.out.println("Acrescento "+st);
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename_backup, true)));
		    writer.println(st);
		    writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }

}
