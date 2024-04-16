package com.example.springboot.service;

import com.example.springboot.entity.Cart;
import com.example.springboot.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 *
 */
public interface IOrdersService extends IService<Orders> {

    void addOrder(List<Cart> carts);

    void updateOrder(Orders orders);
}
