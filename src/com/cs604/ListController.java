package com.cs604;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.Sha2Crypt;

import com.cs604.validators.StringValidator;

/**
 * Servlet implementation class ListController
 */
@WebServlet(description = "handles listing actions", 
urlPatterns = {"/newListing", "/newListing2", "/editListing", "/editListing2", "/deleteListing"})
public class ListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ConnectDAO connectDB;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListController() {
        super();
    }
    
    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
        connectDB = ConnectDAO.getInstance(jdbcURL, jdbcUsername, jdbcPassword); 
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		
		//every item here should be logged in before accessing, so we can check for login right now
		HttpSession currentSession = request.getSession(false);
        if(currentSession!=null){
        	// a session exists
        	String session_email = (String)currentSession.getAttribute("Email");
        	String session_date = (String)currentSession.getAttribute("Date");
        	String session_hash = (String)currentSession.getAttribute("Hash");
        	if(!session_email.isEmpty() && !session_date.isEmpty() && !session_hash.isEmpty() && checkLoginHash(session_email, session_date, session_hash)){
        		// pull the userID for a valid email address
        		int userID = connectDB.getUserID(session_email);
        		// if a user is logged in, default should be their landing page
        		switch(action){
    				case "/newListing": newListingPre(request, response, userID); break;
    				case "/newListing2": newListingPost(request, response, userID); break;
    				case "/editListing": editListingPre(request, response, userID); break;
    				case "/editListing2": editListingPost(request, response, userID); break;
    				case "/deleteListing": deleteListing(request, response, userID); break;
    				default: bounceToDashboard(request, response); break;
        		}
        	}else{
        		// session info failed. bounce to login
        		request.getRequestDispatcher("Index.jsp").forward(request, response);
        	}
        }else{
        	// session info null, bounce to login
    		request.getRequestDispatcher("Index.jsp").forward(request, response);
        }
	}
	private void newListingPre(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
//    	System.out.println("newListingPre started");
        // pull the Product ID
		String itemID = request.getParameter("id");
		int itemNum = 0;
		
		//validate that the itemID is actually a number
		List<String> problems = new ArrayList<>();
		if(StringValidator.validInt(itemID)){
			itemNum = Integer.parseInt(itemID);
			// query the DB
			Item myItem = connectDB.getProductForUser(userID, itemNum);
			if(myItem == null){
				problems.add("no such product for user");
				request.setAttribute("problems", problems);
			}else{
				// we have a valid item, attach it to the request
				request.setAttribute("item", myItem);
				// make dates for today and next week
				LocalDate today = LocalDate.now();
				LocalDate nextWeek = today.plusDays(7);
				request.setAttribute("today", today.toString());
				request.setAttribute("nextWeek", nextWeek.toString());

			}
		}else{
			problems.add("invalid product id error");
			request.setAttribute("problems", problems);
		}
		if(problems.isEmpty()){
//	    	System.out.println("newListingPre success, going to listing.jsp");
			request.getRequestDispatcher("listing.jsp").forward(request, response);
		}else{
//	    	System.out.println("newListingPre failed, going to dashboard");
			request.getRequestDispatcher("/dashboard").forward(request, response);
		}
	}

	private void newListingPost(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
//    	System.out.println("newListingPost started");
		String itemID = request.getParameter("ItemID");
		String l_startDate = request.getParameter("StartDate");
		String l_endDate = request.getParameter("EndDate");
		String l_baseCost = request.getParameter("BaseCost");
		String l_minPurchase = request.getParameter("MinPurchase");
		int itemNum = 0;
				
		List<String> problems = new ArrayList<>();
		problems.addAll(validateList(itemID, l_startDate, l_endDate, l_baseCost, l_minPurchase));
		if(StringValidator.validInt(itemID)){
			itemNum = Integer.parseInt(itemID);
		}else{
			problems.add("invalid product id error");
		}

		if(!problems.isEmpty()){
			request.setAttribute("problems", problems);
		}else{
			//Input has no technical problems, creating a new listing.
    		Listing newListing = new Listing(itemNum, "some name", Double.parseDouble(l_baseCost), Integer.parseInt(l_minPurchase), "some date", l_startDate, l_endDate);
    		//update DB
    		if(connectDB.insertListing(newListing) == 0){
				problems.add("database connection error");
				request.setAttribute("problems", problems);
    		}
		}
		
		if(problems.isEmpty()){
//	    	System.out.println("newListingPost success, going to dashboard");
			request.getRequestDispatcher("/dashboard").forward(request, response);
		}else{
//	    	System.out.println("newListingPost failed, going to reload");
			request.getRequestDispatcher("listing.jsp").forward(request, response);
		}
	}

	private void editListingPre(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
    	//System.out.println("editListingPre started");
        // pull the Listing ID
		String listID = request.getParameter("id");
		int listNum = 0;

		//validate that the listID is actually a number
		List<String> problems = new ArrayList<>();
		if(StringValidator.validInt(listID)){
			listNum = Integer.parseInt(listID);
			// query the DB
			Listing myList = connectDB.listUserListing(userID, listNum);
			if(myList == null){
				problems.add("no such listing for user");
				request.setAttribute("problems", problems);
			}else{
				// we have a valid item
		    	//System.out.println("edit listing pre - listing id: " + myList.getListID());
				request.setAttribute("listing", myList);
			}
		}else{
			problems.add("unable to edit, invalid listing id error");
			request.setAttribute("problems", problems);
		}
		
		if(problems.isEmpty()){
//	    	System.out.println("editListingPre success, going to listing.jsp");
			request.getRequestDispatcher("listing.jsp").forward(request, response);
		}else{
//	    	System.out.println("editListingPre failed, going to dashboard");
			request.getRequestDispatcher("/dashboard").forward(request, response);
		}
	}
	
	private void editListingPost(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
//    	System.out.println("editListingPost started");
		String listID = request.getParameter("ListingID");
		String itemID = request.getParameter("Product");
		String l_startDate = request.getParameter("StartDate");
		String l_endDate = request.getParameter("EndDate");
		String l_baseCost = request.getParameter("BaseCost");
		String l_minPurchase = request.getParameter("MinPurchase");
		int listNum = 0;
		int itemNum = 0;
		System.out.println("editListingPost params - listingID: "+listID+", product: "+itemID+", StartDate: "+l_startDate+", EndDate: "+l_endDate+", BaseCost: "+l_baseCost+", MinPurchase: "+l_minPurchase);
		List<String> problems = new ArrayList<>();
		problems.addAll(validateList(itemID, l_startDate, l_endDate, l_baseCost, l_minPurchase));
		if(StringValidator.validInt(listID)){
			listNum = Integer.parseInt(listID);
		}else{
			problems.add("invalid Listing id error");
		}
		if(StringValidator.validInt(itemID)){
			itemNum = Integer.parseInt(itemID);
		}else{
			problems.add("invalid Listing id error");
		}

		if(!problems.isEmpty()){
			request.setAttribute("problems", problems);
		}else{
			//Input has no technical problems, creating a new listing.
    		Listing newListing = new Listing(listNum, itemNum, "some name", Double.parseDouble(l_baseCost), Integer.parseInt(l_minPurchase), "some date", l_startDate, l_endDate);
    		//update DB
    		if(!connectDB.updateListing(newListing)){
				problems.add("database connection error");
				request.setAttribute("problems", problems);
    		}
		}
		
		if(problems.isEmpty()){
//	    	System.out.println("editListingPost success, going to dashboard");
			request.getRequestDispatcher("/dashboard").forward(request, response);
		}else{
//	    	System.out.println("editListingPost failed, reloading");
			request.getRequestDispatcher("listing.jsp").forward(request, response);
		}
	}

	private void deleteListing(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
//   	System.out.println("deleteListing started");
        // pull the Listing ID
		String listID = request.getParameter("id");
		int listNum = 0;
		
		//validate that the listID is actually a number
		List<String> problems = new ArrayList<>();
		if(StringValidator.validInt(listID)){
			listNum = Integer.parseInt(listID);
			// query the DB
			Listing myList = connectDB.listUserListing(userID, listNum);
			if(myList == null){
				problems.add("no such listing for user");
				request.setAttribute("problems", problems);
			}else{
				// we have a valid item, kill it
				if(!connectDB.deleteListing(listNum)){
					problems.add("error connecting to database");
					request.setAttribute("problems", problems);
				}
			}
		}else{
			problems.add("unable to delete, invalid listing id error");
			request.setAttribute("problems", problems);
		}
		
		// doesn't matter if there was an error, they are going to the same place
//    	System.out.println("deleteListing finished, going to dashboard");
		request.getRequestDispatcher("/dashboard").forward(request, response);
	}
	
	private void bounceToDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/dashboard").forward(request, response);
	}



	private boolean checkLoginHash(String email, String date, String hash){
		//String base64Date = Base64.getEncoder().encodeToString(date.getBytes());
		String validHash = Sha2Crypt.sha256Crypt(email.getBytes(), "$5$" + date);
		return validHash.equals(hash);
	}
	
	private List<String> validateList(String itemID, String l_startDate, String l_endDate, String l_baseCost, String l_minPurchase){
		List<String> errors = new ArrayList<>();
		if (!StringValidator.validInt(itemID)){
			errors.add("invalid product");
		}else if(Integer.parseInt(itemID) <= 0){
			errors.add("invalid product");
		}
		if (!StringValidator.validate(l_startDate)){
			errors.add("Listing Start Date is not valid");
		}
		if (!StringValidator.validate(l_endDate)){
			errors.add("Listing Start Date is not valid");
		}
		if (!StringValidator.validDouble(l_baseCost)){
			errors.add("Cost per unit needs to be an number, eg: 100 or 12.5");
		}else if(Double.parseDouble(l_baseCost) <= 0){
			errors.add("Cost per unit needs to be greater than 0");
		}

		if (!StringValidator.validInt(l_minPurchase)){
			errors.add("Minimum Quantity Ordered needs to be an integer, eg: 100");
		}else if(Integer.parseInt(l_minPurchase) <= 0){
			errors.add("Minimum Quantity Ordered needs to be greater than 0");
		}

		return errors;
	}

}
