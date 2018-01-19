class AccessRight
{
	constructor(userEntryId)
	{
		//console.log(accessRightData,userRowCount);
		var self=this;
		var th=document.createElement("th");
		var thead=document.createElement("thead"); 
		var cancelLink=document.createElement("a"); 
		var savelLink=document.createElement("a"); 
		var closeSpan=document.createElement("span");
		var titleSpan=document.createElement("span");
		var modalContentDiv=document.createElement("div");
		var containerDiv=document.createElement("div");
		var panelDiv=document.createElement("div");
		var sectionDiv=document.createElement("div");
		var settingDetailTable=document.createElement("table");
		var filePermissionTable=document.createElement("table");
		var folderPermissionTable=document.createElement("table");
		var downloadFileCheckBox=document.createElement("input");
		var uploadFileCheckBox=document.createElement("input");
		var deleteFileCheckBox=document.createElement("input");
		var listFileCheckBox=document.createElement("input");
		var createDirCheckBox=document.createElement("input");
		var listDirCheckBox=document.createElement("input");
		var removeSubDirCheckBox=document.createElement("input");
		var denyDirCheckBox=document.createElement("input");
		var hideDirCheckBox=document.createElement("input");
		this.permissionSummary=null;
		this.table=document.createElement("table");
		this.userAccessRightDiv=document.createElement("div");
		
		titleSpan.className="w3-red w3-left";
		titleSpan.style.fontSize="15px";
		$(titleSpan).text("Directory Access");
		
		closeSpan.className="w3-right";
		closeSpan.innerHTML="<i>&#x2718;</i>";
		closeSpan.setAttribute("style","font-size:15px;cursor:pointer;");
		closeSpan.onclick=function()
						 {
							self.close();
						 };
		
		containerDiv.className="w3-container w3-padding w3-red"
		containerDiv.appendChild(titleSpan);
		containerDiv.appendChild(closeSpan);
		
		var row=thead.insertRow(thead.rows.length);
		
		$(th).text("Virtual Path");
		row.appendChild(th);
		
		th=document.createElement("th");
		$(th).text("Physical Path");
		row.appendChild(th);
		
		th=document.createElement("th");
		$(th).html("&#x271A;");
		th.className="addEntry";
		th.onclick=function ()
					{
						self.addAccessRightRow({virtualDir:"",physicalDir:"/"},userEntryId);
					};
		
		row.appendChild(th);
		
		this.table.setAttribute("width","100%");
		this.table.className="display";
		this.table.appendChild(thead);
		
		downloadFileCheckBox.setAttribute("type","checkbox");
		downloadFileCheckBox.id="downloadFile"+userEntryId;
		downloadFileCheckBox.onchange=function()
									{
										if (downloadFileCheckBox.checked)
											self.addPermissionSummaryValue("downloadFile");
										else
											self.removePermissionSummaryValue("downloadFile");			
									}
		
		uploadFileCheckBox.setAttribute("type","checkbox");
		uploadFileCheckBox.id="uploadFile"+userEntryId;
		uploadFileCheckBox.onchange=function()
									{
										if (uploadFileCheckBox.checked)
											self.addPermissionSummaryValue("uploadFile");
										else
											self.removePermissionSummaryValue("uploadFile");			
									}
		
		deleteFileCheckBox.setAttribute("type","checkbox");
		deleteFileCheckBox.id="deleteFile"+userEntryId;
		deleteFileCheckBox.onchange=function()
									{
										if (deleteFileCheckBox.checked)
											self.addPermissionSummaryValue("deleteFile");
										else
											self.removePermissionSummaryValue("deleteFile");			
									}
		
		
		listFileCheckBox.setAttribute("type","checkbox");
		listFileCheckBox.id="listFile"+userEntryId;
		listFileCheckBox.onchange=function()
									{
										if (listFileCheckBox.checked)
											self.addPermissionSummaryValue("listFile");
										else
											self.removePermissionSummaryValue("listFile");			
									}
		
		createDirCheckBox.setAttribute("type","checkbox");
		createDirCheckBox.id="createDir"+userEntryId;
		createDirCheckBox.onchange=function()
									{
										if (createDirCheckBox.checked)
											self.addPermissionSummaryValue("createDir");
										else
											self.removePermissionSummaryValue("createDir");			
									}
		
		listDirCheckBox.setAttribute("type","checkbox");
		listDirCheckBox.id="listDir"+userEntryId;
		listDirCheckBox.onchange=function()
									{
										if (listDirCheckBox.checked)
											self.addPermissionSummaryValue("listDir");
										else
											self.removePermissionSummaryValue("listDir");			
									}
		
		removeSubDirCheckBox.setAttribute("type","checkbox");
		removeSubDirCheckBox.id="removeSubDir"+userEntryId;
		removeSubDirCheckBox.onchange=function()
									{
										if (removeSubDirCheckBox.checked)
											self.addPermissionSummaryValue("removeSubDir");
										else
											self.removePermissionSummaryValue("removeSubDir");			
									}
		denyDirCheckBox.setAttribute("type","checkbox");
		denyDirCheckBox.id="denyDir"+userEntryId;
		denyDirCheckBox.onchange=function()
									{
										if (denyDirCheckBox.checked)
											self.addPermissionSummaryValue("denyDir");
										else
											self.removePermissionSummaryValue("denyDir");			
									}
									
		hideDirCheckBox.setAttribute("type","checkbox");
		hideDirCheckBox.id="hideDir"+userEntryId;
		hideDirCheckBox.onchange=function()
									{
										if (hideDirCheckBox.checked)
											self.addPermissionSummaryValue("hideDir");
										else
											self.removePermissionSummaryValue("hideDir");			
									}
		
		settingDetailTable.setAttribute("width","100%");
		settingDetailTable.style.display="none";
		
		row=settingDetailTable.insertRow(settingDetailTable.rows.length);
		var cell=row.insertCell(row.cells.length);
		var fieldset=document.createElement("fieldset");
		var legend=document.createElement("legend");
		legend.textContent="File access"

		var row2=filePermissionTable.insertRow(filePermissionTable.rows.length);
		var cell2=row2.insertCell(row2.cells.length);
		cell2.textContent="Download";
		cell2=row2.insertCell(row2.cells.length);
		cell2.appendChild(downloadFileCheckBox);
		
		row2=filePermissionTable.insertRow(filePermissionTable.rows.length);
		cell2=row2.insertCell(row2.cells.length);
		cell2.textContent="Upload";
		cell2=row2.insertCell(row2.cells.length);
		cell2.appendChild(uploadFileCheckBox);

		row2=filePermissionTable.insertRow(filePermissionTable.rows.length);
		cell2=row2.insertCell(row2.cells.length);
		cell2.textContent="Delete";
		cell2=row2.insertCell(row2.cells.length);
		cell2.appendChild(deleteFileCheckBox);
		
		fieldset.appendChild(legend);
		fieldset.appendChild(filePermissionTable);
		cell.appendChild(fieldset);
		
		cell=row.insertCell(row.cells.length);
		fieldset=document.createElement("fieldset");
		legend=document.createElement("legend");
		legend.textContent="Folder access"
		row2=folderPermissionTable.insertRow(folderPermissionTable.rows.length);
		cell2=row2.insertCell(row2.cells.length);
		cell2.textContent="List File";
		cell2=row2.insertCell(row2.cells.length);
		cell2.appendChild(listFileCheckBox);
		
		cell2=row2.insertCell(row2.cells.length);
		cell2.textContent="Create Sub. Dir.";
		cell2=row2.insertCell(row2.cells.length);
		cell2.appendChild(createDirCheckBox);
		
		row2=folderPermissionTable.insertRow(folderPermissionTable.rows.length);
		cell2=row2.insertCell(row2.cells.length);
		cell2.textContent="List Dir";
		cell2=row2.insertCell(row2.cells.length);
		cell2.appendChild(listDirCheckBox);
		
		cell2=row2.insertCell(row2.cells.length);
		cell2.textContent="Remove Sub. Dir.";
		cell2=row2.insertCell(row2.cells.length);
		cell2.appendChild(removeSubDirCheckBox);
		
		row2=folderPermissionTable.insertRow(folderPermissionTable.rows.length);
		cell2=row2.insertCell(row2.cells.length);
		cell2.textContent="Deny";
		cell2=row2.insertCell(row2.cells.length);
		cell2.appendChild(denyDirCheckBox);
		
		cell2=row2.insertCell(row2.cells.length);
		cell2.textContent="Hide";
		cell2=row2.insertCell(row2.cells.length);
		cell2.appendChild(hideDirCheckBox);

		fieldset.appendChild(legend);
		fieldset.appendChild(folderPermissionTable);
		cell.appendChild(fieldset);
		
		cancelLink.className="w3-button w3-red";
		cancelLink.innerHTML="Cancel <i>&#x2718;</i>"
		cancelLink.onclick=function()
						 {
							self.close();
						 };
		savelLink.className="w3-button w3-light-grey w3-right";
		savelLink.innerHTML="Save <i>&#x2714;</i>";
		savelLink.onclick=function()
						 {
							self.accessRightTable.rows().deselect();
							var selectedRow=-1;
							var finalResult=new Array();
							var isPermissionEmpty=false,isAllVirtualDirEmpty=true,containHomePath=false;
							
							if (self.accessRightTable.row({selected:true}).index()!=null)
							{
								selectedRow=self.accessRightTable.row({selected:true}).index();
								self.accessRightTable.rows().deselect();
							}
							var accessRightRows=$(self.table).find("tbody>tr");
							
							for (var i=0;i<accessRightRows.length;i++)
							{
								var virtualDir=$(accessRightRows[i]).find("input[id^='virtualDir"+userEntryId+"_']")[0];
								var physicalDir=$(accessRightRows[i]).find("input[id^='physicalDir"+userEntryId+"_']")[0];
								var permissionSummary=$(accessRightRows[i]).find("input[id^='permissionSummary"+userEntryId+"_']")[0];
								if ($(virtualDir).val()!="")
								{	
									isAllVirtualDirEmpty=false;
									if ($(virtualDir).val()=="/")
										containHomePath=true;
								}
								if ($(permissionSummary).val()=="")
								{
									isPermissionEmpty=true;
									alert("Permission setting is missing in row:"+i);
									if (selectedRow!=-1)
									{
										self.accessRightTable.row(selectedRow).select();
									}
									break;
								}
								else
								{	
									var rowResult={"virtualDir":$(virtualDir).val(),
												   "physicalDir":$(physicalDir).val(),
												   "permissionSummary":$(permissionSummary).val().split(",")};
									finalResult.push(rowResult);
								}
							}
							if (!isPermissionEmpty)
							{
								if ((!isAllVirtualDirEmpty)&&(containHomePath))
								{
									document.getElementById("accessRight"+userEntryId).value=JSON.stringify(finalResult);
									self.close();
								}
								else
								{
									if (isAllVirtualDirEmpty)
										alert("Please set at least 1 virtual directory for this user");
									else
									{	if (!containHomePath)
											alert("Please set home path(/) for this user.");
									}
									if (selectedRow!=-1)
									{
										self.accessRightTable.row(selectedRow).select();
									}
								}
							}

						 };
		
		sectionDiv.className="w3-section";
		sectionDiv.appendChild(settingDetailTable);
		sectionDiv.appendChild(cancelLink);
		sectionDiv.appendChild(savelLink);
		
		panelDiv.className="w3-panel";
		panelDiv.appendChild(this.table);
		panelDiv.appendChild(sectionDiv);
		
		modalContentDiv.className="w3-modal-content w3-animate-zoom";
		modalContentDiv.appendChild(containerDiv);
		modalContentDiv.appendChild(panelDiv);
		
		this.userAccessRightDiv.id="userAccessRight"+userEntryId;
		this.userAccessRightDiv.className="w3-modal";
		this.userAccessRightDiv.style.zIndex="4";
		this.userAccessRightDiv.appendChild(modalContentDiv);
		this.userAccessRightDiv.style.display='block';
		
		document.body.appendChild(this.userAccessRightDiv);
		this.accessRightTable=$(this.table).DataTable({
													responsive: true,
													"columnDefs": [
																   {"className": "dt-center", "targets":[2]},
																   {"orderable": false,"targets":[2]}
																	],
													select: {
																style: 'single'
															},				
													"fixedHeader":true
												});
												
		this.accessRightTable.on("select",function ( e, dt, type, indexes ) 
							  {
									//var rowData=JSON.stringify(self.accessRightTable.rows(indexes).data().toArray());
									//console.log("selected row data:"+rowData);
								
									self.permissionSummary=$(self.table).find("tr.selected>td:first-child").children("input[id^='permissionSummary"+userEntryId+"_']");
									
									downloadFileCheckBox.checked=false;
									uploadFileCheckBox.checked=false;
									deleteFileCheckBox.checked=false;
									listFileCheckBox.checked=false;
									createDirCheckBox.checked=false;
									listDirCheckBox.checked=false;
									removeSubDirCheckBox.checked=false;
									denyDirCheckBox.checked=false;
									hideDirCheckBox.checked=false;
									
									
									if (self.permissionSummary.val()!="")
									{
										var permissionSummaryData=self.permissionSummary.val().split(",");
										for (var i=0;i<permissionSummaryData.length;i++)
										{
											switch (permissionSummaryData[i])
											{
												case "downloadFile":
													downloadFileCheckBox.checked=true;
													break;
												case "uploadFile":
													uploadFileCheckBox.checked=true;
													break;
												case "deleteFile":
													deleteFileCheckBox.checked=true;
													break;
												case "listFile":
													listFileCheckBox.checked=true;
													break;
												case "createDir":
													createDirCheckBox.checked=true;
													break;
												case "listDir":
													listDirCheckBox.checked=true;
													break;
												case "removeSubDir":
													removeSubDirCheckBox.checked=true;
													break;
												case "denyDir":
													denyDirCheckBox.checked=true;
													break;
												case "hideDir":
													hideDirCheckBox.checked=true;
													break;
											}
										}
									}
									settingDetailTable.style.display="table";
							  });
		this.accessRightTable.on("deselect",function ( e, dt, type, indexes ) 
							  {
									//console.log("deselect event");
									//var rowData=JSON.stringify(self.accessRightTable.rows(indexes).data().toArray());
									//console.log("deselected row data:"+rowData);
									
									settingDetailTable.style.display="none";
							  });								
		
	}
	addPermissionSummaryValue(value)
	{
		var permissionSummaryData;
		if (this.permissionSummary.val()=="")
		{
			permissionSummaryData=new Array(value);
		}
		else
		{
			permissionSummaryData=this.permissionSummary.val().split(",");
			if (permissionSummaryData.indexOf(value)==-1)
			{
				permissionSummaryData.push(value);
			}
		}
		this.permissionSummary.val(permissionSummaryData);
	}
	removePermissionSummaryValue(value)
	{
		var permissionSummaryData=this.permissionSummary.val();
		permissionSummaryData=permissionSummaryData.replace(value,"");
		permissionSummaryData=permissionSummaryData.replace(/^,/,"");
		permissionSummaryData=permissionSummaryData.replace(/,$/,"");
		permissionSummaryData=permissionSummaryData.replace(",,",",");
		
		this.permissionSummary.val(permissionSummaryData);
	}
	loadData(accessRightData,userEntryId)
	{
		if (accessRightData.length==0)
		{
			this.addAccessRightRow(null,userEntryId);
		}
		else
		{
			for (var i=0;i<accessRightData.length;i++)
			{	
				this.addAccessRightRow(accessRightData[i],userEntryId);
			}
		}					
	}
	addAccessRightRow(accessRightDataEntry,userEntryId)
	{
		var self=this;
		var accessRightEntryId=Utility.getUniqueId();
		var pObj=document.createElement("p");
		var remoteDir=document.createElement("div");
		var remoteDirContainer=document.createElement("div");
		var permissionSummary=document.createElement("input");
		var physicalDirInputBox=document.createElement("input");
		var virtualDirInputBox=document.createElement("input");
		var oldPhysicalDirValue=document.createElement("input");
		var showRemoteDirBtn=document.createElement("input");
		var resumeOldSettingBtn=document.createElement("input");
		var hideRemoteDirBtn=document.createElement("input");
		
		remoteDirContainer.id="remoteDir"+userEntryId+"_"+accessRightEntryId;
		remoteDirContainer.className="remoteDirContainer";
		remoteDir.className="remoteDir";
		
		permissionSummary.setAttribute("type", "hidden");
		permissionSummary.id="permissionSummary"+userEntryId+"_"+accessRightEntryId;
		//permissionSummary.value=new Array('a','b');
		/*permissionSummary.value={"userList":[{userName:"anonymous",
												   password:"",
												   accessRightList:[{virtualDir:"",physicalDir:"/"}]
												  }]};*/
		
		
		virtualDirInputBox.required = true;
		virtualDirInputBox.id="virtualDir"+userEntryId+"_"+accessRightEntryId;
		virtualDirInputBox.setAttribute("type", "text");
		if (accessRightDataEntry!=null)
		{
			virtualDirInputBox.setAttribute("value",accessRightDataEntry.virtualDir);
		}
		physicalDirInputBox.readOnly=true;
		physicalDirInputBox.required = true;
		physicalDirInputBox.id="physicalDir"+userEntryId+"_"+accessRightEntryId;
		physicalDirInputBox.setAttribute("type", "text");
		if (accessRightDataEntry!= null)
		{
			physicalDirInputBox.setAttribute("value",accessRightDataEntry.physicalDir);
		}
		
		oldPhysicalDirValue.id="oldPhysicalDirValue"+userEntryId+"_"+accessRightEntryId;
		oldPhysicalDirValue.setAttribute("type", "hidden");
		if (accessRightDataEntry!= null)
		{
			oldPhysicalDirValue.setAttribute("value",accessRightDataEntry.physicalDir);
		}
		
		showRemoteDirBtn.value="...";
		showRemoteDirBtn.setAttribute("type", "button");
		showRemoteDirBtn.onclick=function()
								 {
									self.getRemoteDir(userEntryId,accessRightEntryId);
								 };
		
		resumeOldSettingBtn.value="Cancel";
		resumeOldSettingBtn.setAttribute("type", "button");
		resumeOldSettingBtn.onclick=function()
									{
										self.resumeOldSetting(userEntryId,accessRightEntryId);
									};
		
		hideRemoteDirBtn.value="Ok";
		hideRemoteDirBtn.setAttribute("type", "button");
		hideRemoteDirBtn.onclick=function()
								{
									self.hideRemoteDir(userEntryId,accessRightEntryId);
								};
		
		var row=this.table.insertRow(this.table.rows.length);
		var cell=row.insertCell(row.cells.length);
		
		cell.appendChild(virtualDirInputBox);
		cell.appendChild(permissionSummary);
		
		cell=row.insertCell(row.cells.length);
		cell.appendChild(physicalDirInputBox);
		cell.innerHTML+="\n";
		cell.appendChild(showRemoteDirBtn);
		
		remoteDirContainer.appendChild(pObj);
		pObj.appendChild(remoteDir);
		pObj.appendChild(oldPhysicalDirValue);
		pObj.appendChild(resumeOldSettingBtn);
		pObj.appendChild(hideRemoteDirBtn);
		
		cell.appendChild(remoteDirContainer);
		cell=row.insertCell(row.cells.length);
		cell.className="removeEntry";
		cell.innerHTML="&#x1F5D1;";
		cell.onclick=function ()
					{
						$(row).addClass("selected");
						self.accessRightTable.row(".selected").remove().draw(true);
					}
		row.id=accessRightEntryId;
		var dt = $(this.table).dataTable().api();
		dt.row.add($(row));
		dt.draw();
		
	}
	getRemoteDir(userEntryId,accessRightEntryId)
	{
		var remoteDirContainer=document.getElementById("remoteDir"+userEntryId+"_"+accessRightEntryId);
		if (remoteDirContainer.style.display=="block")
			this.hideRemoteDir(userEntryId,accessRightEntryId);
		else
			this.showRemoteDir(userEntryId,accessRightEntryId);
	}
	showRemoteDir(userEntryId,accessRightEntryId)
	{	
		$("#remoteDir"+userEntryId+"_"+accessRightEntryId).slideDown({
										duration: 1000, 
										easing:"swing"
										});
	}
	hideRemoteDir(userEntryId,accessRightEntryId)
	{	
		$("#remoteDir"+userEntryId+"_"+accessRightEntryId).slideUp({
										duration: 1000, 
										easing:"swing"
										});
	}
	resumeOldSetting(userEntryId,accessRightEntryId)
	{
		$("#physicalDir"+userEntryId+"_"+accessRightEntryId).val($("#oldPhysicalDirValue"+userEntryId+"_"+accessRightEntryId).val());
		this.hideRemoteDir(userEntryId,accessRightEntryId);
	}
	close()
	{
		this.userAccessRightDiv.style.display='none';
	}				
}									
