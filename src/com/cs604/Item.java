package com.cs604;

public class Item {
	private int itemID;
	private int sellerID;
	private String name;
	private String description;
	private String keywords;
	private int quantityInStock;
	private String submitDate;
	
	public Item(){
	}
	
	public Item(int itemID, int sellerID, String iName, String iDesc, String iKeywords, int quant, String subDate){
		this.itemID = itemID;
		this.sellerID = sellerID;
		this.name = iName;
		this.description = iDesc;
		this.keywords = iKeywords;
		this.quantityInStock = quant;
		this.submitDate = subDate;
	}

	public Item(int sellerID, String iName, String iDesc, String iKeywords, int quant){
		// create an item where we don't know it's ID already
		this.itemID = 0;
		this.sellerID = sellerID;
		this.name = iName;
		this.description = iDesc;
		this.keywords = iKeywords;
		this.quantityInStock = quant;
	}
	
	public int getItemID(){
		return itemID;
	}
	
	protected void setItemID(int itemID){
		this.itemID = itemID;
	}
	
	public int getSellerID(){
		return sellerID;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String newName){
		name = newName;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String newDesc){
		description = newDesc;
	}
	
	public String getKeywords(){
		return keywords;
	}
	
	public void setKeywords(String newKeywords){
		keywords = newKeywords;
	}
	
	public int getQuantityInStock(){
		return quantityInStock;
	}
	
	public void setQuantityInStock(int newQuant){
		quantityInStock = newQuant;
	}
	
	public String getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(String submit_date) {
		this.submitDate = submit_date;
	}

}
