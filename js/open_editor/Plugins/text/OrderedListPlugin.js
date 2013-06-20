var OrderedListPlugin = {
	info: "숫자목록",
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"orderListBtn.gif");
		obj.setStyle('cursor',"pointer");		
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN+"orderListBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"orderListBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"orderListBtn_down.gif");
	},
	eventListener : function(evt,btn,oEdtior){
		try{
			if(!Selection.isEmpty() && $client.browser == "IE"){
				var html = Selection.getHTML();
				var dummy = "<ol><li>" + Selection.getHTML().replace(new RegExp("<BR>", "gi"), "</li><li>") + "</li></ol>";
				oEdtior.insertTag(dummy);				
			}else{				
				Selection.command("insertorderedlist", false, null);	
			}			
		}catch(e){
			debug.log('에러발생 : ' + e);			
		}
	}
}