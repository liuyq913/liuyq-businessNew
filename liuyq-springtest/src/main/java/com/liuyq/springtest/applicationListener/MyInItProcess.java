package com.liuyq.springtest.applicationListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by liuyq on 2019/4/16.
 * ContextRefreshedEvent ：
 * ApplicationContext 被初始化或刷新时，该事件被发布。
 * 这也可以在 ConfigurableApplicationContext接口中使用 refresh() 方法来发生。
 * 此处的初始化是指：所有的Bean被成功装载，后处理Bean被检测并激活，
 * 所有Singleton Bean 被预实例化，ApplicationContext容器已就绪可用
 */
public class MyInItProcess implements ApplicationListener<ContextRefreshedEvent>{

    private static final Logger LOGGER = LoggerFactory.getLogger(MyInItProcess.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LOGGER.info("容器加载完成所有的bean被成功加载，singleton Bean被预实例化，ApplicationContext容器就绪可用");
    }
}
