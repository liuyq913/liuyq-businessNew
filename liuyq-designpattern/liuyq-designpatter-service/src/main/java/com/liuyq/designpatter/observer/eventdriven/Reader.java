package com.liuyq.designpatter.observer.eventdriven;

/**
 * Created by 刘宇青,DAY DAY UP on 2017-12-10.
 */
public class Reader implements WriterListener{

    private String name;

    public Reader(String name){
        super();
        this.name = name;
    }

    //关注一个作者，关注则代表吧自己加到作者的监听器列表里面
    public void subscribe(String writerName){
        WriterManager.getInstance().getWriter(writerName).registerListener(this);
    }
    //取消关注，把自己移除
    public void upSubscribe(String writerName){
        WriterManager.getInstance().getWriter(writerName).unregisterListener(this);
    }
    //作者发布了书，观察者做出回应
    public void addNovel(WriterEvent writerEvent) {
       Writer writer = (Writer) writerEvent.getSource();
        System.out.println(name+"知道"+writer.getName()+"发布了新书"+writer.getLastNovel());
    }
}
