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
	loadtext: "�ε���...",
	pgtext : "/"
};
$.jgrid.search = {
    caption: "�˻���...",
    Find: "�˻�",
    Reset: "�ʱ�ȭ",
    odata : ['equal', 'not equal', 'less', 'less or equal','greater','greater or equal', 'begins with','ends with','contains' ]
};
$.jgrid.edit = {
    addCaption: "�߰�",
    editCaption: "����",
    bSubmit: "����",
    bCancel: "���",
	bClose: "�ݱ�",
    processData: "������...",
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
    caption: "����",
    msg: "Delete selected record(s)?",
    bSubmit: "����",
    bCancel: "���",
    processData: "������..."
};
$.jgrid.nav = {
	edittext: " ",
    edittitle: "���õ� �� ����",
	addtext:" ",
    addtitle: "�ű� �� �߰�",
    deltext: " ",
    deltitle: "���õ� �� ����",
    searchtext: " ",
    searchtitle: "���ڵ� �˻�",
    refreshtext: "",
    refreshtitle: "�׸��� ���ΰ�ħ",
    alertcap: "���",
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
