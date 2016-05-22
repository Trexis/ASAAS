window.asaas = {};
window.asaas.model = [];
window.asaas.loadComponent = function(componentName, append, container, parameters){
	var $component_container = $(asaas.components_container_name);
	if(typeof(container)!="undefined"&&container!=null) $component_container = container;
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
            	$component_container.fadeOut(300, function() { 
            		$(this).empty(); 
            	});
            }
            var $htmlcontainer=$(response.data.html);
            $component_container.fadeIn(300, function() { 
        		$(this).append($htmlcontainer); 
        	});
            
            $.each(response.data.dependencies, function(url, type) {
                if(type=="js"){
                	var script = document.createElement("script");
                	script.type = 'text/javascript';
                	script.src = asaas.ctx + url;
                	$component_container.append(script);
                } else {
                	var link=document.createElement("link");
                	link.href= asaas.ctx + url;
                	$component_container.append(link);
                }
            });

            window.setTimeout(function(){
            	asaas.model[componentName].init($htmlcontainer, parameters);
            }, 1000);
		},
	    error: function(ajaxErrorObject){
	    	asaas.notify("danger", "Unable to load " + componentName, ajaxErrorObject);
	    }
	});
	 
};

window.asaas.notify = function(type, message, input, optionalContainer){
	if(asaas.debug&&typeof(input)!="undefined") console.log(input);
	
	var $notifycontainer = $(asaas.notifications_container_name);
	if(typeof(optionalContainer)!="undefined") notifycontainer = optionalContainer;
	
	var reason = input;
	if(typeof(input)=="OBJECT"){
		reason = "";
		if(typeof(input.responseJSON)!="undefined"){
			reason += input.responseJSON.data;
		} else {
			if(typeof(input.responseText)!=""){
				reason += input.responseText;
			}
		}
	}
	
	var html = "<div class='alert alert-" + type + " alert-dismissible' role='alert'>";
	html += "<button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button>";
	html += message;
	html += "</div>";
	
	var $alert = $(html);
	$notifycontainer.prepend($alert);
	
	//automatically close success messages after 5 seconds
	if(type=="success") $alert.delay(5000).fadeOut(400);
};

window.asaas.convertToPropertiesObject = function(item){
	var newproperties = {};
	$.each(item.properties, function(idx,property){
		newproperties[property.name] = property.value;
	});
	item.properties = newproperties;
};

window.asaas.loadModalPopup = function(modelId, modalTitle){

	var $modal = $("." + modelId);
	$(".modal-title", $modal).html(modalTitle);
	var $body = $(".modal-body", $modal);
	$body.html("Loading...")
	$modal.modal('show');

	//set the new location for notifications to the body of the modal dialog
	var current_notifications_container_name = asaas.notifications_container_name;
	asaas.notifications_container_name = "." + modelId + " .modal-body";
	$modal.on('hidden.bs.modal', function () {
		//Set the notifications area back to where it was before this modal was loaded
		asaas.notifications_container_name = current_notifications_container_name;
		$(this).data('bs.modal', null);
	})
	
	return $modal;
};