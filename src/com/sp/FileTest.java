package com.sp;


import java.io.*;

/**
 * 文件测试
 *
 * @author hejq
 * @date 2018-10-10 11:18
 */
public class FileTest {

    public static void main(String[] args) throws IOException {
        String path = "E:\\img\\1.jpg";
        byte[] bytes = readFileByBytes(path);
        String fileName = "12.jpg";
    }

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public static byte[] readFileByBytes(String fileName) throws IOException {
        byte[] bytes;
        // 第1步、使用File类找到一个文件
        File file = new File(fileName) ;
        // 第2步、通过子类实例化父类对象
        InputStream input = new FileInputStream(file);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int numBytesRead = 0;
        while ((numBytesRead = input.read(buf)) != -1) {
            output.write(buf, 0, numBytesRead);
        }
        bytes = output.toByteArray();
        output.close();
        input.close();
        return bytes;
    }

    private static void readAndSave() {
        //指定要读取的图片
        File originalFile = new File("E:\\img\\1.jpg");
        try {
            //要写入的图片
            File result = new File("E:\\img\\12.jpg");
            // 校验该文件是否已存在
            if (result.exists()) {
                //删除对应的文件，从磁盘中删除
                result.delete();
                //只是创建了一个File对象，并没有在磁盘下创建文件
                result = new File("E:\\img\\13.jpg");
            }
            //如果文件不存在
            if (!result.exists()) {
                //会在磁盘下创建文件，但此时大小为0K
                result.createNewFile();
            }
            FileInputStream in = new FileInputStream(originalFile);
            // 指定要写入的图片
            FileOutputStream out = new FileOutputStream(result);
            // 每次读取的字节长度
            int n;
            // 存储每次读取的内容
            byte[] bb = new byte[1024];
            while ((n = in.read(bb)) != -1) {
                // 将读取的内容，写入到输出流当中
                out.write(bb, 0, n);
            }
            //执行完以上后，磁盘下的该文件才完整，大小是实际大小
            out.close();
            // 关闭输入输出流
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
