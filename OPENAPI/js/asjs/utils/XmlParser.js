asjs.utils.XmlParser = JsClass({
	main : function(xml){
		this.xml = xml;
	},
	toJson : function(xml, obj){
		var kProp, kName, kType, kValue;
		xml = xml || this.xml;
		obj = obj || new Object();
		
		try{
			if(xml.attributes.length > 0 && xml.attributes != null){
				obj["attribute"] = new Object();
				for(var i =0;  i < xml.attributes.length; i++){
					obj["attribute"][xml.attributes[i].name] = xml.attributes[i].value;
				}			
			}
			
		}catch(e){}

		for(var i=0; i < xml.childNodes.length; i++){				
			kName = xml.childNodes[i].nodeName;			
			kType = xml.childNodes[i].nodeType;
			kValue = xml.childNodes[i].nodeValue;
			
			
			if(kType == 3){
				obj["text"] = kValue;
			}

			if (kType == 1 && kName != null) {
				if (obj[kName] == null) {
					obj[kName] = this.toJson(xml.childNodes[i]);
				}else if(obj[kName]._type != 'array') {
					obj[kName] = [obj[kName]];
					obj[kName]._type = 'array';
				}

				if (obj[kName]._type == 'array') {
					obj[kName].unshift(this.toJson(xml.childNodes[i]));
				}
			}				
		}		
		return obj;
	},
	toXML : function(text){
		if(text){
			throw new Error("텍스트 문자열을 찾을 수가 없습니다");
		}
		var parser , xmlDom;

		if($client.browser == "IE"){
			xmlDom = new ActiveXObject("Microsoft.XMLDOM");
			xmlDom.async = false;
			xmlDom.loadXML(text);
		}else{
			parser = new DOMParser();
			xmlDom = parser.parserFromString(text,"text/xml");
		}
		return xmlDom;
	}
});