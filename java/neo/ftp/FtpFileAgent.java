package neo.ftp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import neo.util.comm.DateUtil;

import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

/**
 * 	@Class Name	: 	FtpFileAgent.java
 * 	@���ϼ���		: 	�ΰ� ���� ���� : FtpFileAgent.java, FileList.java
 * 						Ftp�� �̿��Ͽ� �̾�ޱ� ������ Java�� ������ ������.
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
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
public class FtpFileAgent extends FtpClient {
	private String serverIp;
	private int serverPort;
	private String userName;
	private String userPassword;

	private String remotePath = "/ssolog/sso/netegrity/siteminder/log";
	private String remoteFile = "smaccess.log";
	private String localPath;
	private String localFile;

	public FtpFileAgent() {
		super();
	}

	/**
	 * Ftp ���� ���� ������..
	 */
	public FtpFileAgent(String serverIp, int serverPort, String userName,
			String userPassword) {
		super();
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.userName = userName;
		this.userPassword = userPassword;
	}

	/**
	 * Ftp ���� ���� setter
	 * 
	 * @param serverIp
	 * @param serverPort
	 * @param userName
	 * @param userPassword
	 */
	public void setFtpInfo(String serverIp, int serverPort, String userName,
			String userPassword) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.userName = userName;
		this.userPassword = userPassword;
	}

	/**
	 * Remote ���� �� ���� ����
	 * 
	 * @param r_path
	 * @param r_name
	 */
	public void setRemoteFileInfo(String r_path, String r_name) {
		this.remotePath = r_path;
		this.remoteFile = r_name;
	}

	/**
	 * Local ���� �� ���� ����
	 * 
	 * @param l_path
	 */
	public void setLocalFileInfo(String l_path) {
		this.localPath = l_path;
		this.localFile = this.localPath + this.remoteFile;
	}

	/**
	 * Ftp Server Open..
	 * 
	 * @throws IOException
	 */
	public void openServer() throws IOException {
		openServer(serverIp, serverPort);
		login(userName, userPassword);
	}

	/**
	 * Remote ���� ���� �̵� (cd xx/xx/xx.xx)
	 * 
	 * @throws IOException
	 */
	public void cd() throws IOException {
		super.cd(this.remotePath);
	}

	/**
	 * Remote ���� ���� size ���.
	 * 
	 * @return
	 * @throws IOException
	 */
	public int getRemoteFileSize() throws IOException {
		ArrayList f_list = file_list();
		for (int i = 0; i < f_list.size(); i++) {
			FileList df_list = (FileList) f_list.get(i);
			if (df_list.getFileName().equals(this.remoteFile)) {
				return df_list.getFileSize();
			}
		}
		return 0;
	}

	/**
	 * Remote(FTP ����) ���� list ���..
	 * 
	 * ToDo.. �߰��� ByteArrayOutputStream�� ������ ���� ���� -> ��α� ���� ��ڰ� �ٲ��ֱ� �����..
	 * 
	 * @return
	 * @throws IOException
	 */
	public ArrayList file_list() throws IOException {
		TelnetInputStream file_list = null;
		ByteArrayOutputStream os = null;
		ArrayList rf_list = new ArrayList();

		int n;
		byte[] buff = new byte[1024];
		StringBuffer sb = new StringBuffer();

		try {
			file_list = list();
			while ((n = file_list.read(buff)) > 0) {
				os = new ByteArrayOutputStream(1024);
				os.write(buff, 0, n);
				sb.append(os.toString());
			}

			String[] f_lists = sb.toString().split("\\n");

			for (int i = 0; i < f_lists.length - 1; i++) {
				byte[] cut_str = f_lists[i + 1].getBytes();

				if (cut_str.length > 58) {
					String[] ar_str = new String(cut_str, 37,
							cut_str.length - 37).trim().replaceAll("  ", " ")
							.split(" ");

					FileList df_list = new FileList();
					df_list.setFileType(Integer.parseInt(new String(cut_str,
							10, 5).trim()));
					df_list.setFileSize(Integer.parseInt(ar_str[0]));
					df_list.setFileName(ar_str[4]);

					rf_list.add(df_list);
				}
			}

			return rf_list;
		} finally {
			if (file_list != null) {
				try {
					file_list.close();
				} catch (IOException e) {
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Local File�� Remote ������ �ø���..
	 * 
	 * @throws IOException
	 */
	public void putFile() throws IOException {
		File localInputFile = new File(this.localFile);
		FileInputStream localInputStream = new FileInputStream(localInputFile);

		TelnetOutputStream remoteOutputStream = (TelnetOutputStream) put(this.remoteFile);

		remoteFileWrite(localInputStream, remoteOutputStream);
	}

	/**
	 * Local File�� Remote ������ append �ϱ�.. -> �ӵ� ����..
	 * 
	 * @throws IOException
	 */
	public void appendFile() throws IOException {
		File localInputFile = new File(this.localFile);
		FileInputStream localInputStream = new FileInputStream(localInputFile);

		TelnetOutputStream remoteOutputStream = (TelnetOutputStream) append(this.remoteFile);

		remoteFileWrite(localInputStream, remoteOutputStream);
	}

	/**
	 * FTP File ��� ��, Binary, Ascii ��� ���� -> true : Binary, false : ascii
	 * 
	 * @param binary
	 * @throws IOException
	 */
	public void setBinary(boolean binary) throws IOException {
		if (binary)
			binary();
		else
			ascii();
	}

	/**
	 * Remote File�� Local File�� ����..
	 * 
	 * @throws IOException
	 */
	public void getFile() throws IOException {
		OutputStream localOutputStream = null;
		localOutputStream = new FileOutputStream(this.localFile);
		TelnetInputStream remoteInputStream = (TelnetInputStream) get(this.remoteFile);

		localFileWrite(remoteInputStream, localOutputStream);
	}

	/**
	 * Local File ���� ����..
	 * 
	 * @param remoteInputStream
	 * @param localOutputStream
	 * @throws IOException
	 */
	private void localFileWrite(TelnetInputStream remoteInputStream,
			OutputStream localOutputStream) throws IOException {
		try {
			int n;
			byte[] buff = new byte[1024];

			while ((n = remoteInputStream.read(buff)) > 0) {
				localOutputStream.write(buff, 0, n);
			}
		} finally {
			if (remoteInputStream != null)
				try {
					remoteInputStream.close();
				} catch (Exception e) {
				}

			if (localOutputStream != null)
				try {
					localOutputStream.close();
				} catch (Exception e) {
				}
		}
	}

	/**
	 * Remote File ���� ����..
	 * 
	 * @param localInputStream
	 * @param remoteOutputStream
	 * @throws IOException
	 */
	private void remoteFileWrite(FileInputStream localInputStream,
			TelnetOutputStream remoteOutputStream) throws IOException {

		try {
			int charRead;
			int totalBytes = 0;
			byte[] bytes = new byte[1024];

			while ((charRead = localInputStream.read(bytes)) != -1) {
				totalBytes += charRead;
				remoteOutputStream.write(bytes, 0, charRead);
			}
		} finally {
			if (localInputStream != null)
				try {
					localInputStream.close();
				} catch (Exception e) {
				}

			if (remoteOutputStream != null)
				try {
					remoteOutputStream.close();
				} catch (Exception e) {
				}
		}
	}

	/**
	 * FTP �̾�ޱ� (Remote File -> Local File�� ����)
	 * 
	 * @param mark
	 * @throws IOException
	 */
	public void getRestFile(long mark) throws IOException {
		getRestFile(mark, this.localFile, this.remoteFile);
	}

	/**
	 * FTP �̾�ޱ� ����..
	 * 
	 * @param mark
	 * @param localFile
	 * @param remoteFile
	 * @throws IOException
	 */
	public void getRestFile(long mark, String localFile, String remoteFile)
			throws IOException {
		Socket socket = null;
		TelnetInputStream remoteInputStream = null;
		OutputStream localOutputStream = null;

		try {
			socket = openPassiveDataConnection();
			issueCommand("REST " + mark);
			issueCommand("RETR " + remoteFile);

			remoteInputStream = new TelnetInputStream(socket.getInputStream(),
					true);
			localOutputStream = new FileOutputStream(localFile, true);

			localFileWrite(remoteInputStream, localOutputStream);
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
				}
		}
	}

	/**
	 * Local File size ���..
	 * 
	 * @return
	 * @throws IOException
	 */
	public int getLocalFileSize() throws IOException {
		File localInputFile = new File(this.localFile);
		FileInputStream ls = null;
		int file_size = 0;
		try {
			ls = new FileInputStream(localInputFile);
			file_size = ls.available();
			ls.close();
		} catch (FileNotFoundException fnfe) {
		}
		return file_size;
	}

	/**
	 * Life cycle �Ѿ Local File Rename �����κ�.. �ּ� ó���� Local File Rename.. �����
	 * local file ����..
	 * 
	 * @return
	 */
	public String localFileRename() {
		File targetFile = new File(this.localFile);

		// Local File ����..
		targetFile.delete();

		// Local File Rename;
		// targetFile.renameTo(new File(getLastDayFileName(this.localFile)));

		return getLastDayFileName(this.localFile);
	}

	/**
	 * ���� ��¥ ���..
	 * 
	 * @return
	 */
	public String getLastDay() {		
		return DateUtil.getCurrentDateString();
	}

	/**
	 * ���� ���ڷ� �����̸� �����..
	 * 
	 * @param fileName
	 * @return
	 */
	public String getLastDayFileName(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf('.')) + "."
				+ getLastDay();
	}

	/**
	 * �ָ����� ��...
	 * 
	 * @param local_file_size
	 * @throws IOException
	 */
	public void excuteFtpLastDayFile(int local_file_size) throws IOException {
		String ch_l_file = localFileRename();
		getRestFile(local_file_size, ch_l_file,
				getLastDayFileName(this.remoteFile));
	}

	/**
	 * FTP ȯ�� ���� �� Ftp �̾�ޱ� ���� ����..
	 * 
	 * @throws Exception
	 */
	public void excuteSsoFtp() throws Exception {
		int port = 23;
		String id = "id";
		String password = "pw";
		String server = "192.168.1.100";
		String localPath = "c:/temp"; // local ��������..

		/**
		 * FTP Log in
		 */
		setFtpInfo(server, port, id, password);
		openServer();

		/**
		 * Local File ����
		 */
		setLocalFileInfo(localPath);

		cd();
		setBinary(true);

		int remote_file_size = getRemoteFileSize();
		int local_file_size = getLocalFileSize();

		/**
		 * Remote File�� Local File �� �� Local File�� ���� ��� �Ϸ� ���� ������ ���� File Life
		 * Cycle ������ �Ǵ�.. -> Local File Rename �Ǵ� File delete
		 */
		if (remote_file_size < local_file_size) {
			localFileRename();
			local_file_size = 0;
		}

		getRestFile(local_file_size);
		closeServer();
	}

	public static void main(String[] args) {
		FtpFileAgent dl = new FtpFileAgent();
		try {
			dl.excuteSsoFtp();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
