<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String contextRoot = request.getContextPath();
%>	
<sec:authorize access="isAuthenticated()">
	<!-- Navigation -->
	<nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
	    <div class="navbar-header">
	        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
	            <span class="sr-only">Toggle navigation</span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	        </button>
	        <a class="navbar-brand" href="dashboard"><img src="statics/theme/logo/trexis.png" class="pull-left"/> Any Site as a Service Dashboard</a>
	    </div>
	    <!-- /.navbar-header -->
	
	    <ul class="nav navbar-top-links navbar-right">

	        <li class="dropdown">
	            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
	                <i class="fa fa-user fa-fw"></i>  <i class="fa fa-caret-down"></i>
	            </a>
	            <ul class="dropdown-menu dropdown-user">
	                <li><a href="javascript:editProfile()"><i class="fa fa-user fa-fw"></i> User Profile</a>
	                </li>
	                <li class="divider"></li>
	                <li><a href="logout"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
	                </li>
	            </ul>
	            <!-- /.dropdown-user -->
	        </li>
	        <!-- /.dropdown -->
	    </ul>
	    <!-- /.navbar-top-links -->
	
	    <div class="navbar-default sidebar" role="navigation">
	        <div class="sidebar-nav navbar-collapse">
	            <ul class="nav" id="side-menu">
	                <li class="active">
	                    <a class="active" href="javascript:asaas.loadComponent('dashboard')"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a>
	                </li>
	                <li>
	                    <a href="javascript:asaas.loadComponent('repositorylist',false,null,{'all':false})"><i class="fa fa-sitemap fa-fw"></i> My Sites</a>
	                </li>
	                <li>
	                    <a href="javascript:asaas.loadComponent('sessionlist',false,null,{'all':true})"><i class="fa fa-files-o fa-fw"></i> Active Sessions</a>
	                </li>
	                <sec:authorize access="hasRole('ADMIN')">
	                <li>
	                    <a href="#"><i class="fa fa-wrench fa-fw"></i> Configuration<span class="fa arrow"></span></a>
	                    <ul class="nav nav-second-level">
	                        <li>
	                            <a href="javascript:asaas.loadComponent('userlist')">System Users</a>
	                        </li>
	                        <li>
	                            <a href="javascript:asaas.loadComponent('repositorylist',false,null,{'all':true})">All Sites</a>
	                        </li>
	                    </ul>
	                    <!-- /.nav-second-level -->
	                </li>
	                </sec:authorize>
	            </ul>
	        </div>
	        <!-- /.sidebar-collapse -->
	    </div>
	    <!-- /.navbar-static-side -->
	</nav>
</sec:authorize>