class AdminServerUI
{
	constructor(adminPageControl)
	{
		this.adminServer=adminPageControl.adminServer;
		this.adminPageControl=adminPageControl;
	}
	getHTML()
	{
		var a=document.createElement("a");
		var div=document.createElement("div");
		var subDiv=document.createElement("div");
		
		a.className="w3-bar-item w3-button";
		a.innerHTML="Administration Server Setting &#x25bc;"

		subDiv.id="admin";
		subDiv.className="w3-hide w3-animate-left";
		subDiv.appendChild(this.addFunction("QQ",""));
		subDiv.appendChild(this.addFunction("Change Admin user and password",""));
		div.appendChild(a);
		div.appendChild(subDiv);
		return div;
	}
	addFunction(label,id)
	{
		var a2=document.createElement("a");
		var div2=document.createElement("div");
		var span=document.createElement("span");
		
		a2.className="w3-bar-item w3-button w3-border-bottom test w3-hover-light-grey";
		a2.id=id;
		div2.className="w3-container";
		span.className="w3-opacity w3-large";
		span.innerHTML=label;
		div2.appendChild(span);
		a2.appendChild(div2);
		return a2;
	}
}