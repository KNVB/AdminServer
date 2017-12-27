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
		var ipList=serverResponseObj.returnObjects["localIpList"];
		var temp=$(document.createElement("fieldset"));
		var table=document.createElement("table");
		var select=$(document.createElement("select"));
		var row=table.insertRow(table.rows.length);
		var cell=row.insertCell(0);
		cell.innerHTML="IP address:";
		cell=row.insertCell(1);
		cell.innerHTML="Port:";
		row=table.insertRow(table.rows.length);
		cell=row.insertCell(0);
		select.attr("id","bindAddress");
		select.append("<option value=\"*\">All Unassigned</option>");
		for (var i=0;i<ipList.length;i++)
		{
			select.append("<option value=\""+ipList[i]+"\">"+ipList[i]+"</option>");
		}		
		$(cell).append(select);
		cell=row.insertCell(1);
		
		$(cell).append("<input type=\"number\" required id=\"port\" min=\"1\" max=\"65535\" value=\"21\">");
		temp.append("<legend>Binding</legend>");
		temp.append(table);
		this.mainStage.append(temp);
		this.mainStage.append("<br>");		
		temp=$(document.createElement("fieldset"));
		temp.css("border","none");
		temp.append("<legend><input type=\"checkbox\" id=\"isPassiveMode\">Passive mode</legend>");
		this.mainStage.append(temp);
	}
}