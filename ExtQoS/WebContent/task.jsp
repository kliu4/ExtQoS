<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:useBean id="userInfo" scope="session" class="gmu.stc.core.UserInfo" />
<%@ page import="gmu.stc.core.TaskManager" %>
<%@ page import="gmu.stc.core.WebSite" %>
<%
	String result="";
	if (userInfo.getPermission() != 1) {
		return;
	}
	WebSite website = (WebSite)request.getAttribute("website");
	System.out.println(website.getRootPath() + "entry.xml");
	TaskManager  tm = new TaskManager(website.getRootPath() + "entry.xml");
	String service = request.getParameter("service");
	if ("GetMaxID".equals(service)) {		
		result = tm.getMaxID("admin");
	} else if ("Add".equals(service)) {	
		String maxID = tm.getMaxID("admin");
		maxID = Integer.toString(Integer.parseInt(maxID) + 1);
		String name = request.getParameter("name");
		String url = request.getParameter("url");
		String method = request.getParameter("method");
		//String isprivate = request.getParameter("isprivate");
		String saveas = request.getParameter("saveas");
		tm.Add("admin", maxID, name, url, method, "public", saveas);
	}else if ("Remove".equals(service)) {	
		String id = request.getParameter("id");		
		tm.remove("admin", id);
	}else if ("Update".equals(service)) {	
		String maxID = tm.getMaxID("admin");
		maxID = Integer.toString(Integer.parseInt(maxID) + 1);
		String name = request.getParameter("name");
		String url = request.getParameter("url");
		String method = request.getParameter("method");
		//String isprivate = request.getParameter("isprivate");
		String saveas = request.getParameter("saveas");
		tm.update("admin", maxID, name, url, method, "public", saveas);
	}
%>
<%=result%>