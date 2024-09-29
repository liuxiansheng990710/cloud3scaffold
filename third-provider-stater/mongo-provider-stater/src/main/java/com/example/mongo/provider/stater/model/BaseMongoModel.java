package com.example.mongo.provider.stater.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.example.mongo.provider.stater.config.MongoCompositeKeyFillCallback;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * mongo基础Model类
 * </p>
 * <p>
 * 需要将ID（用于替换默认的_id）类型传入 默认为Long
 * </P>
 *
 * @author : 21
 * @since : 2024/9/29 11:24
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseMongoModel<ID> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自定义id，如果使用此id，
     * 若手动填充该值：则保存方法会视为update类型，且与自定义创建时间、创建人冲突（无法自动填充）
     * 若不填充该值：会默认填充19位雪花ID，且不影响自定义创建时间与创建人
     * {@link MongoCompositeKeyFillCallback#onBeforeConvert}
     */
    @Id
    protected ID id;

    public ID getId() {
        return id;
    }

    /**
     * 创建人
     * 注意：这里如果手动填充id的话，会导致创建人失效
     */
    @CreatedBy
    public String createBy;

    /**
     * 修改人
     */
    @LastModifiedBy
    public String updateBy;

    /**
     * 创建时间
     * 注意：这里如果手动填充id的话，会导致创建时间失效
     */
    @CreatedDate
    public Date createTime;

    /**
     * 修改时间
     */
    @LastModifiedDate
    public Date updateTime;
}
