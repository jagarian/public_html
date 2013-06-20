var UndoPlugin = {
	info: "되돌리기",
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"deli.gif");
		obj.setStyle('cursor',"pointer");
		return obj;
	},
	eventListener : function(){}
};