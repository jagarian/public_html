var FontColorPlugin = {
	info: "글자색상",
	main : function(oEditor){
		ColorPicker.init(oEditor);		
	},
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"fontColorBtn.gif");
		obj.setStyle('cursor',"pointer");
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontColorBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontColorBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontColorBtn_down.gif");
	},
	hide : function(){
		ColorPicker.hide();
	},
	eventListener : function(evt,obj,oEditor){
		try{
			ColorPicker.callBack = function(){
				Selection.command("ForeColor", false, ColorPicker.selectedColor);
			}
			ColorPicker.parent = oEditor.webeditorContainer;
			ColorPicker.parent.addChild(ColorPicker.el);
			ColorPicker.show(obj);
		}catch(e){
			alert('에러발생 : ' + e);
		}
	}
}