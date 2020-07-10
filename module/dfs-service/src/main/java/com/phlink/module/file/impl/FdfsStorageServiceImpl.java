/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-06-01 10:32:32
 * @FilePath: /puhuilink-common-framework/module/dfs-service/src/main/java/com/puhuilink/module/file/impl/FdfsStorageServiceImpl.java
 */
package com.puhuilink.module.file.impl;

import java.io.InputStream;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.exception.FdfsServerException;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.puhuilink.module.file.FdfsStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FdfsStorageServiceImpl implements FdfsStorageService {

    @Autowired
    protected FastFileStorageClient fastFileStorageClient;
    @Autowired
    protected AppendFileStorageClient storageClient;

    private static final String GROUP = "group1";

    public StorePath simpleUpload(InputStream inputStream, Long fileSize, String fileExtName){
        StorePath path = storageClient.uploadFile(GROUP, inputStream, fileSize, fileExtName);
        log.info("上传文件-----{}", path.getFullPath());
        return path;
    }

    public Boolean deleteFile(String path){
        try {
            storageClient.deleteFile(GROUP, path.replace(GROUP + "/", ""));
        }catch (FdfsServerException e) {
            log.error("删除文件失败 {}", e.getMessage());
            return false;
        }
        return true;
    }

}
