package com.liuyq.designpatter.observer.advance;

/**
 * Created by Administrator on 2017-12-09.
 * 作者类，要继承自被观察者类
 */
public class Writer extends Observable{

    private String name; //作者的名称

    private String lastNovel;//记录作者最新发布的小说

    public Writer(String name){
        super();
        this.name = name;
        WriterManager.getInstance().add(this); //生成一个被观察者就往观察者管理器里放一个被观察者

    }

    //作者发布了小说了，要通知所有关注自己的读者
    public void addNovel(String novel){
        System.out.println(name+"发布了新书《"+novel+"》！");
        lastNovel = novel;//最新发布的书
        setChanged(); //发生改变
        notifyObservers();//通知所有观测他的人
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastNovel() {
        return lastNovel;
    }

    public void setLastNovel(String lastNovel) {
        this.lastNovel = lastNovel;
    }
}
