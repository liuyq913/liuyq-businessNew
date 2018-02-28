package com.liuyq.designpatter.commandpattern;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liuyq on 2017/12/26.
 */
//产品经理（分配任务）
public class ProductManager {
    private static final Integer TASK_NUMBER_IN_DAY = 4 ;//一天要完成的任务

    private List<Task> taskList; //所有的任务

    public ProductManager(){
        super();
        this.taskList = Lists.newArrayList();
    }
    //接受任务
    public void receive(Task task){
        taskList.add(task);
    }

    //分配任务
    public void assign(){
        //将多余的应该放在copy里面
       Task[] copy = new Task[taskList.size()>TASK_NUMBER_IN_DAY ? taskList.size()- TASK_NUMBER_IN_DAY:0];
       //值分配前4个任务
       for(int i=0;i<TASK_NUMBER_IN_DAY && i<taskList.size();i++){
           taskList.get(i).handle();
       }
       //将多余任务放在copy中
        System.arraycopy(taskList.toArray(), TASK_NUMBER_IN_DAY > taskList.size() ? taskList.size() : TASK_NUMBER_IN_DAY, copy, 0, copy.length);
        taskList = Arrays.asList(copy);
    }
    //打印剩余的任务
    public void printTaskList(){
        if (taskList == null || taskList.size() == 0) {
            System.out.println("----------当前无任务--------");
            return;
        }
        System.out.println("---------当前剩下的任务列表--------");
        for (Task task : taskList) {
            System.out.println(task);
        }
        System.out.println("----------------------------------");
    }
}
