if(typeof(asaas)=="undefined") window.asaas = {};
if(typeof(window.asaas.components)=="undefined") window.asaas.components = {};

asaas.components.RepositoryList = function(){
	this.container;
};
asaas.components.RepositoryList.prototype 		= {};

//must always implement a .init for the asaas library to call it.
asaas.components.RepositoryList.prototype.init	= function(container){
	
	var url = asaas.servicesctx + "/repository";
	var dataAjax = $.ajax({
		url: url,
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
			
			//Bind Edit actions
			$(".list-group .list-group-item .btn-edit", container).click(function(evnt){
				var itemid = $(this).data("id");
				var modal = asaas.loadModalPopup("saveItemModel", "Edit Repository");
				var $modalbody = $(".modal-body", modal);
				window.setTimeout(function(){
					asaas.loadComponent("repository", false, $modalbody, {"id":itemid});
				},1);
			});
			
			
		},
	    error: function(ajaxErrorObject){
	    	asaas.notify("danger", "Unable to load list of repositories", ajaxErrorObject);
	    }
	});
}

//This ties it to the model on load.  The asaas library will call .init
//the array value must match the id/name of the component
asaas.model["repositorylist"] = new asaas.components.RepositoryList();