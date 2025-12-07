package com.addressbook.app;

import com.addressbook.data.DataStore;
import com.addressbook.entity.Student;
import com.addressbook.service.BasicGlobalSearchService;
import com.addressbook.service.ClassManager;
import com.addressbook.service.Classroom;
import com.addressbook.service.GlobalSearchResult;
import com.addressbook.service.GlobalSearchService;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class StudentConsoleApp {
    private final ClassManager classManager;
    private final GlobalSearchService searchService;
    private final Scanner scanner = new Scanner(System.in);

    public StudentConsoleApp(ClassManager classManager) {
        this.classManager = classManager;
        this.searchService = new BasicGlobalSearchService(classManager);
    }

    public static void main(String[] args) {
        StudentConsoleApp app = new StudentConsoleApp(DataStore.getClassManager());
        app.run();
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            printMenu();
            int choice = readInt("请选择功能：");
            switch (choice) {
                case 1 -> viewAllClasses();
                case 2 -> viewStudents();
                case 3 -> sortStudentsById();
                case 4 -> sortStudentsByName();
                case 5 -> addStudent();
                case 6 -> deleteStudent();
                case 7 -> updateStudent();
                case 8 -> searchStudent();
                case 9 -> addClass();
                case 10 -> updateClassInfo();
                case 11 -> globalSearch();
                case 12 -> exit = true;
                default -> System.out.println("无效选项，请重新输入。");
            }
        }
        System.out.println("系统已退出，感谢使用！");
    }

    private void printMenu() {
        System.out.println("\n===== 班级通讯录管理系统 =====");
        System.out.println("1. 查看所有班级");
        System.out.println("2. 选择班级查看学生");
        System.out.println("3. 按学号排序并输出");
        System.out.println("4. 按姓名排序并输出");
        System.out.println("5. 添加学生");
        System.out.println("6. 删除学生");
        System.out.println("7. 修改学生");
        System.out.println("8. 搜索学生");
        System.out.println("9. 新增班级");
        System.out.println("10. 修改班级信息");
        System.out.println("11. 全局搜索");
        System.out.println("12. 退出系统");
    }

    private void viewAllClasses() {
        classManager.getAllClasses().forEach(System.out::println);
    }

    private Classroom selectClassroom() {
        System.out.print("请输入班级编号或名称：");
        String className = scanner.nextLine();
        Classroom classroom = classManager.getClassById(className);
        if (classroom == null) {
            classroom = classManager.getClassByName(className);
        }
        if (classroom == null) {
            System.out.println("未找到该班级，请确认输入。");
        }
        return classroom;
    }

    private void viewStudents() {
        Classroom classroom = selectClassroom();
        if (classroom != null) {
            classroom.getAllStudents().forEach(Student::showInfo);
        }
    }

    private void sortStudentsById() {
        Classroom classroom = selectClassroom();
        if (classroom != null) {
            classroom.sortById();
            classroom.getAllStudents().forEach(Student::showInfo);
        }
    }

    private void sortStudentsByName() {
        Classroom classroom = selectClassroom();
        if (classroom != null) {
            classroom.sortByName();
            classroom.getAllStudents().forEach(Student::showInfo);
        }
    }

    private void addStudent() {
        Classroom classroom = selectClassroom();
        if (classroom == null) {
            return;
        }

        System.out.print("输入学号：");
        String id = scanner.nextLine();
        System.out.print("输入姓名：");
        String name = scanner.nextLine();
        System.out.print("输入手机号：");
        String phone = scanner.nextLine();
        System.out.print("输入邮箱：");
        String email = scanner.nextLine();
        System.out.print("输入QQ：");
        String qq = scanner.nextLine();
        System.out.print("输入宿舍：");
        String dorm = scanner.nextLine();
        System.out.print("输入性别：");
        String gender = scanner.nextLine();
        int age = readInt("输入年龄：");

        Student student = new Student(id, name, phone, email, qq, dorm, gender, age);
        if (classManager.addStudentToClass(classroom.getClassId(), student)) {
            System.out.println("学生添加成功！");
        } else {
            System.out.println("添加学生失败。");
        }
    }

    private void deleteStudent() {
        Classroom classroom = selectClassroom();
        if (classroom == null) {
            return;
        }
        System.out.print("输入要删除的学号：");
        String id = scanner.nextLine();
        boolean removed = classManager.deleteStudentFromClass(classroom.getClassId(), id);
        if (removed) {
            System.out.println("删除成功！");
        } else {
            System.out.println("未找到该学号的学生。");
        }
    }

    private void updateStudent() {
        Classroom classroom = selectClassroom();
        if (classroom == null) {
            return;
        }
        System.out.print("输入要修改的学号：");
        String id = scanner.nextLine();
        List<Student> result = classroom.searchById(id);
        if (result.isEmpty()) {
            System.out.println("未找到该学号的学生。");
            return;
        }
        Student existing = result.get(0);
        System.out.println("当前信息：");
        existing.showInfo();

        System.out.print("输入新姓名(直接回车表示不修改)：");
        String name = scanner.nextLine();
        if (name.isBlank()) {
            name = existing.getName();
        }
        System.out.print("输入新手机号(直接回车表示不修改)：");
        String phone = scanner.nextLine();
        if (phone.isBlank()) {
            phone = existing.getPhone();
        }
        System.out.print("输入新邮箱(直接回车表示不修改)：");
        String email = scanner.nextLine();
        if (email.isBlank()) {
            email = existing.getEmail();
        }
        System.out.print("输入新QQ(直接回车表示不修改)：");
        String qq = scanner.nextLine();
        if (qq.isBlank()) {
            qq = existing.getQq();
        }
        System.out.print("输入新宿舍(直接回车表示不修改)：");
        String dorm = scanner.nextLine();
        if (dorm.isBlank()) {
            dorm = existing.getDormAddress();
        }
        System.out.print("输入新性别(直接回车表示不修改)：");
        String gender = scanner.nextLine();
        if (gender.isBlank()) {
            gender = existing.getGender();
        }
        System.out.print("输入新年龄(直接回车表示不修改)：");
        String ageInput = scanner.nextLine();
        int age = existing.getAge();
        if (!ageInput.isBlank()) {
            try {
                age = Integer.parseInt(ageInput);
            } catch (NumberFormatException e) {
                System.out.println("年龄输入无效，保持原值。");
            }
        }

        Student updated = new Student(existing.getId(), name, phone, email, qq, dorm, gender, age);
        if (classManager.updateStudentInClass(classroom.getClassId(), id, updated)) {
            System.out.println("修改完成！");
        } else {
            System.out.println("修改失败，未找到学生。");
        }
    }

    private void searchStudent() {
        Classroom classroom = selectClassroom();
        if (classroom == null) {
            return;
        }
        System.out.println("选择搜索方式：1.按学号 2.按姓名");
        int type = readInt("请输入选项：");
        if (type == 1) {
            System.out.print("输入学号：");
            String id = scanner.nextLine();
            classroom.searchById(id).forEach(Student::showInfo);
        } else if (type == 2) {
            System.out.print("输入姓名关键词：");
            String keyword = scanner.nextLine();
            classroom.searchByName(keyword).forEach(Student::showInfo);
        } else {
            System.out.println("无效选项。");
        }
    }

    private void addClass() {
        System.out.print("输入班级编号：");
        String classId = scanner.nextLine();
        System.out.print("输入班级名称：");
        String className = scanner.nextLine();
        System.out.print("输入专业：");
        String major = scanner.nextLine();
        System.out.print("输入班主任：");
        String headTeacher = scanner.nextLine();

        Classroom classroom = new Classroom(classId, className, headTeacher, major, List.of());
        if (classManager.addClass(classroom)) {
            System.out.println("班级新增成功！");
        } else {
            System.out.println("新增班级失败。");
        }
    }

    private void updateClassInfo() {
        Classroom classroom = selectClassroom();
        if (classroom == null) {
            return;
        }
        System.out.print("输入新班级名称(回车跳过)：");
        String name = scanner.nextLine();
        System.out.print("输入新班主任(回车跳过)：");
        String headTeacher = scanner.nextLine();
        System.out.print("输入新专业(回车跳过)：");
        String major = scanner.nextLine();
        if (classManager.updateClass(classroom.getClassId(), name, headTeacher, major)) {
            System.out.println("班级信息更新完成！");
        } else {
            System.out.println("更新失败。");
        }
    }

    private void globalSearch() {
        System.out.print("输入搜索关键字：");
        String keyword = scanner.nextLine();
        GlobalSearchResult result = searchService.search(keyword);
        if (result.isEmpty()) {
            System.out.printf("未找到任何与关键字“%s”相关的信息。%n", keyword);
            return;
        }
        result.getClassMatches().forEach(match -> System.out.println(match.getDescription()));
        result.getStudentMatches().forEach(match -> System.out.println(match.getDescription()));
    }

    private int readInt(String tip) {
        while (true) {
            try {
                System.out.print(tip);
                String input = scanner.nextLine();
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("输入的数字无效，请重新输入。");
            }
        }
    }
}
