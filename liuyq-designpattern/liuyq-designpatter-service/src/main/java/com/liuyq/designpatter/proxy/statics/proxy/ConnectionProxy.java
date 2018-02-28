package com.liuyq.designpatter.proxy.statics.proxy;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by liuyq on 2017/12/5.
 */
public class ConnectionProxy implements Connection{

    private Connection connection;

    public ConnectionProxy(java.sql.Connection connection){
        super();
        this.connection = (Connection) connection;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    // 静态代理对于这种，被代理的对象很固定，我们只需要去代理一个类或者若干固定的类，
    // 数量不是太多的时候，可以使用，而且其实效果比动态代理更好，因为动态代理就是在运行期间动态生成代理类，
    // 所以需要消耗的时间会更久一点。就像上述的情况，其实就比较适合使用静态代理
    @Override
    public void close() throws SQLException {
        //close方法也只会归还给连接池了。
        DateSourse.getInstance().recoveryConnection(connection);
        System.out.println("不真正关闭连接，归还给连接池");
    }
}
