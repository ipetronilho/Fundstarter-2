package ws;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.server.ServerEndpoint;

import hey.model.HeyBean;
import rmiserver.InterfaceRMI;

import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

@ServerEndpoint(value = "/ws")
public class WebSocketAnnotation {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private final String username;
    private Session session;
    private static final ArrayList<WebSocketAnnotation> users = new ArrayList<WebSocketAnnotation>();
    InterfaceRMI intRMI;
    
    public WebSocketAnnotation() {
        username = "User" + sequence.getAndIncrement();
        try {
			intRMI = (InterfaceRMI) Naming.lookup("rmi://localhost:7000/benfica");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
    }

    @OnOpen
    public void start(Session session) {
        this.session = session;
        users.add(this);
        String message = "*" + username + "* connected.";
        //sendMessage(message);
    }

    @OnClose
    public void end() {
    	users.remove(this);
    	// clean up once the WebSocket connection is closed
    }

    @OnMessage
    public void receiveMessage(String message) {
		// one should never trust the client, and sensitive HTML
        // characters should be replaced with &lt; &gt; &quot; &amp;
    	String receivedMessage = new StringBuffer(message).toString();
    	sendMessage("[" + username + "] " + receivedMessage);
    }
    
    
    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }
  
    public void sendMessage(String text) {
    	// uses *this* object's session to call sendText()
    	
    	for (int i=0; i<users.size();i++) {
    		try {
				this.users.get(i).session.getBasicRemote().sendText(text);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    /*	int i=0;
    	try {
    			this.session.getBasicRemote().sendText(text);
    		
		} catch (IOException e) {
			// clean up once the WebSocket connection is closed
			try {
				this.session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}*/
    }
    

    public void sendMessageToUser(String msg) {
    	//System.out.println("Message "+msg+"- to user "+((HeyBean)this.getSessionHTTP().getAttribute("heyBean")).getUsername());
        try {
        	
			this.session.getBasicRemote().sendText(msg.toString());		// enviar mensagem para o cliente websocket
		} catch (IOException e) {
			// clean up once the WebSocket connection is closed
			try {
				this.session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    }


}
