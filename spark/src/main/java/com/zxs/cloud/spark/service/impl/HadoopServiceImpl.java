package com.zxs.cloud.spark.service.impl;

import com.zxs.cloud.spark.service.HadoopService;
import com.zxs.cloud.spark.util.HadoopOperationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by bill.zheng in 2018/8/28
 */
@Slf4j
@Service
public class HadoopServiceImpl implements HadoopService{

    @Value("${hdfs.url}")
    private String hdfsUrl;

    @Value("${hdfs.user}")
    private String userName;

    private FileSystem fileSystem;

    private static final String READ_FILE_ERROR_MESSAGE = "文件读取错误";

    @PostConstruct
    public void init(){
        fileSystem = HadoopOperationUtil.getFileSystem(hdfsUrl, userName);
    }

    @Override
    public boolean isExist(Path path) {
        boolean isExist = false;
        try {
            isExist = fileSystem.exists(path);
        } catch (IOException e) {
            log.error(READ_FILE_ERROR_MESSAGE, e);
        }
        return isExist;
    }

    @Override
    public boolean deleteFile(Path path) {
        boolean isSuccess = true;
        if (isExist(path)){
            try {
                isSuccess = fileSystem.delete(path, true);
            } catch (IOException e) {
                log.error(READ_FILE_ERROR_MESSAGE, e);
            }
        }
        return isSuccess;
    }

    @Override
    public void writeFileAppend(String content, Path path) {
        try (FSDataOutputStream fsDataOutputStream = isExist(path) ? fileSystem.append(path): fileSystem.create(path)){
            fsDataOutputStream.write(content.getBytes());
            fsDataOutputStream.flush();
        } catch (IOException e) {
            log.error("文件写入错误", e);
        }
    }

    @Override
    public void writeNewFile(String content, Path path) {
        if (isExist(path)){
            deleteFile(path);
        }
        try (FSDataOutputStream fsDataOutputStream = fileSystem.create(path)){
            fsDataOutputStream.write(content.getBytes());
            fsDataOutputStream.flush();
        } catch (IOException e) {
            log.error("文件写入错误", e);
        }
    }

    @Override
    public void readFile(Path path) {
        try (FSDataInputStream fsDataInputStream = fileSystem.open(path)){
            IOUtils.copyBytes(fsDataInputStream, System.out, 4096, false);
        } catch (IOException e) {
            log.error(READ_FILE_ERROR_MESSAGE, e);
        }
    }
}
