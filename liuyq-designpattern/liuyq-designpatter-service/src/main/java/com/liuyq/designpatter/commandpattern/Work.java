package com.liuyq.designpatter.commandpattern;

import com.liuyq.common.util.DateUtil;

import java.util.Date;

/**
 * Created by liuyq on 2017/12/26.
 */
public class Work {
    public static void main(String[] args) {
       /* Programmer xiaozuo = new Programmer("小左");
        ProductManager productManager = new ProductManager();

        Salesman salesmanA = new Salesman("A");
        Salesman salesmanB = new Salesman("B");
        Salesman salesmanC = new Salesman("C");
        Salesman salesmanD = new Salesman("D");

        salesmanA.putDemand(productManager, xiaozuo);
        salesmanB.putDemand(productManager, xiaozuo);
        salesmanB.putBug(productManager, xiaozuo);
        salesmanC.putDemand(productManager, xiaozuo);
        salesmanC.putProblem(productManager, xiaozuo);
        salesmanD.putDemand(productManager, xiaozuo);

        System.out.println("第一天产品经理分配任务");
        productManager.assign();
        productManager.printTaskList();
        System.out.println("第二天产品经理分配任务");
        productManager.assign();
        productManager.printTaskList();*/

        Date monday = DateUtil.zeroConvertTime(DateUtil.getCycleData(new Date(), DateUtil.cycle_w, 2));
        System.out.println(monday);

    }
}
