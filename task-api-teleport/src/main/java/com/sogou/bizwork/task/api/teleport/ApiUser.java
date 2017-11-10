package com.sogou.bizwork.task.api.teleport;

public class ApiUser {

    /**
     * 所有参数一定不要写中文
     */
    private Long userId;
    private Integer status;
    private Long adminUserId;
    private String passportId;
    private Integer userRoleId;
    private String operator;// 操作者账号
    private String ip;
    private String from;// 只有这个必填
    private String reserve1;// 保留字段1
    private String reserve2;// 保留字段2
    private String reserve3;// 保留字段3
    private String reserve4;// 保留字段4

    public ApiUser() {
    };

    public ApiUser(String from) {
        this.from = from;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the passportId
     */
    public String getPassportId() {
        return passportId;
    }

    /**
     * @return the adminUserId
     */
    public Long getAdminUserId() {
        return adminUserId;
    }

    /**
     * @param adminUserId the adminUserId to set
     */
    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }

    /**
     * @param passportId the passportId to set
     */
    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public Integer getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public String getReserve3() {
        return reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }

    public String getReserve4() {
        return reserve4;
    }

    public void setReserve4(String reserve4) {
        this.reserve4 = reserve4;
    }

    private String transfNULL(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }

    @Override
    public String toString() {
        return "userId:" + transfNULL(this.userId) + ",adminUserId:" + transfNULL(this.adminUserId) + ",userRoleId:"
                + transfNULL(this.userRoleId) + ",operator:" + transfNULL(this.operator) + ",ip:" + transfNULL(this.ip)
                + ",from:" + transfNULL(this.from);
    }
}
