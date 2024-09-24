package com.example.commons.core.utils.config.modelmapper.json;

import org.modelmapper.ModelMapper;
import org.modelmapper.Module;

/**
 * <p>
 * fastJson实体转换
 * <P>这里主要是不去操作源对象</P>
 * <p>
 *
 * @author : 21
 * @since : 2024/9/24 15:28
 */
public class FastJsonModule implements Module {

    @Override
    public void setupModule(ModelMapper modelMapper) {
        modelMapper.getConfiguration().getConverters().add(0, new JSONObjectToJSONObjectConverter());
        modelMapper.getConfiguration().getConverters().add(0, new JSONArrayToJSONArrayConverter());
    }
}
