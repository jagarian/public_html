;(function($){
/**
 * jqGrid English Translation
 * Tony Tomov tony@trirand.com
 * http://trirand.com/blog/ 
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
**/
$.jgrid = {};

$.jgrid.defaults = {
	recordtext: "Row(s)",
	loadtext: "로딩중...",
	pgtext : "/"
};
$.jgrid.search = {
    caption: "검색중...",
    Find: "검색",
    Reset: "초기화",
    odata : ['equal', 'not equal', 'less', 'less or equal','greater','greater or equal', 'begins with','ends with','contains' ]
};
$.jgrid.edit = {
    addCaption: "추가",
    editCaption: "편집",
    bSubmit: "전송",
    bCancel: "취소",
	bClose: "닫기",
    processData: "진행중...",
    msg: {
        required:"Field is required",
        number:"Please, enter valid number",
        minValue:"value must be greater than or equal to ",
        maxValue:"value must be less than or equal to",
        email: "is not a valid e-mail",
        integer: "Please, enter valid integer value",
		date: "Please, enter valid date value"
    }
};
$.jgrid.del = {
    caption: "삭제",
    msg: "Delete selected record(s)?",
    bSubmit: "삭제",
    bCancel: "취소",
    processData: "진행중..."
};
$.jgrid.nav = {
	edittext: " ",
    edittitle: "선택된 줄 수정",
	addtext:" ",
    addtitle: "신규 줄 추가",
    deltext: " ",
    deltitle: "선택된 줄 삭제",
    searchtext: " ",
    searchtitle: "레코드 검색",
    refreshtext: "",
    refreshtitle: "그리드 새로고침",
    alertcap: "경고",
    alerttext: "Please, select row"
};
// setcolumns module
$.jgrid.col ={
    caption: "Show/Hide Columns",
    bSubmit: "Submit",
    bCancel: "Cancel"	
};
$.jgrid.errors = {
	errcap : "Error",
	nourl : "No url is set",
	norecords: "No records to process",
    model : "Length of colNames <> colModel!"
};
$.jgrid.formatter = {
	integer : {thousandsSeparator: " ", defaulValue: 0},
	number : {decimalSeparator:".", thousandsSeparator: " ", decimalPlaces: 2, defaulValue: 0},
	currency : {decimalSeparator:".", thousandsSeparator: " ", decimalPlaces: 2, prefix: "", suffix:"", defaulValue: 0},
	date : {
		dayNames:   [
			"Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat",
			"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
		],
		monthNames: [
			"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
			"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
		],
		AmPm : ["am","pm","AM","PM"],
		S: function (j) {return j < 11 || j > 13 ? ['st', 'nd', 'rd', 'th'][Math.min((j - 1) % 10, 3)] : 'th'},
		srcformat: 'Y-m-d',
		newformat: 'd/m/Y',
		masks : {
            ISO8601Long:"Y-m-d H:i:s",
            ISO8601Short:"Y-m-d",
            ShortDate: "n/j/Y",
            LongDate: "l, F d, Y",
            FullDateTime: "l, F d, Y g:i:s A",
            MonthDay: "F d",
            ShortTime: "g:i A",
            LongTime: "g:i:s A",
            SortableDateTime: "Y-m-d\\TH:i:s",
            UniversalSortableDateTime: "Y-m-d H:i:sO",
            YearMonth: "F, Y"
        },
        reformatAfterEdit : false
	},
	baseLinkUrl: '',
	showAction: 'show',
	addParam : ''
};
// US
// GB
// CA
// AU
})(jQuery);
