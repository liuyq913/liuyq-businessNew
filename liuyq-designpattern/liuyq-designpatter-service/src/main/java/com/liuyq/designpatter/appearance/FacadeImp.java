package com.liuyq.designpatter.appearance;

/**
 * Created by liuyq on 2017/12/25.
 */
public class FacadeImp implements Facade{
    private Sub1 sub1;

    private Sub2 sub2;

    private Sub3 sub3;

    public FacadeImp(){
        super();
        this.sub1 = new SubImp1();
        this.sub2 = new SubImp2();
        this.sub3 = new SubImp3();
    }

    public FacadeImp(Sub1 sub1, Sub2 sub2, Sub3 sub3){
        this.sub1 = sub1;
        this.sub2 = sub2;
        this.sub3 = sub3;
    }

    @Override
    public void function12() {
        sub1.function();
        sub2.function();
    }

    @Override
    public void function23() {
        sub2.function();
        sub3.function();
    }

    @Override
    public void function13() {
        sub1.function();
        sub3.function();
    }
}
