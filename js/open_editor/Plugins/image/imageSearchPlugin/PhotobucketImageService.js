$import("asjs.utils.XmlParser");
$import("asjs.api.photobucket.PhotobucketOAuth");

PhotobucketImageService = {
	OAUTH_CONSUMER_KEY :  "149826901",
	OAUTH_SECRET_KEY : "e4cdf9bc9a3bd7a10af657fa5a01be5e",
	PHOTOBUCKET_URL : "http://api.photobucket.com/search/",
	search : function(q){
		//URL로 Request를 보내는 객체 선언
		var loader = new URLLoader();
		//Request 객체 선언
		var req = new URLRequest();
		//URL Parameter 객체 선언		
		var OAuth = asjs.api.photobucket.PhotobucketOAuth;
		
		var url=PhotobucketImageService.PHOTOBUCKET_URL +	OAuth.encode(q);
		var method= "GET";
		var param = {
			"format" : "XML" ,
			"oauth_version" : "1.0",
			"oauth_consumer_key": PhotobucketImageService.OAUTH_CONSUMER_KEY,
			"perpage":"16"
			};

		OAuth.setKey(PhotobucketImageService.OAUTH_SECRET_KEY);
		var pbRequest = { "url" : url,"method" : method,"parameters" : param};	

		req.url = OAuth.getSignedUrl(pbRequest);
		req.method = method;
		
		//Request 보내기 
		loader.load(req);
		//Load완료시 실행할 이벤트 정의
		loader.addEvent(URLLoaderEvent.onComplete
			,PhotobucketImageService.onSearchCompleteHandler);
		loader.addEvent(URLLoaderEvent.onIoError
			,PhotobucketImageService.onFailHandler);
	},	
	onFailHandler : function(res){
		//document.location.reload();
	},
	//Load완료 이벤트 핸들러로 Response를 인자로 받는다.
	onSearchCompleteHandler : function(response){
		//XML을 Javascript 객체로 파싱		
		var xml = new asjs.utils.XmlParser(response.xml).toJson();

		if(xml.response.content.result.primary.media){
			//RSS결과로 이미지 목록 만들기
			PhotobucketImageService.renderImage(
				xml.response.content.result.primary.media);
		}			
	},
	//검색 결과 이미지 배치
	renderImage : function(items){ 
		$E("resultList").innerHTML = "";

		for(var i=0; i < items.length; i++){
			//IMG 객체 생성
			var img = $new("img");
			//LI 객체 생성
			var li = $new("li");
			//$E = document.getElementById
			$E("resultList").addChild(li);
			li.addChild(img);
			//img 객체에 src속성 정의
			
			var url = items[i].thumb.text;
			img.setAttribute("src",url);
			
			img.imagePath = items[i].url.text;
			img.addEvent("click", function(){
				insertImage(this.imagePath);
			});
		}
	}
}