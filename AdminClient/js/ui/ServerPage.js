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
		//this.adminServer.go();
		//this.request.setAction("GetAddServerPageData");
		//this.adminServer.sendRequest(this.request);
	}
}