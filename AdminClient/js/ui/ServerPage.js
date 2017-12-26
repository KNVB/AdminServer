class ServerPage
{
	constructor(adminServer)
	{
		this.adminServer=adminServer;
	}
	listServers()
	{
		this.adminServer.sendRequest("{\"action\":\"GETServerList\"}");
		//ws.send(messageCoder.encode("{\"action\":\"GETServerList\"}"));
	}		
	addServer()
	{
		console.log("gg");
	}
}