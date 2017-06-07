package com.cs604;

public class Item {
	private int item_id_num;
	private int seller_id_num;
	private String name;
	private String description;
	private String keywords;
	private int quantity_in_stock;
	private String submit_date;
	
	public Item(){
	}
	
	public Item(int itemID, int sellerID, String iName, String iDesc, String iKeywords, int quant, String subDate){
		item_id_num = itemID;
		seller_id_num = sellerID;
		name = iName;
		description = iDesc;
		keywords = iKeywords;
		quantity_in_stock = quant;
		submit_date = subDate;
	}
	
	public int GetItemID(){
		return item_id_num;
	}
	
	public int GetSellerID(){
		return seller_id_num;
	}
	
	public String GetName(){
		return name;
	}
	
	public void SetName(String newName){
		name = newName;
	}
	
	public String GetDescription(){
		return description;
	}
	
	public void SetDescription(String newDesc){
		description = newDesc;
	}
	
	public String GetKeywords(){
		return keywords;
	}
	
	public void SetKeywords(String newKeywords){
		keywords = newKeywords;
	}
	
	public int GetQuantityInStock(){
		return quantity_in_stock;
	}
	
	public void SetQuantityInStock(int newQuant){
		quantity_in_stock = newQuant;
	}
	
	public String GetSubmitDate() {
		return submit_date;
	}

	public void SetSubmitDate(String submit_date) {
		this.submit_date = submit_date;
	}

}
