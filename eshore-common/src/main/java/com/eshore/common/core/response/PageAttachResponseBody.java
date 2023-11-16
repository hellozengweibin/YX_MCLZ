package com.eshore.common.core.response;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author jianlin.liu
 *
 *  * 分页信息数据体
 */
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageAttachResponseBody<T> extends PageResponseBody<T> {

    private static final long serialVersionUID = 1L;

    /**
     * 附加数据
     */
    private Object attach;


    public PageAttachResponseBody<T> setPageInfo(PageInfo<T> pageInfo, Object attach) {
        super.setPageInfo(pageInfo);
        this.attach = attach;
        return this;
    }

}