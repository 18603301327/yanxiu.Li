package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
 * @since 
 */
@Getter
@Setter
@ApiModel(value = "Collect对象", description = "")
public class Collect implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("门票名称")
    @Alias("门票名称")
    private String name;

    @ApiModelProperty("门票id")
    @Alias("门票id")
    private Integer goodsid;

    @ApiModelProperty("图片")
    @Alias("图片")
    private String img;

    @ApiModelProperty("用户id")
    @Alias("用户id")
    private Integer userid;

    private Integer typeId;

}
