package com.sogou.bizwork.task.api.core.task.po;

public class TaskFollow {
    private Integer type;  //0: user 1: group
    private Long followUser; 
    
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Long getFollowUser() {
        return followUser;
    }
    public void setFollowUser(Long followUser) {
        this.followUser = followUser;
    }
}
