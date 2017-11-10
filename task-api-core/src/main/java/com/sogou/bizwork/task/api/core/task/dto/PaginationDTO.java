package com.sogou.bizwork.task.api.core.task.dto;
/**
 * @description 分页参数实体DTO
 * @author yangbing@sogou-inc.com
 * @since 2016-7-28
 * @version 1.0.0
 */
public class PaginationDTO {
	private int pageIndex;
	private int pageSize;
	private int pageStart;
	private String orderFieldStr;
	private String orderDirectionStr;
	public int getPageIndex() {
    	return pageIndex;
    }
	public void setPageIndex(int pageIndex) {
    	this.pageIndex = pageIndex;
    }
	public int getPageSize() {
    	return pageSize;
    }
	public void setPageSize(int pageSize) {
    	this.pageSize = pageSize;
    }
	public int getPageStart() {
    	return pageStart;
    }
	public void setPageStart(int pageStart) {
    	this.pageStart = pageStart;
    }
	public String getOrderFieldStr() {
    	return orderFieldStr;
    }
	public void setOrderFieldStr(String orderFieldStr) {
    	this.orderFieldStr = orderFieldStr;
    }
	public String getOrderDirectionStr() {
    	return orderDirectionStr;
    }
	public void setOrderDirectionStr(String orderDirectionStr) {
    	this.orderDirectionStr = orderDirectionStr;
    }
	
	
}
