package com.liuyq.log2;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/4.20:46
 * @see
 */
public class LogUser<T> {

    protected T user;

    public LogUser(T user) {
        this.user = user;
    }

    public void recycle() {

    }

    static class LogThreadUser extends LogUser<Thread> {

        private static Map<Thread, LogThreadUser> threadUsers = new WeakHashMap<>();    // 弱引用，便于回收

        LogThreadUser(Thread thread) {
            super(thread);
        }

        public static LogThreadUser get(Thread thread) {
            LogThreadUser logThreadUser = threadUsers.get(thread);
            if (logThreadUser == null) {
                logThreadUser = new LogThreadUser(thread);
                threadUsers.put(thread, logThreadUser);
            }
            return logThreadUser;
        }
    }

    static class LogSessionUser extends LogUser<Session> {

        LogSessionUser(Session session) {
            super(session);
        }
    }
}
