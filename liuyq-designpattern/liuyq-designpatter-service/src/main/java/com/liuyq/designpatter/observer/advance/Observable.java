package com.liuyq.designpatter.observer.advance;

import java.util.Vector;

/**
 * Created by Administrator on 2017-12-09.
 * 被观察者
 */
public class Observable {
    //这里一个改变标识，来标记被观察这有没有改变
    private boolean changed = false;

    //持有观察者列表
    //Vector类实现了一个动态数组。和ArrayList和相似，
    // 但是两者是不同的：
    //              Vector是同步访问的。
    //              Vector包含了许多传统的方法，这些方法不属于集合框架。
    private Vector vector;

    public Observable(){
        vector = new Vector(); //和ArrayList一样，默认10个
    }
    //添观察者
    public synchronized void addObserver(Observer o){
        if(o == null) throw  new NullPointerException();
        if(!vector.contains(o)){
            vector.addElement(o); //将指定的组件添加到此向量的末尾，将其大小增加 1。
        }
    }
    //删除观察者
    public synchronized void deleteObserver(Observer o) {
        vector.removeElement(o);
    }
    public void notifyObservers() {
        notifyObservers (null);
    }
    //通知所有观察者，被观察者发生变化就，可以执行update方法
    public void notifyObservers(Object arg){
        //一个临时的数组，用于并发访问被观察者时，留住观察者列表的当前状态，这种处理方式其实也算是一种设计模式，即备忘录模式。
        Object[] arrLocal;
        //这个代码块，表示在获取观察这列表是，该对象是被锁定的
        //也就说，在我读取到被观察者列表之前，不允许其他线程改变观察这列表
        synchronized (this){
            //如果(被观察这)没变化，无需通知其他观察者，直接返回
            if(!changed)return;
            //改变了
            //将当前观察者列表放在临时数组
            arrLocal = vector.toArray();
            //将改变标识重新置回未改变
            clearChanged();
        }
        //注意这个for循环没有在同步块，此时已经释放了被观察者的锁，其他线程可以改变观察者列表
        //但是这并不影响我们当前进行的操作，因为我们已经将观察者列表复制到临时数组
        //在通知时我们只通知数组中的观察者，当前删除和添加观察者，都不会影响我们通知的对象
        for(int i = arrLocal.length-1;i>=0;i--){
            try {
                ((Observer) arrLocal[i]).update(this, arg);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    //删除所有观察这
    public synchronized void deleteObservers(){
        vector.removeAllElements();
    }
    //标识被观察者被改变过
    public synchronized void setChanged(){
        changed = true;
    }
    //标识被观察者没改变
    public synchronized void clearChanged() {
        changed = false;
    }
    //返回被观察者是否改变
    public synchronized boolean hasChanged() {
        return changed;
    }
    //返回观察者数量
    public synchronized int countObservers() {
        return vector.size();
    }
}
