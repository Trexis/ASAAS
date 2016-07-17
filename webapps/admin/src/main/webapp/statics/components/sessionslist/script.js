if(typeof(asaas)=="undefined") window.asaas = {};
if(typeof(window.asaas.components)=="undefined") window.asaas.components = {};

asaas.components.SessionsList = function(){
	this.container;
	this.params;
};
asaas.components.SessionsList.prototype 		= {};

//must always implement a .init for the asaas library to call it.
asaas.components.SessionsList.prototype.init	= function(container, parameters){
	this.container = container;
	var self = this;
	
	//We only set parameters when component created
	if(typeof(parameters)!="undefined") this.params = parameters;
	
	var url = asaas.servicesctx + "/repositorySession?all=" + this.params.all;
	var dataAjax = $.ajax({
		url: url,
		contentType: "application/json",
		type: 'GET',
		dataType: 'json',
		success: function(response){
			//Convert items to properties, and add additional objects to use
			//in mustache
			$.each(response.data, function(idx,item){
				item.style = (idx==0)?"":"";
				asaas.convertToPropertiesObject(item)
			});

			//Render the list of items using mustach
			var mustache_template = $("script[data-template='sessionslist_template']", container).html();
			var $htmlresults = $(Mustache.to_html(mustache_template, response.data));
			$(".listtable", container).empty();
			$(".listtable", container).append($htmlresults);

			//Bind Edit actions
			$(".listtable tr td .btn-delete", container).click(function(evnt){
				self.deleteItem($(this).data("id"));
			});
			
		},
	    error: function(ajaxErrorObject){
	    	asaas.notify("danger", "Unable to load list of sessions", ajaxErrorObject);
	    }
	});
}


asaas.components.SessionsList.prototype.deleteItem = function(itemid){
	var self = this;
	bootbox.confirm("Are you sure you want to delete this session?", function(result) {
		if(result){
			var url = asaas.servicesctx + "/repositorySession?id="+itemid;
			var success = false;
			var response = $.ajax({
				url: url,
				type: 'DELETE',
				contentType: "application/json; charset=utf-8",
				dataType: 'json',
				async:false,
				success: function(response){
					success = true;
				},
			    error: function(ajaxErrorObject){
			    	asaas.notify("danger", "Unable to delete session", ajaxErrorObject);
			    	success = false;
			    }
			});
			if(success){
		    	asaas.notify("success", "Session deleted.");
				self.init(self.container);
			}
		}
	}); 
};

//This ties it to the model on load.  The asaas library will call .init
//the array value must match the id/name of the component
asaas.model["sessionslist"] = new asaas.components.SessionsList();