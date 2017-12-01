function Coder(messageKey,ivText,privateKey,modulus)
{
	keyCoder=new KeyCoder("",privateKey,modulus);
	messageKey=keyCoder.decode(messageKey);
	ivText=keyCoder.decode(ivText);
	var messageCoder=new MessageCoder(messageKey,ivText);
	this.encode=function(plainText)
				{
					return messageCoder.encode(plainText);
				}
	this.decode=function (encodedText)
				{
					return messageCoder.decode(encodedText);
				}
}
function MessageCoder(key,iv)
{
	var ivArray=CryptoJS.enc.Base64.parse(iv);
	var keyArray=CryptoJS.enc.Base64.parse(key);
	this.encode=function(plainText)
				{
					var result;
					var encoded=CryptoJS.AES.encrypt(plainText,
												 keyArray,
												 {
														iv: ivArray,
														mode: CryptoJS.mode.CTR,
														padding: CryptoJS.pad.NoPadding
												  });
					result=encoded.ciphertext.toString(CryptoJS.enc.Base64);
					return  result;
				}
	this.decode=function (encodedText)
				{
					var encryptedArray=CryptoJS.enc.Base64.parse(encodedText);
					var decrypted = CryptoJS.AES.decrypt(
									{ ciphertext:encryptedArray},
									keyArray, 
									{
										iv: ivArray,
										mode: CryptoJS.mode.CTR,
										padding: CryptoJS.pad.NoPadding
									});
		 
					// 转换为 utf8 字符串
					decrypted = CryptoJS.enc.Utf8.stringify(decrypted);
					return decrypted;
				}
}
