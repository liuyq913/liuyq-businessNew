package com.liuyq.designpatter.proxy.statics.proxy;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Created by liuyq on 2017/12/5.
 */
public class DateSourse {

    private static LinkedList<Connection> connectionList = new LinkedList();

    static{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

     private static java.sql.Connection createNewConnection() throws SQLException {
        return DriverManager.getConnection("url","username", "password");
    }

    //初始化连接池
    private DateSourse(){
        if(connectionList == null && connectionList.size() == 0){
            for(int i= 0; i < 10; i++){
                try {
                    connectionList.add((Connection) createNewConnection());
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    //得到链接
    public Connection getConnection() throws Exception{
        if (connectionList.size() > 0) {
            //return connectionList.remove();  这是原有的方式，直接返回连接，这样可能会被程序员把连接给关闭掉
            //下面是使用代理的方式，程序员再调用close时，就会归还到连接池
            return new ConnectionProxy((java.sql.Connection) connectionList.remove());
        }
        return null;
    }

    //用于关闭链接，将连接返回给连接池，本来是直接close
    public void recoveryConnection(Connection connection){
        connectionList.add(connection);
    }


    //单例

    public static DateSourse getInstance(){
        return DataSourceInstance.dateSourse;
    }

    //加载一个类时，其内部类不会同时被加载。一个类被加载，当且仅当其某个静态成员（静态域、构造器、静态方法等）被调用时发生。
    private static class DataSourceInstance{
        private static  DateSourse dateSourse = new DateSourse();
    }

}
