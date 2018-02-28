package com.liuyq.designpatter.appearance;

/**
 * Created by liuyq on 2017/12/25.
 */
public class Client {
    public static void main(String[] args) {
        //外观模式调用方式  （外观模式，客户端只依赖外观的一个接口。原始模式需要依赖整个子类系统，所以外观模式主要解决的是类之间的耦合过于复杂。）
        Facade facade = new FacadeImp();
        facade.function12();
        facade.function13();
        facade.function23();

        System.out.println("-------------以下原始方式--------------");
        Sub1 sub1 = new SubImp1();
        Sub2 sub2 = new SubImp2();
        Sub3 sub3 = new SubImp3();
        sub1.function();
        sub2.function();
        System.out.println("-------------------------");
        sub2.function();
        sub3.function();
        System.out.println("-------------------------");
        sub1.function();
        sub2.function();
        sub3.function();

    }
}
