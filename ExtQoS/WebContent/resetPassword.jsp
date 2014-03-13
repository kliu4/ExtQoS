<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:useBean id="website" scope="request" type="gmu.stc.core.WebSite" />
<jsp:useBean id="userInfo" scope="session" class="gmu.stc.core.UserInfo" />
<%
	if (userInfo.getPermission() != 1) {
		return;
	}
	String result;
	String currentPassword = request.getParameter("currentPassword");
	String newPassword = request.getParameter("newPassword");
	String reNewPassword = request.getParameter("reNewPassword");

	boolean b = userInfo.load(website, "admin", currentPassword);
	if (b) {
		if (!newPassword.equals(reNewPassword))
			result = "Error: you entered two different new password";
		else {
			userInfo.resetPassword(website, "admin", newPassword);
			result = "Success";
		}
	} else
		result = "Error: your current password is not correct";
%>
<%=result%>