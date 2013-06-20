package neo.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import neo.util.comm.StringUtil;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	FileUtil.java
 * 	@���ϼ���		: 	
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
public class FileUtil {
	public static String fileName = "";
	public static boolean ismove = false;
	DataOutputStream dos;

	public static final Pattern dosSeperator = Pattern.compile("\\\\");

	public static final Pattern lastSeperator = Pattern.compile("/$");

	public static String replaceSeparator(String strFileFullPath) {
		if (strFileFullPath == null)
			return strFileFullPath;
		else
			return System.getProperty("os.name", "Windows").startsWith("Windows")
				? strFileFullPath.replace('/', File.separatorChar)
				: strFileFullPath.replace('\\', File.separatorChar).replace('/',File.separatorChar);
	}

	public static String getFileNameChop(String fullpath) {
		if (null == fullpath)
			return null;
		fullpath = dosSeperator.matcher(fullpath).replaceAll("/");
		int pos = fullpath.lastIndexOf("/");
		if (pos > -1)
			return fullpath.substring(pos + 1);
		return fullpath;
	}

	public static String getFilePathChop(String fullpath) {
		if (null == fullpath)
			return null;
		fullpath = dosSeperator.matcher(fullpath).replaceAll("/");
		int pos = fullpath.lastIndexOf("/");
		if (pos > -1)
			return fullpath.substring(0, pos + 1);
		else
			return "./";
	}

	public static String getCompleteLeadingSeperator(String fullpath) {
		if (null == fullpath)
			return null;
		fullpath = dosSeperator.matcher(fullpath).replaceAll("/");
		if (!fullpath.endsWith(File.separator))
			fullpath += "/";
		return fullpath;
	}

	public static String getRemoveLeadingSeperator(String fileName) {
		if (null == fileName)
			return null;
		fileName = dosSeperator.matcher(fileName).replaceAll("/");
		fileName = lastSeperator.matcher(fileName).replaceAll("");
		return fileName;
	}

	public static String getFilesizeString(long size) {
		String tail = "";
		if (1024 > size) {
			tail = "byte";
		} else if (1048576 > size) {
			size = size / 1024;
			tail = "Kb";
		} else if (1073741824 > size) {
			size = size / 1024;
			tail = "Mb";
		}
		return new DecimalFormat("#,###.00").format(size) + tail;
	}

	public static void copyFile(File from, File to) throws IOException {
		InputStream in = null;
		OutputStream out = null;

		try {
			in = new FileInputStream(from);
		} catch (IOException ex) {
			throw new IOException("Utilities.copyFile: opening input stream '"	+ from.getPath()	+ "', " + ex.getMessage());
		}

		try {
			out = new FileOutputStream(to);
		} catch (Exception ex) {
			try {
				in.close();
			} catch (IOException ex1) {
			}
			throw new IOException("Utilities.copyFile: opening output stream '" + to.getPath() + "', " + ex.getMessage());
		}

		copyInputToOutput(in, out, from.length());
	}

	public static void copyInputToOutput(
		InputStream input,
		OutputStream output,
		long byteCount)
		throws IOException {
		int bytes;
		long length;

		BufferedInputStream in = new BufferedInputStream(input);
		BufferedOutputStream out = new BufferedOutputStream(output);

		byte[] buffer;
		buffer = new byte[8192];

		for (length = byteCount; length > 0;) {
			bytes = (int) (length > 8192 ? 8192 : length);

			try {
				bytes = in.read(buffer, 0, bytes);
			} catch (IOException ex) {
				try {
					in.close();
					out.close();
				} catch (IOException ex1) {
				}
				throw new IOException("Reading input stream, " + ex.getMessage());
			}

			if (bytes < 0)
				break;

			length -= bytes;

			try {
				out.write(buffer, 0, bytes);
			} catch (IOException ex) {
				try {
					in.close();
					out.close();
				} catch (IOException ex1) {
				}
				throw new IOException("Writing output stream, " + ex.getMessage());
			}
		}

		try {
			in.close();
			out.close();
		} catch (IOException ex) {
			throw new IOException("Closing file streams, " + ex.getMessage());
		}
	}

	public static void copyInputToOutput(
		InputStream input,
		OutputStream output)
		throws IOException {
		BufferedInputStream in = new BufferedInputStream(input);
		BufferedOutputStream out = new BufferedOutputStream(output);
		byte buffer[] = new byte[8192];
		for (int count = 0; count != -1;) {
			count = in.read(buffer, 0, 8192);
			if (count != -1)
				out.write(buffer, 0, count);
		}

		try {
			in.close();
			out.close();
		} catch (IOException ex) {
			throw new IOException("Closing file streams, " + ex.getMessage());
		}
	}

	public static String encodePassword(String password, String algorithm) {
		byte[] unencodedPassword = password.getBytes();

		MessageDigest md = null;

		try {
			// first create an instance, given the provider
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception e) {
			return password;
		}

		md.reset();

		// call the update method one or more times
		// (useful when you don't know the size of your data, eg. stream)
		md.update(unencodedPassword);

		// now calculate the hash
		byte[] encodedPassword = md.digest();

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < encodedPassword.length; i++) {
			if (((int) encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}

			buf.append(Long.toString((int) encodedPassword[i] & 0xff, 16));
		}

		return buf.toString();
	}
	
	public static int copy(File source, File dest) throws IOException {
		File parentFile = null;

		if (source.isDirectory()) {
			if (dest.isDirectory()) {

			} else if (dest.exists()) {
				throw new IOException("�̹� �����ϴ������Դϴ�. --> '" + dest + "'.");
			} else {
				dest = new File(dest, source.getName());
				dest.mkdirs();
			}
		}
		if (source.isFile() && dest.isDirectory()) {
			parentFile = new File(dest.getAbsolutePath());
			dest = new File(dest, source.getName());
		} else {
			parentFile = new File(dest.getParent());
		}
		parentFile.mkdirs();

		if (!source.canRead()) {
			throw new IOException("Cannot read file '" + source + "'.");
		}

		if (dest.exists() && (!dest.canWrite())) {
			throw new IOException("Cannot write to file '" + dest + "'.");
		}
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
		byte buffer[] = new byte[1024];
		int read = -1;
		int fileSize = 0;
		while ((read = in.read(buffer, 0, 1024)) != -1) {
			fileSize = fileSize + read;
			out.write(buffer, 0, read);
		}
		out.flush();
		out.close();
		in.close();
		return fileSize;
	}

	public static void fileCreate(String content, String fileName)
		throws IOException {
		//���ڿ��� �����Ѵ�. 
		String str = content;
		// �ؽ�Ʈ�� ���Ϸ� ���� �� �ֵ��� FileWriter�� �����. 
		FileWriter fw = new FileWriter(fileName);
		// �� ���� �� ���ھ� ���Ϸ� ����Ѵ�. 
		for (int i = 0; i < str.length(); i++) {
			fw.write(str.charAt(i));
		}
		// ������ �ݴ´�. 
		fw.close();
	}

	/**
	 * ������ �����Ѵ�
	 * 
	 * @param path : �����н�
	 * 				 fname : �����̸�
	 * @return File : ���ϰ�ü , null : ���� ����
	 */
	public static File getFile(String path, String fname) {
		try {
			if (existFile(path, fname)) {
				File f = new File(path, fname);
				return f;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * ���丮�� �ִ��� Ȯ���Ѵ�
	 * 
	 * @param path : �����н�
	 * @return true:���丮 ����, false:���丮����
	 */
	public static boolean existDirectory(String path) {
		boolean i = false;
		try {			
			File f = new File(path);
			i = f.exists();
		} catch (Exception e) {
			i = false;
		}
		return i;
	}
	
	/**
	 * ������ �����ϴ��� Ȯ���Ѵ�
	 * 
	 * @param path : �����н�
	 * 				 filename : �����̸�
	 * @return true:��������,false:���Ͼ���
	 */
	public static boolean existFile(String path, String filename) {
		boolean i = false;

		try {
			File f = new File(path, filename);
			i = f.exists();
		} catch (Exception e) {
			i = false;
		}
		Log.info("i -> " + i, null);
		return i;
	}
	
	/**
	 * ���丮�� �����Ѵ�
	 * 
	 * @param path : �����н�
	 * @return true:����,false:����
	 */
	/**
	 * @title		���丮�� �����Ѵ�
	 * 
	 * @param	[1] path �����н�
	 * @return	true:����,false:����
	 */
	public static boolean createDirectory(String path) {
		boolean i = false;
		try {
			System.out.println("������ ���丮�� �����. :: "+path);
			File f = new File(path);
			i = f.mkdir();
		} catch (Exception e) {
			i = false;
		}
		return i;
	}
	
	/**
	 * ������ �����Ѵ�
	 * 
	 * @param path : �����н�
	 * 				 name : �����̸�
	 * @return ���� ���� �������θ� �����Ѵ�
	 */
	public static boolean delFile(String path, String name) {
		boolean i = false;
		try {
			File f = new File(path + name);
			if (existFile(path, name)) {
				f.delete();
				i = true;
			}
		} catch (Exception e) {
			i = false;
		}
		return i;
	}
	
	/**
	 * ���丮�� �����Ѵ�
	 * 
	 * @param path : �����н�
	 * @return ���丮 ���� �������� ����
	 */
	public static boolean delDirectory(String path) {
		boolean i = false;
		try {
			File f = new File(path);
			if (existDirectory(path)) {
				f.delete();
				i = true;
			}
		} catch (Exception e) {
			i = false;
		}
		return i;
	}
	
	/**
	 * ����ũ�⸦ �����Ѵ�
	 * 
	 * @param path : �����н�
	 * 				 name : �����̸�
	 * @return ���ϻ�����
	 */
	public static long getFileSize(String path, String name) {
		long i = 0;
		if (name.equals("")
			|| name == null
			|| name.toUpperCase().equals("NULL"))
			return 0;
		try {
			if (existFile(path, name)) {
				File f = new File(path + "/" + name);
				i = f.length();
			} else {
				Log.info("������ �� ����!" + path + name, null);
				i = 0;
			}
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}
	
	/**
	 * �����̸��� �����Ѵ�
	 * 
	 * @param file : ����
	 * @return �����̸�
	 */
	public static String getFileName(File file) {
		String FName = "";
		try {
			if (file == null)
				return "";
			if (file.exists()) {
				FName = file.getName();
			} else {
				FName = "";
			}
		} catch (Exception e) {
			FName = "";
		}
		return FName;
	}
	
	/**
	 * ���丮�� �����Ѵ�
	 * 
	 * @param path : ���� �����н�
	 * @return ���� ���� ���� 
	 */
	public static boolean delDir(String path) {
		try {
			if (existDirectory(path)) {
				//���� ���丮 ��ü�� �����Ѵ�.
				File f = new File(path);

				//���丮 ���� �۾��� �����Ѵ�.
				delDir(f.listFiles());

				//���������� ��Ʈ ���丮�� �����.
				if (ismove) {
					ismove = f.delete();
				}
			}
		} catch (Exception e) {
			ismove = false;
		}
		return ismove;
	}
	
	/**
	 * ������ ������� Ȯ���ϴ� �޼ҵ�
	 * 
	 * @param fl[] : ���ϰ� ���丮 ���
	 */
	public static void delDir(File fl[]) throws Exception {
		try {
			for (int j = 0; ismove && j < fl.length; j++) {
				if (fl[j].isDirectory()) {
					//�ڽ��� �ٽ� ȣ���Ѵ�.
					delDir(fl[j].listFiles());
					//���丮�� �����.
					ismove = fl[j].delete();
				} else {
					//������ �����.
					ismove = fl[j].delete();
				}
			}
			ismove = true;

		} catch (Exception e) {
			ismove = false;
		}
	}
	
	/**
	 * ���丮 �̵�/�����Ų��(������ �н��� ��Ʈ �н��� �ȴ�)
	 * 
	 * @param oldPath : ���� �����н�
	 * 				 newPath : �̵��� �����н�
	 * 				 cp : 'cp' ī��, 'mv' �̵�
	 * @return ������ ����̸� true, �ƴϸ� false
	 */
	public static boolean moveDir(String oldPath, 
									String newPath, 
									String cp) {
		String newDirectory = "";
		int i = 0;
		try {
			if (existDirectory(oldPath)) {
				i = oldPath.lastIndexOf(File.separator);
				//����/�̵��� ��Ʈ ���丮���� ã�´�.
				newDirectory = oldPath.substring(i + 1);

				//���� ���丮 ��ü�� �����Ѵ�.
				File oldFile = new File(oldPath);
				//�����н� �ڿ� separator�� �ִ��� Ȯ���Ѵ�.
				newPath = chkSeparator(newPath);
				newPath = newPath + newDirectory;

				//�������� ��Ʈ���丮�� �����.
				createDirectory(newPath);
				//����/�̵� �۾��� �����Ѵ�.
				mvDir(oldFile.listFiles(), newPath, newDirectory, cp);

				//�̵��̸� ���������� ���� ��Ʈ ���丮�� �����.
				if (cp.equals("mv")) {
					delDirectory(oldPath);
				}
			}
		} catch (Exception e) {
			ismove = false;
		}
		return ismove;
	}
	
	/**
	 * ���丮 �̵�/���� ��Ų��
	 * 
	 * @param fl[] : ���ϰ� ���丮 ���
	 * 				 target : �̵��� ��ġ�� root�н�
	 * 				 root : ���� ������ �ֻ��� ���丮
	 * 				 cp : 'cp' ī��, 'mv' �̵�
	 */
	public static void mvDir(File fl[],
								String target,
								String root,
								String cp) {
		String currentName = "";
		String newp = "";
		int idx = 0;
		try {
			for (int j = 0; j < fl.length; j++) {
				if (fl[j].isDirectory()) {
					//���� ���丮���� ���Ѵ�.
					currentName = fl[j].getAbsolutePath();
					idx = currentName.indexOf(root);
					currentName = currentName.substring(idx + root.length() + 1);

					//�� ���丮�н��� �����
					//�����н� �ڿ� separator�� �ִ��� Ȯ���Ѵ�.
					target = chkSeparator(target);
					newp = target + currentName;
					//�� ���丮�� �����Ѵ�.
					ismove = createDirectory(newp);

					//�ڽ��� �ٽ� ȣ���Ѵ�.
					mvDir(fl[j].listFiles(), target, root, cp);
					//�̵��϶� ���丮�� �����.
					if (cp.equals("mv")) {
						fl[j].delete();
					}
				} else {
					//������ ���ϸ��� ���Ѵ�.
					fileName = fl[j].getName();
					//������ ����� �н��� ã�´�.
					idx = fl[j].getAbsolutePath().indexOf(root);
					String temppath = fl[j].getAbsolutePath().substring(idx + root.length(),
																							fl[j].getAbsolutePath().length() - (fileName.length() + 1));

					//������ �����ϴ� �޼��� ȣ��
					//target�� ����� ������ root�̸� ���⿡ temppath���̸� ����
					//�� ����� ��ġ�� �ȴ�.
					if (cp.equals("cp")) {
						//���� ����
						copyFile(fl[j], target + temppath, cp);
					} else {
						//���� �̵�
						moveFile(fl[j], target + temppath);
					}
				}

			}
			ismove = true;
		} catch (Exception e) {
			ismove = false;
		}
	}
	
	/**
	 * ���丮 ���� ���ϵ��� ������ ���丮�� �̵�/�����Ų��
	 * 
	 * @param oldPath : ���� �����н�(d:\\xxx\zzz\)
	 * 				 newPath : �̵��� �����н�(d:\\xxx\yyy\)
	 * 				 cp : 'cp' ī��, 'mv' �̵�
	 * @return true:�۾�����, false:�۾�����
	 */
	public static boolean copyDir(String oldPath, String newPath, String cp) {
		boolean ismove = true;
		try {

			if (existDirectory(oldPath)) {
				//���� ���丮 ��ü�� �����Ѵ�.
				File oldFile = new File(oldPath);

				//���� ���丮���� ���� ����Ʈ�� �����´�.
				File f2[] = oldFile.listFiles();

				//�������� ���丮�� �����.
				createDirectory(newPath);

				if (f2 != null && f2.length > 0) {
					for (int j = 0; j < f2.length; j++) {
						//������ ������ŭ ������ �����Ѵ�.
						copyFile(f2[j], newPath, cp);
						Log.info("���� " + j + "/" + f2.length + " ������ �Դϴ�", null);
					}
				}
			}
		} catch (Exception e) {
			ismove = false;
		}
		return ismove;
	}
	
	/**
	 * ÷�������� �̵���Ų��
	 * 
	 * @param file : ����
	 * 				 path : ������ ����� �н�
	 */
	public static void moveFile(File file, String path) throws Exception {
		String fileName2 = "";
		try {
			//�����н� �ڿ� separator�� �ִ��� Ȯ���Ѵ�.
			path = chkSeparator(path);

			//������ ���� ���丮�� �ִ��� Ȯ���Ѵ�.
			if (!existDirectory(path)) {
				//���丮�� ������ �����.
				createDirectory(path);
			}

			if (existFile(path, fileName)) {
				//������ �����Ϸ��� ���� ���� �̸��� ������ ������ ���� �����.
				fileName2 = makeFileName(path, fileName);

				while (existFile(path, fileName2)) {
					//���� �̸��� ��ġ�� ����ؼ� �� �̸��� �����.
					fileName2 = makeFileName(path, fileName2);
				}
			} else {
				fileName2 = fileName;
			}

			File file2 = new File(path, fileName2);

			file.renameTo(file2); //������ �̵���Ų��.

		} catch (Exception e) {
		}
	}
	
	/**
	 * ÷�������� �����Ų��
	 * 
	 * @param file : ����
	 * 				 path : ������ ����� �н�
	 * 				 cp : 'cp' ī��, 'mv' �̵�
	 */
	public static void copyFile(File file, String path, String cp) {
		String fileName2 = "";

		try {
			//�ѱ� ������ ��� �Ʒ��� ���� �ؾ� �ѱ��� ������ �ʴ´�.
			//fileName = new String(file.getName().getBytes("euc-kr"),"8859_1");
			fileName = file.getName();

			//�����н� �ڿ� separator�� �ִ��� Ȯ���Ѵ�.
			path = chkSeparator(path);

			//������ �� �迭�� ����� ũ�� ����
			/*
			byte[] bytestream = new byte[(int)file.length()];
			
			FileInputStream filestream = new FileInputStream(file);
			
			int i = 0, j = 0;
			
			while((i = filestream.read()) != -1) {
			  bytestream[j] = (byte)i;
			  j++;
			}
			*/

			/*
			//���� Ȯ���ڷ� �ɷ����� �ʹٸ�...
			File[] fileNames = f.listFiles(new FilenameFilter(){
			    public boolean accept(File dir, String name){
			      return name.endsWith(".exe");
			    }
			});
			 */

			//������ ���� ���丮�� �ִ��� Ȯ���Ѵ�.
			if (!existDirectory(path)) {
				//���丮�� ������ �����.
				createDirectory(path);
			}

			if (existFile(path, fileName)) {
				fileName2 = makeFileName(path, fileName);
				while (existFile(path, fileName2)) {
					//���� �̸��� ��ġ�� ����ؼ� �� �̸��� �����.
					fileName2 = makeFileName(path, fileName2);
				}

			} else {
				fileName2 = fileName;
			}

			//����� ���� ��ü�� �����.
			//File file2 = new File(path +fileName);

			FileInputStream filestream = new FileInputStream(file);
			FileOutputStream outStream = new FileOutputStream(path + fileName2);

			byte[] b = new byte[1024];
			int numRead = filestream.read(b);

			while (numRead != -1) {
				outStream.write(b, 0, numRead);
				numRead = filestream.read(b);
			}

			outStream.flush();
			outStream.close();
			if (filestream != null)
				filestream.close();

			//��� ��Ʈ���� �����Ѵ�.
			/*
			FileOutputStream outStream = new FileOutputStream(path + fileName2);
			
			//������ ����Ѵ�.
			outStream.write(bytestream);
			
			//��½�Ʈ���� �ݴ´�.
			outStream.close();
			bytestream = null;
			if(filestream != null) filestream.close();
			*/

			//�̵��ϰ�� ������ ������ �����Ѵ�.
			if (cp.equals("mv")) {
				file.delete();
			}
		} catch (Exception e) {
		} finally {
		}
	}
	
	/**
	 * �ش� ���丮�� ���� �̸��� ������� ���� �̸��� �ٲٴ� �޼��� 
	 * ���ϸ��� �ڿ� _002���� _999 ������ ���δ�.
	 * 
	 * @param fname : ���������̸�
	 * @return ������ �����̸�
	 */
	public static String makeFileName(String path, String fname) {
		String newName = "";
		String tt = "000";
		int i = 0;
		int j = 0;

		try {

			i = fname.lastIndexOf("_");
			j = fname.lastIndexOf(".");

			if (fname.lastIndexOf("_") < 0) {
				//���ʿ��� 001�� �ٿ��ش�.
				newName = fname.substring(0, j) + "_002" + fname.substring(j);
			} else {
				//���� ���ڸ� �߶󳽴�.
				int k = Integer.parseInt(fname.substring(i + 1, j));
				k += 1; //���� ���ڿ� 1�� ���Ѵ�.
				tt = tt + Integer.toString(k);
				//�ڸ����� ���缭 ������ �����.
				tt = "_" + tt.substring(tt.length() - 3);

				//�� ���� �̸��� �����.
				newName = fname.substring(0, i) + tt + fname.substring(j);
			}

		} catch (Exception e) {
			try {
				//�� ���� �̸��� �����.
				newName = fname.substring(0, j) + "_002" + fname.substring(j);
			} catch (Exception ee) {
				newName = fname;
			}

		}
		return newName;
	}
	
	/**
	 *���� �н� �ڿ� \ Ȥ�� / �� ������ �ٿ��ִ� �޼���
	 * 
	 * @param path : �����н�
	 * @return ������ ���� �н�
	 */
	public static String chkSeparator(String path) {
		String tempp = "";
		try {
			tempp = path;
			if (!tempp.substring(tempp.length() - 1, tempp.length()).equals(File.separator)) {
				tempp = tempp + File.separator;
			}
		} catch (Exception e) {
		}
		return tempp;
	}

	public static String getFileSizeStr(String filesize) {
		return getFileSize2(StringUtil.strToDouble(filesize));
	}

	public static String getFileSize2(double filesize) {
		DecimalFormat df = new DecimalFormat(".##");
		String fSize = "";
		if ((filesize > 1024) && (filesize < 1024 * 1024)) {
			fSize = df.format((float) filesize / 1024).toString() + " KB";
		} else if (filesize >= 1024 * 1024) {
			fSize = df.format((float) filesize / (1024 * 1024)).toString() + " MB";
		} else {
			fSize = filesize + " Bytes";
		}
		return fSize;
	}
	
	/**
	 * File���� Ư�� ������ string�� �о �ٸ� ���ڷ� ��ġ
	 */
	public static void file_str_chg(String org, String target) {
		// �������ϰ��
		String fileName = org;

		// file ��ü ����
		File inputFile = new File(fileName);
		File outputFile = new File(target);

		FileInputStream fileInputStream = null;
		BufferedReader bufferedReader = null;
		FileOutputStream fileOutputStream = null;
		BufferedWriter bufferedWriter = null;

		boolean result = false;

		try {
			// FileInputStream,FileOutputStream, BufferdReader, BufferedWriter ����
			fileInputStream = new FileInputStream(inputFile);
			fileOutputStream = new FileOutputStream(outputFile);

			bufferedReader =
				new BufferedReader(new InputStreamReader(fileInputStream));
			bufferedWriter =
				new BufferedWriter(new OutputStreamWriter(fileOutputStream));

			// ���� ���Ͽ��� �о� ���̴� �Ѷ���
			String line;
			// ���Ͽ� ��ġ�ϴ� ���ڷ� ��ü�ϰ� �� ���� string
			String repLine;

			// �ٲٰ��� �ϴ� string�� �ٲ� sting ����
			String originalString = "crm.URL=A";
			String replaceString = "crm.URL=D";

			// ���� ���Ͽ��� �Ѷ��ξ� �д´�.
			while ((line = bufferedReader.readLine()) != null) {
				// ��ġ�ϴ� ���Ͽ����� �ٲ� ���ڷ� ��ȯ
				repLine = line.replaceAll(originalString, replaceString);

				// ���ο� ���Ͽ� ����.
				bufferedWriter.write(repLine, 0, repLine.length());
				bufferedWriter.newLine();
			}
			// ���������� ����Ǿ����� �˸��� flag
			result = true;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// ���ҽ� ����. ���������� �����Ѵ�.
			try {
				bufferedReader.close();
			} catch (IOException ex1) {
				ex1.printStackTrace();
			}
			try {
				bufferedWriter.close();
			} catch (IOException ex2) {
				ex2.printStackTrace();
			}

			// ���������� ����Ǿ��� ��� ���� ������ ����� ���ο� ���ϸ��� �������ϸ����� rename�Ѵ�.
			if (result) {
				inputFile.delete();
				outputFile.renameTo(new File(fileName));
			}
		}
	}
	
	/**
	 * File�� �ٿ�ε� �Ѵ�.
	 */
	public static boolean download(	HttpServletResponse httpservletresponse,
										InputStream inputstream,
										String strFileName,
										String strContentType) {
		javax.servlet.ServletOutputStream servletoutputstream = null;
		try {
			boolean flag1;
			try {
				strFileName = new String(strFileName.getBytes("KSC5601"), "8859_1");
				servletoutputstream = httpservletresponse.getOutputStream();
				httpservletresponse.setHeader("Content-Type", strContentType == null	? "application/octet-stream" : strContentType);
				httpservletresponse.setHeader("Content-Disposition", "attachment; filename=\"" + strFileName + "\"");
				byte abyte0[] = new byte[512];
				int i;
				while ((i = inputstream.read(abyte0)) != -1)
					servletoutputstream.write(abyte0, 0, i);
				servletoutputstream.flush();
				boolean flag2 = true;
				boolean flag3 = flag2;
				return flag3;
			} catch (IOException ioexception) {
				boolean flag = false;
				flag1 = flag;
			}
			return flag1;
		} finally {
			try {
				if (servletoutputstream != null)
					servletoutputstream.close();
			} catch (IOException ioexception1) {
			}
		}
	}
	
	/**
	 * File�� �ٿ�ε� �Ѵ�.
	 */
	public static boolean downloadPDF( HttpServletResponse httpservletresponse,
											InputStream inputstream,
											String strFileName,
											String strContentType) {
		javax.servlet.ServletOutputStream servletoutputstream = null;
		try {
			boolean flag1;
			try {

				// ��� �ݿ� : Characterset : KSC5601 --> 8859-1
				strFileName = new String(strFileName.getBytes("KSC5601"), "8859_1");
				servletoutputstream = httpservletresponse.getOutputStream();
				httpservletresponse.setHeader("Content-Type",	strContentType == null
																					? "application/pdf"
																					: strContentType);
				httpservletresponse.setHeader("Content-Disposition",	"attachment; filename=\"" + strFileName + "\"");
				byte abyte0[] = new byte[512];
				int i;
				while ((i = inputstream.read(abyte0)) != -1)
					servletoutputstream.write(abyte0, 0, i);
				servletoutputstream.flush();
				boolean flag2 = true;
				boolean flag3 = flag2;
				return flag3;
			} catch (IOException ioexception) {
				boolean flag = false;
				flag1 = flag;
			}
			return flag1;
		} finally {
			try {
				if (servletoutputstream != null)
					servletoutputstream.close();
			} catch (IOException ioexception1) {
			}
		}
	}
	
	/**
	 * File�� �ٿ�ε� �Ѵ�.
	 */
	public static boolean download( HttpServletResponse httpservletresponse,
										String strSrc,
										String strFileName,
										String strContentType) {
		ByteArrayInputStream bytearrayinputstream = null;
		try {
			bytearrayinputstream = new ByteArrayInputStream(strSrc.getBytes());
			boolean flag = download(httpservletresponse,
												bytearrayinputstream,
												strFileName,
												strContentType);
			boolean flag1 = flag;
			return flag1;
		} finally {
			try {
				if (bytearrayinputstream != null)
					bytearrayinputstream.close();
			} catch (IOException ioexception) {
			}
		}
	}
	
	/**
	 * File�� �ٿ�ε� �Ѵ�.
	 */
	public static boolean download( HttpServletResponse httpservletresponse,
										File file,
										String strFileName,
										String strContentType) {
		FileInputStream fileinputstream = null;
		try {
			boolean flag2;
			try {
				fileinputstream = new FileInputStream(file);
				boolean flag =
					download(httpservletresponse,
									fileinputstream,
									strFileName == null ? file.getName() : strFileName,
									strContentType);
				boolean flag1 = flag;
				return flag1;
			} catch (IOException ioexception) {
				flag2 = false;
			}
			boolean flag3 = flag2;
			return flag3;
		} finally {
			try {
				if (fileinputstream != null)
					fileinputstream.close();
			} catch (IOException ioexception1) {
			}
		}
	}
	
	/**
	 * �ش� ������ Ȯ���ڸ� ��´�. �ش� ������ ���丮 �Ǵ� Ȯ���ڰ� ���� ��� null �� ����.
	 * 
	 * @param file : ����
	 * @return ���� Ȯ���� (�ҹ���)
	 */
	public static String getExtension(File file) {
		if (file.isDirectory())
			return null;
		else
			return getExtension(file.getName());
	}
	
	/**
	 * �ش� ����(���� ���)�� Ȯ���ڸ� ��´�. �ش� ���� ��ο� Ȯ���ڰ� ���� ���('.' �� ���� ���) null �� ����.
	 * 
	 * @param strFileFullPath : ���� ���
	 * @return ���� Ȯ���� (�ҹ���)
	 */
	public static String getExtension(String strFileFullPath) {
		try {
			return strFileFullPath
				.substring(strFileFullPath.lastIndexOf(".") + 1)
				.toLowerCase();
		} catch (Exception exception) {
			return null;
		}
	}
	
	/**
	 * �ѱ۷� �ۼ��� XML ������ UTF-8�� �ٲ� ������ ���� �Ѵ�. 
	 */
	public static String writeFile_UTF_8( String fileName,
											String content,
											String path) {
		String strResult = "0"; // ó�����

		String strFilePath = path + File.separator + fileName;
		File file = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		PrintWriter pw = null;
		try {
			file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			file = new File(strFilePath);
			if (!file.exists()) {
				file.createNewFile();
			}

			fos = new FileOutputStream(strFilePath);
			osw = new OutputStreamWriter(fos, "UTF-8");

			bw = new BufferedWriter(osw);
			pw = new PrintWriter(bw);
			pw.print(content);
		} catch (Exception ie) {
			ie.printStackTrace();
			strResult = "-1";
		} finally {
			try {
				if (pw != null)
					pw.close();
				if (bw != null)
					bw.close();
				if (osw != null)
					osw.close();
				if (fos != null)
					fos.close();
			} catch (Exception except) {
				except.printStackTrace();
			}
		}
		return strResult;
	}
	
	/**
	 * �Ϲ� ���� ���� 
	 */
	public static String writeFile( String fileName,
										String content,
										String path) {
		String strResult = "0"; // ó�����

		File file = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			file = new File(path + fileName);
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			bw.write(content);
		} catch (Exception e) {
			e.printStackTrace();
			strResult = "-1";
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (Exception except) {
				except.printStackTrace();
			}
		}
		return strResult;
	}
	
	/**
	 * File ���� ����
	 * 
	 * @param file : File ��
	 * @return ���翩��
	 */
	public static boolean exists(File file) {
		return file.exists();
	}
	
	/**
	 * File ���� ����(File Full Path�̿�)
	 * 
	 * @param strFileFullPath : File Full Path
	 * @return ���翩��
	 */
	public static boolean exists(String strFileFullPath) {
		return exists(new File(strFileFullPath));
	}
	
	/**
	 * File ���� ����(document Root �Ʒ��� File)
	 */
	public static boolean exists(ServletContext servletcontext,
									String strFileName) {
		String strFileFullPath = servletcontext.getRealPath(strFileName);
		return strFileFullPath == null ? false : exists(strFileFullPath);
	}
	
	/**
	 * File ���� ����
	 */
	public static boolean exists(String strFilePath, String strFileName) {
		return exists(
			new File(strFilePath + (strFilePath.endsWith(File.separator) ? "" : File.separator) + (strFileName == null ? "" : strFileName)));
	}
	
	/**
	 * ���� Ȯ���� �̱�
	 */
	public static String getExtensionOfFile(String strFileName) {
		int i = strFileName.lastIndexOf("."); 
		return strFileName.substring(i+1);
	}
	
	/**
	 * ���丮 �����ϱ�
	 */
	public void creatZipFile1() throws Exception {
		  

	    File f = new File("D:/file/fileup");
	  
	    String path = "D:/file/fileup";

	    String files[] = f.list(); // f object �� �ִ� ���ϸ��
	    
	    // Create a buffer for reading the files
	    byte[] buf = new byte[1024];
	    
	    try {

	        // Create the ZIP file
	        String outFilename = "D:/outfile.zip";
	        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
	    
	        // Compress the files
	        for (int i=0; i<files.length; i++) {
	    
	            FileInputStream in = new FileInputStream( path + "/" + files[i]);
	    
	            // Add ZIP entry to output stream.
	            out.putNextEntry(new ZipEntry(files[i])); // Zip ���Ͽ� ��θ� ���Ͽ� �����Ҽ� �ִ�.
	    
	            // Transfer bytes from the file to the ZIP file
	            int len;
	            while ((len = in.read(buf)) > 0) {

	                out.write(buf, 0, len);
	            }
	    
	            // Complete the entry
	            out.closeEntry();
	            in.close();
	        }
	    
	        // Complete the ZIP file
	        out.close();
	    }catch(Exception e) {
	        throw e;
	    }
	}

	/**
	 * ���丮 �����ϱ�
	 */
	public void creatZipFile2() throws Exception {
	  

	    File f = new File("D:/file/fileup");


	    int size = 1024;
	    String path = "D:/file/fileup";

	    String files[] = f.list(); // f object �� �ִ� ���ϸ��


	    // Create a buffer for reading the files
	    byte[] buf = new byte[size];
	    try {
	        // Create the ZIP file
	        String outFilename = "D:/outfile.zip";
	        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFilename)));

	        // Compress the files
	        for (int i=0; i<files.length; i++) {
	    
	            FileInputStream fs = new FileInputStream( path + "/" + files[i]);

	            BufferedInputStream in = new BufferedInputStream(fs, size);


	            // Add ZIP entry to output stream.
	            out.putNextEntry(new ZipEntry(files[i])); // Zip ���Ͽ� ��θ� ���Ͽ� �����Ҽ� �ִ�.
	    
	            // Transfer bytes from the file to the ZIP file
	            int len;
	            while ((len = in.read(buf, 0, size)) > 0) {

	                out.write(buf, 0, len);
	            }
	    
	            // Complete the entry
	            out.closeEntry();
	            in.close();
	        }
	    
	        // Complete the ZIP file
	        out.close();
	    }catch(Exception e) {

	        throw e;
	    }
	}
	
	//Java�� ������ ���� ����Ʈ �������� ��� 
	//���� �������� ���α׷� ����Ʈ�� task.exe �� Runtime.exec�� ������ �ش� ��Ʈ���� ���ؼ� �����ü� �ִ�.
	//�ش� ���μ����� ���� ������ ������ sc queryex ���μ������̸�  �̶�� �ؾ� �ش� ������ ���� ������ ��µȴ�.
	public void getWindowServiceList() {
		StringBuffer message = new StringBuffer();
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			Process p = Runtime.getRuntime().exec(System.getenv("windir")+"\\system32\\"+"tasklist.exe");
			isr = new InputStreamReader(p.getInputStream());
			br = new BufferedReader(isr);
	   
			String line = null;
			while ((line = br.readLine())!= null) {
				System.out.println("msg=>"+line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(isr!=null) try {isr.close(); } catch (IOException e) {}
			if(br!=null) try { br.close();} catch (IOException e) {}
		}
	}
	
	//�ᱹ �ڹ��� Ư���� ������ OS �� �������� �ǰ�����... 
	//�и� �־���� ������ Ŭ�����̴�. Runtime �̶�� Ŭ������ �̿�
	public static void runShellCommand() {
		try {
			String [] cmd = {"cmd.exe","/c","start telnet oxen.konkuk.ac.kr"};
			Process m ;
			String S = "" ;
			m = Runtime.getRuntime().exec(cmd) ;
			BufferedReader in = new BufferedReader(new InputStreamReader(m.getInputStream()));
			while((S=in.readLine()) != null) {
				System.out.println(S) ;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
	    }
	}
	
	//java �� MD5 ���ϱ�
	public static String getMD5SUM(final String strOriginal) {
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(strOriginal.getBytes());

            byte[] digest = algorithm.digest();

            for(int i=0; i< digest.length; i++) {
                hexString.append(Integer.toHexString(0xff & digest[i]));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return hexString.toString();
    }


	/**
	 * @title	 	Ư�� property ���Ͽ��� Ư�� ���� ���´�
	 * 
	 * @param	property ���ϸ� 
	 * 					���� ���� key �̸�
	 * @return 	key�� �ش��ϴ� �� 
	 */
    public static String getProperty( String strName, String strKey ){
        String strValue = "";
        strValue = ResourceBundle.getBundle(strName).getString(strKey);
        return strValue.trim();
    }
    
    /*
	 * Utility method to write a given text to a file
	 */
	public boolean writeToFile(String fileName, String dataLine,
			boolean isAppendMode, boolean isNewLine) {
		if (isNewLine) {
			dataLine = "\n" + dataLine;
		}

		try {
			File outFile = new File(fileName);
			if (isAppendMode) {
				dos = new DataOutputStream(new FileOutputStream(fileName, true));
			} else {
				dos = new DataOutputStream(new FileOutputStream(outFile));
			}

			dos.writeBytes(dataLine);
			dos.close();
		} catch (FileNotFoundException ex) {
			return (false);
		} catch (IOException ex) {
			return (false);
		}
		return (true);

	}

	/*
	 * Reads data from a given file
	 */
	public static String readFromFile(String fileName) {
		String lineAdded = "";
		try {
			File inFile = new File(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
			//DataLine = br.readLine();
			String line = "";			
			while ((line = br.readLine())!= null) {
				lineAdded += line + "\n"; 
			}
			br.close();
		} catch (FileNotFoundException ex) {
			System.out.println("FileNotFoundException occoure...");
			return (null);
		} catch (IOException ex) {
			System.out.println("IOException occoure...");
			return (null);
		}
		return StringUtil.rplc(lineAdded,"\t"," ");

	}

	public static boolean isFileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		return file.delete();
	}

	/*
	 * Reads data from a given file into a Vector
	 */

	public Vector fileToVector(String fileName) {
		Vector v = new Vector();
		String inputLine;
		try {
			File inFile = new File(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
			while ((inputLine = br.readLine()) != null) {
				v.addElement(inputLine.trim());
			}
			br.close();
		} // Try
		catch (FileNotFoundException ex) {
			//
		} catch (IOException ex) {
			//
		}
		return (v);
	}

	/*
	 * Writes data from an input vector to a given file
	 */

	public void vectorToFile(Vector v, String fileName) {
		for (int i = 0; i < v.size(); i++) {
			writeToFile(fileName, (String) v.elementAt(i), true, true);
		}
	}

	/*
	 * Copies unique rows from a source file to a destination file
	 */

	public void copyUniqueElements(String sourceFile, String resultFile) {
		Vector v = fileToVector(sourceFile);
		v = MiscUtil.removeDuplicates(v);
		vectorToFile(v, resultFile);
	}

} // end FileUtil

class MiscUtil {

	public static boolean hasDuplicates(Vector v) {
		int i = 0;
		int j = 0;
		boolean duplicates = false;

		for (i = 0; i < v.size() - 1; i++) {
			for (j = (i + 1); j < v.size(); j++) {
				if (v.elementAt(i).toString().equalsIgnoreCase(v.elementAt(j).toString())) {
					duplicates = true;
				}

			}

		}

		return duplicates;
	}

	public static Vector removeDuplicates(Vector s) {
		int i = 0;
		int j = 0;
		boolean duplicates = false;

		Vector v = new Vector();

		for (i = 0; i < s.size(); i++) {
			duplicates = false;
			for (j = (i + 1); j < s.size(); j++) {
				if (s.elementAt(i).toString().equalsIgnoreCase(s.elementAt(j).toString())) {
					duplicates = true;
				}

			}
			if (duplicates == false) {
				v.addElement(s.elementAt(i).toString().trim());
			}

		}

		return v;
	}

	public static Vector removeDuplicateDomains(Vector s) {
		int i = 0;
		int j = 0;
		boolean duplicates = false;
		String str1 = "";
		String str2 = "";

		Vector v = new Vector();

		for (i = 0; i < s.size(); i++) {
			duplicates = false;
			for (j = (i + 1); j < s.size(); j++) {
				str1 = "";
				str2 = "";
				str1 = s.elementAt(i).toString().trim();
				str2 = s.elementAt(j).toString().trim();
				if (str1.indexOf('@') > -1) {
					str1 = str1.substring(str1.indexOf('@'));
				}
				if (str2.indexOf('@') > -1) {
					str2 = str2.substring(str2.indexOf('@'));
				}

				if (str1.equalsIgnoreCase(str2)) {
					duplicates = true;
				}

			}
			if (duplicates == false) {
				v.addElement(s.elementAt(i).toString().trim());
			}

		}

		return v;
	}

	public static boolean areVectorsEqual(Vector a, Vector b) {
		if (a.size() != b.size()) {
			return false;
		}

		int i = 0;
		int vectorSize = a.size();
		boolean identical = true;

		for (i = 0; i < vectorSize; i++) {
			if (!(a.elementAt(i).toString().equalsIgnoreCase(b.elementAt(i).toString()))) {
				identical = false;
			}
		}

		return identical;
	}

	public static Vector removeDuplicates(Vector a, Vector b) {

		int i = 0;
		int j = 0;
		boolean present = true;
		Vector v = new Vector();

		for (i = 0; i < a.size(); i++) {
			present = false;
			for (j = 0; j < b.size(); j++) {
				if (a.elementAt(i).toString().equalsIgnoreCase(b.elementAt(j).toString())) {
					present = true;
				}
			}
			if (!(present)) {
				v.addElement(a.elementAt(i));
			}
		}

		return v;
	}
}

