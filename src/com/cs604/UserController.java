package com.cs604;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cs604.validators.EmailValidator;
import com.cs604.validators.StringValidator;

/**
 * Servlet implementation class UserController
 */
@WebServlet(description = "handles users interaction", urlPatterns = { "/UserController", "/newUser" })
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	// post requests are where we are going to be doing the work
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		
		switch(action){
			case "/newUser": parseNewUserForm(request, response); break;
			case "/login": parseLoginPage(request, response); break;
			default: parseWelcomePage(request, response); break;
		}
	}
		
	private void parseNewUserForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get the meaningful request attributes		
		String p_name = request.getParameter("name");
		String p_email = request.getParameter("email");
		String p_pass = request.getParameter("password");
		boolean p_Buyer = (request.getParameter("Buyer") != null && request.getParameter("Buyer").equals(""));
		boolean p_Seller = (request.getParameter("Seller") != null && request.getParameter("Seller").equals(""));
		String p_Bill_Street1 = request.getParameter("Bill_Street1");
		String p_Bill_Street2 = request.getParameter("Bill_Street2");
		String p_Bill_City = request.getParameter("Bill_City");
		String p_Bill_State = request.getParameter("Bill_State");
		String p_Bill_Zip = request.getParameter("Bill_Zip");
		String p_Bill_Country = request.getParameter("Bill_Country");
		boolean p_SameShip = (request.getParameter("SameShip") != null && request.getParameter("SameShip").equals(""));
		String p_Ship_Street1 = request.getParameter("Ship_Street1");
		String p_Ship_Street2 = request.getParameter("Ship_Street2");
		String p_Ship_City = request.getParameter("Ship_City");
		String p_Ship_State = request.getParameter("Ship_State");
		String p_Ship_Zip = request.getParameter("Ship_Zip");
		String p_Ship_Country = request.getParameter("Ship_Country");
		
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
			Address shipAddr = new Address(p_name, p_Ship_Street1, p_Ship_Street2, p_Ship_City, p_Ship_State, p_Ship_Zip, p_Ship_Country);
			User possibleUser = new User(p_name, p_email, p_pass, billAddr, shipAddr, p_Buyer, p_Seller, p_SameShip);
			
			int userID = connectDB.insertUser(possibleUser);
			if(userID == 0){
				problems.add("database connection error");
				request.setAttribute("problems", problems);
			}
		}
		
		String url = destinationUrl(problems);
		request.getRequestDispatcher(url).forward(request, response);
	}
	

	private void parseLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private void parseWelcomePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
   	
   	private String destinationUrl(List<String> problems){
   		if(!problems.isEmpty()){
   			return "newUser.jsp";
   		}else{
   			return "Welcome.jsp";
   		}
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
}