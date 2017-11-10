package com.sogou.bizwork.task.api.core.user.po;


/**
 * @author 作者 E-mail:sixiaolin@sogou-inc.com
 * @version 创建时间：2016-7-27 下午02:33:40
 * 类说明
 */
public enum UserJobType {
	NO("User"),
	DEV("开发工程师"),
	QA("测试工程师"),
	OP("运维工程师"),
	FE("前端工程师"),
	DBA("数据库管理员"),
	SDIRECTOR("高级总监");
	
    String text;

    public String getText() {
        return text;
    }
    
    UserJobType(String text){
    	this.text=text;
    }
    
    public static UserJobType parse(int i) {
        if (i == NO.ordinal()) {
            return NO;
        }
        if (i == DEV.ordinal()) {
            return DEV;
        }
        if (i == QA.ordinal()) {
            return QA;
        }
        if (i == OP.ordinal()) {
            return OP;
        }
        if (i == FE.ordinal()) {
            return FE;
        }
        if (i == DBA.ordinal()) {
            return DBA;
        }
        if(i==SDIRECTOR.ordinal())
        {
        	return SDIRECTOR;
        }

        return null;
    }
    

}
