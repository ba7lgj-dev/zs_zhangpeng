package com.addressbook.entity;

public class Student extends Person {
    private String qq;
    private String dormAddress;
    private String gender;
    private int age;

    public Student(String id, String name, String phone, String email, String qq, String dormAddress, String gender, int age) {
        super(id, name, phone, email);
        this.qq = qq;
        this.dormAddress = dormAddress;
        this.gender = gender;
        this.age = age;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getDormAddress() {
        return dormAddress;
    }

    public void setDormAddress(String dormAddress) {
        this.dormAddress = dormAddress;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void showInfo() {
        System.out.printf("学号:%s 姓名:%s 性别:%s 年龄:%d 手机:%s 邮箱:%s QQ:%s 宿舍:%s%n",
                getId(), getName(), gender, age, getPhone(), getEmail(), qq, dormAddress);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", qq='" + qq + '\'' +
                ", dormAddress='" + dormAddress + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                '}';
    }
}
