<%@ page language="java" contentType="text/html; utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextRoot = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>${title}</title>
		
		<jsp:include page="includes/header.jsp"/>
	</head>
	<body>
	    <div class="container">
	        <div class="row">
	            <div class="col-md-4 col-md-offset-4">
	                <div class="login-panel panel panel-default">
	                    <div class="panel-heading">
	                        <h3 class="panel-title">Please Sign In</h3>
	                    </div>
	                    <div class="panel-body">
	                        <form role="form" action="<%=contextRoot%>/login" method="POST">
	                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	                            <fieldset>
	                            	<c:if test="${param.error}">
	                                <div class="alert alert-error">    
					                    Invalid username and password.
					                </div>
					                </c:if>
	                            	<c:if test="${param.logout}">
	                            	<div class="alert alert-success"> 
					                    You have been logged out.
					                </div>
					                </c:if>
	                                <div class="form-group">
	                                    <input class="form-control" placeholder="E-mail" name="username" type="email" autofocus/>
	                                </div>
	                                <div class="form-group">
	                                    <input class="form-control" placeholder="Password" name="password" type="password" value=""/>
	                                </div>
	                                <div class="checkbox">
	                                    <label>
	                                        <input name="remember" type="checkbox" value="Remember Me">Remember Me
	                                    </label>
	                                </div>
	                                <button type="submit" class="btn btn-lg btn-success btn-block">Login</button>
	                            </fieldset>
	                        </form>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
		<jsp:include page="includes/footer.jsp"/>
	</body>
</html>