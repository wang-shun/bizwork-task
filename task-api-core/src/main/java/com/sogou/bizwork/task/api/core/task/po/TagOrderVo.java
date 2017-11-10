package com.sogou.bizwork.task.api.core.task.po;

import java.io.Serializable;

public class TagOrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int type;// 0：我负责的任务标签，1：我关注的任务标签,2:负责人顺序
    private long userId;
    private String order;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}
