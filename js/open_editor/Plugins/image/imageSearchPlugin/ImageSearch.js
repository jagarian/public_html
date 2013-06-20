var ImageSearch = {
	info : "이미지 통합 검색 플러그인",
	main : function(oEditor){
		//생성자				
	},
	getInstance : function(){
		//$new -> document.createElement
		var btn = $new("img");
		btn.setAttribute("src",OPEN_EDITOR_SKIN+"imageSearchBtn.gif");
		//스타일 정의
		btn.setStyle("margin-right","3px");
		btn.setStyle("cursor","pointer");
		return btn;
	},
	eventListener : function(evt,obj,oEditor){
		//에디터 메뉴 객체가 클릭되었을 때 이벤트 정의
		//이미지 검색기 팝업 윈도우 띄우기
		ImageSearch.editor = oEditor;
		var src = OPEN_EDITOR_PATH + "/Plugins/image/imageSearchPlugin/ImageSearchPlugin.html";		
		parentSandboxBridge.openWindow(src,341,413);
	},
	hide : function(){
		
	}
};		