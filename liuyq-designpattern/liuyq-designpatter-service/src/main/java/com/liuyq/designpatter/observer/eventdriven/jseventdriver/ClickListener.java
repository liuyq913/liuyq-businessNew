package com.liuyq.designpatter.observer.eventdriven.jseventdriver;

import java.util.EventListener;

/**
 * Created by 刘宇青,DAY DAY UP on 2017-12-10.
 */
//点击监听器
public interface ClickListener extends EventListener{
    void click(ClickEvent clickEvent);
}
//双击监听器
interface DblClickListener extends EventListener{
    void dblClick(DblClickEvent dblClickEvent);
}
//鼠标移动监听器
interface MouseMoveListener extends EventListener{
    void mouseMove(MouseMoveEvent mouseMoveEvent);

}
