package com.liuyq.springtest.applicationListener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by liuyq on 2019/4/16.
 */
public class ServletMyInitProcessor implements ServletContextListener{

    private static final Logger LOGGER =  LoggerFactory.getLogger(ServletMyInitProcessor.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext();
        LOGGER.info("Servlet容器里面，在spring 容器加载完之后执行这里");
    }
}
