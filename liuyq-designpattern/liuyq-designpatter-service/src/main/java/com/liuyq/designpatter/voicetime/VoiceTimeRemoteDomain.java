package com.liuyq.designpatter.voicetime;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.liuyq.common.util.DateUtil;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by liuyq on 2017/12/18.
 */
public class VoiceTimeRemoteDomain implements VoiceTimeDomain{
    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceTimeRemoteDomain.class);

    public Long getVoiceTimeByUrl(String url) {
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
                        File catalog = new File(current.getCanonicalPath() + "\\test");
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
    private Boolean isTrueFile(String urlString) throws IOException {
        HttpURLConnection uc;
        InputStream iputstream = null;
        try {
            URL url = new URL(urlString);
            uc= (HttpURLConnection) url.openConnection();
            uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
            uc.connect();
            iputstream = uc.getInputStream();
        } finally {
            iputstream.close();
        }
        return iputstream == null ? false : true;
    }

    /**
     * 获取mp3格式的音频时长
     * @param url
     * @return
     * @throws IOException
     * @throws BitstreamException
     */
    private Long getMp3Time(String url) throws IOException{
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
            inputStream.close();
            bis.close();
        }
        return new Long(time / 1000);
    }

    /**
     *获取m4a的音频时长（有点坑）
     * @param stringurl
     * @return
     */
    private synchronized Long getM4aTime(String stringurl) throws IOException {
        URL url = new URL(stringurl);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
        uc.connect();
        InputStream iputstream = uc.getInputStream();
        File current = new File(".");
        File catalog =new File(current.getCanonicalPath()+"\\test");
        if(!catalog.exists()){
            catalog.mkdir(); //不存在则创建
        }
        String end = stringurl.substring(stringurl.lastIndexOf("/")+1,stringurl.length());
        File file = new File(current.getCanonicalPath()+"\\test\\"+end);
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
            iputstream.close();
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
    }

    public void inputstreamtofile(InputStream ins,File file) throws IOException{
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }

    /**
     * 递归删除文件目录及目录里面的文件
     * @param dir
     * @return
     */
    private boolean deleteDir(File dir) {
        LOGGER.error("删除文件开始");
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
          //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(children[i]);
                while (!success) {
                    LOGGER.error("删除失败"+children[i]);
                    return false;
                }
            }
        }
        LOGGER.error("删除文件结束");
        // 目录此时为空，可以删除
        return dir.delete();
    }

    @Test
    public void test(){
        List<String> list = Lists.newArrayList();
        list.add("http://cdnpubimg.cheok.com/PUBLIC/CAR_INTRODUCE/2015-09-21/19dcdc6c-4409-4361-88d7-8280c8d76254.mp3");
        long start = System.currentTimeMillis();
        for (String url : list) {
                System.out.println((getVoiceTimeByUrl(url)));
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
    @Test
    public void test2() {
        MultipartFile file = null;
        String fileOldName = file.getOriginalFilename();
        long duration = 0;//音频长度，秒
        CommonsMultipartFile cf= (CommonsMultipartFile)file;
        DiskFileItem fi = (DiskFileItem)cf.getFileItem();
        File source = fi.getStoreLocation();
        Encoder encoder = new Encoder();
        MultimediaInfo m = null;
        try {
             m = encoder.getInfo(source);
        }
        catch (Exception e){
            System.out.println("啊哈");
        }
        long ls = m.getDuration();
        duration = ls/1000;
    }

    class places{
        private Integer cityID;
        private String cityName;
        private Integer provinceID;
        private String provinceName;

        public places(Integer cityID, String cityName, Integer provinceID, String provinceName) {
            this.cityID = cityID;
            this.cityName = cityName;
            this.provinceID = provinceID;
            this.provinceName = provinceName;
        }

        public Integer getCityID() {
            return cityID;
        }

        public void setCityID(Integer cityID) {
            this.cityID = cityID;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public Integer getProvinceID() {
            return provinceID;
        }

        public void setProvinceID(Integer provinceID) {
            this.provinceID = provinceID;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }
    }

    @Test
    public void test5(){
        places places = new places(1, "浙江", 2,"杭州");
        List list= Lists.newArrayList();
        list.add(places);
        System.out.println(list);
    }

    @Test
    public void test6(){
        String json = "[{'cityID':9,'cityName':'杭州市','provinceID':23,'provinceName':'浙江省'}]";
       List<places>  list = JSON.parseArray(json, places.class);
        System.out.println(list);
    }
    @Test
    public void test7(){
        String s = "只收衢州市 杭州市";
        System.out.println(s.replaceAll(" ","，"));
    }
    @Test
    public void test8(){
        Date monday = DateUtil.zeroConvertTime(DateUtil.getCycleData(new Date(), DateUtil.cycle_w, 2));
        Date lastMonday = DateUtil.dateAfter(monday, Calendar.DATE, 7);
        System.out.println(monday+"   "+lastMonday);
    }
    @Test
    public void test9(){
        String str = "2";
        String[] s = str.split("&");
        for(int i =0 ;i<s.length;i++){
            System.out.println(s[i]);
        }
    }
    @Test
    public void test10(){
        StringBuilder placeBuilder = new StringBuilder();
        placeBuilder.append("\"cityID:").append(1).append(",");
        placeBuilder.append("provinceID:").append(10).append("\"");
        System.out.println(placeBuilder.toString());
    }

    @Test
    public void test11(){
        for(int i=0; i<10;i++) {
            System.out.println(new Random().nextInt(2));
        }
    }
    @Test
    public void test12(){
        List<String> list = Lists.newArrayList();
        list.add("1");
        System.out.println(list.get(list.size()-1));
    }
}
