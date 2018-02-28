package com.liuyq.common.util;

import com.liuyq.common.exception.LiuyqException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by liuyq on 2017/10/30.
 */
public class CheckUtil {
    private static Logger LOGGER = null;

    public CheckUtil() {
    }

    public static void checkStatus(boolean status, String message) throws LiuyqException {
        if(status) {
            throw new LiuyqException(message);
        }
    }

    public static <T> void checkListSize(List<T> list, String message) throws LiuyqException {
        checkStatus(null == list || 0 == list.size(), "列表信息不允许为空");
        if(list.size() != 1) {
            throw new LiuyqException(message);
        }
    }

    public static <T> Boolean checkListIsNullOrIsEmpty(List<T> list) {
        return null != list && 0 != list.size()?Boolean.FALSE:Boolean.TRUE;
    }

    public static void checkStatusAndPrintWarnLog(Class clazz, String message) {
        LOGGER = LoggerFactory.getLogger(clazz);
        LOGGER.warn(message);
    }

    public static void checkStatusAndPrintErrorLog(Class clazz, String message) {
        LOGGER = LoggerFactory.getLogger(clazz);
        LOGGER.error(message);
    }
}
