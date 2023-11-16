package com.eshore.domain.model.vo.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName CallBackResult
 * @Description
 * @Author jianlin.liu
 * @Date 2023/5/12
 * @Version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CallBackResult<T> {

    private boolean success;

    private T data;

    private String errMessage;
}
