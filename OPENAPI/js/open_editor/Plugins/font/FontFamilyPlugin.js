var FontFamilyPlugin = {
	info: "글자종류",
	group : "A",
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
		this.el.addChild(this.drawFontFamilyTable(oEditor));
		this.loaded = true;
	},
	drawFontFamilyTable : function(oEditor){
		var fontKind = [		
		{text:"바탕",value:"'Batang', 'Serif'"},
		{text:"바탕체",value:"'BatangChe','Serif'"},
		{text:"굴림",value:"'Gulim', 'Sans-serif'"},
		{text:"굴림체",value:"'GulimChe', 'Sans-serif'"},
		{text:"돋움",value:"'Dotum','Sans-serif'"},
		{text:"돋움체",value:"'DotumChe', 'Sans-serif'"},
		{text:"Arial",value:"arial,helvetica,sans-serif"},
		{text:"Arial black",value:"'arial black',avant garde"},
		{text:"Book Antiqua",value:"'book antiqua',palatino"},
		{text:"Comic Sans MS",value:"'comic sans ms',sand"},
		{text:"Courier New",value:"'courier new',courier,monospace"},
		{text:"Tahoma",value:"tahoma,arial,helvetica,sans-serif"},
		{text:"Times New Roman",value:"'times new roman',times,serif"}
		];
		
		var table = $new("table");
		table.setAttribute("border","0");
		table.setAttribute("cellPadding","2px");
		table.setAttribute("cellSpacing","0px");		
		table.setAttribute("width","120px");
		table.setStyle("background-color","#FFFFFF");
		table.setStyle("border","2px solid #e97d81");
		var tbody = $new("tbody");		
		fontKind.foreach(function(el,idx){
			var tr = $new("tr");
			tr.addEvent("mouseover",function(evt){
				this.setStyle("background-color","#ffe8e8");
			});
			tr.addEvent("mouseout",function(evt){
				this.setStyle("background-color","#FFFFFF");
			});
			var td = $new("td");
			td.setStyle("font-family",el.value);
			td.setStyle("font-size","12px");
			td.setStyle("cursor","pointer");
			td.setHTML(el.text);
			td.addEvent("click",function(evt){
				try{
					Selection.command('fontname', false, el.value);												
				}catch(e){
					debug.log("FontFamily : " + e);
				}finally{
					FontFamilyPlugin.hide();
				}
				
			});
			tbody.addChild(tr);
			tr.addChild(td);
		});		
		
		table.addChild(tbody);
		return table;
		
	},
	getInstance : function(){		
		var obj = $new("IMG");
		obj.setAttribute("src",OPEN_EDITOR_SKIN + "fontKindBtn.gif");
		obj.setStyle('cursor',"pointer");
		obj.setStyle('margin-right',"3px");		
		return obj;
	},
	onMouseOver : function(){		
		this.setAttribute("src",OPEN_EDITOR_SKIN + "fontKindBtn_over.gif");
	},
	onMouseOut : function(){
		if(FontFamilyPlugin.mustShow)return;
		this.setAttribute("src",OPEN_EDITOR_SKIN + "fontKindBtn.gif");
	},
	onMouseDown : function(){
		this.setAttribute("src",OPEN_EDITOR_SKIN + "fontKindBtn_down.gif");
	},
	show : function(target){
		var pos = positionUtil.getComparePosition(target,this.parent);
		this.el.setStyle("display" , "block");
		this.el.setStyle("left" , (pos.x + 1)+"px");
		this.el.setStyle("top" , (pos.y + 20) +"px");
	},
	hide : function(){
		FontFamilyPlugin.el.setStyle("display","none");
	},
	eventListenerKey : "click",
	eventListener : function(evt,obj, oEditor){		
		FontFamilyPlugin.parent = oEditor.webeditorContainer;
		$E(FontFamilyPlugin.parent).addChild(FontFamilyPlugin.el);
		try{
			FontFamilyPlugin.show(obj);
		}catch(e){
			debug.log('에러발생 : ' + e);
		}		
	}
};
