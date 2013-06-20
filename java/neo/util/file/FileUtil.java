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
 * 	@파일설명		: 	
 * 	@Version			: 	1.0
 *	@Author			: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 	작업일 		버젼	구분	작업자		내용
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		생성	hoon09		source create (삼성전기)
 *	2006-11-23 	1.4		수정	hoon09		code convention apply (멀티캠퍼스)
 *	2009-07-03	1.6		수정	hoon09		code convention apply (국민은행, 펜타시큐리티)
 *	2009-09-23	1.7		수정	hoon09		code valid check (푸르덴샬생명보험,뱅뱅)
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
				throw new IOException("이미 존재하는파일입니다. --> '" + dest + "'.");
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
		//문자열을 선언한다. 
		String str = content;
		// 텍스트를 파일로 보낼 수 있도록 FileWriter를 만든다. 
		FileWriter fw = new FileWriter(fileName);
		// 한 번에 한 문자씩 파일로 출력한다. 
		for (int i = 0; i < str.length(); i++) {
			fw.write(str.charAt(i));
		}
		// 파일을 닫는다. 
		fw.close();
	}

	/**
	 * 파일을 리턴한다
	 * 
	 * @param path : 파일패스
	 * 				 fname : 파일이름
	 * @return File : 파일객체 , null : 파일 없음
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
	 * 디렉토리가 있는지 확인한다
	 * 
	 * @param path : 파일패스
	 * @return true:디렉토리 있음, false:디렉토리없음
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
	 * 파일이 존재하는지 확인한다
	 * 
	 * @param path : 파일패스
	 * 				 filename : 파일이름
	 * @return true:파일존재,false:파일없음
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
	 * 디렉토리를 생성한다
	 * 
	 * @param path : 파일패스
	 * @return true:성공,false:실패
	 */
	/**
	 * @title		디렉토리를 생성한다
	 * 
	 * @param	[1] path 파일패스
	 * @return	true:성공,false:실패
	 */
	public static boolean createDirectory(String path) {
		boolean i = false;
		try {
			System.out.println("새로이 디렉토리를 만든다. :: "+path);
			File f = new File(path);
			i = f.mkdir();
		} catch (Exception e) {
			i = false;
		}
		return i;
	}
	
	/**
	 * 파일을 삭제한다
	 * 
	 * @param path : 파일패스
	 * 				 name : 파일이름
	 * @return 파일 삭제 성공여부를 리턴한다
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
	 * 디렉토리를 삭제한다
	 * 
	 * @param path : 파일패스
	 * @return 디렉토리 삭제 성공여부 리턴
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
	 * 파일크기를 리턴한다
	 * 
	 * @param path : 파일패스
	 * 				 name : 파일이름
	 * @return 파일사이즈
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
				Log.info("파일이 미 존재!" + path + name, null);
				i = 0;
			}
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}
	
	/**
	 * 파일이름을 리턴한다
	 * 
	 * @param file : 파일
	 * @return 파일이름
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
	 * 디렉토리를 삭제한다
	 * 
	 * @param path : 현재 파일패스
	 * @return 삭제 성공 여부 
	 */
	public static boolean delDir(String path) {
		try {
			if (existDirectory(path)) {
				//지울 디렉토리 객체를 생성한다.
				File f = new File(path);

				//디렉토리 삭제 작업을 시작한다.
				delDir(f.listFiles());

				//최종적으로 루트 디렉토리를 지운다.
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
	 * 마지막 노드인지 확인하는 메소드
	 * 
	 * @param fl[] : 파일과 디렉토리 목록
	 */
	public static void delDir(File fl[]) throws Exception {
		try {
			for (int j = 0; ismove && j < fl.length; j++) {
				if (fl[j].isDirectory()) {
					//자신을 다시 호출한다.
					delDir(fl[j].listFiles());
					//디렉토리를 지운다.
					ismove = fl[j].delete();
				} else {
					//파일을 지운다.
					ismove = fl[j].delete();
				}
			}
			ismove = true;

		} catch (Exception e) {
			ismove = false;
		}
	}
	
	/**
	 * 디렉토리 이동/복사시킨다(지정한 패스가 루트 패스가 된다)
	 * 
	 * @param oldPath : 현재 파일패스
	 * 				 newPath : 이동할 파일패스
	 * 				 cp : 'cp' 카피, 'mv' 이동
	 * @return 마지막 노드이면 true, 아니면 false
	 */
	public static boolean moveDir(String oldPath, 
									String newPath, 
									String cp) {
		String newDirectory = "";
		int i = 0;
		try {
			if (existDirectory(oldPath)) {
				i = oldPath.lastIndexOf(File.separator);
				//복사/이동할 루트 디렉토리명을 찾는다.
				newDirectory = oldPath.substring(i + 1);

				//원본 디렉토리 객체를 생성한다.
				File oldFile = new File(oldPath);
				//파일패스 뒤에 separator가 있는지 확인한다.
				newPath = chkSeparator(newPath);
				newPath = newPath + newDirectory;

				//목적지에 루트디렉토리를 만든다.
				createDirectory(newPath);
				//복사/이동 작업을 시작한다.
				mvDir(oldFile.listFiles(), newPath, newDirectory, cp);

				//이동이면 최종적으로 원본 루트 디렉토리는 지운다.
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
	 * 디렉토리 이동/복사 시킨다
	 * 
	 * @param fl[] : 파일과 디렉토리 목록
	 * 				 target : 이동할 위치의 root패스
	 * 				 root : 원본 폴더의 최상위 디렉토리
	 * 				 cp : 'cp' 카피, 'mv' 이동
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
					//원본 디렉토리명을 구한다.
					currentName = fl[j].getAbsolutePath();
					idx = currentName.indexOf(root);
					currentName = currentName.substring(idx + root.length() + 1);

					//새 디렉토리패스를 만든다
					//파일패스 뒤에 separator가 있는지 확인한다.
					target = chkSeparator(target);
					newp = target + currentName;
					//새 디렉토리를 생성한다.
					ismove = createDirectory(newp);

					//자신을 다시 호출한다.
					mvDir(fl[j].listFiles(), target, root, cp);
					//이동일때 디렉토리를 지운다.
					if (cp.equals("mv")) {
						fl[j].delete();
					}
				} else {
					//복사할 파일명을 구한다.
					fileName = fl[j].getName();
					//파일이 복사될 패스를 찾는다.
					idx = fl[j].getAbsolutePath().indexOf(root);
					String temppath = fl[j].getAbsolutePath().substring(idx + root.length(),
																							fl[j].getAbsolutePath().length() - (fileName.length() + 1));

					//파일을 복사하는 메서드 호출
					//target은 복사될 목적지 root이며 여기에 temppath붙이면 파일
					//이 복사될 위치가 된다.
					if (cp.equals("cp")) {
						//파일 복사
						copyFile(fl[j], target + temppath, cp);
					} else {
						//파일 이동
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
	 * 디렉토리 내의 파일들을 지정한 디렉토리로 이동/복사시킨다
	 * 
	 * @param oldPath : 현재 파일패스(d:\\xxx\zzz\)
	 * 				 newPath : 이동할 파일패스(d:\\xxx\yyy\)
	 * 				 cp : 'cp' 카피, 'mv' 이동
	 * @return true:작업성공, false:작업실패
	 */
	public static boolean copyDir(String oldPath, String newPath, String cp) {
		boolean ismove = true;
		try {

			if (existDirectory(oldPath)) {
				//원본 디렉토리 객체를 생성한다.
				File oldFile = new File(oldPath);

				//원본 디렉토리에서 파일 리스트를 가져온다.
				File f2[] = oldFile.listFiles();

				//목적지에 디렉토리를 만든다.
				createDirectory(newPath);

				if (f2 != null && f2.length > 0) {
					for (int j = 0; j < f2.length; j++) {
						//파일의 갯수만큼 파일을 복사한다.
						copyFile(f2[j], newPath, cp);
						Log.info("현재 " + j + "/" + f2.length + " 진행중 입니다", null);
					}
				}
			}
		} catch (Exception e) {
			ismove = false;
		}
		return ismove;
	}
	
	/**
	 * 첨부파일을 이동시킨다
	 * 
	 * @param file : 파일
	 * 				 path : 파일이 저장될 패스
	 */
	public static void moveFile(File file, String path) throws Exception {
		String fileName2 = "";
		try {
			//파일패스 뒤에 separator가 있는지 확인한다.
			path = chkSeparator(path);

			//복사할 곳에 디렉토리가 있는지 확인한다.
			if (!existDirectory(path)) {
				//디렉토리가 없으면 만든다.
				createDirectory(path);
			}

			if (existFile(path, fileName)) {
				//파일을 복사하려는 곳에 같은 이름의 파일이 있으면 새로 만든다.
				fileName2 = makeFileName(path, fileName);

				while (existFile(path, fileName2)) {
					//파일 이름이 겹치면 계속해서 새 이름을 만든다.
					fileName2 = makeFileName(path, fileName2);
				}
			} else {
				fileName2 = fileName;
			}

			File file2 = new File(path, fileName2);

			file.renameTo(file2); //파일을 이동시킨다.

		} catch (Exception e) {
		}
	}
	
	/**
	 * 첨부파일을 복사시킨다
	 * 
	 * @param file : 파일
	 * 				 path : 파일이 저장될 패스
	 * 				 cp : 'cp' 카피, 'mv' 이동
	 */
	public static void copyFile(File file, String path, String cp) {
		String fileName2 = "";

		try {
			//한글 파일의 경우 아래와 같이 해야 한글이 깨지지 않는다.
			//fileName = new String(file.getName().getBytes("euc-kr"),"8859_1");
			fileName = file.getName();

			//파일패스 뒤에 separator가 있는지 확인한다.
			path = chkSeparator(path);

			//파일이 들어갈 배열의 선언과 크기 설정
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
			//만약 확장자로 걸러내고 싶다면...
			File[] fileNames = f.listFiles(new FilenameFilter(){
			    public boolean accept(File dir, String name){
			      return name.endsWith(".exe");
			    }
			});
			 */

			//복사할 곳에 디렉토리가 있는지 확인한다.
			if (!existDirectory(path)) {
				//디렉토리가 없으면 만든다.
				createDirectory(path);
			}

			if (existFile(path, fileName)) {
				fileName2 = makeFileName(path, fileName);
				while (existFile(path, fileName2)) {
					//파일 이름이 겹치면 계속해서 새 이름을 만든다.
					fileName2 = makeFileName(path, fileName2);
				}

			} else {
				fileName2 = fileName;
			}

			//복사될 파일 객체를 만든다.
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

			//출력 스트림을 선언한다.
			/*
			FileOutputStream outStream = new FileOutputStream(path + fileName2);
			
			//내용을 출력한다.
			outStream.write(bytestream);
			
			//출력스트림을 닫는다.
			outStream.close();
			bytestream = null;
			if(filestream != null) filestream.close();
			*/

			//이동일경우 기존의 파일을 삭제한다.
			if (cp.equals("mv")) {
				file.delete();
			}
		} catch (Exception e) {
		} finally {
		}
	}
	
	/**
	 * 해당 디렉토리에 같은 이름이 있을경우 파일 이름을 바꾸는 메서드 
	 * 파일명의 뒤에 _002에서 _999 까지를 붙인다.
	 * 
	 * @param fname : 원본파일이름
	 * @return 고쳐진 파일이름
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
				//최초에는 001을 붙여준다.
				newName = fname.substring(0, j) + "_002" + fname.substring(j);
			} else {
				//버젼 숫자를 잘라낸다.
				int k = Integer.parseInt(fname.substring(i + 1, j));
				k += 1; //버젼 숫자에 1을 더한다.
				tt = tt + Integer.toString(k);
				//자리수를 맞춰서 버젼을 만든다.
				tt = "_" + tt.substring(tt.length() - 3);

				//새 파일 이름을 만든다.
				newName = fname.substring(0, i) + tt + fname.substring(j);
			}

		} catch (Exception e) {
			try {
				//새 파일 이름을 만든다.
				newName = fname.substring(0, j) + "_002" + fname.substring(j);
			} catch (Exception ee) {
				newName = fname;
			}

		}
		return newName;
	}
	
	/**
	 *파일 패스 뒤에 \ 혹은 / 이 없으면 붙여주는 메서드
	 * 
	 * @param path : 파일패스
	 * @return 고쳐진 파일 패스
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
	 * File에서 특정 패턴의 string을 읽어서 다른 문자로 대치
	 */
	public static void file_str_chg(String org, String target) {
		// 원본파일경로
		String fileName = org;

		// file 객체 생성
		File inputFile = new File(fileName);
		File outputFile = new File(target);

		FileInputStream fileInputStream = null;
		BufferedReader bufferedReader = null;
		FileOutputStream fileOutputStream = null;
		BufferedWriter bufferedWriter = null;

		boolean result = false;

		try {
			// FileInputStream,FileOutputStream, BufferdReader, BufferedWriter 생성
			fileInputStream = new FileInputStream(inputFile);
			fileOutputStream = new FileOutputStream(outputFile);

			bufferedReader =
				new BufferedReader(new InputStreamReader(fileInputStream));
			bufferedWriter =
				new BufferedWriter(new OutputStreamWriter(fileOutputStream));

			// 원본 파일에서 읽어 들이는 한라인
			String line;
			// 패턴에 일치하는 문자로 대체하고 난 후의 string
			String repLine;

			// 바꾸고자 하는 string과 바꿀 sting 정의
			String originalString = "crm.URL=A";
			String replaceString = "crm.URL=D";

			// 원본 파일에서 한라인씩 읽는다.
			while ((line = bufferedReader.readLine()) != null) {
				// 일치하는 패턴에서는 바꿀 문자로 변환
				repLine = line.replaceAll(originalString, replaceString);

				// 새로운 파일에 쓴다.
				bufferedWriter.write(repLine, 0, repLine.length());
				bufferedWriter.newLine();
			}
			// 정상적으로 수행되었음을 알리는 flag
			result = true;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// 리소스 해제. 개별적으로 해제한다.
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

			// 정상적으로 수행되었을 경우 원본 파일을 지우고 새로운 파일명을 원본파일명으로 rename한다.
			if (result) {
				inputFile.delete();
				outputFile.renameTo(new File(fileName));
			}
		}
	}
	
	/**
	 * File을 다운로드 한다.
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
	 * File을 다운로드 한다.
	 */
	public static boolean downloadPDF( HttpServletResponse httpservletresponse,
											InputStream inputstream,
											String strFileName,
											String strContentType) {
		javax.servlet.ServletOutputStream servletoutputstream = null;
		try {
			boolean flag1;
			try {

				// 운영계 반영 : Characterset : KSC5601 --> 8859-1
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
	 * File을 다운로드 한다.
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
	 * File을 다운로드 한다.
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
	 * 해당 파일의 확장자를 얻는다. 해당 파일이 디렉토리 또는 확장자가 없는 경우 null 을 리턴.
	 * 
	 * @param file : 파일
	 * @return 파일 확장자 (소문자)
	 */
	public static String getExtension(File file) {
		if (file.isDirectory())
			return null;
		else
			return getExtension(file.getName());
	}
	
	/**
	 * 해당 파일(파일 경로)의 확장자를 얻는다. 해당 파일 경로에 확장자가 없는 경우('.' 이 없는 경우) null 을 리턴.
	 * 
	 * @param strFileFullPath : 파일 경로
	 * @return 파일 확장자 (소문자)
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
	 * 한글로 작성된 XML 내용을 UTF-8로 바꿔 파일을 생성 한다. 
	 */
	public static String writeFile_UTF_8( String fileName,
											String content,
											String path) {
		String strResult = "0"; // 처리결과

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
	 * 일반 파일 생성 
	 */
	public static String writeFile( String fileName,
										String content,
										String path) {
		String strResult = "0"; // 처리결과

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
	 * File 존재 여부
	 * 
	 * @param file : File 명
	 * @return 존재여부
	 */
	public static boolean exists(File file) {
		return file.exists();
	}
	
	/**
	 * File 존재 여부(File Full Path이용)
	 * 
	 * @param strFileFullPath : File Full Path
	 * @return 존재여부
	 */
	public static boolean exists(String strFileFullPath) {
		return exists(new File(strFileFullPath));
	}
	
	/**
	 * File 존재 여부(document Root 아래의 File)
	 */
	public static boolean exists(ServletContext servletcontext,
									String strFileName) {
		String strFileFullPath = servletcontext.getRealPath(strFileName);
		return strFileFullPath == null ? false : exists(strFileFullPath);
	}
	
	/**
	 * File 존재 여부
	 */
	public static boolean exists(String strFilePath, String strFileName) {
		return exists(
			new File(strFilePath + (strFilePath.endsWith(File.separator) ? "" : File.separator) + (strFileName == null ? "" : strFileName)));
	}
	
	/**
	 * 파일 확장자 뽑기
	 */
	public static String getExtensionOfFile(String strFileName) {
		int i = strFileName.lastIndexOf("."); 
		return strFileName.substring(i+1);
	}
	
	/**
	 * 디렉토리 압축하기
	 */
	public void creatZipFile1() throws Exception {
		  

	    File f = new File("D:/file/fileup");
	  
	    String path = "D:/file/fileup";

	    String files[] = f.list(); // f object 에 있는 파일목록
	    
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
	            out.putNextEntry(new ZipEntry(files[i])); // Zip 파일에 경로를 정하여 저장할수 있다.
	    
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
	 * 디렉토리 압축하기
	 */
	public void creatZipFile2() throws Exception {
	  

	    File f = new File("D:/file/fileup");


	    int size = 1024;
	    String path = "D:/file/fileup";

	    String files[] = f.list(); // f object 에 있는 파일목록


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
	            out.putNextEntry(new ZipEntry(files[i])); // Zip 파일에 경로를 정하여 저장할수 있다.
	    
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
	
	//Java로 윈도우 서비스 리스트 가져오는 방법 
	//현재 실행중인 프로그램 리스트는 task.exe 를 Runtime.exec로 실행해 해당 스트림을 통해서 가져올수 있다.
	//해당 프로세스의 대한 정보를 보려면 sc queryex 프로세스명이름  이라고 해야 해당 서비스의 세부 내용이 출력된다.
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
	
	//결국 자바의 특성을 버리고 OS 에 의존적이 되겠지만... 
	//분명 있어야할 종류의 클래스이다. Runtime 이라는 클래스를 이용
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
	
	//java 로 MD5 구하기
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
	 * @title	 	특정 property 파일에서 특정 값을 얻어온다
	 * 
	 * @param	property 파일명 
	 * 					얻어올 값의 key 이름
	 * @return 	key에 해당하는 값 
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

