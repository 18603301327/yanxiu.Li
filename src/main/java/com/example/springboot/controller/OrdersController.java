package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.controller.enums.OrderStatusEnum;
import com.example.springboot.entity.*;
import com.example.springboot.service.IAddressService;
import com.example.springboot.service.IGoodsService;
import com.example.springboot.service.IOrderItemService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IOrdersService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 *
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private IOrdersService ordersService;
    @Resource
    IOrderItemService orderItemService;
    @Resource
    IGoodsService goodsService;


    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Orders orders) {
        ordersService.saveOrUpdate(orders);
        return Result.success();
    }

    @PostMapping("/addOrder")
    public Result addOrder(@RequestBody List<Cart> carts) {
       ordersService.addOrder(carts);
        return Result.success();
    }

    @PostMapping("/status")
    public Result addOrder(@RequestBody Orders orders) {
        ordersService.updateOrder(orders);
        if (orders.getStatus() == 7) {
            List<OrderItem> orderItems = orderItemService.list(new QueryWrapper<OrderItem>().eq("order_id", orders.getId()));
            for (OrderItem orderItem : orderItems) {
                Integer goodsId = orderItem.getGoodsId();
                Goods goods = goodsService.getById(goodsId);
                goods.setNums(goods.getNums() + orderItem.getNum());
                goodsService.updateById(goods);  // 退款加库存
            }
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        ordersService.removeById(id);
        orderItemService.remove(new UpdateWrapper<OrderItem>().eq("order_id", id));  // 删除订单详情
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        ordersService.removeByIds(ids);
        for (Integer id : ids) {
            orderItemService.remove(new UpdateWrapper<OrderItem>().eq("order_id", id));  // 删除订单详情
        }
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(ordersService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(ordersService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser.getRole().equals("ROLE_USER")) {
            queryWrapper.eq("userid", currentUser.getId());
        }
        Page<Orders> page = ordersService.page(new Page<>(pageNum, pageSize), queryWrapper);
        for (Orders record : page.getRecords()) {
            Integer id = record.getId();
            List<OrderItem> orderItems = orderItemService.list(new QueryWrapper<OrderItem>().eq("order_id", id));
            record.setOrderItems(orderItems);
        }
        return Result.success(page);
    }

    @GetMapping("/pageEnd")
    public Result pageEnd(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser.getRole().equals("ROLE_USER")) {
            queryWrapper.eq("merchantid", currentUser.getId());
        }
        Page<Orders> page = ordersService.page(new Page<>(pageNum, pageSize), queryWrapper);
        for (Orders record : page.getRecords()) {
            Integer id = record.getId();
            List<OrderItem> orderItems = orderItemService.list(new QueryWrapper<OrderItem>().eq("order_id", id));
            record.setOrderItems(orderItems);
        }
        return Result.success(page);
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Orders> list = ordersService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Orders信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

        }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Orders> list = reader.readAll(Orders.class);

        ordersService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

