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
	
	public String GetFullName(){
		return full_name;
	}
	
	public void SetFullName(String fName){
		full_name = fName;
	}
	
	public String GetStreet1(){
		return street_1;
	}
	
	public void SetStreet1(String s1){
		street_1 = s1;
	}
	
	public String GetStreet2(){
		return street_2;
	}
	
	public void SetStreet2(String s2){
		street_2 = s2;
	}
	
	public String GetCity(){
		return city;
	}
	
	public void SetCity(String cty){
		city = cty;
	}
	
	public String GetState(){
		return state;
	}
	
	public void SetState(String st){
		state = st;
	}
	
	public String GetZip(){
		return zip;
	}
	
	public void SetZip(String zp){
		zip = zp;
	}
	
	public String GetCountry(){
		return country;
	}
	
	public void SetCountry(String cntry){
		country = cntry;
	}
}