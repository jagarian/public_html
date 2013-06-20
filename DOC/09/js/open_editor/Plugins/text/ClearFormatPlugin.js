var ClearFormatPlugin = {
	info: "초기화",
	group : "A",
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"clearFormatBtn.gif");
		obj.setStyle('cursor',"pointer");
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN+"clearFormatBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"clearFormatBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"clearFormatBtn_down.gif");
	},
	eventListener : function(evt,obj,oEditor){
		try{
			Selection.command("removeformat", false, null);
		}catch(e){
			debug.log('에러발생 : ' + e);
		}
	}
}