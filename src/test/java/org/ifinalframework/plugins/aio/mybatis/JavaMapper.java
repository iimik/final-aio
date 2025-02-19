package org.ifinalframework.plugins.aio.mybatis;


/**
 * JavaMapper
 *
 * @author iimik
 * @since 0.0.4
 **/
public interface JavaMapper {
    int insert();

    default int defaultInsert() {
        return insert();
    }

    int update();

    int delete();

    int select();
}
