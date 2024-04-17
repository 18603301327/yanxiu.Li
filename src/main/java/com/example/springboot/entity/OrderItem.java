package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.hutool.core.annotation.Alias;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 *
 */
@Getter
@Setter
@TableName("order_item")
@ApiModel(value = "OrderItem对象", description = "")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("订单id")
    @Alias("订单id")
    private Integer orderId;

    @ApiModelProperty("门票id")
    @Alias("门票id")
    private Integer goodsId;

    @ApiModelProperty("门票数量")
    @Alias("门票数量")
    private Integer num;

    @ApiModelProperty("门票单价")
    @Alias("门票单价")
    private BigDecimal price;

    @ApiModelProperty("门票图片")
    @Alias("门票图片")
    private String img;
    private String goodsName;
    private String comment;
    private Double scores;
    private String category;
    private String selectDate;

}
