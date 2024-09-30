package com.example.mysql.provider.stater.mp.model;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.commons.core.model.BaseConvert;
import com.example.mysql.provider.stater.mp.service.impl.BaseServiceImpl;

/**
 * <p>
 * 业务DAO类 继承于MyBatis-plus
 * <p>
 *
 * @author : 21
 * @see com.example.mysql.provider.stater.mp.service.impl.BaseServiceImpl
 * @since :2024/9/30 16:53
 */

public class BaseDAO<M extends BaseMapper<T>, T extends BaseConvert> extends BaseServiceImpl<M, T> {

}
