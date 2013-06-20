var ParagraphJustifyLeftPlugin = {
	info: "왼쪽 정렬",
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"justLeftBtn.gif");
		obj.setStyle('cursor',"pointer");
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN+"justLeftBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"justLeftBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"justLeftBtn_down.gif");
	},
	eventListener : function(evt){
		try{
			if ($client.browser == "IE") {
				if (!Selection.isEmpty()) {
					var dummy = '<P align="left">' + Selection.range.htmlText +'</P>';
					oEditor.insertTag(dummy);
					return;
				}
			}
			Selection.command("justifyleft", false, null);
		}catch(e){
			debug.log('에러발생 : ' + e);
		}
	}
}