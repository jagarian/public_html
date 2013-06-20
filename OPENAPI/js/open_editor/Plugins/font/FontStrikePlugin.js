var FontStrikePlugin = {
	info: "취소선",	
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"fontStrikeBtn.gif");
		obj.setStyle('cursor',"pointer");
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontStrikeBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontStrikeBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontStrikeBtn_down.gif");
	},
	eventListener : function(evt){
		try{
			Selection.command("strikethrough", false, null);
		}catch(e){
			debug.log('에러발생 : ' + e);
		}
	}
}