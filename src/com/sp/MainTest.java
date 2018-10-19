package com.sp;

import com.sp.entity.Student;
import com.sp.valid.StudentCheck;

/**
 * 测试
 *
 * @author hejq
 * @date 2018-07-28 11:57
 */
public class MainTest {

    public static void main(String[] args) {
        Student student = new Student();

        student.setName("liang");
        student.setAge(11);

        System.out.println(StudentCheck.check(student));
    }
}
