var HyperLinkPlugin = {
	info: "링크",
	parent : "",
	el : "",
	loaded : false,
	initialize : function(oEditor){
		if(!this.loaded){
			this.parent = oEditor.webeditorContainer;
			var hyperLayer = $new("DIV");
			hyperLayer.id = "hyperLayerForTom";
			hyperLayer.setStyles({
				"position" : "absolute",
				"top" : "0",
				"left" : "0",
				"width" :"200px",
				"height" : "70px",
				"border" : "1px solid #CCC",
				"background":"#FFFFFF",
				"display" : "none",
				"z-index" : "100000"
			});
			hyperLayer.setHTML(this.drawLayout());
			this.el = hyperLayer;
			$(this.parent).adopt(hyperLayer);
			this.loaded = true;
		}
	},
	drawLayout : function(){
		var sb = new StringBuffer();
		sb.append("<div style='padding-top:10px;padding-left:10px'>")
		.append("		<input type='text' id='hyperLocationForTom' style='border:1px solid #CCC;width:180px'>")
		.append("		<br/><div style='text-align:right;padding-right:10px;padding-top:8px'>")
		.append("		<input type='button' id='hyperLocationBtnForTom' value='입력' style='border:1px solid #CCC' onclick='HyperLinkPlugin.execCommand()'></div>")
		.append("	</div>");
		return sb.toString();
	},
	getInstance : function(){
		var obj = new Element("IMG");
		obj.setAttribute("src",TOM_SKIN+"linkBtn.gif");
		obj.setAttribute("width","40");
		obj.setStyle('cursor',"pointer");

		obj.addEvent("mouseover",function(evt){
			this.setAttribute("src",TOM_SKIN+"linkBtn_over.gif");
		});
		obj.addEvent("mouseout",function(evt){
			this.setAttribute("src",TOM_SKIN+"linkBtn.gif");
		});
		obj.addEvent("mousedown",function(evt){
			this.setAttribute("src",TOM_SKIN+"linkBtn_down.gif");
		});
		obj.addEvent("mouseup",function(evt){
			this.setAttribute("src",TOM_SKIN+"linkBtn.gif");
		});

		return obj;
	},
	show : function(target,str){
		var pos = positionUtil.getComparePosition(target,this.parent);
		$("hyperLayerForTom").setStyles({
			"display" : "block",
			"left" : pos.x + 1 +"px",
			"top" : pos.y + 20 +"px"
		});
		str = str || "";
		$("hyperLocationForTom").value = str;

	},
	hide : function(){
		$("hyperLayerForTom").setStyles({
			"display" : "none"
		});
	},
	eventListener : function(evt,obj,oEditor){
		try{
			this.parent = oEditor.webeditorContainer;
			$(this.parent).adopt(this.el);

			this.oEditor = oEditor;
			this.range = Selection.getRange();

			this.callBack = function(){
				try{
					if(this.range.parentElement()){
						if(this.range.parentElement().tagName == "A"){
							this.range.parentElement().setAttribute("href",$("hyperLocationForTom").value);
						}else{
							this.range.pasteHTML(this.locationStr);
							this.range.collapse(false);
							this.range.select();
						}
					}else{

					}
				}catch(e){}
			}

			if(this.range.parentElement().tagName == "A"){
				this.show(obj,this.range.parentElement().getAttribute("href"));
				return;
			}
			this.show(obj);

		}catch(e){
		}
	},
	execCommand : function(){
		if(this.range.htmlText){
			this.locationStr = "<a href='"+$("hyperLocationForTom").value+"'>"+this.range.htmlText+"</a>";
			this.callBack();
		}
		this.hide();
	}
}
