package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.entity.*;
import com.example.springboot.mapper.GoodsMapper;
import com.example.springboot.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.service.impl.recommend.RelateDTO;
import com.example.springboot.service.impl.recommend.UserCF;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 *
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICollectService collectService;

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IPraiseService praiseService;
    @Override
    public List<Goods> getRecommend() {
        User currentUser = TokenUtils.getCurrentUser();
        List<User> userList = userService.list(new QueryWrapper<User>().eq("role", "ROLE_USER"));

        List<RelateDTO> relateDTOList = new ArrayList<>();
        List<Goods> goodsList = list(new QueryWrapper<Goods>().orderByDesc("id"));  // 筛选电影
        for (Goods goods : goodsList) {
            for (User user : userList) {
                int index = 1;

                List<Praise> collectList = praiseService.list(new QueryWrapper<Praise>().eq("userid", user.getId()).eq("goodsid", goods.getId()));
                if (collectList.size() > 0) {
                    index += 2;  // 收藏过  分数+2
                }
                if (index > 1) {
                    RelateDTO relateDTO = new RelateDTO(user.getId(), goods.getId(), index);
                    relateDTOList.add(relateDTO);
                }
            }
        }
        List<Integer> recommendFilms = UserCF.recommend(currentUser.getId(), relateDTOList);
        return recommendFilms.stream().map(goodsId -> goodsList.stream().filter(goods -> goods.getId().equals(goodsId)).findFirst().orElse(null)).limit(6).collect(Collectors.toList());
    }

}
