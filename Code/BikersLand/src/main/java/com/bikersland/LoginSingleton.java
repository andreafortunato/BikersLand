package com.bikersland;

public class LoginSingleton {
	private static LoginSingleton loginInstance = null;
	
	private User user = null;
	
	private LoginSingleton() {}
	
	public static LoginSingleton getLoginInstance() {
		if(LoginSingleton.loginInstance == null)
			LoginSingleton.loginInstance = new LoginSingleton();
		
		return loginInstance;
	}
	
	public User getUser() {
		return getLoginInstance().user;
	}
	
	public void setUser(User logged_user) {
		getLoginInstance().user = logged_user;
	}
	
	public static void logout() {
		getLoginInstance().user = null;
	}
}