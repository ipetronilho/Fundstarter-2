package hey.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import hey.model.HeyBean;

public class LoginAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null, password = null;
	private int userID;

	@Override
	public String execute() throws RemoteException {

		HeyBean user = this.getHeyBean();
		if((userID=user.verificaLogin(username, password))>=0) {
			session.put("username", username);
			session.put("loggedin", true); // this marks the user as logged in
			session.put("userID", userID);
			System.out.println("o meu userID e "+session.get("userID"));
			return SUCCESS;
		}
		else
			return LOGIN;
	}
	
	
	
	public void setUsername(String username) {
		this.username = username; // will you sanitize this input? maybe use a prepared statement?
	}

	public void setPassword(String password) {
		System.out.println("A passe e "+password);
		this.password = password; // what about this input? 
	}
	
	public HeyBean getHeyBean() {
		if(!session.containsKey("heyBean"))
			this.setHeyBean(new HeyBean());
		return (HeyBean) session.get("heyBean");
	}

	public void setHeyBean(HeyBean heyBean) {
		this.session.put("heyBean", heyBean);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
