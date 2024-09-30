package com.example.mysql.provider.stater.mp.model;

import java.io.Serial;

import com.example.commons.core.model.BaseConvert;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 自增主键父类
 * </p>
 *
 * @author : 21
 * @since : 2024/9/30 16:58
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseModel extends BaseConvert {

    @Serial
    private static final long serialVersionUID = -4333029898887754221L;

    public static final String ID = "id";

    public BaseModel(Long id) {
        this.id = id;
    }

    protected Long id;

}
