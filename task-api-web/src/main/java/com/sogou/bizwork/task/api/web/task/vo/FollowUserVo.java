package com.sogou.bizwork.task.api.web.task.vo;

import java.io.Serializable;

/**
 * @description 任务关注Vo
 * @author liquancai
 * @date 2016年8月8日
 */
public class FollowUserVo implements Serializable {

    private static final long serialVersionUID = -4347148929152540891L;

    private long id; // 关注人ID
    private int type;// 关注者类型，0：User 1：group
    private String name; // 关注人名称
    private String displayName; // 关注人显示名称

    public FollowUserVo() {}

    public FollowUserVo(long id, int type) {
        super();
        this.id = id;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + (int) (type ^ (type >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FollowUserVo other = (FollowUserVo) obj;
        if (id != other.id)
            return false;
        if (type != other.type)
            return false;
        return true;
    }
}
