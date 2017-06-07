package com.cs604;

public class Listing {
	private int list_id;
	private int prod_id;
	private String base_amount;
	private int min_ammount;
	private String list_date;
	private String effictive_date;
	private String termination_date;

	public Listing(){
	}
	
	public Listing(int listID, int prodID, String baseAmount, int minAmount, String listDate, String effDate, String termDate){
		// constructor for when we are pulling things off the database
		list_id = listID;
		prod_id = prodID;
		base_amount = baseAmount;
		min_ammount = minAmount;
		list_date = listDate;
		effictive_date = effDate;
		termination_date = termDate;
	}
	
	public Listing(int prodID, String baseAmount, int minAmount, String listDate, String effDate, String termDate){
		// constructor for when we don't know the list_id
		prod_id = prodID;
		base_amount = baseAmount;
		min_ammount = minAmount;
		list_date = listDate;
		effictive_date = effDate;
		termination_date = termDate;
	}

	public int GetListID() {
		return list_id;
	}

	public void SetListID(int list_id) {
		this.list_id = list_id;
	}

	public int GetProdID() {
		return prod_id;
	}

	public void SetProdID(int prod_id) {
		this.prod_id = prod_id;
	}

	public String GetBaseAmount() {
		return base_amount;
	}

	public void SetBaseAmount(String base_amount) {
		this.base_amount = base_amount;
	}

	public int GetMinAmmount() {
		return min_ammount;
	}

	public void SetMinAmmount(int min_ammount) {
		this.min_ammount = min_ammount;
	}

	public String GetListDate() {
		return list_date;
	}

	public void SetListDate(String list_date) {
		this.list_date = list_date;
	}

	public String GetEffictiveDate() {
		return effictive_date;
	}

	public void SetEffictivDate(String effictive_date) {
		this.effictive_date = effictive_date;
	}

	public String GetTerminationDate() {
		return termination_date;
	}

	public void SetTerminationDate(String termination_date) {
		this.termination_date = termination_date;
	}


}
