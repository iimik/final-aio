package org.ifinalframework.plugins.aio.mybatis;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

/**
 * JavaMapper
 * @author iimik
 * @since 0.0.4
 **/
public interface JavaMapper {
    int insert();

    default int defaultInsert() {
        return insert();
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
