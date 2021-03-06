package com.liuyq.common.mongo.beanUtil;

import com.liuyq.common.mongo.geospatial.MongoGeospatial;
import org.bson.Document;

import java.util.*;

/**
 * 创建人:LEN 创建日期: 2017/06/08 文件描述:
 */
public class MongoDocumentKit {

    public static Document toDocument(Object obj) {

        Map<String, Object> map;


        if (Document.class.isInstance(obj)) {
            return (Document) obj;
        }

        if (!Map.class.isInstance(obj)) {
            map = MongoKit.INSTANCE.toMap(obj);
        } else {
            map = (Map<String, Object>) obj;
        }
        transferDocument(map);

        String id = String.valueOf(map.get("id"));
        if (id == null || id.equals("") || "null".equalsIgnoreCase(id)) {
            map.remove("id");
        }

        return new Document(map);
    }

    private static void transferDocument(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {

            if (entry.getValue() instanceof MongoGeospatial) {
                map.put(entry.getKey(), ((MongoGeospatial) entry.getValue()).getPoint());
            }

            if (entry.getValue() instanceof MongoBean) {
                Document doc = toDocument((MongoBean) entry.getValue());
                map.put(entry.getKey(), doc);
            }

            if (entry.getValue() instanceof List || entry.getValue() instanceof Set) {
                try {
                    List<Object> list = new ArrayList<Object>((Collection<Object>) entry.getValue());
                    List<Map<String, Object>> docList = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < list.size(); i++) {
                        Document doc = toDocument(list.get(i));
                        docList.add(doc);
                    }
                    map.put(entry.getKey(), docList);
                } catch (RuntimeException e) {
                    MongoKit.INSTANCE.error("MongoDocumentKit.class",
                            "The list must be List<MongoBean> inserted into the database.");
                }

            }
        }
    }

    public static Document toDocument(MongoBean bean) {
        return new Document(bean.toMap());
    }

    /* 用于判断是否是基本类型和JSON对象，如果是的话不需要进行转换 */
    public static boolean conversionValidation(Object obj) {
        if (String.class.isInstance(obj) || Integer.class.isInstance(obj)
                || Double.class.isInstance(obj) || Boolean.class.isInstance(obj)
                || Float.class.isInstance(obj) || Character.class.isInstance(obj)
                || Long.class.isInstance(obj) || Byte.class.isInstance(obj)
                || Short.class.isInstance(obj) || Date.class.isInstance(obj)
                || Map.class.isInstance(obj)) {
            return false;
        }

        if (obj instanceof Object) {
            return true;
        }

        return false;

    }


}


