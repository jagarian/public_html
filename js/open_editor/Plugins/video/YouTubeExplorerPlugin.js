var YouTubeExplorerPlugin = {
    info : "유튜브 동영상 플러그인",
    pop : "",
    selectedEditor : "",
    getInstance : function(){
        var obj = $new("IMG");
        obj.setAttribute("src",OPEN_EDITOR_SKIN+"videoBtn.gif");
        obj.setStyle('cursor',"pointer");
        obj.setStyle("margin","0px 3px 0px 0px");
        return obj;
    },
    insertVideo : function(str){
        YouTubeExplorerPlugin.selectedEditor.insertTag(str);
        
        //생성기를 종료한다.
        YouTubeExplorerPlugin.hide();
    },
    eventListener : function(evt,obj,oEditor){
        //버튼 클릭시 동영상 검색기를 팝업으로 띄움
        YouTubeExplorerPlugin.pop = window.open(OPEN_EDITOR_PATH + "/Plugins/video/YouTubeExplorerPlugin.html"
            ,"YoutubePlugin","width=645,height=750,scrollbars=yes");
        YouTubeExplorerPlugin.selectedEditor = oEditor;                
    },
    hide : function(){
		//띄워진 팝업이 있다면					
		if(YouTubeExplorerPlugin.pop){
            
            //띄워진 팝업을 닫는다.
            YouTubeExplorerPlugin.pop.close();
            
            //값을 초기화 한다.
            YouTubeExplorerPlugin.pop = null;
        }
    }
}