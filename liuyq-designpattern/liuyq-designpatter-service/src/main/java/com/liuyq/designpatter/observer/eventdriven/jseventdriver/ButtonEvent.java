package com.liuyq.designpatter.observer.eventdriven.jseventdriver;

import java.util.EventObject;

/**
 * Created by 刘宇青,DAY DAY UP on 2017-12-10.
 */
//按钮事件基类
public abstract class ButtonEvent extends EventObject{
    public ButtonEvent(Object source) {
        super(source);
    }

    public Button getButton(){
        return (Button) super.getSource();
    }
}

//点击事件
class ClickEvent extends ButtonEvent{

    public ClickEvent(Object source) {
        super(source);
    }
}

//双击事件
class DblClickEvent extends ButtonEvent{

    public DblClickEvent(Object source) {
        super(source);
    }
}

//鼠标移动事件
class MouseMoveEvent extends ButtonEvent{
    //鼠标移动事件比较特殊，因为它需要告诉监听器鼠标当前的坐标是在哪，我们记录为x,y
    private int x;
    private int y;

    public MouseMoveEvent(Object source, int x, int y) {
        super(source);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
