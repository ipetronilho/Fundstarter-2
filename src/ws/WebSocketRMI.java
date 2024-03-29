package ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

@ServerEndpoint(value = "/ws")
public class WebSocketRMI {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private final String username;
    private Session session;
    private static final ArrayList<WebSocketRMI> users = new ArrayList<WebSocketRMI>();

    public WebSocketRMI() {
        username = "User" + sequence.getAndIncrement();
    }

    @OnOpen
    public void start(Session session) {
        this.session = session;
        users.add(this);
        String message = "*" + username + "* connected.";
        sendMessage(message);
        
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

    private void sendMessage(String text) {
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
}
