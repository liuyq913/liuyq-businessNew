package com.liuyq.springtest.aop.service;

import com.liuyq.springtest.aop.annotation.UserLog;
import com.liuyq.springtest.aop.model.Model1;
import com.liuyq.springtest.aop.model.ModelSub;
import org.springframework.stereotype.Service;

/**
 * Created by liuyq on 2019/2/19.
 */
@Service("userService")
public class UserService {

    @UserLog(description = "用户新增")
    public Integer add(ModelSub modelSub) throws Exception {
        System.out.println("UserService add()");
       // throw new Exception("测试异常");
        return 1;
    }

    public boolean delete() {
        System.out.println("UserService delete()");
        return true;
    }

    public void edit() {
        System.out.println("UserService edit()");
        int i = 5 / 0;
    }


    public String manyAdvices(Model1 param1, Model1 param2) {
        System.out.println("方法：manyAdvices");
        return param1 + " 、" + param2;
    }
}
