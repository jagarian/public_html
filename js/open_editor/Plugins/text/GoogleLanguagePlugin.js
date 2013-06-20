var GoogleLanguagePlugin = {
    info : "구글 번역 플러그인",
    pop : "",
    selectedEditor : "",
    getInstance : function(){
        var obj = $new("IMG");
        obj.setAttribute("src",OPEN_EDITOR_SKIN+"languageBtn.gif");
        obj.setStyle('cursor',"pointer");
        obj.setStyle("margin","0px 3px 0px 0px");
        return obj;
    },
    insertLanguage : function(str){
        //번역된 문자열을 편집기에 삽입
        GoogleLanguagePlugin.selectedEditor.insertTag(str);
        
        //플러그인을 종료한다.
        GoogleLanguagePlugin.hide();
    },
    eventListener : function(evt,obj,oEditor){

        //버튼 클릭시 번역기를 팝업으로 띄움
        GoogleLanguagePlugin.pop = window.open(OPEN_EDITOR_PATH + "/Plugins/text/GoogleLanguagePlugin.html"
            ,"YoutubePlugin","width=640,height=380,scrollbars=yes");
        GoogleLanguagePlugin.selectedEditor = oEditor;                
    },
    hide : function(){
        //띄 팝업 있다면
        if(GoogleLanguagePlugin.pop){
            
            //띄워진 팝업을 닫는다.
            GoogleLanguagePlugin.pop.close();
            
            //값을 초기화 한다.
            GoogleLanguagePlugin.pop = null;
        }
    }
}