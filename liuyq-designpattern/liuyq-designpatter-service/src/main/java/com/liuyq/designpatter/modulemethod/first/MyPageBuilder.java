package com.liuyq.designpatter.modulemethod.first;

import org.junit.Test;

/**
 * Created by liuyq on 2017/12/15.
 */
public class MyPageBuilder extends AbstractPageBuilder{
    //子类只需要实现两个模板方法就行
    protected void appendHead(StringBuffer stringBuffer) {
        stringBuffer.append("<head><title>你好</title></head>");
    }

    protected void appendBody(StringBuffer stringBuffer) {
        stringBuffer.append("<head><h1>你好,世界</h1></head>");
    }

    @Test
    public void test(){
        PageBuilder pageBuilder = new MyPageBuilder();
        System.out.println(pageBuilder.builHtml());
    }
}
