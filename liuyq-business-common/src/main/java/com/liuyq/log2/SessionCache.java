package com.liuyq.log2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/5.11:02
 * @see
 */
public class SessionCache extends LogCache {

    private static final Logger log = LoggerFactory.getLogger(SessionCache.class);

    private static Map<Session, SessionCache> sessionLocal = new HashMap<>();
    private static Map<Session, SessionCache> recycledSessionLocal = new WeakHashMap<>();

    private Session session;

    public SessionCache(LogReader logReader, LogConfig logConfig, LogFile logFile, String logPath) {
        super(logReader, logConfig, logFile, logPath, true);
    }

    public SessionCache(LogReader logReader, LogConfig logConfig, LogFile logFile, String logPath, boolean addToTask) {
        super(logReader, logConfig, logFile, logPath, addToTask);
    }

    public static SessionCache get(Session session, LogReader logReader, String logPath) throws IOException {
        return get(session, logReader, logPath, true);
    }

    public static SessionCache get(Session session, LogReader logReader, String logPath, boolean addToTask) throws IOException {
        SessionCache sessionCache = sessionLocal.get(session);
        if (sessionCache == null) {
            try {
                LogFile logFile = new LogFile(logPath);
                sessionCache = new SessionCache(logReader, logReader.logConfig, logFile, logPath, addToTask);
                sessionCache.session = session;
                session.setLogConfig(logReader.logConfig);
                long fileSize = logFile.getFileSize();
                sessionCache.setLastPos(fileSize);
                sessionCache.setLastFileSize(fileSize);
                sessionCache.setLastModifyTime(logFile.getLastModifyTime());
                sessionLocal.put(session, sessionCache);
                logFile.init(logReader.logConfig, sessionCache);
            } catch (IOException e) {
                log.warn("Log Cache-SessionCache init failed for {}, {}", e, e.getMessage());
            }
        }
        return sessionCache;
    }

    @Override
    protected long getCacheTime() {
        return session.getSessionTime();
    }

    @Override
    public void recycle() {
        super.recycle();
        session.recycle();
        // 清除超时的session
        sessionLocal.forEach((session, cache) -> {
            long sessionTime = session.getSessionTime();
            if (sessionTime > 0 && sessionTime < lastUseTime - cache.lastUseTime) {
                session.remove();
                recycledSessionLocal.put(session, cache);
            }
        });
        recycledSessionLocal.forEach((session, cache) -> sessionLocal.remove(session));
        recycledSessionLocal.clear();
    }

    @Override
    protected void doRecycleSchedule() {
        session.remove();
        sessionLocal.remove(session);
    }
}
