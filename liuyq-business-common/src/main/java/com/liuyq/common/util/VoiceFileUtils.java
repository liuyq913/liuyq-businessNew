package com.liuyq.common.util;

import javazoom.jl.decoder.BitstreamException;

import java.io.IOException;

/**
 * Created by liuyq on 2017/12/19.
 */
public class VoiceFileUtils {
  /*  private static final Logger LOGGER = LoggerFactory.getLogger(VoiceFileUtils.class);
    *//**
     * 将文件流读入到目标文件中
     * @param ins
     * @param file
     * @throws IOException
     *//*
    public static void inputstreamtofile(InputStream ins, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }

    *//**
     * 递归删除文件目录及目录里面的文件
     * @param dir
     * @return
     *//*
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(children[i]);
                while (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static Long getVoiceTimeByUrl(String url) {
        Long time = 0L;
        Boolean flage = false;
        try {
            flage = isTrueFile(url);
        }catch (IOException e){
            LOGGER.error("音频url获取链接出错");
        }
        if(flage){ //存在
            String end = url.substring(url.lastIndexOf(".")+1,url.length());
            if(end.equals("mp3")){
                try {
                    time = getMp3Time(url);
                }catch (IOException e){
                    LOGGER.error("音频时间获取失败，音频连接是:"+url);
                }
            }else if(end.equals("m4a")){
                try {
                    time = getM4aTime(url);
                }catch (IOException e){
                    LOGGER.error("音频时间获取失败，音频连接是:"+url);
                }finally {
                    //刪除test目录
                    try {
                        File current = new File(".");
                        File catalog = new File(current.getCanonicalPath() + "//demo");
                        if (catalog.exists()) {
                            deleteDir(catalog);
                        }
                    }catch (IOException e){
                        LOGGER.error("删除目录异常");
                    }
                }
            }
        }
        return time;
    }
    //链接对应的文件是否存在
    private static Boolean isTrueFile(String urlString) throws IOException {
        HttpURLConnection uc;
        InputStream iputstream = null;
        try {
            URL url = new URL(urlString);
            uc= (HttpURLConnection) url.openConnection();
            uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
            uc.connect();
            iputstream = uc.getInputStream();
        } finally {
            if(iputstream != null) {
                iputstream.close();
            }
        }
        return iputstream == null ? false : true;
    }

    *//**
     * 获取mp3格式的音频时长
     * @param url
     * @return
     * @throws IOException
     * @throws BitstreamException
     *//*
    private static Long getMp3Time(String url) throws IOException{
        URL urlfile = new URL(url);
        URLConnection con = null;
        try {
            con = urlfile.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int b = con.getContentLength();
        int time = 0;
        InputStream inputStream = null;
        BufferedInputStream bis = null;
        try {
            inputStream = con.getInputStream();
            bis = new BufferedInputStream(con.getInputStream());
            Bitstream bt = new Bitstream(bis);
            Header h = bt.readFrame();
            time = (int) h.total_ms(b);
        }catch (BitstreamException e){
            LOGGER.error("mp3音频时长计算出错，音频链接为:"+url);
        }finally {
            if(inputStream != null) {
                inputStream.close();
            }
            if(bis !=null) {
                bis.close();
            }
        }
        return new Long(time / 1000);
    }

    *//**
     *获取m4a的音频时长（有点坑）
     * @param stringurl
     * @return
     *//*
    private static synchronized Long getM4aTime(String stringurl) throws IOException {
        URL url = new URL(stringurl);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
        uc.connect();
        InputStream iputstream = uc.getInputStream();
        File current = new File(".");
        File catalog =new File(current.getCanonicalPath()+"//demo");
        LOGGER.error("目录在:"+catalog.getCanonicalPath());
        if(!catalog.exists()){
            catalog.mkdir(); //不存在则创建
        }
        String end = stringurl.substring(stringurl.lastIndexOf("/")+1,stringurl.length());
        File file = new File(current.getCanonicalPath()+"//demo//"+end);
        if(!file.exists()){
            file.createNewFile();
            inputstreamtofile(iputstream, file);
        }
        Encoder encoder = new Encoder();
        Long time = 0L;
        try {
            MultimediaInfo m = encoder.getInfo(file);
            long ls = m.getDuration();
            time = ls/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(iputstream != null) {
                iputstream.close();
            }
            //刪除test目录
            try {
                if (catalog.exists()) {
                    deleteDir(catalog);
                }
            }catch (Exception e){
                LOGGER.error("删除文件异常");
            }
        }
        return time;
    }*/
}
