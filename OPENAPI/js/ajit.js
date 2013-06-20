/*
 * AJIT는 A Javascript Integration Tool의 약자로 자바스크립트의 구조를 잡아주고 통합을 도와주는 프레임워크입니다.
 * Copyright (c) 2008 lovedev(oh.changhun) Resig (asjs.net)
 * 
 * 소스와 바이너리 형태의 재배포 및 사용은 수정 여부에 관계없이 다음 조건을 충족할 때 가능합니다:
 * 1. 소스 코드의 재배포는 위의 저작권 문구와 지금 보이는 조건, 아래의 면책 조항을 반드시 유지해야 합니다.
 * 2. 바이너리 형태의 재배포는 배포판과 함께 제공되는 문서 또는 그외 형태의 매체에 위의 저작권 문구와 지금 보이는 조건, 아래의 면책 조항을 반드시 명시해야 합니다.
 * 3. 이 소프트웨어의 기능 또는 그 사용을 언급하는 모든 광고 매체는 다음과 같은 감사 문구를 반드시 표시해야 합니다:        
 *  이 제품은 lovedev(asjs.net)과 공헌자들이 개발한 소프트웨어를 포함하고 있습니다.
 * 4. 사전 서면 승인 없이는 저자의 이름이나 공헌자들의 이름을 이 소프트웨어로부터 파생된 제품을 보증하거나 홍보할 목적으로 사용할 수 없습니다.
 * 이 소프트웨어는 저자와 공헌자들에 의해 "있는 그대로" 제공될 뿐이며, 상품성이나 특정 목적에 대한 적합성의 묵시적 보증을 포함하여(단, 이에 국한되지 않음), 
 * 어떠한 형태의 보증도 하지 않습니다. 어떠한 경우에도 재단과 공헌자들은 제품이나 서비스의 대체 조달, 또는 데이터, 이윤, 
 * 사용상의 손해, 업무의 중단 등을 포함하여(단, 이에 국한되지 않음), 이 소프트웨어를 사용함으로써 발생한 직접적이거나, 간접적, 또는 우연적이거나,
 * 특수하거나, 전형적이거나, 결과적인 피해에 대해, 계약에 의한 것이든, 엄밀한 책임, 불법 행위(또는 과실과 기타 행위를 포함하여)에 의한 것이든, 
 * 이와 여타 책임 소재에 상관없이, 또한 그러한 손해의 가능성이 예견되어 있었다 하더라도, 전혀 책임을 지지 않습니다
 * 
 * AJIT(A javascript integration tool) ver 0.9
 *
 * Copyright (c) 2008 lovedev(oh.changhun) Resig (asjs.net)
 * BSD (BSD-LICENSE.txt) licenses.
 *
 * Date: 2008-11-04  
 */
if (!ajit) {
	var ajit = {
		//현재 버전
		VERSION: "0.1",
		//AJIT가 기반으로 할 서버 도메인
		SERVER: "",
		//테스트모드로 접근되는 도메인 설정
		TEST_DOMAINS : "",
		//테스트모드로 접근되는 파라미터 설정
		TEST_PARAM : "doTest",
		//서버 도메인을 기반으로한 패키지 기본 경로
		PKG_ROOT: "./js/",
		//자바스크립트 기본 인코딩타입
		DEFAULT_CHARSET : "utf-8",
		//최초 로딩될 자바스크립트 라이브러리 정의
		DEFAULT_JS_LIB : [
				"lib/ajit_lib.js",
				"lib/swfobject.js"
		],
		__init: function(){
			if (this.loaded) return;
			this.loaded = true;
			this.test = false;
			this.included = {};
			this.onLoadFn = [];

			if (this.getParameter(this.TEST_PARAM) == "on" && this.TEST_DOMAINS.indexOf(document.domain) > -1) {
				this.test = true;
				this.include(this.PKG_ROOT + "test/init.js");
			}
			else {
				for(var i =0;i<this.DEFAULT_JS_LIB.length;i++) this.include(this.PKG_ROOT + this.DEFAULT_JS_LIB[i]);
			}
		},
		include: function(src,charset){
			if (this.included[src])  return;
			this.included[src] = "included";
			charset = charset || this.DEFAULT_CHARSET;

			src = (src.indexOf(this.SERVER) > -1) ? src : this.SERVER + src;
			if (src.indexOf("absolute:") > -1) src = src.replace("absolute:", "");
			document.write(unescape("%3Cscript  src='"+src+"?v=" + this.VERSION+"' charset='"+charset+"' type='text/javascript'%3E%3C/script%3E"));
		},
		loadpkg: function(){
			for (var i = 0; i < arguments.length; i++) {
				if (this.__packageChk(null, arguments[i])) {
					if (this.test) this.include(this.PKG_ROOT + "test_" + arguments[i].split(".").join("/") + ".js");					
					else this.include(this.PKG_ROOT + arguments[i].split(".").join("/") + ".js");
				}
			}
		},
		__packageChk: function(obj, path, idx){
			idx = idx || 1;
			var pathArr = path.split(".");
			if(!window[path.split(".")[0]]) window[path.split(".")[0]] = {};
			obj = obj || window[path.split(".")[0]];		
			
			for (var i = idx; i < pathArr.length; i++) {
				if (!obj[pathArr[i]]) obj[pathArr[i]] = {};
				obj = obj[pathArr[i]];
			}
			return true;
		},
		getParameter: function(param){
			try {
				var querySplit = document.location.href.split('?');
				if (querySplit.length > 1) {
					var reqParam = querySplit[1].split('&');
					for (var i = 0, rsLen = reqParam.length; i < rsLen; i++) {
						var keyval = reqParam[i].split('=');
						if (param == keyval[0]) return keyval[1];
					}
				}
				else {
					return false;
				}
			}catch (e) {return false;}
			return false;
		},		
		onComplete: function(fn){
			this.onLoadFn.push(fn);
			if (this.onLoadFn.length == 1) window.onload = function(){ajit.__onCompleteExec();};
		},
		__onCompleteExec: function(){
			if (this.onLoadFn.length > 0) {				
				for (var i = 0; i < this.onLoadFn.length; i++) this.onLoadFn[i].call();
			}
		}
	};ajit.__init();
}