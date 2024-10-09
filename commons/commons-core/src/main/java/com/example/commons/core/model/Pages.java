package com.example.commons.core.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 统一分页模型
 * </p>
 *
 * @author : 21
 * @since : 2024/10/8 10:43
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Pages<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页
     */
    private Integer current;
    /**
     * 每页显示条数
     */
    private Integer size;
    /**
     * 查询数据列表
     */
    private List<T> records;
    /**
     * 总页数
     */
    private Integer pages;
    /**
     * 总数
     */
    private Integer total;

    public Pages(Integer current, Integer size, List<T> records, Integer pages, Integer total) {
        this.current = current;
        this.size = size;
        this.records = records;
        this.pages = pages;
        this.total = total;
    }

    public Pages(Integer current, Integer size, List<T> records) {
        this.current = current;
        this.size = size;
        this.records = records;
    }

    /**
     * 分页转换接口
     *
     * @param <S> 源分页模型
     * @param <T> 返回结果类型
     */
    public interface PageConverter<S, T> {

        Pages<T> convert(S sourcePage);

    }

}
