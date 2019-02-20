package com.liuyq.dynamic.config;


import com.liuyq.dynamic.constant.DynamicConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@Component
/*@DisconfFile(filename="dynamicConfig.properties")*/
public class DynamicConfig {


    @Resource
    private Map<String, Object> properties;

    private static Logger logger = LoggerFactory.getLogger(DynamicConfig.class);

    //存储类型
   // private Map<String, String> dynamicTypeMap = new HashMap<>();

    // 存储类型与配置
    private Map<String, DynamicStyle> dynamicStyleMap = new HashMap<String, DynamicStyle>();


    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;

    }


    public void init() {

        try {
            String allDynamicType = (String) this.properties.get("dynamic.type");
            if (StringUtils.isNotBlank(allDynamicType)) {
                //拆分
                List<String> dynamicTypeList = Arrays.asList(allDynamicType.split(DynamicConstant.REGEX));
                for (String dynamicType : dynamicTypeList) {

            //        List<DynamicStyle> dynamicStyleList = new ArrayList<>();
                    DynamicStyle dynamicStyle = new DynamicStyle();
                    //获取默认的url配置
                    String clickUrl = (String) this.properties.get(dynamicType + ".clickUrl");
                    String clickWapUrl = (String) this.properties.get(dynamicType + ".clickWapUrl");
                    dynamicStyle.setClickurl(clickUrl);
                    dynamicStyle.setClickWapUrl(clickWapUrl);

                    //获取style配置
                    String allitemTypeStyle = (String) this.properties.get(dynamicType + ".style");
                    if (StringUtils.isNotBlank(allitemTypeStyle)) {
                        //拆分样式
                        List<String> styleIndexList = Arrays.asList(allitemTypeStyle.split(DynamicConstant.REGEX));
                        for (String index : styleIndexList) {
                          //  dynamicTypeMap.put(dynamicType , index);
                            DynamicStyle style = new DynamicStyle(dynamicStyle);
                            //逐个获取样式配置
                            String desc = (String) this.properties.get(dynamicType + ".style" + "." + index + ".desc");
                            String otherDesc = (String) this.properties.get(dynamicType + ".style" + "." + index + ".otherdesc");
                            String title = (String) this.properties.get(dynamicType + ".style" + "." + index + ".title");
                            String subTitle = (String) this.properties.get(dynamicType + ".style" + "." + index + ".subtitle");
                            String pic = (String) this.properties.get(dynamicType + ".style" + "." + index + ".pic");
                            //跳转地址url，如果配置了特有的，取特有的
                            String appurl = (String) this.properties.get(dynamicType + ".style" + "." + index + ".clickUrl");
                            String wapUrl = (String) this.properties.get(dynamicType + ".style" + "." + index + ".clickWapUrl");

                            //切割
                            style.setDesc(StringUtils.isNotBlank(desc) ? Arrays.asList(desc.split(DynamicConstant.REGEX)) : null);
                            style.setOtherDesc(StringUtils.isNotBlank(otherDesc) ? Arrays.asList(otherDesc.split(DynamicConstant.REGEX)) : null);
                            style.setTitle(StringUtils.isNotBlank(title) ? Arrays.asList(title.split(DynamicConstant.REGEX)) : null);
                            style.setSubTitle(StringUtils.isNotBlank(subTitle) ? Arrays.asList(subTitle.split(DynamicConstant.REGEX)) : null);
                            style.setPic(StringUtils.isNotBlank(pic) ? Arrays.asList(pic.split(DynamicConstant.REGEX)) : null);
                            style.setClickurl(appurl!=null?appurl:style.getClickurl());
                            style.setClickWapUrl(wapUrl!=null?wapUrl:style.getClickWapUrl());

                            //dynamicStyleList.add(style);
                            //按照子类型存放style
                            dynamicStyleMap.put(dynamicType+"_"+index , style);
                        }
                    } /*else {
                        //没有stype配置
                        dynamicStyleList.add(dynamicStyle);
                    }*/
                    //存放到map中
                    //dynamicStyleMap.put(dynamicType, dynamicStyleList);
                }
            }
        } catch (Exception e) {
            logger.error("init DynamicConfig error");
            e.printStackTrace();
        }
    }



    public DynamicConfig() {


    }



    /**
     * 根据key值获取
     * 这里默认取 default类型的 样式
     * @param key
     * @return
     */
    public DynamicStyle getStyleByType(String key){
        return dynamicStyleMap.get(key+"_default");
    }


    /**
     * 根据key和subkey获取配置
     * @param key
     * @param subType
     * @return
     */
    public DynamicStyle getStyleByTypeAndSubTyle(String key, String subType) {
        return dynamicStyleMap.get(key+"_"+subType);
    }
}


