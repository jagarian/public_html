$import("asjs.utils.XmlParser");
var NaverImageService = {
	NAVER_API_KEY : "2a07fffb543cfd67c1755d2669a08037",
	NAVER_API_URL : "http://openapi.naver.com/search?",		
	search : function (q){
		//URL로 Request를 보내는 객체 선언
		var loader = new URLLoader();
		//Request 객체 선언
		var req = new URLRequest();
		//URL Parameter 객체 선언
		var params = new URLVariables();		
		
		//파라미터 값을 Object로 정의
		params.parameters = {
			"key" : NaverImageService.NAVER_API_KEY,
			"target" : "image",
			"query" : q,
			"display" : "16",
			"sort" : "simm",
			"filter" : "all"
		};

		req.url = NaverImageService.NAVER_API_URL 
				+ params.toString();

		//Request 보내기 
		loader.load(req);
		//Load완료시 실행할 이벤트 정의
		loader.addEvent(URLLoaderEvent.onComplete
			,NaverImageService.onSearchCompleteHandler);
	},
	//Load완료 이벤트 핸들러로 Response를 인자로 받는다.
	onSearchCompleteHandler : function (response){
		//XML을 Javascript 객체로 파싱
		var xml = new asjs.utils.XmlParser(response.xml).toJson();		
		if(xml.rss.channel.item){
			//RSS결과로 이미지 목록 만들기
			NaverImageService.renderImage(xml.rss.channel.item);
		}
	},
	renderImage : function (items){ 
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
			img.imagePath = items[i].link.text;
			img.addEvent("click", function(){
				insertImage(this.imagePath);
			});

		}
	}
}
