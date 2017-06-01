package cs604;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User{
	private int user_id;
	private String name;
	private String email;
	private String password_hash;
	private Address billing_address;
	private Address shipping_address;
	private Boolean buyer_flag;
	private Boolean seller_flag;
	private MessageDigest mdSHA256;
	
	public User(){
	}
	
	public User(String uName, String uEmail, String uPassword){
		name = uName;
		email = uEmail;
		// init the message digest
		try{
			mdSHA256 = MessageDigest.getInstance("SHA-256");
		}catch(NoSuchAlgorithmException e){
			System.err.println("No SHA256 Message Digest: " + e.getMessage());
		}
		password_hash = passwordHash(uPassword);
		buyer_flag = false;
		seller_flag = false;
	}
	
	protected User(int uID, String uName, String uEmail, String uPHash, Address bAddr){
		// function specifically for importing data from DB
		user_id = uID;
		name = uName;
		email = uEmail;
		// init the message digest
		try{
			mdSHA256 = MessageDigest.getInstance("SHA-256");
		}catch(NoSuchAlgorithmException e){
			System.err.println("No SHA256 Message Digest: " + e.getMessage());
		}
		password_hash = uPHash;
		billing_address = bAddr;
		buyer_flag = false;
		seller_flag = false;
	}
	
	public int GetID(){
		return user_id;
	}
	
	public void SetID(int uID){
		user_id = uID;
	}
	
	public String GetName(){
		return name;
	}
	
	public void SetName(String uName){
		name = uName;
	}
	
	public String GetEmail(){
		return email;
	}
	
	public void SetEmail(String uEmail){
		email = uEmail;
	}
	
	public Boolean CheckPasswordHash(String uPassword){
		if (password_hash.equals(passwordHash(uPassword))){
			return true;
		}else{
			return false;
		}
	}
	
	public Boolean SetNewPasswordHash(String uPassword, String newPassword){
		if(password_hash.equals(passwordHash(uPassword))){
			//passwords match, set new password_hash
			password_hash = passwordHash(newPassword);
			return true;
		}else{
			return false;
		}
	}
	
	protected String GetPasswordHash(){
		return password_hash;
	}
	
	public Address GetBillingAddress(){
		return billing_address;
	}
	
	public void SetBillingAddress(Address uBillAddr){
		billing_address = uBillAddr;
	}
	
	public Address GetShippingAddress(){
		return shipping_address;
	}
	
	public void SetShippingAddress(Address uShipAddr){
		shipping_address = uShipAddr;
	}
	
	public Boolean GetBuyerFlag(){
		return buyer_flag;
	}
	
	public void SetBuyerFlag(Boolean bFlag){
		buyer_flag = bFlag;
	}
	
	public Boolean GetSellerFlag(){
		return seller_flag;
	}
	
	public void SetSellerFlag(Boolean sFlag){
		seller_flag = sFlag;
	}
	
	public void Login(String username){
		// push a temporary id to the browser cookie store
	}
	
	public void Logout(String username){
		// remove the temporary id from the browser cookie store
	}
	
	public void commit(){
		//push contents to DB
	}
	
	private String passwordHash(String uPassword){
		//clear the digest (we shouldn't care what was in there before)
		mdSHA256.reset();
		//add the content to the digest
		mdSHA256.update(uPassword.getBytes());
		// calculate resutls
//		String results = new String(mdSHA256.digest());
//		return results;
		return new String(mdSHA256.digest());
	}
}