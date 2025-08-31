package org.ifinalframework.plugins.aio.mybatis;


public class JavaModel {
    private Long id;
    /**
     * @column name1
     */
    private String name;
    /**
     * @typeHandler org.ifinalframework.plugins.aio.mybatis.MyTypeHandler
     */
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
