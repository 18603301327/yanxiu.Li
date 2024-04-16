package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springboot.common.Result;
import com.example.springboot.entity.*;
import com.example.springboot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IOrderItemService orderItemService;

    @GetMapping("/echarts1")
    public Result echarts1() {
        List<String> x = Arrays.asList("2024-01", "2024-02", "2024-03", "2024-04", "2024-05", "2024-06", "2024-07", "2024-08", "2024-09", "2024-10", "2024-11", "2024-12");
        List<Object> y = new ArrayList<>();
        for (String time : x) {
            List<Orders> list = ordersService.list(new LambdaQueryWrapper<Orders>().like(Orders::getCreateTime,time));
            Double total = 0.0;
            for (Orders orders : list) {
                total += orders.getTotal().doubleValue();
            }
            y.add(total);
        }
        Map<Object, Object> result = new HashMap<>();
        result.put("x",x);
        result.put("y",y);
        return Result.success(result);
    }

    @GetMapping("/echarts2")
    public Result echarts2() {
        List<Category> types = categoryService.list();
        List<Map<Object, Object>> list = new ArrayList<>();
        for (Category type : types) {
            Map<Object, Object> result = new HashMap<>();
            result.put("name", type.getName());
            List<OrderItem> list2 = orderItemService.list(new LambdaQueryWrapper<OrderItem>().like(OrderItem::getCategory, type.getName()));
            Integer total = 0;
            for (OrderItem orderItem : list2) {
                total += orderItem.getNum();
            }
            result.put("value", total);
            list.add(result);
        }
        return Result.success(list);
    }
}
