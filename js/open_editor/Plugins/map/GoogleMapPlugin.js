var GoogleMapPlugin = {
    info : "구글 지도 플러그인",
	group : "B",
    pop : "",
    selectedEditor : "",
    getInstance : function(){
        var obj = $new("IMG");
        obj.setAttribute("src",OPEN_EDITOR_SKIN+"mapBtn.gif");
        obj.setStyle('cursor',"pointer");
        obj.setStyle("margin-right","3px");
        return obj;
    },
    insertMap : function(str){
        //차트를 에디터에 삽입한다.
        GoogleMapPlugin.selectedEditor.insertTag(str);
        
        //생성기를 종료한다.
        GoogleMapPlugin.hide();
    },
    eventListener : function(evt,obj,oEditor){

        //버튼 클릭시 라인 차트 생성기를 팝업으로 띄움
        GoogleMapPlugin.pop = window.open(OPEN_EDITOR_PATH + "/Plugins/map/GoogleMapPlugin.html"
            ,"GoogleMapPlugin","width=780,height=480,scrollbars=yes");
        GoogleMapPlugin.selectedEditor = oEditor;                
    },
    hide : function(){
        //띄워진 라인 차트 생성기 팝업 있다면
        if(GoogleMapPlugin.pop){
            
            //띄워진 팝업을 닫는다.
            GoogleMapPlugin.pop.close();
            
            //값을 초기화 한다.
            GoogleMapPlugin.pop = null;
        }
    }
}