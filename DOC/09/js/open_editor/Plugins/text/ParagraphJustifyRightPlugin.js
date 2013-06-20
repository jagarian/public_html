var ParagraphJustifyRightPlugin = {
	info: "오른쪽 정렬",
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"justRightBtn.gif");
		obj.setStyle('cursor',"pointer");	
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN+"justRightBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"justRightBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"justRightBtn_down.gif");
	},
	eventListener : function(evt){
		try{
			if ($client.browser == "IE") {
				if (!Selection.isEmpty()) {
					var dummy = '<P align="right">' + Selection.range.htmlText +'</P>';
					oEditor.insertTag(dummy);
					return;
				}
			}
			Selection.command("justifyright", false, null);
		}catch(e){
			debug.log('에러발생 : ' + e);
		}
	}
}