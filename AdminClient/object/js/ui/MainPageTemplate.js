class MainPageTemplate
{
	constructor()
	{
		this.mainDiv=document.createElement("div");
		this.mainDiv.style.marginLeft="10px";
	}
	setNavigationHeading(text)
	{
		this.mainDiv.appendChild(document.createTextNode(text));
	}
	addContent(domObject)
	{
		this.mainDiv.appendChild(domObject);
	}
	getHTML()
	{
		return this.mainDiv;
	}
}