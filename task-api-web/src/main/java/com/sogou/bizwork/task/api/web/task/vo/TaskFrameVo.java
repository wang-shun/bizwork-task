package com.sogou.bizwork.task.api.web.task.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.sogou.bizwork.task.api.core.task.po.TaskVo;

/**
 * 结构化数据，方便前端数据展示
 * @author yaojun
 *
 */
public class TaskFrameVo implements Serializable {

    private static final long serialVersionUID = 6423630329670053096L;

    private String name;
    private long id;
    private List<TaskVo> taskList = new ArrayList<TaskVo>();
    private String color;

    public TaskFrameVo() {}

    public TaskFrameVo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<TaskVo> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<TaskVo> taskList) {
		this.taskList = taskList;
	}

	public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
