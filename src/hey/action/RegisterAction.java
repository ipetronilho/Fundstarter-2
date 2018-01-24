package hey.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import hey.model.HeyBean;

public class RegisterAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null, password = null;

	@Override
	public String execute() throws RemoteException {
		// any username is accepted without confirmation (should check using RMI)
		HeyBean user = this.getHeyBean();
		
		if(this.username != null && !username.equals("")) {
			user.setUsername(this.username);
			user.setPassword(this.password);
			
			session.put("username", username);
			session.put("loggedin", true); // this marks the user as logged in
			
			int userID = user.registaConta(this.username, this.password);
			user.setUserID(userID);
			session.put("userID", userID); // ${session.userID}
			return SUCCESS;
		}
		else
			return ERROR;
	}
	
	public void setUsername(String username) {
		this.username = username; // will you sanitize this input? maybe use a prepared statement?
	}

	public void setPassword(String password) {
		System.out.println("A passe e "+password);
		this.password = password; // what about this input? 
	}
	
	public HeyBean getHeyBean() {
		if(!session.containsKey("heyBean")) {
			this.setHeyBean(new HeyBean());
		}
		return (HeyBean) session.get("heyBean");
	}

	public void setHeyBean(HeyBean heyBean) {
		this.session.put("heyBean", heyBean);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		System.out.println("Here!");
		this.session = session;
	}
}
