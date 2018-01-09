class ModalList
{
	constructor(callBack)
	{
		this.moduleListDiv=document.createElement("div");
		this.moduleListDiv.id="moduleListDiv";
		this.logoutLink=document.createElement("a");
		this.logoutLink.className="w3-bar-item w3-button"; 
		this.logoutLink.addEventListener('click',function()
								  {
									  callBack();
								  });
		this.logoutLink.text="Logout";
		this.moduleListDiv.appendChild(this.logoutLink);
	}
	collapse()
	{
		$(this.moduleListDiv).children("div").each(function ()
									{
										$(this).children("a").removeClass("w3-red");	
										$(this).children("div").addClass("w3-hide");
									});
	}
	addModalUI(modalUI)
	{
		this.moduleListDiv.insertBefore(modalUI,this.logoutLink);
	}
	updateSideNavigation(obj)
	{
		$(this.moduleListDiv).children("div").each(function ()
									{
										$(this).children("a").removeClass("w3-red");	
										$(this).children("div").addClass("w3-hide");
									});
		$(obj).addClass("w3-red");
		$(obj).next("div").removeClass("w3-hide");
	}
	getModalList()
	{
		return this.moduleListDiv;
	}
}