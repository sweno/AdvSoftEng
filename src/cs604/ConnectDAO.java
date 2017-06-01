package cs604;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConnectDAO {
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;
	
	public ConnectDAO(String URL, String Username, String password){
		jdbcURL = URL;
		jdbcUsername = Username;
		jdbcPassword = password;
	}
	
	protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            jdbcConnection = DriverManager.getConnection(
                                        jdbcURL, jdbcUsername, jdbcPassword);
        }
    }
     
    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }
    
// User Database CRUD operations:
    public int insertUser(User newUser){
    	int newUserID = 0;
    	String countryID = null;
    	String provID = null;
    	try{
    		Address newAddress = newUser.GetBillingAddress();
    		//This is a three step process
    		//1) if the CountryName doesn't exist, insert it
    		String sql = "INSERT INTO Tbl_Country (CountryName) SELECT * FROM (SELECT ?) AS tmp " + 
    					 "WHERE NOT EXISTS (SELECT CountryName FROM Tbl_Country WHERE CountryName = ?) LIMIT 1;";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.GetCountry());
    		state.setString(2, newAddress.GetCountry());
    		state.executeUpdate(); // all that we care about is that we didn't throw an error, both 0 or 1 could be valid returns.
    		
    		//1a)get the CountryID for the Country in question
    		sql = "SELECT CountryID FROM Tbl_Country WHERE CountryName = ? LIMIT 1;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.GetCountry());
    		ResultSet results = state.executeQuery(sql);
	        
	        if (results.next()) {
	        	countryID = results.getString(1);
	        }
    		//2) if  the State (prov) name doesn't exist insert it
	        sql = "INSERT INTO Tbl_Prov (ProvName) SELECT * FROM (SELECT ?) AS tmp " + 
				  "WHERE NOT EXISTS (SELECT ProvName FROM Tbl_Prov WHERE ProvName = ?) LIMIT 1;";
	        state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.GetState());
    		state.setString(2, newAddress.GetState());
    		state.executeUpdate(); // all that we care about is that we didn't throw an error, both 0 or 1 could be valid returns.
    	
    		//2a) get the ProvID for the State in question
    		sql = "SELECT ProvID FROM Tbl_Prov WHERE ProvName = ? LIMIT 1;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.GetState());
    		results = state.executeQuery(sql);
	        
	        if (results.next()) {
	        	provID = results.getString(1);
	        }

    		sql = "INSERT INTO Tbl_User (UserBillAdd1, UserBillAdd2, UserBillCity, UserBillCountryID, UserBillPC, UserBillProvID, " + 
    		 	  "UserCompName, UserEmail, UserPWD) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
			state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.GetStreet1());
    		state.setString(2, newAddress.GetStreet2());
    		state.setString(3, newAddress.GetCity());
    		state.setString(4, countryID);
    		state.setString(5, newAddress.GetZip());
    		state.setString(6, provID);
			state.setString(7, newUser.GetName());
			state.setString(8, newUser.GetEmail());
			state.setString(9, newUser.GetPasswordHash());
			
			boolean rowUpdated = state.executeUpdate() > 0;
			if(rowUpdated){
				sql = "SELECT LAST_INSERT_ID();";
	    		state = jdbcConnection.prepareStatement(sql);
	    		results = state.executeQuery(sql);
	    		if (results.next()) {
		        	newUserID = results.getInt(1);
		        }
			}
			
			state.close();
			disconnect();
		} catch (SQLException e) {
        	System.out.println("Database Error - insertUser: " + e.getMessage());
		}
    	return newUserID;
    }
    
    public List<User> listUsers(){
    	List<User> userList = new ArrayList<>();
       	try {
    		//do a three table join on User, Prov, and County
    		String sql = "SELECT Tbl_User.UserID, Tbl_User.UserEmail, Tbl_User.UserPWD, Tbl_User.UserCompName, " +
    					 "Tbl_User.UserBillCity, Tbl_Prov.ProvName, Tbl_User.UserBillPC, Tbl_Country.CountryName " + 
    				     "Tbl_User.UserBillAdd1, Tbl_User.UserBillAdd2, FROM Tbl_User " + 
    					 "LEFT JOIN Tbl_Prov on Tbl_User.UserBillProvID = Tbl_Prov.ProvID " + 
    					 "LEFT JOIN Tbl_Country on Tbl_User.UserBillCountryID = Tbl_Country.CountryID;";
			connect();
    	
			Statement state = jdbcConnection.createStatement();
	        ResultSet results = state.executeQuery(sql);
	         
	        while (results.next()) {
	        	// build the address
	        	Address shipAddr = new Address(results.getString("UserCompName"),
	        								   results.getString("UserBillAdd1"),
	        								   results.getString("UserBillAdd2"),
	        								   results.getString("UserBillCity"),
	        								   results.getString("ProvName"),
	        								   results.getString("UserBillPC"),
	        								   results.getString("CountryName"));
	        	User newUser = new User(results.getInt("UserID"), 
	        							results.getString("UserCompName"), 
	        							results.getString("UserEmail"), 
	        							results.getString("UserPWD"), 
	        							shipAddr);
	            userList.add(newUser);
	        }
	         
	        results.close();
	        state.close();
	         
	        disconnect();
			return userList;
		} catch (SQLException e) {
        	System.out.println("Database Error - listUsers: " + e.getMessage());
        	return userList;
		}
    }
    
    public boolean deleteUser(User remove){
    	try{
    		String sql = "DELETE FROM Tbl_User where UserID = ?";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, remove.GetID());
    		
    		boolean rowDeleted = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    		return rowDeleted;
    		
    	} catch (SQLException e) {
        	System.out.println("Database Error - deleteUser: " + e.getMessage());
        	return false;
		}
    }
    
    public boolean updateUser(User updateUser){
    	try{
    		String sql = "UPDATE Tbl_User SET UserCompName = ?, UserEmail = ?, UserPWD = ? " + 
    					 "WHERE UserID = ?;";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, updateUser.GetName());
    		state.setString(2, updateUser.GetEmail());
    		state.setString(3, updateUser.GetPasswordHash());
    		state.setInt(4, updateUser.GetID());
    		
    		boolean rowUpdated = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    		return rowUpdated;
    		
    	} catch (SQLException e) {
        	System.out.println("Database Error - updateUser: " + e.getMessage());
        	return false;
		}
    }
    
    public boolean updateAddressForUser(String UserID, Address newAddress){
    	boolean rowUpdated = false;
    	String countryID = null;
    	String provID = null;
    	try{
    		//This is a three step process
    		//1) if the CountryName doesn't exist, insert it
    		String sql = "INSERT INTO Tbl_Country (CountryName) SELECT * FROM (SELECT ?) AS tmp " + 
    					 "WHERE NOT EXISTS (SELECT CountryName FROM Tbl_Country WHERE CountryName = ?) LIMIT 1;";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.GetCountry());
    		state.setString(2, newAddress.GetCountry());
    		state.executeUpdate(); // all that we care about is that we didn't throw an error, both 0 or 1 could be valid returns.
    		
    		//1a)get the CountryID for the Country in question
    		sql = "SELECT CountryID FROM Tbl_Country WHERE CountryName = ? LIMIT 1;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.GetCountry());
    		ResultSet results = state.executeQuery(sql);
	        
	        if (results.next()) {
	        	countryID = results.getString(1);
	        }
    		//2) if  the State (prov) name doesn't exist insert it
	        sql = "INSERT INTO Tbl_Prov (ProvName) SELECT * FROM (SELECT ?) AS tmp " + 
				  "WHERE NOT EXISTS (SELECT ProvName FROM Tbl_Prov WHERE ProvName = ?) LIMIT 1;";
	        state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.GetState());
    		state.setString(2, newAddress.GetState());
    		state.executeUpdate(); // all that we care about is that we didn't throw an error, both 0 or 1 could be valid returns.
    	
    		//2a) get the ProvID for the State in question
    		sql = "SELECT ProvID FROM Tbl_Prov WHERE ProvName = ? LIMIT 1;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.GetState());
    		results = state.executeQuery(sql);
	        
	        if (results.next()) {
	        	provID = results.getString(1);
	        }

    		//3) update the rest of the information in the User table.
    		sql = "UPDATE Tbl_User SET UserBillAdd1 = ?, UserBillAdd2 = ?, UserBillCity = ?, UserBillCountryID = ?, " + 
    			  "UserBillPC = ?, UserBillProvID = ? WHERE UserID = ?";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.GetStreet1());
    		state.setString(2, newAddress.GetStreet2());
    		state.setString(3, newAddress.GetCity());
    		state.setString(4, countryID);
    		state.setString(5, newAddress.GetZip());
    		state.setString(6, provID);
    		state.setString(7, UserID);
    		
    		rowUpdated = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    	} catch (SQLException e) {
        	System.out.println("Database Error - updateAddressForUser: " + e.getMessage());
		}
    	return rowUpdated;
    }
    
    public User getUser(String userID){
    	User newUser = null;
       	try{
    		String sql = "Select Tbl_User.UserID, Tbl_User.UserEmail, Tbl_User.UserPWD, Tbl_User.UserCompName, " +
    					 "Tbl_User.UserBillCity, Tbl_Prov.ProvName, Tbl_User.UserBillPC, Tbl_Country.CountryName " + 
    				     "Tbl_User.UserBillAdd1, Tbl_User.UserBillAdd2, FROM Tbl_User " + 
    					 "LEFT JOIN Tbl_Prov on Tbl_User.UserBillProvID = Tbl_Prov.ProvID " + 
    					 "LEFT JOIN Tbl_Country on Tbl_User.UserBillCountryID = Tbl_Country.CountryID " +
    					 "WHERE Tbl_User.UserID = ?";
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, userID);
    		
	        ResultSet results = state.executeQuery(sql);
	         
	        if (results.next()) {
	        	// build the address
	        	Address shipAddr = new Address(results.getString("UserCompName"),
	        								   results.getString("UserBillAdd1"),
	        								   results.getString("UserBillAdd2"),
	        								   results.getString("UserBillCity"),
	        								   results.getString("ProvName"),
	        								   results.getString("UserBillPC"),
	        								   results.getString("CountryName"));
	        	// create the user
	        	newUser = new User(results.getInt("UserID"), 
	        							results.getString("UserCompName"), 
	        							results.getString("UserEmail"), 
	        							results.getString("UserPWD"), 
	        							shipAddr);
	        }
	        
	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - getUser: " + e.getMessage());
		}
       	return newUser;
    }
    
    public int getUserID(String uEmail){
    	// return the userID for a given email address, or 0 if it doesn't exist.
    	int possibleUserID = 0;
       	try{
    		String sql = "Select TOP 1 UserID FROM Tbl_User WHERE UserEmail = ?";
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, uEmail);
	        ResultSet results = state.executeQuery(sql);
	        
	        if (results.next()) {
	        	possibleUserID = results.getInt("UserID");
	        }
	        
	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - getUserID: " + e.getMessage());
		}
       	return possibleUserID;
    }

// Bid Database CRUD operations:
    public int insertBid(Bid newBid){
    	// insert a bid into the table and return the bid_id of the record
    	int newBidID = 0;
    	try{
    		String sql = "INSERT INTO Tbl_Bid (BidAccept, BidAcceptDate, BidAmount, BidBidderUserID, BidBidderUserShipID, BidDate, " + 
    		 	  "BidProdID, BidQty, BidSellerUserID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setBoolean(1, newBid.GetBidAccepted());
    		state.setString(2, newBid.GetBidAcceptedDate());
    		state.setInt(3, newBid.GetProposedPrice());
    		state.setInt(4, newBid.GetBuyerID());
    		state.setInt(5, newBid.GetBuyerID());
    		state.setString(6, newBid.GetPostedDate());
			state.setInt(7, newBid.GetItemID());
			state.setInt(8, newBid.GetQuantity());
			state.setInt(9, newBid.GetSellerID());
			
			boolean rowUpdated = state.executeUpdate() > 0;
			if(rowUpdated){
				sql = "SELECT LAST_INSERT_ID();";
	    		state = jdbcConnection.prepareStatement(sql);
	    		ResultSet results = state.executeQuery(sql);
	    		if (results.next()) {
	    			newBidID = results.getInt(1);
		        }
			}
			
			state.close();
			disconnect();
		} catch (SQLException e) {
        	System.out.println("Database Error - insertBid: " + e.getMessage());
		}
    	return newBidID;
    }
    
    public List<Bid> listBidsForBidder(int bidderID){
    	List<Bid> bidList = new ArrayList<>();
    	try{
    		String sql = "Select * FROM Tbl_Bid WHERE BidBidderUserID = ?";
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, bidderID);
	        ResultSet results = state.executeQuery(sql);
	        
	        while (results.next()) {
	        	// while we have new results, build the bid list
	        	Bid newBid = new Bid(results.getInt("BidID"),
	        						 results.getInt("BidBidderUserID"),
	        						 results.getInt("BidSellerUserID"),
	        						 results.getInt("BidProdID"),
	        						 results.getString("BidDate"),
	        						 results.getInt("BidQty"),
	        						 results.getInt("BidAmount"), 
	        						 results.getBoolean("BidAccept"),
	        						 results.getString("BidAcceptDate"));
	        	bidList.add(newBid);
	        }

	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - listBidsForBidder: " + e.getMessage());
		}
    	
    	return bidList;
    }
    
    public List<Bid> listBidsForSeller(int sellerID){
    	List<Bid> bidList = new ArrayList<>();
    	try{
    		String sql = "Select * FROM Tbl_Bid WHERE BidSellerUserID = ?";
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, sellerID);
	        ResultSet results = state.executeQuery(sql);
	        
	        while (results.next()) {
	        	// while we have new results, build the bid list
	        	Bid newBid = new Bid(results.getInt("BidID"),
	        						 results.getInt("BidBidderUserID"),
	        						 results.getInt("BidSellerUserID"),
	        						 results.getInt("BidProdID"),
	        						 results.getString("BidDate"),
	        						 results.getInt("BidQty"),
	        						 results.getInt("BidAmount"), 
	        						 results.getBoolean("BidAccept"),
	        						 results.getString("BidAcceptDate"));
	        	bidList.add(newBid);
	        }

	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - listBidsForSeller: " + e.getMessage());
		}
    	
    	return bidList;
    }
    
    public boolean updateBid(Bid updatedBid){
    	try{
    		String sql = "UPDATE Tbl_Bid SET BidAccept = ?, BidAcceptDate = ?, BidAmount = ?, BidBidderUserID = ?, BidBidderUserShipID = ?," + 
    				     "BidDate = ?, BidProdID = ?, BidQty = ?, BidSellerUserID = ? WHERE BidID = ?;";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setBoolean(1, updatedBid.GetBidAccepted());
    		state.setString(2, updatedBid.GetBidAcceptedDate());
    		state.setInt(3, updatedBid.GetProposedPrice());
    		state.setInt(4, updatedBid.GetBuyerID());
    		state.setInt(5, updatedBid.GetBuyerID());
    		state.setString(6, updatedBid.GetPostedDate());
			state.setInt(7, updatedBid.GetItemID());
			state.setInt(8, updatedBid.GetQuantity());
			state.setInt(9, updatedBid.GetSellerID());
			state.setInt(10, updatedBid.GetBidID());
			
    		boolean rowUpdated = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    		return rowUpdated;
    		
    	} catch (SQLException e) {
        	System.out.println("Database Error - updateBid: " + e.getMessage());
        	return false;
		}
    }
    
    public boolean deleteBid(Bid remove){
    	try{
    		String sql = "DELETE FROM Tbl_Bid where BidID = ?";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, remove.GetBidID());
    		
    		boolean rowDeleted = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    		return rowDeleted;
    		
    	} catch (SQLException e) {
        	System.out.println("Database Error - deleteBid: " + e.getMessage());
        	return false;
		}
    }
    
// List Database CRUD operations:
    public int insertListing(Listing newList){
    	// insert a bid into the table and return the bid_id of the record
    	int newListID = 0;
    	try{
    		String sql = "INSERT INTO Tbl_List (List_TDate, ListBaseAmt, ListDate, ListEDate, ListMinQTY, ListProdID) " + 
    					 "VALUES (?, ?, GETDATE(), ?, ?, ?);";
			
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newList.GetTerminationDate());
    		state.setString(2, newList.GetBaseAmount());
    		state.setString(3, newList.GetEffictiveDate());
    		state.setInt(4, newList.GetMinAmmount());
			state.setInt(5, newList.GetProdID());
			
			boolean rowUpdated = state.executeUpdate() > 0;
			if(rowUpdated){
				sql = "SELECT LAST_INSERT_ID();";
	    		state = jdbcConnection.prepareStatement(sql);
	    		ResultSet results = state.executeQuery(sql);
	    		if (results.next()) {
	    			newListID = results.getInt(1);
		        }
			}
			
			state.close();
			disconnect();
		} catch (SQLException e) {
        	System.out.println("Database Error - insertListing: " + e.getMessage());
		}
    	return newListID;
    }
    
    public List<Listing> listAllActiveListing(){
    	List<Listing> listingList = new ArrayList<>();
    	try{
    		String sql = "Select * FROM Tbl_List WHERE ListEDate <= GETDATE() AND GETDATE() <= List_TDate;";
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
	        ResultSet results = state.executeQuery(sql);
	        
	        while (results.next()) {
	        	// while we have new results, build the bid list
	        	Listing newList = new Listing(results.getInt("ListID"),
	        						 results.getInt("ListProdID"),
	        						 results.getString("ListBaseAmt"),
	        						 results.getInt("ListMinQTY"),
	        						 results.getString("ListDate"),
	        						 results.getString("ListEDate"),
	        						 results.getString("List_TDate"));
	        	listingList.add(newList);
	        }

	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - listAllActiveListing: " + e.getMessage());
		}
    	return listingList;
    }
    
    
/*    public List<Listing> listAllUserListing(int userID){
    	List<Listing> listingList = new ArrayList<>();
    	try{
    	// userID -> gets list of products, products gets list of Listings.
    		String sql = "Select ProdID FROM Tbl_Prod WHERE ProdSubmitUserID = ?;";
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
	        ResultSet prodResults = state.executeQuery(sql);
			
			
			
    		
    		sql = "Select * FROM Tbl_List WHERE ...;";
    		state = jdbcConnection.prepareStatement(sql);
	        ResultSet results = state.executeQuery(sql);
	        
	        while (results.next()) {
	        	// while we have new results, build the bid list
	        	Listing newList = new Listing(results.getInt("ListID"),
	        						 results.getInt("ListProdID"),
	        						 results.getString("ListBaseAmt"),
	        						 results.getInt("ListMinQTY"),
	        						 results.getString("ListDate"),
	        						 results.getString("ListEDate"),
	        						 results.getString("List_TDate"));
	        	listingList.add(newList);
	        }

	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - listAllUserListing: " + e.getMessage());
		}
    	return listingList;
    }
*/
  
    public boolean updateListing(Listing updateList){
    	try{
    		String sql = "UPDATE Tbl_List SET List_TDate = ?, ListBaseAmt = ?, ListDate = ?, ListEDate = ?, ListMinQTY = ?," + 
    				     "ListProdID = ? WHERE ListID = ?;";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, updateList.GetTerminationDate());
    		state.setString(2, updateList.GetBaseAmount());
    		state.setString(3, updateList.GetListDate());
    		state.setString(4, updateList.GetEffictiveDate());
    		state.setInt(5, updateList.GetMinAmmount());
    		state.setInt(6, updateList.GetProdID());
    		state.setInt(7, updateList.GetListID());
			
    		boolean rowUpdated = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    		return rowUpdated;
    		
    	} catch (SQLException e) {
        	System.out.println("Database Error - updateListing: " + e.getMessage());
        	return false;
		}
    	
    }
    
    public boolean deleteListing(Listing remove){
    	try{
    		String sql = "DELETE FROM Tbl_List where ListID = ?";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, remove.GetListID());
    		
    		boolean rowDeleted = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    		return rowDeleted;
    		
    	} catch (SQLException e) {
        	System.out.println("Database Error - deleteListing: " + e.getMessage());
        	return false;
		}
    }

// Prod Database CRUD operations:
    public int insertProduct(Item newItem){
    	int newItemID = 0;
    	try{
    		String sql = "INSERT INTO Tbl_Prod (ProdDesc, ProdKeywords, ProdName, ProdQTY, ProdSubmitDate, ProdSubmitUserID) " + 
    					 "VALUES (?, ?, ?, ?, GETDATE(), ?);";
			
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newItem.GetDescription());
    		state.setString(2, newItem.GetKeywords());
    		state.setString(3, newItem.GetName());
    		state.setInt(4, newItem.GetQuantityInStock());
			state.setInt(5, newItem.GetSellerID());
			
			boolean rowUpdated = state.executeUpdate() > 0;
			if(rowUpdated){
				sql = "SELECT LAST_INSERT_ID();";
	    		state = jdbcConnection.prepareStatement(sql);
	    		ResultSet results = state.executeQuery(sql);
	    		if (results.next()) {
	    			newItemID = results.getInt(1);
		        }
			}
			
			state.close();
			disconnect();
		} catch (SQLException e) {
        	System.out.println("Database Error - insertListing: " + e.getMessage());
		}
    	return newItemID;
    }
    
    

}
