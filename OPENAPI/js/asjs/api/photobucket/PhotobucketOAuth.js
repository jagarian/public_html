$include(ajit.SERVER + ajit.PKG_ROOT + "lib/sha1.js");

asjs.api.photobucket.PhotobucketOAuth = 
{
	SIGNATURE_METHOD : "HMAC-SHA1",
	NONCE : "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz",
	setKey : function(key){
		this.key = key+"&";
	},
	getTimestamp : function(){
		return Math.floor(new Date().getTime()/1000);
	},
	getNonce : function(len){
		var tmp = [];
		do{
			tmp.push(this.NONCE.charAt(
				Math.floor(Math.random()*this.NONCE.length)));
			len--;
		}while(len>0);
		return tmp.join("");
	},
	encode : function(str){
		str = encodeURIComponent(str);
		str = str.replace("!", "%21", "g")
			.replace("*", "%2A", "g")
			.replace("'", "%27", "g")
			.replace("(", "%28", "g")
			.replace(")", "%29", "g");
		return str;
	},	
	getParameterString : function(){
		var params = this.getParameterArray();
		var str = [];
		for(var i=0;i < params.length; i++){
			str.push(params[i].join("="));
		}
		return str.join("&");
	},
	getParameterArray : function(){
		var params = [];
		for(p in this.request.parameters){
			params.push([this.encode(p), this.encode(this.request.parameters[p])]);
		}
		params = params.sort(this.customSort);
		return params;
	},
	getBaseString : function(){
		var baseStrArray = [
			this.encode(this.request.method.toUpperCase()),
			this.encode(this.request.url),
			this.encode(this.getParameterString())
			];
		return baseStrArray.join("&");
	},
	customSort : function(a,b){
		if (a[0] < b[0])return -1;
		if (a[0] > b[0]) return 1;
		if(a[1] < b[1]) return -1;
		if (a[1] > b[1]) return 1;
		return 0;
	},	
	createOAuth : function(){
		var oauth = [];

		this.request.parameters.oauth_timestamp = this.getTimestamp();
		this.request.parameters.oauth_nonce = this.getNonce(6);
		this.request.parameters.oauth_signature_method = this.SIGNATURE_METHOD;
		var signature = this.signature();		
		this.request.parameters.oauth_signature = signature;

		for(var prop in this.request.parameters){
			oauth.push(prop +"="+ this.request.parameters[prop]);
		}
		return oauth.join("&");
	},
	signature : function(){
		b64pad = '=';
		var signature = b64_hmac_sha1(this.key, this.getBaseString());
		return signature;
	},
	getSignedParameter : function(req){
		this.request = req;
		var res = this.createOAuth();
		return res;
	},
	getSignedUrl : function(req){
		this.request = req;
		var res = this.createOAuth();
		res = this.request.url + "?" + res;
		return res;
	}
};