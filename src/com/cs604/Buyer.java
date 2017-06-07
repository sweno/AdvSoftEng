package com.cs604;

public class Buyer extends User {
	private Item[] current_sales;
	private Item[] past_sales;

	public void addItemForSale(Item newItem){
		
	}
	
	public void removeItemForSale(Item oldItem){
		
	}
	
	public void sell(){
		
	}
	
	public void rejectBid(){
		
	}
	
	public void getPastSales(){
		
	}

	public class Seller extends User {
		private Item[] current_bids;
		private Item[] past_purchases;
		
		public void addBidForItem(){
			
		}
		
		public void removeBidForItem(){
			
		}
		
		public void buy(){
			
		}
		
		public void getPastPurchases(){
			
		}
	}

}
