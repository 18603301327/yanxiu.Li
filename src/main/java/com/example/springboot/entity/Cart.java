package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value = "Cart对象", description = "")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("门票id")
    @Alias("门票id")
    private Integer goodsId;

    @ApiModelProperty("门票数量")
    @Alias("门票数量")
    private Integer num;

    @ApiModelProperty("用户id")
    @Alias("用户id")
    private Integer userid;
    @TableField(exist = false)
    private String selectDate;
    @TableField(exist = false)
    private String goodName;
    @TableField(exist = false)
    private String img;
    @TableField(exist = false)
    private BigDecimal price;


}
