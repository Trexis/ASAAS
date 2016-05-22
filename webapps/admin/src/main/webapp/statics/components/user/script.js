if(typeof(asaas)=="undefined") window.asaas = {};
if(typeof(window.asaas.components)=="undefined") window.asaas.components = {};

asaas.components.User = function(){
	this.container;
	this.id;
};
asaas.components.User.prototype 		= {};
//must implement a .init for the asaas library to call it.
asaas.components.User.prototype.init	= function(container, parameters){
	this.container = container;
	var self = this;
	
	var mustache_template = $("script[data-template='useritem_template']", container).html();
	var $formcontainer = $(".form-horizontal", container);
	$formcontainer.empty();

	if(parameters.id==-1){
		var $htmlresults = $(Mustache.to_html(mustache_template, {}));
		$formcontainer.append($htmlresults);
		this.id=-1;
	} else {
		this.id=parameters.id;
		var url = asaas.servicesctx + "/user?id=" + parameters.id;
		var dataAjax = $.ajax({
			url: url,
			type: 'GET',
			contentType: "application/json",
			dataType: 'json',
			success: function(response){
				//Convert items to properties, and add additional objects to use
				//in mustache
				var item = response.data;
				item.style = "";
				asaas.convertToPropertiesObject(item)

				//Render the list of items using mustach
				var $htmlresults = $(Mustache.to_html(mustache_template, response.data));
				$formcontainer.append($htmlresults);
				self.setCheckboxes($htmlresults, item);
				
			},
		    error: function(ajaxErrorObject){
		    	asaas.notify("danger", "Unable to load User", ajaxErrorObject);
		    }
		});
		
	}
}

asaas.components.User.prototype.setCheckboxes = function(container, item){
	if(typeof(item.active)!="undefined" && !item.active) $(".itemactive", container).removeAttr("checked");
	if(typeof(item.authority.role)!="undefined" && item.authority.role!="ROLE_ADMIN") $(".itemadmin", container).removeAttr("checked");
}

//This is syncronize to allow followup actions without callback
asaas.components.User.prototype.save = function(){
	var item = {};
	item.id=this.id;
	item.username = $("#itemname", this.container).val();
	item.password = $("#itempassword", this.container).val();
	item.active = $(".itemactive", this.container).is(':checked');
	item.authority = {};
	item.authority.role = $(".itemadmin", this.container).is(':checked')?"ROLE_ADMIN":"ROLE_USER";

	var url = asaas.servicesctx + "/user";
	var success = false;
	var response = $.ajax({
		url: url,
		type: 'POST',
		contentType: "application/json; charset=utf-8",
		dataType: 'json',
		data:JSON.stringify(item),
		async:false,
		success: function(response){
			success = true;
		},
	    error: function(ajaxErrorObject){
	    	asaas.notify("danger", "Unable to save user", ajaxErrorObject);
	    	success = false;
	    }
	});
	
	return success;
}

//This ties it to the model on load.  The asaas library will call .init
//the array value must match the id/name of the component
asaas.model["user"] = new asaas.components.User();