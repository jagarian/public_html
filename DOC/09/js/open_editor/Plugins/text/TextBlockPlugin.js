var TextBlockPlugin = {
	info: "단락지정",
	parent : "",
	el : "",
	initialize : function(oEditor){
		this.parent = oEditor.webeditorContainer;
		this.drawLayer();			
	},
	getInstance : function(){
		var obj = new Element("IMG");
		obj.setAttribute("src",TOM_SKIN+"blockBtn.gif");
		obj.setStyle('cursor',"pointer");
		obj.setStyle('margin-left',"3px");
		obj.addEvent("mouseover",function(evt){
			this.setAttribute("src",TOM_SKIN+"blockBtn_over.gif");
		});
		obj.addEvent("mouseout",function(evt){
			this.setAttribute("src",TOM_SKIN+"blockBtn.gif");
		});
		obj.addEvent("mousedown",function(evt){
			this.setAttribute("src",TOM_SKIN+"blockBtn_down.gif");
		});
		obj.addEvent("mouseup",function(evt){
			this.setAttribute("src",TOM_SKIN+"blockBtn.gif");
		});
		return obj;
	},
	drawLayer : function(){
		if(this.loaded) return;

		var kUL = new Element("UL");
		kUL.id = "TOM__TextBlockLayer";
		kUL.setStyles({
			"position" : "absolute",
			"list-style-type" : "none",
			"padding" : "5px",
			"font-family" : "Gulim,Dotum",
			"font-size" : "12px",
			"border" : "1px solid #CCC",
			"width" : "75px",
			"display" : "none",
			"background-color" : "#FFF",
			"z-index" : "9999"
		});

		var sb = new StringBuffer();
		sb.append('<li style="height:16px"><span  id="TOM__TextBlockLayer__Color" style="cursor:pointer">색상블럭</span></li>')
		.append('<li style="height:16px"><span id="TOM__TextBlockLayer__StyleBlock1" title="제목1" style="cursor:pointer">제목 1</span></li>')
		.append('<li style="height:16px"><span id="TOM__TextBlockLayer__StyleBlock2" title="제목1" style="cursor:pointer">제목 2</span></li>')
		.append('<li style="height:16px"><span id="TOM__TextBlockLayer__StyleBlock3" title="제목1" style="cursor:pointer">제목 3</span></li>')
		.append('<li style="height:16px"><span id="TOM__TextBlockLayer__StyleBlock4" title="제목1" style="cursor:pointer">제목 4</span></li>');

		kUL.setHTML(sb.toString());
		$(this.parent).adopt(kUL);
		this.el = kUL;

		$("TOM__TextBlockLayer__Color").addEvent("click",function(evt){
			TextBlockPlugin.doAttachColorBlock(this);
		});

		$("TOM__TextBlockLayer__StyleBlock1").addEvent("click",function(evt){
			TextBlockPlugin.doAttachStyleBlock("1");
			TextBlockPlugin.hide();
		});

		$("TOM__TextBlockLayer__StyleBlock2").addEvent("click",function(evt){
			TextBlockPlugin.doAttachStyleBlock(2);
			TextBlockPlugin.hide();
		});

		$("TOM__TextBlockLayer__StyleBlock3").addEvent("click",function(evt){
			TextBlockPlugin.doAttachStyleBlock(3);
			TextBlockPlugin.hide();
		});

		$("TOM__TextBlockLayer__StyleBlock4").addEvent("click",function(evt){
			TextBlockPlugin.doAttachStyleBlock(4);
			TextBlockPlugin.hide();
		});

		this.loaded = true;
	},
	doAttachColorBlock : function(obj){
		ColorPicker.callBack = function(){
			var blockDiv = new Element("DIV");
			blockDiv.className = "TOMBlockLayer";
			blockDiv.setStyles({
				"background-color" : ColorPicker.selectedColor,
				"padding" : "8px"
			});
			TextBlockPlugin.oEditor.insertTag(blockDiv);
		}
		ColorPicker.show(obj);
	},
	doAttachStyleBlock : function(kind){
		var blockStyle = new Element("H"+kind);
		blockStyle.setHTML("제목"+kind);
		blockStyle.className="TOMBlockLayer";
		blockStyle.setStyles({
			"padding" : "5px",
			"border-bottom" : "2px solid #000"
		});
		TextBlockPlugin.oEditor.insertTag(blockStyle);
	},
	show : function(target){
		var pos = positionUtil.getComparePosition(target,this.parent);
		$("TOM__TextBlockLayer").setStyles({
			"display" : "block",
			"left" : (pos.x + 1)+"px",
			"top" : (pos.y + 20) +"px"
		});

	},
	hide : function(){
		$("TOM__TextBlockLayer").setStyles({
			"display" : "none"
		});
	},
	eventListener : function(evt,obj,oEditor){
		TextBlockPlugin.parent = oEditor.webeditorContainer;
		$(TextBlockPlugin.parent).adopt(TextBlockPlugin.el);
		TextBlockPlugin.oEditor = oEditor;
		try{
			TextBlockPlugin.show(obj);
		}catch(e){
			debug.log('에러발생 : ' + e);
		}
	}
}