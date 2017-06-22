package com.cs604;

import java.io.IOException;
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
 * Servlet implementation class ProdListBidController
 */
@WebServlet(description = "handles product actions", 
			urlPatterns = {"/productList", "/newProduct", "/editProduct", "/editProduct2", "/deleteProduct"})
public class ProdController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ConnectDAO connectDB;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProdController() {
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
		String action = request.getServletPath();
		
		//every item here should be logged in before accessing, so we can check for login right now
		HttpSession currentSession = request.getSession(false);
        if(validateLogin(currentSession)){
        	// a session exists, so pull the userID for a valid email address
        	int userID = connectDB.getUserID((String)currentSession.getAttribute("Email"));
        	// all pages reference user information, so save that as part of the request
    		request.setAttribute("user", connectDB.getUser(userID));
        	// if a user is logged in, default should be their landing page
        	switch(action){
    			case "/productList": productList(request, response, userID); break;
    			default: bounceToDashboard(request, response); break;
        	}
        }else{
        	// session info failed. bounce to login
        	request.getRequestDispatcher("Index.jsp").forward(request, response);
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		
		//every item here should be logged in before accessing, so we can check for login right now
		HttpSession currentSession = request.getSession(false);
        if(validateLogin(currentSession)){
        	// a session exists, so pull the userID for a valid email address
        	int userID = connectDB.getUserID((String)currentSession.getAttribute("Email"));
        	// if a user is logged in, default should be their landing page
        	switch(action){
        		case "/productList": productList(request, response, userID); break;
    			case "/newProduct": newProduct(request, response, userID); break;
    			case "/editProduct": editProductPre(request, response, userID); break;
    			case "/editProduct2": editProductPost(request, response, userID); break;
    			case "/deleteProduct": deleteProduct(request, response, userID); break;
    			default: bounceToDashboard(request, response); break;
        	}
        }else{
        	// session info null, bounce to login
    		request.getRequestDispatcher("Index.jsp").forward(request, response);
        }
	}
	
	private void productList(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
//    	System.out.println("productList started");
    	request.setAttribute("sellerItemList", connectDB.listProductsForUser(userID));
    	request.getRequestDispatcher("productlist.jsp").forward(request, response);
	}

	
	private void newProduct(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException, NumberFormatException {
//    	System.out.println("newProduct started");
		// pull the form data
		String i_name = request.getParameter("name");
		String i_description = request.getParameter("desc");
		String i_keywords = request.getParameter("keywords");
		String i_quantityInStock = request.getParameter("StockQuant");

		List<String> problems = new ArrayList<>();
		problems.addAll(validateItem(i_name, i_description, i_keywords, i_quantityInStock));
		
		if(!problems.isEmpty()){
			request.setAttribute("problems", problems);
		}else{
			//Input has no technical problems, creating a new item.
			//pull their existing user info
    		Item newItem = new Item(userID, i_name, i_description, i_keywords, Integer.parseInt(i_quantityInStock));
    		//post to DB
    		int itemID = connectDB.insertProduct(newItem);
    		if(itemID == 0){
				problems.add("database connection error");
				request.setAttribute("problems", problems);
    		}
		}
		
		if(problems.isEmpty()){
//	    	System.out.println("newProduct success, going to dashboard");
			request.getRequestDispatcher("/productList").forward(request, response);
		}else{
//	    	System.out.println("newProduct failure, reloading page");
			request.getRequestDispatcher("product.jsp").forward(request, response);
		}
	}
	
	private void editProductPre(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException, NumberFormatException {
//    	System.out.println("editProductPre started");
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
				// we have a valid item
				request.setAttribute("product", myItem);
			}
		}else{
			problems.add("invalid product id error");
			request.setAttribute("problems", problems);
		}
		
		if(problems.isEmpty()){
//	    	System.out.println("editProductPre success, going to product.jsp");
			request.getRequestDispatcher("product.jsp").forward(request, response);
		}else{
//	    	System.out.println("editProductPre failed, going to dashboard");
			request.getRequestDispatcher("/productList").forward(request, response);
		}
	}
	
	private void editProductPost(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
//    	System.out.println("editProductPost started");
		String itemID = request.getParameter("itemID");
		String i_name = request.getParameter("name");
		String i_description = request.getParameter("desc");
		String i_keywords = request.getParameter("keywords");
		String i_quantityInStock = request.getParameter("StockQuant");
		int itemNum = 0;
				
		List<String> problems = new ArrayList<>();
		problems.addAll(validateItem(i_name, i_description, i_keywords, i_quantityInStock));
		if(StringValidator.validInt(itemID)){
			itemNum = Integer.parseInt(itemID);
		}else{
			problems.add("invalid product id error");
		}
		
		if(!problems.isEmpty()){
			request.setAttribute("problems", problems);
		}else{
			//Input has no technical problems, creating a new item.
    		Item newItem = new Item(userID, i_name, i_description, i_keywords, Integer.parseInt(i_quantityInStock));
    		newItem.setItemID(itemNum);
    		//update DB
    		if(!connectDB.updateProduct(newItem)){
				problems.add("database connection error");
				request.setAttribute("problems", problems);
    		}
		}
		
		if(problems.isEmpty()){
//	    	System.out.println("editProductPost success, going to dashboard");
			request.getRequestDispatcher("/productList").forward(request, response);
		}else{
//	    	System.out.println("editProductPost failed, going to reload");
			request.getRequestDispatcher("product.jsp").forward(request, response);
		}
	}
	
	private void deleteProduct(HttpServletRequest request, HttpServletResponse response, int userID) throws ServletException, IOException {
//    	System.out.println("deleteProduct started");
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
				// we have a valid item, test for dependencies
				List<Listing> userLists = connectDB.listAllUserListing(userID);
				boolean conflict = false;
				for(Listing myList : userLists){
					if(myList.getProdID() == itemNum) conflict = true;
				}
				if(conflict){
					problems.add("Product has listings dependent on it, please remove them and their bids first");
					request.setAttribute("problems", problems);
				}else if(!connectDB.deleteProduct(itemNum)){ //kill it
					problems.add("error connecting to database");
					request.setAttribute("problems", problems);
				}
			}
		}else{
			problems.add("invalid product id error");
			request.setAttribute("problems", problems);
		}
		
		// doesn't matter if there was an error, they are going to the same place
//    	System.out.println("deleteProduct finished, going to dashboard");
		request.getRequestDispatcher("/productList").forward(request, response);
		
	}
	
	private void bounceToDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/myAccount").forward(request, response);
	}



	private boolean checkLoginHash(String email, String date, String hash){
		//String base64Date = Base64.getEncoder().encodeToString(date.getBytes());
		String validHash = Sha2Crypt.sha256Crypt(email.getBytes(), "$5$" + date);
		return validHash.equals(hash);
	}
	
	private List<String> validateItem(String name, String desc, String keywords, String amountInStock){
		List<String> errors = new ArrayList<>();
		if (!StringValidator.validate(name)){
			errors.add("Name is mandatory");
		}
		if (!StringValidator.validate(desc)){
			errors.add("Description is mandatory");
		}
		//TODO: add validator for keywords
//		if (!StringValidator.validate(keywords)){
//			errors.add("password is mandatory");
//		}
		if (!StringValidator.validInt(amountInStock)){
			errors.add("Amount in Stock needs to be an integer, eg: 100");
		}else if(Integer.parseInt(amountInStock) <= 0){
			errors.add("Amount in Stock needs to be greater than 0");
		}

		return errors;
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

}
