package cs604;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs604.validators.EmailValidator;
import cs604.validators.StringValidator;

/**
 * Servlet implementation class UserController
 */
@WebServlet(description = "handles account creation for new users", urlPatterns = { "/UserController", "/newUser" })
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserController() {
        super();
        // TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		//doGet(request, response);
		// get the meaningful request attributes
		String param_name = request.getParameter("name");
		String param_email = request.getParameter("email");
		String param_pass = request.getParameter("password");
		
		User possibleUser = new User(param_name, param_email, param_pass);
		// set the request attributes so that we can reference them in error messages
		request.setAttribute("name", param_name);
        request.setAttribute("email", param_email);
        request.setAttribute("password", param_pass);
        
		List<String> problems = validateInput(param_name, param_email, param_pass);
		
		if(!problems.isEmpty()){
			request.setAttribute("problems", problems);
		}else{
			//no problems, commit new user
			possibleUser.commit();
		}
		
		String url = destinationUrl(problems);
		request.getRequestDispatcher(url).forward(request, response);
	}
   	
   	private String destinationUrl(List<String> problems){
   		if(!problems.isEmpty()){
   			return "newUser.jsp";
   		}else{
   			return "Welcome.jsp";
   		}
   	}
	
	public List<String> validateInput(String name, String email, String password){
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
	
}