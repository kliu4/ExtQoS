package gmu.stc.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class GetTasks
 */
@WebServlet("/GetTasks")
public class GetTasks extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTasks() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		SAXBuilder builder = new SAXBuilder();
		String name = request.getParameter("username");
		String pass = request.getParameter("password");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JsonObject obj = new JsonObject();
		obj.addProperty("expanded", "true");
		obj.addProperty("text", "");
		obj.addProperty("user", "");
		obj.addProperty("status", "");
		try{
			InputStream in = getServletContext().getResourceAsStream("/WEB-INF/classes/Tasks.xml");
			Document document = (Document) builder.build(in);
			Element rootNode = document.getRootElement();
			List list = rootNode.getChildren("user");			 
			for(int i = 0; i < list.size(); i++){
				Element node = (Element) list.get(i);
				if(node.getAttribute("user").equals(name)&&node.getAttribute("password").equals(pass))
				{
					
				}
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

}
