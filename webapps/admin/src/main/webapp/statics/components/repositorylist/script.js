if(typeof(asaas)=="undefined") window.asaas = {};
if(typeof(window.asaas.components)=="undefined") window.asaas.components = {};

asaas.components.RepositoryList = function(){
	this.container;
	this.params;
};
asaas.components.RepositoryList.prototype 		= {};

//must always implement a .init for the asaas library to call it.
asaas.components.RepositoryList.prototype.init	= function(container, parameters){
	this.container = container;
	var self = this;
	
	//We only set parameters when component created
	if(typeof(parameters)!="undefined") this.params = parameters;
	
	var url = asaas.servicesctx + "/repository?all=" + this.params.all;
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
			var mustache_template = $("script[data-template='repositorylist_template']", container).html();
			var $htmlresults = $(Mustache.to_html(mustache_template, response.data));
			$(".list-group", container).empty();
			$(".list-group", container).append($htmlresults);

			//Bind view actions
			$(".list-group .list-group-item .btn-view", container).click(function(evnt){
				self.viewItem($(this).data("id"),$(this).data("name"));
			});
			
			//Bind Edit actions
			$(".list-group .list-group-item .btn-edit", container).click(function(evnt){
				self.editItem($(this).data("id"));
			});
			
			//Bind Edit actions
			$(".list-group .list-group-item .btn-delete", container).click(function(evnt){
				self.deleteItem($(this).data("id"));
			});

			//Bind create button
			$(".btn-new", container).click(function(evnt){
				self.editItem(-1);
			});
			
		},
	    error: function(ajaxErrorObject){
	    	asaas.notify("danger", "Unable to load list of sites", ajaxErrorObject);
	    }
	});
}

asaas.components.RepositoryList.prototype.viewItem	= function(itemid, itemname){
	var self = this;
	var $modal = asaas.loadModalPopup("viewItemModal", "View Site");
	var $modalbody = $(".modal-body", $modal);
	window.setTimeout(function(){
		asaas.loadComponent("repositoryview", false, $modalbody, {"id":itemid,"name":itemname});
	},1);
};


asaas.components.RepositoryList.prototype.editItem	= function(itemid){
	var self = this;
	var $modal = asaas.loadModalPopup("saveItemModal", "Edit Site");
	var $modalbody = $(".modal-body", $modal);
	window.setTimeout(function(){
		asaas.loadComponent("repository", false, $modalbody, {"id":itemid});
		$(".btn-save", $modal).unbind('click').bind('click', function (e) {
			var success = asaas.model["repository"].save();
			if(success){
				$(".btn-close", $modal).click();
		    	asaas.notify("success", "Site saved.");
				self.init(self.container);
			}
		});
	},1);
};

asaas.components.RepositoryList.prototype.deleteItem = function(itemid){
	var self = this;
	bootbox.confirm("Are you sure you want to delete this site?", function(result) {
		if(result){
			var url = asaas.servicesctx + "/repository?id="+itemid;
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
			    	asaas.notify("danger", "Unable to delete repository", ajaxErrorObject);
			    	success = false;
			    }
			});
			if(success){
		    	asaas.notify("success", "Site deleted.");
				self.init(self.container);
			}
		}
	}); 
};

//This ties it to the model on load.  The asaas library will call .init
//the array value must match the id/name of the component
asaas.model["repositorylist"] = new asaas.components.RepositoryList();