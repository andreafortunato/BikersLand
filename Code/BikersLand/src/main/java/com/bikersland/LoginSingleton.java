package com.bikersland;

public class LoginSingleton {
	private static LoginSingleton loginInstance = null;
	
	private User user = null;
	
	private LoginSingleton() {
		
	}
	
	public static LoginSingleton getLoginInstance() {
		if(LoginSingleton.loginInstance == null)
			LoginSingleton.loginInstance = new LoginSingleton();
		
		return loginInstance;
	}
	
	
	
	public User getUser() {
		return loginInstance.user;
	}
	
	public void setUser(User user2) {
		loginInstance.user = user2;
	}
}


