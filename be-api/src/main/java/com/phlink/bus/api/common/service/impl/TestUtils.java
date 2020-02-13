package com.phlink.bus.api.common.service.impl;

import java.io.*;
import java.net.URL;

public class TestUtils {

    private TestUtils() {
        // hide for utils
    }

    /**
     * 将String 转换为InputStream
     * 
     * @param text
     * @return
     * @throws IOException
     */
    public static InputStream getTextInputStream(String text) {
        // 将String转换为InputStream
        return new ByteArrayInputStream(text.getBytes(TestConstants.DEFAULT_CHARSET));
    }

    /**
     * 获取String长度
     * 
     * @param text
     * @return
     * @throws IOException
     */
    public static long getTextLength(String text) {
        return text.getBytes(TestConstants.DEFAULT_CHARSET).length;
    }

    /**
     * 获取文件InputStream
     * 
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getFileInputStream(String path) throws FileNotFoundException {
        return new FileInputStream(getFile(path));
    }

    /**
     * 获取文件
     * 
     * @param path
     * @return
     */
    public static File getFile(String path) {
        URL url = TestUtils.class.getResource(path);
        File file = new File(url.getFile());
        return file;
    }

}