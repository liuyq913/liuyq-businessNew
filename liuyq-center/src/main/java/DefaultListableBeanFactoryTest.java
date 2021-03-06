import com.liuyq.solr.domain.PrivateCarSearchDomain;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.lang.reflect.Constructor;

/**
 * Created by liuyq on 2018/12/28.
 */
public class DefaultListableBeanFactoryTest {

    AbstractApplicationContext applicationcontext=null;

   // @Before
    public void before() {
        System.out.println("》》》Spring ApplicationContext容器开始初始化了......");
        applicationcontext= new ClassPathXmlApplicationContext(new String[]{"classpath:applicationContext.xml"});
        System.out.println("》》》Spring ApplicationContext容器初始化完毕了......");
    }

    @Test
    public void test() throws NoSuchMethodException {
        FileSystemXmlApplicationContext resource = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
        PrivateCarSearchDomain privateCarSearchDomain = (PrivateCarSearchDomain) resource.getBean("privateCarSearchDomain");
        //System.out.println(privateCarSearchDomain);
        Class clazz = privateCarSearchDomain.getClass();
        Constructor constructorToUse = clazz.getDeclaredConstructor((Class[])null);
        Object o  = BeanUtils.instantiateClass(constructorToUse, new Object[0]);
        System.out.println("-----------"+o);
    }

    @Test
    public void  test2() {
        applicationcontext.registerShutdownHook();
    }
}


