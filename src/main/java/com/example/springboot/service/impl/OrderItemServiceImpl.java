package com.example.springboot.service.impl;

import com.example.springboot.entity.OrderItem;
import com.example.springboot.mapper.OrderItemMapper;
import com.example.springboot.service.IOrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 *
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {

}
