package com.phlink.demo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.phlink.core.web.base.enums.ResultCode;
import com.phlink.core.web.base.exception.BizException;
import com.phlink.module.file.FdfsStorageService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/file")
@Validated
public class FileController {

    @Autowired
    private FdfsStorageService fdfsStorageService;

    @PostMapping("/single")
    @ApiOperation(value = "上传一个文件", notes = "上传一个文件", httpMethod = "POST")
    public String singleFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws BizException {
        if (file.isEmpty()) {
            throw new BizException(ResultCode.BODY_NOT_MATCH, "上传的文件大小为空,请检查!!");
        }
        //获取文件名称、后缀名、大小
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new BizException(ResultCode.BODY_NOT_MATCH, "文件名为空, 请检查!!");
        }
        String[] names = fileName.split("\\.");
        String suffixName = names[names.length - 1];
        long size = file.getSize();

        String path = "";
        try {
            StorePath storePath = fdfsStorageService.simpleUpload(file.getInputStream(), size, suffixName);
            path = storePath.getFullPath();
        } catch (IOException e) {
            log.error("上传的文件异常", e);
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR, "上传的文件异常!!");
        }
        return path;
    }

    @DeleteMapping("/single")
    @ApiOperation(value = "删除一个已上传的文件", notes = "删除一个已上传的文件", httpMethod = "DELETE")
    public Boolean deleteFile(@RequestParam("path") String path) {
        try {
            fdfsStorageService.deleteFile(path);
        } catch (Exception e) {
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return true;
    }
}
