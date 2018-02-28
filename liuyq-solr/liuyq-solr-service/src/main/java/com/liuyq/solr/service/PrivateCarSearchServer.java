package com.liuyq.solr.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.liuyq.solr.bo.PrivateCarQueryBo;
import com.liuyq.solr.model.CluesDocument;
import com.liuyq.solr.model.PrivateCarDocument;
import com.liuyq.solr.repository.PrivateCarSearchRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liuyq on 2017/9/25.
 */
@Service
public class PrivateCarSearchServer {
    @Resource
    private PrivateCarSearchRepository privateCarSearchRepository;
    @Resource
    private SolrTemplate solrTemplate;


   public  List<PrivateCarDocument> findPrivateCarDocumentByEentryID(Integer entryID){
      return  privateCarSearchRepository.findByEnterID(entryID);
   }
    /**
     * 通过关键字搜索车列表
     */
    public FacetPage<PrivateCarDocument> findByKey(Integer memberID, Integer mainMember, List<Integer> focusList,
                                                   String key, Pageable pageable){
        String foucusQuery  = getFocusQuery(memberID, mainMember, focusList);
        String saleStatusFilterQuery = "saleStatus:1";
        FacetPage<PrivateCarDocument> facetPage =  privateCarSearchRepository.findByName(key, saleStatusFilterQuery, foucusQuery, pageable);
        return facetPage;
    }

    private String getFocusQuery(Integer memberID,Integer mainMember, List<Integer> focusList) {
        if (null == memberID || CollectionUtils.isEmpty(focusList)) {
            return StringUtils.EMPTY;
        }
        StringBuilder queryBuilder = new StringBuilder();
        if(CollectionUtils.isEmpty(focusList)){
            focusList.add(-100);
        }
        String focusListString = StringUtils.join(focusList.toArray()," ");
        if(null != mainMember){   //自己是客户经理
            queryBuilder.append("((enterID:(").append(focusListString).append(")) AND !(custId:").
                    append(mainMember).append(" !enterID:").append(mainMember).append("))"); //客户经理关注了同级客户经理是看不到对方的车的
            return queryBuilder.toString();
        }
        //自己是车商(可以看到车商是自己但发车非自己的车)
        queryBuilder.append("((enterID:(").append(focusListString).append(")) OR (custId:").append(memberID).append(" !enterID:").append(memberID).append("))");
        return queryBuilder.toString();
    }

    public void Search(PrivateCarQueryBo privateCarQueryBo, int pageSize, int currentPage){
        solrTemplate.setSolrCore("privateCarSearch");
        SolrClient solrClient = solrTemplate.getSolrClient();
        StringBuilder query = new StringBuilder();
        List<Integer> focusList = privateCarQueryBo.getFocusList();
        if(CollectionUtils.isEmpty(focusList)){
            focusList.add(-100);
        }
        String s = StringUtils.join(focusList.toArray()," ");
        if(null != privateCarQueryBo.getMainMemberID()){ //客户经理
            query.append("((enterID:(").append(s).append(")) AND !( custID:").append(privateCarQueryBo.getMainMemberID()).
                    append(" !enterID:").append(privateCarQueryBo.getCurrentUserID()).append("))");
        }else{ //自己是车商
            query.append("((enterID:(").append(s).append(")) OR ( custID:").
                    append(privateCarQueryBo.getMainMemberID()).append(" !enterID:").append(privateCarQueryBo.getCurrentUserID()).append("))");
        }
        SolrQuery solrQuery = new SolrQuery(query.toString());
        if(StringUtils.isNoneBlank(privateCarQueryBo.getQueryKey())){
           String keyWord = ClientUtils.escapeQueryChars(privateCarQueryBo.getQueryKey().toUpperCase().replaceAll(" ",""));
           if(keyWord.trim().matches("a-zA-Z+")){
               solrQuery.addFacetQuery("pinyin:*"+keyWord+"*");
           }else{
               solrQuery.addFacetQuery("name:*"+keyWord+"*");
           }
        }
        if(privateCarQueryBo.getCustID()!=null){
            solrQuery.addFacetQuery("enterID:"+privateCarQueryBo.getCustID());
        }
        solrQuery.setStart(currentPage*pageSize);
        solrQuery.setRows(pageSize);
        solrQuery.setSort("addTime", SolrQuery.ORDER.desc);
        try {
            QueryResponse queryResponse = solrClient.query(solrQuery);
            List<PrivateCarQueryBo> list = queryResponse.getBeans(PrivateCarQueryBo.class);
            System.out.println(list.size());
        } catch (Exception e){
            System.out.println("solr查询出错");
        }
    }

    @Test
    public  void test(){
        List list = Arrays.asList(23,24);
        String s = getFocusQuery(1, null,list);
        System.out.println(s);
    }

    @Test
    public void test2(){
        CluesDocument cluesDocument =new CluesDocument();
        cluesDocument.setId("1");
        CluesDocument cluesDocument2 =new CluesDocument();
        cluesDocument2.setId("2");
        CluesDocument cluesDocument3 =new CluesDocument();
        List<CluesDocument> list = Lists.newArrayList();
        list.add(cluesDocument);
        list.add(cluesDocument2);
        list.add(cluesDocument3);
        for(int i=0;i<10;i++) {
            System.out.println("呵呵");
            for (int a = 0; a < list.size(); a++) {
                CluesDocument cluesDocument1 = list.get(a);
                if(cluesDocument1.getId() == null){
                    continue;
                }
                System.out.println("哈哈");
            }
        }
    }

    @Test
    public void testString(){
        ProxyFactoryBean proxyFactoryBean;
    }
}
