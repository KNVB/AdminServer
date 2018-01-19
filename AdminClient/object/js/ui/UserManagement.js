class UserManagement
{
	constructor(adminPageControl)
	{
		var cell,span,self=this;
		var th=document.createElement("th");
		var thead=document.createElement("thead"); 
		var tbody=document.createElement("tbody");
		var row=thead.insertRow(thead.rows.length);
		var legend=document.createElement("legend");
		
		this.adminPageControl=adminPageControl;
		this.accessRightArray=new Array();
		this.table=document.createElement("table");
		this.fieldset=document.createElement("fieldset");
		
		$(this.fieldset).append(legend);
		$(legend).text("User Management");
		
		$(th).text("User Name");
		$(row).append(th);
		
		th=document.createElement("th");
		$(th).text("Password");
		$(row).append(th);
		
		th=document.createElement("th");
		$(th).text("Access Right");
		$(row).append(th);

		th=document.createElement("th");
		$(th).text("Enabled");
		$(row).append(th);

		th=document.createElement("th");
		$(th).text("Remove");
		$(row).append(th);
		this.table.appendChild(thead);
		this.table.className="display";
		this.fieldset.appendChild(this.table);
		this.userTable=$(this.table).DataTable(
					{ 
						"dom": '<"toolbar">frtip',
						"CaseSensitive":true, 
						"order": [[ 0, "desc" ]],
						paging: true,
						"pageLength": 10,
						responsive: true,
						"info": true,
						"orderCellsTop": true, 
						"fixedHeader":true,
						"columnDefs": [{"className": "dt-center", "targets": [2,3,4]},
										{"orderable": false,"targets":[1,2,3,4]}
									  ],
					})
		var toolbar=$(this.fieldset).children("div").children(".toolbar")
		
		$(toolbar).html("&nbsp;&#x271A;");					
		$(toolbar).on("click",function()
								{
									var userData={accessRightList:[{virtualDir:"/",physicalDir:"/"}]}
									self.addUserRow(userData);
								});
	}
	getHTML()
	{
		return this.fieldset;
	}
	addUserRow(thisUserData)
	{
		var self=this;
		var tbody=this.userTable.table().body();
		var entryId=Utility.getUniqueId();
		var deleteEntrySpan=document.createElement("span");
		var popupAccessRightSpan=document.createElement("span");
		var accessRight=document.createElement("input");
		var userNameInputBox=document.createElement("input");
		var passwordInputBox=document.createElement("input");
		var userEnableCheckBox=document.createElement("input");
		var isRemoveThisUserInputBox=document.createElement("input");
																		
		var row=document.createElement("tr");
		
		userNameInputBox.id="userName"+entryId;
		userNameInputBox.setAttribute("type","text");
		userNameInputBox.required = true;
		
		accessRight.id="accessRight"+entryId;
		accessRight.setAttribute("type","hidden");
		
		passwordInputBox.id="password"+entryId;
		passwordInputBox.required = true;
		passwordInputBox.setAttribute("type","password");					
		
		userEnableCheckBox.id="isUserEnable"+entryId;
		userEnableCheckBox.setAttribute("type","checkbox");
		
		isRemoveThisUserInputBox.id="isRemoveThisUser"+entryId;
		isRemoveThisUserInputBox.setAttribute("type","hidden");
		isRemoveThisUserInputBox.value=0;
		
		popupAccessRightSpan.className="popupAccessRightPage";
		popupAccessRightSpan.innerHTML="&#x1F589;";
		popupAccessRightSpan.onclick=function()
									 {
										self.popupAccessRightModal(self,entryId,thisUserData);
									 };
		deleteEntrySpan.innerHTML="&#x1F5D1;";
		deleteEntrySpan.className="removeEntry";
		$(deleteEntrySpan).click(function()
					{
						self.removeRow(row,entryId);
					});
		
		
		var cell=row.insertCell(row.cells.length);
		cell.appendChild(userNameInputBox);
		cell.appendChild(accessRight);
		
		cell=row.insertCell(row.cells.length);
		cell.appendChild(passwordInputBox);
		
		cell=row.insertCell(row.cells.length);
		cell.appendChild(popupAccessRightSpan);

		cell=row.insertCell(row.cells.length);
		cell.appendChild(userEnableCheckBox);

		cell=row.insertCell(row.cells.length);
		cell.appendChild(deleteEntrySpan);
		cell.appendChild(isRemoveThisUserInputBox);
		
		if ((thisUserData.userName!=null))
		{
			if (thisUserData.userName=="anonymous")
			{
				userNameInputBox.setAttribute("type","hidden");
				cell=$(userNameInputBox).parent();
				cell.attr("data-order","0");
				var textNode=document.createTextNode("anonymous");
				$(textNode).insertBefore(userNameInputBox);
				$(passwordInputBox).remove();
				$(deleteEntrySpan).remove();
			}
			else
			{
				passwordInputBox.setAttribute("value",thisUserData.password);
			}
			userNameInputBox.setAttribute("value",thisUserData.userName);
		}
		var dt = $(this.table).dataTable().api();
		dt.row.add($(row));
		dt.draw();
	}
	popupAccessRightModal(self,userEntryId,thisUserData)
	{
		var accessRightData=thisUserData.accessRightList;
		if (userEntryId in this.accessRightArray)
		{
			this.accessRightArray[userEntryId].show();
		}
		else
		{
			var accessRight=new AccessRight(userEntryId,self.adminPageControl);
			accessRight.loadData(accessRightData,userEntryId);
			this.accessRightArray[userEntryId]=accessRight;
		}				
	}
	removeRow(row,userCount)
	{
		var userName=$("#userName"+userCount).val();
		var password=$("#password"+userCount).val();
		if ((userName=="") && (password==""))
		{	
			$(row).addClass("selected");
			this.userTable.row(".selected").remove().draw(true);
		}
		else	
		{
			var field=document.getElementById("isRemoveThisUser"+userCount);
			field.value="1";
			$(row).hide();
		}
	}			
}
