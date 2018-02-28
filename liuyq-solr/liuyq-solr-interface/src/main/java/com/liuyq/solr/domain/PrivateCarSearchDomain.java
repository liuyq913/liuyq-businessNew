package com.liuyq.solr.domain;

import com.liuyq.Pages.Page;
import com.liuyq.solr.bo.PrivateCarDocumentBo;
import com.liuyq.solr.bo.PrivateCarQueryBo;

import java.util.List;

/**
 * Created by liuyq on 2017/9/26.
 */
public interface PrivateCarSearchDomain {

    List<PrivateCarDocumentBo> findPrivateCarDocumentByEentryID(Integer entryID);

    Page<PrivateCarDocumentBo> findByKey(Integer memberID, Integer mainMember, List<Integer> focusList,
                                         String key, Integer pageSize, Integer currentPage);

    void search(PrivateCarQueryBo privateCarQueryBo, int pageSize, int currentPage);
}
