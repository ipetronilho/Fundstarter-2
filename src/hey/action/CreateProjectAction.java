
package hey.action;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import hey.model.HeyBean;

public class CreateProjectAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null, password = null;
	String projectname = null, description = null, goal, year, month, day;
	int userID;
	
	@Override
	public String execute() throws RemoteException {
		// any username is accepted without confirmation (should check using RMI)
		HeyBean user = this.getHeyBean();
		if (projectname!="") {
			float goalFloat = Float.parseFloat(goal);
			Calendar dataFinal = new GregorianCalendar();
			int yearInt = Integer.parseInt(year);
			int monthInt = Integer.parseInt(month);
			int dayInt = Integer.parseInt(day);
			dataFinal.set(Calendar.YEAR, yearInt);
			dataFinal.set(Calendar.MONTH, monthInt);
			dataFinal.set(Calendar.DAY_OF_MONTH, dayInt);
			
			user.criaProjeto(userID, projectname, goalFloat, dataFinal, description);
			
			user.createTumblrPost(projectname);
			
			return SUCCESS;
		}
		else
			return ERROR;

	}
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
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
	
	// getters/setters

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
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
