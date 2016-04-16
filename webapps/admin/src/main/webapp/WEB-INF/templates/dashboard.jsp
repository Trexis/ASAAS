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
		<div id="wrapper">
		
			<jsp:include page="includes/navigation.jsp"/>
		
	        <!-- Page Content -->
	        <div id="page-wrapper">
	            <div class="container-fluid">
	                <div class="row">
	                    <div class="col-lg-12">
	                        <h1 class="page-header">Dashboard</h1>
	                    </div>
	                    <!-- /.col-lg-12 -->
	                </div>
	                <!-- /.row -->
	            </div>
	            <!-- /.container-fluid -->
	        </div>
	        <!-- /#page-wrapper -->
	
	    </div>
	    <!-- /#wrapper -->	
		<jsp:include page="includes/footer.jsp"/>
	</body>
</html>