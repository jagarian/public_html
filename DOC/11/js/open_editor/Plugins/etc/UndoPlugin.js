var UndoPlugin = {
	info: "�ǵ�����",
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"deli.gif");
		obj.setStyle('cursor',"pointer");
		return obj;
	},
	eventListener : function(){}
};