package com.liuyq.designpatter.proxy.statics.proxy;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by liuyq on 2017/12/5.
 */
public interface Connection {

    Statement createStatement() throws SQLException;

    void close() throws SQLException;
}
