asjs.utils.XMLRPC = JsClass({
	method : "",
	params:"",
	main : function(){
		this.metho = "";
		this.params =  new StringBuffer();
	},
	addParam : function(data,type){		
		 this.params.append("<param>").append("<value>" + this.toXMLNode(data,type) + "</value>").append("</param>");	
		return true;
	},
	getType : function(obj){
		var type = typeof(obj);
		type = type.toLowerCase();

		switch(type){
			case "number":
			  if (Math.round(obj) == obj) type = "i4";
			  else type = "double";
			  break;
			case "object":
			  if (obj.constructor == Date) type = "date";
			  else if (obj.constructor == Array) type = "array";
			  else type = "struct";
			  break;
			case "function" :
				type = "";
			break;
		}
		return type;		
	},
	toXMLNode : function(data,type){
		try{
			if(type){
				return this["to"+(type)+"Node"](data);
			}else{
				return this["to"+this.getType(data)+"Node"](data);
			}			
		}catch(e){}
	},
	todoubleNode : function(data){
		return this.tovalueNode("double",data);
	},
	toi4Node : function(data){
		return this.tovalueNode("i4",data);
	},
	tobase64Node : function(data){
		return this.tovalueNode("base64",data);
	},
	tovalueNode : function(type,data){
		return "<" + type + ">" + data + "</" + type + ">";
	},
	tostringNode : function(data){
		return this.tovalueNode("string",data);
	},
	tobooleanNode : function(data){
		return this.getValueNode("boolean", data ? true : false);
	},
	todateNode : function(data){
		data = this.dateToISO8601(data);
  		return this.getValueNode("dateTime.iso8601",data);
	},
	toarrayNode : function(data){
		var sb = new StringBuffer();
		sb.append("<array><data>");
		for (var i = 0; i < data.length; i++){
			sb.append("<value>" + this.toXMLNode(data[i]) + "</value>");
		 }
		sb.append("</data></array>");		
		return sb.toString();
	},
	tostructNode : function(data){
		var sb = new StringBuffer();
		sb.append("<struct>");
		for (var i in data){
			sb.append("<member>")
			.append("<name>" + i + "</name>");
			
			if(data[i].type){
				sb.append("<value>" + this.toXMLNode(data[i].data,data[i].type) + "</value>");
			}else{				
				sb.append("<value>" + this.toXMLNode(data[i]) + "</value>");
			}
			sb.append("</member>");
		 }
		 sb.append("</struct>");
		 return sb.toString();
	},
	dateToISO8601 : function(date){
	  var yyyy = new String(date.getYear());
	  var mm = this.zerofill(new String(date.getMonth()));
	  var hh = this.zerofill(new String(date.getDate()));
	  var time = this.zerofill(new String(date.getHours())) 
		  + ":" + this.zerofill(new String(date.getMinutes()))
		  + ":" + this.zerofill(new String(date.getSeconds()));
	 return ( yyyy+mm+dd+"T"+time );
	},
	zerofill : function(str){
		if (str.length==1) str = "0" + str;
		return str;
	},
	getXML : function(){
		var sb = new StringBuffer();
		
		sb.append('<?xml version="1.0"?>')
			.append("<methodCall>")
			.append("<methodName>" + this.method+ "</methodName>")
			.append("<params>")
			.append(this.params.toString())
			.append("</params>")
			.append("</methodCall>");

		return sb.toString();
	}
});
/*
var xmlrpc = new XMLRPC();
xmlrpc.addParam(["test",122.11],"base64");
alert(xmlrpc.getXML());
*/


