package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(value = "Posts对象", description = "")
public class Posts implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("标题")
    @Alias("标题")
    private String name;

    @ApiModelProperty("内容")
    @Alias("内容")
    private String content;

    @ApiModelProperty("封面")
    @Alias("封面")
    private String img;

    @ApiModelProperty("发布时间")
    @Alias("发布时间")
    private String time;

    @ApiModelProperty("发布人")
    @Alias("发布人")
    private String user;

    @ApiModelProperty("发布人id")
    @Alias("发布人id")
    private Integer userid;

    @ApiModelProperty("简介")
    @Alias("简介")
    private String descr;


    @TableField(exist = false)
    private Boolean hasCollect;

    @TableField(exist = false)
    private Long collectCount;
}
