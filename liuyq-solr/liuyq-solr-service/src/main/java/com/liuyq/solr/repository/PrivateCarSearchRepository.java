package com.liuyq.solr.repository;

import com.liuyq.solr.model.PrivateCarDocument;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Created by liuyq on 2017/9/25.
 */
public interface PrivateCarSearchRepository extends SolrCrudRepository<PrivateCarDocument, String> {
    /**
     * 通过enterID来查询solr
     * @param enterID
     * @return
     */
     List<PrivateCarDocument> findByEnterID(Integer enterID);

     @Query(value = "?2 AND (alias:*?0*)", filters = "?1")
     @Facet(fields = { "facetValue" }, limit = 100)
    FacetPage<PrivateCarDocument> findByName(String name, String saleStatusFilterQuery, String focusQuery, Pageable pageable);
    /**
     * 通过关键字和特殊字来查询
     */
    @Query(value="?3 AND (brandName:*?0*, alias:*?1*) ", filters = "specialName:?1,?2")
    @Facet(fields = {"facetValye"}, limit = 100)
    FacetPage<PrivateCarDocument> findByNameAndSpecialName(String name, String specialName, String saleStatusFilterQuery, String focusQuery, Pageable pageable);
}
