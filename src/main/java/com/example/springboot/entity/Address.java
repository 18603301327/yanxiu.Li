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
@ApiModel(value = "Address对象", description = "")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("地址")
    @Alias("地址")
    private String content;

    @ApiModelProperty("电话")
    @Alias("电话")
    private String phone;

    @ApiModelProperty("用户名称")
    @Alias("用户名称")
    private String user;

    @ApiModelProperty("用户id")
    @Alias("用户id")
    private Integer userid;


}
