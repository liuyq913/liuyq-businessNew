package com.liuyq.common.mongo;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.liuyq.common.mongo.beanUtil.MongoDocumentKit;
import com.liuyq.common.mongo.beanUtil.MongoKit;
import com.liuyq.common.util.DateUtil;
import com.mongodb.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertManyOptions;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by 姚俊 on 2017/3/27.
 * <p>
 * * MongoDB工具类 Mongo实例代表了一个数据库连接池，即使在多线程的环境中，一个Mongo实例对我们来说已经足够了<br>
 * 注意Mongo已经实现了连接池，并且是线程安全的。 <br>
 * 设计为单例模式， 因 MongoDB的Java驱动是线程安全的，对于一般的应用，只要一个Mongo实例即可，<br>
 * Mongo有个内置的连接池（默认为10个） 对于有大量写和读的环境中，为了确保在一个Session中使用同一个DB时，<br>
 * DB和DBCollection是绝对线程安全的<br>
 */
public final class MongoUtil {
    private static Logger logger = LoggerFactory.getLogger(MongoUtil.class);

    private static MongoClient mongoClient = null;

    private String domains;
    private String user;
    private String password;
    private String database;
    private int connectionsPerHost;
    private int connectTimeout;
    private int maxWaitTime;
    private int socketTimeout;
    private int threadsAllowedToBlockForConnectionMultiplier;

    public String getDomains() {
        return domains;
    }

    public void setDomains(String domains) {
        this.domains = domains;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public int getConnectionsPerHost() {
        return connectionsPerHost;
    }

    public void setConnectionsPerHost(int connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getThreadsAllowedToBlockForConnectionMultiplier() {
        return threadsAllowedToBlockForConnectionMultiplier;
    }

    public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
        this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }

    //初始化客户端
    private MongoClient getClient() {
        try {
            if (null != mongoClient) {
                return mongoClient;
            }

            //组装mongo服务端地址
            final List<ServerAddress> addressLists = new ArrayList<>();
            for (String domain : domains.split(";")) {
                String[] hostAndPort = domain.split(":");
                String host = hostAndPort[0];
                String port = hostAndPort[1];
                ServerAddress serverAddress = new ServerAddress(host, Integer.parseInt(port));
                addressLists.add(serverAddress);
            }

            MongoClientOptions.Builder options = new MongoClientOptions.Builder();

            options.connectionsPerHost(connectionsPerHost);// 连接池设置为300个连接,默认为100
            options.connectTimeout(connectTimeout);// 连接超时，推荐>3000毫秒
            options.maxWaitTime(maxWaitTime); //
            options.socketTimeout(socketTimeout);// 套接字超时时间，0无限制
            options.threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
            options.writeConcern(WriteConcern.SAFE);//

            final MongoClientOptions op = options.build();
            //组装权限对象
            final List<MongoCredential> credentialsLists = new ArrayList<>();
            MongoCredential credential = MongoCredential.createCredential(user, database, password.toCharArray());
            credentialsLists.add(credential);

            //创建客户端
            mongoClient = new MongoClient(addressLists, credentialsLists, op);
        } catch (NumberFormatException e) {
            logger.error("MongoDB init error", e);
        }
        return mongoClient;
    }

    public void init() {
        MongoKit.INSTANCE.init(getClient(), database);
    }

    /**
     * 暴露mongoClient
     */
    public MongoClient getMongoClient(){
        return mongoClient;
    }

    /**----------------------------------------------------------CRUD-------------------------------------------------------------*/
    /**-------------------------新增-----------------------------*/
    /**
     * 新增一条记录
     *
     * @param collName
     * @param bean
     */
    public void insert(String collName, Object bean) {
        MongoKit.INSTANCE.insert(collName, MongoDocumentKit.toDocument(bean));
    }


    /**
     * 新增多条记录
     *
     * @param collName
     * @param list
     */
    public void insert(String collName, List<Object> list) {
        if (CollectionUtils.isNotEmpty(list) && StringUtils.isNotEmpty(collName)) {
            List<Document> docList = new ArrayList<>();
            for (Object bean : list) {
                docList.add(MongoDocumentKit.toDocument(bean));
            }
            MongoKit.INSTANCE.insert(collName, docList, new InsertManyOptions());
        }
    }

    /**-------------------------查询-----------------------------*/
    /**
     * 查询集合所有文档
     *
     * @param collName
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> findAll(String collName, Class<T> clazz) {
        return MongoKit.INSTANCE.find(collName, 0, new BsonDocument(), new BsonDocument(), clazz);
    }

    /**
     * 根据条件查询
     *
     * @param
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public <T> List<T> find(String collName, Bson filter, Class<T> clazz) {
        Bson query = filter;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, new BsonDocument(), new BsonDocument(), clazz);
    }

    /**
     * 条件查询
     * 返回JSON对象
     * @param collName
     * @param filter
     * @return
     */
    public List<JSONObject> find(String collName, Bson filter) {
        Bson query = filter;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, new BsonDocument());
    }

    /**
     * 条件排序查询 ,返回JSON对象
     * @param collName
     * @param filter
     * @param sortColumn
     * @param isDesc
     * @return
     */
    public List<JSONObject> find(String collName, Bson filter, String sortColumn, boolean isDesc) {
        Bson query = filter;
        int desc = isDesc ? -1 : 1;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, new BasicDBObject(sortColumn, desc), new BsonDocument());


    }

    /**
     * 条件排序查询
     * @param collName
     * @param filter
     * @param sortColumn
     * @param isDesc true 降序 ， false 升序
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> find(String collName, Bson filter, String sortColumn, boolean isDesc, Class<T> clazz) {
        Bson query = filter;
        int desc = isDesc ? -1 : 1;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, new BasicDBObject(sortColumn, desc), new BsonDocument(), clazz);
    }


    /**
     * 条件排序查询，取前n条数据
     * @param collName
     * @param filter
     * @param sortColumn
     * @param isDesc
     * @param limit
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> find(String collName, Bson filter, String sortColumn, boolean isDesc, Integer limit, Class<T> clazz) {
        Bson query = filter;
        int desc = isDesc ? -1 : 1;
        if (filter == null) {
            query = new BsonDocument();
        }
        int limitNum = (limit==null ? 0 : limit);
        return MongoKit.INSTANCE.find(collName , query, new BasicDBObject(sortColumn, desc), new BsonDocument() , limitNum,  clazz);
    }


    /**
     * 分页查询 pageNo 从 1 开始
     * @param collName
     * @param filter
     * @param pageNo
     * @param pageSize
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> findByPage(String collName, Bson filter, int pageNo, int pageSize , Class<T> clazz) {
        Bson orderBy = new BasicDBObject("_id", 1);
        int skip = (pageNo - 1) * pageSize;
        int limit = pageSize;
        Bson query = filter;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, orderBy, limit, skip, new BsonDocument() , clazz);
    }

    /**
     *  查询，跳过skip 查询limit条数据
     * @param collName
     * @param filter
     * @param skip
     * @param limit
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> findBySkipAndLimit(String collName, Bson filter, int skip, int limit , Class<T> clazz) {
        Bson orderBy = new BasicDBObject("_id", 1);
        Bson query = filter;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, orderBy, limit, skip, new BsonDocument() , clazz);
    }

    /**
     * 分页查询 , 按字段排序 , pageNo 从 1 开始
     * @param collName
     * @param filter
     * @param sortColumn
     * @param isDesc
     * @param pageNo
     * @param pageSize
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> findByPage(String collName, Bson filter, String sortColumn, boolean isDesc ,
                                  int pageNo, int pageSize , Class<T> clazz) {
        int desc = isDesc ? -1 : 1;
        int skip = (pageNo - 1) * pageSize;
        int limit = pageSize;
        Bson query = filter;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, new BasicDBObject(sortColumn, desc),
                                    limit, skip, new BsonDocument() , clazz);
    }


    /**
     * 万金油查询
     * @param collName
     * @param filter
     * @param sortColumn
     * @param isDesc
     * @param pageNo
     * @param pageSize
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> find(String collName, Bson filter, Bson project , String sortColumn, boolean isDesc ,
                            int pageNo, int pageSize , String join , Class<T> clazz) {
        int desc = isDesc ? -1 : 1;
        int skip = (pageNo - 1) * pageSize;
        int limit = pageSize;
        Bson query = filter;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, new BasicDBObject(sortColumn, desc),project,
                limit, skip,  join,  clazz);
    }

    /**
     * 按条件查询数量
     * @param collName
     * @param filter
     * @return
     */
    public Long count(String collName, Bson filter){
        return MongoKit.INSTANCE.count(collName , filter);
    }

    /**
     * 更新一条记录(全部覆盖)
     *
     * @param collName 表名
     * @param bson     条件
     * @param doc      更新内容
     */
    public long updateData(String collName, Bson bson, Document doc) {
        return MongoKit.INSTANCE.replaceOne(collName, bson, doc);
    }

    /**
     * 更新一条记录(更新对应列)
     *
     * @param collName
     * @param bson
     * @param doc
     * @return
     */
    public long updateField(String collName, Bson bson, Document doc) {
        return MongoKit.INSTANCE.update(collName, bson, new Document("$set", doc));
    }

    /**
     * 更新多条条记录(更新对应列)
     *
     * @param collName
     * @param bson
     * @param doc
     * @return
     */
    public long updateFieldMutil(String collName, Bson bson, Document doc) {
        return MongoKit.INSTANCE.update(collName, bson, new Document("$set", doc) , true);
    }



    /**
     * 删除一条数据
     * @param collName
     * @param bson
     * @return
     */
    public long deleteOne(String collName, Bson bson) {
        return MongoKit.INSTANCE.deleteOne(collName, bson);
    }

    /**
     * 删除多条
     * @param collName
     * @param bson
     * @return
     */
    public long deleteMany(String collName, Bson bson) {
        return MongoKit.INSTANCE.delete(collName, bson);
    }


    public  <T> List<T> aggregate(String collName, List<Bson> query , Class<T> document) {
        return MongoKit.INSTANCE.aggregateDoNotDealID(collName, query,true,document);
    }

    public  List<JSONObject>  aggregate(String collName, List<Bson> query ) {
        return MongoKit.INSTANCE.aggregateDoNotDealID(collName, query,true);
    }


    public static void main(String[] args) {
        MongoUtil db = new MongoUtil();
        db.setDomains("192.168.100.125:27017");
        db.setUser("root");
        db.setPassword("root");
        db.setDatabase("AK_LOCATION");
        db.setConnectionsPerHost(100);
        db.setConnectTimeout(15000);
        db.setMaxWaitTime(10000);
        db.setSocketTimeout(0);
        db.setThreadsAllowedToBlockForConnectionMultiplier(5);
        try {
            db.init();

            //==========aggregate==================

            List<Document> pipeline = Arrays.asList(
                    new Document("$group", new Document( "_id",
                            new Document("provinceID","$provinceID")
                                    .append("cityID","$cityID")
                    )
                            .append("count", new Document("$sum",1))
                    ));


             new Document("$match",new Document("provinceID",30).append("cityID",348));




            Bson bson = Filters.and(Filters.eq("provinceId" , 30 ),
                  Filters.ne("cityId" , 348));

            //Bson bson3 = Filters.and(bson,bson2);
            List<Bson> bsonList = Lists.newArrayList();
            bsonList.add(new Document("$match",new Document("provinceID",30)));
            bsonList.addAll(pipeline);
            bsonList.add(new Document("$sort", new Document("count", -1)));
            List<JSONObject>resultList = db.aggregate("search_history",bsonList);
            System.out.print(JSON.toJSONString(resultList));
            System.out.print("==============start================");

            List<Document> pipeline2 = Arrays.asList(
                    new Document("$group", new Document( "_id",
                            new Document("brandID","$brandID")
                    )
                            .append("count", new Document("$sum",1))
                    ));
            List<Bson> bsonList2 = Lists.newArrayList();
            bsonList2.add(new Document("$match",new Document("brandID",new Document("$ne",null)).append("provinceID",30).append("cityID",348)));
            bsonList2.addAll(pipeline2);

            bsonList2.add(new Document("$sort", new Document("count", -1)));
            List<JSONObject>resultList2 = db.aggregate("search_history",bsonList2);
            System.out.print(JSON.toJSONString(resultList2));


            System.out.print("==============start2================");
            Date monday = DateUtil.getCycleData(new Date(), DateUtil.cycle_w, 2);
            Document match0 = new Document("$match",new Document("searchContent",new Document("$regex","lll")));

            Document match = new Document("$match",
                    new Document("searchTime",new Document("$gt",0)).append("searchLocation",3));
            Document match2 = new Document("$match",
                    new Document("searchTime",new Document("$lt",new Date().getTime())));

            Document group = new Document("$group", new Document( "_id",
                    new Document("provinceID","$provinceID")
                            .append("cityID","$cityID")
                            .append("searchContent","$searchContent"))
                    .append("count", new Document("$sum",1)));
            List<Bson> bson2List = Lists.newArrayList();
            bson2List.add(match0);
            bson2List.add(match);
            bson2List.add(match2);
            bson2List.add(group);
            bson2List.add(new Document("$sort", new Document("count", -1)));
            List<AggregationBean>resultList3 = db.aggregate("search_history",bson2List,AggregationBean.class);
            System.out.print(JSON.toJSONString(resultList3));
            for (AggregationBean jsonObject : resultList3) {
                System.out.print(jsonObject.getId().get("cityID"));
            }
            System.out.print("\n");
            System.out.print(JSON.toJSONString(hehe(resultList3)));

                /*System.out.print("\n");
                System.out.print(JSON.toJSONString(resultLst));
                System.out.print("\n");*/







            //=============end======================






            //插入
            /*List<String> valueList = new ArrayList<String>();
            valueList.add("asdf1");
            valueList.add("asdf2");
            valueList.add("asdf3");
            valueList.add("asdf4");
            valueList.add("asdf5");
            valueList.add("asdf6");
            valueList.add("asdf7");
            db.insert("testDB" , new TestBean( 23131 , new Date(),valueList));*/
            //条件案例
            //and
//            Bson bson = Filters.and(Filters.eq("memberID" , 12231 ), // =
//                    Filters.ne("type" , 3),            // !=
//                    Filters.gt("createTime" , new Date().getTime()), // >
//                    Filters.lt("createTime"  , new Date().getTime())// <
//            );
//
//            //or
//            Bson bson1 = Filters.or(Filters.eq("memberID" , 12231 ), // =
//                    Filters.ne("type" , 3),            // !=
//                    Filters.gt("createTime" , new Date().getTime()), // >
//                    Filters.lt("createTime"  , new Date().getTime())// <
//            );


            //in, 在list中存在既满足
            /*List<Integer> inList = new ArrayList();
            inList.add(1231); inList.add(45);inList.add(7456);inList.add(12423431);
            Bson bson2 = Filters.in("memberID" , inList);*/


            //all 匹配list中所有才满足
//            List<Integer> inList = new ArrayList();
//            inList.add(1231); inList.add(45);inList.add(7456);inList.add(12423431);
//            Bson bson2 = Filters.all("memberID" , inList);

            //查询所有
            /*List<TestBean> objects = db.find("testDB" , null , TestBean.class);
            System.out.println(JSON.toJSONString(objects));*/

            //排序查询 true 倒序， false 正序
            /*List<Object> objects = db.find("message" , bson , "createTime" , true ,  Object.class);
            System.out.println(JSON.toJSONString(objects));*/

            //分页查询 ,，页码从1开始
            /*List<Object> objects = db.findByPage("message" , bson , 1 , 20 , Object.class);
            System.out.println(JSON.toJSONString(objects));*/


            //更新
//            Document doc = new Document("type" , 2).append("title" , "修改过的title").append("content" , "内容");
//            db.updateField("message" , bson , doc);//更新给定的字段
//            db.updateData("message"  , bson , doc);//覆盖整条数据，doc没给的字段就是空





        } catch (Exception e) {
            e.printStackTrace();
            mongoClient.close();
        }
    }


    private static Map<String, List<AggregationBean>> hehe(List<AggregationBean> aggregationBeanList) {
        if(CollectionUtils.isEmpty(aggregationBeanList)) {
            return null;
        }
        Map<String, List<AggregationBean>> resultMap = Maps.newHashMap();
        for (AggregationBean aggregationBean : aggregationBeanList) {
            String cityID = StringUtils.isBlank(aggregationBean.getId().get("cityID")) ? "null" : aggregationBean.getId().get("cityID");
            String province = StringUtils.isBlank(aggregationBean.getId().get("provinceID")) ? "null" : aggregationBean.getId().get("provinceID");
            StringBuilder sb = new StringBuilder(cityID);
            sb.append(province);
            String key = sb.toString();
            sb = null;
            if (resultMap.containsKey(key)) {
                if (resultMap.get((key)).size() < 5) {
                    resultMap.get((key)).add(aggregationBean);
                }
            } else {
                List<AggregationBean> cityList = Lists.newArrayList();
                cityList.add(aggregationBean);
                resultMap.put(key, cityList);
            }
        }
        return resultMap;
    }


}
