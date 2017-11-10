package com.sogou.bizwork.task.api.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sogou.bizdev.dfs.DfsService;
import com.sogou.bizdev.dfs.DfsServiceFactory;
import com.sogou.bizwork.task.api.common.exception.DFSException;

public class DFSUtils {

    private static Logger logger = LoggerFactory.getLogger(DFSUtils.class);

    static DfsServiceFactory dfsf = new DfsServiceFactory();
    static DfsService dfss = dfsf.getDfsService();

    public static String writeToDFS(byte[] b) {
        return dfss.writeBytes(b);
    }

    public static InputStream readFromDFS(String fid) throws DFSException {
        try {
            InputStream stream = dfss.readByStream(fid);
            return stream;
        } catch (Exception ex) {
            logger.info("读取DFS流时出现异常", ex);
            throw new DFSException("读取DFS流时出现异常");
        }
    }

    public static File readFromDFS(String fid, File file) {
        file = dfss.readByFile(fid, file);
        return file;
    }

    public static String writeToDFS(InputStream fileInputStream) {
        return dfss.writeStream(fileInputStream);
    }

    public static boolean deleteFromDFS(String fid) {
        return dfss.delete(fid);
    }

    public static void main(String args[]) {
        DfsServiceFactory dfsf = new DfsServiceFactory();
        DfsService dfss = dfsf.getDfsService();

        String fid = null;
        fid = dfss.writeFile(new File("D:\\test.txt"));
        System.out.println(fid);

        InputStream is = dfss.readByStream(fid);

        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(is);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bas = new ByteArrayOutputStream();

        try {
            wb.write(bas);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bas.toByteArray());

        String downloadurl = dfss.writeStream(bis);

        System.out.println(downloadurl);
    }
}
