package com.example.provider.base.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.commons.core.model.BaseConvert;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@TableName("app")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class App extends BaseConvert {

    private static final long serialVersionUID = 1L;

    //--------------------------------------------------------------数据库字段常量--------------------------------------------------------------
    public static final String ID = "id";
    public static final String NAME = "name";

//--------------------------------------------------------------字段--------------------------------------------------------------

    protected Long id;

    @TableField(value = NAME, keepGlobalFormat = true)
    private String name;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
