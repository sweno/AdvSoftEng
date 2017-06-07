package com.cs604;

public class Bid {
	private int bid_id;
	private int buyer_id;
	private int seller_id;
	private int item_id;
	private String posted_date;
	private int quantity_requested;
	private int proposed_price;
	private boolean bid_accepted;
	private String bid_accepted_date;
	
	public Bid(){
	}
	
	public Bid(int buyerID, int sellerID, int itemID, String postDate, int quant, int proposal, boolean bidAcc, String accDate){
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
	
	public Bid(int bidID, int buyerID, int sellerID, int itemID, String postDate, int quant, int proposal, boolean bidAcc, String accDate){
		// Constructor for when we know everything
		bid_id = bidID;
		buyer_id = buyerID;
		seller_id = sellerID;
		item_id = itemID;
		posted_date = postDate;
		quantity_requested = quant;
		proposed_price = proposal;
		bid_accepted = bidAcc;
		bid_accepted_date = accDate;
	}

	public int GetBidID(){
		return bid_id;
	}
	
	public void SetBidID(int bidID){
		bid_id = bidID;
	}
	
	public int GetBuyerID(){
		return buyer_id;
	}
	
	public void SetBuyerID(int buyerID){
		buyer_id = buyerID;
	}
	
	public int GetSellerID(){
		return seller_id;
	}
	
	public void SetSellerID(int sellerID){
		seller_id = sellerID;
	}
	
	public int GetItemID(){
		return item_id;
	}
	
	public void SetItemID(int itemID){
		item_id = itemID;
	}
	
	public String GetPostedDate(){
		return posted_date;
	}
	
	public void SetPostedDate(String postedDate){
		posted_date = postedDate;
	}
	
	public int GetQuantity(){
		return quantity_requested;
	}
	
	public void SetQuantity(int newQuant){
		quantity_requested = newQuant;
	}
	
	public int GetProposedPrice(){
		return proposed_price;
	}
	
	public void SetProposedPrice(int proposal){
		proposed_price = proposal;
	}
	
	public boolean GetBidAccepted(){
		return bid_accepted;
	}
	
	public void SetBidAccepted(boolean bidAccepted ){
		bid_accepted = bidAccepted;
	}
	
	public String GetBidAcceptedDate(){
		return bid_accepted_date;
	}
	
	public void SetBidAcceptedDate(String accDate){
		bid_accepted_date = accDate;
	}
}