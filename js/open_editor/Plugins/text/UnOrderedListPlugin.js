var UnOrderedListPlugin = {
	info: "기호목록",	
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"unOrderListBtn.gif");
		obj.setStyle('cursor',"pointer");
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN+"unOrderListBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"unOrderListBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"unOrderListBtn_down.gif");
	},
	eventListener : function(evt,btn,oEdtior){
		try{
			if (!Selection.isEmpty() && $client.browser == "IE") {
				var html = Selection.getHTML();
				var dummy = "<ul><li>" + Selection.getHTML().replace(new RegExp("<BR>", "gi"), "</li><li>") + "</li></ul>";
				oEdtior.insertTag(dummy);
			}
			else {
				Selection.command("insertunorderedlist", false, null);
			}
			
		}catch(e){
			debug.log('에러발생 : ' + e);
		}
	}
}