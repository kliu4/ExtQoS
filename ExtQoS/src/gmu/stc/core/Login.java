package gmu.stc.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
	
    public Login() {
    	System.out.println("run");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		UserInfo userinfo = (UserInfo) request.getSession().getAttribute("userinfo");

		SAXBuilder builder = new SAXBuilder();
		String name = request.getParameter("username");
		System.out.println("sevlet" + name);
		String pass = request.getParameter("password");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JsonObject obj = new JsonObject();
		try{
			InputStream in = getServletContext().getResourceAsStream("/WEB-INF/classes/Security.xml");
			Document document = (Document) builder.build(in);
			Element rootNode = document.getRootElement();
			List list = rootNode.getChildren("user");			
			Boolean b = false; 
			for(int i = 0; i < list.size(); i++){
				Element node = (Element) list.get(i);
				if(node.getAttribute("name").equals(name)&&node.getAttribute("password").equals(pass))
				{
					b = true;
					obj.addProperty("success", "true");
					break;
				}
			}
			if(!b){
				obj.addProperty("success", "false");
			}
			in.close();
			
		}catch(IOException io){
			System.out.println(io.getMessage());
			obj.addProperty("success", "false");
		}catch(JDOMException jdomex){
			System.out.println(jdomex.getMessage());
			obj.addProperty("success", "false");
		}
		
		out.flush();
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

}
