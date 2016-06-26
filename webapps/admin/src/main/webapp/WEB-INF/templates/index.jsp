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
		
		
		<style>
			body{
				background-color:black;
				color:white;
			}
			.wizard {
			    margin: 20px auto;
			    background: #fff;
			}
		
		    .wizard .nav-tabs {
		        position: relative;
		        margin: 40px auto;
		        margin-bottom: 0;
		        border-bottom-color: #e0e0e0;
		    }
		
		    .wizard > div.wizard-inner {
		        position: relative;
		    }
			
			.connecting-line {
			    height: 2px;
			    background: #e0e0e0;
			    position: absolute;
			    width: 80%;
			    margin: 0 auto;
			    left: 0;
			    right: 0;
			    top: 50%;
			    z-index: 1;
			}
			
			.wizard .nav-tabs > li.active > a, .wizard .nav-tabs > li.active > a:hover, .wizard .nav-tabs > li.active > a:focus {
			    color: #555555;
			    cursor: default;
			    border: 0;
			    border-bottom-color: transparent;
			}
			
			span.round-tab {
			    width: 70px;
			    height: 70px;
			    line-height: 70px;
			    display: inline-block;
			    border-radius: 100px;
			    background: #fff;
			    border: 2px solid #e0e0e0;
			    z-index: 2;
			    position: absolute;
			    left: 0;
			    text-align: center;
			    font-size: 25px;
			}
			span.round-tab i{
			    color:#555555;
			}
			.wizard li.active span.round-tab {
			    background: #fff;
			    border: 2px solid #5bc0de;
			    
			}
			.wizard li.active span.round-tab i{
			    color: #5bc0de;
			}
			
			span.round-tab:hover {
			    color: #333;
			    border: 2px solid #333;
			}
			
			.wizard .nav-tabs > li {
			    width: 25%;
			}
			
			.wizard li:after {
			    content: " ";
			    position: absolute;
			    left: 46%;
			    opacity: 0;
			    margin: 0 auto;
			    bottom: 0px;
			    border: 5px solid transparent;
			    border-bottom-color: #5bc0de;
			    transition: 0.1s ease-in-out;
			}
			
			.wizard li.active:after {
			    content: " ";
			    position: absolute;
			    left: 46%;
			    opacity: 1;
			    margin: 0 auto;
			    bottom: 0px;
			    border: 10px solid transparent;
			    border-bottom-color: #5bc0de;
			}
			
			.wizard .nav-tabs > li a {
			    width: 70px;
			    height: 70px;
			    margin: 20px auto;
			    border-radius: 100%;
			    padding: 0;
			}
			
			    .wizard .nav-tabs > li a:hover {
			        background: transparent;
			    }
			
			.wizard .tab-pane {
			    position: relative;
			    padding-top: 50px;
			}
			
			.wizard h3 {
			    margin-top: 0;
			}
			
			@media( max-width : 585px ) {
			
			    .wizard {
			        width: 90%;
			        height: auto !important;
			    }
			
			    span.round-tab {
			        font-size: 16px;
			        width: 50px;
			        height: 50px;
			        line-height: 50px;
			    }
			
			    .wizard .nav-tabs > li a {
			        width: 50px;
			        height: 50px;
			        line-height: 50px;
			    }
			
			    .wizard li.active:after {
			        content: " ";
			        position: absolute;
			        left: 35%;
			    }
			}
			
			.circleheader {
			    display: block;
			    width: 190px;
			    height: 190px;
			    border:1px solid black;
			    background: white;
			    -moz-border-radius: 20px;
			    -webkit-border-radius: 20px;
			    background-image: url(statics/theme/logo/logo-128.png);
			    background-position:50% 50%;
			    background-repeat:no-repeat;   
			    margin: 0 auto; 
			}
			
		</style>
		
	</head>
	<body>
	    <div class="container">
	        <div class="row">
	            <div class="col-lg-12">
	            	<br/><br/>
	            	
	            	<div class="circleheader img-circle"></div>
	            	
	                <h1 class="text-center">Any Site as a Service</h1>
	                
	                <br/><br/>
	                
	                <div class="row">
	                	<div class="col-lg-5 col-md-6">
	                		<h3>Give us a try</h3>
	                		
                            <div class="form-group">
                                <input class="form-control" placeholder="Enter any website URL here. e.g. http://www.google.com" type="text"/>
                            </div>
                            <div class="form-group">
                            	Any site as a service allow any audience (managers and developers) to gain access to existing websites, and have it returned as a service.
                            	<br/>
                            	Enter any website URL above, and see us in action!
                            </div>
                            <button type="button" class="btn btn-lg btn-success btn-block">Show me</button>
                            
   	                	</div>
	                	<div class="col-lg-1 visible-lg-block" style="border-right:1px solid white">
	                	 	<div style="height:300px;width:2px" class="text-center">&nbsp;</div>
	                	</div>
	                	<div class="col-lg-1 visible-lg-block">
	                	</div>
	                	<div class="col-lg-5 col-md-6">
	                		<h3>Login to your dashboard</h3>
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
	                
	                
					<section class="hidden">
				        <div class="wizard">
				            <div class="wizard-inner">
				                <div class="connecting-line"></div>
				                <ul class="nav nav-tabs" role="tablist">
				
				                    <li role="presentation" class="active">
				                        <a href="#step1" data-toggle="tab" aria-controls="step1" role="tab" title="Step 1">
				                            <span class="round-tab">
				                                <i class="glyphicon glyphicon-folder-open"></i>
				                            </span>
				                        </a>
				                    </li>
				
				                    <li role="presentation" class="disabled">
				                        <a href="#step2" data-toggle="tab" aria-controls="step2" role="tab" title="Step 2">
				                            <span class="round-tab">
				                                <i class="glyphicon glyphicon-pencil"></i>
				                            </span>
				                        </a>
				                    </li>
				                    <li role="presentation" class="disabled">
				                        <a href="#step3" data-toggle="tab" aria-controls="step3" role="tab" title="Step 3">
				                            <span class="round-tab">
				                                <i class="glyphicon glyphicon-picture"></i>
				                            </span>
				                        </a>
				                    </li>
				
				                    <li role="presentation" class="disabled">
				                        <a href="#complete" data-toggle="tab" aria-controls="complete" role="tab" title="Complete">
				                            <span class="round-tab">
				                                <i class="glyphicon glyphicon-ok"></i>
				                            </span>
				                        </a>
				                    </li>
				                </ul>
				            </div>
				
				            <form role="form">
				                <div class="tab-content">
				                    <div class="tab-pane active" role="tabpanel" id="step1">
				                        <h3>Step 1</h3>
				                        <p>This is step 1</p>
				                        <ul class="list-inline pull-right">
				                            <li><button type="button" class="btn btn-primary next-step">Save and continue</button></li>
				                        </ul>
				                    </div>
				                    <div class="tab-pane" role="tabpanel" id="step2">
				                        <h3>Step 2</h3>
				                        <p>This is step 2</p>
				                        <ul class="list-inline pull-right">
				                            <li><button type="button" class="btn btn-default prev-step">Previous</button></li>
				                            <li><button type="button" class="btn btn-primary next-step">Save and continue</button></li>
				                        </ul>
				                    </div>
				                    <div class="tab-pane" role="tabpanel" id="step3">
				                        <h3>Step 3</h3>
				                        <p>This is step 3</p>
				                        <ul class="list-inline pull-right">
				                            <li><button type="button" class="btn btn-default prev-step">Previous</button></li>
				                            <li><button type="button" class="btn btn-default next-step">Skip</button></li>
				                            <li><button type="button" class="btn btn-primary btn-info-full next-step">Save and continue</button></li>
				                        </ul>
				                    </div>
				                    <div class="tab-pane" role="tabpanel" id="complete">
				                        <h3>Complete</h3>
				                        <p>You have successfully completed all steps.</p>
				                    </div>
				                    <div class="clearfix"></div>
				                </div>
				            </form>
				        </div>
			    	</section>

					<div class="text-center">
	                	<h3>This is still in beta, and under development</h3>
	                	<p>No subscriptions is allowed at this stage</p>
	                	<p>For any questions, please email asaas@trexis.net</p>
					</div>
					
					<div style="margin: 0 auto;" class="text-center"><a href="http://www.trexis.net"><img src="statics/theme/logo/Trexis Inverted.png" alt="Proud product of treXis"/></a></div>

		        </div>
	            <!-- /.col-lg-12 -->
		    	
	        </div>
	        <!-- /.row -->
	    </div>
        <!-- /.container -->
		<jsp:include page="includes/footer.jsp"/>
		<script type="text/javascript">
			$(document).ready(function () {
			    //Initialize tooltips
			    $('.nav-tabs > li a[title]').tooltip();
			    
			    //Wizard
			    $('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
	
			        var $target = $(e.target);
			    
			        if ($target.parent().hasClass('disabled')) {
			            return false;
			        }
			    });
	
			    $(".next-step").click(function (e) {
	
			        var $active = $('.wizard .nav-tabs li.active');
			        $active.next().removeClass('disabled');
			        nextTab($active);
	
			    });
			    $(".prev-step").click(function (e) {
	
			        var $active = $('.wizard .nav-tabs li.active');
			        prevTab($active);
	
			    });
			});
	
			function nextTab(elem) {
			    $(elem).next().find('a[data-toggle="tab"]').click();
			}
			function prevTab(elem) {
			    $(elem).prev().find('a[data-toggle="tab"]').click();
			}
		</script>
	</body>
</html>