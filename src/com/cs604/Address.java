package com.cs604;

public class Address{
	private String full_name;
	private String street_1;
	private String street_2;
	private String city;
	private String state;
	private String zip;
	private String country;
	
	public Address(){
	}
	
	public Address(String fName, String s1, String s2, String cty, String st, String zp, String cntry){
		full_name = fName;
		street_1 = s1;
		street_2 = s2;
		city = cty;
		state = st;
		zip = zp;
		country = cntry;
	}
	
	public String getFullName(){
		return full_name;
	}
	
	public void setFullName(String fName){
		full_name = fName;
	}
	
	public String getStreet1(){
		return street_1;
	}
	
	public void setStreet1(String s1){
		street_1 = s1;
	}
	
	public String getStreet2(){
		return street_2;
	}
	
	public void setStreet2(String s2){
		street_2 = s2;
	}
	
	public String getCity(){
		return city;
	}
	
	public void setCity(String cty){
		city = cty;
	}
	
	public String getState(){
		return state;
	}
	
	public void setState(String st){
		state = st;
	}
	
	public String getZip(){
		return zip;
	}
	
	public void setZip(String zp){
		zip = zp;
	}
	
	public String getCountry(){
		return country;
	}
	
	public void setCountry(String cntry){
		country = cntry;
	}
	
	public String asString(){
		return full_name + "\n" + street_1 + "\n" + street_2  + "\n" + city + ", " + state +" "+zip+"\n"+country;
	}
	
}