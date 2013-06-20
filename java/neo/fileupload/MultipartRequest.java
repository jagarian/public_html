package neo.fileupload;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import neo.exception.FileUploadException;

import neo.util.file.FileUtil;
import neo.util.log.Log;
import neo.util.comm.StringUtil;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

/**
 * 	@Class Name	: 	MultipartRequest.java
 * 	@파일설명		: 	
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
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
public class MultipartRequest {

	private HttpServletRequest req;
	private List formInfos = new ArrayList();
	private List fileInfos = new ArrayList();

	private int MAX_SIZE;
	private String TEMP_DIR;
	private int SIZE_THRESHOLD;
	private String HEADER_ENCODING;
	private String TARGET_DIR;

	private MultipartRequest() {
	}

	public MultipartRequest(HttpServletRequest req,
							int max_size,
							String temp_dir,
							int size_threhold,
							String header_encoding,
							String target_dir)
		throws FileUploadException, Exception {
		this.req = req;
		this.MAX_SIZE = max_size;
		this.TEMP_DIR = temp_dir;
		this.SIZE_THRESHOLD = size_threhold;
		this.HEADER_ENCODING = header_encoding;
		this.TARGET_DIR = target_dir;
		initRequest();
	}

	private void initRequest() throws Exception {

		DiskFileUpload fu = new DiskFileUpload();
		fu.setSizeMax(this.MAX_SIZE);
		fu.setRepositoryPath(this.TEMP_DIR);
		fu.setSizeThreshold(this.SIZE_THRESHOLD);
		fu.setHeaderEncoding(this.HEADER_ENCODING);

		List fileItems = null;
		Iterator iter = null;
		FileItem item = null;
		File file = null;
		ArrayList cleanupBuffer = new ArrayList();

		try {
			fileItems = fu.parseRequest(this.req);
			iter = fileItems.iterator();

			while (iter.hasNext()) {
				item = (FileItem) iter.next();
				Log.info("item.getName() :: " + item.getFieldName(), this);
				if (item.isFormField()) {
					formInfos.add(new FormInfo(item));
				} else {
					if (item.getName().length() == 0)
						continue;
					file =
						new File(
							this.TARGET_DIR
								+ FileUtil.getFileNameChop(item.getName()));
					file = getConflictSafeFile(file);
					item.write(file);
					fileInfos.add(new FileInfo(item, file));
					cleanupBuffer.add(file);
				}
			}
		} catch (FileUploadException e) {
			throw e;
		} catch (Exception e) {
			Log.error("initRequest() error found whild saving files. Uploaded file cleanup process started =>", this);
			if (item != null)
				item.delete();
			Log.error(FileUtil.getFileNameChop(item.getName())	+ " : delete from " + this.TARGET_DIR, this);
			Iterator i = cleanupBuffer.iterator();
			while (i.hasNext()) {
				file = (File) i.next();
				item.delete();
				file.delete();
				Log.error(file.getAbsolutePath() + " : delete", this);
			}
			throw e;
		}
	}

	public String getString(String name) {
		Iterator iter = formInfos.iterator();
		while (iter.hasNext()) {
			FormInfo item = (FormInfo) iter.next();
			if (name.equals(item.getFieldName()))
				return item.getFieldValue();
		}
		return null;
	}

	public String getParameter(String name) {
		Iterator iter = formInfos.iterator();
		while (iter.hasNext()) {
			FormInfo item = (FormInfo) iter.next();
			if (name.equals(item.getFieldName()))
				return item.getFieldValue();
		}
		return null;
	}

	public int getInt(String name) {
		Iterator iter = formInfos.iterator();
		while (iter.hasNext()) {
			FormInfo item = (FormInfo) iter.next();
			if (name.equals(item.getFieldName()))
				return StringUtil.strToInt(
					(item.getFieldValue() == null
						? "0"
						: item.getFieldValue()));
		}
		return 0;
	}

	public String[] getParameterValues(String name) {
		ArrayList al = new ArrayList();
		Iterator iter = formInfos.iterator();
		while (iter.hasNext()) {
			FormInfo item = (FormInfo) iter.next();
			if (name.equals(item.getFieldName()))
				al.add(item.getFieldValue());
		}
		return (String[]) al.toArray(new String[al.size()]);
	}

	public String[] getParameterValues() {
		ArrayList al = new ArrayList();
		Iterator iter = formInfos.iterator();
		while (iter.hasNext()) {
			FormInfo item = (FormInfo) iter.next();
			al.add(item.getFieldValue());
		}
		return (String[]) al.toArray(new String[al.size()]);
	}

	public Enumeration getParameterNames() {
		Vector names = new Vector();
		Iterator iter = formInfos.iterator();
		while (iter.hasNext()) {
			FormInfo item = (FormInfo) iter.next();
			names.add(item.getFieldName());
		}
		return names.elements();
	}

	public List getParameters() {
		return formInfos;
	}

	public FileInfo getFileInfo(String name) {
		Iterator iter = fileInfos.iterator();
		while (iter.hasNext()) {
			FileInfo item = (FileInfo) iter.next();
			if (name.equals(item.getFieldName()))
				return item;
		}
		return null;
	}

	public FileInfo[] getFileInfos(String name) {
		ArrayList al = new ArrayList();
		Iterator iter = fileInfos.iterator();
		while (iter.hasNext()) {
			FileInfo item = (FileInfo) iter.next();
			if (name.equals(item.getFieldName()))
				al.add(item);
		}
		return (FileInfo[]) al.toArray(new FileInfo[al.size()]);
	}

	public FileInfo[] getFileInfos() {
		return (FileInfo[]) fileInfos.toArray(new FileInfo[fileInfos.size()]);
	}

	public Enumeration getFileInfoNames() {
		Vector names = new Vector();
		Iterator iter = fileInfos.iterator();
		while (iter.hasNext()) {
			FileInfo item = (FileInfo) iter.next();
			names.add(item.getFieldName());
		}
		return names.elements();
	}

	public List getFileInfoList() {
		return fileInfos;
	}

	public File getConflictSafeFile(File file) {
		if (!file.exists())
			return file;
		String filename = file.getName();
		int lastDot = filename.lastIndexOf('.');
		String extension = (lastDot == -1) ? "" : filename.substring(lastDot);
		String prefix =
			(lastDot == -1) ? filename : filename.substring(0, lastDot);
		int count = 0;
		do {
			file =	new File(	FileUtil.getCompleteLeadingSeperator(file.getParent())
								+ prefix
								+ "["
								+ count
								+ "]"
								+ extension);
			count++;
		} while (file.exists());
		return file;
	}

}