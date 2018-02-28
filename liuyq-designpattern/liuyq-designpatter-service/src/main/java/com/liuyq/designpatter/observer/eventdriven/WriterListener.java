package com.liuyq.designpatter.observer.eventdriven;

import java.util.EventListener;

/**
 * Created by 刘宇青,DAY DAY UP on 2017-12-10.
 *
 * 监听器（相当于观察者）
 */
public interface WriterListener extends EventListener {
    //发布新书的相应
    void addNovel(WriterEvent writerEvent);
}
