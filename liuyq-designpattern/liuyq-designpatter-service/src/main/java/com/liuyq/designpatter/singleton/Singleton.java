package com.liuyq.designpatter.singleton;

/**
 * Created by liuyq on 2017/12/4.
 * 单例模式
 *
 * 1.静态实例，带有static关键字的属性在每一个类中都是唯一的。

 2.限制客户端随意创造实例，即私有化构造方法，此为保证单例的最重要的一步。

 3.给一个公共的获取实例的静态方法，注意，是静态的方法，因为这个方法是在我们未获取到实例的时候就要提供给客户端调用的，所以如果是非静态的话，那就变成一个矛盾体了，因为非静态的方法必须要拥有实例才可以调用。

 4.判断只有持有的静态实例为null时才调用构造方法创造一个实例，否则就直接返回。
 */
public class Singleton {
    private static Singleton singleton;
    //私有构造
    private Singleton(){}

   /* public static Singleton getInstance(){ (高并发不安全，会创建多个实例)
        分析：当线程A 进来的时候，实例是null,开始创建，还没创建完的时候，线程B也进来了，事例还是null,又会去创建一次，所有导致会有多个实例
        if(singleton == null){
            return new Singleton();
        }
        return singleton;
    }
*/


    /*public synchronized  static Singleton getInstance(){ 这样的话第一个线程进来后所有的线程就全部挂起等待时间过长
        if(singleton == null){
            return new Singleton();
        }
        return singleton;
    }*/

      /* 首先要明白在JVM创建新的对象时，主要要经过三步。

            1.分配内存

            2.初始化构造器

            3.将对象指向分配的内存的地址

    这种顺序在上述双重加锁的方式是没有问题的，因为这种情况下JVM是完成了整个对象的构造才将内存的地址交给了对象。但是如果2和3步骤是相反的（2和3可能是相反的是因为JVM会针对字节码进行调优，而其中的一项调优便是调整指令的执行顺序），就会出现问题了。

    因为这时将会先将内存地址赋给对象，针对上述的双重加锁，就是说先将分配好的内存地址指给synchronizedSingleton，然后再进行初始化构造器，这时候后面的线程去请求getInstance方法时，会认为synchronizedSingleton对象已经实例化了，直接返回一个引用。如果在初始化构造器之前，这个线程使用了synchronizedSingleton，就会产生莫名的错误。*/
    public static Singleton getInstance(){
        if(singleton == null ){
            synchronized (Singleton.class){  //能对类名的引用取得在内存中该类型class对象的引用
                if(singleton == null){
                    return new Singleton();
                }
            }
        }
        return singleton;
    }
}
