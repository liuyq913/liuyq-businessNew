package com.liuyq.redpacket.sequencefactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.liuyq.common.util.CheckUtil;
import com.liuyq.redpacket.bo.RedPacketDistributionBo;
import com.liuyq.redpacket.model.Person;
import com.liuyq.redpacket.pojo.ActivityConfigPojo;
import com.liuyq.redpacket.pojo.RedPacketPojo;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by liuyq on 2017/10/30.
 */
public  class SequenceFactory {
    List<String> doCreateSequence(ActivityConfigPojo activityConfigPojo){
        List<RedPacketDistributionBo> redPacketDistributionBos = activityConfigPojo.getDistributionBoList();
        Set<RedPacketPojo> set = Sets.newHashSet();
        redPacketDistributionBos.stream().filter(t -> t != null).forEach(t -> buildPockedSet(set, t.getRedPacketNum(), t.getPrizeId()));
        //听说 tableSize/entrySize = 2 插入的效率高，而且为素数的话更好
        RedPacketPojo[] redPacketPojos = new RedPacketPojo[activityConfigPojo.getRedPacketTotalNum() * 2 + 13];
        set.forEach(pocket -> hashIntoArray(redPacketPojos, pocket));
        List<String> result = Arrays.stream(redPacketPojos).filter(t -> t!= null)
                .map(t -> String.valueOf(t.getPrizeId())).collect(Collectors.toList());
        return result;
    }

    /**
     *随机分布红包
     */
    private void  hashIntoArray(RedPacketPojo[] redPacketPojos, RedPacketPojo redPacketPojo){
        int size = redPacketPojos.length;
        int index = getHashVal(redPacketPojo.hashCode()%size, size);
        if(redPacketPojos[index] == null ){
            redPacketPojos[index] = redPacketPojo;
        }
        do{
            if(index == size-1){
                index=-1;
            }
            index ++;
        }
        while(redPacketPojos[index] != null);
        redPacketPojos[index] = redPacketPojo;
    }

    private int getHashVal(int i, int size) {
        return i < 0 ? (i + size) : i;
    }

    /**
     * @param pocketsSet 红包序列
     * @param count 红包个数
     * @param prizeId
     * @return
     */
    public Set<RedPacketPojo> buildPockedSet(Set<RedPacketPojo> pocketsSet, int count, Integer prizeId) {
        CheckUtil.checkStatus(count <= 0,"红包数需要大于0");
        CheckUtil.checkStatus(null == prizeId,"礼品id不能为空");
        for(int i=0;i<count;i++){
            createAndAddToSet(pocketsSet, prizeId);
        }
        return pocketsSet;
    }
        /**
         * 生产红包对象赛到set里面
         * @param pocketsSet
         * @param prizeId
         */
    private void createAndAddToSet(Set<RedPacketPojo> pocketsSet, Integer prizeId){
        if(null == pocketsSet) return;
        RedPacketPojo redPacketPojo = new RedPacketPojo(UUID.randomUUID().toString(), prizeId);
        if(!pocketsSet.contains(redPacketPojo)){
            pocketsSet.add(redPacketPojo);
            return;
        }
        createAndAddToSet(pocketsSet, prizeId);
    }

    @Test
    public void test(){
        ActivityConfigPojo activityConfigPojo = new ActivityConfigPojo();
        List<RedPacketDistributionBo> list = Lists.newArrayList();
        RedPacketDistributionBo redPacketDistributionBo = new RedPacketDistributionBo();
        redPacketDistributionBo.setRedPacketNum(2);
        redPacketDistributionBo.setPrizeId(2);
        list.add(redPacketDistributionBo);
        RedPacketDistributionBo redPacketDistributionBo2 = new RedPacketDistributionBo();
        redPacketDistributionBo2.setRedPacketNum(2);
        redPacketDistributionBo2.setPrizeId(2);
        list.add(redPacketDistributionBo2);
        activityConfigPojo.setRedPacketNum(4);
        activityConfigPojo.setRedPacketTotalNum(10);
        activityConfigPojo.setDistributionBoList(list);
        List<String> s = doCreateSequence(activityConfigPojo);
        System.out.println(s.toString());
    }
    @Test
    public void Test2(){
        List<String> list = Lists.newArrayList();
        for(int i=0;i<4;i++){
            list.add(new Person("1","a",1+"").toString());
        }
       Stream<Person> stream = Stream.of(new Person("1", "aa", "12"), new Person("1", "bb", "13"), new Person("3", "cc", "14"));
        Map<String,List<String>> map =  list.stream().collect(Collectors.groupingBy(Object::toString));
        System.out.println(12);
    }
}
