$import("asjs.utils.XmlParser");
var DaumImageService = {
	DAUM_API_KEY : "547946a501a615fc12162cacdc3ee3e1ba689d27",
	DAUM_API_URL : "http://apis.daum.net/search/image?",	
	search : function(q){
		//URL로 Request를 보내는 객체 선언
		var loader = new URLLoader();
		//Request 객체 선언
		var req = new URLRequest();
		//URL Parameter 객체 선언
		var params = new URLVariables();		
		
		//파라미터 값을 Object로 정의
		params.parameters = {
			"apikey" : DaumImageService.DAUM_API_KEY,
			"q" : q,
			"start" : "1",
			"result" : "16",
			"sort" : "0",
			"output" : "xml"
		};

		//req.url = NAVER_API_URL + params.toString();
		req.url = DaumImageService.DAUM_API_URL + 
			params.toString();

		//Request 보내기 
		loader.load(req);
		//Load완료시 실행할 이벤트 정의
		loader.addEvent(URLLoaderEvent.onComplete
			,DaumImageService.onSearchCompleteHandler);
	},

	//Load완료 이벤트 핸들러로 Response를 인자로 받는다.
	onSearchCompleteHandler : function(response){
		//XML을 Javascript 객체로 파싱
		var xml = new asjs.utils.XmlParser(response.xml).toJson();
		if(xml.channel.item){
			//RSS결과로 이미지 목록 만들기
			DaumImageService.renderImage(xml.channel.item);
		}
	},	
	//검색 결과 이미지 배치
	renderImage : function(items){ 
		$E("resultList").setHTML("");

		for(var i=0; i < items.length; i++){
			//IMG 객체 생성
			var img = $new("img");
			//LI 객체 생성
			var li = $new("li");
			//$E = document.getElementById
			$E("resultList").addChild(li);
			li.addChild(img);
			//img 객체에 src속성 정의
			img.setAttribute("src",items[i].thumbnail.text);	
			
			//추가 
			img.imagePath = items[i].image.text;
			img.addEvent("click", function(){
				insertImage(this.imagePath);
			});
		}
	}
}