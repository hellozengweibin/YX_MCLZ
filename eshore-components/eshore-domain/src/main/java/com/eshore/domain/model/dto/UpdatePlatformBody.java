package com.eshore.domain.model.dto;

import com.eshore.domain.annotation.EnumValue;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName UpdatePlatformBody
 * @Description 平台类型修改请求体
 * @Author jianlin.liu
 * @Date 2023/7/1
 * @Version 1.0
 **/
@Data
public class UpdatePlatformBody {

    @NotNull(message = "id不能为空")
    private Long id;

    @EnumValue(intValues = {1, 2, 3, 4, 5})
    private Integer platformType;

    private List<Integer> platformTypeList;
}
