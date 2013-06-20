<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page language="java"%>
<%@ page import="com.tobesoft.platform.*" %>
<%@ page import="com.tobesoft.platform.data.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.sql.*" %>

<%

	String default_charset = "euc-kr";
	String outDSnm = "output";
	boolean bFlag = false;

	PlatformRequest platformRequest = new PlatformRequest(request, default_charset);
	PlatformFRResponse platformFirstRowResponse = null;
	out.clearBuffer();

	String strFlag = null;
	String strRows = null;
	String strSize = null;

 	if ( request.getContentLength() > 0 )
 	{
		try {
		 	platformRequest.receiveData();
			strFlag = platformRequest.getVariableList().getValue("first").toString();
			strRows = platformRequest.getVariableList().getValue("rows").toString();
			strSize = platformRequest.getVariableList().getValue("size").toString();
	 	}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
 	}

	int nSize = 10;       //FireFirstCount
	int nRows = 100000;   //FireNextCount

	if (strFlag == null || strFlag.compareToIgnoreCase("true") != 0)
		bFlag = false;
	else
		bFlag = true;

	if (strRows != null)
		nRows =  new Integer(strRows).intValue();

	if (strSize != null)
		nSize =  new Integer(strSize).intValue();

System.out.println("nRows:" + nRows);
System.out.println("nSize:"+ nSize);

	int default_encode_method = PlatformRequest.ZLIB_COMP;

	VariableList out_vl = new VariableList();
	DatasetList  out_dl = new DatasetList();

	Statement stmt = null;
	ResultSet rs = null;

	int totalCnt = 0;


	Dataset dsOut = new Dataset(outDSnm, default_charset);

	dsOut.addColumn("SEQ", ColumnInfo.CY_COLINFO_INT, (short)4);
	dsOut.addColumn("Value1", ColumnInfo.CY_COLINFO_STRING, (short)128);
	dsOut.addColumn("Value2", ColumnInfo.CY_COLINFO_STRING, (short)128);
	dsOut.addColumn("Value3", ColumnInfo.CY_COLINFO_STRING, (short)128);
	dsOut.addColumn("Value4", ColumnInfo.CY_COLINFO_STRING, (short)128);

	boolean FirstFlag = true;
	int Packet_Size = nSize;
	for (int i = 0; i < nRows; i++)
	{
		int row = dsOut.appendRow();
		dsOut.setColumn(row, "SEQ", new Variant(i + 1));
		String strVal;
		strVal = "Value >>>>>> " + i;
		dsOut.setColumn(row, "Value1", new Variant(strVal));
		dsOut.setColumn(row, "Value2", new Variant(strVal));
		dsOut.setColumn(row, "Value3", new Variant(strVal));
		dsOut.setColumn(row, "Value4", new Variant(strVal));
		dsOut.setColumn(row, "Value5", new Variant(strVal));

		if (bFlag)
		{
			if (FirstFlag && row >= nSize)
			{
				platformFirstRowResponse = new PlatformFRResponse(response,default_charset);
				out_vl.addStr("ErrorCode", "0");
				out_vl.addStr("ErrorMsg","SUCC");
				out_dl.addDataset(outDSnm, dsOut);
				platformFirstRowResponse.sendFirstData(out_vl, out_dl);
				dsOut.deleteAll();
				FirstFlag = false;
			}
			else if (!FirstFlag && row >= Packet_Size)
			{
				platformFirstRowResponse.sendNextData(dsOut, 0, Packet_Size,true);
				dsOut.deleteAll();
			}
		}
		totalCnt++;
	}

	if (bFlag)
		platformFirstRowResponse.sendNextData(dsOut,0,Packet_Size,false);
	else
	{
		PlatformData out_data = new PlatformData(default_charset);

		out_data.getVariableList().addVariable("ErrorCode", new Variant(0));
		out_data.getVariableList().addVariable("ErrorMsg", new Variant("Success to run"));

		out_data.getDatasetList().addDataset(outDSnm, dsOut);

		PlatformResponse platformRes = new PlatformResponse(response, default_encode_method, default_charset);

		platformRes.sendData(out_data);
	}
%>