package com.danbro.plugins;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Danrbo
 * @Classname Page
 * @Description TODO
 * @Date 2021/5/11 13:06
 */
@Data
@Accessors(chain = true)
public class Page {
    /**
     * 数据总数
     */
    private Integer total;
    /**
     * 页码 从1开始
     */
    private Integer index;
    /**
     * 每页大小
     */
    private Integer size;

    public Integer getOffset() {
        return size * (index - 1);
    }
}
