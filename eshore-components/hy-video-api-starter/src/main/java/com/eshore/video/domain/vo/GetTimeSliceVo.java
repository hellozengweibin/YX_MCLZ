package com.eshore.video.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author shanglangxin
 * @since 2023/1/17 9:25
 */
@Data
public class GetTimeSliceVo {

    private List<String[]> timeBar;

    private Integer storageType;

}
