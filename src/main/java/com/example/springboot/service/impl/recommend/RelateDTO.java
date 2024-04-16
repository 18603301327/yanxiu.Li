package com.example.springboot.service.impl.recommend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RelateDTO {
    /** 用户id */
    private Integer useId;
    /** 电影id */
    private Integer itemId;
    /** 指数 最小为1 最大为5 */
    private Integer index;
}