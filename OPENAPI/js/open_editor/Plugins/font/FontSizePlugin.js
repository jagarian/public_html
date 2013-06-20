var FontSizePlugin = {
	info: "글자크기",
	main : function(oEditor){
		if(this.loaded) return;
		this.parent = $E(oEditor.webeditorContainer);
		this.el = $new("div");
		this.el.setStyle("width","100px");
		this.el.setStyle("height","100px");
		this.el.setStyle("position","absolute");
		this.el.setStyle("left","0");
		this.el.setStyle("top","0");
		this.el.setStyle("display","none");
		this.el.setStyle("z-index","999");
		this.parent.addChild(this.el);
		this.el.addChild(this.drawFontSizeTable(oEditor));		
		this.loaded = true;
	},
	getInstance : function(){		
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN + "fontSizeBtn.gif");
		obj.setStyle('cursor',"pointer");
		return obj;
	},
	drawFontSizeTable : function(oEditor){
		var sizeKind = [		
		{text:"가나다(8pt)",value:"1"},
		{text:"가나다(10pt)",value:"2"},
		{text:"가나다(12pt)",value:"3"},
		{text:"가나다(14pt)",value:"4"},
		{text:"가나다(18pt)",value:"5"},
		{text:"가나다(24pt)",value:"6"},
		{text:"가나다(36pt)",value:"7"}
		];
		
		var table = $new("table");
		table.setAttribute("border","0");
		table.setAttribute("cellPadding","2px");
		table.setAttribute("cellSpacing","0px");		
		table.setAttribute("width","300px");
		table.setStyle("background-color","#FFFFFF");
		table.setStyle("border","2px solid #e97d81");
		var tbody = $new("tbody");		
		sizeKind.foreach(function(el,idx){
			var tr = $new("tr");
			tr.addEvent("mouseover",function(evt){
				this.setStyle("background-color","#ffe8e8");
			});
			tr.addEvent("mouseout",function(evt){
				this.setStyle("background-color","#FFFFFF");
			});
			var td = $new("td");
			td.setStyle("font-family","Dotum,Gulim");			
			td.setStyle("cursor","pointer");
			td.setHTML("<font size="+el.value+">" + el.text + "</font>");
			td.addEvent("click",function(evt){
				try{
					Selection.command('fontsize', false, el.value);
				}catch(e){
					debug.log("FontSize : " + e);
				}finally{
					FontSizePlugin.hide();
				}
				
			});
			tbody.addChild(tr);
			tr.addChild(td);
		});		
		
		table.addChild(tbody);
		return table;
		
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN + "fontSizeBtn_over.gif");
	},
	onMouseOut : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN + "fontSizeBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN + "fontSizeBtn_down.gif");
	},
	show : function(target){
		var pos = positionUtil.getComparePosition(target,this.parent);
		this.el.setStyle("display" , "block");
		this.el.setStyle("left" , (pos.x + 1)+"px");
		this.el.setStyle("top" , (pos.y + 20) +"px");
	},
	hide : function(){		
		FontSizePlugin.el.setStyle("display","none");
	},
	eventListenerKey : "click",
	eventListener : function(evt,obj,oEditor){		
		FontSizePlugin.parent = oEditor.webeditorContainer;
		$E(FontSizePlugin.parent).addChild(FontSizePlugin.el);
		try{
			FontSizePlugin.show(obj);
		}catch(e){
			debug.log('에러발생 : ' + e);
		}		
	}
}
