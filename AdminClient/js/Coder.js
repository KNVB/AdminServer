function Coder(encryptionExponent, decryptionExponent, modulus)
{
	setMaxDigits(130);
	key = new RSAKeyPair(encryptionExponent, decryptionExponent, modulus);
	this.encode=function(data)
				{
				 return encryptedString(key,data);
				}							
	this.decode=function(data)
				{
				 return decryptedString(key,data);
				}			
}