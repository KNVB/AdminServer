class FtpServerUI
{
	constructor(adminServer,adminPageControl)
	{
		this.adminServer=adminServer;
		this.adminPageControl=adminPageControl;
	}
	getUI(callBack)
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
		
		subDiv.appendChild(this.addFunction("List Server","listFtpServer",this.listFtpServer.bind(this)));
		subDiv.appendChild(this.addFunction("Add Server &#x1F589;","addFtpServer",this.addFtpServer.bind(this)));
		subDiv.appendChild(this.addFunction("Remove Server &#x1F589;","removeFtpServer",this.removeFtpServer.bind(this)));
		div.appendChild(a);
		div.appendChild(subDiv);
		return div;
	}
	addFunction(label,id, theFunction)
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
		a2.addEventListener('click',function()
								  {
									  theFunction();
								  });
		return a2;
	}
	listFtpServer()
	{
		this.adminPageControl.setContent("List server");
	}
	addFtpServer()
	{
		this.adminPageControl.setContent("add server");
	}
	removeFtpServer()
	{
		this.adminPageControl.setContent("remove server");
	}
}