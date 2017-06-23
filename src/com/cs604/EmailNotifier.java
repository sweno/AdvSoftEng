package com.cs604;

import org.apache.commons.mail.*;

public class EmailNotifier {

	private String hostname;
	private int port;
	private DefaultAuthenticator defAuth;
	private String fromAddr;
	
    public EmailNotifier(String name, String pass, String host, String fromAddr){
    	hostname = host;
    	port = 465;
    	defAuth = new DefaultAuthenticator(name, pass);
    	this.fromAddr = fromAddr;
    }
	
    public void notify_seller(String name, String sellerEmail, String productName, int amount, double cost, Address destination){
    	
    	//when sending an email we need:
    	// recipient email address
    	// recipient name
    	// message:
    	// 		what was sold, for how much, address to send it to 
    	String message = "Hello "+name+"\nYou have sold "+amount+" of "+productName+" for $"+cost+"\nPlease send the product to:\n";
    	message += destination.asString();
    	
    	try{
    	Email email = new SimpleEmail();
    	//set up debugging
//    	email.setDebug(true);
    	email.setHostName(hostname);
    	email.setSmtpPort(port);
    	email.setAuthenticator(defAuth);
    	email.setSSLOnConnect(true);
    	email.setFrom(fromAddr);
    	email.setSubject("GUTO automated invoice");
    	email.setMsg(message);
    	email.addTo(sellerEmail);
    	email.send();
    	} catch(EmailException e){
    		System.out.println("EmailNotifier Error - notify_seller: " + e.getMessage());
    	}
    }
    
    public void notify_buyer(String name, String buyerEmail, String productName, int amount, double cost, Address destination){
    	
    	//when sending an email we need:
    	// recipient email address
    	// recipient name
    	// message:
    	// 		what was bought, for how much, address to send payment to 
    	
    	String message = "Hello "+name+"\nYou have bought "+amount+" of "+productName+" for $"+cost+"\nPlease send the payment to:\n";
    	message += destination.asString();

    	
    	try{
    	Email email = new SimpleEmail();
    	email.setHostName(hostname);
    	email.setSmtpPort(port);
    	email.setAuthenticator(defAuth);
    	email.setSSLOnConnect(true);
    	email.setFrom(fromAddr);
    	email.setSubject("GUTO automated invoice");
    	email.setMsg(message);
    	email.addTo(buyerEmail);
    	email.send();
    	} catch(EmailException e){
    		System.out.println("EmailNotifier Error - notify_buyer: " + e.getMessage());
    	}

    }

}
