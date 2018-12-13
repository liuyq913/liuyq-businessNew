package com.liuyq.log2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/5.10:54
 * @see
 */
public class Session {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    private static Map<Integer, Session> sessions = new ConcurrentHashMap<>(); // Session池，所有的session都缓存在这里

    private int sessionId;  // 会话ID

    AtomicBoolean using;    // 是否被使用
    Long sessionTime;   // 会话超时时间

    private LogConfig logConfig;

    private Session(Integer sessionId) {
        this.sessionId = sessionId;
        this.using = new AtomicBoolean(true);
    }

    /**
     * 创建一个未被使用的session
     * @return
     */
    public static Session create() {
        if (Integer.MAX_VALUE == ID_GENERATOR.get()) {
            synchronized (Session.class) {
                if (Integer.MAX_VALUE == ID_GENERATOR.get()) {
                    ID_GENERATOR.compareAndSet(Integer.MAX_VALUE, 0);
                }
            }
        }
        int sessionId = ID_GENERATOR.getAndIncrement();
        Session session = new Session(sessionId);
        sessions.put(sessionId, session);
        return session;
    }

    public static Session get(Integer sessionId) {
        Session session = sessions.get(sessionId);
        /*if (session == null) {
            session = new Session(sessionId);
            sessions.put(sessionId, session);
        } else {
            if (! session.touch()) return null;
        }*/
        return session;
    }

    public int getSessionId() {
        return sessionId;
    }

    public long getSessionTime() {
        if (sessionTime == null) sessionTime = logConfig.getSessionTime();
        return sessionTime;
    }

    public void setLogConfig(LogConfig logConfig) {
        this.logConfig = logConfig;
    }

    public boolean touch() {
        if (using.compareAndSet(false, true)) {
            return true;
        }
        return false;
    }

    public void recycle() {
        //using.compareAndSet(true, false);
    }

    public void remove() {
        sessions.remove(this);
    }
}
