<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder, org.springframework.security.core.Authentication" %>
<%@ page import="net.trexis.asaas.web.admin.model.User" %>
<%
    String contextRoot = request.getContextPath();
	
%>
	<script>
		if(typeof(asaas)=="undefined") window.asaas = {};
		asaas.user = {};
		<%
		if(SecurityContextHolder.getContext()!=null){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth!=null){
				User user = (User)auth.getPrincipal();
				%>
				asaas.user = <%=user.getUserdata()%>
				<%
			}
		}
		%>
	</script>    
