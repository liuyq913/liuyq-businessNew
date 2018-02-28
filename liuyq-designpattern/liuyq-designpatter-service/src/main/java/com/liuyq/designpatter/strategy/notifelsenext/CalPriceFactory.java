package com.liuyq.designpatter.strategy.notifelsenext;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by liuyq on 2017/12/11.
 */
public class CalPriceFactory {
    private static final String CAL_PRICE_PACKAGE = "com.liuyq.designpatter.strategy.notifelsenext";//扫描策略的包名

    private ClassLoader classLoader = getClass().getClassLoader();//我们加载策略时的类加载器，我们任何类运行时信息必须来自该类加载器

    private List<Class<? extends CalPrice>> calPriceList;//策略列表

    //根据客户的总金额产生相应的策略
    public CalPrice createCalPrice(Customer customer) throws Exception{
        //可排序map 为了存储我们当前策略的运行时类信息
        SortedMap<Integer, Class<? extends CalPrice>> clazzMap = new TreeMap<Integer, Class<? extends CalPrice>>();
        for(Class<? extends CalPrice> clazz : calPriceList) {
            //获取策略的注解
            Annotation validRegion = handleAnnotationn(clazz);
            //根据策略进行不同的判断
            if (validRegion instanceof TotalValidRegion) {
                TotalValidRegion totalValidRegion = (TotalValidRegion) validRegion;
                if (customer.getTotalAmount() < totalValidRegion.value().max() &&
                        customer.getTotalAmount() > totalValidRegion.value().min()) {
                    clazzMap.put(totalValidRegion.value().order(), clazz);//将采用的策略放到map里面
                }
            } else if (validRegion instanceof OnceValidRegion) {
                OnceValidRegion onceValidRegion = (OnceValidRegion) validRegion;
                if (customer.getAmount() < onceValidRegion.value().max() &&
                        customer.getAmount() > onceValidRegion.value().min()) {
                    clazzMap.put(onceValidRegion.value().order(), clazz);
                }
            }
        }

            try {
                return CalPriceProxy.getProxy(clazzMap);
            }catch (Exception e){
                throw new Exception("策略获取失败");
            }
    }


    //处理注解，返回策略的注解
    private Annotation handleAnnotationn(Class<? extends CalPrice> calPrice){
        Annotation[] annotations = calPrice.getAnnotations();
        if(annotations == null || annotations.length == 0){
            return null;
        }

        for(int i=0;i<annotations.length;i++){
            Annotation annotation = annotations[i];
            //是TotalValidRegion或者OnceValidRegion则返回
            if(annotation instanceof TotalValidRegion || annotation instanceof OnceValidRegion){
                return annotation;
            }
        }
        return null;
    }

    private CalPriceFactory(){
        init();
    }
    //在工厂初始化时要初始化策略类列表
    private void init(){
        calPriceList = new ArrayList<Class<? extends CalPrice>>();
        File[] resources = getResources();//获取包下所有的class文件
        Class<CalPrice> calPriceClazz = null;
        try {
            calPriceClazz = (Class<CalPrice>) classLoader.loadClass(CalPrice.class.getName()); //使用相同类加载器的策略接口
        }catch (ClassNotFoundException e){
            throw new RuntimeException("未找到策略接口");
        }
        for(int i = 0;i<resources.length;i++){
            try {
                //载入包下的类
                Class<?> clazz = classLoader.loadClass(CAL_PRICE_PACKAGE+"."+resources[i].getName().replace(".class",""));
                //判断是CalPrice的实现类，而且不是calPriceClazz本身，则放到列表里面
                if((CalPrice.class).isAssignableFrom(clazz) && clazz !=calPriceClazz){  //isAssignableFrom 是用来判断一个类Class1和另一个类Class2是否相同或是另一个类的超类或接口。
                    calPriceList.add((Class<? extends CalPrice>) clazz);
                }
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }
    //获取扫描包下面所有的class文件
    private File[] getResources(){
        try {
            File file = new File(classLoader.getResource(CAL_PRICE_PACKAGE.replace(".","/")).toURI());
            return file.listFiles(new FileFilter() {//返回抽象路径名数组，这些路径名表示此抽象路径名表示的目录中满足指定过滤器的文件和目录。
                public boolean accept(File pathname) {
                   if(pathname.getName().endsWith(".class")){ //我们只扫描class文件
                       return true;
                   }else{
                       return false;
                   }
                }
            });

        }catch (URISyntaxException e){
            throw new RuntimeException("未找到策略资源");
        }
    }
    //单例
    public static CalPriceFactory getInstance(){
        return CalPriceFactoryInstance.calPriceFactory;
    }

    private static class CalPriceFactoryInstance{
        private static CalPriceFactory calPriceFactory = new CalPriceFactory();
    }
}
