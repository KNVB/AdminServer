class FtpServerUI_AddServer extends MainPageTemplate
{
	constructor(adminPageControl)
	{
		super();
		var self=this;
		var row,cell;
		var p=document.createElement("p");
		var saveLink=document.createElement("a");
		this.setNavigationHeading("FTP Server Administration > Add Server");
		var userData={"userList":[{userName:"anonymous",
								   password:"",
								   accessRightList:[{virtualDir:"/",physicalDir:"/"}]
								  }]};
		

		var descriptionInputBox=document.createElement("input");
		var descriptionTextNode=document.createTextNode("Description:");
		
		
		var bindingTable=document.createElement("table");
		var bindingLegend=document.createElement("Legend");
		var bindingFieldSet=document.createElement("fieldset");
		this.bindingAddressDropDown=document.createElement("select");
		
		var controlPortInputBox=document.createElement("input");
		var passiveModeFieldSet=document.createElement("fieldset");
		var passiveModeLegend=document.createElement("Legend");
		var passiveModeCheckBox=document.createElement("input");
		var passiveModeDetailDiv=document.createElement("div");
		var passiveModePortRangeInputBox=document.createElement("input");
		this.adminPageControl=adminPageControl;
		this.userManagement=new UserManagement(this.adminPageControl);
		
		
		$(bindingLegend).text("Binding");
		bindingFieldSet.appendChild(bindingLegend);
		
		this.bindingAddressDropDown.id="bindingAddress";
		this.bindingAddressDropDown.multiple = true;
		controlPortInputBox.id="controlPort";
		controlPortInputBox.setAttribute("type","number");
		controlPortInputBox.value=21;
		controlPortInputBox.min=1;
		controlPortInputBox.max=65535;
		controlPortInputBox.required=true;
		
		row=bindingTable.insertRow(bindingTable.rows.length);
		cell=row.insertCell(row.cells.length);
		cell.innerHTML="IP address:";
		
		cell=row.insertCell(row.cells.length);
		cell.innerHTML="Port:";

		row=bindingTable.insertRow(bindingTable.rows.length);
		cell=row.insertCell(row.cells.length);
		cell.appendChild(this.bindingAddressDropDown);
		
		cell=row.insertCell(row.cells.length);
		cell.style.verticalAlign="top";
		cell.appendChild(controlPortInputBox);
		
		bindingFieldSet.appendChild(bindingTable);
		
		passiveModeCheckBox.id="isPassiveModeEnable";
		passiveModeCheckBox.setAttribute("type","checkbox");
		passiveModeCheckBox.onclick=function()
									{ 
										self.togglePassiveMode(this);
									};
		passiveModeLegend.appendChild(passiveModeCheckBox);
		passiveModeLegend.appendChild(document.createTextNode("Passive Mode"));
		
		passiveModeDetailDiv.className="w3-hide";
		passiveModeDetailDiv.id="passiveModeDetail";
		passiveModeDetailDiv.innerHTML="Port Range:";
		
		passiveModePortRangeInputBox.setAttribute("type","text");
		passiveModePortRangeInputBox.setAttribute("id","passiveModePortRange");
		passiveModePortRangeInputBox.required=true;

		passiveModeDetailDiv.appendChild(passiveModePortRangeInputBox);
		
		passiveModeFieldSet.id="passiveModeSetting";
		passiveModeFieldSet.className="passiveModeSetting";
		passiveModeFieldSet.appendChild(passiveModeLegend);
		passiveModeFieldSet.appendChild(passiveModeDetailDiv);
		
		this.userManagement.addUserRow(userData.userList[0]);
		saveLink.className="w3-red w3-button w3-right w3-margin-top w3-margin-right";
		$(saveLink).html("Add &#x2714;");
		p.appendChild(descriptionTextNode);
		p.appendChild(descriptionInputBox);
		p.appendChild(bindingFieldSet);
		p.appendChild(passiveModeFieldSet);
		p.appendChild(this.userManagement.getHTML());
		p.appendChild(saveLink);
		this.addContent(p);
	}
	togglePassiveMode(passiveModeDetail)
	{
	 		var passiveModeDetail=$("#passiveModeDetail");
	 		var passiveModeSetting=$("#passiveModeSetting");
			
			passiveModeSetting.toggleClass("passiveModeSetting");
			passiveModeDetail.toggleClass("w3-hide");
	}
	updateBindingIpList(bindingIpList)
	{
		$(this.bindingAddressDropDown).empty();
		for (var i=0;i<bindingIpList.length;i++)
		{
			var option=document.createElement("option");
			option.text=bindingIpList[i];
			option.value=bindingIpList[i];
			this.bindingAddressDropDown.appendChild(option);
		}
	}
}