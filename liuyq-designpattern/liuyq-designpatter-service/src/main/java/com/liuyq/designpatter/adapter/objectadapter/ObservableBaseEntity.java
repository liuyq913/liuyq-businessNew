package com.liuyq.designpatter.adapter.objectadapter;

import com.liuyq.designpatter.observer.advance.Observable;
import com.liuyq.designpatter.observer.advance.Observer;

/**
 * Created by liuyq on 2017/12/15.
 *
 *
 * 对基类进行适配，然后user等子类就直接继承ObservableBaseEntity就可以成为（被观察者）
 */
public class ObservableBaseEntity extends BaseEntity{

    private Observable observable = new Observable();

    public synchronized void addObserver(Observer o) {
        observable.addObserver(o);
    }

    public synchronized void deleteObserver(Observer o) {
        observable.deleteObserver(o);
    }

    public void notifyObservers() {
        observable.notifyObservers();
    }

    public void notifyObservers(Object arg) {
        observable.notifyObservers(arg);
    }

    public synchronized void deleteObservers() {
        observable.deleteObservers();
    }

    protected synchronized void setChanged() {
        observable.setChanged();
    }

    protected synchronized void clearChanged() {
        observable.clearChanged();
    }

    public synchronized boolean hasChanged() {
        return observable.hasChanged();
    }

    public synchronized int countObservers() {
        return observable.countObservers();
    }
}
