window.asaas = {};
window.asaas.container = "#page-wrapper";
window.asaas.model = [];
window.asaas.loadComponent = function(componentName, append){
	var url = asaas.ctx + "/component/" + componentName;
	var dataAjax = $.ajax({
		url: url,
		type: 'GET',
		dataType: 'json',
		success: function(response){
            var clearcontainer = true;
            if(typeof(append)!="undefined"){
            	if(append==true) clearcontainer=false;
            }
            if(clearcontainer){
            	$(asaas.container).fadeOut(300, function() { 
            		$(this).empty(); 
            	});
            }
            var htmlcontainer=$(response.data.html);
            $(asaas.container).fadeIn(300, function() { 
        		$(this).append(htmlcontainer); 
        	});
            
            $.each(response.data.dependencies, function(url, type) {
                if(type=="js"){
                	var script = document.createElement("script");
                	script.type = 'text/javascript';
                	script.src = asaas.ctx + url;
                	$(asaas.container).append(script);
                	asaas.model[componentName].init(htmlcontainer);
                } else {
                	var link=document.createElement("link");
                	link.href= asaas.ctx + url;
                	$(asaas.container).append(link);
                }
            });
		},
	    error: function(ajaxErrorObject){
	    	console.log(ajaxErrorObject);
	    	asaas.notify("danger", "Unable to load " + componentName, ajaxErrorObject.responseJSON.data);
	    }
	});
	 
}

window.asaas.notify = function(type, message, reason){
	var html = "<div class='alert alert-" + type + " alert-dismissible' role='alert'>"
	html += "<button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button>";
	html += message
	html += "</div>";
	
	var $alert = $(html);
	$(asaas.container).prepend($alert);
	$alert.delay(5000).fadeOut(400)
}
