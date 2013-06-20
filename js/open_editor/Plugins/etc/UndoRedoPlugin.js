var UndoRedoPlugin = {
	info: "UndoRedoPlungin",
	main : function(oEditor){
		oEditor.addEvent("keyup", UndoRedoPlugin.keyupHandler)
	},
	getInstance : function(){
		var obj = $new("span");
		obj.setHTML("UNDO/REDO")
		return obj;
	},
	eventListener : function(){
		
	},
	keyupHandler : function(e){
		alert(e.keyCode);
	}
};