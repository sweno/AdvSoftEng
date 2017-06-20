package com.cs604;

public class Bid {
	private int bid_id;
	private int buyer_id;
	private String buyer_name;
	private int seller_id;
	private String seller_name;
	private int item_id;
	private String item_name;
	private String posted_date;
	private int quantity_requested;
	private double proposed_price;
	private boolean bid_accepted;
	private String bid_accepted_date;
	
	public Bid(){
	}
	
	public Bid(int buyerID, int sellerID, int itemID, String postDate, int quant, double proposal, boolean bidAcc, String accDate){
		// Constructor for when we don't know what the bid_id is going to be
		buyer_id = buyerID;
		seller_id = sellerID;
		item_id = itemID;
		posted_date = postDate;
		quantity_requested = quant;
		proposed_price = proposal;
		bid_accepted = bidAcc;
		bid_accepted_date = accDate;
	}
	
	public Bid(int bidID, int buyerID, String bName, int sellerID, String sName, int itemID, String iName, String postDate, int quant, double proposal, boolean bidAcc, String accDate){
		// Constructor for when we know everything
		bid_id = bidID;
		buyer_id = buyerID;
		buyer_name = bName;
		seller_id = sellerID;
		seller_name = sName;
		item_id = itemID;
		item_name = iName;
		posted_date = postDate;
		quantity_requested = quant;
		proposed_price = proposal;
		bid_accepted = bidAcc;
		bid_accepted_date = accDate;
	}

	public int getBidID(){
		return bid_id;
	}
	
	public void setBidID(int bidID){
		bid_id = bidID;
	}
	
	public String getBuyer_name() {
		return buyer_name;
	}

	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public int getBuyerID(){
		return buyer_id;
	}
	
	public void setBuyerID(int buyerID){
		buyer_id = buyerID;
	}
	
	public int getSellerID(){
		return seller_id;
	}
	
	public void setSellerID(int sellerID){
		seller_id = sellerID;
	}
	
	public int getItemID(){
		return item_id;
	}
	
	public void setItemID(int itemID){
		item_id = itemID;
	}
	
	public String getPostedDate(){
		return posted_date;
	}
	
	public void setPostedDate(String postedDate){
		posted_date = postedDate;
	}
	
	public int getQuantity(){
		return quantity_requested;
	}
	
	public void setQuantity(int newQuant){
		quantity_requested = newQuant;
	}
	
	public double getProposedPrice(){
		return proposed_price;
	}
	
	public void setProposedPrice(double proposal){
		proposed_price = proposal;
	}
	
	public boolean getBidAccepted(){
		return bid_accepted;
	}
	
	public void setBidAccepted(boolean bidAccepted ){
		bid_accepted = bidAccepted;
	}
	
	public String getBidAcceptedDate(){
		return bid_accepted_date;
	}
	
	public void setBidAcceptedDate(String accDate){
		bid_accepted_date = accDate;
	}
}