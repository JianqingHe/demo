package com.sp.support;

import java.io.*;

/**
 * @author hejq
 * @date 2018-10-10 13:34
 */
public class FileUtils {

    /**
     * 保存文件
     *
     * @param bytes 文件字节
     * @param fileName 文件名
     * @param path 路径
     * @param suffix 文件后缀
     * @return 文件路径
     */
    public static String saveFile(byte[] bytes, String fileName, String path, String suffix) {
        String url = "";
        OutputStream os = null;
        try {
            String filePath = path + fileName + suffix;
            os = new FileOutputStream(filePath);
            // 输出的文件流保存到本地文件

            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            url = filePath;
            os.write(bytes, 0, bytes.length);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    /**
     * 图片文件
     */
    public class Image {

        /**
         * jpg格式图片
         */
        public static final String JPG = ".jpg";

        /**
         * png格式图片
         */
        public static final String PNG = ".png";

        /**
         * gif格式图片
         */
        public static final String GIF = ".gif";
    }

    /**
     * 文本文件
     */
    public class Text {

        /**
         * txt文件
         */
        public static final String TXT = ".txt";

        /**
         * doc文件
         */
        public static final String DOC = ".doc";
    }
}
