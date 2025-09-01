package org.ifinalframework.plugins.aio.mybatis;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * JavaMapper
 * @author iimik
 * @since 0.0.4
 **/
public interface JavaMapper {
    int insert(JavaModel model);

    int insertList(List<JavaModel> list);

    default int defaultInsert() {
        return insert(null);
    }

    @Insert("")
    int insertWithInsert();

    @InsertProvider
    int insertWithProvider();

    int update();

    int delete();

    int deleteById(@Param("id") Integer id);

    int select();

    JavaModel selectById(@Param("id") Long id);

    List<JavaModel> selectByIds(@Param("ids") List<Long> ids);

    JavaModel selectByIdAndName(@Param("id") Long id, @Param("name") String name, @Param("query") JavaQuery query);

    JavaModel selectSingle(JavaQuery query);
}
