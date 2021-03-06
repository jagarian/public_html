var LineChartPlugin = {
    info : "라인 차트 플러그인",
    group:"B",
    pop : "",
    selectedEditor : "",
    getInstance : function(){
        var obj = $new("IMG");
        obj.setAttribute("src",OPEN_EDITOR_SKIN+"lineChartBtn.gif");
        obj.setStyle('cursor',"pointer");
        obj.setStyle("margin-right","3px");
        return obj;
    },
    insertChart : function(src){
        //차트를 에디터에 삽입한다.
        LineChartPlugin.selectedEditor.insertImage(src);
        
        //생성기를 종료한다.
        LineChartPlugin.hide();
    },
    eventListener : function(evt,obj,oEditor){
		
        //버튼 클릭시 라인 차트 생성기를 팝업으로 띄움
        LineChartPlugin.pop = window.open(OPEN_EDITOR_PATH + "/Plugins/chart/lineChartGeneratorPluigin.html"
            ,"chart","width=345,height=500,scrollbars=yes");
        LineChartPlugin.selectedEditor = oEditor;                
    },
    hide : function(){
        //띄워진 라인 차트 생성기 팝업 있다면
        if(LineChartPlugin.pop){
            
            //띄워진 팝업을 닫는다.
            LineChartPlugin.pop.close();
            
            //값을 초기화 한다.
            LineChartPlugin.pop = null;
        }
    }
}