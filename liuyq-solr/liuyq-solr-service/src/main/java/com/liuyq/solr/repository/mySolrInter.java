package com.liuyq.solr.repository;

import com.liuyq.solr.model.CluesDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * Created by liuyq on 2017/7/24.
 */
public interface mySolrInter extends SolrCrudRepository<CluesDocument,String> {
    @Query(value = "*:*")
    @Facet(fields = { "brandName" }, limit = 5)
    FacetPage<CluesDocument> findAllFacetOnName(Pageable page);
}
