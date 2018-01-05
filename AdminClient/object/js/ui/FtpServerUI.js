class FtpServerUI
{
	constructor(adminServer,adminPage)
	{
		this.adminServer=adminServer;
	}
	getModal(callBack)
	{
		var a=document.createElement("a");
	
		var div=document.createElement("div");
		
		var subDiv=document.createElement("div");
		
		a.className="w3-bar-item w3-button";
		a.innerHTML="FTP Server Administration &#x25bc;"
		
		a.addEventListener('click',function()
								  {
									  callBack(this);
								  });
		subDiv.id="ftp";
		subDiv.className="w3-hide w3-animate-left";
		
		subDiv.appendChild(this.addFunction("List Server","listFtpServer"));
		subDiv.appendChild(this.addFunction("Add Server &#x1F589;","addFtpServer"));
		subDiv.appendChild(this.addFunction("Remove Server &#x1F589;","removeFtpServer"));
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