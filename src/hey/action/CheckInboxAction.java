package hey.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import hey.model.HeyBean;

public class CheckInboxAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null, password = null;
	String allmessages, inboxprojectname;
	
	@Override
	public String execute() throws RemoteException {
		HeyBean user = this.getHeyBean();
		int projID = user.procuraProjeto(inboxprojectname);
		if((allmessages=user.consultaMensagens(user.getUserID(), projID)) != "") {
			session.put("allmessages", allmessages);
			return SUCCESS;
		}
		else
			return ERROR;
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

	public String consultarSaldo(int userID) {
		return this.consultarSaldo(userID);
	}
	
	public void setUsername(String username) {
		this.username = username; // will you sanitize this input? maybe use a prepared statement?
		System.out.println("My username is "+username);
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

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
