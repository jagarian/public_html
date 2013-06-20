var ParagraphIndentPlugin = {
	info: "들여쓰기",
	shortCutKey : "alt+tab",
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"indentBtn.gif");
		obj.setStyle('cursor',"pointer");		
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN+"indentBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"indentBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"indentBtn_down.gif");
	},	
	eventListener : function(evt,obj,oEditor){
		try{
			if ($client.browser == "IE") {
				if(!Selection.isEmpty()){
					var dummy = '<BLOCKQUOTE dir=ltr style="MARGIN-RIGHT: 0px;">' + Selection.range.htmlText +'</BLOCKQUOTE>';
					oEditor.insertTag(dummy);
					return;					
				}					
			}
			Selection.command("indent", false, null);
		}catch(e){
			debug.log('에러발생 : ' + e);
		}
	}
}