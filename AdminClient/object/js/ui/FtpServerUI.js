class FtpServerUI
{
	constructor(adminPageControl)
	{
		this.ftpServerUI_AddServer=null;
		this.adminPageControl=adminPageControl;
	}
	getHTML()
	{
		var a=document.createElement("a");
	
		var div=document.createElement("div");
		
		var subDiv=document.createElement("div");
		
		a.className="w3-bar-item w3-button";
		a.innerHTML="FTP Server Administration &#x25bc;"
		
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
		this.adminPageControl.clearMainStage();
		this.adminPageControl.addContent("List server");
	}
	addFtpServer()
	{
		this.adminPageControl.getBindingAddress();
		this.adminPageControl.clearMainStage();
		
		this.ftpServerUI_AddServer=new FtpServerUI_AddServer(this.adminPageControl);
		this.adminPageControl.addContent(this.ftpServerUI_AddServer.getHTML());
	}
	removeFtpServer()
	{
		this.adminPageControl.clearMainStage();
		this.adminPageControl.addContent("remove server");
	}
	updateBindingIpList(bindingIpList)
	{
		this.ftpServerUI_AddServer.updateBindingIpList(bindingIpList);
	}	           
    updateRemoteDir(userEntryId,accessRightEntryId,dirList)
	{
    	this.ftpServerUI_AddServer.updateRemoteDir(userEntryId,accessRightEntryId,dirList);
	}       
            
}