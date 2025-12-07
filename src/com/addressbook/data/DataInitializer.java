package com.addressbook.data;

import com.addressbook.entity.Student;
import com.addressbook.service.Classroom;
import java.util.Arrays;
import java.util.List;

public final class DataInitializer {
    private DataInitializer() {
    }

    public static List<Classroom> initializeClasses() {
        Classroom software2301 = new Classroom(
                "软件2301", "刘伟", "软件工程",
                Arrays.asList(
                        new Student("230101", "王子浩", "13812345670", "wangzh@univ.edu", "523456789", "南苑3-201", "男", 20),
                        new Student("230102", "陈可盈", "13987654321", "chenky@univ.edu", "623456789", "南苑3-202", "女", 19),
                        new Student("230103", "刘晨曦", "13711223344", "liucx@univ.edu", "723456789", "南苑3-203", "女", 20),
                        new Student("230104", "郑宇航", "13655667788", "zhengyh@univ.edu", "823456789", "南苑3-204", "男", 21),
                        new Student("230105", "黄雅文", "13566778899", "huangyw@univ.edu", "923456789", "南苑3-205", "女", 20)
                )
        );

        Classroom software2302 = new Classroom(
                "软件2302", "张敏", "软件工程",
                Arrays.asList(
                        new Student("230201", "李浩然", "18812349876", "lihr@univ.edu", "512345678", "北苑5-101", "男", 20),
                        new Student("230202", "周思源", "18722334455", "zhousy@univ.edu", "612345678", "北苑5-102", "男", 21),
                        new Student("230203", "林婉清", "18633445566", "linwq@univ.edu", "712345678", "北苑5-103", "女", 19),
                        new Student("230204", "赵一凡", "18544556677", "zhaoyf@univ.edu", "812345678", "北苑5-104", "男", 20),
                        new Student("230205", "孙诗雨", "18455667788", "sunshy@univ.edu", "912345678", "北苑5-105", "女", 20)
                )
        );

        return Arrays.asList(software2301, software2302);
    }
}
