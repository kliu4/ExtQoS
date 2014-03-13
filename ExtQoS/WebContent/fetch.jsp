<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="website" scope="request" type="gmu.stc.core.WebSite" />
<jsp:useBean id="userInfo" scope="session" class="gmu.stc.core.UserInfo" />
<%@ page import="gmu.stc.core.CswFetcher" %>
<%@ page import="gmu.stc.core.FetchManager" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fetch the Web Services</title>
</head>
<body>
	<% 
	if(userInfo.getPermission()!=1){
		return;
	}
	String id = request.getParameter("id");
	String taskName = request.getParameter("taskname");
	String url = request.getParameter("url");
	String method = request.getParameter("method");
	//String isprivate = request.getParameter("isprivate");
	String saveas = request.getParameter("saveas");
	
	/* CswFetcher cswfetcher = new CswFetcher(website, taskName, url, method, isprivate, saveas);
	cswfetcher.createAtom(id, website.getRootPath()+saveas); */
	FetchManager fm = new FetchManager(website, id, taskName, url, method, "public", saveas, "24", website.getRootPath()+saveas);
	fm.start();
	

	%>
	
</body>
</html>