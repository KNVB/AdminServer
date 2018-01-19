class AdminPageControl
{
	constructor()
	{
		this.isLogined=false;
		this.modalList=new ModalList();
		this.adminServer=new AdminServer();
		this.ftpServerUI=new FtpServerUI(this);
		this.adminServerUI=new AdminServerUI(this);
		this.adminSidebar=document.createElement("nav");
		this.buildUI();
	}
	buildUI()
	{
		var self=this;
		var modalListDom;
		var logoutLink=document.createElement("a");
		document.body.appendChild(this.getLoginSideBar());
		this.adminServer.setServerResponseHandler(this.serverResponseHandler.bind(this));
		this.modalList.addModalUI(this.ftpServerUI.getHTML());
		this.modalList.addModalUI(this.adminServerUI.getHTML());
		modalListDom=this.modalList.getHTML();
		
		logoutLink.className="w3-bar-item w3-button"; 
		logoutLink.addEventListener('click',function()
								  {
									  self.logout();
								  });
		logoutLink.text="Logout";
		modalListDom.appendChild(logoutLink);
		
		//Side Navigation
		this.adminSidebar.id="mySidebar";
		this.adminSidebar.className="w3-hide w3-sidebar w3-bar-block w3-collapse w3-white w3-animate-left w3-card";
		this.adminSidebar.setAttribute("style","z-index:3;width:320px;");
		
		var a=document.createElement("a");
		a.className="w3-bar-item w3-button w3-hide-large w3-large";
		a.setAttribute("title","Close Sidemenu");
		a.onclick=this.w3_close;
		a.innerHTML="Close &#x2718;";
		
		this.adminSidebar.appendChild(a);
		this.adminSidebar.appendChild(modalListDom);
		document.body.appendChild(this.adminSidebar);
		
		// Overlay effect when opening the side navigation on small screens
		this.overLay=document.createElement("div");
		this.overLay.className="w3-overlay w3-hide-large w3-animate-opacity"; 
		this.overLay.id="myOverlay";
		this.overLay.style.cursor="pointer";
		this.overLay.setAttribute("title","Close Sidemenu"); 
		this.overLay.onclick=this.w3_close; 
		document.body.appendChild(this.overLay);	
		
		this.mainContainer=document.createElement("div");
		this.mainContainer.className="w3-main";
		this.mainContainer.style.marginLeft="320px";
		
		this.mainStage=document.createElement("div");
		this.mainStage.id="mainStage";
		var a2=document.createElement("a");
		a2.className="w3-button w3-white w3-hide-large w3-xlarge w3-margin-left w3-margin-top";
		               
		a2.innerHTML="&#x2630;";
		a2.onclick=this.w3_open;
		this.mainContainer.appendChild(a2);
		this.mainContainer.appendChild(this.mainStage);
		document.body.appendChild(this.mainContainer);
	}
	initTheStage()
	{
		$(this.adminSidebar).toggleClass("w3-hide");
	}
	serverResponseHandler(serverResponseObj)
	{
		if(serverResponseObj==null)
		{
			this.hideLoginSideBar(this.initTheStage.bind(this));	
		}
		else
		{
			switch (serverResponseObj.action)
			{
				case "Login":
						if(serverResponseObj.responseCode==0)
						{
							
							//$('.loginDiv').animate({opacity: 'hide', height: 'hide'}, 500);
							//$(".adminTable").show();
							//console.log(serverPage==null);
							this.hideLoginSideBar();
						}
						else
						{
							this.adminServer.disConnect();
							alert("Invalid user name or password");	
						}
						break;
				case "GetBindingAccess":
						//console.log(serverResponseObj.returnObjects.bindingIpList);
						this.ftpServerUI.updateBindingIpList(serverResponseObj.returnObjects.bindingIpList);
						break;
				case "GetRemoteDir":
						this.ftpServerUI.updateRemoteDir(serverResponseObj.returnObjects.userEntryId,
								                         serverResponseObj.returnObjects.accessRightEntryId,
								                         serverResponseObj.returnObjects.dirList);
						break;
			}
		}		
	}
	getLoginSideBar()
	{
		var self=this;
		var row=null,cell=null;
		var myAdminServer=this.adminServer;
		var loginDiv=document.createElement("div");
		var loginTable=document.createElement("table");
		var loginButton=document.createElement("input");
		var adminServerNameInputBox=document.createElement("input");
		var adminServerPortNoInputBox=document.createElement("input");
		var loginUserNameInputBox=document.createElement("input");
		var loginPasswordInputBox=document.createElement("input");
		
		this.loginSideBar=document.createElement("div");
		this.loginSideBar.className="sidenav";
		
		loginDiv.className="loginDiv";
		
		loginTable.className="loginTable";
		
		adminServerNameInputBox.id="hostName";
		adminServerNameInputBox.setAttribute("size","10");
		adminServerNameInputBox.setAttribute("type","text");
		adminServerNameInputBox.required = true;
		
		adminServerPortNoInputBox.id="portNo";
		adminServerPortNoInputBox.setAttribute("type","number");
		adminServerPortNoInputBox.required = true;
		adminServerPortNoInputBox.setAttribute("min","1")
		adminServerPortNoInputBox.setAttribute("max","65535");
		
		loginUserNameInputBox.id="userName";
		loginUserNameInputBox.setAttribute("size","10");
		loginUserNameInputBox.setAttribute("type","text");
		loginUserNameInputBox.required = true;
		
		loginPasswordInputBox.id="password";
		loginPasswordInputBox.setAttribute("size","10");
		loginPasswordInputBox.setAttribute("type","password");
		loginPasswordInputBox.required = true;
		
		loginButton.id="loginButton";
		loginButton.setAttribute("type","button");
		loginButton.value="Login";
		
		loginButton.addEventListener('click',function()
							{
								var loginObj=new Login(adminServerNameInputBox.value,
								                    adminServerPortNoInputBox.value,
													loginUserNameInputBox.value,
													loginPasswordInputBox.value);
								myAdminServer.login(loginObj);
								//self.serverResponseHandler();
							})
		row=loginTable.insertRow(loginTable.rows.length);
		cell=row.insertCell(row.cells.length);
		cell.id="adminServerLoginTitle";
		cell.colSpan=2;
		cell.style.textAlign="center";
		cell.textContent="Admin. Server Login";
		
		row=loginTable.insertRow(loginTable.rows.length);
		cell=row.insertCell(row.cells.length);
		cell.className="loginCaption";
		cell.id="adminServerNameLabel";
		cell.textContent="Server name:";
		cell=row.insertCell(row.cells.length);
		cell.appendChild(adminServerNameInputBox);
		
		row=loginTable.insertRow(loginTable.rows.length);
		cell=row.insertCell(row.cells.length);
		cell.className="loginCaption";
		cell.id="adminServerPortNoLabel";
		cell.textContent="Server port no:";
		cell=row.insertCell(row.cells.length);
		cell.appendChild(adminServerPortNoInputBox);
		
		row=loginTable.insertRow(loginTable.rows.length);
		cell=row.insertCell(row.cells.length);
		cell.className="loginCaption";
		cell.id="adminServerLoginUserNameLabel";
		cell.textContent="User name:";
		cell=row.insertCell(row.cells.length);
		cell.appendChild(loginUserNameInputBox);
		
		row=loginTable.insertRow(loginTable.rows.length);
		cell=row.insertCell(row.cells.length);
		cell.className="loginCaption";
		cell.id="adminServerLoginPasswordLabel";
		cell.textContent="Password:";
		cell=row.insertCell(row.cells.length);
		cell.appendChild(loginPasswordInputBox);
		
		row=loginTable.insertRow(loginTable.rows.length);
		cell=row.insertCell(row.cells.length);
		cell.colSpan=2;
		cell.style.textAlign="center";
		cell.appendChild(loginButton);
		loginDiv.appendChild(loginTable);
		this.loginSideBar.appendChild(loginDiv);
		
		adminServerNameInputBox.value="localhost";
		adminServerPortNoInputBox.value="4466";
		loginUserNameInputBox.value ="admin";
		loginPasswordInputBox.value ="password";
		
		return this.loginSideBar;
	}
	hideLoginSideBar(callBack)
	{
		if (this.loginSideBar!=null)
		{
			$(this.loginSideBar).hide("slide",this.showHideOption,1000,this.initTheStage());
		}
	}
	getBindingAddress()
	{
		this.adminServer.getBindingAddress();
	}
	getRemoteDir(physicalDir,userEntryId,accessRightEntryId)
	{
		this.adminServer.getRemoteDir(physicalDir,userEntryId,accessRightEntryId);
	}
	addContent(content)
	{
		$(this.mainStage).append(content);
	}
	clearMainStage()
	{ 
		$(this.mainStage).empty();
	}
	logout()
	{
		this.clearMainStage();
		this.adminServer.disConnect();
		$(this.loginSideBar).show("slide",this.showHideOption,1000);
		$(this.adminSidebar).toggleClass("w3-hide");
		this.modalList.collapse();
	}	
	w3_open() 
	{
		document.getElementById("mySidebar").style.display = "block";
		document.getElementById("myOverlay").style.display = "block";
	}
	w3_close() 
	{
		document.getElementById("mySidebar").style.display = "none";
		document.getElementById("myOverlay").style.display = "none";
	}
}