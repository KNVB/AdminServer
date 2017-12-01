function KeyCoder(encryptionExponent, decryptionExponent, modulus)
{
	setMaxDigits(130);
	var key = new RSAKeyPair(encryptionExponent, decryptionExponent, modulus);
	this.encode=function(data)
				{
				 return encryptedString(key,data);
				}							
	this.decode=function(data)
				{
				 return stringReverse(decryptedString(key,data));
				}
	function stringReverse(str) 
	{
	   if (!str) return '';
	   var revstr='';
	   for (i = str.length-1; i>=0; i--)
	       revstr+=str.charAt(i)
	   return revstr;
	};
}