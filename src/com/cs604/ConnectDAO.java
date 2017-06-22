package com.cs604;

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
	private static ConnectDAO singletonDAO = null;
	
	protected ConnectDAO(String URL, String Username, String password){
		//protected so we don't allow instantiation
		jdbcURL = URL;
		jdbcUsername = Username;
		jdbcPassword = password;
	}
	
	public static ConnectDAO getInstance(String URL, String Username, String password){
		if(singletonDAO == null){
			singletonDAO = new ConnectDAO(URL, Username, password);
		}
		return singletonDAO;
	}
	
	protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
        	//System.out.println("Database - attempting connection to: " + jdbcURL +" username: "+jdbcUsername+"  password: "+jdbcPassword);
            jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        	//System.out.println("Database - we should be connected");

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
    	int countryID = 0;
    	int provID = 0;
    	try{
    		Address billAddress = newUser.getBillingAddress();
    		//This is a three step process
    		
    		String msg = "Database - insert user called with:\ncompany name: "+newUser.getName();
    		msg += "\nseller bit: "+newUser.getSellerFlag();
    		msg += "\nbuyer bit: "+newUser.getBuyerFlag();
    		msg += "\nShip-bill bit: "+newUser.getShippingIsBilling();

        	System.out.println(msg);

    		
    		connect();
    		
    		//1) if the CountryName doesn't exist, insert it
    		String sql = "IF NOT EXISTS (SELECT TOP 1 CountryID FROM Tbl_Country WHERE CountryName = ?) " + 
    					 "INSERT INTO Tbl_Country (CountryName) VALUES (?);";
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, billAddress.getCountry());
    		state.setString(2, billAddress.getCountry());
        	//System.out.println("Database - pre country sql query");
    		state.executeUpdate(); // all that we care about is that we didn't throw an error, both 0 or 1 could be valid returns.
        	//System.out.println("Database - post country sql query");
    		
    		//1a)get the CountryID for the Country in question
    		sql = "SELECT TOP 1 CountryID FROM Tbl_Country WHERE CountryName = ?;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, billAddress.getCountry());
        	//System.out.println("Database - pre countryID sql query");
    		ResultSet results = state.executeQuery();
        	//System.out.println("Database - post countryID sql query");
	        
	        if (results.next()) {
	        	countryID = results.getInt(1);
	        }
	        
    		//2) if  the State (prov) name doesn't exist insert it
	        sql = "IF NOT EXISTS (SELECT TOP 1 ProvID FROM Tbl_Prov WHERE ProvName = ?) " + 
	        	  "INSERT INTO Tbl_Prov (ProvName) VALUES (?);";
	        state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, billAddress.getState());
    		state.setString(2, billAddress.getState());
    		state.executeUpdate(); // all that we care about is that we didn't throw an error, both 0 or 1 could be valid returns.
    	
    		//2a) get the ProvID for the State in question
    		sql = "SELECT TOP 1 ProvID FROM Tbl_Prov WHERE ProvName = ?;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, billAddress.getState());
    		results = state.executeQuery();
	        
	        if (results.next()) {
	        	provID = results.getInt(1);
	        }
			
    		sql = "INSERT INTO Tbl_User (UserBillAdd1, UserBillAdd2, UserBillCity, UserBillCountryID, UserBillPC, UserBillProvID, " + 
    		 	  "UserCompName, UserEmail, UserPWD, UserBuyerBIt, UserSellerBit, UserShipSame) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
			state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, billAddress.getStreet1());
    		state.setString(2, billAddress.getStreet2());
    		state.setString(3, billAddress.getCity());
    		state.setInt(4, countryID);
    		state.setString(5, billAddress.getZip());
    		state.setInt(6, provID);
			state.setString(7, newUser.getName());
			state.setString(8, newUser.getEmail());
			state.setString(9, newUser.getPasswordHash());
			state.setBoolean(10, newUser.getBuyerFlag());
			state.setBoolean(11, newUser.getSellerFlag());
			state.setBoolean(12, newUser.getShippingIsBilling());
			
			boolean rowUpdated = state.executeUpdate() > 0;
			if(rowUpdated){
				sql = "SELECT TOP 1 UserID FROM Tbl_User WHERE UserEmail = ?";
	    		state = jdbcConnection.prepareStatement(sql);
	    		state.setString(1, newUser.getEmail());
	    		results = state.executeQuery();
	    		
	    		if (results.next()) {
		        	newUserID = results.getInt(1);
		        }
	    		
		        System.out.println("Database - insertUser: newUserID = " + newUserID);
			}
			
			// also insert their shipping address
			Address shipAddress = newUser.getShippingAddress();
    		sql = "IF NOT EXISTS (SELECT TOP 1 CountryID FROM Tbl_Country WHERE CountryName = ?) " + 
				  "INSERT INTO Tbl_Country (CountryName) VALUES (?);";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, shipAddress.getCountry());
    		state.setString(2, shipAddress.getCountry());
    		state.executeUpdate(); // all that we care about is that we didn't throw an error, both 0 or 1 could be valid returns.
		
    		//1a)get the CountryID for the Country in question
    		sql = "SELECT TOP 1 CountryID FROM Tbl_Country WHERE CountryName = ?;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, shipAddress.getCountry());
    		results = state.executeQuery();
       
    		if (results.next()) {
    			countryID = results.getInt(1);
    		}
       
    		//2) if  the State (prov) name doesn't exist insert it
    		sql = "IF NOT EXISTS (SELECT TOP 1 ProvID FROM Tbl_Prov WHERE ProvName = ?) " + 
    			  "INSERT INTO Tbl_Prov (ProvName) VALUES (?);";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, shipAddress.getState());
    		state.setString(2, shipAddress.getState());
    		state.executeUpdate(); // all that we care about is that we didn't throw an error, both 0 or 1 could be valid returns.
	
    		//2a) get the ProvID for the State in question
    		sql = "SELECT TOP 1 ProvID FROM Tbl_Prov WHERE ProvName = ?;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, shipAddress.getState());
    		results = state.executeQuery();
       
    		if (results.next()) {
    			provID = results.getInt(1);
    		}

    		sql = "INSERT INTO Tbl_UserShip (UserShipUserID, UserShipAdd1, UserShipAdd2, UserShipCity, UserShipCountryID, UserShipPC, UserShipProvID)" + 
      		 	  "VALUES (?, ?, ?, ?, ?, ?, ?);";
  			
  			state = jdbcConnection.prepareStatement(sql);
      		state.setInt(1, newUserID);
      		state.setString(2, shipAddress.getStreet1());
      		state.setString(3, shipAddress.getStreet2());
      		state.setString(4, shipAddress.getCity());
      		state.setInt(5, countryID);
      		state.setString(6, shipAddress.getZip());
      		state.setInt(7, provID);

      		if(state.executeUpdate() > 0){
            	System.out.println("Database InsertUser - shipping address updated as well");
      		}else{
            	System.out.println("Database Error InsertUser - shipping address failed");
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
    				     "Tbl_User.UserBillAdd1, Tbl_User.UserBillAdd2, Tbl_User.UserBuyerBIt, Tbl_User.UserSellerBit, " + 
    				     "Tbl_User.UserShipSame, FROM Tbl_User " + 
    					 "LEFT JOIN Tbl_Prov on Tbl_User.UserBillProvID = Tbl_Prov.ProvID " + 
    					 "LEFT JOIN Tbl_Country on Tbl_User.UserBillCountryID = Tbl_Country.CountryID;";
			connect();
    	
			Statement state = jdbcConnection.createStatement();
	        ResultSet results = state.executeQuery(sql);
	         
	        while (results.next()) {
	        	int userID = results.getInt("UserID");
	        	String compName = results.getString("UserCompName");
	        	// build the address
	        	Address billAddr = new Address(compName,
	        								   results.getString("UserBillAdd1"),
	        								   results.getString("UserBillAdd2"),
	        								   results.getString("UserBillCity"),
	        								   results.getString("ProvName"),
	        								   results.getString("UserBillPC"),
	        								   results.getString("CountryName"));
	        	Address shipAddr = null;
	        	if(!results.getBoolean("UserShipSame")){
	        		// shipping is different than billing, so fetch the shipping address
	        		String shipSql = "SELECT Tbl_UserShip.UserShipAdd1, Tbl_UserShip.UserShipAdd2, Tbl_UserShip.UserShipCity, " +
	        				"Tbl_UserShip.UserShipCountryID, Tbl_UserShip.UserShipID, Tbl_UserShip.UserShipPC, Tbl_UserShip.UserShipProvID, " +
	        				"Tbl_UserShip.UserShipUserID FROM Tbl_UserShip WHERE UserShipUserID = ?";
	        		
	        		PreparedStatement shipState = jdbcConnection.prepareStatement(shipSql);
	        		shipState.setInt(1, userID);
	    	        ResultSet shipResults = shipState.executeQuery();
	    	        
	    	        if(shipResults.next()){
	    	        	shipAddr = new Address(compName,
	    	        						   shipResults.getString("UserShipAdd1"),
	    	        						   shipResults.getString("UserShipAdd2"),
	    	        						   shipResults.getString("UserShipCity"),
	    	        						   shipResults.getString("ProvName"),
	    	        						   shipResults.getString("UserBillPC"),
	    	   								   shipResults.getString("CountryName"));
	    	        }
	        	}

	        	User newUser = new User(userID, 
	        							results.getString("UserCompName"), 
	        							results.getString("UserEmail"), 
	        							results.getString("UserPWD"), 
	        							billAddr, shipAddr, 
	        							results.getBoolean("UserBuyerBIt"),
	        							results.getBoolean("UserSellerBit"),
	        							results.getBoolean("UserShipSame"));
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
    		state.setInt(1, remove.getID());
    		
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
    		String sql = "UPDATE Tbl_User SET UserCompName = ?, UserEmail = ?, UserPWD = ?, UserBuyerBIt = ?, UserSellerBIt = ?, UserShipSame = ? WHERE UserID = ?;";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, updateUser.getName());
    		state.setString(2, updateUser.getEmail());
    		state.setString(3, updateUser.getPasswordHash());
    		state.setBoolean(4, updateUser.getBuyerFlag());
    		state.setBoolean(5, updateUser.getSellerFlag());
    		state.setBoolean(6, updateUser.getShippingIsBilling());
    		state.setInt(7, updateUser.getID());
    		
    		boolean rowUpdated = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    		return rowUpdated;
    		
    	} catch (SQLException e) {
        	System.out.println("Database Error - updateUser: " + e.getMessage());
        	return false;
		}
    }
    
    public boolean updateBillAddressForUser(int UserID, Address newAddress){
    	boolean rowUpdated = false;
    	String countryID = null;
    	String provID = null;
    	try{
    		//This is a three step process
    		//1) if the CountryName doesn't exist, insert it
    		String sql = "IF NOT EXISTS (SELECT TOP 1 CountryName FROM Tbl_Country WHERE CountryName = ?) " + 
    					 "INSERT INTO Tbl_Country (CountryName) VALUES (?);";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.getCountry());
    		state.setString(2, newAddress.getCountry());
    		state.executeUpdate(); // all that we care about is that we didn't throw an error, both 0 or 1 could be valid returns.
    		
    		//1a)get the CountryID for the Country in question
    		sql = "SELECT TOP 1 CountryID FROM Tbl_Country WHERE CountryName = ?;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.getCountry());
    		ResultSet results = state.executeQuery();
	        
	        if (results.next()) {
	        	countryID = results.getString(1);
	        }
    		//2) if  the State (prov) name doesn't exist insert it
	        sql = "IF NOT EXISTS (SELECT TOP 1 ProvID FROM Tbl_Prov WHERE ProvName = ?) " + 
		          "INSERT INTO Tbl_Prov (ProvName) VALUES (?);";
	        state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.getState());
    		state.setString(2, newAddress.getState());
    		state.executeUpdate(); // all that we care about is that we didn't throw an error, both 0 or 1 could be valid returns.
    	
    		//2a) get the ProvID for the State in question
    		sql = "SELECT TOP 1 ProvID FROM Tbl_Prov WHERE ProvName = ?;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.getState());
    		results = state.executeQuery();
	        
	        if (results.next()) {
	        	provID = results.getString(1);
	        }

    		//3) update the rest of the information in the User table.
    		sql = "UPDATE Tbl_User SET UserBillAdd1 = ?, UserBillAdd2 = ?, UserBillCity = ?, UserBillCountryID = ?, " + 
    			  "UserBillPC = ?, UserBillProvID = ? WHERE UserID = ?";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.getStreet1());
    		state.setString(2, newAddress.getStreet2());
    		state.setString(3, newAddress.getCity());
    		state.setString(4, countryID);
    		state.setString(5, newAddress.getZip());
    		state.setString(6, provID);
    		state.setInt(7, UserID);
    		
    		rowUpdated = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    	} catch (SQLException e) {
        	System.out.println("Database Error - updateAddressForUser: " + e.getMessage());
		}
    	return rowUpdated;
    }
    
    public User getUser(int userID){
    	User newUser = null;
       	try{
    		String sql = "Select Tbl_User.UserID, Tbl_User.UserEmail, Tbl_User.UserPWD, Tbl_User.UserCompName, Tbl_User.UserBillCity, " +
    					 "Tbl_Prov.ProvName, Tbl_User.UserBillPC, Tbl_Country.CountryName, Tbl_User.UserBillAdd1, Tbl_User.UserBillAdd2, " + 
    				     "Tbl_User.UserBuyerBIt, Tbl_User.UserSellerBit, Tbl_User.UserShipSame FROM Tbl_User " + 
    					 "LEFT JOIN Tbl_Prov on Tbl_User.UserBillProvID = Tbl_Prov.ProvID " + 
    					 "LEFT JOIN Tbl_Country on Tbl_User.UserBillCountryID = Tbl_Country.CountryID " +
    					 "WHERE Tbl_User.UserID = ?";
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, userID);
    		
	        ResultSet results = state.executeQuery();
	        if (results.next()) {
	        	// build the billing address
	        	String companyName = results.getString("UserCompName");
	        	Address billAddr = new Address(companyName,
	        								   results.getString("UserBillAdd1"),
	        								   results.getString("UserBillAdd2"),
	        								   results.getString("UserBillCity"),
	        								   results.getString("ProvName"),
	        								   results.getString("UserBillPC"),
	        								   results.getString("CountryName"));
	        	// possibly build the shipping address
	        	boolean billIsShip = results.getBoolean("UserShipSame");
	        	Address shipAddr = null;
	        	if(!billIsShip){
	        		String shipSql = "SELECT Tbl_UserShip.*, Tbl_Country.CountryName, Tbl_Prov.ProvName " +
	        				"FROM Tbl_UserShip LEFT JOIN Tbl_Country on Tbl_UserShip.UserShipCountryID = Tbl_Country.CountryID " +
	        				"LEFT JOIN Tbl_Prov on Tbl_UserShip.UserShipProvID = Tbl_Prov.ProvID WHERE UserShipUserID = ?";
	        		
	        		PreparedStatement shipState = jdbcConnection.prepareStatement(shipSql);
	        		shipState.setInt(1, userID);
	    	        ResultSet shipResults = shipState.executeQuery();
	    	        
	    	        if(shipResults.next()){
	    	        	shipAddr = new Address(companyName,
	    	        						   shipResults.getString("UserShipAdd1"),
	    	        						   shipResults.getString("UserShipAdd2"),
	    	        						   shipResults.getString("UserShipCity"),
	    	        						   shipResults.getString("ProvName"),
	    	        						   shipResults.getString("UserShipPC"),
	    	   								   shipResults.getString("CountryName"));
	    	        }
	        	}
	        	// create the user
	        	newUser = new User(userID, 
	        					   results.getString("UserCompName"), 
	        					   results.getString("UserEmail"), 
	        					   results.getString("UserPWD"), 
	        					   billAddr, shipAddr,
	        					   results.getBoolean("UserBuyerBIt"),
	        					   results.getBoolean("UserSellerBit"),
	        					   billIsShip);
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
	        ResultSet results = state.executeQuery();
	        
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
    
// Crud operations for UserShip table
    public int insertUserShip(User updateUser){
    	int newShipID = 0;
    	int countryID = 0;
    	int provID = 0;
    	Address shipAddr = updateUser.getShippingAddress();
    	try{
    		//This is a three step process
    		
    		//1) if the CountryName doesn't exist, insert it
    		String sql = "IF NOT EXISTS (SELECT TOP 1 CountryID FROM Tbl_Country WHERE CountryName = ?) " + 
    					 "INSERT INTO Tbl_Country (CountryName) VALUES (?);";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, shipAddr.getCountry());
    		state.setString(2, shipAddr.getCountry());
    		state.executeUpdate(); // all that we care about is that we didn't throw an error, both 0 or 1 could be valid returns.
    		
    		//1a)get the CountryID for the Country in question
    		sql = "SELECT TOP 1 CountryID FROM Tbl_Country WHERE CountryName = ?;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, shipAddr.getCountry());
    		ResultSet results = state.executeQuery();
	        
	        if (results.next()) {
	        	countryID = results.getInt(1);
	        }
	        
    		//2) if  the State (prov) name doesn't exist insert it
	        sql = "IF NOT EXISTS (SELECT TOP 1 ProvID FROM Tbl_Prov WHERE ProvName = ?) " + 
	        	  "INSERT INTO Tbl_Prov (ProvName) VALUES (?);";
	        state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, shipAddr.getState());
    		state.setString(2, shipAddr.getState());
    		state.executeUpdate(); // all that we care about is that we didn't throw an error, both 0 or 1 could be valid returns.
    	
    		//2a) get the ProvID for the State in question
    		sql = "SELECT TOP 1 ProvID FROM Tbl_Prov WHERE ProvName = ?;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, shipAddr.getState());
    		results = state.executeQuery();
	        
	        if (results.next()) {
	        	provID = results.getInt(1);
	        }
			
    		sql = "INSERT INTO Tbl_UserShip (UserShipAdd1, UserShipAdd2, UserShipCity, UserShipCountryID, UserShipPC, UserShipProvID, " + 
    		 	  "UserShipUserID) VALUES (?, ?, ?, ?, ?, ?, ?);";
			
			state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, shipAddr.getStreet1());
    		state.setString(2, shipAddr.getStreet2());
    		state.setString(3, shipAddr.getCity());
    		state.setInt(4, countryID);
    		state.setString(5, shipAddr.getZip());
    		state.setInt(6, provID);
			state.setInt(7, updateUser.getID());
			
			boolean rowUpdated = state.executeUpdate() > 0;
			if(rowUpdated){
				sql = "SELECT TOP 1 UserID FROM Tbl_UserShip WHERE UserShipUserID = ?";
	    		state = jdbcConnection.prepareStatement(sql);
	    		state.setInt(1, updateUser.getID());
	    		results = state.executeQuery();
	    		
	    		if (results.next()) {
		        	newShipID = results.getInt(1);
		        }
	    		
		        System.out.println("Database - insertUserShip: newShipID = " + newShipID);
			}
			
			state.close();
			disconnect();
		} catch (SQLException e) {
        	System.out.println("Database Error - insertUserShip: " + e.getMessage());
		}    	
    	return newShipID;
    }
    
    public boolean updateShipAddressForUser(int UserID, Address newAddress){
    	boolean rowUpdated = false;
    	String countryID = null;
    	String provID = null;
    	try{
    		//This is a three step process
    		//1) if the CountryName doesn't exist, insert it
    		String sql = "IF NOT EXISTS (SELECT TOP 1 CountryName FROM Tbl_Country WHERE CountryName = ?) " + 
    					 "INSERT INTO Tbl_Country (CountryName) VALUES (?);";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.getCountry());
    		state.setString(2, newAddress.getCountry());
    		state.executeUpdate(); // all that we care about is that we didn't throw an error, both 0 or 1 could be valid returns.
    		
    		//1a)get the CountryID for the Country in question
    		sql = "SELECT TOP 1 CountryID FROM Tbl_Country WHERE CountryName = ?;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.getCountry());
    		ResultSet results = state.executeQuery();
	        
	        if (results.next()) {
	        	countryID = results.getString(1);
	        }
    		//2) if  the State (prov) name doesn't exist insert it
	        sql = "IF NOT EXISTS (SELECT TOP 1 ProvID FROM Tbl_Prov WHERE ProvName = ?) " + 
		          "INSERT INTO Tbl_Prov (ProvName) VALUES (?);";
	        state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.getState());
    		state.setString(2, newAddress.getState());
    		state.executeUpdate(); // all that we care about is that we didn't throw an error, both 0 or 1 could be valid returns.
    	
    		//2a) get the ProvID for the State in question
    		sql = "SELECT TOP 1 ProvID FROM Tbl_Prov WHERE ProvName = ?;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.getState());
    		results = state.executeQuery();
	        
	        if (results.next()) {
	        	provID = results.getString(1);
	        }

    		//3) update the rest of the information in the UserShip table.
    		sql = "UPDATE Tbl_UserShip SET UserShipAdd1 = ?, UserShipAdd2 = ?, UserShipCity = ?, UserShipCountryID = ?, " + 
    			  "UserShipPC = ?, UserShipProvID = ? WHERE UserShipUserID = ?";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newAddress.getStreet1());
    		state.setString(2, newAddress.getStreet2());
    		state.setString(3, newAddress.getCity());
    		state.setString(4, countryID);
    		state.setString(5, newAddress.getZip());
    		state.setString(6, provID);
    		state.setInt(7, UserID);
    		
    		rowUpdated = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    	} catch (SQLException e) {
        	System.out.println("Database Error - updateShipAddressForUser: " + e.getMessage());
		}
    	return rowUpdated;
    }
    
    public boolean userHasShipAddress(int userID){
    	boolean result = false;
       	try{
    		String sql = "Select TOP 1 UserShipUserID FROM Tbl_UserShip WHERE UserShipUserID = ?";
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, userID);
	        ResultSet results = state.executeQuery();
	        
	        if (results.next()) {
	        	result = true;
	        }
	        
	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - userHasShipAddress: " + e.getMessage());
		}
    	return result;
    }
    
    public boolean deleteUserShipAddress(int userID){
    	try{
    		String sql = "DELETE FROM Tbl_UserShip where UserShipUserID = ?";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, userID);
    		
    		boolean rowDeleted = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    		return rowDeleted;
    		
    	} catch (SQLException e) {
        	System.out.println("Database Error - deleteUserShip: " + e.getMessage());
        	return false;
		}
    }


    
// Bid Database CRUD operations:
    public int insertBid(Bid newBid){
    	// insert a bid into the table and return the bid_id of the record
    	int newBidID = 0;
    	int userShipID = 0;
    	try{
    		String sql = "SELECT TOP 1 UserShipID FROM Tbl_UserShip WHERE UserShipUserID = ?";
     		connect();
     		
     		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, newBid.getBuyerID());
	        ResultSet results = state.executeQuery();
	        
	        if (results.next()) {
	        	userShipID = results.getInt("UserShipID");
	        }
    		
    		sql = "INSERT INTO Tbl_Bid (BidAccept, BidAcceptDate, BidAmount, BidBidderUserID, BidBidderUserShipID, BidDate, " + 
    		 	  "BidProdID, BidQty, BidSellerUserID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setBoolean(1, newBid.getBidAccepted());
    		state.setString(2, newBid.getBidAcceptedDate());
    		state.setDouble(3, newBid.getProposedPrice());
    		state.setInt(4, newBid.getBuyerID());
    		state.setInt(5, userShipID);
    		state.setString(6, newBid.getPostedDate());
			state.setInt(7, newBid.getItemID());
			state.setInt(8, newBid.getQuantity());
			state.setInt(9, newBid.getSellerID());
			
			boolean rowUpdated = state.executeUpdate() > 0;
			if(rowUpdated){
				sql = "SELECT @@IDENTITY;";
	    		state = jdbcConnection.prepareStatement(sql);
	    		results = state.executeQuery();
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
    		String sql = "Select Tbl_Bid.*, Tbl_User.UserCompName, Tbl_Prod.ProdName FROM Tbl_Bid LEFT JOIN Tbl_User on Tbl_Bid.BidSellerUserID = Tbl_User.UserID " + 
    					 "LEFT JOIN Tbl_Prod on Tbl_Bid.BidProdID = Tbl_Prod.ProdID WHERE BidBidderUserID = ?;";
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, bidderID);
	        ResultSet results = state.executeQuery();
	        
	        while (results.next()) {
	        	// while we have new results, build the bid list
	        	Bid newBid = new Bid(results.getInt("BidID"),
	        						 results.getInt("BidBidderUserID"),
	        						 "shouldn't display",
	        						 results.getInt("BidSellerUserID"),
	        						 results.getString("UserCompName"),
	        						 results.getInt("BidProdID"),
	        						 results.getString("ProdName"),
	        						 results.getString("BidDate"),
	        						 results.getInt("BidQty"),
	        						 results.getDouble("BidAmount"), 
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
    
    public Bid getBidForBidder(int bidderID, int bidID){
    	Bid myBid = null;
    	try{
    		String sql = "Select Tbl_Bid.*, Tbl_User.UserCompName, Tbl_Prod.ProdName FROM Tbl_Bid LEFT JOIN Tbl_User on Tbl_Bid.BidSellerUserID = Tbl_User.UserID " + 
    					 "LEFT JOIN Tbl_Prod on Tbl_Bid.BidProdID = Tbl_Prod.ProdID WHERE BidBidderUserID = ? AND BidID = ?;";
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, bidderID);
    		state.setInt(2, bidID);
	        ResultSet results = state.executeQuery();
	        
	        if (results.next()) {
	        	// while we have new results, build the bid list
	        	myBid = new Bid(results.getInt("BidID"),
	        						 results.getInt("BidBidderUserID"),
	        						 "shouldn't display",
	        						 results.getInt("BidSellerUserID"),
	        						 results.getString("UserCompName"),
	        						 results.getInt("BidProdID"),
	        						 results.getString("ProdName"),
	        						 results.getString("BidDate"),
	        						 results.getInt("BidQty"),
	        						 results.getDouble("BidAmount"), 
	        						 results.getBoolean("BidAccept"),
	        						 results.getString("BidAcceptDate"));
	        }

	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - getBidForBidder: " + e.getMessage());
		}
    	
    	return myBid;
    }

    public Bid getBidForSeller(int sellerID, int bidID){
    	Bid myBid = null;
    	try{
    		String sql = "Select Tbl_Bid.*, Tbl_User.UserCompName, Tbl_Prod.ProdName FROM Tbl_Bid LEFT JOIN Tbl_User on Tbl_Bid.BidSellerUserID = Tbl_User.UserID " + 
    					 "LEFT JOIN Tbl_Prod on Tbl_Bid.BidProdID = Tbl_Prod.ProdID WHERE BidSellerUserID = ? AND BidID = ?;";
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, sellerID);
    		state.setInt(2, bidID);
	        ResultSet results = state.executeQuery();
	        
	        if (results.next()) {
	        	// while we have new results, build the bid list
	        	myBid = new Bid(results.getInt("BidID"),
	        						 results.getInt("BidBidderUserID"),
	        						 "shouldn't display",
	        						 results.getInt("BidSellerUserID"),
	        						 results.getString("UserCompName"),
	        						 results.getInt("BidProdID"),
	        						 results.getString("ProdName"),
	        						 results.getString("BidDate"),
	        						 results.getInt("BidQty"),
	        						 results.getDouble("BidAmount"), 
	        						 results.getBoolean("BidAccept"),
	        						 results.getString("BidAcceptDate"));
	        }

	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - getBidForBidder: " + e.getMessage());
		}
    	
    	return myBid;
    }

    
    public List<Bid> listBidsForSeller(int sellerID){
    	List<Bid> bidList = new ArrayList<>();
    	try{
    		String sql = "Select Tbl_Bid.*, Tbl_User.UserCompName, Tbl_Prod.ProdName FROM Tbl_Bid LEFT JOIN Tbl_User on Tbl_Bid.BidBidderUserID = Tbl_User.UserID " + 
    					 "LEFT JOIN Tbl_Prod on Tbl_Bid.BidProdID = Tbl_Prod.ProdID WHERE BidSellerUserID = ?";
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, sellerID);
	        ResultSet results = state.executeQuery();
	        
	        while (results.next()) {
	        	// while we have new results, build the bid list
	        	Bid newBid = new Bid(results.getInt("BidID"),
	        						 results.getInt("BidBidderUserID"),
	        						 results.getString("UserCompName"),
	        						 results.getInt("BidSellerUserID"),
	        						 "shouldn't display",
	        						 results.getInt("BidProdID"),
	        						 results.getString("ProdName"),
	        						 results.getString("BidDate"),
	        						 results.getInt("BidQty"),
	        						 results.getDouble("BidAmount"), 
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
    	int userShipID = 0;
    	try{
    		
    		String sql = "SELECT TOP 1 UserShipID FROM Tbl_UserShip WHERE UserShipUserID = ?";
     		connect();
     		
     		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, updatedBid.getBuyerID());
	        ResultSet results = state.executeQuery();
	        
	        if (results.next()) {
	        	userShipID = results.getInt("UserShipID");
	        }

    		
    		sql = "UPDATE Tbl_Bid SET BidAccept = ?, BidAcceptDate = ?, BidAmount = ?, BidBidderUserID = ?, BidBidderUserShipID = ?," + 
    				     "BidDate = ?, BidProdID = ?, BidQty = ?, BidSellerUserID = ? WHERE BidID = ?;";
    		state = jdbcConnection.prepareStatement(sql);
    		state.setBoolean(1, updatedBid.getBidAccepted());
    		state.setString(2, updatedBid.getBidAcceptedDate());
    		state.setDouble(3, updatedBid.getProposedPrice());
    		state.setInt(4, updatedBid.getBuyerID());
    		state.setInt(5, userShipID);
    		state.setString(6, updatedBid.getPostedDate());
			state.setInt(7, updatedBid.getItemID());
			state.setInt(8, updatedBid.getQuantity());
			state.setInt(9, updatedBid.getSellerID());
			state.setInt(10, updatedBid.getBidID());
			
    		boolean rowUpdated = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    		return rowUpdated;
    		
    	} catch (SQLException e) {
        	System.out.println("Database Error - updateBid: " + e.getMessage());
        	return false;
		}
    }
    
    public boolean deleteBid(int bidID){
    	try{
    		String sql = "DELETE FROM Tbl_Bid where BidID = ?";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, bidID);
    		
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
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newList.getTerminationDate());
    		state.setDouble(2, newList.getBaseCost());
    		state.setString(3, newList.getEffectiveDate());
    		state.setInt(4, newList.getMinAmount());
			state.setInt(5, newList.getProdID());
			
			boolean rowUpdated = state.executeUpdate() > 0;
			if(rowUpdated){
				sql = "SELECT ListID FROM Tbl_List WHERE List_TDate = ? AND ListBaseAmt = ? AND " + 
					  "ListEDate = ? AND ListMinQTY = ? AND ListProdID = ?;";
	    		state = jdbcConnection.prepareStatement(sql);
	    		state.setString(1, newList.getTerminationDate());
	    		state.setDouble(2, newList.getBaseCost());
	    		state.setString(3, newList.getEffectiveDate());
	    		state.setInt(4, newList.getMinAmount());
				state.setInt(5, newList.getProdID());
	    		ResultSet results = state.executeQuery();
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
    		String sql = "Select Tbl_List.*, Tbl_Prod.ProdName FROM Tbl_List LEFT JOIN Tbl_Prod on Tbl_List.ListProdID = Tbl_Prod.ProdID " + 
    					 "WHERE ListEDate <= GETDATE() AND GETDATE() <= List_TDate;";
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
	        ResultSet results = state.executeQuery();
	        
	        while (results.next()) {
	        	// while we have new results, build the bid list
	        	Listing newList = new Listing(results.getInt("ListID"),
	        						 results.getInt("ListProdID"),
	        						 results.getString("ProdName"),
	        						 results.getDouble("ListBaseAmt"),
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
    
    public Listing getActiveListing(int listID){
    	// pull an active listing for a bidder
    	Listing myListing = null;
    	try{
    		String sql = "Select Tbl_List.*, Tbl_Prod.ProdName FROM Tbl_List LEFT JOIN Tbl_Prod on Tbl_List.ListProdID = Tbl_Prod.ProdID " + 
    					 "WHERE ListEDate <= GETDATE() AND GETDATE() <= List_TDate AND ListID = ?;";
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, listID);
    		ResultSet results = state.executeQuery();
	        
	        if (results.next()) {
	        	// while we have new results, build the bid list
	        	myListing = new Listing(results.getInt("ListID"),
	        						 results.getInt("ListProdID"),
	        						 results.getString("ProdName"),
	        						 results.getDouble("ListBaseAmt"),
	        						 results.getInt("ListMinQTY"),
	        						 results.getString("ListDate"),
	        						 results.getString("ListEDate"),
	        						 results.getString("List_TDate"));
	        }

	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - listAllActiveListing: " + e.getMessage());
		}
    	return myListing;
    }

    
    public List<Listing> listAllUserListing(int userID){
    	List<Listing> listingList = new ArrayList<>();
    	try{
    	// userID -> gets list of products, products gets list of Listings.
    		String sql = "SELECT Tbl_List.*, Tbl_Prod.ProdName FROM Tbl_List LEFT JOIN Tbl_Prod on Tbl_Prod.ProdID = Tbl_List.ListProdID " +
    					 "WHERE Tbl_Prod.ProdSubmitUserID = ?;";
    		 
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, userID);
	        ResultSet results = state.executeQuery();
	        
	        while (results.next()) {
	        	// while we have new results, build the bid list
	        	Listing newList = new Listing(results.getInt("ListID"),
	        						 results.getInt("ListProdID"),
	        						 results.getString("ProdName"),
	        						 results.getDouble("ListBaseAmt"),
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

    public Listing listUserListing(int userID, int listID){
    	Listing myListing = null;
    	try{
    	// userID -> gets list of products, products gets list of Listings.
    		String sql = "SELECT Tbl_List.*, Tbl_Prod.ProdName FROM Tbl_List LEFT JOIN Tbl_Prod on Tbl_Prod.ProdID = Tbl_List.ListProdID " +
    					 "WHERE Tbl_Prod.ProdSubmitUserID = ? AND Tbl_List.ListID = ?;";
    		 
			connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, userID);
    		state.setInt(2, listID);
	        ResultSet results = state.executeQuery();
	        
	        if (results.next()) {
	        	// build the bid listing
	        	myListing = new Listing(results.getInt("ListID"),
	        						 results.getInt("ListProdID"),
	        						 results.getString("ProdName"),
	        						 results.getDouble("ListBaseAmt"),
	        						 results.getInt("ListMinQTY"),
	        						 results.getString("ListDate"),
	        						 results.getString("ListEDate"),
	        						 results.getString("List_TDate"));
	        }

	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - listAllUserListing: " + e.getMessage());
		}
    	return myListing;
    }

  
    public boolean updateListing(Listing updateList){
    	try{
    		String sql = "UPDATE Tbl_List SET List_TDate = ?, ListBaseAmt = ?, ListEDate = ?, ListMinQTY = ?," + 
    				     "ListProdID = ? WHERE ListID = ?;";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, updateList.getTerminationDate());
    		state.setDouble(2, updateList.getBaseCost());
    		state.setString(3, updateList.getEffectiveDate());
    		state.setInt(4, updateList.getMinAmount());
    		state.setInt(5, updateList.getProdID());
    		state.setInt(6, updateList.getListID());
			
    		boolean rowUpdated = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    		return rowUpdated;
    		
    	} catch (SQLException e) {
        	System.out.println("Database Error - updateListing: " + e.getMessage());
        	return false;
		}
    	
    }
    
    public boolean deleteListing(int listingID){
    	try{
    		String sql = "DELETE FROM Tbl_List where ListID = ?";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, listingID);
    		
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
    		connect();
    		//TODO: also manage Tbl_ProdKeyword

    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, newItem.getDescription());
    		state.setString(2, newItem.getKeywords());
    		state.setString(3, newItem.getName());
    		state.setInt(4, newItem.getQuantityInStock());
			state.setInt(5, newItem.getSellerID());
			
			boolean rowUpdated = state.executeUpdate() > 0;
			if(rowUpdated){
				sql = "SELECT @@IDENTITY;";
	    		state = jdbcConnection.prepareStatement(sql);
	    		ResultSet results = state.executeQuery();
	    		if (results.next()) {
	    			newItemID = results.getInt(1);
		        }
			}
			
			state.close();
			disconnect();
		} catch (SQLException e) {
        	System.out.println("Database Error - insertProduct: " + e.getMessage());
		}
    	return newItemID;
    }
    
    public List<Item> listProducts(){
    	List<Item> itemList = new ArrayList<>();
    	try{
    		String sql = "Select * FROM Tbl_Prod;";

    		//TODO: also manage Tbl_ProdKeyword

    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
	        ResultSet results = state.executeQuery();
	        
	        while (results.next()) {
	        	// while we have new results, build the bid list
	        	Item newItem = new Item(results.getInt("ProdID"),
	        						 results.getInt("ProdSubmitUserID"),
	        						 results.getString("ProdName"),
	        						 results.getString("ProdDesc"),
	        						 results.getString("ProdKeywords"),
	        						 results.getInt("ProdQTY"),
	        						 results.getString("ProdSubmitDate"));
	        	itemList.add(newItem);
	        }

	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - listProducts: " + e.getMessage());
		}
    	return itemList;
    }

    public List<Item> listProductsForUser(int userID){
    	List<Item> itemList = new ArrayList<>();
    	try{
    		String sql = "Select * FROM Tbl_Prod WHERE ProdSubmitUserID = ?;";

    		//TODO: also manage Tbl_ProdKeyword

    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, userID);
	        ResultSet results = state.executeQuery();
	        
	        while (results.next()) {
	        	// while we have new results, build the bid list
	        	Item newItem = new Item(results.getInt("ProdID"),
	        						 results.getInt("ProdSubmitUserID"),
	        						 results.getString("ProdName"),
	        						 results.getString("ProdDesc"),
	        						 results.getString("ProdKeywords"),
	        						 results.getInt("ProdQTY"),
	        						 results.getString("ProdSubmitDate"));
	        	itemList.add(newItem);
	        }

	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - listProductsForUser: " + e.getMessage());
		}
    	return itemList;
    }

    public Item getProductForUser(int userID, int prodID){
    	Item myItem = null;
    	try{
    		String sql = "Select * FROM Tbl_Prod WHERE ProdSubmitUserID = ? AND ProdID = ?;";

    		//TODO: also manage Tbl_ProdKeyword

    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, userID);
    		state.setInt(2, prodID);
	        ResultSet results = state.executeQuery();
	        
	        while (results.next()) {
	        	// while we have new results, build the bid list
	        	myItem = new Item(results.getInt("ProdID"),
	        					  results.getInt("ProdSubmitUserID"),
	        					  results.getString("ProdName"),
	        					  results.getString("ProdDesc"),
	        					  results.getString("ProdKeywords"),
	        					  results.getInt("ProdQTY"),
	        					  results.getString("ProdSubmitDate"));
	        }

	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - getProductForUser: " + e.getMessage());
		}
    	return myItem;
    }

    protected User getSellerForProduct(int prodID){
    	// get a subset of the seller information based on product ID, so we can properly build bids
    	User seller = new User();
    	try{
    		String sql = "Select Tbl_User.* FROM Tbl_Prod LEFT JOIN Tbl_User on Tbl_Prod.ProdSubmitUserID = Tbl_User.UserID WHERE ProdID = ?;";

    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, prodID);
	        ResultSet results = state.executeQuery();
	        
	        if (results.next()) {
	        	// while we have new results, build the bid list
	        	seller = new User(results.getInt("UserID"), 
	        					  results.getString("UserCompName"), 
	        					  "", 
	        					  "", 
	        					  null, null,
	        					  false,
	        					  false,
	        					  false);
	        }

	        results.close();
	        state.close();
	        disconnect();

    	} catch (SQLException e) {
        	System.out.println("Database Error - getSellerForProduct: " + e.getMessage());
		}
    	return seller;
    }

    
    public boolean updateProduct(Item updated){
      	try{
    		String sql = "UPDATE Tbl_Prod SET ProdDesc = ?, ProdKeywords = ?, ProdName = ?, ProdQTY = ?, " + 
    				     "ProdSubmitUserID = ? WHERE ProdID = ?;";
    		
    		//TODO: also manage Tbl_ProdKeyword
    		
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setString(1, updated.getDescription());
    		state.setString(2, updated.getKeywords());
    		state.setString(3, updated.getName());
    		state.setInt(4, updated.getQuantityInStock());
    		state.setInt(5, updated.getSellerID());
    		state.setInt(6, updated.getItemID());
			
    		boolean rowUpdated = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    		return rowUpdated;
    		
    	} catch (SQLException e) {
        	System.out.println("Database Error - updateProduct: " + e.getMessage());
        	return false;
		}
    }
    
    public boolean deleteProduct(int ProdID){
    	try{
    		String sql = "DELETE FROM Tbl_Prod where ProdID = ?";
    		connect();
    		PreparedStatement state = jdbcConnection.prepareStatement(sql);
    		state.setInt(1, ProdID);
    		
    		boolean rowDeleted = state.executeUpdate() > 0;
    		state.close();
    		disconnect();
    		return rowDeleted;
    		
    	} catch (SQLException e) {
        	System.out.println("Database Error - deleteProduct: " + e.getMessage());
        	return false;
		}
    }

}
