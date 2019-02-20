package com.liuyq.designpatter.singleton;

import javax.sound.sampled.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by liuyq on 2017/12/5.
 */
public class AudioLength {

    public static void inputstreamtofile(InputStream ins,File file) throws Exception{
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }
    public static void main(String[] args) throws LineUnavailableException,
            UnsupportedAudioFileException, IOException ,Exception{
        String fileUrl = "http://192.168.100.142/public/M00/00/E0/wKhkjVolLW6ABxxfAAAnmn4pMTU433.m4a";
        URL url = new URL(fileUrl);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
        uc.connect();
        InputStream iputstream = uc.getInputStream();
        File file = new File("D:\\demo\\demo.m4a");
        inputstreamtofile(iputstream, file);
        Clip clip = AudioSystem.getClip();
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);
        clip.open(ais);
        System.out.println( clip.getMicrosecondLength() / 1000000D + " s" );//获取音频文件时长
    }
}
