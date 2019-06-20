package com.liuyq.springtest.tx.mapper;

import com.liuyq.springtest.tx.model.TxException;
import com.liuyq.springtest.tx.model.TxExceptionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TxExceptionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_tx_exception
     *
     * @mbg.generated
     */
    long countByExample(TxExceptionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_tx_exception
     *
     * @mbg.generated
     */
    int deleteByExample(TxExceptionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_tx_exception
     *
     * @mbg.generated
     */
    int insert(TxException record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_tx_exception
     *
     * @mbg.generated
     */
    int insertSelective(TxException record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_tx_exception
     *
     * @mbg.generated
     */
    List<TxException> selectByExample(TxExceptionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_tx_exception
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") TxException record, @Param("example") TxExceptionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_tx_exception
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") TxException record, @Param("example") TxExceptionExample example);
}