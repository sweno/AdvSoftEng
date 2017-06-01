package cs604;

public class Item {
	private int item_id_num;
	private int seller_id_num;
	private String name;
	private String description;
	private String keywords;
	private int quantity_in_stock;
	private int minimum_buy_quantity;
	private float price;
	private boolean accept_bids_at_price_flag;
	
	public Item(){
	}
	
	public Item(int itemID, int sellerID, String iName, String iDesc, String iKeywords, int quant, int minimum, float iPrice, boolean acceptFlag){
		item_id_num = itemID;
		seller_id_num = sellerID;
		name = iName;
		description = iDesc;
		keywords = iKeywords;
		quantity_in_stock = quant;
		minimum_buy_quantity = minimum;
		price = iPrice;
		accept_bids_at_price_flag = acceptFlag;
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
	
	public int GetMinimumBuyQuantity(){
		return minimum_buy_quantity;
	}
	
	public void SetMinimumBuyQuantity(int newMin){
		minimum_buy_quantity = newMin;
	}
	
	public float GetPrice(){
		return price;
	}
	
	public void SetPrice(int newPrice){
		price = newPrice;
	}
	
	public boolean GetAcceptBidsAtPrice(){
		return accept_bids_at_price_flag;
	}
	
	public void SetAcceptBidsAtPrice(boolean newSet){
		accept_bids_at_price_flag = newSet;
	}
	
}
