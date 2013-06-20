$import("asjs.utils.XmlParser");
var FlickrImageService = {
	FLICKR_API_KEY : "3992e476d5472dd15708d7989b7364ef",
	FLICKR_API_URL : "http://api.flickr.com/services/rest/?",
	search : function(q){
		//URL로 Request를 보내는 객체 선언
		var loader = new URLLoader();
		//Request 객체 선언
		var req = new URLRequest();
		//URL Parameter 객체 선언
		var params = new URLVariables();		
		
		//파라미터 값을 Object로 정의
		params.parameters = {
			"api_key" : FlickrImageService.FLICKR_API_KEY,
			"tags" : q,
			"method" : "flickr.photos.search",
			"per_page" : "16"
		};

		//req.url = NAVER_API_URL + params.toString();
		req.url = FlickrImageService.FLICKR_API_URL + params.toString();
		//Request 보내기 
		loader.load(req);
		//Load완료시 실행할 이벤트 정의
		loader.addEvent(URLLoaderEvent.onComplete
			,FlickrImageService.onSearchCompleteHandler);
	},
	//Load완료 이벤트 핸들러로 Response를 인자로 받는다.
	onSearchCompleteHandler : function(response){
		
		var xml = new asjs.utils.XmlParser(response.xml).toJson();

		if(xml.rsp.photos.photo){
			//RSS결과로 이미지 목록 만들기
			FlickrImageService.renderImage(xml.rsp.photos.photo);
		}

	},	
	getImageSize : function(photoid,size){
		var loader = new URLLoader();
		var req = new URLRequest();
		var params = new URLVariables();		
		
		//파라미터 값을 Object로 정의
		params.parameters = {
			"api_key" : FlickrImageService.FLICKR_API_KEY,
			"photo_id" : photoid,
			"method" : "flickr.photos.getSizes"
		};

		//req.url = NAVER_API_URL + params.toString();
		req.url = FlickrImageService.FLICKR_API_URL + params.toString();
		//Request 보내기 
		loader.load(req);
		//Load완료시 실행할 이벤트 정의
		loader.addEvent(URLLoaderEvent.onComplete
			,FlickrImageService.onGetImageSizeHandler);
	},
	onGetImageSizeHandler : function(response){
		alert(response.text);
	},
	//검색 결과 이미지 배치
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
			
			var prefix_url = "http://farm" + items[i].attribute.farm 
				+ ".static.flickr.com/" + items[i].attribute.server + "/" 
				+ items[i].attribute.id+"_"+items[i].attribute.secret;

			img.setAttribute("src",prefix_url +"_m.jpg");

			img.imagePath = prefix_url +"_m.jpg";
			img.photo_id = items[i].$id;
			img.addEvent("click", function(){
				insertImage(this.imagePath);			
				//FlickrImageService.getImageSize(this.photo_id);
			});

		}
	}
}

