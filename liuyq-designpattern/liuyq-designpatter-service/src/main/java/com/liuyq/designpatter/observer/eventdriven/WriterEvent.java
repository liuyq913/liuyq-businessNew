package com.liuyq.designpatter.observer.eventdriven;

import java.util.EventObject;

/**
 * Created by 刘宇青,DAY DAY UP on 2017-12-10.
 *
 * 如果我们采用事件驱动模型去分析上面的例子，那么作者就是事件源，
 * 而读者就是监听器，依据这个思想，我们把上述例子改一下，
 * 首先我们需要自定义我们自己的监听器和事件。所以我们定义如下作者事件。
 */
//每个事件都包含事件源
public class WriterEvent extends EventObject {

    public WriterEvent(Writer o) {
        super(o);
    }

    public Writer getWriter(){
        return (Writer) super.getSource();
    }
}
