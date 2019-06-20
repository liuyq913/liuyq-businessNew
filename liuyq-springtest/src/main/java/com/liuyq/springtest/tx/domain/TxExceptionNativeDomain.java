package com.liuyq.springtest.tx.domain;

import com.liuyq.springtest.tx.model.TxException;
import com.liuyq.springtest.tx.service.TxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by liuyq on 2019/6/20.
 */
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
@Service("txExceptionDomain")
public class TxExceptionNativeDomain implements TxExceptionDomain{
    @Resource
    private TxService txService;

    @Override
    public Integer insert(TxException tx) {
        return txService.insert(tx);
    }
}
