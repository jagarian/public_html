package neo.ftp;

import java.net.*;
import java.util.*;

import java.io.*;

import neo.config.Config;
import neo.exception.ConfigException;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	RemoteFileAccess.java
 * 	@���ϼ���		:	- ����Ʈ ���丮 ȣ��� ���氡���ؾ� �Ѵ�. (�ٷ��ذᰡ��)
 *					    - �ٿ�ε� ���丮 ���� �����ؾ� �Ѵ�. (�ٷ��ذᰡ��)
 *						- ���ε� �� ������ ó���� ȿ������ �÷��� �Ѵ�. ( ���̵�(��) )
 *						- �̱۷������� ftp ���� ���� �������� �� �پ��� ������ ���� ��� �� ? (�ٷ��ذᰡ��)
 *						- �������� ftp ���� ������ �ٿ�°��� ȿ�����ΰ� ? ( ���̵�(��) )
 *						. ftp poll �
 *						. ����κ� ����� ftp connection �� �յ��ϰ� getconnection �Ҽ� �ְ� : max �� ���س���)
 *
 *						����)
 *						try{
 *							RemoteFileAccess rf = new RemoteFileAccess("s01");
 *							if (rf.FTPConnect()){ //ftp����
 *								Log.info("���ӿ� �����Ͽ����ϴ�!");
 *								Log.info(rf.getFileName("address.prop"));
 *								rf.putFile("webapp.zip","C:\\intisys\\work\\webapp\\FTPUPLOAD\\webapp.zip",false); //������ ���ε� ó�� 
 *								rf.getFile("webapp.zip"); //������ �ٿ�ε� ó��
 *								rf.FTPClose(); //ftp�ݴ´�
 *							}
 *						}catch(Exception e){
 *							e.printStackTrace();
 *						}
 * 	@Version			: 	1.0
 *	@Author			: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 	�۾��� 		����	����	�۾���		����
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		����	hoon09		source create (�Ｚ����)
 *	2006-11-23 	1.4		����	hoon09		code convention apply (��Ƽķ�۽�)
 *	2009-07-03	1.6		����	hoon09		code convention apply (��������, ��Ÿ��ť��Ƽ)
 *	2009-09-23	1.7		����	hoon09		code valid check (Ǫ������������,���)
 **********************************************************************************************             
 */
public class RemoteFileAccess {
	private static final String FTP_PROP_BASE = "/configuration/neo/ftp/spec<";

	// ftp server ip
	private String server = "";

	// ����
	private String user = "";
	private String pw = "";

	// port / defauly directory Setting
	private int port = 0;
	private String defaultPath = "";

	private Socket socket;
	private BufferedReader in;
	private BufferedWriter out;

	private String viewFile = "";
	private String status = "";
	private boolean passive = false;
	private Hashtable fileHash = new Hashtable();

	public static final String homePath = System.getProperty("user.dir");

	/**
	   Default ������
	 */
	public RemoteFileAccess(String spec) throws ConfigException {
		Config conf = Config.getInstance();

		String ftp_server_ip 	= conf.getString(FTP_PROP_BASE + spec + ">/server_ip", null);
		String ftp_user 			= conf.getString(FTP_PROP_BASE + spec + ">/user", null);
		String ftp_password 	=	conf.getString(FTP_PROP_BASE + spec + ">/password", null);
		int ftp_port 				= conf.getInt(FTP_PROP_BASE + spec + ">/port", 0);
		String ftp_default_path = conf.getString(FTP_PROP_BASE + spec + ">/defaultPath", null);

		this.server = ftp_server_ip;
		this.user = ftp_user;
		this.pw = ftp_password;
		this.port = ftp_port;
		this.defaultPath = ftp_default_path;
	}

	/**
	  Default ������
	*/
	public boolean FTPConnect() {
		boolean connectFlag = false;

		Log.info("server :: " + server, this);
		Log.info("user :: " + user, this);
		Log.info("pw :: " + pw, this);
		Log.info("port :: " + port, this);
		Log.info("defaultPath :: " + defaultPath, this);

		try {
			if (socket != null)
				socket = null;

			socket = new Socket(server, port);
			Log.info("===1=== :: socket ready...", this);

			in =
				new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			Log.info("===2=== :: InputStream ready...", this);

			out =
				new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream()));
			Log.info("===3=== :: OutputStream ready... ", this);

			getStatusDisplay();
			Log.info("===4=== :: display...", this);

			ftpLogin(user, pw);
			Log.info("===5=== :: ftp login...", this);

			getDirecotry();
			Log.info("===6=== :: get dir...", this);

			connectFlag = true;

		} catch (Exception e) {
			Log.error("===err=== :: " + e.getMessage(), this);
		} finally {
			return connectFlag;
		}
	}

	public boolean FTPClose() {
		boolean connectFlag = false;
		try {
			if (in != null)
				socket = null;
			if (out != null)
				socket = null;
			if (socket != null)
				socket = null;
			connectFlag = true;
			Log.info("ftp ���������� �ݾҽ�...", this);
		} catch (Exception e) {
			Log.error("ftp �ݴµ��� �����߻�...", this);
		} finally {
			return connectFlag;
		}
	}

	/**
	  FTP Login
	*/
	public void ftpLogin(String in_user, String in_pw) throws IOException {
		exeCommand("USER " + in_user);
		exeCommand("PASS " + in_pw);

		if (!status.substring(0, 3).equals("230")) {
			close();
			throw new IOException("FTP Login Error!");
		}
	}

	/**
	  TransferMode Setting ASCll, Binary
	*/
	public void setTransferMode(boolean in_transferMode) throws Exception {
		if (in_transferMode) {
			exeCommand("TYPE A");
		} else {
			exeCommand("TYPE I");
		}
	}

	/**
	  Ftp������ Default���     ����
	*/
	public void setRemoteDefaultDirectory(String in_remoteDirectory)
		throws Exception {
		exeCommand("CWD " + in_remoteDirectory);
	}

	/**
	  ��ɾ ������� ���� ����� Stream���� �޾ƿ��� Function
	*/
	private Socket getDataSocket(String[] commands, boolean lsw)
		throws Exception {
		Log.info("getDataSocket in commands is => " + commands, this);

		Socket sock = null;
		ServerSocket ssock = null;

		try {
			// Establish data conncetion using passive or active mode.
			if (!passive) {
				Log.info("Active ������ ã�´�...", this);
				ssock = getActiveDataSocket();
			}

			// Send ftp commands
			for (int i = 0; i < commands.length; i++) {
				Log.info("commands is :: " + commands[i], this);
				exeCommand(commands[i]);
			}

			Log.info("status is :: " + status, this);

			if (lsw
				&& (!(status.substring(0, 3).equals("150")
					|| status.substring(0, 3).equals("125")))) {
				if (sock != null)
					sock.close();
				Log.error(">>>here getDataSocket1 error...", this);
				throw new IOException(); // command failed
			}

			// Get Socket object for active mode.
			if (!passive) {
				sock = ssock.accept();
			}
			if (lsw
				&& (!(status.substring(0, 3).equals("150")
					|| status.substring(0, 3).equals("125")))) {
				if (sock != null)
					sock.close();
				Log.error(">>>here getDataSocket2 error...", this);
				throw new IOException(); // command failed
			}
		} catch (Exception e) {
			Log.error(">>>here getDataSocket3 error...", this);
			throw e;
		}

		Log.info("getDataSocket finish...", this);
		return sock;
	}

	/**
	  getActiveDataSocket
	*/
	private ServerSocket getActiveDataSocket() throws IOException {
		int[] port_numbers = new int[6]; // Array that contains

		// Get ip address of local machine. ip address and port numbers
		String local_address = socket.getLocalAddress().getHostAddress();

		// Assign the ip address of local machine to the array.
		StringTokenizer st = new StringTokenizer(local_address, ".");
		for (int i = 0; i < 4; i++) {
			port_numbers[i] = Integer.parseInt(st.nextToken());
		}

		ServerSocket ssocket = new ServerSocket(0);
		// ServerSocket to listen to a random free port number
		int local_port = ssocket.getLocalPort();
		// The port number it is listenning to

		// Assign port numbers the array
		port_numbers[4] = ((local_port & 0xff00) >> 8);
		port_numbers[5] = (local_port & 0x00ff);

		// Send "PORT" command to server
		String port_command = "PORT ";
		for (int i = 0; i < port_numbers.length; i++) {
			port_command = port_command.concat(String.valueOf(port_numbers[i]));
			if (i < port_numbers.length - 1) {
				port_command = port_command.concat(",");
			}
		}
		exeCommand(port_command);

		if (!status.substring(0, 3).equals("200")) {
			Log.error(">>>here getActiveDataSocket error...", this);
			throw new IOException();
		}

		return ssocket;
	}

	/**
	  ���ӻ��¿��� ��ɾ� �����Ű�� Function
	*/
	public void exeCommand(String command) throws IOException {
		if (out == null)
			return;

		Log.info("[command]" + command, this);

		out.write(command + "\r\n");
		out.flush();
		getStatusDisplay();
	}

	/**
	  ���ӻ��¸� Display���ְ� ��ɾ�������� ��������Checking�� Support���ִ� Function
	*/
	public void getStatusDisplay() throws IOException {
		do {
			status = in.readLine();
			Log.info("[STATUS]" + status, this);
			if (!msgCheck(status))
				break;
		} while (true);

	}

	/**
	  ��ɾ� �������� 3�ڸ�����ǥ�ø� Checkgin���ִ� Function
	*/
	private boolean msgCheck(String str) {

		if (str.length() > 3 && 
			str.charAt(3) == ' ' && 
			Character.isDigit(str.charAt(0)) && 
			Character.isDigit(str.charAt(1))	&& 
			Character.isDigit(str.charAt(2))) {
			return false;
		} else {
			return true;
		}
	}

	/**
	���� Direcotry�� FileList�� �����ش�.
	*/
	public void getDirecotry() throws Exception {
		if (out == null)
			return;

		String file_info = "";
		Socket direct_socket = null;
		BufferedReader br_reader = null;

		setTransferMode(true);

		setRemoteDefaultDirectory(defaultPath);

		try {
			direct_socket = getDataSocket(new String[] { "LIST -f" }, true);
			br_reader =
				new BufferedReader(
					new InputStreamReader(direct_socket.getInputStream()));

			while ((file_info = br_reader.readLine()) != null) {
				String fileName1 = null;
				String fileName2 = null;

				StringTokenizer st = new StringTokenizer(file_info, " ");

				while (st.hasMoreElements()) {
					fileName2 = st.nextToken();
					if (fileName2.indexOf(".") > 0) {
						//fileName1 = fileName2.substring(0, fileName2.indexOf(".")); //Ȯ�尡�� �ʿ����� �ʴ� ��쿡�� Ȯ���ڸ� �ڸ���.
						fileName1 = fileName2;
					} else {
						fileName1 = "";
					}
				}

				if (fileName1 != null && fileName1.trim().length() > 0) {
					Log.info("fileName1 :: " + fileName1, this);
					fileHash.put(fileName1, fileName2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			if (direct_socket != null)
				direct_socket.close();
			if (br_reader != null)
				br_reader.close();
		}
	}

	public String getFileName(String fileNm) {
		Log.info("search fileNm :: " + fileNm, this);
		return nullToStr(String.valueOf(fileHash.get(fileNm)));
	}

	public String nullToStr(String inParm) {
		if (inParm == null || inParm.equals("null"))
			inParm = "";

		return inParm.trim();
	}

	public void getFile(String ftpfile) throws Exception {
		byte[] buf = new byte[1024];
		int len;

		String receiveFile = "c:\\" + ftpfile;
		Socket download_socket = getDataSocket(new String[] { "RETR " + ftpfile }, false);
		BufferedInputStream indata = 	new BufferedInputStream(download_socket.getInputStream());
		FileOutputStream fos = new FileOutputStream(receiveFile);
		while ((len = indata.read(buf)) > 0) {
			fos.write(buf, 0, len);
		}
		indata.close();
		fos.close();
		getStatusDisplay();
		Log.info("�ٿ�ε� ����", this);
	}

	public void putFile(String ftpfile, String localfile, boolean transferMode)
		throws Exception {
		Socket upload_socket = null;

		if (out == null)
			return;

		if (transferMode == true) {
			setTransferMode(true);
		} else {
			setTransferMode(false);
		}
		getStatusDisplay();

		try {
			upload_socket = getDataSocket(new String[] { "STOR " + ftpfile }, false);
			BufferedOutputStream writer = new BufferedOutputStream(upload_socket.getOutputStream());
			RandomAccessFile fin = new RandomAccessFile(localfile, "r");
			outData(writer, fin);
			writer.close();
			fin.close();
			getStatusDisplay();
			if (!status.substring(0, 3).equals("226")) {
				throw new IOException(); // transfer incomplete
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			if (upload_socket != null)
				upload_socket.close();
			Log.info("���ε� ����", this);
		}
	}

	private void outData(BufferedOutputStream writer, RandomAccessFile in)
		throws IOException {
		int offset;
		byte[] data = new byte[1024];
		while ((offset = in.read(data, 0, 1024)) != -1) {
			writer.write(data);
		}
	}

	/**
	  Socket�� close�ϴ� Function
	*/
	public void close() throws IOException {
		exeCommand("QUIT");

		if (!(status.substring(0, 3).equals("221")	|| 
			(status.substring(0, 3).equals("226")))) {
			throw new IOException();
		}

		if (in != null)
			in.close();
		if (out != null)
			out.close();
		if (socket != null)
			socket.close();

		Log.info("******����������  Socket close******", this);
	}
}
