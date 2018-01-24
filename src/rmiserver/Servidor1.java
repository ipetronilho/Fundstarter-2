// TCPServer2.java: Multithreaded server
package rmiserver;
import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Servidor1{
   static int serv_id = 1;
   static boolean DEBUG = true, Primario;
   static int[] serverSockets = {6000, 8000};

   
	public static void main(String args[]){
        int numero=0;
        ArrayList <DataOutputStream> lista = new ArrayList <DataOutputStream>();
        
        String host="localhost";
        //String host = "169.254.36.100"; // host
     
        ServerSocket listenSocket=null;
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			
			e1.printStackTrace();
		}
        Primario=true;
        // parte TCP
        try{
        	listenSocket = new ServerSocket(6000); // mudar
            //System.out.println("Ligo-me ao " + serverSockets[serv_id - 1]);
            
            while(true) {
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                System.out.println("CLIENT_SOCKET (created at accept())="+clientSocket);
                numero ++;
                new ConexaoTCP(clientSocket, numero, lista);
            }
        }  catch(IOException e){
        	System.out.println("Fui a vida");
        	System.out.println("Listen:" + e.getMessage());
		}
}
		

}
