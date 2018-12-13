package com.liuyq.common.GlobalProperties;

import com.alibaba.fastjson.parser.ParserConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

/**
 * global.properties配置文件读取工具类
 *
 * @author zf
 * @since p2p_cloud_v1.0
 */
public final class GlobalPropertiesUtil
{

    private static final Logger logger = LoggerFactory.getLogger(GlobalPropertiesUtil.class);

    private static Properties properties;

    private GlobalPropertiesUtil()
    {
    }

    static
    {
        try
        {
            properties = PropertiesLoaderUtils.loadAllProperties("global.properties");
            ParserConfig.getGlobalInstance().setAsmEnable(false);
        }
        catch(Exception e)
        {
            logger.error(e.getMessage());
        }

    }

    public static String get(String key)
    {
        return properties.getProperty(key);
    }

    public static int getInt(String key)
    {
        String value = get(key);
        return Integer.parseInt(value);
    }
    
    public static boolean getBoolean(String key)
    {
        String value = get(key);
        return "true".equals(value);
    }
}
