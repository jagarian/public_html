var ColorPicker ={
	info : "컬러픽커",
	parent : "",
	el : "",
	loaded : false,
	maxValue : {'h':360,'s':100,'v':100},
	HSV : [360,100,100],
	hSV : 167,
	wSV : 170,
	hH : 170,
	selectedColor : "#000000",
	slideHSV : [360,100,100],
	callBack : function(){},
	init : function(oEditor){
		if(this.loaded) return;
		this.parent = oEditor.webeditorContainer;
		this.loaded = true;
		this.makePalette();
	},
	show : function(target,mode){
		var pos = positionUtil.getComparePosition(target,this.parent);
		if($E("ColorPickerContainer").getStyle("display") == "block"){
			this.hide();
			return;
		}
		$E("ColorPickerContainer").setStyle("display" , "block");
		$E("ColorPickerContainer").setStyle("left" , (pos.x + 5) +"px");
		$E("ColorPickerContainer").setStyle("top" , (pos.y + 15) +"px");
	},
	makePalette : function(){
		var paletteContainer = $new("DIV");
		paletteContainer.id = "ColorPickerContainer";
		$each({
			"position" : "absolute",
			"top" : "100px",
			"left" : "100px",
			"width" :"200px",
			"background" : "#FFFFF",
			"display" : "none",
			"border"  : "2px solid #ccc",
			"padding" : "0px",
			"z-index" : "10000"
		},function(val,prop){
			paletteContainer.setStyle(prop,val);
		});
		
		try{
			$E(this.parent).addChild(paletteContainer);
			this.el = paletteContainer;
		}catch(e){
			debug.log("ColorPicker : " + e);
		}

		var platte_style = ($client.browser != "IE") ?
				"background:url("+OPEN_EDITOR_SKIN+"/colorPlatte.png)#ff0000" : "background:#ff0000; background-image:none" ;

		var suggestColor = ["ff0000","ff6c00","ffaa00","ffef00","a6cf00","009e25","00b0a2","0075c8","3a32c3","7820b9","ef007c","000000","252525","464646","636363","7d7d7d","9a9a9a",
			"e97d81","e19b73","d1b274","cfcca2","cfcca2","61b977","53aea8","518fbb","6a65bb","9a54ce","e573ae","5a504b","767b86","009900","66CC99","CC3300","006600",
			"CCFF66","CC99FF","FF99FF","ffe8e8","f7e2d2","f5eddc","f5f4e0","edf2c2","def7e5","d9eeec","c9e0f0","d6d4eb","e7dbed","f1e2ea","acacac","c2c2c2","cccccc"
			,"e1e1e1","ebebeb","ffffff"];

		var sb = new StringBuffer();
		sb.append('<table id="colorpicker" width="200px" bgcolor="#FFFFFF" cellspacing="1"  border=0 cellpadding=0 cellspacing=0 tabindex="1">')
		.append('		<tr>')
		.append('			<? for(var i=0;i<suggestColor.length;i++){?>')
		.append('				<td style="cursor: pointer" width="10px" height="10px" bgcolor="#<?= suggestColor[i]?>" onclick="ColorPicker.execCommand(\'#<?= suggestColor[i]?>\')"></td>')
		.append('				<? if((i+1)%18 == 0){?>')
		.append('					</tr>')
		.append('					<tr>')
		.append('				<?}?>')
		.append('			<?}?>')
		.append('		</tr>		')
		.append('	</tbody>')
		.append('</table>')
		.append('<table id="colorpicker_sub" width="200px" bgcolor="#FFFFFF" height="180px" border=0 cellpadding=0 cellspacing=0>')
		.append('	<tr>')
		.append('		<td height="22" colspan=2>')
		.append('			<table width="200" cellpadding="0" cellspacing="0" align="center">')
		.append('				<tr>')
		.append('					<td width="55" style="padding-left:5px">')
		.append('						<input type="text" id="selectedColorHex" style="font-size:12px;width:55px;height:15px;border:1px solid #FFF;color:#838383" value="#000000"></td>')
		.append('					<td width="80" align="right" style="padding-right:5px">')
		.append('						<div style="width:62px;border:1px solid #CCC;padding:2px">')
		.append('							<div id="selectedColorSpan" style="width:60px;height:13px;background:#<?= selectedColor?>"></div>')
		.append('						</div>')
		.append('					</td>')
		.append('					<td width="65"><img src="'+OPEN_EDITOR_SKIN+'colorSelectBtn.gif" alt="색상선택" id="applyColorBtn" style="cursor:pointer"></td>')
		.append('				</tr>')
		.append('			</table>')
		.append('		</td>')
		.append('	</tr>')
		.append('	<tr><td height="3" colspan="2"></td></tr>')
		.append('	<tr>')
		.append('		<td width="25" align=center><div id="HSVslide" style="cursor: crosshair;width:15px;border:1px solid #CCC">')
		.append('			<? for(var i =0 ; i < getSlidePaletteElements.length; i++){?>')
		.append('			<div style="background:#<?= getSlidePaletteElements[i]?>;width:15px;height:1px;float:left;position:relative;clear:both;overflow:hidden"></div>')
		.append('			<?}?>')
		.append('			</div></td>')
		.append('		<td width="175">')
		.append('			<div id="hsvplatte"	 style="<?= platte_style?>;')
		.append('				filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='+OPEN_EDITOR_SKIN+'/colorPlatte.png, ')
		.append('				sizingMethod=crop); width: 170px;cursor: crosshair;height: 170px;border:1px solid #CCC"></div>')
		.append('		</td>')
		.append('	</tr>	')
		.append('	<tr><td height="4" colspan="2"></td></tr>')
		.append('</table>');
		
		this.readyPlatte(Jst.load(sb.toString(),{
			"getSlidePaletteElements" : this.getSlidePaletteElements(),
			"platte_style" : platte_style,
			"selectedColor" : this.selectedColor,
			"suggestColor" : suggestColor
		}));		
		

	},
	hide : function(){
		$E("ColorPickerContainer").setStyle("display","none");
	},
	readyPlatte : function(html){		
		$E("ColorPickerContainer").setHTML(html);
		$E("hsvplatte").addEvent("mousedown",function(evt){this.stop = 1;});
		$E("hsvplatte").addEvent('mouseup',function(evt){this.stop = 0;});
		$E("HSVslide").addEvent("mousedown",function(evt){this.stop = 1;});
		$E("HSVslide").addEvent('mouseup',function(evt){this.stop = 0;});
		$E("applyColorBtn").addEvent("click",function(evt){
			ColorPicker.execCommand(ColorPicker.selectedColor);
		});		
		

		$E("hsvplatte").addEvent('mousemove',function(evt){			
			if(!this.stop) return;			
			ColorPicker.slideHSV[1]=ColorPicker.mkHSV(100,170,(evt.clientX - this.getOffset("left")));
			ColorPicker.slideHSV[2]=100-ColorPicker.mkHSV(100,170,(ColorPicker.getXY(evt,1)- this.getOffset("top")));
			ColorPicker.setHSV();			
		});

		$E("hsvplatte").addEvent('click',function(evt){			
			ColorPicker.slideHSV[1]=ColorPicker.mkHSV(100,170,(evt.clientX - this.getOffset("left")));
			ColorPicker.slideHSV[2]=100-ColorPicker.mkHSV(100,170,(ColorPicker.getXY(evt,1)- this.getOffset("top")));
			ColorPicker.setHSV();
		});

		$E("HSVslide").addEvent('click',function(evt){			
			var ck = ColorPicker.ckHSV((ColorPicker.getXY(evt,1) - this.getOffset("top")),170);
			var j, r='hsv', z={};
			for(var i=0; i<=r.length-1; i++) {
				j=r.substr(i,1);
				z[i]=(j=='h')?ColorPicker.maxValue[j]-ColorPicker.mkHSV(ColorPicker.maxValue[j],ColorPicker.hH,ck)
					:ColorPicker.HSV[i];
			}
			ColorPicker.slideHSV[0] = z[0];
			ColorPicker.slideHSV[1] = ColorPicker.slideHSV[2] = 100;
			$E("hsvplatte").setStyle("backgroundColor","#"+ColorPicker.hsv2hex(z));
		});


		$E("HSVslide").addEvent('mousemove',function(evt){
			if(!this.stop) return;			
			var ck = ColorPicker.ckHSV((ColorPicker.getXY(evt,1) - this.getOffset("top")),170);
			var j, r='hsv', z={};
			for(var i=0; i<=r.length-1; i++) {
				j=r.substr(i,1);
				z[i]=(j=='h')?ColorPicker.maxValue[j]-ColorPicker.mkHSV(ColorPicker.maxValue[j],ColorPicker.hH,ck)
					:ColorPicker.HSV[i];
			}
			ColorPicker.slideHSV[0] = z[0];
			ColorPicker.slideHSV[1] = ColorPicker.slideHSV[2] = 100;
			$E("hsvplatte").setStyle("backgroundColor","#"+ColorPicker.hsv2hex(z));
		});		
	},
	getSlidePaletteElements : function(){
		var colorArr = [];
		for(var i= this.hSV; i>=0; i--){
			colorArr.push(this.hsv2hex([Math.round((360/this.hSV)*i),100,100]));
		}
		return colorArr;
	},
	toHex : function (v){
		v=Math.round(Math.min(Math.max(0,v),255));
		return("0123456789ABCDEF".charAt((v-v%16)/16)+"0123456789ABCDEF".charAt(v%16));
	},
	rgb2hex : function(r){
		 return(this.toHex(r[0])+this.toHex(r[1])+this.toHex(r[2]));
	},
	hsv2hex : function(h){
		return(this.rgb2hex(this.hsv2rgb(h)));
	},
	hsv2rgb : function(r) {
		var R,B,G,S=r[1]/100,V=r[2]/100,H=r[0]/360;
		if(S>0) { if(H>=1) H=0;
			H=6*H; 
			F=H-Math.floor(H);
			A=Math.round(255*V*(1.0-S));
			B=Math.round(255*V*(1.0-(S*F)));
			C=Math.round(255*V*(1.0-(S*(1.0-F))));
			V=Math.round(255*V);

			switch(Math.floor(H)) {
				case 0: R=V; G=C; B=A; break;
				case 1: R=B; G=V; B=A; break;
				case 2: R=A; G=V; B=C; break;
				case 3: R=A; G=B; B=V; break;
				case 4: R=C; G=A; B=V; break;
				case 5: R=V; G=A; B=B; break;
			}
			return([R?R:0,G?G:0,B?B:0]);
		}
		else return([(V=Math.round(V*255)),V,V]);
	},
	within : function(v,a,z){
		return((v>=a && v<=z)?true:false);
	},
	getXY : function(e,idx) {
		idx = idx || 0;		
		var xy=($client.browser == "IE")?[e.clientX+Stage.scrollLeft(),
			e.clientY+Stage.scrollTop()]:[e.pageX,e.pageY];
		return(xy[idx]);
	},
	mkHSV : function(a,b,c) {
		return(Math.min(a,Math.max(0,Math.ceil((parseInt(c)/b)*a))));
	},
	ckHSV : function(a,b){
		if(this.within(a,0,b)) return(a);
		else if(a>b) return(b);
		else if(a<0) return 0;
	},
	setHSV :function(v){
		v = this.hsv2hex(this.HSV=v?v:this.slideHSV);
		this.selectedColor = '#'+v;
		$E("selectedColorSpan").setStyle("background" , "#"+v);
		$E("selectedColorHex").value=this.selectedColor;
	},
	execCommand : function(color){
		if(color)ColorPicker.selectedColor = color;
		ColorPicker.callBack();
		ColorPicker.hide();
		ColorPicker.initVal();
		return true;
	},
	initVal : function(){
		this.callBack = function(){};
	}
};