package org.ifinalframework.plugins.aio.mybatis;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

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

    int select();

    int selectSingle(JavaQuery query);
}
