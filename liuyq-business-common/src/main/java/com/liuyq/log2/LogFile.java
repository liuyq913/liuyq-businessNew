package com.liuyq.log2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/4.20:49
 * @see
 */
public class LogFile implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(LogFile.class);

    private static final String windowsFileSeparator = "\\";
    static final boolean isWindows = isWindows();
    private static volatile LogFileWatcher logFileWatcher;

    private LogConfig logConfig;
    private LogCache logCache;

    private Path path;
    private FileChannel fileChannel;
    private long lastModifyTimeMark;

    public LogFile(String logPath) {
        this.path = Paths.get(logPath);
        init();
    }

    public LogFile(URI logURI) {
        this.path = Paths.get(logURI);
        init();
    }

    private void init() {
        try {
            this.lastModifyTimeMark = Files.getLastModifiedTime(path).toMillis();
        } catch (Exception e) {
            log.warn("LogFile init failed for {}, {}", e, e.getMessage());
        }
    }

    public void init(LogConfig logConfig, LogCache logCache) throws IOException {
        setLogConfig(logConfig);
        this.logCache = logCache;
        reuse();
    }

    public void setLogConfig(LogConfig logConfig) {
        this.logConfig = logConfig;
        if (! isWindows) {
            registryToWatchService();
            logFileWatcher.addLogFile(this);
        }
    }

    public void setLogCache(LogCache logCache) {
        this.logCache = logCache;
    }

    public long getLastModifyTimeMark() {
        return lastModifyTimeMark;
    }

    private void registryToWatchService() {
        if (logFileWatcher == null) {
            synchronized (LogFile.class) {
                if (logFileWatcher == null) {
                    logFileWatcher = new LogFileWatcher(logConfig.getLogDirPath());
                    logFileWatcher.start();
                }
            }
        }
    }

    /**
     * 初始化/重置{@link FileChannel}
     * @throws IOException
     */
    void reuse() throws IOException {
        close();
        this.fileChannel = FileChannel.open(path, StandardOpenOption.READ);
    }

    public long getFileSize() throws IOException {
        return Files.size(path);
    }

    /**
     * 日志文件是否被重置
     * @return
     */
    public boolean isRebuild() throws IOException {
        if (isWindows) {
            return checkLastModifyTime(getLastModifyTime());
        } else {
            return logFileWatcher.isRebuild(this);
        }
    }

    public long getLastModifyTime() throws IOException {
        return Files.getLastModifiedTime(path).toMillis();
    }

    private boolean checkLastModifyTime(long lastModifyTime) throws IOException {
        if ((lastModifyTime ^ lastModifyTimeMark) != 0) {
            reuse();
            this.lastModifyTimeMark = lastModifyTime;
            return true;
        }
        return false;
    }

    /**
     * 读取指定位置的数据，通过预读，提高性能
     * @param pos
     * @return
     * @throws IOException
     */
    public PooledByteBuffer read(long pos) throws IOException {
        ByteBuffer buffer = logCache.getReadBuffer().buffer();
        buffer.clear();
        fileChannel.read(buffer, pos);
        return logCache.getReadBuffer();
    }

    @Override
    public void close() throws IOException {
        if (fileChannel != null) fileChannel.close();
    }

    public void recycle() {
        try {
            close();
            if (logFileWatcher != null) logFileWatcher.recycle(this);
        } catch (Exception e) {
            log.warn("LogFile {} recycle failed for {}, {}", path.toString(), e, e.getMessage());
        }
    }

    /**
     * 获取默认的日志文件的路径
     * @return
     * @throws IOException
     */
    public static List<String> getDefaultLogPaths() throws IOException {
        try {
            Path dir = Paths.get(getDefaultLogDir());
            if (Files.isDirectory(dir))
                return Files.list(dir).filter(p -> ! Files.isDirectory(p)).map(p -> p.toString()).collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("Log directory init failed for {}, {}", e, e.getMessage());
        }
        return null;
    }

    /**
     * 获取默认的日志所在目录
     * @return
     */
    public static String getDefaultLogDir() {
        String logDir = null;
        try {
            URI uri = LogFile.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            String path = uri.toString();
            log.info("Log current path is {}", path);
            Pattern jarPath = Pattern.compile("^jar:file:(.*?)!/.*?!/$");    // 去除jar包内的部分URI
            Matcher matcher = jarPath.matcher(path);
            if (matcher.find()) logDir = Paths.get(matcher.group(1)).getParent().getParent().toString() + "/logs";
            else logDir = Paths.get(uri).toAbsolutePath().getParent().getParent().getParent().getParent().toString() + "/logs";
            log.info("Log directory is {}", logDir);
        } catch (Exception e) {
            log.warn("Log directory init failed for {}, {}", e, e.getMessage());
        }
        return logDir;
    }

    public static boolean isWindows() {
        if (isWindows0() || isWindows1()) {
            log.info("Platform is windows !");
            return true;
        }
        log.info("Platform is not windows !");
        return false;
    }

    public static boolean isWindows0() {    // 不兼容JDK11
        try {
            Class clazz = Class.forName("sun.nio.fs.WindowsPath", false, ClassLoader.getSystemClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            log.warn("");
        }
        return false;
    }

    public static boolean isWindows1() {
        if (File.separator.equals(windowsFileSeparator)) return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogFile logFile = (LogFile) o;

        if (lastModifyTimeMark != logFile.lastModifyTimeMark) return false;
        if (logConfig != null ? !logConfig.equals(logFile.logConfig) : logFile.logConfig != null) return false;
        if (logCache != null ? !logCache.equals(logFile.logCache) : logFile.logCache != null) return false;
        if (path != null ? !path.equals(logFile.path) : logFile.path != null) return false;
        return fileChannel != null ? fileChannel.equals(logFile.fileChannel) : logFile.fileChannel == null;
    }

    @Override
    public int hashCode() {
        int result = logConfig != null ? logConfig.hashCode() : 0;
        result = 31 * result + (logCache != null ? logCache.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (fileChannel != null ? fileChannel.hashCode() : 0);
        result = 31 * result + (int) (lastModifyTimeMark ^ (lastModifyTimeMark >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "LogFile{" +
                "path=" + path +
                '}';
    }

    /**
     * 监听文件的创建和删除，主要用于linux，windows直接轮询上次修改时间
     */
    static class LogFileWatcher implements Runnable {

        private static final Logger log = LoggerFactory.getLogger(LogFileWatcher.class);

        private Map<String, List<LogFile>> logFiles = new HashMap<>();
        private Map<LogFile, Boolean> logFileRebuildMap = new HashMap<>();
        private Path logDirPath;

        private WatchService watchService;
        private boolean deleteMark; // 是否有删除事件
        private boolean createMark; // 是否有创建事件

        LogFileWatcher(String logDirPath) {
            this(Paths.get(logDirPath));
        }

        LogFileWatcher(URI logDirPath) {
            this(Paths.get(logDirPath));
        }

        LogFileWatcher(Path logDirPath) {
            this.logDirPath = logDirPath;
        }

        private void register() {
            try {
                // 注册监听
                watchService = logDirPath.getFileSystem().newWatchService();
                logDirPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
                log.info("LogFileWatcher register logDir {} success !", logDirPath);
            } catch (IOException e) {
                log.warn("LogFileWatcher register failed for {}, {}", e, e.getMessage());
            }
        }

        @Override
        public void run() {
            WatchKey watchKey = null;
            while (true) {
                try {
                    watchKey = watchService.take(); // 阻塞

                    List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                    for (final WatchEvent<?> event : watchEvents) {
                        WatchEvent<Path> watchEvent = (WatchEvent<Path>) event;
                        WatchEvent.Kind<Path> kind = watchEvent.kind();
                        if( kind == StandardWatchEventKinds.ENTRY_CREATE){
                            Path watchable = ((Path) watchKey.watchable()).resolve(watchEvent.context());
                            if (Files.isDirectory(watchable)) {
                                watchable.resolve(watchEvent.context()).register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
                            }
                            this.createMark = true;
                            doRun(watchable);
                        } else if (StandardWatchEventKinds.ENTRY_DELETE.equals(kind)) {
                            Path watchable = ((Path) watchKey.watchable()).resolve(watchEvent.context());
                            if (Files.isDirectory(watchable)) {
                                watchable.resolve(watchEvent.context()).register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
                            }
                            this.deleteMark = true;
                        }
                    }
                } catch (Exception e) {
                    log.warn("LogFile watch excepted for {}, {}", e, e.getMessage());
                }finally {
                    if(watchKey != null) watchKey.reset();  // 用于下一轮监听
                }
            }
        }

        private void doRun(Path watchable) {
            String path = watchable.toString();
            log.info("deleteMark: {}, createMark: {}, logPath: {}", deleteMark, createMark, path);
            if (deleteMark && createMark) {
                List<LogFile> logFileList = logFiles.get(path);
                if (logFileList != null) {
                    long currentTime = System.currentTimeMillis();
                    logFileList.forEach(logFile -> {
                        try {
                            logFile.lastModifyTimeMark = currentTime;
                            logFile.reuse();
                            logFileRebuildMap.put(logFile, true);
                            log.info("LogFile {} rebuild !", logFile);
                        } catch (IOException e) {
                            log.warn("LogFile {} rebuild failed for {}, {}", logFile, e, e.getMessage());
                        }
                    });
                }
            }
        }

        void start() {
            register();
            Thread thread = new Thread(this, "LogFileWatcher");
            thread.setPriority(3);
            thread.setDaemon(true);
            thread.start();
            log.info("LogFileWatcher start success !");
        }

        void addLogFile(LogFile logFile) {
            String path = logFile.path.toString();
            List<LogFile> logFileList = logFiles.get(path);
            if (logFileList != null) {
                logFileList.add(logFile);
            } else {
                logFileList = new ArrayList<>();
                logFileList.add(logFile);
                logFiles.put(path, logFileList);
            }
        }

        boolean isRebuild(LogFile logFile) {
            Boolean isRebuild = logFileRebuildMap.get(logFile);
            if (isRebuild != null && isRebuild) {
                logFileRebuildMap.put(logFile, false);
                return true;
            }
            return false;
        }

        void recycle(LogFile logFile) {
            List<LogFile> logFileList = logFiles.get(logFile.path.toString());
            if (logFileList != null) logFileList.remove(logFile);
            logFileRebuildMap.remove(logFile);
        }
    }
}
