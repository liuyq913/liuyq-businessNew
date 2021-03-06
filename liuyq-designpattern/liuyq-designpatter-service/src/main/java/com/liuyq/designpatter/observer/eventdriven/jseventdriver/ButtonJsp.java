package com.liuyq.designpatter.observer.eventdriven.jseventdriver;

/**
 * Created by 刘宇青,DAY DAY UP on 2017-12-10.
 */
//模仿一个只有button的jsp页面
public class ButtonJsp {
    private Button button;

    public ButtonJsp(){
        super();
        button = new Button();//这个可以当做我们在页面写了一个button元素
        button.setId("submitButton");
        button.setValue("提交");//提交按钮
        button.setOnclick(new ClickListener() {//我们给按钮注册点击监听器
            public void click(ClickEvent clickEvent) {
                System.out.println("--------单击事件代码---------");
                System.out.println("if('表单合法'){");
                System.out.println("\t表单提交");
                System.out.println("}else{");
                System.out.println("\treturn false");
                System.out.println("}");
            }
        });
        button.setOnDblClick(new DblClickListener() {
            public void dblClick(DblClickEvent dblClickEvent) {
                //双击的话我们提示用户不能双击“提交”按钮
                    System.out.println("--------双击事件代码---------");
                    System.out.println("alert('您不能双击"+dblClickEvent.getButton().getValue()+"按钮')");
            }
        });
        button.setOnMouseMove(new MouseMoveListener() {
            //这个我们只简单提示用户鼠标当前位置，示例中加入这个事件
            //目的只是为了说明事件驱动中，可以包含一些特有的信息，比如坐标
            public void mouseMove(MouseMoveEvent mouseMoveEvent) {
                System.out.println("--------鼠标移动代码---------");
                System.out.println("alert('您当前鼠标的位置，x坐标为："+mouseMoveEvent.getX()+"，y坐标为："+mouseMoveEvent.getY()+"')");
            }
        });
    }

    public Button getButton(){
        return button;
    }
}
