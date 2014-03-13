<%@ page language="java" pageEncoding="UTF-8"%><jsp:useBean id="website" scope="request" type="gmu.stc.core.WebSite" /><jsp:useBean id="userInfo" scope="session" class="gmu.stc.core.UserInfo" />
<%
	String result;
	String loginUsername = request.getParameter("username");
	String loginPassword = request.getParameter("password");
	if (null == loginUsername) {
		boolean b = userInfo.load(website, "admin", loginPassword);
		if (b)
			result = "{success:true}";
		else
			result = "Login failed.Try again";
 
	} else {
		result = "Login failed.Try again";
	}
%><%=result %>