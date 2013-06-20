var ParagraphOutdentPlugin = {
	info: "내어쓰기",
	shortCutKey : "tab",
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"outdentBtn.gif");
		obj.setStyle('cursor',"pointer");
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN+"outdentBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"outdentBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"outdentBtn_down.gif");
	},
	eventListener : function(evt,obj,oEditor){
		try{
			Selection.command("outdent", false, null);			
		}catch(e){
			debug.log('에러발생 : ' + e);
		}
	}
}