package com.bikersland;

public class Tappa {
	
	public static int totalTappaNumber = 1;
	
    private int tappaNumber;
    private String city;

    public Tappa() {
    	tappaNumber = totalTappaNumber++;
    }
    
    public void setTappaNumber(Integer tappaNumber) {
    	this.tappaNumber = tappaNumber;
    }
    
    public void setCity(String city) {
        this.city = city;
    }

    public int getTappaNumber() {
        return tappaNumber;
    }

    public String getCity() {
        return city;
    }
}
