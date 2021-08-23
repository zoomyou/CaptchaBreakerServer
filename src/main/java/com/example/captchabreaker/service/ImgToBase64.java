package com.example.captchabreaker.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;

@Service
public class ImgToBase64 {

    public String getBase64(String path) throws IOException {
        File file = new File(path);
        return fileToBase64Str(file);
    }

    private static final Base64.Encoder ENCODER_64 = Base64.getEncoder();
    public static String fileToBase64Str(File file) throws IOException {
        String base64Str = null;
        FileInputStream fin = null;
        BufferedInputStream bin = null;
        ByteArrayOutputStream baos = null;
        BufferedOutputStream bout = null;
        try {
            fin = new FileInputStream(file);
            bin = new BufferedInputStream(fin);
            baos = new ByteArrayOutputStream();
            bout = new BufferedOutputStream(baos);
            // io
            byte[] buffer = new byte[1024];
            int len = bin.read(buffer);
            while (len != -1) {
                bout.write(buffer, 0, len);
                len = bin.read(buffer);
            }
            // 刷新此输出流，强制写出所有缓冲的输出字节
            bout.flush();
            byte[] bytes = baos.toByteArray();
            // Base64字符编码
            base64Str = ENCODER_64.encodeToString(bytes).trim();
        } catch (IOException e) {
            e.getMessage();
        } finally {
            try {
                fin.close();
                bin.close();
                bout.close();
            } catch (IOException e) {
                e.getMessage();
            }
        }
        return base64Str;


    }
}
