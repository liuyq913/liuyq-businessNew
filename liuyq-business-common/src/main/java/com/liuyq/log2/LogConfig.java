package com.liuyq.log2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/4.20:39
 * @see
 */
@Configuration
public class LogConfig {

    private static final Logger log = LoggerFactory.getLogger(LogConfig.class);

    @Value("${log.reader.readBufferSize:4096}")
    private int readBufferSize; // 读数据的缓冲区的大小，默认4K

    @Value("${log.reader.initLineBufferSize:65536}")
    private int initLineBufferSize; // 缓存日志行字符串的缓冲区的初始大小，默认64K，可以缓存300-700行左右的日志

    @Value("${log.reader.maxLineBufferSize:131072}")
    private int maxLineBufferSize;  // 日志行最大缓冲区，超过会被回收，默认128K，暂时无效，看心情改

    @Value("${log.reader.sessionTime:1800000}")
    private long sessionTime;   // 会话超时时间，默认30分钟，<0 永不超时

    @Value("${log.reader.threadTime:1800000}")
    private long threadTime;   // ThreadLocal缓存超时时间，默认30分钟，<0 永不超时

    @Value("${log.reader.cache.recycle.scheduleTime:1800000}")
    private long cacheRecycleScheduleTime;  // 缓存回收的周期时间，默认30分钟，<0 永不执行

    @Value("${log.reader.logPath:}")
    private String logPath; // 日志文件的路径，多个路径使用,分割，格式为xx=xxxx,xx=xxxx

    @Value("${log.reader.logDirPath:}")
    private String logDirPath; // 日志所在目录的路径，当系统启动时，日志文件部分没有初始化的时候可以通过指定目录的方式，重新获取日志文件信息，是一种补救措施

    @Value("${log.reader.logSuffix:.log}")
    private String logSuffix;   // 日志文件的后缀，默认为.log

    private Map<String, String> logPaths;

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    public int getInitLineBufferSize() {
        return initLineBufferSize;
    }

    public void setInitLineBufferSize(int initLineBufferSize) {
        this.initLineBufferSize = initLineBufferSize;
    }

    public int getMaxLineBufferSize() {
        return maxLineBufferSize;
    }

    public void setMaxLineBufferSize(int maxLineBufferSize) {
        this.maxLineBufferSize = maxLineBufferSize;
    }

    public long getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(long sessionTime) {
        this.sessionTime = sessionTime;
    }

    public long getThreadTime() {
        return threadTime;
    }

    public void setThreadTime(long threadTime) {
        this.threadTime = threadTime;
    }

    public long getCacheRecycleScheduleTime() {
        return cacheRecycleScheduleTime;
    }

    public void setCacheRecycleScheduleTime(long cacheRecycleScheduleTime) {
        this.cacheRecycleScheduleTime = cacheRecycleScheduleTime;
    }

    public String getLogDirPath() {
        if ("".equals(logDirPath)) {    // 获取默认的日志目录
            logDirPath = LogFile.getDefaultLogDir();
        }
        return logDirPath;
    }

    public void setLogDirPath(String logDirPath) {
        this.logDirPath = logDirPath;
    }

    public Map<String, String> getLogPaths() throws IOException {
        if (logPaths != null) return logPaths;

        Map<String, String> logPathMap = new HashMap<>(2);
        if ("".equals(logPath)) {   // 使用默认配置
            List<String> logPaths = LogFile.getDefaultLogPaths();
            if (logPaths != null && logPaths.size() > 0) {
                for (String path : logPaths) {
                    if (path.contains("error")) logPathMap.put("error", path);
                    else if (path.contains("info")) logPathMap.put("info", path);
                }
            }
        } else {    // 解析配置项
            parseLogPaths(logPathMap);
        }
        this.logPaths = logPathMap;
        return logPathMap;
    }

    private void parseLogPaths(Map<String, String> logPathMap) {
        int start = 0, internal = 0, end, logPathLen = logPath.length();
        for (int i = 0; i < logPathLen; i++) {
            char chr = logPath.charAt(i);
            switch (chr) {
                case '=': internal = i; break;
                case ',':
                    end = i;
                    if (end > internal && internal > start)
                        logPathMap.put(logPath.substring(start, internal), logPath.substring(internal + 1, end));
                    start = end + 1;
                    break;
                default: break;
            }
        }

        if (internal > start && internal < logPathLen) {
            logPathMap.put(logPath.substring(start, internal), logPath.substring(internal + 1, logPathLen));
        }
    }

    public String getLogSuffix() {
        return logSuffix;
    }

    public void setLogSuffix(String logSuffix) {
        this.logSuffix = logSuffix;
    }
}
