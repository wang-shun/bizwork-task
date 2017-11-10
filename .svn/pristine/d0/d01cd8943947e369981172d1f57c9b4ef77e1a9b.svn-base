package com.sogou.bizwork.task.api.core.activiti.po;

public enum SvnRepositoryErrorEnum {
	
	KEY_ERROR(1001, "密钥错误"),
	
	PERMISSION_ERROR(1002, "无权限"),
	
	PARAMETERS_ERROR(1003, "参数错误"),
	
	EXIST_ERROR(1004, "创库已存在"),
	
	UNKNOWN_ERROR(1099, "创建路径时未知错误");
	
	
	private int code;
	private String message;
	
	private SvnRepositoryErrorEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
    public static String getMessage(int code) {
        for (SvnRepositoryErrorEnum em : SvnRepositoryErrorEnum.values()) {
            if (em.getCode() == code) {
                return em.getMessage();
            }
        }
        return null;
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
