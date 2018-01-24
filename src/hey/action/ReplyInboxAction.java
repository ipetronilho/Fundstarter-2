package hey.action;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import hey.model.HeyBean;

public class ReplyInboxAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null, password = null;
	String replymessage="", replyusername = "", projectname="";
	
	@Override
	public String execute() throws RemoteException {
		HeyBean user = this.getHeyBean();
		projectname=(String) session.get("inboxprojectname");
		if (replyusername!="" && replymessage!="") {
			int projID = user.procuraProjeto(projectname);
			int userID = user.devolveIDUser(replyusername);
			user.respondeMensagens(user.getUserID(), userID, projID, replymessage);
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
	
	// getters/setters

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getReplymessage() {
		return replymessage;
	}

	public void setReplymessage(String replymessage) {
		this.replymessage = replymessage;
	}

	public String getReplyusername() {
		return replyusername;
	}

	public void setReplyusername(String replyusername) {
		this.replyusername = replyusername;
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
