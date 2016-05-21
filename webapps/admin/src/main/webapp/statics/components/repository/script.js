if(typeof(asaas)=="undefined") window.asaas = {};
if(typeof(window.asaas.components)=="undefined") window.asaas.components = {};

asaas.components.Repository = function(){
	this.container;
};
asaas.components.Repository.prototype 		= {};
//must implement a .init for the asaas library to call it.
asaas.components.Repository.prototype.init	= function(container, parameters){

	var mustache_template = $("script[data-template='repositoryitem_template']", container).html();
	var $formcontainer = $(".form-horizontal", container);
	$formcontainer.empty();

	if(parameters.id==null){
		var $htmlresults = $(Mustache.to_html(mustache_template, {}));
		$formcontainer.append($htmlresults);
	} else {
		var url = asaas.servicesctx + "/repository?id=" + parameters.id;
		var dataAjax = $.ajax({
			url: url,
			type: 'GET',
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
				
				
			},
		    error: function(ajaxErrorObject){
				console.log(this)
		    	asaas.notify("danger", "Unable to load repository", ajaxErrorObject);
		    }
		});
		
	}
}

//This ties it to the model on load.  The asaas library will call .init
//the array value must match the id/name of the component
asaas.model["repository"] = new asaas.components.Repository();