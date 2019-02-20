package com.liuyq.solr.domain;

import com.liuyq.Pages.Page;
import com.liuyq.common.util.BeanUtil;
import com.liuyq.solr.bo.PrivateCarDocumentBo;
import com.liuyq.solr.bo.PrivateCarQueryBo;
import com.liuyq.solr.model.PrivateCarDocument;
import com.liuyq.solr.service.PrivateCarSearchServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liuyq on 2017/9/25.
 */
@Service("privateCarSearchDomain")
public class PrivateCarSearchNativeDomain implements PrivateCarSearchDomain{
    @Resource
    private PrivateCarSearchServer privateCarSearchServer;
    @Value("${redis.timeout}")
    private Integer timeOut;

    @Override
    public List<PrivateCarDocumentBo> findPrivateCarDocumentByEentryID(Integer entryID) {
        return BeanUtil.convertList(privateCarSearchServer.findPrivateCarDocumentByEentryID(entryID), PrivateCarDocumentBo.class);
    }

    @Override
    public Page<PrivateCarDocumentBo> findByKey(Integer memberID, Integer mainMember, List<Integer> focusList, String key, Integer pageSize, Integer currentPage) {
        Pageable pageable = new PageRequest(currentPage,pageSize);
        FacetPage<PrivateCarDocument> privateCarDocuments= privateCarSearchServer.findByKey(memberID, mainMember, focusList, key, pageable) ;
        List<PrivateCarDocument> privateCarDocumentBos = privateCarDocuments.getContent();
        List<PrivateCarDocumentBo> list =  BeanUtil.convertList(privateCarDocumentBos, PrivateCarDocumentBo.class);
        return new Page<>(pageSize,currentPage, list, privateCarDocuments.getTotalElements());
    }

    @Override
    public void search(PrivateCarQueryBo privateCarQueryBo, int pageSize, int currentPage) {
        privateCarSearchServer.Search(privateCarQueryBo, pageSize, currentPage);
    }

    @Override
    public void setUser(Integer id) {

    }

}
