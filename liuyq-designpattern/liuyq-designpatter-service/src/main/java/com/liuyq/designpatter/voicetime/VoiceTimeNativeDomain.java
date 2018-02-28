package com.liuyq.designpatter.voicetime;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by liuyq on 2017/12/19.
 */
public class VoiceTimeNativeDomain {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceTimeNativeDomain.class);

    /**
     * 目录对应的抽象
     */
    private static Path catalog;

    /**
     * 目录地址
     */
    private static String dirPath;

    /**
     * 统计文件夹中文件的总数
     */
    private static volatile AtomicInteger fileNum;

    /**
     * 文件的删除上限
     */
    private static final int deleteNum = 0;

    /**
     * 线程池基类；创建一个拥有两个核心线程的固定线程数的线程池
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    /**
     * 线程显示锁
     */
    private Lock lock = new ReentrantLock();

    static {
        try {
            //File current = new File(".");
            catalog = Paths.get(/*current.getCanonicalPath() + "\\" + */"test");
            if (! Files.isDirectory(catalog)) {
                Files.createDirectories(catalog);
            }
            dirPath = catalog.toRealPath().toString() + File.separator;
            fileNum = new AtomicInteger(Integer.parseInt(String.valueOf(Files.list(catalog).count())));

            // 启动清空
            if (fileNum.get() > 0) {
                deleteDirFiles(catalog);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件本地IO使用NIO
     * @param urls
     */
    public void getVoiceTimeByUrl(List<String> urls) {
        long startTime = System.currentTimeMillis();
        if (urls != null && urls.size() > 0) {
            Map<String, Future<Long>> futures = new HashMap<>();
            // 使用线程执行操作
            for (String url : urls) {
                Future<Long> future = executorService.submit(new VoiceProcessor(url));
                // 不阻塞
                futures.put(url, future);
            }

            // 所有线程执行完，再执行Future.get()，这样不会阻塞
            try {
                for (Map.Entry<String, Future<Long>> future : futures.entrySet()) {
                    System.out.println("当前文件的结果：" + future.getKey() + "------>" + future.getValue().get());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                System.out.println("耗时为：" + (System.currentTimeMillis() - startTime));
                try {
                    // 最后关闭线程池，不然主线程无法关闭，但这一步在应用中不需要，因为应用的主线程一直开着，所以要删除
                    executorService.shutdown();
                    Thread.sleep(500);
                    if (! executorService.isShutdown()) {
                        executorService.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //链接对应的文件是否存在
    private InputStream getFileStream(String urlString) throws IOException {
        HttpURLConnection uc;
        InputStream iputstream = null;

        URL url = new URL(urlString);
        uc= (HttpURLConnection) url.openConnection();
        uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
        uc.connect();
        iputstream = uc.getInputStream();

        return iputstream;
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
    private synchronized long getM4aTime(InputStream inputStream, String stringurl) throws IOException {
        String end = stringurl.substring(stringurl.lastIndexOf("/")+1,stringurl.length());
        Path path = Paths.get(dirPath + end);
        Files.deleteIfExists(path);
        path = Files.createFile(path);
        inputstreamtofile(inputStream, path);
        File file = new File(dirPath + end);
        try {
            lock.lock();
            fileNum.compareAndSet(fileNum.get(), fileNum.get() + 1);
            System.out.println("新增一个文件，文件数为：" + fileNum.get());
        } finally {
            lock.unlock();
        }

        Encoder encoder = new Encoder();
        long time = 0L;
        try {
            MultimediaInfo m = encoder.getInfo(file);
            long ls = m.getDuration();
            time = ls/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            inputStream.close();
        }
        return time;
    }

    /**
     * 使用{@link FileChannel}写文件
     * @param ins
     * @param path
     * @throws IOException
     */
    public void inputstreamtofile(InputStream ins,Path path) throws IOException{
        FileChannel fileChannel = null;
        BufferedReader bReader = null;
        try {
            fileChannel = FileChannel.open(path, StandardOpenOption.WRITE);
            bReader = new BufferedReader(new InputStreamReader(ins));

            String line;
            while ((line = bReader.readLine()) != null) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(line.getBytes());
                fileChannel.write(byteBuffer);
            }
        } finally {
            if (fileChannel != null) {
                fileChannel.close();
            }
            if (bReader != null) {
                bReader.close();
            }
        }
    }

    /**
     * 递归删除文件目录及目录里面的文件；不删除文件夹
     * @param dir
     * @return
     */
    private static boolean deleteDirFiles(Path dir) throws IOException {
        LOGGER.error("删除文件开始");
        if (Files.isDirectory(dir)) {
            //递归删除目录中的子目录下
            Files.list(dir).forEach(sub -> {
                try {
                    boolean success = deleteDirFiles(sub);
                    if (!success) {
                        LOGGER.error("删除失败" + sub.toRealPath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return true;
        }
        LOGGER.error("删除文件结束");
        // 目录此时为空，可以删除
        return Files.deleteIfExists(dir);
    }

    /**
     * 线程实现子类
     */
    private class VoiceProcessor implements Callable<Long> {

        private final String url;

        public VoiceProcessor(String url) {
            this.url = url;
        }

        @Override
        public Long call() throws Exception {
            System.out.println("当前线程为：" + Thread.currentThread().getName());
            Long time = null;
            InputStream inputStream;
            if ((inputStream = getFileStream(url)) != null) { //存在
                String end = url.substring(url.lastIndexOf(".") + 1, url.length());
                if (end.equals("mp3")) {
                    try {
                        time = getMp3Time(url);
                    } catch (IOException e) {
                        LOGGER.error("音频时间获取失败，音频连接是:" + url);
                    }
                } else if (end.equals("m4a")) {
                    try {
                        time = getM4aTime(inputStream, url);
                    } catch (IOException e) {
                        LOGGER.error("音频时间获取失败，音频连接是:" + url);
                    }
                }

                //刪除test目录
                try {
                    int num = fileNum.get();
                    if (num > deleteNum) {
                        try {
                            lock.lock();
                            num = fileNum.get();
                            if (num > deleteNum) {
                                System.out.println("执行了删除文件的操作，文件数为：" + num);
                                deleteDirFiles(catalog);
                                // 重置文件数为0
                                fileNum.compareAndSet(num, 0);
                            }
                        } finally{
                            lock.unlock();
                        }
                    }
                } catch (IOException e) {
                    LOGGER.error("删除目录异常");
                }
            }

            return time;
        }
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList();
        /*"/PUBLIC/CAR_INTRODUCE/2015-09-21/19dcdc6c-4409-4361-88d7-8280c8d76254.mp3"
        list.add("http://192.168.100.142/public/M00/01/07/wKhkjVopEQGAK1ppAAAkIEH50yA987.m4a");
        list.add("http://192.168.100.142/public/M00/01/07/wKhkjVopEPKAFjSwAAAkIEH50yA681.m4a");
        list.add("http://192.168.100.142/public/M00/01/19/wKhkjVoqN6iAPv-vAAAZuLGVlT4837.m4a");*/

        new VoiceTimeNativeDomain().getVoiceTimeByUrl(list);
    }
}
