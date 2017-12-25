class ServerDetail
{
	setWebSock(ws) 
	{
		this.ws=ws;
	}
	listServers()
	{
		ws.send(messageCoder.encode("{\"action\":\"GETServerList\"}"));
	}		

}