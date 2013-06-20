  <%@ page contentType="text/csv;charset=euc-kr" %>
  <%@ page language="java"%>
  <%@ page import="com.tobesoft.platform.*" %>
  <%@ page import="com.tobesoft.platform.data.*" %>
  <%@ page import="com.tobesoft.platform.util.*"%>
  <%@ page import="java.io.*" %>
  <%@ page import="java.util.*" %>
  <%@ page import="java.util.zip.*" %>
  <%@ page import="java.sql.*" %>
  <%
  	String default_charset = "euc-kr";
  	
  	// Request Parameter 처리
  	PlatformRequest platformRequest = new PlatformRequest(request, default_charset);
  	platformRequest.receiveData();
  
  	VariableList in_vl = platformRequest.getVariableList();
  	DatasetList in_dl = platformRequest.getDatasetList();
  	Statement stmt = null;
  	ResultSet rs = null;
  
  	Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver"); 
  	Connection conn = DriverManager.getConnection("jdbc:microsoft:sqlserver://localhost:1433;databasename=tobe;user=tobe;password=0701"); 
  	
  	StringBuffer sb = new StringBuffer();
  	
  	try { 
  		String sql = "SELECT * from tobe_post_zip ";
  						  	
  		stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY); 
  		rs = stmt.executeQuery(sql);
  
  		
  		//이부분의 Check 가 필요합니다. (속도에 영향이 많습니다.)
  		// edit by imchaser >>>>>>>>>>>>> SQL Server JDBC에서는 지원이 안되네......
//  		rs.setFetchDirection(ResultSet.FETCH_FORWARD);
//  		rs.setFetchSize(100);   //여기에 있는 수치는 환경에 따라서 수정해 가면서 작업을 하십시오.
  		// <<<<<<<<<<<<edit by imchaser
  
  		ResultSetMetaData rsmd = rs.getMetaData();
  	
  		int numberOfColumns = rsmd.getColumnCount();
  		int    ColSize;
  		
  		sb.append("CSV:euc-kr\n");
  		sb.append("Dataset:output\n");
  		  
  		for ( int j = 1 ; j <= numberOfColumns ; j++ )
  		{
  			String Colnm = rsmd.getColumnName(j); 
  			int    ColType = rsmd.getColumnType(j);
  			sb.append(Colnm);
  			sb.append(":STRING(255)");
  			sb.append(",");
  		}
  		sb.append("chk:STRING(1)");
  		sb.append("\n");
  	
  		while (rs.next()) {
  			for ( int j = 1 ; j <= numberOfColumns ; j++ )
  			{
  				String tmpStr = rs.getString(j);
  		// edit by imchaser  >>>>>>>>>>>>>>>>
				if (tmpStr != null)
					sb.append(tmpStr.trim());
				else
					sb.append("");
  		// <<<<<<<<<<<   edit by imchaser
  
  				sb.append(","); 
  			}
  			sb.append("0");
  			sb.append("\n");
  		}  
  	}
  	catch (SQLException e)
  	{
  		e.printStackTrace();
  		//out_vl.addStr("ErrorCode", "-1");
  		//out_vl.addStr("ErrorMsg", e.getMessage());
  	} 
  	finally
  	{
  		if (rs != null) try{ rs.close(); } catch ( Exception e1){ e1.printStackTrace(); }
  		if (stmt !=null)try{stmt.close(); } catch ( Exception e1){ e1.printStackTrace(); }
  		if (conn != null)try{conn.close(); } catch ( Exception e1){ e1.printStackTrace() ;}
  	}
  	
  	String resStr = sb.toString();	
  	
  //	System.err.println(resStr);
  	short TOBE_COMPRESS_MARK   = (short)0xFFAD;
  	
  	response.resetBuffer();
  	
  	// 데이터를 압축할 때는 이부분을 풀어주세요.  >>>>>>>>>>>>>>>>>>>>>
  	/*
  	DataOutputStream ostream = new DataOutputStream(response.getOutputStream());
  	ostream.writeShort(TOBE_COMPRESS_MARK);
  	
  	DeflaterOutputStream compress = new DeflaterOutputStream(ostream);
  	Writer writer = new PrintWriter(compress);
  	
  	try
  	{
  		writer.write(resStr);
  	} catch (IOException e)
  	{
  		System.out.println(e.toString());
  	} 
  	
  	writer.flush();
  	writer.close();
  	*/
  	// <<<<<<<<<<<<<<<<<<      데이터를 압축할 때는 이부분을 풀어주세요.  
  	
  	out.clearBuffer();
  	
  	out.println(resStr);
  %>
