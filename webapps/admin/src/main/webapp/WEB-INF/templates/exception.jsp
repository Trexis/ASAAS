<%@ page language="java" contentType="text/html; utf-8" pageEncoding="utf-8"%>
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
	            <div class="col-lg-12">
	                <h1 class="page-header">Something went wrong</h1>
	                <h2>${message}</h2>
	                <p>${cause}</p>
	            </div>
	            <!-- /.col-lg-12 -->
	        </div>
	        <!-- /.row -->
	    </div>
        <!-- /.container -->
		<jsp:include page="includes/footer.jsp"/>
	</body>
</html>