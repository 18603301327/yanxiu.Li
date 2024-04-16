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
 *
 */
@Getter
@Setter
@ApiModel(value = "Lun对象", description = "")
public class Lun implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("图片")
    @Alias("图片")
    private String img;

    @ApiModelProperty("门票链接")
    @Alias("门票链接")
    private String link;

    @ApiModelProperty("门票id")
    @Alias("门票id")
    private Integer goodsId;

    @ApiModelProperty("门票名称")
    @Alias("门票名称")
    private String goodsName;


}
