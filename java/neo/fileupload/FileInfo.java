package neo.fileupload;

import java.io.File;

import neo.util.file.FileUtil;

import org.apache.commons.fileupload.FileItem;

/**
 * 	@Class Name	: 	FileInfo.java
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
public class FileInfo {

	private String fieldName;
	private String fieldValue;
	private String contentType;
	private String clientFileName;
	private String fileName;
	private String path;
	private String dir;
	private File file;

	private FileInfo() {
	}

	public FileInfo(FileItem item, File newFileInfo) {
		this.fieldName = item.getFieldName();
		this.fieldValue = item.getName();
		this.fileName = FileUtil.getFileNameChop(newFileInfo.getName());
		this.clientFileName = FileUtil.getFileNameChop(this.fieldValue);
		this.dir = FileUtil.getFilePathChop(newFileInfo.getAbsolutePath());
		this.path = this.dir + this.fileName;
		this.contentType = item.getContentType();
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public String getFieldValue() {
		return this.fieldValue;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getClientPath() {
		return this.fieldValue;
	}

	public String getClientFileName() {
		return clientFileName;
	}

	public String getServerPath() {
		return path;
	}

	public String getContentType() {
		return this.contentType;
	}

	public long getSize() {
		return getFile().length();
	}

	public File getFile() {
		if (null == this.file)
			this.file = new File(path);
		return this.file;
	}

	public boolean delete() {
		return getFile().delete();
	}

	public void renameTo(File dest) {
		getFile().renameTo(dest);
	}
}
