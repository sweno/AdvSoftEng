package com.cs604;

import java.io.IOException;
import java.time.LocalDateTime;
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
 * Servlet implementation class BidController
 */
@WebServlet(description = "handles biding actions", 
			urlPatterns = {"/currentBids", "/accpetBid", "/rejectBid", "/newBid", "/newBid2", "/editBid", "/editBid2", "/deleteBid"})
public class BidController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ConnectDAO connectDB;
	private EmailNotifier mailer;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BidController() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
        connectDB = ConnectDAO.getInstance(jdbcURL, jdbcUsername, jdbcPassword);
        
        String emailusername = getServletContext().getInitParameter("emailUsername");
        String emailpassword = getServletContext().getInitParameter("emailPassword");
        String emailhost = getServletContext().getInitParameter("emailHost");
        mailer = new EmailNotifier(emailusername, emailpassword, emailhost);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		
		//every item here should be logged in before accessing, so we can check for login right now
		HttpSession currentSession = request.getSession(false);
        if(validateLogin(currentSession)){
        	// pull the userID for a valid email address
        	int userID = connectDB.getUserID((String)currentSession.getAttribute("Email"));
        	// all pages reference user information, so save that as part of the request
    		request.setAttribute("user", connectDB.getUser(userID));
        	// if a user is logged in, default should be their landing page
        	switch(action){
        		case "/currentBids": bidList(request, response, userID); break;
	        	default: bounceToDashboard(request, response); break;
        	}
        }else{
        	// session info null, bounce to login
    		request.getRequestDispatcher("Index.jsp").forward(request, response);
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		
		//every item here should be logged in before accessing, so we can check for login right now
		HttpSession currentSession = request.getSession(false);
        if(validateLogin(currentSession)){
        	// pull the userID for a valid email address
        	int userID = connectDB.getUserID((String)currentSession.getAttribute("Email"));
        	// all pages reference user information, so save that as part of the request
    		request.setAttribute("user", connectDB.getUser(userID));
        	// if a user is logged in, default should be their landing page
        	switch(action){
        		case "/currentBids": bidList(request, response, userID); break;
	        	case "/accpetBid": acceptBid(request, response, userID); break;
	        	case "/rejectBid": rejectBid(request, response, userID); break;
	        	case "/newBid": newBidPre(request, response, userID); break;
	        	case "/newBid2": newBidPost(request, response, userID); break;
	        	case "/editBid": editBidPre(request, response, userID); break;
	        	case "/editBid2": editBidPost(request, response, userID); break;
	        	case "/deleteBid": deleteBid(request, response, userID); break;
	        	default: bounceToDashboard(request, response); break;
        	}
        }else{
        	// session info null, bounce to login
    		request.getRequestDispatcher("Index.jsp").forward(request, response);
        }
	}

	private void bidList(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
//    	System.out.println("bidList started");
    	request.setAttribute("buyerBidList", connectDB.listBidsForBidder(userID));
    	request.getRequestDispatcher("bidlist.jsp").forward(request, response);
	}

	private void newBidPre(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
//    	System.out.println("newBidPre started");
        // get the listing id
		String listID = request.getParameter("id");
		int listNum = 0;
		
		//validate that the listID is actually a number
		List<String> problems = new ArrayList<>();
		if(StringValidator.validInt(listID)){
			listNum = Integer.parseInt(listID);
			// query the DB
			Listing myList = connectDB.getActiveListing(listNum);
			if(myList == null){
				problems.add("unable to bid, no listing found");
				request.setAttribute("problems", problems);
			}else{
				// we have a valid item, attach it to the request
				request.setAttribute("listing", myList);
				// also pull the seller name
				User seller = connectDB.getSellerForProduct(myList.getProdID());
				if(seller == null){
					problems.add("unable to bid, incomplete listing information");
					request.setAttribute("problems", problems);
				}else{
					// attach the seller info.
					request.setAttribute("sellerName", seller.getName());
					request.setAttribute("sellerID", seller.getID());
				}
			}
		}else{
			problems.add("unable to bid, invalid listing id error");
			request.setAttribute("problems", problems);
		}
		if(problems.isEmpty()){
//	    	System.out.println("newBidPre success, going to bid.jsp");
			request.getRequestDispatcher("bid.jsp").forward(request, response);
		}else{
//	    	System.out.println("newBidPre failed, going to dashboard");
			request.getRequestDispatcher("/currentBids").forward(request, response);
		}

	}

	private void newBidPost(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
//    	System.out.println("newBidPost started");
		String sellerID = request.getParameter("SellerID");
		String itemID = request.getParameter("ItemID");
		String quantity_requested = request.getParameter("MinPurchase");
		String BaseCost = request.getParameter("BaseCost");

		List<String> problems = new ArrayList<>();
		problems.addAll(validateBid(sellerID, itemID, quantity_requested, BaseCost));
		
		if(!problems.isEmpty()){
			request.setAttribute("problems", problems);
		}else{
			//Input has no technical problems, creating a new bid.
			LocalDateTime now = LocalDateTime.now();
			//int sellerNum = Integer.parseInt(sellerID);
			//int itemNum = Integer.parseInt(itemID);
			//int quantNum = Integer.parseInt(quantity_requested);
			//double baseDoub = Double.parseDouble(BaseCost);
	    	//System.out.println("newBidPost content - userid: "+userID+", sellerid: "+sellerNum+", itemID: "+itemNum+", now: "+now.toString()+", quantity_requested: "+quantNum+", baseCost: "+baseDoub);
    		Bid newBid = new Bid(userID, Integer.parseInt(sellerID), Integer.parseInt(itemID), now.toString(), Integer.parseInt(quantity_requested), Double.parseDouble(BaseCost), false, now.toString());
    		//update DB
    		if(connectDB.insertBid(newBid) == 0){
				problems.add("database connection error");
				request.setAttribute("problems", problems);
    		}
		}
		
		if(problems.isEmpty()){
//	    	System.out.println("newBidPost success, going to dashboard");
			request.getRequestDispatcher("/currentBids").forward(request, response);
		}else{
//	    	System.out.println("newBidPost failed, reloading");
			request.getRequestDispatcher("bid.jsp").forward(request, response);
		}
	}

	private void editBidPre(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
    	//System.out.println("editBidPre started");
        // pull the Bid ID
		String bidID = request.getParameter("id");
		int bidNum = 0;

		//validate that the bidID is actually a number
		List<String> problems = new ArrayList<>();
		if(StringValidator.validInt(bidID)){
			bidNum = Integer.parseInt(bidID);
			// query the DB
			Bid myBid = connectDB.getBidForBidder(userID, bidNum);
			if(myBid == null){
				problems.add("no such bid for user");
				request.setAttribute("problems", problems);
			}else{
				// we have a valid bid
				request.setAttribute("bid", myBid);
			}
		}else{
			problems.add("unable to edit, invalid bid id error");
			request.setAttribute("problems", problems);
		}
		
		if(problems.isEmpty()){
//	    	System.out.println("editBidPre success, going to bid.jsp");
			request.getRequestDispatcher("bid.jsp").forward(request, response);
		}else{
//	    	System.out.println("editBidPre failed, going to dashboard");
			request.getRequestDispatcher("/currentBids").forward(request, response);
		}
	}
	
	private void editBidPost(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
//    	System.out.println("editBidPost started");
		String bidID = request.getParameter("BidID");
		String quantity_requested = request.getParameter("MinPurchase");
		String BaseCost = request.getParameter("BaseCost");
		int bidNum = 0;
    	System.out.println("editBidPost bidID: "+bidID+", minPurchase: "+quantity_requested+", BaseCose: "+BaseCost);

		List<String> problems = new ArrayList<>();
		// don't care about sellerID and itemID for these purposes
		problems.addAll(validateBid("1", "1", quantity_requested, BaseCost));

		if(StringValidator.validInt(bidID)){
			bidNum = Integer.parseInt(bidID);
		}else{
			problems.add("invalid bid id error");
		}

		if(!problems.isEmpty()){
			request.setAttribute("problems", problems);
		}else{
			//Input has no technical problems, migrate the bid.
			// because we don't want to have a race condition between someone editing their bid and it being accepted, 
			// we are going to create a new bid and delete the old one.
			// read in the old one.
    		Bid myBid = connectDB.getBidForBidder(userID, bidNum);
    		if(myBid != null){
    			// update it
    			myBid.setQuantity(Integer.parseInt(quantity_requested));
    			myBid.setProposedPrice(Double.parseDouble(BaseCost));
    			//delete the old one
    			if(!connectDB.deleteBid(bidNum)){
    				problems.add("database connection error");
    				request.setAttribute("problems", problems);
    			}else{
    		    	//System.out.println("editBidPost bidID: "+myBid.getBidID()+", minPurchase: "+myBid.getQuantity()+", BaseCose: "+myBid.getProposedPrice());
    				// we killed the old one, make a new one
    				if(connectDB.insertBid(myBid) == 0){
        				problems.add("database error, bid lost");
        				request.setAttribute("problems", problems);
    				}
    			}
    		}else{
    			problems.add("unable to edit, Database Error");
				request.setAttribute("problems", problems);
    		}
		}
		
		if(problems.isEmpty()){
//	    	System.out.println("editBidPost success, going to dashboard");
			request.getRequestDispatcher("/currentBids").forward(request, response);
		}else{
//	    	System.out.println("editBidPost failed, reloading");
			request.getRequestDispatcher("bid.jsp").forward(request, response);
		}
		
	}
	
	private void deleteBid(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
//   	System.out.println("deleteBid started");
        // pull the bid ID
		String bidID = request.getParameter("id");
		int bidNum = 0;
		
		//validate that the bidID is actually a number
		List<String> problems = new ArrayList<>();
		if(StringValidator.validInt(bidID)){
			bidNum = Integer.parseInt(bidID);
			// query the DB
			Bid myBid = connectDB.getBidForBidder(userID, bidNum);
			if(myBid == null){
				problems.add("unable to delete, no such bid for user");
				request.setAttribute("problems", problems);
			}else{
				// we have a valid item, kill it
				if(!connectDB.deleteBid(bidNum)){
					problems.add("error connecting to database");
					request.setAttribute("problems", problems);
				}
			}
		}else{
			problems.add("unable to delete, invalid bid id error");
			request.setAttribute("problems", problems);
		}
		// doesn't matter if there was an error, they are going to the same place
//    	System.out.println("deleteBid finished, going to dashboard");
		request.getRequestDispatcher("/currentBids").forward(request, response);
	}
	
	private void acceptBid(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
   	System.out.println("acceptBid started");
        // pull the bid ID
		String bidID = request.getParameter("id");
		int bidNum = 0;
		
		//validate that the bidID is actually a number
		List<String> problems = new ArrayList<>();
		if(StringValidator.validInt(bidID)){
			bidNum = Integer.parseInt(bidID);
			// query the DB
			Bid myBid = connectDB.getBidForSeller(userID, bidNum);
			if(myBid == null){
				problems.add("unable to accept bid, no such bid for user");
				request.setAttribute("problems", problems);
			}else{
				// we have a valid item
				User buyer = connectDB.getUser(myBid.getBuyerID());
				User seller = connectDB.getUser(myBid.getSellerID());
				Item product = connectDB.getProductForUser(myBid.getSellerID(), myBid.getItemID());
				// check and make sure we have meaningful data
				if(buyer != null && seller != null && product != null){
					// check to make sure that there is enough product to fulfill the order
					if(product.getQuantityInStock() < myBid.getQuantity()){
						// don't have enough, so can't accept the bid.
						problems.add("unable to accept bid, insuffient product in stock");
						request.setAttribute("problems", problems);
					}else{
						// we have enough to fulfill the order
						Address billing = seller.getBillingAddress();
						Address shipping;
						if(buyer.getShippingIsBilling()){
							shipping = buyer.getBillingAddress();
						}else{
							shipping = buyer.getShippingAddress();
						}
					
						// subtract the amount bid from the stock of the item
						product.setQuantityInStock(product.getQuantityInStock() - myBid.getQuantity());
						// mark bid as accepted
						myBid.setBidAccepted(true);
						myBid.setBidAcceptedDate(LocalDateTime.now().toString());
						// update the DB
						if(!connectDB.updateProduct(product) || !connectDB.updateBid(myBid)){
							problems.add("unable to accept bid, database access problems");
							request.setAttribute("problems", problems);
						}else{
							double cost = myBid.getProposedPrice() * myBid.getQuantity();
							//generate notifications (String name, String sellerEmail, String productName, int amount, double cost, Address destination)
							mailer.notify_buyer(buyer.getName(), buyer.getEmail(), product.getName(), myBid.getQuantity(), cost, billing);
							mailer.notify_seller(seller.getName(), seller.getEmail(), product.getName(), myBid.getQuantity(), cost, shipping);
						}
					}
				}else{
					problems.add("unable to accept bid, database access problems");
					request.setAttribute("problems", problems);
				}
			}
		}else{
			problems.add("unable to accept bid, invalid bid id error");
			request.setAttribute("problems", problems);
		}
		// doesn't matter if there was an error, they are going to the same place
//    	System.out.println("acceptBid finished, going to dashboard");
		request.getRequestDispatcher("/currentBids").forward(request, response);
	}

	private void rejectBid(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
//   	System.out.println("rejectBid started");
        // pull the bid ID
		String bidID = request.getParameter("id");
		int bidNum = 0;
		
		//validate that the bidID is actually a number
		List<String> problems = new ArrayList<>();
		if(StringValidator.validInt(bidID)){
			bidNum = Integer.parseInt(bidID);
			// query the DB
			Bid myBid = connectDB.getBidForSeller(userID, bidNum);
			if(myBid == null){
				problems.add("unable to reject bid, no such bid for user");
				request.setAttribute("problems", problems);
			}else{
				// we have a valid item
				if(!connectDB.deleteBid(bidNum)){
					problems.add("error connecting to database");
					request.setAttribute("problems", problems);
				}
			}
		}else{
			problems.add("unable to reject bid, invalid bid id error");
			request.setAttribute("problems", problems);
		}
		// doesn't matter if there was an error, they are going to the same place
//    	System.out.println("rejectBid finished, going to dashboard");
		request.getRequestDispatcher("/currentBids").forward(request, response);
		
	}
	
	private void bounceToDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/currentBids").forward(request, response);
	}

	private boolean validateLogin(HttpSession currentSession){
		//every page here should be logged in before accessing, so check for login at start
		if(currentSession!=null){
			// a session exists
			String session_email = (String)currentSession.getAttribute("Email");
			String session_date = (String)currentSession.getAttribute("Date");
			String session_hash = (String)currentSession.getAttribute("Hash");
		    if(session_email != null && !session_email.isEmpty() && 
		    	session_date != null &&!session_date.isEmpty() && 
		    	session_hash != null && !session_hash.isEmpty() && 
		    	checkLoginHash(session_email, session_date, session_hash)){
		    	return true;
		    }
		}
		return false;
	}

	private List<String> validateBid(String sellerID, String itemID, String quantity_requested, String proposed_price){
		List<String> errors = new ArrayList<>();
		if (!StringValidator.validInt(sellerID)){
			errors.add("invalid seller");
		}else if(Integer.parseInt(sellerID) <= 0){
			errors.add("invalid seller");
		}
		if (!StringValidator.validInt(itemID)){
			errors.add("invalid product");
		}else if(Integer.parseInt(itemID) <= 0){
			errors.add("invalid product");
		}
		if (!StringValidator.validInt(quantity_requested)){
			errors.add("Quantity Ordered needs to be an integer, eg: 100");
		}else if(Integer.parseInt(quantity_requested) <= 0){
			errors.add("Quantity Ordered needs to be greater than 0");
		}
		if (!StringValidator.validDouble(proposed_price)){
			errors.add("Cost per unit needs to be an number, eg: 100 or 12.5");
		}else if(Double.parseDouble(proposed_price) <= 0){
			errors.add("Cost per unit needs to be greater than 0");
		}
		return errors;
	}

	private boolean checkLoginHash(String email, String date, String hash){
		//String base64Date = Base64.getEncoder().encodeToString(date.getBytes());
		String validHash = Sha2Crypt.sha256Crypt(email.getBytes(), "$5$" + date);
		return validHash.equals(hash);
	}
}
