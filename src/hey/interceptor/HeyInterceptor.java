package hey.interceptor;

import java.util.Map;

import java.util.Map;
import java.util.Calendar;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class HeyInterceptor implements Interceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> session = invocation.getInvocationContext().getSession();
		
		// this method intercepts the execution of the action and we get access
		// to the session, to the action, and to the context of this invocation
		
		//&& (boolean) session.get("loggedin"))
		if ((session!= null) && session.containsKey(("loggedin")))
			return invocation.invoke();
		else
			return Action.LOGIN; // falha
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
	
	
	
	
	
	

