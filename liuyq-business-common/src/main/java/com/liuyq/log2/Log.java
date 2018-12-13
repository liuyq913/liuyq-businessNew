package com.liuyq.log2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/5.11:10
 * @see
 */
@RestController
@RequestMapping("lccx/log")
public class Log {

    private static final String LOG_SESSIONID = "LogSessionId";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String TEXT_PLAIN_UTF_8 = "text/plain;charset=UTF-8";
    private static final String HEAD = "head";
    private static final String TAIL = "tail";

    @Resource
    private LogHeader logHeader;

    @Resource
    private LogTailer logTailer;

    @RequestMapping("head")
    public void head(Integer line, String path, HttpServletResponse response) throws Exception {
        if (line == null) line =10;
        ByteWrapper wrapper = logHeader.head(line, path);
        response.addHeader(CONTENT_TYPE, TEXT_PLAIN_UTF_8); // 中文不乱码
        response.getOutputStream().write(wrapper.bytes(), wrapper.offset(), wrapper.len());
    }

    @RequestMapping("tail")
    public void tail(Integer line, String path, Integer sessionId,
                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (line == null) line = 10;
        if (sessionId == null) {    // 优先使用queryString
            String sessionIdStr = request.getHeader(LOG_SESSIONID);
            if (sessionIdStr != null) sessionId = Integer.parseInt(sessionIdStr);
        }
        if (sessionId != null) response.addHeader(LOG_SESSIONID, sessionId + "");   // 响应头要在输出流之前
        response.addHeader(CONTENT_TYPE, TEXT_PLAIN_UTF_8); // 中文不乱码
        ByteWrapper wrapper = logTailer.tail(line, path, sessionId);
        response.getOutputStream().write(wrapper.bytes(), wrapper.offset(), wrapper.len());
    }

    @RequestMapping("session")
    public int session(HttpServletResponse response) throws Exception { // 使用会话机制需要先获取sessionId，一个sessionId只对应一个日志文件
        int sessionId = Session.create().getSessionId();
        response.addHeader(LOG_SESSIONID, sessionId + "");
        return sessionId;
    }

    @RequestMapping("cache/global")
    public String cacheGlobal(String path, String type) throws Exception { // 用户查询缓存的部分全局配置信息
        if (path == null || type == null)
            return "path should be set as info/error/... and type should be set as head/tail/...";
        if (HEAD.equals(type)) return logHeader.getGlobalLogCacheByPath(path);
        else if (TAIL.equals(type)) return logTailer.getGlobalLogCacheByPath(path);
        return "path should be set as info/error/... and type should be set as head/tail/...";
    }
}
