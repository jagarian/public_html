var FontBoldPlugin = {
	info: "굵게",
	shortCutKey : "ctrl+b",
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"fontBoldBtn.gif");		
		obj.setStyle('cursor',"pointer");
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontBoldBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontBoldBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontBoldBtn_down.gif");
	},	
	eventListener : function(evt){
		try{
			Selection.command("bold", false, null);
		}catch(e){
			debug.log(e);
		}
	}
}