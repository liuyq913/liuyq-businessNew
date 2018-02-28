package com.liuyq.designpatter.singleton;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by liuyq on 2017/12/5.
 */
public class NetFileTest {
    public static void main(String[] args) throws IOException {
        URL url = new URL("http://192.168.100.142/public/M00/00/E0/wKhkjVolLW6ABxxfAAAnmn4pMTU433.m4a");
        HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
        // 根据响应获取文件大小
        int fileLength = urlcon.getContentLength();
        System.out.println(fileLength);
    }
}