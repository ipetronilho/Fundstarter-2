package hey.action;
import ws.WebSocketAnnotation;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import hey.model.HeyBean;
import ws.WebSocketAnnotation;

public class PledgeProjectAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null, password = null;
	String projectpledgename="", pledgequantity = "";
	int userID;
	WebSocketAnnotation webSocket = new WebSocketAnnotation();
	
	@Override
	public String execute() throws RemoteException {
		HeyBean user = this.getHeyBean();
		
		if (projectpledgename!="" && pledgequantity!="") {
			int projID = user.procuraProjeto(projectpledgename);
			int userID = (Integer) session.get("userID");
			Float pledgequantityFloat = Float.parseFloat(pledgequantity);
			System.out.println("Userid: "+userID);
			user.doarDinheiro(userID, projID, pledgequantityFloat);
			String tumblrProjID = user.procuraIDProjectoTumblr(projID);
			
			user.likeTumblrPost(tumblrProjID);
			
			// a partir daqui vejo no ws qual o admin do projeto e mando a msg para a session dele
			//webSocket.sendMessageToUser(projID, "User "+userID+" acabou de doar.");
			return SUCCESS;
		}
		else
			return ERROR;

	}
	
	// getters & setters
	public void setUsername(String username) {
		this.username = username; // will you sanitize this input? maybe use a prepared statement?
		System.out.println("My username is "+username);
	}

	public String getProjectpledgename() {
		return projectpledgename;
	}

	public void setProjectpledgename(String projectpledgename) {
		this.projectpledgename = projectpledgename;
	}

	public String getPledgequantity() {
		return pledgequantity;
	}

	public void setPledgequantity(String pledgequantity) {
		this.pledgequantity = pledgequantity;
	}

	public void setPassword(String password) {
		System.out.println("My password is "+password);
		this.password = password; // what about this input? 
	}
	
	public HeyBean getHeyBean() {
		if(!session.containsKey("heyBean")) {
			this.setMainMenuBean(new HeyBean());
		}
		return (HeyBean) session.get("heyBean");
	}

	public void setMainMenuBean(HeyBean mainMenuBean) {
		this.session.put("heyBean", mainMenuBean);
	}
	
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}


	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
