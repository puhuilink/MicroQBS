package com.phlink.bus.api.system.controller;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.BusApiResponse;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.service.FdfsStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/file-support")
@Validated
@Api(tags = ApiTagsConstant.TAG_FILESUPPORT)
public class FileSupportController {

    @Autowired
    private FdfsStorageService fdfsStorageService;

    @Value("${fdfs.host}")
    private String host;
    @Value("${fdfs.port}")
    private Integer port;

    @Log("上传一个文件")
    @PostMapping("/single")
    @ApiOperation(value = "上传一个文件", notes = "上传一个文件", tags = ApiTagsConstant.TAG_FILESUPPORT, httpMethod = "POST")
    public BusApiResponse singleFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws BusApiException {
        if (file.isEmpty()) {
            throw new BusApiException("上传的文件大小为空,请检查!!");
        }
        //获取文件名称、后缀名、大小
        String fileName = file.getOriginalFilename();
        if(StringUtils.isBlank(fileName)) {
            throw new BusApiException("文件名为空,请检查!!");
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
            throw new BusApiException("上传的文件异常!!");
        }
        return new BusApiResponse().data(path);
    }

    @Log("删除一个已上传的文件")
    @DeleteMapping("/single")
    @ApiOperation(value = "删除一个已上传的文件", notes = "删除一个已上传的文件", tags = ApiTagsConstant.TAG_FILESUPPORT, httpMethod = "DELETE")
    public BusApiResponse deleteFile(@RequestParam("path") String path) {
        Boolean success = fdfsStorageService.deleteFile(path);
        return new BusApiResponse().data(success);
    }

    @GetMapping("/server-info")
    @ApiOperation(value = "文件服务器地址信息", notes = "文件服务器地址信息", tags = ApiTagsConstant.TAG_FILESUPPORT, httpMethod = "GET")
    public BusApiResponse serverInfo() {
        Map<String, Object> serverInfo = new HashMap<>();
        serverInfo.put("host", host);
        serverInfo.put("port", port);
        return new BusApiResponse().put("data", serverInfo);
    }

    @PostMapping("/multi")
    @ApiOperation(value = "上传多个文件", notes = "上传多个文件", tags = ApiTagsConstant.TAG_FILESUPPORT, httpMethod = "POST", hidden = true)
    public BusApiResponse file(HttpServletRequest request) throws BusApiException {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("files");
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new BusApiException("上传的文件大小为空,请检查!!");
            }
        }
        List<String> result = new ArrayList<>();
        for (MultipartFile file : files) {
            //获取文件名称、后缀名、大小
            String fileName = file.getOriginalFilename();
            if(StringUtils.isBlank(fileName)) {
                throw new BusApiException("文件名为空,请检查!!");
            }
            String[] names = fileName.split("\\.");
            String suffixName = names[names.length - 1];
            long size = file.getSize();

            String path = "";
            try {
                StorePath storePath = fdfsStorageService.simpleUpload(file.getInputStream(), size, suffixName);
                path = storePath.getPath();
                result.add(path);
            } catch (IOException e) {
                log.error("上传的文件异常", e);
                throw new BusApiException("上传的文件异常!!");
            }
        }
        return new BusApiResponse().data(result);
    }

}
