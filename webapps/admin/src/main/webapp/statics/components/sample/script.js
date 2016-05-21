if(typeof(asaas)=="undefined") window.asaas = {};
if(typeof(window.asaas.components)=="undefined") window.asaas.components = {};

asaas.components.Sample = function(){
	this.container;
};
asaas.components.Sample.prototype 		= {};

//must always implement a .init for the asaas library to call it.
asaas.components.Sample.prototype.init	= function(container, parameters){
	
}

//This ties it to the model on load.  The asaas library will call .init
//the array value must match the id/name of the component
asaas.model["sample"] = new asaas.components.Sample();