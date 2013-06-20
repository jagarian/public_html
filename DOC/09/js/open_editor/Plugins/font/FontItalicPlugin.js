var FontItalicPlugin = {
	info: "기울게",
	shortCutKey : "ctrl+i",
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"fontItalicBtn.gif");
		obj.setStyle('cursor',"pointer");
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontItalicBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontItalicBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontItalicBtn_down.gif");
	},
	eventListener : function(evt){
		try{
			Selection.command("italic", false, null);
		}catch(e){
			debug.log('에러발생 : ' + e);
		}
	}
}