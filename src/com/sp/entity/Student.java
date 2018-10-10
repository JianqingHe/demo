package com.sp.entity;

import com.sp.support.Validate;

/**
 * 学生信息
 *
 * @author hejq
 * @date 2018-07-28 11:49
 */
public class Student {

    /**
     * 姓名
     */
    @Validate(isNotNull = false)
    private String name;

    /**
     * 年龄
     */
    @Validate(max = 30, min = 10)
    private Integer age;

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
