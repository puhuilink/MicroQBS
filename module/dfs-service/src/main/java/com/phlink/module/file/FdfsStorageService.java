package com.puhuilink.module.file;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;

import java.io.InputStream;

public interface FdfsStorageService {

    StorePath simpleUpload(InputStream inputStream, Long fileSize, String fileExtName);

    Boolean deleteFile(String path);
}
