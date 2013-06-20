/**
 * @author 오창훈(lovedev)
 * @version 0.1
 * @license 
 * @since 2008
*/

//설정필요
var OPEN_EDITOR_PATH = "./js/open_editor";

var OPEN_EDITOR_PLUGIN_SET = OPEN_EDITOR_PLUGIN_SET  || "DefaultPluginSet";
var OPEN_EDITOR_SKIN = OPEN_EDITOR_SKIN || OPEN_EDITOR_PATH + "/Skins/Default/";
var OPEN_EDITOR_PLUGIN_PATH = OPEN_EDITOR_PATH + "/Plugins/";

$include(OPEN_EDITOR_PATH + "/Utils/jstparser.js");
$include(OPEN_EDITOR_PATH + "/Utils/ColorPicker.js");

var OPEN_EDITOR_BASIC_PLUGIN = [];
var open_editor_cnt = 0;

var positionUtil = {
	getComparePosition : function(a,b){
		var pos = {};
		pos.x = ($E(b).getTag() != "body") ? $E(a).getOffset("left") - $E(b).getOffset("left") : $E(a).getOffset("left");
		pos.y = ($E(b).getTag() != "body") ? $E(a).getOffset("top") - $E(b).getOffset("top") : $E(a).getOffset("top");
		return pos;
	}
};


var loadPlugin = function(){
	var tmpPluginSetObj = {
		loaded : false,
		plugins : []		
	};	
	var path = "";
	for(var i=0;i< arguments.length;i++){
		path = arguments[i].split(".").join("/");
		$include(OPEN_EDITOR_PLUGIN_PATH + path+".js");
		tmpPluginSetObj.plugins.push(arguments[i].split(".").pop());		
	}
	OPEN_EDITOR_BASIC_PLUGIN.push(tmpPluginSetObj);	
	tmpPluginSetObj = null;
};

var OpenEditor = JsClass({
	main : function(options){
		if(options){
			OPEN_EDITOR_PLUGIN_SET = options.pluginset || OPEN_EDITOR_PLUGIN_SET;
			OPEN_EDITOR_SKIN = options.skin || OPEN_EDITOR_SKIN;
		}		
							
		if(this.loadPluginSet()){
			this.setOptions(options);
			this.initVal();
			this.drawLayout();			
		}
	},
	loadPluginSet : function(){
		$include(OPEN_EDITOR_PATH + "/PluginSet/"+OPEN_EDITOR_PLUGIN_SET+".js");
		return true;
	},
	setOptions: function(options){
		this.options = {
			editMode : "HTML",
			properties : {
				"width" : "100%",
				"height" : "300px",
				"frame-border" : "0",
				"allowtransparency" : false
			},
			styles : {
				"width" : "710px",
				"height": "300px",
				"margin" : "0px"	,
				"border" : "1px solid #ccc"
			},
			toolbar_bg : "#f2f2f2"
		};
		for(var prop in options)this.options[prop] = options[prop];
	},
	initVal : function(){
		this.registedPlugin = [];
		this.hideLayer = [];
		this.groupA = [];
		this.groupB = [];
		this.editMode =  "HTML";
		this.webeditorContainer = null;		
		this.toolbarContainer = $new("DIV");
		this.frameContainer = $new("DIV");
		this.frame = $new("IFRAME");
		this.frame._this = this;
		this.textarea = $new("TEXTAREA");
		this.webeditor = $new("DIV");		
		this.editMode = this.options.editMode;
		this.charset = this.options.charset || "euc-kr";
		this.isXhtml = this.options.xhtml || true;		
		this.webeditorStyles = {
			"background-color" : this.options.toolbar_bg,
			"border" : "1px solid #BBB",
			"width" : (parseInt(this.options.styles.width,10)+2) + "px",
			"padding" : "2px"		
		};
		this.textareaStyles = {			
			"width" : "100%",
			"margin" : "0px",
			"padding" : "0px",
			"border" : "0px",
			"display" : "none"
		};		
		this.toolbarStyles = {
			"width" : "100%",
			"textAlign" : "left",
			"clear" : "both",
			"padding-bottom" : "2px",
			"padding-left" : "1px"
		};
	},	
	drawLayout : function(){
		$each(this.webeditorStyles,function(val,prop){
			this.webeditor.setStyle(prop,val);
		},this);
		
		$each(this.options.properties, function(val,prop){
			this.frame.setAttribute(prop.camelCase(),val);
		},this);
		
		$each(this.options.styles,function(val,prop){			
			this.frame.setStyle(prop,val);
			this.textareaStyles[prop] = val;
		},this);
		
		$each(this.textareaStyles,function(val,prop){
			this.textarea.setStyle(prop,val);
		},this);
		
		$each(this.toolbarStyles,function(val,prop){
			this.toolbarContainer.setStyle(prop,val);
		},this);
		
		this.toolbarContainer.setStyle("margin-bottom","3px");
		this.frameContainer.setStyle("width" , "100%");		
		this.frameContainer.addChild(this.frame);
		this.frameContainer.addChild(this.textarea);
	},	
	changeMode : function(){		
		try{
			this.editMode = (this.editMode == "HTML") ? "TEXTAREA" : "HTML";
			if(this.editMode == "HTML"){
				this.updateHTMLData();
				this.frame.setStyle('display','block');
				this.textarea.setStyle('display','none');
			}else{
				this.updateTEXTData();
				this.frame.setStyle('display','none');
				this.textarea.setStyle('display','block');
			}
		}catch(e){
			
		}
	},	
	getData : function(){ return this.frame.contentWindow.document.body.innerHTML; },
	setData : function(val){ this.frame.contentWindow.document.body.innerHTML = this.textarea.value = val; },
	updateHTMLData : function(){ this.frame.contentWindow.document.body.innerHTML = this.textarea.value; },
	updateTEXTData : function(){ this.textarea.value = this.getData(); },	
	write: function(textarea,flag){		
		var target = $E(textarea);
		var realTarget = null;
		while($E(target).parentNode){
			if($E(target).parentNode.tagName == "BODY") break;
			target = $E(target).parentNode;
			if(target.tagName == "DIV"){				
				if($E(target).getStyle("overflow") == "scroll" 
					|| $E(target).getStyle("overflow") == "auto" 
					|| $E(target).getStyle("overflow-y") == "scroll"
					|| $E(target).getStyle("overflow-y") == "auto"
					|| $E(target).getStyle("overflow-y") == "0"
					){
					$E(target).setStyle("position" , "relative");
					realTarget = target;					
					break;
				}
			}
		}		
		this.webeditorContainer = realTarget ? realTarget : document.body;
		if ($E(textarea)) {
			this.onLoad.bindThis(this,[textarea,flag])();
		}
		else {
			throw new Error("대상 TEXTAREA가 필요합니다.");
		}
	},
	onLoad : function(textarea,flag){		
		var contents = $E(textarea).value;
		this.textarea.name = $E(textarea).name;
		this.textarea.id = $E(textarea).id;
		this.frame.id = "frame_" + $E(textarea).id;
		this.frame.name = "frame_" + $E(textarea).id;
		this.toolbarContainer.id = "toolbar_"+$E(textarea).id;
		this.webeditor.id = "webeditorDiv_"+$E(textarea).id;			
		this.webeditor.addChild(this.toolbarContainer);
		this.webeditor.addChild(this.frameContainer);
		$E(textarea).replaceChildNode(this.webeditor);
		this.onWriteCompleteEvent.bindThis(this,[contents,flag])();		
		
	},
	onWriteCompleteEvent : function(contents,flag){
		contents = contents || "";
		flag = flag || false;
		if(this.injectInitContent.bindThis(this,[contents])()){
			this.initEventHandler.bindThis(this,[flag])();
		};
	},
	injectInitContent : function(contents){
		this.textarea.value = contents;		
		contents = contents || "";	
		
		var xhtmlDocType = this.isXhtml ? '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">' : "";
		var xhtmlXmlns = this.isXhtml ? 'xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko"' : "";
		var sb = new StringBuffer();
		sb.append(xhtmlDocType)
		.append('<html '+xhtmlXmlns+'><head>')
		.append('<meta http-equiv="content-type" content="text/html; charset='+this.charset+'" />')
		.append('<link rel="stylesheet" type="text/css" href="'+OPEN_EDITOR_SKIN+'OPEN_EDITOR.css"/>')
		.append("</head><body>")
		.append(contents)
		.append("</body></html>");
		
		this.frame.contentWindow.document.open("text/html", "replace");
		this.frame.contentWindow.document.write(sb.toString());
		this.frame.contentWindow.document.close();
		this.frame.contentWindow.document.designMode = "On";	
		return true;
	},
	initEventHandler : function(flag){
		var oEditor = this;		
		function editorKeyDownHandler(evt){	
			evt = evt || window.event;		
			oEditor.customKeyDownEvent(evt, oEditor);
			if (evt.keyCode == 13) {
				oEditor.setSelection();
				var range = Selection.getRange();
				
				if ($client.browser == "IE" && range.parentElement().tagName != "LI") {
					evt.returnValue = false;
					evt.cancelBubble = true;
					range.pasteHTML("<br />");
					range.collapse(false);
					range.select();
				}
			}
		}
		
		function editorloadHandler(evt){
				oEditor.configuratePlugins();					
				oEditor.configPlugin(oEditor,"A");
				oEditor.configPlugin(oEditor,"B");
				
				if(oEditor.editMode == "TEXTAREA"){
					oEditor.editMode = "HTML";
					oEditor.changeMode();
				}else{
					oEditor.editMode = "TEXTAREA";
					oEditor.changeMode();
				}

			oEditor.customLoadEvent(evt, oEditor);
		}
		
		function editorMouseDownHanlder(evt){			
			try{
				evt = evt || oEditor.frame.contentWindow.event;
				oEditor.selectedObj = evt.srcElement || evt.target;
				oEditor.customMouseDownEvent(evt, oEditor);
			}catch(e){}
		}
		
		function editorBlurHanlder(evt){
			if(oEditor.editMode == "HTML"){
				oEditor.updateTEXTData();
			}
		}
		
		function editorClickHanlder(evt){
			if(oEditor.editMode == "HTML"){
				oEditor.hidePGLayer();	
			}
		}
		this.addEvent("keydown",editorKeyDownHandler);
		this.addEvent("mousedown",editorMouseDownHanlder);		
		this.addEvent("click",editorClickHanlder);

		$E(this.frame.contentWindow).addEvent('load',editorloadHandler);
		
		var evtTarget = ($client.browser == "IE") ? this.frame.contentWindow : this.frame.contentWindow.document;		
		$E(evtTarget).addEvent("blur",editorBlurHanlder);
	},
	customKeyDownEvent : function(){},
	customMouseDownEvent : function(){},	
	customLoadEvent : function(){},
	configPlugin : function(oEditor,group){
		var groupToolbarLst = oEditor["group"+group];		
		if(groupToolbarLst.length > 0){
			var toolbar = $new("DIV");
			toolbar.setStyle("clear" , "both");
			toolbar.setStyle("margin-top" , "3px");
			groupToolbarLst.foreach(function(el){toolbar.addChild(el);});
			oEditor.toolbarContainer.addChild(toolbar);
		}
	},	
	addPlugin : function(pluginObj){
		var where = pluginObj.group || "A";		
		if(typeof pluginObj.main == "function") pluginObj.main(this);		
		if(typeof(pluginObj.hide) == "function")this.hideLayer.push(pluginObj.hide);
		try{
			var btn = pluginObj.getInstance();		
			btn.setAttribute("alt",pluginObj.info);
			pluginObj.eventListenerKey = pluginObj.eventListenerKey || "click";	
		
			btn.addEvent(pluginObj.eventListenerKey,function(evt,oEditor){			
				try{
					oEditor.focus();
					evt = evt || window.event;
					if(oEditor.editMode == "TEXTAREA" && btn.alt != "에디트 모드전환") return;				
					Selection.setSelection(oEditor);
					oEditor.setSelection();
					oEditor.hidePGLayer();
					pluginObj.eventListener(evt,btn,oEditor);
				}catch(e){
					
					alert(e);
				}
				
			},[this,btn]);	
		}catch(e){

		}
		
		if(typeof pluginObj.onMouseOver == "function")btn.addEvent("mouseover",pluginObj.onMouseOver);
		if(typeof pluginObj.onMouseDown == "function")btn.addEvent("mousedown",pluginObj.onMouseDown);
		if(typeof pluginObj.onMouseOut == "function")btn.addEvent("mouseout",pluginObj.onMouseOut);

		this["group"+where].push(btn);
	},
	registPlugin : function(pluginObj){
		this.registedPlugin.push(pluginObj);
	},	
	setBasicPlugin : function(){		
		var plugins = [];
		
		plugins = (OPEN_EDITOR_BASIC_PLUGIN.length > open_editor_cnt) ? plugins.concat(OPEN_EDITOR_BASIC_PLUGIN[open_editor_cnt].plugins).concat(this.registedPlugin) 
		:
		plugins.concat(OPEN_EDITOR_BASIC_PLUGIN[OPEN_EDITOR_BASIC_PLUGIN.length - 1].plugins).concat(this.registedPlugin);
		
		this.registedPlugin = [];
		
		for(i=0;i < plugins.length;i++)
			this.registedPlugin.push(eval(plugins[i]));
		
		plugins = null;
		open_editor_cnt++;
		return true;
	},
	configuratePlugins : function(){		
		if(this.setBasicPlugin()){
			for(var i =0;i < this.registedPlugin.length;i++){
				this.addPlugin(this.registedPlugin[i]);
			}	
		}		
	},
	removePlugin : function(obj){
		this.registedPlugin.remove(obj);
	},
	hidePGLayer : function(){		
		this.hideLayer.foreach(function(el,idx){			
			el();
		},this);
	},
	insertImage : function(src){
		if(!Selection.win) this.setSelection();		
		var _img = $new("IMG");
		_img.setAttribute("src",src);
		Selection.injectElement(_img);
		this.hidePGLayer();
	},
	insertTag : function(tag){
		if(!Selection.win) this.setSelection();
		if(typeof tag == "string"){
			Selection.injectTag(tag);
		}else{
			Selection.injectElement(tag);
		}
		this.hidePGLayer();
	},
	setSelection : function(){		
		Selection.setSelection(this);
		try{
			Selection.init();
		}catch(e){}
	},
	focus : function(){		
		document.getElementsByName(this.frame.name)[0].focus();
		this.setSelection();
	},
	getElement : function(id){
		return this.frame.contentWindow.document.getElementById(id);
	},
	addEvent : function(evtType,fn,args){
		$E(this.frame.contentWindow.document).addEvent(evtType,fn,args);
	},
	removeEvent : function(evtType,fn){
		$E(this.frame.contentWindow.document).removeEvent(evtType,fn);
	}
});

var Selection = {
	doc : "",
	editor : "",
	range : "",
	selection : "",
	tempid : "",
	win :"",
	init : function(){
		Selection.range = Selection.getRange();
		Selection.selection = Selection.getSelection();
	},
	setSelection : function(oEditor){		
		Selection.editor = oEditor;
		Selection.doc = oEditor.frame.contentWindow.document;
		Selection.win = oEditor.frame.contentWindow;
	},
	getSelection : function(){
		if (this.win.getSelection) {
			return Selection.win.getSelection();
		} else if (Selection.doc.selection) {
			return Selection.doc.selection;
		} else {
			return null;
		}
	},
	getRange : function(){		
		try{
			Selection.win.focus();
		}catch(e){}
		return (($client.browser == "IE")? Selection.getSelection().createRange() : Selection.getSelection().getRangeAt(0));
	},
	isEmpty : function(){
		var flag = ($client.browser == "IE") ? Selection.range.text.length == 0 : Selection.range.toString().length == 0;
		return flag;
	},
	getHTML : function(){
		if(this.range.htmlText){
			return Selection.range.htmlText;
		}else{
			var dummy = $new("div");
			var contentNode = Selection.range.extractContents();
			dummy.addChild(contentNode);
			return dummy.innerHTML;	
		}
	},
	getParent : function(){
		return ($client.browser == "IE") ? $E(Selection.range.parentElement()) :
			$E(Selection.range.commonAncestorContainer.parentNode);
	},
	getTempId : function(){
		if(Selection.tempid != "") return  Selection.tempid;
		Selection.tempid = "tempid_" + Math.round(Math.random()*200);
		return this.tempid;
	},
	chkEmptyContents : function(_command,val){
		if(!this.isEmpty()) return false;
		switch(_command){
			case "fontname" :
				Selection.injectTag("<font face="+val+"></font>");				
			break;
			case "fontsize" :
				Selection.injectTag("<font size="+val+"></font>");
			break;
			case "bold" :
				Selection.injectTag("<b></b>");
			break;
			case "strikethrough" :
				Selection.injectTag("<strike></strike>");
			break;
			case "italic" :
				Selection.injectTag("<em></em>");
			break;
			case "underline" :
				Selection.injectTag("<u></u>");
			break;
			case "ForeColor" :						
				Selection.injectTag("<font color="+val+"></font>");		
			break;
			case "BackColor" :
				Selection.injectTag("<font style='background-color: "+val+"'></font>");
			break;			
			default :
			return false;
		}
		return true;
	},
	command : function(_command,capture,val){
		val = val  || null;
		capture = capture || false;
		Selection.win.focus();
		if(Selection.range.execCommand){
			if(Selection.chkEmptyContents(_command,val))return;
			Selection.range.execCommand(_command, capture, val);
			Selection.range.select();
		}else{
			Selection.doc.execCommand(_command, capture, val);
		}
	},
	injectElement : function(el){
		
		Selection.editor.focus();

		if(typeof el == "string"){			
			Selection.injectTag(el);			
		}else{			
			var dummy = $new("DIV");
			dummy.addChild(el);			
			Selection.injectTag(dummy.innerHTML);			
			dummy.remove();
		}
	},	
	injectTag : function(str){
		Selection.editor.focus();
		if($client.browser == "IE"){
			var dummy = $new("div");
			dummy.setHTML(str);	
			if(dummy.getChildAt(0).innerHTML == "" && $E(dummy.getChildAt(0)).getTag() != "img"){
				Selection.win.focus();				
				dummy.getChildAt(0).innerHTML = "&nbsp;<span id='OpenEditor_focusElement'></span>";				
				Selection.range.pasteHTML(dummy.innerHTML);				
				var ofocus = $E(Selection.doc.getElementById("OpenEditor_focusElement"));
				Selection.range.moveToElementText(ofocus);
				Selection.range.select();
				ofocus.parentNode.innerHTML = "";
				try{
					ofocus.remove();
				}catch(e){}	
			}else{				
				this.range.pasteHTML(str);	
			}
		}else{			
			Selection.range.insertNode(Selection.range.createContextualFragment(str));
		}
	},
	clearTempId : function(){
		Selection.tempid = "";
	}

};