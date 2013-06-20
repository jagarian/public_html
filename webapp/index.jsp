<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="java.util.*" %>
<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader("Expires",0);
	if (request.getProtocol().equals("HTTP/1.1")){
		response.setHeader("Cache-Control", "no-cache");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="Expires" content="-1" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="No-Cache" />

<title>e휴게소 고도화</title>
<script type="text/javascript">
<!--
function fn_send(num){

    if(num == "1"){
        alert("사용자--1--준비중");
        //location.href = "./miplatform/egovframework/index.html";
    }
    else if(num == "2"){
        alert("사용자--2--준비중");
        //location.href = "./miplatform/egovframework2/index.html";
    }
    else if(num == "3"){
        alert("사용자--3");
        //location.href = "./miplatform/egovframework3/index.html";
        location.href = "./UI/index.html";
    }
    else if(num == "4"){
        alert("준비중--4");
        //location.href = "./UI/egovframework4/index.html";
    }
}
function fn_load()
{
	window.location = "index.html";
}
//-->
</script>
<style type="text/css">
	table{ border-collapse: collapse; font-size:9pt}
	td.border{ border: 1px solid #FC0; padding: 10px; }

</style>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="fn_load();">
<br /><br /><br />
    <!-- 
    <table width="600px" align="center">
    <tr>
        <td class="border"><a href="javascript:fn_send('1')">사용자-1</a></td>
        <td class="border"><a href="javascript:fn_send('2')">사용자-2</a></td>
    </tr>
    <tr>
        <td class="border"><a href="javascript:fn_send('3')">사용자-3</a></td>
        <td class="border"><a href="javascript:fn_send('4')">사용자-4</a></td>
    </tr>
    </table>
 	-->
</body>
</html>
