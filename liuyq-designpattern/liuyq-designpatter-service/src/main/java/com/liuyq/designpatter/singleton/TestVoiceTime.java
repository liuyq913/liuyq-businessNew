package com.liuyq.designpatter.singleton;


import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by liuyq on 2017/12/4.
 */
public class TestVoiceTime {
    @Test
    public void Test() throws IOException, BitstreamException{
        //URL urlfile = new URL("http://192.168.100.142/public/M00/00/DD/wKhkjVok5j6AQWIZAACbUi2UD3o563.m4a");
        URL urlfile = new URL("http://sc1.111ttt.com/2015/1/06/06/99060941326.mp3");
        URLConnection con = null;
        try {
            con = urlfile.openConnection();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int b = con.getContentLength();//
        BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
        Bitstream bt = new Bitstream(bis);
        Header h = bt.readFrame();
        int time = (int) h.total_ms(b);
        System.out.println(time / 1000);
    }


    public static byte[] getImageFromNetByUrl(String strUrl){
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

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
    /*@Test
    public void test()throws Exception{
        String fileUrl = "http://192.168.100.142/public/M00/00/E0/wKhkjVolLW6ABxxfAAAnmn4pMTU433.m4a";
        URL url = new URL(fileUrl);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
        uc.connect();
        InputStream iputstream = uc.getInputStream();
        File current = new File(".");
        System.out.println(current.getCanonicalPath());

        File file = new File("D:\\test\\test.m4a");
        inputstreamtofile(iputstream, file);
        Encoder encoder = new Encoder();
        try {
            MultimediaInfo m = encoder.getInfo(file);
            long ls = m.getDuration();
            System.out.println("此视频时长为:" + ls / 1000 + "秒！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    @Test
    public void test2(){
       /* MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
        MultipartFile file = multiRequest.getFile(iter.next());
        String fileOldName = file.getOriginalFilename();
        long duration = 0;//音频长度，秒
        CommonsMultipartFile cf= (CommonsMultipartFile)file;
        DiskFileItem fi = (DiskFileItem)cf.getFileItem();
        File source = fi.getStoreLocation();
        Encoder encoder = new Encoder();
        MultimediaInfo m = encoder.getInfo(source);
        long ls = m.getDuration();
        duration = ls/1000;*/
    }
}
