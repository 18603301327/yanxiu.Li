package com.example.springboot.service;

import com.example.springboot.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.Posts;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 *
 */
public interface IGoodsService extends IService<Goods> {

    List<Goods> getRecommend();

}
