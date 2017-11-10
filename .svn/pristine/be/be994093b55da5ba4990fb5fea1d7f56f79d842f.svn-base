package com.sogou.bizwork.task.api.web.task.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.sogou.bizwork.task.api.core.task.po.AttachmentInfo;
import com.sogou.bizwork.task.api.core.task.po.FollowUserVo;
import com.sogou.bizwork.task.api.web.task.vo.TaskVo;

/**
 * 
 * @author liquancai
 * @date 2016年8月9日
 */
public class TaskUtil {

    /**
     * 比较两个TaskVo是否相同
     * @param taskVoPre
     * @param taskVo
     * @return 比较结果
     */
    public static boolean compareTaskVo(com.sogou.bizwork.task.api.core.task.po.TaskVo taskVoPre, com.sogou.bizwork.task.api.core.task.po.TaskVo taskVo) {
        if (taskVoPre == null && taskVo == null)
            return true;

        if (taskVoPre == null || taskVo == null)
            return false;

        if (taskVoPre.getTaskId() != (taskVo.getTaskId()))
            return false;

        if (!taskVoPre.getTaskName().equals(taskVo.getTaskName()))
            return false;

        if (taskVoPre.getChargeUser() != taskVo.getChargeUser())
            return false;

        if (!taskVoPre.getDescription().equals(taskVo.getDescription()))
            return false;

//        if (taskVoPre.getStartTime() != null && taskVo.getStartTime() != null
//                && !taskVoPre.getStartTime().equals(taskVo.getStartTime()))
//            return false;
        if (taskVo.getStartTime() != null && !taskVo.getStartTime().equals(taskVoPre.getStartTime())
        		|| taskVoPre.getStartTime() != null && !taskVoPre.getStartTime().equals(taskVo.getStartTime())) {
        	return false;
        }
        if (taskVo.getEndTime() != null && !taskVo.getEndTime().equals(taskVoPre.getEndTime())
        		|| taskVoPre.getEndTime() != null && !taskVoPre.getEndTime().equals(taskVo.getEndTime())) {
        	return false;
        }
//        if (taskVoPre.getEndTime() != null && taskVo.getEndTime() != null
//                && !taskVoPre.getEndTime().equals(taskVo.getEndTime()))
//            return false;

        List<FollowUserVo> followUserVos1 = taskVoPre.getFollowUsers();
        List<FollowUserVo> followUserVos2 = taskVo.getFollowUsers();
        if (followUserVos1 != null && followUserVos2 != null) {
            if (followUserVos1.size() != followUserVos2.size())
                return false;
            else {
                Set<Long> followUserSet1 = new HashSet<Long>();
                for (FollowUserVo followUserVo : followUserVos1) {
                    followUserSet1.add(followUserVo.getId());
                }
                for (FollowUserVo followUserVo : followUserVos2) {
                    if (!followUserSet1.contains(followUserVo.getId()))
                        return false;
                }
            }
        } else if (followUserVos1 != null || followUserVos2 != null) {
            return false;
        }

        return true;
    }

    /**
     * 将文件地址信息转出结构化数据
     * 如:http://ssdf2335_sdgf*name1.jpg;http://sg3r934*name2.jpg;
     * 转成 {
     * fileName:name1.jpg,
     * filePath:http://sougou.1234.jpg
     * },
     * {
     * fileName:name1.jpg,
     * filePath:http://sougou.1234.jpg
     * },
     * 
     * @param attachmentInfo
     * @return
     */
    public static List<AttachmentInfo> getFileLists(String attachmentInfo, StringBuffer downloadPath) {
        if (StringUtils.isEmpty(attachmentInfo))
            return Collections.emptyList();
        List<AttachmentInfo> attachments = new ArrayList<AttachmentInfo>();
        String[] files = attachmentInfo.split(";");
        for (String file : files) {
            StringBuffer newFilePath = new StringBuffer(downloadPath);
            String[] fileDetails = file.split("\\*");
            String fileName = null;
            if (fileDetails.length == 2) {
                fileName = fileDetails[1];
            }
            String fid = fileDetails[0];
            newFilePath.append("filePath=" + fid).append("&fileName=" + fileName);
            AttachmentInfo attachment = new AttachmentInfo(newFilePath.toString(), fileName);
            attachment.setFid(fid);
            attachments.add(attachment);
        }
        return attachments;
    }

    /**
     * 将原始日期格式2016-09-18 12:12:12转成9.18
     * 
     * @param orgDate
     * @return
     */
    public static String parseDate(String orgDate) {
        if (StringUtils.isEmpty(orgDate))
            return "";
        // throw new RuntimeException("date format error ,as orgDate is NULL");
        StringBuffer sb = new StringBuffer(5);
        sb.append(Integer.parseInt(orgDate.substring(5, 7))).append(".")
                .append(Integer.parseInt(orgDate.substring(8, 10)));
        return sb.toString();
    }
}
