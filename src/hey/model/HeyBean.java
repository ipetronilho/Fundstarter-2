package hey.model;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuthService;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import rmiserver.InterfaceRMI;

// tem de ter todos os métodos
//index.jsp -> LoginAction -> HeyBean -> RMI
public class HeyBean {
	
	// Access codes #1: per application used to get access codes #2	
	private static final String API_APP_KEY = "83j0EFwE75ykS8xdzA0hWTlaZW0rXT6kis5JlbUy4zfzAXtVcX";
	private static final String API_APP_SECRET = "cGnRTUbja2EGs0O847QjsiYghcfSDbSIZfghN7Z2uoM5RBFgYL";
	
	// Access codes #2: per user per application
	private static final String API_USER_TOKEN = "zaE3SxD3ZwxTnecdFYGMr3vu8d4AIT9qlacHxL7aslsGhiiweC";
	private static final String API_USER_SECRET = "CBPQWJHjIr01Z14Av8ylh9uouMflSBWH7CMRtAwp7h2e3722SM";
	private OAuthService service;
	private Token accessToken;
	
	//private RMIServerInterface server;
	private InterfaceRMI server;
	private String username; // username and password supplied by the user
	private String password;
	int userID;

	public HeyBean() {
		try {
			server = (InterfaceRMI) Naming.lookup("server");
			
			service = new ServiceBuilder()
					.provider(TumblrApi.class)
					.apiKey(API_APP_KEY)
					.apiSecret(API_APP_SECRET)
					.callback("http://localhost:8080/ficha5Hey/HeyBean")
					.build();
			accessToken = new Token(API_USER_TOKEN, API_USER_SECRET);
		}
		catch(NotBoundException|MalformedURLException|RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
	}
	
	public int verificaLogin(String nomeUser, String password) throws RemoteException {
		userID = server.verificaLogin(nomeUser, password);
		System.out.println("O meu userID e "+userID);
		return userID;
	}
	
	public int registaConta(String nomeUser, String password) throws RemoteException {
		System.out.println("Vou registar user "+nomeUser+" com password "+password);
		userID =  server.registaConta(nomeUser, password);
		System.out.println("O meu userID e "+userID);
		return userID;
	}
	
	// saldo
	public String consultarSaldo() throws RemoteException {
		return server.consultarSaldo(userID);
	}

	public void criaProjeto(int userID, String nome, float valor_objetivo, Calendar dataFinal, String descricao) throws RemoteException {
		server.criaProjeto(userID, nome, valor_objetivo, dataFinal, descricao);
	}
	
	public String consultarProjetos() throws RemoteException {
		return server.consultarProjetos(userID);
	}
	
	public String listaProjetosActuais() throws RemoteException {
		return server.listaProjetosActuais();
	}
	
	public String listaProjetosAntigos() throws RemoteException {
		return server.listaProjetosAntigos();
	}
	
	public void addRecompensa(int userID, int projID, String nome, float valor) throws RemoteException {
		server.addRecompensa(userID, projID, nome, valor);
	}
	
	public int procuraProjeto(String nome) throws RemoteException {
		return server.procuraProjeto(nome);
	}
	
	public void removeRecompensa(int userID, int projID, String nome) throws RemoteException {
		server.removeRecompensa(userID, projID, nome);
	}
	
	public String consultarRecompensas() throws RemoteException {
		return server.consultarRecompensas(userID);
	}
	
	public void doarDinheiro(int userID, int projID, float dinheiro) throws RemoteException {
		server.doarDinheiro(userID, projID, dinheiro);
	}
	
	public void eliminaProjeto(int userID, int projID) throws RemoteException {
		server.eliminaProjeto(userID, projID);
	}
	
	// retornar booleanos
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	// getters and setters
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getUserID() {
		return userID;
	}
	
	public void adicionaMensagem(int userID, int projID, String mensagem) throws RemoteException {
		server.adicionaMensagem(userID, projID, mensagem);
	}
	
	public int devolveIDUser(String username) throws RemoteException {
		return server.devolveIDUser(username);
	}
	public void respondeMensagens(int adminID, int userID, int projID, String mensagem) throws RemoteException {
		server.respondeMensagens(adminID, userID, projID, mensagem);
	}
	
	public String createTumblrPost(String projectName) {
		// makes request
		OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.tumblr.com/v2/blog/us-sd.tumblr.com/post", service);
		request.addBodyParameter("type","text");
		request.addBodyParameter("body",projectName);
		request.addHeader("Accept", "application/json");
		service.signRequest(accessToken, request);
		System.out.println(request.getHeaders().keySet());
		Response response = request.send();
		
		// gets response
		System.out.println("Got it! Lets see what we found...");
		System.out.println("HTTP RESPONSE: =============");
		System.out.println(response.getCode());
		System.out.println(response.getBody());
		System.out.println("END RESPONSE ===============");
		
		// gets projectID
		String projectID = getProjectIDFromTumblrPost(response.getBody());	
		System.out.println("Project id is "+projectID);
		return projectID;
	}

	public String getProjectIDFromTumblrPost(String responseString) {
		String newProjectID="";
		if (responseString.contains("id")) {
			String[] separateID = responseString.split("id\":");
			String part1 = separateID[1];
			separateID = part1.split("}");
			newProjectID = separateID[0];
		}
		return newProjectID;
	}
	
	public void likeTumblrPost(String projectID) {
		// makes request
		
		String reblog_key = getReblogKeyFromTumblrPost(service, accessToken, projectID);
		OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.tumblr.com/v2/user/like", service);
		request.addBodyParameter("id", projectID);
		request.addBodyParameter("reblog_key", reblog_key);
		request.addHeader("Accept", "application/json");
		service.signRequest(accessToken, request);
		System.out.println(request.getHeaders().keySet());
		Response response = request.send();
		
		// gets response
		System.out.println("Got it! Lets see what we found...");
		System.out.println("HTTP RESPONSE: =============");
		System.out.println(response.getCode());
		System.out.println(response.getBody());
		System.out.println("END RESPONSE ===============");
		
	}	
	
	public String getReblogKeyFromTumblrPost(OAuthService service, Token accessToken, String projectID) {
		Response response = getAllPostsFromUserTumblr(service, accessToken);
		String reblog_key = getReblogKey(response, projectID);	
		System.out.println("Reblog key: "+reblog_key);
		return reblog_key;
	}
	
	public Response getAllPostsFromUserTumblr(OAuthService service, Token accessToken) {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.tumblr.com/v2/blog/us-sd.tumblr.com/posts", service);
		request.addHeader("Accept", "application/json");
		service.signRequest(accessToken, request);
		System.out.println(request.getHeaders().keySet());
		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println("HTTP RESPONSE: =============");
		System.out.println("***"+response.getCode()+"***");
		System.out.println(response.getBody());
		System.out.println("END RESPONSE ===============");
		
		return response;
		
	}	
	
	public String getReblogKey(Response response, String projectID) {
		JSONObject inf = (JSONObject) JSONValue.parse(response.getBody());
		JSONArray arr = (JSONArray) ((JSONObject) inf.get("response")).get("posts");
		for(int i=0; i< arr.size(); i++) {
			JSONObject item = (JSONObject) arr.get(i);
			
			if (item.get("id").toString().compareToIgnoreCase(projectID)==0) {
				System.out.println("Match for project with id = "+projectID);
				return (String) item.get("reblog_key");
			}
		}
		return "";
	}
	
	public String procuraIDProjectoTumblr(int projID) throws RemoteException {
		return server.procuraIDProjectoTumblr(projID);
	}
	
	public String consultaMensagens(int utilizadorID, int projID) throws RemoteException {
		return server.consultaMensagens(utilizadorID, projID);
	}
}
