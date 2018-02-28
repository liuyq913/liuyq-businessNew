package com.liuyq.designpatter.strategy.notifelse;

/**
 * Created by liuyq on 2017/12/13.
 */
public class Domain {
    public static void main(String[] args){
        Customer customer = new Customer();
        customer.buy(1000000.0);
        System.out.println(customer.getCalPrice());
    }
}
