var HTMLSourcePlugin = {
	info: "에디트 모드전환",
	group : "A",
	getInstance : function(){
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN+"HTMLbtn.gif");
		obj.setAttribute("width","40");
		obj.setStyle('cursor',"pointer");
		//obj.setStyle('margin-left',"3px");
		return obj;
	},
	eventListener : function(evt,obj,oEditor){
		try{
			obj.setAttribute("src",OPEN_EDITOR_SKIN+((oEditor.editMode == "TEXTAREA") ? "HTMLbtn.gif" : "HTMLbtn_change.gif"));
			oEditor.changeMode();
		}catch(e){
			debug.log('에러발생 : ' + e);
		}
	}
}