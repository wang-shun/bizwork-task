package com.sogou.bizwork.task.api.core.common.dto;

import java.io.Serializable;

public class QueryCondition implements Serializable {

    private Integer pageSize;
    private Integer pageNo;
    private Integer offSet;

    public QueryCondition() {
    }

    public QueryCondition(Integer pageSize, Integer pageNo) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * 分页起始值
     * @return
     */
    public Integer getOffSet() {
        return (pageNo - 1) * pageSize;
    }
}
