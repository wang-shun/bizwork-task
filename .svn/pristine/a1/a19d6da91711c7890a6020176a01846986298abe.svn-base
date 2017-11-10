package com.sogou.bizwork.task.api.web.common.handler;

import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.sogou.bizwork.task.api.common.exception.ApiTException;
import com.sogou.bizwork.task.api.constant.common.BizErrorEnum;
import com.sogou.bizwork.task.api.web.common.exception.BizException;

/**
 * 全局异常处理类
 * 
 */
public class ExceptionHandler extends SimpleMappingExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {

        ModelAndView model = new ModelAndView(new MappingJacksonJsonView());
        String errorMsg = null;
//        List<String> errors = new ArrayList<String>();

        if (e instanceof TypeMismatchException || e instanceof HttpMessageNotReadableException) { // 参数类型转换错误
        	errorMsg = "参数类型转换错误";
            logger.info(e.getMessage(), e);
        } else if (e instanceof BindException) { // 参数绑定错误
        	errorMsg = "参数绑定错误";
            logger.info(e.getMessage(), e);
        } else if (e instanceof SizeLimitExceededException || e instanceof MaxUploadSizeExceededException) { // 上传文件过大
        	errorMsg = "上传文件必须小于20M";
            logger.info(e.getMessage(), e);
        } else if (e instanceof BizException) {// 业务异常就不打印日志了
            BizException be = (BizException) e;
            if (be.getErrorMsg() != null) {
            	errorMsg = be.getErrorMsg();
            }
            if (be.getErrors() != null) {
            	errorMsg = be.getErrors().toString();
            }
            if (be.getFieldErrors() != null) {
            	errorMsg = be.getFieldErrors().toString();
            }
        } else if (e instanceof ApiTException) {
            ApiTException apiTException = (ApiTException) e;
            errorMsg = BizErrorEnum.getMessage(apiTException.getErrorCode());
        } else {
        	errorMsg = "系统异常,请稍后再试";
            logger.info(e.getMessage(), e);
        }
        return paramErrorMsg(model,errorMsg);
    }

    private ModelAndView paramErrorMsg(ModelAndView mv, String errorMsg) {
        mv.addObject("success", 0);
        mv.addObject("data", null);
        mv.addObject("errorCode", 0);
        mv.addObject("errorMsg", errorMsg);
        return mv;
    }

}
