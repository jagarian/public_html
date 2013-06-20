var FontBackGroundColorPlugin = {
	info: "배경색상",
	main : function(oEditor){
		ColorPicker.init(oEditor);		
	},
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"fontBgColorBtn.gif");
		obj.setStyle('cursor',"pointer");
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontBgColorBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontBgColorBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN+"fontBgColorBtn_down.gif");
	},
	hide : function(){
		ColorPicker.hide();
	},
	eventListener : function(evt,obj,oEditor){
		try{
			ColorPicker.callBack = function(){
				if ($client.browser == "IE") {
					Selection.command("BackColor",false,ColorPicker.selectedColor);
				}else{
					Selection.command("HiliteColor",false,ColorPicker.selectedColor);
				}
			}
			ColorPicker.parent = oEditor.webeditorContainer;
			ColorPicker.parent.addChild(ColorPicker.el);
			ColorPicker.show(obj);
		}catch(e){
			debug.log('에러발생 : ' + e);
		}
	}
}