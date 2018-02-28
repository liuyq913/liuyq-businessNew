package com.liuyq.designpatter.observer.eventdriven;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 刘宇青 DAY DAY UP on 2017-12-09.
 */
//管理器，保持一份独有的作者列表
public class WriterManager {
    private Map<String,Writer> writerMap = new HashMap<String,Writer>();

    //添加作者
    public void add(Writer writer){
        writerMap.put(writer.getName(), writer);
    }
    //根据作者姓名获取作者
    public Writer getWriter(String name){
        return writerMap.get(name);
    }

    //单例
    private WriterManager(){}

    public static WriterManager getInstance(){
       return  WriterManagerInstance.writerManager;
    }

    private static class WriterManagerInstance{
       private static WriterManager writerManager = new WriterManager();
    }
}
