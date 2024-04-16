package com.example.springboot.mapper;

import com.example.springboot.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 *
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    @Update("update orders set status = #{status}, pay_time = #{payTime}, payno = #{payno} where orderno = #{orderno}")
    void updateState(@Param("orderno") String orderno, @Param("status")Integer status, @Param("payTime")String payTime, @Param("payno") String payno);

}
