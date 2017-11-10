package com.sogou.bizwork.task.api.web.session.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.bizwork.task.api.constant.common.BizErrorEnum;
import com.sogou.bizwork.task.api.constant.user.UserConstants;
import com.sogou.bizwork.task.api.core.dto.Result;
import com.sogou.bizwork.task.api.web.user.vo.UserVo;

@Controller
@RequestMapping("/session")
public class SessionController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/checkSession.do")
    @ResponseBody
    public Result checkSession(HttpServletRequest request) {
        Result result = new Result();

        return result;
    }

    @RequestMapping(value = "/initSession.do", method = RequestMethod.POST)
    @ResponseBody
    public Result initSession(@RequestBody UserVo userVo, HttpServletRequest request) {
        Result result = new Result();
        try {
            HttpSession session = request.getSession();
            session.setAttribute(UserConstants.BIZWORK_TASK_USER_SID, userVo.getUserInfo());

        } catch (Exception e) {
            logger.error("init session error", e);
            result.setErrorCode(BizErrorEnum.SYSTEM_ERROR.getCode());
        }

        return result;
    }

}
