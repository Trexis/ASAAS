if(typeof(asaas)=="undefined") window.asaas = {};
if(typeof(window.asaas.components)=="undefined") window.asaas.components = {};

asaas.components.RepositoryList = function(){
	this.container;
};
asaas.components.RepositoryList.prototype 		= {};
//must implement a .init for the asaas library to call it.
asaas.components.RepositoryList.prototype.init	= function(container){
	console.log(container)
}

//This ties it to the model on load.  The asaas library will call .init
//the array value must match the id/name of the component
asaas.model["repositorylist"] = new asaas.components.RepositoryList();