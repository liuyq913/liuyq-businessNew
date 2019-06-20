package com.liuyq.springtest.tx.service;

import com.liuyq.common.exception.LiuyqException;
import com.liuyq.springtest.tx.mapper.TxExceptionMapper;
import com.liuyq.springtest.tx.model.TxException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liuyq on 2019/6/18.
 */
@Service
public class TxService {
    @Resource
    private TxExceptionMapper txExceptionMapper;

    public Integer insert(TxException txException){
        txExceptionMapper.insert(txException);
        throw new LiuyqException("eee");
        //txException.setGroup_id("2");
        //return txExceptionMapper.insert(txException);
    }
}
