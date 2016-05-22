if(typeof(asaas)=="undefined") window.asaas = {};
if(typeof(window.asaas.components)=="undefined") window.asaas.components = {};

asaas.components.Dashboard = function(){
	this.container;
};
asaas.components.Dashboard.prototype 		= {};

//must always implement a .init for the asaas library to call it.
asaas.components.Dashboard.prototype.init	= function(container, parameters){
	this.container = container;
	var self = this;
}

//This ties it to the model on load.  The asaas library will call .init
//the array value must match the id/name of the component
asaas.model["dashboard"] = new asaas.components.Dashboard();