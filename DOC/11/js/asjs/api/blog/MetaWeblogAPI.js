$import("asjs.utils.XMLRPC");


asjs.api.blog.MetaWeblogAPI = {
    blog_api_url : "",
    blog_url : "",
    blogType : "tistory",
	blog_apikey : "",
    blog_id : "",
    blog_userid : "",
    blog_password : "",
    META_WEBLOG_APIS : {
        "GET_BLOG_INFO" : "blogger.getUsersBlogs",
        "WRITE_POST_DATA" : "metaWeblog.newPost",
        "WRITE_MULTIMEDIA_DATA" : "metaWeblog.newMediaObject",
        "GET_CATEGOIES" : "metaWeblog.getCategories",
        "GET_RECENT_POST" : "metaWeblog.getRecentPosts",
        "GET_POST" : "metaWeblog.getPost",
        "UPDATE_POST" : "metaWeblog.editPost"
    },
    login : function (blogUrl,apikey,userid,password,callback){
		
		//URL에서 HOST에 관련된 부분만 추출한다.
		var patt = /(http(s)?:\/\/)?\w+(\.\w+)+/gi;
  		this.blog_url = (blogUrl.match(patt))[0];
		
        if(blogUrl.indexOf("wordpress.com") > -1){
            this.blog_api_url = blogUrl+"/xmlrpc.php";            
            this.blogType = "wordpress";
        }else if(blogUrl.indexOf("tistory.com") > -1){
            this.blog_api_url = blogUrl+"/api";            
            this.blogType = "tistory";
        }else{
			this.blog_api_url = blogUrl;            
            this.blogType = "etc";
        }
        this.blog_apikey = apikey || this.blog_apikey;
        this.blog_userid = userid;
        this.blog_password = password;
        var xmlrpc = this.getXMLRPC(this.META_WEBLOG_APIS.GET_BLOG_INFO);
           this.send(xmlrpc, callback);        
    },
    writePost : function(struct,publishFlag,callback){    
        if(!this.blog_id) return false;        
        var xmlrpc = this.getXMLRPC(this.META_WEBLOG_APIS.WRITE_POST_DATA);
        xmlrpc.addParam(struct);
        xmlrpc.addParam(publishFlag);        
        this.send(xmlrpc,callback);        
    },
    writeFile : function(struct,callback) {
        if(!this.blog_id) return false;        
        var xmlrpc = this.getXMLRPC(this.META_WEBLOG_APIS.WRITE_MULTIMEDIA_DATA);
        xmlrpc.addParam(struct);
        this.send(xmlrpc,callback);
    },
    updatePost : function(postid,struct,publishFlag,callback){
        if(!this.blog_id) return false;
        
        var xmlrpc = this.getXMLRPC(this.META_WEBLOG_APIS.UPDATE_POST,postid);
                
        xmlrpc.addParam(struct);
        xmlrpc.addParam(publishFlag);        
        this.send(xmlrpc,callback);
    },
    getPost : function(postid,callback){
        if(!this.blog_id) return false;
        
        var xmlrpc = this.getXMLRPC(this.META_WEBLOG_APIS.GET_POST,postid);
        this.send(xmlrpc,callback);
    },
    getRecentlyPosts : function(callback){        
        if(!this.blog_id) return false;
        var xmlrpc = this.getXMLRPC(this.META_WEBLOG_APIS.GET_RECENT_POST);
        this.send(xmlrpc,callback);
    },
    getCategories : function(callback){
        if(!this.blog_id) return false;
        var xmlrpc = this.getXMLRPC(this.META_WEBLOG_APIS.GET_CATEGOIES);
        this.send(xmlrpc,callback);
    },
    getXMLRPC : function(method,postid){
        var xmlrpc = new asjs.utils.XMLRPC();
           xmlrpc.method = method;
           if(method == this.META_WEBLOG_APIS.GET_BLOG_INFO){
		   	   xmlrpc.addParam(this.blog_apikey);
		   }else{
	           xmlrpc.addParam(postid || this.blog_id);		   	
		   }
           xmlrpc.addParam(this.blog_userid);
           xmlrpc.addParam(this.blog_password);
        return xmlrpc;
    },
    send : function(xmlrpc,callback){
        if(!this.blog_api_url) return;
        var loader = new URLLoader();
        var req = new URLRequest();
        req.contentType = "text/xml; charset=UTF-8";
        req.data = xmlrpc.getXML();
        req.url = this.blog_api_url;
        loader.addEvent(URLLoaderEvent.COMPLETE, callback);
        loader.load(req);
    }
}
