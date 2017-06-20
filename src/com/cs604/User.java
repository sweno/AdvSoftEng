package com.cs604;

import org.apache.commons.codec.digest.*;
import org.apache.commons.lang3.StringUtils;

public class User{
	private int user_id;
	private String name;
	private String email;
	private String password_hash;
	private Address billing_address;
	private Address shipping_address;
	private boolean buyer_flag;
	private boolean seller_flag;
	private boolean shipping_is_billing;
	
	public User(){
	}
	
	public User(String uName, String uEmail, String uPassword, Address bAddr, Address sAddr, boolean buyer, boolean seller, boolean SisB){
		name = uName;
		email = uEmail;
		password_hash = passwordHash(uPassword);
		billing_address = bAddr;
		shipping_address = sAddr;
		buyer_flag = buyer;
		seller_flag = seller;
		shipping_is_billing = SisB;
	}
	
	protected User(int uID, String uName, String uEmail, String uPHash, Address bAddr, Address sAddr, boolean buyer, boolean seller, boolean SisB){
		// function specifically for importing data from DB
		user_id = uID;
		name = uName;
		email = uEmail;
		password_hash = uPHash;
		billing_address = bAddr;
		shipping_address = sAddr;
		buyer_flag = buyer;
		seller_flag = seller;
		shipping_is_billing = SisB;
	}
	
	public int getID(){
		return user_id;
	}
	
	public void setID(int uID){
		user_id = uID;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String uName){
		name = uName;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String uEmail){
		email = uEmail;
	}
	
	public Boolean CheckPasswordHash(String uPassword){
		return password_hash.equals(passwordHash(uPassword));
	}
	
	public Boolean setNewPasswordHash(String uPassword, String newPassword){
		if(password_hash.equals(passwordHash(uPassword))){
			//passwords match, set new password_hash
			password_hash = passwordHash(newPassword);
			return true;
		}else{
			return false;
		}
	}
	
	protected String getPasswordHash(){
		return password_hash;
	}
	
	public Address getBillingAddress(){
		return billing_address;
	}
	
	public void setBillingAddress(Address uBillAddr){
		billing_address = uBillAddr;
	}
	
	public Address getShippingAddress(){
		return shipping_address;
	}
	
	public void setShippingAddress(Address uShipAddr){
		shipping_address = uShipAddr;
	}
	
	public Boolean getBuyerFlag(){
		return buyer_flag;
	}
	
	public void setBuyerFlag(Boolean bFlag){
		buyer_flag = bFlag;
	}
	
	public Boolean getSellerFlag(){
		return seller_flag;
	}
	
	public void setSellerFlag(Boolean sFlag){
		seller_flag = sFlag;
	}
		
	public boolean getShippingIsBilling() {
		return shipping_is_billing;
	}

	public void setShippingIsBilling(boolean shipping_is_billing) {
		this.shipping_is_billing = shipping_is_billing;
	}
	
	private String passwordHash(String uPassword){
		if(StringUtils.isEmpty(password_hash)){
			return Sha2Crypt.sha256Crypt(uPassword.getBytes());
		}else{
			return Sha2Crypt.sha256Crypt(uPassword.getBytes(), password_hash);
		}
	}
	
	protected void debugUser(){
		String debug = "User debug info ::";
		debug += "\nuserID: " + user_id;
		debug += "\nname: " + name;
		debug += "\nemail: " + email;
		debug += "\npass: " + password_hash;
		debug += "\nbuyer: " + buyer_flag;
		debug += "\nseller: " + seller_flag;
		debug += "\nship=bill: " + shipping_is_billing;
		System.out.println(debug);
	}

}