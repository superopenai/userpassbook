package me.superning.userpassbook.myMapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author superning
 * @Classname MaMapper
 * @Description TODO
 * @Date 2020/2/8 14:49
 * @Created by superning
 */
public interface MyMapper<T> extends MySqlMapper<T>, Mapper<T> {
}
