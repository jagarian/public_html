var ParagraphJustifyCenterPlugin = {
	info: "중앙 정렬",
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"justCenterBtn.gif");
		obj.setStyle('cursor',"pointer");
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN+"justCenterBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"justCenterBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"justCenterBtn_down.gif");
	},
	eventListener : function(evt,obj,oEditor){
		try{
			if ($client.browser == "IE") {
				if (!Selection.isEmpty()) {
					var dummy = '<P align="center">' + Selection.range.htmlText +'</P>';
					oEditor.insertTag(dummy);
					return;
				}
			}
			Selection.command("justifycenter", false, null);
		}catch(e){
			debug.log('에러발생 : ' + e);
		}
	}
}