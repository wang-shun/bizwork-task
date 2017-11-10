package com.sogou.bizwork.task.api.web.task.vo;

import java.io.Serializable;

public class AttachmentInfo implements Serializable{
	
    private static final long serialVersionUID = 4908270709048096451L;
    
    private String filePath;
	private String fileName;
	private String fid;
	
	public AttachmentInfo() {
    }
	
	public AttachmentInfo(String filePath, String fileName) {
		this.filePath = filePath;
	    this.fileName = fileName;
    }
	
	public String getFilePath() {
    	return filePath;
    }
	public void setFilePath(String filePath) {
    	this.filePath = filePath;
    }
	
	public String getFileName() {
    	return fileName;
    }
	public void setFileName(String fileName) {
    	this.fileName = fileName;
    }

	public String getFid() {
    	return fid;
    }

	public void setFid(String fid) {
    	this.fid = fid;
    }
	

}
