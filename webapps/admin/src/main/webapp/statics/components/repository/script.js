if(typeof(asaas)=="undefined") window.asaas = {};
if(typeof(window.asaas.components)=="undefined") window.asaas.components = {};

asaas.components.Repository = function(){
	this.container;
	this.id;
};
asaas.components.Repository.prototype 		= {};
//must implement a .init for the asaas library to call it.
asaas.components.Repository.prototype.init	= function(container, parameters){
	this.container = container;
	var self = this;
	
	var mustache_template = $("script[data-template='repositoryitem_template']", container).html();
	var $formcontainer = $(".form-horizontal", container);
	$formcontainer.empty();

	if(parameters.id==-1){
		var $htmlresults = $(Mustache.to_html(mustache_template, {}));
		$formcontainer.append($htmlresults);
		this.id=-1;
	} else {
		this.id=parameters.id;
		var url = asaas.servicesctx + "/repository?id=" + parameters.id;
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
		    	asaas.notify("danger", "Unable to load site", ajaxErrorObject);
		    }
		});
		
	}
}

asaas.components.Repository.prototype.setCheckboxes = function(container, item){
	if(typeof(item.properties.enableapimanagement)!="undefined" && item.properties.enableapimanagement=="false") $(".itemapimanagement", container).removeAttr("checked");
	if(typeof(item.properties.enablecache)!="undefined" && item.properties.enablecache=="false") $(".itemcache", container).removeAttr("checked");
	if(typeof(item.properties.enablesession)!="undefined" && item.properties.enablesession=="false") $(".itemsessionmanagement", container).removeAttr("checked");
}

//This is syncronize to allow followup actions without callback
asaas.components.Repository.prototype.save = function(){
	var item = {};
	item.name = $("#itemname", this.container).val();
	item.id=this.id;
	item.properties = [];
	item.properties[0] = {name:"description", value:$("#itemdescription", this.container).val()};
	item.properties[1] = {name:"homeurl", value:$("#itemurl", this.container).val()};
	item.properties[2] = {name:"loginurl", value:$("#itemloginurl", this.container).val()};
	item.properties[3] = {name:"enablesession", value:$(".itemsessionmanagement", this.container).is(':checked')};
	item.properties[4] = {name:"enablecache", value:$(".itemcache", this.container).is(':checked')};
	item.properties[5] = {name:"enableapimanagement", value:$(".itemapimanagement", this.container).is(':checked')};

	var url = asaas.servicesctx + "/repository";
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
	    	asaas.notify("danger", "Unable to save site", ajaxErrorObject);
	    	success = false;
	    }
	});
	
	return success;
}

//This ties it to the model on load.  The asaas library will call .init
//the array value must match the id/name of the component
asaas.model["repository"] = new asaas.components.Repository();