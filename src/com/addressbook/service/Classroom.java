package com.addressbook.service;

import com.addressbook.entity.Student;
import com.addressbook.interfaces.SortStrategy;
import com.addressbook.interfaces.StudentOperations;
import com.addressbook.strategy.StudentIdSortStrategy;
import com.addressbook.strategy.StudentNameSortStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Classroom implements StudentOperations {
    private final String className;
    private final String headTeacher;
    private final String major;
    private final List<Student> students;

    public Classroom(String className, String headTeacher, String major, List<Student> students) {
        this.className = className;
        this.headTeacher = headTeacher;
        this.major = major;
        this.students = new ArrayList<>(students);
    }

    public String getClassName() {
        return className;
    }

    public String getHeadTeacher() {
        return headTeacher;
    }

    public String getMajor() {
        return major;
    }

    public int getStudentCount() {
        return students.size();
    }

    @Override
    public boolean addStudent(Student student) {
        return students.add(student);
    }

    @Override
    public boolean deleteStudent(String studentId) {
        return students.removeIf(student -> student.getId().equals(studentId));
    }

    @Override
    public boolean updateStudent(String studentId, Student updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(studentId)) {
                Student existing = students.get(i);
                updatedStudent.setId(existing.getId());
                students.set(i, updatedStudent);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Student> searchById(String studentId) {
        return students.stream()
                .filter(student -> student.getId().equals(studentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> searchByName(String nameKeyword) {
        return students.stream()
                .filter(student -> student.getName().contains(nameKeyword))
                .collect(Collectors.toList());
    }

    @Override
    public void sortById() {
        applySortStrategy(new StudentIdSortStrategy());
    }

    @Override
    public void sortByName() {
        applySortStrategy(new StudentNameSortStrategy());
    }

    private void applySortStrategy(SortStrategy strategy) {
        strategy.sort(students);
    }

    @Override
    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(students);
    }

    @Override
    public String toString() {
        return String.format("班级: %s | 班主任: %s | 专业: %s | 学生人数: %d", className, headTeacher, major, students.size());
    }
}
