package hey.action;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import hey.model.HeyBean;

public class AddRewardAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null, password = null;
	String projectname="", rewardname = "", rewardvalue = null;

	int userID;
	
	@Override
	public String execute() throws RemoteException {
		HeyBean user = this.getHeyBean();
		if (projectname!="" && rewardname!="") {
			float rewardvalueFloat = Float.parseFloat(rewardvalue);
			int projID = user.procuraProjeto(projectname);
			user.addRecompensa(userID, projID, rewardname, rewardvalueFloat);
			return SUCCESS;
		}
		else
			return ERROR;

	}
	
	// getters & setters
	public String getRewardname() {
		return rewardname;
	}

	public void setRewardname(String rewardname) {
		this.rewardname = rewardname;
	}

	public String getRewardvalue() {
		return rewardvalue;
	}

	public void setRewardvalue(String rewardvalue) {
		this.rewardvalue = rewardvalue;
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
	
	// getters/setters

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
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
