if(typeof(asaas)=="undefined") window.asaas = {};
if(typeof(window.asaas.components)=="undefined") window.asaas.components = {};

asaas.components.RepositoryView = function(){
	this.container;
};
asaas.components.RepositoryView.prototype 		= {};

//must always implement a .init for the asaas library to call it.
asaas.components.RepositoryView.prototype.init	= function(container, parameters){
	this.container = container;
	var self = this;
	
	//We only set parameters when component created
	if(typeof(parameters)!="undefined") this.params = parameters;
	
	var url = asaas.servicesctx + "/service/" + this.params.name;
	var dataAjax = $.ajax({
		url: url,
		contentType: "text/html",
		type: 'GET',
		dataType: 'html',
		success: function(response){
			$(".htmlframe", container).contents().find('html').html(response);
		},
	    error: function(ajaxErrorObject){
	    	asaas.notify("danger", "Unable to view site", ajaxErrorObject);
	    }
	});

	
}

//This ties it to the model on load.  The asaas library will call .init
//the array value must match the id/name of the component
asaas.model["repositoryview"] = new asaas.components.RepositoryView();