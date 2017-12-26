const WsStateDisconnected = 0;
const WsStateDisconnecting = 1;
const WsStateConnected = 2;
const WsStateConnecting = 3;

class AdminServer
{
	constructor(hostName,portNo)
	{
		this.ws = null;
		this.loginObj=null;
		this.customHandler=null;
		this.keyCoder=new JSEncrypt({
		    default_key_size: 1234
		});
		this.messageCoder=null;
		this.isFirstConnect=true;
		this.hostName=hostName;
		this.portNo=portNo;
		this.wsState = WsStateDisconnected;
	}	
	login(loginObj)
	{
		this.loginObj=loginObj;
		this.wsState = WsStateConnecting;
		this.ws = new WebSocket("ws://"+this.hostName+":"+this.portNo+"/websocket");
		this.ws.onopen = function (e) {
	        this.wsState = WsStateConnected;
	        if (this.wsState === WsStateConnected) {
	          this.sendPublicKey();
	        } else {
	          console.log('connection is closed or closing')
	        }
	      }.bind(this);
	    this.ws.onmessage = function (e) {
	    	this.serverResponseHandler(e);
	    }.bind(this);
	    this.ws.onerror = function (e) {
	          // TODO
	          this.errorHandler(e);
	        }.bind(this);
	}
	// virtual function
	sendPublicKey() 
    {
		var dt = new Date();
		var time = -(dt.getTime());
		this.keyCoder.getKey();
		//console.log("Public key="+keyCoder.getPublicKey());
		var publicKey=this.keyCoder.getPublicKey();
		publicKey=publicKey.replace("-----BEGIN PUBLIC KEY-----\n","");
		publicKey=publicKey.replace("\n-----END PUBLIC KEY-----","");
		console.log("Public key="+publicKey);
		this.ws.send(publicKey);
		this.keyCoder.setPrivateKey(this.keyCoder.getPrivateKey());
    }
	serverResponseHandler(evt)
	{
		var serverResponseMessage = evt.data;
		if (this.isFirstConnect)
		{	
			var decodedServerResponseMessage = this.keyCoder.decrypt(serverResponseMessage);
			console.log("decoded AES key="+decodedServerResponseMessage);
			var aesKey=JSON.parse(decodedServerResponseMessage);
			//console.log(aesKey.messageKey,aesKey.ivText);
			this.messageCoder=new MessageCoder(aesKey.messageKey,aesKey.ivText);
			this.ws.send(this.messageCoder.encode(JSON.stringify(this.loginObj)));
			this.isFirstConnect=false;
		}
		else
		{
			var responseString=this.messageCoder.decode(serverResponseMessage);
			console.log("decoded server response message="+responseString)
			var responseObj=JSON.parse(responseString);
			this.customHandler(responseObj);
		}	
	}
	sendRequest(requestObj) 
	{
		if (this.wsState === WsStateConnected) 
		{
			//this.ws.send(message);
			this.ws.send(this.messageCoder.encode(JSON.stringify(requestObj)));
		} 
		else 
		{
			console.log('connection is closed or closing')
		}
	}
	setServerResponseHandler(srh)
	{
		this.customHandler=srh;
	}
	errorHandler(evt)
	{
		alert(evt.type);
	}
	disConnect()
	{
	      //this.setreconnect(false);
	      if (this.ws !== null) {
	        if (this.wsState === WsStateConnected) {
	          this.wsState = WsStateDisconnecting;
	          //this.ws.close(1000, 'doclose');
	          this.ws.close();
	          this.isFirstConnect=true;
	        } else {
	          console.log('connection is not complete');
	        }
	      } else {
	        console.log('WebSocket session is null');
	      }
	}
}