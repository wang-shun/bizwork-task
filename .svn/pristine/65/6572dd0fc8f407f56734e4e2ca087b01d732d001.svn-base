package com.sogou.bizwork.task.api.web.message.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sogou.bizwork.task.api.common.util.BeanUtils;
import com.sogou.bizwork.task.api.core.message.dto.MessageDTO;
import com.sogou.bizwork.task.api.core.task.po.MessageVO;

public class MessageConvertor {

    private static final Logger logger = LoggerFactory.getLogger(MessageConvertor.class);

    public static MessageDTO convertVo2Dto(MessageVO messageVO) {
        MessageDTO messageDTO = new MessageDTO();
        if (messageVO != null)
            BeanUtils.copy(messageVO, messageDTO);
        return messageDTO;
    }

    public static MessageVO convertDto2Vo(MessageDTO messageDTO) {
        MessageVO messageVO = new MessageVO();
        if (messageDTO != null)
            BeanUtils.copy(messageDTO, messageVO);
        return messageVO;
    }
}
