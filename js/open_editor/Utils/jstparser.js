var CharacterStack = JsClass({
	characters : [],
	main : function(str){
		this.characters = [];
		for(var i = str.length; i >= 0; i--){
			this.characters.push(str.charAt(i));			
		}		
	},
	peek : function peek(){
		return this.characters[this.characters.length - 1]; 
	},
	pop : function pop() { 
		return this.characters.pop();
	},
	push : function push(c){ 
		this.characters.push(c);
	},
	hasMore :  function hasMore() {
		if(this.characters.length > 0) {
			return true; 
		}else{
			return false; 
		} 
	}
});

var Jst = {
	delimiter : "?",
	parseScriptlet : function (stack){		
		var sb = new StringBuffer();
		while(stack.hasMore()){
		if(stack.peek() == this.delimiter){ //possible end delimiter
			var c = stack.pop();
			if(stack.peek() == '>'){ //end delimiter
            // pop > so that it is not available to main parse loop
            stack.pop();
            if(stack.peek() == '\n'){
              sb.append(stack.pop());
            }
            break;
          } else {
            sb.append(c);
          }
        } else {
          sb.append(stack.pop());
        }
      }	  
      return sb.toString();
    },    
	isOpeningDelimiter : function(c){
      if(c == "<" || c == (this.delimiter+"lt;")){
        return true;
      } else {
        return false;
      }
    },    
	isClosingDelimiter : function (c){
      if(c == ">" || c == (this.delimiter+"gt;")){
        return true;
      } else {
        return false;
      }
    },    
	appendExpressionFragment : function(sb, fragment){		
      // check to be sure quotes are on both ends of a string literal	 
      if(fragment.startsWith("\"") && !fragment.endsWith("\"")){
	  	
        //some scriptlets end with \n, especially if the script ends the file
        if(fragment.endsWith("\n") && fragment.charAt(fragment.length - 2) == '"'){
          //we're ok...		 
        } else {
			
          throw { "message":"'" + fragment + "' is not properly quoted"};
        }
      }	 
      if(!fragment.startsWith("\"") && fragment.endsWith("\"")){
        throw { "message":"'" + fragment + "' is not properly quoted"};
      }

      if(fragment.endsWith("\n")){
        sb.append("Jst.writeln(");
        //strip the newline
        fragment = fragment.substring(0, fragment.length - 1);
      } else {
        sb.append("Jst.write(");
      }
	 
      if(fragment.startsWith("\"") && fragment.endsWith("\"")){
        //strip the quotes
        fragment = fragment.substring(1, fragment.length - 1);
        sb.append("\"");
        for(var i = 0; i < fragment.length; i = i + 1){
          var c = fragment.charAt(i);
          if(c == '"'){
            sb.append("\\");
            sb.append(c);
          }
        }
        sb.append("\"");
      } else {
        for(var j = 0; j < fragment.length; j = j + 1){
          sb.append(fragment.charAt(j));
        }
      } 
      sb.append(");");	 
	},    
	appendTextFragment : function (sb, fragment){
      if(fragment.endsWith("\n")){
        sb.append("Jst.writeln(\"");
      } else {
        sb.append("Jst.write(\"");
      }

      for(var i = 0; i < fragment.length; ++i){
        var c = fragment.charAt(i);
        if(c == '"'){
          sb.append("\\");
        }
        // we took care of the line break with print vs. println
        if(c != '\n' && c != '\r'){
          sb.append(c);
        }
      }
      sb.append("\");");
    },    
	parseExpression : function (stack){
      var sb = new StringBuffer();
      while(stack.hasMore()){
        if(stack.peek() == this.delimiter){ //possible end delimiter
          var c = stack.pop();
          if(this.isClosingDelimiter(stack.peek())){ //end delimiter
            //pop > so that it is not available to main parse loop
            stack.pop();
            if(stack.peek() == '\n'){
              sb.append(stack.pop());
            }
            break;
          } else {
            sb.append(this.delimiter);
          }
        } else {
          sb.append(stack.pop());
        }
      }
      return sb.toString();
    },    
	parseText : function (stack){
      var sb = new StringBuffer();
      while(stack.hasMore()){
        if(this.isOpeningDelimiter(stack.peek())){ //possible delimiter
          var c = stack.pop();
          if(stack.peek() == this.delimiter){ // delimiter!
            // push c onto the stack to be used in main parse loop
            stack.push(c);
            break;
          } else {
            sb.append(c);
          }
        } else {
          var d = stack.pop();
          sb.append(d);
          if(d == '\n'){ //done with this fragment.  println it.
            break;
          }
        }
      }
      return sb.toString();
    },    
	parse : function(src){
      src = src.replaceAll("&lt;", "<");
      src = src.replaceAll("&gt;", ">");
      var stack = new CharacterStack(src);
      var sb = new StringBuffer();
      var c;
      var fragment;

      while(stack.hasMore()){	  	
        if(this.isOpeningDelimiter(stack.peek())){ //possible delimiter
          c = stack.pop();
          if(stack.peek() == this.delimiter){ //delimiter!
            c = stack.pop();			
            if(stack.peek() == "="){
              stack.pop();
              fragment = this.parseExpression(stack);
              this.appendExpressionFragment(sb, fragment);			 
            } else {				
              fragment = this.parseScriptlet(stack);
              sb.append(fragment);
            }
          } else {  //not a delimiter          	
            stack.push(c);
            fragment = this.parseText(stack);
            this.appendTextFragment(sb, fragment);
          }
        } else {
          fragment = this.parseText(stack);
          this.appendTextFragment(sb, fragment);		  
        }
        
      }	  
      return sb.toString();
	},
	write : function(s){		
		  this.html.append(s);
	},
	writeln : function(s){
      this.write(s + "\n");
	},
	loadTemplateHTML : function(src,fn,agrs){
		new Ajax(src,{
			method : "get",
			onComplete : function(text,xml){
				try{
					fn(Jst.load(text,agrs));
				}catch(e){
					debug.log("templateHTML load fail");
				}
			},
			evalScripts : true
		}).request();
	},
	loadTemplate : function(src,dest,agrs){
		new Ajax(src,{
			method : "get",
			onComplete : function(text){
				try{					
					$(dest).setHTML(Jst.load(text,agrs));
				}catch(e){
					debug.log("template load fail" + e);
				}
			},
			evalScripts : true
		}).request();
	},
	load : function(template,args){
		try{
			with(args){
				this.html = new StringBuffer();					
				var script = this.parse(template);
				try{					
					eval(script);
				}catch(e){
					throw new Error("Jst parse Error");
				}				
				return this.html.toString();
			}
		}catch(e){			
		}
		
	},
	generate : function (template, dest, args){		
		$(dest).setHTML(this.load(template,args));
	},
	html:""
};