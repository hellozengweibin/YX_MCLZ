package com.eshore.domain.model.vo.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName TreeVo
 * @Description
 * @Author jianlin.liu
 * @Date 2023/5/16
 * @Version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreeVo<T> {
    private Object id;

    private String label;

    private Object parentId;

    private T data;

    private Object extra;

    private Integer count = 0;

    private List<TreeVo<T>> children;
}
