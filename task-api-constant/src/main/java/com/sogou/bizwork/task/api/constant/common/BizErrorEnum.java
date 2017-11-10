package com.sogou.bizwork.task.api.constant.common;

public enum BizErrorEnum {

    PARAMETER_IS_EMPTY(00000, "必要的参数为空."),

    // 1 for tag
    TAG_TASK_ID_NULL(10001, "标签任务id不能为空"), TAG_USER_ID_NULL(10002, "标签拥有者id不能为空"), TAG_NAME_NULL(10003, "标签名称不能为空"), TAG_STATUS_NULL(
            10004, "标签状态不能为空"), TAG_STATUS_ERROR(10005, "标签状态只能为0或者-1"),
            
    //2 for task
    TASK_TASK_ID_INVALID(20001, "非法任务id"),
    TASK_CREATE_USER_NULL(20002, "任务创建者不能为空"), TASK_CHARGE_USER_NULL(20003, "任务负责人不能为空"), TASK_START_TIME_NULL(20004, "任务开始时间不能为空"),
    TASK_END_TIME_NULL(20005, "任务截止时间不能为空"), TASK_START_END_TIME_ERROR(20006, "任务截止时间必须晚于开始时间"), TASK_NAME_NULL(20007, "任务名称不能为空"),
    TASK_DESCRIPTION_NULL(20008, "任务描述不能为空"), TASK_ATTACHMENT_NULL(20009, "任务附件不能为空"),TASK_STATUS_NULL(20010, "任务状态不能为空"), 
    TASK_STATUS_ERROR(20011, "任务状态只能为0、1、2或者-1"),TASK_ATTACHMENT_UPLOAD_FAIL(20012,"任务附件上传失败"),TASK_ATTACHMENT_DEL_FAIL(20013,"任务附件删除失败"),
    TASK_START_END_TIME_INVALID(20014, "任务开始时间或截止时间格式错误"), TASK_CHARGER_UPDATE_FAIL(20015, "更新任务负责人失败"),TASK_DESC_UPDATE_FAIL(20016, "更新任务描述失败"),
    TASK_VALID_TIME_UPDATE_FAIL(20018, "更新有效起止时间失败"), TASK_STATUS_UPDATE_FAIL(20019, "更新任务状态失败"),TASK_FOLLOWS_STATUS_UPDATE_FAIL(20020, "更新关注人状态为删除失败"),
    TASK_UPLOAD_FILE_FAIL_RETURN_NULL(20021, "上传DFS返回空"),
    
    // 3 for task_log
    TASK_LOG_OPERATE_TYPE_NULL(30001, "操作日志类型不能为空"), TASK_LOG_OPERATE_TYPE_ERROR(30002, "操作日志类型只能为0,1,-1"), TASK_LOG_OPERATE_USER_NULL(30003, "操作日志操作人不能为空"),
    
    // 500 for system error
    SYSTEM_ERROR(500, "系统异常.");

    private int code;
    private String message;

    private BizErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(int code) {
        for (BizErrorEnum em : BizErrorEnum.values()) {
            if (em.getCode() == code) {
                return em.getMessage();
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
