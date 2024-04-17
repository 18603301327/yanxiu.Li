package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.*;
import com.example.springboot.service.ICollectService;
import com.example.springboot.service.IPraiseService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IGoodsService;

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
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private IGoodsService goodsService;
    @Resource
    IPraiseService praiseService;
    @Resource
    ICollectService collectService;

    private final String now = DateUtil.now();

    @GetMapping("/recommend")
    public Result recommend() {
        return Result.success(goodsService.getRecommend().stream().limit(4).collect(Collectors.toList()));
    }

    @GetMapping("/top")
    public Result top() {
        return Result.success(goodsService.list(new LambdaQueryWrapper<Goods>().orderByDesc(Goods::getSales)).stream().limit(4).collect(Collectors.toList()));
    }

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Goods goods) {
        User currentUser = TokenUtils.getCurrentUser();
        if (goods.getId() == null) {
            goods.setCode("G-" + RandomUtil.randomNumbers(8));
            goods.setUserid(currentUser.getId());
        }
        System.out.println(goods);
        goodsService.saveOrUpdate(goods);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        goodsService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        goodsService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll(@RequestParam(required = false) Boolean status) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(status != null, "status", status);
        return Result.success(goodsService.list(queryWrapper));
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        Integer userid = TokenUtils.getCurrentUser().getId();
        Goods goods = goodsService.getById(id);
        List<Praise> praises = praiseService.list();
        List<Collect> collects = collectService.list();
        Praise praise = praiseService.getOne(new QueryWrapper<Praise>().eq("userid", userid).eq("goodsid", id));
        Collect collect = collectService.getOne(new QueryWrapper<Collect>().eq("userid", userid).eq("goodsid", id));
        goods.setHasPraise(praise != null);
        goods.setHasCollect(collect != null);
        goods.setPraiseCount(praises.stream().filter(p -> p.getGoodsid().equals(goods.getId())).count());
        goods.setCollectCount(collects.stream().filter(c -> c.getGoodsid().equals(goods.getId())).count());
        return Result.success(goods);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") String category,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        if (!"".equals(category)) {
            queryWrapper.eq("category", category);
        }
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser.getRole().equals("ROLE_USER")) {
            queryWrapper.eq("status", 1);
        }
        List<Praise> praises = praiseService.list();
        List<Collect> collects = collectService.list();
        Page<Goods> page = goodsService.page(new Page<>(pageNum, pageSize), queryWrapper);
        for (Goods goods : page.getRecords()) {
            goods.setPraiseCount(praises.stream().filter(praise -> praise.getGoodsid().equals(goods.getId())).count());
            goods.setCollectCount(collects.stream().filter(c -> c.getGoodsid().equals(goods.getId())).count());
        }
        return Result.success(page);
    }

    @GetMapping("/pageEnd")
    public Result pageEnd(@RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") String category,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        if (!"".equals(category)) {
            queryWrapper.eq("category", category);
        }
        List<Praise> praises = praiseService.list();
        List<Collect> collects = collectService.list();
        Page<Goods> page = goodsService.page(new Page<>(pageNum, pageSize), queryWrapper);
        for (Goods goods : page.getRecords()) {
            goods.setPraiseCount(praises.stream().filter(praise -> praise.getGoodsid().equals(goods.getId())).count());
            goods.setCollectCount(collects.stream().filter(c -> c.getGoodsid().equals(goods.getId())).count());
        }
        return Result.success(page);
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Goods> list = goodsService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Goods信息表", "UTF-8");
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
        List<Goods> list = reader.readAll(Goods.class);

        goodsService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

