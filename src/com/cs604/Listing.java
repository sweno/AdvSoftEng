package com.cs604;

public class Listing {
	private int list_id;
	private int prod_id;
	private String prod_name;
	private double base_cost;
	private int min_ammount;
	private String list_date;
	private String effective_date;
	private String termination_date;

	public Listing(){
	}
	
	public Listing(int listID, int prodID, String prodName, double baseAmount, int minAmount, String listDate, String effDate, String termDate){
		// constructor for when we are pulling things off the database
		list_id = listID;
		prod_id = prodID;
		prod_name = prodName;
		base_cost = baseAmount;
		min_ammount = minAmount;
		list_date = listDate;
		effective_date = effDate;
		termination_date = termDate;
	}
	
	public Listing(int prodID, String prodName, double baseAmount, int minAmount, String listDate, String effDate, String termDate){
		// constructor for when we don't know the list_id
		prod_id = prodID;
		prod_name = prodName;
		base_cost = baseAmount;
		min_ammount = minAmount;
		list_date = listDate;
		effective_date = effDate;
		termination_date = termDate;
	}

	public int getListID() {
		return list_id;
	}

	public void setListID(int list_id) {
		this.list_id = list_id;
	}

	public int getProdID() {
		return prod_id;
	}

	public void setProdID(int prod_id) {
		this.prod_id = prod_id;
	}

	public String getProdName() {
		return prod_name;
	}

	public void setProdName(String prod_name) {
		this.prod_name = prod_name;
	}

	public double getBaseCost() {
		return base_cost;
	}

	public void setBaseCost(double base_amount) {
		this.base_cost = base_amount;
	}

	public int getMinAmount() {
		return min_ammount;
	}

	public void setMinAmount(int min_ammount) {
		this.min_ammount = min_ammount;
	}

	public String getListDate() {
		return list_date;
	}

	public void setListDate(String list_date) {
		this.list_date = list_date;
	}

	public String getEffectiveDate() {
		return effective_date;
	}

	public void setEffectiveDate(String effictive_date) {
		this.effective_date = effictive_date;
	}

	public String getTerminationDate() {
		return termination_date;
	}

	public void setTerminationDate(String termination_date) {
		this.termination_date = termination_date;
	}


}
