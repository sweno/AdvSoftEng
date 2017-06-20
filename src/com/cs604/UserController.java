package com.cs604;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.Sha2Crypt;

import com.cs604.validators.EmailValidator;
import com.cs604.validators.StringValidator;

/**
 * Servlet implementation class UserController
 */
@WebServlet(description = "handles users interaction", 
			urlPatterns = {"/newUser", "/login", "/logout", "/dashboard", "/myAccount", "/updateUser", "/changePassword" })
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ConnectDAO connectDB;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserController() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
 
        connectDB = new ConnectDAO(jdbcURL, jdbcUsername, jdbcPassword);
 
    }
	// we should just pass along get requests
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		// handle the action that we don't need post data for
		switch(action){
			case "/logout": parseLogoutPage(request, response); break;
			case "/myAccount": buildAccountPage(request, response); break;
			default: parseWelcomePage(request, response); break;
		}
	}

	// post requests are where we are going to be doing the work
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		
		switch(action){
			case "/newUser": parseNewUserForm(request, response); break;
			case "/login": parseLoginPage(request, response); break;
			case "/logout": parseLogoutPage(request, response); break;
			case "/dashboard": parseDashboard(request, response); break;
			case "/updateUser": parseUpdateUser(request, response); break;
			case "/changePassword": parseChangePassword(request, response); break;
			default: parseWelcomePage(request, response); break;
		}
	}
		
	private void parseNewUserForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get the meaningful request attributes		
		String p_name = request.getParameter("name");
		String p_email = request.getParameter("email");
		String p_pass = request.getParameter("password");
		boolean p_Buyer = (request.getParameter("Buyer") != null && request.getParameter("Buyer").equals("on"));
		boolean p_Seller = (request.getParameter("Seller") != null && request.getParameter("Seller").equals("on"));
		String p_Bill_Street1 = request.getParameter("Bill_Street1");
		String p_Bill_Street2 = request.getParameter("Bill_Street2");
		String p_Bill_City = request.getParameter("Bill_City");
		String p_Bill_State = request.getParameter("Bill_State");
		String p_Bill_Zip = request.getParameter("Bill_Zip");
		String p_Bill_Country = request.getParameter("Bill_Country");
		boolean p_SameShip = (request.getParameter("SameShip") != null && request.getParameter("SameShip").equals("on"));
		String p_Ship_Street1 = request.getParameter("Ship_Street1");
		String p_Ship_Street2 = request.getParameter("Ship_Street2");
		String p_Ship_City = request.getParameter("Ship_City");
		String p_Ship_State = request.getParameter("Ship_State");
		String p_Ship_Zip = request.getParameter("Ship_Zip");
		String p_Ship_Country = request.getParameter("Ship_Country");
		
		if(request.getParameter("Buyer") != null){
			System.out.println("Parse newUser page, Buyer = "+request.getParameter("Buyer")+", Seller = "+request.getParameter("Seller"));
		}
		
		// set the request attributes so that we can reference them in error messages
		request.setAttribute("name", p_name);
        request.setAttribute("email", p_email);
        request.setAttribute("password", p_pass);
                
		List<String> problems = new ArrayList<>();
		problems.addAll(validateUser(p_name, p_email, p_pass, p_Buyer, p_Seller));
		problems.addAll(validateAddr("Billing", p_Bill_Street1, p_Bill_Street2, p_Bill_City, p_Bill_State, p_Bill_Zip, p_Bill_Country));
		if(!p_SameShip){
			problems.addAll(validateAddr("Shipping", p_Ship_Street1, p_Ship_Street2, p_Ship_City, p_Ship_State, p_Ship_Zip, p_Ship_Country));
		}
		
		if(!problems.isEmpty()){
			request.setAttribute("problems", problems);
		}else{
			//Input has no technical problems, test to see if it already exists.
			Address billAddr = new Address(p_name, p_Bill_Street1, p_Bill_Street2, p_Bill_City, p_Bill_State, p_Bill_Zip, p_Bill_Country);
			Address shipAddr;
			if(p_SameShip){
				shipAddr = billAddr; // make sure that it is populated
			}else{
				shipAddr = new Address(p_name, p_Ship_Street1, p_Ship_Street2, p_Ship_City, p_Ship_State, p_Ship_Zip, p_Ship_Country);
			}
			User possibleUser = new User(p_name, p_email, p_pass, billAddr, shipAddr, p_Buyer, p_Seller, p_SameShip);
			
			if(connectDB.getUserID(p_email) > 0){
				// email already in use
				problems.add("email address already in use");
				request.setAttribute("problems", problems);
			}else{
				int userID = connectDB.insertUser(possibleUser);
				if(userID == 0){
					problems.add("database connection error");
					request.setAttribute("problems", problems);
				}
			}
		}
		
		if(problems.isEmpty()){
			request.getRequestDispatcher("Welcome.jsp").forward(request, response);
		}else{
			request.getRequestDispatcher("newUser.jsp").forward(request, response);
		}
	}
	

	private void parseLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String p_email = request.getParameter("email");
		String p_pass = request.getParameter("password");
		List<String> problems = new ArrayList<>();

		//The assumption here is that the user exists. so pull a user id based on email.
		int userID = connectDB.getUserID(p_email);
    	System.out.println("Parse login page, email: " + p_email + ", password: " + p_pass + ", ID: "+ userID);
		if(userID > 0){
			// email exists, pull the rest of the user data
			User fullUser = connectDB.getUser(userID);
			//fullUser.debugUser();
			if(fullUser.CheckPasswordHash(p_pass)){
		    	System.out.println("Parse login page, password match");
				//passwords match, login the user
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
				String timestamp = dateformat.format(new Date());
				
				//Set the session variables
				HttpSession currentSession=request.getSession();
		        currentSession.setAttribute("Email",p_email);
		        currentSession.setAttribute("Date",timestamp);
		        currentSession.setAttribute("Hash",newLoginHash(p_email, timestamp));
		        
		        //Set the user dashboard variables
		        request.setAttribute("user", fullUser);
		        if(fullUser.getSellerFlag()){
		        	request.setAttribute("sellerBidList", connectDB.listBidsForSeller(userID));
		        	request.setAttribute("sellerItemList", connectDB.listProductsForUser(userID));
		        	request.setAttribute("sellerList", connectDB.listAllUserListing(userID));
		        }
		        if(fullUser.getBuyerFlag()){
		        	request.setAttribute("buyerBidList", connectDB.listBidsForBidder(userID));
		        	request.setAttribute("buyerList", connectDB.listAllActiveListing());
		        }
		        
			}else{
				//passwords don't match, return a generic error
		    	System.out.println("Parse login page, password miss-match");
				problems.add("Username or Password doesn't match");
			}
		}else{
			// email doesn't exist, return a generic error
			problems.add("Username or Password doesn't match");
		}
		if(!problems.isEmpty()){
			request.setAttribute("problems", problems);
			request.getRequestDispatcher("Index.jsp").forward(request, response);
		}else{
			request.getRequestDispatcher("userDashboard.jsp").forward(request, response);
		}
	}
	
	private void parseLogoutPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession currentSession=request.getSession();
		currentSession.invalidate();
		
		request.getRequestDispatcher("Index.jsp").forward(request, response);
	}
	
	private void parseWelcomePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // if a user is not logged in, it should be the login page
		String url = "Index.jsp";
		
        HttpSession currentSession = request.getSession(false);
        if(currentSession!=null){
        	// a session exists
        	String session_email = (String)currentSession.getAttribute("Email");
        	String session_date = (String)currentSession.getAttribute("Date");
        	String session_hash = (String)currentSession.getAttribute("Hash");
        	if(!session_email.isEmpty() && !session_date.isEmpty() && !session_hash.isEmpty() && 
        		checkLoginHash(session_email, session_date, session_hash)){
        		// if a user is logged in, default should be their landing page
        		url = "dashboard";
        	}
        }
		request.getRequestDispatcher(url).forward(request, response);
	}

	
	private void parseDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // if a user is not logged in, we should kick them to the index page.
		String url = "Index.jsp";
		
		// pull the current session if one exists
        HttpSession currentSession = request.getSession(false);
        if(currentSession!=null){
        	// a session exists
        	String session_email = (String)currentSession.getAttribute("Email");
        	String session_date = (String)currentSession.getAttribute("Date");
        	String session_hash = (String)currentSession.getAttribute("Hash");
        	if(!session_email.isEmpty() && !session_date.isEmpty() && !session_hash.isEmpty() && 
        		checkLoginHash(session_email, session_date, session_hash)){
		    	//System.out.println("Building dashboard for user: " + session_email);
        		url = "userDashboard.jsp";
        		// user is logged in, so pull the necessary data from the DB to build their dashboard
        		int userID = connectDB.getUserID(session_email);
        		// we assume the user is valid, otherwise they couldn't have logged in
    			User fullUser = connectDB.getUser(userID);
    			request.setAttribute("user", fullUser);
		        if(fullUser.getSellerFlag()){
		        	request.setAttribute("sellerBidList", connectDB.listBidsForSeller(userID));
		        	request.setAttribute("sellerItemList", connectDB.listProductsForUser(userID));
		        	request.setAttribute("sellerList", connectDB.listAllUserListing(userID));
		        }
		        if(fullUser.getBuyerFlag()){
		        	request.setAttribute("buyerBidList", connectDB.listBidsForBidder(userID));
		        	request.setAttribute("buyerList", connectDB.listAllActiveListing());
		        }
        	}else{
		    	System.out.println("failed dashboard for user: " + session_email);
        	}
        }
    	//System.out.println("dashboard variables setup, passing onto RequestDispatcher");
		request.getRequestDispatcher(url).forward(request, response);
	}

	
	private void buildAccountPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // if a user is not logged in, we should kick them to the index page.
		String url = "Index.jsp";
		
		// pull the current session if one exists
        HttpSession currentSession = request.getSession(false);
        if(currentSession!=null){
        	// a session exists
        	String session_email = (String)currentSession.getAttribute("Email");
        	String session_date = (String)currentSession.getAttribute("Date");
        	String session_hash = (String)currentSession.getAttribute("Hash");
        	if(!session_email.isEmpty() && !session_date.isEmpty() && !session_hash.isEmpty() && 
        		checkLoginHash(session_email, session_date, session_hash)){
		    	//System.out.println("Building dashboard for user: " + session_email);
        		url = "myAccount.jsp";
        		// user is logged in, so pull the necessary data from the DB to build their dashboard
        		int userID = connectDB.getUserID(session_email);
        		// we assume the user is valid, otherwise they couldn't have logged in
    			User fullUser = connectDB.getUser(userID);
    			request.setAttribute("user", fullUser);
		        if(fullUser.getSellerFlag()){
		        	request.setAttribute("sellerBidList", connectDB.listBidsForSeller(userID));
		        	request.setAttribute("sellerItemList", connectDB.listProductsForUser(userID));
		        	request.setAttribute("sellerList", connectDB.listAllUserListing(userID));
		        }
		        if(fullUser.getBuyerFlag()){
		        	request.setAttribute("buyerBidList", connectDB.listBidsForBidder(userID));
		        	request.setAttribute("buyerList", connectDB.listAllActiveListing());
		        }
        	}else{
		    	System.out.println("failed dashboard for user: " + session_email);
        	}
        }
    	//System.out.println("dashboard variables setup, passing onto RequestDispatcher");
		request.getRequestDispatcher(url).forward(request, response);
	}

	private void parseUpdateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // if a user is not logged in, we should kick them to the index page.
		String url = "Index.jsp";
		
		// pull the current session if one exists
        HttpSession currentSession = request.getSession(false);
        if(currentSession!=null){
        	// a session exists
        	String session_email = (String)currentSession.getAttribute("Email");
        	String session_date = (String)currentSession.getAttribute("Date");
        	String session_hash = (String)currentSession.getAttribute("Hash");
        	if(!session_email.isEmpty() && !session_date.isEmpty() && !session_hash.isEmpty() && 
        		checkLoginHash(session_email, session_date, session_hash)){
		    	//System.out.println("updating info for user: " + session_email);
        		url = "userDashboard.jsp"; //valid users should reload the dashboard
        		        		
        		// get the meaningful request attributes		
        		String u_name = request.getParameter("name");
        		boolean u_Buyer = (request.getParameter("Buyer") != null && request.getParameter("Buyer").equals("on"));
        		boolean u_Seller = (request.getParameter("Seller") != null && request.getParameter("Seller").equals("on"));
        		String u_Bill_Street1 = request.getParameter("Bill_Street1");
        		String u_Bill_Street2 = request.getParameter("Bill_Street2");
        		String u_Bill_City = request.getParameter("Bill_City");
        		String u_Bill_State = request.getParameter("Bill_State");
        		String u_Bill_Zip = request.getParameter("Bill_Zip");
        		String u_Bill_Country = request.getParameter("Bill_Country");
        		boolean u_SameShip = (request.getParameter("SameShip") != null && request.getParameter("SameShip").equals("on"));
        		String u_Ship_Street1 = request.getParameter("Ship_Street1");
        		String u_Ship_Street2 = request.getParameter("Ship_Street2");
        		String u_Ship_City = request.getParameter("Ship_City");
        		String u_Ship_State = request.getParameter("Ship_State");
        		String u_Ship_Zip = request.getParameter("Ship_Zip");
        		String u_Ship_Country = request.getParameter("Ship_Country");
        		
        		// set the request attributes so that we can reference them in error messages
        		request.setAttribute("name", u_name);
                request.setAttribute("email", session_email);
                        
        		List<String> problems = new ArrayList<>();
        		problems.addAll(validateUser(u_name, session_email, "filler_password", u_Buyer, u_Seller));
        		problems.addAll(validateAddr("Billing", u_Bill_Street1, u_Bill_Street2, u_Bill_City, u_Bill_State, u_Bill_Zip, u_Bill_Country));
        		if(!u_SameShip){
    		    	//System.out.println("shipping address is different: " + session_email);
        			problems.addAll(validateAddr("Shipping", u_Ship_Street1, u_Ship_Street2, u_Ship_City, u_Ship_State, u_Ship_Zip, u_Ship_Country));
        		}
        		
        		if(!problems.isEmpty()){
        			request.setAttribute("problems", problems);
        		}else{
        			//Input has no technical problems, update existing user.
        			Address billAddr = new Address(u_name, u_Bill_Street1, u_Bill_Street2, u_Bill_City, u_Bill_State, u_Bill_Zip, u_Bill_Country);
        			Address shipAddr;
        			if(u_SameShip){
        				shipAddr = billAddr; // make sure that it is populated
        			}else{
        				shipAddr = new Address(u_name, u_Ship_Street1, u_Ship_Street2, u_Ship_City, u_Ship_State, u_Ship_Zip, u_Ship_Country);
        			}

        			//pull their existing user info
            		int userID = connectDB.getUserID(session_email);
        			User fullUser = connectDB.getUser(userID);
        			//update the info
        			fullUser.setBillingAddress(billAddr);
        			fullUser.setShippingAddress(shipAddr);
        			fullUser.setName(u_name);
        			fullUser.setBuyerFlag(u_Buyer);
        			fullUser.setSellerFlag(u_Seller);        			
        			fullUser.setShippingIsBilling(u_SameShip);
        			//write it back to the DB
        			if(connectDB.userHasShipAddress(userID)){
        				if(!connectDB.updateShipAddressForUser(userID, shipAddr)){
            		    	System.out.println("failed to update shipping address for : " + session_email);
            			}
        			}else{
        				connectDB.insertUserShip(fullUser);
        			}
        			if(!connectDB.updateBillAddressForUser(userID, billAddr)){
        		    	System.out.println("failed to update billing address for : " + session_email);
        			}
        			if(!connectDB.updateUser(fullUser)){
        		    	System.out.println("failed to update user info for : " + session_email);
        			}

        			//fullUser.debugUser();
        			request.setAttribute("user", fullUser);
    		        if(fullUser.getSellerFlag()){
    		        	request.setAttribute("sellerBidList", connectDB.listBidsForSeller(userID));
    		        	request.setAttribute("sellerItemList", connectDB.listProductsForUser(userID));
    		        	request.setAttribute("sellerList", connectDB.listAllUserListing(userID));
    		        }
    		        if(fullUser.getBuyerFlag()){
    		        	request.setAttribute("buyerBidList", connectDB.listBidsForBidder(userID));
    		        	request.setAttribute("buyerList", connectDB.listAllActiveListing());
    		        }	
        		}        		
        	}else{
		    	System.out.println("failed dashboard for user: " + session_email);
        	}
        }
		request.getRequestDispatcher(url).forward(request, response);
	}

	private void parseChangePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // if a user is not logged in, we should kick them to the index page.
		String url = "Index.jsp";
		
		// pull the current session if one exists
        HttpSession currentSession = request.getSession(false);
        if(currentSession!=null){
        	// a session exists
        	String session_email = (String)currentSession.getAttribute("Email");
        	String session_date = (String)currentSession.getAttribute("Date");
        	String session_hash = (String)currentSession.getAttribute("Hash");
        	if(!session_email.isEmpty() && !session_date.isEmpty() && !session_hash.isEmpty() && 
        		checkLoginHash(session_email, session_date, session_hash)){
		    	System.out.println("Building dashboard for user: " + session_email);
        		url = "userDashboard.jsp";
        		// user is logged in, so pull the necessary data from the DB to build their dashboard
        		int userID = connectDB.getUserID(session_email);
        		// we assume the user is valid, otherwise they couldn't have logged in
    			User fullUser = connectDB.getUser(userID);
    			request.setAttribute("user", fullUser);
		        if(fullUser.getSellerFlag()){
		        	request.setAttribute("sellerBidList", connectDB.listBidsForSeller(userID));
		        	request.setAttribute("sellerItemList", connectDB.listProductsForUser(userID));
		        	request.setAttribute("sellerList", connectDB.listAllUserListing(userID));
		        }
		        if(fullUser.getBuyerFlag()){
		        	request.setAttribute("buyerBidList", connectDB.listBidsForBidder(userID));
		        	request.setAttribute("buyerList", connectDB.listAllActiveListing());
		        }
        	}else{
		    	System.out.println("failed dashboard for user: " + session_email);
        	}
        }
		request.getRequestDispatcher(url).forward(request, response);
	}

	private List<String> validateUser(String name, String email, String password, boolean buyer, boolean seller){
		List<String> errors = new ArrayList<>();
		if (!StringValidator.validate(name)){
			errors.add("Name is mandatory");
		}
		if (!EmailValidator.validate(email)){
			errors.add("Email not properly formated");
		}
		if (!StringValidator.validate(password)){
			errors.add("password is mandatory");
		}
		return errors;
	}
	
	private List<String> validateAddr(String prefix, String street1, String street2, String city, String state, String zip, String country){
		List<String> errors = new ArrayList<>();
		if (!StringValidator.validate(street1)){
			errors.add(prefix + " Street is mandatory");
		}
		// we should check for invalid content, but for now, ignore it
		//if (!StringValidator.validate(street2)){
		//	errors.add("Name is mandatory");
		//}
		if (!StringValidator.validate(city)){
			errors.add(prefix + " city is mandatory");
		}
		if (!StringValidator.validate(state)){
			errors.add(prefix + " state is mandatory");
		}
		if (!StringValidator.validate(zip)){
			errors.add(prefix + " zip is mandatory");
		}
		if (!StringValidator.validate(country)){
			errors.add(prefix + " country is mandatory");
		}
		return errors;
	}
	
	private String newLoginHash(String email, String date){
		//String base64Date = Base64.getEncoder().encodeToString(date.getBytes());
		return Sha2Crypt.sha256Crypt(email.getBytes(), "$5$" + date);
	}
	
	private boolean checkLoginHash(String email, String date, String hash){
		//String base64Date = Base64.getEncoder().encodeToString(date.getBytes());
		String validHash = Sha2Crypt.sha256Crypt(email.getBytes(), "$5$" + date);
		return validHash.equals(hash);
	}
}