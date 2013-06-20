var FontUnderLinePlugin = {
	info: "밑줄",
	shortCutKey : "ctrl+u",
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"fontUnderLineBtn.gif");
		obj.setStyle('cursor',"pointer");
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontUnderLineBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontUnderLineBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontUnderLineBtn_down.gif");
	},	
	eventListener : function(evt){
		try{
			Selection.command("underline", false, null);
		}catch(e){
			debug.log('에러발생 : ' + e);
		}
	}
}
