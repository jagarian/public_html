var $client = {
	browser : "",
	os : "",
	initialize : function(){	
		var userAgent = navigator.userAgent.toLowerCase();		
		var browser = {IE : /msie/.test(userAgent),FF :/gecko/.test(userAgent),OP:/opera/.test(userAgent)};
		for(var prop in browser){if(browser[prop]){this.browser = prop;break;}}		
		if(!this.browser)this.browser = "IE";
	}
};

var $import = function(str){ajit.loadpkg(str);};
var $include = function(str){ajit.include(str);};

var $cache ={
	elements : {},
	put : function(key,val){
		if(this.get(key)) return;
		this.elements[key] = val;
	},
	get : function(key){
		return this.elements[key];
	},
	remove : function(key){
		this.elements[key] = null;
	},
	clear : function(){
		this.elements = null;
	}
};

var JsClass = function(members){	
	var Class = function(){
		if (this.main) this.main.apply(this, arguments);
	};	
	Class.prototype = members;	
	return Class;	
};

var setPrototype = function(el,properties){	
		for (prop in properties) {
			try{el.prototype[prop] = el.prototype[prop] ? el.prototype[prop] : properties[prop];}catch(e){}
		}		
	return el;
};

var setProperty = function(el,properties){
	for(prop in properties){
		el[prop] = properties[prop];
	}	
	return el;
}

var $E = function(el){
	var element = null;	
	if(typeof el == "string"){
		if($cache.get(el)) return $cache.elements[el];
		element = document.getElementById(el);
	}else{
		if($cache.get(el)) return $cache.elements[el];
		element = el;
	}
	
	try{
		$cache.put(element.id, element);	
	}catch(e){}		
	
	try{
		setProperty(element,DisplayObject.prototype);	
	}catch(e){}	
	return element;	
};


var $swf = function(el){
	return ($client.browser == "IE") ? window[el] : document[el];
};

var StringHelper = {
	test: function(value, params){		
		return this.match(new RegExp(value, params));
	},
	capitalize: function(){
		return this.toLowerCase().replace(/\b[a-z]/g, function(match){
			return match.toUpperCase();
		});
	},
	camelCase: function(){
		return this.replace(/-\D/gi, function(match){
			return match.charAt(match.length - 1).toUpperCase();
		});
	},
	trim: function(){
		return this.replace(/^\s*|\s*$/g,'');
	},
	clean: function(){
		return this.replace(/\s\s/g, ' ').trim();
	},
	endsWith : function(c){
		if(this.charAt(this.length - 1) == c){
			return true;
		} else {
			return false;
		}
	},
	startsWith : function(c){
		if(this.charAt(0) == c){
			return true;
		} else {
			return false;
		}
	},
	replaceAll : function replaceAll(a, b){
		var k = this.replace(new RegExp(a, "gi"), b);
		return k;
	}
};

var ArrayHelper = {
	foreach : function(fn,thisObject){
		thisObject = thisObject || null;
		for (var i = 0; i < this.length; i++) fn.call(thisObject, this[i], i);
	},	
	test : function(item){
		for(var i =0,total = this.length ; i < total; i++){
			if(this[i] === item) return this[i];
		}
		return null;
	},	
	remove : function(item){
		for(var i =0,total = this.length ; i < total; i++){
			if (this[i] === item) this.splice(i, 1);
		}
		return this;
	},
	map: function(fn,thisObject){
		thisObject = thisObject || null;
		var results = [];
		for (var i = 0, j = this.length; i < j; i++) results[i] = fn.call(thisObject, this[i], i, this);
		return results;
	},
	every : function(fn,thisObject){
		thisObject = thisObject || null;
		for(var i =0,total = this.length ; i < total; i++){
			if (!fn.call(thisObject, this[i], i, this)) return false;
		}
		return true;
	},
	filter: function(fn, thisObject){
		thisObject = thisObject || null;
		var results = [];
		for(var i =0,total = this.length ; i < total; i++){
			if (fn.call(thisObject, this[i], i, this)) results.push(this[i]);
		}
		return results;
	}
};

var FunctionHelper = {
	bindThis : function(obj,args){
		var _this = this;
		var targetThis = obj;		
		args = args || [];
		var t= new Array();
		var fn = function(){
			args = args.concat(arguments);
			return _this.apply(targetThis,args);
		};
		return fn;
	}
}


var StringBuffer = JsClass({
	buffer : null,
	main : function(){
		this.buffer = new Array();
		return this;
	},
	append : function(str){
		this.buffer.push(str);
		return this;
	},
	toString : function(){
		return this.buffer.join("");
	}
}); 

var EventManager = {	
	addEvent : function(el,evtType,fn,args){
		el = $E(el);			
		
		function dispatchEventHandler(evt){			
			evt = evt || window.event;			
			args = args || [evt];
			if (!args[0].type) {args.unshift(evt);}else{args[0] = evt;}
			fn.apply(el,args);
		}
		
		if(el.addEventListener){
			el.addEventListener(evtType, dispatchEventHandler ,false);			
		}else{			
			el.attachEvent("on"+evtType, dispatchEventHandler);
		}
		return el;
	},
	removeEvent : function(el,evtType,fn){
		el = $E(el);
		
		function dispatchEventHandler(evt){			
			evt = evt || window.event;			
			args = args || [evt];
			if (!args[0].type) {args.unshift(evt);}else{args[0] = evt;}
			fn.apply(el,args);
		}
		
		if (el.removeEventListener) {
			el.removeEventListener(evtType, dispatchEventHandler, false);
		}else{
			el.detachEvent("on" + evtType , dispatchEventHandler);
		}
		return el;		
	}
};

var $new = function(el){
	el = document.createElement(el);
	$E(el);
	return el;	
}

var $each = function(obj,fn,thisObject){
	thisObject = thisObject || null;
	if(typeof obj == "array"){
		obj.foreach(fn);
	}else{
		for(var prop in obj) fn.call(thisObject, obj[prop], prop);
	}
}

var DisplayObject = JsClass({
	addChild : function(el){
		this.appendChild(el);
	},	
	addChildAt : function(idx,el){
		if(this.numChildren() > idx || idx == 0){
			this.insertBefore(this.childNodes[idx],el);
		}else if(this.numChildren() <= idx){			
			this.addChild(el);
		} else{
			false;
		}
	},
	remove : function(){
		try{
			this.parentNode.removeChild(this);
		}catch(e){}		
	},
	removeChildNode : function(el){
		this.removeChild(el);
	},
	removeChildAt : function(idx){
		try{
			this.removeChildNode(this.getChildAt(idx));
		}catch(e){}		
	},
	numChildren : function(){
		return this.childNodes.length;
	},
	getIndex : function(){
		for(var i = 0; i < $E(this.parentNode).numChildren() ; i++){
			if(this == this.parentNode.childNodes[i]) return i;
		}
		return false;		
	},
	getChildAt : function(idx){
		for(var i = 0; i < this.numChildren();i++){
			if(idx == i) return $E(this.childNodes[i]);
		}
		return false;
	},
	getChildByName : function(name){
		for(var i = 0; i < this.numChildren();i++){
			if (this.childNodes[i].name == name) {
				return $E(this.childNodes[i]);
				break;
			}
		}
		return false;
	},
	getChildById : function(id){
		for(var i = 0; i < this.numChildren();i++){
			if(this.childNodes[i].id == id){
				return $E(this.childNodes[i]);
				break;
			} 
		}
		return false;
	},
	getFirstChild : function(){
		return $E(this.childNodes[0]);
	},
	getLastChild : function(){
		return $E(this.childNodes[this.numChildren()-1]);
	},
	replaceChildNode : function(el){
		this.parentNode.replaceChild(el, this);
	},
	hasChild : function(){
		return this.numChildren() > 0 ? true : false;
	},
	addEvent : function(evtType,fn,agrs){
		EventManager.addEvent(this,evtType,fn,agrs)
	},
	removeEvent: function(evtType,fn){
		EventManager.removeEvent(this,evtType,fn);
	},
	getTag : function(){
		return this.tagName.toLowerCase();
	},
	getPosition : function(){
		var el = this;
		var left = 0;
		var top = 0;
		do {
			left += el.offsetLeft || 0;
			top += el.offsetTop || 0;
			el = el.offsetParent;
		} while (el);			
		return {'x': left, 'y': top};		
	},
	getOffset : function(where){
		where = where.capitalize();
		var el = this;
		var offset = 0;
		do {
			offset += el['offset'+where] || 0;
			el = el.offsetParent;
		} while (el);
		return offset;
	},
	setStyle : function(prop,value){		
		try {
			this.style[prop.camelCase()] = value;
		}catch (e) { return false;}		
	},
	getStyle: function(prop,num){
		try{
			return num ? parseInt(this.style[prop.camelCase()]) : this.style[prop.camelCase()];	
		}catch(e){
			return 0;
		}		
	},
	addClassName : function(className){
		if (!this.hasClassName(className)) this.className = (this.className+' '+className.trim()).clean();
		return this;
	},
	removeClassName : function(className){
		if (this.hasClassName(className)) this.className = this.className.replace(className.trim(), '').clean();
		return this;
	},
	hasClassName : function(className){
		return this.className.test("\\b"+className+"\\b");
	},
	setClassName : function(className){
		this.className = className;
	},
	appendText : function(text){
		this.appendChild(document.createTextNode(text));
	},
	setHTML : function(html){
		this.innerHTML = html;
	},
	getHTML : function(){
		return this.innerHTML;
	}
});

var URLLoaderEvent = {
	OPEN : "open",
	LOAD:"load",
	LOADED:"loaded",
	READ_RESPONSE : "readResponse",
	COMPLETE : "complete",
	STATUS : "status",
	IOERROR: "ioError",
	SECURITY_ERROR : "securityError"
};

var URLLoader = JsClass({	
	req : "",
	data : "",
	async : true,
	main : function(request){
		this.data = "";
		this.async = true;
		this.req = request || null;
	},
	load : function(request){
		request = request || this.req;
		this.req = request;
		
		this.req.httpRequest.open(this.req.method.toUpperCase(), this.req.url, this.async);
		
		if(this.setRequest(this.req)){			
			this.req.httpRequest.onreadystatechange = this.onHttpStatus.bindThis(this);
			try{
				this.req.httpRequest.send(this.data || null);
			}catch(e){
				this.req.httpRequest.send(null);
			}
		}
	},
	addEvent : function(evtType,fn){
		var validEventType = false;
		for(var prop in URLLoaderEvent)
			if(URLLoaderEvent[prop] === evtType) validEventType = true;		
		
		if(!validEventType)	
			throw new Error("ajit Error : " + evtType + " is not available event type");
		
		if(this[("on-"+evtType).camelCase()]){
			this[("on-"+evtType).camelCase()] = fn;			
		}		
	},
	removeEvent : function(evtType){
		var validType = [];
		var event = "on-"+evtType;
		if(this[event.camelCase()]){
			this[event.camelCase()] = function(){};
		}
	},
	setRequest : function(){
		this.req.requestHeaders.push(new URLRequestHeader("Content-type",this.req.contentType));
		if (this.req.method.toLowerCase() == 'post'){
			if(this.req.httpRequest.overrideMimeType){
					this.req.requestHeaders.push(new URLRequestHeader('Connection', 'close'));
			}

			this.tmp = [];

			if(typeof this.req.data == "object"){
				$each(this.req.data,function(val,prop){
					this.tmp.push(encodeURIComponent(prop)+"="+encodeURIComponent(val));
				},this);
				this.data = this.tmp.join("&");		
			}
			this.data = this.data || this.req.data;			
		}
		
		for (var i=0;i < this.req.requestHeaders.length;i++){			
			try {
				this.req.httpRequest.setRequestHeader(this.req.requestHeaders[i].name,this.req.requestHeaders[i].value);
			} catch(e){				
			};
		}
		return true;
	},	
	onHttpStatus : function(){
		switch(this.req.httpRequest.readyState){
			case 0 : 
				//"uninitialized"
				this.onOpen();
			break;
			case 1 : 
				//"loading"
				this.onLoad();
			break;
			case 2 : 
				//"loaded"
				this.onLoaded();
			break;
			case 3 : 
				//"interactive"
				this.onReadResponse();
			break;
			case 4 : 
				var response = {
					text : this.req.httpRequest.responseText,
					xml : this.req.httpRequest.responseXML
				};
				this.validateRequestStatus(this.req.httpRequest.status) ? this.onComplete(response): this.onIoError(response);
			break;
			default :
				throw new Error("ajit Error: Could not catch reponse");
			break;			
		}		
	},
	validateRequestStatus : function(status){
		this.onStatus(status);
		if(status == 401){
			
		}
		return ((status >= 200) && (status < 300));
	},	
	onOpen : function(){},
	onLoad : function(){},
	onLoaded : function(){},
	onReadResponse : function(){},
	onComplete : function(){},	
	onIoError : function(){},
	onStatus : function(status){},
	onSecurityError : function(){}
});

var URLRequest = JsClass({
	type : "URLRequest",
	httpRequest : "",
	charset : "UTF-8",
	contentType: "",		
	url : "",
	requestHeaders : [],
	data : {},
	method : "POST",
	main : function(_url){
		this.charset = "UTF-8";
		this.requestHeaders = [];
		this.data = {};
		this.url = _url;
		this.method = "POST";
		this.contentType = "application/x-www-form-urlencoded; charset="+this.charset;
		this.httpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() 
			: ($client.browser == "IE") ? new ActiveXObject('Microsoft.XMLHTTP') : false;		
		
		if(!this.httpRequest){
			throw new Error("ajit Error : Not found xmlHttpRequest");
		}
	}
});

var URLRequestHeader = JsClass({
	type : "URLRequestHeader",
	name : "",
	value : "",
	main : function(_name,_value){
		this.name = _name;
		this.value = _value;
	}
});

var URLVariables = JsClass({
	main : function(params){
		this.parameters = params || {};
	},
	toString : function(){
		var tmp = [];
		$each(this.parameters,function(val,prop){
			tmp.push(encodeURIComponent(prop)+"="+encodeURIComponent(val));
		},this);
		return (tmp.join("&"));
	}
});

var Tween = JsClass({
	main : function(){
		
	}
});

var Stage = {
	width : function(){
		return window.innerWidth || document.documentElement.clientWidth || 0;
	},
	height : function(){
		return window.innerHeight || document.documentElement.clientHeight || 0;
	},
	scrollHeight : function(){
		return document.documentElement.scrollHeight;
	},
	scrollWidth : function(){
		return document.documentElement.scrollWidth;
	},
	scrollTop : function(){
		return document.documentElement.scrollTop || window.pageYOffset || 0;
	},
	scrollLeft : function(){
		return document.documentElement.scrollLeft || window.pageXOffset || 0;
	},
	creationDomComplete : function(){
		//onDomReady
	},
	applicationComplete : function(){
		//onLoad
	}		
};


$client.initialize();
setPrototype(String,StringHelper);
setPrototype(Array,ArrayHelper); 
setPrototype(Function,FunctionHelper);


var debug = {
	log : function(){
		
	}
}