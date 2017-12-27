class ServerPage
{
	constructor(adminServer,mainStage)
	{
		this.request=new Request();
		this.adminServer=adminServer;
		this.mainStage=mainStage;
	}
	listServers()
	{
		this.request.setAction("GetServerList");
		this.adminServer.sendRequest(this.request);
		//this.adminServer.sendRequest("{\"action\":\"GETServerList\"}");
		//ws.send(messageCoder.encode("{\"action\":\"GETServerList\"}"));
	}		
	getAddServerPageData()
	{
		this.request.setAction("GetAddServerPageData");
		this.adminServer.sendRequest(this.request);
	}
	showNoServerPage()
	{
		this.mainStage.append("<br><br><span style=\"font:italic 16px times new roman;\">There are no server have been defined.</span>");
		this.mainStage.append("<br>");
		var span=document.createElement("span");
		span.id="addServer";
		span.style="font:italic 16px times new roman;cursor:pointer";
		span.textContent="Click here to define new ftp server.";
		$(span).click(
				function ()
				{
					mainStage.empty();
					serverPage.getAddServerPageData();
				}
			 );
		this.mainStage.append(span);
	}
	buildAddServerPage(serverResponseObj)
	{
		var passiveModeDetail=$(document.createElement("div"));
		var ipList=serverResponseObj.returnObjects["localIpList"];
		var fieldSet=$(document.createElement("fieldset"));
		var bindingTable=document.createElement("table");
		var select=$(document.createElement("select"));
		var table=document.createElement("table");
		var row=table.insertRow(table.rows.length);
		var cell=row.insertCell(row.length);
		cell.innerHTML="Description:<input type=text required id=\"desc\">";
		fieldSet.append("<legend>Binding</legend>");
		row=bindingTable.insertRow(bindingTable.rows.length);
		cell=row.insertCell(cell.length);
		cell.innerHTML="IP address:";
		cell=row.insertCell(1);
		cell.innerHTML="Port:";
		row=bindingTable.insertRow(bindingTable.rows.length);
		cell=row.insertCell(row.cells.length);
		select.attr("id","bindAddress");
		select.append("<option value=\"*\">All Unassigned</option>");
		for (var i=0;i<ipList.length;i++)
		{
			select.append("<option value=\""+ipList[i]+"\">"+ipList[i]+"</option>");
		}		
		$(cell).append(select);
		cell=row.insertCell(row.cells.length);
		
		$(cell).append("<input type=\"number\" required id=\"port\" min=\"1\" max=\"65535\" value=\"21\">");
		fieldSet.append(bindingTable);
		
		row=table.insertRow(table.rows.length);
		cell=row.insertCell(row.cells.length);
		$(cell).append(fieldSet);

		row=table.insertRow(table.rows.length);
		cell=row.insertCell(row.cells.length);
		fieldSet=$(document.createElement("fieldset"));
		fieldSet.attr("id","passiveModeSetting");
		fieldSet.css("border","none");
		fieldSet.append("<legend><input type=\"checkbox\" id=\"isPassiveMode\">Support Passive mode</legend>");
		passiveModeDetail.attr("id","passiveModeDetail"); 
		passiveModeDetail.css("display","none");
		passiveModeDetail.append("Port Range:<input type=text id=\"portRange\" required>");
		fieldSet.append(passiveModeDetail);
		$(cell).append(fieldSet);
		$(table).css("height","100%");
		this.mainStage.append(table);
		row=table.insertRow(table.rows.length);
		cell=row.insertCell(row.cells.length);
		cell.innerHTML="<input type=\"submit\" value=\"add server definition\" style=\"float: right;\">";
		$("#isPassiveMode").click(function()
				  {
					var passiveModeSetting=document.getElementById("passiveModeSetting");
					var passiveModeDetail=$("#passiveModeDetail");
					passiveModeDetail.toggle();
					if (passiveModeSetting.style.border=="none")
					{
						passiveModeSetting.style.border="1px solid black";
					}
					else
					{
						passiveModeSetting.style.border="none";
					}
				  });
	}
}