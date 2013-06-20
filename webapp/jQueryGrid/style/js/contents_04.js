var lastsel3;

$(document).ready(function(){

    $("#gridList").jqGrid({
        url:'server.json',
        mtype: 'GET',
        datatype: "json",
        colNames:['인벤토리','날짜', '고객', '수량','세금','합계','비고'],
        colModel:[
        	{name:'id',index:'id', width:100,align:"center",key:true},
        	{name:'invdate',index:'invdate', width:120,align:"center", editable:true,sorttype:"date",formatter:'date', formatoptions:{srcformat:"Y-m-d",newformat:"d-M-Y"}},
        	{name:'name',index:'name asc, invdate', width:100,editable:true,editoptions:{size:"20",maxlength:"30"}},
        	{name:'amount',index:'amount', width:100, align:"right",editable:true,editable:true,editrules:{number:true,minValue:100,maxValue:350},formatter:'currency',formatoptions:{thousandsSeparator:","}},
        	{name:'tax',index:'tax', width:100, align:"right",editable:true,edittype:"select",editoptions:{value:"FE:FedEx;IN:InTime;TN:TNT;AR:ARAMEX"}},
        	{name:'total',index:'total', width:100,align:"right",editable:true,edittype:"checkbox",editoptions: {value:"Yes:No"}},
        	{name:'note',index:'note', width:150, sortable:false,editable:true,edittype:"textarea", editoptions:{rows:"1",cols:"20"}}
        ],
        rowNum:-1,  //30,
        height:278,
        sortname: 'id',
        sortorder: "desc",
        viewrecords: true,
        multiselect: true,//앞에 체크박스처리
        multikey: "",//multikey: "ctrlKey/shiftKey",
        editurl: "server.json",
		autowidth: false,
		//shrinkToFit:false,
		altRows: true,	
	    ignoreCase: true,
        onSelectRow: function(id){  //row가 선택되었을 경우
    		if(id && id!==lastsel3){
    			jQuery('#gridList').restoreRow(lastsel3);
    			jQuery('#gridList').editRow(id,true,pickdates);
    			lastsel3=id;
		    }
	    },
        /*jsonReader: {//스크롤할때마다 가져오기
    		repeatitems : true,
    		cell:"",
    		id: "0"
    	},*/
        /*afterInsertRow: function(rowid, aData){
        	switch (aData.name) {
        		case 'Client 1':
        			jQuery("#gridList").setCell(rowid,'total','',{color:'green'});
        		break;
        		case 'Client 2':
        			jQuery("#gridList").setCell(rowid,'total','',{color:'red'});
        		break;
        		case 'Client 3':
        			jQuery("#gridList").setCell(rowid,'total','',{color:'blue'});
        		break;
        		
        	}
        },*/
        loadError : function(xhr,st,err) {
        	jQuery("#rsperror").html("Type: "+st+"; Response: "+ xhr.status + " "+xhr.statusText);
        },
        imgpath: 'style/grid'
    });


/*

*/	
    $("#a1").click( function(){
    	var id = jQuery("#gridList").getGridParam('selrow');
    	if (id)	{
    		var ret = jQuery("#gridList").getRowData(id);
    		alert("id="+ret.id+" invdate="+ret.invdate+"...");
    	} else { alert("Row를 선택해주세요");}
    });
    $("#a2").click( function(){
    	var su=jQuery("#gridList").delRowData(12);
    	if(su) alert("id가 12인 Row삭제"); else alert("이미 지워졌삼~");
    });
    $("#a3").click( function(){
    	var datarow = {id:"99",invdate:"2007-09-01",name:"test3",note:"note3",amount:"400.00",tax:"30.00",total:"430.00"};
    	var su=$("#gridList").addRowData(99,datarow);
    	if(su) alert("마지막Row추가 성공- 서버쪽 업데이트처리해주세요"); else alert("처리가 되지 않았음.");
    });

    $("#a4").click( function() {
        $("#gridList").resetSelection();
    	$("#gridList").setSelection("13");
    });

    $("#a5").click( function() {
    	$("#gridList").hideCol("tax");
    });
    $("#a6").click( function() {
    	$("#gridList").showCol("tax");
    });

    jQuery("#a7").click( function() {
    	jQuery("#gridList").editRow("13");
    	this.disabled = 'true';
    	jQuery("#a8,#a9").attr("disabled",false);
    });
    jQuery("#a8").click( function() {
    	jQuery("#gridList").saveRow("13");
    	jQuery("#a8,#a9").attr("disabled",true);
    	jQuery("#a7").attr("disabled",false);
    });
    jQuery("#a9").click( function() {
    	jQuery("#gridList").restoreRow("13");
    	jQuery("#a8,#a9").attr("disabled",true);
    	jQuery("#a7").attr("disabled",false);
    });

    jQuery("#a10").click( function() {
    	$("#gridList").setLabel("tax","Tax Amt",{'font-weight': 'bold','font-style': 'italic'});
    });
    
    jQuery("#a11").click( function() {
    	$("#gridList").setCell("12","tax","",{'font-weight': 'bold',color: 'red','text-align':'center'});
    });
    
    jQuery("#a12").click( function() {
    	$("#gridList").clearGridData();
    });

    jQuery("#a13").click( function() {
    	$("#gridList").setGridWidth(500);
    	$("#gridList").setGridHeight(400);

    });

    jQuery("#a14").click(function (){
    	tableToGrid("#htmlGrid");
    	$("#htmlGrid").setGridWidth(910);
    });

	jQuery("#a15").click( function() {
		alert($("#gridList").getGridParam('records'));
	});






});



function pickdates(id){
	alert(id);
	var myArray = [ 'a', 'b', 'c' ];
	var len = myArray.length;
	log( len ); // logs 3
	alert(id);
}
